package com.info.demo.job;

import com.info.demo.service.serviceImpl.SalesManMessageToCustomerService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

// This class implements Job interface and implements its overriding method execute()
public class SalesManMessageJobForCustomerThree implements Job {

    @Autowired
    private SalesManMessageToCustomerService salesManMessageToCustomerService;

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        //fetch parameters from JobDataMap object
        Long customerId = dataMap.getLong("customerId");
        System.out.println("Second parameter value : " + customerId);
        // Calling the sendMessageToCustomer method from the service SalesManMessageToCustomerService
        salesManMessageToCustomerService.sendMessageToCustomer(customerId);
    }
}
