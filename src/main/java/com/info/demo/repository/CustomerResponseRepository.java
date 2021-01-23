package com.info.demo.repository;

import com.info.demo.model.Customer;
import com.info.demo.model.CustomerResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerResponseRepository extends JpaRepository<CustomerResponse, Long> {
    CustomerResponse findByCustomerAndStage(Customer customer, int stage);
//    List<CustomerResponse> findByCustomerAndStage(Customer customer, int stage);
    List<CustomerResponse> findByCustomer(Customer customer);
    List<CustomerResponse> findByCustomerOrderByStage(Customer customer);
}