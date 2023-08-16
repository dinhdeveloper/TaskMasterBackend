package com.dinh.logistics.model;

import javax.persistence.*;

@Entity
public class JobTypeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Job_type_id;

    @Column(name = "Job_type_name")
    private String Job_type_name;

    @Column(name = "Job_type_desc")
    private Long Job_type_desc;

    @Column(name = "State")
    private String State;

    public long getJob_type_id() {
        return Job_type_id;
    }

    public void setJob_type_id(long job_type_id) {
        Job_type_id = job_type_id;
    }

    public String getJob_type_name() {
        return Job_type_name;
    }

    public void setJob_type_name(String job_type_name) {
        Job_type_name = job_type_name;
    }

    public Long getJob_type_desc() {
        return Job_type_desc;
    }

    public void setJob_type_desc(Long job_type_desc) {
        Job_type_desc = job_type_desc;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
