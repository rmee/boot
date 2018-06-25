package com.github.rmee.boot.database.initializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;
import javax.sql.DataSource;

import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceInitializer {

	private static final String CLASSPATH_PREFIX = "classpath:";

	public static final String FILESYSTEM_PREFIX = "filesystem:";

	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceInitializer.class);

	private final DataSourceInitializerProperties properties;

	private final DataSource dataSource;

	public DataSourceInitializer(DataSource dataSource, DataSourceInitializerProperties properties) {
		this.dataSource = dataSource;
		this.properties = properties;
	}

	public void executeCreateScript() {
		String scriptName = properties.getCreateScript();
		if (scriptName == null) {
			throw new IllegalStateException("no create script provided in configuration");
		}
		executeScript(scriptName);
	}

	public void executeDropScript() {
		String scriptName = properties.getDropScript();
		if (scriptName == null) {
			throw new IllegalStateException("no drop script provided in configuration");
		}
		executeScript(scriptName);
	}

	private void executeScript(String scriptName) {
		try (Connection connection = dataSource.getConnection();
				InputStream setupScriptStream = getScript(scriptName);
				Scanner setupScriptScanner = new Scanner(setupScriptStream).useDelimiter("\\A")) {

			String script = setupScriptScanner.hasNext() ? setupScriptScanner.next() : "";
			Properties templateProperties = properties.getProperties();
			StrSubstitutor templater = new StrSubstitutor(templateProperties);
			script = templater.replace(script);
			executeSql(connection, script, "\\n/");
		}
		catch (SQLException e) {
			throw new IllegalStateException("error executing the SQL script '" + scriptName + "'", e);
		}
		catch (IOException e) {
			throw new IllegalStateException("Error reading script '" + scriptName + "'", e);
		}
	}

	public static void executeSql(
			Connection connection, String sqlScript, String statementSeparator) throws SQLException {
		try (Statement stmt = connection.createStatement()) {
			String[] sqls = sqlScript.split(statementSeparator);
			String sql = "";

			try {
				for (int i = 0; i < sqls.length; i++) {
					sql = sqls[i].trim();
					LOGGER.debug("executing {}", sql);
					stmt.executeUpdate(sql);
				}
			}
			catch (SQLException e) {
				throw new SQLException("Failed to execute '" + sql + "'", e);
			}
		}
	}

	private InputStream getScript(String scriptName) {
		if (scriptName == null) {
			throw new IllegalStateException("scriptName must not be null");
		}

		if (scriptName.startsWith(CLASSPATH_PREFIX)) {
			String resourceName = scriptName.substring(CLASSPATH_PREFIX.length());
			InputStream in = getClass().getClassLoader().getResourceAsStream(resourceName);
			if (in == null) {
				throw new IllegalStateException("script " + scriptName + " not found");
			}
			return in;
		}
		else if (scriptName.startsWith(FILESYSTEM_PREFIX)) {
			File file = new File(scriptName.substring(FILESYSTEM_PREFIX.length()));
			if (!file.exists()) {
				throw new IllegalStateException("script " + scriptName + " not found");
			}
			try {
				return new FileInputStream(file);
			}
			catch (FileNotFoundException e) {
				throw new IllegalStateException(e);
			}
		}
		throw new IllegalStateException(
				"location '" + scriptName + "' must start with " + CLASSPATH_PREFIX + " or " + FILESYSTEM_PREFIX);

	}
}
