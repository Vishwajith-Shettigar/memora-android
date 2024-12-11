package com.example.domain.usecase

import com.example.data.local.entity.Review
import com.example.data.repository.ReviewRepository
import com.example.data.repository.UserRepository


class InsertReviewUseCase(
  private val reviewRepository: ReviewRepository,
  private val userRepository: UserRepository
) {
  suspend operator fun invoke(review: Review) {
    reviewRepository.insertOrUpdateReview(review)
    userRepository.insertOrUpdateUserReview(review)
  }
}
