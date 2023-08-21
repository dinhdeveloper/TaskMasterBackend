package com.dinh.logistics.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dinh.logistics.model.RolePj;

@Repository
public interface RolePjRepository extends JpaRepository<RolePj, Integer>{

	List<Integer> findByRoleCode(String string);

	
	
}
