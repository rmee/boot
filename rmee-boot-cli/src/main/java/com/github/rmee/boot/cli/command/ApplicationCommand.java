package com.github.rmee.boot.cli.command;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import com.github.rmee.boot.cli.util.ApplicationCliOutput;
import picocli.CommandLine;

public abstract class ApplicationCommand {

	@CommandLine.Option(names = { "-h", "--help" }, help = true, description = "Prints help message and exits")
	protected boolean help = false;

	@CommandLine.Option(names = { "-d", "--debug" }, split = ",", description = "Shows debug information during execution.")
	protected String[] debug = null;

	@CommandLine.Option(names = { "-D", "--property" }, paramLabel = "KEY=VALUE")
	private Map<String, String> properties = new LinkedHashMap<>();

	@CommandLine.Spec
	private CommandLine.Model.CommandSpec spec;

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> map) {
		for (String key : map.keySet()) {
			String newValue = map.get(key);
			validateUnique(key, newValue);
			properties.put(key, newValue);
		}
	}

	private void validateUnique(String key, String newValue) {
		String existing = properties.get(key);
		if (existing != null && !existing.equals(newValue)) {
			throw new CommandLine.ParameterException(spec.commandLine(),
					String.format("Duplicate key '%s' for values '%s' and '%s'.", key, existing, newValue));
		}
	}

	public void run(ApplicationCommandContext context) {
		if (debug != null) {
			context.debug(Optional.ofNullable(debug));
		}
		if (help) {
			showHelp(context);
		}
		else {
			for (Map.Entry<String, String> property : properties.entrySet()) {
				System.setProperty(property.getKey(), property.getValue());
			}

			execute(context);
		}
	}

	protected void showHelp(ApplicationCommandContext context) {
		ApplicationCliOutput output = context.getOutput();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			PrintStream ps = new PrintStream(baos, true, "utf-8");
			printHelp(context, ps);
			String content = new String(baos.toByteArray(), StandardCharsets.UTF_8);
			output.info(content);
		}
		catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	protected abstract void execute(ApplicationCommandContext context);

	protected void printHelp(ApplicationCommandContext context, PrintStream ps) { // NOSONAR context param must be in place
		CommandLine.usage(this, ps);
	}
}
