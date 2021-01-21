package com.example.demo;


import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    CamelContext context;

    @RequestMapping("/request")
    public String msg() {
        context.createProducerTemplate().sendBody("jms:queue:ROOT", "BODY");
        //return context.createProducerTemplate().requestBody("jms:queue:ROOT", "BODY").toString();
        return "";
    }

    /*@JmsListener(destination = "activemq:queue:ROOT")
    public void listener() {

    }*/
}
