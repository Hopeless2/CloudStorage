package com.example.cloudstorage.config;

import com.example.cloudstorage.properties.CorsProperties;
import com.example.cloudstorage.security.JwtAuthenticationEntryPoint;
import com.example.cloudstorage.security.JwtRequestFilter;
import com.example.cloudstorage.service.security.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // login/logout paths
    private static final String LOGIN = "/login";
    private static final String LOGOUT = "/logout";

    private static final String COOKIE = "JSESSIONID";

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtUserDetailsService userDetailService;

    private final JwtRequestFilter jwtRequestFilter;

    private final CorsProperties corsProperties;

    PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtUserDetailsService userDetailService,
                          JwtRequestFilter jwtRequestFilter, CorsProperties corsProperties,
                          PasswordEncoder passwordEncoder) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.userDetailService = userDetailService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.corsProperties = corsProperties;
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors()

                .and()
                .authorizeRequests().antMatchers(LOGIN).permitAll()
                .anyRequest().authenticated()

                .and()
                .logout()
                .logoutUrl(LOGOUT)
                .deleteCookies(COOKIE)
                .clearAuthentication(true)
                .logoutSuccessUrl(LOGIN)

                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)

                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .addFilterAfter(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsProperties.getAllowedOrigins());
        configuration.setAllowedMethods(corsProperties.getAllowedMethods());
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

