package com.ito.vip.common.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //enable PreAuthorize annotation
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        ProviderManager authenticationManager = new ProviderManager(
            Arrays.asList(authenticationProvider()));
        return authenticationManager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // disable csrf
            .csrf().disable()
            .cors().disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            // doesn't need session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
            // can access to static resources
            .antMatchers(HttpMethod.GET, "/", "/assets/**", "/pages/**", "/favicon.ico", "/webjars/*","/swagger-resources/*", "/v2/api-docs/*").permitAll()
            .antMatchers("/users/login").permitAll()
            .antMatchers("/users/logout").permitAll()
            .antMatchers("/users/add").permitAll();
            // all request need authentication except for the above request
            //.anyRequest().authenticated();

        // disable cache
        //http.headers().cacheControl();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // static resources to add cache
        web.ignoring().antMatchers("/assets/**","/pages/**");
    }

}
