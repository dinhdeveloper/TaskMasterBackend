package com.dinh.logistics.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dinh.logistics.model.UserDevice;
import com.dinh.logistics.respository.UserDeviceRepository;

@Service
public class TokenManager {
	
	@Value("${app.jwtSecret}")
    private String jwtSecret;
	
	@Autowired
	UserDeviceRepository userDeviceRepository;
	
    private static final Map<String, String> tokenMap = new ConcurrentHashMap<>();

    public void addToken(UserDevice userDevice) {
    	userDevice.setIsActiveAccessToken(true);
    	userDeviceRepository.save(userDevice);
    }

    public static String getToken(String username) {
        return tokenMap.get(username);
    }

    public static boolean containsToken(String username, String token) {
        String storedToken = getToken(username);
        return storedToken != null && storedToken.equals(token);
    }

    public static void removeToken(String username) {
        tokenMap.remove(username);
    }
    
    
}
