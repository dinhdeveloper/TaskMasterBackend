package com.dinh.logistics.service.portal;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dinh.logistics.dao.ReportDao;
import com.dinh.logistics.dto.portal.JobListDto;
import com.dinh.logistics.dto.portal.JobListResponseDto;
import com.dinh.logistics.dto.portal.ReportListDto;
import com.dinh.logistics.dto.portal.reportListResponseDto;
import com.dinh.logistics.model.Employee;
import com.dinh.logistics.model.JobEmployee;
import com.dinh.logistics.ultils.AppConstants;

@Service
public class ReportManagement {

	@Autowired
	ReportDao reportDao;
	
	public List<Object[]> getReportListResponse(String startDate, String endDate, String cusName) {
		
		reportListResponseDto jobListResponseDto = new reportListResponseDto();
	
		List<Object[]> reportList = reportDao.getReportByFilter(startDate, endDate, cusName);
		
//		jobListResponseDto.setData(reportList);
//	
//		return jobListResponseDto;
		
		return reportList;
		
	}

}
