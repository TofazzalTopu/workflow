package com.info.demo.service.inf;

import com.info.demo.model.Customer;
import com.info.demo.model.CustomerResponse;

import java.util.List;

public interface CustomerResponseServiceInf {

    CustomerResponse findByCustomerAndStage(Customer customer, int stage);
    CustomerResponse save(CustomerResponse customerResponse);
    List<CustomerResponse> findByCustomer(Customer customer);
    List<CustomerResponse> findByCustomerOrderByStage(Customer customer);
}
