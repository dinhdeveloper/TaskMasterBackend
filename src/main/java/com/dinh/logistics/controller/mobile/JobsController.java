package com.dinh.logistics.controller.mobile;

import com.dinh.logistics.dto.mobile.AddJobsDto;
import com.dinh.logistics.dto.mobile.JobDetailsDTO;
import com.dinh.logistics.dto.mobile.UpdateJobsResponse;
import com.dinh.logistics.service.mobile.JobsService;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/mobile")
@Slf4j
@Transactional
public class JobsController {

    @Autowired
    private JobsService jobsService;

    @GetMapping("/details/{id}")
    public ResponseEntity<Object> jobsDetails(@PathVariable Integer id){
        JobDetailsDTO jobDetails = jobsService.jobsDetails(id);
        if (jobDetails != null) {
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, jobDetails);
        } else {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, null);
        }
    }

    @PutMapping("/update/update_state_job/{jobId}/{newStateId}")
    public ResponseEntity<Object> updateStateJob(
            @PathVariable Integer jobId,
            @PathVariable Integer newStateId) {
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
}
