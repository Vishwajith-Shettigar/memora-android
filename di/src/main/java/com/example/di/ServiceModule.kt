package com.example.di

import com.example.data.remote.UserRemoteDataSource
import com.example.data.repository.UploadFileRepository
import com.example.domain.usecase.DownloadFilesUseCase
import com.example.domain.usecase.UpdateFCMTokenUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

  @ServiceScoped
  @Provides
  fun provideDownloadFilesUseCase(
    repository: UploadFileRepository
  ): DownloadFilesUseCase {
    return DownloadFilesUseCase(repository)
  }

  @ServiceScoped
  @Provides
  fun provideUpdateFCMTokenUseCase(
    userRemoteDataSource: UserRemoteDataSource
  ): UpdateFCMTokenUseCase {
    return UpdateFCMTokenUseCase(userRemoteDataSource)
  }
}
