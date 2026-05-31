package com.example.mini_game;

public class Subject {
    public String name;
    public double marksObtained;
    public double totalMarks;
    public double percentage;
    public String grade;
    
    public Subject(String name, double marksObtained, double totalMarks) {
        this.name = name;
        this.marksObtained = marksObtained;
        this.totalMarks = totalMarks;
        this.percentage = (marksObtained / totalMarks) * 100;
        this.grade = calculateGrade(percentage);
    }
    
    private String calculateGrade(double percentage) {
        if (percentage >= 90) return "A*";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B";
        if (percentage >= 60) return "C";
        if (percentage >= 50) return "D";
        if (percentage >= 40) return "E";
        return "U";
    }
    
    @Override
    public String toString() {
        return String.format("%s: %.1f%% (%s)", name, percentage, grade);
    }
}