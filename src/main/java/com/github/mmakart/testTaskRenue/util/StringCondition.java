package com.github.mmakart.testTaskRenue.util;

import com.github.mmakart.testTaskRenue.Airport;

public class StringCondition extends AbstractCondition {
	private String value;

	public StringCondition(int columnNumber, RelationType relationType, String value) {
		super(columnNumber, relationType);
		this.value = value;
	}

	@Override
	public boolean matches(Airport airport) {
		String airportValue = (String) airport.getValue(columnNumber);
		
		switch (relationType) {
		case EQUALS:
			return airportValue.equalsIgnoreCase(value);
		case NOT_EQUALS:
			return !airportValue.equalsIgnoreCase(value);
		case MORE_THAN:
			return airportValue.compareTo(value) > 0;
		case LESS_THAN:
			return airportValue.compareTo(value) < 0;
		}
		return false;
	}

}