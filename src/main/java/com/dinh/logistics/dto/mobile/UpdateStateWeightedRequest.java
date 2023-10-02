package com.dinh.logistics.dto.mobile;

import lombok.Data;


@Data
public class UpdateStateWeightedRequest {
    private int empUpdate;
    private int jodId;
    private int stateJob;
    private Double totalMoney;
    private int paymentMethod;
    private int paymentStateId;
    private Double amountPaidEmp;
    private int priority;
    private int empOldId;
    private int empNewId;
    private String note;
}
