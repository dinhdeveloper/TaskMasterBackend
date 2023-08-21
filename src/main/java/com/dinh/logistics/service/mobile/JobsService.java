package com.dinh.logistics.service.mobile;

import com.dinh.logistics.dto.mobile.AddJobsDto;
import com.dinh.logistics.dto.mobile.CollectPointDto;
import com.dinh.logistics.respository.mobile.JobsRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
