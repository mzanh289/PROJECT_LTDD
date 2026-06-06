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
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()
    private val userProfileRepository by lazy {
        UserProfileRepository(DatabaseProvider.getDatabase().userProfileDao())
    }

    private suspend fun syncUserProfile(user: FirebaseUser, isRegistration: Boolean) {
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
                val displayName = email.substringBefore("@")
                val newProfile = UserProfileEntity(
                    userId = uid,
                    email = email,
                    displayName = displayName,
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
                        syncUserProfile(firebaseUser, isRegistration = true)
                    }
                    repository.sendEmailVerification()
                    onSuccess()
                }
                .onFailure {
                    error = it.message ?: "Register failed"
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
                    error = it.message ?: "Login failed"
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

                    error =
                        it.message ?: "Something went wrong"
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

                    error =
                        it.message ?: "Something went wrong"
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

                    error =
                        it.message
                            ?: "Google Sign-In failed"
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

                    error =
                        it.message
                            ?: "Failed to resend email"
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
}