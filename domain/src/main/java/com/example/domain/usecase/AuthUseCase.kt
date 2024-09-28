package com.example.domain.usecase

import com.example.data.repository.AuthRepository
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.UserRepository
import com.example.model.UserDetails
import com.example.util.Response
import com.example.util.UnspecifiedException
import javax.inject.Inject

class SignInUseCase @Inject constructor(
  private val authRepository: AuthRepository,
  private val userRepository: UserRepository,
) {
  suspend operator fun invoke(email: String, password: String): Response<Any> {
    val result = authRepository.signIn(email, password)
    if (result is Response.Error)
      return result
    return userRepository.checkUserRecordExists((result as Response.Success).data!!)
  }
}

class SignUpUseCase @Inject constructor(
  private val authRepository: AuthRepository,
  private val userRepository: UserRepository
) {
  suspend operator fun invoke(
    userName: String,
    email: String,
    password: String,
  ): Response<Exception> {
//    val checkUserNameDoesntExist = userRepository.checkUserNameDoesntExists(userName = userName)
//    if (checkUserNameDoesntExist is Response.Error)
//      return checkUserNameDoesntExist
    return authRepository.signUp(email, password)
  }
}
