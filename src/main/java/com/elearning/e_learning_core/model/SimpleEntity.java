package com.elearning.e_learning_core.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "simple-entity")
public class SimpleEntity extends  BaseEntity {

    private String description;

}
