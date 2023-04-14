package com.github.mmakart.testTaskRenue.util;

import java.util.ArrayList;
import java.util.List;

import com.github.mmakart.testTaskRenue.Airport;

public class Disjunction implements Expression, MultiExpression {
	private List<Expression> subexpressions = new ArrayList<>();
	
	@Override
	public boolean matches(Airport airport) {
		boolean result = false;
		for (Expression subexpression : subexpressions) {
			result |= subexpression.matches(airport);
			if (result) {
				return result;
			}
		}
		return result;
	}
	
	@Override
	public void addSubexpression(Expression subexpression) {
		subexpressions.add(subexpression);
	}

	@Override
	public String toString() {
		return "Disjunction " + subexpressions;
	}
	
}