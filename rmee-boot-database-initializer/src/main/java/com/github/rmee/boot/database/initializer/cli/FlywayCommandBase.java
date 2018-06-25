package com.github.rmee.boot.database.initializer.cli;

import com.github.rmee.boot.cli.command.ApplicationCommand;
import com.github.rmee.boot.cli.command.ApplicationCommandContext;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

abstract class FlywayCommandBase extends ApplicationCommand {

	private final Class configurationClass;

	private final boolean setupFlyway;

	private final boolean runMigrations;

	public FlywayCommandBase(Class configurationClass, boolean setupFlyway, boolean runMigrations) {
		super();
		this.configurationClass = configurationClass;
		this.setupFlyway = setupFlyway;
		this.runMigrations = runMigrations;
	}

	@Override
	protected final void execute(ApplicationCommandContext context) {
		if (setupFlyway) {
			System.setProperty("flyway.enabled", "true");
			System.setProperty("spring.flyway.enabled", "true");
		}
		else {
			System.setProperty("spring.flyway.enabled", "false");
			System.setProperty("flyway.enabled", "false");
			System.setProperty("spring.autoconfigure.exclude", DataSourceAutoConfiguration.class.getName() + "," +
					FlywayAutoConfiguration.class.getName() + "," + HibernateJpaAutoConfiguration.class.getName());
		}

		System.setProperty("flyway.migrate-on-start", Boolean.toString(runMigrations));
		System.setProperty("spring.flyway.migrate-on-start", Boolean.toString(runMigrations));

		prepare(context);
		context.initialize(configurationClass);
		doExecute(context);
	}

	protected void prepare(ApplicationCommandContext context) {
	}

	protected abstract void doExecute(ApplicationCommandContext context);
}
