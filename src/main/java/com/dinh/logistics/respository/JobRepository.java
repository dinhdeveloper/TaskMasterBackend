package com.dinh.logistics.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dinh.logistics.model.Jobs;

@Repository
public interface JobRepository  extends JpaRepository<Jobs, Integer> {
	
}
