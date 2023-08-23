package com.dinh.logistics.dto.mobile;

public class UpdateJobsResponse {
    int stateJobs;
    String description;

    public int getStateJobs() {
        return stateJobs;
    }

    public void setStateJobs(int stateJobs) {
        this.stateJobs = stateJobs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
