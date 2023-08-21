package com.dinh.logistics.dto.mobile;

import lombok.Data;

@Data
public class MaterialDto {
    private Integer mateId;
    private Integer jobId;
    private Long weight;
    private Long weightToCus;
    private Long price;
}
