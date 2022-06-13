package de.tinycodecrank.java.lexer;

import java.io.File;

public record Location(int line, int col, String file)
{
	@Override
	public String toString()
	{
		return ":" + line + ":" + col;
	}
	
	public String errorPos()
	{
		final var	tmp			= "at (%s:%s:%s)";
		final var	fileName	= new File(file).getName();
		return String.format(tmp, fileName, line, col);
	}
	
	public static Location calcEnd(Location start, String value, String lineSeparator, String file)
	{
		final var	split	= value.split(lineSeparator, -1);
		final var	lines	= split.length - 1;
		final var	col		= split[split.length - 1].length();
		if (lines == 0)
		{
			return new Location(start.line() + lines, start.col() + col, file);
		}
		else
		{
			return new Location(start.line() + lines, col, file);
		}
	}
	
	public Location addCol(int amount)
	{
		return new Location(line, col + amount, file);
	}
}