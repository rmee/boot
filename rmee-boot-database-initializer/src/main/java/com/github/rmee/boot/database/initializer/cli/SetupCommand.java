package com.github.rmee.boot.database.initializer.cli;

import com.github.rmee.boot.cli.command.ApplicationCommandContext;
import com.github.rmee.boot.database.initializer.DataSourceInitializer;
import picocli.CommandLine;

@CommandLine.Command(name = "setup",
		header = "Setups the database.",
		description = "Runs the setup script to setup the schema."
)
public class SetupCommand extends FlywayCommandBase {


	public SetupCommand(Class configurationClass) {
		super(configurationClass, false, false);
	}

	@Override
	public void doExecute(ApplicationCommandContext context) {
		DataSourceInitializer initializer = context.getBean(DataSourceInitializer.class);

		initializer.executeCreateScript();
		context.getOutput().info("database setup completed");
	}
}
