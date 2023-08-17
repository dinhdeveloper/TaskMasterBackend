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
    private Long id;
	
	@Column(name = "access_token")
	private String accessToken;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "is_active_access_token")
	private Boolean isActiveAccessToken;
	
	@Column(name = "device_id")
	private String deviceId;
	
}
