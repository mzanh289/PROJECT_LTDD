package com.example.project_enlishlearning.ui.vocabulary

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_enlishlearning.ui.components.AppCard
import com.example.project_enlishlearning.ui.components.AppGradientBackground
import com.example.project_enlishlearning.ui.components.AppTagChip
import com.example.project_enlishlearning.ui.components.AppTextField
import com.example.project_enlishlearning.ui.components.AppToolbar
import com.example.project_enlishlearning.ui.components.BottomNavItem
import com.example.project_enlishlearning.ui.components.BottomNavigationBar
import com.example.project_enlishlearning.ui.components.PrimaryButton
import com.example.project_enlishlearning.ui.theme.AppDimens
import com.example.project_enlishlearning.ui.theme.ProjectEnlishLearningTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.project_enlishlearning.navigation.Screen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_enlishlearning.viewmodel.VocabularyViewModel
import com.example.project_enlishlearning.data.local.entity.VocabularySetEntity
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VocabularySetListScreen(
    navController: NavController,
    selected: BottomNavItem = BottomNavItem.Vocabulary,
    onBottomItemSelected: (BottomNavItem) -> Unit = {},
    viewModel: VocabularyViewModel = viewModel<VocabularyViewModel>()
) {
    var search by remember { mutableStateOf("") }
    val vocabularySets by viewModel.vocabularySets.collectAsState()


    Scaffold(topBar = {
        AppToolbar(
            title = "Vocabulary Sets",
            subtitle = "Manage and review your vocabulary collections.",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            onNavigationClick = { navController.popBackStack() })
    }, bottomBar = {
        BottomNavigationBar(
            selected = selected, onItemSelected = onBottomItemSelected
        )
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                navController.navigate(Screen.CreateSet.route)
            }, containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White
            )
        }
    }) { innerPadding ->
        AppGradientBackground(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = AppDimens.ScreenPadding,
                    end = AppDimens.ScreenPadding,
                    top = 12.dp,
                    bottom = AppDimens.BottomBarPadding
                ), verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    AppTextField(
                        value = search,
                        onValueChange = { search = it },
                        label = "Search",
                        placeholder = "Search vocabulary set...",
                        leadingIcon = Icons.Default.Search
                    )
                }

                items(vocabularySets.filter { it.title.contains(search, true) }) { item ->
                    VocabularySetCard(item, navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun VocabularySetCard(
    item: VocabularySetEntity, navController: NavController
) {
    AppCard(
        modifier = Modifier.fillMaxWidth().clickable {
            navController.navigate("${Screen.VocabularySetDetail.route}/${item.setId}")
        }) {
        Column(modifier = Modifier.padding(AppDimens.CardPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(52.dp).background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), CircleShape
                        ), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${item.totalWords} words",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                var expanded by remember { mutableStateOf(false) }

                Box {
                    IconButton(
                        onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert, contentDescription = null
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("View set") },
                            onClick = {
                                expanded = false
                                // Sửa lại: Truyền đúng setId vào route
                                navController.navigate("${Screen.VocabularySetDetail.route}/${item.setId}")
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Edit Set") },
                            onClick = {
                                expanded = false
                                // Sửa lại: Truyền đúng setId vào route
                                navController.navigate("${Screen.EditVocabularySet.route}/${item.setId}")
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Delete Set") },
                            onClick = {
                                expanded = false
                                // Ở đây bạn có thể gọi hàm xóa từ ViewModel
                                //viewModel.deleteVocabularySet(item)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Learning Progress",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { item.progress / 100f },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${item.progress}% completed",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                text = "Start Learning", onClick = {
                    navController.navigate(Screen.NewWordsPreview.route)
                }, modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VocabularySetListPreview() {
    ProjectEnlishLearningTheme {
        VocabularySetListScreen(navController = rememberNavController())
    }
}