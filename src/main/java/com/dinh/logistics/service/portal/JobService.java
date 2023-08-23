package com.dinh.logistics.service.portal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dinh.logistics.dto.portal.JobListDto;
import com.dinh.logistics.respository.JobRepository;

public interface JobService {

	List<JobListDto> getAllJobByFilter(String startDate, String endDate, int page, int size);

	
}
