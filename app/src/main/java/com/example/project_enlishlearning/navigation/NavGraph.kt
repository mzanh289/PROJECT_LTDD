package com.example.project_enlishlearning.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.example.project_enlishlearning.data.local.database.AppDatabase
import com.example.project_enlishlearning.data.repository.DashboardRepository
import com.example.project_enlishlearning.data.repository.UserProfileRepository

import com.example.project_enlishlearning.ui.auth.EmailVerificationScreen
import com.example.project_enlishlearning.ui.auth.ForgotPasswordScreen
import com.example.project_enlishlearning.ui.auth.LoginScreen
import com.example.project_enlishlearning.ui.auth.RegisterScreen

import com.example.project_enlishlearning.ui.components.BottomNavItem

import com.example.project_enlishlearning.ui.dashboard.DashboardScreen

import com.example.project_enlishlearning.ui.flashcard.FlashcardLearningScreen
import com.example.project_enlishlearning.ui.flashcard.FlashcardResultScreen
import com.example.project_enlishlearning.ui.flashcard.NewWordsPreviewScreen
import com.example.project_enlishlearning.ui.flashcard.ReviewVocabularyScreen

import com.example.project_enlishlearning.ui.notification.NotificationSettingsScreen

import com.example.project_enlishlearning.ui.profile.EditProfileScreen
import com.example.project_enlishlearning.ui.profile.ProfileScreen

import com.example.project_enlishlearning.ui.splash.SplashScreen

import com.example.project_enlishlearning.ui.vocabulary.AddVocabularyScreen
import com.example.project_enlishlearning.ui.vocabulary.CreateVocabularySetScreen
import com.example.project_enlishlearning.ui.vocabulary.EditVocabularyScreen
import com.example.project_enlishlearning.ui.vocabulary.EditVocabularySetScreen
import com.example.project_enlishlearning.ui.vocabulary.ImportVocabularyScreen
import com.example.project_enlishlearning.ui.vocabulary.VocabularySetDetailScreen
import com.example.project_enlishlearning.ui.vocabulary.VocabularySetListScreen

import com.example.project_enlishlearning.viewmodel.AuthViewModel
import com.example.project_enlishlearning.viewmodel.DashboardViewModel
import com.example.project_enlishlearning.viewmodel.DashboardViewModelFactory
import com.example.project_enlishlearning.viewmodel.ProfileViewModel
import com.example.project_enlishlearning.viewmodel.ProfileViewModelFactory

@Composable
fun AppNavGraph(
    navController: NavHostController,
    database: AppDatabase,
    authViewModel: AuthViewModel
) {
    val userId = authViewModel.getCurrentUserId() ?: "local_user"

    val userProfileRepository = UserProfileRepository(
        userProfileDao = database.userProfileDao()
    )

    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(
            repository = userProfileRepository,
            userId = userId
        )
    )
    val dashboardRepository = DashboardRepository(
        vocabularyDao = database.vocabularyDao(),
        learningProgressDao = database.learningProgressDao()
    )

    val dashboardViewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModelFactory(
            repository = dashboardRepository,
            userId = userId
        )
    )

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        // Splash
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        // Login
        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                onForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }

        // Register
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }

        // Forgot Password
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController)
        }

        // Email Verification
        composable(Screen.EmailVerification.route) {
            EmailVerificationScreen(navController)
        }

        // Dashboard
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                viewModel = dashboardViewModel,
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                },
                onVocabularyClick = {
                    navController.navigate(Screen.VocabularySetList.route)
                },
                onFlashcardClick = {
                    navController.navigate(Screen.VocabularySetList.route)
                }
            )
        }

        // Vocabulary List
        composable(Screen.VocabularySetList.route) {
            VocabularySetListScreen(
                navController = navController,
                selected = BottomNavItem.Vocabulary,
                onBottomItemSelected = {
                    navigateToBottomTab(navController, it)
                }
            )
        }

        // Create Vocabulary Set
        composable(Screen.CreateSet.route) {
            CreateVocabularySetScreen(
                navController = navController
            )
        }

        // Add Vocabulary
        composable(
            route = "${Screen.AddVocabulary.route}/{setId}",
            arguments = listOf(
                navArgument("setId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val setId =
                backStackEntry.arguments?.getInt("setId") ?: 0

            AddVocabularyScreen(
                navController = navController,
                setId = setId
            )
        }

        // Vocabulary Detail
        composable(
            route = "${Screen.VocabularySetDetail.route}/{setId}",
            arguments = listOf(
                navArgument("setId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val setId =
                backStackEntry.arguments?.getInt("setId") ?: 0

            VocabularySetDetailScreen(
                navController = navController,
                setId = setId,
                selected = BottomNavItem.Vocabulary,
                onBottomItemSelected = {
                    navigateToBottomTab(navController, it)
                }
            )
        }

        // Import Vocabulary
        composable(
            route = "${Screen.ImportVocabulary.route}/{setId}",
            arguments = listOf(
                navArgument("setId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val setId =
                backStackEntry.arguments?.getInt("setId") ?: 0

            ImportVocabularyScreen(
                navController = navController,
                setId = setId
            )
        }

        // Edit Vocabulary Set
        composable(
            route = "${Screen.EditVocabularySet.route}/{setId}",
            arguments = listOf(
                navArgument("setId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val setId =
                backStackEntry.arguments?.getInt("setId") ?: 0

            EditVocabularySetScreen(
                navController = navController,
                setId = setId
            )
        }

        // Flashcard Learning
        composable(
            route = "${Screen.FlashcardLearning.route}/{setId}/{mode}",
            arguments = listOf(
                navArgument("setId") {
                    type = NavType.IntType
                },
                navArgument("mode") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val setId = backStackEntry.arguments?.getInt("setId") ?: 0
            val mode = backStackEntry.arguments?.getString("mode") ?: "normal"

            FlashcardLearningScreen(
                navController = navController,
                setId = setId,
                mode = mode
            )
        }

        // Flashcard Result
        composable(
            route = "${Screen.FlashcardResult.route}/{setId}/{correct}/{wrong}/{total}",
            arguments = listOf(
                navArgument("setId") {
                    type = NavType.IntType
                },
                navArgument("correct") {
                    type = NavType.IntType
                },
                navArgument("wrong") {
                    type = NavType.IntType
                },
                navArgument("total") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val setId = backStackEntry.arguments?.getInt("setId") ?: 0
            val correct = backStackEntry.arguments?.getInt("correct") ?: 0
            val wrong = backStackEntry.arguments?.getInt("wrong") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 0

            FlashcardResultScreen(
                navController = navController,
                setId = setId,
                correct = correct,
                wrong = wrong,
                total = total
            )
        }

        // New Words Preview
        composable(
            route = "${Screen.NewWordsPreview.route}/{setId}",
            arguments = listOf(
                navArgument("setId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val setId =
                backStackEntry.arguments?.getInt("setId") ?: 0

            NewWordsPreviewScreen(
                navController = navController,
                setId = setId
            )
        }

        // Review Vocabulary
        composable(
            route = "${Screen.ReviewVocabulary.route}/{setId}",
            arguments = listOf(
                navArgument("setId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val setId = backStackEntry.arguments?.getInt("setId") ?: 0

            ReviewVocabularyScreen(
                navController = navController,
                setId = setId
            )
        }

        // Notification
        composable(Screen.Notification.route) {
            NotificationSettingsScreen(
                navController = navController,
                selected = BottomNavItem.Notification,
                onBottomItemSelected = {
                    navigateToBottomTab(navController, it)
                }
            )
        }

        // Profile
        composable(Screen.Profile.route) {
            ProfileScreen(
                authViewModel = authViewModel,
                profileViewModel = profileViewModel,
                onEditProfileClick = {
                    navController.navigate(Screen.EditProfileScreen.route)
                },
                onLogoutClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Edit Profile
        composable(Screen.EditProfileScreen.route) {
            EditProfileScreen(
                navController = navController,
                authViewModel = authViewModel,
                profileViewModel = profileViewModel
            )
        }

        // Edit Vocabulary
        composable(
            route = "edit_word/{wordId}",
            arguments = listOf(
                navArgument("wordId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val wordId =
                backStackEntry.arguments?.getLong("wordId") ?: 0L

            EditVocabularyScreen(
                navController = navController,
                wordId = wordId
            )
        }
    }
}

private fun navigateToBottomTab(
    navController: NavController,
    item: BottomNavItem
) {
    val target = when (item) {
        BottomNavItem.Dashboard -> Screen.Dashboard.route
        BottomNavItem.Vocabulary -> Screen.VocabularySetList.route
        BottomNavItem.Notification -> Screen.Notification.route
        BottomNavItem.Profile -> Screen.Profile.route
    }

    navController.navigate(target) {
        launchSingleTop = true
    }
}