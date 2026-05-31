package com.example.mini_game;

import androidx.appcompat.app.AppCompatActivity;
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

public class QuizActivity extends AppCompatActivity {
    private TextView questionText, scoreText;
    private Button[] optionButtons = new Button[4];
    private int currentQuestion = 0;
    private int score = 0;
    private List<QuizQuestion> questions = new ArrayList<>();
    private boolean loading = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);
        
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
        
        setContentView(layout);
        
        // Load questions from API
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
                    
                    List<String> options = new ArrayList<>();
                    options.add(correct);
                    JSONArray incorrect = q.getJSONArray("incorrect_answers");
                    for (int j = 0; j < incorrect.length(); j++) {
                        options.add(decodeHTML(incorrect.getString(j)));
                    }
                    Collections.shuffle(options);
                    
                    questions.add(new QuizQuestion(question, options, correct));
                }
                
                loading = false;
                runOnUiThread(this::showQuestion);
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Failed to load questions. Using local questions.", Toast.LENGTH_SHORT).show();
                    loadLocalQuestions();
                });
            }
        }).start();
    }
    
    private void loadLocalQuestions() {
        questions.add(new QuizQuestion("What is 2 + 2?", 
            new ArrayList<String>() {{ add("4"); add("3"); add("5"); add("6"); }}, "4"));
        questions.add(new QuizQuestion("What is 10 * 5?", 
            new ArrayList<String>() {{ add("50"); add("40"); add("60"); add("70"); }}, "50"));
        questions.add(new QuizQuestion("What is 100 / 4?", 
            new ArrayList<String>() {{ add("25"); add("20"); add("30"); add("35"); }}, "25"));
        
        loading = false;
        showQuestion();
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
            questionText.setText("Quiz Complete! Final Score: " + score + "/" + questions.size());
            for (Button btn : optionButtons) {
                btn.setEnabled(false);
            }
            return;
        }
        
        QuizQuestion q = questions.get(currentQuestion);
        questionText.setText(q.question);
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
    
    static class QuizQuestion {
        String question;
        List<String> options;
        String correctAnswer;
        
        QuizQuestion(String question, List<String> options, String correctAnswer) {
            this.question = question;
            this.options = options;
            this.correctAnswer = correctAnswer;
        }
    }
}