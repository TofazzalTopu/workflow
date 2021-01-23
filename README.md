# Project Title - Workflow Management Service

## Technology
* Programming language: Java- 1.8
* Framework: Spring Boot - 2.1.0
* Hibernate-5
* JPA-2
* Automated build: Apache Maven
* UI: Bootstrap- 3.4.1
* Spring Security
* Database: MySql-8

## Pre-requisite
* Configure workflow time interval in application.properties file.
* DB Name: workflow
* username=root
* password=root

## Workflow Management Service
* Project home url: http://localhost:8080/workflow/workflow
* Select a customer and click on 'Start Workflow For Customer One/Two/Three' button and workflow will started immediately for the selected customer.
* Different workflow for the different customer will be started when you click on 'Start Workflow..' button.
* For response select a customer from the dropdown.
* Write your response in 'Customer Response To The Sales Man:' textarea and your submit response.
* You can see the 'Sales Man's Message History' and 'Customer Response History' by clicking 'View Messages' button for the selected customer.
* You can also see 'Sales Man's Message History' and 'Customer Response History' by changing the customer from the customer dropdown.
* If you submit a response for the current stage then workflow will start for the next stage.
* You will not get anymore message from sales man for the current stage.
* If you do not submit any response then sales man will send all the message's for the current stage.
* If you do not submit any response and sales man send all the message's for the current stage, after that workflow will start for the next stage.
* Workflow will continue until all the stage's completed.
