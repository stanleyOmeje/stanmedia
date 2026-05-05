package com.stan.payment.mapper;

import com.stan.payment.dto.request.PaymentRequest;
import com.stan.payment.entity.Payment;
import com.stan.payment.enums.PaymentMethod;
import com.stan.payment.util.PaymentUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class PaymentMapper {
    public Payment mapPaymentRequestToPayment(PaymentRequest request){
        Payment payment = new Payment();
        payment.setAmount(request.amount());
        payment.setPaymentMethod(request.paymentMethod());
        payment.setPaymentReference(PaymentUtil.generateReference(String.valueOf(request.orderId())));
        payment.setOrderId(request.orderId());
        payment.setCreatedDate(LocalDateTime.now());

        return payment;
    }
}
