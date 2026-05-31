package com.example.mini_game;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class QuizActivityEnhanced extends AppCompatActivity {
    private TextView questionText, scoreText, categoryText;
    private Button[] optionButtons = new Button[4];
    private int currentQuestion = 0;
    private int score = 0;
    private List<QuizQuestion> questions = new ArrayList<>();
    private String currentCategory = "General";
    private SharedPreferences prefs;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        prefs = getSharedPreferences("QuizScores", MODE_PRIVATE);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);
        
        categoryText = new TextView(this);
        categoryText.setTextSize(16);
        categoryText.setPadding(0, 0, 0, 10);
        layout.addView(categoryText);
        
        scoreText = new TextView(this);
        scoreText.setTextSize(20);
        scoreText.setPadding(0, 0, 0, 20);
        layout.addView(scoreText);
        
        questionText = new TextView(this);
        questionText.setTextSize(24);
        questionText.setPadding(0, 0, 0, 30);
        layout.addView(questionText);
        
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new Button(this);
            optionButtons[i].setTextSize(18);
            optionButtons[i].setPadding(0, 20, 0, 20);
            final int index = i;
            optionButtons[i].setOnClickListener(v -> checkAnswer(index));
            layout.addView(optionButtons[i]);
        }
        
        Button leaderboardBtn = new Button(this);
        leaderboardBtn.setText("View Leaderboard");
        leaderboardBtn.setOnClickListener(v -> showLeaderboard());
        layout.addView(leaderboardBtn);
        
        setContentView(layout);
        loadQuestionsFromAPI();
    }
    
    private void loadQuestionsFromAPI() {
        new Thread(() -> {
            try {
                String url = "https://opentdb.com/api.php?amount=10&type=multiple";
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                JSONObject json = new JSONObject(response.toString());
                JSONArray results = json.getJSONArray("results");
                
                for (int i = 0; i < results.length(); i++) {
                    JSONObject q = results.getJSONObject(i);
                    String question = decodeHTML(q.getString("question"));
                    String correct = decodeHTML(q.getString("correct_answer"));
                    String category = q.getString("category");
                    
                    List<String> options = new ArrayList<>();
                    options.add(correct);
                    JSONArray incorrect = q.getJSONArray("incorrect_answers");
                    for (int j = 0; j < incorrect.length(); j++) {
                        options.add(decodeHTML(incorrect.getString(j)));
                    }
                    Collections.shuffle(options);
                    
                    questions.add(new QuizQuestion(question, options, correct, category));
                }
                
                runOnUiThread(this::showQuestion);
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Failed to load questions", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        }).start();
    }
    
    private String decodeHTML(String text) {
        return text.replace("&quot;", "\"")
                   .replace("&amp;", "&")
                   .replace("&lt;", "<")
                   .replace("&gt;", ">")
                   .replace("&#039;", "'");
    }
    
    private void showQuestion() {
        if (currentQuestion >= questions.size()) {
            saveScore();
            questionText.setText("Quiz Complete! Final Score: " + score + "/" + questions.size());
            for (Button btn : optionButtons) {
                btn.setEnabled(false);
            }
            return;
        }
        
        QuizQuestion q = questions.get(currentQuestion);
        currentCategory = q.category;
        questionText.setText(q.question);
        categoryText.setText("Category: " + q.category);
        scoreText.setText("Score: " + score + "/" + (currentQuestion + 1));
        
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(q.options.get(i));
            optionButtons[i].setEnabled(true);
            optionButtons[i].setBackgroundColor(0xFF2196F3);
        }
    }
    
    private void checkAnswer(int selected) {
        QuizQuestion q = questions.get(currentQuestion);
        String selectedAnswer = q.options.get(selected);
        
        if (selectedAnswer.equals(q.correctAnswer)) {
            score++;
            optionButtons[selected].setBackgroundColor(0xFF00FF00);
        } else {
            optionButtons[selected].setBackgroundColor(0xFFFF0000);
            for (int i = 0; i < q.options.size(); i++) {
                if (q.options.get(i).equals(q.correctAnswer)) {
                    optionButtons[i].setBackgroundColor(0xFF00FF00);
                }
            }
        }
        
        for (Button btn : optionButtons) {
            btn.setEnabled(false);
        }
        
        new android.os.Handler().postDelayed(() -> {
            currentQuestion++;
            showQuestion();
        }, 1500);
    }
    
    private void saveScore() {
        SharedPreferences.Editor editor = prefs.edit();
        int bestScore = prefs.getInt("bestScore", 0);
        if (score > bestScore) {
            editor.putInt("bestScore", score);
        }
        editor.putInt("lastScore", score);
        editor.apply();
    }
    
    private void showLeaderboard() {
        int bestScore = prefs.getInt("bestScore", 0);
        int lastScore = prefs.getInt("lastScore", 0);
        Toast.makeText(this, "Best: " + bestScore + " | Last: " + lastScore, Toast.LENGTH_LONG).show();
    }
    
    static class QuizQuestion {
        String question;
        List<String> options;
        String correctAnswer;
        String category;
        
        QuizQuestion(String question, List<String> options, String correctAnswer, String category) {
            this.question = question;
            this.options = options;
            this.correctAnswer = correctAnswer;
            this.category = category;
        }
    }
}