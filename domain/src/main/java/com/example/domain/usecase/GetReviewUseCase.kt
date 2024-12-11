package com.example.domain.usecase

import com.example.data.local.Review
import com.example.data.repository.ReviewRepository

class GetReviewUseCase(private val reviewRepository: ReviewRepository) {

  suspend operator fun invoke(): Review? {
    return reviewRepository.getReview()
  }
}
