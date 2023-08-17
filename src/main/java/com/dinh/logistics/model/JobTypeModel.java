package com.dinh.logistics.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class JobTypeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long JobTypeId;

    @Column(name = "Job_type_name")
    private String JobTypeName;

    @Column(name = "Job_type_desc")
    private Long JobTypeDesc;

    @Column(name = "State")
    private String State;

    public long getJobTypeId() {
        return JobTypeId;
    }

    public void setJobTypeId(long jobTypeId) {
        JobTypeId = jobTypeId;
    }

    public String getJobTypeName() {
        return JobTypeName;
    }

    public void setJobTypeName(String jobTypeName) {
        JobTypeName = jobTypeName;
    }

    public Long getJobTypeDesc() {
        return JobTypeDesc;
    }

    public void setJobTypeDesc(Long jobTypeDesc) {
        JobTypeDesc = jobTypeDesc;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
