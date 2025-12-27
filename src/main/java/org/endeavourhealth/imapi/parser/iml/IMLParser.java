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
		T__0=1, DEFINE=2, AS=3, MATCH=4, FROM=5, EITHER=6, OR=7, AND=8, EXCLUDE=9, 
		IF=10, OF=11, IS=12, IN=13, ON=14, BETWEEN=15, WITH=16, WHERE=17, OPEN_BRACE=18, 
		CLOSE_BRACE=19, OPEN_PAREN=20, CLOSE_PAREN=21, ARROW=22, OPTIONAL_ARROW=23, 
		COMMA=24, GT=25, GTE=26, LT=27, LTE=28, EQ=29, NEQ=30, PNAME_NS=31, IRIREF=32, 
		IDENTIFIER=33, NUMBER=34, WS=35, COMMENT=36, BLOCK_COMMENT=37, VARIABLE=38;
	public static final int
		RULE_iml = 0, RULE_definition = 1, RULE_matchStatement = 2, RULE_entity = 3, 
		RULE_fromStatement = 4, RULE_booleanExpression = 5, RULE_nestedBooleanExpression = 6, 
		RULE_disjunction = 7, RULE_conjunction = 8, RULE_whereStatement = 9, RULE_property = 10, 
		RULE_expression = 11, RULE_exclusion = 12, RULE_pathSegment = 13, RULE_conceptExpression = 14, 
		RULE_rangeExpression = 15, RULE_range = 16, RULE_valueCompare = 17, RULE_functionExpression = 18, 
		RULE_methodCall = 19, RULE_argumentList = 20, RULE_comparisonOperator = 21, 
		RULE_simpleExpression = 22;
	private static String[] makeRuleNames() {
		return new String[] {
			"iml", "definition", "matchStatement", "entity", "fromStatement", "booleanExpression", 
			"nestedBooleanExpression", "disjunction", "conjunction", "whereStatement", 
			"property", "expression", "exclusion", "pathSegment", "conceptExpression", 
			"rangeExpression", "range", "valueCompare", "functionExpression", "methodCall", 
			"argumentList", "comparisonOperator", "simpleExpression"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, "'{'", "'}'", "'('", "')'", "'->'", 
			"'?->'", "','", "'>'", "'>='", "'<'", "'<='", "'='", "'!='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "DEFINE", "AS", "MATCH", "FROM", "EITHER", "OR", "AND", "EXCLUDE", 
			"IF", "OF", "IS", "IN", "ON", "BETWEEN", "WITH", "WHERE", "OPEN_BRACE", 
			"CLOSE_BRACE", "OPEN_PAREN", "CLOSE_PAREN", "ARROW", "OPTIONAL_ARROW", 
			"COMMA", "GT", "GTE", "LT", "LTE", "EQ", "NEQ", "PNAME_NS", "IRIREF", 
			"IDENTIFIER", "NUMBER", "WS", "COMMENT", "BLOCK_COMMENT", "VARIABLE"
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
		public DefinitionContext definition() {
			return getRuleContext(DefinitionContext.class,0);
		}
		public TerminalNode EOF() { return getToken(IMLParser.EOF, 0); }
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
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(46);
			definition();
			setState(47);
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
	public static class DefinitionContext extends ParserRuleContext {
		public TerminalNode DEFINE() { return getToken(IMLParser.DEFINE, 0); }
		public TerminalNode IRIREF() { return getToken(IMLParser.IRIREF, 0); }
		public TerminalNode AS() { return getToken(IMLParser.AS, 0); }
		public TerminalNode OPEN_BRACE() { return getToken(IMLParser.OPEN_BRACE, 0); }
		public MatchStatementContext matchStatement() {
			return getRuleContext(MatchStatementContext.class,0);
		}
		public TerminalNode CLOSE_BRACE() { return getToken(IMLParser.CLOSE_BRACE, 0); }
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
		enterRule(_localctx, 2, RULE_definition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49);
			match(DEFINE);
			setState(50);
			match(IRIREF);
			setState(51);
			match(AS);
			setState(52);
			match(OPEN_BRACE);
			setState(53);
			matchStatement();
			setState(54);
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
	public static class MatchStatementContext extends ParserRuleContext {
		public TerminalNode MATCH() { return getToken(IMLParser.MATCH, 0); }
		public EntityContext entity() {
			return getRuleContext(EntityContext.class,0);
		}
		public FromStatementContext fromStatement() {
			return getRuleContext(FromStatementContext.class,0);
		}
		public List<BooleanExpressionContext> booleanExpression() {
			return getRuleContexts(BooleanExpressionContext.class);
		}
		public BooleanExpressionContext booleanExpression(int i) {
			return getRuleContext(BooleanExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public MatchStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterMatchStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitMatchStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitMatchStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchStatementContext matchStatement() throws RecognitionException {
		MatchStatementContext _localctx = new MatchStatementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_matchStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
			match(MATCH);
			setState(57);
			entity();
			setState(59);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FROM) {
				{
				setState(58);
				fromStatement();
				}
			}

			setState(65);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8591008576L) != 0)) {
				{
				setState(63);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case EITHER:
				case AND:
				case EXCLUDE:
					{
					setState(61);
					booleanExpression();
					}
					break;
				case IN:
				case ON:
				case OPEN_PAREN:
				case IDENTIFIER:
					{
					setState(62);
					expression();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(67);
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
	public static class EntityContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(IMLParser.IDENTIFIER, 0); }
		public TerminalNode VARIABLE() { return getToken(IMLParser.VARIABLE, 0); }
		public EntityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_entity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterEntity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitEntity(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitEntity(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EntityContext entity() throws RecognitionException {
		EntityContext _localctx = new EntityContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_entity);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(69);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VARIABLE) {
				{
				setState(68);
				match(VARIABLE);
				}
			}

			setState(71);
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
	public static class FromStatementContext extends ParserRuleContext {
		public TerminalNode FROM() { return getToken(IMLParser.FROM, 0); }
		public TerminalNode IDENTIFIER() { return getToken(IMLParser.IDENTIFIER, 0); }
		public FromStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fromStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterFromStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitFromStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitFromStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FromStatementContext fromStatement() throws RecognitionException {
		FromStatementContext _localctx = new FromStatementContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_fromStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73);
			match(FROM);
			setState(74);
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
	public static class BooleanExpressionContext extends ParserRuleContext {
		public DisjunctionContext disjunction() {
			return getRuleContext(DisjunctionContext.class,0);
		}
		public ConjunctionContext conjunction() {
			return getRuleContext(ConjunctionContext.class,0);
		}
		public ExclusionContext exclusion() {
			return getRuleContext(ExclusionContext.class,0);
		}
		public BooleanExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterBooleanExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitBooleanExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitBooleanExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanExpressionContext booleanExpression() throws RecognitionException {
		BooleanExpressionContext _localctx = new BooleanExpressionContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_booleanExpression);
		try {
			setState(79);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EITHER:
				enterOuterAlt(_localctx, 1);
				{
				setState(76);
				disjunction();
				}
				break;
			case AND:
				enterOuterAlt(_localctx, 2);
				{
				setState(77);
				conjunction();
				}
				break;
			case EXCLUDE:
				enterOuterAlt(_localctx, 3);
				{
				setState(78);
				exclusion();
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
	public static class NestedBooleanExpressionContext extends ParserRuleContext {
		public TerminalNode OPEN_PAREN() { return getToken(IMLParser.OPEN_PAREN, 0); }
		public BooleanExpressionContext booleanExpression() {
			return getRuleContext(BooleanExpressionContext.class,0);
		}
		public TerminalNode CLOSE_PAREN() { return getToken(IMLParser.CLOSE_PAREN, 0); }
		public NestedBooleanExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nestedBooleanExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterNestedBooleanExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitNestedBooleanExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitNestedBooleanExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NestedBooleanExpressionContext nestedBooleanExpression() throws RecognitionException {
		NestedBooleanExpressionContext _localctx = new NestedBooleanExpressionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_nestedBooleanExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			match(OPEN_PAREN);
			setState(82);
			booleanExpression();
			setState(83);
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
	public static class DisjunctionContext extends ParserRuleContext {
		public TerminalNode EITHER() { return getToken(IMLParser.EITHER, 0); }
		public List<WhereStatementContext> whereStatement() {
			return getRuleContexts(WhereStatementContext.class);
		}
		public WhereStatementContext whereStatement(int i) {
			return getRuleContext(WhereStatementContext.class,i);
		}
		public List<TerminalNode> OR() { return getTokens(IMLParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(IMLParser.OR, i);
		}
		public DisjunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_disjunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterDisjunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitDisjunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitDisjunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DisjunctionContext disjunction() throws RecognitionException {
		DisjunctionContext _localctx = new DisjunctionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_disjunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			match(EITHER);
			setState(86);
			whereStatement();
			setState(89); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(87);
				match(OR);
				setState(88);
				whereStatement();
				}
				}
				setState(91); 
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
	public static class ConjunctionContext extends ParserRuleContext {
		public List<TerminalNode> AND() { return getTokens(IMLParser.AND); }
		public TerminalNode AND(int i) {
			return getToken(IMLParser.AND, i);
		}
		public List<WhereStatementContext> whereStatement() {
			return getRuleContexts(WhereStatementContext.class);
		}
		public WhereStatementContext whereStatement(int i) {
			return getRuleContext(WhereStatementContext.class,i);
		}
		public List<NestedBooleanExpressionContext> nestedBooleanExpression() {
			return getRuleContexts(NestedBooleanExpressionContext.class);
		}
		public NestedBooleanExpressionContext nestedBooleanExpression(int i) {
			return getRuleContext(NestedBooleanExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ConjunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conjunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterConjunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitConjunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitConjunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConjunctionContext conjunction() throws RecognitionException {
		ConjunctionContext _localctx = new ConjunctionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_conjunction);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(99); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(93);
					match(AND);
					setState(97);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
					case 1:
						{
						setState(94);
						whereStatement();
						}
						break;
					case 2:
						{
						setState(95);
						nestedBooleanExpression();
						}
						break;
					case 3:
						{
						setState(96);
						expression();
						}
						break;
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(101); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
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
	public static class WhereStatementContext extends ParserRuleContext {
		public TerminalNode WHERE() { return getToken(IMLParser.WHERE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<PathSegmentContext> pathSegment() {
			return getRuleContexts(PathSegmentContext.class);
		}
		public PathSegmentContext pathSegment(int i) {
			return getRuleContext(PathSegmentContext.class,i);
		}
		public WhereStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterWhereStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitWhereStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitWhereStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereStatementContext whereStatement() throws RecognitionException {
		WhereStatementContext _localctx = new WhereStatementContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_whereStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ARROW || _la==OPTIONAL_ARROW) {
				{
				{
				setState(103);
				pathSegment();
				}
				}
				setState(108);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(109);
			match(WHERE);
			setState(110);
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
	public static class PropertyContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(IMLParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(IMLParser.IDENTIFIER, i);
		}
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
		enterRule(_localctx, 20, RULE_property);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			match(IDENTIFIER);
			setState(113);
			match(T__0);
			setState(114);
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
	public static class ExpressionContext extends ParserRuleContext {
		public NestedBooleanExpressionContext nestedBooleanExpression() {
			return getRuleContext(NestedBooleanExpressionContext.class,0);
		}
		public FunctionExpressionContext functionExpression() {
			return getRuleContext(FunctionExpressionContext.class,0);
		}
		public ConceptExpressionContext conceptExpression() {
			return getRuleContext(ConceptExpressionContext.class,0);
		}
		public SimpleExpressionContext simpleExpression() {
			return getRuleContext(SimpleExpressionContext.class,0);
		}
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
		enterRule(_localctx, 22, RULE_expression);
		try {
			setState(120);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(116);
				nestedBooleanExpression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(117);
				functionExpression();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(118);
				conceptExpression();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(119);
				simpleExpression();
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
	public static class ExclusionContext extends ParserRuleContext {
		public List<TerminalNode> EXCLUDE() { return getTokens(IMLParser.EXCLUDE); }
		public TerminalNode EXCLUDE(int i) {
			return getToken(IMLParser.EXCLUDE, i);
		}
		public List<TerminalNode> IF() { return getTokens(IMLParser.IF); }
		public TerminalNode IF(int i) {
			return getToken(IMLParser.IF, i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ExclusionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exclusion; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterExclusion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitExclusion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitExclusion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExclusionContext exclusion() throws RecognitionException {
		ExclusionContext _localctx = new ExclusionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_exclusion);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(125); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(122);
					match(EXCLUDE);
					setState(123);
					match(IF);
					setState(124);
					expression();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(127); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
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
	public static class PathSegmentContext extends ParserRuleContext {
		public TerminalNode ARROW() { return getToken(IMLParser.ARROW, 0); }
		public TerminalNode OPTIONAL_ARROW() { return getToken(IMLParser.OPTIONAL_ARROW, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(IMLParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(IMLParser.IDENTIFIER, i);
		}
		public PathSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterPathSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitPathSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitPathSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathSegmentContext pathSegment() throws RecognitionException {
		PathSegmentContext _localctx = new PathSegmentContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_pathSegment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			_la = _input.LA(1);
			if ( !(_la==ARROW || _la==OPTIONAL_ARROW) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(131); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(130);
				match(IDENTIFIER);
				}
				}
				setState(133); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==IDENTIFIER );
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
	public static class ConceptExpressionContext extends ParserRuleContext {
		public PropertyContext property() {
			return getRuleContext(PropertyContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(IMLParser.IDENTIFIER, 0); }
		public TerminalNode OF() { return getToken(IMLParser.OF, 0); }
		public TerminalNode IS() { return getToken(IMLParser.IS, 0); }
		public TerminalNode IN() { return getToken(IMLParser.IN, 0); }
		public TerminalNode EQ() { return getToken(IMLParser.EQ, 0); }
		public ConceptExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conceptExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterConceptExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitConceptExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitConceptExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConceptExpressionContext conceptExpression() throws RecognitionException {
		ConceptExpressionContext _localctx = new ConceptExpressionContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_conceptExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(135);
			property();
			setState(136);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 536885248L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(137);
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
	public static class RangeExpressionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(IMLParser.IDENTIFIER, 0); }
		public RangeContext range() {
			return getRuleContext(RangeContext.class,0);
		}
		public RangeExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rangeExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterRangeExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitRangeExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitRangeExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeExpressionContext rangeExpression() throws RecognitionException {
		RangeExpressionContext _localctx = new RangeExpressionContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_rangeExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			match(IDENTIFIER);
			setState(140);
			range();
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
	public static class RangeContext extends ParserRuleContext {
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
		public RangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_range; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeContext range() throws RecognitionException {
		RangeContext _localctx = new RangeContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_range);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(142);
			match(BETWEEN);
			setState(143);
			comparisonOperator();
			setState(144);
			match(NUMBER);
			setState(145);
			match(AND);
			setState(146);
			comparisonOperator();
			setState(147);
			match(NUMBER);
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
	public static class ValueCompareContext extends ParserRuleContext {
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
		public ValueCompareContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueCompare; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterValueCompare(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitValueCompare(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitValueCompare(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueCompareContext valueCompare() throws RecognitionException {
		ValueCompareContext _localctx = new ValueCompareContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_valueCompare);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
			comparisonOperator();
			setState(150);
			match(NUMBER);
			setState(151);
			match(AND);
			setState(152);
			comparisonOperator();
			setState(153);
			match(NUMBER);
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
		public RangeContext range() {
			return getRuleContext(RangeContext.class,0);
		}
		public ValueCompareContext valueCompare() {
			return getRuleContext(ValueCompareContext.class,0);
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
		enterRule(_localctx, 36, RULE_functionExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(155);
			methodCall();
			setState(158);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BETWEEN:
				{
				setState(156);
				range();
				}
				break;
			case GT:
			case GTE:
			case LT:
			case LTE:
			case EQ:
			case NEQ:
				{
				setState(157);
				valueCompare();
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
		enterRule(_localctx, 38, RULE_methodCall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160);
			property();
			setState(161);
			match(OPEN_PAREN);
			setState(163);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(162);
				argumentList();
				}
			}

			setState(165);
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
		enterRule(_localctx, 40, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(167);
			match(IDENTIFIER);
			setState(172);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(168);
				match(COMMA);
				setState(169);
				match(IDENTIFIER);
				}
				}
				setState(174);
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
		enterRule(_localctx, 42, RULE_comparisonOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(175);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 2113929216L) != 0)) ) {
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
		enterRule(_localctx, 44, RULE_simpleExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IN || _la==ON) {
				{
				setState(177);
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

			setState(180);
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
		"\u0004\u0001&\u00b7\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002<\b\u0002\u0001\u0002"+
		"\u0001\u0002\u0005\u0002@\b\u0002\n\u0002\f\u0002C\t\u0002\u0001\u0003"+
		"\u0003\u0003F\b\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005P\b\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0004\u0007Z\b\u0007\u000b\u0007\f\u0007[\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0003\bb\b\b\u0004\bd\b\b\u000b\b\f\be\u0001"+
		"\t\u0005\ti\b\t\n\t\f\tl\t\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000b"+
		"y\b\u000b\u0001\f\u0001\f\u0001\f\u0004\f~\b\f\u000b\f\f\f\u007f\u0001"+
		"\r\u0001\r\u0004\r\u0084\b\r\u000b\r\f\r\u0085\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u009f\b\u0012\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0003\u0013\u00a4\b\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0005\u0014\u00ab\b\u0014\n\u0014"+
		"\f\u0014\u00ae\t\u0014\u0001\u0015\u0001\u0015\u0001\u0016\u0003\u0016"+
		"\u00b3\b\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0000\u0000\u0017\u0000"+
		"\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c"+
		"\u001e \"$&(*,\u0000\u0004\u0001\u0000\u0016\u0017\u0002\u0000\u000b\r"+
		"\u001d\u001d\u0001\u0000\u0019\u001e\u0001\u0000\r\u000e\u00b3\u0000."+
		"\u0001\u0000\u0000\u0000\u00021\u0001\u0000\u0000\u0000\u00048\u0001\u0000"+
		"\u0000\u0000\u0006E\u0001\u0000\u0000\u0000\bI\u0001\u0000\u0000\u0000"+
		"\nO\u0001\u0000\u0000\u0000\fQ\u0001\u0000\u0000\u0000\u000eU\u0001\u0000"+
		"\u0000\u0000\u0010c\u0001\u0000\u0000\u0000\u0012j\u0001\u0000\u0000\u0000"+
		"\u0014p\u0001\u0000\u0000\u0000\u0016x\u0001\u0000\u0000\u0000\u0018}"+
		"\u0001\u0000\u0000\u0000\u001a\u0081\u0001\u0000\u0000\u0000\u001c\u0087"+
		"\u0001\u0000\u0000\u0000\u001e\u008b\u0001\u0000\u0000\u0000 \u008e\u0001"+
		"\u0000\u0000\u0000\"\u0095\u0001\u0000\u0000\u0000$\u009b\u0001\u0000"+
		"\u0000\u0000&\u00a0\u0001\u0000\u0000\u0000(\u00a7\u0001\u0000\u0000\u0000"+
		"*\u00af\u0001\u0000\u0000\u0000,\u00b2\u0001\u0000\u0000\u0000./\u0003"+
		"\u0002\u0001\u0000/0\u0005\u0000\u0000\u00010\u0001\u0001\u0000\u0000"+
		"\u000012\u0005\u0002\u0000\u000023\u0005 \u0000\u000034\u0005\u0003\u0000"+
		"\u000045\u0005\u0012\u0000\u000056\u0003\u0004\u0002\u000067\u0005\u0013"+
		"\u0000\u00007\u0003\u0001\u0000\u0000\u000089\u0005\u0004\u0000\u0000"+
		"9;\u0003\u0006\u0003\u0000:<\u0003\b\u0004\u0000;:\u0001\u0000\u0000\u0000"+
		";<\u0001\u0000\u0000\u0000<A\u0001\u0000\u0000\u0000=@\u0003\n\u0005\u0000"+
		">@\u0003\u0016\u000b\u0000?=\u0001\u0000\u0000\u0000?>\u0001\u0000\u0000"+
		"\u0000@C\u0001\u0000\u0000\u0000A?\u0001\u0000\u0000\u0000AB\u0001\u0000"+
		"\u0000\u0000B\u0005\u0001\u0000\u0000\u0000CA\u0001\u0000\u0000\u0000"+
		"DF\u0005&\u0000\u0000ED\u0001\u0000\u0000\u0000EF\u0001\u0000\u0000\u0000"+
		"FG\u0001\u0000\u0000\u0000GH\u0005!\u0000\u0000H\u0007\u0001\u0000\u0000"+
		"\u0000IJ\u0005\u0005\u0000\u0000JK\u0005!\u0000\u0000K\t\u0001\u0000\u0000"+
		"\u0000LP\u0003\u000e\u0007\u0000MP\u0003\u0010\b\u0000NP\u0003\u0018\f"+
		"\u0000OL\u0001\u0000\u0000\u0000OM\u0001\u0000\u0000\u0000ON\u0001\u0000"+
		"\u0000\u0000P\u000b\u0001\u0000\u0000\u0000QR\u0005\u0014\u0000\u0000"+
		"RS\u0003\n\u0005\u0000ST\u0005\u0015\u0000\u0000T\r\u0001\u0000\u0000"+
		"\u0000UV\u0005\u0006\u0000\u0000VY\u0003\u0012\t\u0000WX\u0005\u0007\u0000"+
		"\u0000XZ\u0003\u0012\t\u0000YW\u0001\u0000\u0000\u0000Z[\u0001\u0000\u0000"+
		"\u0000[Y\u0001\u0000\u0000\u0000[\\\u0001\u0000\u0000\u0000\\\u000f\u0001"+
		"\u0000\u0000\u0000]a\u0005\b\u0000\u0000^b\u0003\u0012\t\u0000_b\u0003"+
		"\f\u0006\u0000`b\u0003\u0016\u000b\u0000a^\u0001\u0000\u0000\u0000a_\u0001"+
		"\u0000\u0000\u0000a`\u0001\u0000\u0000\u0000bd\u0001\u0000\u0000\u0000"+
		"c]\u0001\u0000\u0000\u0000de\u0001\u0000\u0000\u0000ec\u0001\u0000\u0000"+
		"\u0000ef\u0001\u0000\u0000\u0000f\u0011\u0001\u0000\u0000\u0000gi\u0003"+
		"\u001a\r\u0000hg\u0001\u0000\u0000\u0000il\u0001\u0000\u0000\u0000jh\u0001"+
		"\u0000\u0000\u0000jk\u0001\u0000\u0000\u0000km\u0001\u0000\u0000\u0000"+
		"lj\u0001\u0000\u0000\u0000mn\u0005\u0011\u0000\u0000no\u0003\u0016\u000b"+
		"\u0000o\u0013\u0001\u0000\u0000\u0000pq\u0005!\u0000\u0000qr\u0005\u0001"+
		"\u0000\u0000rs\u0005!\u0000\u0000s\u0015\u0001\u0000\u0000\u0000ty\u0003"+
		"\f\u0006\u0000uy\u0003$\u0012\u0000vy\u0003\u001c\u000e\u0000wy\u0003"+
		",\u0016\u0000xt\u0001\u0000\u0000\u0000xu\u0001\u0000\u0000\u0000xv\u0001"+
		"\u0000\u0000\u0000xw\u0001\u0000\u0000\u0000y\u0017\u0001\u0000\u0000"+
		"\u0000z{\u0005\t\u0000\u0000{|\u0005\n\u0000\u0000|~\u0003\u0016\u000b"+
		"\u0000}z\u0001\u0000\u0000\u0000~\u007f\u0001\u0000\u0000\u0000\u007f"+
		"}\u0001\u0000\u0000\u0000\u007f\u0080\u0001\u0000\u0000\u0000\u0080\u0019"+
		"\u0001\u0000\u0000\u0000\u0081\u0083\u0007\u0000\u0000\u0000\u0082\u0084"+
		"\u0005!\u0000\u0000\u0083\u0082\u0001\u0000\u0000\u0000\u0084\u0085\u0001"+
		"\u0000\u0000\u0000\u0085\u0083\u0001\u0000\u0000\u0000\u0085\u0086\u0001"+
		"\u0000\u0000\u0000\u0086\u001b\u0001\u0000\u0000\u0000\u0087\u0088\u0003"+
		"\u0014\n\u0000\u0088\u0089\u0007\u0001\u0000\u0000\u0089\u008a\u0005!"+
		"\u0000\u0000\u008a\u001d\u0001\u0000\u0000\u0000\u008b\u008c\u0005!\u0000"+
		"\u0000\u008c\u008d\u0003 \u0010\u0000\u008d\u001f\u0001\u0000\u0000\u0000"+
		"\u008e\u008f\u0005\u000f\u0000\u0000\u008f\u0090\u0003*\u0015\u0000\u0090"+
		"\u0091\u0005\"\u0000\u0000\u0091\u0092\u0005\b\u0000\u0000\u0092\u0093"+
		"\u0003*\u0015\u0000\u0093\u0094\u0005\"\u0000\u0000\u0094!\u0001\u0000"+
		"\u0000\u0000\u0095\u0096\u0003*\u0015\u0000\u0096\u0097\u0005\"\u0000"+
		"\u0000\u0097\u0098\u0005\b\u0000\u0000\u0098\u0099\u0003*\u0015\u0000"+
		"\u0099\u009a\u0005\"\u0000\u0000\u009a#\u0001\u0000\u0000\u0000\u009b"+
		"\u009e\u0003&\u0013\u0000\u009c\u009f\u0003 \u0010\u0000\u009d\u009f\u0003"+
		"\"\u0011\u0000\u009e\u009c\u0001\u0000\u0000\u0000\u009e\u009d\u0001\u0000"+
		"\u0000\u0000\u009f%\u0001\u0000\u0000\u0000\u00a0\u00a1\u0003\u0014\n"+
		"\u0000\u00a1\u00a3\u0005\u0014\u0000\u0000\u00a2\u00a4\u0003(\u0014\u0000"+
		"\u00a3\u00a2\u0001\u0000\u0000\u0000\u00a3\u00a4\u0001\u0000\u0000\u0000"+
		"\u00a4\u00a5\u0001\u0000\u0000\u0000\u00a5\u00a6\u0005\u0015\u0000\u0000"+
		"\u00a6\'\u0001\u0000\u0000\u0000\u00a7\u00ac\u0005!\u0000\u0000\u00a8"+
		"\u00a9\u0005\u0018\u0000\u0000\u00a9\u00ab\u0005!\u0000\u0000\u00aa\u00a8"+
		"\u0001\u0000\u0000\u0000\u00ab\u00ae\u0001\u0000\u0000\u0000\u00ac\u00aa"+
		"\u0001\u0000\u0000\u0000\u00ac\u00ad\u0001\u0000\u0000\u0000\u00ad)\u0001"+
		"\u0000\u0000\u0000\u00ae\u00ac\u0001\u0000\u0000\u0000\u00af\u00b0\u0007"+
		"\u0002\u0000\u0000\u00b0+\u0001\u0000\u0000\u0000\u00b1\u00b3\u0007\u0003"+
		"\u0000\u0000\u00b2\u00b1\u0001\u0000\u0000\u0000\u00b2\u00b3\u0001\u0000"+
		"\u0000\u0000\u00b3\u00b4\u0001\u0000\u0000\u0000\u00b4\u00b5\u0005!\u0000"+
		"\u0000\u00b5-\u0001\u0000\u0000\u0000\u0010;?AEO[aejx\u007f\u0085\u009e"+
		"\u00a3\u00ac\u00b2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}