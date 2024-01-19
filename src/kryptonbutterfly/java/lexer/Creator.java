package kryptonbutterfly.java.lexer;

import java.util.function.Function;

@FunctionalInterface
public interface Creator<E>
{
	Function<Location, E> create(String value);
}