package com.example.timecapsule.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.usecase.SearchUsersUseCase
import com.example.domain.usecase.UploadFilesUseCase
import com.example.model.FileUploadProgress
import com.example.model.FileUploaded
import com.example.model.UserDetails
import com.example.timecapsule.routes.Screen
import com.example.util.Response
import com.example.util.bytesToMegabytes
import com.example.util.getFileSizeAndName
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.Exception
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class SearchPeopleState {
  object Idle : SearchPeopleState()
  data class Success(val data: List<UserDetails>) : SearchPeopleState()
  data class Error(val message: String, val exception: Exception? = null) : SearchPeopleState()
}

enum class ShareWithPeopleOption {
  DONT_SHARE,
  SHARE_ALL,
  SELECTED_PEOPLES
}

enum class LocationOption {
  SELECT_LOCATION,
  DONT_SELECT_LOCATION
}

sealed class StorageWarningState {
  object NoWarning : StorageWarningState()
  object Warning : StorageWarningState()
}

@HiltViewModel
class CapsuleCreationViewModel @Inject constructor(
  private val searchUsersUseCase: SearchUsersUseCase,
  private val uploadFilesUseCase: UploadFilesUseCase,
  @ApplicationContext private val context: Context
) : ViewModel() {

  private val capsuleSizeInMB: Double = 5.0
  private var contentSizeInMB: Double = 0.0

  private var totalFiles: Int = 0

  private val _searchPeopleState = MutableStateFlow<SearchPeopleState>(SearchPeopleState.Idle)
  val searchPeopleState: StateFlow<SearchPeopleState> = _searchPeopleState

  private val _storageWarningState =
    MutableStateFlow<StorageWarningState>(StorageWarningState.NoWarning)
  val storageWarningState: StateFlow<StorageWarningState> = _storageWarningState

  private val _fileProgrerssState =
    MutableStateFlow<List<FileUploadProgress>>(listOf())
  val fileProgrerssState: StateFlow<List<FileUploadProgress>> = _fileProgrerssState

  private val _fileUploadedState =
    MutableStateFlow<List<FileUploaded>>(mutableListOf())
  val fileUploadedState: StateFlow<List<FileUploaded>> = _fileUploadedState

  val selectedPeoples = mutableStateListOf<UserDetails>()

  val isSataliteView =
    mutableStateOf(false)

  var latLang =
    LatLng(
      1.3521,
      103.8198
    )

  fun getFileStatus() {
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        while (true) {
          if (totalFiles != fileUploadedState.value.size) {
            delay(100)
            _fileProgrerssState.value = uploadFilesUseCase.getUploadProgress().map {
              it.copy()
            }
            _fileUploadedState.value = uploadFilesUseCase.getUploadedFiles().map {
              it.copy()
            }
          }
        }
      }
    }
  }

  // Store timestamp
  var selectedTimeStamp: Timestamp? = null

  // Store share with people option
  var shareWithPeopleOption: ShareWithPeopleOption = ShareWithPeopleOption.DONT_SHARE

  // Store location option
  var selectedLocationOption: LocationOption = LocationOption.DONT_SELECT_LOCATION

  fun setTimeStamp(p0: Timestamp) {
    selectedTimeStamp = p0
  }

  fun setShareWithPeople(p0: ShareWithPeopleOption) {
    shareWithPeopleOption = p0
  }

  fun setLocationOption(p0: LocationOption) {
    selectedLocationOption = p0
  }

  fun searchUsers(query: String) {
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        val result = searchUsersUseCase(query)
        _searchPeopleState.value = when (result) {
          is Response.Success -> {
            SearchPeopleState.Success(result.data!!)
          }

          is Response.Error -> {
            SearchPeopleState.Error(
              exception = result.exception,
              message = result.exception.message!!
            )
          }
        }
      }
    }
  }

  fun uploadFiles(uri: Uri) {
    val size = getFileSizeAndName(uri, context).first
    val inMB = bytesToMegabytes(size)
    contentSizeInMB += inMB
    if (contentSizeInMB <= capsuleSizeInMB) {
      totalFiles++
      viewModelScope.launch {
        withContext(Dispatchers.IO) {
          uploadFilesUseCase.uploadFile(uri)
        }
      }
    } else {
      contentSizeInMB -= inMB
      _storageWarningState.value = StorageWarningState.Warning
    }
  }

  fun setStorageNoWaringState() {
    _storageWarningState.value = StorageWarningState.NoWarning
  }

  fun cancelFileUploading(uri: Uri) {
    val size = getFileSizeAndName(uri, context).first
    val inMB = bytesToMegabytes(size)
    contentSizeInMB -= inMB
    totalFiles--
    uploadFilesUseCase.cancelFileUploding(uri)
  }

  fun deleteUploadedFile(uri: Uri, fileUri: Uri) {
    totalFiles--
    val size = getFileSizeAndName(uri, context).first
    val inMB = bytesToMegabytes(size)
    contentSizeInMB -= inMB
    uploadFilesUseCase.deleteUploadedFile(uri)
  }

  override fun onCleared() {
    super.onCleared()
    uploadFilesUseCase.cancelAllFilesUploading()
  }
}
