package com.stan.profile.mapper;

import com.stan.profile.dto.request.RegisterRequest;
import com.stan.profile.dto.response.RegisterResponse;
import com.stan.profile.entity.Users;
import com.stan.profile.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public Users mapRegisterRequestToUsers(RegisterRequest request) {
        log.info("Inside UserMapper::mapRegisterRequestToUsers with request {}", request);
        if (request == null) return null;
        Users users = new Users();
        users.setRole(Role.USER);
        users.setFirstName(request.getFirstName());
        users.setLastName(request.getLastName());
        users.setEmail(request.getEmail());
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        log.info("User in mapRegisterRequestToUsers is ...{}", users);
        return users;
    }

    public RegisterResponse mapUsersToRegisterResponse(Users users) {
        log.info("Inside UserMapper::mapRegisterRequestToUsers with users {}", users);
        if (users == null) return null;
        RegisterResponse response = new RegisterResponse();
        response.setFirstName(users.getFirstName());
        response.setLastName(users.getLastName());
        response.setEmail(users.getEmail());
        response.setPassword(passwordEncoder.encode(users.getPassword()));
        log.info("RegisterResponse in mapUsersToRegisterResponse is ...{}", response);
        return response;
    }
}
