package com.dinh.logistics.model;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "custom_id")
    private Long cusId;

    @Column(name = "user_name")
    private String userName;
    
    @Column(name = "password")
    private String password;

    @Column(name = "state")
    private Long state;

}
