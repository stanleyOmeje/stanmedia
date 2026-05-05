package com.stan.payment.service.impl;

import com.stan.payment.dto.request.PaymentRequest;
import com.stan.payment.dto.response.CreatePaymentResponse;
import com.stan.payment.dto.response.DefaultResponse;
import com.stan.payment.entity.Payment;
import com.stan.payment.enums.ResponseStatus;
import com.stan.payment.exceptions.NotFoundException;
import com.stan.payment.exceptions.PaymentMisMatchException;
import com.stan.payment.kafka.PaymentNotificationProducer;
import com.stan.payment.kafka.PaymentNotificationRequest;
import com.stan.payment.mapper.PaymentMapper;
import com.stan.payment.repository.PaymentRepository;
import com.stan.payment.service.PaymentService;
import com.stan.payment.service.http.OrderHttpService;
import com.stan.payment.util.PaymentUtil;
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
    private final OrderHttpService orderHttpService;

    @Override
    public DefaultResponse<CreatePaymentResponse> makePayment(PaymentRequest paymentRequest) {
        log.info("Inside PaymentServiceImpl::makePayment with request: {}", paymentRequest);
        DefaultResponse<CreatePaymentResponse> response = new DefaultResponse<>();
        String paymentReference = null;
        try {
            //Get Order
            var order = orderHttpService.getOrderByReference(paymentRequest.orderReference());
            if (!ResponseStatus.SUCCESS.getCode().equals(order.getStatus())) {
                throw new NotFoundException(ResponseStatus.NOT_FOUND.getCode(),
                    "ORDER "+ResponseStatus.NOT_FOUND.getMessage());
            }
            //Check that order amount is equal to amount in paymentment request
            if (!PaymentUtil.isValidOrderAmount(order.getData().amount(), paymentRequest.amount())) {
                throw new PaymentMisMatchException(ResponseStatus.PAYMENT_MISMATCH.getCode(),
                    ResponseStatus.PAYMENT_MISMATCH.getMessage());
            }
            Payment payment = paymentRepository.save(paymentMapper.mapPaymentRequestToPayment(paymentRequest));
            paymentReference = payment.getPaymentReference();
        } catch (NotFoundException e) {
            response.setStatus(e.getCode());
            response.setMessage(e.getMessage());
            return response;
        } catch (PaymentMisMatchException e) {
            response.setStatus(e.getCode());
            response.setMessage(e.getMessage());
            return response;
        } catch (Exception e) {
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
        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage(ResponseStatus.SUCCESS.getMessage());
        response.setData(new CreatePaymentResponse(paymentReference));
        return response;
    }
}
