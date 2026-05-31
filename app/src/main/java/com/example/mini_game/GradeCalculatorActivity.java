package com.example.mini_game;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GradeCalculatorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);
        
        TextView title = new TextView(this);
        title.setText("A-Level Grade Calculator");
        title.setTextSize(28);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setPadding(0, 0, 0, 30);
        layout.addView(title);
        
        Button addSubjectBtn = new Button(this);
        addSubjectBtn.setText("Add Subject");
        addSubjectBtn.setTextSize(18);
        addSubjectBtn.setPadding(0, 20, 0, 20);
        addSubjectBtn.setOnClickListener(v -> showAddSubjectDialog());
        layout.addView(addSubjectBtn);
        
        Button viewSubjectsBtn = new Button(this);
        viewSubjectsBtn.setText("View Subjects");
        viewSubjectsBtn.setTextSize(18);
        viewSubjectsBtn.setPadding(0, 20, 0, 20);
        viewSubjectsBtn.setOnClickListener(v -> startActivity(new Intent(this, SubjectsListActivity.class)));
        layout.addView(viewSubjectsBtn);
        
        Button calculateBtn = new Button(this);
        calculateBtn.setText("Calculate Overall Grade");
        calculateBtn.setTextSize(18);
        calculateBtn.setPadding(0, 20, 0, 20);
        calculateBtn.setOnClickListener(v -> startActivity(new Intent(this, ResultsActivity.class)));
        layout.addView(calculateBtn);
        
        Button settingsBtn = new Button(this);
        settingsBtn.setText("Settings");
        settingsBtn.setTextSize(18);
        settingsBtn.setPadding(0, 20, 0, 20);
        settingsBtn.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
        layout.addView(settingsBtn);
        
        setContentView(layout);
    }
    
    private void showAddSubjectDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Add Subject");
        
        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setPadding(20, 20, 20, 20);
        
        android.widget.EditText subjectName = new android.widget.EditText(this);
        subjectName.setHint("Subject Name");
        dialogLayout.addView(subjectName);
        
        android.widget.EditText marksObtained = new android.widget.EditText(this);
        marksObtained.setHint("Marks Obtained");
        marksObtained.setInputType(android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        dialogLayout.addView(marksObtained);
        
        android.widget.EditText totalMarks = new android.widget.EditText(this);
        totalMarks.setHint("Total Marks");
        totalMarks.setInputType(android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        dialogLayout.addView(totalMarks);
        
        builder.setView(dialogLayout);
        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = subjectName.getText().toString();
            String marks = marksObtained.getText().toString();
            String total = totalMarks.getText().toString();
            
            if (!name.isEmpty() && !marks.isEmpty() && !total.isEmpty()) {
                GradeDataManager.addSubject(this, name, Double.parseDouble(marks), Double.parseDouble(total));
                android.widget.Toast.makeText(this, "Subject added!", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}