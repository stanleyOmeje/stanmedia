package com.stan.account.entity;


import com.stan.account.enums.AccountHolderStatus;
import com.stan.account.enums.AccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * This extends the user details
 */
@Entity
@Table(name = "ac_holder")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AccountHolder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "profile_id", nullable = false)
    private String profileId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @NotNull
    @Column(name = "account_name", nullable = false)
    private String accountName;

    @NotNull
    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @NotNull
    @Column(name = "bank_code", nullable = false)
    private String bankCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountHolderStatus status;

    @NotNull
    @Column(name = "currency", nullable = false)
    private String currency;

    @NotNull
    @Column(name = "available_balance", precision = 21, scale = 2, nullable = false)
    private BigDecimal availableBalance;

    @Column(name = "hold_balance", precision = 21, scale = 2)
    private BigDecimal holdBalance;

    @NotNull
    @Column(name = "minimum_balance", precision = 21, scale = 2, nullable = false)
    private BigDecimal minimumBalance;

    @NotNull
    @Column(name = "is_restricted_account", nullable = false)
    private Boolean isRestrictedAccount;

    @NotNull
    @Column(name = "opening_date", nullable = false)
    private Instant openingDate;

    @ManyToOne
    private Profile user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccountHolder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfileId() {
        return this.profileId;
    }

    public AccountHolder profileId(String profileId) {
        this.setProfileId(profileId);
        return this;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public AccountType getAccountType() {
        return this.accountType;
    }

    public AccountHolder accountType(AccountType accountType) {
        this.setAccountType(accountType);
        return this;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public AccountHolder accountName(String accountName) {
        this.setAccountName(accountName);
        return this;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public AccountHolder accountNumber(String accountNumber) {
        this.setAccountNumber(accountNumber);
        return this;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankCode() {
        return this.bankCode;
    }

    public AccountHolder bankCode(String bankCode) {
        this.setBankCode(bankCode);
        return this;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public AccountHolderStatus getStatus() {
        return this.status;
    }

    public AccountHolder status(AccountHolderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(AccountHolderStatus status) {
        this.status = status;
    }

    public String getCurrency() {
        return this.currency;
    }

    public AccountHolder currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAvailableBalance() {
        return this.availableBalance;
    }

    public AccountHolder availableBalance(BigDecimal availableBalance) {
        this.setAvailableBalance(availableBalance);
        return this;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getHoldBalance() {
        return this.holdBalance;
    }

    public AccountHolder holdBalance(BigDecimal holdBalance) {
        this.setHoldBalance(holdBalance);
        return this;
    }

    public void setHoldBalance(BigDecimal holdBalance) {
        this.holdBalance = holdBalance;
    }

    public BigDecimal getMinimumBalance() {
        return this.minimumBalance;
    }

    public AccountHolder minimumBalance(BigDecimal minimumBalance) {
        this.setMinimumBalance(minimumBalance);
        return this;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public Boolean getIsRestrictedAccount() {
        return this.isRestrictedAccount;
    }

    public AccountHolder isRestrictedAccount(Boolean isRestrictedAccount) {
        this.setIsRestrictedAccount(isRestrictedAccount);
        return this;
    }

    public void setIsRestrictedAccount(Boolean isRestrictedAccount) {
        this.isRestrictedAccount = isRestrictedAccount;
    }

    public Instant getOpeningDate() {
        return this.openingDate;
    }

    public AccountHolder openingDate(Instant openingDate) {
        this.setOpeningDate(openingDate);
        return this;
    }

    public void setOpeningDate(Instant openingDate) {
        this.openingDate = openingDate;
    }

    public Profile getUser() {
        return this.user;
    }

    public void setUser(Profile profile) {
        this.user = profile;
    }

    public AccountHolder user(Profile profile) {
        this.setUser(profile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountHolder)) {
            return false;
        }
        return id != null && id.equals(((AccountHolder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountHolder{" +
            "id=" + getId() +
            ", profileId='" + getProfileId() + "'" +
            ", accountType='" + getAccountType() + "'" +
            ", accountName='" + getAccountName() + "'" +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", bankCode='" + getBankCode() + "'" +
            ", status='" + getStatus() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", availableBalance=" + getAvailableBalance() +
            ", holdBalance=" + getHoldBalance() +
            ", minimumBalance=" + getMinimumBalance() +
            ", isRestrictedAccount='" + getIsRestrictedAccount() + "'" +
            ", openingDate='" + getOpeningDate() + "'" +
            "}";
    }
}
