package com.example.timecapsule.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.dto.NotificationDto
import com.example.domain.usecase.CreateCapsuleUseCase
import com.example.domain.usecase.GetCapsuleAssetsUseCase
import com.example.domain.usecase.GetUserDetailsUseCase
import com.example.domain.usecase.SearchUsersUseCase
import com.example.domain.usecase.SendCapsuleCreationNotificationUseCase
import com.example.domain.usecase.UploadFilesUseCase
import com.example.domain.usecase.getUserIDUseCase
import com.example.model.CapsuleAsset
import com.example.model.CapsuleDetails
import com.example.model.FileUploadProgress
import com.example.model.FileUploaded
import com.example.model.UserDetails
import com.example.timecapsule.routes.Screen
import com.example.util.Response
import com.example.util.bytesToMegabytes
import com.example.util.getFileSizeAndName
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.util.nextAlphanumericString
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.Exception
import kotlin.random.Random
import kotlinx.coroutines.CoroutineScope
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

sealed class CapsuleSelectionState {
  object Loading : CapsuleSelectionState()
  class Success(val data: List<CapsuleAsset>) : CapsuleSelectionState()
  data class Error(val message: String, val exception: Exception? = null) : CapsuleSelectionState()
}

sealed class CapsuleCreationState {
  object Loading : CapsuleCreationState()
  object Success : CapsuleCreationState()
  data class Error(val message: String, val exception: Exception? = null) : CapsuleCreationState()
}

object DefaultLocation {
  val latitude = 1.3521
  val longitude = 103.8198
}

@HiltViewModel
class CapsuleCreationViewModel @Inject constructor(
  private val searchUsersUseCase: SearchUsersUseCase,
  private val uploadFilesUseCase: UploadFilesUseCase,
  private val getCapsuleAssetsUseCase: GetCapsuleAssetsUseCase,
  private val getUserIDUseCase: getUserIDUseCase,
  private val createCapsuleUseCase: CreateCapsuleUseCase,
  private val getUsersDetailsUseCase: GetUserDetailsUseCase,
  private val sendCapsuleCreationNotificationUseCase: SendCapsuleCreationNotificationUseCase,
  @ApplicationContext private val context: Context
) : ViewModel() {

  var userId: String? = null

  lateinit var ownerUserDetails: UserDetails

  init {
    CoroutineScope(Dispatchers.IO).launch {
      val response = getUserIDUseCase.invoke()
      if (response is Response.Success)
        userId = response.data

      userId?.let {
        val userDetailsResponse = getUsersDetailsUseCase(it)
        if (userDetailsResponse is Response.Success) {
          ownerUserDetails = userDetailsResponse.data!!
          selectedPeoples.add(userDetailsResponse.data!!)
        }
      }
    }
  }


  val amount = 500

  private val CAPSULE_ID: String = Random.nextAlphanumericString(10)

  private val capsuleSizeInMB: Double = 5.0
  private var contentSizeInMB: Double = 0.0

  var selectedCapsuleModelId: String? = null
  var selectedCapsuleImageUrl: String? = null

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

  private val _capsuleSelectionState =
    MutableStateFlow<CapsuleSelectionState>(CapsuleSelectionState.Loading)
  val capsuleSelectionState: StateFlow<CapsuleSelectionState> = _capsuleSelectionState

  private val _capsuleCreationState =
    MutableStateFlow<CapsuleCreationState>(CapsuleCreationState.Loading)
  val capsuleCreationState: StateFlow<CapsuleCreationState> = _capsuleCreationState

  val selectedPeoples = mutableStateListOf<UserDetails>()

  var userLetterText by mutableStateOf("")

  var capsuleName by mutableStateOf("")
  var capsuleDescription by mutableStateOf("")

  val isSataliteView =
    mutableStateOf(false)

  var latLang: LatLng? = null


  private val _name = mutableStateOf("")
  val name: State<String> = _name

  private val _address = mutableStateOf("")
  val address: State<String> = _address

  private val _postalCode = mutableStateOf("")
  val postalCode: State<String> = _postalCode

  private val _city = mutableStateOf("")
  val city: State<String> = _city

  private val _state = mutableStateOf("")
  val state: State<String> = _state

  private val _country = mutableStateOf("")
  val country: State<String> = _country

  // Update functions to change state
  fun updateName(newName: String) {
    _name.value = newName
  }

  fun updateAddress(newAddress: String) {
    _address.value = newAddress
  }

  fun updatePostalCode(newPostalCode: String) {
    _postalCode.value = newPostalCode
  }

  fun updateCity(newCity: String) {
    _city.value = newCity
  }

  fun updateState(newState: String) {
    _state.value = newState
  }

  fun updateCountry(newCountry: String) {
    _country.value = newCountry
  }

  fun sendCapsuleCreationNotifications() {

    val selectedUserIds = selectedPeoples.filter {
      it.userId != ownerUserDetails.userId
    }.map {
      it.userId
    }

    if (selectedUserIds.size == 0)
      return

    val notificationDto = NotificationDto(
      userIds = selectedUserIds.toList(),
      title = "📢 New Capsule Alert! 🚀",
      body = "has shared new capsule with you.",
      capsuleId = CAPSULE_ID,
      username = ownerUserDetails.userName,
      userImageUrl = ownerUserDetails.imageUrl
    )

    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        sendCapsuleCreationNotificationUseCase(notificationDto)
      }
    }
  }

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
            val temp = result.data!!.filter {
              it.userId != userId
            }
            SearchPeopleState.Success(temp)
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
          uploadFilesUseCase.uploadFile(uri, CAPSULE_ID)
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
    val size = getFileSizeAndName(fileUri, context).first
    val inMB = bytesToMegabytes(size)
    contentSizeInMB -= inMB
    viewModelScope.launch {
      withContext(Dispatchers.IO)
      {
        uploadFilesUseCase.deleteUploadedFile(uri, capsuleId = CAPSULE_ID)
      }
    }
  }

  fun getCapsuleAssets() {
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        val response = getCapsuleAssetsUseCase()
        when (response) {
          is Response.Success -> {
            _capsuleSelectionState.value =
              CapsuleSelectionState.Success(response.data ?: emptyList())
          }

          is Response.Error -> {
            _capsuleSelectionState.value =
              CapsuleSelectionState.Error(response.exception.message ?: "", response.exception)
          }
        }
      }
    }
  }

  fun saveCapsule() {
    _capsuleCreationState.value = CapsuleCreationState.Loading

    viewModelScope.launch {
      try {
        withContext(Dispatchers.IO) {
          val location =
            if (latLang == null || selectedLocationOption == LocationOption.DONT_SELECT_LOCATION)
              null
            else
              GeoPoint(latLang!!.latitude, latLang!!.longitude)
          val users: MutableList<Map<String, Any>> = selectedPeoples.map { selectedPeople ->
            mapOf(
              "isOwner" to (selectedPeople.userId == userId),
              "userId" to selectedPeople.userId,
              "userName" to selectedPeople.userName,
              "imageUrl" to selectedPeople.imageUrl
            )
          }.toMutableList()

          val fileUrls: List<Map<String, String>> = _fileUploadedState.value.map { file ->
            mapOf(
              "url" to file.uri.toString(),
              "fileName" to file.fileName,
              "fileType" to file.fileType,
              "size" to file.totalSize.toString()
            )
          }

          val letter = if (userLetterText.length == 0)
            null
          else
            userLetterText

          val capsuleDetails = CapsuleDetails(
            id = CAPSULE_ID,
            modelId = selectedCapsuleModelId?.toIntOrNull() ?: 100,
            imageUrl = selectedCapsuleImageUrl!!,
            title = capsuleName,
            description = capsuleDescription,
            location = location,
            users = users.toList(),
            fileUrls = fileUrls,
            isDeleted = false,
            time = selectedTimeStamp!!,
            isOwner = false, // just placeholder
            ownerUserName = "",
            letter = letter
          )

          val response = createCapsuleUseCase(capsuleDetails)
          withContext(Dispatchers.Main)
          {
            when (response) {
              is Response.Success -> {
                sendCapsuleCreationNotifications()
                delay(2000)
                _capsuleCreationState.value = CapsuleCreationState.Success
              }

              is Response.Error -> {
                _capsuleCreationState.value =
                  CapsuleCreationState.Error("", response.exception)
              }
            }
          }
        }
      } catch (e: Exception) {
      }
    }
  }

  override fun onCleared() {
    super.onCleared()
    uploadFilesUseCase.cancelAllFilesUploading()
  }
}
