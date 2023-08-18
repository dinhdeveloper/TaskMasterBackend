package com.dinh.logistics.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dinh.logistics.model.UserDevice;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Integer>{
	
	Optional <UserDevice> findByUserId(Integer userId);
	Optional <UserDevice> findByAccessTokenAndIsActiveAccessTokenTrue(String accessToken);
	Optional <UserDevice> findByIsActiveAccessTokenTrue();

	@Modifying
	@Query(value = "UPDATE user_devices SET is_active_access_token = ?1 WHERE user_id = ?2", nativeQuery = true)
	void updateIsActiveAccessTokenByUserId(boolean isActive, Integer userId);
	
	Optional <UserDevice> findByDeviceId(String deviceId);

}
