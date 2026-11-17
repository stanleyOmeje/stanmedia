package com.stan.account.mapper;

import com.stan.account.dto.request.OpenAccountDto;
import com.stan.account.dto.request.ProfileDTO;
import com.stan.account.utils.DateUtils;
import com.stan.account.utils.RandomeNumberUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class AccountMapper {
    public HashMap<String, Object> buildNubandAccountRequest(ProfileDTO profile, OpenAccountDto openAccReq) {
        final String ACCOUNT_NAME = "accountName";

        HashMap<String, Object> requestData = new HashMap<>();
        if (StringUtils.isNotBlank(profile.getFullName())) {
            requestData.put(ACCOUNT_NAME, profile.getFullName());
        } else if (profile.getOtherName() == null || StringUtils.isBlank(profile.getOtherName())) {
            requestData.put(ACCOUNT_NAME, profile.getFirstName() + " " + profile.getLastName());
        } else {
            requestData.put(ACCOUNT_NAME, profile.getFirstName() + " " + profile.getOtherName() + " " + profile.getLastName());
        }
        requestData.put("bvn", profile.getBvn());
        requestData.put("city", profile.getCity());
        // todo: Start collecting state on create client requests
        requestData.put("state", profile.getCity());
        requestData.put("dob", DateUtils.formatDate.apply(profile.getDateOfBirth()));
        requestData.put("gender", profile.getGender());
        requestData.put("phoneNumber", profile.getPhone());
        requestData.put("email", profile.getEmail());
        requestData.put("tenant", openAccReq.getTenant());
        requestData.put("profileId", openAccReq.getMerchantId());

        return requestData;
    }


    public HashMap<String, Object> buildReservedAccountRequest(ProfileDTO profile, OpenAccountDto openAccReq) {
        String acctReference = RandomeNumberUtils.generateUniqueRandomNumber(11);

        final String ACCOUNT_NAME = "accountName";
        final String CUSTOMER_NAME = "customerName";

        HashMap<String, Object> data = new HashMap<>();
        if (StringUtils.isNotBlank(profile.getFullName())) {
            data.put(ACCOUNT_NAME, profile.getFullName());
            data.put(CUSTOMER_NAME, profile.getFullName());
        } else if (profile.getOtherName() == null || StringUtils.isBlank(profile.getOtherName())) {
            data.put(ACCOUNT_NAME, profile.getFirstName() + " " + profile.getLastName());
            data.put(CUSTOMER_NAME, profile.getFirstName() + " " + profile.getLastName());
        } else {
            data.put(ACCOUNT_NAME, profile.getFirstName() + " " + profile.getOtherName() + " " + profile.getLastName());
            data.put(CUSTOMER_NAME, profile.getFirstName() + " " + profile.getOtherName() + " " + profile.getLastName());
        }
        data.put("email", profile.getEmail());
        data.put("accountReference", acctReference);
        data.put("phoneNumber", profile.getPhone());
        data.put("tenant", openAccReq.getTenant());
        data.put("profileId", openAccReq.getMerchantId());

        return data;
    }

}
