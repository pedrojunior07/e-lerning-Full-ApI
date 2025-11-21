package com.elearning.e_learning_core.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapeia /files/** para /var/www/files/
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:/var/www/files/")
                .setCachePeriod(3600); // opcional: cache de 1 hora
    }
}
