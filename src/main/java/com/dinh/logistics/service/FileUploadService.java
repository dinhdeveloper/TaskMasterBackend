package com.dinh.logistics.service;

import com.dinh.logistics.model.FileData;
import com.dinh.logistics.respository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileUploadService {

    @Autowired
    private FileRepository fileRepository;

    public void uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        byte[] fileData = file.getBytes();
        fileRepository.save(new FileData(fileName, fileData));
    }
}
