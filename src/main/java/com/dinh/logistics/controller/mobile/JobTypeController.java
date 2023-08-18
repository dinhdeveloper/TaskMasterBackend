package com.dinh.logistics.controller.mobile;

import com.dinh.logistics.dto.LoginDto;
import com.dinh.logistics.model.JobType;
import com.dinh.logistics.respository.JobRepository;
import com.dinh.logistics.respository.MediaRepository;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/api/mobile")
@Slf4j
@Transactional
public class JobTypeController {

    @Autowired
    JobRepository jobRepository;

    @GetMapping("/job_type/list")
    public ResponseEntity<Object> getListJobType(){
//        return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS,
//                jobRepository.findAll(Sort.by(Sort.Direction.ASC, "job_type_id")));

        List<JobType> data = jobRepository.findAll();
        if (data.isEmpty()){
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, null);
        }else {
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, data);
        }
    }
}
