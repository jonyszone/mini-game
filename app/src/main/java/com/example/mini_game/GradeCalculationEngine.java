package com.example.mini_game;

import java.util.List;

public class GradeCalculationEngine {
    
    public static class GradeStats {
        public double overallPercentage;
        public String overallGrade;
        public int countA;
        public int countB;
        public int countC;
        public int countD;
        public int countE;
        public int countU;
        public double highestPercentage;
        public double lowestPercentage;
        public String bestSubject;
        public String worstSubject;
    }
    
    public static GradeStats calculateOverallGrade(List<Subject> subjects) {
        GradeStats stats = new GradeStats();
        
        if (subjects.isEmpty()) {
            stats.overallPercentage = 0;
            stats.overallGrade = "N/A";
            return stats;
        }
        
        double totalPercentage = 0;
        stats.highestPercentage = 0;
        stats.lowestPercentage = 100;
        
        for (Subject subject : subjects) {
            totalPercentage += subject.percentage;
            
            if (subject.percentage > stats.highestPercentage) {
                stats.highestPercentage = subject.percentage;
                stats.bestSubject = subject.name;
            }
            
            if (subject.percentage < stats.lowestPercentage) {
                stats.lowestPercentage = subject.percentage;
                stats.worstSubject = subject.name;
            }
            
            switch (subject.grade) {
                case "A*": stats.countA++; break;
                case "A": stats.countA++; break;
                case "B": stats.countB++; break;
                case "C": stats.countC++; break;
                case "D": stats.countD++; break;
                case "E": stats.countE++; break;
                case "U": stats.countU++; break;
            }
        }
        
        stats.overallPercentage = totalPercentage / subjects.size();
        stats.overallGrade = getGradeFromPercentage(stats.overallPercentage);
        
        return stats;
    }
    
    private static String getGradeFromPercentage(double percentage) {
        if (percentage >= 90) return "A*";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B";
        if (percentage >= 60) return "C";
        if (percentage >= 50) return "D";
        if (percentage >= 40) return "E";
        return "U";
    }
}