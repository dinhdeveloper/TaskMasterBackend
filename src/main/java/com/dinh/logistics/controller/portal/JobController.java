package com.dinh.logistics.controller.portal;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dinh.logistics.controller.mobile.CollectPointController;
import com.dinh.logistics.dto.Authentication;
import com.dinh.logistics.dto.LoginDto;
import com.dinh.logistics.dto.portal.JobListResponseDto;
import com.dinh.logistics.model.CollectPoint;
import com.dinh.logistics.model.UserDevice;
import com.dinh.logistics.model.Users;
import com.dinh.logistics.service.portal.ExcelFileService;
import com.dinh.logistics.service.portal.JobManagement;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/portal/job")
@Slf4j
@Transactional
public class JobController {
	
	@Autowired
	ExcelFileService excelFileService;
	
	@Autowired
	JobManagement jobManagement;

	@PostMapping("/uploadJobs")
    public ResponseEntity<Resource> uploadJobs(@RequestParam(name = "file", required = true) MultipartFile file){
        try {
        	//upload jobs
        	File outputFile = excelFileService.uploadJobs(file);
        	
        	// Tạo ResponseEntity để trả về tệp xuất ra để tải về
            InputStreamResource resource = new InputStreamResource(new FileInputStream(outputFile));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CongViec-Upload-Report.xlsx");
        	
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(outputFile.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch(Exception e) {
        	e.printStackTrace();
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
	@GetMapping("/list")
    public ResponseEntity<Object> getJobList(@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "size", defaultValue = "1") Integer size,
			@RequestParam(name = "fromDate",required = false,defaultValue = "")  String startDate,
			@RequestParam(name = "toDate",required = false,defaultValue = "")  String endDate){
		
		try {
			JobListResponseDto response = new JobListResponseDto();
			response = jobManagement.getJobListResponse(startDate, endDate, page, size);
			
			return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, response);
        }catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, e);
        }
    }
	
	@GetMapping("/jobsExport")
    public ResponseEntity<Resource> jobsExport(@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "size", defaultValue = "1") Integer size,
			@RequestParam(name = "fromDate",required = false,defaultValue = "")  String startDate,
			@RequestParam(name = "toDate",required = false,defaultValue = "")  String endDate){
		
		try {
			
        	File outputFile = excelFileService.exportJobs(startDate, endDate, page, size);
        	
        	// Tạo ResponseEntity để trả về tệp xuất ra để tải về
            InputStreamResource resource = new InputStreamResource(new FileInputStream(outputFile));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=job-export.xlsx");
        	
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(outputFile.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch(Exception e) {
        	e.printStackTrace();
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
}
