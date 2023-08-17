package com.dinh.logistics.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class JobEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "job_id")
    private String jobId;

    @Column(name = "emp_id")
    private String empId;

    @Column(name = "state")
    private Integer state;


}
