package com.dinh.logistics.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class JobMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_media_id")
    private Integer jobMediaId;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "url")
    private String url;

    @Column(name = "media_type")
    private Integer mediaType;

    @Column(name = "media_cate_id")
    private Integer mediaCateId;
}
