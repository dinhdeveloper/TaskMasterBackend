package com.dinh.logistics.controller.mobile;

import com.dinh.logistics.dto.mobile.AddJobsDto;
import com.dinh.logistics.dto.mobile.CollectPointDto;
import com.dinh.logistics.service.mobile.JobsService;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/mobile")
@Slf4j
@Transactional
public class JobsController {

    @Autowired
    private JobsService jobsService;
    @PostMapping("/add_job")
    public ResponseEntity<Object> addCollectPoint(@RequestBody AddJobsDto addJobsDto){
        if (jobsService.addJobs(addJobsDto)) {
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, "Giao việc thành công");
        } else {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, "Giao việc thất bại");
        }
    }
}
