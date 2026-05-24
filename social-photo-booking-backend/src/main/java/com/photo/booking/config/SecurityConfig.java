package com.photo.booking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/api/upload/**", "/uploads/**", "/api/auth/login", "/api/auth/login/token", "/api/auth/login/phone", "/api/auth/delete", "/auth/login", "/api/auth/register", "/api/auth/user/role", "/api/content/info", "/api/content/list", "/api/content/hot", "/api/photographer/list", "/api/photographer/hot", "/api/photographer/info", "/api/photographer/debug", "/api/photographer/user", "/api/package/photographer", "/api/category/list", "/api/banner/list", "/api/report/**", "/photographer/list", "/photographer/hot", "/photographer/info", "/content/info", "/content/list", "/content/hot", "/category/list", "/banner/list").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilter(new JwtAuthorizationFilter(authenticationManager));
        return http.build();
    }
}
