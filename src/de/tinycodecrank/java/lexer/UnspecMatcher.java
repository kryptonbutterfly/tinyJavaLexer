package de.tinycodecrank.java.lexer;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import de.tinycodecrank.java.lexer.tokens.Section;

public class UnspecMatcher
{
	private final Predicate<String>							testValue;
	private final BiFunction<String, Location, Section<?>>	create;
	
	public UnspecMatcher(Predicate<String> testValue, BiFunction<String, Location, Section<?>> create)
	{
		this.testValue	= testValue;
		this.create		= create;
	}
	
	public boolean testValue(String value)
	{
		return testValue.test(value);
	}
	
	public Section<?> create(String value, Location loc)
	{
		return create.apply(value, loc);
	}
}