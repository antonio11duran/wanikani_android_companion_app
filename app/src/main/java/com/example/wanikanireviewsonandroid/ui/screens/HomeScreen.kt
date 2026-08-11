package com.example.wanikanireviewsonandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.unit.dp
import com.example.wanikanireviewsonandroid.ReviewStatus


@Composable
fun HomeScreen(
    waniUiState: WaniUiState,
    reviewIndex: Int,
    onSubmit: (String) -> Unit,
    modifier: Modifier,
    reviewType: ReviewType,
    contentPaddingValues: PaddingValues = PaddingValues(0.dp)
) {
    when (waniUiState) {
        is WaniUiState.Loading -> ReviewStatus("Loading...", modifier = modifier)
        is WaniUiState.Error -> ReviewStatus(waniUiState.message, modifier = modifier)
        is WaniUiState.Success -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxSize()
            ) {
                if (waniUiState.reviews.totalCount == 0) {
                    Text("All Caught Up!")
                } else {
                    val review = waniUiState.reviews.data.getOrNull(reviewIndex)
                    val subject =
                        waniUiState.subjects.data.find { it.id == review?.data?.subjectID }
                    ReviewStatus(
                        "${subject?.data?.characters}",
                        modifier = modifier
                    )
                    var userInput by remember { mutableStateOf("") }
                    val typeOfReview = subject?.objectType?.replaceFirstChar { it.uppercase() }
                    if (reviewType == ReviewType.MEANING) {
                        Text("$typeOfReview Meaning")
                    } else {
                        Text("$typeOfReview Reading")
                    }
                    OutlinedTextField(
                        value = userInput,
                        onValueChange = { userInput = it },
                        label = {
                            if (reviewType == ReviewType.MEANING) {
                                Text("Enter Meaning")
                            } else {
                                Text("Enter Reading")
                            }
                        },
                        keyboardOptions =
                            if (reviewType == ReviewType.READING) {
                                KeyboardOptions(
                                    hintLocales = LocaleList("ja"),
                                    imeAction = ImeAction.Done
                                )
                            } else {
                                KeyboardOptions(
                                    imeAction = ImeAction.Done
                                )
                            },
                        keyboardActions = KeyboardActions(
                            onDone = { onSubmit(userInput); userInput = "" }
                        )
                    )
                    ElevatedButton({ onSubmit(userInput); userInput = "" }) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}

