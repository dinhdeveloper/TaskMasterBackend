package com.dinh.logistics.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dinh.logistics.model.Customers;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, Integer>{

}
