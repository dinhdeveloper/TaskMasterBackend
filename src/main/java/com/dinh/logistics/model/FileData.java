package com.dinh.logistics.model;

import javax.persistence.*;

@Entity
public class FileData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @Lob
    private byte[] fileData;

    // Constructors, getters, and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public FileData(Long id, String fileName, byte[] fileData) {
        this.id = id;
        this.fileName = fileName;
        this.fileData = fileData;
    }

    public FileData(String fileName, byte[] fileData) {
        this.fileName = fileName;
        this.fileData = fileData;
    }


}

