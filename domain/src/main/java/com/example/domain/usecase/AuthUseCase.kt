package com.example.domain.usecase

import com.example.data.repository.AuthRepository
import com.example.data.repository.AuthRepositoryImpl
import com.example.model.UserDetails
import com.example.util.Response
import com.example.util.UnspecifiedException
import javax.inject.Inject

class SignInUseCase @Inject constructor(
  private val authRepository: AuthRepository,

  ) {
  suspend operator fun invoke(email: String, password: String): Response<Unit> {
    return authRepository.signIn(email, password)
  }
}

class SignUpUseCase @Inject constructor(
  private val authRepository: AuthRepository,
  private val saveUserDetailsUseCase: SaveUserDetailsUseCase
) {
  suspend operator fun invoke(
    userName: String,
    email: String,
    password: String,
  ): Response<Unit> {
    val signUpResult = authRepository.signUp(email, password)

    return when (signUpResult) {
      is Response.Success -> {
        val userId = signUpResult.data
        if (userId.isNullOrBlank()) {
          Response.Error(UnspecifiedException())
        } else {
          val userDetails =
            UserDetails(userName = userName, userId = userId, email = email, password = password)
          val response = saveUserDetailsUseCase(userDetails.copy(userId = userId))
          if (response is Response.Error) {
            authRepository.deleteUser()
          }
          return response
        }
      }

      is Response.Error -> {
        Response.Error(signUpResult.exception)
      }
    }
  }
}
