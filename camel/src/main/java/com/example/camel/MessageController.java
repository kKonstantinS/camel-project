package com.example.camel;


import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {

    @Autowired
    CamelContext context;
    @Autowired
    MessageRepository messageRepository;

    /**
     * request* - sync
     * send* - async
     */
    @RequestMapping("/explicit-call")
    public String ans() {
        //context.createProducerTemplate().sendBody("direct:handler", messageRepository.findAll().stream().findFirst());

        Object result = context.createProducerTemplate().requestBody("direct:no-handler", messageRepository.findAll().stream().findFirst().get());
        return result.toString();
    }

    @RequestMapping("/all")
    public List<Message> findAll() {
        return messageRepository.findAll();
    }


    @RequestMapping("/add/{id}")
    public void addMsg(@PathVariable Long id) {
        for (int i = 0; i < id; i++) {
            Message m = new Message();
            m.setBody("BODY");
            m.setStatus("NEW_DATA");

            messageRepository.save(m);
        }
    }
}
