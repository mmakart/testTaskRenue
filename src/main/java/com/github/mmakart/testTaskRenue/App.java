package com.github.mmakart.testTaskRenue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

import com.github.mmakart.testTaskRenue.util.Expression;
import com.github.mmakart.testTaskRenue.util.LexicalAnalyzerException;
import com.github.mmakart.testTaskRenue.util.Parser;
import com.github.mmakart.testTaskRenue.util.SyntaxAnalizerException;

public class App {
	private static final int COL_WITH_AIRPORT_NAME = 2;

	public static void main(String[] args) {
		Parser parser = new Parser();

		try (Scanner in = new Scanner(Path.of("airports.csv"), StandardCharsets.UTF_8);
				Scanner stdin = new Scanner(System.in)) {

			System.out.print("Enter filter: ");
			String filterString = stdin.nextLine();
			Expression expression = parser.parseExpression(filterString);

			AirportsBuffer airportsBuffer = new AirportsBuffer();
			while (in.hasNextLine()) {
				Airport airport = new Airport(in.nextLine());
				if (expression.matches(airport)) {
					airportsBuffer.addAirport(airport);
				}
			}
			airportsBuffer.store();
			
			String input;
			while (true) {
				
				System.out.print("Enter start of airport name ('!quit' to exit) ");
				input = stdin.nextLine();
				if (input.equalsIgnoreCase("!quit")) {
					System.exit(0);
				}
				
				long start = System.currentTimeMillis();
				
				int counter = 0;
				while (airportsBuffer.hasNextAirport()) {

					Airport airport = airportsBuffer.nextAirport();
					String airportName = (String) airport.getValue(COL_WITH_AIRPORT_NAME);
					if (input.length() <= airportName.length()) {
						String substring = airportName.substring(0, input.length());

						if (substring.equalsIgnoreCase(input)) {
							System.out.printf("\"%s\"%s%n", airportName, Arrays.toString(airport.getValues()));
							counter++;
						}
					}

				}
				long time = System.currentTimeMillis() - start;
				
				System.out.printf("Airports found: %d%n", counter);
				System.out.printf("Search time: %d ms%n", time);
				
				airportsBuffer.rewind();
			}

		} catch (IOException ex) {
			System.err.println("Error: " + ex.getLocalizedMessage());
		} catch (LexicalAnalyzerException | SyntaxAnalizerException ex) {
			System.err.println("Error in input: " + ex.getLocalizedMessage());
		}
	}
}