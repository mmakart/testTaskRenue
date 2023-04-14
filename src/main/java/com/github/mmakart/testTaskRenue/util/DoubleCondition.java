package com.github.mmakart.testTaskRenue.util;

import com.github.mmakart.testTaskRenue.Airport;

public class DoubleCondition extends AbstractCondition {
	private Double value;

	public DoubleCondition(int columnNumber, RelationType relationType, Double value) {
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
			return ((Double) airport.getValue(columnNumber)).compareTo(value) > 0;
		case LESS_THAN:
			return ((Double) airport.getValue(columnNumber)).compareTo(value) < 0;
		}
		return false;
	}

}