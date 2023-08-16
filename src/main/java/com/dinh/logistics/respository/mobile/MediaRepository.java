package com.dinh.logistics.respository.mobile;

import com.dinh.logistics.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Employee, Long> {
}