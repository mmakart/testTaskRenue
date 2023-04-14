package com.github.mmakart.testTaskRenue.util;

@FunctionalInterface
public interface MultiExpression {
	void addSubexpression(Expression subexpression);
}