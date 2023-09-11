package com.dinh.logistics.dto.mobile;

import lombok.Data;


@Data
public class UpdateStateRequest {
    private int empUpdate;
    private int jobsId;
    private int stateJob;
    private int paymentMethod;
    private int paymentStateStatus;
    private Long amountPaidEmp;
    private Long amountTotal;
    private String dateCreate;
}
