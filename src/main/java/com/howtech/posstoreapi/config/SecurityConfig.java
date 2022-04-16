package com.howtech.posstoreapi.config;

import com.howtech.posstoreapi.security.BasicAuthFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfigurerAdapter.class);

    private final BasicAuthFilter basicAuthFilter;

    public SecurityConfig(BasicAuthFilter basicAuthFilter) {
        this.basicAuthFilter = basicAuthFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
                .authenticationEntryPoint(basicAuthFilter);

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .inMemoryAuthentication()
                .withUser("admin")
                .password("{noop}pass")
                .roles("ADMIN");
    }
}
