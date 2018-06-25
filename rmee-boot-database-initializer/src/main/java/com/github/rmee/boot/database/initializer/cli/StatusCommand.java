package com.github.rmee.boot.database.initializer.cli;


import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.github.rmee.boot.cli.command.ApplicationCommandContext;
import com.github.rmee.boot.cli.util.ApplicationCliOutput;
import com.github.rmee.boot.cli.util.AsciiTable;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import picocli.CommandLine;

@CommandLine.Command(name = "status",
		header = "Status of migrations.",
		description = "Shows the status of the Flyway migrations."
)
public class StatusCommand extends FlywayCommandBase {


	public StatusCommand(Class configurationClass) {
		super(configurationClass, true, false);
	}

	@Override
	public void doExecute(ApplicationCommandContext context) {
		Flyway flyway = context.getBean(Flyway.class);

		ApplicationCliOutput output = context.getOutput();
		MigrationInfoService info = flyway.info();

		MigrationInfo[] migrationInfos = info.all();
		AsciiTable table = new AsciiTable();
		table.addColumn("Version");
		table.addColumn("Name");
		table.addColumn("State");
		table.addColumn("Installed on");
		for (MigrationInfo migrationInfo : migrationInfos) {
			String installed = "";
			if (migrationInfo.getInstalledOn() != null) {
				DateFormat format = SimpleDateFormat.getDateTimeInstance();
				installed = format.format(migrationInfo.getInstalledOn());
			}

			table.addRow(
					migrationInfo.getVersion().toString(),
					migrationInfo.getScript(),
					migrationInfo.getState(),
					installed
			);
		}
		output.info("Migration status:\n" + table.toString());
	}
}
