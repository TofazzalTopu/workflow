package com.info.demo.service.serviceImpl;

import com.info.demo.model.StageWiseMessage;
import com.info.demo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StageWiseMessageService {

    // Inject the dependency with the @Autowired annotation
    @Autowired
    MessageRepository messageRepository;

    public void saveMessage() {
        try {
            // Find the existing list of messages
            List<StageWiseMessage> messageList = messageRepository.findAll();

            // Declare an Array of integer for the stages
            int stage[] = {1, 2, 3};

            // Declare an Array with messages
            String messages[] = {
                    "Hi $, \n               This is my first message, Do you want to buy Mobile Phone? \n Sally",
                    "Hi $, \n               This is my second message, New model is on market, Do you want to buy? \n Sally",
                    "Hi $, \n               This is my third message, Do you want to buy a new phone? \n Sally",

                    "Hi $, \n               This is my first message, we have three model j6, j7, M21. Which one do you like? \n Sibly",
                    "Hi $, \n               This is my second message, Do you like M21 with 48 MP camera? \n Sibly",
                    "Hi $, \n               This is my third message, Do you like high configuration sel-fie camera phone? \n Sibly",

                    "Hi $, \n               This is my first message, How much is your budget? \n Peter",
                    "Hi $, \n               This is my second message, You can buy Samsung M21 with only 20k. Would you like to order today? \n Peter",
                    "Hi $, \n               This is my third message, M31 is 24k now,  Would you like to order today? \n Peter"
            };

            // Initialise stage number using the variable s
            int s = 0;
            int k = 0;
            // Message will be persists if the messageList is empty

            if (messageList.isEmpty()) {
                // Execute for loop to save messages
                for (int i = 0; i < messages.length; i++) {
                    StageWiseMessage message = new StageWiseMessage();
                    message.setMessage(messages[i]);

                    // Pass the stage number using s
                    message.setStage(stage[s]);
                    messageRepository.save(message);

                    /*
                    increase k's value by 1. When k's value is 3 then again assign k's value to 0 and
                    increase the value of s by 1
                    */
                    k += 1;
                    if (k == 3) {
                        k = 0;
                        s += 1;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
