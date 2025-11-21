package com.elearning.e_learning_core.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "quiz")
public class Quiz extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private int numberOfQuestions;

    @Column(nullable = false)
    private String duration; // Exemplo: "30 minutes"

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<Question> questions;

    public Quiz() {
    }

    public Quiz(String title, Course course, int numberOfQuestions, String duration) {
        this.title = title;
        this.course = course;
        this.numberOfQuestions = numberOfQuestions;
        this.duration = duration;
    }

    public Quiz(String title, Course course, int numberOfQuestions, String duration, List<Question> questions) {
        this.title = title;
        this.course = course;
        this.numberOfQuestions = numberOfQuestions;
        this.duration = duration;
        this.questions = questions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
