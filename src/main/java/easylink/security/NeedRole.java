package easylink.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to declare required roles for class or method
 * Use is permitted if having any of the declared roles (at least one role)
 * @author phamd
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface NeedRole {
	/**
	 * List of required roles: User only need one of those roles to access class/method
	 * @return
	 */
	String[] value();
}
