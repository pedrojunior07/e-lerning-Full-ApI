package com.elearning.e_learning_core.Dtos;

import java.time.LocalDate;
import java.util.List;

import com.elearning.e_learning_core.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserRegisterDTO {

    private String firstName;
    private String lastName;
    private String userName;
    private String phoneNumber;
    private String email;
    private String password;
    private String gender;
    private LocalDate dob;
    private Role role;
    private List<ExperienceDTO> experiences;
    private String profileImageUrl;
    private String bio;

    // Construtor vazio
    public UserRegisterDTO() {
    }

    // Construtor com todos os campos
    public UserRegisterDTO(String firstName, String lastName, String userName, String phoneNumber, String email,
            String password, String gender, LocalDate dob, Role role, List<ExperienceDTO> experiences,
            String profileImageUrl, String bio) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.dob = dob;
        this.role = role;
        this.experiences = experiences;
        this.profileImageUrl = profileImageUrl;
        this.bio = bio;
    }

    // Getters e Setters

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<ExperienceDTO> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<ExperienceDTO> experiences) {
        this.experiences = experiences;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}