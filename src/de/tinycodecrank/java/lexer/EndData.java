package de.tinycodecrank.java.lexer;

public record EndData(int start, String raw, String lineSeparator, Location loc)
{
	@Override
	public String toString()
	{
		return "EndData:\n\tstart\t"
			+ start
			+ "\n\tloc\t\t"
			+ loc;
	}
}