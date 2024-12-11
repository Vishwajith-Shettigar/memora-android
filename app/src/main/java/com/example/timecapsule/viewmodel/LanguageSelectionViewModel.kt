package com.example.timecapsule.viewmodel

import androidx.lifecycle.ViewModel
import com.example.domain.usecase.ChooseLanguageUseCase
import com.google.rpc.Code
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class LanguageSelectionViewModel @Inject constructor(
  private val chooseLanguageUseCase: ChooseLanguageUseCase
) : ViewModel() {

  private val _selectedLanguageCode = MutableStateFlow("en")
  val selectedLanguageCode: StateFlow<String> = _selectedLanguageCode

  init {
    _selectedLanguageCode.value = chooseLanguageUseCase.getSelectedLanguageCode()
  }

  fun setSelectedLanguageCode(code: String) {
    _selectedLanguageCode.value = code
    chooseLanguageUseCase.setSelectedLanguageCode(code)
  }
}
