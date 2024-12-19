package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.util.Converters

@Entity(tableName = "update_details")
@TypeConverters(Converters::class)
data class UpdateDetails(
  @PrimaryKey val id: Long = 1,
  @ColumnInfo(name = "version_code") val versionCode: Int,
  @ColumnInfo(name = "version_name") val versionName: String,
  @ColumnInfo(name = "details") val details: List<String>
)
