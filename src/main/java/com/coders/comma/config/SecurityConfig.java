package com.coders.comma.config;

import com.nexmotion.board.account.AccountSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private String errorAccessDeniedRedirectURL = "/error/denied";

    private String loginURL = "/auth/login";

    private String loginProcessingURL = "/auth/proc";

    private String loginSuccessUrl = "/";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        String[] permitted = new String[] {
                "/resources/**", "/css/**", "/js/**", "/lib/**", "/auth/*", "/error/denied", "/img/**"
        };

        http.authorizeRequests()
                .antMatchers(permitted).permitAll()
//              .antMatchers("/").permitAll()
//              .antMatchers("/").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage(loginURL)
                .loginProcessingUrl(loginProcessingURL)
                .defaultSuccessUrl(loginSuccessUrl)
                .successHandler(new AccountSuccessHandler())
                .failureUrl(loginURL + "?error=true")
                .permitAll()
                // logout 세팅
                .and()
                .logout().permitAll()
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login")
                .and()
                .exceptionHandling().accessDeniedPage(errorAccessDeniedRedirectURL)
                .and().csrf().disable();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
