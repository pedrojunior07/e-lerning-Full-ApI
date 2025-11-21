package com.elearning.e_learning_core.Dtos;

import java.util.List;

public class ModuleDto {

    private long id;
    private String title;
    private String description;
    private List<LessonDTO> lessons;

    public ModuleDto() {
    }

    public ModuleDto(long id, String title, String description, List<LessonDTO> lessons) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.lessons = lessons;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<LessonDTO> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonDTO> lessons) {
        this.lessons = lessons;
    }
}