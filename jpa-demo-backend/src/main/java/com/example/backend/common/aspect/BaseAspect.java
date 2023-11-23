package com.example.backend.common.aspect;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class BaseAspect {
	static final String LOG_BREAKER_OPEN = "**********************************************************************";
	static final String LOG_BREAKER_CLOSE = "############################## xxxxxxxx ##############################";
	static final String LOG_PREFIX = "----------  ";
	static final String LOG_SUFFIX = "  ----------";

	private static final Logger applicationLogger = LogManager.getLogger("applicationLogs." + BaseAspect.class.getName());

	@Pointcut("execution(public * *(..))")
	void publicMethod() {
	}

	Method getMethod(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		return signature.getMethod();
	}

	RequestMapping getRootMapper(JoinPoint joinPoint) {
		return joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
	}

	String getClassName(JoinPoint joinPoint) {
		return joinPoint.getTarget().getClass().getSimpleName();
	}

	String getMethodName(JoinPoint joinPoint) {
		return joinPoint.getSignature().getName();
	}

	MethodMappingInfo getMethodMappingInfo(Method method) {
		MethodMappingInfo mapping = new MethodMappingInfo();
		// set default as GET
		mapping.setRequestMethod(RequestMethod.GET.name());
		mapping.setMethodMappingUrl("");

		Annotation[] annotations = method.getAnnotations();

		for (Annotation annotation : annotations) {
			if (annotation instanceof GetMapping) {
				GetMapping autualMapper = (GetMapping) annotation;
				if (autualMapper.value().length > 0) {
					mapping.setMethodMappingUrl(autualMapper.value()[0]);
				}
				// set the request method
				mapping.setRequestMethod(RequestMethod.GET.name());
				break;
			}
			if (annotation instanceof PostMapping) {
				PostMapping autualMapper = (PostMapping) annotation;
				if (autualMapper.value().length > 0) {
					mapping.setMethodMappingUrl(autualMapper.value()[0]);
				}
				// set the request method
				mapping.setRequestMethod(RequestMethod.POST.name());
				break;
			}
			if (annotation instanceof PutMapping) {
				PutMapping autualMapper = (PutMapping) annotation;
				if (autualMapper.value().length > 0) {
					mapping.setMethodMappingUrl(autualMapper.value()[0]);
				}
				// set the request method
				mapping.setRequestMethod(RequestMethod.PUT.name());
				break;
			}
			if (annotation instanceof DeleteMapping) {
				DeleteMapping autualMapper = (DeleteMapping) annotation;
				if (autualMapper.value().length > 0) {
					mapping.setMethodMappingUrl(autualMapper.value()[0]);
				}
				// set the request method
				mapping.setRequestMethod(RequestMethod.DELETE.name());
				break;
			}
		}
		return mapping;
	}

	void logRequestParams(Object[] arguments) {
		if (applicationLogger.isDebugEnabled()) {
			applicationLogger.debug("==================== Request parameters ===========================");
			if (arguments.length > 0) {
				for (Object arg : arguments) {
					if (arg != null) {
						applicationLogger.debug(LOG_PREFIX + arg + LOG_SUFFIX);
					}
				}
			}
			else {
				applicationLogger.debug(LOG_PREFIX + "[EMPTY Request parameters]" + LOG_SUFFIX);
			}
			applicationLogger.debug("===================================================================");
		}
	}

	@Getter
	@Setter
	static class MethodMappingInfo {
		private String methodMappingUrl;
		private String requestMethod;
	}
}
