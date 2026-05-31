package com.example.mini_game;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ScrollView;
import java.util.List;

public class SubjectsListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(20, 20, 20, 20);
        
        TextView title = new TextView(this);
        title.setText("Your Subjects");
        title.setTextSize(28);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setPadding(0, 0, 0, 20);
        mainLayout.addView(title);
        
        ScrollView scrollView = new ScrollView(this);
        LinearLayout contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        
        List<Subject> subjects = GradeDataManager.getSubjects(this);
        
        if (subjects.isEmpty()) {
            TextView noSubjects = new TextView(this);
            noSubjects.setText("No subjects added yet. Add one to get started!");
            noSubjects.setTextSize(16);
            noSubjects.setPadding(0, 20, 0, 20);
            contentLayout.addView(noSubjects);
        } else {
            for (Subject subject : subjects) {
                LinearLayout subjectCard = createSubjectCard(subject);
                contentLayout.addView(subjectCard);
            }
        }
        
        scrollView.addView(contentLayout);
        mainLayout.addView(scrollView, new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        
        Button backBtn = new Button(this);
        backBtn.setText("Back");
        backBtn.setOnClickListener(v -> finish());
        mainLayout.addView(backBtn);
        
        setContentView(mainLayout);
    }
    
    private LinearLayout createSubjectCard(Subject subject) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(15, 15, 15, 15);
        card.setBackgroundColor(0xFFE3F2FD);
        
        LinearLayout margin = new LinearLayout(this);
        margin.setOrientation(LinearLayout.VERTICAL);
        margin.addView(card);
        margin.setPadding(0, 0, 0, 10);
        
        TextView nameText = new TextView(this);
        nameText.setText(subject.name);
        nameText.setTextSize(18);
        nameText.setTypeface(null, android.graphics.Typeface.BOLD);
        card.addView(nameText);
        
        TextView percentageText = new TextView(this);
        percentageText.setText(String.format("%.1f%% - Grade: %s", subject.percentage, subject.grade));
        percentageText.setTextSize(16);
        percentageText.setTextColor(0xFF1976D2);
        card.addView(percentageText);
        
        TextView marksText = new TextView(this);
        marksText.setText(String.format("Marks: %.0f/%.0f", subject.marksObtained, subject.totalMarks));
        marksText.setTextSize(14);
        card.addView(marksText);
        
        Button deleteBtn = new Button(this);
        deleteBtn.setText("Delete");
        deleteBtn.setTextSize(12);
        final String subjectName = subject.name;
        deleteBtn.setOnClickListener(v -> {
            GradeDataManager.deleteSubject(this, subjectName);
            recreate();
        });
        card.addView(deleteBtn);
        
        return margin;
    }
}