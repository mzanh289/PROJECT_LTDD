package com.example.project_enlishlearning.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_enlishlearning.navigation.Screen
import com.example.project_enlishlearning.ui.components.AppCard
import com.example.project_enlishlearning.ui.components.AppGradientBackground
import com.example.project_enlishlearning.ui.components.AppPasswordField
import com.example.project_enlishlearning.ui.components.AppTextField
import com.example.project_enlishlearning.ui.components.AppToolbar
import com.example.project_enlishlearning.ui.components.AuthLogo
import com.example.project_enlishlearning.ui.components.PrimaryButton
import com.example.project_enlishlearning.ui.components.SecondaryButton
import com.example.project_enlishlearning.ui.theme.AppDimens
import com.example.project_enlishlearning.ui.theme.ProjectEnlishLearningTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_enlishlearning.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    onGoogle: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }
    var acceptedTerms by remember { mutableStateOf(false) }
    var validationError by remember {
        mutableStateOf("")
    }

    var successMessage by remember {
        mutableStateOf("")
    }
    val loading = authViewModel.loading
    val error = authViewModel.error

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Create Account",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = { navController.popBackStack() }
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
                    .verticalScroll(rememberScrollState())
                    .padding(AppDimens.ScreenPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                AuthLogo()
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Start learning English with MinLish today.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(AppDimens.SectionSpacing))

                AppCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(AppDimens.CardPadding)) {
                        AppTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = "Full Name",
                            leadingIcon = Icons.Default.Person
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        AppTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email Address",
                            leadingIcon = Icons.Default.Email,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = KeyboardType.Email
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        AppPasswordField(
                            value = password,
                            onValueChange = { password = it },
                            label = "Password",
                            visible = passwordVisible,
                            onToggle = { passwordVisible = !passwordVisible }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        AppPasswordField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = "Confirm Password",
                            visible = confirmVisible,
                            onToggle = { confirmVisible = !confirmVisible }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = acceptedTerms,
                                onCheckedChange = { acceptedTerms = it }
                            )
                            Text(
                                text = "I agree to Terms & Privacy Policy",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        PrimaryButton(
                            text = if (loading) "Loading..." else "Create Account",
                            onClick = {

                                validationError = ""
                                successMessage = ""

                                when {

                                    fullName.isBlank() -> {
                                        validationError = "Full name cannot be empty"
                                    }

                                    email.isBlank() -> {
                                        validationError = "Email cannot be empty"
                                    }

                                    !android.util.Patterns.EMAIL_ADDRESS
                                        .matcher(email)
                                        .matches() -> {

                                        validationError = "Invalid email format"
                                    }

                                    password.isBlank() -> {
                                        validationError = "Password cannot be empty"
                                    }

                                    password.length < 6 -> {
                                        validationError = "Password must be at least 6 characters"
                                    }

                                    confirmPassword.isBlank() -> {
                                        validationError = "Please confirm password"
                                    }

                                    password != confirmPassword -> {
                                        validationError = "Passwords do not match"
                                    }

                                    !acceptedTerms -> {
                                        validationError = "Please accept Terms & Privacy Policy"
                                    }

                                    else -> {

                                        authViewModel.register(
                                            email = email,
                                            password = password
                                        ) {
                                            successMessage =
                                                "Account created successfully. Verification email sent."

                                            navController.navigate(Screen.EmailVerification.route) {
                                                popUpTo(Screen.Register.route) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (validationError.isNotEmpty()) {

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = validationError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        if (successMessage.isNotEmpty()) {

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = successMessage,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        if (error.isNotEmpty()) {

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            HorizontalDivider(modifier = Modifier.weight(1f))
                            Text(
                                text = " OR ",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            HorizontalDivider(modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        SecondaryButton(
                            text = "Continue with Google",
                            onClick = {
                                onGoogle()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Already have an account?",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Sign In",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { navController.navigate(Screen.Login.route) }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    ProjectEnlishLearningTheme {
        RegisterScreen(navController = rememberNavController())
    }
}