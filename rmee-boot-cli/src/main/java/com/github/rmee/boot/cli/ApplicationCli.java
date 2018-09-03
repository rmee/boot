package com.github.rmee.boot.cli;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ch.qos.logback.classic.Level;
import com.github.rmee.boot.cli.command.ApplicationCommand;
import com.github.rmee.boot.cli.command.ApplicationCommandContext;
import com.github.rmee.boot.cli.command.ApplicationContainerCommand;
import com.github.rmee.boot.cli.provider.BeanProvider;
import com.github.rmee.boot.cli.util.ApplicationCliOutput;
import com.github.rmee.boot.cli.util.Slf4jOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

public class ApplicationCli {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationCli.class);

	private final Package mainPackage;

	private ApplicationCliOutput output;

	private ApplicationContainerCommand mainCommand;

	private BeanProvider beanProvider;


	public ApplicationCli(Package mainPackage) {
		this(mainPackage, mainPackage.getName() + ".cli");
	}

	public ApplicationCli(Package mainPackage, String loggerName) {
		this.mainPackage = mainPackage;
		this.output = new Slf4jOutput(LoggerFactory.getLogger(loggerName)); // NOSONAR System.out by default, no logger for this
	}

	public BeanProvider getBeanProvider() {
		return beanProvider;
	}

	public void setBeanProvider(BeanProvider beanProvider) {
		this.beanProvider = beanProvider;
	}

	public void setMainCommand(ApplicationContainerCommand mainCommand) {
		this.mainCommand = mainCommand;
	}

	public void setOutput(ApplicationCliOutput output) {
		this.output = output;
	}

	public void run(String cmd) {
		String[] args = Arrays.asList(cmd.split("\\s+"))
				.stream()
				.map(String::trim)
				.filter(it -> !it.isEmpty())
				.collect(Collectors.toList())
				.toArray(new String[0]);
		run(args);
	}

	public void run(String[] args) {
		LOGGER.debug("execute {}", Arrays.toString(args));

		try {
			CommandLine commandLine = mainCommand.getCommandLine();
			List<CommandLine> parsed = commandLine.parse(args);
			ApplicationCommand command = parsed.get(parsed.size() - 1).getCommand();
			command.run(new DefaultApplicationCommandContext());
		}
		catch (CliException e) {
			output.error(e.getMessage());
			LOGGER.debug("failed to execute " + Arrays.toString(args), e);
			System.exit(1);
		}
		catch (CommandLine.MissingParameterException e) {
			output.error(e.getMessage() + " in command '" + Arrays.toString(args) + "'");
			LOGGER.debug("failed to execute " + Arrays.toString(args), e);
			System.exit(1);
		}
		catch (Exception e) {
			if (e.getClass().getSimpleName().equals("SilentExitException")) {
				// ok
				LOGGER.debug("restart triggered", e);
			}
			else {
				output.error(e.getMessage() + " in command '" + Arrays.toString(args) + "'");
				LOGGER.error("failed to execute " + Arrays.toString(args), e);
				System.exit(1);
			}
		}
	}

	private class DefaultApplicationCommandContext implements ApplicationCommandContext {


		@Override
		public ApplicationCliOutput getOutput() {
			return output;
		}

		@Override
		public void debug(Optional<String[]> loggerNames) {
			if (loggerNames.isPresent()) {
				// by default enable debug for application
				ch.qos.logback.classic.Logger dbLogger = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory
						.getLogger(mainPackage.getName());
				dbLogger.setLevel(Level.DEBUG);
			}
			else {
				// enable debug for specified packages
				for (String loggerName : loggerNames.get()) {
					ch.qos.logback.classic.Logger dbLogger = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory
							.getLogger(loggerName);
					dbLogger.setLevel(Level.DEBUG);
				}
			}
		}

		@Override
		public <T> T getBean(Class<T> clazz) {
			return beanProvider.getBean(clazz);
		}

		@Override
		public void initialize(Class configurationClass) {
			beanProvider.initialize(configurationClass);
		}
	}
}
