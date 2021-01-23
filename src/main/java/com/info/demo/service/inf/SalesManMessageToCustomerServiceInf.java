package com.info.demo.service.inf;

import com.info.demo.model.Customer;
import com.info.demo.model.SalesManMessageToCustomer;

import java.util.List;

public interface SalesManMessageToCustomerServiceInf {

    String sendMessageToCustomer(Long customerId);

    List<SalesManMessageToCustomer> findByCustomerOrderByStageAsc(Customer customer);

    List<SalesManMessageToCustomer> findByCustomerAndStage(Customer customer, int stage);

}
