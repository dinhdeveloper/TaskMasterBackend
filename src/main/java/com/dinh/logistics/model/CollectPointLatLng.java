package com.dinh.logistics.model;

import lombok.Data;

@Data
public class CollectPointLatLng {
    private Integer jobId;
    private String latitude;
    private String longitude;
    private String cpName;
    private String fullName;
    private String jobStateDesc;
}
