
package com.example.demo.security;

import java.beans.Customizer;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfiguration {
    private JwtAuth authEntryPoint;
    private newUserDetailsService userDetailsService;

    @Autowired

    public SecurityConfiguration(newUserDetailsService userDetailsService, JwtAuth authEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
    }
    
      @Bean
      public SecurityFilterChain customFilterChadin(HttpSecurity httpSecurity)
      throws Exception {
      httpSecurity.csrf().disable()
     .authorizeHttpRequests()

     .requestMatchers("/addUser",
     "/cars","/listOfroles","/getCoursesSuggestion/{email}","/login","/forgotPassword","/resetPassword/{token}","/resetPassword","/updateUser/{email}","/progressandUpdate","/profile/{email}")
      .permitAll();
      httpSecurity.addFilterBefore(jwtAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
      return httpSecurity.build();
      }
     


    /*
     * @Bean
     * public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     * http
     * .csrf().disable()
     * .exceptionHandling()
     * .authenticationEntryPoint(authEntryPoint)
     * .and()
     * .sessionManagement()
     * .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
     * .and()
     * .authorizeHttpRequests()
     * .requestMatchers("/login").permitAll()
     * .anyRequest().authenticated()
     * .and()
     * .httpBasic();
     * http.addFilterBefore(jwtAuthenticationFilter(),
     * UsernamePasswordAuthenticationFilter.class);
     * return http.build();
     * }
     */

   
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserNamePassFiltering jwtAuthenticationFilter() {
        return new UserNamePassFiltering();
    }

}