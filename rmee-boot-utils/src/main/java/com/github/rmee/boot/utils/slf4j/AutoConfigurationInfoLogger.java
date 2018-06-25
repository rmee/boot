package com.github.rmee.boot.utils.slf4j;

import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

/**
 * A bit more compact flavor than the Spring debug report. Can be used to directly copy/past into a class to set it up manually.
 */
public class AutoConfigurationInfoLogger implements ApplicationListener<WebServerInitializedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AutoConfigurationInfoLogger.class);

	@Autowired
	private ConditionEvaluationReport autoConfigurationReport;

	@Override
	public void onApplicationEvent(final WebServerInitializedEvent event) {
		Map<String, ConditionEvaluationReport.ConditionAndOutcomes> conditionAndOutcomesBySource =
				autoConfigurationReport.getConditionAndOutcomesBySource();


		String strClasses = conditionAndOutcomesBySource.entrySet().stream()
				.filter(it -> it.getValue().isFullMatch())
				.map(it -> it.getKey() + "" + ".class")
				.filter(it -> !it.contains("$") && !it.contains("#"))
				.collect(Collectors.joining(",\n\t"));

		StringBuilder report = new StringBuilder();
		report.append("@Import({");
		report.append(strClasses);
		report.append("})");
		LOGGER.debug("AutoConfiguration in use:\n{}", report);
	}
}
