package com.example.project_enlishlearning.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_enlishlearning.ui.components.*
import com.example.project_enlishlearning.ui.theme.AppDimens
import com.example.project_enlishlearning.ui.theme.ProjectEnlishLearningTheme

@Composable
fun EditProfileScreen(
    navController: NavController
) {
    var name by remember { mutableStateOf("Linh Nguyen") }
    var level by remember { mutableStateOf("B1") }
    var goal by remember { mutableStateOf("20 minutes/day") }

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Edit Profile",
                subtitle = "Update your personal information",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = { navController.popBackStack() }
            )
        }
    ) { padding ->

        AppGradientBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            LazyColumn(
                contentPadding = PaddingValues(
                    start = AppDimens.ScreenPadding,
                    end = AppDimens.ScreenPadding,
                    top = 12.dp,
                    bottom = AppDimens.SectionSpacing
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {

                    AppCard(modifier = Modifier.fillMaxWidth()) {

                        Column(modifier = Modifier.padding(AppDimens.CardPadding)) {

                            AppTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = "Full Name",
                                leadingIcon = Icons.Default.Person
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            AppTextField(
                                value = level,
                                onValueChange = { level = it },
                                label = "English Level",
                                leadingIcon = Icons.Default.School
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            AppTextField(
                                value = goal,
                                onValueChange = { goal = it },
                                label = "Daily Goal"
                            )

                            Spacer(modifier = Modifier.height(22.dp))

                            PrimaryButton(
                                text = "Save Changes",
                                onClick = {
                                    navController.popBackStack()
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditProfileScreenPreview() {
    ProjectEnlishLearningTheme {
        EditProfileScreen(
            navController = rememberNavController()
        )
    }
}