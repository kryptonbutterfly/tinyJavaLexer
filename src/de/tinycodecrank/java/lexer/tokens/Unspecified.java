package de.tinycodecrank.java.lexer.tokens;

import de.tinycodecrank.java.lexer.Location;

public class Unspecified implements Section<String>
{
	private final String	value;
	private final Location	loc;
	
	public Unspecified(String value, Location loc)
	{
		this.value	= value;
		this.loc	= loc;
	}
	
	public String value()
	{
		return value;
	}
	
	public Location loc()
	{
		return loc;
	}
	
	@Override
	public String toString()
	{
		return value;
	}
	
	@Override
	public String description()
	{
		return "unspecified";
	}
}