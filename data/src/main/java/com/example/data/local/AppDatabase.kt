package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Review::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract fun reviewDao(): ReviewDao
}
