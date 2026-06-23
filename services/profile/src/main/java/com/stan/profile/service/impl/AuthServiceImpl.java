package com.stan.profile.service.impl;

import com.stan.profile.dto.request.LoginRequest;
import com.stan.profile.dto.request.RegisterRequest;
import com.stan.profile.dto.response.DefaultResponse;
import com.stan.profile.dto.response.LoginResponse;
import com.stan.profile.dto.response.RegisterResponse;
import com.stan.profile.entity.Users;
import com.stan.profile.enums.ResponseStatus;
import com.stan.profile.exception.BadRequestException;
import com.stan.profile.mapper.UserMapper;
import com.stan.profile.repository.UserRepository;
import com.stan.profile.service.AuthService;
import com.stan.profile.service.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public DefaultResponse<RegisterResponse> register(RegisterRequest request) {
        log.info("Inside AuthServiceImpl::register with request ...{}", request);
        DefaultResponse<RegisterResponse> response = new DefaultResponse<>();
        try {
            Optional<Users> userCheck = userRepository.findByEmail(request.getEmail());
            if (userCheck.isPresent()) {
                throw new BadRequestException("User with email "+request.getEmail()+" already exists");
            }
            Users user = userMapper.mapRegisterRequestToUsers(request);
            Users savedUser = userRepository.save(user);
            response.setStatus(ResponseStatus.SUCCESS.getCode());
            response.setMessage(ResponseStatus.SUCCESS.getMessage());
            response.setData(userMapper.mapUsersToRegisterResponse(savedUser));
            log.info("Response Inside AuthServiceImpl::register is ...{}", response);
            return response;
        } catch (BadRequestException e){
            throw e;
        }
        catch (Exception e) {
            response.setStatus("01");
            response.setMessage(e.getMessage());
            log.info("Response Inside Exception block AuthServiceImpl::register is ...{}", response);
            return response;
        }
    }

    @Override
    public DefaultResponse<LoginResponse> login(LoginRequest request) {
        log.info("Inside AuthServiceImpl::login with request ...{}", request);
        DefaultResponse<LoginResponse> response = new DefaultResponse<>();
        try {
            LoginResponse loginResponse = new LoginResponse();
            if (request == null) {
                throw new BadRequestException("Request can not be null");
            }
            var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            Users user = userRepository.findByEmail(request.getUsername()).orElseThrow();
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);
                loginResponse.setToken(token);
                loginResponse.setRefreshToken(refreshToken);
            }
            response.setStatus(ResponseStatus.SUCCESS.getCode());
            response.setMessage(ResponseStatus.SUCCESS.getMessage());
            response.setData(loginResponse);
            log.info("Response Inside AuthServiceImpl::login is ...{}", response);
            return response;
        } catch (Exception e) {
            response.setStatus(ResponseStatus.FAILED.getCode());
            response.setMessage(e.getMessage());
            log.info("Response Inside Exception block AuthServiceImpl::login is ...{}", response);
            return response;
        }
    }
}
