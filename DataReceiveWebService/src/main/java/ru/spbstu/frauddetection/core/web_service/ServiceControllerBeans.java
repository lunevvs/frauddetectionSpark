package ru.spbstu.frauddetection.core.web_service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceControllerBeans {
	@Bean
	public SpringKafkaProducer initProducer() {
		return new SpringKafkaProducer();
	}
}
