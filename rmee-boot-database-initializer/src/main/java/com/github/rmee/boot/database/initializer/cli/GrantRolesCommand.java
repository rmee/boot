package com.github.rmee.boot.database.initializer.cli;

import com.github.rmee.boot.cli.command.ApplicationCommandContext;
import com.github.rmee.boot.cli.util.ApplicationCliOutput;
import com.github.rmee.boot.database.grant.GrantManager;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "grantRoles",
		header = "Grants necessary roles to users.",
		description = "Grants table access to application database user."
)
public class GrantRolesCommand extends FlywayCommandBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(GrantRolesCommand.class);

	public GrantRolesCommand(Class configurationClass) {
		super(configurationClass, true, false);
	}

	@Override
	public void doExecute(ApplicationCommandContext context) {
		Flyway flyway = context.getBean(Flyway.class);
		GrantManager grant = context.getBean(GrantManager.class);
		int n = grant.applyGrants(flyway);

		ApplicationCliOutput output = context.getOutput();
		if (n > 0) {
			output.info("granted access to " + n + " tables and views");
		}
		else {
			output.error("no grants created");
		}
	}
}
