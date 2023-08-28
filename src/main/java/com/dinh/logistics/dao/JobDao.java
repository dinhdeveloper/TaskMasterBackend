package com.dinh.logistics.dao;

import java.util.List;

import com.dinh.logistics.dto.UserDeviceDto;
import com.dinh.logistics.dto.portal.JobListDto;
import com.dinh.logistics.model.UserDevice;


public interface JobDao {

	List<JobListDto> getAllJobByFilter(String startDate, String endDate, int page, int size);

	int getCountAllJobByFilter(String startDate, String endDate, int page, int size);

	List<UserDeviceDto> getListUserDeviceToPushNotification(List<Integer> ids);

}
