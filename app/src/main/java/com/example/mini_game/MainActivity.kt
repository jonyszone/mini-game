package com.example.mini_game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mini_game.presentation.history.HistoryScreen
import com.example.mini_game.presentation.home.HomeScreen
import com.example.mini_game.presentation.quiz.QuizScreen
import com.example.mini_game.presentation.results.ResultsScreen
import com.example.mini_game.presentation.theme.BrainSparkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { BrainSparkAppRoot() }
    }
}

@Composable
fun BrainSparkAppRoot() {
    val navController = rememberNavController()
    BrainSparkTheme {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeScreen(
                    viewModel = hiltViewModel(),
                    onQuizReady = { navController.navigate("quiz/$it") },
                    onHistory = { navController.navigate("history") }
                )
            }
            composable(
                route = "quiz/{quizId}",
                arguments = listOf(navArgument("quizId") { type = NavType.LongType })
            ) {
                QuizScreen(
                    viewModel = hiltViewModel(),
                    onFinished = { navController.navigate("results/$it") { popUpTo("home") } }
                )
            }
            composable(
                route = "results/{quizId}",
                arguments = listOf(navArgument("quizId") { type = NavType.LongType })
            ) {
                ResultsScreen(
                    viewModel = hiltViewModel(),
                    onNewTopic = { navController.navigate("home") { popUpTo("home") { inclusive = true } } },
                    onRetry = { navController.navigate("home") { popUpTo("home") { inclusive = true } } }
                )
            }
            composable("history") {
                HistoryScreen(
                    viewModel = hiltViewModel(),
                    onOpen = { navController.navigate("results/$it") },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
