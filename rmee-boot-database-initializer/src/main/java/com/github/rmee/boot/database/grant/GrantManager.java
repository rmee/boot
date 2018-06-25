package com.github.rmee.boot.database.grant;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

public class GrantManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(GrantManager.class);

	private final FlywayProperties flywayProperties;

	private final DataSourceProperties dataSourceProperties;

	public GrantManager(FlywayProperties flywayProperties, DataSourceProperties dataSourceProperties) {
		this.flywayProperties = flywayProperties;
		this.dataSourceProperties = dataSourceProperties;
	}

	public int applyGrants(Flyway flyway) {
		DataSource dataSource = flyway.getDataSource();
		String username = dataSourceProperties.getUsername();
		String ownerName = flywayProperties.getUser();
		String schemaName = ownerName.toUpperCase(); // TODO remo: oracle specific, setup configuration this way?
		String flywayTable = flyway.getTable();
		if (username == null) {
			throw new IllegalStateException("datasource.username is null");
		}
		if (ownerName == null) {
			throw new IllegalStateException("spring.flyway.user is null");
		}

		int n = 0;
		if (!username.equals(ownerName)) {
			// may consider Oracle specific implementation like
			// OracleSchema.getObjectsGroupedByType from Flyway
			// if more is needed
			try (Connection connection = dataSource.getConnection(); Statement s = connection.createStatement()) {
				DatabaseMetaData m = connection.getMetaData();
				LOGGER.debug("granting table access to {} in {}", username, ownerName);
				String[] types = { "TABLE", "VIEW" };
				try (ResultSet tables = m.getTables(connection.getCatalog(), schemaName, null, types)) {
					while (tables.next()) {
						String tableName = tables.getString(3);
						if (tableName.equalsIgnoreCase(flywayTable)) {
							// disallow access to flyway table
							continue;
						}
						String sql = "GRANT SELECT, INSERT, UPDATE, DELETE ON " + tableName + " TO " + username;
						s.execute(sql);
						n++;
					}
				}
			}
			catch (SQLException e) {
				throw new IllegalStateException("failed to grant roles", e);
			}
		}
		return n;
	}
}
