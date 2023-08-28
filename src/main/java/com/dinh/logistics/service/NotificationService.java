package com.dinh.logistics.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.dinh.logistics.dto.FirebaseDataDto;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;

@Service
public class NotificationService {

	public String pushNotification(String token, String title, String type, String message) {
		try {
			// Dữ liệu JSON để gửi
            FirebaseDataDto sendFirebaseData = new FirebaseDataDto();
            sendFirebaseData.setTitle(title);
            sendFirebaseData.setType(type);
            sendFirebaseData.setData(message);

            Gson gson = new Gson();
            String jsonData = gson.toJson(sendFirebaseData);
            
            //Gửi
            Message mess = Message.builder()
            		.setToken(token)
            		.putData("data", jsonData)
            		.build();
            String response = FirebaseMessaging.getInstance().send(mess);
            
            return response;
		}catch (Exception e) {
			return null;
		}
	}
	
}
