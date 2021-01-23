package com.info.demo.service.serviceImpl;

import com.info.demo.model.Customer;
import com.info.demo.model.CustomersCurrentStage;
import com.info.demo.repository.CustomersCurrentStageRepository;
import com.info.demo.service.inf.CustomersCurrentStageServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomersCurrentStageService implements CustomersCurrentStageServiceInf {

    // Inject the dependency with the @Autowired annotation
    @Autowired
    CustomersCurrentStageRepository customersCurrentStageRepository;

    //Find CustomersCurrentStage using Customer
    @Override
    public CustomersCurrentStage findByCustomer(Customer customer){
        return customersCurrentStageRepository.findByCustomer(customer);
    }

    //Save CustomersCurrentStage
    @Override
    public CustomersCurrentStage save(CustomersCurrentStage stage){
        return customersCurrentStageRepository.save(stage);
    }

}
