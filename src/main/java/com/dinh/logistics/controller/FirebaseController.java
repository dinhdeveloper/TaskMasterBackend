package com.dinh.logistics.controller;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dinh.logistics.model.UserDevice;
import com.dinh.logistics.respository.UserDeviceRepository;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/firebase")
@Slf4j
@Transactional
public class FirebaseController {

	@Autowired
	UserDeviceRepository userDeviceRepository;
	
	@PostMapping("/save")
	public ResponseEntity<Object> save(
//			HttpServletRequest request,
			@RequestParam String firebaseToken,
			@RequestParam String deviceId,
			@RequestParam String deviceName
	){
//		String token = request.getHeader("Authorization");
//		if(StringUtils.isBlank(token)) {
//			return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, null);
//		}
		Instant instant = Instant.now();
		Timestamp currentTimestamp = Timestamp.from(instant);
//
//		String jwtToken = token.substring(7);
//		List<UserDevice> userDeviceList = userDeviceRepository.getListUserDeviceByDeviceId(deviceId);
//		for (UserDevice userDevice : userDeviceList){
//			userDevice.setFirebaseToken(null);
//			userDeviceRepository.save(userDevice);
//		}

		UserDevice userDevice = userDeviceRepository.findByDeviceId(deviceId).orElse(new UserDevice());
        if (!Objects.isNull(userDevice)){
        	userDevice.setFirebaseToken(firebaseToken);
        	userDevice.setDeviceId(deviceId);
        	userDevice.setDeviceName(deviceName);
			userDevice.setDateCreateLogin(currentTimestamp);
        	userDeviceRepository.save(userDevice);
        	return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, null);
        }else {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, null);
        }
    }

}
