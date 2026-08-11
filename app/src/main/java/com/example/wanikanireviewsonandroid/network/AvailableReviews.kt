package com.example.wanikanireviewsonandroid.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ReviewResponse(
    @SerialName("total_count")
    val totalCount: Int,
    val data: List<ReviewEntry>
)

@Serializable
data class ReviewEntry(
    val id: Int,
    val data: ReviewData
    )

@Serializable
data class ReviewData(
    @SerialName("subject_id")
    val subjectID: Int,
    @SerialName("subject_type")
    val subjectType: String,
    @SerialName("srs_stage")
    val srsStage: Int,
    @SerialName("available_at")
    val availableAt: String?,
    @SerialName("burned_at")
    val burnedAt: String?,
    @SerialName("passed_at")
    val passedAt: String?,
    @SerialName("resurrected_at")
    val resurrectedAt: String?,
    val hidden: Boolean
)