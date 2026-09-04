package com.glebzapara.nexor.config;

import com.glebzapara.nexor.security.AdminDetails;
import com.glebzapara.nexor.security.ClientUserDetails;
import com.glebzapara.nexor.services.AdminDetailsService;
import com.glebzapara.nexor.services.ClientUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AdminDetailsService adminDetailsService;
    private final ClientUserDetailsService clientUserDetailsService;
    private final LastSeenFilter lastSeenFilter;

    public SecurityConfig(AdminDetailsService adminDetailsService,
                          ClientUserDetailsService clientUserDetailsService,
                          LastSeenFilter lastSeenFilter) {
        this.adminDetailsService = adminDetailsService;
        this.clientUserDetailsService = clientUserDetailsService;
        this.lastSeenFilter = lastSeenFilter;
    }

    @Bean
    public DaoAuthenticationProvider adminAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(adminDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public DaoAuthenticationProvider userDetailsService() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(clientUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(adminAuthProvider())
                .authenticationProvider(userDetailsService())
                .csrf(csrf -> csrf.disable())
                .addFilterAfter(lastSeenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login",
                                "/register",
                                "/robots.txt",
                                "/favicon.ico",
                                "/images/**").permitAll()
                        .requestMatchers("/admins/**").hasRole("SUPER_ADMIN")
                        .requestMatchers(
                                "/students/new",
                                "/students/edit/**",
                                "/students/delete/**",
                                "/teachers/new",
                                "/teachers/edit/**",
                                "/teachers/delete/**",
                                "/groups/new",
                                "/groups/edit/**",
                                "/groups/delete/**",
                                "/departments/new",
                                "/departments/edit/**",
                                "/departments/delete/**",
                                "/subjects/new",
                                "/subjects/delete/**",
                                "/lessons/new",
                                "/lessons/edit/**",
                                "/lessons/delete/**",
                                "/grades/new",
                                "/grades/delete/**"
                        ).hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers("/subjects/edit/**",
                                "/subjects/grades/**",
                                "/grades/edit/**")
                        .hasAnyRole("SUPER_ADMIN", "ADMIN", "TEACHER")
                        .requestMatchers("/students/**")
                        .hasAnyRole("SUPER_ADMIN", "ADMIN", "STUDENT", "TEACHER")
                        .requestMatchers("/teachers/**")
                        .hasAnyRole("SUPER_ADMIN", "ADMIN", "STUDENT", "TEACHER")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/403")
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login?error")
                        .successHandler((request,
                                         response,
                                         authentication) -> {
                            Object principal = authentication.getPrincipal();

                            if (principal instanceof AdminDetails) {
                                response.sendRedirect("/");
                            } else if (principal instanceof ClientUserDetails u) {
                                response.sendRedirect("/users/" + u.getUser().getId() + "/profile");
                            }
                        })
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}