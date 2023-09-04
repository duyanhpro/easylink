package easylink;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import easylink.security.AuditorAwareImpl;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing()
public class SpringDataConfig {
	
	/**
	 * Configure auditorAware bean to inject createdDate, createdUser, modifiedUser, modifiedDate when save/update entity
	 * @return
	 */
	@Bean
    public AuditorAware<Integer> auditorAware() {
        return new AuditorAwareImpl();
    }
}
