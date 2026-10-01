package com.stan.profile.config.securityconfig;

import com.stan.profile.service.JWTService;
import com.stan.profile.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Inside JWT Authentication Filter::doFilterInternal");
        String token = request.getHeader("Authorization");
        log.info("token...{}", token);
        if (token == null || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = token.substring(7);
        String userName = jwtService.getUsername(jwt);
        log.info("userName...{}", userName);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null && userName != null) {
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userName);
            log.info("userDetails...{}", userDetails);
            if (jwtService.isValidateToken(jwt, userDetails)) {
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        userDetails.getAuthorities());
                authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                securityContext.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(securityContext);

//                SecurityContextHolder
//                    .getContext()
//                    .setAuthentication(authenticationToken);

                log.info("JWT authentication successfully set for user: {}",
                    userName);


                // SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);


    }


//@Override
//protected void doFilterInternal(
//    HttpServletRequest request,
//    HttpServletResponse response,
//    FilterChain filterChain)
//    throws ServletException, IOException {
//
//    log.info("Inside JWT Authentication Filter::doFilterInternal");
//
//    String token = request.getHeader("Authorization");
//
//    if (token == null || !token.startsWith("Bearer ")) {
//        filterChain.doFilter(request, response);
//        return;
//    }
//
//    String jwt = token.substring(7);
//
//    String userName = jwtService.getUsername(jwt);
//
//    log.info("userName...{}", userName);
//
//    if (userName != null &&
//        SecurityContextHolder.getContext().getAuthentication() == null) {
//
//        UserDetails userDetails =
//            userService.userDetailsService()
//                .loadUserByUsername(userName);
//
//        if (jwtService.isValidateToken(jwt, userDetails)) {
//
//            UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(
//                    userDetails,
//                    null,
//                    userDetails.getAuthorities()
//                );
//
//            authenticationToken.setDetails(
//                new WebAuthenticationDetailsSource()
//                    .buildDetails(request)
//            );
//
//            SecurityContextHolder
//                .getContext()
//                .setAuthentication(authenticationToken);
//
//            log.info("JWT authentication successfully set for user: {}",
//                userName);
//        }
//    }
//
//    filterChain.doFilter(request, response);
//}


//@Override
//protected void doFilterInternal(
//    HttpServletRequest request,
//    HttpServletResponse response,
//    FilterChain filterChain)
//    throws ServletException, IOException {
//
//    log.info("========== JWT FILTER START ==========");
//    log.info("Request URI: {}", request.getRequestURI());
//
//    String authHeader = request.getHeader("Authorization");
//
//    log.info("Authorization header: {}", authHeader);
//
//    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//        log.info("No Bearer token found");
//        filterChain.doFilter(request, response);
//        return;
//    }
//
//    String jwt = authHeader.substring(7);
//
//    String username = jwtService.getUsername(jwt);
//
//    log.info("Username extracted from JWT: {}", username);
//
//    if (username == null) {
//        log.info("Username is null. JWT authentication cannot continue.");
//        filterChain.doFilter(request, response);
//        return;
//    }
//
//    Authentication currentAuthentication =
//        SecurityContextHolder.getContext().getAuthentication();
//
//    log.info("Authentication BEFORE JWT processing: {}",
//        currentAuthentication);
//
//    if (currentAuthentication == null ||
//        currentAuthentication instanceof AnonymousAuthenticationToken) {
//
//        UserDetails userDetails =
//            userService.userDetailsService()
//                .loadUserByUsername(username);
//
//        log.info("Loaded UserDetails: {}", userDetails);
//
//        boolean valid = jwtService.isValidateToken(jwt, userDetails);
//
//        log.info("JWT valid: {}", valid);
//
//        if (valid) {
//
//            UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(
//                    userDetails,
//                    null,
//                    userDetails.getAuthorities()
//                );
//
//            authenticationToken.setDetails(
//                new WebAuthenticationDetailsSource()
//                    .buildDetails(request)
//            );
//
//            SecurityContextHolder
//                .getContext()
//                .setAuthentication(authenticationToken);
//
//            log.info("Authentication SET successfully");
//
//            log.info("Authentication AFTER JWT processing: {}",
//                SecurityContextHolder.getContext()
//                    .getAuthentication());
//        }
//    }
//
//    filterChain.doFilter(request, response);
//
//    log.info("========== JWT FILTER END ==========");
//}
}
