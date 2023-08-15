package com.dinh.logistics.controller;

import com.dinh.logistics.exception.RecordNotFoundException;
import com.dinh.logistics.model.Customer;
import com.dinh.logistics.model.LoginUser;
import com.dinh.logistics.service.CustomerService;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @RequestMapping("/list")
    public ResponseEntity<Object> getAllProduct() {
        List<Customer> list = customerService.getAllCustomer();
        if (list != null){
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, list);
        }else {
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.ERROR, null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@Valid @RequestBody LoginUser loginUser)
            throws RecordNotFoundException {
        List<Customer> requestLogin = customerService.loginUser(loginUser.getUserName(),loginUser.getPassWord());
        if (requestLogin != null){
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, requestLogin);
        }else {
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.ERROR, null);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchCustomers(@RequestParam("keyword") String keyword) {
        List<Customer> reponse = customerService.searchCustomers(keyword);
        if (reponse != null){
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, reponse);
        }else {
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.ERROR, null);
        }
    }

}
