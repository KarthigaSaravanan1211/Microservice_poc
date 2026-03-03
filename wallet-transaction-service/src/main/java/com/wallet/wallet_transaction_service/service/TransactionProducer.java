package com.wallet.wallet_transaction_service.service;

import com.wallet.wallet_transaction_service.dto.TransactionEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionProducer {

    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    public TransactionProducer(
            KafkaTemplate<String, TransactionEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;

    }


    public void sendTransactionEvent(TransactionEvent event) {
        kafkaTemplate.send("wallet-topic", event);
        System.out.println("Kafka Event Sent : " + event.getTransactionId());
    }
}
