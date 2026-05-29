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
}