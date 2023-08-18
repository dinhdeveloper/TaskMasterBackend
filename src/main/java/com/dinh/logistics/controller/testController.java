package com.dinh.logistics.controller;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.dinh.logistics.dto.FirebaseDataDto;
import com.dinh.logistics.dto.SendFirebaseDto;
import com.dinh.logistics.model.UserDevice;
import com.dinh.logistics.respository.UserDeviceRepository;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;

@RestController
@RequestMapping("/api/test")
@Transactional
public class testController {
	
	@Value("${app.firebase.serverKey}")
    private String fireBaseServerKey;
	
	@Autowired
	UserDeviceRepository userDeviceRepository;

	@PostMapping("/test1")
	public ResponseEntity<Object> test1(@RequestParam String deviceId){
		try {
			UserDevice userDevice = userDeviceRepository.findByDeviceId(deviceId).orElse(null);
			if(userDevice == null) {
				return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, "Không tìm thấy device");
			}
			RestTemplate restTemplate = new RestTemplate();
	        String apiUrl = "https://fcm.googleapis.com/fcm/send";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.set("Authorization", "key=" + fireBaseServerKey);

	        // Dữ liệu JSON để gửi
//	        SendFirebaseDto sendFirebase = new SendFirebaseDto();
            FirebaseDataDto sendFirebaseData = new FirebaseDataDto();
            sendFirebaseData.setTitle("test");
            sendFirebaseData.setType("test");
            sendFirebaseData.setData("Sống chết có số");
//            sendFirebase.setTo(userDevice.getAccessToken());
//            sendFirebase.setData(sendFirebaseData);

            Gson gson = new Gson();
            String jsonData = gson.toJson(sendFirebaseData);
            
            //
            Message message = Message.builder()
            		.setToken(userDevice.getFirebase_token())
            		.putData("data", jsonData)
            		.build();
            String response = FirebaseMessaging.getInstance().send(message);
            
//	        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

	        // Gửi yêu cầu POST và nhận phản hồi
//	        String jsonResponse = restTemplate.postForObject(apiUrl, request, String.class);

	        return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, response);

        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, e);
        }
    }
	
	@PostMapping("/test2")
	public ResponseEntity<Object> test2(@RequestParam String token){
		try {
			
			RestTemplate restTemplate = new RestTemplate();
	        String apiUrl = "https://fcm.googleapis.com/fcm/send";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.set("Authorization", "key=" + fireBaseServerKey);

	        // Dữ liệu JSON để gửi
//	        SendFirebaseDto sendFirebase = new SendFirebaseDto();
            FirebaseDataDto sendFirebaseData = new FirebaseDataDto();
            sendFirebaseData.setTitle("test");
            sendFirebaseData.setType("test");
            sendFirebaseData.setData("Sống chết có số");
//            sendFirebase.setTo(token);
//            sendFirebase.setData(sendFirebaseData);

            Gson gson = new Gson();
            String jsonData = gson.toJson(sendFirebaseData);
            
            //
            Message message = Message.builder()
            		.setToken(token)
            		.putData("data", jsonData)
            		.build();
            String response = FirebaseMessaging.getInstance().send(message);
            
//	        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

	        // Gửi yêu cầu POST và nhận phản hồi
//	        String jsonResponse = restTemplate.postForObject(apiUrl, request, String.class);

	        return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, response);
			
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, e);
        }
    }
	
}
