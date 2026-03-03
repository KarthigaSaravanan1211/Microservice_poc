package com.wallet.wallet_transaction_service.dto;

import java.math.BigDecimal;

public class TransactionEvent {

    private Long transactionId;
    private Double amount;
    private String status;

    public TransactionEvent(Long transactionId,
                            BigDecimal amount,
                            String status) {
        this.transactionId = transactionId;
        this.amount = amount.doubleValue();
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
