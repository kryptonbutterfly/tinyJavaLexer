package kryptonbutterfly.java.lexer;

public enum Keywords
{
	ABSTRACT("abstract"),
	ASSERT("assert"),
	
	BOOLEAN("boolean"),
	BREAK("break"),
	BYTE("byte"),
	
	CASE("case"),
	CATCH("catch"),
	CHAR("char"),
	CLASS("class"),
	CONST("const"),
	CONTINUE("continue"),
	
	DEFAULT("default"),
	DO("do"),
	DOUBLE("double"),
	
	ELSE("else"),
	ENUM("enum"),
	EXPORTS("exports"),
	EXTENDS("extends"),
	
	FOR("for"),
	FINAL("final"),
	FINALLY("finally"),
	FLOAT("float"),
	
	GOTO("goto"),
	
	IF("if"),
	IMPLEMENTS("implements"),
	IMPORT("import"),
	INSTANCEOF("instanceof"),
	INT("int"),
	INTERFACE("interface"),
	
	LONG("long"),
	
	MODULE("module"),
	
	NATIVE("native"),
	NEW("new"),
	NON_SEALED("non-sealed"),
	
	OPEN("open"),
	OPENS("opens"),
	
	PACKAGE("package"),
	PERMITS("permits"),
	PRIVATE("private"),
	PROVIDES("provides"),
	PROTECTED("protected"),
	PUBLIC("public"),
	
	RECORD("record"),
	REQUIRES("requires"),
	RETURN("return"),
	
	SEALED("sealed"),
	SHORT("short"),
	STATIC("static"),
	STRICTFP("strictfp"),
	SUPER("super"),
	SWITCH("switch"),
	SYNCHRONIZED("synchronized"),
	
	THIS("this"),
	THROW("throw"),
	THROWS("throws"),
	TO("to"),
	TRANSIENT("transient"),
	TRANSITIVE("transitive"),
	TRY("try"),
	
	USES("uses"),
	
	VAR("var"),
	VOID("void"),
	VOLATILE("volatile"),
	
	WHILE("while"),
	WITH("with"),
	
	YIELD("yield");
	
	public final String value;
	
	Keywords(String value)
	{
		this.value = value;
	}
}