package com.dinh.logistics.controller.mobile;

import com.dinh.logistics.dao.JobDao;
import com.dinh.logistics.dto.mobile.AddJobsDto;
import com.dinh.logistics.dto.mobile.JobDetailsDTO;
import com.dinh.logistics.dto.mobile.JobSearchResponse;
import com.dinh.logistics.dto.mobile.JobSearchResponseDto;
import com.dinh.logistics.dto.mobile.UpdateJobsResponse;
import com.dinh.logistics.model.UserDevice;
import com.dinh.logistics.respository.UserDeviceRepository;
import com.dinh.logistics.service.mobile.JobsService;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/mobile")
@Slf4j
@Transactional
public class JobsController {

    @Autowired
    private JobsService jobsService;
    
    @Autowired
    UserDeviceRepository userDeviceRepository;
    
    @Autowired
    JobDao jobDao;

    @GetMapping("/details/{id}")
    public ResponseEntity<Object> jobsDetails(@PathVariable Integer id){
        JobDetailsDTO jobDetails = jobsService.jobsDetails(id);
        if (jobDetails != null) {
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, jobDetails);
        } else {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, null);
        }
    }

    @PutMapping("/update/update_state_job/{jobId}/{newStateId}/{dateCreate}")
    public ResponseEntity<Object> updateStateJob(
            @PathVariable Integer jobId,
            @PathVariable Integer newStateId,
            @PathVariable String dateCreate
    ) {
        try {
            jobsService.updateStateJob(jobId, newStateId);
            UpdateJobsResponse response = new UpdateJobsResponse();
            response.setStateJobs(newStateId);
            response.setDescription("Cập nhật thành công");
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, response);
        } catch (Exception e) {
            log.error("Error updating job state: {}", e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, "Cập nhật thất bại");
        }
    }

    @PostMapping("/add_job")
    public ResponseEntity<Object> addJobs(@RequestBody AddJobsDto addJobsDto){
        if (jobsService.addJobs(addJobsDto)) {
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, "Giao việc thành công");
        } else {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, "Giao việc thất bại");
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "size", defaultValue = "1") Integer size,
			@RequestParam(name = "startDate",required = false,defaultValue = "")  String startDate,
			@RequestParam(name = "endDate",required = false,defaultValue = "")  String endDate,
			@RequestParam(name = "empStatus",required = false,defaultValue = "")  Integer empStatus,
			@RequestParam(name = "empId",required = false,defaultValue = "")  Integer empId,
			@RequestParam(name = "status",required = false,defaultValue = "")  Integer status,
			@RequestParam(name = "paymentStatus",required = false,defaultValue = "")  Integer paymentStatus,
			@RequestParam(name = "jobId",required = false,defaultValue = "")  Integer jobId,
			@RequestParam(name = "collectPoint",required = false,defaultValue = "")  String collectPoint){
    	
    	JobSearchResponse jobSearchResponse = new JobSearchResponse();
    	jobSearchResponse = jobsService.searchJobByFilter(empStatus, empId, status, paymentStatus, startDate, endDate, jobId, collectPoint);
    	
    	return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, jobSearchResponse);
    }
    
}
