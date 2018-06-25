package com.github.rmee.boot.cli.command;

import java.util.Optional;

import com.github.rmee.boot.cli.util.ApplicationCliOutput;

public interface ApplicationCommandContext {

	ApplicationCliOutput getOutput();

	void debug(Optional<String[]> loggerNames);

	<T> T getBean(Class<T> clazz);

	void initialize(Class configurationClass);
}
