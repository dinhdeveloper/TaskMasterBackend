package com.dinh.logistics.controller.mobile;

import com.dinh.logistics.dao.JobDao;
import com.dinh.logistics.dto.mobile.*;
import com.dinh.logistics.model.CollectPointLatLng;
import com.dinh.logistics.model.Jobs;
import com.dinh.logistics.model.NotifyTopic;
import com.dinh.logistics.respository.UserDeviceRepository;
import com.dinh.logistics.respository.mobile.UtilsNotification;
import com.dinh.logistics.service.mobile.JobsService;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    UtilsNotification utilsNotification;


    @GetMapping("/details/{jobId}/{empId}")
    public ResponseEntity<Object> jobsDetails(
            @PathVariable Integer jobId,
            @PathVariable Integer empId
            ){
        JobDetailsDTO jobDetails = jobsService.jobsDetails(jobId,empId);
        if (jobDetails != null) {
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, jobDetails);
        } else {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, null);
        }
    }

    @PutMapping("/update/update_state_job_weighted")
    public ResponseEntity<Object> updateStateJob(@RequestBody UpdateStateWeightedRequest updateStateWeightedRequest) {
        try {
            jobsService.updateStateWeightedJob(updateStateWeightedRequest);
            UpdateJobsResponse response = new UpdateJobsResponse();
            response.setStateJobs(updateStateWeightedRequest.getStateJob());
            response.setDescription("Cập nhật thành công");
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, response);
        } catch (Exception e) {
            log.error("Error updating job state: ", e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, "Cập nhật thất bại");
        }
    }

    @PutMapping("/update/update_state_job_compacted_done")
    public ResponseEntity<Object> updateStateJobCompactedAndDone(@RequestBody CompactedAndDoneRequest updateStateRequest) {
        try {
            jobsService.updateStateJobCompactedAndDone(updateStateRequest);
            UpdateJobsResponse response = new UpdateJobsResponse();
            response.setStateJobs(updateStateRequest.getStateJob());
            response.setDescription("Cập nhật thành công");
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, response);
        } catch (Exception e) {
            log.error("Error updating job state: ", e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, "Cập nhật thất bại");
        }
    }

    @PostMapping("/update_job_detail")
    public ResponseEntity<Object> updateJobSave(@RequestBody DataUpdateJobRequest dataUpdateJobRequest){
        Jobs jobs = jobsService.updateJobSave(dataUpdateJobRequest);
        if (jobs != null) {
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, "Cập nhật thành công");
        } else {
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
			@RequestParam(name = "empRequest",required = false,defaultValue = "")  Integer empRequest,
			@RequestParam(name = "collectPoint",required = false,defaultValue = "")  String collectPoint){
    	
    	JobSearchResponse jobSearchResponse = new JobSearchResponse();
    	jobSearchResponse = jobsService.searchJobByFilter(empStatus, empId, status, paymentStatus, startDate, endDate, jobId, collectPoint,empRequest);
    	
    	return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, jobSearchResponse);
    }

    @PostMapping("/mozi/{id}/{jobId}")
    public ResponseEntity<Object> saveA(@PathVariable Integer id, @PathVariable Integer jobId){
        List<NotifyTopic> notifyTopic = utilsNotification.pushNotifyByEmpId(id, jobId);
        return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, notifyTopic);
    }

    @GetMapping("/collect_point/latlng")
    public ResponseEntity<Object> getCollectPointLatLng(){
        List<CollectPointLatLng> collectPointLatLng = jobsService.getCollectPointLatLng();
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("listItem", collectPointLatLng);
        return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, responseData);
    }
}
