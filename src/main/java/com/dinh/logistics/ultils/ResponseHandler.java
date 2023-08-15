package com.dinh.logistics.ultils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    public static ResponseEntity<Object> generateResponse(HttpStatus status, int result_code, StatusResult statusResult, Object responseObj) {

        String result_description = statusResult.name();

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map.put("result_code", result_code);
            map.put("result_description", result_description);
            map.put("data", responseObj);
            map.put("timestamp", new Date());
            map.put("code_status", status.value());

            return new ResponseEntity<Object>(map,status);
        } catch (Exception e) {
            map.clear();
            map.put("result_code",-99);
            map.put("result_description", e.getMessage());
            map.put("data", null);
            map.put("timestamp", new Date());
            map.put("code_status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<Object>(map,status);
        }
    }
}
