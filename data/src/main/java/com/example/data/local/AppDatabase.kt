package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.local.entity.Review
import com.example.data.local.entity.UpdateDetails
import com.example.util.Converters

@Database(entities = [Review::class, UpdateDetails::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun reviewDao(): ReviewDao
  abstract fun updateDetailsDao(): UpdateDetailsDao
}
