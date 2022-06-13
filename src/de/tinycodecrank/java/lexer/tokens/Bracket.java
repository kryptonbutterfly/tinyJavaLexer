package de.tinycodecrank.java.lexer.tokens;

import java.util.Objects;
import java.util.function.ToIntFunction;

import de.tinycodecrank.java.lexer.BracketDef;
import de.tinycodecrank.java.lexer.BracketDef.BracketType;
import de.tinycodecrank.java.lexer.Creator;
import de.tinycodecrank.java.lexer.EndData;
import de.tinycodecrank.java.lexer.Location;
import de.tinycodecrank.java.lexer.Matcher;
import de.tinycodecrank.java.lexer.StringUtils.IsStartOf;

public class Bracket extends Group<BracketType>
{
	private final BracketType	value;
	private final Location		loc;
	
	private Bracket(BracketType value, Location loc)
	{
		this.value	= value;
		this.loc	= loc;
	}
	
	public BracketType value()
	{
		return value;
	}
	
	public Location loc()
	{
		return loc;
	}
	
	private static Creator<Bracket> create(BracketType type)
	{
		return _value -> loc -> new Bracket(type, loc);
	}
	
	@Override
	public String toString()
	{
		final var sb = new StringBuilder();
		sb.append(value.opening.value());
		for (final var e : content())
		{
			sb.append(e.toString());
		}
		sb.append(value.closing.value());
		return sb.toString();
	}
	
	private static IsStartOf start(BracketDef type)
	{
		return (target, offset, start) -> target.startsWith(type.value(), offset);
	}
	
	private static ToIntFunction<EndData> end(BracketDef type)
	{
		return data -> data.start() + type.value().length();
	}
	
	@Override
	public String description()
	{
		return value.opening.value();
	}
	
	public static Matcher matchOpening(BracketType type)
	{
		return new Matcher(type.opening.value(), start(type.opening), end(type.opening), create(type));
	}
	
	public static Matcher matchClosing(BracketType type)
	{
		return new Matcher(type.closing.value(), start(type.closing), end(type.closing), type.closing);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(value);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof Bracket other)
			return value == other.value;
		return false;
	}
}