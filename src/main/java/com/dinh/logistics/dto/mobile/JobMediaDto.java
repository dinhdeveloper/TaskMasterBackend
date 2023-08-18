package com.dinh.logistics.dto.mobile;

import lombok.Data;

import javax.persistence.Column;

@Data
public class JobMediaDto {
    private Integer jobId;

    private String url;

    private Integer mediaType;

    private Integer mediaCateId;

}
