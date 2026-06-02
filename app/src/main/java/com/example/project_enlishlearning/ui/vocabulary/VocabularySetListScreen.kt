package com.example.project_enlishlearning.ui.vocabulary

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_enlishlearning.data.local.entity.VocabularySetEntity
import com.example.project_enlishlearning.navigation.Screen
import com.example.project_enlishlearning.ui.components.AppCard
import com.example.project_enlishlearning.ui.components.AppGradientBackground
import com.example.project_enlishlearning.ui.components.AppTextField
import com.example.project_enlishlearning.ui.components.AppToolbar
import com.example.project_enlishlearning.ui.components.BottomNavItem
import com.example.project_enlishlearning.ui.components.BottomNavigationBar
import com.example.project_enlishlearning.ui.components.PrimaryButton
import com.example.project_enlishlearning.ui.theme.AppDimens
import com.example.project_enlishlearning.ui.theme.ProjectEnlishLearningTheme
import com.example.project_enlishlearning.utils.constants.SET_ACTION_RESULT
import com.example.project_enlishlearning.utils.constants.SetAction
import com.example.project_enlishlearning.viewmodel.VocabularyViewModel
import kotlinx.coroutines.launch

@Composable
fun VocabularySetListScreen(
    navController: NavController,
    selected: BottomNavItem = BottomNavItem.Vocabulary,
    onBottomItemSelected: (BottomNavItem) -> Unit = {},
    viewModel: VocabularyViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    var search by remember { mutableStateOf("") }
    val vocabularySets by viewModel.vocabularySets.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val result = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<SetAction>(SET_ACTION_RESULT)

        when (result) {
            SetAction.CREATED ->
                snackbarHostState.showSnackbar(
                    "Vocabulary set created successfully"
                )

            SetAction.UPDATED ->
                snackbarHostState.showSnackbar(
                    "Vocabulary set updated successfully"
                )

            else -> {}
        }

        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.remove<SetAction>(SET_ACTION_RESULT)
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            AppToolbar(
                title = "Vocabulary Sets",
                subtitle = "Manage and review your vocabulary collections.",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selected = selected,
                onItemSelected = onBottomItemSelected
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.CreateSet.route)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        val filteredSets = vocabularySets.filter { it.title.contains(search, ignoreCase = true) }

        AppGradientBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = AppDimens.ScreenPadding,
                    end = AppDimens.ScreenPadding,
                    top = 12.dp,
                    bottom = AppDimens.BottomBarPadding
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    AppCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AppTextField(
                            value = search,
                            onValueChange = { search = it },
                            label = "Search",
                            placeholder = "Search vocabulary set...",
                            leadingIcon = Icons.Default.Search,
                            modifier = Modifier.padding(AppDimens.CardPadding)
                        )
                    }
                }

                if (filteredSets.isEmpty()) {
                    item {
                        EmptySetListView(
                            onCreateSet = {
                                navController.navigate(Screen.CreateSet.route)
                            }
                        )
                    }
                } else {
                    items(filteredSets, key = { it.setId }) { item ->
                        VocabularySetCard(
                            item = item,
                            navController = navController,
                            onDeleteClick = {
                                viewModel.deleteVocabularySet(item)
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Vocabulary set deleted successfully"
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptySetListView(
    onCreateSet: () -> Unit
) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimens.CardPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Folder,
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("No vocabulary sets")

            Spacer(modifier = Modifier.height(8.dp))

            PrimaryButton(
                text = "Create first set",
                onClick = onCreateSet,
                leadingIcon = Icons.Default.Add
            )
        }
    }
}

@Composable
private fun VocabularySetCard(
    item: VocabularySetEntity,
    navController: NavController,
    onDeleteClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }

    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("${Screen.VocabularySetDetail.route}/${item.setId}")
            }
    ) {
        Column(modifier = Modifier.padding(AppDimens.CardPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
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

                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null
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
                                navController.navigate("${Screen.VocabularySetDetail.route}/${item.setId}")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Edit set") },
                            onClick = {
                                expanded = false
                                navController.navigate("${Screen.EditVocabularySet.route}/${item.setId}")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete set") },
                            onClick = {
                                expanded = false
                                confirmDelete = true
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

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Learning Progress",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { item.progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
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
                text = "Start Learning",
                onClick = {
                    navController.navigate("${Screen.NewWordsPreview.route}/${item.setId}")
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Delete set?") },
            text = { Text("This will remove the vocabulary set and all of its words.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmDelete = false
                        onDeleteClick()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VocabularySetListPreview() {
    ProjectEnlishLearningTheme {
        VocabularySetListScreen(navController = rememberNavController())
    }
}
