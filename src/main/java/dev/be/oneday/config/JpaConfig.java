package dev.be.oneday.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    // TODO: 추후에 권한추가 해야함.
    @Bean
    public AuditorAware<String> auditorAware(){
        return () -> Optional.of("test");
    }
}
