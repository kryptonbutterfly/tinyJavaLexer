package kryptonbutterfly.java.lexer.tokens;

import kryptonbutterfly.java.lexer.Creator;
import kryptonbutterfly.java.lexer.EndData;
import kryptonbutterfly.java.lexer.Location;
import kryptonbutterfly.java.lexer.Matcher;
import kryptonbutterfly.java.lexer.StringUtils.IsStartOf;

public class LineFeed implements Section<String>
{
	private final Location loc;
	
	private LineFeed(Location loc)
	{
		this.loc = loc;
	}
	
	public Location loc()
	{
		return loc;
	}
	
	private static Creator<LineFeed> create()
	{
		return _value -> loc -> new LineFeed(loc);
	}
	
	@Override
	public String toString()
	{
		return "¶ " + loc.line() + "\n";
	}
	
	private static IsStartOf start()
	{
		return (target, offset, start) -> target.startsWith("\n", offset);
	}
	
	private static int end(EndData data)
	{
		return data.start() + data.lineSeparator().length();
	}
	
	@Override
	public String value()
	{
		return "\n"; // TODO consider using ARGS!
	}
	
	@Override
	public String description()
	{
		return "linefeed";
	}
	
	public static Matcher matcher()
	{
		return new Matcher("linefeed", start(), LineFeed::end, create());
	}
}