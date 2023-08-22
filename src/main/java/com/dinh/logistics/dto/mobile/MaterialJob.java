package com.dinh.logistics.dto.mobile;

public class MaterialJob {
    private Integer mateId;
    private Integer jobId;
    private Integer weight;
    private Integer weightToCus;
    private Integer price;

    public Integer getMateId() {
        return mateId;
    }

    public void setMateId(Integer mateId) {
        this.mateId = mateId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getWeightToCus() {
        return weightToCus;
    }

    public void setWeightToCus(Integer weightToCus) {
        this.weightToCus = weightToCus;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
