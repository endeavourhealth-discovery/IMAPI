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
		DEFINE=1, AS=2, MATCH=3, FROM=4, EITHER=5, OR=6, AND=7, EXCLUDE=8, IF=9, 
		OF=10, IS=11, IN=12, ON=13, BETWEEN=14, WITH=15, WHERE=16, OPEN_BRACE=17, 
		CLOSE_BRACE=18, OPEN_PAREN=19, CLOSE_PAREN=20, ARROW=21, COMMA=22, GT=23, 
		GTE=24, LT=25, LTE=26, EQ=27, NEQ=28, IDENTIFIER=29, NUMBER=30, WS=31, 
		COMMENT=32, BLOCK_COMMENT=33;
	public static final int
		RULE_iml = 0, RULE_definition = 1, RULE_matchStatement = 2, RULE_entity = 3, 
		RULE_fromStatement = 4, RULE_booleanExpression = 5, RULE_nestedBooleanExpression = 6, 
		RULE_disjunction = 7, RULE_conjunction = 8, RULE_whereStatement = 9, RULE_standaloneExpression = 10, 
		RULE_expression = 11, RULE_exclusion = 12, RULE_pathExpression = 13, RULE_pathSegment = 14, 
		RULE_conceptExpression = 15, RULE_rangeExpression = 16, RULE_range = 17, 
		RULE_valueCompare = 18, RULE_functionExpression = 19, RULE_methodCall = 20, 
		RULE_argumentList = 21, RULE_comparisonOperator = 22, RULE_simpleExpression = 23;
	private static String[] makeRuleNames() {
		return new String[] {
			"iml", "definition", "matchStatement", "entity", "fromStatement", "booleanExpression", 
			"nestedBooleanExpression", "disjunction", "conjunction", "whereStatement", 
			"standaloneExpression", "expression", "exclusion", "pathExpression", 
			"pathSegment", "conceptExpression", "rangeExpression", "range", "valueCompare", 
			"functionExpression", "methodCall", "argumentList", "comparisonOperator", 
			"simpleExpression"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, "'{'", "'}'", "'('", "')'", "'->'", "','", 
			"'>'", "'>='", "'<'", "'<='", "'='", "'!='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "DEFINE", "AS", "MATCH", "FROM", "EITHER", "OR", "AND", "EXCLUDE", 
			"IF", "OF", "IS", "IN", "ON", "BETWEEN", "WITH", "WHERE", "OPEN_BRACE", 
			"CLOSE_BRACE", "OPEN_PAREN", "CLOSE_PAREN", "ARROW", "COMMA", "GT", "GTE", 
			"LT", "LTE", "EQ", "NEQ", "IDENTIFIER", "NUMBER", "WS", "COMMENT", "BLOCK_COMMENT"
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
			setState(48);
			definition();
			setState(49);
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
		public TerminalNode IDENTIFIER() { return getToken(IMLParser.IDENTIFIER, 0); }
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
			setState(51);
			match(DEFINE);
			setState(52);
			match(IDENTIFIER);
			setState(53);
			match(AS);
			setState(54);
			match(OPEN_BRACE);
			setState(55);
			matchStatement();
			setState(56);
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
			setState(58);
			match(MATCH);
			setState(59);
			entity();
			setState(61);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FROM) {
				{
				setState(60);
				fromStatement();
				}
			}

			setState(67);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 539508128L) != 0)) {
				{
				setState(65);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case EITHER:
				case AND:
				case EXCLUDE:
					{
					setState(63);
					booleanExpression();
					}
					break;
				case OF:
				case IS:
				case IN:
				case ON:
				case OPEN_PAREN:
				case ARROW:
				case IDENTIFIER:
					{
					setState(64);
					expression();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(69);
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
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(70);
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
			setState(72);
			match(FROM);
			setState(73);
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
			setState(78);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EITHER:
				enterOuterAlt(_localctx, 1);
				{
				setState(75);
				disjunction();
				}
				break;
			case AND:
				enterOuterAlt(_localctx, 2);
				{
				setState(76);
				conjunction();
				}
				break;
			case EXCLUDE:
				enterOuterAlt(_localctx, 3);
				{
				setState(77);
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
			setState(80);
			match(OPEN_PAREN);
			setState(81);
			booleanExpression();
			setState(82);
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
			setState(84);
			match(EITHER);
			setState(85);
			whereStatement();
			setState(90);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OR) {
				{
				{
				setState(86);
				match(OR);
				setState(87);
				whereStatement();
				}
				}
				setState(92);
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
	public static class ConjunctionContext extends ParserRuleContext {
		public TerminalNode AND() { return getToken(IMLParser.AND, 0); }
		public WhereStatementContext whereStatement() {
			return getRuleContext(WhereStatementContext.class,0);
		}
		public StandaloneExpressionContext standaloneExpression() {
			return getRuleContext(StandaloneExpressionContext.class,0);
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
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
			match(AND);
			setState(96);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case WITH:
			case WHERE:
				{
				setState(94);
				whereStatement();
				}
				break;
			case OF:
			case IS:
			case IN:
			case ON:
			case OPEN_PAREN:
			case ARROW:
			case IDENTIFIER:
				{
				setState(95);
				standaloneExpression();
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
	public static class WhereStatementContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode WITH() { return getToken(IMLParser.WITH, 0); }
		public TerminalNode WHERE() { return getToken(IMLParser.WHERE, 0); }
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
			setState(98);
			_la = _input.LA(1);
			if ( !(_la==WITH || _la==WHERE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(99);
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
	public static class StandaloneExpressionContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public StandaloneExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_standaloneExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterStandaloneExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitStandaloneExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitStandaloneExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StandaloneExpressionContext standaloneExpression() throws RecognitionException {
		StandaloneExpressionContext _localctx = new StandaloneExpressionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_standaloneExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101);
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
		public NestedBooleanExpressionContext nestedBooleanExpression() {
			return getRuleContext(NestedBooleanExpressionContext.class,0);
		}
		public PathExpressionContext pathExpression() {
			return getRuleContext(PathExpressionContext.class,0);
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
			setState(108);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(103);
				nestedBooleanExpression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(104);
				pathExpression();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(105);
				functionExpression();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(106);
				conceptExpression();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(107);
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
		public TerminalNode EXCLUDE() { return getToken(IMLParser.EXCLUDE, 0); }
		public TerminalNode IF() { return getToken(IMLParser.IF, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
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
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			match(EXCLUDE);
			setState(111);
			match(IF);
			setState(112);
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
	public static class PathExpressionContext extends ParserRuleContext {
		public ConceptExpressionContext conceptExpression() {
			return getRuleContext(ConceptExpressionContext.class,0);
		}
		public List<PathSegmentContext> pathSegment() {
			return getRuleContexts(PathSegmentContext.class);
		}
		public PathSegmentContext pathSegment(int i) {
			return getRuleContext(PathSegmentContext.class,i);
		}
		public PathExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).enterPathExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLListener ) ((IMLListener)listener).exitPathExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLVisitor ) return ((IMLVisitor<? extends T>)visitor).visitPathExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathExpressionContext pathExpression() throws RecognitionException {
		PathExpressionContext _localctx = new PathExpressionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_pathExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(115); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(114);
				pathSegment();
				}
				}
				setState(117); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==ARROW );
			setState(119);
			conceptExpression();
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
		public TerminalNode IDENTIFIER() { return getToken(IMLParser.IDENTIFIER, 0); }
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
		enterRule(_localctx, 28, RULE_pathSegment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			match(ARROW);
			setState(122);
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
	public static class ConceptExpressionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(IMLParser.IDENTIFIER, 0); }
		public TerminalNode OF() { return getToken(IMLParser.OF, 0); }
		public TerminalNode IS() { return getToken(IMLParser.IS, 0); }
		public TerminalNode IN() { return getToken(IMLParser.IN, 0); }
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
		enterRule(_localctx, 30, RULE_conceptExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 7168L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(125);
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
		enterRule(_localctx, 32, RULE_rangeExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(127);
			match(IDENTIFIER);
			setState(128);
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
		enterRule(_localctx, 34, RULE_range);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(130);
			match(BETWEEN);
			setState(131);
			comparisonOperator();
			setState(132);
			match(NUMBER);
			setState(133);
			match(AND);
			setState(134);
			comparisonOperator();
			setState(135);
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
		enterRule(_localctx, 36, RULE_valueCompare);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(137);
			comparisonOperator();
			setState(138);
			match(NUMBER);
			setState(139);
			match(AND);
			setState(140);
			comparisonOperator();
			setState(141);
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
		enterRule(_localctx, 38, RULE_functionExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
			methodCall();
			setState(146);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BETWEEN:
				{
				setState(144);
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
				setState(145);
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
		public TerminalNode IDENTIFIER() { return getToken(IMLParser.IDENTIFIER, 0); }
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
		enterRule(_localctx, 40, RULE_methodCall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			match(IDENTIFIER);
			setState(149);
			match(OPEN_PAREN);
			setState(151);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(150);
				argumentList();
				}
			}

			setState(153);
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
		enterRule(_localctx, 42, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(155);
			match(IDENTIFIER);
			setState(160);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(156);
				match(COMMA);
				setState(157);
				match(IDENTIFIER);
				}
				}
				setState(162);
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
		enterRule(_localctx, 44, RULE_comparisonOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 528482304L) != 0)) ) {
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
		enterRule(_localctx, 46, RULE_simpleExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IN || _la==ON) {
				{
				setState(165);
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

			setState(168);
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
		"\u0004\u0001!\u00ab\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002"+
		">\b\u0002\u0001\u0002\u0001\u0002\u0005\u0002B\b\u0002\n\u0002\f\u0002"+
		"E\t\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005O\b\u0005\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0005\u0007Y\b\u0007\n\u0007\f\u0007\\\t\u0007\u0001\b\u0001"+
		"\b\u0001\b\u0003\ba\b\b\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000bm\b"+
		"\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\r\u0004\rt\b\r\u000b\r\f"+
		"\ru\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u0093\b\u0013\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0003\u0014\u0098\b\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0005\u0015\u009f\b\u0015\n\u0015\f\u0015"+
		"\u00a2\t\u0015\u0001\u0016\u0001\u0016\u0001\u0017\u0003\u0017\u00a7\b"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0000\u0000\u0018\u0000\u0002"+
		"\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e"+
		" \"$&(*,.\u0000\u0004\u0001\u0000\u000f\u0010\u0001\u0000\n\f\u0001\u0000"+
		"\u0017\u001c\u0001\u0000\f\r\u00a2\u00000\u0001\u0000\u0000\u0000\u0002"+
		"3\u0001\u0000\u0000\u0000\u0004:\u0001\u0000\u0000\u0000\u0006F\u0001"+
		"\u0000\u0000\u0000\bH\u0001\u0000\u0000\u0000\nN\u0001\u0000\u0000\u0000"+
		"\fP\u0001\u0000\u0000\u0000\u000eT\u0001\u0000\u0000\u0000\u0010]\u0001"+
		"\u0000\u0000\u0000\u0012b\u0001\u0000\u0000\u0000\u0014e\u0001\u0000\u0000"+
		"\u0000\u0016l\u0001\u0000\u0000\u0000\u0018n\u0001\u0000\u0000\u0000\u001a"+
		"s\u0001\u0000\u0000\u0000\u001cy\u0001\u0000\u0000\u0000\u001e|\u0001"+
		"\u0000\u0000\u0000 \u007f\u0001\u0000\u0000\u0000\"\u0082\u0001\u0000"+
		"\u0000\u0000$\u0089\u0001\u0000\u0000\u0000&\u008f\u0001\u0000\u0000\u0000"+
		"(\u0094\u0001\u0000\u0000\u0000*\u009b\u0001\u0000\u0000\u0000,\u00a3"+
		"\u0001\u0000\u0000\u0000.\u00a6\u0001\u0000\u0000\u000001\u0003\u0002"+
		"\u0001\u000012\u0005\u0000\u0000\u00012\u0001\u0001\u0000\u0000\u0000"+
		"34\u0005\u0001\u0000\u000045\u0005\u001d\u0000\u000056\u0005\u0002\u0000"+
		"\u000067\u0005\u0011\u0000\u000078\u0003\u0004\u0002\u000089\u0005\u0012"+
		"\u0000\u00009\u0003\u0001\u0000\u0000\u0000:;\u0005\u0003\u0000\u0000"+
		";=\u0003\u0006\u0003\u0000<>\u0003\b\u0004\u0000=<\u0001\u0000\u0000\u0000"+
		"=>\u0001\u0000\u0000\u0000>C\u0001\u0000\u0000\u0000?B\u0003\n\u0005\u0000"+
		"@B\u0003\u0016\u000b\u0000A?\u0001\u0000\u0000\u0000A@\u0001\u0000\u0000"+
		"\u0000BE\u0001\u0000\u0000\u0000CA\u0001\u0000\u0000\u0000CD\u0001\u0000"+
		"\u0000\u0000D\u0005\u0001\u0000\u0000\u0000EC\u0001\u0000\u0000\u0000"+
		"FG\u0005\u001d\u0000\u0000G\u0007\u0001\u0000\u0000\u0000HI\u0005\u0004"+
		"\u0000\u0000IJ\u0005\u001d\u0000\u0000J\t\u0001\u0000\u0000\u0000KO\u0003"+
		"\u000e\u0007\u0000LO\u0003\u0010\b\u0000MO\u0003\u0018\f\u0000NK\u0001"+
		"\u0000\u0000\u0000NL\u0001\u0000\u0000\u0000NM\u0001\u0000\u0000\u0000"+
		"O\u000b\u0001\u0000\u0000\u0000PQ\u0005\u0013\u0000\u0000QR\u0003\n\u0005"+
		"\u0000RS\u0005\u0014\u0000\u0000S\r\u0001\u0000\u0000\u0000TU\u0005\u0005"+
		"\u0000\u0000UZ\u0003\u0012\t\u0000VW\u0005\u0006\u0000\u0000WY\u0003\u0012"+
		"\t\u0000XV\u0001\u0000\u0000\u0000Y\\\u0001\u0000\u0000\u0000ZX\u0001"+
		"\u0000\u0000\u0000Z[\u0001\u0000\u0000\u0000[\u000f\u0001\u0000\u0000"+
		"\u0000\\Z\u0001\u0000\u0000\u0000]`\u0005\u0007\u0000\u0000^a\u0003\u0012"+
		"\t\u0000_a\u0003\u0014\n\u0000`^\u0001\u0000\u0000\u0000`_\u0001\u0000"+
		"\u0000\u0000a\u0011\u0001\u0000\u0000\u0000bc\u0007\u0000\u0000\u0000"+
		"cd\u0003\u0016\u000b\u0000d\u0013\u0001\u0000\u0000\u0000ef\u0003\u0016"+
		"\u000b\u0000f\u0015\u0001\u0000\u0000\u0000gm\u0003\f\u0006\u0000hm\u0003"+
		"\u001a\r\u0000im\u0003&\u0013\u0000jm\u0003\u001e\u000f\u0000km\u0003"+
		".\u0017\u0000lg\u0001\u0000\u0000\u0000lh\u0001\u0000\u0000\u0000li\u0001"+
		"\u0000\u0000\u0000lj\u0001\u0000\u0000\u0000lk\u0001\u0000\u0000\u0000"+
		"m\u0017\u0001\u0000\u0000\u0000no\u0005\b\u0000\u0000op\u0005\t\u0000"+
		"\u0000pq\u0003\u0016\u000b\u0000q\u0019\u0001\u0000\u0000\u0000rt\u0003"+
		"\u001c\u000e\u0000sr\u0001\u0000\u0000\u0000tu\u0001\u0000\u0000\u0000"+
		"us\u0001\u0000\u0000\u0000uv\u0001\u0000\u0000\u0000vw\u0001\u0000\u0000"+
		"\u0000wx\u0003\u001e\u000f\u0000x\u001b\u0001\u0000\u0000\u0000yz\u0005"+
		"\u0015\u0000\u0000z{\u0005\u001d\u0000\u0000{\u001d\u0001\u0000\u0000"+
		"\u0000|}\u0007\u0001\u0000\u0000}~\u0005\u001d\u0000\u0000~\u001f\u0001"+
		"\u0000\u0000\u0000\u007f\u0080\u0005\u001d\u0000\u0000\u0080\u0081\u0003"+
		"\"\u0011\u0000\u0081!\u0001\u0000\u0000\u0000\u0082\u0083\u0005\u000e"+
		"\u0000\u0000\u0083\u0084\u0003,\u0016\u0000\u0084\u0085\u0005\u001e\u0000"+
		"\u0000\u0085\u0086\u0005\u0007\u0000\u0000\u0086\u0087\u0003,\u0016\u0000"+
		"\u0087\u0088\u0005\u001e\u0000\u0000\u0088#\u0001\u0000\u0000\u0000\u0089"+
		"\u008a\u0003,\u0016\u0000\u008a\u008b\u0005\u001e\u0000\u0000\u008b\u008c"+
		"\u0005\u0007\u0000\u0000\u008c\u008d\u0003,\u0016\u0000\u008d\u008e\u0005"+
		"\u001e\u0000\u0000\u008e%\u0001\u0000\u0000\u0000\u008f\u0092\u0003(\u0014"+
		"\u0000\u0090\u0093\u0003\"\u0011\u0000\u0091\u0093\u0003$\u0012\u0000"+
		"\u0092\u0090\u0001\u0000\u0000\u0000\u0092\u0091\u0001\u0000\u0000\u0000"+
		"\u0093\'\u0001\u0000\u0000\u0000\u0094\u0095\u0005\u001d\u0000\u0000\u0095"+
		"\u0097\u0005\u0013\u0000\u0000\u0096\u0098\u0003*\u0015\u0000\u0097\u0096"+
		"\u0001\u0000\u0000\u0000\u0097\u0098\u0001\u0000\u0000\u0000\u0098\u0099"+
		"\u0001\u0000\u0000\u0000\u0099\u009a\u0005\u0014\u0000\u0000\u009a)\u0001"+
		"\u0000\u0000\u0000\u009b\u00a0\u0005\u001d\u0000\u0000\u009c\u009d\u0005"+
		"\u0016\u0000\u0000\u009d\u009f\u0005\u001d\u0000\u0000\u009e\u009c\u0001"+
		"\u0000\u0000\u0000\u009f\u00a2\u0001\u0000\u0000\u0000\u00a0\u009e\u0001"+
		"\u0000\u0000\u0000\u00a0\u00a1\u0001\u0000\u0000\u0000\u00a1+\u0001\u0000"+
		"\u0000\u0000\u00a2\u00a0\u0001\u0000\u0000\u0000\u00a3\u00a4\u0007\u0002"+
		"\u0000\u0000\u00a4-\u0001\u0000\u0000\u0000\u00a5\u00a7\u0007\u0003\u0000"+
		"\u0000\u00a6\u00a5\u0001\u0000\u0000\u0000\u00a6\u00a7\u0001\u0000\u0000"+
		"\u0000\u00a7\u00a8\u0001\u0000\u0000\u0000\u00a8\u00a9\u0005\u001d\u0000"+
		"\u0000\u00a9/\u0001\u0000\u0000\u0000\f=ACNZ`lu\u0092\u0097\u00a0\u00a6";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}