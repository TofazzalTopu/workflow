package com.info.demo.controller;

import com.info.demo.job.SalesManMessageJobForCustomerOne;
import com.info.demo.job.SalesManMessageJobForCustomerTwo;
import com.info.demo.model.Customer;
import com.info.demo.model.CustomerResponse;
import com.info.demo.model.CustomersCurrentStage;
import com.info.demo.model.SalesManMessageToCustomer;
import com.info.demo.repository.CustomerRepository;
import com.info.demo.service.inf.CustomerResponseServiceInf;
import com.info.demo.service.inf.CustomersCurrentStageServiceInf;
import com.info.demo.service.inf.SalesManMessageToCustomerServiceInf;
import io.swagger.annotations.ApiOperation;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
    Annotate the with @RestController to define the class is a controller class and
     will work as RESTFul webservice
*/

@CrossOrigin
@Controller
@RequestMapping(value = "/workflow")
public class WorkflowController {

    // Inject the dependency with the @Autowired annotation
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerResponseServiceInf customerResponseService;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    CustomersCurrentStageServiceInf customersCurrentStageService;

    @Autowired
    SalesManMessageToCustomerServiceInf salesManMessageToCustomerService;

    // @Read the property value from application.properties file
    @Value("${scheduler_delay_in_second_for_customer_one}")
    private String scheduler_delay_in_second_for_customer_one;

    @Value("${scheduler_delay_in_second_for_customer_two}")
    private String scheduler_delay_in_second_for_customer_two;

    @Value("${scheduler_delay_in_second_for_customer_three}")
    private String scheduler_delay_in_second_for_customer_three;

    /*
    Load workflow form in which you can start the workflow with a list of customers
   */
    @ApiOperation(value = "Load workflow form with a list of customers")
    @RequestMapping(value = {"/workflow"}, method = RequestMethod.GET)
    public String workflow(Model model) {
        List<Customer> customerList = new ArrayList<>();
        try {
            // Find all customer list
            customerList = customerRepository.findAll();
            model.addAttribute("customerList", customerList);
            return "workflow/workflow";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("customerList", customerList);
            return "workflow/workflow";
        }
    }

    // @Comment: Load customer form with a list of customers in a table
    @ApiOperation(value = "Load customer form with a list of customers in a table")
    @RequestMapping(method = RequestMethod.GET, value = {"/customerList"})
    public String customerList(Model model) throws IOException {
        List<Customer> customerList = new ArrayList<>();
        try {
            // Find all customer list
            customerList = customerRepository.findAll();
            // print the result
            //Iterating customer list by passing lambda expression with the  forEach() method
            customerList.forEach(customer -> {
                System.out.println(customer.getId());
            });
            if (customerList.isEmpty()) {
                model.addAttribute("error", "No data found!");
            }
            model.addAttribute("customerList", customerList);
            return "workflow/customer_list";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("public/error", "Error during fetching data!");
            model.addAttribute("customerList", customerList);
            return "workflow/customer_list";
        }
    }

    // View Customer Wise Message List - fetching message list by customer id
    @ApiOperation(value = "View Customer Wise Message List - fetching message list by customer id")
    @RequestMapping(value = "/viewCustomerWiseMessageList/{id}", method = RequestMethod.GET)
    public String viewCustomerWiseMessageList(Model model, @PathVariable Long id) {
//        fetching Customer by customer id
        Optional<Customer> customer = customerRepository.findById(id);
        // Declaring the ArrayList which will store list of SalesManMessageToCustomer object
        List<SalesManMessageToCustomer> salesManMessageToCustomerList = new ArrayList<>();
        try {
            if (customer.isPresent()) {
                // Find Message list which is sent to the specific customer from sales man
                salesManMessageToCustomerList = salesManMessageToCustomerService.findByCustomerOrderByStageAsc(customer.get());
                model.addAttribute("salesManMessageToCustomerList", salesManMessageToCustomerList);
                return "workflow/customer_message_list";
            } else {
                model.addAttribute("error", "ERROR - Customer Message not exist!");
                model.addAttribute("salesManMessageToCustomerList", salesManMessageToCustomerList);
                return "workflow/customer_message_list";
            }
        } catch (Exception e) {
            model.addAttribute("salesManMessageToCustomerList", salesManMessageToCustomerList);
            return "workflow/customer_message_list";
        }
    }

    // Save customer response (If a customer respond to a message sent by the sales man)
    @ApiOperation(value = "Save customer response")
    @RequestMapping(value = {"/saveResponse"}, method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> saveResponse(@RequestParam(name = "response") String responseMessage,
                                            @RequestParam(name = "customerId") Long customerId) {
        Map<String, String> map = new HashMap<>();
        // Declaring the ArrayList which will store list of CustomerResponse object
        List<CustomerResponse> customerResponseList = new ArrayList<>();
        // create a StringBuilder object using StringBuilder() constructor
        StringBuilder customerResponseSB = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        try {
            // creating object of class Customer
            Customer customer = new Customer();
            if (customerId != null) {
                customer = customerRepository.getOne(customerId);
                if (customer != null) {
                    // Find the current stage for which message already sent
                    CustomersCurrentStage customersCurrentStage = customersCurrentStageService.findByCustomer(customer);

                    int stage = 1;
                    // If customers current stage is empty the save new customersCurrentStage
                    if (customersCurrentStage != null) {
                        stage = customersCurrentStage.getStage();
                        saveCustomerResponse(customer, stage, responseMessage);
                        map.put("success", "Customer " + customer.getName() + "'s response saved successfully.");
                    }

                    // Find customer response list for the specific customer
                    customerResponseList = customerResponseService.findByCustomerOrderByStage(customer);

                    // appending customer response message to customerResponseSB object
                    customerResponseSB.append("Response History From The Customer " + customerResponseList.get(0).getCustomer().getName() + ":: \n " + "\n ");
                    //Iterating customer response list by passing lambda expression with the  forEach() method
                    customerResponseList.forEach(customerResponse -> {
                        customerResponseSB.append("Stage:: " + customerResponse.getStage() + "   Message:: " + customerResponse.getResponseMessage() + "                     Time: " + simpleDateFormat.format(customerResponse.getCreateDate()) + "\n \n");
                    });
                }
            }
            map.put("responseMessage", customerResponseSB.toString());
            return map;
        } catch (Exception e) {
            map.put("error", "Something went wrong - please try again!");
            return map;
        }
    }

    // This end point will return the Customer Response history for a specific customer
    @ApiOperation(value = "Return the Customer Response history for a specific customer")
    @RequestMapping(value = {"/responseHistory"}, method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> responseHistory(Model model,
                                               @RequestParam(name = "customerId") Long customerId) throws SchedulerException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        Map<String, String> map = new HashMap<>();
        List<SalesManMessageToCustomer> salesManMessageToCustomerList = new ArrayList<>();
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
                    // Find Message list which is sent to the specific customer from sales man
                    salesManMessageToCustomerList = salesManMessageToCustomerService.findByCustomerOrderByStageAsc(customer);
                    // Find customer response list for the specific customer
                    customerResponseList = customerResponseService.findByCustomerOrderByStage(customer);
                }
            }
            if (!salesManMessageToCustomerList.isEmpty()) {
                // appending Workflow Start time to messageToCustomerSB object
                messageToCustomerSB.append("Workflow Started at:: " + simpleDateFormat.format(salesManMessageToCustomerList.get(0).getCreateDate()) + ".\n " + "\n ");
                messageToCustomerSB.append("Message History For The Customer " + salesManMessageToCustomerList.get(0).getCustomer().getName() + ":: \n " + "\n ");
                // appending sales man's message to messageToCustomerSB object
                salesManMessageToCustomerList.forEach(salesManMessageToCustomer -> {
                    messageToCustomerSB.append("Stage:: (" + salesManMessageToCustomer.getStage() + ") " + "                   Time: " + simpleDateFormat.format(salesManMessageToCustomer.getCreateDate()) + " \n" +
                            salesManMessageToCustomer.getMessage().getMessage().replace("$", salesManMessageToCustomer.getCustomer().getName()) + "\n \n");
                });
            }

            if (!customerResponseList.isEmpty()) {
                // appending customer's response to customerResponseSB object
                customerResponseSB.append("Response History From The Customer " + customerResponseList.get(0).getCustomer().getName() + ":: \n " + "\n ");
                customerResponseList.forEach(customerResponse -> {
                    customerResponseSB.append("Stage:: " + customerResponse.getStage() + "   Message:: " + customerResponse.getResponseMessage() + "                     Time: " + simpleDateFormat.format(customerResponse.getCreateDate()) + "\n \n");
                });
            }
            //Put messageToCustomerSB and customerResponseSB into to map
            map.put("messageToCustomerSB", messageToCustomerSB.toString());
            map.put("customerResponseSB", customerResponseSB.toString());
            return map;
        } catch (Exception e) {
            map.put("error", "Something went wrong - please try again!");
            return map;
        }
    }

    /*
    This end point will start workflow For The Customer One
    after the 'Start Workflow' button is clicked from the front-end
   */
    @ApiOperation(value = "Start workflow For The Customer One")
    @RequestMapping(value = {"/startWorkflowForCustomerOne"}, method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> startWorkflowForCustomerOne(@RequestParam(name = "customerId") Long customerId) {
        Map<String, String> map = new HashMap<>();
        try {
            if (customerId != null) {
                Customer customer = customerRepository.getOne(customerId);
                if (customer != null) {
                    // Call the executeWorkflowForCustomerThree method to start the workflow for the customer one
                    executeWorkflowForCustomerOne(customerId);
                }
            }
            return map;
        } catch (Exception e) {
            map.put("error", "Something went wrong - please try again!");
            return map;
        }
    }

    // This end point will start workflow For The Customer Two after the 'Start Workflow' button is clicked from the front-end
    @ApiOperation(value = "Start workflow For The Customer Two")
    @RequestMapping(value = {"/startWorkflowForCustomerTwo"}, method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> startWorkflowForCustomerTwo(@RequestParam(name = "customerId") Long customerId) {
        Map<String, String> map = new HashMap<>();
        try {
            if (customerId != null) {
                // Find customer by customerId
                Customer customer = customerRepository.getOne(customerId);
                if (customer != null) {
                    // Call the executeWorkflowForCustomerThree method to start the workflow for the customer two
                    executeWorkflowForCustomerTwo(customerId);
                    map.put("success", "Workflow Started For The Customer " + customer.getName());
                }
            }
            return map;
        } catch (Exception e) {
            map.put("error", "Something went wrong - please try again!");
            return map;
        }
    }

    // This end point will start workflow For The Customer Three after the 'Start Workflow' button is clicked from the front-end
    @ApiOperation(value = "Start workflow For The Customer Three")
    @RequestMapping(value = {"/startWorkflowForCustomerThree"}, method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> startWorkflowForCustomerThree(@RequestParam(name = "customerId") Long customerId) {
        Map<String, String> map = new HashMap<>();
        try {
            if (customerId != null) {
                // Find customer by customerId
                Customer customer = customerRepository.getOne(customerId);
                if (customer != null) {
                    // Call the executeWorkflowForCustomerThree method to start the workflow for the customer three
                    executeWorkflowForCustomerThree(customerId);
                    map.put("success", "Workflow Started For The Customer " + customer.getName());
                }
            }
            return map;
        } catch (Exception e) {
            map.put("error", "Something went wrong - please try again!");
            return map;
        }
    }

    // Workflow method which will execute the workflow scheduler
    public void executeWorkflowForCustomerOne(Long customerId) throws SchedulerException {
        //Create a new Job
        JobKey jobKeyOne = JobKey.jobKey("workflowJobForCustomerOne", "workflowJobGroupForCustomerOne");

        // defining an instance of the Job using the JobDetail class
        JobDetail job = JobBuilder.newJob(SalesManMessageJobForCustomerOne.class).withIdentity(jobKeyOne).storeDurably().build();
        // Put the parameter
        job.getJobDataMap().put("customerId", customerId);
        // schedule the job
        scheduler.scheduleJob(job, triggerForCustomerOne(job));
    }

    // Trigger will schedule a Job, i.e. a Trigger instance “fires” the execution of a job
    private SimpleTrigger triggerForCustomerOne(JobDetail jobDetail) {
        System.out.println("scheduler_delay_in_second_for_customer_one:: " + scheduler_delay_in_second_for_customer_one);
        return TriggerBuilder.newTrigger().forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), jobDetail.getKey().getGroup())
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(Integer.parseInt(scheduler_delay_in_second_for_customer_one)))
                .build();
    }

    // Workflow method which will execute the workflow scheduler
    public void executeWorkflowForCustomerTwo(Long customerId) throws SchedulerException {
        //Create a new Job
        JobKey jobKey = JobKey.jobKey("workflowJobForCustomerTwo", "workflowJobGroupForCustomerTwo");

        // defining an instance of the Job using the JobDetail class
        JobDetail job = JobBuilder.newJob(SalesManMessageJobForCustomerTwo.class).withIdentity(jobKey).storeDurably().build();
        // Put the parameter
        job.getJobDataMap().put("customerId", customerId);
        // schedule the job
        scheduler.scheduleJob(job, triggerForCustomerTwo(job));
    }

    // Trigger will schedule a Job, i.e. a Trigger instance “fires” the execution of a job
    private SimpleTrigger triggerForCustomerTwo(JobDetail jobDetail) {
        System.out.println("scheduler_delay_in_second_for_customer_two:: " + scheduler_delay_in_second_for_customer_two);
        return TriggerBuilder.newTrigger().forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), jobDetail.getKey().getGroup())
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(Integer.parseInt(scheduler_delay_in_second_for_customer_two)))
                .build();
    }

    // Workflow method which will execute the workflow scheduler
    public void executeWorkflowForCustomerThree(Long customerId) throws SchedulerException {
        //Create a new Job
        JobKey jobKey = JobKey.jobKey("workflowJobForCustomerThree", "workflowJobGroupForCustomerThree");

        // defining an instance of the Job using the JobDetail class
        JobDetail job = JobBuilder.newJob(SalesManMessageJobForCustomerOne.class).withIdentity(jobKey).storeDurably().build();
        // Put the parameter
        job.getJobDataMap().put("customerId", customerId);
        // schedule the job
        scheduler.scheduleJob(job, triggerForCustomerThree(job));
    }

    // Trigger will schedule a Job, i.e. a Trigger instance “fires” the execution of a job
    private SimpleTrigger triggerForCustomerThree(JobDetail jobDetail) {
        System.out.println("scheduler_delay_in_second_for_customer_three:: " + scheduler_delay_in_second_for_customer_three);
        return TriggerBuilder.newTrigger().forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), jobDetail.getKey().getGroup())
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(Integer.parseInt(scheduler_delay_in_second_for_customer_three)))
                .build();
    }

    // This method will create a new customer response object
    public void saveCustomerResponse(Customer customer, int stage, String responseMessage) {
        CustomerResponse customerResponse = customerResponseService.findByCustomerAndStage(customer, stage);
        if (customerResponse != null) {
            return;
        }
        // creating object of class CustomerResponse
        customerResponse = new CustomerResponse();
        customerResponse.setResponseMessage(responseMessage);
        customerResponse.setStage(stage);
        customerResponse.setCustomer(customer);
        customerResponse.setCreateDate(new Date());
        customerResponseService.save(customerResponse);
    }

}
