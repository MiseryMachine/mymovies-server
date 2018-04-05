package com.rjs.mymovies.server.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AppUserDetailsService userDetailsService;

    public WebSecurityConfig() {
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/rest/**").hasRole("REST_USER")
                .antMatchers("/shows/**").hasRole("USER")
                .antMatchers("/", "/home", "/user/signup", "/user/registration", "/ws/**", "/webjars/**", "/css/**", "/img/**", "/js/**", "/datatables/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/user/login").permitAll()
//                .loginPage("/login").permitAll()
//                .successForwardUrl("/home")
//                .failureForwardUrl("/user/login")
                .and()
            .logout()
                .deleteCookies("JESSSIONID")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .permitAll()
                .logoutSuccessUrl("/home");
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());

        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
}
