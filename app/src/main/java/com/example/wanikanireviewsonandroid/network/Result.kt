package com.example.wanikanireviewsonandroid.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultParent(
    val review: ReviewResult
) {
}

@Serializable
data class ReviewResult(
    @SerialName("assignment_id")
    val assignmentId: Int,
    @SerialName("incorrect_meaning_answers")
    var countMeaningIncorrect: Int = 0,
    @SerialName("incorrect_reading_answers")
    var countReadingIncorrect: Int = 0
)