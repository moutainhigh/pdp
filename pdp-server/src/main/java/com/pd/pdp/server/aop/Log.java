package com.pd.pdp.server.aop;

import java.lang.annotation.*;

/**
 * ClassName: Log
 * @Description: TODO  
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

	public String value();
}
