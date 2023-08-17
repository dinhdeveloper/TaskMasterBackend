package com.dinh.logistics.respository.mobile;

import com.dinh.logistics.model.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository  extends JpaRepository<JobType, Integer> {
}
