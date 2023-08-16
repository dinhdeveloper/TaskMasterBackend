package com.dinh.logistics.respository.mobile;

import com.dinh.logistics.model.JobTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository  extends JpaRepository<JobTypeModel, Long> {
}
