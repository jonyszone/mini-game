package com.example.mini_game;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

public class GradeDataManager {
    private static final String PREFS_NAME = "GradeCalculatorPrefs";
    private static final String SUBJECTS_KEY = "subjects";
    
    public static void addSubject(Context context, String name, double marksObtained, double totalMarks) {
        Subject subject = new Subject(name, marksObtained, totalMarks);
        List<Subject> subjects = getSubjects(context);
        subjects.add(subject);
        saveSubjects(context, subjects);
    }
    
    public static void deleteSubject(Context context, String name) {
        List<Subject> subjects = getSubjects(context);
        subjects.removeIf(s -> s.name.equals(name));
        saveSubjects(context, subjects);
    }
    
    public static List<Subject> getSubjects(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String data = prefs.getString(SUBJECTS_KEY, "");
        
        List<Subject> subjects = new ArrayList<>();
        if (data.isEmpty()) return subjects;
        
        String[] entries = data.split("\\|");
        for (String entry : entries) {
            String[] parts = entry.split(",");
            if (parts.length == 3) {
                try {
                    String name = parts[0];
                    double marks = Double.parseDouble(parts[1]);
                    double total = Double.parseDouble(parts[2]);
                    subjects.add(new Subject(name, marks, total));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return subjects;
    }
    
    private static void saveSubjects(Context context, List<Subject> subjects) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < subjects.size(); i++) {
            Subject s = subjects.get(i);
            data.append(s.name).append(",").append(s.marksObtained).append(",").append(s.totalMarks);
            if (i < subjects.size() - 1) data.append("|");
        }
        
        editor.putString(SUBJECTS_KEY, data.toString());
        editor.apply();
    }
    
    public static void clearAllData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}