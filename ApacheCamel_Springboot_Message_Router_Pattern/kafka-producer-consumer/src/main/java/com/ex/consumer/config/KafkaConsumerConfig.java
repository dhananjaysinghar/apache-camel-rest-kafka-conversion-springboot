package com.ex.consumer.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${app.kafka.host}")
    private String kafkaHost;

    @Value("${app.kafka.max-poll-record:1000}")
    private int maxPollRecord;

    @Value("${app.kafka.consumer-group-id}")
    private String kafkaConsumerGroupId;

    private static final Map<String, Object> DEFAULT_CONSUMER_CONFIG_MAP = new HashMap<>();

    @PostConstruct
    public void configureKafkaProperties() {
        DEFAULT_CONSUMER_CONFIG_MAP.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost);
        DEFAULT_CONSUMER_CONFIG_MAP.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        DEFAULT_CONSUMER_CONFIG_MAP.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        DEFAULT_CONSUMER_CONFIG_MAP.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecord);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> concurrentKafkaListenerWFConsumerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(wfConsumerFactory());
        return factory;
    }


    @Bean
    public ConsumerFactory<String, String> wfConsumerFactory() {
        DEFAULT_CONSUMER_CONFIG_MAP.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        Map<String, Object> map = new HashMap<>(DEFAULT_CONSUMER_CONFIG_MAP);
        map.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerGroupId);
        return new DefaultKafkaConsumerFactory<>(map, new StringDeserializer(), new StringDeserializer());
    }
}
