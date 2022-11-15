package com.ex.consumer;

import com.ex.model.BookingRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BookingConsumer {

    @Value("${app.kafka.booking-topic}")
    private String topicName;


    @KafkaListener(topics = "${app.kafka.booking-topic}", groupId = "${app.kafka.consumer-group-id}", containerFactory = "concurrentKafkaListenerWFConsumerFactory")
    public void receive(String request) {
        log.info("received data='{}={}' from topic: {}", request.hashCode(), request, topicName);
    }
}
