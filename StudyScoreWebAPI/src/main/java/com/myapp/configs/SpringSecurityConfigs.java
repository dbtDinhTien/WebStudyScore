/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Cookie;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 *
 * @author ADMIN
 */
@EnableMethodSecurity
@Configuration
@EnableWebSecurity
@EnableTransactionManagement
@ComponentScan(basePackages = "com.myapp")
public class SpringSecurityConfigs {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
            Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/").authenticated()
                .requestMatchers("/api/**").permitAll()
                // Các api classes
                .requestMatchers(HttpMethod.GET, "/api/classes/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/classes/**").hasRole("ADMIN")
                // Các api classSubjects
                .requestMatchers(HttpMethod.GET, "/api/classSubjects/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/classSubjects/lecturer/**").hasRole("LECTURER")
                .requestMatchers(HttpMethod.DELETE, "/api/classSubjects/**").hasRole("ADMIN")
                // Phân quyền cho API chat
                .requestMatchers("/api/chat/**").authenticated()
                // Các API score
                .requestMatchers(HttpMethod.DELETE, "/api/scores/**").hasRole("ADMIN")
                .requestMatchers(
                        "/api/scores/add",
                        "/api/scores/import/**",
                        "/api/scores/lock/**",
                        "/api/scores/export-excel/**",
                        "/api/scores/export-pdf/**",
                        "/api/scores/**"
                ).hasAnyRole("LECTURER", "ADMIN")
                .requestMatchers("/api/scores/student/**", "/api/scores/stuClassSubject/**").authenticated()
                // Các API StudentClassSubjects
                .requestMatchers(HttpMethod.DELETE, "/api/stuClassSubjects/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/stuClassSubjects/export/**").hasAnyRole("LECTURER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/stuClassSubjects/classSubject/**").hasAnyRole("LECTURER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/stuClassSubjects/student/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/stuClassSubjects/**").hasAnyRole("LECTURER", "ADMIN") // lấy toàn bộ danh sách
                // Các API Subject     
                .requestMatchers(HttpMethod.GET, "/api/subjects/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/subjects/**").hasRole("ADMIN")
                //Các API User
                .requestMatchers("/api/secure/profile").authenticated()
                .requestMatchers("/api/students/search").authenticated()
                .requestMatchers("/api/lecturers/search").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
                )
                .logout(logout -> logout
                .logoutSuccessUrl("/login")
                .permitAll()
                );
        return http.build();
    }

    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary
                = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", "dq5ajyj0q",
                        "api_key", "683173744638424",
                        "api_secret", "LkEEzzQsC9AfYLfT5AG9XCOx73A",
                        "secure", true));
        return cloudinary;
    }

    @Bean
    @Order(0)
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000/")); // frontend origin
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true); // Nếu dùng cookie/session

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
