//package com.is.Lab1.config
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
//
//@Configuration
//@EnableWebSecurity
//class SecurityConfig : WebSecurityConfigurerAdapter() {
//    @kotlin.Throws(Exception::class)
//    protected override fun configure(http: HttpSecurity) {
//        http
//            .authorizeRequests()
//            .antMatchers("/login").permitAll()
//            .anyRequest().authenticated()
//            .and()
//            .formLogin()
//            .loginPage("/login")
//            .permitAll()
//    }
//}