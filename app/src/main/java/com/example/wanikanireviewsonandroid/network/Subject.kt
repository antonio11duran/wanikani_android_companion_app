package com.example.wanikanireviewsonandroid.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SubjectResponse(
    @SerialName("total_count")
    val totalCount: Int,
    val data: List<SubjectEntry>
)

@Serializable
data class SubjectEntry(
    val id: Int,
    val data: SubjectData,
    @SerialName("object")
    val objectType: String
)

@Serializable
data class SubjectData(
    val characters: String?,
    val meanings: List<Meaning>,
    val readings: List<Reading> = emptyList(),
    @SerialName("meaning_mnemonic")
    val meaningMnemonic: String,
    @SerialName("meaning_hint")
    val meaningHint: String? = "No Hint Available",
    @SerialName("reading_mnemonic")
    val readingMnemonic: String? = "No Reading Mnemonic",
    @SerialName("reading_hint")
    val readingHint: String? = "No Hint Available"
)

@Serializable
data class Meaning(
    val meaning: String,
    val primary: Boolean,
    @SerialName("accepted_answer")
    val acceptedAnswer: Boolean,
)

@Serializable
data class Reading(
    val reading: String,
    val primary: Boolean,
    @SerialName("accepted_answer")
    val acceptedAnswer: Boolean,
    val type: String? = "N/A"
)