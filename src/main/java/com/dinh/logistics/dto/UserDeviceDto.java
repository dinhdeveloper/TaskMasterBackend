package com.dinh.logistics.dto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
public class UserDeviceDto {

    private Integer id;
	private String accessToken;
	private int userId;
	private boolean isActiveAccessToken;
	private String deviceId;
	private String firebaseToken;
	
	public UserDeviceDto (Integer id, String accessToken, Integer userId, String deviceId, String firebaseToken ) {
		this.id = id;
		this.accessToken = accessToken;
		this.userId = userId;
		this.deviceId = deviceId;
		this.firebaseToken = firebaseToken;
	}
	
	
}
