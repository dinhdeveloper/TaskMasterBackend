package com.dinh.logistics.model;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @Column(name = "Employee_id")
    private Integer employeeId;

    @Column(name = "Custom_id")
    private Integer cusId;

    @Column(name = "User_name")
    private String userName;
    
    @Column(name = "Password")
    private String password;

    @Column(name = "State")
    private Integer state;

}
