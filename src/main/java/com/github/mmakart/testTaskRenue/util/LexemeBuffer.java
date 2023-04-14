package com.github.mmakart.testTaskRenue.util;

import java.util.List;

public class LexemeBuffer {
	private List<Lexeme> lexemes;
	private int pos = 0;
	
	public LexemeBuffer(List<Lexeme> lexemes) {
		this.lexemes = lexemes;
	}

	public Lexeme next() {
		return lexemes.get(pos++);
	}
	
	public void back() {
		pos--;
	}
	
	public int getPos() {
		return pos;
	}
	
}