package kryptonbutterfly.java.lexer.tokens;

import static kryptonbutterfly.math.utils.range.Range.*;

import kryptonbutterfly.java.lexer.Location;

public class Name implements Section<String>
{
	public static final String	DESCRIPTION	= "identifier";
	private final String		value;
	private final Location		loc;
	
	public Name(String value, Location loc)
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
	
	public static boolean valid(String value)
	{
		if (!value.isEmpty())
		{
			final char first = value.charAt(0);
			if (Character.isJavaIdentifierStart(first))
			{
				for (int i : range(1, value.length()))
				{
					if (!Character.isJavaIdentifierPart(value.charAt(i)))
					{
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String description()
	{
		return DESCRIPTION;
	}
}