package com.dinh.logistics.dto.mobile;

import lombok.Data;

@Data
public class DeleteMaterialRequest {
    private int jobId;
    private int jobMateId;
}
