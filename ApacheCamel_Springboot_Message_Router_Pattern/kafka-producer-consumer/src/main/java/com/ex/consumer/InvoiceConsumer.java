package com.ex.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InvoiceConsumer {

    @Value("${app.kafka.invoice-topic}")
    private String topicName;

    @KafkaListener(topics = "${app.kafka.invoice-topic}", groupId = "${app.kafka.consumer-group-id}", containerFactory = "concurrentKafkaListenerWFConsumerFactory")
    public void receive(String request) {
        log.info("received data='{}={}' from topic: {}", request.hashCode(), request, topicName);
    }
}
