package com.dinh.logistics.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class JobMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_mate_id")
    private Integer jobMateId;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "mate_id")
    private Integer mateId;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "unit_price")
    private Integer unitPrice;

    @Column(name = "material_name")
    private String materialName;

}
