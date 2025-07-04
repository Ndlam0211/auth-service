package com.example.gateway.config;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig{
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .uri("lb://auth-service"))
                .route("auth-service", r -> r.path("/api/v1/users/**")
                        .uri("lb://auth-service"))
                .route("auth-service", r -> r.path("/api/v1/user_catalogues/**")
                        .uri("lb://auth-service"))
                .route("auth-service", r -> r.path("/api/v1/permissions/**")
                        .uri("lb://auth-service"))
                .build();
    }
}