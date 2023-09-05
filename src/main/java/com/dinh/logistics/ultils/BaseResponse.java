package com.dinh.logistics.ultils;

import org.springframework.core.io.Resource;

import lombok.Data;

@Data
public class BaseResponse {
	
	private int result_code;
	private String result_description;
	private Object data;
	private int code_status;
	private Resource resource;
	

}
