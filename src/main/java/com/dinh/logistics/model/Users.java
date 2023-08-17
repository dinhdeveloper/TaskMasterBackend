package com.dinh.logistics.model;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "custom_id")
    private Integer cusId;

    @Column(name = "user_name")
    private String userName;
    
    @Column(name = "password")
    private String password;

    @Column(name = "state")
    private Integer state;

}
