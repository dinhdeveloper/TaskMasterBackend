package com.dinh.logistics.service.mobile;

import com.dinh.logistics.dto.mobile.MaterialDto;
import com.dinh.logistics.model.JobMaterial;
import com.dinh.logistics.model.JobMedia;
import com.dinh.logistics.respository.mobile.JobMaterialRepository;
import com.dinh.logistics.respository.mobile.MaterialRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class MaterialService {
    @Autowired
    MaterialRepositoryImp repositoryImp;

    @Autowired
    JobMaterialRepository jobMaterialRepository;

    public boolean addMaterial(MaterialDto materialDto) {
        try {
            repositoryImp.addMaterial(
                    materialDto.getMateId(),
                    materialDto.getJobId(),
                    materialDto.getWeight(),
                    materialDto.getWeightToCus(),
                    materialDto.getPrice());
            return true; // Thêm dữ liệu thành công
        } catch (Exception e) {
            return false; // Thêm dữ liệu thất bại
        }
    }

    @Transactional
    public boolean deleteMaterial(int jobMateId) {
        try {
            int deletedCount = jobMaterialRepository.deleteByJobIdAndJobMateId(jobMateId);

            if (deletedCount > 0) {
                return true; // Deleted successfully
            } else {
                return false; // No media found to delete
            }
        } catch (Exception e) {
            return false;
        }
    }

}
