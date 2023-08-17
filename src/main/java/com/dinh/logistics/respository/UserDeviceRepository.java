package com.dinh.logistics.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dinh.logistics.model.UserDevice;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Long>{
	
	Optional <UserDevice> findByUserId(Long userId);
	Optional <UserDevice> findByAccessTokenAndIsActiveAccessTokenTrue(String accessToken);

}
