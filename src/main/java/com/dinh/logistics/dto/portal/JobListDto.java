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
