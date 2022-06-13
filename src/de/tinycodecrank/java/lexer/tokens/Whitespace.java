package de.tinycodecrank.java.lexer.tokens;

import static de.tinycodecrank.math.utils.range.Range.*;

import de.tinycodecrank.java.lexer.Creator;
import de.tinycodecrank.java.lexer.EndData;
import de.tinycodecrank.java.lexer.Location;
import de.tinycodecrank.java.lexer.Matcher;
import de.tinycodecrank.java.lexer.StringUtils;
import de.tinycodecrank.java.lexer.StringUtils.IsStartOf;

public class Whitespace implements Section<String>
{
	private final String	value;
	private final Location	loc;
	
	private Whitespace(String value, Location loc)
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
	
	private static Creator<Whitespace> create()
	{
		return value -> loc -> new Whitespace(value, loc);
	}
	
	@Override
	public String toString()
	{
		final var sb = new StringBuilder();
		for (char c : value.toCharArray())
		{
			sb.append(StringUtils.toVisibleControll(c));
		}
		return sb.toString();
	}
	
	private static IsStartOf start()
	{
		return (target, offset, start) -> isValidAt(target, offset, "\n");
	}
	
	private static int end(EndData data)
	{
		for (int i : range(data.start(), data.raw().length()))
		{
			if (!isValidAt(data.raw(), i, data.lineSeparator()))
			{
				return i;
			}
		}
		return data.raw().length();
	}
	
	private static boolean isValidAt(String text, int index, String lineSeparator)
	{
		char charAt = text.charAt(index);
		return Character.isWhitespace(charAt) && !text.startsWith(lineSeparator, index);
	}
	
	@Override
	public String description()
	{
		return "whitespace";
	}
	
	public static Matcher matcher()
	{
		return new Matcher("whitespace", start(), Whitespace::end, create());
	}
}