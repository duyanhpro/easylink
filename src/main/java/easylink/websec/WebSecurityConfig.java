package easylink.websec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)		// to enable @Secured
																				//to enable @PreAuthorize & @PostAuthorize
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	// Web Security

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new SpringPasswordEncoder();
	}
	
	@Bean
	public WebAuthenticationSuccessHandler successHandler() {
		return new WebAuthenticationSuccessHandler();
	}
	
	@Bean
	public WebAuthenticationFailureHandler failureHandler() {
		return new WebAuthenticationFailureHandler();
	}
	
	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		authProvider.setHideUserNotFoundExceptions(false);
		return authProvider;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}

	// For global security (ignore resources, set debug mode, reject requests by implementing a custom fire-wall definition)
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web
	        .ignoring()
	        .antMatchers("/assets/**", "/css/**", "/fonts/**", "/img/**", "/js/**", "/plugins/**", "/*.html"
	        		, "/v2/**", "/swagger-resources/**", "/webjars/**"		// swagger
	        		, "/api/**");	// Ignore spring security to use custom token authentication for API
	}

	// For resource level security
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
			.antMatchers("/login").permitAll()		// will break down permission/role in each controller
				.antMatchers("/api/login").permitAll()
			.anyRequest().fullyAuthenticated()
//		.and()
//			.exceptionHandling()
//			.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
		.and()
			.formLogin()
			.loginPage("/login")
			.permitAll()
			.usernameParameter("username")
			.passwordParameter("password")
			.successHandler(successHandler())
			.failureHandler(failureHandler())
		.and()
			.logout().logoutUrl("/logout")
			.permitAll()
			.logoutSuccessUrl("/login?logout")		// normally redirect to /login?logout, but can redirect to other page
		.and()
			.csrf()
			.disable()
		;
	} 

}
