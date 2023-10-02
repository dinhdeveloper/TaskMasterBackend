package com.dinh.logistics.dto.mobile;

import lombok.Data;

@Data
public class MaterialDto {
    private Integer mateId;
    private Integer jobId;
    private Double weight;
    private Double weightToCus;
    private Double price;
}
