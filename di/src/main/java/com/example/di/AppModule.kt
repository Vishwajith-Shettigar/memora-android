package com.example.di

import com.example.data.remote.AuthRemoteDataSource
import com.example.data.repository.AuthRepository
import com.example.data.repository.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

  @Binds
  @Singleton
  abstract fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

  companion object {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
      return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(
      firebaseAuth: FirebaseAuth
    ): AuthRemoteDataSource {
      return AuthRemoteDataSource(firebaseAuth)
    }
  }
}
