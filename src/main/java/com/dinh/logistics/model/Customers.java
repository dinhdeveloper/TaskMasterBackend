package com.dinh.logistics.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Customers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custom_id")
    private Integer cusId;

    @Column(name = "custom_name")
    private String customName;

    @Column(name = "contact_name_1")
    private String contactName1;

    @Column(name = "phone_1")
    private String phone1;

    @Column(name = "contact_name_2")
    private String contactName2;

    @Column(name = "phone_2")
    private String phone2;

    @Column(name = "type")
    private Integer type;

    @Column(name = "bank_acct_name")
    private String bankAcctName;

    @Column(name = "bank_acct")
    private String bankAcct;

    @Column(name = "bank_acct_number")
    private String bankAcctNumber;

    @Column(name = "state")
    private Integer state;

}
