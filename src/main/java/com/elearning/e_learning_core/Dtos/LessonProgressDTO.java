package com.elearning.e_learning_core.Dtos;

import java.time.LocalDateTime;

public class LessonProgressDTO {
    private boolean completed;
    private int progressPercentage;
    private LocalDateTime completedAt;

    public LessonProgressDTO() {
    }

    public LessonProgressDTO(boolean completed, int progressPercentage, LocalDateTime completedAt) {
        this.completed = completed;
        this.progressPercentage = progressPercentage;
        this.completedAt = completedAt;
    }

    // Getters e Setters
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}