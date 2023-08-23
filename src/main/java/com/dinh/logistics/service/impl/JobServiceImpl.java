package com.dinh.logistics.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dinh.logistics.dao.JobDao;
import com.dinh.logistics.dto.portal.JobListDto;
import com.dinh.logistics.service.portal.JobService;

@Service
public class JobServiceImpl implements JobService{

	@Autowired
	JobDao jobDao;
	
	@Override
	public List<JobListDto> getAllJobByFilter(String startDate, String endDate, int page, int size) {
		return jobDao.getAllJobByFilter(startDate, endDate, page, size);
	}
}
