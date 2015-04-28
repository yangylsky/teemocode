package com.teemocode.commons.component.async;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SendEventInterceptor {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 切入点：com.teemocode.product包下的所有public方法执行(测试用)
	 */
	@Pointcut("execution(public * com.teemocode.web..action..*.*(..))")
	private void allAction() {
	}

	/**
	 * 切入点：com.teemocode包下的所有带SendEvent注解的public方法执行
	 */
	@Pointcut("execution(@com.teemocode.commons.component.async.SendEvent * *(..))")
	private void allSendEvent() {
	}

	@AfterReturning(value = "allAction()", returning = "result")
	public void afterAllAction(JoinPoint jp, Object result) {
		String className = jp.getThis().toString();
		String methodName = jp.getSignature().getName();
		logger.error("allAction():" + className + " invoke " + methodName + " end. result: " + result);
	}

	@AfterReturning(value = "allSendEvent()", returning = "result")
	public void afterAllSendEvent(JoinPoint jp, Object result) {
		String className = jp.getThis().toString();
		String methodName = jp.getSignature().getName();
		logger.error("afterAllSendEvent():" + className + " invoke " + methodName + " end. result: " + result);
	}
}
