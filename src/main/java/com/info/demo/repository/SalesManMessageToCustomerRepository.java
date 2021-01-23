package com.info.demo.repository;

import com.info.demo.model.Customer;
import com.info.demo.model.SalesManMessageToCustomer;
import com.info.demo.model.StageWiseMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesManMessageToCustomerRepository extends JpaRepository<SalesManMessageToCustomer, Long> {
    List<SalesManMessageToCustomer> findByCustomerAndStage(Customer customer, int stage);
    List<SalesManMessageToCustomer> findByCustomer(Customer customer);
    List<SalesManMessageToCustomer> findByCustomerAndStageAndMessage(Customer customer, int stage, StageWiseMessage message);
    List<SalesManMessageToCustomer> findByCustomerOrderByStageAsc(Customer customer);
}
