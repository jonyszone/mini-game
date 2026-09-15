package com.example.mini_game.presentation.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun QuizScreen(viewModel: QuizViewModel, onFinished: (Long) -> Unit) {
    val state by viewModel.state.collectAsState()
    val quiz = state.quiz ?: return Text("Loading quiz...", Modifier.padding(20.dp))
    val question = quiz.questions[state.index]

    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Question ${state.index + 1} of ${quiz.questions.size}", style = MaterialTheme.typography.labelLarge)
        LinearProgressIndicator(progress = (state.index + 1).toFloat() / quiz.questions.size, modifier = Modifier.fillMaxWidth())
        Text(question.questionText, style = MaterialTheme.typography.headlineSmall)
        question.options.forEachIndexed { index, option ->
            val selected = state.selected
            val color = when {
                selected == null -> Color.Transparent
                index == question.correctIndex -> Color(0xFFD6F5DD)
                index == selected -> Color(0xFFFFDAD6)
                else -> Color.Transparent
            }
            OutlinedButton(
                onClick = { if (selected == null) viewModel.select(index) },
                modifier = Modifier.fillMaxWidth().background(color)
            ) { Text(option) }
        }
        state.selected?.let {
            Text(question.explanation, style = MaterialTheme.typography.bodyLarge)
            Button(onClick = { viewModel.next(onFinished) }, modifier = Modifier.fillMaxWidth()) {
                Text(if (state.index == quiz.questions.lastIndex) "See results" else "Next")
            }
        }
    }
}
