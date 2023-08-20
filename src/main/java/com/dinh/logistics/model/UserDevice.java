package com.dinh.logistics.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.Data;

@Entity
@Data
@Table(name = "user_devices")
public class UserDevice {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_devices_id")
    private Integer id;
	
	@Column(name = "access_token")
	private String accessToken;
	
	@Column(name = "user_id")
	private int userId;
	
	@Column(name = "is_active_access_token")
	private boolean isActiveAccessToken;
	
	@Column(name = "device_id")
	private String deviceId;
	
	@Column(name = "firebase_token")
	private String firebase_token;
	
}
