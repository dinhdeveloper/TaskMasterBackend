package com.dinh.logistics.dto.mobile;

import com.dinh.logistics.model.EmployeeJob;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class JobDetailsDTO {
    private int jobId;
    private int jobStateId;
    private String stateDecs;
    private String numAddress;
    private String namePoint;
    private BigDecimal priority;
    private String noteJob;
    private String jobStateCode;
    private List<MediaDto> jobMedia;
    private List<MaterialJob> jobMaterial;
    private List<EmployeeJob> employeeJobs;
}

