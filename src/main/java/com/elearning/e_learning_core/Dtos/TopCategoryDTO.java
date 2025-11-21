package com.elearning.e_learning_core.Dtos;

public class TopCategoryDTO {

    private Long categoryId;
    private String categoryName;
    private Long courseCount;

    public TopCategoryDTO(Long categoryId, String categoryName, Long courseCount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.courseCount = courseCount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public long getCourseCount() {
        return courseCount;
    }

    public long getId() {
        return categoryId;
    }

    public void setId(long id) {
        this.categoryId = id;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCourseCount(long courseCount) {
        this.courseCount = courseCount;
    }

}