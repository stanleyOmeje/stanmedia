package com.stan.order.util;

import lombok.extern.slf4j.Slf4j;

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
}
