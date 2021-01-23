package com.info.demo.service.serviceImpl;

import com.info.demo.model.Customer;
import com.info.demo.model.CustomerResponse;
import com.info.demo.repository.CustomerResponseRepository;
import com.info.demo.service.inf.CustomerResponseServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerResponseService implements CustomerResponseServiceInf {

    // Inject the dependency with the @Autowired annotation
    @Autowired
    CustomerResponseRepository customerResponseRepository;

    //Find CustomerResponse using Customer ID and Stage
    @Override
    public CustomerResponse findByCustomerAndStage(Customer customer, int stage) {
        return customerResponseRepository.findByCustomerAndStage(customer, stage);
    }

    //Save customer response
    @Override
    public CustomerResponse save(CustomerResponse customerResponse) {
        return customerResponseRepository.save(customerResponse);
    }

    //Find list of response using Customer ID
    @Override
    public List<CustomerResponse> findByCustomer(Customer customer) {
        return customerResponseRepository.findByCustomer(customer);
    }

    //Find list of response using Customer ID and Order By Stage
    @Override
    public List<CustomerResponse> findByCustomerOrderByStage(Customer customer) {
        return customerResponseRepository.findByCustomerOrderByStage(customer);
    }
}
