package com.dinh.logistics.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dinh.logistics.dao.JobDao;
import com.dinh.logistics.dto.FirebaseDataDto;
import com.dinh.logistics.dto.UserDeviceDto;
import com.dinh.logistics.dto.portal.JobListDto;
import com.dinh.logistics.model.Employee;
import com.dinh.logistics.model.JobEmployee;
import com.dinh.logistics.model.Jobs;
import com.dinh.logistics.model.Team;
import com.dinh.logistics.model.UserDevice;
import com.dinh.logistics.model.Users;
import com.dinh.logistics.respository.EmployeeRepository;
import com.dinh.logistics.respository.JobEmployeeRepository;
import com.dinh.logistics.respository.JobRepository;
import com.dinh.logistics.respository.RolePjRepository;
import com.dinh.logistics.respository.TeamRepository;
import com.dinh.logistics.respository.UserDeviceRepository;
import com.dinh.logistics.respository.UserRepository;
import com.dinh.logistics.respository.mobile.JobsRepositoryImp;
import com.dinh.logistics.service.NotificationService;
import com.dinh.logistics.ultils.AppConstants;
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
	
	@Autowired
	RolePjRepository rolePjRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserDeviceRepository userDeviceRepository;
	
	@Autowired
	TeamRepository teamRepository;
	
	@PostMapping("/pushNoti")
	public ResponseEntity<Object> test2(@RequestParam List<Integer> ids){
		try {
			
			List<JobListDto> jobList = jobDao.getAllJobNotiByFilter(ids);
			
			for(JobListDto job : jobList) {
				List<JobEmployee> jobEmplList = jobEmployeeRepository.findAllByJobId(job.getId());
				for(JobEmployee JobEmployee : jobEmplList) {
					Employee employee = employeeRepository.findById(JobEmployee.getEmpId()).orElse(null);
					if(employee != null) {
						// Nội dung noti
						String title = "new work";
						String type = "work";
						String message = employee.getName() + "," + job.getJobType() + "," + job.getCollectPoint();
						
						// Bắn noti cho nv
						Users user = userRepository.findByEmployeeId(employee.getEmpId());
						UserDevice userDevice = userDeviceRepository.findByUserId(user.getUser_id()).orElse(null);
						
						if(userDevice != null && !StringUtils.isEmpty(userDevice.getFirebaseToken())) {
							notificationService.pushNotification(userDevice.getFirebaseToken(), title, type, message);
						}
						
						// Bắn noti cho leader
						Team team = teamRepository.findById(employee.getEmpId()).orElse(null);
						Users lead = userRepository.findByEmployeeId(team.getLeaderId());
						UserDevice userDeviceLead = userDeviceRepository.findByUserId(lead.getUser_id()).orElse(null);
						
						if(userDeviceLead != null && !StringUtils.isEmpty(userDeviceLead.getFirebaseToken())) {
							notificationService.pushNotification(userDeviceLead.getFirebaseToken(), title, type, message);

						}
						
					}
				}
			}
			
			//
//			String title = "new work";
//			String type = "work";
//			String message = "con gà";
//			
//			List<UserDeviceDto> userDeviceList = jobDao.getListUserDeviceToPushNotification(ids);
//			
//			for(UserDeviceDto userDevice : userDeviceList) {
//				notificationService.pushNotification(userDevice.getFirebaseToken(), title, type, message);
//			}
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
