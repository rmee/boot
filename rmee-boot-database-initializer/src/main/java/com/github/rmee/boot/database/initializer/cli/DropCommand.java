package com.github.rmee.boot.database.initializer.cli;

import com.github.rmee.boot.cli.command.ApplicationCommandContext;
import com.github.rmee.boot.database.initializer.DataSourceInitializer;
import picocli.CommandLine;

@CommandLine.Command(name = "drop",
		header = "Drops all application objects.",
		description = "Runs the configured drop script to remove all application-related database objects."
)
public class DropCommand extends FlywayCommandBase {

	public DropCommand(Class configurationClass) {
		super(configurationClass, false, false);
	}

	@Override
	public void doExecute(ApplicationCommandContext context) {
		DataSourceInitializer initializer = context.getBean(DataSourceInitializer.class);
		initializer.executeDropScript();
		context.getOutput().info("all application-related database objects dropped");
	}
}
