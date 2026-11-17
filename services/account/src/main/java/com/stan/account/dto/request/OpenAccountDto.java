package com.stan.account.dto.request;

import com.stan.account.enums.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class OpenAccountDto {

    @NotNull
    private String profileId;

    @NotNull
    private AccountType accountType;

    private Boolean isRestrictedAccount = false;
    private CreateFieldDto extraFields;
    private Boolean cbaAccount = false;
    private String merchantId;
    private Map<String, String> tenant;
    private String fineractTenant;
    private boolean isNuban = false;
}
