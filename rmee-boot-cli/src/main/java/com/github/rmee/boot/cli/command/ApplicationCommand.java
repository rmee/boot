package com.github.rmee.boot.cli.command;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.github.rmee.boot.cli.util.ApplicationCliOutput;
import picocli.CommandLine;

public abstract class ApplicationCommand {

	@CommandLine.Option(names = {"-h", "--help"}, help = true, description = "Prints help message and exits")
	protected boolean help = false;

	@CommandLine.Option(names = {"-d", "--debug"}, split = ",", description = "Shows debug information during execution.")
	protected String[] debug = null;

	public void run(ApplicationCommandContext context) {
		if (debug != null) {
			context.debug(Optional.ofNullable(debug));
		}
		if (help) {
			showHelp(context);
		}
		else {
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
