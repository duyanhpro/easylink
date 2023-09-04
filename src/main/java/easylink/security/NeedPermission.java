package easylink.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to declare permission authorization for controller method
 * Usage:  @NeedPermission("resource:action")
 * Example:   @NeedPermission("agent:create"), @NeedPermission("agent"), @NeedPermission("agent:create,view")
 * @author phamd
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface NeedPermission {
	String value();
}
