package com.github.rmee.boot.database.initializer.cli;

import com.github.rmee.boot.cli.command.ApplicationContainerCommand;
import picocli.CommandLine;

@CommandLine.Command(name = "", sortOptions = false)
public class DataSourceMainCommand extends ApplicationContainerCommand {


	public DataSourceMainCommand(Class configurationClass) {
		super();
		addCommand("setup", new SetupCommand(configurationClass));
		addCommand("drop", new DropCommand(configurationClass));
		addCommand("status", new StatusCommand(configurationClass));
		addCommand("migrate", new MigrateCommand(configurationClass));
		addCommand("repair", new RepairCommand(configurationClass));
		addCommand("grantRoles", new GrantRolesCommand(configurationClass));
	}
}
