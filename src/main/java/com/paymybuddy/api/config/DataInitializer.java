package com.paymybuddy.api.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Profile("hibernate-init")
@Component
public class DataInitializer {

    private final InitService initService;
	
	public DataInitializer(InitService initService) {
		this.initService = initService;
	}
	
	@EventListener(ApplicationReadyEvent.class)
    public void initDataSetOnApplicationReady() throws Exception {
        initService.initDataSet();
    }	
}
