package com.example.di

import com.example.data.repository.AuthRepository
import com.example.data.repository.AuthRepositoryImpl
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
  fun provideSignUpUseCase(authRepository: AuthRepository): SignUpUseCase {
    return SignUpUseCase(authRepository)
  }

  @Provides
  @ViewModelScoped
  fun provideSignInUseCase(authRepository: AuthRepository): SignInUseCase {
    return SignInUseCase(authRepository)
  }
}
