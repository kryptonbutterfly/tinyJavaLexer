package de.tinycodecrank.java.lexer.tokens;

import de.tinycodecrank.java.lexer.Creator;
import de.tinycodecrank.java.lexer.EndData;
import de.tinycodecrank.java.lexer.Location;
import de.tinycodecrank.java.lexer.Matcher;

public class BooleanLiteral implements Section<Boolean>
{
	public static final String DESCRIPTION = "boolean literal";
	
	private static final String	TRUE	= "true";
	private static final String	FALSE	= "false";
	
	private final boolean	value;
	private final Location	loc;
	
	private BooleanLiteral(boolean value, Location loc)
	{
		this.value	= value;
		this.loc	= loc;
	}
	
	private static Creator<BooleanLiteral> create()
	{
		return value ->
		{
			return loc -> new BooleanLiteral(Boolean.parseBoolean(value), loc);
		};
	}
	
	@Override
	public Boolean value()
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
		return value ? TRUE : FALSE;
	}
	
	private static boolean start(String target, int offset, int start)
	{
		return target.startsWith(TRUE, offset) || target.startsWith(FALSE, offset);
	}
	
	private static int end(EndData data)
	{
		if (data.raw().startsWith(TRUE, data.start()))
		{
			return data.start() + TRUE.length();
		}
		else if (data.raw().startsWith(FALSE, data.start()))
		{
			return data.start() + FALSE.length();
		}
		else
		{
			return -1;
		}
	}
	
	@Override
	public String description()
	{
		return Boolean.toString(value);
	}
	
	public static Matcher matcher()
	{
		return new Matcher("bool", BooleanLiteral::start, BooleanLiteral::end, create());
	}
}