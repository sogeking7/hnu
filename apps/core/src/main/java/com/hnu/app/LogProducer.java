package com.hnu.app;

import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogProducer {
	@Produces
	public Logger produce(InjectionPoint injectionPoint) {
		return LogManager.getLogger(injectionPoint.getBean().getBeanClass());
	}
}
