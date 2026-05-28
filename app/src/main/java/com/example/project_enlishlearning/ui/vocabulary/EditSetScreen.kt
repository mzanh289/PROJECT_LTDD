package com.example.project_enlishlearning.ui.vocabulary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_enlishlearning.ui.components.*
import com.example.project_enlishlearning.ui.theme.AppDimens

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditVocabularySetScreen(
    navController: NavController,
    selected: BottomNavItem = BottomNavItem.Vocabulary,
    onBottomItemSelected: (BottomNavItem) -> Unit = {},
    setId: String = "",
    onSave: () -> Unit = {}
) {

    // 🔥 mock data (sau này replace bằng Room/ViewModel)
    var setName by remember {
        mutableStateOf("IELTS Academic Vocabulary")
    }

    var description by remember {
        mutableStateOf("Common academic vocabulary for IELTS Reading and Writing.")
    }

    val tags = listOf("IELTS", "Business", "Travel", "TOEIC", "Academic", "Daily Life")
    val selectedTags = remember {
        mutableStateListOf("IELTS", "Academic")
    }

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Edit Vocabulary Set",
                subtitle = "Update your vocabulary collection.",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selected = selected,
                onItemSelected = onBottomItemSelected
            )
        }
    ) { innerPadding ->

        AppGradientBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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

                        Column(
                            modifier = Modifier.padding(AppDimens.CardPadding)
                        ) {

                            AppTextField(
                                value = setName,
                                onValueChange = { setName = it },
                                label = "Vocabulary Set Name",
                                leadingIcon = Icons.Default.Book
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            AppTextField(
                                value = description,
                                onValueChange = { description = it },
                                label = "Description",
                                leadingIcon = Icons.Default.Description,
                                minLines = 4,
                                singleLine = false
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = "Tags",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {

                                tags.forEach { tag ->

                                    FilterChip(
                                        selected = selectedTags.contains(tag),
                                        onClick = {
                                            if (selectedTags.contains(tag)) {
                                                selectedTags.remove(tag)
                                            } else {
                                                selectedTags.add(tag)
                                            }
                                        },
                                        label = { Text(tag) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(22.dp))

                            PrimaryButton(
                                text = "Save Changes",
                                onClick = {
                                    // TODO: update Room DB
                                    onSave()
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