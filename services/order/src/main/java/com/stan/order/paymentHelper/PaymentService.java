package com.stan.order.paymentHelper;

import com.stan.order.dto.response.DefaultResponse;
import com.stan.order.enums.ResponseStatus;
import com.stan.order.paymentHelper.dto.CreatePaymentResponse;
import com.stan.order.paymentHelper.dto.PaymentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentClient paymentClient;
   public DefaultResponse<CreatePaymentResponse> makePayment(PaymentRequest paymentRequest, String bearerToken) {
       log.info("Inside makePayment");
       DefaultResponse<CreatePaymentResponse> response = new DefaultResponse<>();
       try {
           var paymentResponse =
               paymentClient.makePayment(paymentRequest, bearerToken);
           log.info("Make payment response: {}", paymentResponse);
           if (paymentResponse == null || paymentResponse.getBody() == null) {
               log.error("Make payment response is null");
               response.setStatus(ResponseStatus.FAILED.getCode());
               response.setMessage("Make payment failed");
               return response;
           }
           String status = paymentResponse.getBody().getStatus();
           if(ResponseStatus.SUCCESS.getCode().equals(status)) {
               response.setStatus(ResponseStatus.SUCCESS.getCode());
               response.setMessage("Make payment success");
               response.setData(paymentResponse.getBody().getData());
               return response;
           }else {
               response.setStatus(ResponseStatus.FAILED.getCode());
               response.setMessage(paymentResponse.getBody().getMessage());
               return response;
           }
       }catch (Exception e) {
           log.error(e.getMessage());
           //Query payment
           response.setStatus(ResponseStatus.FAILED.getCode());
           response.setMessage(ResponseStatus.FAILED.getMessage());
           return response;
       }
   }
}
