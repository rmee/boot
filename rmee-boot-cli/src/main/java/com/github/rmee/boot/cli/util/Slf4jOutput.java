package com.github.rmee.boot.cli.util;

import org.slf4j.Logger;

public class Slf4jOutput implements ApplicationCliOutput {

	private final Logger logger;


	public Slf4jOutput(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void info(String message) {
		logger.info(message);
	}


	@Override
	public void error(String message) {
		logger.error(message);
	}
}
