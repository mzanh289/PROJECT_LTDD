package com.example.project_enlishlearning.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project_enlishlearning.navigation.Screen
import com.example.project_enlishlearning.ui.components.*
import com.example.project_enlishlearning.ui.theme.AppDimens
import com.example.project_enlishlearning.viewmodel.AuthViewModel

@Composable
fun EmailVerificationScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {

    Scaffold {

        AppGradientBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
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
                        ),

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Verify Your Email",
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text =
                                "We sent a verification email to your account."
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        PrimaryButton(
                            text = "I Verified",

                            onClick = {

                                authViewModel.checkEmailVerification {

                                    navController.navigate(
                                        Screen.Login.route
                                    ) {

                                        popUpTo(0)
                                    }
                                }
                            },

                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        SecondaryButton(
                            text = "Resend Email",

                            onClick = {

                                authViewModel
                                    .resendVerificationEmail()
                            },

                            modifier = Modifier.fillMaxWidth()
                        )

                        if (authViewModel.message.isNotEmpty()) {

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = authViewModel.message,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}