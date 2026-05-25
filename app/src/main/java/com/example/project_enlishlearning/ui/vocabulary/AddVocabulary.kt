package com.example.project_enlishlearning.ui.vocabulary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val Primary = Color(0xFF4F46E5)

@Composable
fun AddVocabularyScreen() {

    var word by remember { mutableStateOf("") }
    var pronunciation by remember { mutableStateOf("") }
    var meaning by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var example by remember { mutableStateOf("") }
    var collocation by remember { mutableStateOf("") }
    var relatedWords by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFEDE9FE),
                        Color.White
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Add Vocabulary",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Add detailed vocabulary information for learning.",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(28.dp))

        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                VocabularyField(
                    value = word,
                    onValueChange = { word = it },
                    label = "Word",
                    icon = Icons.Default.Language
                )

                VocabularyField(
                    value = pronunciation,
                    onValueChange = { pronunciation = it },
                    label = "Pronunciation",
                    icon = Icons.Default.RecordVoiceOver
                )

                VocabularyField(
                    value = meaning,
                    onValueChange = { meaning = it },
                    label = "Meaning",
                    icon = Icons.Default.Description
                )

                VocabularyField(
                    value = description,
                    onValueChange = { description = it },
                    label = "Description (English)",
                    icon = Icons.Default.Description,
                    lines = 4
                )

                VocabularyField(
                    value = example,
                    onValueChange = { example = it },
                    label = "Example",
                    icon = Icons.Default.Description,
                    lines = 3
                )

                VocabularyField(
                    value = collocation,
                    onValueChange = { collocation = it },
                    label = "Collocation",
                    icon = Icons.Default.Tag
                )

                VocabularyField(
                    value = relatedWords,
                    onValueChange = { relatedWords = it },
                    label = "Related Words",
                    icon = Icons.Default.Language
                )

                VocabularyField(
                    value = note,
                    onValueChange = { note = it },
                    label = "Note",
                    icon = Icons.Default.Note,
                    lines = 4
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary
                    )
                ) {

                    Text("Save Vocabulary")
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun VocabularyField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    lines: Int = 1
) {

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label)
        },
        leadingIcon = {
            androidx.compose.material3.Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        minLines = lines,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Primary
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddVocabularyPreview() {

    MaterialTheme {
        AddVocabularyScreen()
    }
}