package com.example.timecapsule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.Review
import com.example.domain.usecase.GetReviewUseCase
import com.example.domain.usecase.InsertReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class ReviewViewModel @Inject constructor(
  private val getReviewUseCase: GetReviewUseCase,
  private val insertReviewUseCase: InsertReviewUseCase
) : ViewModel() {

  var review: Review? = null

  init {
    getReview()
  }

  // Get the review from repository
  fun getReview() {
    viewModelScope.launch(Dispatchers.IO) {
      review = getReviewUseCase()
    }
  }

  // Insert or update the review
  fun insertReview(review: Review) {
    viewModelScope.launch {
      insertReviewUseCase(review)
    }
  }
}
