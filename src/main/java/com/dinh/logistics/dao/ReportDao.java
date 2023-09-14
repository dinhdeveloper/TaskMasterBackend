package com.dinh.logistics.dao;

import java.io.File;
import java.util.List;

import com.dinh.logistics.dto.portal.JobListDto;
import com.dinh.logistics.dto.portal.ReportListDto;

public interface ReportDao {

	List<Object[]> getReportByFilter(String startDate, String endDate, String cusName, String role, String userName);

	File exportToExcelWithResultSet(String excel_output_file, int row_start, int column_start, String startDate,
			String endDate, String cusName, String role, String userName);

}
