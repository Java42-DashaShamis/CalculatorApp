package telran.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InputOutput {
	String readString(String prompt);
	void writeObject(Object obj);
	
	default void writeObjectLine(Object obj) {
		writeObject(obj.toString() + "\n");
	}
	
	default <R> R readObject(String prompt, String errorMessage, Function<String,R> mapper ) {
		
		while(true) {
			String string = readString(prompt);
			try {
				R result = mapper.apply(string);
				return result;
			} catch (Exception e) {
				writeObjectLine(errorMessage);
			}
		}
		
	}
	
	default String readStringPredicate(String prompt, String errorMessage, Predicate<String> predicate) {
		
		return readObject(prompt, errorMessage, str -> {
			if(predicate.test(str)) {
				return str;
			}
			throw new IllegalArgumentException();
		});
	}

	String errorNumber = "It is not a number";
	String errorRange = "The number is not in a certain range";
	String errorOptions = "There is no such option";
	String errorLocalDate = "It is wrong format of date";
	
	default Double readDouble(String prompt) {
		return readObject(prompt, errorNumber, Double::parseDouble);
	}
	
	default Integer readInt(String prompt) {
		return readObject(prompt, errorNumber ,str -> Integer.parseInt(str));
	}
	
	default Integer readInt(String prompt, int min, int max) {
		return checkRange(prompt, min, max);
	}
	private Integer checkRange(String prompt, int min, int max) {
		return readObject(prompt, String.format("No number in the range [%d-%d]",min,max), str -> {
			int res = Integer.parseInt(str);
			if (res < min || res > max) {
				throw new IllegalArgumentException();
			}
			return res;
		});
	}
	
	default Long readLong(String prompt) {
		return readObject(prompt, errorNumber ,str -> Long.parseLong(str));
	}
	
	default String readStringOption(String prompt, Set<String>options) {
		return readStringPredicate(prompt, errorOptions, options::contains);
	}
	
	default LocalDate readDate(String prompt) {
		return readObject(prompt, errorLocalDate, LocalDate::parse);
	}
	
	default LocalDate readDate(String prompt, DateTimeFormatter formatter) {
		return readObject(prompt, errorLocalDate, str -> LocalDate.parse(str, formatter));
	}
	
}
