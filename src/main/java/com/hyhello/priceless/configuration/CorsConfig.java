package com.hyhello.priceless.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    private CorsConfiguration buildConfig() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        /**
         *  When allowCredentials is true, allowedOrigins cannot contain
         *  the special value "*"since that cannot be set on the
         *  "Access-Control-Allow-Origin" response header.
         *  To allow credentials to a set of origins,
         *  list them explicitly or consider using "allowedOriginPatterns" instead.
         */
        //corsConfiguration.addAllowedOrigin("*"); // 1允许任何域名使用
        corsConfiguration.addAllowedHeader("*"); // 2允许任何头
        corsConfiguration.addAllowedMethod("*"); // 3允许任何方法（post、get等）
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }
}