package com.camel.integration.router;

import com.camel.integration.filter.ApplicationBookingServiceRestFilter;
import com.camel.integration.filter.ApplicationInvoiceServiceRestFilter;
import com.camel.integration.processor.ApplicationRestProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import static org.apache.camel.model.rest.RestParamType.body;

@Component
public class RestApiBasedRouter extends RouteBuilder {

    @Autowired
    private ApplicationRestProcessor applicationProcessor;
    @Autowired
    private ApplicationBookingServiceRestFilter bookingServiceFilter;
    @Autowired
    private ApplicationInvoiceServiceRestFilter invoiceServiceFilter;

    @Autowired
    private Environment env;

    @Value("${camel.servlet.mapping.context-path}")
    private String contextPath;

    @Override
    public void configure() {
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true")
                .enableCORS(true)
                .port(env.getProperty("server.port", "8080"))
               // .contextPath(contextPath.substring(0, contextPath.length() - 2))
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "User API")
                .apiProperty("api.version", "1.0.0");

        rest("/camel").description("User REST service")
                .post().type(Object.class)
                .param().name("body").type(body).description("requestBody").endParam()
                .responseMessage().code(200).endResponseMessage()
                .to("direct:camel");

        from("direct:camel")
                .log("Input: ${body}")
                .process(applicationProcessor)
                .log("Output: ${body}")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .choice()
                .when(bookingServiceFilter)
                .to("{{app.camel-to-booking-service}}")
                .when(invoiceServiceFilter)
                .to("{{app.camel-to-invoice-service}}")
                .end();


    }
}

