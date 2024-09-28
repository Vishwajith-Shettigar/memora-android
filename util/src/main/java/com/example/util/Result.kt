package com.example.util

sealed class Response<out T> {
  data class Success<T>(val data: T? = null) : Response<T>()
  data class Error<T>(val exception: Exception, val data: T? = null) : Response<T>()
}
