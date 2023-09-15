package com.dinh.logistics.dto.mobile;


public class MediaDto {
    private Integer jobId;
    private Integer mediaId;
    private String url;
    private String urlHard;
    private Integer mediaType;


    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlHard() {
        return urlHard;
    }

    public void setUrlHard(String urlHard) {
        this.urlHard = urlHard;
    }

    public Integer getMediaType() {
        return mediaType;
    }

    public void setMediaType(Integer mediaType) {
        this.mediaType = mediaType;
    }
}
