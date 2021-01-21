package com.example.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MessageRoute extends RouteBuilder {

    Logger logger = LoggerFactory.getLogger(MessageRoute.class);

    @Autowired
    MessageRepository messageRepository;

    @Override
    public void configure() throws Exception {
        from("timer:from-db?period=15s")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        if (exchange.getMessage().getBody() == null) return;
                        Message m = messageRepository.findFirstByStatusEquals("NEW_DATA");
                        logger.info("1. Message id: {}", m.getId());
                        m.setStatus("PROCESSED");
                        messageRepository.save(m);
                    }
                })
                .log("1. end of timer processing.")
                .to("direct:process");
    }
}
