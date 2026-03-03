package com.wallet.wallet_account_service.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.wallet.wallet_account_service.dto.TransactionEvent;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, TransactionEvent> consumerFactory() {

        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092");

        config.put(ConsumerConfig.GROUP_ID_CONFIG,
                "account-group");

        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);

        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                ErrorHandlingDeserializer.class);

        config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS,
                JsonDeserializer.class);

        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
                TransactionEvent.class.getName());

        config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS,
                false);

        config.put(JsonDeserializer.TRUSTED_PACKAGES,
                "*");

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionEvent>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, TransactionEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());

        return factory;
    }
}
