package com.stan.profile.service.impl;

import com.stan.profile.dto.request.AuthenticationRequest;
import com.stan.profile.dto.response.DefaultResponse;
import com.stan.profile.dto.response.RegisterResponse;
import com.stan.profile.entity.Users;
import com.stan.profile.enums.ResponseStatus;
import com.stan.profile.mapper.UserMapper;
import com.stan.profile.repository.UserRepository;
import com.stan.profile.service.JWTService;
import com.stan.profile.service.UserProfileService;
import com.stan.profile.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserProfileServiceImpl implements UserProfileService {
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private static String RESPONSE_LOG = "Response of authenticaUser in UserProfileServiceImpl is ...{}, response";

    @Override
    public DefaultResponse<RegisterResponse> authenticateUser(AuthenticationRequest request) {
        log.info("Inside UserProfileServiceImpl::authenticaUser");
        DefaultResponse<RegisterResponse> response = new DefaultResponse<>();
        try {
            if (request.getToken() == null || request.getToken().isEmpty()) {
                response.setStatus(ResponseStatus.FAILED.getCode());
                response.setMessage("Token cannot be null or empty");
                log.info("Response of authenticaUser in UserProfileServiceImpl is ...{}", response);
                return response;
            }
            //String userName = jwtService.getUsername(request.getToken());
            String userName = SecurityUtil.getCurrentLoginUser().orElseThrow();
            Optional<Users> optionalUsers = userRepository.findByEmail(userName);
            if (optionalUsers.isEmpty()) {
                response.setStatus(ResponseStatus.FAILED.getCode());
                response.setMessage("User not found");
                log.info(RESPONSE_LOG);
                return response;
            }
            String encodedPassword = optionalUsers.get().getPassword();
            log.info("Username is ...{} and password is ...{},", userName, encodedPassword);
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userName, encodedPassword)
            );
            if (authentication.isAuthenticated()) {
                response.setStatus(ResponseStatus.SUCCESS.getCode());
                response.setMessage("User is authenticated");
                response.setData(userMapper.mapUsersToRegisterResponse(optionalUsers.get()));
                log.info(RESPONSE_LOG);
                return response;
            } else {
                response.setStatus(ResponseStatus.FAILED.getCode());
                response.setMessage("User is NOT authenticated");
                response.setData(userMapper.mapUsersToRegisterResponse(optionalUsers.get()));
                log.info(RESPONSE_LOG);
                return response;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus("01");
            response.setMessage(e.getMessage());
            log.info("Response Inside Exception block UserProfileServiceImpl::authenticaUser is ...{}", response);
            return response;
        }
    }


    public DefaultResponse<RegisterResponse> authenticateUser() {
        log.info("Inside UserProfileServiceImpl::authenticaUser");
        DefaultResponse<RegisterResponse> response = new DefaultResponse<>();
        try {
            String userName = SecurityUtil.getCurrentLoginUser().orElseThrow();
            Optional<Users> optionalUsers = userRepository.findByEmail(userName);
            if (optionalUsers.isEmpty()) {
                response.setStatus(ResponseStatus.FAILED.getCode());
                response.setMessage("User not found");
                log.info(RESPONSE_LOG);
                return response;
            }
            Authentication authentication = SecurityUtil.getAuthentication();
            if (authentication.isAuthenticated()) {
                response.setStatus(ResponseStatus.SUCCESS.getCode());
                response.setMessage("User is authenticated");
                response.setData(userMapper.mapUsersToRegisterResponse(optionalUsers.get()));
                log.info(RESPONSE_LOG);
                return response;
            } else {
                response.setStatus(ResponseStatus.FAILED.getCode());
                response.setMessage("User is NOT authenticated");
                response.setData(userMapper.mapUsersToRegisterResponse(optionalUsers.get()));
                log.info(RESPONSE_LOG);
                return response;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus("01");
            response.setMessage(e.getMessage());
            log.info("Response Inside Exception block UserProfileServiceImpl::authenticaUser is ...{}", response);
            return response;
        }
    }
}
