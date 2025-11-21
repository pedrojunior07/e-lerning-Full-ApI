package com.elearning.e_learning_core.Dtos;

import java.math.BigDecimal;

import com.elearning.e_learning_core.model.ExpiryType;

public class CourseDtoStep5 {
    private boolean isFree;
    private BigDecimal price;
    private boolean hasDiscount;
    private BigDecimal discountPrice;
    private ExpiryType expiryType;
    private Integer expiryMonths;

    public CourseDtoStep5() {
    }

    public CourseDtoStep5(boolean isFree, BigDecimal price, boolean hasDiscount, BigDecimal discountPrice,
            ExpiryType expiryType, Integer expiryMonths) {
        this.isFree = isFree;
        this.price = price;
        this.hasDiscount = hasDiscount;
        this.discountPrice = discountPrice;
        this.expiryType = expiryType;
        this.expiryMonths = expiryMonths;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean isFree) {
        this.isFree = isFree;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isHasDiscount() {
        return hasDiscount;
    }

    public void setHasDiscount(boolean hasDiscount) {
        this.hasDiscount = hasDiscount;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public ExpiryType getExpiryType() {
        return expiryType;
    }

    public void setExpiryType(ExpiryType expiryType) {
        this.expiryType = expiryType;
    }

    public Integer getExpiryMonths() {
        return expiryMonths;
    }

    public void setExpiryMonths(Integer expiryMonths) {
        this.expiryMonths = expiryMonths;
    }

}
