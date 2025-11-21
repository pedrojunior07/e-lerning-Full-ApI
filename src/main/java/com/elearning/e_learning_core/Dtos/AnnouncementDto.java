package com.elearning.e_learning_core.Dtos;

import java.time.LocalDateTime;

public class AnnouncementDto {

    private Long id;
    private String title;
    private String message;
    private Long courseId;
    private String courseTitle;
    private LocalDateTime createdAt;

    public AnnouncementDto(Long id, String title, String message, Long courseId, String courseTitle,
            LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.createdAt = createdAt;
    }

    public AnnouncementDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
