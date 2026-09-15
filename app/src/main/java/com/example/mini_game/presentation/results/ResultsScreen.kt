package com.example.mini_game.presentation.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(viewModel: ResultsViewModel, onNewTopic: () -> Unit, onRetry: () -> Unit) {
    val quiz by viewModel.quiz.collectAsState()
    val value = quiz ?: return Text("Loading results...", Modifier.padding(20.dp))
    val score = value.score ?: value.questions.count { it.userSelectedIndex == it.correctIndex }
    val percent = if (value.questions.isEmpty()) 0 else score * 100 / value.questions.size
    val label = when {
        percent >= 90 -> "Spark master"
        percent >= 70 -> "Strong spark"
        percent >= 50 -> "Warming up"
        else -> "Keep sparking"
    }
    val missed = value.questions.filter { it.userSelectedIndex != null && it.userSelectedIndex != it.correctIndex }

    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(Modifier.fillMaxWidth().graphicsLayer()) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(value.topic, style = MaterialTheme.typography.headlineSmall)
                Text("$score/${value.questions.size} • $percent%", style = MaterialTheme.typography.displaySmall)
                Text(label, style = MaterialTheme.typography.titleMedium)
            }
        }
        Text("Strengths: ${if (score > 0) "You handled ${value.difficulty.name.lowercase()} ${value.topic} questions well." else "Start with Easy and build momentum."}")
        Text("Weak spots: ${if (missed.isEmpty()) "No missed questions." else missed.take(2).joinToString { it.questionText.take(48) }}")
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = onRetry, modifier = Modifier.weight(1f)) { Text("Retry harder") }
            Button(onClick = onNewTopic, modifier = Modifier.weight(1f)) { Text("New topic") }
        }
        OutlinedButton(onClick = { }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Share, contentDescription = null)
            Text("Share score card")
        }
    }
}
