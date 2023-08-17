package com.dinh.logistics.respository.mobile;

import com.dinh.logistics.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query(value = "select * from employee where emp_id != ?1", nativeQuery = true)
    List<Employee> findAllByIdNot(Integer id);
}
