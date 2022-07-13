module de.tinycodecrank.JavaLexer
{
	exports de.tinycodecrank.java.lexer;
	exports de.tinycodecrank.java.lexer.tokens;
	
	requires de.tinycodecrank.Monads;
	requires de.tinycodecrank.mathUtils;
	requires transitive de.tinycodecrank.Functional;
	requires de.tinycodecrank.Collections;
}