package com.dinh.logistics.respository;

import com.dinh.logistics.model.CollectPoint;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectPointRepository extends JpaRepository<CollectPoint, Integer> {
	
	@Query(value = "select colle_point_id from collect_point", nativeQuery = true)
	List<Integer> findAllCollectPointId();
	
}
