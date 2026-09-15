package com.example.mini_game.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "quizzes", indices = [Index(value = ["cacheKey"], unique = true)])
data class QuizEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cacheKey: String,
    val topic: String,
    val difficulty: String,
    val questionCount: Int,
    val createdAt: Long,
    val score: Int?,
    val totalQuestions: Int
)

@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(
            entity = QuizEntity::class,
            parentColumns = ["id"],
            childColumns = ["quizId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("quizId")]
)
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val quizId: Long,
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val userSelectedIndex: Int?
)

data class QuizWithQuestions(
    @androidx.room.Embedded val quiz: QuizEntity,
    @androidx.room.Relation(parentColumn = "id", entityColumn = "quizId")
    val questions: List<QuestionEntity>
)
