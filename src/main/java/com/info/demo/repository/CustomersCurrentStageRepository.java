package com.info.demo.repository;

import com.info.demo.model.Customer;
import com.info.demo.model.CustomersCurrentStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomersCurrentStageRepository extends JpaRepository<CustomersCurrentStage, Long> {

    CustomersCurrentStage findByCustomer(Customer customer);
}
