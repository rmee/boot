package com.github.rmee.boot.cli.provider;

public interface BeanProvider {

	<T> T getBean(Class<T> clazz);

	void initialize(Class configurationClass);
}
