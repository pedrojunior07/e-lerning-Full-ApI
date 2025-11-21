package com.elearning.e_learning_core.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "course_like", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "student_id", "course_id" })
})
public class CourseLike extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "liked_at")
    private LocalDateTime likedAt;

    public CourseLike() {
    }

    public CourseLike(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.likedAt = LocalDateTime.now();
    }

    // Getters e Setters
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDateTime getLikedAt() {
        return likedAt;
    }

    public void setLikedAt(LocalDateTime likedAt) {
        this.likedAt = likedAt;
    }
}