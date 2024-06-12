package com.dblog.dblog.Config;

import com.dblog.dblog.utils.RateLimitFilter;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Aplica CORS a todas las rutas
                .allowedOrigins("*")
                //.allowedOrigins("https://www.tecnosapiens.blog", "https://tecnosapiens.blog")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Métodos HTTP permitidos
                //.allowCredentials(true)
                .allowedHeaders("*");  // Permite todas las cabeceras
    }
    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilter() {
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitFilter(limit));
        registrationBean.addUrlPatterns("/v1/*");
        return registrationBean;
    }
}
