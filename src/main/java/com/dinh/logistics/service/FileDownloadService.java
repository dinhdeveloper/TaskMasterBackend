package com.dinh.logistics.service;

import com.dinh.logistics.model.FileData;
import com.dinh.logistics.respository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileDownloadService {

    @Autowired
    private FileRepository fileRepository;

    public FileData getFileDataById(Long fileId) {
        return fileRepository.findById(fileId).orElseThrow(() -> new RuntimeException("File not found"));
    }
}

