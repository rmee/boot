package com.github.rmee.boot.utils.slf4j;

import java.io.File;
import java.util.Comparator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

public class EnvironmentInfoLogger implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

	private final Logger LOGGER = LoggerFactory.getLogger(EnvironmentInfoLogger.class);

	@Override
	public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
		LOGGER.debug("working dir: {}", new File(".").getAbsolutePath());
		System.getenv().entrySet().stream()
				.sorted(Comparator.comparing(Map.Entry::getKey))
				.forEach(it ->
				{
					String key = it.getKey();
					if (key.toLowerCase().contains("pass")) {
						LOGGER.debug("using env: {}=********", it.getKey());
						LOGGER.trace("using env: {}", it);
					}
					else {
						LOGGER.debug("using env: {}", it);
					}
				});
	}
}
