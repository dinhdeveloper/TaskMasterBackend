package com.dinh.logistics.dto.portal;

import java.math.BigDecimal;
import java.util.Date;

import com.dinh.logistics.ultils.DateHelper;

import lombok.Data;

@Data
public class JobListDto {

	private Integer id;
	private String collectPoint;
	private String createDate;
	private String jobType;
	private String employee_1;
	private String employee_2;
	private String employee_3;
	private Integer priority;
	private String note;
	
	private Integer employeeId_1;
	private Integer employeeId_2;
	private Integer employeeId_3;
	
	public JobListDto(Integer id, String collectPoint, Date createDate, String jobType,
			String employee_1, String employee_2, String employee_3, BigDecimal priority, String note,
			Integer employeeId_1, Integer employeeId_2, Integer employeeId_3) {
		this.id = id;
		this.collectPoint = collectPoint;
		this.createDate = DateHelper.convertDateToStringJobList(createDate);
		this.jobType = jobType;
		this.employee_1 = employee_1;
		this.employee_2 = employee_2;
		this.employee_3 = employee_3;
		this.priority = priority.intValue();
		this.note = note;
		this.employeeId_1 = employeeId_1;
		this.employeeId_2 = employeeId_2;
		this.employeeId_3 = employeeId_3;
		
	}
	
	public JobListDto(Integer id, String collectPoint, Date createDate, String jobType,
			String employee_1, String employee_2, String employee_3, BigDecimal priority, String note) {
		this.id = id;
		this.collectPoint = collectPoint;
		this.createDate = DateHelper.convertDateToStringJobList(createDate);
		this.jobType = jobType;
		this.employee_1 = employee_1;
		this.employee_2 = employee_2;
		this.employee_3 = employee_3;
		this.priority = priority.intValue();
		this.note = note;
		
	}
}
