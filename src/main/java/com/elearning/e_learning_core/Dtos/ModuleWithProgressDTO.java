package com.elearning.e_learning_core.Dtos;

import java.util.List;

public class ModuleWithProgressDTO {
    private long id;
    private String title;
    private String description;
    private List<LessonWithProgressDTO> lessons;

    public ModuleWithProgressDTO() {
    }

    public ModuleWithProgressDTO(long id, String title, String description, List<LessonWithProgressDTO> lessons) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.lessons = lessons;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<LessonWithProgressDTO> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonWithProgressDTO> lessons) {
        this.lessons = lessons;
    }
}