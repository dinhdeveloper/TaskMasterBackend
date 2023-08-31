package com.dinh.logistics.dto.portal;

import java.math.BigDecimal;
import java.util.Date;

import com.dinh.logistics.ultils.DateHelper;

import lombok.Data;

@Data
public class ReportListDto {

	private Integer id;
	private String customerName;
	private String createDate;
	private Integer mar1;
	private Integer mar2;
	private Integer mar3;
	private Integer total;
	
	
	public ReportListDto(String customerName, Date createDate,
			BigDecimal mar1, BigDecimal mar2, BigDecimal mar3, BigDecimal priority) {
		this.customerName = customerName;
		this.createDate = DateHelper.convertDateToStringJobList(createDate);
		this.mar1 = mar1.intValue();
		this.mar1 = mar2.intValue();
		this.mar1 = mar3.intValue();
		this.total = priority.intValue();
		
	}
	
}
