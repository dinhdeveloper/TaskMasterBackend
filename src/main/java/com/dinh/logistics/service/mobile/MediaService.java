package com.dinh.logistics.service.mobile;

import com.dinh.logistics.dto.mobile.CollectPointDto;
import com.dinh.logistics.dto.mobile.JobMediaDto;
import com.dinh.logistics.model.JobMedia;
import com.dinh.logistics.respository.JobMediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.google.common.io.Files.getFileExtension;

@Service
public class MediaService {

	@Value("${app.file.media}")
	private String mediaFileLocation;

    @Autowired
    private JobMediaRepository jobMediaRepository;

    private String getFolderDateYear(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        String subFolderPath = String.format("/%d/%02d/%02d/", year, month, date);
        return subFolderPath;
    }

    public boolean uploadVideos(int jobId, MultipartFile urlVideo){
        try {

//            List<JobMedia> existingVideos = jobMediaRepository.getJobMediaByJobIdAndMediaType(jobId, 2);
//            if (existingVideos.size() > 0){
//                for (JobMedia existingVideo : existingVideos) {
//                    deleteMediaFile(existingVideo.getUrl());
//                    jobMediaRepository.deleteJobMediaByUrl(existingVideo.getUrl());
//                }
//            }

            List<JobMedia> existingVideos = jobMediaRepository.getJobMediaByJobIdAndMediaType(jobId, 2);
            if (existingVideos.size() >= 1){
                return false; // Thêm dữ liệu thất bại
            }else {
                if (urlVideo != null && !urlVideo.isEmpty()) {
                    String videoFilename = urlVideo.getOriginalFilename();

                    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss");
                    String newVideoFileNameWithoutExtension = sdf.format(new Date()) + "_video";
                    String videoExtension = getFileExtension(videoFilename);
                    String newVideoFileName = newVideoFileNameWithoutExtension + "." + videoExtension;

                    try {
                        // Tạo folder nếu chưa có
                        Path folder = Paths.get(mediaFileLocation).toAbsolutePath().normalize();
                        Files.createDirectories(folder);
                        // Tạo đường dẫn tuyệt đối cho file video
                        Path targetVideoLocation = Paths.get(mediaFileLocation+ getFolderDateYear()).toAbsolutePath().normalize().resolve(newVideoFileName);
                        // Lưu video
                        Files.copy(urlVideo.getInputStream(), targetVideoLocation, StandardCopyOption.REPLACE_EXISTING);
                        // Lưu dữ liệu vào bảng JobMedia cho video
                        JobMediaDto videoJobMedia = new JobMediaDto();
                        videoJobMedia.setJobId(jobId);
                        videoJobMedia.setUrl(mediaFileLocation + getFolderDateYear() + newVideoFileName);
                        videoJobMedia.setMediaType(2);
                        jobMediaRepository.insertJobMedia(videoJobMedia.getUrl(), videoJobMedia.getMediaType(), videoJobMedia.getJobId());
                    } catch (IOException ex) {
                        // Xử lý ngoại lệ
                    }
                }
                return true; // Thêm dữ liệu thành công
            }
        } catch (Exception e) {
            return false; // Thêm dữ liệu thất bại
        }
    }

    public boolean uploadListImages(int jobId, List<MultipartFile> listUrlImage) {
        try {

//            List<JobMedia> existingImages = jobMediaRepository.getJobMediaByJobIdAndMediaType(jobId, 1);
//            if (existingImages.size() > 0) {
//                for (JobMedia existingImage : existingImages) {
//                    deleteMediaFile(existingImage.getUrl());
//                    jobMediaRepository.deleteJobMediaByUrl(existingImage.getUrl());
//                }
//            }

            List<JobMedia> existingImages = jobMediaRepository.getJobMediaByJobIdAndMediaType(jobId, 1);
            if (existingImages.size() + listUrlImage.size() > 5){
                return false;
            }else {
                int count = 0;
                for (MultipartFile image : listUrlImage) {
                    count ++;
                    String filename = image.getOriginalFilename();

                    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss");
                    String newFileNameWithoutExtension = sdf.format(new Date()) + "_IMG_" + count;
                    // Lấy phần mở rộng của tập tin gốc
                    String extension = getFileExtension(filename);
                    // Tạo tên tập tin mới với phần mở rộng
                    String newFileName = newFileNameWithoutExtension + "." + extension;
                    try {
                        // Tạo folder nếu chưa có
                        Path folder = Paths.get(mediaFileLocation + getFolderDateYear()).toAbsolutePath().normalize();
                        Files.createDirectories(folder);

                        // Tạo đường dẫn tuyệt đối cho file
                        Path targetLocation = Paths.get(mediaFileLocation + getFolderDateYear()).toAbsolutePath().normalize().resolve(newFileName);

                        // Lưu file
                        Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                    } catch (IOException ex) {
                        //
                    }

                    // Lưu dữ liệu vào bảng JobMedia
                    JobMediaDto jobMedia = new JobMediaDto();
                    jobMedia.setJobId(jobId);
                    jobMedia.setUrl(mediaFileLocation + getFolderDateYear() + newFileName);
                    jobMedia.setMediaType(1);
                    jobMediaRepository.insertJobMedia(jobMedia.getUrl(), jobMedia.getMediaType(), jobMedia.getJobId());
                }
                return true; // Thêm dữ liệu thành công
            }
        } catch (Exception e) {
            return false; // Thêm dữ liệu thất bại
        }
    }

    public boolean uploadListImagesDeleteVideo(int jobId, List<MultipartFile> listUrlImage, int mediaType) {
        try {
            // First, delete existing video associated with the job
            List<JobMedia> existingVideos = jobMediaRepository.getJobMediaByJobIdAndMediaType(jobId, 2);
            if (existingVideos.size() > 0){
                for (JobMedia existingVideo : existingVideos) {
                    deleteMediaFile(existingVideo.getUrl());
                    jobMediaRepository.deleteJobMediaByUrl(existingVideo.getUrl());
                }
            }
            // Upload new images
            if (uploadListImages(jobId, listUrlImage)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean uploadVideosDeleteImage(int jobId, MultipartFile urlVideo, int mediaType) {
        try {
            // First, delete existing images associated with the job
            List<JobMedia> existingImages = jobMediaRepository.getJobMediaByJobIdAndMediaType(jobId, 1);
            if (existingImages.size() > 0){
                for (JobMedia existingImage : existingImages) {
                    deleteMediaFile(existingImage.getUrl());
                    jobMediaRepository.deleteJobMediaByUrl(existingImage.getUrl());
                }
            }

            // Upload new video
            if (uploadVideos(jobId, urlVideo)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private void deleteMediaFile(String url) {
        try {
            Path filePath = Paths.get(url);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception here, e.g., log the error
        }
    }
    @Transactional
    public boolean deleteMedia(int jobId, String urlVideo, int mediaType) {
        try {
            if (urlVideo == null) {
                return false; // urlVideo is null, cannot proceed
            }

            // Tìm kiếm Job_media dựa trên jobId, urlVideo và mediaType
            JobMedia existingMedia = jobMediaRepository.findByJobIdAndUrlAndMediaType(jobId, urlVideo, mediaType);

            if (existingMedia != null) {
                deleteMediaFile(existingMedia.getUrl());
                jobMediaRepository.deleteJobMediaByUrl(existingMedia.getUrl());
                return true; // Deleted successfully
            }
            return false; // No media found to delete
        } catch (Exception e) {
            return false;
        }
    }
}
