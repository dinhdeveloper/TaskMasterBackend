package com.dinh.logistics.dao;

import java.util.List;

import com.dinh.logistics.dto.UserDeviceDto;
import com.dinh.logistics.dto.mobile.JobSearchResponseDto;
import com.dinh.logistics.dto.portal.JobListDto;


public interface JobDao {

	List<JobListDto> getAllJobByFilter(String startDate, String endDate, int page, int size);

	int getCountAllJobByFilter(String startDate, String endDate, int page, int size);

	List<UserDeviceDto> getListUserDeviceToPushNotification(List<Integer> ids);

	List<JobSearchResponseDto> searchJobByFilter(Integer empId, Integer status, Integer paymentStatus, String startDate,
												 String endDate, Integer jobId, String collectPoint, Integer teamId);

	List<JobListDto> getAllJobNotiByFilter(List<Integer> ids);

}
