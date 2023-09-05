package com.dinh.logistics.controller.portal;

import java.io.File;
import java.io.FileInputStream;
import java.sql.ResultSet;

import javax.transaction.Transactional;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dinh.logistics.dao.TableDao;
import com.dinh.logistics.service.portal.ExcelFileService;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/portal/tables")
@Slf4j
@Transactional
public class TableController {
	
	@Autowired
	TableDao tableDao;
	
	@Autowired
	ExcelFileService excelFileService;

	@GetMapping("/export")
    public ResponseEntity<Resource> jobsExport(@RequestParam String tableName){
		try {
			
        	File outputFile = tableDao.exportToExcelWithResultSet(" select * from " + tableName, "data", 1, 1);
        	
        	// Tạo ResponseEntity để trả về tệp xuất ra để tải về
            InputStreamResource resource = new InputStreamResource(new FileInputStream(outputFile));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + tableName + ".xlsx");
        	
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
	
	@PostMapping("/upload")
    public ResponseEntity<Object> uploadJobs(@RequestParam(name = "file", required = true) MultipartFile file,
    		@RequestParam(name = "tableName", required = true) String tableName){
        try {
        	//upload jobs
        	if(StringUtils.equalsIgnoreCase(tableName, "customers")) {
        		excelFileService.uploadTableCustomers(file);
        	}else if(StringUtils.equalsIgnoreCase(tableName, "collect_point")) {
        		excelFileService.uploadTableCollectPoint(file);
        	}else if(StringUtils.equalsIgnoreCase(tableName, "users")) {
        		excelFileService.uploadTableUser(file);
        	}else {
        		return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, "Không tìm thấy bảng");
        	}
        	
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, null);
        }catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, e);
        }
    }
}
