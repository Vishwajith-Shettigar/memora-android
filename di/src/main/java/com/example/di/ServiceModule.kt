package com.example.di

import com.example.data.repository.UploadFileRepository
import com.example.domain.usecase.DownloadFilesUseCase
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
}
