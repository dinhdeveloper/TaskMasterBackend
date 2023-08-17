package com.dinh.logistics.model;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
public class CollectPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "colle_point_id")
    private Integer empId;

    @Column(name = "name")
    private String name;

    @Column(name = "num_address")
    private String numAddress;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "ward")
    private String ward;

    @Column(name = "dist")
    private String dist;

    @Column(name = "ref_place")
    private String refPlace;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "custom_id")
    private Integer customId;

    @Column(name = "pos_long")
    private String posLong;

    @Column(name = "pos_lat")
    private String posLat;

    @Column(name = "bank_acct_name")
    private String bankAcctName;

    @Column(name = "bank_acct")
    private String bankAcct;

    @Column(name = "bank_acct_number")
    private String bankAcctNumber;

    @Column(name = "use_cus_bank")
    private String useCusBank;

    @Column(name = "state")
    private String state;

}
