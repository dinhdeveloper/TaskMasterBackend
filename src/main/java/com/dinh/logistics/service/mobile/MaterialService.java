package com.dinh.logistics.service.mobile;

import com.dinh.logistics.dto.mobile.MaterialDto;
import com.dinh.logistics.respository.mobile.MaterialRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialService {
    @Autowired
    MaterialRepositoryImp repositoryImp;

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
}
