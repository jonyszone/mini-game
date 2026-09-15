package com.example.mini_game.presentation.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mini_game.domain.model.Quiz
import com.example.mini_game.domain.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuizUiState(val quiz: Quiz? = null, val index: Int = 0, val answers: List<Int> = emptyList(), val selected: Int? = null)

@HiltViewModel
class QuizViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: QuizRepository
) : ViewModel() {
    private val _state = MutableStateFlow(QuizUiState())
    val state: StateFlow<QuizUiState> = _state
    private val quizId: Long = savedStateHandle["quizId"] ?: 0

    init {
        viewModelScope.launch { _state.update { it.copy(quiz = repository.getQuiz(quizId)) } }
    }

    fun select(answer: Int) = _state.update { it.copy(selected = answer) }

    fun next(onFinished: (Long) -> Unit) {
        val current = state.value
        val quiz = current.quiz ?: return
        val selected = current.selected ?: return
        val answers = current.answers + selected
        if (answers.size == quiz.questions.size) {
            viewModelScope.launch {
                repository.saveQuizResult(quiz, answers)
                onFinished(quiz.id)
            }
        } else {
            _state.value = current.copy(index = current.index + 1, answers = answers, selected = null)
        }
    }
}
