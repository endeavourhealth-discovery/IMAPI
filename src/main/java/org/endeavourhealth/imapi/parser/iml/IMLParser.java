// Generated from C:/Users/david/GithubRepos/IMAPI/src/main/grammars/IML.g4 by ANTLR 4.13.2
package iml;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class IMLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, DEFINE=2, PREFIX=3, DEFAULT=4, ASSIGN=5, AS=6, MATCH=7, FROM=8, 
		EITHER=9, OR=10, AND=11, EXCLUDE=12, IF=13, OF=14, IS=15, IN=16, ON=17, 
		BETWEEN=18, WITH=19, WHERE=20, LATEST=21, EARLIEST=22, LARGEST=23, SMALLEST=24, 
		RELATIVE=25, TO=26, OPEN_BRACE=27, CLOSE_BRACE=28, OPEN_PAREN=29, CLOSE_PAREN=30, 
		ARROW=31, OPTIONAL_ARROW=32, REVERSE_ARROW=33, COMMA=34, GT=35, GTE=36, 
		LT=37, LTE=38, EQ=39, NEQ=40, PNAME_NS=41, IDENTIFIER=42, NODEREF=43, 
		PARAMETER=44, IRI=45, NUMBER=46, WS=47, COMMENT=48, BLOCK_COMMENT=49;
	public static final int
		RULE_iml = 0, RULE_prefix = 1, RULE_default = 2, RULE_definition = 3, 
		RULE_expression = 4, RULE_assignment = 5, RULE_iriRef = 6, RULE_matchType = 7, 
		RULE_type = 8, RULE_match = 9, RULE_path = 10, RULE_orderBy = 11, RULE_matchIn = 12, 
		RULE_from = 13, RULE_keepAs = 14, RULE_booleanMatch = 15, RULE_nestedBooleanMatch = 16, 
		RULE_disjunctionMatch = 17, RULE_exclusionMatch = 18, RULE_conjunctionMatch = 19, 
		RULE_disjunctionWhere = 20, RULE_conjunctionWhere = 21, RULE_nestedBooleanWhere = 22, 
		RULE_booleanWhere = 23, RULE_whereClause = 24, RULE_where = 25, RULE_property = 26, 
		RULE_propertyOf = 27, RULE_whereIs = 28, RULE_concept = 29, RULE_disjunctionConcept = 30, 
		RULE_whereRange = 31, RULE_units = 32, RULE_whereRelativeTo = 33, RULE_whereValue = 34, 
		RULE_functionExpression = 35, RULE_methodCall = 36, RULE_argumentList = 37, 
		RULE_comparisonOperator = 38, RULE_simpleExpression = 39;
	private static String[] makeRuleNames() {
		return new String[] {
			"iml", "prefix", "default", "definition", "expression", "assignment", 
			"iriRef", "matchType", "type", "match", "path", "orderBy", "matchIn", 
			"from", "keepAs", "booleanMatch", "nestedBooleanMatch", "disjunctionMatch", 
			"exclusionMatch", "conjunctionMatch", "disjunctionWhere", "conjunctionWhere", 
			"nestedBooleanWhere", "booleanWhere", "whereClause", "where", "property", 
			"propertyOf", "whereIs", "concept", "disjunctionConcept", "whereRange", 
			"units", "whereRelativeTo", "whereValue", "functionExpression", "methodCall", 
			"argumentList", "comparisonOperator", "simpleExpression"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "':'", null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, "'{'", "'}'", "'('", "')'", "'->'", "'?->'", "'<-'", 
			"','", "'>'", "'>='", "'<'", "'<='", "'='", "'!='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "DEFINE", "PREFIX", "DEFAULT", "ASSIGN", "AS", "MATCH", "FROM", 
			"EITHER", "OR", "AND", "EXCLUDE", "IF", "OF", "IS", "IN", "ON", "BETWEEN", 
			"WITH", "WHERE", "LATEST", "EARLIEST", "LARGEST", "SMALLEST", "RELATIVE", 
			"TO", "OPEN_BRACE", "CLOSE_BRACE", "OPEN_PAREN", "CLOSE_PAREN", "ARROW", 
			"OPTIONAL_ARROW", "REVERSE_ARROW", "COMMA", "GT", "GTE", "LT", "LTE", 
			"EQ", "NEQ", "PNAME_NS", "IDENTIFIER", "NODEREF", "PARAMETER", "IRI", 
			"NUMBER", "WS", "COMMENT", "BLOCK_COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "IML.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public IMLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ImlContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(IMLParser.EOF, 0); }
		public List<DefinitionContext> definition() {
			return getRuleContexts(DefinitionContext.class);
		}
		public DefinitionContext definition(int i) {
			return getRuleContext(DefinitionContext.class,i);
		}
		public AssignmentContext assignment() {
			return getRuleContext(AssignmentContext.class,0);
		}
		public PrefixContext prefix() {
			return getRuleContext(PrefixContext.class,0);
		}
		public DefaultContext default_() {
			return getRuleContext(DefaultContext.class,0);
		}
		public ImlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iml; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterIml(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitIml(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitIml(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImlContext iml() throws RecognitionException {
		ImlContext _localctx = new ImlContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_iml);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(80);
				definition();
				}
				}
				setState(83); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==DEFINE );
			setState(86);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(85);
				assignment();
				}
			}

			setState(89);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PREFIX) {
				{
				setState(88);
				prefix();
				}
			}

			setState(92);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEFAULT) {
				{
				setState(91);
				default_();
				}
			}

			setState(94);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrefixContext extends ParserRuleContext {
		public TerminalNode PREFIX() { return getToken(IMLParser.PREFIX, 0); }
		public TerminalNode OPEN_BRACE() { return getToken(IMLParser.OPEN_BRACE, 0); }
		public TerminalNode CLOSE_BRACE() { return getToken(IMLParser.CLOSE_BRACE, 0); }
		public List<TerminalNode> PNAME_NS() { return getTokens(IMLParser.PNAME_NS); }
		public TerminalNode PNAME_NS(int i) {
			return getToken(IMLParser.PNAME_NS, i);
		}
		public List<TerminalNode> IRI() { return getTokens(IMLParser.IRI); }
		public TerminalNode IRI(int i) {
			return getToken(IMLParser.IRI, i);
		}
		public PrefixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterPrefix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitPrefix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitPrefix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixContext prefix() throws RecognitionException {
		PrefixContext _localctx = new PrefixContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_prefix);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(96);
			match(PREFIX);
			setState(97);
			match(OPEN_BRACE);
			setState(100); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(98);
				match(PNAME_NS);
				setState(99);
				match(IRI);
				}
				}
				setState(102); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==PNAME_NS );
			setState(104);
			match(CLOSE_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DefaultContext extends ParserRuleContext {
		public TerminalNode DEFAULT() { return getToken(IMLParser.DEFAULT, 0); }
		public TerminalNode EQ() { return getToken(IMLParser.EQ, 0); }
		public TerminalNode IRI() { return getToken(IMLParser.IRI, 0); }
		public DefaultContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_default; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterDefault(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitDefault(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitDefault(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefaultContext default_() throws RecognitionException {
		DefaultContext _localctx = new DefaultContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_default);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
			match(DEFAULT);
			setState(107);
			match(EQ);
			setState(108);
			match(IRI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DefinitionContext extends ParserRuleContext {
		public TerminalNode DEFINE() { return getToken(IMLParser.DEFINE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public DefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefinitionContext definition() throws RecognitionException {
		DefinitionContext _localctx = new DefinitionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_definition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			match(DEFINE);
			setState(111);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public TerminalNode IRI() { return getToken(IMLParser.IRI, 0); }
		public TerminalNode AS() { return getToken(IMLParser.AS, 0); }
		public TerminalNode OPEN_BRACE() { return getToken(IMLParser.OPEN_BRACE, 0); }
		public MatchTypeContext matchType() {
			return getRuleContext(MatchTypeContext.class,0);
		}
		public TerminalNode CLOSE_BRACE() { return getToken(IMLParser.CLOSE_BRACE, 0); }
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			match(IRI);
			setState(114);
			match(AS);
			setState(115);
			match(OPEN_BRACE);
			setState(116);
			matchType();
			setState(117);
			match(CLOSE_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssignmentContext extends ParserRuleContext {
		public TerminalNode ASSIGN() { return getToken(IMLParser.ASSIGN, 0); }
		public TerminalNode OPEN_BRACE() { return getToken(IMLParser.OPEN_BRACE, 0); }
		public TerminalNode CLOSE_BRACE() { return getToken(IMLParser.CLOSE_BRACE, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(IMLParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(IMLParser.IDENTIFIER, i);
		}
		public List<TerminalNode> EQ() { return getTokens(IMLParser.EQ); }
		public TerminalNode EQ(int i) {
			return getToken(IMLParser.EQ, i);
		}
		public List<IriRefContext> iriRef() {
			return getRuleContexts(IriRefContext.class);
		}
		public IriRefContext iriRef(int i) {
			return getRuleContext(IriRefContext.class,i);
		}
		public AssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterAssignment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitAssignment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitAssignment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentContext assignment() throws RecognitionException {
		AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_assignment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(119);
			match(ASSIGN);
			setState(120);
			match(OPEN_BRACE);
			setState(124); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(121);
				match(IDENTIFIER);
				setState(122);
				match(EQ);
				setState(123);
				iriRef();
				}
				}
				setState(126); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==IDENTIFIER );
			setState(128);
			match(CLOSE_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IriRefContext extends ParserRuleContext {
		public TerminalNode PNAME_NS() { return getToken(IMLParser.PNAME_NS, 0); }
		public TerminalNode IDENTIFIER() { return getToken(IMLParser.IDENTIFIER, 0); }
		public TerminalNode IRI() { return getToken(IMLParser.IRI, 0); }
		public IriRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iriRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterIriRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitIriRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitIriRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IriRefContext iriRef() throws RecognitionException {
		IriRefContext _localctx = new IriRefContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_iriRef);
		try {
			setState(134);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PNAME_NS:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(130);
				match(PNAME_NS);
				setState(131);
				match(T__0);
				setState(132);
				match(IDENTIFIER);
				}
				}
				break;
			case IRI:
				enterOuterAlt(_localctx, 2);
				{
				setState(133);
				match(IRI);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MatchTypeContext extends ParserRuleContext {
		public TerminalNode MATCH() { return getToken(IMLParser.MATCH, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public MatchContext match() {
			return getRuleContext(MatchContext.class,0);
		}
		public BooleanMatchContext booleanMatch() {
			return getRuleContext(BooleanMatchContext.class,0);
		}
		public MatchTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterMatchType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitMatchType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitMatchType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchTypeContext matchType() throws RecognitionException {
		MatchTypeContext _localctx = new MatchTypeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_matchType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(136);
			match(MATCH);
			setState(137);
			type();
			setState(140);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(138);
				match();
				}
				break;
			case 2:
				{
				setState(139);
				booleanMatch();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(IMLParser.IDENTIFIER, 0); }
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(142);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MatchContext extends ParserRuleContext {
		public MatchInContext matchIn() {
			return getRuleContext(MatchInContext.class,0);
		}
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public OrderByContext orderBy() {
			return getRuleContext(OrderByContext.class,0);
		}
		public PathContext path() {
			return getRuleContext(PathContext.class,0);
		}
		public KeepAsContext keepAs() {
			return getRuleContext(KeepAsContext.class,0);
		}
		public FromContext from() {
			return getRuleContext(FromContext.class,0);
		}
		public MatchContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_match; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterMatch(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitMatch(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitMatch(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchContext match() throws RecognitionException {
		MatchContext _localctx = new MatchContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_match);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(145);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 31457280L) != 0)) {
				{
				setState(144);
				orderBy();
				}
			}

			setState(148);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 15032385536L) != 0)) {
				{
				setState(147);
				path();
				}
			}

			setState(155);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IN:
				{
				setState(150);
				matchIn();
				}
				break;
			case FROM:
				{
				{
				setState(151);
				from();
				setState(152);
				whereClause();
				}
				}
				break;
			case WHERE:
				{
				setState(154);
				whereClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(158);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(157);
				keepAs();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PathContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(IMLParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(IMLParser.IDENTIFIER, i);
		}
		public List<TerminalNode> ARROW() { return getTokens(IMLParser.ARROW); }
		public TerminalNode ARROW(int i) {
			return getToken(IMLParser.ARROW, i);
		}
		public List<TerminalNode> OPTIONAL_ARROW() { return getTokens(IMLParser.OPTIONAL_ARROW); }
		public TerminalNode OPTIONAL_ARROW(int i) {
			return getToken(IMLParser.OPTIONAL_ARROW, i);
		}
		public List<TerminalNode> REVERSE_ARROW() { return getTokens(IMLParser.REVERSE_ARROW); }
		public TerminalNode REVERSE_ARROW(int i) {
			return getToken(IMLParser.REVERSE_ARROW, i);
		}
		public PathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_path; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterPath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitPath(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitPath(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathContext path() throws RecognitionException {
		PathContext _localctx = new PathContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_path);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(160);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 15032385536L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(161);
				match(IDENTIFIER);
				}
				}
				setState(164); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 15032385536L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OrderByContext extends ParserRuleContext {
		public TerminalNode LATEST() { return getToken(IMLParser.LATEST, 0); }
		public TerminalNode EARLIEST() { return getToken(IMLParser.EARLIEST, 0); }
		public TerminalNode LARGEST() { return getToken(IMLParser.LARGEST, 0); }
		public TerminalNode SMALLEST() { return getToken(IMLParser.SMALLEST, 0); }
		public TerminalNode NUMBER() { return getToken(IMLParser.NUMBER, 0); }
		public OrderByContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderBy; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterOrderBy(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitOrderBy(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitOrderBy(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderByContext orderBy() throws RecognitionException {
		OrderByContext _localctx = new OrderByContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_orderBy);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 31457280L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(168);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NUMBER) {
				{
				setState(167);
				match(NUMBER);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MatchInContext extends ParserRuleContext {
		public TerminalNode IN() { return getToken(IMLParser.IN, 0); }
		public TerminalNode IDENTIFIER() { return getToken(IMLParser.IDENTIFIER, 0); }
		public MatchInContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchIn; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterMatchIn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitMatchIn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitMatchIn(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchInContext matchIn() throws RecognitionException {
		MatchInContext _localctx = new MatchInContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_matchIn);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(170);
			match(IN);
			setState(171);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FromContext extends ParserRuleContext {
		public TerminalNode FROM() { return getToken(IMLParser.FROM, 0); }
		public TerminalNode IDENTIFIER() { return getToken(IMLParser.IDENTIFIER, 0); }
		public FromContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_from; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterFrom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitFrom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitFrom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FromContext from() throws RecognitionException {
		FromContext _localctx = new FromContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_from);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(173);
			match(FROM);
			setState(174);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class KeepAsContext extends ParserRuleContext {
		public TerminalNode AS() { return getToken(IMLParser.AS, 0); }
		public TerminalNode IDENTIFIER() { return getToken(IMLParser.IDENTIFIER, 0); }
		public KeepAsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keepAs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterKeepAs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitKeepAs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitKeepAs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeepAsContext keepAs() throws RecognitionException {
		KeepAsContext _localctx = new KeepAsContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_keepAs);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(176);
			match(AS);
			setState(177);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BooleanMatchContext extends ParserRuleContext {
		public DisjunctionMatchContext disjunctionMatch() {
			return getRuleContext(DisjunctionMatchContext.class,0);
		}
		public ConjunctionMatchContext conjunctionMatch() {
			return getRuleContext(ConjunctionMatchContext.class,0);
		}
		public BooleanMatchContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanMatch; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterBooleanMatch(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitBooleanMatch(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitBooleanMatch(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanMatchContext booleanMatch() throws RecognitionException {
		BooleanMatchContext _localctx = new BooleanMatchContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_booleanMatch);
		try {
			setState(181);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(179);
				disjunctionMatch();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(180);
				conjunctionMatch();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NestedBooleanMatchContext extends ParserRuleContext {
		public TerminalNode OPEN_PAREN() { return getToken(IMLParser.OPEN_PAREN, 0); }
		public BooleanMatchContext booleanMatch() {
			return getRuleContext(BooleanMatchContext.class,0);
		}
		public TerminalNode CLOSE_PAREN() { return getToken(IMLParser.CLOSE_PAREN, 0); }
		public NestedBooleanMatchContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nestedBooleanMatch; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterNestedBooleanMatch(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitNestedBooleanMatch(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitNestedBooleanMatch(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NestedBooleanMatchContext nestedBooleanMatch() throws RecognitionException {
		NestedBooleanMatchContext _localctx = new NestedBooleanMatchContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_nestedBooleanMatch);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			match(OPEN_PAREN);
			setState(184);
			booleanMatch();
			setState(185);
			match(CLOSE_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DisjunctionMatchContext extends ParserRuleContext {
		public List<MatchContext> match() {
			return getRuleContexts(MatchContext.class);
		}
		public MatchContext match(int i) {
			return getRuleContext(MatchContext.class,i);
		}
		public List<NestedBooleanMatchContext> nestedBooleanMatch() {
			return getRuleContexts(NestedBooleanMatchContext.class);
		}
		public NestedBooleanMatchContext nestedBooleanMatch(int i) {
			return getRuleContext(NestedBooleanMatchContext.class,i);
		}
		public TerminalNode EITHER() { return getToken(IMLParser.EITHER, 0); }
		public List<TerminalNode> OR() { return getTokens(IMLParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(IMLParser.OR, i);
		}
		public DisjunctionMatchContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_disjunctionMatch; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterDisjunctionMatch(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitDisjunctionMatch(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitDisjunctionMatch(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DisjunctionMatchContext disjunctionMatch() throws RecognitionException {
		DisjunctionMatchContext _localctx = new DisjunctionMatchContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_disjunctionMatch);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(188);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EITHER) {
				{
				setState(187);
				match(EITHER);
				}
			}

			setState(192);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FROM:
			case IN:
			case WHERE:
			case LATEST:
			case EARLIEST:
			case LARGEST:
			case SMALLEST:
			case ARROW:
			case OPTIONAL_ARROW:
			case REVERSE_ARROW:
				{
				setState(190);
				match();
				}
				break;
			case OPEN_PAREN:
				{
				setState(191);
				nestedBooleanMatch();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(199); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(194);
				match(OR);
				setState(197);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case FROM:
				case IN:
				case WHERE:
				case LATEST:
				case EARLIEST:
				case LARGEST:
				case SMALLEST:
				case ARROW:
				case OPTIONAL_ARROW:
				case REVERSE_ARROW:
					{
					setState(195);
					match();
					}
					break;
				case OPEN_PAREN:
					{
					setState(196);
					nestedBooleanMatch();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(201); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==OR );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExclusionMatchContext extends ParserRuleContext {
		public TerminalNode EXCLUDE() { return getToken(IMLParser.EXCLUDE, 0); }
		public MatchContext match() {
			return getRuleContext(MatchContext.class,0);
		}
		public NestedBooleanMatchContext nestedBooleanMatch() {
			return getRuleContext(NestedBooleanMatchContext.class,0);
		}
		public TerminalNode IF() { return getToken(IMLParser.IF, 0); }
		public ExclusionMatchContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exclusionMatch; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterExclusionMatch(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitExclusionMatch(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitExclusionMatch(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExclusionMatchContext exclusionMatch() throws RecognitionException {
		ExclusionMatchContext _localctx = new ExclusionMatchContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_exclusionMatch);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(203);
			match(EXCLUDE);
			setState(205);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IF) {
				{
				setState(204);
				match(IF);
				}
			}

			setState(209);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FROM:
			case IN:
			case WHERE:
			case LATEST:
			case EARLIEST:
			case LARGEST:
			case SMALLEST:
			case ARROW:
			case OPTIONAL_ARROW:
			case REVERSE_ARROW:
				{
				setState(207);
				match();
				}
				break;
			case OPEN_PAREN:
				{
				setState(208);
				nestedBooleanMatch();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConjunctionMatchContext extends ParserRuleContext {
		public List<MatchContext> match() {
			return getRuleContexts(MatchContext.class);
		}
		public MatchContext match(int i) {
			return getRuleContext(MatchContext.class,i);
		}
		public List<NestedBooleanMatchContext> nestedBooleanMatch() {
			return getRuleContexts(NestedBooleanMatchContext.class);
		}
		public NestedBooleanMatchContext nestedBooleanMatch(int i) {
			return getRuleContext(NestedBooleanMatchContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(IMLParser.AND); }
		public TerminalNode AND(int i) {
			return getToken(IMLParser.AND, i);
		}
		public List<ExclusionMatchContext> exclusionMatch() {
			return getRuleContexts(ExclusionMatchContext.class);
		}
		public ExclusionMatchContext exclusionMatch(int i) {
			return getRuleContext(ExclusionMatchContext.class,i);
		}
		public ConjunctionMatchContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conjunctionMatch; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterConjunctionMatch(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitConjunctionMatch(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitConjunctionMatch(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConjunctionMatchContext conjunctionMatch() throws RecognitionException {
		ConjunctionMatchContext _localctx = new ConjunctionMatchContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_conjunctionMatch);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(212);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AND) {
				{
				setState(211);
				match(AND);
				}
			}

			setState(216);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FROM:
			case IN:
			case WHERE:
			case LATEST:
			case EARLIEST:
			case LARGEST:
			case SMALLEST:
			case ARROW:
			case OPTIONAL_ARROW:
			case REVERSE_ARROW:
				{
				setState(214);
				match();
				}
				break;
			case OPEN_PAREN:
				{
				setState(215);
				nestedBooleanMatch();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(224); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(224);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case AND:
					{
					{
					setState(218);
					match(AND);
					setState(221);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case FROM:
					case IN:
					case WHERE:
					case LATEST:
					case EARLIEST:
					case LARGEST:
					case SMALLEST:
					case ARROW:
					case OPTIONAL_ARROW:
					case REVERSE_ARROW:
						{
						setState(219);
						match();
						}
						break;
					case OPEN_PAREN:
						{
						setState(220);
						nestedBooleanMatch();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					}
					break;
				case EXCLUDE:
					{
					setState(223);
					exclusionMatch();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(226); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==AND || _la==EXCLUDE );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DisjunctionWhereContext extends ParserRuleContext {
		public List<WhereContext> where() {
			return getRuleContexts(WhereContext.class);
		}
		public WhereContext where(int i) {
			return getRuleContext(WhereContext.class,i);
		}
		public List<NestedBooleanWhereContext> nestedBooleanWhere() {
			return getRuleContexts(NestedBooleanWhereContext.class);
		}
		public NestedBooleanWhereContext nestedBooleanWhere(int i) {
			return getRuleContext(NestedBooleanWhereContext.class,i);
		}
		public TerminalNode EITHER() { return getToken(IMLParser.EITHER, 0); }
		public List<TerminalNode> OR() { return getTokens(IMLParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(IMLParser.OR, i);
		}
		public DisjunctionWhereContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_disjunctionWhere; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterDisjunctionWhere(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitDisjunctionWhere(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitDisjunctionWhere(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DisjunctionWhereContext disjunctionWhere() throws RecognitionException {
		DisjunctionWhereContext _localctx = new DisjunctionWhereContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_disjunctionWhere);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(229);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EITHER) {
				{
				setState(228);
				match(EITHER);
				}
			}

			setState(233);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
			case NODEREF:
				{
				setState(231);
				where();
				}
				break;
			case OPEN_PAREN:
				{
				setState(232);
				nestedBooleanWhere();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(240); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(235);
					match(OR);
					setState(238);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case IDENTIFIER:
					case NODEREF:
						{
						setState(236);
						where();
						}
						break;
					case OPEN_PAREN:
						{
						setState(237);
						nestedBooleanWhere();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(242); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConjunctionWhereContext extends ParserRuleContext {
		public List<WhereContext> where() {
			return getRuleContexts(WhereContext.class);
		}
		public WhereContext where(int i) {
			return getRuleContext(WhereContext.class,i);
		}
		public List<NestedBooleanWhereContext> nestedBooleanWhere() {
			return getRuleContexts(NestedBooleanWhereContext.class);
		}
		public NestedBooleanWhereContext nestedBooleanWhere(int i) {
			return getRuleContext(NestedBooleanWhereContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(IMLParser.AND); }
		public TerminalNode AND(int i) {
			return getToken(IMLParser.AND, i);
		}
		public ConjunctionWhereContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conjunctionWhere; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterConjunctionWhere(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitConjunctionWhere(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitConjunctionWhere(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConjunctionWhereContext conjunctionWhere() throws RecognitionException {
		ConjunctionWhereContext _localctx = new ConjunctionWhereContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_conjunctionWhere);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(245);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AND) {
				{
				setState(244);
				match(AND);
				}
			}

			setState(249);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
			case NODEREF:
				{
				setState(247);
				where();
				}
				break;
			case OPEN_PAREN:
				{
				setState(248);
				nestedBooleanWhere();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(256); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(251);
					match(AND);
					setState(254);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case IDENTIFIER:
					case NODEREF:
						{
						setState(252);
						where();
						}
						break;
					case OPEN_PAREN:
						{
						setState(253);
						nestedBooleanWhere();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(258); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NestedBooleanWhereContext extends ParserRuleContext {
		public TerminalNode OPEN_PAREN() { return getToken(IMLParser.OPEN_PAREN, 0); }
		public BooleanWhereContext booleanWhere() {
			return getRuleContext(BooleanWhereContext.class,0);
		}
		public TerminalNode CLOSE_PAREN() { return getToken(IMLParser.CLOSE_PAREN, 0); }
		public NestedBooleanWhereContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nestedBooleanWhere; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterNestedBooleanWhere(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitNestedBooleanWhere(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitNestedBooleanWhere(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NestedBooleanWhereContext nestedBooleanWhere() throws RecognitionException {
		NestedBooleanWhereContext _localctx = new NestedBooleanWhereContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_nestedBooleanWhere);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(260);
			match(OPEN_PAREN);
			setState(261);
			booleanWhere();
			setState(262);
			match(CLOSE_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BooleanWhereContext extends ParserRuleContext {
		public DisjunctionWhereContext disjunctionWhere() {
			return getRuleContext(DisjunctionWhereContext.class,0);
		}
		public ConjunctionWhereContext conjunctionWhere() {
			return getRuleContext(ConjunctionWhereContext.class,0);
		}
		public BooleanWhereContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanWhere; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterBooleanWhere(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitBooleanWhere(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitBooleanWhere(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanWhereContext booleanWhere() throws RecognitionException {
		BooleanWhereContext _localctx = new BooleanWhereContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_booleanWhere);
		try {
			setState(266);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(264);
				disjunctionWhere();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(265);
				conjunctionWhere();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhereClauseContext extends ParserRuleContext {
		public TerminalNode WHERE() { return getToken(IMLParser.WHERE, 0); }
		public BooleanWhereContext booleanWhere() {
			return getRuleContext(BooleanWhereContext.class,0);
		}
		public WhereContext where() {
			return getRuleContext(WhereContext.class,0);
		}
		public WhereClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterWhereClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitWhereClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitWhereClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereClauseContext whereClause() throws RecognitionException {
		WhereClauseContext _localctx = new WhereClauseContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(268);
			match(WHERE);
			setState(271);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(269);
				booleanWhere();
				}
				break;
			case 2:
				{
				setState(270);
				where();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhereContext extends ParserRuleContext {
		public PropertyContext property() {
			return getRuleContext(PropertyContext.class,0);
		}
		public WhereRangeContext whereRange() {
			return getRuleContext(WhereRangeContext.class,0);
		}
		public WhereValueContext whereValue() {
			return getRuleContext(WhereValueContext.class,0);
		}
		public WhereIsContext whereIs() {
			return getRuleContext(WhereIsContext.class,0);
		}
		public WhereContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_where; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterWhere(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitWhere(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitWhere(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereContext where() throws RecognitionException {
		WhereContext _localctx = new WhereContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_where);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(273);
			property();
			setState(277);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				{
				setState(274);
				whereRange();
				}
				break;
			case 2:
				{
				setState(275);
				whereValue();
				}
				break;
			case 3:
				{
				setState(276);
				whereIs();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PropertyContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(IMLParser.IDENTIFIER, 0); }
		public TerminalNode NODEREF() { return getToken(IMLParser.NODEREF, 0); }
		public PropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_property; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyContext property() throws RecognitionException {
		PropertyContext _localctx = new PropertyContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_property);
		try {
			setState(282);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(279);
				match(IDENTIFIER);
				}
				break;
			case NODEREF:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(280);
				match(NODEREF);
				setState(281);
				match(IDENTIFIER);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PropertyOfContext extends ParserRuleContext {
		public PropertyContext property() {
			return getRuleContext(PropertyContext.class,0);
		}
		public TerminalNode OF() { return getToken(IMLParser.OF, 0); }
		public TerminalNode IDENTIFIER() { return getToken(IMLParser.IDENTIFIER, 0); }
		public PropertyOfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyOf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterPropertyOf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitPropertyOf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitPropertyOf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyOfContext propertyOf() throws RecognitionException {
		PropertyOfContext _localctx = new PropertyOfContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_propertyOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(284);
			property();
			setState(285);
			match(OF);
			setState(286);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhereIsContext extends ParserRuleContext {
		public TerminalNode OF() { return getToken(IMLParser.OF, 0); }
		public TerminalNode IS() { return getToken(IMLParser.IS, 0); }
		public TerminalNode IN() { return getToken(IMLParser.IN, 0); }
		public TerminalNode EQ() { return getToken(IMLParser.EQ, 0); }
		public ConceptContext concept() {
			return getRuleContext(ConceptContext.class,0);
		}
		public DisjunctionConceptContext disjunctionConcept() {
			return getRuleContext(DisjunctionConceptContext.class,0);
		}
		public WhereIsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereIs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterWhereIs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitWhereIs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitWhereIs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereIsContext whereIs() throws RecognitionException {
		WhereIsContext _localctx = new WhereIsContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_whereIs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(288);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 549755928576L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(291);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				{
				setState(289);
				concept();
				}
				break;
			case 2:
				{
				setState(290);
				disjunctionConcept();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConceptContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(IMLParser.IDENTIFIER, 0); }
		public ConceptContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_concept; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterConcept(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitConcept(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitConcept(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConceptContext concept() throws RecognitionException {
		ConceptContext _localctx = new ConceptContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_concept);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(293);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DisjunctionConceptContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(IMLParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(IMLParser.IDENTIFIER, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(IMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(IMLParser.COMMA, i);
		}
		public DisjunctionConceptContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_disjunctionConcept; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterDisjunctionConcept(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitDisjunctionConcept(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitDisjunctionConcept(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DisjunctionConceptContext disjunctionConcept() throws RecognitionException {
		DisjunctionConceptContext _localctx = new DisjunctionConceptContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_disjunctionConcept);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(295);
			match(IDENTIFIER);
			setState(298); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(296);
				match(COMMA);
				setState(297);
				match(IDENTIFIER);
				}
				}
				setState(300); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==COMMA );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhereRangeContext extends ParserRuleContext {
		public TerminalNode BETWEEN() { return getToken(IMLParser.BETWEEN, 0); }
		public List<ComparisonOperatorContext> comparisonOperator() {
			return getRuleContexts(ComparisonOperatorContext.class);
		}
		public ComparisonOperatorContext comparisonOperator(int i) {
			return getRuleContext(ComparisonOperatorContext.class,i);
		}
		public List<TerminalNode> NUMBER() { return getTokens(IMLParser.NUMBER); }
		public TerminalNode NUMBER(int i) {
			return getToken(IMLParser.NUMBER, i);
		}
		public TerminalNode AND() { return getToken(IMLParser.AND, 0); }
		public UnitsContext units() {
			return getRuleContext(UnitsContext.class,0);
		}
		public WhereRelativeToContext whereRelativeTo() {
			return getRuleContext(WhereRelativeToContext.class,0);
		}
		public WhereRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterWhereRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitWhereRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitWhereRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereRangeContext whereRange() throws RecognitionException {
		WhereRangeContext _localctx = new WhereRangeContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_whereRange);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(302);
			match(BETWEEN);
			setState(303);
			comparisonOperator();
			setState(304);
			match(NUMBER);
			setState(305);
			match(AND);
			setState(306);
			comparisonOperator();
			setState(307);
			match(NUMBER);
			setState(309);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(308);
				units();
				}
			}

			setState(312);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==RELATIVE) {
				{
				setState(311);
				whereRelativeTo();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnitsContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(IMLParser.IDENTIFIER, 0); }
		public UnitsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_units; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterUnits(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitUnits(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitUnits(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnitsContext units() throws RecognitionException {
		UnitsContext _localctx = new UnitsContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_units);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(314);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhereRelativeToContext extends ParserRuleContext {
		public TerminalNode RELATIVE() { return getToken(IMLParser.RELATIVE, 0); }
		public TerminalNode TO() { return getToken(IMLParser.TO, 0); }
		public TerminalNode PARAMETER() { return getToken(IMLParser.PARAMETER, 0); }
		public WhereRelativeToContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereRelativeTo; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterWhereRelativeTo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitWhereRelativeTo(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitWhereRelativeTo(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereRelativeToContext whereRelativeTo() throws RecognitionException {
		WhereRelativeToContext _localctx = new WhereRelativeToContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_whereRelativeTo);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(316);
			match(RELATIVE);
			setState(317);
			match(TO);
			setState(318);
			match(PARAMETER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhereValueContext extends ParserRuleContext {
		public ComparisonOperatorContext comparisonOperator() {
			return getRuleContext(ComparisonOperatorContext.class,0);
		}
		public PropertyOfContext propertyOf() {
			return getRuleContext(PropertyOfContext.class,0);
		}
		public TerminalNode NUMBER() { return getToken(IMLParser.NUMBER, 0); }
		public UnitsContext units() {
			return getRuleContext(UnitsContext.class,0);
		}
		public WhereRelativeToContext whereRelativeTo() {
			return getRuleContext(WhereRelativeToContext.class,0);
		}
		public WhereValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterWhereValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitWhereValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitWhereValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereValueContext whereValue() throws RecognitionException {
		WhereValueContext _localctx = new WhereValueContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_whereValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(320);
			comparisonOperator();
			setState(329);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NUMBER:
				{
				{
				setState(321);
				match(NUMBER);
				setState(323);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(322);
					units();
					}
				}

				setState(326);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==RELATIVE) {
					{
					setState(325);
					whereRelativeTo();
					}
				}

				}
				}
				break;
			case IDENTIFIER:
			case NODEREF:
				{
				setState(328);
				propertyOf();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionExpressionContext extends ParserRuleContext {
		public MethodCallContext methodCall() {
			return getRuleContext(MethodCallContext.class,0);
		}
		public WhereRangeContext whereRange() {
			return getRuleContext(WhereRangeContext.class,0);
		}
		public WhereValueContext whereValue() {
			return getRuleContext(WhereValueContext.class,0);
		}
		public WhereIsContext whereIs() {
			return getRuleContext(WhereIsContext.class,0);
		}
		public FunctionExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterFunctionExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitFunctionExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitFunctionExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionExpressionContext functionExpression() throws RecognitionException {
		FunctionExpressionContext _localctx = new FunctionExpressionContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_functionExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(331);
			methodCall();
			setState(335);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				{
				setState(332);
				whereRange();
				}
				break;
			case 2:
				{
				setState(333);
				whereValue();
				}
				break;
			case 3:
				{
				setState(334);
				whereIs();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MethodCallContext extends ParserRuleContext {
		public PropertyContext property() {
			return getRuleContext(PropertyContext.class,0);
		}
		public TerminalNode OPEN_PAREN() { return getToken(IMLParser.OPEN_PAREN, 0); }
		public TerminalNode CLOSE_PAREN() { return getToken(IMLParser.CLOSE_PAREN, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public MethodCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterMethodCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitMethodCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitMethodCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodCallContext methodCall() throws RecognitionException {
		MethodCallContext _localctx = new MethodCallContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_methodCall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(337);
			property();
			setState(338);
			match(OPEN_PAREN);
			setState(340);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(339);
				argumentList();
				}
			}

			setState(342);
			match(CLOSE_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentListContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(IMLParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(IMLParser.IDENTIFIER, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(IMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(IMLParser.COMMA, i);
		}
		public ArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterArgumentList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitArgumentList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(344);
			match(IDENTIFIER);
			setState(349);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(345);
				match(COMMA);
				setState(346);
				match(IDENTIFIER);
				}
				}
				setState(351);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ComparisonOperatorContext extends ParserRuleContext {
		public TerminalNode GT() { return getToken(IMLParser.GT, 0); }
		public TerminalNode GTE() { return getToken(IMLParser.GTE, 0); }
		public TerminalNode LT() { return getToken(IMLParser.LT, 0); }
		public TerminalNode LTE() { return getToken(IMLParser.LTE, 0); }
		public TerminalNode EQ() { return getToken(IMLParser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(IMLParser.NEQ, 0); }
		public ComparisonOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparisonOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterComparisonOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitComparisonOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitComparisonOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparisonOperatorContext comparisonOperator() throws RecognitionException {
		ComparisonOperatorContext _localctx = new ComparisonOperatorContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_comparisonOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(352);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 2164663517184L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SimpleExpressionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(IMLParser.IDENTIFIER, 0); }
		public TerminalNode ON() { return getToken(IMLParser.ON, 0); }
		public TerminalNode IN() { return getToken(IMLParser.IN, 0); }
		public SimpleExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterSimpleExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitSimpleExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitSimpleExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleExpressionContext simpleExpression() throws RecognitionException {
		SimpleExpressionContext _localctx = new SimpleExpressionContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_simpleExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(355);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IN || _la==ON) {
				{
				setState(354);
				_la = _input.LA(1);
				if ( !(_la==IN || _la==ON) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(357);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u00011\u0168\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002"+
		"#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007\'\u0001"+
		"\u0000\u0004\u0000R\b\u0000\u000b\u0000\f\u0000S\u0001\u0000\u0003\u0000"+
		"W\b\u0000\u0001\u0000\u0003\u0000Z\b\u0000\u0001\u0000\u0003\u0000]\b"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0004\u0001e\b\u0001\u000b\u0001\f\u0001f\u0001\u0001\u0001\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0004\u0005}\b\u0005\u000b\u0005\f\u0005~\u0001\u0005\u0001\u0005\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006\u0087\b\u0006\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u008d\b\u0007\u0001"+
		"\b\u0001\b\u0001\t\u0003\t\u0092\b\t\u0001\t\u0003\t\u0095\b\t\u0001\t"+
		"\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u009c\b\t\u0001\t\u0003\t\u009f"+
		"\b\t\u0001\n\u0001\n\u0004\n\u00a3\b\n\u000b\n\f\n\u00a4\u0001\u000b\u0001"+
		"\u000b\u0003\u000b\u00a9\b\u000b\u0001\f\u0001\f\u0001\f\u0001\r\u0001"+
		"\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f"+
		"\u0003\u000f\u00b6\b\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0011\u0003\u0011\u00bd\b\u0011\u0001\u0011\u0001\u0011\u0003\u0011"+
		"\u00c1\b\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u00c6\b"+
		"\u0011\u0004\u0011\u00c8\b\u0011\u000b\u0011\f\u0011\u00c9\u0001\u0012"+
		"\u0001\u0012\u0003\u0012\u00ce\b\u0012\u0001\u0012\u0001\u0012\u0003\u0012"+
		"\u00d2\b\u0012\u0001\u0013\u0003\u0013\u00d5\b\u0013\u0001\u0013\u0001"+
		"\u0013\u0003\u0013\u00d9\b\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0003"+
		"\u0013\u00de\b\u0013\u0001\u0013\u0004\u0013\u00e1\b\u0013\u000b\u0013"+
		"\f\u0013\u00e2\u0001\u0014\u0003\u0014\u00e6\b\u0014\u0001\u0014\u0001"+
		"\u0014\u0003\u0014\u00ea\b\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0003"+
		"\u0014\u00ef\b\u0014\u0004\u0014\u00f1\b\u0014\u000b\u0014\f\u0014\u00f2"+
		"\u0001\u0015\u0003\u0015\u00f6\b\u0015\u0001\u0015\u0001\u0015\u0003\u0015"+
		"\u00fa\b\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u00ff\b"+
		"\u0015\u0004\u0015\u0101\b\u0015\u000b\u0015\f\u0015\u0102\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0003\u0017"+
		"\u010b\b\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u0110\b"+
		"\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u0116"+
		"\b\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0003\u001a\u011b\b\u001a"+
		"\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0003\u001c\u0124\b\u001c\u0001\u001d\u0001\u001d\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0004\u001e\u012b\b\u001e\u000b\u001e\f\u001e"+
		"\u012c\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001"+
		"\u001f\u0001\u001f\u0003\u001f\u0136\b\u001f\u0001\u001f\u0003\u001f\u0139"+
		"\b\u001f\u0001 \u0001 \u0001!\u0001!\u0001!\u0001!\u0001\"\u0001\"\u0001"+
		"\"\u0003\"\u0144\b\"\u0001\"\u0003\"\u0147\b\"\u0001\"\u0003\"\u014a\b"+
		"\"\u0001#\u0001#\u0001#\u0001#\u0003#\u0150\b#\u0001$\u0001$\u0001$\u0003"+
		"$\u0155\b$\u0001$\u0001$\u0001%\u0001%\u0001%\u0005%\u015c\b%\n%\f%\u015f"+
		"\t%\u0001&\u0001&\u0001\'\u0003\'\u0164\b\'\u0001\'\u0001\'\u0001\'\u0000"+
		"\u0000(\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018"+
		"\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLN\u0000\u0005\u0001\u0000\u001f"+
		"!\u0001\u0000\u0015\u0018\u0002\u0000\u000e\u0010\'\'\u0001\u0000#(\u0001"+
		"\u0000\u0010\u0011\u0173\u0000Q\u0001\u0000\u0000\u0000\u0002`\u0001\u0000"+
		"\u0000\u0000\u0004j\u0001\u0000\u0000\u0000\u0006n\u0001\u0000\u0000\u0000"+
		"\bq\u0001\u0000\u0000\u0000\nw\u0001\u0000\u0000\u0000\f\u0086\u0001\u0000"+
		"\u0000\u0000\u000e\u0088\u0001\u0000\u0000\u0000\u0010\u008e\u0001\u0000"+
		"\u0000\u0000\u0012\u0091\u0001\u0000\u0000\u0000\u0014\u00a2\u0001\u0000"+
		"\u0000\u0000\u0016\u00a6\u0001\u0000\u0000\u0000\u0018\u00aa\u0001\u0000"+
		"\u0000\u0000\u001a\u00ad\u0001\u0000\u0000\u0000\u001c\u00b0\u0001\u0000"+
		"\u0000\u0000\u001e\u00b5\u0001\u0000\u0000\u0000 \u00b7\u0001\u0000\u0000"+
		"\u0000\"\u00bc\u0001\u0000\u0000\u0000$\u00cb\u0001\u0000\u0000\u0000"+
		"&\u00d4\u0001\u0000\u0000\u0000(\u00e5\u0001\u0000\u0000\u0000*\u00f5"+
		"\u0001\u0000\u0000\u0000,\u0104\u0001\u0000\u0000\u0000.\u010a\u0001\u0000"+
		"\u0000\u00000\u010c\u0001\u0000\u0000\u00002\u0111\u0001\u0000\u0000\u0000"+
		"4\u011a\u0001\u0000\u0000\u00006\u011c\u0001\u0000\u0000\u00008\u0120"+
		"\u0001\u0000\u0000\u0000:\u0125\u0001\u0000\u0000\u0000<\u0127\u0001\u0000"+
		"\u0000\u0000>\u012e\u0001\u0000\u0000\u0000@\u013a\u0001\u0000\u0000\u0000"+
		"B\u013c\u0001\u0000\u0000\u0000D\u0140\u0001\u0000\u0000\u0000F\u014b"+
		"\u0001\u0000\u0000\u0000H\u0151\u0001\u0000\u0000\u0000J\u0158\u0001\u0000"+
		"\u0000\u0000L\u0160\u0001\u0000\u0000\u0000N\u0163\u0001\u0000\u0000\u0000"+
		"PR\u0003\u0006\u0003\u0000QP\u0001\u0000\u0000\u0000RS\u0001\u0000\u0000"+
		"\u0000SQ\u0001\u0000\u0000\u0000ST\u0001\u0000\u0000\u0000TV\u0001\u0000"+
		"\u0000\u0000UW\u0003\n\u0005\u0000VU\u0001\u0000\u0000\u0000VW\u0001\u0000"+
		"\u0000\u0000WY\u0001\u0000\u0000\u0000XZ\u0003\u0002\u0001\u0000YX\u0001"+
		"\u0000\u0000\u0000YZ\u0001\u0000\u0000\u0000Z\\\u0001\u0000\u0000\u0000"+
		"[]\u0003\u0004\u0002\u0000\\[\u0001\u0000\u0000\u0000\\]\u0001\u0000\u0000"+
		"\u0000]^\u0001\u0000\u0000\u0000^_\u0005\u0000\u0000\u0001_\u0001\u0001"+
		"\u0000\u0000\u0000`a\u0005\u0003\u0000\u0000ad\u0005\u001b\u0000\u0000"+
		"bc\u0005)\u0000\u0000ce\u0005-\u0000\u0000db\u0001\u0000\u0000\u0000e"+
		"f\u0001\u0000\u0000\u0000fd\u0001\u0000\u0000\u0000fg\u0001\u0000\u0000"+
		"\u0000gh\u0001\u0000\u0000\u0000hi\u0005\u001c\u0000\u0000i\u0003\u0001"+
		"\u0000\u0000\u0000jk\u0005\u0004\u0000\u0000kl\u0005\'\u0000\u0000lm\u0005"+
		"-\u0000\u0000m\u0005\u0001\u0000\u0000\u0000no\u0005\u0002\u0000\u0000"+
		"op\u0003\b\u0004\u0000p\u0007\u0001\u0000\u0000\u0000qr\u0005-\u0000\u0000"+
		"rs\u0005\u0006\u0000\u0000st\u0005\u001b\u0000\u0000tu\u0003\u000e\u0007"+
		"\u0000uv\u0005\u001c\u0000\u0000v\t\u0001\u0000\u0000\u0000wx\u0005\u0005"+
		"\u0000\u0000x|\u0005\u001b\u0000\u0000yz\u0005*\u0000\u0000z{\u0005\'"+
		"\u0000\u0000{}\u0003\f\u0006\u0000|y\u0001\u0000\u0000\u0000}~\u0001\u0000"+
		"\u0000\u0000~|\u0001\u0000\u0000\u0000~\u007f\u0001\u0000\u0000\u0000"+
		"\u007f\u0080\u0001\u0000\u0000\u0000\u0080\u0081\u0005\u001c\u0000\u0000"+
		"\u0081\u000b\u0001\u0000\u0000\u0000\u0082\u0083\u0005)\u0000\u0000\u0083"+
		"\u0084\u0005\u0001\u0000\u0000\u0084\u0087\u0005*\u0000\u0000\u0085\u0087"+
		"\u0005-\u0000\u0000\u0086\u0082\u0001\u0000\u0000\u0000\u0086\u0085\u0001"+
		"\u0000\u0000\u0000\u0087\r\u0001\u0000\u0000\u0000\u0088\u0089\u0005\u0007"+
		"\u0000\u0000\u0089\u008c\u0003\u0010\b\u0000\u008a\u008d\u0003\u0012\t"+
		"\u0000\u008b\u008d\u0003\u001e\u000f\u0000\u008c\u008a\u0001\u0000\u0000"+
		"\u0000\u008c\u008b\u0001\u0000\u0000\u0000\u008d\u000f\u0001\u0000\u0000"+
		"\u0000\u008e\u008f\u0005*\u0000\u0000\u008f\u0011\u0001\u0000\u0000\u0000"+
		"\u0090\u0092\u0003\u0016\u000b\u0000\u0091\u0090\u0001\u0000\u0000\u0000"+
		"\u0091\u0092\u0001\u0000\u0000\u0000\u0092\u0094\u0001\u0000\u0000\u0000"+
		"\u0093\u0095\u0003\u0014\n\u0000\u0094\u0093\u0001\u0000\u0000\u0000\u0094"+
		"\u0095\u0001\u0000\u0000\u0000\u0095\u009b\u0001\u0000\u0000\u0000\u0096"+
		"\u009c\u0003\u0018\f\u0000\u0097\u0098\u0003\u001a\r\u0000\u0098\u0099"+
		"\u00030\u0018\u0000\u0099\u009c\u0001\u0000\u0000\u0000\u009a\u009c\u0003"+
		"0\u0018\u0000\u009b\u0096\u0001\u0000\u0000\u0000\u009b\u0097\u0001\u0000"+
		"\u0000\u0000\u009b\u009a\u0001\u0000\u0000\u0000\u009c\u009e\u0001\u0000"+
		"\u0000\u0000\u009d\u009f\u0003\u001c\u000e\u0000\u009e\u009d\u0001\u0000"+
		"\u0000\u0000\u009e\u009f\u0001\u0000\u0000\u0000\u009f\u0013\u0001\u0000"+
		"\u0000\u0000\u00a0\u00a1\u0007\u0000\u0000\u0000\u00a1\u00a3\u0005*\u0000"+
		"\u0000\u00a2\u00a0\u0001\u0000\u0000\u0000\u00a3\u00a4\u0001\u0000\u0000"+
		"\u0000\u00a4\u00a2\u0001\u0000\u0000\u0000\u00a4\u00a5\u0001\u0000\u0000"+
		"\u0000\u00a5\u0015\u0001\u0000\u0000\u0000\u00a6\u00a8\u0007\u0001\u0000"+
		"\u0000\u00a7\u00a9\u0005.\u0000\u0000\u00a8\u00a7\u0001\u0000\u0000\u0000"+
		"\u00a8\u00a9\u0001\u0000\u0000\u0000\u00a9\u0017\u0001\u0000\u0000\u0000"+
		"\u00aa\u00ab\u0005\u0010\u0000\u0000\u00ab\u00ac\u0005*\u0000\u0000\u00ac"+
		"\u0019\u0001\u0000\u0000\u0000\u00ad\u00ae\u0005\b\u0000\u0000\u00ae\u00af"+
		"\u0005*\u0000\u0000\u00af\u001b\u0001\u0000\u0000\u0000\u00b0\u00b1\u0005"+
		"\u0006\u0000\u0000\u00b1\u00b2\u0005*\u0000\u0000\u00b2\u001d\u0001\u0000"+
		"\u0000\u0000\u00b3\u00b6\u0003\"\u0011\u0000\u00b4\u00b6\u0003&\u0013"+
		"\u0000\u00b5\u00b3\u0001\u0000\u0000\u0000\u00b5\u00b4\u0001\u0000\u0000"+
		"\u0000\u00b6\u001f\u0001\u0000\u0000\u0000\u00b7\u00b8\u0005\u001d\u0000"+
		"\u0000\u00b8\u00b9\u0003\u001e\u000f\u0000\u00b9\u00ba\u0005\u001e\u0000"+
		"\u0000\u00ba!\u0001\u0000\u0000\u0000\u00bb\u00bd\u0005\t\u0000\u0000"+
		"\u00bc\u00bb\u0001\u0000\u0000\u0000\u00bc\u00bd\u0001\u0000\u0000\u0000"+
		"\u00bd\u00c0\u0001\u0000\u0000\u0000\u00be\u00c1\u0003\u0012\t\u0000\u00bf"+
		"\u00c1\u0003 \u0010\u0000\u00c0\u00be\u0001\u0000\u0000\u0000\u00c0\u00bf"+
		"\u0001\u0000\u0000\u0000\u00c1\u00c7\u0001\u0000\u0000\u0000\u00c2\u00c5"+
		"\u0005\n\u0000\u0000\u00c3\u00c6\u0003\u0012\t\u0000\u00c4\u00c6\u0003"+
		" \u0010\u0000\u00c5\u00c3\u0001\u0000\u0000\u0000\u00c5\u00c4\u0001\u0000"+
		"\u0000\u0000\u00c6\u00c8\u0001\u0000\u0000\u0000\u00c7\u00c2\u0001\u0000"+
		"\u0000\u0000\u00c8\u00c9\u0001\u0000\u0000\u0000\u00c9\u00c7\u0001\u0000"+
		"\u0000\u0000\u00c9\u00ca\u0001\u0000\u0000\u0000\u00ca#\u0001\u0000\u0000"+
		"\u0000\u00cb\u00cd\u0005\f\u0000\u0000\u00cc\u00ce\u0005\r\u0000\u0000"+
		"\u00cd\u00cc\u0001\u0000\u0000\u0000\u00cd\u00ce\u0001\u0000\u0000\u0000"+
		"\u00ce\u00d1\u0001\u0000\u0000\u0000\u00cf\u00d2\u0003\u0012\t\u0000\u00d0"+
		"\u00d2\u0003 \u0010\u0000\u00d1\u00cf\u0001\u0000\u0000\u0000\u00d1\u00d0"+
		"\u0001\u0000\u0000\u0000\u00d2%\u0001\u0000\u0000\u0000\u00d3\u00d5\u0005"+
		"\u000b\u0000\u0000\u00d4\u00d3\u0001\u0000\u0000\u0000\u00d4\u00d5\u0001"+
		"\u0000\u0000\u0000\u00d5\u00d8\u0001\u0000\u0000\u0000\u00d6\u00d9\u0003"+
		"\u0012\t\u0000\u00d7\u00d9\u0003 \u0010\u0000\u00d8\u00d6\u0001\u0000"+
		"\u0000\u0000\u00d8\u00d7\u0001\u0000\u0000\u0000\u00d9\u00e0\u0001\u0000"+
		"\u0000\u0000\u00da\u00dd\u0005\u000b\u0000\u0000\u00db\u00de\u0003\u0012"+
		"\t\u0000\u00dc\u00de\u0003 \u0010\u0000\u00dd\u00db\u0001\u0000\u0000"+
		"\u0000\u00dd\u00dc\u0001\u0000\u0000\u0000\u00de\u00e1\u0001\u0000\u0000"+
		"\u0000\u00df\u00e1\u0003$\u0012\u0000\u00e0\u00da\u0001\u0000\u0000\u0000"+
		"\u00e0\u00df\u0001\u0000\u0000\u0000\u00e1\u00e2\u0001\u0000\u0000\u0000"+
		"\u00e2\u00e0\u0001\u0000\u0000\u0000\u00e2\u00e3\u0001\u0000\u0000\u0000"+
		"\u00e3\'\u0001\u0000\u0000\u0000\u00e4\u00e6\u0005\t\u0000\u0000\u00e5"+
		"\u00e4\u0001\u0000\u0000\u0000\u00e5\u00e6\u0001\u0000\u0000\u0000\u00e6"+
		"\u00e9\u0001\u0000\u0000\u0000\u00e7\u00ea\u00032\u0019\u0000\u00e8\u00ea"+
		"\u0003,\u0016\u0000\u00e9\u00e7\u0001\u0000\u0000\u0000\u00e9\u00e8\u0001"+
		"\u0000\u0000\u0000\u00ea\u00f0\u0001\u0000\u0000\u0000\u00eb\u00ee\u0005"+
		"\n\u0000\u0000\u00ec\u00ef\u00032\u0019\u0000\u00ed\u00ef\u0003,\u0016"+
		"\u0000\u00ee\u00ec\u0001\u0000\u0000\u0000\u00ee\u00ed\u0001\u0000\u0000"+
		"\u0000\u00ef\u00f1\u0001\u0000\u0000\u0000\u00f0\u00eb\u0001\u0000\u0000"+
		"\u0000\u00f1\u00f2\u0001\u0000\u0000\u0000\u00f2\u00f0\u0001\u0000\u0000"+
		"\u0000\u00f2\u00f3\u0001\u0000\u0000\u0000\u00f3)\u0001\u0000\u0000\u0000"+
		"\u00f4\u00f6\u0005\u000b\u0000\u0000\u00f5\u00f4\u0001\u0000\u0000\u0000"+
		"\u00f5\u00f6\u0001\u0000\u0000\u0000\u00f6\u00f9\u0001\u0000\u0000\u0000"+
		"\u00f7\u00fa\u00032\u0019\u0000\u00f8\u00fa\u0003,\u0016\u0000\u00f9\u00f7"+
		"\u0001\u0000\u0000\u0000\u00f9\u00f8\u0001\u0000\u0000\u0000\u00fa\u0100"+
		"\u0001\u0000\u0000\u0000\u00fb\u00fe\u0005\u000b\u0000\u0000\u00fc\u00ff"+
		"\u00032\u0019\u0000\u00fd\u00ff\u0003,\u0016\u0000\u00fe\u00fc\u0001\u0000"+
		"\u0000\u0000\u00fe\u00fd\u0001\u0000\u0000\u0000\u00ff\u0101\u0001\u0000"+
		"\u0000\u0000\u0100\u00fb\u0001\u0000\u0000\u0000\u0101\u0102\u0001\u0000"+
		"\u0000\u0000\u0102\u0100\u0001\u0000\u0000\u0000\u0102\u0103\u0001\u0000"+
		"\u0000\u0000\u0103+\u0001\u0000\u0000\u0000\u0104\u0105\u0005\u001d\u0000"+
		"\u0000\u0105\u0106\u0003.\u0017\u0000\u0106\u0107\u0005\u001e\u0000\u0000"+
		"\u0107-\u0001\u0000\u0000\u0000\u0108\u010b\u0003(\u0014\u0000\u0109\u010b"+
		"\u0003*\u0015\u0000\u010a\u0108\u0001\u0000\u0000\u0000\u010a\u0109\u0001"+
		"\u0000\u0000\u0000\u010b/\u0001\u0000\u0000\u0000\u010c\u010f\u0005\u0014"+
		"\u0000\u0000\u010d\u0110\u0003.\u0017\u0000\u010e\u0110\u00032\u0019\u0000"+
		"\u010f\u010d\u0001\u0000\u0000\u0000\u010f\u010e\u0001\u0000\u0000\u0000"+
		"\u01101\u0001\u0000\u0000\u0000\u0111\u0115\u00034\u001a\u0000\u0112\u0116"+
		"\u0003>\u001f\u0000\u0113\u0116\u0003D\"\u0000\u0114\u0116\u00038\u001c"+
		"\u0000\u0115\u0112\u0001\u0000\u0000\u0000\u0115\u0113\u0001\u0000\u0000"+
		"\u0000\u0115\u0114\u0001\u0000\u0000\u0000\u01163\u0001\u0000\u0000\u0000"+
		"\u0117\u011b\u0005*\u0000\u0000\u0118\u0119\u0005+\u0000\u0000\u0119\u011b"+
		"\u0005*\u0000\u0000\u011a\u0117\u0001\u0000\u0000\u0000\u011a\u0118\u0001"+
		"\u0000\u0000\u0000\u011b5\u0001\u0000\u0000\u0000\u011c\u011d\u00034\u001a"+
		"\u0000\u011d\u011e\u0005\u000e\u0000\u0000\u011e\u011f\u0005*\u0000\u0000"+
		"\u011f7\u0001\u0000\u0000\u0000\u0120\u0123\u0007\u0002\u0000\u0000\u0121"+
		"\u0124\u0003:\u001d\u0000\u0122\u0124\u0003<\u001e\u0000\u0123\u0121\u0001"+
		"\u0000\u0000\u0000\u0123\u0122\u0001\u0000\u0000\u0000\u01249\u0001\u0000"+
		"\u0000\u0000\u0125\u0126\u0005*\u0000\u0000\u0126;\u0001\u0000\u0000\u0000"+
		"\u0127\u012a\u0005*\u0000\u0000\u0128\u0129\u0005\"\u0000\u0000\u0129"+
		"\u012b\u0005*\u0000\u0000\u012a\u0128\u0001\u0000\u0000\u0000\u012b\u012c"+
		"\u0001\u0000\u0000\u0000\u012c\u012a\u0001\u0000\u0000\u0000\u012c\u012d"+
		"\u0001\u0000\u0000\u0000\u012d=\u0001\u0000\u0000\u0000\u012e\u012f\u0005"+
		"\u0012\u0000\u0000\u012f\u0130\u0003L&\u0000\u0130\u0131\u0005.\u0000"+
		"\u0000\u0131\u0132\u0005\u000b\u0000\u0000\u0132\u0133\u0003L&\u0000\u0133"+
		"\u0135\u0005.\u0000\u0000\u0134\u0136\u0003@ \u0000\u0135\u0134\u0001"+
		"\u0000\u0000\u0000\u0135\u0136\u0001\u0000\u0000\u0000\u0136\u0138\u0001"+
		"\u0000\u0000\u0000\u0137\u0139\u0003B!\u0000\u0138\u0137\u0001\u0000\u0000"+
		"\u0000\u0138\u0139\u0001\u0000\u0000\u0000\u0139?\u0001\u0000\u0000\u0000"+
		"\u013a\u013b\u0005*\u0000\u0000\u013bA\u0001\u0000\u0000\u0000\u013c\u013d"+
		"\u0005\u0019\u0000\u0000\u013d\u013e\u0005\u001a\u0000\u0000\u013e\u013f"+
		"\u0005,\u0000\u0000\u013fC\u0001\u0000\u0000\u0000\u0140\u0149\u0003L"+
		"&\u0000\u0141\u0143\u0005.\u0000\u0000\u0142\u0144\u0003@ \u0000\u0143"+
		"\u0142\u0001\u0000\u0000\u0000\u0143\u0144\u0001\u0000\u0000\u0000\u0144"+
		"\u0146\u0001\u0000\u0000\u0000\u0145\u0147\u0003B!\u0000\u0146\u0145\u0001"+
		"\u0000\u0000\u0000\u0146\u0147\u0001\u0000\u0000\u0000\u0147\u014a\u0001"+
		"\u0000\u0000\u0000\u0148\u014a\u00036\u001b\u0000\u0149\u0141\u0001\u0000"+
		"\u0000\u0000\u0149\u0148\u0001\u0000\u0000\u0000\u014aE\u0001\u0000\u0000"+
		"\u0000\u014b\u014f\u0003H$\u0000\u014c\u0150\u0003>\u001f\u0000\u014d"+
		"\u0150\u0003D\"\u0000\u014e\u0150\u00038\u001c\u0000\u014f\u014c\u0001"+
		"\u0000\u0000\u0000\u014f\u014d\u0001\u0000\u0000\u0000\u014f\u014e\u0001"+
		"\u0000\u0000\u0000\u0150G\u0001\u0000\u0000\u0000\u0151\u0152\u00034\u001a"+
		"\u0000\u0152\u0154\u0005\u001d\u0000\u0000\u0153\u0155\u0003J%\u0000\u0154"+
		"\u0153\u0001\u0000\u0000\u0000\u0154\u0155\u0001\u0000\u0000\u0000\u0155"+
		"\u0156\u0001\u0000\u0000\u0000\u0156\u0157\u0005\u001e\u0000\u0000\u0157"+
		"I\u0001\u0000\u0000\u0000\u0158\u015d\u0005*\u0000\u0000\u0159\u015a\u0005"+
		"\"\u0000\u0000\u015a\u015c\u0005*\u0000\u0000\u015b\u0159\u0001\u0000"+
		"\u0000\u0000\u015c\u015f\u0001\u0000\u0000\u0000\u015d\u015b\u0001\u0000"+
		"\u0000\u0000\u015d\u015e\u0001\u0000\u0000\u0000\u015eK\u0001\u0000\u0000"+
		"\u0000\u015f\u015d\u0001\u0000\u0000\u0000\u0160\u0161\u0007\u0003\u0000"+
		"\u0000\u0161M\u0001\u0000\u0000\u0000\u0162\u0164\u0007\u0004\u0000\u0000"+
		"\u0163\u0162\u0001\u0000\u0000\u0000\u0163\u0164\u0001\u0000\u0000\u0000"+
		"\u0164\u0165\u0001\u0000\u0000\u0000\u0165\u0166\u0005*\u0000\u0000\u0166"+
		"O\u0001\u0000\u0000\u00001SVY\\f~\u0086\u008c\u0091\u0094\u009b\u009e"+
		"\u00a4\u00a8\u00b5\u00bc\u00c0\u00c5\u00c9\u00cd\u00d1\u00d4\u00d8\u00dd"+
		"\u00e0\u00e2\u00e5\u00e9\u00ee\u00f2\u00f5\u00f9\u00fe\u0102\u010a\u010f"+
		"\u0115\u011a\u0123\u012c\u0135\u0138\u0143\u0146\u0149\u014f\u0154\u015d"+
		"\u0163";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}