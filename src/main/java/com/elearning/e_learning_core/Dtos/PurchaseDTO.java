package com.elearning.e_learning_core.Dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PurchaseDTO {
    private List<Long> courseIds;
    private PaymentMethodDTO paymentMethod;
    private BigDecimal amount;
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataCompra;

    private String metodoPagamento;
    private String nomeEstudante;

    // 👇 Adicione este construtor vazio
    public PurchaseDTO() {
    }

    public PurchaseDTO(Long id, BigDecimal amount, LocalDateTime dataCompra, String metodoPagamento,
            String nomeEstudante) {
        this.amount = amount;
        this.dataCompra = dataCompra;
        this.metodoPagamento = metodoPagamento;
        this.nomeEstudante = nomeEstudante;
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((courseIds == null) ? 0 : courseIds.hashCode());
        result = prime * result + ((paymentMethod == null) ? 0 : paymentMethod.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PurchaseDTO other = (PurchaseDTO) obj;
        if (courseIds == null) {
            if (other.courseIds != null)
                return false;
        } else if (!courseIds.equals(other.courseIds))
            return false;
        if (paymentMethod == null) {
            if (other.paymentMethod != null)
                return false;
        } else if (!paymentMethod.equals(other.paymentMethod))
            return false;
        return true;
    }

    public PurchaseDTO(List<Long> courseIds, PaymentMethodDTO paymentMethod) {
        this.courseIds = courseIds;
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethodDTO getPaymentMethod() {
        return paymentMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PurchaseDTO(List<Long> courseIds, PaymentMethodDTO paymentMethod, BigDecimal amount,
            LocalDateTime dataCompra, String metodoPagamento, String nomeEstudante) {
        this.courseIds = courseIds;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.dataCompra = dataCompra;
        this.metodoPagamento = metodoPagamento;
        this.nomeEstudante = nomeEstudante;
    }

    public LocalDateTime getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDateTime dataCompra) {
        this.dataCompra = dataCompra;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getNomeEstudante() {
        return nomeEstudante;
    }

    public void setNomeEstudante(String nomeEstudante) {
        this.nomeEstudante = nomeEstudante;
    }

    public void setPaymentMethod(PaymentMethodDTO paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<Long> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<Long> courseIds) {
        this.courseIds = courseIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
