package com.dinh.logistics.respository;

import com.dinh.logistics.model.Employee;
import com.dinh.logistics.model.JobMedia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<JobMedia, Integer> {
}