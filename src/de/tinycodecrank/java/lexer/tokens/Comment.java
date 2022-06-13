package de.tinycodecrank.java.lexer.tokens;

import java.util.function.Function;

import de.tinycodecrank.java.lexer.Creator;
import de.tinycodecrank.java.lexer.EndData;
import de.tinycodecrank.java.lexer.Location;
import de.tinycodecrank.java.lexer.Matcher;
import de.tinycodecrank.java.lexer.StringUtils.IsStartOf;

public class Comment implements Section<String>
{
	private final String		value;
	private final Location		loc;
	private final CommentType	type;
	
	public Comment(String value, Location loc, CommentType type)
	{
		this.value	= value;
		this.loc	= loc;
		this.type	= type;
	}
	
	public String value()
	{
		return value;
	}
	
	public Location loc()
	{
		return loc;
	}
	
	public CommentType type()
	{
		return type;
	}
	
	@Override
	public String toString()
	{
		final var stop = type == CommentType.LineComment ? "" : type.stop("\n");
		return type.start + value + stop;
	}
	
	public static enum CommentType
	{
		LineComment("//", lf -> lf),
		BlockComment("/*", "*/");
		
		private final String					start;
		private final Function<String, String>	stop;
		
		CommentType(String start, Function<String, String> stop)
		{
			this.start	= start;
			this.stop	= stop;
		}
		
		CommentType(String start, String stop)
		{
			this.start	= start;
			this.stop	= _lineSeparator -> stop;
		}
		
		public String stop(String lineSeparator)
		{
			return stop.apply(lineSeparator);
		}
	}
	
	private static Creator<Comment> create(CommentType type)
	{
		final var stop = type == CommentType.LineComment ? "" : type.stop("\n");
		return value -> loc -> new Comment(
			value.substring(type.start.length(), value.length() - stop.length()),
			loc,
			type);
	}
	
	private static IsStartOf start(CommentType type)
	{
		return (target, offset, start) -> target.startsWith(type.start, offset);
	}
	
	private static int endBlock(EndData data)
	{
		final var	stop	= CommentType.BlockComment.stop(data.lineSeparator());
		final var	index	= data.raw().indexOf(stop, data.start());
		if (index == -1)
		{
			System.err.printf("Unclosed comment block at (%s) expected %s", data.loc(), stop);
			return index;
		}
		else
		{
			return index + stop.length();
		}
	}
	
	private static int endLine(EndData data)
	{
		final var	stop	= CommentType.LineComment.stop(data.lineSeparator());
		final var	index	= data.raw().indexOf(stop, data.start());
		if (index == -1)
		{
			return data.raw().length();
		}
		else
		{
			return index;
		}
	}
	
	@Override
	public String description()
	{
		switch (this.type)
		{
			case BlockComment:
				return "block comment";
			case LineComment:
				return "line comment";
			default:
				final var err = String.format("Unknown type %s", this.type);
				throw new IllegalStateException(err);
		}
	}
	
	public static Matcher matcherLineComment()
	{
		return new Matcher(
			"lineComment",
			start(CommentType.LineComment),
			Comment::endLine,
			create(CommentType.LineComment));
	}
	
	public static Matcher matcherBlockComment()
	{
		return new Matcher(
			"blockComment",
			start(CommentType.BlockComment),
			Comment::endBlock,
			create(CommentType.BlockComment));
	}
}