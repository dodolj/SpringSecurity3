package org.example.springsecurity4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import java.time.Instant;
import java.util.Optional;

@Configuration
public class JpaConfig {

    @Bean
    public AuditorAware<Instant> auditorProvider() {
        return () -> Optional.of(Instant.now());
    }
}