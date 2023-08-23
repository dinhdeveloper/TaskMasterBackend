package com.dinh.logistics.service.portal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dinh.logistics.dao.JobDao;
import com.dinh.logistics.dto.portal.JobListDto;
import com.dinh.logistics.dto.portal.JobListResponseDto;
import com.dinh.logistics.model.Employee;
import com.dinh.logistics.model.JobEmployee;
import com.dinh.logistics.model.Jobs;
import com.dinh.logistics.respository.EmployeeRepository;
import com.dinh.logistics.respository.JobEmployeeRepository;
import com.dinh.logistics.respository.JobRepository;
import com.dinh.logistics.respository.RolePjRepository;
import com.dinh.logistics.ultils.AppConstants;

@Service
public class JobManagement {
	
	@Autowired
	JobDao jobDao;
	
	@Autowired
	JobRepository JobRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	JobEmployeeRepository jobEmployeeRepository;
	
	@Autowired
	RolePjRepository rolePjRepository;
	
	public JobListResponseDto getJobListResponse(String startDate, String endDate, int page, int size) {
		
		JobListResponseDto jobListResponseDto = new JobListResponseDto();
		
		//role_id tx
		List<Integer> roleTxId = rolePjRepository.getListIdByRoleCode(AppConstants.ROLE_CODE_TX);
		
		//role_id nv
		List<Integer> roleNvId = rolePjRepository.getListIdByRoleCode(AppConstants.ROLE_CODE_NV);
		
		//total
		int total = jobDao.getCountAllJobByFilter(startDate, endDate, page, size);
		
		//get job list
		List<JobListDto> jobList = jobDao.getAllJobByFilter(startDate, endDate, page, size);
		
		//get employee
		for(JobListDto job : jobList) {
			List<JobEmployee> jobEmplList = jobEmployeeRepository.findAllByJobId(job.getId());
			for(JobEmployee JobEmployee : jobEmplList) {
				Employee employee = employeeRepository.findById(JobEmployee.getEmpId()).orElse(null);
				if(employee != null) {
					if(employee.getRoleId() == roleTxId.get(0)) {
						job.setEmployee_3(employee.getName());
					}else {
						if(StringUtils.isEmpty(job.getEmployee_1())) {
							job.setEmployee_1(employee.getName());
						}else {
							job.setEmployee_2(employee.getName());
						}
					}
				}
			}
		}
		
		jobListResponseDto.setTotal(total);
		jobListResponseDto.setData(jobList);
		
		return jobListResponseDto;
	}
	
}
