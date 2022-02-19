package telran.numbers.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class CalculatorProxy implements Calculator{
	
	private PrintStream writer;
	private BufferedReader reader;
	public CalculatorProxy(PrintStream writer, BufferedReader reader) {
		this.writer = writer;
		this.reader = reader;
	}
	public double compute(String operator, double op1, double op2) {
		/* V.R.
		 *  writer.println() has to be placed inside try-catch brackets.
		 *  The catch() will get 2 types of exceptions - IOException and NumberFormatException
		 *  (Double.parseDouble)
		 */
		writer.println(String.format("%s#%s#%s", operator, Double.toString(op1),Double.toString(op2)));
		try {
			double responce = Double.parseDouble(reader.readLine());
			return responce;
		} catch (IOException e) {
			throw new RuntimeException(e.toString());
		}
	}
}
