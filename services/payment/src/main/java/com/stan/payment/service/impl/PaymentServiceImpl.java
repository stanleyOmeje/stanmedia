package com.stan.payment.service.impl;

import com.stan.payment.dto.request.PaymentRequest;
import com.stan.payment.kafka.PaymentNotificationProducer;
import com.stan.payment.kafka.PaymentNotificationRequest;
import com.stan.payment.mapper.PaymentMapper;
import com.stan.payment.repository.PaymentRepository;
import com.stan.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentNotificationProducer paymentNotificationProducer;

    @Override
    public Long makePayment(PaymentRequest paymentRequest) {
        log.info("Inside PaymentServiceImpl::makePayment with request: {}", paymentRequest);
        try {
            //Get Order
            //Check that order amount is equal to amount in paymentment request
            //Return PaymentMismatchException
            paymentRepository.save(paymentMapper.mapPaymentRequestToPayment(paymentRequest));
        }catch (Exception e){
            log.error(e.getMessage());
        }
        //sent Payment Notification
        PaymentNotificationRequest paymentNotificationRequest =
            new PaymentNotificationRequest(
               paymentRequest.orderReference(),
               paymentRequest.amount(),
               paymentRequest.paymentMethod(),
               paymentRequest.customer().firstName(),
               paymentRequest.customer().lastName(),
               paymentRequest.customer().email()
            );
        paymentNotificationProducer.sendPaymentNotification(paymentNotificationRequest);
        return paymentRequest.orderId();
    }
}
