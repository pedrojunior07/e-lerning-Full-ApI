package com.elearning.e_learning_core.Dtos;

public class CategoryDTO {
    private Long id;
    private String name;
    private Long quantCourse;

    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String name, Long quantCourse) {
        this.id = id;
        this.name = name;
        this.quantCourse = quantCourse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getQuantCourse() {
        return quantCourse;
    }

    public void setQuantCourse(Long quantCourse) {
        this.quantCourse = quantCourse;
    }

}
