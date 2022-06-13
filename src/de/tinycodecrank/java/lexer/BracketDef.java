package de.tinycodecrank.java.lexer;

public class BracketDef
{
	private final String		value;
	private final boolean		opening;
	private final BracketType	type;
	
	private BracketDef(String value, boolean opening, BracketType type)
	{
		this.value		= value;
		this.opening	= opening;
		this.type		= type;
	}
	
	public static enum BracketType
	{
		BLOCK("{", "}"),
		ARRAY("[", "]"),
		GROUP("(", ")");
		
		public final BracketDef	opening;
		public final BracketDef	closing;
		
		BracketType(String opening, String closing)
		{
			this.opening	= new BracketDef(opening, true, this);
			this.closing	= new BracketDef(closing, false, this);
		}
	}
	
	public String value()
	{
		return value;
	}
	
	public boolean opening()
	{
		return opening;
	}
	
	public BracketType type()
	{
		return type;
	}
}