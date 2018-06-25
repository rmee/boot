package com.github.rmee.boot.database.initializer;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FIXME move to flyway-based approach and remove for simple use cases
 */
@Configuration
@ConditionalOnProperty(prefix = "datasource.initializer", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass(DataSourceInitializer.class)
@ConditionalOnMissingBean(DataSourceInitializer.class)
@EnableConfigurationProperties({DataSourceInitializerProperties.class})
@AutoConfigureAfter(FlywayAutoConfiguration.class)
public class DataSourceInitializerAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceInitializerAutoConfiguration.class);

	private final Flyway flyway;

	private final FlywayProperties flywayProperties;

	private DataSourceInitializerProperties properties;

	private DataSource initializerDataSource;

	private DataSourceProperties dataSourceProperties;


	public DataSourceInitializerAutoConfiguration(
			@DataSourceInitializerDataSource ObjectProvider<DataSource> initializerDataSource,
			ObjectProvider<FlywayProperties> flywayProperties,
			DataSourceInitializerProperties properties,
			ObjectProvider<DataSourceProperties> datasourceProperties,
			ObjectProvider<Flyway> flyway) {

		this.flyway = flyway.getIfAvailable();
		this.flywayProperties = flywayProperties.getIfAvailable();
		this.properties = properties;
		this.initializerDataSource = initializerDataSource.getIfAvailable();
		this.dataSourceProperties = datasourceProperties.getIfAvailable();
	}

	@Bean
	public DataSourceInitializer databaseInitializer() {
		DataSource dataSource = initializerDataSource;
		if (dataSource != null) {
			LOGGER.debug("using DataSourceInitializer dataSource");
		}
		else if (properties.getUsername() != null) {
			if (flywayProperties != null && flywayProperties.isCreateDataSource()) {
				LOGGER.debug("using DataSourceInitializer user based on flyway datasource properties");
				dataSource = DataSourceBuilder.create()
						.url(flywayProperties.getUrl())
						.username(properties.getUsername())
						.password(properties.getPassword())
						.build();
			}
			else if (dataSourceProperties != null) {
				LOGGER.debug("using DataSourceInitializer user based on generic datasource properties");
				dataSource = DataSourceBuilder.create()
						.url(dataSourceProperties.getUrl())
						.driverClassName(dataSourceProperties.getDriverClassName())
						.username(properties.getUsername())
						.password(properties.getPassword())
						.build();
			}
			else {
				throw new IllegalStateException("no database properties configured for database initializer");
			}
		}
		else if (flyway != null) {
			LOGGER.debug("using flyway dataSource");
			dataSource = flyway.getDataSource();
		}
		else {
			throw new IllegalStateException("no dataSource available perform setup");
		}

		return new DataSourceInitializer(dataSource, properties);
	}
}
