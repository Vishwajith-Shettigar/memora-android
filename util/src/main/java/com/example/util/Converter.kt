package com.example.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
  private val gson = Gson()

  @TypeConverter
  fun fromListToString(list: List<String>): String {
    return gson.toJson(list)
  }

  @TypeConverter
  fun fromStringToList(value: String): List<String> {
    val type = object : TypeToken<List<String>>() {}.type
    return gson.fromJson(value, type)
  }
}
