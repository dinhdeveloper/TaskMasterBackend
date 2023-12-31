package com.dinh.logistics.respository;

import com.dinh.logistics.model.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query(value = "SELECT * FROM employee e JOIN role_pj rp ON e.role_id = rp.role_id WHERE rp.role_code NOT IN ('MASTER', 'ADMIN') AND e.emp_id != ?1 and emp_id != 0 ORDER BY e.name ASC", nativeQuery = true)
    List<Employee> findEmployeesExcludingRolesAndId(Integer id);
    
    @Query(value = "select e.emp_id from employee e join role_pj rp on e.role_id = rp.role_id where rp.role_code = ?1 AND e.state = true", nativeQuery = true)
    List<Integer> findAllIdByRole(String roleCode);

    @Query(value = "SELECT * FROM employee e JOIN role_pj rp ON e.role_id = rp.role_id WHERE rp.role_code NOT IN ('MASTER', 'ADMIN') AND e.state = true and emp_id != 0 ORDER BY e.name ASC", nativeQuery = true)
    List<Employee> findEmployeesExcludingRoles();

}
