package com.github.rmee.boot.utils.systemd;

import info.faljse.SDNotify.SDNotify;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnExpression("#{systemEnvironment['NOTIFY_SOCKET'] != null}")
@ConditionalOnClass(SDNotify.class)
public class SystemdAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SystemdHandler sytemdHandler() {
		return new SystemdHandler();
	}
}
