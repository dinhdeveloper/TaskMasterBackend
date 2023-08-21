package com.dinh.logistics.controller.portal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dinh.logistics.controller.mobile.CollectPointController;
import com.dinh.logistics.model.CollectPoint;
import com.dinh.logistics.service.portal.ExcelFileService;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/portal")
@Slf4j
@Transactional
public class JobController {
	
	@Autowired
	ExcelFileService excelFileService;

	@PostMapping("/uploadJobs")
    public ResponseEntity<Object> getAllEmployees(@RequestParam(name = "file", required = true) MultipartFile file){
        try {
        	excelFileService.uploadJobs(file);
        	
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, null);
        } catch(Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, e);
        }
    }
	
}
