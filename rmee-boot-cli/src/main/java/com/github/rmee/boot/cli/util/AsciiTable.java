package com.github.rmee.boot.cli.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AsciiTable {

	private final List<Column> columns = new ArrayList<>();

	private final List<Row> data = new ArrayList<>();

	private int maxColumnWidth = Integer.MAX_VALUE;

	private void calculateColumnWidth() {

		for (Column column : columns) {
			column.width = column.name.length() + 1;
		}

		for (Row row : data) {
			int colIdx = 0;
			for (String value : row.values) {
				Column column = columns.get(colIdx);
				if (value == null) {
					continue;
				}

				column.width = Math.max(column.width, value.length() + 1);
				colIdx++;
			}
		}

		for (Column column : columns) {
			column.width = Math.min(column.width, maxColumnWidth);
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		calculateColumnWidth();
		writeColumnNames(columns, sb);
		writeSeparator(columns, sb);

		// values
		writeValues(columns, data, sb);
		return sb.toString();
	}

	private static void writeColumnNames(final List<Column> columns, final StringBuilder sb) {
		for (Column column : columns) {
			String format = "%-" + (column.width) + "s";
			sb.append(String.format(format, column.name));
			sb.append(" ");
		}
		sb.append("\n");
	}

	private static void writeSeparator(final List<Column> columns, final StringBuilder sb) {
		for (Column column : columns) {
			String format = "%-" + (column.width + 1) + "s";
			sb.append(String.format(format, "").replace(' ', '-'));
		}
		sb.append("\n");
	}

	private void writeValues(final List<Column> columns, final List<Row> rows, final StringBuilder sb) {
		for (Row row : rows) {
			int columnIdx = 0;
			for (String value : row.values) {

				if (value != null && value.length() > maxColumnWidth) {
					value = value.substring(0, maxColumnWidth - 1);
				}

				String format = "%-" + columns.get(columnIdx).width + "s";
				sb.append(String.format(format, value));
				sb.append(" ");

				columnIdx++;
			}
			sb.append("\n");
		}
	}

	public List<Column> getColumns() {
		return columns;
	}

	public List<Row> getData() {
		return data;
	}

	public int getMaxColumnWidth() {
		return maxColumnWidth;
	}

	public void setMaxColumnWidth(final int maxColumnWidth) {
		this.maxColumnWidth = maxColumnWidth;
	}

	public void addColumn(String name) {
		this.columns.add(new Column(name));
	}

	public void addRow(Object... values) {
		data.add(new Row(values));
	}

	public static class Column {

		private String name;

		private int width;

		public Column(final String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "Column[" +
					"name='" + name + '\'' +
					", width=" + width +
					']';
		}
	}

	public static class Row {

		private final List<String> values = new ArrayList<>();

		public Row(Object... objects) {
			Arrays.asList(objects).forEach(it -> values.add(it != null ? it.toString() : ""));
		}

		@Override
		public String toString() {
			return "Row[" +
					"values=" + values +
					']';
		}
	}
}
