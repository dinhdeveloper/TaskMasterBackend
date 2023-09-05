package com.dinh.logistics.controller;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
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
	
	@Autowired
	UserDeviceRepository userDeviceRepository;
	
	@PersistenceContext
    private EntityManager entityManager;

	@PostMapping("/test1")
	public ResponseEntity<Object> test1(@RequestParam String deviceId){
		try {
			UserDevice userDevice = userDeviceRepository.findByDeviceId(deviceId).orElse(null);
			if(userDevice == null) {
				return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, "Không tìm thấy device");
			}

	        // Dữ liệu JSON để gửi
            FirebaseDataDto sendFirebaseData = new FirebaseDataDto();
            sendFirebaseData.setTitle("test");
            sendFirebaseData.setType("test");
            sendFirebaseData.setData("Con gà");

            Gson gson = new Gson();
            String jsonData = gson.toJson(sendFirebaseData);
            
            // Gửi
            Message message = Message.builder()
            		.setToken(userDevice.getFirebaseToken())
            		.putData("data", jsonData)
            		.build();
            String response = FirebaseMessaging.getInstance().send(message);
            
	        return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, response);

        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, e);
        }
    }
	
	@PostMapping("/test2")
	public ResponseEntity<Object> test2(@RequestParam String token,
			@RequestParam String title,
			@RequestParam String type,
			@RequestParam String message){
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

	        return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, response);
			
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, e);
        }
    }
	
	@PostMapping("/test3")
	public ResponseEntity<Object> test3(@RequestParam String startDate,
			@RequestParam String endDate,
			@RequestParam Integer size,
			@RequestParam Integer page){
		try {
			
			StringBuilder builder = new StringBuilder();

	        builder.append(" SELECT j.job_id, cp.num_address, j.creation_time, jt.job_type_name, j.priority, j.note ");
	        builder.append(" FROM jobs j ");
	        builder.append(" LEFT JOIN collect_point cp on cp.colle_point_id = j.colle_point_id ");
	        builder.append(" LEFT JOIN job_type jt on jt.job_type_id = j.job_type_id ");
	        builder.append(" WHERE 1=1 ");
	        generateSearchFilter(startDate, endDate, builder, false);
	        Query query = entityManager.createNativeQuery(builder.toString());

	        setSearchFilter(startDate,endDate,query);

//	        query.setFirstResult((page - 1) * size);
//	        query.setMaxResults(size);
	        List<Object[]> results = query.getResultList();

	        return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, null);
			
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, e);
        }
    }
	
	public void generateSearchFilter(String startDate,String endDate, StringBuilder stringBuilder, boolean isCount){

    	if (!StringUtils.isEmpty(startDate)) {
            stringBuilder.append(" AND DATE_TRUNC('day', j.creation_time) >= TO_DATE(:startDate,'dd/MM/yyyy') ");
        }

        if (!StringUtils.isEmpty(endDate)) {
            stringBuilder.append(" AND DATE_TRUNC('day', j.creation_time) <= TO_DATE(:endDate,'dd/MM/yyyy') ");
        }

        if(isCount == false) {
        	stringBuilder.append(" ORDER BY j.creation_time desc");
        }
        
    }
	
	public void setSearchFilter(String startDate,String endDate, Query query){

        if (!StringUtils.isEmpty(startDate)) {
            query.setParameter("startDate",startDate);
        }

        if (!StringUtils.isEmpty(endDate)) {
            query.setParameter("endDate",endDate);
        }
        
    }
	
}
