package com.dinh.logistics.controller;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dinh.logistics.dto.FirebaseBodyDto;
import com.dinh.logistics.dto.SendFirebaseDto;
import com.dinh.logistics.model.UserDevice;
import com.dinh.logistics.respository.UserDeviceRepository;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;
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
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=" + fireBaseServerKey);
            conn.setRequestProperty("Content-Type", "application/json");

            conn.setDoOutput(true);
            
            SendFirebaseDto sendFirebase = new SendFirebaseDto();
            FirebaseBodyDto sendFirebaseBody = new FirebaseBodyDto();
            sendFirebaseBody.setMessage("chúc mừng năm mới!!!");
            sendFirebase.setTo(userDevice.getFirebase_token());
            sendFirebase.setData(sendFirebaseBody);

            Gson gson = new Gson();
            String input = gson.toJson(sendFirebase);
            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            conn.disconnect();
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, null);

        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, e);
        }
    }
}
