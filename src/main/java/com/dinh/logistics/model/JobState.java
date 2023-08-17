package com.dinh.logistics.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class JobState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_state_id")
    private Integer jobStateId;

    @Column(name = "job_state_desc")
    private String jobStateDesc;

    @Column(name = "state")
    private String state;
}
