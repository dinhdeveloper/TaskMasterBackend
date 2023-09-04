package com.dinh.logistics.respository.mobile;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class CollectPointRepositoryImp {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertCollectPoint(String namePoint, String nameAddress, String nameContact, String phoneContact) {
        String sql = "INSERT INTO collect_point(name, num_address, contact_name, phone) VALUES (?, ?, ?, ?)";
        entityManager.createNativeQuery(sql)
                .setParameter(1, namePoint)
                .setParameter(2, nameAddress)
                .setParameter(3, nameContact)
                .setParameter(4, phoneContact)
                .executeUpdate();
    }
}
