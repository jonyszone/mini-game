package com.example.mini_game.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApi {
    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

@Serializable
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val generationConfig: GeminiGenerationConfig = GeminiGenerationConfig()
)

@Serializable
data class GeminiGenerationConfig(
    val temperature: Double = 0.7,
    val responseMimeType: String = "application/json"
)

@Serializable
data class GeminiContent(val parts: List<GeminiPart>)

@Serializable
data class GeminiPart(val text: String)

@Serializable
data class GeminiResponse(val candidates: List<GeminiCandidate> = emptyList())

@Serializable
data class GeminiCandidate(val content: GeminiContent? = null)

@Serializable
data class AiQuizDto(
    val topic: String,
    val questions: List<AiQuestionDto>
)

@Serializable
data class AiQuestionDto(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)
