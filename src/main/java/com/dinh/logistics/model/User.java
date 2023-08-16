package com.dinh.logistics.model;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "custom_id")
    private String cusId;

    @Column(name = "user_name")
    private String userName;
    
    @Column(name = "password")
    private String password;

    @Column(name = "state")
    private String state;

}
