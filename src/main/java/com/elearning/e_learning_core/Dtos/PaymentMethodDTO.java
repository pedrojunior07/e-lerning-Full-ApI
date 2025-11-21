package com.elearning.e_learning_core.Dtos;

public class PaymentMethodDTO {
    private String type;
    private String phone;

    // Construtor
    public PaymentMethodDTO(String type, String phone) {
        this.type = type;
        this.phone = phone;
    }

    // Getters e setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "PaymentMethodDTO [type=" + type + ", phone=" + phone + "]";
    }

}
