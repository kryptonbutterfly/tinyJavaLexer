package kryptonbutterfly.java.lexer.tokens;

import java.util.ArrayList;

public abstract class Group<V> implements Section<V>
{
	static final String					INDENT	= "\t";
	private final ArrayList<Section<?>>	content	= new ArrayList<>();
	
	public ArrayList<Section<?>> content()
	{
		return content;
	}
}