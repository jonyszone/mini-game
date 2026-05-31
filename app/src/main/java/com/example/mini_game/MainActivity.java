package com.example.mini_game;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(20, 20, 20, 20);
        
        TextView title = new TextView(this);
        title.setText("Study & Games Hub");
        title.setTextSize(32);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setPadding(0, 0, 0, 20);
        mainLayout.addView(title);
        
        ScrollView scrollView = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        
        // Grade Calculator Section
        addSectionTitle(layout, "📊 Grade Calculator");
        
        Button addBtn = new Button(this);
        addBtn.setText("Add Subject");
        addBtn.setTextSize(16);
        addBtn.setPadding(0, 15, 0, 15);
        addBtn.setOnClickListener(v -> startActivity(new Intent(this, AddSubjectActivity.class)));
        layout.addView(addBtn);
        
        Button viewBtn = new Button(this);
        viewBtn.setText("View Subjects");
        viewBtn.setTextSize(16);
        viewBtn.setPadding(0, 15, 0, 15);
        viewBtn.setOnClickListener(v -> startActivity(new Intent(this, SubjectsListActivity.class)));
        layout.addView(viewBtn);
        
        Button resultsBtn = new Button(this);
        resultsBtn.setText("Calculate Results");
        resultsBtn.setTextSize(16);
        resultsBtn.setPadding(0, 15, 0, 15);
        resultsBtn.setOnClickListener(v -> startActivity(new Intent(this, ResultsActivity.class)));
        layout.addView(resultsBtn);
        
        // Games Section
        addSectionTitle(layout, "🎮 Games");
        
        Button pongBtn = new Button(this);
        pongBtn.setText("Pong Game");
        pongBtn.setTextSize(16);
        pongBtn.setPadding(0, 15, 0, 15);
        pongBtn.setOnClickListener(v -> startActivity(new Intent(this, PongActivity.class)));
        layout.addView(pongBtn);
        
        Button spaceBtn = new Button(this);
        spaceBtn.setText("Space Invaders");
        spaceBtn.setTextSize(16);
        spaceBtn.setPadding(0, 15, 0, 15);
        spaceBtn.setOnClickListener(v -> startActivity(new Intent(this, SpaceInvadersActivity.class)));
        layout.addView(spaceBtn);
        
        Button quizBtn = new Button(this);
        quizBtn.setText("Quiz Game");
        quizBtn.setTextSize(16);
        quizBtn.setPadding(0, 15, 0, 15);
        quizBtn.setOnClickListener(v -> startActivity(new Intent(this, QuizActivityEnhanced.class)));
        layout.addView(quizBtn);
        
        // Settings
        addSectionTitle(layout, "⚙️ Settings & Tools");
        
        Button weatherBtn = new Button(this);
        weatherBtn.setText("Weather Info");
        weatherBtn.setTextSize(16);
        weatherBtn.setPadding(0, 15, 0, 15);
        weatherBtn.setOnClickListener(v -> startActivity(new Intent(this, WeatherActivity.class)));
        layout.addView(weatherBtn);
        
        Button settingsBtn = new Button(this);
        settingsBtn.setText("Settings");
        settingsBtn.setTextSize(16);
        settingsBtn.setPadding(0, 15, 0, 15);
        settingsBtn.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
        layout.addView(settingsBtn);
        
        scrollView.addView(layout);
        mainLayout.addView(scrollView, new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        
        setContentView(mainLayout);
    }
    
    private void addSectionTitle(LinearLayout parent, String title) {
        TextView sectionTitle = new TextView(this);
        sectionTitle.setText(title);
        sectionTitle.setTextSize(18);
        sectionTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        sectionTitle.setPadding(0, 20, 0, 10);
        parent.addView(sectionTitle);
    }
}