package com.stan.notification.enums;

import lombok.Getter;

@Getter
public enum EmailTemplate {
    PAYMENT_CONFIRMATION("payment-confirmation.html","PAYMENT SUCCESSFULLY PROCESSED"),
    ORDER_CONFIRMATION("order-confirmation.html","ORDER SUCCESS");

    private final String templateName;
    private final String subject ;

      EmailTemplate(String templateName, String subject){
        this.templateName = templateName;
          this.subject = subject;
    }

}
