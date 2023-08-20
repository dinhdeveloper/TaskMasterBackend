package com.dinh.logistics.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mate_id")
    private Integer mate_id;

    @Column(name = "name")
    private String name;

    @Column(name = "unit_price")
    private Integer unitPrice;

    @Column(name = "note")
    private String note;


    @Column(name = "state")
    private boolean state;
}
