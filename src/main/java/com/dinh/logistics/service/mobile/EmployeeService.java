package com.dinh.logistics.service.mobile;


import com.dinh.logistics.model.Employee;
import com.dinh.logistics.respository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> findEmployeesExcludingRolesAndId(Integer id) {
        if (id != null) {
            return employeeRepository.findEmployeesExcludingRolesAndId(id);
        }
        return employeeRepository.findEmployeesExcludingRoles();
    }
}
