package com.dinh.logistics.respository;

import com.dinh.logistics.model.CollectPoint;
import com.dinh.logistics.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {
    @Query(value = "select * from material WHERE state = true", nativeQuery = true)
    List<Material> findAllMaterialByState();
}
