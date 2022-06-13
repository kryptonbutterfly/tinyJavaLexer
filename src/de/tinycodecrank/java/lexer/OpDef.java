package de.tinycodecrank.java.lexer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import de.tinycodecrank.math.utils.limit.LimitInt;

public class OpDef implements Comparable<OpDef>
{
	private static final Collector c = new Collector();
	
	public static final OpDef	ASSIGNMENT				= c.inc("=");
	public static final OpDef	TERNARY					= c.inc("?");
	public static final OpDef	COND_OR					= c.inc("||");
	public static final OpDef	COND_AND				= c.inc("&&");
	public static final OpDef	BIT_OR					= c.inc("|");
	public static final OpDef	BIT_XOR					= c.inc("^");
	public static final OpDef	BIT_AND					= c.inc("&");
	public static final OpDef	EQUAL					= c.inc("==");
	public static final OpDef	NOT_EQUAL				= c.curr("!=");
	public static final OpDef	LESS					= c.curr("<");
	public static final OpDef	LESS_EQUAL				= c.curr("<=");
	public static final OpDef	GREATER_EQUAL			= c.curr(">=");
	public static final OpDef	GREATER					= c.curr(">");
	public static final OpDef	SHIFT_LEFT_UNSIGHNED	= c.curr(">>>");
	public static final OpDef	SHIFT_LEFT				= c.curr("<<");
	public static final OpDef	SHIFT_RIGHT				= c.curr(">>");
	public static final OpDef	ADD						= c.inc("+");
	public static final OpDef	SUB						= c.curr("-");
	public static final OpDef	DIV						= c.inc("/");
	public static final OpDef	DIV2					= c.curr("÷");
	public static final OpDef	MUL						= c.curr("*");
	public static final OpDef	REMAINDER				= c.curr("%");
	public static final OpDef	DOT_PRODUCT				= c.inc("·");
	public static final OpDef	CROSS_PRODUCT			= c.curr("×");
	public static final OpDef	COMPLEMENT				= c.inc("~");
	public static final OpDef	NOT						= c.inc("!");
	public static final OpDef	LAMBDA					= c.inc("→");
	public static final OpDef	LAMBDA2					= c.curr("->");
	public static final OpDef	DEREFERENCE				= c.inc(".");
	public static final OpDef	COMMA					= c.inc(",");
	public static final OpDef	COLON					= c.inc(":");
	public static final OpDef	SEMICOLON				= c.inc(";");
	
	public static final OpDef[] operators = c.toArray();
	
	private final String	op;
	private final int		precedence;
	
	private OpDef(String op, int precedence)
	{
		this.op			= op;
		this.precedence	= precedence;
	}
	
	public String op()
	{
		return op;
	}
	
	public int precedence()
	{
		return precedence;
	}
	
	@Override
	public int compareTo(OpDef o)
	{
		final int diff = o.op.length() - op.length();
		return LimitInt.clamp(-1, diff, 1);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (!(obj instanceof OpDef))
		{
			return false;
		}
		OpDef other = (OpDef) obj;
		return Objects.equals(op, other.op);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(op);
	}
	
	private static class Collector
	{
		final HashMap<String, OpDef> ops = new HashMap<>();
		
		int value = 0;
		
		OpDef inc(String value)
		{
			final var opDef = new OpDef(value, ++this.value);
			validate(opDef);
			return opDef;
		}
		
		OpDef curr(String value)
		{
			final var opDef = new OpDef(value, this.value);
			validate(opDef);
			return opDef;
		}
		
		void validate(OpDef op)
		{
			if (ops.containsKey(op.op))
			{
				throw new IllegalStateException(
					String.format("duplicate OpDef %s", op.op));
			}
			else
			{
				ops.put(op.op, op);
			}
		}
		
		OpDef[] toArray()
		{
			final var op = ops.values().toArray(new OpDef[ops.size()]);
			ops.clear();
			Arrays.sort(op, OpDef::compareTo);
			return op;
		}
	}
}