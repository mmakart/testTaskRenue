package com.github.mmakart.testTaskRenue.util;

import com.github.mmakart.testTaskRenue.Airport;

public abstract class AbstractCondition implements Expression {
	protected int columnNumber;
	protected RelationType relationType;
	
	public AbstractCondition(int columnNumber, RelationType relationType) {
		this.columnNumber = columnNumber;
		this.relationType = relationType;
	}

	@Override
	public abstract boolean matches(Airport airport);

}
