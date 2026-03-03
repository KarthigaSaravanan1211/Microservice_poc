package com.wallet.wallet_account_service.dto;

public class TransactionEvent {

    private Long transactionId;
    private Double amount;
    private String status;

    // ✅ Empty constructor REQUIRED for Kafka
    public TransactionEvent() {
    }

    public TransactionEvent(Long transactionId,
                            Double amount,
                            String status) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.status = status;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
