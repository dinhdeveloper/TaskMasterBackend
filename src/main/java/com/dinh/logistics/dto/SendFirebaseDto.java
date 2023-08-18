package com.dinh.logistics.dto;

import lombok.Data;

@Data
public class SendFirebaseDto {

	private String to;
	private FirebaseBodyDto data;
	
}
