package com.github.mmakart.testTaskRenue.util;

public class LexicalAnalyzerException extends RuntimeException {

	private static final long serialVersionUID = -2674520972380856547L;

	public LexicalAnalyzerException() {
		super();
	}

	public LexicalAnalyzerException(String message) {
		super(message);
	}

	public LexicalAnalyzerException(Throwable cause) {
		super(cause);
	}

	public LexicalAnalyzerException(String message, Throwable cause) {
		super(message, cause);
	}

	public LexicalAnalyzerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
