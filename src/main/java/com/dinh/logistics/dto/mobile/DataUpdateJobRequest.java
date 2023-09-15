package com.dinh.logistics.dto.mobile;

import lombok.Data;


@Data
public class DataUpdateJobRequest {
    Integer jodId;
    Integer empOldId;
    Integer empNewId;
    Integer paymentMethod;
    Integer paymentStateId;
    Integer empAssignId;
    Long amountPaidEmp;
    Long totalMoney;
    Integer priority;
    String note;
}
