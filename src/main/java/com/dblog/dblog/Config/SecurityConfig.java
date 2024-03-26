package com.dblog.dblog.Config;

import com.dblog.dblog.security.JwtAuthenticationFilter;
import com.dblog.dblog.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private final AuthService authService;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
       http.csrf(csrf ->
                        csrf.disable())
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/blog/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/v1/user/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/v1/users/**").authenticated()
                                .requestMatchers(HttpMethod.POST, "/blog/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/blog/**").authenticated()

                                .anyRequest().authenticated()
                )
                        .addFilterBefore(new JwtAuthenticationFilter(authService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
