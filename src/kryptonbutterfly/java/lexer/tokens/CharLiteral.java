package kryptonbutterfly.java.lexer.tokens;

import kryptonbutterfly.java.lexer.Creator;
import kryptonbutterfly.java.lexer.EndData;
import kryptonbutterfly.java.lexer.Location;
import kryptonbutterfly.java.lexer.Matcher;
import kryptonbutterfly.java.lexer.StringUtils;

public class CharLiteral implements Section<Character>
{
	public static final String DESCRIPTION = "char literal";
	
	private final char		value;
	private final Location	loc;
	
	private CharLiteral(char value, Location loc)
	{
		this.value	= value;
		this.loc	= loc;
	}
	
	public Location loc()
	{
		return loc;
	}
	
	private static Creator<CharLiteral> create()
	{
		return value ->
		{
			return loc -> new CharLiteral(StringUtils.unescape(value.substring(1, value.length() - 1)), loc);
		};
	}
	
	@Override
	public Character value()
	{
		return value;
	}
	
	@Override
	public String toString()
	{
		return "'" + StringUtils.escape(value) + "'";
	}
	
	private static boolean start(String target, int offset, int start)
	{
		return target.charAt(offset) == '\'';
	}
	
	private static int end(EndData data)
	{
		final var msg = "Unclosed char literal at: %s\n";
		if (data.raw().length() < data.start() + 3)
		{
			System.err.printf(msg, data.loc());
			return -1;
		}
		int offset = 0;
		if (data.raw().charAt(data.start()) == '\\')
		{
			offset = 1;
		}
		if (data.raw().length() < data.start() + offset + 3)
		{
			System.err.printf(msg, data.loc());
			return -1;
		}
		final var charAt = data.raw().charAt(data.start() + offset + 2);
		if (charAt == '\'')
		{
			return data.start() + 3 + offset;
		}
		else
		{
			System.err.printf("Expected \"'\" but found %s instead at: %s\n", charAt, data.loc());
			return -1;
		}
	}
	
	@Override
	public String description()
	{
		return DESCRIPTION;
	}
	
	public static Matcher matcher()
	{
		return new Matcher("char", CharLiteral::start, CharLiteral::end, create());
	}
}