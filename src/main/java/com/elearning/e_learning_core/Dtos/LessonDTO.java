package com.elearning.e_learning_core.Dtos;

public class LessonDTO {

    private long id;
    private String title;
    private String content;

    public LessonDTO() {
    }

    public LessonDTO(long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}