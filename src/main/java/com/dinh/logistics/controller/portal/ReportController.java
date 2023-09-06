package com.dinh.logistics.controller.portal;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dinh.logistics.dto.portal.JobListResponseDto;
import com.dinh.logistics.dto.portal.reportListResponseDto;
import com.dinh.logistics.service.portal.ReportManagement;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/portal/report")
@Slf4j
@Transactional
public class ReportController {

	@Autowired
	ReportManagement reportManagement;
	
	@GetMapping("/list")
    public ResponseEntity<Object> getJobList(@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "size", defaultValue = "1") Integer size,
			@RequestParam(name = "fromDate",required = false,defaultValue = "")  String startDate,
			@RequestParam(name = "toDate",required = false,defaultValue = "")  String endDate,
			@RequestParam(name = "cusName",required = false,defaultValue = "")  String cusName
			){
		try {
//			reportListResponseDto response = new reportListResponseDto();
			
			List<Object[]> listObj = reportManagement.getReportListResponse(startDate, endDate, cusName);
			
			// Tạo danh sách mới để chứa danh sách con List<String>
	        List<List<String>> resultList = new ArrayList<>();

	        // Trích xuất giá trị từ danh sách ban đầu và chuyển đổi thành danh sách con
	        for (Object[] itemArray : listObj) {
	            List<String> subList = new ArrayList<>();
	            for (Object item : itemArray) {
	            	if(item == null) {
	            		subList.add(null);
	            	}else {
	            		subList.add(item.toString()); // Chuyển đổi thành chuỗi
	            	}
	            }
	            resultList.add(subList);
	        }
			
			return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, resultList);
        }catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, e);
        }
    }
	
}
