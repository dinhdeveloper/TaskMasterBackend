package com.dinh.logistics.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserProfileInfo {
    private Integer empId;
    private String name;
    private BigDecimal age;
    private String gender;
    private String phone;
    private String numAddress;
    private String streetAddress;
    private String ward;
    private String dist;
    private String province;
    private Integer teamId;
    private String teamName;
    private Integer leaderId;
    private String territory;
    private Integer roleId;
    private String roleName;
    private String roleCode;

    public UserProfileInfo(Integer empId, String name, BigDecimal age, String gender, String phone, String numAddress, String streetAddress, String ward, String dist, String province, Integer teamId, String teamName, Integer leaderId, String territory, Integer roleId, String roleName, String roleCode) {
        this.empId = empId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.numAddress = numAddress;
        this.streetAddress = streetAddress;
        this.ward = ward;
        this.dist = dist;
        this.province = province;
        this.teamId = teamId;
        this.teamName = teamName;
        this.leaderId = leaderId;
        this.territory = territory;
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleCode = roleCode;
    }
}

