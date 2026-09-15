package com.example.mini_game.presentation.history

import androidx.lifecycle.ViewModel
import com.example.mini_game.domain.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(repository: QuizRepository) : ViewModel() {
    val history = repository.getQuizHistory()
}
