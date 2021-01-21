package com.example.camel;

import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageService {


    Logger logger = LoggerFactory.getLogger(MessageService.class);
    @Autowired
    MessageRepository messageRepository;

    @Handler
    public String processMessage(Message message) {
        logger.info("Received message with id{}:", message.getId());
        return "message processed" + message.getId();
    }

    public String noHandler(Message m) {
        logger.info("message processed by method without HANDLER annotation {}.", m.getId());
        return "message processed by method without HANDLER annotation." + m.getId();
    }

}
