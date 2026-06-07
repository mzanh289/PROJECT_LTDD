package com.example.project_enlishlearning.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_enlishlearning.data.local.entity.UserProfileEntity
import com.example.project_enlishlearning.data.repository.AuthRepository
import com.example.project_enlishlearning.data.repository.UserProfileRepository
import com.example.project_enlishlearning.utils.DatabaseProvider
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()
    private val userProfileRepository by lazy {
        UserProfileRepository(DatabaseProvider.getDatabase().userProfileDao())
    }

    private suspend fun syncUserProfile(user: FirebaseUser, isRegistration: Boolean, displayName: String? = null) {
        val uid = user.uid
        val email = user.email ?: ""

        if (isRegistration) {
            Log.d("AUTH", "Registration success: UID = $uid, email = $email")
        } else {
            Log.d("AUTH", "Login success: UID = $uid, email = $email")
        }

        try {
            val existingProfile = userProfileRepository.getProfileOneShot(uid)
            if (existingProfile == null) {
                val finalDisplayName = displayName ?: email.substringBefore("@")
                val newProfile = UserProfileEntity(
                    userId = uid,
                    email = email,
                    displayName = finalDisplayName,
                    englishLevel = "A1",
                    learningGoal = "",
                    dailyNewWordTarget = 10,
                    dailyReviewTarget = 20
                )
                userProfileRepository.saveProfile(newProfile)
                Log.d("AUTH", "Profile created: UID = $uid")
            } else {
                Log.d("AUTH", "Existing profile found: UID = $uid")
            }
        } catch (e: Exception) {
            Log.e("AUTH", "Failed to sync user profile: ${e.message}", e)
        }
    }

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf("")
        private set

    var successMessage by mutableStateOf("")
        private set

    var message by mutableStateOf("")
        private set

    fun register(
        email: String,
        password: String,
        displayName: String? = null,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            loading = true

            repository.register(
                email,
                password
            )
                .onSuccess { firebaseUser ->
                    if (firebaseUser != null) {
                        syncUserProfile(firebaseUser, isRegistration = true, displayName = displayName)
                    }
                    repository.sendEmailVerification()
                    onSuccess()
                }
                .onFailure {
                    error = getFriendlyErrorMessage(it)
                }

            loading = false
        }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            loading = true

            repository.login(
                email,
                password
            )
                .onSuccess { firebaseUser ->
                    if (firebaseUser != null) {
                        syncUserProfile(firebaseUser, isRegistration = false)
                    }
                    onSuccess()
                }
                .onFailure {
                    error = getFriendlyErrorMessage(it)
                }

            loading = false
        }
    }

    fun resetPassword(
        email: String,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            loading = true

            repository
                .sendPasswordResetEmail(email)
                .onSuccess {

                    successMessage =
                        "Reset email sent successfully"

                    onSuccess()
                }
                .onFailure {
                    error = getFriendlyErrorMessage(it)
                }

            loading = false
        }
    }

    fun checkEmailVerification(
        onVerified: () -> Unit
    ) {

        viewModelScope.launch {

            loading = true

            repository
                .checkEmailVerified()
                .onSuccess { verified ->

                    if (verified) {

                        onVerified()

                    } else {

                        error =
                            "Email is not verified yet"
                    }
                }
                .onFailure {
                    error = getFriendlyErrorMessage(it)
                }

            loading = false
        }
    }

    fun loginWithGoogle(
        idToken: String,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            loading = true

            repository
                .loginWithGoogle(idToken)
                .onSuccess { firebaseUser ->
                    if (firebaseUser != null) {
                        syncUserProfile(firebaseUser, isRegistration = false)
                    }
                    onSuccess()
                }
                .onFailure {

                    error = getFriendlyErrorMessage(it)
                }

            loading = false
        }
    }

    fun resendVerificationEmail() {

        viewModelScope.launch {

            repository
                .resendEmailVerification()
                .onSuccess {

                    message =
                        "Verification email resent"
                }
                .onFailure {

                    error = getFriendlyErrorMessage(it)
                }
        }
    }
    fun getCurrentUserId(): String? {
        return repository.getCurrentUserId()
    }

    fun getCurrentUserEmail(): String {
        return repository.getCurrentUserEmail()
    }

    fun logout(onLogout: () -> Unit) {
        repository.logout()
        onLogout()
    }

    fun checkSession(
        onSessionValid: () -> Unit,
        onSessionInvalid: () -> Unit
    ) {
        viewModelScope.launch {
            val user = repository.currentUser()
            if (user == null) {
                onSessionInvalid()
                return@launch
            }
            try {
                // Reload user to verify if they still exist in Firebase Console
                user.reload().await()
                onSessionValid()
            } catch (e: FirebaseAuthInvalidUserException) {
                // User was deleted/disabled in Firebase Console
                val uid = user.uid
                try {
                    userProfileRepository.deleteProfile(uid)
                } catch (dbEx: Exception) {
                    Log.e("AUTH", "Failed to delete user profile from Room: ${dbEx.message}", dbEx)
                }
                repository.logout()
                onSessionInvalid()
            } catch (e: Exception) {
                // Other exceptions (e.g. network offline) are ignored, assume valid
                Log.w("AUTH", "Session verification failed: ${e.message}", e)
                onSessionValid()
            }
        }
    }

    private fun getFriendlyErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is FirebaseAuthInvalidUserException -> {
                "No account found with this email address, or the account has been disabled."
            }
            is FirebaseAuthInvalidCredentialsException -> {
                "Incorrect email or password. Please try again."
            }
            is FirebaseAuthUserCollisionException -> {
                "This email address is already registered."
            }
            is FirebaseAuthWeakPasswordException -> {
                "The password is too weak. Please use a stronger password (at least 6 characters)."
            }
            is FirebaseAuthRecentLoginRequiredException -> {
                "For security reasons, please log out and log in again before performing this action."
            }
            is FirebaseNetworkException -> {
                "Network error. Please check your internet connection."
            }
            else -> {
    "An unexpected error occurred. Please try again."
}
        }
    }
}