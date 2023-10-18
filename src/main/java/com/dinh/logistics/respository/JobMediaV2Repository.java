package com.dinh.logistics.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dinh.logistics.model.JobMedia;

@Repository
public interface JobMediaV2Repository extends JpaRepository<JobMedia, Integer> {

}
