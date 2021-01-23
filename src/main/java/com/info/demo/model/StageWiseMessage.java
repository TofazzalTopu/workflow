package com.info.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
@Data annotation is the feature from Lombok and used for getter and setter
@Entity annotation define the class as an entity class
@Table annotation along with name argument is used to define the table name
*/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stage_wise_message")
// Create StageWiseMessage class to store Messages for the specific stage
public class StageWiseMessage {

    /*
        @Id annotation will behave as the primary key attribute in the table
        @Generatedvalue(strategy = generationtype.auto) in order to auto increment the primary key attribute
    */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "stage", nullable = false)
    private int stage;


}
