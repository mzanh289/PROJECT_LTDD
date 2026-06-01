package com.example.project_enlishlearning.navigation

sealed class Screen(val route: String) {
	data object Splash : Screen("splash")
	data object Login : Screen("login")
	data object Register : Screen("register")
	data object Dashboard : Screen("dashboard")
	data object VocabularySetList : Screen("vocabulary_set_list")
	data object CreateSet : Screen("create_set")
	data object AddVocabulary : Screen("add_vocabulary")
	data object FlashcardLearning : Screen("flashcard_learning") {
		fun createRoute(setId: Int, mode: String = "normal"): String {
			return "$route/$setId/$mode"
		}
	}
	data object FlashcardResult : Screen("flashcard_result") {
		fun createRoute(
			setId: Int,
			correct: Int,
			wrong: Int,
			total: Int
		): String {
			return "$route/$setId/$correct/$wrong/$total"
		}
	}
	data object NewWordsPreview : Screen("new_words_preview") {
		fun createRoute(setId: Int): String {
			return "$route/$setId"
		}
	}
	data object Profile : Screen("profile")
	data object Notification : Screen("notification")
	data object VocabularySetDetail : Screen("vocabulary_set_detail")
	data object EditVocabularySet : Screen("edit_vocabulary_set")
	data object ReviewVocabulary : Screen("review_vocabulary") {
		fun createRoute(setId: Int): String {
			return "$route/$setId"
		}
	}
	data object EditProfileScreen : Screen("edit_profile")
	data object ForgotPassword : Screen("forgot_password")
	data object EmailVerification : Screen("email_verification")
}

