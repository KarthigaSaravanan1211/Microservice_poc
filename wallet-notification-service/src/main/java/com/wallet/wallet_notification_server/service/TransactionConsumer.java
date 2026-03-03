package com.wallet.wallet_notification_server.service;

import com.wallet.wallet_notification_server.dto.TransactionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class TransactionConsumer {

    private static final Logger logger = LoggerFactory.getLogger(TransactionConsumer.class);

    @KafkaListener(
            topics = "wallet-topic",
            groupId = "wallet-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(
            @Payload TransactionEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        try {
            logger.info("==========================================");
            logger.info("� Notification Service - New Transaction Event Received");
            logger.info("Partition: {} | Offset: {}", partition, offset);
            logger.info("Transaction ID: {}", event.getTransactionId());
            logger.info("Amount: {}", event.getAmount());
            logger.info("Status: {}", event.getStatus());
            logger.info("==========================================");

            // TODO: Add actual notification logic here
            // - Send email notification
            // - Send SMS notification
            // - Send push notification
            // - Update notification database

            logger.info("✅ Notification processed successfully for transaction: {}", 
                       event.getTransactionId());

        } catch (Exception e) {
            logger.error("❌ Error processing notification for transaction: {}", 
                        event.getTransactionId(), e);
            // In production, you might want to send this to a dead letter queue
        }
    }
}
