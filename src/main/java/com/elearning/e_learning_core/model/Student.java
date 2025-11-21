package com.elearning.e_learning_core.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("STUDENT")
@Entity
public class Student extends Person {
    @ManyToMany
    @JoinTable(name = "student_course", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses = new ArrayList<>();

    @Override
    public LocalDate getDob() {
        // TODO Auto-generated method stub
        return super.getDob();
    }

    @Override
    public String getEmail() {
        // TODO Auto-generated method stub
        return super.getEmail();
    }

    @Override
    public String getFirstName() {
        // TODO Auto-generated method stub
        return super.getFirstName();
    }

    @Override
    public String getGender() {
        // TODO Auto-generated method stub
        return super.getGender();
    }

    @Override
    public String getLastName() {
        // TODO Auto-generated method stub
        return super.getLastName();
    }

    @Override
    public String getPhoneNumber() {
        // TODO Auto-generated method stub
        return super.getPhoneNumber();
    }

    @Override
    public String getUserName() {
        // TODO Auto-generated method stub
        return super.getUserName();
    }

    @Override
    public void setDob(LocalDate dob) {
        // TODO Auto-generated method stub
        super.setDob(dob);
    }

    @Override
    public void setEmail(String email) {
        // TODO Auto-generated method stub
        super.setEmail(email);
    }

    @Override
    public void setFirstName(String firstName) {
        // TODO Auto-generated method stub
        super.setFirstName(firstName);
    }

    @Override
    public void setGender(String gender) {
        // TODO Auto-generated method stub
        super.setGender(gender);
    }

    @Override
    public void setLastName(String lastName) {
        // TODO Auto-generated method stub
        super.setLastName(lastName);
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        // TODO Auto-generated method stub
        super.setPhoneNumber(phoneNumber);
    }

    @Override
    public void setUserName(String userName) {
        // TODO Auto-generated method stub
        super.setUserName(userName);
    }

    @Override
    public LocalDateTime getCreatedAt() {
        // TODO Auto-generated method stub
        return super.getCreatedAt();
    }

    @Override
    public Long getCreatedBy() {
        // TODO Auto-generated method stub
        return super.getCreatedBy();
    }

    @Override
    public Long getId() {
        // TODO Auto-generated method stub
        return super.getId();
    }

    @Override
    public LocalDateTime getUpdatedAt() {
        // TODO Auto-generated method stub
        return super.getUpdatedAt();
    }

    @Override
    public Long getUpdatedBy() {
        // TODO Auto-generated method stub
        return super.getUpdatedBy();
    }

    @Override
    public void setCreatedAt(LocalDateTime createdAt) {
        // TODO Auto-generated method stub
        super.setCreatedAt(createdAt);
    }

    @Override
    public void setCreatedBy(Long createdBy) {
        // TODO Auto-generated method stub
        super.setCreatedBy(createdBy);
    }

    @Override
    public void setId(Long id) {
        // TODO Auto-generated method stub
        super.setId(id);
    }

    @Override
    public void setUpdatedAt(LocalDateTime updatedAt) {
        // TODO Auto-generated method stub
        super.setUpdatedAt(updatedAt);
    }

    @Override
    public void setUpdatedBy(Long updatedBy) {
        // TODO Auto-generated method stub
        super.setUpdatedBy(updatedBy);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    @Override
    protected void finalize() throws Throwable {
        // TODO Auto-generated method stub
        super.finalize();
    }

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CourseLike> likedCourses = new ArrayList<>();

    public Student() {
        super();
    }

    // NOVOS MÉTODOS PARA GERENCIAR LIKES
    public List<CourseLike> getLikedCourses() {
        return likedCourses;
    }

    public void setLikedCourses(List<CourseLike> likedCourses) {
        this.likedCourses = likedCourses;
    }

    public void addLikedCourse(CourseLike like) {
        likedCourses.add(like);
        like.setStudent(this);
    }

    public void removeLikedCourse(CourseLike like) {
        likedCourses.remove(like);
        like.setStudent(null);
    }

    // Método utilitário para verificar se curtiu um curso específico
    public boolean hasLikedCourse(Course course) {
        return likedCourses.stream().anyMatch(like -> like.getCourse().equals(course));
    }

    // Método utilitário para obter like específico
    public CourseLike getLikeForCourse(Course course) {
        return likedCourses.stream()
                .filter(like -> like.getCourse().equals(course))
                .findFirst()
                .orElse(null);
    }

    // Getters e setters existentes para courses
    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void enrollInCourse(Course course) {
        courses.add(course);
        course.getStudents().add(this);
    }

    public void unenrollFromCourse(Course course) {
        courses.remove(course);
        course.getStudents().remove(this);
    }
}