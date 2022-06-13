package de.tinycodecrank.java.lexer.tokens;

import java.util.stream.Stream;

import de.tinycodecrank.collections.Stack;
import de.tinycodecrank.java.lexer.Location;

//TODO add hashcode and equals
public class Root extends Group<String>
{
	private final Location loc;
	
	public Root(Location loc)
	{
		this.loc = loc;
	}
	
	public Location loc()
	{
		return loc;
	}
	
	@Override
	public String value()
	{
		return "";
	}
	
	@Override
	public String toString()
	{
		final var sb = new StringBuilder();
		for (final var e : content())
		{
			sb.append(e.toString());
		}
		return sb.toString();
	}
	
	@Override
	public String description()
	{
		return "root";
	}
	
	public Stream<Section<?>> stream()
	{
		final var	builder	= Stream.<Section<?>>builder();
		final var	stack	= new Stack<Section<?>>();
		stack.push(this);
		while (!stack.isEmpty())
		{
			var last = stack.pop();
			if (last instanceof Group<?> group)
			{
				stack.pushAll(group.content());
			}
			else
			{
				builder.add(last);
			}
		}
		return builder.build();
	}
}