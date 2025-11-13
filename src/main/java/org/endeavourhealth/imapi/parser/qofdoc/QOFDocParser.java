// Generated from Z:/IdeaProjects/Endeavour/InformationManager/IMAPI/src/main/grammars/QOFDoc.g4 by ANTLR 4.13.2
package org.endeavourhealth.imapi.parser.qofdoc;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class QOFDocParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, OR=2, AND=3, NOT=4, COMMA=5, LPAREN=6, RPAREN=7, OPERATOR=8, COMPARISON_OPERATOR=9, 
		IDENTIFIER=10, NUMBER=11, UNIT=12, DATE=13, STRING_LITERAL=14, FUNCTION_NAME=15, 
		NULL=16, WS=17;
	public static final int
		RULE_expression = 0, RULE_orExpression = 1, RULE_andExpression = 2, RULE_notExpression = 3, 
		RULE_comparisonExpression = 4, RULE_term = 5;
	private static String[] makeRuleNames() {
		return new String[] {
			"expression", "orExpression", "andExpression", "notExpression", "comparisonExpression", 
			"term"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'If'", null, null, null, "','", "'('", "')'", null, null, null, 
			null, null, null, null, "'Latest of'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "OR", "AND", "NOT", "COMMA", "LPAREN", "RPAREN", "OPERATOR", 
			"COMPARISON_OPERATOR", "IDENTIFIER", "NUMBER", "UNIT", "DATE", "STRING_LITERAL", 
			"FUNCTION_NAME", "NULL", "WS"
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
	public String getGrammarFileName() { return "QOFDoc.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public QOFDocParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public OrExpressionContext orExpression() {
			return getRuleContext(OrExpressionContext.class,0);
		}
		public TerminalNode EOF() { return getToken(QOFDocParser.EOF, 0); }
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QOFDocListener ) ((QOFDocListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QOFDocListener ) ((QOFDocListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QOFDocVisitor ) return ((QOFDocVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(12);
			orExpression();
			setState(13);
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
	public static class OrExpressionContext extends ParserRuleContext {
		public List<AndExpressionContext> andExpression() {
			return getRuleContexts(AndExpressionContext.class);
		}
		public AndExpressionContext andExpression(int i) {
			return getRuleContext(AndExpressionContext.class,i);
		}
		public List<TerminalNode> OR() { return getTokens(QOFDocParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(QOFDocParser.OR, i);
		}
		public OrExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QOFDocListener ) ((QOFDocListener)listener).enterOrExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QOFDocListener ) ((QOFDocListener)listener).exitOrExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QOFDocVisitor ) return ((QOFDocVisitor<? extends T>)visitor).visitOrExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrExpressionContext orExpression() throws RecognitionException {
		OrExpressionContext _localctx = new OrExpressionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_orExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(15);
			andExpression();
			setState(20);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OR) {
				{
				{
				setState(16);
				match(OR);
				setState(17);
				andExpression();
				}
				}
				setState(22);
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
	public static class AndExpressionContext extends ParserRuleContext {
		public List<NotExpressionContext> notExpression() {
			return getRuleContexts(NotExpressionContext.class);
		}
		public NotExpressionContext notExpression(int i) {
			return getRuleContext(NotExpressionContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(QOFDocParser.AND); }
		public TerminalNode AND(int i) {
			return getToken(QOFDocParser.AND, i);
		}
		public AndExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QOFDocListener ) ((QOFDocListener)listener).enterAndExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QOFDocListener ) ((QOFDocListener)listener).exitAndExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QOFDocVisitor ) return ((QOFDocVisitor<? extends T>)visitor).visitAndExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndExpressionContext andExpression() throws RecognitionException {
		AndExpressionContext _localctx = new AndExpressionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_andExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(23);
			notExpression();
			setState(28);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND) {
				{
				{
				setState(24);
				match(AND);
				setState(25);
				notExpression();
				}
				}
				setState(30);
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
	public static class NotExpressionContext extends ParserRuleContext {
		public ComparisonExpressionContext comparisonExpression() {
			return getRuleContext(ComparisonExpressionContext.class,0);
		}
		public TerminalNode NOT() { return getToken(QOFDocParser.NOT, 0); }
		public NotExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QOFDocListener ) ((QOFDocListener)listener).enterNotExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QOFDocListener ) ((QOFDocListener)listener).exitNotExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QOFDocVisitor ) return ((QOFDocVisitor<? extends T>)visitor).visitNotExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NotExpressionContext notExpression() throws RecognitionException {
		NotExpressionContext _localctx = new NotExpressionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_notExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(32);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NOT) {
				{
				setState(31);
				match(NOT);
				}
			}

			setState(34);
			comparisonExpression();
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
	public static class ComparisonExpressionContext extends ParserRuleContext {
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public TerminalNode COMPARISON_OPERATOR() { return getToken(QOFDocParser.COMPARISON_OPERATOR, 0); }
		public ComparisonExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparisonExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QOFDocListener ) ((QOFDocListener)listener).enterComparisonExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QOFDocListener ) ((QOFDocListener)listener).exitComparisonExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QOFDocVisitor ) return ((QOFDocVisitor<? extends T>)visitor).visitComparisonExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparisonExpressionContext comparisonExpression() throws RecognitionException {
		ComparisonExpressionContext _localctx = new ComparisonExpressionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_comparisonExpression);
		int _la;
		try {
			setState(46);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
			case LPAREN:
			case IDENTIFIER:
			case NUMBER:
			case DATE:
			case STRING_LITERAL:
			case FUNCTION_NAME:
			case NULL:
				enterOuterAlt(_localctx, 1);
				{
				setState(37);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(36);
					match(T__0);
					}
				}

				setState(39);
				term(0);
				setState(42);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMPARISON_OPERATOR) {
					{
					setState(40);
					match(COMPARISON_OPERATOR);
					setState(41);
					term(0);
					}
				}

				}
				break;
			case COMPARISON_OPERATOR:
				enterOuterAlt(_localctx, 2);
				{
				setState(44);
				match(COMPARISON_OPERATOR);
				setState(45);
				term(0);
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
	public static class TermContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(QOFDocParser.IDENTIFIER, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(QOFDocParser.STRING_LITERAL, 0); }
		public TerminalNode NUMBER() { return getToken(QOFDocParser.NUMBER, 0); }
		public TerminalNode DATE() { return getToken(QOFDocParser.DATE, 0); }
		public TerminalNode LPAREN() { return getToken(QOFDocParser.LPAREN, 0); }
		public OrExpressionContext orExpression() {
			return getRuleContext(OrExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(QOFDocParser.RPAREN, 0); }
		public TerminalNode FUNCTION_NAME() { return getToken(QOFDocParser.FUNCTION_NAME, 0); }
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(QOFDocParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(QOFDocParser.COMMA, i);
		}
		public TerminalNode NULL() { return getToken(QOFDocParser.NULL, 0); }
		public TerminalNode OPERATOR() { return getToken(QOFDocParser.OPERATOR, 0); }
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QOFDocListener ) ((QOFDocListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QOFDocListener ) ((QOFDocListener)listener).exitTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QOFDocVisitor ) return ((QOFDocVisitor<? extends T>)visitor).visitTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		return term(0);
	}

	private TermContext term(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		TermContext _localctx = new TermContext(_ctx, _parentState);
		TermContext _prevctx = _localctx;
		int _startState = 10;
		enterRecursionRule(_localctx, 10, RULE_term, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(49);
				match(IDENTIFIER);
				}
				break;
			case STRING_LITERAL:
				{
				setState(50);
				match(STRING_LITERAL);
				}
				break;
			case NUMBER:
				{
				setState(51);
				match(NUMBER);
				}
				break;
			case DATE:
				{
				setState(52);
				match(DATE);
				}
				break;
			case LPAREN:
				{
				setState(53);
				match(LPAREN);
				setState(54);
				orExpression();
				setState(55);
				match(RPAREN);
				}
				break;
			case FUNCTION_NAME:
				{
				setState(57);
				match(FUNCTION_NAME);
				setState(58);
				match(LPAREN);
				setState(67);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 126016L) != 0)) {
					{
					setState(59);
					term(0);
					setState(64);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(60);
						match(COMMA);
						setState(61);
						term(0);
						}
						}
						setState(66);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(69);
				match(RPAREN);
				}
				break;
			case NULL:
				{
				setState(70);
				match(NULL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(78);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new TermContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_term);
					setState(73);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(74);
					match(OPERATOR);
					setState(75);
					term(2);
					}
					} 
				}
				setState(80);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 5:
			return term_sempred((TermContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean term_sempred(TermContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0011R\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0005\u0001\u0013\b\u0001\n\u0001\f\u0001\u0016\t\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002\u001b\b\u0002\n\u0002"+
		"\f\u0002\u001e\t\u0002\u0001\u0003\u0003\u0003!\b\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0004\u0003\u0004&\b\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0003\u0004+\b\u0004\u0001\u0004\u0001\u0004\u0003\u0004/\b\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0005\u0005?\b\u0005\n\u0005\f\u0005B\t\u0005"+
		"\u0003\u0005D\b\u0005\u0001\u0005\u0001\u0005\u0003\u0005H\b\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0005\u0005M\b\u0005\n\u0005\f\u0005P\t"+
		"\u0005\u0001\u0005\u0000\u0001\n\u0006\u0000\u0002\u0004\u0006\b\n\u0000"+
		"\u0000Z\u0000\f\u0001\u0000\u0000\u0000\u0002\u000f\u0001\u0000\u0000"+
		"\u0000\u0004\u0017\u0001\u0000\u0000\u0000\u0006 \u0001\u0000\u0000\u0000"+
		"\b.\u0001\u0000\u0000\u0000\nG\u0001\u0000\u0000\u0000\f\r\u0003\u0002"+
		"\u0001\u0000\r\u000e\u0005\u0000\u0000\u0001\u000e\u0001\u0001\u0000\u0000"+
		"\u0000\u000f\u0014\u0003\u0004\u0002\u0000\u0010\u0011\u0005\u0002\u0000"+
		"\u0000\u0011\u0013\u0003\u0004\u0002\u0000\u0012\u0010\u0001\u0000\u0000"+
		"\u0000\u0013\u0016\u0001\u0000\u0000\u0000\u0014\u0012\u0001\u0000\u0000"+
		"\u0000\u0014\u0015\u0001\u0000\u0000\u0000\u0015\u0003\u0001\u0000\u0000"+
		"\u0000\u0016\u0014\u0001\u0000\u0000\u0000\u0017\u001c\u0003\u0006\u0003"+
		"\u0000\u0018\u0019\u0005\u0003\u0000\u0000\u0019\u001b\u0003\u0006\u0003"+
		"\u0000\u001a\u0018\u0001\u0000\u0000\u0000\u001b\u001e\u0001\u0000\u0000"+
		"\u0000\u001c\u001a\u0001\u0000\u0000\u0000\u001c\u001d\u0001\u0000\u0000"+
		"\u0000\u001d\u0005\u0001\u0000\u0000\u0000\u001e\u001c\u0001\u0000\u0000"+
		"\u0000\u001f!\u0005\u0004\u0000\u0000 \u001f\u0001\u0000\u0000\u0000 "+
		"!\u0001\u0000\u0000\u0000!\"\u0001\u0000\u0000\u0000\"#\u0003\b\u0004"+
		"\u0000#\u0007\u0001\u0000\u0000\u0000$&\u0005\u0001\u0000\u0000%$\u0001"+
		"\u0000\u0000\u0000%&\u0001\u0000\u0000\u0000&\'\u0001\u0000\u0000\u0000"+
		"\'*\u0003\n\u0005\u0000()\u0005\t\u0000\u0000)+\u0003\n\u0005\u0000*("+
		"\u0001\u0000\u0000\u0000*+\u0001\u0000\u0000\u0000+/\u0001\u0000\u0000"+
		"\u0000,-\u0005\t\u0000\u0000-/\u0003\n\u0005\u0000.%\u0001\u0000\u0000"+
		"\u0000.,\u0001\u0000\u0000\u0000/\t\u0001\u0000\u0000\u000001\u0006\u0005"+
		"\uffff\uffff\u00001H\u0005\n\u0000\u00002H\u0005\u000e\u0000\u00003H\u0005"+
		"\u000b\u0000\u00004H\u0005\r\u0000\u000056\u0005\u0006\u0000\u000067\u0003"+
		"\u0002\u0001\u000078\u0005\u0007\u0000\u00008H\u0001\u0000\u0000\u0000"+
		"9:\u0005\u000f\u0000\u0000:C\u0005\u0006\u0000\u0000;@\u0003\n\u0005\u0000"+
		"<=\u0005\u0005\u0000\u0000=?\u0003\n\u0005\u0000><\u0001\u0000\u0000\u0000"+
		"?B\u0001\u0000\u0000\u0000@>\u0001\u0000\u0000\u0000@A\u0001\u0000\u0000"+
		"\u0000AD\u0001\u0000\u0000\u0000B@\u0001\u0000\u0000\u0000C;\u0001\u0000"+
		"\u0000\u0000CD\u0001\u0000\u0000\u0000DE\u0001\u0000\u0000\u0000EH\u0005"+
		"\u0007\u0000\u0000FH\u0005\u0010\u0000\u0000G0\u0001\u0000\u0000\u0000"+
		"G2\u0001\u0000\u0000\u0000G3\u0001\u0000\u0000\u0000G4\u0001\u0000\u0000"+
		"\u0000G5\u0001\u0000\u0000\u0000G9\u0001\u0000\u0000\u0000GF\u0001\u0000"+
		"\u0000\u0000HN\u0001\u0000\u0000\u0000IJ\n\u0001\u0000\u0000JK\u0005\b"+
		"\u0000\u0000KM\u0003\n\u0005\u0002LI\u0001\u0000\u0000\u0000MP\u0001\u0000"+
		"\u0000\u0000NL\u0001\u0000\u0000\u0000NO\u0001\u0000\u0000\u0000O\u000b"+
		"\u0001\u0000\u0000\u0000PN\u0001\u0000\u0000\u0000\n\u0014\u001c %*.@"+
		"CGN";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}