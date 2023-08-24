package com.dinh.logistics.controller.mobile;

import com.dinh.logistics.dto.mobile.AddJobsDto;
import com.dinh.logistics.dto.mobile.DeleteMediaRequest;
import com.dinh.logistics.service.mobile.MediaService;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/api/mobile")
@Slf4j
@Transactional
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @PostMapping("/upload_image_video")
    public ResponseEntity<Object> uploadMultiImageVideo(
            @RequestParam("job_id") int jobId,
            @RequestParam("url_image") List<MultipartFile> listUrlImage,
            @RequestParam("url_video") MultipartFile urlVideo,
            @RequestParam("media_type") int mediaType
    ) {
        try {
            if (mediaService.uploadVideos(jobId, urlVideo)) {
                if (mediaService.uploadListImages(jobId, listUrlImage)){
                    return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, "Upload thành công");
                }else {
                    return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, "Upload thất bại");
                }
            } else {
                return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, "Upload thất bại");
            }
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, e.getMessage());
        }
    }

    @PostMapping("/upload_image")
    public ResponseEntity<Object> uploadMultiImage(
            @RequestParam("job_id") int jobId,
            @RequestParam("url_image") List<MultipartFile> listUrlImage,
            @RequestParam("media_type") int mediaType
    ) {
        try {
            if (mediaService.uploadListImages(jobId, listUrlImage)){
                return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, "Upload hình ảnh thành công");
            }else {
                return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, "Upload hình ảnh thất bại");
            }
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, e.getMessage());
        }
    }


    @PostMapping("/upload_video")
    public ResponseEntity<Object> uploadVideo(
            @RequestParam("job_id") int jobId,
            @RequestParam("url_video") MultipartFile urlVideo,
            @RequestParam("media_type") int mediaType
    ) {
        try {
            if (mediaService.uploadVideos(jobId, urlVideo)) {
                return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, "Upload video thành công");
            } else {
                return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, "Upload video thất bại");
            }
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, e.getMessage());
        }
    }

    @PostMapping("/delete_media")
    public ResponseEntity<Object> deleteMedia(@RequestBody DeleteMediaRequest deleteMediaRequest) {
        if (mediaService.deleteMedia(deleteMediaRequest.getJobId(), deleteMediaRequest.getUrl(), deleteMediaRequest.getMediaType())) {
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, "Cập nhật thành công");
        } else {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, "Cập nhật thất bại");
        }
    }
}
