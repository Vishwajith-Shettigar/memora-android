package com.example.data.repository

import com.example.data.local.Review
import com.example.data.local.ReviewDao
import javax.inject.Inject

interface ReviewRepository {
  suspend fun insertOrUpdateReview(review: Review)
  suspend fun getReview(): Review?
  suspend fun deleteReview()
}


class ReviewRepositoryImpl @Inject constructor(private val reviewDao: ReviewDao) : ReviewRepository {

  // Insert or update the review
  override suspend fun insertOrUpdateReview(review: Review) {
    val existingReview = reviewDao.getReview()
    if (existingReview != null) {
      // Update existing review
      reviewDao.updateReview(review)
    } else {
      // Insert new review
      reviewDao.insertReview(review)
    }
  }

  // Get the current review
  override suspend fun getReview(): Review? {
    return reviewDao.getReview()
  }

  // Delete the review
  override suspend fun deleteReview() {
    reviewDao.deleteReview()
  }
}
