package com.dinh.logistics.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "role_pj")
public class RolePj {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "state")
    private boolean state;

    @Column(name = "role_code")
    private String roleCode;
    
}
