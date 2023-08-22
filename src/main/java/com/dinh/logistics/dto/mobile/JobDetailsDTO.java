package com.dinh.logistics.dto.mobile;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class JobDetailsDTO {
    private int jobId;
    private String stateDecs;
    private String numAddress;
    private String namePoint;
    private BigDecimal priority;
    private String noteJob;
    private List<MediaDto> jobMedia;
    private List<MaterialJob> jobMaterial;
}

