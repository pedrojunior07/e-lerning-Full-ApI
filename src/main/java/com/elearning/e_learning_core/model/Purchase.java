package com.elearning.e_learning_core.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Purchase extends BaseEntity {

    @ManyToOne
    private Person student;

    private String paymentStatus;

    private BigDecimal amount;

    @ManyToMany
    private List<Course> courses;

    @OneToOne(cascade = CascadeType.ALL)
    private PaymentMethod paymentMethod;

    private LocalDateTime dataCompra;
    private String externalId;

    public Purchase(Person student, List<Course> courses, PaymentMethod paymentMethod, LocalDateTime dataCompra,
            BigDecimal amount) {
        this.student = student;
        this.courses = courses;
        this.paymentMethod = paymentMethod;
        this.dataCompra = dataCompra;
        this.amount = amount;
    }

    public Purchase() {
    }

    public Purchase(Person student, String paymentStatus, BigDecimal amount, List<Course> courses,
            PaymentMethod paymentMethod, LocalDateTime dataCompra, String externalId) {
        this.student = student;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.courses = courses;
        this.paymentMethod = paymentMethod;
        this.dataCompra = dataCompra;
        this.externalId = externalId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public Purchase(Person student, String paymentStatus, BigDecimal amount, List<Course> courses,
            PaymentMethod paymentMethod, LocalDateTime dataCompra) {
        this.student = student;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.courses = courses;
        this.paymentMethod = paymentMethod;
        this.dataCompra = dataCompra;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDateTime dataCompra) {
        this.dataCompra = dataCompra;
    }

    public Person getStudent() {
        return student;
    }

    public void setStudent(Person student) {
        this.student = student;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

}