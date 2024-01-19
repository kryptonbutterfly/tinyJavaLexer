package kryptonbutterfly.java.lexer.tokens;

import kryptonbutterfly.java.lexer.Keywords;
import kryptonbutterfly.java.lexer.Location;

public class Keyword implements Section<Keywords>
{
	private final Keywords	value;
	private final Location	loc;
	
	public Keyword(Keywords value, Location loc)
	{
		this.value	= value;
		this.loc	= loc;
	}
	
	public Keywords value()
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
		return value.value;
	}
	
	@Override
	public String description()
	{
		return value.value;
	}
}