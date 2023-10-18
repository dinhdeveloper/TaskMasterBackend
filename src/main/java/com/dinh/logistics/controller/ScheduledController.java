package com.dinh.logistics.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dinh.logistics.model.JobMedia;
import com.dinh.logistics.respository.JobMediaRepository;
import com.dinh.logistics.respository.JobMediaV2Repository;

@Component
public class ScheduledController {
	
	@Autowired
	JobMediaRepository jobMediaRepository;
	
	@Autowired
	JobMediaV2Repository jobMediaV2Repository;
	
	@Value("${app.deleteFileMedia.time}")
	private int deleteFileMediaTime;

	@Scheduled(cron = "${app.scheduler.cron.deleteFileMedia}")
    public void deleteMedia() {
		
		List<JobMedia> jobMediaList = jobMediaV2Repository.findAll();
		List<JobMedia> jobMediaListDelete = new ArrayList<>();
		Date currentDate = new Date();
		
		for(JobMedia jobMedia : jobMediaList) {
			if(currentDate.getTime() - jobMedia.getCreateDate().getTime() > deleteFileMediaTime) {
				jobMediaListDelete.add(jobMedia);
			}
		}
		jobMediaV2Repository.deleteAll(jobMediaListDelete);
//		jobMediaRepository.deleteAll(jobMediaListDelete);
		
		for(JobMedia jobMedia : jobMediaListDelete) {
			File file = new File(jobMedia.getUrl());
			file.delete();
		}
		
    }
	
}
