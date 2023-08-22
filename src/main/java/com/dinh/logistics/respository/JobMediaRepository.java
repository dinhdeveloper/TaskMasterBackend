package com.dinh.logistics.respository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.dinh.logistics.model.JobMedia;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobMediaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void insertJobMedia(String url, Integer mediaType, Integer jobId) {
        String sql = "INSERT INTO job_media(url, media_type, job_id) VALUES (?, ?, ?)";
        entityManager.createNativeQuery(sql)
                .setParameter(1, url)
                .setParameter(2, mediaType)
                .setParameter(3, jobId)
                .executeUpdate();
    }

    public List<JobMedia> getJobMediaByJobIdAndMediaType(int jobId, int mediaType) {
        String hql = "SELECT jm FROM JobMedia jm WHERE jm.jobId = :jobId AND jm.mediaType = :mediaType";
        TypedQuery<JobMedia> query = entityManager.createQuery(hql, JobMedia.class);
        query.setParameter("jobId", jobId);
        query.setParameter("mediaType", mediaType);
        return query.getResultList();
    }

    public void deleteJobMediaByUrl(String url) {
        String sql = "DELETE FROM job_media WHERE url = ?";
        entityManager.createNativeQuery(sql)
                .setParameter(1, url)
                .executeUpdate();
    }
}

