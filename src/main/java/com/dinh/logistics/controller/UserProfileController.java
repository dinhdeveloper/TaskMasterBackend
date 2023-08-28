package com.dinh.logistics.controller;


import com.dinh.logistics.model.UserProfileInfo;
import com.dinh.logistics.service.UserProfileService;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/mobile")
@Slf4j
@Transactional
public class UserProfileController {
    @Autowired
    UserProfileService userProfileService;

    @GetMapping("/userprofile/{username}")
    public ResponseEntity<Object> getEmployeeById(@PathVariable String username){
        UserProfileInfo data = userProfileService.findEmployeesByUsername(username);
        if (data == null){
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, null);
        } else {
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, data);
        }
    }
}
