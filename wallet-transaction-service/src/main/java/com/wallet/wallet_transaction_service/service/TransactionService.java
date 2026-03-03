package com.wallet.wallet_transaction_service.service;

import com.wallet.wallet_transaction_service.dto.TransactionEvent;
import com.wallet.wallet_transaction_service.exception.TransactionException;
import com.wallet.wallet_transaction_service.repository.TransactionRepository;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    private final RestTemplate restTemplate;
    private final TransactionRepository repo;
    private final TransactionProducer transactionProducer;

    public TransactionService(RestTemplate restTemplate,
                              TransactionRepository repo,
                              TransactionProducer transactionProducer) {
        this.restTemplate = restTemplate;
        this.repo = repo;
        this.transactionProducer = transactionProducer;
    }
    

    public Long transfer(Long fromUserId, Long toUserId, BigDecimal amount) {

        if (fromUserId.equals(toUserId)) {
            throw new TransactionException("Cannot transfer to same user");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionException("Invalid transfer amount");
        }

        Long transactionId =
                repo.save(fromUserId, toUserId, amount, "PENDING");

        try {

            System.out.println("Debiting amount from user: " + fromUserId);

            restTemplate.exchange(
                    "http://WALLET-ACCOUNT-SERVICE/accounts/debit/"
                            + fromUserId + "?amount=" + amount,
                    HttpMethod.POST,
                    null,
                    String.class
            );

            try {

                System.out.println("Crediting amount to user: " + toUserId);

                restTemplate.exchange(
                        "http://WALLET-ACCOUNT-SERVICE/accounts/credit/"
                                + toUserId + "?amount=" + amount,
                        HttpMethod.POST,
                        null,
                        String.class
                );

                repo.updateStatus(transactionId, "SUCCESS");

                TransactionEvent event =
                        new TransactionEvent(transactionId, amount, "SUCCESS");

                transactionProducer.sendTransactionEvent(event);

                System.out.println("Transaction SUCCESS");

            } catch (Exception creditException) {

                System.out.println("Credit failed. Rolling back debit...");

                restTemplate.exchange(
                        "http://WALLET-ACCOUNT-SERVICE/accounts/credit/"
                                + fromUserId + "?amount=" + amount,
                        HttpMethod.POST,
                        null,
                        String.class
                );

                repo.updateStatus(transactionId, "FAILED");

                throw new TransactionException(
                        "Transfer failed while crediting receiver"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            repo.updateStatus(transactionId, "FAILED");

            throw new TransactionException(
                    "Transaction failed : " + e.getMessage()
            );
        }

        return transactionId;
    }

    public String getTransactionStatus(Long transactionId) {
        return repo.getStatus(transactionId);
    }

    public List<Map<String, Object>> getTransactions(Long userId) {
        return repo.findByUserId(userId);
    }
    
    public List<Map<String, Object>> getAllTransactions() {
        return repo.findAll();
    }
}
