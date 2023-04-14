package com.github.mmakart.testTaskRenue.util;

import java.util.List;
import java.util.NoSuchElementException;

public class LexemeBuffer {
	private List<Lexeme> lexemes;
	private int pos = 0;
	
	public LexemeBuffer(List<Lexeme> lexemes) {
		this.lexemes = lexemes;
	}

	public Lexeme next() {
		if (pos >= lexemes.size()) {
			throw new NoSuchElementException();
		}
		return lexemes.get(pos++);
	}
	
	public void back() {
		pos--;
	}
	
	public int getPos() {
		return pos;
	}
	
}