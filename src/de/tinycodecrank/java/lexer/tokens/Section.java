package de.tinycodecrank.java.lexer.tokens;

import de.tinycodecrank.java.lexer.Location;

public interface Section<V>
{
	public V value();
	
	public Location loc();
	
	@Override
	public String toString();
	
	public String description();
}