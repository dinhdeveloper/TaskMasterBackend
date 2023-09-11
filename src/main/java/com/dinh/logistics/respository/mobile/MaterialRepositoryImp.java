package com.dinh.logistics.respository.mobile;


import com.dinh.logistics.model.JobMaterial;
import com.dinh.logistics.model.JobMedia;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class MaterialRepositoryImp {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void addMaterial(int mateId, int jobId, Long weight, Long weightToCus, Long price) {
        // Kiểm tra nếu weightToCus là null, thay thế nó bằng 0
        if (weightToCus == null) {
            weightToCus = 0L;
        }
        if (weight == null) {
            weight = 0L;
        }
        if (price == null) {
            price = 0L;
        }

        String sql = "INSERT INTO job_material(mate_id, job_id, weight, weight_to_cus, unit_price) VALUES (?, ?, ?, ?, ?)";
        entityManager.createNativeQuery(sql)
                .setParameter(1, mateId)
                .setParameter(2, jobId)
                .setParameter(3, weight)
                .setParameter(4, weightToCus)
                .setParameter(5, price)
                .executeUpdate();
    }

}
