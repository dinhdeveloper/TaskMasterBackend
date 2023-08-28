package com.dinh.logistics.service;

import com.dinh.logistics.model.UserProfileInfo;
import com.dinh.logistics.respository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    UserProfileRepository userProfileRepository;

    public UserProfileInfo findEmployeesByUsername(String username) {
        return userProfileRepository.getUserInfoByUserName(username);
    }
}
