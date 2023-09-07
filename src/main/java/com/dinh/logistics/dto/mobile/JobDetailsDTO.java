package com.dinh.logistics.dto.mobile;

import com.dinh.logistics.model.EmployeeJob;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class JobDetailsDTO {
    private Integer jobId;
    private Integer jobStateId;
    private String stateDecs;
    private String numAddress;
    private String namePoint;
    private BigDecimal priority;
    private String noteJob;
    private String jobStateCode;
    private BigDecimal amountPaidEmp;
    private BigDecimal paymentMethod;
    private Integer paymentStateId;
    private List<MediaDto> jobMedia;
    private List<MaterialJob> jobMaterial;
    private List<EmployeeJob> employeeJobs;
}

