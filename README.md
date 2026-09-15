# BrainSpark

BrainSpark is a native Android quiz app built with Kotlin, Jetpack Compose, Material 3, Hilt, Room, Retrofit, Coroutines, and Flow.

The MVP lets users type any topic, choose a difficulty and question count, generate an AI-powered multiple-choice quiz with Gemini, answer one question at a time with immediate feedback, review results, and revisit saved quiz history.

## MVP Features

- Custom topic quiz generation with Gemini structured JSON output
- Easy, Medium, and Hard difficulty selection
- 5, 10, and 15 question options
- Hardcoded topic suggestions and deterministic daily challenge
- Cache-first repository to avoid regenerating identical quizzes
- Room-backed quiz history and saved answers
- Compose quiz flow with correct/incorrect feedback and explanations
- Results summary with score, percentage, grade label, and share card stub
- PremiumGate composable stub for future monetization

## Configuration

Set a Gemini API key in Gradle properties:

```properties
GEMINI_API_KEY=your_key_here
```

Without an API key, BrainSpark uses deterministic sample quizzes so the app remains runnable during local development.

## Build

```bash
JAVA_HOME=/home/shafi/.jdks/jbr-17.0.14 PATH=/home/shafi/.jdks/jbr-17.0.14/bin:$PATH bash gradlew --no-daemon :app:assembleDebug --console=plain
```
