package com.exam.online.security;

import com.exam.online.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class DemoSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public DemoSecurityConfig(@Lazy CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    // Configure JDBC-based user details manager
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // Custom query to fetch user details
        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT username, password, enabled FROM users_details WHERE username=?");

        // Custom query to fetch roles
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT u.username, CONCAT('ROLE_', r.name) FROM users_details u JOIN roles r ON u.role_id = r.id WHERE u.username=?");
        return jdbcUserDetailsManager;
    }

    // Use BCryptPasswordEncoder for encoding passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configure DaoAuthenticationProvider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder()); // Use BCryptPasswordEncoder
        return authProvider;
    }

    // Configure AuthenticationManager with custom provider
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(authenticationProvider()));
    }

    // Configure security filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("Teacher")

                        .requestMatchers(HttpMethod.GET, "/api/exams").hasAnyRole("Student", "Teacher")
                        .requestMatchers(HttpMethod.GET, "/api/exams/**").hasAnyRole("Student", "Teacher")
                        .requestMatchers(HttpMethod.POST, "/api/exams").hasRole("Teacher")
                        .requestMatchers(HttpMethod.PUT, "/api/exams/**").hasRole("Teacher")
                        .requestMatchers(HttpMethod.DELETE, "/api/exams/**").hasRole("Teacher")

                        .requestMatchers(HttpMethod.GET, "/api/questions").hasAnyRole("Student", "Teacher")
                        .requestMatchers(HttpMethod.GET, "/api/questions/**").hasAnyRole("Student", "Teacher")
                        .requestMatchers(HttpMethod.POST, "/api/questions").hasRole("Teacher")
                        .requestMatchers(HttpMethod.PUT, "/api/questions/**").hasRole("Teacher")
                        .requestMatchers(HttpMethod.DELETE, "/api/questions/**").hasRole("Teacher")

                        .requestMatchers(HttpMethod.GET, "/api/results").hasAnyRole("Student", "Teacher")
                        .requestMatchers(HttpMethod.GET, "/api/results/**").hasAnyRole("Student", "Teacher")
                        .requestMatchers(HttpMethod.POST, "/api/results").hasRole("Teacher")
                        .requestMatchers(HttpMethod.PUT, "/api/results/**").hasRole("Teacher")
                        .requestMatchers(HttpMethod.DELETE, "/api/results/**").hasRole("Teacher")

                        .requestMatchers(HttpMethod.GET, "/api/roles").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/roles").permitAll()
                        .anyRequest().authenticated());

        // Enable Basic Auth
        http.httpBasic(Customizer.withDefaults());

        // Disable CSRF (since it's a REST API)
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
