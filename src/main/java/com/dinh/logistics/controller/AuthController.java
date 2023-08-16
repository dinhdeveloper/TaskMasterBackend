package com.dinh.logistics.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

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
import com.dinh.logistics.model.LoginUser;
import com.dinh.logistics.model.User;
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
	
	@PostMapping("/login")
    public ResponseEntity<Object> loginUser(@Valid @RequestBody LoginDto loginDto)
            throws RecordNotFoundException {
		if(StringUtils.isEmpty(loginDto.getUsername()) || StringUtils.isEmpty(loginDto.getPassword())) {
			return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.ERROR, null);
		}
		User user = userRepository.findByUsername(loginDto.getUsername()).orElse(null);
        if (user != null){
        	try {
        		MessageDigest sha256 = MessageDigest.getInstance(loginDto.getPassword());
	            byte[] hash = sha256.digest(loginDto.getPassword().getBytes(StandardCharsets.UTF_8));
	            
	            // Convert the hash bytes to hexadecimal representation
	            StringBuilder hexString = new StringBuilder();
	            for (byte b : hash) {
	                hexString.append(String.format("%02x", b));
	            }
	            if(StringUtils.equals(user.getPassword(), hexString.toString())) {
	            	String token = tokenGenerator.generateToken(loginDto.getUsername(), loginDto.getPassword());
	            	tokenManager.addToken(loginDto.getUsername(), token);
	            	Authentication auth = new Authentication();
	            	auth.setTokenAuth(token);
	            	return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, auth);
	            } else {
	            	return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.ERROR, "Sai mat khau");
	            }
        	} catch (Exception e) {
        		return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.ERROR, e);
        	}
        }else {
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.ERROR, "Khong tim thay user");
        }
    }
	
}
