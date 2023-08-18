package com.dinh.logistics.service.mobile;

import com.dinh.logistics.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.common.io.Files.getFileExtension;
import static org.apache.tomcat.util.http.FastHttpDateFormat.getCurrentDate;

@Service
public class FileStorageService {

    public String storeFile(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String currentDate = getCurrentDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm");
        String newFileNameWithoutExtension = sdf.format(new Date());
        // Lấy phần mở rộng của tập tin gốc
        String extension = getFileExtension(originalFileName);

        // Tạo tên tập tin mới với phần mở rộng
        String newFileName = newFileNameWithoutExtension + "." + extension;

        try {
            InputStream inputStream = file.getInputStream();
            String uploadDir = "static";
            Path filePath = Paths.get(uploadDir + "/" + newFileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return newFileName;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + newFileName, e);
        }
    }


}

