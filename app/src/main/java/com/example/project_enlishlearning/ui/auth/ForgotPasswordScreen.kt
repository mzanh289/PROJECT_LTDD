package com.example.project_enlishlearning.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project_enlishlearning.ui.components.*
import com.example.project_enlishlearning.ui.theme.AppDimens
import com.example.project_enlishlearning.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {

    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Forgot Password",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = {
                    navController.popBackStack()
                }
            )
        }
    ) { innerPadding ->

        AppGradientBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppDimens.ScreenPadding),

                verticalArrangement = Arrangement.Center,

                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AppCard(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(
                            AppDimens.CardPadding
                        )
                    ) {

                        Text(
                            text = "Reset Password",
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Enter your email to receive reset link."
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        AppTextField(
                            value = email,
                            onValueChange = {
                                email = it
                            },
                            label = "Email",
                            leadingIcon = Icons.Default.Email,
                            keyboardOptions =
                                androidx.compose.foundation.text.KeyboardOptions(
                                    keyboardType = KeyboardType.Email
                                )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        PrimaryButton(
                            text = if (viewModel.loading)
                                "Sending..."
                            else
                                "Send Reset Email",

                            enabled = !viewModel.loading,

                            onClick = {

                                FirebaseAuth
                                    .getInstance()
                                    .sendPasswordResetEmail(email)
                                    .addOnSuccessListener {

                                        message =
                                            "Reset email sent successfully"
                                    }
                                    .addOnFailureListener {

                                        message =
                                            it.message ?: "Something went wrong"
                                    }
                            },

                            modifier = Modifier.fillMaxWidth()
                        )

                        if (message.isNotEmpty()) {

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = message,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}