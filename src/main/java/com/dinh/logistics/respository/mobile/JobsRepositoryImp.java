package com.dinh.logistics.respository.mobile;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JobsRepositoryImp {

    @PersistenceContext
    private EntityManager entityManager;

    private List<Integer> idJobs = new ArrayList<Integer>();

    public void insertCollectPoint(int jobType, int idNV1, int idNV2, List<Integer> listIdPoint, String ghiChu) {
        for (int i = 0; i < listIdPoint.size(); i++) {
            String sql = "INSERT INTO jobs(colle_point_id, job_type_id,payment_state_id, note) VALUES (?, ?, ?,?) RETURNING job_id";
            Integer generatedId = (Integer) entityManager.createNativeQuery(sql)
                    .setParameter(1, listIdPoint.get(i))
                    .setParameter(2, jobType)
                    .setParameter(3, 1)
                    .setParameter(4, ghiChu)
                    .getSingleResult();

            String sql2 = "INSERT INTO job_employee(job_id, emp_id) VALUES (?, ?)";
            entityManager.createNativeQuery(sql2)
                    .setParameter(1, generatedId)
                    .setParameter(2, idNV1)
                    .executeUpdate();

            String sql3 = "INSERT INTO job_employee(job_id, emp_id) VALUES (?, ?)";
            entityManager.createNativeQuery(sql3)
                    .setParameter(1, generatedId)
                    .setParameter(2, idNV2)
                    .executeUpdate();

        }
    }
}
