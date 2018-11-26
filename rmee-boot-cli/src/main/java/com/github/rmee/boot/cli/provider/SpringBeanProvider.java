package com.github.rmee.boot.cli.provider;


import com.github.rmee.boot.cli.CliException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindException;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.boot.context.properties.bind.validation.BindValidationException;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

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

		try {
			application = new SpringApplication(configurationClass);
			application.setWebApplicationType(WebApplicationType.NONE);
			context = application.run();
		}
		catch (UnsatisfiedDependencyException e) {
			// give message message in cause of configuration errrors
			if (e.getCause() instanceof ConfigurationPropertiesBindException) {
				Throwable bindException = e.getCause();
				if (bindException.getCause() instanceof BindException) {
					if (bindException.getCause().getCause() instanceof BindValidationException) {
						BindValidationException validationException =
								(BindValidationException) bindException.getCause().getCause();
						ValidationErrors validationErrors = validationException.getValidationErrors();

						StringBuilder builder = new StringBuilder();
						builder.append("validation of configuration properties failed:");
						for (ObjectError error : validationErrors.getAllErrors()) {
							builder.append("\n- ");
							builder.append(error.getObjectName());
							if (error instanceof FieldError) {
								builder.append(".");
								builder.append(((FieldError) error).getField());
								builder.append("=");
								Object rejectedValue = ((FieldError) error).getRejectedValue();
								if (rejectedValue != null) {
									builder.append("null");
								}
								else {
									builder.append(rejectedValue);
								}
							}
							builder.append(" ");
							builder.append(error.getDefaultMessage());
						}

						throw new CliException(builder.toString());
					}
				}
			}
			throw e;
		}
	}
}
