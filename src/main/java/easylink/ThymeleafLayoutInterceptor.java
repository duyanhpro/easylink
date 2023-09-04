package easylink;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ThymeleafLayoutInterceptor extends HandlerInterceptorAdapter {
	
	static class LayoutConfig {
		Map<String, String> layoutMap = new HashMap<>();
		String viewAttribute = "RESULT_PAGE";
		String defaultLayout =  "layout/default";
		public Map<String, String> getLayoutMap() {
			return layoutMap;
		}
		public void setLayoutMap(Map<String, String> layoutMap) {
			this.layoutMap = layoutMap;
		}
		public String getViewAttribute() {
			return viewAttribute;
		}
		public void setViewAttribute(String viewAttribute) {
			this.viewAttribute = viewAttribute;
		}
		public String getDefaultLayout() {
			return defaultLayout;
		}
		public void setDefaultLayout(String defaultLayout) {
			this.defaultLayout = defaultLayout;
		}
		
	}
	
    static Logger log = LoggerFactory.getLogger(ThymeleafLayoutInterceptor.class);
    LayoutConfig layoutConfig;
    static String layoutConfigFile = "classpath:thymeleaf-layout.json";
    
    private ResourceLoader resourceLoader;
    
    @Autowired(required = true)
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;

		try {
			InputStream in =  this.resourceLoader.getResource(layoutConfigFile).getInputStream();
	    	
	    	String str = null;

	    	ByteArrayOutputStream result = new ByteArrayOutputStream();
	    	byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = in.read(buffer)) != -1) {
	    	    result.write(buffer, 0, length);
	    	}
	    	str = result.toString("UTF-8");
	    	result.close();
	    	ObjectMapper mapper = new ObjectMapper();

	    	layoutConfig = mapper.readValue(str, LayoutConfig.class);
		}
		catch (Exception ex) {
			throw new RuntimeException("Can not load layout config file");
		}
	}

    protected String chooseLayout(String controllerResult) {
    	String layout = layoutConfig.getLayoutMap().get(controllerResult);
        if (layout==null)
        	layout = layoutConfig.getDefaultLayout();
        return layout;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, 
    		Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView == null || !modelAndView.hasView()) {
            return;
        }
        String originalViewName = modelAndView.getViewName();
        if (originalViewName.startsWith("redirect:") || (originalViewName.startsWith("forward:")))
        	return;
        
        String layout = chooseLayout(originalViewName);
        
        modelAndView.setViewName(layout);
        log.trace(originalViewName + " --> Going to layout {}", layout);
        
        modelAndView.addObject(layoutConfig.getViewAttribute(), originalViewName);
    }
}
