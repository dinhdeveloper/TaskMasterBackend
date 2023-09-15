package com.dinh.logistics.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dinh.logistics.dto.Authentication;
import com.dinh.logistics.dto.LoginDto;
import com.dinh.logistics.exception.RecordNotFoundException;
import com.dinh.logistics.model.Employee;
import com.dinh.logistics.model.RolePj;
import com.dinh.logistics.model.UserDevice;
import com.dinh.logistics.model.Users;
import com.dinh.logistics.respository.EmployeeRepository;
import com.dinh.logistics.respository.RolePjRepository;
import com.dinh.logistics.respository.UserDeviceRepository;
import com.dinh.logistics.respository.UserRepository;
import com.dinh.logistics.service.TokenGenerator;
import com.dinh.logistics.service.TokenManager;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;
import org.apache.commons.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@Transactional
public class AuthController {
	
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TokenManager tokenManager;
	
	@Autowired
	TokenGenerator tokenGenerator;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	RolePjRepository rolePjRepository;
	
	@Autowired UserDeviceRepository userDeviceRepository;
	
	@PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody LoginDto loginDto){
		if(StringUtils.isEmpty(loginDto.getUsername()) || StringUtils.isEmpty(loginDto.getPassword())) {
			return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.ERROR, null);
		}
		Users user = userRepository.findByUserName(loginDto.getUsername()).orElse(null);
        if (user != null){
        	try {
        		MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
	            byte[] hash = sha256.digest(loginDto.getPassword().getBytes(StandardCharsets.UTF_8));
	            
	            // Convert the hash bytes to hexadecimal representation
	            StringBuilder hexString = new StringBuilder();
	            for (byte b : hash) {
	                hexString.append(String.format("%02x", b));
	            }
				Instant instant = Instant.now();
				// Chuyển đổi Instant thành Timestamp
				Timestamp currentTimestamp = Timestamp.from(instant);

	            if(StringUtils.equals(user.getPassword(), hexString.toString())) {
	            	String token = tokenGenerator.generateToken(loginDto.getUsername(), loginDto.getPassword());
	            	UserDevice userDevice = userDeviceRepository.findByUserId(user.getUser_id()).orElse(new UserDevice());
	            	userDevice.setAccessToken(token);
	            	userDevice.setUserId(user.getUser_id().intValue());
	            	userDevice.setDeviceId(loginDto.getDeviceId());
	            	userDevice.setFirebaseToken(null);
	            	userDevice.setDeviceName(loginDto.getDeviceName());
	            	userDevice.setDateCreateLogin(currentTimestamp);
	            	tokenManager.addToken(userDevice);
	            	
	            	
	            	
	            	Authentication auth = new Authentication();
	            	auth.setTokenAuth(token);
	            	if(user.getEmployeeId() != 0) {
	            		Employee empl = employeeRepository.findById(user.getEmployeeId()).orElse(null);
		            	if(empl != null) {
		            		if(!empl.isState()) {
		            			return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, "User bị khóa!");
		            		}
		            		
		            		RolePj role = rolePjRepository.findById(empl.getRoleId()).orElse(null);
		            		auth.setRole(role.getRoleCode());
		            		if(StringUtils.equalsIgnoreCase(role.getRoleCode(), "ADMIN") || StringUtils.equalsIgnoreCase(role.getRoleCode(), "MASTER")) {
		            			//
		            		}else {
//		            			return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, "Role này không có quyền!");
		            		}
		            	}
	            	}else {
	            		auth.setRole("CUSTOMER");
	            	}
	            	
	            	return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, auth);
	            } else {
	            	return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, "Sai mật khẩu");
	            }
        	} catch (Exception e) {
        		return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, e);
        	}
        }else {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, "Tên đăng nhập không tồn tại");
        }
    }
	
	@PostMapping("/logout")
    public ResponseEntity<Object> logoutUser(HttpServletRequest request){
		String token = request.getHeader("Authorization");
		if(StringUtils.isBlank(token)) {
			return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, null);
		}
		String jwtToken = token.substring(7);
		UserDevice userDevice = userDeviceRepository.findByAccessTokenAndIsActiveAccessTokenTrue(jwtToken).orElse(new UserDevice());
        if (!Objects.isNull(userDevice)){
        	userDeviceRepository.updateIsActiveAccessTokenByUserId(false, userDevice.getUserId());
        	return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, null);
        }else {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, null);
        }
    }
	
}
