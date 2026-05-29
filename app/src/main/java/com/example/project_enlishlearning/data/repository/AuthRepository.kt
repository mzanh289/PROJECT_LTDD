package com.example.project_enlishlearning.data.repository

import com.example.project_enlishlearning.utils.FirebaseManager
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseManager.auth

    suspend fun register(
        email: String,
        password: String
    ): Result<FirebaseUser?> {

        return try {

            val result = auth
                .createUserWithEmailAndPassword(
                    email,
                    password
                )
                .await()

            Result.success(result.user)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun login(
        email: String,
        password: String
    ): Result<FirebaseUser?> {

        return try {

            val result = auth
                .signInWithEmailAndPassword(
                    email,
                    password
                )
                .await()

            Result.success(result.user)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun currentUser(): FirebaseUser? {
        return auth.currentUser
    }
}