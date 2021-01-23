package com.info.demo.service.serviceImpl;

import com.info.demo.model.Customer;
import com.info.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    // Inject the dependency with the @Autowired annotation
    @Autowired
    CustomerRepository customerRepository;

    // save Customer
    public void saveCustomer(){
        // Find the existing list of customers
        List<Customer> customerList = customerRepository.findAll();

        try{
            // Customer will be persists if the customerList is empty
            if (customerList.isEmpty()) {
                // Declare an Array with customer names
                String customerNames[] = {"John Doe", "Daniel Christian", "David"};

                // Execute for loop to save customers object
                for (int i = 0; i < customerNames.length; i++) {
                    Customer customer = new Customer();
                    customer.setName(customerNames[i]);
                    customerRepository.save(customer);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
