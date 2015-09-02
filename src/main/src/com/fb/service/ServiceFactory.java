package com.fb.service;

public interface ServiceFactory {

	public Service getService(String serviceName);

	public Service getLocalService(String serviceName);
	
}
