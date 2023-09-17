package com.prodapt.learningspring;
import com.prodapt.learningspring.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import com.prodapt.learningspring.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(customUserDetailsService);
    }
 
   @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests((requests) -> requests
            .requestMatchers("/forum/register").permitAll()
            .requestMatchers("/Admin/reported-entries").hasRole("ADMIN")
            .anyRequest().authenticated())
        .logout(withDefaults())
        .formLogin(withDefaults())
        .exceptionHandling((exceptionHandling) -> exceptionHandling
            .accessDeniedHandler((request, response, accessDeniedException) -> {
                // Redirect to the login page when access is denied
                response.sendRedirect("/login"); // Adjust the URL as needed
            }));
    
    return http.build();
}
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        http
//            .authorizeHttpRequests((requests) -> requests
//            .requestMatchers("/forum/register").permitAll()
//			.requestMatchers("/Admin/reported-entries").hasRole("ADMIN")
//            .anyRequest().authenticated())
//            .logout(withDefaults())
//            .formLogin(withDefaults());
//        
//        return http.build();
//    }
    
   
}
