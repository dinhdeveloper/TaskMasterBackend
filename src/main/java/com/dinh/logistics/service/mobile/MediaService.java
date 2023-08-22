package com.dinh.logistics.service.mobile;

import com.dinh.logistics.dto.mobile.CollectPointDto;
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

    public boolean uploadVideos(int jobId, MultipartFile urlVideo, int mediaType, int mediaCateId){
        try {
            if (urlVideo != null && !urlVideo.isEmpty()) {
                String videoFilename = urlVideo.getOriginalFilename();

                SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm");
                String newVideoFileNameWithoutExtension = sdf.format(new Date()) + "_video";
                String videoExtension = getFileExtension(videoFilename);
                String newVideoFileName = newVideoFileNameWithoutExtension + "." + videoExtension;

                try {
                    // Tạo folder nếu chưa có
                    Path folder = Paths.get(mediaFileLocation).toAbsolutePath().normalize();
                    Files.createDirectories(folder);
                    // Tạo đường dẫn tuyệt đối cho file video
                    Path targetVideoLocation = Paths.get(mediaFileLocation).toAbsolutePath().normalize().resolve(newVideoFileName);
                    // Lưu video
                    Files.copy(urlVideo.getInputStream(), targetVideoLocation, StandardCopyOption.REPLACE_EXISTING);
                    // Lưu dữ liệu vào bảng JobMedia cho video
                    JobMediaDto videoJobMedia = new JobMediaDto();
                    videoJobMedia.setJobId(jobId);
                    videoJobMedia.setUrl(mediaFileLocation + "/" + newVideoFileName);
                    videoJobMedia.setMediaType(mediaType);
                    videoJobMedia.setMediaCateId(mediaCateId);
                    jobMediaRepository.insertJobMedia(videoJobMedia.getUrl(), videoJobMedia.getMediaType(), videoJobMedia.getMediaCateId(), videoJobMedia.getJobId());
                } catch (IOException ex) {
                    // Xử lý ngoại lệ
                }
            }
            return true; // Thêm dữ liệu thành công
        } catch (Exception e) {
            return false; // Thêm dữ liệu thất bại
        }
    }

    public boolean uploadListImages(int jobId, List<MultipartFile> listUrlImage, int mediaType, int mediaCateId) {
        try {

            int count = 0;
            for (MultipartFile image : listUrlImage) {
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
            return true; // Thêm dữ liệu thành công
        } catch (Exception e) {
            return false; // Thêm dữ liệu thất bại
        }
    }
}
