package com.dinh.logistics.respository;

import com.dinh.logistics.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository  extends JpaRepository<Customer, Long> {

//    @Query("SELECT e FROM Customer e WHERE e.userName = :userName AND e.passWord = :passWord")
//    Customer loginUser(@Param("userName")String userName, @Param("passWord")String passWord);

    @Query("SELECT e FROM Customer e WHERE e.userName = :userName AND e.passWord = :passWord")
    List<Customer> findByUsernameAndPassword(@Param("userName") String userName, @Param("passWord") String passWord);

    @Query("SELECT c FROM Customer c WHERE c.userName LIKE %:keyword%")
    List<Customer> searchCustomers(@Param("keyword") String keyword);

}
