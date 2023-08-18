package com.dinh.logistics.service.mobile;

import com.dinh.logistics.model.JobMedia;
import com.dinh.logistics.respository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class MediaService {

    @Autowired
    private MediaRepository jobMediaRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public void uploadImages(int jobId, List<MultipartFile> images, int mediaType, int mediaCateId){
        // Xử lý logic với jobId, mediaType, mediaCateId
        for (MultipartFile image : images) {
            String fileName = fileStorageService.storeFile(image);
            // Lưu dữ liệu vào bảng JobMedia
            JobMedia jobMedia = new JobMedia();
            jobMedia.setJobId(jobId);
            jobMedia.setUrl("/upload/" + fileName);
            jobMedia.setMediaType(mediaType);
            jobMedia.setMediaCateId(mediaCateId);
            jobMediaRepository.save(jobMedia);
        }
    }

    public void uploadVideo(MultipartFile video) {
        String fileName = fileStorageService.storeFile(video);
        // Logic xử lý sau khi lưu trữ file
    }
}
