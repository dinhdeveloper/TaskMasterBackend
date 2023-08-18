package com.dinh.logistics.controller.mobile;

import com.dinh.logistics.dto.LoginDto;
import com.dinh.logistics.respository.UserRepository;
import com.dinh.logistics.respository.MediaRepository;
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
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/mobile")
@Slf4j
@Transactional
public class MediaController {

    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    private MediaService mediaService;

    @PostMapping("/upload_image")
    public ResponseEntity<Object> uploadImages(
            @RequestParam("job_id") int jobId,
            @RequestParam("url") List<MultipartFile> images,
            @RequestParam("media_type") int mediaType,
            @RequestParam("media_cate_id") int mediaCateId
    ) {
        try {
            mediaService.uploadImages(jobId, images, mediaType, mediaCateId);
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, -99, StatusResult.ERROR, e.getMessage());
        }
    }

    @PostMapping("/upload/video")
    public ResponseEntity<Object> uploadVideo(@RequestParam("video") MultipartFile video) {
        try {
            mediaService.uploadVideo(video);
            return ResponseEntity.ok("Video uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload video");
        }
    }

}
