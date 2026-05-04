package com.stan.notification.kafka.consumer;

import com.stan.notification.domaim.Notification;
import com.stan.notification.enums.NotificationType;
import com.stan.notification.kafka.order.OrderConfirmation;
import com.stan.notification.kafka.payment.PaymentConfirmation;
import com.stan.notification.repository.NotificationRepository;
import com.stan.notification.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationConsumer {
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @KafkaListener(topics = "payment-topic",containerFactory = "paymentKafkaListenerContainerFactory")
    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation) throws MessagingException {
        log.info("Consuming message from payment topic");

        notificationRepository.save(
            Notification.builder()
                .notificationType(NotificationType.PAYMENT_CONFIRMATION)
                .notificationDate(LocalDateTime.now())
                .paymentConfirmation(paymentConfirmation)
                .build()
        );

        //semd email
        String customerName = paymentConfirmation.customerFirstName() + " " + paymentConfirmation.customerLastName();
        emailService.sendPaymentSuccessEmail(
            paymentConfirmation.customerEmail(),
            customerName,
            paymentConfirmation.amount(),
            paymentConfirmation.orderReference()
        );
    }

    @KafkaListener(topics = "order-topic",containerFactory = "orderKafkaListenerContainerFactory")
    public void consumeOrderConfirmationNotification(OrderConfirmation orderConfirmation) throws MessagingException {
        log.info("Consuming message from Order successful notification");

        notificationRepository.save(
            Notification.builder()
                .notificationType(NotificationType.PAYMENT_CONFIRMATION)
                .notificationDate(LocalDateTime.now())
                .orderConfirmation(orderConfirmation)
                .build()
        );

        //semd email
        String customerName = orderConfirmation.customer().firstName() + " " + orderConfirmation.customer().lastName();
        emailService.sendOrderConfirmationEmail(
            orderConfirmation.customer().email(),
            customerName,
            orderConfirmation.amount(),
            orderConfirmation.orderReference(),
            orderConfirmation.product()
        );
    }
}
