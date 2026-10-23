package com.stan.order.service.impl;

import com.stan.order.entity.Transactions;
import com.stan.order.enums.ResponseStatus;
import com.stan.order.exceptions.BadRequestException;
import com.stan.order.repository.TransactionRepository;
import com.stan.order.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    public Transactions createTransaction(Transactions transactions) {
        log.info("Inside TransactionServiceImpl::createTransaction with transactions: {}", transactions);
        if(transactions == null){
            throw new BadRequestException(ResponseStatus.FAILED.getMessage(),ResponseStatus.FAILED.getCode());
        }
        try {
            Transactions txs = transactionRepository.save(transactions);
            log.info("Transactions Created: {}", txs);
            return txs;
        }catch (Exception e){
            log.warn("Error creating transaction "+e.getMessage(), e);
        }
        return null;
    }

}
