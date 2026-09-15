package com.example.mini_game.data.repository

import com.example.mini_game.BuildConfig
import com.example.mini_game.data.local.QuestionEntity
import com.example.mini_game.data.local.QuizDao
import com.example.mini_game.data.local.QuizEntity
import com.example.mini_game.data.local.QuizWithQuestions
import com.example.mini_game.data.remote.AiQuizDto
import com.example.mini_game.data.remote.GeminiApi
import com.example.mini_game.data.remote.GeminiContent
import com.example.mini_game.data.remote.GeminiPart
import com.example.mini_game.data.remote.GeminiRequest
import com.example.mini_game.domain.model.Difficulty
import com.example.mini_game.domain.model.Question
import com.example.mini_game.domain.model.Quiz
import com.example.mini_game.domain.model.QuizHistoryItem
import com.example.mini_game.domain.repository.QuizRepository
import java.security.MessageDigest
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class QuizRepositoryImpl @Inject constructor(
    private val dao: QuizDao,
    private val api: GeminiApi
) : QuizRepository {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun generateQuiz(topic: String, difficulty: Difficulty, count: Int): Result<Quiz> = runCatching {
        val normalizedTopic = topic.trim().ifBlank { "general knowledge" }
        val cacheKey = cacheKey(normalizedTopic, difficulty, count)
        dao.findByCacheKey(cacheKey)?.let { return@runCatching it.toDomain() }

        val dto = if (BuildConfig.GEMINI_API_KEY.isBlank()) {
            sampleQuiz(normalizedTopic, difficulty, count)
        } else {
            requestQuiz(normalizedTopic, difficulty, count)
        }
        persist(cacheKey, dto, difficulty, count)
    }

    override fun getQuizHistory(): Flow<List<QuizHistoryItem>> = dao.history().map { rows ->
        rows.map {
            QuizHistoryItem(
                id = it.id,
                topic = it.topic,
                difficulty = Difficulty.valueOf(it.difficulty),
                createdAt = it.createdAt,
                score = it.score,
                totalQuestions = it.totalQuestions
            )
        }
    }

    override suspend fun getQuiz(quizId: Long): Quiz? = dao.getQuiz(quizId)?.toDomain()

    override suspend fun saveQuizResult(quiz: Quiz, answers: List<Int>): Quiz {
        val score = quiz.questions.zip(answers).count { (question, answer) -> question.correctIndex == answer }
        val row = QuizEntity(
            id = quiz.id,
            cacheKey = cacheKey(quiz.topic, quiz.difficulty, quiz.questionCount),
            topic = quiz.topic,
            difficulty = quiz.difficulty.name,
            questionCount = quiz.questionCount,
            createdAt = quiz.createdAt,
            score = score,
            totalQuestions = quiz.questions.size
        )
        dao.updateQuiz(row)
        quiz.questions.zip(answers).forEach { (question, answer) -> dao.updateAnswer(question.id, answer) }
        return quiz.copy(score = score, questions = quiz.questions.zip(answers).map { (q, a) -> q.copy(userSelectedIndex = a) })
    }

    private suspend fun requestQuiz(topic: String, difficulty: Difficulty, count: Int): AiQuizDto {
        var lastError: Throwable? = null
        repeat(2) {
            try {
                val text = api.generateContent(
                    apiKey = BuildConfig.GEMINI_API_KEY,
                    request = GeminiRequest(
                        contents = listOf(GeminiContent(listOf(GeminiPart(prompt(topic, difficulty, count)))))
                    )
                ).candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text.orEmpty()
                return json.decodeFromString(stripFences(text))
            } catch (error: Throwable) {
                lastError = error
            }
        }
        throw lastError ?: IllegalStateException("Gemini returned malformed quiz JSON.")
    }

    private suspend fun persist(cacheKey: String, dto: AiQuizDto, difficulty: Difficulty, count: Int): Quiz {
        val quizId = dao.insertQuiz(
            QuizEntity(
                cacheKey = cacheKey,
                topic = dto.topic,
                difficulty = difficulty.name,
                questionCount = count,
                createdAt = System.currentTimeMillis(),
                score = null,
                totalQuestions = dto.questions.size
            )
        )
        dao.insertQuestions(dto.questions.map {
            QuestionEntity(
                quizId = quizId,
                questionText = it.question,
                options = it.options.take(4),
                correctIndex = it.correctIndex.coerceIn(0, 3),
                explanation = it.explanation,
                userSelectedIndex = null
            )
        })
        return dao.getQuiz(quizId)!!.toDomain()
    }

    private fun QuizWithQuestions.toDomain(): Quiz = Quiz(
        id = quiz.id,
        topic = quiz.topic,
        difficulty = Difficulty.valueOf(quiz.difficulty),
        questionCount = quiz.questionCount,
        createdAt = quiz.createdAt,
        score = quiz.score,
        questions = questions.map {
            Question(
                id = it.id,
                questionText = it.questionText,
                options = it.options,
                correctIndex = it.correctIndex,
                explanation = it.explanation,
                userSelectedIndex = it.userSelectedIndex
            )
        }
    )

    private fun prompt(topic: String, difficulty: Difficulty, count: Int): String =
        """Generate a quiz on the topic "$topic" at ${difficulty.name} difficulty with exactly $count multiple-choice questions.
Return ONLY valid JSON matching this schema, no markdown, no commentary:
{"topic":"string","questions":[{"question":"string","options":["string","string","string","string"],"correctIndex":0,"explanation":"string"}]}"""

    private fun stripFences(value: String): String = value.trim()
        .removePrefix("```json")
        .removePrefix("```")
        .removeSuffix("```")
        .trim()

    private fun cacheKey(topic: String, difficulty: Difficulty, count: Int): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest("${topic.lowercase()}|$difficulty|$count".toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun sampleQuiz(topic: String, difficulty: Difficulty, count: Int): AiQuizDto = AiQuizDto(
        topic = topic,
        questions = (1..count).map {
            com.example.mini_game.data.remote.AiQuestionDto(
                question = "Which statement best matches $topic at ${difficulty.name} level? #$it",
                options = listOf("A core concept", "An unrelated detail", "A random guess", "None of these"),
                correctIndex = 0,
                explanation = "This MVP sample appears when GEMINI_API_KEY is not configured."
            )
        }
    )
}
