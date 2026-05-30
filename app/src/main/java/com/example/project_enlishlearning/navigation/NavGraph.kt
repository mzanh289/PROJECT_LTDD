package com.example.project_enlishlearning.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.project_enlishlearning.ui.vocabulary.EditVocabularyScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController
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
        // 1. Màn hình Tạo bộ từ vựng (Không cần setId, không có BottomBar)
        composable(Screen.CreateSet.route) {
            CreateVocabularySetScreen(
                navController = navController
            )
        }

        // 2. Màn hình Thêm từ vựng (Bắt buộc có setId, không có BottomBar)
        composable(
            route = Screen.AddVocabulary.route + "/{setId}",
            arguments = listOf(navArgument("setId") { type = NavType.IntType })
        ) { backStackEntry ->
            val setId = backStackEntry.arguments?.getInt("setId") ?: 0
            AddVocabularyScreen(
                navController = navController,
                setId = setId
            )
        }

        // 3. Màn hình Chi tiết bộ từ vựng (Bắt buộc có setId, CÓ BottomBar)
        composable(
            route = Screen.VocabularySetDetail.route + "/{setId}",
            arguments = listOf(navArgument("setId") { type = NavType.IntType })
        ) { backStackEntry ->
            val setId = backStackEntry.arguments?.getInt("setId") ?: 0
            VocabularySetDetailScreen(
                navController = navController,
                setId = setId,
                selected = BottomNavItem.Vocabulary,
                onBottomItemSelected = { item -> navigateToBottomTab(navController, item) }
            )
        }

        // 4. Màn hình Sửa thông tin bộ từ vựng (Bắt buộc có setId, không có BottomBar)
        composable(
            route = Screen.EditVocabularySet.route + "/{setId}",
            arguments = listOf(navArgument("setId") { type = NavType.IntType })
        ) { backStackEntry ->
            val setId = backStackEntry.arguments?.getInt("setId") ?: 0
            EditVocabularySetScreen(
                navController = navController,
                setId = setId
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
        composable(
            route = "${Screen.NewWordsPreview.route}/{setId}",
            arguments = listOf(navArgument("setId") { type = NavType.IntType })
        ) { backStackEntry ->
            // Lấy setId từ đường dẫn xuống
            val setId = backStackEntry.arguments?.getInt("setId") ?: 0

            NewWordsPreviewScreen(
                navController = navController,
                setId = setId // Đã truyền đủ tham số!
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
        composable(
            route = "edit_word/{wordId}",
            arguments = listOf(navArgument("wordId") { type = NavType.IntType })
        ) { backStackEntry ->
            val wordId = backStackEntry.arguments?.getInt("wordId") ?: 0
            EditVocabularyScreen(navController = navController, wordId = wordId)
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

