package com.example.wanikanireviewsonandroid.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanikanireviewsonandroid.BuildConfig
import com.example.wanikanireviewsonandroid.network.ReviewResponse
import com.example.wanikanireviewsonandroid.network.ReviewResult
import com.example.wanikanireviewsonandroid.network.SubjectData
import com.example.wanikanireviewsonandroid.network.SubjectResponse
import com.example.wanikanireviewsonandroid.network.WaniApi
import kotlinx.coroutines.launch

const val myKey = BuildConfig.apiKey

sealed interface WaniUiState {
    data class Success(val reviews: ReviewResponse, val subjects: SubjectResponse) : WaniUiState
    data class Error(val message: String): WaniUiState
    object Loading: WaniUiState
}

enum class ReviewType {
    MEANING,
    READING
}

data class PendingReview(
    val assignmentId: Int,
    var meaningIncorrect: Int = 0,
    var readingIncorrect: Int = 0
)

class WaniViewModel : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var waniUiState: WaniUiState by mutableStateOf(WaniUiState.Loading)
        private set

    var reviewIndex: Int by mutableStateOf(0)
        private set

    var reviewType: ReviewType by mutableStateOf(ReviewType.MEANING)
        private set

    val pendingReviews = mutableMapOf<Int, PendingReview>()

    /**
     * Call getWaniKani() on init so we can display status immediately.
     */
    init {
        getWaniKani()
    }

    fun getWaniKani() {
        viewModelScope.launch {
            waniUiState = try {
                val listResult = WaniApi.retrofitService.getAssignments("Bearer $myKey")
                val subjectIds: List<Int> = listResult.data.map { it.data.subjectID }
                val subjectResults = WaniApi.retrofitService.getSubjects("Bearer $myKey", subjectIds.joinToString(","))
                WaniUiState.Success(listResult, subjectResults)
            } catch (e: Exception) {
                WaniUiState.Error(e.toString())
            }

        }
    }

    fun incrementIndex() {
        val state = waniUiState
        if (state is WaniUiState.Success) {
            if (reviewIndex < state.reviews.totalCount - 1) {
                reviewIndex++
            }
        }
    }

    fun answerValidation(userInput: String, currentSubject: SubjectData, assignmentId: Int) {
        val meanings: List<String> = currentSubject.meanings.filter { it.acceptedAnswer }.map { it.meaning }.map { it.lowercase() }
        val readings: List<String> = currentSubject.readings.filter { it.acceptedAnswer }.map { it.reading }.map { it.lowercase() }
        val lowercaseInput = userInput.lowercase()
        when (reviewType) {
            ReviewType.MEANING -> {
                for (meaning in meanings) {
                    if (meaning == lowercaseInput) {
                        if (readings.isEmpty()) {
                            incrementIndex()
                        } else {
                            reviewType = ReviewType.READING
                        }
                        return
                    }
                }
                val entry = pendingReviews.getOrPut(assignmentId) { PendingReview(assignmentId) }
                entry.meaningIncorrect++
            }
            ReviewType.READING -> {
                for (reading in readings) {
                    if (reading == lowercaseInput) {
                        reviewType = ReviewType.MEANING
                        incrementIndex()
                        return
                    }
                }
                val entry = pendingReviews.getOrPut(assignmentId) { PendingReview(assignmentId) }
                entry.readingIncorrect++
            }
        }

    }
}