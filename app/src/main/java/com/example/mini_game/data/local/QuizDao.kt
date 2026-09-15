package com.example.mini_game.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Transaction
    @Query("SELECT * FROM quizzes WHERE cacheKey = :cacheKey LIMIT 1")
    suspend fun findByCacheKey(cacheKey: String): QuizWithQuestions?

    @Transaction
    @Query("SELECT * FROM quizzes WHERE id = :quizId LIMIT 1")
    suspend fun getQuiz(quizId: Long): QuizWithQuestions?

    @Query("SELECT * FROM quizzes ORDER BY createdAt DESC")
    fun history(): Flow<List<QuizEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: QuizEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Update
    suspend fun updateQuiz(quiz: QuizEntity)

    @Query("UPDATE questions SET userSelectedIndex = :answer WHERE id = :questionId")
    suspend fun updateAnswer(questionId: Long, answer: Int)
}
