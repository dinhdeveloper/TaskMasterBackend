package com.dinh.logistics.controller.mobile;

import com.dinh.logistics.dto.mobile.DeleteMediaRequest;
import com.dinh.logistics.service.mobile.MediaService;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
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
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, "Xóa thành công");
        } else {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, "Xóa thất bại");
        }
    }

//    @GetMapping("/view/{path1}/{path2}/{path3}/{path4}/{path5:.+}")
//    public ResponseEntity<FileSystemResource> viewImage3(
//            @PathVariable("path1") String path1,
//            @PathVariable("path2") String path2,
//            @PathVariable("path3") String path3,
//            @PathVariable("path4") String path4,
//            @PathVariable("path5") String path5
//    ) {
//        try {
//            File imageFile = new File(path1+"/"+path2+"/"+path3+"/"+path4+"/"+path5);
//
//            if (imageFile.exists() && imageFile.isFile()) {
//                return ResponseEntity.ok()
//                        .contentType(MediaType.IMAGE_JPEG) // Loại hình ảnh của bạn (có thể thay đổi)
//                        .body(new FileSystemResource(imageFile));
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }


    @GetMapping("/view/{path1}/{path2}/{path3}/{path4}/{path5:.+}")
    public ResponseEntity<InputStreamResource> viewMedia(
            @PathVariable("path1") String path1,
            @PathVariable("path2") String path2,
            @PathVariable("path3") String path3,
            @PathVariable("path4") String path4,
            @PathVariable("path5") String path5
    ) {
        try {
            File mediaFile = new File(path1 + "/" + path2 + "/" + path3 + "/" + path4 + "/" + path5);

            if (mediaFile.exists() && mediaFile.isFile()) {
                // Kiểm tra đuôi tệp
                String fileExtension = FilenameUtils.getExtension(mediaFile.getName()).toLowerCase();

                if (isImageExtension(fileExtension)) {
                    // Nếu đuôi là hình ảnh
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_JPEG); // Đặt loại phản hồi cho hình ảnh

                    return new ResponseEntity<>(new InputStreamResource(new FileInputStream(mediaFile)), headers, HttpStatus.OK);
                } else if (isVideoExtension(fileExtension)) {
                    // Nếu đuôi là video
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // Đặt loại phản hồi cho video

                    // Đặt tên tệp để trình duyệt hiểu rằng nó nên mở trong trình xem video
                    headers.add("Content-Disposition", "inline; filename=" + mediaFile.getName());

                    return new ResponseEntity<>(new InputStreamResource(new FileInputStream(mediaFile)), headers, HttpStatus.OK);
                } else {
                    // Nếu đuôi không được hỗ trợ
                    return ResponseEntity.badRequest().build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private boolean isImageExtension(String extension) {
        // Thêm các đuôi tệp hình ảnh được hỗ trợ vào đây, ví dụ: "jpg", "jpeg", "png", ...
        return Arrays.asList("jpg", "jpeg", "png").contains(extension);
    }

    private boolean isVideoExtension(String extension) {
        // Thêm các đuôi tệp video được hỗ trợ vào đây, ví dụ: "mp4", "avi", "mkv", ...
        return Arrays.asList("mp4", "avi", "mkv").contains(extension);
    }


}
