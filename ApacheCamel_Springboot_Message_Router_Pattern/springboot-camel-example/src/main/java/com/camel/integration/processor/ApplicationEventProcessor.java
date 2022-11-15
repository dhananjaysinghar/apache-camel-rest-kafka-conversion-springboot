package com.camel.integration.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationEventProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        String body = exchange.getIn().getBody(String.class);
        Message output = exchange.getMessage();
        RecordHeaders headers = (RecordHeaders) exchange.getMessage().getHeaders().get("kafka.HEADERS");
        Header task_name = headers.headers("X-TASK-NAME").iterator().next();
        Header order_id = headers.headers("X-ORDER-ID").iterator().next();
        String headerValue = new String(task_name.value());
        String order_id_value = new String(order_id.value());
        log.info("Received task_header : {}", headerValue);
        if (headerValue.equalsIgnoreCase("CAPTURED")) {
            output.setBody("{\"status\":\"CAPTURED\",\"orderId\":\"" + order_id_value + "\"}");
        } else if (headerValue.equalsIgnoreCase("VALIDATED")) {
            output.setBody("{\"status\":\"VALIDATED\",\"orderId\":\"" + order_id_value + "\"}");
        } else {
            output.setBody(body);
        }
    }
}
