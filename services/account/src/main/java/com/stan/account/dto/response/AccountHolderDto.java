package com.stan.account.dto.response;

import com.stan.account.dto.request.ProfileDTO;
import com.stan.account.enums.AccountHolderStatus;
import com.stan.account.enums.AccountType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class AccountHolderDto {
    private Long id;
    private String profileId;
    private AccountType accountType;
    private String accountName;
    private String accountNumber;
    private String bankCode;
    private AccountHolderStatus status;
    private String currency;
    private BigDecimal availableBalance;
    private BigDecimal holdBalance;
    private BigDecimal minimumBalance;
    private Boolean isRestrictedAccount;
    private Instant openingDate;
    private ProfileDTO user;
    private String bankName;
}
