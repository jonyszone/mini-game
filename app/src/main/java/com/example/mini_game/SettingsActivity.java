package com.example.mini_game;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);
        
        TextView title = new TextView(this);
        title.setText("Settings");
        title.setTextSize(28);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setPadding(0, 0, 0, 30);
        layout.addView(title);
        
        // About section
        TextView aboutTitle = new TextView(this);
        aboutTitle.setText("About");
        aboutTitle.setTextSize(20);
        aboutTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        aboutTitle.setPadding(0, 0, 0, 10);
        layout.addView(aboutTitle);
        
        TextView appVersion = new TextView(this);
        appVersion.setText("A-Level Grade Calculator v1.0");
        appVersion.setTextSize(16);
        appVersion.setPadding(0, 0, 0, 5);
        layout.addView(appVersion);
        
        TextView appDesc = new TextView(this);
        appDesc.setText("Calculate your A-level grades with ease. Track your subjects and get instant statistics.");
        appDesc.setTextSize(14);
        appDesc.setPadding(0, 0, 0, 20);
        layout.addView(appDesc);
        
        // Grade Scale section
        TextView gradeTitle = new TextView(this);
        gradeTitle.setText("Grade Scale");
        gradeTitle.setTextSize(20);
        gradeTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        gradeTitle.setPadding(0, 0, 0, 10);
        layout.addView(gradeTitle);
        
        addGradeInfo(layout, "A*", "90% - 100%");
        addGradeInfo(layout, "A", "80% - 89%");
        addGradeInfo(layout, "B", "70% - 79%");
        addGradeInfo(layout, "C", "60% - 69%");
        addGradeInfo(layout, "D", "50% - 59%");
        addGradeInfo(layout, "E", "40% - 49%");
        addGradeInfo(layout, "U", "Below 40%");
        
        // Data Management section
        TextView dataTitle = new TextView(this);
        dataTitle.setText("Data Management");
        dataTitle.setTextSize(20);
        dataTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        dataTitle.setPadding(0, 20, 0, 10);
        layout.addView(dataTitle);
        
        Button clearDataBtn = new Button(this);
        clearDataBtn.setText("Clear All Data");
        clearDataBtn.setTextSize(16);
        clearDataBtn.setPadding(0, 15, 0, 15);
        clearDataBtn.setBackgroundColor(0xFFFF5252);
        clearDataBtn.setTextColor(0xFFFFFFFF);
        clearDataBtn.setOnClickListener(v -> {
            GradeDataManager.clearAllData(this);
            android.widget.Toast.makeText(this, "All data cleared!", android.widget.Toast.LENGTH_SHORT).show();
            finish();
        });
        layout.addView(clearDataBtn);
        
        Button backBtn = new Button(this);
        backBtn.setText("Back");
        backBtn.setOnClickListener(v -> finish());
        layout.addView(backBtn);
        
        setContentView(layout);
    }
    
    private void addGradeInfo(LinearLayout parent, String grade, String range) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(10, 8, 10, 8);
        
        TextView gradeText = new TextView(this);
        gradeText.setText(grade);
        gradeText.setTextSize(16);
        gradeText.setTypeface(null, android.graphics.Typeface.BOLD);
        gradeText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        row.addView(gradeText);
        
        TextView rangeText = new TextView(this);
        rangeText.setText(range);
        rangeText.setTextSize(14);
        row.addView(rangeText);
        
        parent.addView(row);
    }
}