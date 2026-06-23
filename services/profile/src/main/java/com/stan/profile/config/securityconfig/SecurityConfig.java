package com.stan.profile.config.securityconfig;


import com.stan.profile.enums.Role;
import com.stan.profile.service.JWTService;
import com.stan.profile.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JWTService jwtService;
    private final UserService userDetailsService;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(
                request ->
                    request.requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login").hasAuthority(Role.ADMIN.name())
                        .anyRequest().authenticated()
            )
            .sessionManagement(manager ->
                manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider()).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService.userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//        @Bean
//        SecurityFilterChain securityFilterChain(HttpSecurity http)
//            throws Exception {
//
//            return http
//                .csrf(AbstractHttpConfigurer::disable)
//
//                .sessionManagement(session ->
//                    session.sessionCreationPolicy(
//                        SessionCreationPolicy.STATELESS))
//
//                .authorizeHttpRequests(auth -> auth
//
//                    .requestMatchers(
//                        "/actuator/health",
//                        "/swagger-ui/**",
//                        "/v3/api-docs/**")
//                    .permitAll()
//
//                    .requestMatchers(HttpMethod.GET,
//                        "/api/profile/me")
//                    .authenticated()
//
//                    .anyRequest()
//                    .authenticated()
//                )
//
//                .oauth2ResourceServer(oauth ->
//                    oauth.jwt(Customizer.withDefaults()))
//
//                .build();
//        }
//

}
