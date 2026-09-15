package com.example.mini_game.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mini_game.domain.model.Difficulty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, onQuizReady: (Long) -> Unit, onHistory: () -> Unit) {
    val state by viewModel.state.collectAsState()
    val messages = listOf("Igniting curiosity", "Writing clever distractors", "Checking the answer key", "Polishing explanations")

    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("BrainSpark", style = MaterialTheme.typography.headlineMedium)
            IconButton(onClick = onHistory) { Icon(Icons.Default.History, contentDescription = "History") }
        }
        OutlinedTextField(
            value = state.topic,
            onValueChange = viewModel::setTopic,
            label = { Text("What should we quiz you on?") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(viewModel.suggestions.size) { index ->
                OutlinedButton(onClick = { viewModel.setTopic(viewModel.suggestions[index]) }) {
                    Text(viewModel.suggestions[index])
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Difficulty.values().forEach { difficulty ->
                val selected = state.difficulty == difficulty
                if (selected) {
                    Button(onClick = { viewModel.setDifficulty(difficulty) }) { Text(difficulty.name) }
                } else {
                    OutlinedButton(onClick = { viewModel.setDifficulty(difficulty) }) { Text(difficulty.name) }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(5, 10, 15).forEach { count ->
                val selected = state.count == count
                if (selected) {
                    Button(onClick = { viewModel.setCount(count) }) { Text("$count") }
                } else {
                    OutlinedButton(onClick = { viewModel.setCount(count) }) { Text("$count") }
                }
            }
        }
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Daily Challenge", style = MaterialTheme.typography.titleMedium)
                Text(viewModel.dailyTopic)
                Spacer(Modifier.height(8.dp))
                Button(onClick = { viewModel.startDaily(onQuizReady) }, enabled = !state.loading) { Text("Play today's quiz") }
            }
        }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button(
            onClick = { viewModel.generate(onQuizReady) },
            enabled = !state.loading && state.topic.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Text(if (state.loading) messages[state.statusIndex] else "Generate quiz")
        }
    }
}
