package com.info.demo;

import com.info.demo.service.serviceImpl.CustomerService;
import com.info.demo.service.serviceImpl.StageWiseMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication annotation define that the application is a Spring Boot Application
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    // Inject the dependency with the @Autowired annotation
    @Autowired
    StageWiseMessageService stageWiseMessageService;

    @Autowired
    CustomerService customerService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // Override run() method from CommandLineRunner interface
    @Override
    public void run(String... args) {
        try {
            // call this method to save message in database at the time of application is running
            stageWiseMessageService.saveMessage();
            // call this method to save customer in database at the time of application is running
            customerService.saveCustomer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
