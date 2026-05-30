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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import com.example.project_enlishlearning.ui.components.AppToolbar
import com.example.project_enlishlearning.ui.components.AppCard
import com.example.project_enlishlearning.ui.components.AppGradientBackground
import com.example.project_enlishlearning.ui.components.AppPasswordField
import com.example.project_enlishlearning.ui.components.AppTextField
import com.example.project_enlishlearning.ui.components.AuthLogo
import com.example.project_enlishlearning.ui.components.PrimaryButton
import com.example.project_enlishlearning.ui.components.SecondaryButton
import com.example.project_enlishlearning.ui.theme.AppDimens
import com.example.project_enlishlearning.ui.theme.ProjectEnlishLearningTheme
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.project_enlishlearning.R
import com.example.project_enlishlearning.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import androidx.compose.ui.res.stringResource

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel(),
    onForgotPassword: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var validationError by remember {
        mutableStateOf("")
    }

    var successMessage by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    val gso = GoogleSignInOptions.Builder(
        GoogleSignInOptions.DEFAULT_SIGN_IN
    )
        .requestIdToken(
            "330755271275-83ou1449iqobacau0ib8mkm37bb35n9u.apps.googleusercontent.com"
        )
        .requestEmail()
        .build()

    val googleSignInClient =
        GoogleSignIn.getClient(context, gso)

    val launcher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.StartActivityForResult()
        ) { result ->

            val task = GoogleSignIn
                .getSignedInAccountFromIntent(result.data)

            try {

                val account = task.getResult(
                    ApiException::class.java
                )

                val credential =
                    GoogleAuthProvider.getCredential(
                        account.idToken,
                        null
                    )

                Firebase.auth
                    .signInWithCredential(credential)
                    .addOnCompleteListener {

                        if (it.isSuccessful) {

                            navController.navigate(
                                Screen.Dashboard.route
                            )
                        }
                    }

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Sign In",
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
                    text = "Welcome Back",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Please sign in to continue learning.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(AppDimens.SectionSpacing))

                AppCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(AppDimens.CardPadding)) {
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

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End
                        ) {
                            Text(
                                text = "Forgot Password?",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable { onForgotPassword() }
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        PrimaryButton(
                            text = if (viewModel.loading) "Loading..." else "Sign In",
                            onClick = {

                                validationError = ""
                                successMessage = ""

                                when {

                                    email.isBlank() -> {
                                        validationError = "Email cannot be empty"
                                        return@PrimaryButton
                                    }

                                    !android.util.Patterns.EMAIL_ADDRESS
                                        .matcher(email)
                                        .matches() -> {

                                        validationError = "Invalid email format"
                                        return@PrimaryButton
                                    }

                                    password.isBlank() -> {
                                        validationError = "Password cannot be empty"
                                        return@PrimaryButton
                                    }

                                    password.length < 6 -> {
                                        validationError = "Password must be at least 6 characters"
                                        return@PrimaryButton
                                    }
                                }

                                viewModel.login(
                                    email = email,
                                    password = password
                                ) {

                                    successMessage = "Login successful"

                                    navController.navigate(
                                        Screen.Dashboard.route
                                    ) {
                                        popUpTo(Screen.Login.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (validationError.isNotEmpty()) {

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = validationError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        if (viewModel.error.isNotEmpty()) {

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = viewModel.error,
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

                                launcher.launch(
                                    googleSignInClient.signInIntent
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Don't have an account?",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Register",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { navController.navigate(Screen.Register.route) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "By continuing, you agree to our Terms & Privacy Policy.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    ProjectEnlishLearningTheme {
        LoginScreen(navController = rememberNavController())
    }
}