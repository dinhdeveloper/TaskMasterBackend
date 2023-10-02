package com.dinh.logistics.dto.mobile;

public class MaterialJob {
    private Integer mateId;
    private Integer jobId;
    private Double weight;
    private Double weightToCus;
    private Double price;
    private String  name;

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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getWeightToCus() {
        return weightToCus;
    }

    public void setWeightToCus(Double weightToCus) {
        this.weightToCus = weightToCus;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
