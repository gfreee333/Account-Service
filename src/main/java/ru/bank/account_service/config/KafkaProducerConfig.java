package ru.bank.account_service.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${KAFKA_BOOTSTRAP_SERVERS}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, String> producerFactory(){
        Map<String, Object> configProducer = new HashMap<>();
        configProducer.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProducer.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProducer.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProducer.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        configProducer.put(ProducerConfig.ACKS_CONFIG, "all");
        configProducer.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        configProducer.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000);
        configProducer.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 60000);
        return new DefaultKafkaProducerFactory<>(configProducer);
    }

    @Bean(name = "criticalKafkaTemplate")
    public KafkaTemplate<String, String> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }

}
