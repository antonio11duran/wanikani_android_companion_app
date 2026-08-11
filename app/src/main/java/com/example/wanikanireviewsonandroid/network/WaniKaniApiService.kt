package com.example.wanikanireviewsonandroid.network

import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

private const val BASE_URL = "https://api.wanikani.com/v2/"
val networkJson = Json {ignoreUnknownKeys = true}
private val retrofit = Retrofit.Builder()
    .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

// immediately_available_for_review is added to only pull immediate reviews
// check API docs for other query parameters
interface WaniKaniApiService {
    @GET("assignments?immediately_available_for_review")
    suspend fun getAssignments(
        @Header("Authorization") myKey: String,
    ): ReviewResponse
    @GET("subjects")
    suspend fun getSubjects(
        @Header("Authorization") myKey: String,
        @Query("ids") ids: String,
    ): SubjectResponse

    @POST(value = "reviews")
    suspend fun sendReview(
        @Header("Authorization") myKey: String,
        @Body completedReview: ResultParent
    )
}

object WaniApi {
    val retrofitService : WaniKaniApiService by lazy {
        retrofit.create(WaniKaniApiService::class.java)
    }
}
