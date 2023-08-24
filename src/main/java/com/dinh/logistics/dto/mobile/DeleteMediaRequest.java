package com.dinh.logistics.dto.mobile;

import lombok.Data;

@Data
public class DeleteMediaRequest {
    private int jobId;
    private String url;
    private int mediaType;
}
