package com.dblog.dblog.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Aplica CORS a todas las rutas
                //.allowedOrigins("*")
                .allowedOrigins("https://www.tecnosapiens.blog", "https://tecnosapiens.blog")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Métodos HTTP permitidos
                .allowCredentials(true)
                .allowedHeaders("*");  // Permite todas las cabeceras
    }
}
