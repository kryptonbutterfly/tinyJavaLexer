module kryptonbutterfly.JavaLexer
{
	exports kryptonbutterfly.java.lexer;
	exports kryptonbutterfly.java.lexer.tokens;
	
	requires transitive kryptonbutterfly.Functional;
	requires kryptonbutterfly.Monads;
	requires kryptonbutterfly.mathUtils;
	requires kryptonbutterfly.Collections;
}