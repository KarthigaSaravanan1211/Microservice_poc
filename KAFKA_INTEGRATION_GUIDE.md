# Wallet Microservices Platform - Kafka Integration Guide

## Overview
This guide explains the Kafka integration in the Wallet Microservices Platform.

## Architecture

### Services Using Kafka

1. **Transaction Service** (Producer)
   - Publishes transaction events to Kafka topics
   - Port: 8084
   - Topic: `transaction-events`

2. **Notification Service** (Consumer)
   - Consumes transaction events from Kafka
   - Port: 8086
   - Consumer Group: `wallet-group`

## Kafka Configuration

### Kafka Broker
- **Bootstrap Servers**: `localhost:9092` (external) / `kafka:29092` (internal)
- **Zookeeper**: `localhost:2181`
- **Auto Create Topics**: Enabled
- **Replication Factor**: 1 (single broker setup)

### Transaction Service (Producer) Configuration

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
```

### Notification Service (Consumer) Configuration

```yaml
spring:
  kafka:
    consumer:
      bootstrap-servers: localhost:9092
      group-id: wallet-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.value.default.type: com.wallet.wallet_notification_server.dto.TransactionEvent
        spring.json.trusted.packages: "*"
```

## Event Flow

1. **Transaction Creation**
   - User initiates a transaction via Transaction Service API
   - Transaction is saved to the database
   - Transaction event is published to Kafka topic

2. **Event Publishing**
   - Transaction Service serializes the event as JSON
   - Event is sent to `transaction-events` topic
   - Kafka confirms the message receipt

3. **Event Consumption**
   - Notification Service listens to `transaction-events` topic
   - Events are consumed in order
   - Notifications are processed and sent

## Testing Kafka Integration

### 1. Start All Services
```bash
./start-all.sh
```

### 2. Create a Transaction
```bash
curl -X POST http://localhost:8084/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "fromAccountId": 1,
    "toAccountId": 2,
    "amount": 100.00,
    "type": "TRANSFER"
  }'
```

### 3. Check Notification Service Logs
```bash
docker-compose logs -f notification-service
```

### 4. Monitor Kafka Topics (Optional)

Connect to Kafka container:
```bash
docker exec -it kafka bash
```

List topics:
```bash
kafka-topics --bootstrap-server localhost:9092 --list
```

Consume messages from topic:
```bash
kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic transaction-events \
  --from-beginning
```

## Troubleshooting

### Kafka Connection Issues

1. **Check Kafka Health**
   ```bash
   docker-compose ps kafka
   docker-compose logs kafka
   ```

2. **Verify Zookeeper is Running**
   ```bash
   docker-compose ps zookeeper
   ```

3. **Network Connectivity**
   - Ensure all services are on the same Docker network (`wallet-network`)
   - Check service discovery via Eureka

### Message Not Being Consumed

1. **Check Consumer Group Status**
   ```bash
   docker exec -it kafka bash
   kafka-consumer-groups --bootstrap-server localhost:9092 \
     --group wallet-group --describe
   ```

2. **Verify Topic Exists**
   ```bash
   kafka-topics --bootstrap-server localhost:9092 --list
   ```

3. **Check Consumer Logs**
   ```bash
   docker-compose logs -f notification-service
   ```

### Serialization Errors

- Ensure the DTO classes match between Producer and Consumer
- Verify `spring.json.trusted.packages` is set correctly
- Check that JSON serializer/deserializer versions are compatible

## Event Schema

### TransactionEvent
```json
{
  "transactionId": "123e4567-e89b-12d3-a456-426614174000",
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 100.00,
  "type": "TRANSFER",
  "status": "SUCCESS",
  "timestamp": "2026-02-26T10:30:00Z",
  "description": "Payment transfer"
}
```

## Best Practices

1. **Error Handling**
   - Implement retry logic for transient failures
   - Use Dead Letter Topics (DLT) for failed messages
   - Log all errors with context

2. **Performance**
   - Configure appropriate batch sizes
   - Use async processing where possible
   - Monitor consumer lag

3. **Monitoring**
   - Track message throughput
   - Monitor consumer group lag
   - Set up alerts for failed deliveries

4. **Security**
   - Use SASL/SSL for production
   - Implement proper access controls
   - Encrypt sensitive data in messages

## Production Considerations

For production deployment:

1. **Multiple Brokers**
   - Set replication factor > 1
   - Configure min.insync.replicas
   - Use partition strategy for scaling

2. **Persistence**
   - Configure appropriate retention policies
   - Set up log compaction if needed
   - Regular backups of topic data

3. **Monitoring & Alerting**
   - Integrate with Prometheus/Grafana
   - Set up consumer lag alerts
   - Monitor broker health metrics

4. **High Availability**
   - Deploy Kafka cluster with multiple brokers
   - Use rack awareness for broker placement
   - Configure proper disaster recovery
