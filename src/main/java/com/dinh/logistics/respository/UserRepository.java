package com.dinh.logistics.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.dinh.logistics.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
	
	Optional<Users> findByUserName(String username);
	
	@Query(value = "select * from users where user_name=?1 ", nativeQuery = true)
	Users findUserByUserName(String userName);
	
}
