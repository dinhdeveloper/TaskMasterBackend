package com.dinh.logistics.respository;

import com.dinh.logistics.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query(value = "select * from employee where emp_id != ?1", nativeQuery = true)
    List<Employee> findAllByIdNot(Integer id);
    
    @Query(value = "select e.emp_id from employee e join role_pj rp on e.role_id = rp.role_id where rp.role_code = ?1", nativeQuery = true)
    List<Integer> findAllIdByRole(String roleCode);
}
