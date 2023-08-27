package com.dinh.logistics.respository.mobile;

import com.dinh.logistics.model.JobMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

@Transactional
public interface JobMaterialRepository extends JpaRepository<JobMaterial, Integer> {
    @Modifying
    @Query("DELETE FROM JobMaterial jm WHERE jm.jobMateId = :jobMateId")
    int deleteByJobIdAndJobMateId(@Param("jobMateId") int jobMateId);
}

