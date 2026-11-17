package com.stan.account.mapper;

import com.stan.account.dto.request.CreateCustomerDto;
import com.stan.account.entity.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CustomerMapper {

    public Profile mapCustomerToProfile(CreateCustomerDto request) {
        if (request == null) return null;
        Profile profile = new Profile();
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setEmail(request.getEmail());
        profile.setPhone(request.getPhone());
        profile.setFullName(request.getFullName());
        profile.setAlias(request.getAlias());
        profile.setPhoneCountryCode(request.getPhoneCountryCode());
        profile.setDateOfBirth(request.getDateOfBirth().toInstant());
        profile.setGender(request.getGender());
        profile.setBvn(request.getBvn());
        // profile.setStreet(request.gets);
        profile.setCity(request.getCity());
        //profile.setPostalCode(request.getPos);
        profile.setCbaTenant(request.getFineractTenant());
        profile.setCreationDate(Instant.now());
        profile.setActive(true);


        return profile;
    }
}
