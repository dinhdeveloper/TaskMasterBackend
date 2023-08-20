package com.dinh.logistics.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class MediaCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_cate_id")
    private Integer mediaCateId;

    @Column(name = "media_desc")
    private String mediaDesc;

    @Column(name = "state")
    private boolean state;

}
