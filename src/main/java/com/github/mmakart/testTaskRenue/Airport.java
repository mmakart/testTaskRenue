package com.github.mmakart.testTaskRenue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Airport {
	
	private static final int SIZE = 14;
	private Object[] values = new Object[SIZE];
	
	private static final List<Class<?>> types = List.of(
			Integer.class, String.class, String.class,
			String.class, String.class, String.class,
			Double.class, Double.class, Integer.class,
			String.class, String.class, String.class,
			String.class, String.class);

	public Airport(String s) {
		String[] splitted = split(s);
		
		if (splitted.length != SIZE) {
			throw new RuntimeException("Airport string has not 14 comma separated values");
		}

		for (int i = 0; i < SIZE; i++) {
			if (types.get(i) == Integer.class) {
				values[i] = Integer.valueOf(splitted[i]);
			} else if (types.get(i) == Double.class) {
				values[i] = Double.valueOf(splitted[i]);
			} else {
				values[i] = splitted[i];
			}
		}
	}
	
	public Airport(Object... values) {
		this.values = values;
	}
	
	private static enum State {
		INSIDE_STRING, OUTSIDE_STRING
	}
	
	private static final char QUOTE = '"';
	private static final char DELIMITER = ',';
	
	private String[] split(String s) {
		
		State state = State.OUTSIDE_STRING;
		List<String> splitted = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		
		int pos = 0;
		while (pos < s.length()) {
			char c = s.charAt(pos);
			
			if (c == QUOTE && state == State.OUTSIDE_STRING) {
				state = State.INSIDE_STRING;
			} else if (c == QUOTE && state == State.INSIDE_STRING) {
				state = State.OUTSIDE_STRING;
			} else if (c == DELIMITER && state == State.OUTSIDE_STRING) {
				splitted.add(sb.toString());
				sb.setLength(0);
			} else {
				sb.append(c);
			}
			
			pos++;
		}
		
		while (pos < s.length()) {
			char c = s.charAt(pos);
			sb.append(c);
		}
		splitted.add(sb.toString());
		
		return splitted.toArray(String[]::new);
	}
	
	// index = 1, 2, 3, ... , 14
	public Object getValue(int colNum) {
		return types.get(colNum - 1).cast(values[colNum - 1]);
	}
	
	public static Class<?> getType(int colNum) {
		return types.get(colNum - 1);
	}

	public Object[] getValues() {
		return values;
	}

	@Override
	public String toString() {
		return "Airport " + Arrays.toString(values);
	}
	
}