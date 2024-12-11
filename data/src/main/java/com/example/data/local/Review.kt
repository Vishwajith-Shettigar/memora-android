package com.example.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "review")
data class Review(
  @PrimaryKey val id: Long = 1,  // Single review per user, so id is 1
  @ColumnInfo(name = "rating") val rating: Double,     // Rating as Double
  @ColumnInfo(name = "review_text") val reviewText: String,  // Review text as String
  @ColumnInfo(name = "timestamp") val timestamp: Long   // Timestamp stored as Long (epoch time)
)
