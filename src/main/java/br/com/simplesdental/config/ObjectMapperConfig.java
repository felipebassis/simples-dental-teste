package br.com.simplesdental.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ObjectMapperConfig {

    @Bean
    Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        };
    }
}
