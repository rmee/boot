package com.github.rmee.boot.utils.flyway;

import java.sql.Connection;

import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.callback.BaseFlywayCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlywayLogger extends BaseFlywayCallback {

	private final Logger LOGGER = LoggerFactory.getLogger(FlywayLogger.class);

	@Override
	public void beforeClean(Connection connection) {
		LOGGER.info("clean started");
	}

	@Override
	public void afterClean(Connection connection) {
		LOGGER.info("clean finished");
	}

	@Override
	public void beforeMigrate(Connection connection) {
		LOGGER.info("migrations started");
	}

	@Override
	public void afterMigrate(Connection connection) {
		LOGGER.info("migrations finished");
	}

	@Override
	public void beforeEachMigrate(Connection connection, MigrationInfo info) {
		LOGGER.info("migration {} started", info.getScript());
	}

	@Override
	public void afterEachMigrate(Connection connection, MigrationInfo info) {
		LOGGER.info("migration {} finished", info.getScript());
	}

	@Override
	public void beforeBaseline(Connection connection) {
		LOGGER.info("setting baseline");
	}
}
