package com.dinh.logistics.service.mobile;

import com.dinh.logistics.dao.JobDao;
import com.dinh.logistics.dto.mobile.*;
import com.dinh.logistics.model.*;
import com.dinh.logistics.respository.EmployeeRepository;
import com.dinh.logistics.respository.TeamRepository;
import com.dinh.logistics.respository.mobile.JobsRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
            repositoryImp.addJobs(
                    addJobsDto.getJobType(),
                    addJobsDto.getJobStateId(),
                    addJobsDto.getNv1Id(),
                    addJobsDto.getNv2Id(),
                    addJobsDto.getAssignId(),
                    addJobsDto.getListIdPoint(),
                    addJobsDto.getGhiChu());
            return true; // Thêm dữ liệu thành công
        } catch (Exception e) {
            return false; // Thêm dữ liệu thất bại
        }
    }

    public JobDetailsDTO jobsDetails(Integer jobId,Integer empId) {
        return repositoryImp.jobsDetails(jobId,empId);
    }


    @Transactional
    public void updateStateJob(UpdateStateRequest updateStateRequest) {

        Date date = new Date();

        Jobs job = repositoryImp.findJobById(updateStateRequest.getJobsId());
        if (job != null) {
            JobState jobState = repositoryImp.findJobStateById(job.getJobStateId());
            //UPDATE STATE
            job.setJobStateId(updateStateRequest.getStateJob());
            //DA LAM GON
            if (updateStateRequest.getStateJob() == 15){
                job.setCollectFinishTime(new Timestamp(date.getTime()));
            }
            //DA CAN
            if (updateStateRequest.getStateJob() == 20){
                job.setPaymentMethod(updateStateRequest.getPaymentMethod());
                Integer paymentStateId = repositoryImp.findByStateStatus(updateStateRequest.getPaymentStateStatus());
                job.setPaymentStateId(paymentStateId);
                if (updateStateRequest.getPaymentStateStatus() == 1 && updateStateRequest.getPaymentMethod() == 1){ //nv ung tien va da thanh toan
                    job.setAmountPaidEmp(updateStateRequest.getAmountPaidEmp());
                    job.setAmount(updateStateRequest.getAmountTotal());
                }
                if (updateStateRequest.getPaymentStateStatus() == 1 && updateStateRequest.getPaymentMethod() == 2){ // bank & da thanh toan
                    job.setAmount(updateStateRequest.getAmountTotal());
                    job.setWeightTime(new Timestamp(date.getTime()));
                    /* type: info; receiver: master user; content: CK cho [khách hàng], [địa điểm], [số tiền], [số tàikhoản], [ngân hàng của khách hàng]*/
                    repositoryImp.pushNotifyStateWeighted(updateStateRequest);
                }
                if (updateStateRequest.getPaymentStateStatus() == 0 && updateStateRequest.getPaymentMethod() == -1){ //chua thanh toan
                    job.setAmount(updateStateRequest.getAmountTotal());
                }
            }
            //DA XONG
            if (updateStateRequest.getStateJob() == 30){
                job.setFinishTime(new Timestamp(date.getTime()));
            }


            Jobs jobsNew = repositoryImp.saveJob(job);
            if (updateStateRequest.getStateJob() != 30 && !(updateStateRequest.getStateJob() == 20
                    && updateStateRequest.getPaymentStateStatus() == 1
                    && updateStateRequest.getPaymentMethod() == 2)){
                repositoryImp.pushNotifyUpdateJobState(jobsNew, updateStateRequest.getStateJob());
            }

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

    public Jobs updateJobSave(DataUpdateJobRequest dataUpdateJobRequest) {
        try {
            Jobs job = repositoryImp.findJobById(dataUpdateJobRequest.getJodId());
           return repositoryImp.updateJobSave(dataUpdateJobRequest,job);
        } catch (Exception e) {
            return null; // Thêm dữ liệu thất bại
        }
    }
}
