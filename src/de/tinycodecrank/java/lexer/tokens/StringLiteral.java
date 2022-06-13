package de.tinycodecrank.java.lexer.tokens;

import de.tinycodecrank.java.lexer.Creator;
import de.tinycodecrank.java.lexer.EndData;
import de.tinycodecrank.java.lexer.Location;
import de.tinycodecrank.java.lexer.Matcher;
import de.tinycodecrank.math.utils.Min;

public class StringLiteral implements Section<String>
{
	public static final String	DESCRIPTION		= "string literal";
	public static final String	delimiterStart	= "\"";
	public static final String	delimiterEnd	= "\"";
	
	private final String	value;
	private final Location	loc;
	
	private StringLiteral(String value, Location loc)
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
	
	private static Creator<StringLiteral> create()
	{
		return value -> loc -> new StringLiteral(cutValue(value), loc);
	}
	
	private static String cutValue(String in)
	{
		int end = in.length() - 1;
		return in.substring(1, end);
	}
	
	@Override
	public String toString()
	{
		return delimiterStart + value + delimiterEnd;
	}
	
	private static boolean start(String target, int offset, int start)
	{
		return target.startsWith(delimiterStart, offset);
	}
	
	private static int end(EndData data)
	{
		String		msg		= "Unclosed string literal at: %s\n";
		final var	ESCAPE	= "\\";
		
		int	escaped	= -1;
		int	endChar	= data.start() + 1;
		int	offset	= data.start() + 1;
		while (escaped < endChar)
		{
			if (offset >= data.raw().length())
			{
				System.err.printf(msg, data.loc());
				return -1;
			}
			escaped	= data.raw().indexOf(ESCAPE, offset);
			endChar	= data.raw().indexOf(delimiterEnd, offset);
			if (endChar == -1)
			{
				System.err.printf(msg, data.loc());
				return -1;
			}
			if (escaped == -1)
			{
				return endChar + delimiterEnd.length();
			}
			offset = Min.min(escaped + ESCAPE.length() + 1, endChar + delimiterEnd.length());
		}
		return endChar + delimiterEnd.length();
	}
	
	@Override
	public String description()
	{
		return DESCRIPTION;
	}
	
	public static Matcher matcher()
	{
		return new Matcher("string", StringLiteral::start, StringLiteral::end, create());
	}
}