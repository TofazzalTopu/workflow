package com.info.demo.service.inf;

import com.info.demo.model.Customer;
import com.info.demo.model.CustomersCurrentStage;

public interface CustomersCurrentStageServiceInf {

    CustomersCurrentStage findByCustomer(Customer customer);
    CustomersCurrentStage save(CustomersCurrentStage stage);
}
