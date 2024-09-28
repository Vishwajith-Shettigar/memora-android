package com.example.di

import com.example.data.repository.AuthRepository
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.UserRepository
import com.example.domain.usecase.SaveUserDetailsUseCase
import com.example.domain.usecase.SignInUseCase
import com.example.domain.usecase.SignUpUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

  @Provides
  @ViewModelScoped
  fun provideSignUpUseCase(authRepository: AuthRepository,userRepository: UserRepository): SignUpUseCase {
    return SignUpUseCase(authRepository,userRepository)
  }

  @Provides
  @ViewModelScoped
  fun provideSignInUseCase(authRepository: AuthRepository,userRepository: UserRepository): SignInUseCase {
    return SignInUseCase(authRepository,userRepository)
  }

  @Provides
  @ViewModelScoped
  fun provideSaveUserDetailsUseCase(userRepository: UserRepository): SaveUserDetailsUseCase {
    return SaveUserDetailsUseCase(userRepository)
  }
}
