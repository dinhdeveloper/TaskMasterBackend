package com.dinh.logistics.controller.portal;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity<Resource> getAllEmployees(@RequestParam(name = "file", required = true) MultipartFile file){
        try {
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
	
}
