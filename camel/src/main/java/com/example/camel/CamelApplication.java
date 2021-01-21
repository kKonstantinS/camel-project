package com.example.camel;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

/**
 * direct - single CamelContext, synchronous (blocks producer)
 * SEDA - single CamelContext, asynchronous (does not block producer)
 * VM - multiple CamelContext, asynchronous (does not block producer)
 * direct-VM - multiple CamelContext, synchronous (blocks producer)
 */

@SpringBootApplication
@EnableJms
public class CamelApplication {

    Logger logger = LoggerFactory.getLogger(CamelApplication.class);

    /**
     * from("activemq:queue:order.in")
     *   .to("direct:processOrder");
     *
     * from("jms:sender:queue")
     *   .to("direct:processOrder");
     *
     * from("file:some/file/path")
     *   .to("direct:processOrder");
     *
     * from("jetty:http://0.0.0.0/order/in")
     *   .to("direct:processOrder");
     *
     * from("direct:processOrder")
     *   .to("bean:orderService?method=process")
     *   .to("activemq:queue:order.out");
     */

    @Bean
    RoutesBuilder myRouter() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                rest().get("/hello").to("direct:hello")
                        .get("/no-handler").to("direct:no-handler")
                        .get("/handler").to("direct:handler")
                        .get("/all").to("direct:hello");

                from("direct:hello")
                        .process(exchange -> {
                            logger.info("Message processed by hello processor");
                        })
                        .log(LoggingLevel.INFO, "Hello World");

                from("direct:handler")
                        .to("bean:messageService");

                from("direct:no-handler")
                        .to("bean:messageService?method=noHandler");

                from("direct:process")
                        .log("MESSAGE PROCESSED");

                from("timer:amq?period=25s")
                        .log("Message sent to ActiveMQ")
                        .setBody(constant("MESSAGE"))
                        .to("activemq:queue:ROOT");


                from("activemq:queue:ROOT")
                        .log("Message received from ActiveMQ");

                /*from(source)
                        .choice()
                        .when(condition).to(endpoint)
                        .otherwise()
                        .to(anotherEndpoint)
                        .end();*/
            }
        };
    }

    @Bean
    Processor customProcessor() {
        return exchange -> {
            logger.info("Custom processor invocation./");
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(CamelApplication.class, args);
    }

}
