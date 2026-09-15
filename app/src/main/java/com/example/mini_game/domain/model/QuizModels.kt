package com.example.mini_game.domain.model

data class Quiz(
    val id: Long = 0,
    val topic: String,
    val difficulty: Difficulty,
    val questionCount: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val score: Int? = null,
    val questions: List<Question>
)

data class Question(
    val id: Long = 0,
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val userSelectedIndex: Int? = null
)

enum class Difficulty {
    Easy,
    Medium,
    Hard;

    fun next(): Difficulty = when (this) {
        Easy -> Medium
        Medium -> Hard
        Hard -> Hard
    }
}

data class QuizHistoryItem(
    val id: Long,
    val topic: String,
    val difficulty: Difficulty,
    val createdAt: Long,
    val score: Int?,
    val totalQuestions: Int
)
