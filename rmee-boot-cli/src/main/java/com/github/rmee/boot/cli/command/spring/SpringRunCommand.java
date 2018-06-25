package com.github.rmee.boot.cli.command.spring;

import com.github.rmee.boot.cli.command.ApplicationCommand;
import com.github.rmee.boot.cli.command.ApplicationCommandContext;
import org.springframework.boot.SpringApplication;
import picocli.CommandLine;

@CommandLine.Command(name = "run",
		header = "Runs the application.",
		description = "Runs the application service."
)
public class SpringRunCommand extends ApplicationCommand {

	@Override
	public void execute(ApplicationCommandContext context) {
		SpringApplication app = context.getBean(SpringApplication.class);
		//app.run("--debug");
		app.run();
	}
}
