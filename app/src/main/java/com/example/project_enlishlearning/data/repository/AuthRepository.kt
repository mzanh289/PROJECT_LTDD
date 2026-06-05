package com.example.project_enlishlearning.data.repository

import com.example.project_enlishlearning.utils.FirebaseManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
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

    fun sendEmailVerification() {
        Firebase.auth.currentUser
            ?.sendEmailVerification()
    }

    suspend fun sendPasswordResetEmail(
        email: String
    ): Result<Unit> {

        return try {

            auth.sendPasswordResetEmail(email)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun checkEmailVerified(): Result<Boolean> {

        return try {

            auth.currentUser
                ?.reload()
                ?.await()

            Result.success(
                auth.currentUser?.isEmailVerified == true
            )

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun loginWithGoogle(
        idToken: String
    ): Result<FirebaseUser?> {

        return try {

            val credential =
                GoogleAuthProvider.getCredential(
                    idToken,
                    null
                )

            val result = auth
                .signInWithCredential(credential)
                .await()

            Result.success(result.user)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun resendEmailVerification(): Result<Unit> {

        return try {

            auth.currentUser
                ?.sendEmailVerification()
                ?.await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun getCurrentUserEmail(): String {
        return auth.currentUser?.email ?: ""
    }
}