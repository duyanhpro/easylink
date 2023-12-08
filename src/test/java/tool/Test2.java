package tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

import easylink.service.ConfigParamService;

@SpringBootApplication(exclude={org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration.class} )
public class Test2 {

	public static void main(String[] args) {
		SpringApplication.run(Test2.class, args);
	}

	@Autowired
	ApplicationContext applicationContext ;

	@Autowired
	ConfigParamService configService;
	
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
//		System.out.println(configService.findLastSyncDate());
//		configService.updatePricePerHour(234l);
//		System.out.println(configService.findPricePerHour());
	}


}
