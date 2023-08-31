package com.dinh.logistics.dto.mobile;

import java.math.BigDecimal;
import java.util.Date;

import com.dinh.logistics.ultils.DateHelper;

import lombok.Data;

@Data
public class JobSearchResponseDto {

	private Integer jobId;
	private String collectPoint;
	private String emp;
	private String status;
	private Integer priority;
	private String date;
	private Integer empId;

	private String temp;
	
	public JobSearchResponseDto(Integer id, String collectPoint, String emp, String status, BigDecimal priority, Date date, Integer empId) {
		this.jobId = id;
		this.collectPoint = collectPoint;
		this.emp = emp;
		this.status = status;
		this.priority = priority.intValue();
		this.date = DateHelper.convertDateToStringJobList(date);
		this.empId = empId;
	}
	
}
