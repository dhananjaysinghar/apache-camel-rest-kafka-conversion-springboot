package com.ex.producer;

import com.ex.model.BookingRequest;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@Slf4j
public class ApacheCamelKafkaSender {

    @Autowired
    @Qualifier("camelKafkaTemplate")
    private KafkaTemplate<String, BookingRequest> bookingTemplate;

    @Value("${app.kafka.camel-topic}")
    private String topicName;

    private Integer count = 0;

    public String send(BookingRequest bookingRequest) {
        count++;
        log.info("sending data='{}={}' to topic='{}'", bookingRequest.hashCode(), bookingRequest, topicName);
        ProducerRecord<String, BookingRequest> producerRecord = new ProducerRecord<>(topicName, UUID.randomUUID().toString(), bookingRequest);
        producerRecord.headers().add("X-ORDER-ID", UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
        if (count % 2 != 0) {
            producerRecord.headers().add("X-TASK-NAME", "BOOKING".getBytes(StandardCharsets.UTF_8));
        } else {
            producerRecord.headers().add("X-TASK-NAME", "INVOICE".getBytes(StandardCharsets.UTF_8));
        }
        ListenableFuture<SendResult<String, BookingRequest>> response = bookingTemplate.send(producerRecord);
        logKafkaResponse(producerRecord, response);
        return "data sent successfully";
    }

    @SneakyThrows
    private <T> void logKafkaResponse(ProducerRecord<String, T> producerRecord, ListenableFuture<SendResult<String, T>> future) {
        future.addCallback(new ListenableFutureCallback<SendResult<String, T>>() {
            @Override
            public void onSuccess(SendResult<String, T> result) {

            }

            @Override
            public void onFailure(@NonNull Throwable ex) {
                log.error("Unable to send Payload to kafka topic:[{}] due to : {}", producerRecord.topic(), ex);
            }
        });
    }
}
