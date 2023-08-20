package com.dinh.logistics.controller.mobile;

import com.dinh.logistics.exception.ResourceNotFoundException;
import com.dinh.logistics.model.Employee;
import com.dinh.logistics.model.JobType;
import com.dinh.logistics.respository.EmployeeRepository;
import com.dinh.logistics.service.mobile.EmployeeService;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mobile/")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;
    private List<Employee> employeeList;

    // get all employees
    @GetMapping("/employees")
    public ResponseEntity<Object> getAllEmployees(){
        List<Employee> data = employeeRepository.findAll();
        if (data.isEmpty()){
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, null);
        } else {
            List<Employee> employeeDTOList = new ArrayList<>();
            for (Employee jobType : data) {
                employeeDTOList.add(jobType);
            }
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("listItem", employeeDTOList);

            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, responseData);
        }
    }

    // get employee by id rest api
    @GetMapping("/employees/{id}")
    public ResponseEntity<Object> getEmployeeById(@PathVariable Integer id) {
        if (id == -1){
            employeeList = employeeRepository.findAll();
        }else {
            employeeList = employeeService.getData(id);
        }
        if (employeeList.isEmpty()){
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, null);
        } else {
            List<Employee> dTOList = new ArrayList<>();
            for (Employee jobType : employeeList) {
                dTOList.add(jobType);
            }
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("listItem", dTOList);

            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, responseData);
        }
    }
}
