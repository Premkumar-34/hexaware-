package com.example.recruiting_application.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        http.cors().and()
                .csrf().disable()
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/**",
//                                        "/api/drafts",
//                                        "/api/createJob",
//                                        "/api/allJobs",
//                                        "/api/published",
//                                        "/api/applications/all",
                                        "/api/applications/**",
//                                        "/api/requiredJobs",
//                                        "/api/search",
//                                        "/api/profile/{email}",
//                                        "/api/password-recovery",
//                                        "/api/updateJob/{id}",
//                                        "/api/applications/apply",
//                                        "/api/reset-password",
//                                        "/api/login",
                                        "/login.html",
                                        "/register.html",
//                                        "/api/job/{id}",
                                        "/css/**",
                                        "/js/**").permitAll()
                                .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .formLogin().disable();

        return http.build();
    }
}
