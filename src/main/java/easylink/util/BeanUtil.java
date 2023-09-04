package easylink.util;

import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Simple utility class to load bean into non-spring class
 * @author Pham Duy Anh
 *
 */

@Service
public class BeanUtil implements ApplicationContextAware {
	
    private static ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * Return a spring bean by class
     * @param beanClass
     * @return
     */
    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }
    
	/**
	 * Copy/overwrite all non-null properties from update into obj
	 * @param obj
	 * @param update
	 */
	public static void merge(Object obj, Object update){
		
		if (obj == null) {
			obj = update;
			return;
		}
		if (update == null)
			return;
		
	    if(!obj.getClass().isAssignableFrom(update.getClass())){
	        return;
	    }

	    Method[] methods = obj.getClass().getMethods();

	    for(Method fromMethod: methods){
	        if(fromMethod.getDeclaringClass().equals(obj.getClass())
	                && fromMethod.getName().startsWith("get")){

	            String fromName = fromMethod.getName();
	            String toName = fromName.replace("get", "set");

	            try {
	                Method toMetod = obj.getClass().getMethod(toName, fromMethod.getReturnType());
	                Object value = fromMethod.invoke(update, (Object[])null);
	                if(value != null){
	                    toMetod.invoke(obj, value);
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            } 
	        }
	    }
	}
}