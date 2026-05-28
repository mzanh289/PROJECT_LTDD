package com.example.project_enlishlearning.navigation

sealed class Screen(val route: String) {
	data object Splash : Screen("splash")
	data object Login : Screen("login")
	data object Register : Screen("register")
	data object Dashboard : Screen("dashboard")
	data object VocabularySetList : Screen("vocabulary_set_list")
	data object CreateSet : Screen("create_set")
	data object AddVocabulary : Screen("add_vocabulary")
	data object FlashcardLearning : Screen("flashcard_learning")
	data object FlashcardResult : Screen("flashcard_result")
	data object NewWordsPreview : Screen("new_words_preview")
	data object Profile : Screen("profile")
	data object Notification : Screen("notification")
	data object VocabularySetDetail : Screen("vocabulary_set_detail")
	data object EditVocabularySet : Screen("edit_vocabulary_set")
	data object ReviewVocabulary : Screen("review_vocabulary")
	data object EditProfileScreen : Screen("edit_profile")
}

