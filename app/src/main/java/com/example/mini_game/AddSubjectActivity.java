package com.example.mini_game;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddSubjectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);
        
        TextView title = new TextView(this);
        title.setText("Add Subject");
        title.setTextSize(28);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setPadding(0, 0, 0, 30);
        layout.addView(title);
        
        EditText subjectName = new EditText(this);
        subjectName.setHint("Subject Name (e.g., Mathematics)");
        subjectName.setPadding(15, 15, 15, 15);
        layout.addView(subjectName);
        
        EditText marksObtained = new EditText(this);
        marksObtained.setHint("Marks Obtained");
        marksObtained.setInputType(android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        marksObtained.setPadding(15, 15, 15, 15);
        layout.addView(marksObtained);
        
        EditText totalMarks = new EditText(this);
        totalMarks.setHint("Total Marks");
        totalMarks.setInputType(android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        totalMarks.setPadding(15, 15, 15, 15);
        layout.addView(totalMarks);
        
        Button addBtn = new Button(this);
        addBtn.setText("Add Subject");
        addBtn.setTextSize(18);
        addBtn.setPadding(0, 20, 0, 20);
        addBtn.setOnClickListener(v -> {
            String name = subjectName.getText().toString().trim();
            String marks = marksObtained.getText().toString().trim();
            String total = totalMarks.getText().toString().trim();
            
            if (name.isEmpty() || marks.isEmpty() || total.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            
            try {
                double m = Double.parseDouble(marks);
                double t = Double.parseDouble(total);
                
                if (m > t) {
                    Toast.makeText(this, "Marks cannot exceed total", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                GradeDataManager.addSubject(this, name, m, t);
                Toast.makeText(this, "Subject added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid marks format", Toast.LENGTH_SHORT).show();
            }
        });
        layout.addView(addBtn);
        
        Button backBtn = new Button(this);
        backBtn.setText("Back");
        backBtn.setOnClickListener(v -> finish());
        layout.addView(backBtn);
        
        setContentView(layout);
    }
}