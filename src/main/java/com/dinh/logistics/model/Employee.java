package com.dinh.logistics.model;

import java.sql.Date;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long emp_id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Long age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "team_id")
    private Long teamId;
    
    @Column(name = "num_address")
    private String numAddress;
    
    @Column(name = "street_address")
    private String streetAddress;
    
    @Column(name = "ward")
    private String ward;
    
    @Column(name = "dist")
    private String dist;
    
    @Column(name = "province")
    private String province;
    
    @Column(name = "state")
    private Long state;
    
    @Column(name = "start_date")
    private Date startDate;
    
    @Column(name = "role")
    private String role;
}
