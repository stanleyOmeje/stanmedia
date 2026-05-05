package com.stan.order.customerHelper;

import com.stan.order.dto.response.DefaultResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
    name = "customer-service",
    url = "${app.config.customer-url}"
)
public interface CustomerClient {

    @GetMapping("/email/{email}")
    Optional<DefaultResponse<CustomerResponse>> getCustomerByEmail(@PathVariable("email") String email);
}
