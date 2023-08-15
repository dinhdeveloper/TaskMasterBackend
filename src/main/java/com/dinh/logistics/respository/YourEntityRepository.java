package com.dinh.logistics.respository;

import com.dinh.logistics.model.YourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YourEntityRepository extends JpaRepository<YourEntity, Long> {
    // You can add custom query methods here if needed
}

