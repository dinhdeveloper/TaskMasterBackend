package com.dinh.logistics.service.mobile;

import com.dinh.logistics.dto.mobile.CollectPointDto;
import com.dinh.logistics.dto.mobile.JobMediaDto;
import com.dinh.logistics.model.CollectPoint;
import com.dinh.logistics.respository.CollectPointRepository;
import com.dinh.logistics.respository.JobMediaRepository;
import com.dinh.logistics.respository.mobile.CollectPointRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectPointService {

    @Autowired
    CollectPointRepositoryImp collectPointRepositoryImp;

    public boolean addCollectPoint(CollectPointDto collectPoint) {
        try {
            collectPointRepositoryImp.insertCollectPoint(
                    collectPoint.getNamePoint(),
                    collectPoint.getNameAddress(),
                    collectPoint.getNameContact(),
                    collectPoint.getPhoneContact());
            return true; // Thêm dữ liệu thành công
        } catch (Exception e) {
            return false; // Thêm dữ liệu thất bại
        }
    }
}
