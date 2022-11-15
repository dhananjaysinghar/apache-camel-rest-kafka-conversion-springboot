package com.camel.integration.router;

import com.camel.integration.filter.ApplicationBookingServiceEventFilter;
import com.camel.integration.filter.ApplicationInvoiceServiceEventFilter;
import com.camel.integration.processor.ApplicationEventProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventBasedRouter extends RouteBuilder {

    @Autowired
    private ApplicationEventProcessor applicationProcessor;
    @Autowired
    private ApplicationBookingServiceEventFilter bookingServiceFilter;
    @Autowired
    private ApplicationInvoiceServiceEventFilter invoiceServiceFilter;

    @Override
    public void configure() {
        from("{{app.camel-from}}")
                .log("Input: ${body}")
                .process(applicationProcessor)
                .log("Output: ${body}")
                .choice()
                .when(bookingServiceFilter)
                .to("{{app.camel-to-booking-service}}")
                .when(invoiceServiceFilter)
                .to("{{app.camel-to-invoice-service}}")
                .endChoice();
    }
}
