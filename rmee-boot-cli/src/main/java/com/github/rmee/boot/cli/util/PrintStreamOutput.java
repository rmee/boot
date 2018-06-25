package com.github.rmee.boot.cli.util;

import java.io.PrintStream;

public class PrintStreamOutput implements ApplicationCliOutput {

	private final PrintStream out;


	public PrintStreamOutput(PrintStream out) {
		this.out = out;
	}

	@Override
	public void info(String message) {
		out.println(message);
	}

	public PrintStream out() {
		return out;
	}

	@Override
	public void error(String message) {
		out.println(message);
	}
}
