package com.example.Gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GWConfig  {
    @Bean
    public RouteLocator configurarRutas(RouteLocatorBuilder builder,
                                        @Value("${gateway.url-microservicio-pruebas}") String uriPruebas,
                                        @Value("${gateway.url-microservicio-empleados}") String uriEmpleados,
                                        @Value("${gateway.url-microservicio-notificaciones}") String uriNotificaciones){

        return builder.routes()
                .route(p -> p.path("/api/pruebas/**").uri(uriPruebas))
                .route(p -> p.path("/api/posiciones/**").uri(uriPruebas))
                .route(p -> p.path("/api/interesados/**").uri(uriPruebas))
                .route(p -> p.path("/api/reportes/**").uri(uriPruebas))
                .route(p -> p.path("/api/empleados/**").uri(uriEmpleados))
                .route(p -> p.path("/api/notificaciones/**").uri(uriNotificaciones))
                .build();

    }
}
