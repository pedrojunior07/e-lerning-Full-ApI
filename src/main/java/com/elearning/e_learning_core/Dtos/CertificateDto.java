package com.elearning.e_learning_core.Dtos;

import java.time.LocalDateTime;

public class CertificateDto {
    private String certificateCode;
    private LocalDateTime issuedAt;
    private boolean revoked;
    private PersonDto student;
    private CourseDto course;

    public CertificateDto() {
    }

    public CertificateDto(String certificateCode, LocalDateTime issuedAt, boolean revoked,
            PersonDto student, CourseDto course) {
        this.certificateCode = certificateCode;
        this.issuedAt = issuedAt;
        this.revoked = revoked;
        this.student = student;
        this.course = course;
    }

    public String getCertificateCode() {
        return certificateCode;
    }

    public void setCertificateCode(String certificateCode) {
        this.certificateCode = certificateCode;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public PersonDto getStudent() {
        return student;
    }

    public void setStudent(PersonDto student) {
        this.student = student;
    }

    public CourseDto getCourse() {
        return course;
    }

    public void setCourse(CourseDto course) {
        this.course = course;
    }
}
