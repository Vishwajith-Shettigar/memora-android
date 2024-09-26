package com.example.util

sealed class Response<T> {
  data class Success<T>(val data: T? = null) : Response<T>()
  data class Error<T>(val message: String, val data: T? = null) : Response<T>()
}
