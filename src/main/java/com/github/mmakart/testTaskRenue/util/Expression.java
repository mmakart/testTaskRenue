package com.github.mmakart.testTaskRenue.util;

import com.github.mmakart.testTaskRenue.Airport;

@FunctionalInterface
public interface Expression {
	boolean matches(Airport airport);
}