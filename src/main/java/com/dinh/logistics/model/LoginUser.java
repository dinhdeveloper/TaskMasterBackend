package com.dinh.logistics.model;

import javax.persistence.Column;

import lombok.Data;

@Data
public class LoginUser {
	
    private String userName;
    private String passWord;
}
