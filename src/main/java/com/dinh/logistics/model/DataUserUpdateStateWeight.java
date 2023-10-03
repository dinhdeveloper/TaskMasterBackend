package com.dinh.logistics.model;


import lombok.Data;

@Data
public class DataUserUpdateStateWeight {
    private String customName;
    private String bankAcctName;
    private String bankAcct;
    private String bankAcctNumber;
    private String collectPointName;
    private Double amount;
    private Integer empAssignId;
}
