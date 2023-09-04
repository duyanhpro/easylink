package easylink;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class TestConfiguration {
//
//	@Bean
//	public ConfigParamRepository configParamRepository() {
//		return new ConfigParamRepository();
//	}
////	
//	@Bean
//	public UserDetailsService customUserDetailService() {
//		return new TestUserDetailsService();
//	}
}
