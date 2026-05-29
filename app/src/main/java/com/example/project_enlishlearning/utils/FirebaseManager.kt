package com.example.project_enlishlearning.utils

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

object FirebaseManager {

    val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
}