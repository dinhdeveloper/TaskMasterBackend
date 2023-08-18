package com.dinh.logistics.service.mobile;

import com.dinh.logistics.dto.mobile.JobMediaDto;
import com.dinh.logistics.respository.JobMediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.google.common.io.Files.getFileExtension;

@Service
public class MediaService {

    @Autowired
    private JobMediaRepository jobMediaRepository;

    public void uploadImages(int jobId, List<MultipartFile> images, int mediaType, int mediaCateId){
        // Xử lý logic với jobId, mediaType, mediaCateId
        for (MultipartFile image : images) {
            String filename = image.getOriginalFilename();

            SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm");
            String newFileNameWithoutExtension = sdf.format(new Date());
            // Lấy phần mở rộng của tập tin gốc
            String extension = getFileExtension(filename);
            // Tạo tên tập tin mới với phần mở rộng
            String newFileName = newFileNameWithoutExtension + "." + extension;

            // Lưu dữ liệu vào bảng JobMedia
            JobMediaDto jobMedia = new JobMediaDto();
            jobMedia.setJobId(jobId);
            jobMedia.setUrl("/upload/" + newFileName);
            jobMedia.setMediaType(mediaType);
            jobMedia.setMediaCateId(mediaCateId);
            jobMediaRepository.insertJobMedia(jobMedia.getUrl(), jobMedia.getMediaType(), jobMedia.getMediaCateId(), jobMedia.getJobId());
        }
    }
}
