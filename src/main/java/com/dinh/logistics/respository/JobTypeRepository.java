package com.dinh.logistics.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dinh.logistics.model.JobType;

@Repository
public interface JobTypeRepository extends JpaRepository<JobType, Integer>{

	@Query(value = "select job_type_id from job_type where state = true", nativeQuery = true)
	List<Integer> findAllJobTypeId();

	@Query(value = "select * from job_type where state = true", nativeQuery = true)
	List<JobType> findAllJobTypeByState();

}
