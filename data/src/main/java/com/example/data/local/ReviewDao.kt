package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReviewDao {

  // Insert or replace the review (since there is only one review per user)
  @Insert
  suspend fun insertReview(review: Review)

  // Update the review if it already exists
  @Update
  suspend fun updateReview(review: Review)

  // Get the current review
  @Query("SELECT * FROM review LIMIT 1")
  suspend fun getReview(): Review?

  // Delete the review
  @Query("DELETE FROM review")
  suspend fun deleteReview()
}
