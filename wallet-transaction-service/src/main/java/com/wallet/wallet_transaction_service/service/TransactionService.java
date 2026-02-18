package com.wallet.wallet_transaction_service.service;

import com.wallet.wallet_transaction_service.exception.TransactionException;
import com.wallet.wallet_transaction_service.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    private final RestTemplate restTemplate;
    private final TransactionRepository repo;

    public TransactionService(RestTemplate restTemplate,
                              TransactionRepository repo) {
        this.restTemplate = restTemplate;
        this.repo = repo;
    }

    public Long transfer(Long fromUserId, Long toUserId, BigDecimal amount) {

        Long transactionId =
                repo.save(fromUserId, toUserId, amount, "PENDING");
        if (fromUserId.equals(toUserId)) {
            throw new TransactionException("Cannot transfer to same user");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionException("Invalid transfer amount");
        }

        try {
//debit
            restTemplate.postForObject(
                    "http://localhost:8083/accounts/debit/" +
                            fromUserId + "?amount=" + amount,
                    null,
                    Void.class
            );

            try {
                //credit
                restTemplate.postForObject(
                        "http://localhost:8083/accounts/credit/" +
                                toUserId + "?amount=" + amount,
                        null,
                        Void.class
                );

                repo.updateStatus(transactionId, "SUCCESS");

            } catch (Exception creditException) {

                //  COMPENSATION (Refund sender)
                try {
                    restTemplate.postForObject(
                            "http://localhost:8083/accounts/credit/" +
                                    fromUserId + "?amount=" + amount,
                            null,
                            Void.class
                    );
                } catch (Exception refundException) {
                    throw new TransactionException(
                            "CRITICAL: Refund failed. Manual intervention required."
                    );
                }

                throw new TransactionException("Transfer failed while crediting receiver");
            }

        } catch (TransactionException e) {
            repo.updateStatus(transactionId, "FAILED");
            throw e;
        } catch (Exception e) {
            repo.updateStatus(transactionId, "FAILED");
            throw new TransactionException("Transaction failed");
        }

        return transactionId;
    }

    public String getTransactionStatus(Long transactionId) {
        return repo.getStatus(transactionId);
    }


    public List<Map<String, Object>> getTransactions(Long userId) {
        return repo.findByUserId(userId);
    }

}
