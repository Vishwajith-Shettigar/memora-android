package com.example.di

import android.content.Context
import com.example.data.remote.AuthRemoteDataSource
import com.example.data.remote.CapsulesRemoteDataSource
import com.example.data.remote.FilesRemoteDataSource
import com.example.data.remote.UserRemoteDataSource
import com.example.data.repository.AuthRepository
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.CapsulesRepository
import com.example.data.repository.CapsulesRepositoryImpl
import com.example.data.repository.UploadFileRepository
import com.example.data.repository.UploadFileRepositoryImpl
import com.example.data.repository.UserRepository
import com.example.data.repository.UserRepositoryImpl
import com.example.data.sharedpreference.SharedPreferencesHelper
import com.example.domain.usecase.OnBoardingDataUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

  @Binds
  @Singleton
  abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

  @Binds
  @Singleton
  abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

  @Binds
  @Singleton
  abstract fun bindCapsulesRepository(capsulesRepositoryImpl: CapsulesRepositoryImpl): CapsulesRepository

  @Binds
  @Singleton
  abstract fun bindUploadFilesRepository(uploadFileRepositoryImpl: UploadFileRepositoryImpl): UploadFileRepository

  companion object {

    @Provides
    @Singleton
    fun provideSharedPreferencesHelper(@ApplicationContext context: Context): SharedPreferencesHelper {
      return SharedPreferencesHelper(context)
    }

    @Provides
    @Singleton
    fun provideOnBoardingDetailsUseCase(sharedPreferencesHelper: SharedPreferencesHelper): OnBoardingDataUseCase {
      return OnBoardingDataUseCase(sharedPreferencesHelper)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
      return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
      return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
      return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(
      firebaseAuth: FirebaseAuth
    ): AuthRemoteDataSource {
      return AuthRemoteDataSource(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideFilesRemoteDataSource(
      firebaseStorage: FirebaseStorage,
      @ApplicationContext context: Context,
      firestore: FirebaseFirestore,
authRemoteDataSource: AuthRemoteDataSource    ): FilesRemoteDataSource {
      return FilesRemoteDataSource(firebaseStorage, context, firestore, authRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(
      firestore: FirebaseFirestore,
      remoteDataSource: AuthRemoteDataSource
    ): UserRemoteDataSource {
      return UserRemoteDataSource(firestore, remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideCapsulesRemoteDataSource(
      firestore: FirebaseFirestore,
      remoteDataSource: AuthRemoteDataSource,
      userRemoteDataSource: UserRemoteDataSource
    ): CapsulesRemoteDataSource {
      return CapsulesRemoteDataSource(firestore, remoteDataSource,userRemoteDataSource)
    }
  }
}
