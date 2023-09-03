package com.dinh.logistics.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dinh.logistics.dao.JobDao;
import com.dinh.logistics.dto.FirebaseDataDto;
import com.dinh.logistics.dto.UserDeviceDto;
import com.dinh.logistics.model.JobEmployee;
import com.dinh.logistics.model.Jobs;
import com.dinh.logistics.model.UserDevice;
import com.dinh.logistics.respository.JobEmployeeRepository;
import com.dinh.logistics.respository.JobRepository;
import com.dinh.logistics.respository.mobile.JobsRepositoryImp;
import com.dinh.logistics.service.NotificationService;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;

@RestController
@RequestMapping("/api/notification")
@Transactional
public class NotificationController {
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	JobRepository jobsRepository;
	
	@Autowired
	JobEmployeeRepository jobEmployeeRepository;
	
	@Autowired
	JobDao jobDao;
	
	@PostMapping("/pushNoti")
	public ResponseEntity<Object> test2(@RequestParam List<Integer> ids){
		try {
			String title = "test";
			String type = "test";
			String message = "con gà";
			
			List<UserDeviceDto> userDeviceList = jobDao.getListUserDeviceToPushNotification(ids);
			
			for(UserDeviceDto userDevice : userDeviceList) {
				notificationService.pushNotification(userDevice.getFirebaseToken(), title, type, message);
			}
			return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, e);
        }
    }

    @GetMapping("/list/notify")
	public ResponseEntity<Object> getListNotify(@PathVariable Integer empId){
		return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, null);
	}
}
