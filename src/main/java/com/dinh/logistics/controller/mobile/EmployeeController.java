package com.dinh.logistics.controller.mobile;

import com.dinh.logistics.exception.ResourceNotFoundException;
import com.dinh.logistics.model.Employee;
import com.dinh.logistics.respository.mobile.EmployeeRepository;
import com.dinh.logistics.service.mobile.EmployeeService;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        List<Employee> employeeList = employeeRepository.findAll();
        if (employeeList.isEmpty()){
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, null);
        }else {
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, employeeList);
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
        }else {
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, employeeList);
        }
    }
}
