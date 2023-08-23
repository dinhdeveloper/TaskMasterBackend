package com.dinh.logistics.dto.portal;

import java.util.List;

import lombok.Data;

@Data
public class JobListResponseDto {

	private Integer total;
	private List<JobListDto> data;
	
}
