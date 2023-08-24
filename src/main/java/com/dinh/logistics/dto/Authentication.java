package com.dinh.logistics.dto;

import lombok.Data;

@Data
public class Authentication {

	private String tokenAuth;
	private String tokenFirebase;
	private String role;
}
