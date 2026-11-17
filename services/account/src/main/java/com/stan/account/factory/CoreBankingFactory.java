package com.stan.account.factory;

import com.stan.account.dto.request.CreateClientRequestDto;
import com.stan.account.dto.request.CreateCustomerDto;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CoreBankingFactory {
    public static final String DD_MMMM_YYYY = "dd MMMM yyyy";
    public static final String LOCALE = "en";
    public static final String OFFICE_ID = "1";
    public static final String LEGAL_FORM_ID = "1";
    public CreateClientRequestDto buildCreateClientRequest(CreateCustomerDto openAccountDto, String fineractTenant) {
        CreateClientRequestDto request = new CreateClientRequestDto();
        request.setFirstname(openAccountDto.getFirstName());
        request.setLastname(openAccountDto.getLastName());
        request.setMiddlename(openAccountDto.getOtherName());
        request.setMobileNo(openAccountDto.getPhone());
        request.setOfficeId(OFFICE_ID);
        request.setDateFormat(DD_MMMM_YYYY);
        request.setDateOfBirth(getFormatedDate(openAccountDto.getDateOfBirth()));
        request.setActive("true");
        request.setActivationDate(getFormatedDate(new Date()));
        request.setLocale(LOCALE);
        //request.setClientTypeId(getClientType(openAccountDto.getClientType().toUpperCase(), fineractTenant));
        //request.setGenderId(getGenderId(openAccountDto.getGender(), fineractTenant));
        request.setMobileNo(openAccountDto.getPhone());
        request.setLegalFormId(LEGAL_FORM_ID);
        return request;
    }
    public static String getFormatedDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DD_MMMM_YYYY);
        return formatter.format(date);
    }
}
