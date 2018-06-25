package com.github.rmee.boot.cli.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringBeanProvider implements BeanProvider {

	private SpringApplication application;

	private ConfigurableApplicationContext context;

	public SpringBeanProvider(SpringApplication application) {
		this.application = application;
	}

	@Override
	public <T> T getBean(Class<T> clazz) {
		if (clazz.isInstance(application)) {
			return (T) application;
		}

		return context.getBean(clazz);
	}

	@Override
	public void initialize(Class configurationClass) {
		if (context != null) {
			throw new IllegalStateException("already configured");
		}

		application = new SpringApplication(configurationClass);
		application.setWebEnvironment(false);
		context = application.run();
	}
}
