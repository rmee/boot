package com.github.rmee.boot.database.initializer.cli;

import com.github.rmee.boot.cli.command.ApplicationCommandContext;
import picocli.CommandLine;

@CommandLine.Command(name = "migrate",
		header = "Migrate to new version.",
		description = "Migrates the database the the newest resp. specified version."
)
public class MigrateCommand extends FlywayCommandBase {


	@CommandLine.Option(names = { "-b", "--baseline" }, description = "Baseline version to migrate from")
	String baseline;

	@CommandLine.Option(names = { "-t", "--target" }, description = "Target version to migrate to")
	String target;

	public MigrateCommand(Class configurationClass) {
		super(configurationClass, true, true);
	}

	@Override
	public void prepare(ApplicationCommandContext context) {
		if (target != null) {
			System.setProperty("spring.flyway.targetAsString", target);
		}
		if (baseline != null) {
			System.setProperty("spring.flyway.setBaselineVersionAsString", baseline);
		}
	}

	@Override
	public void doExecute(ApplicationCommandContext context) {
		// nothing to do, ran on startup
	}
}
