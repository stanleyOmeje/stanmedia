package com.stan.order.service;

import com.stan.order.entity.Transactions;

public interface TransactionService {
    Transactions createTransaction(Transactions transactions);
}
