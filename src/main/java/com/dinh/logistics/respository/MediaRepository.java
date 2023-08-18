package com.dinh.logistics.respository;

import com.dinh.logistics.model.JobMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MediaRepository extends JpaRepository<JobMedia, Integer> {

    @Query(value = "INSERT INTO job_media(url, media_type, media_cate_id, job_id) VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
    void insertJobMedia(String url, Integer mediaType, Integer mediaCateId, Integer jobId);
}