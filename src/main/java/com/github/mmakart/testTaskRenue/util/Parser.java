package com.github.mmakart.testTaskRenue.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Parser {
	private static final char REAL_NUMBER_DELIMITER = '.';
	
	private static enum State {
		INSIDE_STRING, OUTSIDE_STRING
	}

	private List<Lexeme> parseToLexemes(String s) {
		if (s.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<Lexeme> lexemes = new ArrayList<>();
		
		State state = State.OUTSIDE_STRING;
		
		for (int pos = 0; pos < s.length(); ) {
			char c = s.charAt(pos);
			
			if (state == State.INSIDE_STRING) {
				StringBuilder sb = new StringBuilder();
				while (c != '\'') {
					sb.append(c);
					pos++;
					if (pos >= s.length()) {
						break;
					}
					c = s.charAt(pos);
				}
				
				lexemes.add(new Lexeme(LexemeType.STRING, sb.toString()));
			}
			
			switch (c) {
			case ' ':
				pos++;
				continue;
			case '(':
				lexemes.add(new Lexeme(LexemeType.LEFT_BRACKET, c));
				pos++;
				continue;
			case ')':
				lexemes.add(new Lexeme(LexemeType.RIGHT_BRACKET, c));
				pos++;
				continue;
			case '[':
				lexemes.add(new Lexeme(LexemeType.LEFT_SQUARE_BRACKET, c));
				pos++;
				continue;
			case ']':
				lexemes.add(new Lexeme(LexemeType.RIGHT_SQUARE_BRACKET, c));
				pos++;
				continue;
			case '\'':
				lexemes.add(new Lexeme(LexemeType.SINGLE_QUOTE, c));
				if (state == State.OUTSIDE_STRING) {
					state = State.INSIDE_STRING;
				} else if (state == State.INSIDE_STRING) {
					state = State.OUTSIDE_STRING;
				}
				pos++;
				continue;
			case '&':
				lexemes.add(new Lexeme(LexemeType.AND, c));
				pos++;
				continue;
			case '>':
				lexemes.add(new Lexeme(LexemeType.MORE_THAN, c));
				pos++;
				continue;
			case '=':
				lexemes.add(new Lexeme(LexemeType.EQUALS, c));
				pos++;
				continue;
			default:
				if (c == '|') {
					pos++;
					if (pos >= s.length() || s.charAt(pos) != '|') {
						throw new LexicalAnalyzerException("Expected '||'");
					}
					lexemes.add(new Lexeme(LexemeType.OR, "||"));
					pos++;
					continue;
				} else if (c == '<') {
					pos++;
					if (pos >= s.length() || s.charAt(pos) != '>') {
						lexemes.add(new Lexeme(LexemeType.LESS_THAN, c));
						continue;
					}
					
					c = s.charAt(pos);
					if (c == '>') {
						lexemes.add(new Lexeme(LexemeType.NOT_EQUALS, "<>"));
						pos++;
						continue;
					}
				} else if (Character.isDigit(c) || c == '-') {
					boolean withPoint = false;
					boolean withMinus = c == '-';
					StringBuilder sb = new StringBuilder();
					do {
						if (c == REAL_NUMBER_DELIMITER && !withPoint) {
							withPoint = true;
						} else if (c == REAL_NUMBER_DELIMITER && withPoint) {
							throw new LexicalAnalyzerException(
									"Real number has more than one '.'");
						}
						sb.append(c);
						pos++;
						if (pos >= s.length()) {
							break;
						}
						c = s.charAt(pos);
						if (c == '-' && withMinus) {
							throw new LexicalAnalyzerException(
									"Number has unexpected '-'");
						}
					} while (Character.isDigit(c) || c == REAL_NUMBER_DELIMITER);
					
					lexemes.add(new Lexeme(LexemeType.NUMBER, sb.toString()));
				} else if (Character.isLetter(c)) {
					StringBuilder sb = new StringBuilder();
					do {
						sb.append(c);
						pos++;
						if (pos >= s.length()) {
							break;
						}
						c = s.charAt(pos);
					} while (Character.isLetter(c));
					
					lexemes.add(new Lexeme(LexemeType.STRING, sb.toString()));
				} else {
					throw new LexicalAnalyzerException("Unexpected character: " + c);
				}
			}
		}
		lexemes.add(new Lexeme(LexemeType.EOF, ""));
		
		return lexemes;
	}

	// Parsing rules
	// expression: disjunction* EOF
	// disjunction: conjunction ('||' conjunction)*
	// conjunction: condition ('&' condition)*
	// condition: 'column' '[' INTEGER ']' ('<'|'>'|'='|'<>') ('\'' STRING '\'' | (INTEGER|REAL) ) | '(' expression ')'
	
	private Expression parseCondition(LexemeBuffer lexemes) {
		Lexeme lexeme = lexemes.next();
		switch (lexeme.getType()) {
		case STRING:
			if (!lexeme.getValue().equalsIgnoreCase("column")) {
				throw new SyntaxAnalizerException("Unexpected token: " +
						lexeme.getValue());
			}
			
			lexeme = lexemes.next();
			if (lexeme.getType() != LexemeType.LEFT_SQUARE_BRACKET) {
				throw new SyntaxAnalizerException("Unexpected token: " +
						lexeme.getValue());
			}
			
			lexeme = lexemes.next();
			if (lexeme.getType() != LexemeType.NUMBER
					|| lexeme.getType() == LexemeType.NUMBER
					&& lexeme.getValue().contains(".")) {
				throw new SyntaxAnalizerException("Unexpected token: " +
						lexeme.getValue());
			}
			int colNum = Integer.parseInt(lexeme.getValue());
			
			lexeme = lexemes.next();
			if (lexeme.getType() != LexemeType.RIGHT_SQUARE_BRACKET) {
				throw new SyntaxAnalizerException("Unexpected token: " +
						lexeme.getValue());
			}
			
			lexeme = lexemes.next();
			RelationType relationType;
			switch (lexeme.getType()) {
			case EQUALS:
				relationType = RelationType.EQUALS;
				break;
			case NOT_EQUALS:
				relationType = RelationType.NOT_EQUALS;
				break;
			case MORE_THAN:
				relationType = RelationType.MORE_THAN;
				break;
			case LESS_THAN:
				relationType = RelationType.LESS_THAN;
				break;
			default:
				throw new SyntaxAnalizerException("Unexpected token: " +
						lexeme.getValue());
			}
			
			lexeme = lexemes.next();
			// All of them must implement Comparable
			String strValue = null;
			Double doubleValue = null;
			Integer intValue = null;
			
			switch (lexeme.getType()) {
			case SINGLE_QUOTE:
				lexeme = lexemes.next();
				strValue = lexeme.getValue();
				lexeme = lexemes.next();
				if (lexeme.getType() != LexemeType.SINGLE_QUOTE) {
					throw new SyntaxAnalizerException("Unexpected token: " +
							lexeme.getValue());
				}
				return new StringCondition(colNum, relationType, strValue);
			case NUMBER:
				if (lexeme.getValue().contains(".")) {
					doubleValue = Double.parseDouble(lexeme.getValue());
					return new DoubleCondition(colNum, relationType, doubleValue);
				} else {
					intValue = Integer.parseInt(lexeme.getValue());
					return new IntegerCondition(colNum, relationType, intValue);
				}
			default:
				throw new SyntaxAnalizerException("Unexpected token: " +
						lexeme.getValue());
				
			}
		case LEFT_BRACKET:
			Expression subexpression = parseDisjunction(lexemes);
			lexeme = lexemes.next();
			if (lexeme.getType() != LexemeType.RIGHT_BRACKET) {
				throw new SyntaxAnalizerException("Unexpected token: "
						+ lexeme.getValue());
			}
			return subexpression;
		default:
			throw new SyntaxAnalizerException("Unexpected token: " +
					lexeme.getValue());
		}
	}
	
	private Expression parseConjunction(LexemeBuffer lexemes) {
		
		MultiExpression result = new Conjunction();
		result.addSubexpression(parseCondition(lexemes));
		
		while (true) {
			Lexeme lexeme = lexemes.next();
			
			switch (lexeme.getType()) {
			case AND:
				result.addSubexpression(parseCondition(lexemes));
				break;
			case EOF:
			case RIGHT_BRACKET:
			case OR:
				lexemes.back();
				return (Expression) result;
			default:
				throw new SyntaxAnalizerException("Unexpected token: "  +
						lexeme.getValue());
			}
		}
	}
	
	private Expression parseDisjunction(LexemeBuffer lexemes) {
		
		MultiExpression result = new Disjunction();
		result.addSubexpression(parseConjunction(lexemes));
		
		while (true) {
			Lexeme lexeme = lexemes.next();
			switch (lexeme.getType()) {
			case OR:
				result.addSubexpression(parseConjunction(lexemes));
				break;
			case EOF:
			case RIGHT_BRACKET:
				lexemes.back();
				return (Expression) result;
			default:
				throw new SyntaxAnalizerException("Unexpected token: " +
						lexeme.getValue());
			}
		}
	}
	
	private Expression parseExpression(LexemeBuffer lexemes) {
		Lexeme lexeme = lexemes.next();
		if (lexeme.getType() == LexemeType.EOF) {
			return null;
		} else {
			lexemes.back();
			return parseDisjunction(lexemes);
		}
	}
	
	public Expression parseExpression(String s) {
		if (s.isEmpty()) {
			return (airport) -> true;
		}
		
		LexemeBuffer lexemes = new LexemeBuffer(parseToLexemes(s));
		return parseExpression(lexemes);
	}

}