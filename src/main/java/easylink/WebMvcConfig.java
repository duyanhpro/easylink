package easylink;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import easylink.security.ApiAuthenticationInterceptor;
import easylink.security.AuthorizationInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Autowired
	ThymeleafLayoutInterceptor	thymeleafLayoutInterceptor;

	@Autowired
	ApiAuthenticationInterceptor apiAuthenticationInterceptor;

	@Autowired
	AuthorizationInterceptor authorizationInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		// add Interceptor to handle layout
		registry.addInterceptor(thymeleafLayoutInterceptor).excludePathPatterns("/api/**");
		
		// Add interceptor for Locale change.  Example:   http://xxx/abc?lang=vn
		registry.addInterceptor(localeChangeInterceptor()).excludePathPatterns("/api/**");

		registry.addInterceptor(apiAuthenticationInterceptor)
				.addPathPatterns("/api/**")
				.excludePathPatterns("/api/login");
//
		// Interceptor for Authorization
		registry.addInterceptor(authorizationInterceptor)
			//.addPathPatterns("/**")
			.excludePathPatterns("/login","/logout","/error", "/api/login"
					,"/assets/**","/css/**","/img/**","/js/**", "/fonts/**","/plugins/**");
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("swagger-ui.html")
	      .addResourceLocations("classpath:/META-INF/resources/");
	 
	    registry.addResourceHandler("/webjars/**")
	      .addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
	
	@Bean
	public LocaleResolver localeResolver() {
		// The default implementation is AcceptHeaderLocaleResolver which use HTTP header of request to select language
		// SessionLocaleResolver allows setting locale 
	    SessionLocaleResolver slr = new SessionLocaleResolver();
	    //slr.setDefaultLocale(Locale.US);
	    slr.setDefaultLocale(new Locale("vn", "Vietnam"));
	    return slr;
	}
	
	/*
	 * MessageResource for translation are auto-provisioned by Spring boot
	 * Location for translation file is specified by spring.messages.basename
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
	    LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
	    lci.setParamName("lang");
	    return lci;
	}


}
