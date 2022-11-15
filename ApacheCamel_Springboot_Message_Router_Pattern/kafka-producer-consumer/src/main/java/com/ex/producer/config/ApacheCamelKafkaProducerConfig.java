package com.ex.producer.config;

import com.ex.model.BookingRequest;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ApacheCamelKafkaProducerConfig {

    @Value("${app.kafka.host}")
    private String kafkaHost;

    private static final Map<String, Object> DEFAULT_PRODUCER_CONFIG_MAP = new HashMap<>();

    @PostConstruct
    public void configureKafkaProperties() {
        DEFAULT_PRODUCER_CONFIG_MAP.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost);
        DEFAULT_PRODUCER_CONFIG_MAP.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        DEFAULT_PRODUCER_CONFIG_MAP.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    }

    @Bean
    public KafkaTemplate<String, BookingRequest> camelKafkaTemplate() {
        return new KafkaTemplate<>(camelProducerFactory());
    }

    @Bean
    public ProducerFactory<String, BookingRequest> camelProducerFactory() {
        JsonSerializer<BookingRequest> userJsonSerializer = new JsonSerializer<>();
        userJsonSerializer.setAddTypeInfo(false);
        return new DefaultKafkaProducerFactory<>(DEFAULT_PRODUCER_CONFIG_MAP, new StringSerializer(), userJsonSerializer);
    }
}
