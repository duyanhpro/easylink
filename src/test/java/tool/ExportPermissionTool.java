package tool;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import easylink.security.NeedPermission;
import easylink.service.RolePermissionService;

@SpringBootApplication(exclude={org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration.class} )
@ComponentScan({"com.myapp","quickj"})
public class ExportPermissionTool {

	public static void main(String[] args) {
		SpringApplication.run(ExportPermissionTool.class, args);
	}

	@Autowired
	ApplicationContext applicationContext ;

	@Autowired
	RolePermissionService rpService;

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		System.out.println("hello world, I have just started up");
		// Generate default global permission
		rpService.insertPermission("*", "*", null, null, true);

		// Find list of controller
		Map<RequestMappingInfo, HandlerMethod> a = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();

		for (Map.Entry<RequestMappingInfo, HandlerMethod> entry: a.entrySet()) {
			System.out.println("Saving permission for " + entry.getKey().getPatternsCondition());
			HandlerMethod hm=(HandlerMethod)entry.getValue();

			if (hm.hasMethodAnnotation(NeedPermission.class)) {
				String permissionString =  hm.getMethodAnnotation(NeedPermission.class).value();
				System.out.print(permissionString);

				String [] p = permissionString.split(":");
				rpService.insertPermission(p[0], "*", null, null, true);
				if (p[1].indexOf(",")>0) {
					String as[] = p[1].split(",");
					for (String action: as) {
						try {
							rpService.insertPermission(p[0], action, null, null, true);
						} finally {}
					}
				} else {
					try {
						rpService.insertPermission(p[0], p[1], null, null, true);
					} finally {}
				}
			}
			System.out.println();
		}
	}


}
