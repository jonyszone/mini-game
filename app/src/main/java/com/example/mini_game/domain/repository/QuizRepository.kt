package com.example.mini_game.domain.repository

import com.example.mini_game.domain.model.Difficulty
import com.example.mini_game.domain.model.Quiz
import com.example.mini_game.domain.model.QuizHistoryItem
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    suspend fun generateQuiz(topic: String, difficulty: Difficulty, count: Int): Result<Quiz>
    fun getQuizHistory(): Flow<List<QuizHistoryItem>>
    suspend fun getQuiz(quizId: Long): Quiz?
    suspend fun saveQuizResult(quiz: Quiz, answers: List<Int>): Quiz
}
