package com.example.mini_game.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mini_game.domain.model.Difficulty
import com.example.mini_game.domain.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val topic: String = "",
    val difficulty: Difficulty = Difficulty.Easy,
    val count: Int = 5,
    val loading: Boolean = false,
    val statusIndex: Int = 0,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: QuizRepository) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state
    val suggestions = listOf("Space travel", "World history", "Kotlin", "Nutrition", "Bangladesh", "Music theory")
    private val dailyTopics = listOf("Ocean life", "Ancient inventions", "Climate science", "Cricket", "Human brain", "Architecture")
    val dailyTopic: String = dailyTopics[LocalDate.now().dayOfYear % dailyTopics.size]

    fun setTopic(value: String) = _state.update { it.copy(topic = value, error = null) }
    fun setDifficulty(value: Difficulty) = _state.update { it.copy(difficulty = value) }
    fun setCount(value: Int) = _state.update { it.copy(count = value) }

    fun generate(onReady: (Long) -> Unit) {
        val current = state.value
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null, statusIndex = (it.statusIndex + 1) % 4) }
            repository.generateQuiz(current.topic, current.difficulty, current.count)
                .onSuccess { onReady(it.id) }
                .onFailure { error -> _state.update { it.copy(error = error.message ?: "Could not generate quiz.") } }
            _state.update { it.copy(loading = false) }
        }
    }

    fun startDaily(onReady: (Long) -> Unit) {
        _state.update { it.copy(topic = dailyTopic, difficulty = Difficulty.Medium, count = 5) }
        generate(onReady)
    }
}
