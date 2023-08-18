package com.dinh.logistics.service.mobile;

import com.dinh.logistics.dto.mobile.JobMediaDto;
import com.dinh.logistics.respository.JobMediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.google.common.io.Files.getFileExtension;

@Service
public class MediaService {

	@Value("${app.file.media}")
	private String mediaFileLocation;
	
    @Autowired
    private JobMediaRepository jobMediaRepository;

    public void uploadImages(int jobId, List<MultipartFile> images, int mediaType, int mediaCateId){
        // Xử lý logic với jobId, mediaType, mediaCateId
    	int count = 0;
        for (MultipartFile image : images) {
        	count ++;
            String filename = image.getOriginalFilename();

            SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm");
            String newFileNameWithoutExtension = sdf.format(new Date()) + "_" + count;
            // Lấy phần mở rộng của tập tin gốc
            String extension = getFileExtension(filename);
            // Tạo tên tập tin mới với phần mở rộng
            String newFileName = newFileNameWithoutExtension + "." + extension;
            
            try {
    			// Tạo folder nếu chưa có
            	Path folder = Paths.get(mediaFileLocation).toAbsolutePath().normalize();
            	Files.createDirectories(folder);
            	
            	// Tạo đường dẫn tuyệt đối cho file
    			Path targetLocation = Paths.get(mediaFileLocation).toAbsolutePath().normalize().resolve(newFileName);
    			
    			// Lưu file
    			Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
    			
    		} catch (IOException ex) {
    			//
    		}
            
            // Lưu dữ liệu vào bảng JobMedia
            JobMediaDto jobMedia = new JobMediaDto();
            jobMedia.setJobId(jobId);
            jobMedia.setUrl(mediaFileLocation + "/" + newFileName);
            jobMedia.setMediaType(mediaType);
            jobMedia.setMediaCateId(mediaCateId);
            jobMediaRepository.insertJobMedia(jobMedia.getUrl(), jobMedia.getMediaType(), jobMedia.getMediaCateId(), jobMedia.getJobId());
        }
    }
}
