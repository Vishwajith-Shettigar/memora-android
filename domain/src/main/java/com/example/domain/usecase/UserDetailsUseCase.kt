package com.example.domain.usecase

import com.example.data.repository.UserRepository
import com.example.model.UserDetails
import com.example.util.Response
import javax.inject.Inject

class SaveUserDetailsUseCase @Inject constructor(
  private val userRepository: UserRepository
) {
  suspend operator fun invoke(userName: String, fName: String, lName: String): Response<Unit> {
    return userRepository.saveUserDetails(userName, fName, lName)
  }
}

class SearchUsersUseCase @Inject constructor(
  private val userRepository: UserRepository
) {
  suspend operator fun invoke(query: String): Response<List<UserDetails>> {
    return userRepository.searchUsers(query)
  }
}

