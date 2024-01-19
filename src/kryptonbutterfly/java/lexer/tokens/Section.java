package kryptonbutterfly.java.lexer.tokens;

import kryptonbutterfly.java.lexer.Location;

public interface Section<V>
{
	public V value();
	
	public Location loc();
	
	@Override
	public String toString();
	
	public String description();
}