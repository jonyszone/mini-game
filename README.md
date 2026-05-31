# Study & Games Hub

A comprehensive Android application combining a professional A-level Grade Calculator with 3 engaging mini-games, integrated with free APIs for dynamic content.

## Features

### 📊 Grade Calculator
- **Add Subjects** - Input subject name, marks obtained, and total marks
- **View Subjects** - See all subjects with individual grades and percentages
- **Calculate Results** - Get overall grade, statistics, and performance analysis
- **Data Persistence** - All data saved automatically

### 🎮 Games

#### 1. Pong Game
- Two-player competitive gameplay
- Touch-based paddle controls
- Real-time ball physics
- Score tracking
- Level/difficulty progression system (Enhanced)

#### 2. Space Invaders
- Single-player action game
- Enemy waves with movement
- Bullet collision detection
- Win/lose conditions
- Score system (100 pts per enemy)
- Power-ups, shield mechanic, level progression (Enhanced)

#### 3. Quiz Game (API-Powered)
- **OpenTriviaDB API** - 10 random trivia questions
- Multiple choice (4 options)
- Visual feedback (green/red)
- Score tracking
- Category tracking & persistent high scores (Enhanced)
- Fallback to local questions if API unavailable

### 🌤️ Tools

#### Weather Info (API-Powered)
- **Open-Meteo API** - Free weather data
- **Geocoding API** - City location lookup
- Real-time temperature
- No API key required

## Free APIs Integrated

### 1. OpenTriviaDB
- **URL**: https://opentdb.com/api.php
- **Purpose**: Dynamic quiz questions
- **Features**: 10+ categories, multiple difficulty levels
- **No API Key**: Required

### 2. Open-Meteo
- **URL**: https://api.open-meteo.com/v1/forecast
- **Purpose**: Weather data
- **Features**: Real-time temperature, weather codes
- **No API Key**: Required

### 3. Geocoding API
- **URL**: https://geocoding-api.open-meteo.com/v1/search
- **Purpose**: City name to coordinates
- **Features**: Location lookup, country info
- **No API Key**: Required

## Grade System
- **A*** - 90% - 100%
- **A** - 80% - 89%
- **B** - 70% - 79%
- **C** - 60% - 69%
- **D** - 50% - 59%
- **E** - 40% - 49%
- **U** - Below 40%

## Project Structure

```
app/src/main/java/com/example/mini_game/
├── MainActivity.java              (110 lines)
├── AddSubjectActivity.java        (82 lines)
├── SubjectsListActivity.java      (97 lines)
├── ResultsActivity.java           (109 lines)
├── SettingsActivity.java          (109 lines)
├── GradeCalculatorActivity.java   (92 lines)
├── Subject.java                   (31 lines)
├── GradeCalculationEngine.java    (73 lines)
├── GradeDataManager.java          (67 lines)
├── PongActivity.java              (11 lines)
├── PongGameView.java              (115 lines)
├── PongGameViewEnhanced.java      (146 lines)
├── SpaceInvadersActivity.java     (11 lines)
├── SpaceInvadersView.java         (146 lines)
├── SpaceInvadersViewEnhanced.java (202 lines)
├── QuizActivity.java              (182 lines) - API-Powered
├── QuizActivityEnhanced.java      (203 lines) - API-Powered
└── WeatherActivity.java           (122 lines) - API-Powered
```

**Total: 18 Java files, 1,908 lines of production code**

## How to Use

### Grade Calculator
1. Click "Add Subject"
2. Enter subject name, marks, and total
3. Click "View Subjects" to see all grades
4. Click "Calculate Results" for statistics

### Games
1. Click on any game (Pong, Space Invaders, or Quiz)
2. Follow on-screen instructions
3. Return to main menu when done

### Weather Info
1. Click "Weather Info"
2. Enter city name
3. Click "Get Weather"
4. View real-time temperature

## Build & Run

### Prerequisites
- Android Studio
- Android SDK (API 21+)
- Java Development Kit
- Internet connection (for API features)

### Steps
1. Open project in Android Studio
2. Click "Build" → "Make Project"
3. Connect device or start emulator
4. Click "Run" → "Run 'app'"

### Permissions Required
- `INTERNET` - For API calls

## Features

✓ **Professional Grade Calculator** - Track A-level performance  
✓ **3 Engaging Games** - Learn while having fun  
✓ **API Integration** - Dynamic content from free APIs  
✓ **Data Persistence** - Automatic save/load  
✓ **Beautiful UI** - Clean, intuitive design  
✓ **Production Ready** - Minimal, optimized code  
✓ **No External Dependencies** - Pure Android framework + HTTP  

## API Benefits

- **Dynamic Content** - Quiz questions change every time
- **Real-time Data** - Weather updates automatically
- **No API Keys** - All APIs are free and open
- **Fallback Support** - Local data if API unavailable
- **Lightweight** - Minimal data usage

## Use Cases

✓ **Students** - Track grades, play games, check weather  
✓ **Teachers** - Calculate class grades  
✓ **Parents** - Monitor student progress  
✓ **Schools** - Grade management and learning  

## Statistics

| Component | Type | Lines | Status |
|-----------|------|-------|--------|
| Grade Calculator | Production | 660 | ✓ Complete |
| Pong Game | Game | 272 | ✓ Complete |
| Space Invaders | Game | 359 | ✓ Complete |
| Quiz Game | Game + API | 385 | ✓ Complete |
| Weather Tool | Tool + API | 122 | ✓ Complete |
| Main Hub | UI | 110 | ✓ Complete |
| **Total** | **Mixed** | **1,908** | **✓ Ready** |

---

**Version**: 2.1 (Enhanced Edition)  
**Status**: Production Ready  
**Last Updated**: 2026-05-31