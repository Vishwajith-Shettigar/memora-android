package com.example.data.remote

import com.example.util.EmailAlreadyExistsException
import com.example.util.EmailDoesntExistException
import com.example.util.PasswordDoesntMatchException
import com.example.util.Response
import com.example.util.UnspecifiedException
import com.example.util.UnverifiedEmailException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

// AuthRemoteDataSource.kt
class AuthRemoteDataSource @Inject constructor(
  private val firebaseAuth: FirebaseAuth,
  val firestore: FirebaseFirestore,
  private val firebaseMessaging: FirebaseMessaging,
) {
  suspend fun signInDeprecated(
    email: String,
    password: String,
    onResult: (Response<Exception>) -> Unit
  ) {
    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
      if (task.isSuccessful) {
        val user = firebaseAuth.currentUser
        if (user != null) {
          if (user.isEmailVerified) {
            onResult(Response.Success())
          } else {
            // Sign out if email is not verified
            firebaseAuth.signOut()
            onResult(Response.Success(data = UnverifiedEmailException()))
          }
        } else {
          onResult(Response.Error(exception = UnspecifiedException()))
        }
      } else {
        val exception = task.exception
        when (exception) {
          is FirebaseAuthInvalidUserException -> {
            onResult(Response.Error(exception = EmailDoesntExistException()))
          }

          is FirebaseAuthInvalidCredentialsException -> {
            onResult(Response.Error(exception = PasswordDoesntMatchException()))
          }

          else -> {
            onResult(Response.Error(exception = UnspecifiedException()))
          }
        }
      }
    }
  }

  suspend fun signIn(email: String, password: String): Response<String> {
    return try {
      firebaseAuth.signInWithEmailAndPassword(email, password).await()
      val user = firebaseAuth.currentUser
      if (user != null) {
        if (user.isEmailVerified) {
          saveTokenToFirestore(null)
          Response.Success(user.uid)
        } else {
          // Sign out if email is not verified
          firebaseAuth.signOut()
          Response.Error(exception = UnverifiedEmailException())
        }
      } else {
        Response.Error(exception = UnspecifiedException())
      }

    } catch (e: Exception) {
      when (e) {
        is FirebaseAuthInvalidUserException -> {
          Response.Error(exception = EmailDoesntExistException())
        }

        is FirebaseAuthInvalidCredentialsException -> {
          Response.Error(exception = PasswordDoesntMatchException())
        }

        else -> {
          Response.Error(exception = UnspecifiedException())
        }
      }
    }
  }

  suspend fun signUpDeprecated(
    email: String,
    password: String,
    onResult: (Response<Exception>) -> Response<Exception>
  ) {
    firebaseAuth.createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          // User created successfully, send verification email
          val user = firebaseAuth.currentUser
          user?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
            if (verificationTask.isSuccessful) {
              // Sign out the user since email is not verified
              firebaseAuth.signOut()
              onResult(Response.Success(data = UnverifiedEmailException()))
            } else {
              firebaseAuth.signOut()
              onResult(Response.Error(exception = UnspecifiedException()))
            }
          }
        } else {
          val exception = task.exception
          if (exception is FirebaseAuthUserCollisionException)
            onResult(Response.Error(exception = EmailAlreadyExistsException()))
          else
            onResult(Response.Error(exception = UnspecifiedException()))
        }
      }
  }

  suspend fun signUp(
    email: String,
    password: String,
  ): Response<Exception> {
    return try {
      firebaseAuth.createUserWithEmailAndPassword(email, password).await()
      val user = firebaseAuth.currentUser
      user?.sendEmailVerification()?.await()
      firebaseAuth.signOut()
      Response.Success(data = UnverifiedEmailException())
    } catch (e: Exception) {
      if (e is FirebaseAuthUserCollisionException)
        Response.Error(exception = EmailAlreadyExistsException())
      else
        Response.Error(exception = UnspecifiedException())
    }
  }

  suspend fun signOut() {
    try {
      firebaseAuth.signOut()
    } catch (
      _: Exception
    ) {
    }
  }

  suspend fun deleteUser() {
    try {
      firebaseAuth.currentUser?.delete()?.await()
    } catch (_: Exception) {
    }
  }

  fun getAuth(): FirebaseUser? {
    return try {
      firebaseAuth.currentUser

    } catch (_: Exception) {
      return null
    }
  }

  suspend fun saveTokenToFirestore(token: String?) {
    try {

      val user = getAuth()

      val fcmtoken: String =
        token ?: firebaseMessaging.token.await()

      val tokenData = mapOf("fcmToken" to fcmtoken)

      user?.uid?.let {
        firestore.collection("users").document(it)
          .update(tokenData).await()
      }

    } catch (_: Exception) {
    }
  }
}
