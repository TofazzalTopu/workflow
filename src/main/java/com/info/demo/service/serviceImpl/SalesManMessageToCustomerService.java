package com.info.demo.service.serviceImpl;

import com.info.demo.model.*;
import com.info.demo.repository.CustomerRepository;
import com.info.demo.repository.CustomerResponseRepository;
import com.info.demo.repository.MessageRepository;
import com.info.demo.repository.SalesManMessageToCustomerRepository;
import com.info.demo.service.inf.CustomersCurrentStageServiceInf;
import com.info.demo.service.inf.SalesManMessageToCustomerServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

// This service is responsible to persists the Message from the Sales Man to the Customer
// into the database
@Service
public class SalesManMessageToCustomerService implements SalesManMessageToCustomerServiceInf {

    // Inject the dependency with the @Autowired annotation
    @Autowired
    CustomerResponseRepository customerResponseRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SalesManMessageToCustomerRepository salesManMessageToCustomerRepository;

    @Autowired
    CustomersCurrentStageServiceInf customersCurrentStageService;

    @Override
    public String sendMessageToCustomer(Long customerId) {

        try {
            int stage[] = {1, 2, 3};
            // Find customer using customerId and stored in customer object
            Customer customer = customerRepository.getOne(customerId);

            // If customerList list isn't empty then a loop will execute for all the customer's and
            // sales man's message will sent to the customer for the specific stage
            // Find message stage for which message already sent
            CustomersCurrentStage customersCurrentStage = customersCurrentStageService.findByCustomer(customer);
            int stg = stage[0];
            if (customersCurrentStage != null) {
                stg = customersCurrentStage.getStage();
            } else {
                saveCustomersCurrentStage(customer, stg);
            }
            // Fetching message list for the particular stage and stored in stageWiseMessageList
            List<StageWiseMessage> stageWiseMessageList = messageRepository.findByStage(stg);

            // Find the message list for current the customer and stages and stored to stageAndCustomerWiseMessageList
            List<SalesManMessageToCustomer> stageAndCustomerWiseMessageList = findByCustomerAndStage(customer, stg);

            // Find the customerResponse for the current stages and customer and stored to customerResponse
            CustomerResponse customerResponse = customerResponseRepository.findByCustomerAndStage(customer, stg);

            // Checking if the total stage and current stage is equal
            if (stg == stage.length) {
                // If customerResponse is not null for the current stages
                // then sales man's message will not sent to the customer for the current stage
                if (customerResponse != null) {
                    return null;
                }

                // If the size of stageAndCustomerWiseMessageList and stageWiseMessageList is equal
                // then sales man's message will not sent to the customer for the current stage
                if (stageAndCustomerWiseMessageList.size() == stageWiseMessageList.size()) {
                    return null;
                }
            }

            // Total Number of message for the current customer and specific stage
            int msgNumber = stageAndCustomerWiseMessageList.size();

            if (stageAndCustomerWiseMessageList.size() < stageWiseMessageList.size()) {
                msgNumber = stageAndCustomerWiseMessageList.size();
            } else if (stageAndCustomerWiseMessageList.size() == stageWiseMessageList.size()) {
                stg = stg + 1;
                msgNumber = 0;
                saveCustomersCurrentStage(customer, stg);
                stageWiseMessageList = messageRepository.findByStage(stg);
            } else {
                return null;
            }
            /*customerResponse = customerResponseRepository.findByCustomerAndStage(customer, stg);
            if (customerResponse != null) {
                return null;
            }*/
            // Checking if the customer response already exists for the stage
            customerResponse = customerResponseRepository.findByCustomerAndStage(customer, stg);
            if (customerResponse != null) {
                stg = stg + 1;
                msgNumber = 0;
                stageWiseMessageList = messageRepository.findByStage(stg);
                saveCustomersCurrentStage(customer, stg);
            }
            saveSalesManMessageToCustomer(customer, stg, stageWiseMessageList.get(msgNumber));
            return null;
        } catch (NullPointerException n) {
            n.printStackTrace();
            return null;
        }
    }

    // save Customers Current Stage for which the customer is getting message's or sending response
    public void saveCustomersCurrentStage(Customer customer, int stage) {
        try {
            CustomersCurrentStage customersCurrentStage = customersCurrentStageService.findByCustomer(customer);
            if (customersCurrentStage == null) {
                customersCurrentStage = new CustomersCurrentStage();
                customersCurrentStage.setCustomer(customer);
            }
            customersCurrentStage.setStage(stage);
            customersCurrentStageService.save(customersCurrentStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // save Sales Man's Message To The specific Customer and for the specific stage
    public SalesManMessageToCustomer saveSalesManMessageToCustomer(Customer customer, int stage, StageWiseMessage message) {
        try {
            SalesManMessageToCustomer salesManMessageToCustomer = new SalesManMessageToCustomer();
            salesManMessageToCustomer.setCustomer(customer);
            salesManMessageToCustomer.setMessage(message);
            salesManMessageToCustomer.setStage(stage);
            salesManMessageToCustomer.setCreateDate(new Date());
            salesManMessageToCustomerRepository.save(salesManMessageToCustomer);
            return salesManMessageToCustomer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
        Find List of Sales Man's Message By Customer those are sent to the specific customer
    */
    @Override
    public List<SalesManMessageToCustomer> findByCustomerOrderByStageAsc(Customer customer) {
        return salesManMessageToCustomerRepository.findByCustomerOrderByStageAsc(customer);
    }

    /*
        Find List of Customers By Customer And Stage
    */
    @Override
    public List<SalesManMessageToCustomer> findByCustomerAndStage(Customer customer, int stage) {
        return salesManMessageToCustomerRepository.findByCustomerAndStage(customer, stage);
    }
}
