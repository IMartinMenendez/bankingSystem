package com.ironhack.banking_system.security;

import com.ironhack.banking_system.services.CustomUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final PasswordEncoder passwordEncoder;

  private final CustomUserDetailsService userDetailsService;

  public SecurityConfiguration(PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService) {
    this.passwordEncoder = passwordEncoder;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.httpBasic();
    http.csrf().disable();
    http.authorizeRequests()
        .mvcMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
        .mvcMatchers(HttpMethod.GET, "/accounts").hasRole("ADMIN")
        .mvcMatchers(HttpMethod.POST, "/accounts").hasRole("ADMIN")
        .mvcMatchers(HttpMethod.PUT, "/accounts").hasRole("ADMIN")
        .mvcMatchers(HttpMethod.GET, "/accounts/{user_id}").hasAnyRole("ADMIN", "ACCOUNTHOLDER")
        .mvcMatchers(HttpMethod.PUT, "/accounts").hasAnyRole("ADMIN", "USER")
        .anyRequest().permitAll();

  }
}
