package de.tinycodecrank.java.lexer;

import static de.tinycodecrank.math.utils.range.Range.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import de.tinycodecrank.collections.Stack;
import de.tinycodecrank.java.lexer.BracketDef.BracketType;
import de.tinycodecrank.java.lexer.StringUtils.IsStartOf;
import de.tinycodecrank.java.lexer.tokens.BooleanLiteral;
import de.tinycodecrank.java.lexer.tokens.Bracket;
import de.tinycodecrank.java.lexer.tokens.CharLiteral;
import de.tinycodecrank.java.lexer.tokens.Comment;
import de.tinycodecrank.java.lexer.tokens.Group;
import de.tinycodecrank.java.lexer.tokens.Keyword;
import de.tinycodecrank.java.lexer.tokens.LineFeed;
import de.tinycodecrank.java.lexer.tokens.Name;
import de.tinycodecrank.java.lexer.tokens.Operator;
import de.tinycodecrank.java.lexer.tokens.Root;
import de.tinycodecrank.java.lexer.tokens.Section;
import de.tinycodecrank.java.lexer.tokens.StringLiteral;
import de.tinycodecrank.java.lexer.tokens.Unspecified;
import de.tinycodecrank.java.lexer.tokens.Whitespace;
import de.tinycodecrank.monads.either.Either;
import de.tinycodecrank.monads.opt.Opt;

public final class Lexer
{
	private final Matcher[]			matcher;
	private final UnspecMatcher[]	unspecMatcher;
	
	public Lexer()
	{
		this.matcher		= createMatcher();
		this.unspecMatcher	= createUnspecMatcher();
	}
	
	private static final UnspecMatcher[] createUnspecMatcher()
	{
		final var list = new ArrayList<UnspecMatcher>();
		for (final var key : Keywords.values())
		{
			list.add(new UnspecMatcher(key.value::equals, (_value, loc) -> new Keyword(key, loc)));
		}
		list.add(new UnspecMatcher(Name::valid, Name::new));
		return list.toArray(new UnspecMatcher[list.size()]);
	}
	
	private static final Matcher[] createMatcher()
	{
		final var list = new ArrayList<Matcher>();
		
		list.add(CharLiteral.matcher());
		list.add(BooleanLiteral.matcher());
		list.add(Comment.matcherLineComment());
		list.add(Comment.matcherBlockComment());
		list.add(LineFeed.matcher());
		list.add(StringLiteral.matcher());
		list.add(Whitespace.matcher());
		
		for (final var opType : OpDef.operators)
		{
			final var	start	= Operator.start(opType);
			final var	end		= Operator.end(opType);
			final var	creator	= Operator.create(opType);
			list.add(new Matcher(opType.op(), start, end, creator));
		}
		
		for (final var bracketType : BracketType.values())
		{
			list.add(Bracket.matchOpening(bracketType));
			list.add(Bracket.matchClosing(bracketType));
		}
		
		return list.toArray(Matcher[]::new);
	}
	
	public Root readFile(String text, String file)
	{
		final var run = new LexerRun(text, file);
		run.readFile();
		return run.root;
	}
	
	private final class LexerRun
	{
		private final Stack<Group<?>>	blocks	= new Stack<>();
		private final String			file;
		private final Root				root;
		
		private int			offset	= 0;
		private Location	location;
		
		private final String text;
		
		private final IsStartOf[] STARTER = Arrays.stream(matcher).map(Matcher::startOf).toArray(IsStartOf[]::new);
		
		private LexerRun(String text, String file)
		{
			this.text		= text;
			this.file		= file;
			this.location	= new Location(1, 0, file);
			this.root		= new Root(location);
		}
		
		private void readFile()
		{
			blocks.push(root);
			while (offset < text.length())
			{
				findStart().if_(next ->
				{
					final int start = next.start();
					if (next.start() > offset)
					{
						final var	value	= text.substring(offset, start);
						final var	unspec	= buildFromUnspecified(value, location);
						blocks.getLast().content().add(unspec);
						location = Location.calcEnd(location, value, "\n", file);
					}
					next.match()
						.forEither(
							section -> section(section.apply(location), start),
							closing -> closing(closing, start));
					offset		= next.end();
					location	= Location.calcEnd(location, text.substring(start, offset), "\n", file);
				}).else_(() ->
				{
					blocks.getLast().content().add(buildFromUnspecified(text.substring(offset), location));
					offset = text.length();
				});
			}
		}
		
		private Section<?> buildFromUnspecified(String value, Location loc)
		{
			for (final var matcher : range(unspecMatcher).element())
			{
				if (matcher.testValue(value))
				{
					return matcher.create(value, loc);
				}
			}
			return new Unspecified(value, loc);
		}
		
		private void section(Section<?> section, int start)
		{
			final var content = blocks.getLast().content();
			if (section instanceof Bracket bracket)
			{
				content.add(bracket);
				blocks.push(bracket);
			}
			else if (section instanceof Operator op)
				content.add(op);
			else
				Opt.of(section)
					.if_(content::add);
		}
		
		private void closing(BracketDef closing, int start)
		{
			if (blocks.getLast() instanceof Bracket bracket)
			{
				if (bracket.value() == closing.type())
					blocks.pop();
				else
				{
					final var	msg			= "Unexpected Token: %s expected %s instead: (%s)\n";
					final var	found		= closing.value();
					final var	expected	= bracket.value().closing.value();
					System.err.printf(msg, found, expected, location);
				}
			}
			else
			{
				final var	msg		= "Unexpected Token: %s (%s)\n";
				final var	found	= closing.value();
				System.err.printf(msg, found, location);
			}
			offset = start + closing.value().length();
		}
		
		private Opt<Occurence> findStart()
		{
			return StringUtils.indexOfFirst(text, offset, STARTER).flatmap(find ->
			{
				final var	matcher	= Lexer.this.matcher[find.index];
				final int	end		= matcher.end(new EndData(find.min, text, "\n", location));
				if (end != -1)
				{
					final var value = text.substring(find.min, end);
					return Opt.of(new Occurence(find.min, end, matcher.creator().mapLeft(l -> l.create(value))));
				}
				return Opt.empty();
			});
		}
		
		private record Occurence(int start, int end, Either<Function<Location, ? extends Section<?>>, BracketDef> match)
		{}
	}
}