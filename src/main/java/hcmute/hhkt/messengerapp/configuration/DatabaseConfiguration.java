package hcmute.hhkt.messengerapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import hcmute.hhkt.messengerapp.util.AuditorAwareImpl;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories({"hcmute.hhkt.messengerapp.repository"})
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class DatabaseConfiguration {

    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
