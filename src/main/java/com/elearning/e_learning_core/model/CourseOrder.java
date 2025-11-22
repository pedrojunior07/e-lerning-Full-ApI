package com.elearning.e_learning_core.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class CourseOrder extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletType paymentMethod;

    @Column(nullable = false)
    private BigDecimal amount;

    private String proofOfPaymentUrl;

    private LocalDateTime orderDate = LocalDateTime.now();

    private LocalDateTime validatedDate;

    private String rejectionReason;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private PaymentWallet selectedWallet;

    public CourseOrder() {
    }

    public CourseOrder(Student student, Course course, WalletType paymentMethod, BigDecimal amount, PaymentWallet selectedWallet) {
        this.student = student;
        this.course = course;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.selectedWallet = selectedWallet;
    }

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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public WalletType getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(WalletType paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getProofOfPaymentUrl() {
        return proofOfPaymentUrl;
    }

    public void setProofOfPaymentUrl(String proofOfPaymentUrl) {
        this.proofOfPaymentUrl = proofOfPaymentUrl;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getValidatedDate() {
        return validatedDate;
    }

    public void setValidatedDate(LocalDateTime validatedDate) {
        this.validatedDate = validatedDate;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public PaymentWallet getSelectedWallet() {
        return selectedWallet;
    }

    public void setSelectedWallet(PaymentWallet selectedWallet) {
        this.selectedWallet = selectedWallet;
    }
}
