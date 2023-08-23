package com.dinh.logistics.service.mobile;

import com.dinh.logistics.dto.mobile.AddJobsDto;
import com.dinh.logistics.dto.mobile.JobDetailsDTO;
import com.dinh.logistics.model.Jobs;
import com.dinh.logistics.respository.mobile.JobsRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class JobsService {

    @Autowired
    private JobsRepositoryImp repositoryImp;

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
}
