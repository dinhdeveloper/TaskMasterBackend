package com.dinh.logistics.service.mobile;

import com.dinh.logistics.dao.JobDao;
import com.dinh.logistics.dto.mobile.AddJobsDto;
import com.dinh.logistics.dto.mobile.JobDetailsDTO;
import com.dinh.logistics.dto.mobile.JobSearchResponse;
import com.dinh.logistics.dto.mobile.JobSearchResponseDto;
import com.dinh.logistics.model.Employee;
import com.dinh.logistics.model.Jobs;
import com.dinh.logistics.model.Team;
import com.dinh.logistics.respository.EmployeeRepository;
import com.dinh.logistics.respository.TeamRepository;
import com.dinh.logistics.respository.mobile.JobsRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.transaction.Transactional;

@Service
public class JobsService {

    @Autowired
    private JobsRepositoryImp repositoryImp;
    
    @Autowired
    JobDao jobDao;
    
    @Autowired
    EmployeeRepository employeeRepository;
    
    @Autowired
    TeamRepository teamRepository;

    public boolean addJobs(AddJobsDto addJobsDto) {
        try {
            repositoryImp.insertCollectPoint(
                    addJobsDto.getJobType(),
                    addJobsDto.getNv1Id(),
                    addJobsDto.getNv2Id(),
                    addJobsDto.getListIdPoint(),
                    addJobsDto.getGhiChu());
            return true; // Thêm dữ liệu thành công
        } catch (Exception e) {
            return false; // Thêm dữ liệu thất bại
        }
    }

    public JobDetailsDTO jobsDetails(Integer id) {
        return repositoryImp.jobsDetails(id);
    }


    @Transactional
    public void updateStateJob(Integer jobId, Integer newStateId) {
        Jobs job = repositoryImp.findJobById(jobId);
        if (job != null) {
            job.setJobStateId(newStateId);
            repositoryImp.saveJob(job);
        } else {
            // Handle the case when the job with the given ID doesn't exist
        }
    }
    
    public JobSearchResponse searchJobByFilter(Integer empStatus, Integer empId, Integer status, Integer paymentStatus, String startDate,
			String endDate, Integer jobId, String collectPoint) {
    	
    	JobSearchResponse jobSearchResponse = new JobSearchResponse();
    	if(empStatus == 1) {
    		List<JobSearchResponseDto> resultList = jobDao.searchJobByFilter(empId, status, paymentStatus, startDate, endDate, jobId, collectPoint);
    		jobSearchResponse.setData(resultList);
    	}else if(empStatus == 2) {
    		Employee emp = employeeRepository.findById(empId).orElse(null);
    		Team team = teamRepository.findById(emp.getTeamId()).orElse(null);
    		List<JobSearchResponseDto> resultList = jobDao.searchJobByFilter(team.getLeaderId(), status, paymentStatus, startDate, endDate, jobId, collectPoint);
    		jobSearchResponse.setData(resultList);
    	}else {
    		List<JobSearchResponseDto> resultList = jobDao.searchJobByFilter(null, status, paymentStatus, startDate, endDate, jobId, collectPoint);
    		jobSearchResponse.setData(resultList);
    	}
    	
    	
    	return jobSearchResponse;
    }
}
