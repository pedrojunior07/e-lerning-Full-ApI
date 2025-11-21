package com.elearning.e_learning_core.Dtos;

public class PriceTypeCountDTO {
    private boolean isFree;
    private Long count;

    public PriceTypeCountDTO(boolean isFree, Long count) {
        this.isFree = isFree;
        this.count = count;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
