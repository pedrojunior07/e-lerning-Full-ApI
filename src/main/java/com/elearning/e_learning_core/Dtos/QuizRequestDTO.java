package com.elearning.e_learning_core.Dtos;

public class QuizRequestDTO {

    private Long courseId;
    private Long id;
    private String title;
    private int numberOfQuestions;
    private String duration;
    private String courseName;

    public QuizRequestDTO() {
    }

    public QuizRequestDTO(Long id, Long courseId, String title, int numberOfQuestions, String duration,
            String courseName) {
        this.courseId = courseId;
        this.title = title;
        this.numberOfQuestions = numberOfQuestions;
        this.duration = duration;
        this.id = id;
        this.courseName = courseName;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

}
