package com.example.project_enlishlearning.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_enlishlearning.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

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
                .onSuccess {
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
                .onSuccess {
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
                .onSuccess {

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