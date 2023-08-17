package com.dinh.logistics.respository.mobile;

import com.dinh.logistics.model.CollectPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectPointRepository extends JpaRepository<CollectPoint, Long> {
}
