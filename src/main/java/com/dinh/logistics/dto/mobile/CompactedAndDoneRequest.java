package com.dinh.logistics.dto.mobile;

import lombok.Data;

@Data
public class CompactedAndDoneRequest {
    Integer jobsId;
    Integer stateJob;
    private int empUpdate;
}
