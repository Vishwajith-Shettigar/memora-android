package com.example.timecapsule.ui.util

data class Language(
  val code: String,
  val name: String,
  val iconPath: String
)

val languageList = listOf(
  Language(code = "en", name = "English", iconPath = "flags/english_flag.png"),
)
