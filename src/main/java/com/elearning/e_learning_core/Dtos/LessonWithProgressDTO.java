package com.elearning.e_learning_core.Dtos;

import java.time.LocalDateTime;

public class LessonWithProgressDTO {
    private long id;
    private String title;
    private String content;
    private LessonProgressDTO progress;

    public LessonWithProgressDTO() {
    }

    public LessonWithProgressDTO(long id, String title, String content, LessonProgressDTO progress) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.progress = progress;
    }

    // Getters e Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LessonProgressDTO getProgress() {
        return progress;
    }

    public void setProgress(LessonProgressDTO progress) {
        this.progress = progress;
    }
}