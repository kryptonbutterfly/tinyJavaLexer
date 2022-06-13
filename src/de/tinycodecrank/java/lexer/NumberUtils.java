package de.tinycodecrank.java.lexer;

import static de.tinycodecrank.math.utils.range.Range.*;

public class NumberUtils
{
	private static final char[]	characters	= "_0123456789abcdef".toCharArray();
	public static final String	BIN_DELIM	= "0b";
	public static final String	OCT_DELIM	= "0o";
	public static final String	HEX_DELIM	= "0x";
	
	public static boolean isValidChar(char target, int radix)
	{
		for (final int index : range(0, radix + 1))
		{
			if (characters[index] == target)
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isValidDigit(char target, int radix)
	{
		for (final int index : range(1, radix + 1))
		{
			if (characters[index] == target)
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isValidNumber(int offset, String value, int radix)
	{
		for (final int i : range(offset, value.length()))
		{
			if (!isValidChar(value.charAt(i), radix))
			{
				return false;
			}
		}
		return value.length() > offset;
	}
	
	public static boolean validBinary(String value)
	{
		return value.startsWith(BIN_DELIM) && isValidNumber(2, value, 2);
	}
	
	public static boolean validOctal(String value)
	{
		return value.startsWith(OCT_DELIM) && isValidNumber(2, value, 8);
	}
	
	public static boolean validHexaDecimal(String value)
	{
		return value.startsWith(HEX_DELIM) && isValidNumber(2, value, 16);
	}
	
	public static boolean validDecimal(String value)
	{
		return isValidNumber(0, value, 10);
	}
	
	public static boolean validFloat(String value)
	{
		if (value.endsWith("F") || value.endsWith("f"))
		{
			return validDecimal(value.substring(value.length() - 1));
		}
		else
		{
			return false;
		}
	}
}