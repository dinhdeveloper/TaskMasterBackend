package com.dinh.logistics.respository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    public JobMedia findByJobIdAndUrlAndMediaType(int jobId, String url, int mediaType) {
        String jpql = "SELECT jm FROM JobMedia jm WHERE jm.jobId = :jobId AND jm.url = :url AND jm.mediaType = :mediaType";
        try {
            return entityManager.createQuery(jpql, JobMedia.class)
                    .setParameter("jobId", jobId)
                    .setParameter("url", url)
                    .setParameter("mediaType", mediaType)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Trả về null nếu không có kết quả thỏa mãn
        }
    }
    
    public List<JobMedia> findAll() {
        String hql = "SELECT * FROM JobMedia";
        TypedQuery<JobMedia> query = entityManager.createQuery(hql, JobMedia.class);
        return query.getResultList();
    }

	public void deleteAll(List<JobMedia> jobMediaListDelete) {
		for (JobMedia entity : jobMediaListDelete) {
	        entityManager.remove(entity);
	    }
	}

//    @Transactional
//    public void deleteJobMediaByUrl(String url) {
//        String sql = "DELETE FROM job_media WHERE url = :url";
//        entityManager.createNativeQuery(sql)
//                .setParameter("url", url)
//                .executeUpdate();
//    }
}

