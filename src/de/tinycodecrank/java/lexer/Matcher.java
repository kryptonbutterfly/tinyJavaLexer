package de.tinycodecrank.java.lexer;

import java.util.function.ToIntFunction;

import de.tinycodecrank.java.lexer.StringUtils.IsStartOf;
import de.tinycodecrank.java.lexer.tokens.Section;
import de.tinycodecrank.monads.sum.of2.Sum2;

public record Matcher(
	String content,
	IsStartOf startOf,
	ToIntFunction<EndData> end,
	Sum2<Creator<? extends Section<?>>, BracketDef> creator)
{
	public Matcher(String content, IsStartOf startOf, ToIntFunction<EndData> end, Creator<? extends Section<?>> creator)
	{
		this(content, startOf, end, Sum2.left(creator));
	}
	
	public Matcher(String content, IsStartOf startOf, ToIntFunction<EndData> end, BracketDef type)
	{
		this(content, startOf, end, Sum2.right(type));
	}
	
	public int end(EndData data)
	{
		return this.end.applyAsInt(data);
	}
}