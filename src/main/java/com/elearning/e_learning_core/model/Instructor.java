package com.elearning.e_learning_core.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("INSTRUCTOR")
public class Instructor extends Person {

    private String bio;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences = new ArrayList<>();

    public Instructor() {
        super();
    }

    public Instructor(String firstName, String lastName, String userName, String phoneNumber, String email,
            String gender,
            java.time.LocalDate dob, String bio, List<Course> courses, List<Experience> experiences) {
        super(firstName, lastName, userName, phoneNumber, email, gender, dob);
        this.bio = bio;
        this.courses = courses != null ? courses : new ArrayList<>();
        this.experiences = experiences != null ? experiences : new ArrayList<>();
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses != null ? courses : new ArrayList<>();
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences != null ? experiences : new ArrayList<>();
    }
}
