package de.tinycodecrank.java.lexer;

import static de.tinycodecrank.math.utils.range.Range.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.IntPredicate;

import de.tinycodecrank.functions.ObjIntBiPredicate;
import de.tinycodecrank.functions.applicable.ApplicableLeft;
import de.tinycodecrank.functions.int_.IntBiPredicate;
import de.tinycodecrank.functions.int_.applicable.IntApplicableRight;
import de.tinycodecrank.monads.Opt;

public final class StringUtils
{
	private static final HashMap<Character, String>	escapeMap	= new HashMap<>();
	private static final HashMap<String, Character>	unescapeMap	= new HashMap<>();
	
	private static final HashMap<Character, String> visControll = new HashMap<>();
	
	private static void regEsc(Character c, String esc)
	{
		escapeMap.put(c, esc);
		unescapeMap.put(esc, c);
	}
	
	private static void visCon(char key, String value)
	{
		visControll.put(key, value);
	}
	
	static
	{
		regEsc('\b', "\\b");
		regEsc('\t', "\\t");
		regEsc('\n', "\\n");
		regEsc('\f', "\\f");
		regEsc('\r', "\\r");
		regEsc('"', "\\\"");
		regEsc('\'', "\\'");
		regEsc('\\', "\\\\");
		
		visCon(' ', "␣");
		visCon('\t', "»   ");
	}
	
	public static String escape(char c)
	{
		final var res = escapeMap.get(c);
		return res != null ? res : Character.toString(c);
	}
	
	public static String toVisibleControll(char c)
	{
		final var res = visControll.get(c);
		return res != null ? res : Character.toString(c);
	}
	
	public static char unescape(String s)
	{
		final var res = unescapeMap.get(s);
		return res != null ? res : s.charAt(0);
	}
	
	public static String unescapeString(String s)
	{
		final var sb = new StringBuilder(s.length());
		for (int i = 0; i < s.length(); i++)
		{
			final char c = s.charAt(i);
			if (c == '\\')
			{
				final var escaped = s.substring(i, i + 2);
				sb.append(unescape(escaped));
				i++;
			}
			else
			{
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	public static Opt<FindResult> indexOfFirst(String target, int offset, IsStartOf[] isMatch)
	{
		return indexOfFirst(
			target.length(),
			offset,
			Arrays.stream(isMatch)
				.map(m -> m.aptFirst(target))
				.map(m -> m.aptLast(offset))
				.toArray(IntPredicate[]::new));
	}
	
	private static Opt<FindResult> indexOfFirst(int size, int offset, IntPredicate[] isMatch)
	{
		for (int min : range(offset, size))
		{
			for (final var ie : range(isMatch))
			{
				if (ie.element().test(min))
				{
					return Opt.of(new FindResult(min, ie.index()));
				}
			}
		}
		return Opt.empty();
	}
	
	public static final class FindResult
	{
		public final int	min;
		public final int	index;
		
		private FindResult(int min, int index)
		{
			this.min	= min;
			this.index	= index;
		}
		
		@Override
		public String toString()
		{
			return "FindResult [min=" + min + ", index=" + index + "]";
		}
	}
	
	@FunctionalInterface
	public static interface IsStartOf extends ApplicableLeft<String, IntBiPredicate>, IntApplicableRight<ObjIntBiPredicate<String>>
	{
		boolean test(String target, int offset, int start);
		
		@Override
		default IntBiPredicate aptFirst(String target)
		{
			return (offset, start) -> test(target, offset, start);
		}
		
		@Override
		default ObjIntBiPredicate<String> aptLast(int start)
		{
			return (target, offset) -> test(target, offset, start);
		}
	}
}