package com.example.project_enlishlearning.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
import com.example.project_enlishlearning.ui.vocabulary.EditVocabularySetScreen
import com.example.project_enlishlearning.ui.vocabulary.VocabularySetDetailScreen
import com.example.project_enlishlearning.ui.vocabulary.VocabularySetListScreen

@Composable
fun AppNavGraph() {
	val navController = rememberNavController()

	NavHost(
		navController = navController,
		startDestination = Screen.Splash.route
	) {
		composable(Screen.Splash.route) {
			SplashScreen(navController = navController)
		}
		composable(Screen.Login.route) {
			LoginScreen(
				navController = navController,
				onForgotPassword = {
					navController.navigate(
						Screen.ForgotPassword.route
					)
				}
			)
		}
		composable(Screen.Register.route) {
			RegisterScreen(
				navController = navController
			)
		}
		composable(Screen.Dashboard.route) {
			DashboardScreen(
				navController = navController,
				selected = BottomNavItem.Dashboard,
				onBottomItemSelected = { item -> navigateToBottomTab(navController, item) }
			)
		}
		composable(Screen.VocabularySetList.route) {
			VocabularySetListScreen(
				navController = navController,
				selected = BottomNavItem.Vocabulary,
				onBottomItemSelected = { item -> navigateToBottomTab(navController, item) }
			)
		}
		composable(Screen.CreateSet.route) {
			CreateVocabularySetScreen(
				navController = navController,
				selected = BottomNavItem.Vocabulary,
				onBottomItemSelected = { item -> navigateToBottomTab(navController, item) }
			)
		}
		composable(Screen.AddVocabulary.route) {
			AddVocabularyScreen(
				navController = navController,
				selected = BottomNavItem.Vocabulary,
				onBottomItemSelected = { item -> navigateToBottomTab(navController, item) },
			)
		}
		composable(Screen.VocabularySetDetail.route) {
			VocabularySetDetailScreen(
				navController = navController,
				selected = BottomNavItem.Vocabulary,
				onBottomItemSelected = { item -> navigateToBottomTab(navController, item) }
			)
		}
		composable(Screen.EditVocabularySet.route) {
			EditVocabularySetScreen(
				navController = navController,
				selected = BottomNavItem.Vocabulary,
				onBottomItemSelected = { item -> navigateToBottomTab(navController, item) }
			)
		}
		composable(Screen.FlashcardLearning.route) {
			FlashcardLearningScreen(
				navController = navController
			)
		}
		composable(Screen.FlashcardResult.route) {
			FlashcardResultScreen(
				navController = navController
			)
		}
		composable(Screen.NewWordsPreview.route) {
			NewWordsPreviewScreen(
				navController = navController,
			)
		}
		composable(Screen.Profile.route) {
			ProfileScreen(
				navController = navController,
				selected = BottomNavItem.Profile,
				onBottomItemSelected = { item -> navigateToBottomTab(navController, item) }
			)
		}
		composable(Screen.Notification.route) {
			NotificationSettingsScreen(
				navController = navController,
				selected = BottomNavItem.Notification,
				onBottomItemSelected = { item -> navigateToBottomTab(navController, item) }
			)
		}
		composable(Screen.ReviewVocabulary.route) {
			ReviewVocabularyScreen(
				navController = navController
			)
		}

		composable(Screen.EditProfileScreen.route) {
			EditProfileScreen(navController)
		}

		composable(Screen.ForgotPassword.route) {
			ForgotPasswordScreen(navController)
		}

		composable(Screen.EmailVerification.route) {
			EmailVerificationScreen(navController)
		}
	}
}

private fun navigateToBottomTab(navController: NavController, item: BottomNavItem) {
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

