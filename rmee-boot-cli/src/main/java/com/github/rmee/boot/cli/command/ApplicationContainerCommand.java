package com.github.rmee.boot.cli.command;


import java.io.PrintStream;

import picocli.CommandLine;

public abstract class ApplicationContainerCommand extends ApplicationCommand {

	protected CommandLine commandLine;

	protected ApplicationContainerCommand() {
		commandLine = new CommandLine(this);
	}

	public CommandLine getCommandLine() {
		return commandLine;
	}

	@Override
	protected void printHelp(ApplicationCommandContext context, PrintStream ps) {
		commandLine.usage(ps);
	}

	@Override
	public void execute(ApplicationCommandContext context) {
		showHelp(context);
	}

	public void addCommand(String name, Object command) {
		if (command instanceof ApplicationContainerCommand) {
			commandLine.addSubcommand(name, ((ApplicationContainerCommand) command).commandLine);
		}
		else {
			commandLine.addSubcommand(name, command);
		}
	}
}
