package com.dinh.logistics.dao;

import java.util.List;

import com.dinh.logistics.dto.portal.JobListDto;
import com.dinh.logistics.dto.portal.ReportListDto;

public interface ReportDao {

	List<ReportListDto> getReportByFilter(String startDate, String endDate, String cusName);

	int getCountAllJobByFilter(String startDate, String endDate, String cusName);

}
