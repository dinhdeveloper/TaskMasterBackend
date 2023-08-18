package com.dinh.logistics.respository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class JobMediaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void insertJobMedia(String url, Integer mediaType, Integer mediaCateId, Integer jobId) {
        String sql = "INSERT INTO job_media(url, media_type, media_cate_id, job_id) VALUES (?, ?, ?, ?)";
        entityManager.createNativeQuery(sql)
                .setParameter(1, url)
                .setParameter(2, mediaType)
                .setParameter(3, mediaCateId)
                .setParameter(4, jobId)
                .executeUpdate();
    }
}

