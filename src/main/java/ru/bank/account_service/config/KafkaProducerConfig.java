package ru.bank.account_service.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, String> criticalProducerFactory(){
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
    public KafkaTemplate<String, String> criticalKafkaTemplate(){
        return new KafkaTemplate<>(criticalProducerFactory());
    }

    @Bean
    public ProducerFactory<String, Object> defualtProducerFactory(){
        Map<String, Object> configProducer = new HashMap<>();
        configProducer.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProducer.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProducer.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProducer.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "false");
        configProducer.put(ProducerConfig.ACKS_CONFIG, "1");
        configProducer.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProducer.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 30000);
        configProducer.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 15000);
        return new DefaultKafkaProducerFactory<>(configProducer);
    }

    @Bean(name = "defualtKafkaTemplate")
    public KafkaTemplate<String, Object> defualtKafkaTemplate(){
        return new KafkaTemplate<>(defualtProducerFactory());
    }

}
