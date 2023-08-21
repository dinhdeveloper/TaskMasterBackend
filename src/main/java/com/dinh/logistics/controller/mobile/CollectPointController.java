package com.dinh.logistics.controller.mobile;

import com.dinh.logistics.dto.mobile.CollectPointDto;
import com.dinh.logistics.model.CollectPoint;
import com.dinh.logistics.model.Employee;
import com.dinh.logistics.model.JobType;
import com.dinh.logistics.respository.CollectPointRepository;
import com.dinh.logistics.respository.EmployeeRepository;
import com.dinh.logistics.service.mobile.CollectPointService;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mobile")
@Slf4j
@Transactional
public class CollectPointController {

    @Autowired
    private CollectPointRepository collectPointRepository;

    @Autowired
    private CollectPointService pointService;

    // get all employees
    @GetMapping("/collect_point")
    public ResponseEntity<Object> getAllEmployees(){
        List<CollectPoint> collectPoints = collectPointRepository.findAll();
        if (collectPoints.isEmpty()){
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, null);
        } else {
            List<CollectPoint> dTOList = new ArrayList<>();
            for (CollectPoint jobType : collectPoints) {
                dTOList.add(jobType);
            }
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("listItem", dTOList);

            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, responseData);
        }
    }

    @PostMapping("/add_collect_point")
    public ResponseEntity<Object> addCollectPoint(@RequestBody CollectPointDto collectPoint){
        if (pointService.addCollectPoint(collectPoint)) {
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, "Thêm đia điểm thành công");
        } else {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, "Thêm đia điểm thất bại");
        }
    }
}
