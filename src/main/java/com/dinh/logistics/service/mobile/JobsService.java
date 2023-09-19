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
import java.util.ArrayList;
import java.util.Date;
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
    public void updateStateWeightedJob(UpdateStateWeightedRequest updateStateWeightedRequest) {

        Date date = new Date();
        Jobs job = repositoryImp.findJobById(updateStateWeightedRequest.getJodId());

        repositoryImp.updateJobSaveWeighted(updateStateWeightedRequest,job);

        if (job != null) {
            if (updateStateWeightedRequest.getStateJob() == 20){
                if (updateStateWeightedRequest.getPaymentStateId() == 1 && updateStateWeightedRequest.getPaymentMethod() == 2){ // bank & da thanh toan
                    job.setWeightTime(new Timestamp(date.getTime()));
                    /* type: info; receiver: master user; content: CK cho [khách hàng], [địa điểm], [số tiền], [số tàikhoản], [ngân hàng của khách hàng]*/
                    repositoryImp.pushNotifyStateWeighted(updateStateWeightedRequest,job);
                }
            }
            job.setJobStateId(updateStateWeightedRequest.getStateJob());

            Jobs jobsNew = repositoryImp.saveJob(job);
            repositoryImp.pushNotifyUpdateState(jobsNew, updateStateWeightedRequest);

        } else {
            // Handle the case when the job with the given ID doesn't exist
        }
    }
    
    public JobSearchResponse searchJobByFilter(Integer empStatus, Integer empId, Integer status, Integer paymentStatus, String startDate,
                                               String endDate, Integer jobId, String collectPoint, Integer empRequest) {
    	
    	JobSearchResponse jobSearchResponse = new JobSearchResponse();
    	if(empStatus == 1) {
    		List<JobSearchResponseDto> resultList = jobDao.searchJobByFilter(empId, status, paymentStatus, startDate, endDate, jobId, collectPoint, -1);
    		jobSearchResponse.setData(resultList);
    	}else if(empStatus == 2) {
    		Employee emp = employeeRepository.findById(empId).orElse(null);
    		Team team = teamRepository.findById(emp.getTeamId()).orElse(null);
    		List<JobSearchResponseDto> resultList = jobDao.searchJobByFilter(null, status, paymentStatus, startDate, endDate, jobId, collectPoint, team.getTeamId());
    		jobSearchResponse.setData(resultList);
    	}else {
            List<JobSearchResponseDto> resultList = null;
    	    if (empRequest != empId){
               resultList = jobDao.searchJobByFilter(empRequest, status, paymentStatus, startDate, endDate, jobId, collectPoint, -1);
            }else {
                resultList = jobDao.searchJobByFilter(null, status, paymentStatus, startDate, endDate, jobId, collectPoint, -1);
            }
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

    public List<CollectPointLatLng> getCollectPointLatLng() {
        List<CollectPointLatLng> collectPointLatLngList = repositoryImp.getCollectPointLatLng();

        collectPointLatLngList.sort((a, b) -> {
            int latitudeComparison = a.getLatitude().compareTo(b.getLatitude());
            if (latitudeComparison != 0) {
                return latitudeComparison;
            }
            return a.getLongitude().compareTo(b.getLongitude());
        });

        // Tạo danh sách mới với các mục đã gộp lại
        List<CollectPointLatLng> mergedList = new ArrayList<>();
        CollectPointLatLng current = null;

        for (CollectPointLatLng item : collectPointLatLngList) {
            if (current == null || !current.getLatitude().equals(item.getLatitude()) || !current.getLongitude().equals(item.getLongitude())) {
                // Tạo một mục mới nếu latitude hoặc longitude khác với mục hiện tại
                current = new CollectPointLatLng();
                current.setJobId(item.getJobId());
                current.setLatitude(item.getLatitude());
                current.setLongitude(item.getLongitude());
                current.setCpName(item.getCpName());
                current.setFullName(item.getFullName());
                current.setJobStateDesc(item.getJobStateDesc());
                mergedList.add(current);
            } else {
                // Gộp các giá trị fullName của các mục có cùng latitude và longitude
                current.setFullName(current.getFullName() + ",\n" + item.getFullName());
            }
        }

        return mergedList;
    }

    public void updateStateJobCompactedAndDone(CompactedAndDoneRequest updateStateRequest) {
        Date date = new Date();

        Jobs job = repositoryImp.findJobById(updateStateRequest.getJobsId());
        if (job != null) {
            //UPDATE STATE
            job.setJobStateId(updateStateRequest.getStateJob());

            if (updateStateRequest.getStateJob() == 15){
                job.setCollectFinishTime(new Timestamp(date.getTime()));
            }
            if (updateStateRequest.getStateJob() == 30){
                job.setFinishTime(new Timestamp(date.getTime()));
            }

            Jobs jobsNew = repositoryImp.saveJob(job);

            if (updateStateRequest.getStateJob() != 30){
                //repositoryImp.pushNotifyUpdateJobState(jobsNew, updateStateRequest.getStateJob());
                repositoryImp.pushNotifyUpdateStateCompactedAndDone(jobsNew, updateStateRequest);
            }

        } else {
            // Handle the case when the job with the given ID doesn't exist
        }
    }
}
