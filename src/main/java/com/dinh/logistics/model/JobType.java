package com.dinh.logistics.model;

import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class JobType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobTypeId;

    @Column(name = "job_type_name")
    private String jobTypeName;

    @Column(name = "job_type_desc")
    private String jobTypeDesc;

    @Column(name = "state")
    private String state;
}
