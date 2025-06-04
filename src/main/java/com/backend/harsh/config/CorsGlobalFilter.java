package com.backend.harsh.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsGlobalFilter {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of( "https://harshclinic-5smh.vercel.app"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // config.setAllowedHeaders(Arrays.asList("*"));
        // config.setAllowCredentials(true);
         config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // Allow cookies
        config.setMaxAge(3600L); // Cache pre-flight for 1 hour
        // source.registerCorsConfiguration("/**", config);
        // return new CorsFilter(source);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
