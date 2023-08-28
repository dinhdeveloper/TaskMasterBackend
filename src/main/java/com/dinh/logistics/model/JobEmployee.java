package com.dinh.logistics.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class JobEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Job_emp_id")
    private Integer JobEmpId;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "emp_id")
    private Integer empId;

    @Column(name = "state")
    private boolean state;

    @Column(name = "serial_number")
    private String serialNumber;

}
