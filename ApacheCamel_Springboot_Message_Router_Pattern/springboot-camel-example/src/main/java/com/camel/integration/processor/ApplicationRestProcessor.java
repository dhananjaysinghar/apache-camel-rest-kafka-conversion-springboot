package com.camel.integration.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.kafka.KafkaConstants;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class ApplicationRestProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        String body = exchange.getIn().getBody(String.class);
        Message output = exchange.getMessage();
        //exchange.getMessage().setHeader(KafkaConstants.KEY, UUID.randomUUID().toString().getBytes());
        output.setBody(body);
    }
}
