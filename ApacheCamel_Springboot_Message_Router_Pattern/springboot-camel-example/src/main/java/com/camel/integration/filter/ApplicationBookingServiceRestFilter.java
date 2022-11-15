package com.camel.integration.filter;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.springframework.stereotype.Component;

@Component
public class ApplicationBookingServiceRestFilter implements Predicate {

    @Override
    public boolean matches(Exchange exchange) {
        Object headerValue = exchange.getMessage().getHeaders().get("X-TASK-NAME");
        return headerValue.equals("BOOKING");
    }
}
