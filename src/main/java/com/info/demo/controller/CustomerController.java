package com.info.demo.controller;

import com.info.demo.model.Customer;
import com.info.demo.model.CustomerResponse;
import com.info.demo.model.SalesManMessageToCustomer;
import com.info.demo.repository.CustomerRepository;
import com.info.demo.service.inf.CustomerResponseServiceInf;
import com.info.demo.service.inf.SalesManMessageToCustomerServiceInf;
import io.swagger.annotations.ApiOperation;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

    // Inject the dependency with the @Autowired annotation
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SalesManMessageToCustomerServiceInf salesManMessageToCustomerService;

    @Autowired
    CustomerResponseServiceInf customerResponseService;

    // @Comment: Load customer form with a list of customers in a table
    @ApiOperation(value = "Load customer form with a list of customers in a table")
    @RequestMapping(method = RequestMethod.GET, value = {"/customerListForReact"})
    public List<Customer> customerListForReact(Model model) throws IOException {
        List<Customer> customerList = new ArrayList<>();
        try {
            // Find all customer list
            customerList = customerRepository.findAll();
            // print the result
            //Iterating customer list by passing lambda expression with the  forEach() method
            customerList.forEach(customer -> {
                System.out.println(customer.getId());
            });
            return customerList;
        } catch (Exception e) {
            e.printStackTrace();
            return customerList;
        }
    }

    // View Customer Wise Message List - fetching message list by customer id
    @ApiOperation(value = "View Customer Wise Message List - fetching message list by customer id")
    @RequestMapping(value = "/viewMessageListByCustomerId/{id}", method = RequestMethod.GET)
    public List<SalesManMessageToCustomer> viewMessageListByCustomerId(Model model, @PathVariable Long id) {
//        fetching Customer by customer id
        Optional<Customer> customer = customerRepository.findById(id);
        // Declaring the ArrayList which will store list of SalesManMessageToCustomer object
        List<SalesManMessageToCustomer> salesManMessageToCustomerList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        try {
            if (customer.isPresent()) {
                // Find Message list which is sent to the specific customer from sales man
                salesManMessageToCustomerList = salesManMessageToCustomerService.findByCustomerOrderByStageAsc(customer.get());
                map.put("salesManMessageToCustomerList", salesManMessageToCustomerList.toString());
            } else {
                map.put("error", "ERROR - Customer Message not exist!");
                map.put("salesManMessageToCustomerList", salesManMessageToCustomerList.toString());
            }
        } catch (Exception e) {
            map.put("salesManMessageToCustomerList", salesManMessageToCustomerList.toString());
        }
        return salesManMessageToCustomerList;
    }

    // This end point will return the Customer Response history for a specific customer
    @ApiOperation(value = "Return the Customer Response history for a specific customer")
    @RequestMapping(value = {"/findResponseHistory"}, method = RequestMethod.GET)
//    @ResponseBody
    public String responseHistory(Model model,
                                               @RequestParam(name = "customerId") Long customerId) throws SchedulerException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        List<CustomerResponse> customerResponseList = new ArrayList<>();
        StringBuilder messageToCustomerSB = new StringBuilder();
        StringBuilder customerResponseSB = new StringBuilder();
        try {
            // creating object of class Customer
            Customer customer = new Customer();
            if (customerId != null) {
                // Get the Customer with customerId
                customer = customerRepository.getOne(customerId);
                if (customer != null) {
                    // Find customer response list for the specific customer
                    customerResponseList = customerResponseService.findByCustomerOrderByStage(customer);
                }
            }

            if (!customerResponseList.isEmpty()) {
                // appending customer's response to customerResponseSB object
                customerResponseSB.append("Response History From The Customer " + customerResponseList.get(0).getCustomer().getName() + ":: \n " + "\n ");
                customerResponseList.forEach(customerResponse -> {
                    customerResponseSB.append("Stage:: " + customerResponse.getStage() + "   Message:: " + customerResponse.getResponseMessage() + "                     Time: " + simpleDateFormat.format(customerResponse.getCreateDate()) + "\n \n");
                });
            }
            return customerResponseSB.toString();
        } catch (Exception e) {
            return "Error";
        }
    }
}
