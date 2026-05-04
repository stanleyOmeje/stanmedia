package com.stan.notification.service;

import com.stan.notification.enums.EmailTemplate;
import com.stan.notification.kafka.order.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendPaymentSuccessEmail(
        String destinationEmail,
        String customerName,
        BigDecimal amount,
        String orderReference
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        //mimeMessageHelper.
        mimeMessageHelper.setFrom("omejechidozie@gmail.com");
        mimeMessageHelper.setTo(destinationEmail);
        final String templateName = EmailTemplate.PAYMENT_CONFIRMATION.getTemplateName();
        String subject = EmailTemplate.PAYMENT_CONFIRMATION.getSubject();

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("amount", amount);
        variables.put("orderReference", orderReference);
        // variables.put("destinationEmail", destinationEmail);

        Context context = new Context();
        context.setVariables(variables);

        try {
            String templateMessageHtml = templateEngine.process(templateName, context);
            mimeMessageHelper.setText(templateMessageHtml, true);
            mimeMessageHelper.setSubject(subject);

            mailSender.send(mimeMessage);
            log.info("Email sent successfully sent to " + destinationEmail);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }

    }


    @Async
    public void sendOrderConfirmationEmail(
        String destinationEmail,
        String customerName,
        BigDecimal amount,
        String orderReference,
        List<Product> products
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        //mimeMessageHelper.
        mimeMessageHelper.setFrom("omejechidozie@gmail.com");
        mimeMessageHelper.setTo(destinationEmail);
        final String templateName = EmailTemplate.ORDER_CONFIRMATION.getTemplateName();
        String subject = EmailTemplate.ORDER_CONFIRMATION.getSubject();

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("amount", amount);
        variables.put("orderReference", orderReference);
        variables.put("products", products);
        // variables.put("destinationEmail", destinationEmail);

        Context context = new Context();
        context.setVariables(variables);

        try {
            String templateMessageHtml = templateEngine.process(templateName, context);
            mimeMessageHelper.setText(templateMessageHtml, true);
            mimeMessageHelper.setSubject(subject);

            mailSender.send(mimeMessage);
            log.info("Email sent successfully sent to " + destinationEmail);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }

    }
}
