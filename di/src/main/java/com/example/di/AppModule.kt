package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.AppDatabase
import com.example.data.local.ReviewDao
import com.example.data.remote.AuthRemoteDataSource
import com.example.data.remote.CapsulesRemoteDataSource
import com.example.data.remote.FilesRemoteDataSource
import com.example.data.remote.NearByCapsulesDataSource
import com.example.data.remote.NotificationDataSource
import com.example.data.remote.UserRemoteDataSource
import com.example.data.repository.AuthRepository
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.CapsulesRepository
import com.example.data.repository.CapsulesRepositoryImpl
import com.example.data.repository.NearByCapsulesRepositoryImpl
import com.example.data.repository.NearByCapsulesRepository
import com.example.data.repository.NotificationRepository
import com.example.data.repository.NotificationRepositoryImpl
import com.example.data.repository.ReviewRepository
import com.example.data.repository.ReviewRepositoryImpl
import com.example.data.repository.UploadFileRepository
import com.example.data.repository.UploadFileRepositoryImpl
import com.example.data.repository.UserRepository
import com.example.data.repository.UserRepositoryImpl
import com.example.data.retrofilApi.NotificationApi
import com.example.data.sharedpreference.SharedPreferencesHelper
import com.example.domain.usecase.OnBoardingDataUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
  abstract fun bindNearByCapsulesRepository(nearByCapsulesRepositoryImpl: NearByCapsulesRepositoryImpl): NearByCapsulesRepository

  @Binds
  @Singleton
  abstract fun bindCapsulesRepository(capsulesRepositoryImpl: CapsulesRepositoryImpl): CapsulesRepository

  @Binds
  @Singleton
  abstract fun bindReviewRepository(reviewRepositoryImpl: ReviewRepositoryImpl): ReviewRepository

  @Binds
  @Singleton
  abstract fun bindNotificationRepository(notificationRepositoryImpl: NotificationRepositoryImpl): NotificationRepository


  @Binds
  @Singleton
  abstract fun bindUploadFilesRepository(uploadFileRepositoryImpl: UploadFileRepositoryImpl): UploadFileRepository

  companion object {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
      return Retrofit.Builder()
        .baseUrl(BuildConfig.SERVER_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    }

    @Provides
    fun provideNotificationApi(retrofit: Retrofit): NotificationApi {
      return retrofit.create(NotificationApi::class.java)
    }

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
    fun provideFirebaseMessaging(): FirebaseMessaging {
      return FirebaseMessaging.getInstance()
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
      authRemoteDataSource: AuthRemoteDataSource
    ): FilesRemoteDataSource {
      return FilesRemoteDataSource(firebaseStorage, context, firestore, authRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(
      firestore: FirebaseFirestore,
      firebaseAuth: FirebaseAuth,
      firebaseMessaging: FirebaseMessaging,
      remoteDataSource: AuthRemoteDataSource,
      storage: FirebaseStorage
    ): UserRemoteDataSource {
      return UserRemoteDataSource(
        firestore,
        firebaseAuth,
        firebaseMessaging,
        remoteDataSource,
        storage
      )
    }

    @Provides
    @Singleton
    fun provideNearByCapsulesDataSource(
      firestore: FirebaseFirestore,
    ): NearByCapsulesDataSource {
      return NearByCapsulesDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideNotificationRemoteDataSource(
      firestore: FirebaseFirestore,
      remoteDataSource: AuthRemoteDataSource,
    ): NotificationDataSource {
      return NotificationDataSource(firestore, remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideCapsulesRemoteDataSource(
      firestore: FirebaseFirestore,
      remoteDataSource: AuthRemoteDataSource,
      userRemoteDataSource: UserRemoteDataSource
    ): CapsulesRemoteDataSource {
      return CapsulesRemoteDataSource(firestore, remoteDataSource, userRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
      return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database"
      ).build()
    }

    @Provides
    @Singleton
    fun provideReviewDao(database: AppDatabase) = database.reviewDao()
  }
}
