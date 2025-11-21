package com.elearning.e_learning_core.model;

import jakarta.persistence.Entity;

@Entity
public class PaymentMethod extends BaseEntity {

    private String tipo;
    private String numeroTelefone;


    
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getNumeroTelefone() {
        return numeroTelefone;
    }
    public void setNumeroTelefone(String numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }


    

     

}