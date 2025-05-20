package com.paymybuddy.api.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Profile("hibernate-init")
@Component
public class DataInitializer {
	private final ApplicationContext context;
	
	public DataInitializer(ApplicationContext context) {
		this.context = context;
	}
	
	@EventListener(ApplicationReadyEvent.class)
    public void initDataSetOnApplicationReady() throws Exception {
        context.getBean(InitService.class).initDataSet();
    }	
}
