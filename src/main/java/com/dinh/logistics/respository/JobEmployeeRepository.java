package com.dinh.logistics.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dinh.logistics.model.JobEmployee;

@Repository
public interface JobEmployeeRepository extends JpaRepository<JobEmployee, Integer>{

}
