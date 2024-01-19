package kryptonbutterfly.java.lexer.tokens;

import java.util.function.ToIntFunction;

import kryptonbutterfly.java.lexer.Creator;
import kryptonbutterfly.java.lexer.EndData;
import kryptonbutterfly.java.lexer.Location;
import kryptonbutterfly.java.lexer.OpDef;
import kryptonbutterfly.java.lexer.StringUtils.IsStartOf;

public class Operator implements Section<OpDef>
{
	public static final String						DESCRIPTION	= "operator";
	private final kryptonbutterfly.java.lexer.OpDef	value;
	private final Location							loc;
	
	private Operator(OpDef value, Location loc)
	{
		this.value	= value;
		this.loc	= loc;
	}
	
	public OpDef value()
	{
		return value;
	}
	
	public Location loc()
	{
		return loc;
	}
	
	public static Creator<Operator> create(OpDef op)
	{
		return _value -> loc -> new Operator(op, loc);
	}
	
	@Override
	public String toString()
	{
		return value.op() + " ";
	}
	
	public static IsStartOf start(OpDef op)
	{
		return (target, offset, start) -> target.startsWith(op.op(), offset);
	}
	
	public static ToIntFunction<EndData> end(OpDef op)
	{
		return data -> data.start() + op.op().length();
	}
	
	@Override
	public String description()
	{
		return DESCRIPTION + " " + value.op();
	}
}