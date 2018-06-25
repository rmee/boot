package com.github.rmee.boot.database.initializer;

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configures the setup of the database. Goal usually is to bring a database to a state to be able to start
 * executing database migrations with Flyway.
 */
@ConfigurationProperties("datasource.initializer")
public class DataSourceInitializerProperties {

	private String username;

	private String password;

	private String createScript;

	private String dropScript;

	private Properties properties;

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return (this.password == null ? "" : this.password);
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * @return script to execute to setup the schema/database/users. Usually prepares an empty schema and user to proceed with
	 * the Flyway migrations.
	 */
	public String getCreateScript() {
		return createScript;
	}

	public void setCreateScript(String createScript) {
		this.createScript = createScript;
	}

	/**
	 * @return script to execute to drop all schema, users, etc. Used for testing-only (usually).
	 */
	public String getDropScript() {
		return dropScript;
	}

	public void setDropScript(String dropScript) {
		this.dropScript = dropScript;
	}
}
