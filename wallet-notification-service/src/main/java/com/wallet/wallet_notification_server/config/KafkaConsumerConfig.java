package com.wallet.wallet_notification_server.config;

import com.wallet.wallet_notification_server.dto.TransactionEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id:wallet-group}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, TransactionEvent> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        config.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        
        // JSON Deserializer configuration
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, TransactionEvent.class.getName());
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        
        // Connection timeout configuration to prevent hanging
        config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);
        config.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 10000);
        config.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 40000);
        
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
                new JsonDeserializer<>(TransactionEvent.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionEvent>
    kafkaListenerContainerFactory(ConsumerFactory<String, TransactionEvent> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, TransactionEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        
        // Configure acknowledgment mode
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        
        // Set concurrency (number of consumer threads)
        factory.setConcurrency(1);
        
        // Auto startup
        factory.setAutoStartup(true);
        
        // Error handling - log and continue
        factory.setCommonErrorHandler(new org.springframework.kafka.listener.DefaultErrorHandler());

        return factory;
    }
}

