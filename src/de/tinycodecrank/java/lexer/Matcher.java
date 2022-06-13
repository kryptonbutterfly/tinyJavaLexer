package de.tinycodecrank.java.lexer;

import java.util.function.ToIntFunction;

import de.tinycodecrank.java.lexer.StringUtils.IsStartOf;
import de.tinycodecrank.java.lexer.tokens.Section;
import de.tinycodecrank.monads.Either;

public record Matcher(
	String content,
	IsStartOf startOf,
	ToIntFunction<EndData> end,
	Either<Creator<? extends Section<?>>, BracketDef> creator)
{
	public Matcher(String content, IsStartOf startOf, ToIntFunction<EndData> end, Creator<? extends Section<?>> creator)
	{
		this(content, startOf, end, Either.left(creator));
	}
	
	public Matcher(String content, IsStartOf startOf, ToIntFunction<EndData> end, BracketDef type)
	{
		this(content, startOf, end, Either.right(type));
	}
	
	public int end(EndData data)
	{
		return this.end.applyAsInt(data);
	}
}