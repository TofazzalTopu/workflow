package com.info.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/*
@Data annotation is used for getter and setter
*/

/*
@Data annotation is the feature from Lombok and used for getter and setter
@Entity annotation define the class as an entity class
@Table annotation along with name argument is used to define the table name
*/


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_current_stage")
// Create CustomersCurrentStage class to store Customer's Current Stage in the workflow
public class CustomersCurrentStage {
     /*
        @Id annotation will behave as the primary key attribute in the table
        @Generatedvalue(strategy = generationtype.auto) in order to auto increment the primary key attribute
      */

     @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = Customer.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "stage", nullable = false)
    private int stage;
}
