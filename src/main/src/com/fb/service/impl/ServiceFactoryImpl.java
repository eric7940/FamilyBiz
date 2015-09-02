package com.fb.service.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.fb.service.Service;
import com.fb.service.ServiceFactory;

public class ServiceFactoryImpl implements ApplicationContextAware, ServiceFactory {

	private ApplicationContext context;
	
	public void setApplicationContext(ApplicationContext context) {
		this.context = context;
	}
	
	private Object getProxyBean(String beanName) {
		return context.getBean(beanName + "Proxy");
	}
	
	public Service getService(String serviceName) {
		return (Service) this.getProxyBean(serviceName);
	}
	
	public Service getLocalService(String serviceName) {
		return (Service) this.getProxyBean(serviceName);
	}

}
