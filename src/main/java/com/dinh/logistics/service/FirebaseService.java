package com.dinh.logistics.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dinh.logistics.dto.FirebaseDataDto;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;

@Service
public class FirebaseService {
	
	public void SendMessageByToken(String token) {
		try {
			
	        // Dữ liệu JSON để gửi
            FirebaseDataDto sendFirebaseData = new FirebaseDataDto();
            sendFirebaseData.setTitle("test");
            sendFirebaseData.setType("test");
            sendFirebaseData.setData("Sống chết có số");

            Gson gson = new Gson();
            String jsonData = gson.toJson(sendFirebaseData);
            
            //Gửi
            Message message = Message.builder()
            		.setToken(token)
            		.putData("data", jsonData)
            		.build();
            String response = FirebaseMessaging.getInstance().send(message);
			
        } catch (Exception e) {
        	
        }
	}

}
