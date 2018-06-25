package com.github.rmee.boot.database.initializer.cli;

import com.github.rmee.boot.cli.command.ApplicationCommandContext;
import org.flywaydb.core.Flyway;
import picocli.CommandLine;

@CommandLine.Command(name = "repair",
		header = "Repairs failed migrations.",
		description = "Removes any failed migrations. Realign the checksums, descriptions and types of the applied migrations "
				+ "with the ones of the available migrations."
)
public class RepairCommand extends FlywayCommandBase {

	public RepairCommand(Class configurationClass) {
		super(configurationClass, true, false);
	}

	@Override
	public void doExecute(ApplicationCommandContext context) {
		Flyway flyway = context.getBean(Flyway.class);
		flyway.repair();
	}
}
