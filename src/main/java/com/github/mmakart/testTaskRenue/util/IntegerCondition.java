package com.github.mmakart.testTaskRenue.util;

import com.github.mmakart.testTaskRenue.Airport;

public class IntegerCondition extends AbstractCondition {
	private Integer value;

	public IntegerCondition(int columnNumber, RelationType relationType, Integer value) {
		super(columnNumber, relationType);
		this.value = value;
	}

	@Override
	public boolean matches(Airport airport) {
		switch (relationType) {
		case EQUALS:
			return airport.getValue(columnNumber).equals(value);
		case NOT_EQUALS:
			return !airport.getValue(columnNumber).equals(value);
		case MORE_THAN:
			return ((Integer) airport.getValue(columnNumber)).compareTo(value) > 0;
		case LESS_THAN:
			return ((Integer) airport.getValue(columnNumber)).compareTo(value) < 0;
		}
		return false;
	}

}