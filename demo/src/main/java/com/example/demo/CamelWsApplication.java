package com.example.demo;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class CamelWsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CamelWsApplication.class, args);
    }

    @Bean
    RoutesBuilder myRouter() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                rest().get("/get")
                        .to("direct:get");

                from("direct:get")
                        .process(exchange -> log.info(exchange.getMessage().getBody().toString()))
                        .setBody(constant("ANSWER"))
                        .to("activemq:queue:ANSWER");
            }
        };
    }
}
