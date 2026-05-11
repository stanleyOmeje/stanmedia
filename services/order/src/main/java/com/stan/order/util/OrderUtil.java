package com.stan.order.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
public class OrderUtil {

    public static String generateReference(String email) {
        log.info("Generating reference for customer email: " + email);
        String reference = UUID.randomUUID().toString();
        String date = LocalDate.now().toString();

        String orderReference = "ORD-" + email + "-" + date + "-" + reference;
        log.info("Order reference: " + orderReference);
        return orderReference;
    }

    public static boolean validateAmount(BigDecimal savedAmount, BigDecimal requestedAmount) {
        log.info("In validateAmount method with savedAmount: " + savedAmount + " requestedAmount: " + requestedAmount);
        if(savedAmount == null || requestedAmount == null) {
            return false;
        }
        if (savedAmount.compareTo(BigDecimal.ZERO) < 0 || requestedAmount.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        return savedAmount.compareTo(requestedAmount) == 0;
    }
}
