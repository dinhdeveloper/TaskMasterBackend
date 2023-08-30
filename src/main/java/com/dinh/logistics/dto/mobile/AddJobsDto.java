package com.dinh.logistics.dto.mobile;

import lombok.Data;

import java.util.List;

@Data
public class AddJobsDto {
    private int jobType;
    private int nv1Id;
    private int nv2Id;
    private int assignId;
    private List<Integer> listIdPoint;
    private String ghiChu;
}
