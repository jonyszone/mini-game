package com.example.mini_game;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ScrollView;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(20, 20, 20, 20);
        
        List<Subject> subjects = GradeDataManager.getSubjects(this);
        GradeCalculationEngine.GradeStats stats = GradeCalculationEngine.calculateOverallGrade(subjects);
        
        TextView title = new TextView(this);
        title.setText("Your Results");
        title.setTextSize(28);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setPadding(0, 0, 0, 20);
        mainLayout.addView(title);
        
        if (subjects.isEmpty()) {
            TextView noData = new TextView(this);
            noData.setText("Add subjects to see results");
            noData.setTextSize(16);
            mainLayout.addView(noData);
        } else {
            // Overall Grade Card
            LinearLayout gradeCard = new LinearLayout(this);
            gradeCard.setOrientation(LinearLayout.VERTICAL);
            gradeCard.setPadding(20, 20, 20, 20);
            gradeCard.setBackgroundColor(0xFF4CAF50);
            
            TextView gradeLabel = new TextView(this);
            gradeLabel.setText("Overall Grade");
            gradeLabel.setTextSize(16);
            gradeLabel.setTextColor(0xFFFFFFFF);
            gradeCard.addView(gradeLabel);
            
            TextView gradeValue = new TextView(this);
            gradeValue.setText(stats.overallGrade);
            gradeValue.setTextSize(56);
            gradeValue.setTypeface(null, android.graphics.Typeface.BOLD);
            gradeValue.setTextColor(0xFFFFFFFF);
            gradeCard.addView(gradeValue);
            
            TextView percentage = new TextView(this);
            percentage.setText(String.format("%.1f%%", stats.overallPercentage));
            percentage.setTextSize(18);
            percentage.setTextColor(0xFFFFFFFF);
            gradeCard.addView(percentage);
            
            mainLayout.addView(gradeCard);
            
            // Statistics
            ScrollView scrollView = new ScrollView(this);
            LinearLayout statsLayout = new LinearLayout(this);
            statsLayout.setOrientation(LinearLayout.VERTICAL);
            statsLayout.setPadding(0, 20, 0, 0);
            
            addStatRow(statsLayout, "Best Subject", stats.bestSubject + " (" + String.format("%.1f%%", stats.highestPercentage) + ")");
            addStatRow(statsLayout, "Worst Subject", stats.worstSubject + " (" + String.format("%.1f%%", stats.lowestPercentage) + ")");
            addStatRow(statsLayout, "Total Subjects", String.valueOf(subjects.size()));
            addStatRow(statsLayout, "A* & A Count", String.valueOf(stats.countA));
            addStatRow(statsLayout, "B Count", String.valueOf(stats.countB));
            addStatRow(statsLayout, "C Count", String.valueOf(stats.countC));
            
            scrollView.addView(statsLayout);
            mainLayout.addView(scrollView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        }
        
        Button backBtn = new Button(this);
        backBtn.setText("Back");
        backBtn.setOnClickListener(v -> finish());
        mainLayout.addView(backBtn);
        
        setContentView(mainLayout);
    }
    
    private void addStatRow(LinearLayout parent, String label, String value) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(15, 12, 15, 12);
        row.setBackgroundColor(0xFFF5F5F5);
        
        TextView labelText = new TextView(this);
        labelText.setText(label);
        labelText.setTextSize(16);
        labelText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        row.addView(labelText);
        
        TextView valueText = new TextView(this);
        valueText.setText(value);
        valueText.setTextSize(16);
        valueText.setTypeface(null, android.graphics.Typeface.BOLD);
        valueText.setTextColor(0xFF1976D2);
        row.addView(valueText);
        
        parent.addView(row);
    }
}