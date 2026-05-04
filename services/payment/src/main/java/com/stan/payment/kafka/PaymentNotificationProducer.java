package com.stan.payment.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentNotificationProducer {

    private final KafkaTemplate<String, PaymentNotificationRequest> kafkaTemplate;

    public void sendPaymentNotification(PaymentNotificationRequest request) {
        log.info("sending PayConfirmation ....{}", request);
        Message<PaymentNotificationRequest> message = MessageBuilder
            .withPayload(request)
            .setHeader(KafkaHeaders.TOPIC, "payment-topic")
            .build();
        kafkaTemplate.send(message);
    }
}
