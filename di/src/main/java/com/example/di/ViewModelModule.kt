package com.example.di

import com.example.data.repository.AuthRepository
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.CapsulesRepository
import com.example.data.repository.CapsulesRepositoryImpl
import com.example.data.repository.NotificationRepository
import com.example.data.repository.UploadFileRepository
import com.example.data.repository.UserRepository
import com.example.data.sharedpreference.SharedPreferencesHelper
import com.example.domain.usecase.CreateCapsuleUseCase
import com.example.domain.usecase.GetCapsuleAssetsUseCase
import com.example.domain.usecase.GetCapsuleDetailsUseCase
import com.example.domain.usecase.GetCapsuleListUseCase
import com.example.domain.usecase.GetNotificationUseCase
import com.example.domain.usecase.GetUserDetailsUseCase
import com.example.domain.usecase.OnBoardingDataUseCase
import com.example.domain.usecase.OpenCapsuleScreenCheckPointUseCase
import com.example.domain.usecase.SaveUserDetailsUseCase
import com.example.domain.usecase.SearchUsersUseCase
import com.example.domain.usecase.SignInUseCase
import com.example.domain.usecase.SignOutUseCase
import com.example.domain.usecase.SignUpUseCase
import com.example.domain.usecase.UploadFilesUseCase
import com.example.domain.usecase.getAuthUseCase
import com.example.domain.usecase.getUserIDUseCase
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
  fun provideSignUpUseCase(
    authRepository: AuthRepository,
    userRepository: UserRepository
  ): SignUpUseCase {
    return SignUpUseCase(authRepository, userRepository)
  }

  @Provides
  @ViewModelScoped
  fun provideSignInUseCase(
    authRepository: AuthRepository,
    userRepository: UserRepository
  ): SignInUseCase {
    return SignInUseCase(authRepository, userRepository)
  }

  @Provides
  @ViewModelScoped
  fun provideSignOutUseCase(authRepository: AuthRepository): SignOutUseCase {
    return SignOutUseCase(authRepository)
  }

  @Provides
  @ViewModelScoped
  fun provideNotificationUseCase(notificationRepository: NotificationRepository): GetNotificationUseCase {
    return GetNotificationUseCase(notificationRepository)
  }

  @Provides
  @ViewModelScoped
  fun provideSaveUserDetailsUseCase(userRepository: UserRepository): SaveUserDetailsUseCase {
    return SaveUserDetailsUseCase(userRepository)
  }

  @Provides
  @ViewModelScoped
  fun provideGetAuthUseCase(
    authRepository: AuthRepository,
    onBoardingDataUseCase: OnBoardingDataUseCase
  ): getAuthUseCase {
    return getAuthUseCase(authRepository, onBoardingDataUseCase)
  }

  @Provides
  @ViewModelScoped
  fun provideCreateCapsuleUseCase(
    capsulesRepository: CapsulesRepository
  ): CreateCapsuleUseCase {
    return CreateCapsuleUseCase(capsulesRepository)
  }

  @Provides
  @ViewModelScoped
  fun provideOpenCapsuleScreenCheckPointUseCase(sharedPreferencesHelper: SharedPreferencesHelper): OpenCapsuleScreenCheckPointUseCase {
    return OpenCapsuleScreenCheckPointUseCase(sharedPreferencesHelper)
  }

  @Provides
  @ViewModelScoped
  fun provideGetUserIdUseCase(
    authRepository: AuthRepository,
  ): getUserIDUseCase {
    return getUserIDUseCase(authRepository)
  }

  @Provides
  @ViewModelScoped
  fun provideGetCapsulesListUseCase(capsulesRepository: CapsulesRepository): GetCapsuleListUseCase {
    return GetCapsuleListUseCase(capsulesRepository)
  }

  @Provides
  @ViewModelScoped
  fun provideGetCapsuleDetailsUseCase(capsulesRepository: CapsulesRepository): GetCapsuleDetailsUseCase {
    return GetCapsuleDetailsUseCase(capsulesRepository)
  }

  @Provides
  @ViewModelScoped
  fun provideSearchUsersUseCase(userRepository: UserRepository): SearchUsersUseCase {
    return SearchUsersUseCase(userRepository)
  }

  @Provides
  @ViewModelScoped
  fun provideGetUserDetailsUseCase(userRepository: UserRepository): GetUserDetailsUseCase {
    return GetUserDetailsUseCase(userRepository)
  }

  @Provides
  @ViewModelScoped
  fun provideUploadFilesUseCase(fileRepository: UploadFileRepository): UploadFilesUseCase {
    return UploadFilesUseCase(fileRepository)
  }

  @Provides
  @ViewModelScoped
  fun provideGetCapsuleAssetsUseCase(capsulesRepository: CapsulesRepository): GetCapsuleAssetsUseCase {
    return GetCapsuleAssetsUseCase(capsulesRepository)
  }
}
