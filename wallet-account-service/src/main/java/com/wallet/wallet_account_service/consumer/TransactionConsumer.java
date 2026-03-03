package com.wallet.wallet_account_service.consumer;

import com.wallet.wallet_account_service.dto.TransactionEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionConsumer {

    @KafkaListener(
            topics = "wallet-topic",
            groupId = "account-group"
    )
    public void consume(TransactionEvent event) {

        System.out.println("🔥 Transaction Event Received");

        System.out.println("TransactionId : "
                + event.getTransactionId());

        System.out.println("Amount : "
                + event.getAmount());

        System.out.println("Status : "
                + event.getStatus());
    }
}
