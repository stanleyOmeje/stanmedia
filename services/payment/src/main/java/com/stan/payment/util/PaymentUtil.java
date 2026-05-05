package com.stan.payment.util;

import com.stan.payment.dto.request.PaymentRequest;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
public class PaymentUtil {

    public static boolean isValidOrderAmount(BigDecimal orderAmount, BigDecimal paymentAmount) {
        if (paymentAmount == null || paymentAmount.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        return orderAmount.compareTo(paymentAmount) == 0;
    }

    public static String generateReference(String orderId) {
        log.info("Generating reference for orderId id: " + orderId);
        String reference = UUID.randomUUID().toString();
        String date = LocalDate.now().toString();

        String paymentReference = "PMT-" + orderId + "-" + date + "-" + reference;
        log.info("Order reference: " + paymentReference);
        return paymentReference;
    }
}
