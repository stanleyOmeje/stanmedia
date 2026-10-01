package com.stan.gateway.interceptors;

import com.google.gson.Gson;
import com.stan.gateway.dto.response.AuthenticationResponse;
import com.stan.gateway.dto.response.DefaultResponse;
import com.stan.gateway.enums.ResponseStatus;
import com.stan.gateway.service.ProfileService;
import com.stan.gateway.service.http.ProfileHttp;
import com.stan.gateway.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
@Slf4j
@Component
public class ProfileInterceptor implements HandlerInterceptor {
    private static final String LOG_MESSAGE = "[+] Error writing the response {}";
    Gson gson = new Gson();
    private final ProfileHttp profileHttp;
    private final ProfileService profileService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        var url = request.getRequestURL();
        log.info("Request URL: {}", url);

        var uri = request.getRequestURI();
        log.info("Request URI: {}", uri);
        if (uri.startsWith("/api/v1/auth")) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null) {
            return buildErrorResponse(response, ResponseStatus.UNAUTHORIZED);
        }
        log.info("token inside prehandle is ...{}", token);

        String email = JwtUtil.getUserEmail(request);
        try {
            boolean exists = profileService.isExistingProfile(email);
            if (exists) {
                log.info("profile already exists");
                return true;
            }
        } catch (Exception e) {
            log.error("Profile does not exist");
        }


        String aToken = token.substring(7);
        AuthenticationResponse authResponse = profileHttp.authenticate(aToken);
        if (authResponse == null || !"00".equals(authResponse.getStatus())) {
            return buildErrorResponse(response, ResponseStatus.UNAUTHORIZED);
        }
        try {
            profileService.createProfile(authResponse.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    /**
     * Build error response with default status code (200)
     */
    private boolean buildErrorResponse(HttpServletResponse response,
                                       ResponseStatus responseCode) {
        return buildErrorResponse(response, responseCode, HttpServletResponse.SC_OK);
    }

    /**
     * Build error response with custom status code
     */
    private boolean buildErrorResponse(HttpServletResponse response,
                                       ResponseStatus responseCode,
                                       int httpStatus) {
        DefaultResponse apiResponse = new DefaultResponse();
        apiResponse.setStatus(responseCode.getCode());
        apiResponse.setMessage(responseCode.getMessage());

        response.setStatus(httpStatus);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(gson.toJson(apiResponse));
        } catch (IOException e) {
            log.error(LOG_MESSAGE, e);
        }

        return false;
    }

}
