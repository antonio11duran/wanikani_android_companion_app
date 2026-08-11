package com.example.wanikanireviewsonandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wanikanireviewsonandroid.ui.theme.WaniKaniReviewsOnAndroidTheme
import com.example.wanikanireviewsonandroid.ui.screens.WaniViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wanikanireviewsonandroid.ui.screens.HomeScreen
import com.example.wanikanireviewsonandroid.ui.screens.WaniUiState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WaniKaniReviewsOnAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val waniViewModel = viewModel<WaniViewModel>()
                    HomeScreen(
                        waniUiState = waniViewModel.waniUiState,
                        reviewIndex = waniViewModel.reviewIndex,
                        onSubmit = { userInput ->
                            val state = waniViewModel.waniUiState
                            if (state is WaniUiState.Success) {
                                val review = state.reviews.data.getOrNull(waniViewModel.reviewIndex)
                                val subject = state.subjects.data.find { it.id == review?.data?.subjectID }
                                if (subject != null) {
                                    waniViewModel.answerValidation(userInput, subject.data, assignmentId = review?.id)
                                }
                            }
                                   },
                        reviewType = waniViewModel.reviewType,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewStatus(status: String, modifier: Modifier = Modifier) {
    Text(
        text = status,
        modifier = modifier,
        fontSize = 72.sp
    )
}

@Preview(showBackground = true)
@Composable
fun ResponsePreview() {
    WaniKaniReviewsOnAndroidTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            ReviewStatus(
                status = "Sample Status"
            )
        }
    }
}