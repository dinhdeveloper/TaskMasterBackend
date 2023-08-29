package com.dinh.logistics.service.mobile;


import com.dinh.logistics.model.Employee;
import com.dinh.logistics.respository.EmployeeRepository;
import com.dinh.logistics.respository.mobile.EmployeeRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeRepositoryImp repositoryImp;



    public List<Employee> findEmployeesExcludingRolesAndId(Integer id) {
        if (id != null) {
            return employeeRepository.findEmployeesExcludingRolesAndId(id);
        }
        return employeeRepository.findEmployeesExcludingRoles();
    }

    public List<Employee> findEmployeesByJobId(Integer jobId) {
        if (jobId != null) {
            return repositoryImp.findEmployeesByJobId(jobId);
        }
        return employeeRepository.findEmployeesExcludingRoles();
    }
}
