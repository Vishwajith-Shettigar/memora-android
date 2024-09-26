package com.example.domain.usecase

import com.example.data.repository.AuthRepository
import com.example.data.repository.AuthRepositoryImpl
import com.example.util.Response
import javax.inject.Inject

class SignInUseCase @Inject constructor(
  private val authRepository: AuthRepository
) {
  suspend operator fun invoke(email: String, password: String): Response<Unit> {
    return authRepository.signIn(email, password)
  }
}

class SignUpUseCase @Inject constructor(
  private val authRepository: AuthRepository
) {
  suspend operator fun invoke(email: String, password: String): Response<Unit> {
    return authRepository.signUp(email, password)
  }
}