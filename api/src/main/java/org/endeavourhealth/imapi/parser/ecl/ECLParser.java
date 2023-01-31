// Generated from java-escape by ANTLR 4.11.1
package org.endeavourhealth.imapi.parser.ecl;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class ECLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.11.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		UTF8_LETTER=1, TAB=2, LF=3, CR=4, SPACE=5, EXCLAMATION=6, QUOTE=7, POUND=8, 
		DOLLAR=9, PERCENT=10, AMPERSAND=11, APOSTROPHE=12, LEFT_PAREN=13, RIGHT_PAREN=14, 
		ASTERISK=15, PLUS=16, COMMA=17, DASH=18, PERIOD=19, SLASH=20, ZERO=21, 
		ONE=22, TWO=23, THREE=24, FOUR=25, FIVE=26, SIX=27, SEVEN=28, EIGHT=29, 
		NINE=30, COLON=31, SEMICOLON=32, LESS_THAN=33, EQUALS=34, GREATER_THAN=35, 
		QUESTION=36, AT=37, CAP_A=38, CAP_B=39, CAP_C=40, CAP_D=41, CAP_E=42, 
		CAP_F=43, CAP_G=44, CAP_H=45, CAP_I=46, CAP_J=47, CAP_K=48, CAP_L=49, 
		CAP_M=50, CAP_N=51, CAP_O=52, CAP_P=53, CAP_Q=54, CAP_R=55, CAP_S=56, 
		CAP_T=57, CAP_U=58, CAP_V=59, CAP_W=60, CAP_X=61, CAP_Y=62, CAP_Z=63, 
		LEFT_BRACE=64, BACKSLASH=65, RIGHT_BRACE=66, CARAT=67, UNDERSCORE=68, 
		ACCENT=69, A=70, B=71, C=72, D=73, E=74, F=75, G=76, H=77, I=78, J=79, 
		K=80, L=81, M=82, N=83, O=84, P=85, Q=86, R=87, S=88, T=89, U=90, V=91, 
		W=92, X=93, Y=94, Z=95, LEFT_CURLY_BRACE=96, PIPE=97, RIGHT_CURLY_BRACE=98, 
		TILDE=99;
	public static final int
		RULE_ecl = 0, RULE_expressionconstraint = 1, RULE_refinedexpressionconstraint = 2, 
		RULE_compoundexpressionconstraint = 3, RULE_conjunctionexpressionconstraint = 4, 
		RULE_disjunctionexpressionconstraint = 5, RULE_exclusionexpressionconstraint = 6, 
		RULE_dottedexpressionconstraint = 7, RULE_dottedexpressionattribute = 8, 
		RULE_subexpressionconstraint = 9, RULE_eclfocusconcept = 10, RULE_dot = 11, 
		RULE_memberof = 12, RULE_eclconceptreference = 13, RULE_conceptid = 14, 
		RULE_term = 15, RULE_wildcard = 16, RULE_constraintoperator = 17, RULE_descendantof = 18, 
		RULE_descendantorselfof = 19, RULE_childof = 20, RULE_ancestorof = 21, 
		RULE_ancestororselfof = 22, RULE_parentof = 23, RULE_conjunction = 24, 
		RULE_disjunction = 25, RULE_exclusion = 26, RULE_eclrefinement = 27, RULE_conjunctionrefinementset = 28, 
		RULE_disjunctionrefinementset = 29, RULE_subrefinement = 30, RULE_eclattributeset = 31, 
		RULE_conjunctionattributeset = 32, RULE_disjunctionattributeset = 33, 
		RULE_subattributeset = 34, RULE_eclattributegroup = 35, RULE_eclattribute = 36, 
		RULE_cardinality = 37, RULE_minvalue = 38, RULE_to = 39, RULE_maxvalue = 40, 
		RULE_many = 41, RULE_reverseflag = 42, RULE_eclattributename = 43, RULE_expressioncomparisonoperator = 44, 
		RULE_numericcomparisonoperator = 45, RULE_stringcomparisonoperator = 46, 
		RULE_numericvalue = 47, RULE_stringvalue = 48, RULE_integervalue = 49, 
		RULE_decimalvalue = 50, RULE_nonnegativeintegervalue = 51, RULE_sctid = 52, 
		RULE_ws = 53, RULE_mws = 54, RULE_comment = 55, RULE_nonstarchar = 56, 
		RULE_nonspacechar = 57, RULE_starwithnonfslash = 58, RULE_nonfslash = 59, 
		RULE_sp = 60, RULE_htab = 61, RULE_cr = 62, RULE_lf = 63, RULE_qm = 64, 
		RULE_bs = 65, RULE_digit = 66, RULE_zero = 67, RULE_digitnonzero = 68, 
		RULE_nonwsnonpipe = 69, RULE_anynonescapedchar = 70, RULE_escapedchar = 71;
	private static String[] makeRuleNames() {
		return new String[] {
			"ecl", "expressionconstraint", "refinedexpressionconstraint", "compoundexpressionconstraint", 
			"conjunctionexpressionconstraint", "disjunctionexpressionconstraint", 
			"exclusionexpressionconstraint", "dottedexpressionconstraint", "dottedexpressionattribute", 
			"subexpressionconstraint", "eclfocusconcept", "dot", "memberof", "eclconceptreference", 
			"conceptid", "term", "wildcard", "constraintoperator", "descendantof", 
			"descendantorselfof", "childof", "ancestorof", "ancestororselfof", "parentof", 
			"conjunction", "disjunction", "exclusion", "eclrefinement", "conjunctionrefinementset", 
			"disjunctionrefinementset", "subrefinement", "eclattributeset", "conjunctionattributeset", 
			"disjunctionattributeset", "subattributeset", "eclattributegroup", "eclattribute", 
			"cardinality", "minvalue", "to", "maxvalue", "many", "reverseflag", "eclattributename", 
			"expressioncomparisonoperator", "numericcomparisonoperator", "stringcomparisonoperator", 
			"numericvalue", "stringvalue", "integervalue", "decimalvalue", "nonnegativeintegervalue", 
			"sctid", "ws", "mws", "comment", "nonstarchar", "nonspacechar", "starwithnonfslash", 
			"nonfslash", "sp", "htab", "cr", "lf", "qm", "bs", "digit", "zero", "digitnonzero", 
			"nonwsnonpipe", "anynonescapedchar", "escapedchar"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'\\t'", "'\\n'", "'\\r'", "' '", "'!'", "'\"'", "'#'", "'$'", 
			"'%'", "'&'", "'''", "'('", "')'", "'*'", "'+'", "','", "'-'", "'.'", 
			"'/'", "'0'", "'1'", "'2'", "'3'", "'4'", "'5'", "'6'", "'7'", "'8'", 
			"'9'", "':'", "';'", "'<'", "'='", "'>'", "'?'", "'@'", "'A'", "'B'", 
			"'C'", "'D'", "'E'", "'F'", "'G'", "'H'", "'I'", "'J'", "'K'", "'L'", 
			"'M'", "'N'", "'O'", "'P'", "'Q'", "'R'", "'S'", "'T'", "'U'", "'V'", 
			"'W'", "'X'", "'Y'", "'Z'", "'['", "'\\'", "']'", "'^'", "'_'", "'`'", 
			"'a'", "'b'", "'c'", "'d'", "'e'", "'f'", "'g'", "'h'", "'i'", "'j'", 
			"'k'", "'l'", "'m'", "'n'", "'o'", "'p'", "'q'", "'r'", "'s'", "'t'", 
			"'u'", "'v'", "'w'", "'x'", "'y'", "'z'", "'{'", "'|'", "'}'", "'~'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "UTF8_LETTER", "TAB", "LF", "CR", "SPACE", "EXCLAMATION", "QUOTE", 
			"POUND", "DOLLAR", "PERCENT", "AMPERSAND", "APOSTROPHE", "LEFT_PAREN", 
			"RIGHT_PAREN", "ASTERISK", "PLUS", "COMMA", "DASH", "PERIOD", "SLASH", 
			"ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", 
			"NINE", "COLON", "SEMICOLON", "LESS_THAN", "EQUALS", "GREATER_THAN", 
			"QUESTION", "AT", "CAP_A", "CAP_B", "CAP_C", "CAP_D", "CAP_E", "CAP_F", 
			"CAP_G", "CAP_H", "CAP_I", "CAP_J", "CAP_K", "CAP_L", "CAP_M", "CAP_N", 
			"CAP_O", "CAP_P", "CAP_Q", "CAP_R", "CAP_S", "CAP_T", "CAP_U", "CAP_V", 
			"CAP_W", "CAP_X", "CAP_Y", "CAP_Z", "LEFT_BRACE", "BACKSLASH", "RIGHT_BRACE", 
			"CARAT", "UNDERSCORE", "ACCENT", "A", "B", "C", "D", "E", "F", "G", "H", 
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", 
			"W", "X", "Y", "Z", "LEFT_CURLY_BRACE", "PIPE", "RIGHT_CURLY_BRACE", 
			"TILDE"
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
	public String getGrammarFileName() { return "java-escape"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ECLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EclContext extends ParserRuleContext {
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public ExpressionconstraintContext expressionconstraint() {
			return getRuleContext(ExpressionconstraintContext.class,0);
		}
		public TerminalNode EOF() { return getToken(ECLParser.EOF, 0); }
		public EclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterEcl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitEcl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitEcl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EclContext ecl() throws RecognitionException {
		EclContext _localctx = new EclContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_ecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			ws();
			setState(145);
			expressionconstraint();
			setState(146);
			ws();
			setState(147);
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
	public static class ExpressionconstraintContext extends ParserRuleContext {
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public RefinedexpressionconstraintContext refinedexpressionconstraint() {
			return getRuleContext(RefinedexpressionconstraintContext.class,0);
		}
		public CompoundexpressionconstraintContext compoundexpressionconstraint() {
			return getRuleContext(CompoundexpressionconstraintContext.class,0);
		}
		public DottedexpressionconstraintContext dottedexpressionconstraint() {
			return getRuleContext(DottedexpressionconstraintContext.class,0);
		}
		public SubexpressionconstraintContext subexpressionconstraint() {
			return getRuleContext(SubexpressionconstraintContext.class,0);
		}
		public ExpressionconstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionconstraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterExpressionconstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitExpressionconstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitExpressionconstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionconstraintContext expressionconstraint() throws RecognitionException {
		ExpressionconstraintContext _localctx = new ExpressionconstraintContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_expressionconstraint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
			ws();
			setState(154);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(150);
				refinedexpressionconstraint();
				}
				break;
			case 2:
				{
				setState(151);
				compoundexpressionconstraint();
				}
				break;
			case 3:
				{
				setState(152);
				dottedexpressionconstraint();
				}
				break;
			case 4:
				{
				setState(153);
				subexpressionconstraint();
				}
				break;
			}
			setState(156);
			ws();
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
	public static class RefinedexpressionconstraintContext extends ParserRuleContext {
		public SubexpressionconstraintContext subexpressionconstraint() {
			return getRuleContext(SubexpressionconstraintContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public TerminalNode COLON() { return getToken(ECLParser.COLON, 0); }
		public EclrefinementContext eclrefinement() {
			return getRuleContext(EclrefinementContext.class,0);
		}
		public RefinedexpressionconstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_refinedexpressionconstraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterRefinedexpressionconstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitRefinedexpressionconstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitRefinedexpressionconstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RefinedexpressionconstraintContext refinedexpressionconstraint() throws RecognitionException {
		RefinedexpressionconstraintContext _localctx = new RefinedexpressionconstraintContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_refinedexpressionconstraint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(158);
			subexpressionconstraint();
			setState(159);
			ws();
			setState(160);
			match(COLON);
			setState(161);
			ws();
			setState(162);
			eclrefinement();
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
	public static class CompoundexpressionconstraintContext extends ParserRuleContext {
		public ConjunctionexpressionconstraintContext conjunctionexpressionconstraint() {
			return getRuleContext(ConjunctionexpressionconstraintContext.class,0);
		}
		public DisjunctionexpressionconstraintContext disjunctionexpressionconstraint() {
			return getRuleContext(DisjunctionexpressionconstraintContext.class,0);
		}
		public ExclusionexpressionconstraintContext exclusionexpressionconstraint() {
			return getRuleContext(ExclusionexpressionconstraintContext.class,0);
		}
		public CompoundexpressionconstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compoundexpressionconstraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterCompoundexpressionconstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitCompoundexpressionconstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitCompoundexpressionconstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompoundexpressionconstraintContext compoundexpressionconstraint() throws RecognitionException {
		CompoundexpressionconstraintContext _localctx = new CompoundexpressionconstraintContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_compoundexpressionconstraint);
		try {
			setState(167);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(164);
				conjunctionexpressionconstraint();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(165);
				disjunctionexpressionconstraint();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(166);
				exclusionexpressionconstraint();
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
	public static class ConjunctionexpressionconstraintContext extends ParserRuleContext {
		public List<SubexpressionconstraintContext> subexpressionconstraint() {
			return getRuleContexts(SubexpressionconstraintContext.class);
		}
		public SubexpressionconstraintContext subexpressionconstraint(int i) {
			return getRuleContext(SubexpressionconstraintContext.class,i);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<ConjunctionContext> conjunction() {
			return getRuleContexts(ConjunctionContext.class);
		}
		public ConjunctionContext conjunction(int i) {
			return getRuleContext(ConjunctionContext.class,i);
		}
		public ConjunctionexpressionconstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conjunctionexpressionconstraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterConjunctionexpressionconstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitConjunctionexpressionconstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitConjunctionexpressionconstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConjunctionexpressionconstraintContext conjunctionexpressionconstraint() throws RecognitionException {
		ConjunctionexpressionconstraintContext _localctx = new ConjunctionexpressionconstraintContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_conjunctionexpressionconstraint);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			subexpressionconstraint();
			setState(175); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(170);
					ws();
					setState(171);
					conjunction();
					setState(172);
					ws();
					setState(173);
					subexpressionconstraint();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(177); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
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
	public static class DisjunctionexpressionconstraintContext extends ParserRuleContext {
		public List<SubexpressionconstraintContext> subexpressionconstraint() {
			return getRuleContexts(SubexpressionconstraintContext.class);
		}
		public SubexpressionconstraintContext subexpressionconstraint(int i) {
			return getRuleContext(SubexpressionconstraintContext.class,i);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<DisjunctionContext> disjunction() {
			return getRuleContexts(DisjunctionContext.class);
		}
		public DisjunctionContext disjunction(int i) {
			return getRuleContext(DisjunctionContext.class,i);
		}
		public DisjunctionexpressionconstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_disjunctionexpressionconstraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDisjunctionexpressionconstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDisjunctionexpressionconstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDisjunctionexpressionconstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DisjunctionexpressionconstraintContext disjunctionexpressionconstraint() throws RecognitionException {
		DisjunctionexpressionconstraintContext _localctx = new DisjunctionexpressionconstraintContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_disjunctionexpressionconstraint);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			subexpressionconstraint();
			setState(185); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(180);
					ws();
					setState(181);
					disjunction();
					setState(182);
					ws();
					setState(183);
					subexpressionconstraint();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(187); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
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
	public static class ExclusionexpressionconstraintContext extends ParserRuleContext {
		public List<SubexpressionconstraintContext> subexpressionconstraint() {
			return getRuleContexts(SubexpressionconstraintContext.class);
		}
		public SubexpressionconstraintContext subexpressionconstraint(int i) {
			return getRuleContext(SubexpressionconstraintContext.class,i);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public ExclusionContext exclusion() {
			return getRuleContext(ExclusionContext.class,0);
		}
		public ExclusionexpressionconstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exclusionexpressionconstraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterExclusionexpressionconstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitExclusionexpressionconstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitExclusionexpressionconstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExclusionexpressionconstraintContext exclusionexpressionconstraint() throws RecognitionException {
		ExclusionexpressionconstraintContext _localctx = new ExclusionexpressionconstraintContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_exclusionexpressionconstraint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(189);
			subexpressionconstraint();
			setState(190);
			ws();
			setState(191);
			exclusion();
			setState(192);
			ws();
			setState(193);
			subexpressionconstraint();
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
	public static class DottedexpressionconstraintContext extends ParserRuleContext {
		public SubexpressionconstraintContext subexpressionconstraint() {
			return getRuleContext(SubexpressionconstraintContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<DottedexpressionattributeContext> dottedexpressionattribute() {
			return getRuleContexts(DottedexpressionattributeContext.class);
		}
		public DottedexpressionattributeContext dottedexpressionattribute(int i) {
			return getRuleContext(DottedexpressionattributeContext.class,i);
		}
		public DottedexpressionconstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dottedexpressionconstraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDottedexpressionconstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDottedexpressionconstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDottedexpressionconstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DottedexpressionconstraintContext dottedexpressionconstraint() throws RecognitionException {
		DottedexpressionconstraintContext _localctx = new DottedexpressionconstraintContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_dottedexpressionconstraint);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			subexpressionconstraint();
			setState(199); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(196);
					ws();
					setState(197);
					dottedexpressionattribute();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(201); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
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
	public static class DottedexpressionattributeContext extends ParserRuleContext {
		public DotContext dot() {
			return getRuleContext(DotContext.class,0);
		}
		public WsContext ws() {
			return getRuleContext(WsContext.class,0);
		}
		public EclattributenameContext eclattributename() {
			return getRuleContext(EclattributenameContext.class,0);
		}
		public DottedexpressionattributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dottedexpressionattribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDottedexpressionattribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDottedexpressionattribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDottedexpressionattribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DottedexpressionattributeContext dottedexpressionattribute() throws RecognitionException {
		DottedexpressionattributeContext _localctx = new DottedexpressionattributeContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_dottedexpressionattribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(203);
			dot();
			setState(204);
			ws();
			setState(205);
			eclattributename();
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
	public static class SubexpressionconstraintContext extends ParserRuleContext {
		public EclfocusconceptContext eclfocusconcept() {
			return getRuleContext(EclfocusconceptContext.class,0);
		}
		public ConstraintoperatorContext constraintoperator() {
			return getRuleContext(ConstraintoperatorContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public MemberofContext memberof() {
			return getRuleContext(MemberofContext.class,0);
		}
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public ExpressionconstraintContext expressionconstraint() {
			return getRuleContext(ExpressionconstraintContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public SubexpressionconstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subexpressionconstraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterSubexpressionconstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitSubexpressionconstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitSubexpressionconstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubexpressionconstraintContext subexpressionconstraint() throws RecognitionException {
		SubexpressionconstraintContext _localctx = new SubexpressionconstraintContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_subexpressionconstraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la - 33)) & ~0x3f) == 0 && ((1L << (_la - 33)) & 4505386334814629L) != 0) {
				{
				setState(207);
				constraintoperator();
				setState(208);
				ws();
				}
			}

			setState(215);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CARAT) {
				{
				setState(212);
				memberof();
				setState(213);
				ws();
				}
			}

			setState(224);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ASTERISK:
			case ONE:
			case TWO:
			case THREE:
			case FOUR:
			case FIVE:
			case SIX:
			case SEVEN:
			case EIGHT:
			case NINE:
			case H:
				{
				setState(217);
				eclfocusconcept();
				}
				break;
			case LEFT_PAREN:
				{
				{
				setState(218);
				match(LEFT_PAREN);
				setState(219);
				ws();
				setState(220);
				expressionconstraint();
				setState(221);
				ws();
				setState(222);
				match(RIGHT_PAREN);
				}
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
	public static class EclfocusconceptContext extends ParserRuleContext {
		public EclconceptreferenceContext eclconceptreference() {
			return getRuleContext(EclconceptreferenceContext.class,0);
		}
		public WildcardContext wildcard() {
			return getRuleContext(WildcardContext.class,0);
		}
		public EclfocusconceptContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eclfocusconcept; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterEclfocusconcept(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitEclfocusconcept(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitEclfocusconcept(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EclfocusconceptContext eclfocusconcept() throws RecognitionException {
		EclfocusconceptContext _localctx = new EclfocusconceptContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_eclfocusconcept);
		try {
			setState(228);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ONE:
			case TWO:
			case THREE:
			case FOUR:
			case FIVE:
			case SIX:
			case SEVEN:
			case EIGHT:
			case NINE:
			case H:
				enterOuterAlt(_localctx, 1);
				{
				setState(226);
				eclconceptreference();
				}
				break;
			case ASTERISK:
				enterOuterAlt(_localctx, 2);
				{
				setState(227);
				wildcard();
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
	public static class DotContext extends ParserRuleContext {
		public TerminalNode PERIOD() { return getToken(ECLParser.PERIOD, 0); }
		public DotContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dot; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DotContext dot() throws RecognitionException {
		DotContext _localctx = new DotContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_dot);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(230);
			match(PERIOD);
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
	public static class MemberofContext extends ParserRuleContext {
		public TerminalNode CARAT() { return getToken(ECLParser.CARAT, 0); }
		public MemberofContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberof; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterMemberof(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitMemberof(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitMemberof(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemberofContext memberof() throws RecognitionException {
		MemberofContext _localctx = new MemberofContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_memberof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(232);
			match(CARAT);
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
	public static class EclconceptreferenceContext extends ParserRuleContext {
		public ConceptidContext conceptid() {
			return getRuleContext(ConceptidContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<TerminalNode> PIPE() { return getTokens(ECLParser.PIPE); }
		public TerminalNode PIPE(int i) {
			return getToken(ECLParser.PIPE, i);
		}
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public EclconceptreferenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eclconceptreference; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterEclconceptreference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitEclconceptreference(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitEclconceptreference(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EclconceptreferenceContext eclconceptreference() throws RecognitionException {
		EclconceptreferenceContext _localctx = new EclconceptreferenceContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_eclconceptreference);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(234);
			conceptid();
			setState(242);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				{
				setState(235);
				ws();
				setState(236);
				match(PIPE);
				setState(237);
				ws();
				setState(238);
				term();
				setState(239);
				ws();
				setState(240);
				match(PIPE);
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
	public static class ConceptidContext extends ParserRuleContext {
		public SctidContext sctid() {
			return getRuleContext(SctidContext.class,0);
		}
		public ConceptidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conceptid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterConceptid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitConceptid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitConceptid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConceptidContext conceptid() throws RecognitionException {
		ConceptidContext _localctx = new ConceptidContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_conceptid);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(244);
			sctid();
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
		public List<NonwsnonpipeContext> nonwsnonpipe() {
			return getRuleContexts(NonwsnonpipeContext.class);
		}
		public NonwsnonpipeContext nonwsnonpipe(int i) {
			return getRuleContext(NonwsnonpipeContext.class,i);
		}
		public List<SpContext> sp() {
			return getRuleContexts(SpContext.class);
		}
		public SpContext sp(int i) {
			return getRuleContext(SpContext.class,i);
		}
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_term);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(247); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(246);
					nonwsnonpipe();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(249); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(263);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(252); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(251);
						sp();
						}
						}
						setState(254); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==SPACE );
					setState(257); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(256);
							nonwsnonpipe();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(259); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					}
					} 
				}
				setState(265);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
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
	public static class WildcardContext extends ParserRuleContext {
		public TerminalNode ASTERISK() { return getToken(ECLParser.ASTERISK, 0); }
		public WildcardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterWildcard(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitWildcard(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitWildcard(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WildcardContext wildcard() throws RecognitionException {
		WildcardContext _localctx = new WildcardContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_wildcard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(266);
			match(ASTERISK);
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
	public static class ConstraintoperatorContext extends ParserRuleContext {
		public ChildofContext childof() {
			return getRuleContext(ChildofContext.class,0);
		}
		public DescendantorselfofContext descendantorselfof() {
			return getRuleContext(DescendantorselfofContext.class,0);
		}
		public DescendantofContext descendantof() {
			return getRuleContext(DescendantofContext.class,0);
		}
		public ParentofContext parentof() {
			return getRuleContext(ParentofContext.class,0);
		}
		public AncestororselfofContext ancestororselfof() {
			return getRuleContext(AncestororselfofContext.class,0);
		}
		public AncestorofContext ancestorof() {
			return getRuleContext(AncestorofContext.class,0);
		}
		public ConstraintoperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constraintoperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterConstraintoperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitConstraintoperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitConstraintoperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstraintoperatorContext constraintoperator() throws RecognitionException {
		ConstraintoperatorContext _localctx = new ConstraintoperatorContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_constraintoperator);
		try {
			setState(274);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(268);
				childof();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(269);
				descendantorselfof();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(270);
				descendantof();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(271);
				parentof();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(272);
				ancestororselfof();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(273);
				ancestorof();
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
	public static class DescendantofContext extends ParserRuleContext {
		public TerminalNode LESS_THAN() { return getToken(ECLParser.LESS_THAN, 0); }
		public List<TerminalNode> D() { return getTokens(ECLParser.D); }
		public TerminalNode D(int i) {
			return getToken(ECLParser.D, i);
		}
		public List<TerminalNode> CAP_D() { return getTokens(ECLParser.CAP_D); }
		public TerminalNode CAP_D(int i) {
			return getToken(ECLParser.CAP_D, i);
		}
		public List<TerminalNode> E() { return getTokens(ECLParser.E); }
		public TerminalNode E(int i) {
			return getToken(ECLParser.E, i);
		}
		public List<TerminalNode> CAP_E() { return getTokens(ECLParser.CAP_E); }
		public TerminalNode CAP_E(int i) {
			return getToken(ECLParser.CAP_E, i);
		}
		public TerminalNode S() { return getToken(ECLParser.S, 0); }
		public TerminalNode CAP_S() { return getToken(ECLParser.CAP_S, 0); }
		public TerminalNode C() { return getToken(ECLParser.C, 0); }
		public TerminalNode CAP_C() { return getToken(ECLParser.CAP_C, 0); }
		public List<TerminalNode> N() { return getTokens(ECLParser.N); }
		public TerminalNode N(int i) {
			return getToken(ECLParser.N, i);
		}
		public List<TerminalNode> CAP_N() { return getTokens(ECLParser.CAP_N); }
		public TerminalNode CAP_N(int i) {
			return getToken(ECLParser.CAP_N, i);
		}
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode O() { return getToken(ECLParser.O, 0); }
		public TerminalNode CAP_O() { return getToken(ECLParser.CAP_O, 0); }
		public TerminalNode F() { return getToken(ECLParser.F, 0); }
		public TerminalNode CAP_F() { return getToken(ECLParser.CAP_F, 0); }
		public DescendantofContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_descendantof; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDescendantof(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDescendantof(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDescendantof(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DescendantofContext descendantof() throws RecognitionException {
		DescendantofContext _localctx = new DescendantofContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_descendantof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(289);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LESS_THAN:
				{
				setState(276);
				match(LESS_THAN);
				}
				break;
			case CAP_D:
			case D:
				{
				{
				setState(277);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(278);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(279);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(280);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(281);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(282);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(283);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(284);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(285);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(286);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(287);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(288);
				_la = _input.LA(1);
				if ( !(_la==CAP_F || _la==F) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
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
	public static class DescendantorselfofContext extends ParserRuleContext {
		public List<TerminalNode> LESS_THAN() { return getTokens(ECLParser.LESS_THAN); }
		public TerminalNode LESS_THAN(int i) {
			return getToken(ECLParser.LESS_THAN, i);
		}
		public List<TerminalNode> D() { return getTokens(ECLParser.D); }
		public TerminalNode D(int i) {
			return getToken(ECLParser.D, i);
		}
		public List<TerminalNode> CAP_D() { return getTokens(ECLParser.CAP_D); }
		public TerminalNode CAP_D(int i) {
			return getToken(ECLParser.CAP_D, i);
		}
		public List<TerminalNode> E() { return getTokens(ECLParser.E); }
		public TerminalNode E(int i) {
			return getToken(ECLParser.E, i);
		}
		public List<TerminalNode> CAP_E() { return getTokens(ECLParser.CAP_E); }
		public TerminalNode CAP_E(int i) {
			return getToken(ECLParser.CAP_E, i);
		}
		public List<TerminalNode> S() { return getTokens(ECLParser.S); }
		public TerminalNode S(int i) {
			return getToken(ECLParser.S, i);
		}
		public List<TerminalNode> CAP_S() { return getTokens(ECLParser.CAP_S); }
		public TerminalNode CAP_S(int i) {
			return getToken(ECLParser.CAP_S, i);
		}
		public TerminalNode C() { return getToken(ECLParser.C, 0); }
		public TerminalNode CAP_C() { return getToken(ECLParser.CAP_C, 0); }
		public List<TerminalNode> N() { return getTokens(ECLParser.N); }
		public TerminalNode N(int i) {
			return getToken(ECLParser.N, i);
		}
		public List<TerminalNode> CAP_N() { return getTokens(ECLParser.CAP_N); }
		public TerminalNode CAP_N(int i) {
			return getToken(ECLParser.CAP_N, i);
		}
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public List<TerminalNode> O() { return getTokens(ECLParser.O); }
		public TerminalNode O(int i) {
			return getToken(ECLParser.O, i);
		}
		public List<TerminalNode> CAP_O() { return getTokens(ECLParser.CAP_O); }
		public TerminalNode CAP_O(int i) {
			return getToken(ECLParser.CAP_O, i);
		}
		public TerminalNode R() { return getToken(ECLParser.R, 0); }
		public TerminalNode CAP_R() { return getToken(ECLParser.CAP_R, 0); }
		public TerminalNode L() { return getToken(ECLParser.L, 0); }
		public TerminalNode CAP_L() { return getToken(ECLParser.CAP_L, 0); }
		public List<TerminalNode> F() { return getTokens(ECLParser.F); }
		public TerminalNode F(int i) {
			return getToken(ECLParser.F, i);
		}
		public List<TerminalNode> CAP_F() { return getTokens(ECLParser.CAP_F); }
		public TerminalNode CAP_F(int i) {
			return getToken(ECLParser.CAP_F, i);
		}
		public DescendantorselfofContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_descendantorselfof; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDescendantorselfof(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDescendantorselfof(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDescendantorselfof(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DescendantorselfofContext descendantorselfof() throws RecognitionException {
		DescendantorselfofContext _localctx = new DescendantorselfofContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_descendantorselfof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(311);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LESS_THAN:
				{
				{
				setState(291);
				match(LESS_THAN);
				setState(292);
				match(LESS_THAN);
				}
				}
				break;
			case CAP_D:
			case D:
				{
				{
				setState(293);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(294);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(295);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(296);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(297);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(298);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(299);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(300);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(301);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(302);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(303);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(304);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(305);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(306);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(307);
				_la = _input.LA(1);
				if ( !(_la==CAP_L || _la==L) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(308);
				_la = _input.LA(1);
				if ( !(_la==CAP_F || _la==F) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(309);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(310);
				_la = _input.LA(1);
				if ( !(_la==CAP_F || _la==F) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
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
	public static class ChildofContext extends ParserRuleContext {
		public TerminalNode LESS_THAN() { return getToken(ECLParser.LESS_THAN, 0); }
		public TerminalNode EXCLAMATION() { return getToken(ECLParser.EXCLAMATION, 0); }
		public TerminalNode C() { return getToken(ECLParser.C, 0); }
		public TerminalNode CAP_C() { return getToken(ECLParser.CAP_C, 0); }
		public TerminalNode H() { return getToken(ECLParser.H, 0); }
		public TerminalNode CAP_H() { return getToken(ECLParser.CAP_H, 0); }
		public TerminalNode I() { return getToken(ECLParser.I, 0); }
		public TerminalNode CAP_I() { return getToken(ECLParser.CAP_I, 0); }
		public TerminalNode L() { return getToken(ECLParser.L, 0); }
		public TerminalNode CAP_L() { return getToken(ECLParser.CAP_L, 0); }
		public TerminalNode D() { return getToken(ECLParser.D, 0); }
		public TerminalNode CAP_D() { return getToken(ECLParser.CAP_D, 0); }
		public TerminalNode O() { return getToken(ECLParser.O, 0); }
		public TerminalNode CAP_O() { return getToken(ECLParser.CAP_O, 0); }
		public TerminalNode F() { return getToken(ECLParser.F, 0); }
		public TerminalNode CAP_F() { return getToken(ECLParser.CAP_F, 0); }
		public ChildofContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_childof; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterChildof(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitChildof(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitChildof(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ChildofContext childof() throws RecognitionException {
		ChildofContext _localctx = new ChildofContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_childof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(322);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LESS_THAN:
				{
				{
				setState(313);
				match(LESS_THAN);
				setState(314);
				match(EXCLAMATION);
				}
				}
				break;
			case CAP_C:
			case C:
				{
				{
				setState(315);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(316);
				_la = _input.LA(1);
				if ( !(_la==CAP_H || _la==H) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(317);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(318);
				_la = _input.LA(1);
				if ( !(_la==CAP_L || _la==L) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(319);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(320);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(321);
				_la = _input.LA(1);
				if ( !(_la==CAP_F || _la==F) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
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
	public static class AncestorofContext extends ParserRuleContext {
		public TerminalNode GREATER_THAN() { return getToken(ECLParser.GREATER_THAN, 0); }
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode N() { return getToken(ECLParser.N, 0); }
		public TerminalNode CAP_N() { return getToken(ECLParser.CAP_N, 0); }
		public TerminalNode C() { return getToken(ECLParser.C, 0); }
		public TerminalNode CAP_C() { return getToken(ECLParser.CAP_C, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode S() { return getToken(ECLParser.S, 0); }
		public TerminalNode CAP_S() { return getToken(ECLParser.CAP_S, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public List<TerminalNode> O() { return getTokens(ECLParser.O); }
		public TerminalNode O(int i) {
			return getToken(ECLParser.O, i);
		}
		public List<TerminalNode> CAP_O() { return getTokens(ECLParser.CAP_O); }
		public TerminalNode CAP_O(int i) {
			return getToken(ECLParser.CAP_O, i);
		}
		public TerminalNode R() { return getToken(ECLParser.R, 0); }
		public TerminalNode CAP_R() { return getToken(ECLParser.CAP_R, 0); }
		public TerminalNode F() { return getToken(ECLParser.F, 0); }
		public TerminalNode CAP_F() { return getToken(ECLParser.CAP_F, 0); }
		public AncestorofContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ancestorof; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterAncestorof(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitAncestorof(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitAncestorof(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AncestorofContext ancestorof() throws RecognitionException {
		AncestorofContext _localctx = new AncestorofContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_ancestorof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(335);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GREATER_THAN:
				{
				setState(324);
				match(GREATER_THAN);
				}
				break;
			case CAP_A:
			case A:
				{
				{
				setState(325);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(326);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(327);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(328);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(329);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(330);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(331);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(332);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(333);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(334);
				_la = _input.LA(1);
				if ( !(_la==CAP_F || _la==F) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
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
	public static class AncestororselfofContext extends ParserRuleContext {
		public List<TerminalNode> GREATER_THAN() { return getTokens(ECLParser.GREATER_THAN); }
		public TerminalNode GREATER_THAN(int i) {
			return getToken(ECLParser.GREATER_THAN, i);
		}
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode N() { return getToken(ECLParser.N, 0); }
		public TerminalNode CAP_N() { return getToken(ECLParser.CAP_N, 0); }
		public TerminalNode C() { return getToken(ECLParser.C, 0); }
		public TerminalNode CAP_C() { return getToken(ECLParser.CAP_C, 0); }
		public List<TerminalNode> E() { return getTokens(ECLParser.E); }
		public TerminalNode E(int i) {
			return getToken(ECLParser.E, i);
		}
		public List<TerminalNode> CAP_E() { return getTokens(ECLParser.CAP_E); }
		public TerminalNode CAP_E(int i) {
			return getToken(ECLParser.CAP_E, i);
		}
		public List<TerminalNode> S() { return getTokens(ECLParser.S); }
		public TerminalNode S(int i) {
			return getToken(ECLParser.S, i);
		}
		public List<TerminalNode> CAP_S() { return getTokens(ECLParser.CAP_S); }
		public TerminalNode CAP_S(int i) {
			return getToken(ECLParser.CAP_S, i);
		}
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public List<TerminalNode> O() { return getTokens(ECLParser.O); }
		public TerminalNode O(int i) {
			return getToken(ECLParser.O, i);
		}
		public List<TerminalNode> CAP_O() { return getTokens(ECLParser.CAP_O); }
		public TerminalNode CAP_O(int i) {
			return getToken(ECLParser.CAP_O, i);
		}
		public List<TerminalNode> R() { return getTokens(ECLParser.R); }
		public TerminalNode R(int i) {
			return getToken(ECLParser.R, i);
		}
		public List<TerminalNode> CAP_R() { return getTokens(ECLParser.CAP_R); }
		public TerminalNode CAP_R(int i) {
			return getToken(ECLParser.CAP_R, i);
		}
		public TerminalNode L() { return getToken(ECLParser.L, 0); }
		public TerminalNode CAP_L() { return getToken(ECLParser.CAP_L, 0); }
		public List<TerminalNode> F() { return getTokens(ECLParser.F); }
		public TerminalNode F(int i) {
			return getToken(ECLParser.F, i);
		}
		public List<TerminalNode> CAP_F() { return getTokens(ECLParser.CAP_F); }
		public TerminalNode CAP_F(int i) {
			return getToken(ECLParser.CAP_F, i);
		}
		public AncestororselfofContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ancestororselfof; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterAncestororselfof(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitAncestororselfof(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitAncestororselfof(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AncestororselfofContext ancestororselfof() throws RecognitionException {
		AncestororselfofContext _localctx = new AncestororselfofContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_ancestororselfof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(355);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GREATER_THAN:
				{
				{
				setState(337);
				match(GREATER_THAN);
				setState(338);
				match(GREATER_THAN);
				}
				}
				break;
			case CAP_A:
			case A:
				{
				{
				setState(339);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(340);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(341);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(342);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(343);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(344);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(345);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(346);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(347);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(348);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(349);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(350);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(351);
				_la = _input.LA(1);
				if ( !(_la==CAP_L || _la==L) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(352);
				_la = _input.LA(1);
				if ( !(_la==CAP_F || _la==F) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(353);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(354);
				_la = _input.LA(1);
				if ( !(_la==CAP_F || _la==F) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
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
	public static class ParentofContext extends ParserRuleContext {
		public TerminalNode GREATER_THAN() { return getToken(ECLParser.GREATER_THAN, 0); }
		public TerminalNode EXCLAMATION() { return getToken(ECLParser.EXCLAMATION, 0); }
		public TerminalNode P() { return getToken(ECLParser.P, 0); }
		public TerminalNode CAP_P() { return getToken(ECLParser.CAP_P, 0); }
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode R() { return getToken(ECLParser.R, 0); }
		public TerminalNode CAP_R() { return getToken(ECLParser.CAP_R, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode N() { return getToken(ECLParser.N, 0); }
		public TerminalNode CAP_N() { return getToken(ECLParser.CAP_N, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode O() { return getToken(ECLParser.O, 0); }
		public TerminalNode CAP_O() { return getToken(ECLParser.CAP_O, 0); }
		public TerminalNode F() { return getToken(ECLParser.F, 0); }
		public TerminalNode CAP_F() { return getToken(ECLParser.CAP_F, 0); }
		public ParentofContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parentof; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterParentof(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitParentof(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitParentof(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParentofContext parentof() throws RecognitionException {
		ParentofContext _localctx = new ParentofContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_parentof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(367);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GREATER_THAN:
				{
				{
				setState(357);
				match(GREATER_THAN);
				setState(358);
				match(EXCLAMATION);
				}
				}
				break;
			case CAP_P:
			case P:
				{
				{
				setState(359);
				_la = _input.LA(1);
				if ( !(_la==CAP_P || _la==P) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(360);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(361);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(362);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(363);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(364);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(365);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(366);
				_la = _input.LA(1);
				if ( !(_la==CAP_F || _la==F) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
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
	public static class ConjunctionContext extends ParserRuleContext {
		public MwsContext mws() {
			return getRuleContext(MwsContext.class,0);
		}
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode N() { return getToken(ECLParser.N, 0); }
		public TerminalNode CAP_N() { return getToken(ECLParser.CAP_N, 0); }
		public TerminalNode D() { return getToken(ECLParser.D, 0); }
		public TerminalNode CAP_D() { return getToken(ECLParser.CAP_D, 0); }
		public TerminalNode COMMA() { return getToken(ECLParser.COMMA, 0); }
		public ConjunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conjunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterConjunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitConjunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitConjunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConjunctionContext conjunction() throws RecognitionException {
		ConjunctionContext _localctx = new ConjunctionContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_conjunction);
		int _la;
		try {
			setState(374);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CAP_A:
			case A:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(369);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(370);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(371);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(372);
				mws();
				}
				}
				break;
			case COMMA:
				enterOuterAlt(_localctx, 2);
				{
				setState(373);
				match(COMMA);
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
	public static class DisjunctionContext extends ParserRuleContext {
		public MwsContext mws() {
			return getRuleContext(MwsContext.class,0);
		}
		public TerminalNode O() { return getToken(ECLParser.O, 0); }
		public TerminalNode CAP_O() { return getToken(ECLParser.CAP_O, 0); }
		public TerminalNode R() { return getToken(ECLParser.R, 0); }
		public TerminalNode CAP_R() { return getToken(ECLParser.CAP_R, 0); }
		public DisjunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_disjunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDisjunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDisjunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDisjunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DisjunctionContext disjunction() throws RecognitionException {
		DisjunctionContext _localctx = new DisjunctionContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_disjunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(376);
			_la = _input.LA(1);
			if ( !(_la==CAP_O || _la==O) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(377);
			_la = _input.LA(1);
			if ( !(_la==CAP_R || _la==R) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(378);
			mws();
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
		public MwsContext mws() {
			return getRuleContext(MwsContext.class,0);
		}
		public TerminalNode M() { return getToken(ECLParser.M, 0); }
		public TerminalNode CAP_M() { return getToken(ECLParser.CAP_M, 0); }
		public TerminalNode I() { return getToken(ECLParser.I, 0); }
		public TerminalNode CAP_I() { return getToken(ECLParser.CAP_I, 0); }
		public TerminalNode N() { return getToken(ECLParser.N, 0); }
		public TerminalNode CAP_N() { return getToken(ECLParser.CAP_N, 0); }
		public TerminalNode U() { return getToken(ECLParser.U, 0); }
		public TerminalNode CAP_U() { return getToken(ECLParser.CAP_U, 0); }
		public TerminalNode S() { return getToken(ECLParser.S, 0); }
		public TerminalNode CAP_S() { return getToken(ECLParser.CAP_S, 0); }
		public ExclusionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exclusion; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterExclusion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitExclusion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitExclusion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExclusionContext exclusion() throws RecognitionException {
		ExclusionContext _localctx = new ExclusionContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_exclusion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(380);
			_la = _input.LA(1);
			if ( !(_la==CAP_M || _la==M) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(381);
			_la = _input.LA(1);
			if ( !(_la==CAP_I || _la==I) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(382);
			_la = _input.LA(1);
			if ( !(_la==CAP_N || _la==N) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(383);
			_la = _input.LA(1);
			if ( !(_la==CAP_U || _la==U) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(384);
			_la = _input.LA(1);
			if ( !(_la==CAP_S || _la==S) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(385);
			mws();
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
	public static class EclrefinementContext extends ParserRuleContext {
		public SubrefinementContext subrefinement() {
			return getRuleContext(SubrefinementContext.class,0);
		}
		public WsContext ws() {
			return getRuleContext(WsContext.class,0);
		}
		public ConjunctionrefinementsetContext conjunctionrefinementset() {
			return getRuleContext(ConjunctionrefinementsetContext.class,0);
		}
		public DisjunctionrefinementsetContext disjunctionrefinementset() {
			return getRuleContext(DisjunctionrefinementsetContext.class,0);
		}
		public EclrefinementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eclrefinement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterEclrefinement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitEclrefinement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitEclrefinement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EclrefinementContext eclrefinement() throws RecognitionException {
		EclrefinementContext _localctx = new EclrefinementContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_eclrefinement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(387);
			subrefinement();
			setState(388);
			ws();
			setState(391);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				{
				setState(389);
				conjunctionrefinementset();
				}
				break;
			case 2:
				{
				setState(390);
				disjunctionrefinementset();
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
	public static class ConjunctionrefinementsetContext extends ParserRuleContext {
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<ConjunctionContext> conjunction() {
			return getRuleContexts(ConjunctionContext.class);
		}
		public ConjunctionContext conjunction(int i) {
			return getRuleContext(ConjunctionContext.class,i);
		}
		public List<SubrefinementContext> subrefinement() {
			return getRuleContexts(SubrefinementContext.class);
		}
		public SubrefinementContext subrefinement(int i) {
			return getRuleContext(SubrefinementContext.class,i);
		}
		public ConjunctionrefinementsetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conjunctionrefinementset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterConjunctionrefinementset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitConjunctionrefinementset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitConjunctionrefinementset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConjunctionrefinementsetContext conjunctionrefinementset() throws RecognitionException {
		ConjunctionrefinementsetContext _localctx = new ConjunctionrefinementsetContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_conjunctionrefinementset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(398); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(393);
					ws();
					setState(394);
					conjunction();
					setState(395);
					ws();
					setState(396);
					subrefinement();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(400); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
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
	public static class DisjunctionrefinementsetContext extends ParserRuleContext {
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<DisjunctionContext> disjunction() {
			return getRuleContexts(DisjunctionContext.class);
		}
		public DisjunctionContext disjunction(int i) {
			return getRuleContext(DisjunctionContext.class,i);
		}
		public List<SubrefinementContext> subrefinement() {
			return getRuleContexts(SubrefinementContext.class);
		}
		public SubrefinementContext subrefinement(int i) {
			return getRuleContext(SubrefinementContext.class,i);
		}
		public DisjunctionrefinementsetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_disjunctionrefinementset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDisjunctionrefinementset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDisjunctionrefinementset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDisjunctionrefinementset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DisjunctionrefinementsetContext disjunctionrefinementset() throws RecognitionException {
		DisjunctionrefinementsetContext _localctx = new DisjunctionrefinementsetContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_disjunctionrefinementset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(407); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(402);
					ws();
					setState(403);
					disjunction();
					setState(404);
					ws();
					setState(405);
					subrefinement();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(409); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
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
	public static class SubrefinementContext extends ParserRuleContext {
		public EclattributesetContext eclattributeset() {
			return getRuleContext(EclattributesetContext.class,0);
		}
		public EclattributegroupContext eclattributegroup() {
			return getRuleContext(EclattributegroupContext.class,0);
		}
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public EclrefinementContext eclrefinement() {
			return getRuleContext(EclrefinementContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public SubrefinementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subrefinement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterSubrefinement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitSubrefinement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitSubrefinement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubrefinementContext subrefinement() throws RecognitionException {
		SubrefinementContext _localctx = new SubrefinementContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_subrefinement);
		try {
			setState(419);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(411);
				eclattributeset();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(412);
				eclattributegroup();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(413);
				match(LEFT_PAREN);
				setState(414);
				ws();
				setState(415);
				eclrefinement();
				setState(416);
				ws();
				setState(417);
				match(RIGHT_PAREN);
				}
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
	public static class EclattributesetContext extends ParserRuleContext {
		public SubattributesetContext subattributeset() {
			return getRuleContext(SubattributesetContext.class,0);
		}
		public WsContext ws() {
			return getRuleContext(WsContext.class,0);
		}
		public ConjunctionattributesetContext conjunctionattributeset() {
			return getRuleContext(ConjunctionattributesetContext.class,0);
		}
		public DisjunctionattributesetContext disjunctionattributeset() {
			return getRuleContext(DisjunctionattributesetContext.class,0);
		}
		public EclattributesetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eclattributeset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterEclattributeset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitEclattributeset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitEclattributeset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EclattributesetContext eclattributeset() throws RecognitionException {
		EclattributesetContext _localctx = new EclattributesetContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_eclattributeset);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(421);
			subattributeset();
			setState(422);
			ws();
			setState(425);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				{
				setState(423);
				conjunctionattributeset();
				}
				break;
			case 2:
				{
				setState(424);
				disjunctionattributeset();
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
	public static class ConjunctionattributesetContext extends ParserRuleContext {
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<ConjunctionContext> conjunction() {
			return getRuleContexts(ConjunctionContext.class);
		}
		public ConjunctionContext conjunction(int i) {
			return getRuleContext(ConjunctionContext.class,i);
		}
		public List<SubattributesetContext> subattributeset() {
			return getRuleContexts(SubattributesetContext.class);
		}
		public SubattributesetContext subattributeset(int i) {
			return getRuleContext(SubattributesetContext.class,i);
		}
		public ConjunctionattributesetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conjunctionattributeset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterConjunctionattributeset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitConjunctionattributeset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitConjunctionattributeset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConjunctionattributesetContext conjunctionattributeset() throws RecognitionException {
		ConjunctionattributesetContext _localctx = new ConjunctionattributesetContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_conjunctionattributeset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(432); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(427);
					ws();
					setState(428);
					conjunction();
					setState(429);
					ws();
					setState(430);
					subattributeset();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(434); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
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
	public static class DisjunctionattributesetContext extends ParserRuleContext {
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<DisjunctionContext> disjunction() {
			return getRuleContexts(DisjunctionContext.class);
		}
		public DisjunctionContext disjunction(int i) {
			return getRuleContext(DisjunctionContext.class,i);
		}
		public List<SubattributesetContext> subattributeset() {
			return getRuleContexts(SubattributesetContext.class);
		}
		public SubattributesetContext subattributeset(int i) {
			return getRuleContext(SubattributesetContext.class,i);
		}
		public DisjunctionattributesetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_disjunctionattributeset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDisjunctionattributeset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDisjunctionattributeset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDisjunctionattributeset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DisjunctionattributesetContext disjunctionattributeset() throws RecognitionException {
		DisjunctionattributesetContext _localctx = new DisjunctionattributesetContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_disjunctionattributeset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(441); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(436);
					ws();
					setState(437);
					disjunction();
					setState(438);
					ws();
					setState(439);
					subattributeset();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(443); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
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
	public static class SubattributesetContext extends ParserRuleContext {
		public EclattributeContext eclattribute() {
			return getRuleContext(EclattributeContext.class,0);
		}
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public EclattributesetContext eclattributeset() {
			return getRuleContext(EclattributesetContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public SubattributesetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subattributeset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterSubattributeset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitSubattributeset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitSubattributeset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubattributesetContext subattributeset() throws RecognitionException {
		SubattributesetContext _localctx = new SubattributesetContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_subattributeset);
		try {
			setState(452);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(445);
				eclattribute();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(446);
				match(LEFT_PAREN);
				setState(447);
				ws();
				setState(448);
				eclattributeset();
				setState(449);
				ws();
				setState(450);
				match(RIGHT_PAREN);
				}
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
	public static class EclattributegroupContext extends ParserRuleContext {
		public TerminalNode LEFT_CURLY_BRACE() { return getToken(ECLParser.LEFT_CURLY_BRACE, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public EclattributesetContext eclattributeset() {
			return getRuleContext(EclattributesetContext.class,0);
		}
		public TerminalNode RIGHT_CURLY_BRACE() { return getToken(ECLParser.RIGHT_CURLY_BRACE, 0); }
		public TerminalNode LEFT_BRACE() { return getToken(ECLParser.LEFT_BRACE, 0); }
		public CardinalityContext cardinality() {
			return getRuleContext(CardinalityContext.class,0);
		}
		public TerminalNode RIGHT_BRACE() { return getToken(ECLParser.RIGHT_BRACE, 0); }
		public EclattributegroupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eclattributegroup; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterEclattributegroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitEclattributegroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitEclattributegroup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EclattributegroupContext eclattributegroup() throws RecognitionException {
		EclattributegroupContext _localctx = new EclattributegroupContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_eclattributegroup);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(459);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LEFT_BRACE) {
				{
				setState(454);
				match(LEFT_BRACE);
				setState(455);
				cardinality();
				setState(456);
				match(RIGHT_BRACE);
				setState(457);
				ws();
				}
			}

			setState(461);
			match(LEFT_CURLY_BRACE);
			setState(462);
			ws();
			setState(463);
			eclattributeset();
			setState(464);
			ws();
			setState(465);
			match(RIGHT_CURLY_BRACE);
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
	public static class EclattributeContext extends ParserRuleContext {
		public EclattributenameContext eclattributename() {
			return getRuleContext(EclattributenameContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public TerminalNode LEFT_BRACE() { return getToken(ECLParser.LEFT_BRACE, 0); }
		public CardinalityContext cardinality() {
			return getRuleContext(CardinalityContext.class,0);
		}
		public TerminalNode RIGHT_BRACE() { return getToken(ECLParser.RIGHT_BRACE, 0); }
		public ReverseflagContext reverseflag() {
			return getRuleContext(ReverseflagContext.class,0);
		}
		public ExpressioncomparisonoperatorContext expressioncomparisonoperator() {
			return getRuleContext(ExpressioncomparisonoperatorContext.class,0);
		}
		public SubexpressionconstraintContext subexpressionconstraint() {
			return getRuleContext(SubexpressionconstraintContext.class,0);
		}
		public NumericcomparisonoperatorContext numericcomparisonoperator() {
			return getRuleContext(NumericcomparisonoperatorContext.class,0);
		}
		public TerminalNode POUND() { return getToken(ECLParser.POUND, 0); }
		public NumericvalueContext numericvalue() {
			return getRuleContext(NumericvalueContext.class,0);
		}
		public StringcomparisonoperatorContext stringcomparisonoperator() {
			return getRuleContext(StringcomparisonoperatorContext.class,0);
		}
		public List<QmContext> qm() {
			return getRuleContexts(QmContext.class);
		}
		public QmContext qm(int i) {
			return getRuleContext(QmContext.class,i);
		}
		public StringvalueContext stringvalue() {
			return getRuleContext(StringvalueContext.class,0);
		}
		public EclattributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eclattribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterEclattribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitEclattribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitEclattribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EclattributeContext eclattribute() throws RecognitionException {
		EclattributeContext _localctx = new EclattributeContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_eclattribute);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(472);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LEFT_BRACE) {
				{
				setState(467);
				match(LEFT_BRACE);
				setState(468);
				cardinality();
				setState(469);
				match(RIGHT_BRACE);
				setState(470);
				ws();
				}
			}

			setState(477);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CAP_R) {
				{
				setState(474);
				reverseflag();
				setState(475);
				ws();
				}
			}

			setState(479);
			eclattributename();
			setState(480);
			ws();
			setState(496);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				{
				setState(481);
				expressioncomparisonoperator();
				setState(482);
				ws();
				setState(483);
				subexpressionconstraint();
				}
				}
				break;
			case 2:
				{
				{
				setState(485);
				numericcomparisonoperator();
				setState(486);
				ws();
				setState(487);
				match(POUND);
				setState(488);
				numericvalue();
				}
				}
				break;
			case 3:
				{
				{
				setState(490);
				stringcomparisonoperator();
				setState(491);
				ws();
				setState(492);
				qm();
				setState(493);
				stringvalue();
				setState(494);
				qm();
				}
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
	public static class CardinalityContext extends ParserRuleContext {
		public MinvalueContext minvalue() {
			return getRuleContext(MinvalueContext.class,0);
		}
		public ToContext to() {
			return getRuleContext(ToContext.class,0);
		}
		public MaxvalueContext maxvalue() {
			return getRuleContext(MaxvalueContext.class,0);
		}
		public CardinalityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cardinality; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterCardinality(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitCardinality(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitCardinality(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CardinalityContext cardinality() throws RecognitionException {
		CardinalityContext _localctx = new CardinalityContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_cardinality);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(498);
			minvalue();
			setState(499);
			to();
			setState(500);
			maxvalue();
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
	public static class MinvalueContext extends ParserRuleContext {
		public NonnegativeintegervalueContext nonnegativeintegervalue() {
			return getRuleContext(NonnegativeintegervalueContext.class,0);
		}
		public MinvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_minvalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterMinvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitMinvalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitMinvalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MinvalueContext minvalue() throws RecognitionException {
		MinvalueContext _localctx = new MinvalueContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_minvalue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(502);
			nonnegativeintegervalue();
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
	public static class ToContext extends ParserRuleContext {
		public List<TerminalNode> PERIOD() { return getTokens(ECLParser.PERIOD); }
		public TerminalNode PERIOD(int i) {
			return getToken(ECLParser.PERIOD, i);
		}
		public ToContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_to; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterTo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitTo(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitTo(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ToContext to() throws RecognitionException {
		ToContext _localctx = new ToContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_to);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(504);
			match(PERIOD);
			setState(505);
			match(PERIOD);
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
	public static class MaxvalueContext extends ParserRuleContext {
		public NonnegativeintegervalueContext nonnegativeintegervalue() {
			return getRuleContext(NonnegativeintegervalueContext.class,0);
		}
		public ManyContext many() {
			return getRuleContext(ManyContext.class,0);
		}
		public MaxvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_maxvalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterMaxvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitMaxvalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitMaxvalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MaxvalueContext maxvalue() throws RecognitionException {
		MaxvalueContext _localctx = new MaxvalueContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_maxvalue);
		try {
			setState(509);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ZERO:
			case ONE:
			case TWO:
			case THREE:
			case FOUR:
			case FIVE:
			case SIX:
			case SEVEN:
			case EIGHT:
			case NINE:
				enterOuterAlt(_localctx, 1);
				{
				setState(507);
				nonnegativeintegervalue();
				}
				break;
			case ASTERISK:
				enterOuterAlt(_localctx, 2);
				{
				setState(508);
				many();
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
	public static class ManyContext extends ParserRuleContext {
		public TerminalNode ASTERISK() { return getToken(ECLParser.ASTERISK, 0); }
		public ManyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_many; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterMany(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitMany(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitMany(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ManyContext many() throws RecognitionException {
		ManyContext _localctx = new ManyContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_many);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(511);
			match(ASTERISK);
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
	public static class ReverseflagContext extends ParserRuleContext {
		public TerminalNode CAP_R() { return getToken(ECLParser.CAP_R, 0); }
		public ReverseflagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reverseflag; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterReverseflag(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitReverseflag(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitReverseflag(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReverseflagContext reverseflag() throws RecognitionException {
		ReverseflagContext _localctx = new ReverseflagContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_reverseflag);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(513);
			match(CAP_R);
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
	public static class EclattributenameContext extends ParserRuleContext {
		public SubexpressionconstraintContext subexpressionconstraint() {
			return getRuleContext(SubexpressionconstraintContext.class,0);
		}
		public EclattributenameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eclattributename; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterEclattributename(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitEclattributename(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitEclattributename(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EclattributenameContext eclattributename() throws RecognitionException {
		EclattributenameContext _localctx = new EclattributenameContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_eclattributename);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(515);
			subexpressionconstraint();
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
	public static class ExpressioncomparisonoperatorContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(ECLParser.EQUALS, 0); }
		public TerminalNode EXCLAMATION() { return getToken(ECLParser.EXCLAMATION, 0); }
		public ExpressioncomparisonoperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressioncomparisonoperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterExpressioncomparisonoperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitExpressioncomparisonoperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitExpressioncomparisonoperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressioncomparisonoperatorContext expressioncomparisonoperator() throws RecognitionException {
		ExpressioncomparisonoperatorContext _localctx = new ExpressioncomparisonoperatorContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_expressioncomparisonoperator);
		try {
			setState(520);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EQUALS:
				enterOuterAlt(_localctx, 1);
				{
				setState(517);
				match(EQUALS);
				}
				break;
			case EXCLAMATION:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(518);
				match(EXCLAMATION);
				setState(519);
				match(EQUALS);
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
	public static class NumericcomparisonoperatorContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(ECLParser.EQUALS, 0); }
		public TerminalNode EXCLAMATION() { return getToken(ECLParser.EXCLAMATION, 0); }
		public TerminalNode LESS_THAN() { return getToken(ECLParser.LESS_THAN, 0); }
		public TerminalNode GREATER_THAN() { return getToken(ECLParser.GREATER_THAN, 0); }
		public NumericcomparisonoperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numericcomparisonoperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterNumericcomparisonoperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitNumericcomparisonoperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitNumericcomparisonoperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumericcomparisonoperatorContext numericcomparisonoperator() throws RecognitionException {
		NumericcomparisonoperatorContext _localctx = new NumericcomparisonoperatorContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_numericcomparisonoperator);
		try {
			setState(531);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(522);
				match(EQUALS);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(523);
				match(EXCLAMATION);
				setState(524);
				match(EQUALS);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(525);
				match(LESS_THAN);
				setState(526);
				match(EQUALS);
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(527);
				match(LESS_THAN);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				{
				setState(528);
				match(GREATER_THAN);
				setState(529);
				match(EQUALS);
				}
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(530);
				match(GREATER_THAN);
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
	public static class StringcomparisonoperatorContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(ECLParser.EQUALS, 0); }
		public TerminalNode EXCLAMATION() { return getToken(ECLParser.EXCLAMATION, 0); }
		public StringcomparisonoperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringcomparisonoperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterStringcomparisonoperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitStringcomparisonoperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitStringcomparisonoperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringcomparisonoperatorContext stringcomparisonoperator() throws RecognitionException {
		StringcomparisonoperatorContext _localctx = new StringcomparisonoperatorContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_stringcomparisonoperator);
		try {
			setState(536);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EQUALS:
				enterOuterAlt(_localctx, 1);
				{
				setState(533);
				match(EQUALS);
				}
				break;
			case EXCLAMATION:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(534);
				match(EXCLAMATION);
				setState(535);
				match(EQUALS);
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
	public static class NumericvalueContext extends ParserRuleContext {
		public DecimalvalueContext decimalvalue() {
			return getRuleContext(DecimalvalueContext.class,0);
		}
		public IntegervalueContext integervalue() {
			return getRuleContext(IntegervalueContext.class,0);
		}
		public TerminalNode DASH() { return getToken(ECLParser.DASH, 0); }
		public TerminalNode PLUS() { return getToken(ECLParser.PLUS, 0); }
		public NumericvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numericvalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterNumericvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitNumericvalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitNumericvalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumericvalueContext numericvalue() throws RecognitionException {
		NumericvalueContext _localctx = new NumericvalueContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_numericvalue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(539);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PLUS || _la==DASH) {
				{
				setState(538);
				_la = _input.LA(1);
				if ( !(_la==PLUS || _la==DASH) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(543);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				{
				setState(541);
				decimalvalue();
				}
				break;
			case 2:
				{
				setState(542);
				integervalue();
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
	public static class StringvalueContext extends ParserRuleContext {
		public List<AnynonescapedcharContext> anynonescapedchar() {
			return getRuleContexts(AnynonescapedcharContext.class);
		}
		public AnynonescapedcharContext anynonescapedchar(int i) {
			return getRuleContext(AnynonescapedcharContext.class,i);
		}
		public List<EscapedcharContext> escapedchar() {
			return getRuleContexts(EscapedcharContext.class);
		}
		public EscapedcharContext escapedchar(int i) {
			return getRuleContext(EscapedcharContext.class,i);
		}
		public StringvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringvalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterStringvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitStringvalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitStringvalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringvalueContext stringvalue() throws RecognitionException {
		StringvalueContext _localctx = new StringvalueContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_stringvalue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(547); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(547);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case UTF8_LETTER:
				case TAB:
				case LF:
				case CR:
				case SPACE:
				case EXCLAMATION:
				case POUND:
				case DOLLAR:
				case PERCENT:
				case AMPERSAND:
				case APOSTROPHE:
				case LEFT_PAREN:
				case RIGHT_PAREN:
				case ASTERISK:
				case PLUS:
				case COMMA:
				case DASH:
				case PERIOD:
				case SLASH:
				case ZERO:
				case ONE:
				case TWO:
				case THREE:
				case FOUR:
				case FIVE:
				case SIX:
				case SEVEN:
				case EIGHT:
				case NINE:
				case COLON:
				case SEMICOLON:
				case LESS_THAN:
				case EQUALS:
				case GREATER_THAN:
				case QUESTION:
				case AT:
				case CAP_A:
				case CAP_B:
				case CAP_C:
				case CAP_D:
				case CAP_E:
				case CAP_F:
				case CAP_G:
				case CAP_H:
				case CAP_I:
				case CAP_J:
				case CAP_K:
				case CAP_L:
				case CAP_M:
				case CAP_N:
				case CAP_O:
				case CAP_P:
				case CAP_Q:
				case CAP_R:
				case CAP_S:
				case CAP_T:
				case CAP_U:
				case CAP_V:
				case CAP_W:
				case CAP_X:
				case CAP_Y:
				case CAP_Z:
				case LEFT_BRACE:
				case RIGHT_BRACE:
				case CARAT:
				case UNDERSCORE:
				case ACCENT:
				case A:
				case B:
				case C:
				case D:
				case E:
				case F:
				case G:
				case H:
				case I:
				case J:
				case K:
				case L:
				case M:
				case N:
				case O:
				case P:
				case Q:
				case R:
				case S:
				case T:
				case U:
				case V:
				case W:
				case X:
				case Y:
				case Z:
				case LEFT_CURLY_BRACE:
				case PIPE:
				case RIGHT_CURLY_BRACE:
				case TILDE:
					{
					setState(545);
					anynonescapedchar();
					}
					break;
				case BACKSLASH:
					{
					setState(546);
					escapedchar();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(549); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((_la) & ~0x3f) == 0 && ((1L << _la) & -130L) != 0 || (((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 68719476735L) != 0 );
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
	public static class IntegervalueContext extends ParserRuleContext {
		public DigitnonzeroContext digitnonzero() {
			return getRuleContext(DigitnonzeroContext.class,0);
		}
		public List<DigitContext> digit() {
			return getRuleContexts(DigitContext.class);
		}
		public DigitContext digit(int i) {
			return getRuleContext(DigitContext.class,i);
		}
		public ZeroContext zero() {
			return getRuleContext(ZeroContext.class,0);
		}
		public IntegervalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_integervalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterIntegervalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitIntegervalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitIntegervalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntegervalueContext integervalue() throws RecognitionException {
		IntegervalueContext _localctx = new IntegervalueContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_integervalue);
		int _la;
		try {
			setState(559);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ONE:
			case TWO:
			case THREE:
			case FOUR:
			case FIVE:
			case SIX:
			case SEVEN:
			case EIGHT:
			case NINE:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(551);
				digitnonzero();
				setState(555);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((_la) & ~0x3f) == 0 && ((1L << _la) & 2145386496L) != 0) {
					{
					{
					setState(552);
					digit();
					}
					}
					setState(557);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				break;
			case ZERO:
				enterOuterAlt(_localctx, 2);
				{
				setState(558);
				zero();
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
	public static class DecimalvalueContext extends ParserRuleContext {
		public IntegervalueContext integervalue() {
			return getRuleContext(IntegervalueContext.class,0);
		}
		public TerminalNode PERIOD() { return getToken(ECLParser.PERIOD, 0); }
		public List<DigitContext> digit() {
			return getRuleContexts(DigitContext.class);
		}
		public DigitContext digit(int i) {
			return getRuleContext(DigitContext.class,i);
		}
		public DecimalvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decimalvalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDecimalvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDecimalvalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDecimalvalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DecimalvalueContext decimalvalue() throws RecognitionException {
		DecimalvalueContext _localctx = new DecimalvalueContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_decimalvalue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(561);
			integervalue();
			setState(562);
			match(PERIOD);
			setState(564); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(563);
				digit();
				}
				}
				setState(566); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((_la) & ~0x3f) == 0 && ((1L << _la) & 2145386496L) != 0 );
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
	public static class NonnegativeintegervalueContext extends ParserRuleContext {
		public DigitnonzeroContext digitnonzero() {
			return getRuleContext(DigitnonzeroContext.class,0);
		}
		public List<DigitContext> digit() {
			return getRuleContexts(DigitContext.class);
		}
		public DigitContext digit(int i) {
			return getRuleContext(DigitContext.class,i);
		}
		public ZeroContext zero() {
			return getRuleContext(ZeroContext.class,0);
		}
		public NonnegativeintegervalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonnegativeintegervalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterNonnegativeintegervalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitNonnegativeintegervalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitNonnegativeintegervalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonnegativeintegervalueContext nonnegativeintegervalue() throws RecognitionException {
		NonnegativeintegervalueContext _localctx = new NonnegativeintegervalueContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_nonnegativeintegervalue);
		int _la;
		try {
			setState(576);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ONE:
			case TWO:
			case THREE:
			case FOUR:
			case FIVE:
			case SIX:
			case SEVEN:
			case EIGHT:
			case NINE:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(568);
				digitnonzero();
				setState(572);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((_la) & ~0x3f) == 0 && ((1L << _la) & 2145386496L) != 0) {
					{
					{
					setState(569);
					digit();
					}
					}
					setState(574);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				break;
			case ZERO:
				enterOuterAlt(_localctx, 2);
				{
				setState(575);
				zero();
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
	public static class SctidContext extends ParserRuleContext {
		public TerminalNode H() { return getToken(ECLParser.H, 0); }
		public List<TerminalNode> T() { return getTokens(ECLParser.T); }
		public TerminalNode T(int i) {
			return getToken(ECLParser.T, i);
		}
		public TerminalNode P() { return getToken(ECLParser.P, 0); }
		public List<NonspacecharContext> nonspacechar() {
			return getRuleContexts(NonspacecharContext.class);
		}
		public NonspacecharContext nonspacechar(int i) {
			return getRuleContext(NonspacecharContext.class,i);
		}
		public DigitnonzeroContext digitnonzero() {
			return getRuleContext(DigitnonzeroContext.class,0);
		}
		public List<DigitContext> digit() {
			return getRuleContexts(DigitContext.class);
		}
		public DigitContext digit(int i) {
			return getRuleContext(DigitContext.class,i);
		}
		public SctidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sctid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterSctid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitSctid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitSctid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SctidContext sctid() throws RecognitionException {
		SctidContext _localctx = new SctidContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_sctid);
		int _la;
		try {
			int _alt;
			setState(686);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case H:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(578);
				match(H);
				setState(579);
				match(T);
				setState(580);
				match(T);
				setState(581);
				match(P);
				setState(583); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(582);
						nonspacechar();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(585); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				}
				break;
			case ONE:
			case TWO:
			case THREE:
			case FOUR:
			case FIVE:
			case SIX:
			case SEVEN:
			case EIGHT:
			case NINE:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(587);
				digitnonzero();
				{
				setState(588);
				digit();
				}
				{
				setState(589);
				digit();
				}
				{
				setState(590);
				digit();
				}
				{
				setState(591);
				digit();
				}
				{
				setState(592);
				digit();
				}
				setState(684);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
				case 1:
					{
					setState(594);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (((_la) & ~0x3f) == 0 && ((1L << _la) & 2145386496L) != 0) {
						{
						setState(593);
						digit();
						}
					}

					}
					break;
				case 2:
					{
					{
					{
					setState(596);
					digit();
					}
					{
					setState(597);
					digit();
					}
					}
					}
					break;
				case 3:
					{
					{
					{
					setState(599);
					digit();
					}
					{
					setState(600);
					digit();
					}
					{
					setState(601);
					digit();
					}
					}
					}
					break;
				case 4:
					{
					{
					{
					setState(603);
					digit();
					}
					{
					setState(604);
					digit();
					}
					{
					setState(605);
					digit();
					}
					{
					setState(606);
					digit();
					}
					}
					}
					break;
				case 5:
					{
					{
					{
					setState(608);
					digit();
					}
					{
					setState(609);
					digit();
					}
					{
					setState(610);
					digit();
					}
					{
					setState(611);
					digit();
					}
					{
					setState(612);
					digit();
					}
					}
					}
					break;
				case 6:
					{
					{
					{
					setState(614);
					digit();
					}
					{
					setState(615);
					digit();
					}
					{
					setState(616);
					digit();
					}
					{
					setState(617);
					digit();
					}
					{
					setState(618);
					digit();
					}
					{
					setState(619);
					digit();
					}
					}
					}
					break;
				case 7:
					{
					{
					{
					setState(621);
					digit();
					}
					{
					setState(622);
					digit();
					}
					{
					setState(623);
					digit();
					}
					{
					setState(624);
					digit();
					}
					{
					setState(625);
					digit();
					}
					{
					setState(626);
					digit();
					}
					{
					setState(627);
					digit();
					}
					}
					}
					break;
				case 8:
					{
					{
					{
					setState(629);
					digit();
					}
					{
					setState(630);
					digit();
					}
					{
					setState(631);
					digit();
					}
					{
					setState(632);
					digit();
					}
					{
					setState(633);
					digit();
					}
					{
					setState(634);
					digit();
					}
					{
					setState(635);
					digit();
					}
					{
					setState(636);
					digit();
					}
					}
					}
					break;
				case 9:
					{
					{
					{
					setState(638);
					digit();
					}
					{
					setState(639);
					digit();
					}
					{
					setState(640);
					digit();
					}
					{
					setState(641);
					digit();
					}
					{
					setState(642);
					digit();
					}
					{
					setState(643);
					digit();
					}
					{
					setState(644);
					digit();
					}
					{
					setState(645);
					digit();
					}
					{
					setState(646);
					digit();
					}
					}
					}
					break;
				case 10:
					{
					{
					{
					setState(648);
					digit();
					}
					{
					setState(649);
					digit();
					}
					{
					setState(650);
					digit();
					}
					{
					setState(651);
					digit();
					}
					{
					setState(652);
					digit();
					}
					{
					setState(653);
					digit();
					}
					{
					setState(654);
					digit();
					}
					{
					setState(655);
					digit();
					}
					{
					setState(656);
					digit();
					}
					{
					setState(657);
					digit();
					}
					}
					}
					break;
				case 11:
					{
					{
					{
					setState(659);
					digit();
					}
					{
					setState(660);
					digit();
					}
					{
					setState(661);
					digit();
					}
					{
					setState(662);
					digit();
					}
					{
					setState(663);
					digit();
					}
					{
					setState(664);
					digit();
					}
					{
					setState(665);
					digit();
					}
					{
					setState(666);
					digit();
					}
					{
					setState(667);
					digit();
					}
					{
					setState(668);
					digit();
					}
					{
					setState(669);
					digit();
					}
					}
					}
					break;
				case 12:
					{
					{
					{
					setState(671);
					digit();
					}
					{
					setState(672);
					digit();
					}
					{
					setState(673);
					digit();
					}
					{
					setState(674);
					digit();
					}
					{
					setState(675);
					digit();
					}
					{
					setState(676);
					digit();
					}
					{
					setState(677);
					digit();
					}
					{
					setState(678);
					digit();
					}
					{
					setState(679);
					digit();
					}
					{
					setState(680);
					digit();
					}
					{
					setState(681);
					digit();
					}
					{
					setState(682);
					digit();
					}
					}
					}
					break;
				}
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
	public static class WsContext extends ParserRuleContext {
		public List<SpContext> sp() {
			return getRuleContexts(SpContext.class);
		}
		public SpContext sp(int i) {
			return getRuleContext(SpContext.class,i);
		}
		public List<HtabContext> htab() {
			return getRuleContexts(HtabContext.class);
		}
		public HtabContext htab(int i) {
			return getRuleContext(HtabContext.class,i);
		}
		public List<CrContext> cr() {
			return getRuleContexts(CrContext.class);
		}
		public CrContext cr(int i) {
			return getRuleContext(CrContext.class,i);
		}
		public List<LfContext> lf() {
			return getRuleContexts(LfContext.class);
		}
		public LfContext lf(int i) {
			return getRuleContext(LfContext.class,i);
		}
		public List<CommentContext> comment() {
			return getRuleContexts(CommentContext.class);
		}
		public CommentContext comment(int i) {
			return getRuleContext(CommentContext.class,i);
		}
		public WsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ws; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterWs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitWs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitWs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WsContext ws() throws RecognitionException {
		WsContext _localctx = new WsContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_ws);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(695);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(693);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case SPACE:
						{
						setState(688);
						sp();
						}
						break;
					case TAB:
						{
						setState(689);
						htab();
						}
						break;
					case CR:
						{
						setState(690);
						cr();
						}
						break;
					case LF:
						{
						setState(691);
						lf();
						}
						break;
					case SLASH:
						{
						setState(692);
						comment();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(697);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
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
	public static class MwsContext extends ParserRuleContext {
		public List<SpContext> sp() {
			return getRuleContexts(SpContext.class);
		}
		public SpContext sp(int i) {
			return getRuleContext(SpContext.class,i);
		}
		public List<HtabContext> htab() {
			return getRuleContexts(HtabContext.class);
		}
		public HtabContext htab(int i) {
			return getRuleContext(HtabContext.class,i);
		}
		public List<CrContext> cr() {
			return getRuleContexts(CrContext.class);
		}
		public CrContext cr(int i) {
			return getRuleContext(CrContext.class,i);
		}
		public List<LfContext> lf() {
			return getRuleContexts(LfContext.class);
		}
		public LfContext lf(int i) {
			return getRuleContext(LfContext.class,i);
		}
		public List<CommentContext> comment() {
			return getRuleContexts(CommentContext.class);
		}
		public CommentContext comment(int i) {
			return getRuleContext(CommentContext.class,i);
		}
		public MwsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mws; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterMws(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitMws(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitMws(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MwsContext mws() throws RecognitionException {
		MwsContext _localctx = new MwsContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_mws);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(703); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					setState(703);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case SPACE:
						{
						setState(698);
						sp();
						}
						break;
					case TAB:
						{
						setState(699);
						htab();
						}
						break;
					case CR:
						{
						setState(700);
						cr();
						}
						break;
					case LF:
						{
						setState(701);
						lf();
						}
						break;
					case SLASH:
						{
						setState(702);
						comment();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(705); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,54,_ctx);
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
	public static class CommentContext extends ParserRuleContext {
		public List<TerminalNode> SLASH() { return getTokens(ECLParser.SLASH); }
		public TerminalNode SLASH(int i) {
			return getToken(ECLParser.SLASH, i);
		}
		public List<TerminalNode> ASTERISK() { return getTokens(ECLParser.ASTERISK); }
		public TerminalNode ASTERISK(int i) {
			return getToken(ECLParser.ASTERISK, i);
		}
		public List<NonstarcharContext> nonstarchar() {
			return getRuleContexts(NonstarcharContext.class);
		}
		public NonstarcharContext nonstarchar(int i) {
			return getRuleContext(NonstarcharContext.class,i);
		}
		public List<StarwithnonfslashContext> starwithnonfslash() {
			return getRuleContexts(StarwithnonfslashContext.class);
		}
		public StarwithnonfslashContext starwithnonfslash(int i) {
			return getRuleContext(StarwithnonfslashContext.class,i);
		}
		public CommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitComment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommentContext comment() throws RecognitionException {
		CommentContext _localctx = new CommentContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_comment);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(707);
			match(SLASH);
			setState(708);
			match(ASTERISK);
			}
			setState(714);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,56,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(712);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case UTF8_LETTER:
					case TAB:
					case LF:
					case CR:
					case SPACE:
					case EXCLAMATION:
					case QUOTE:
					case POUND:
					case DOLLAR:
					case PERCENT:
					case AMPERSAND:
					case APOSTROPHE:
					case LEFT_PAREN:
					case RIGHT_PAREN:
					case PLUS:
					case COMMA:
					case DASH:
					case PERIOD:
					case SLASH:
					case ZERO:
					case ONE:
					case TWO:
					case THREE:
					case FOUR:
					case FIVE:
					case SIX:
					case SEVEN:
					case EIGHT:
					case NINE:
					case COLON:
					case SEMICOLON:
					case LESS_THAN:
					case EQUALS:
					case GREATER_THAN:
					case QUESTION:
					case AT:
					case CAP_A:
					case CAP_B:
					case CAP_C:
					case CAP_D:
					case CAP_E:
					case CAP_F:
					case CAP_G:
					case CAP_H:
					case CAP_I:
					case CAP_J:
					case CAP_K:
					case CAP_L:
					case CAP_M:
					case CAP_N:
					case CAP_O:
					case CAP_P:
					case CAP_Q:
					case CAP_R:
					case CAP_S:
					case CAP_T:
					case CAP_U:
					case CAP_V:
					case CAP_W:
					case CAP_X:
					case CAP_Y:
					case CAP_Z:
					case LEFT_BRACE:
					case BACKSLASH:
					case RIGHT_BRACE:
					case CARAT:
					case UNDERSCORE:
					case ACCENT:
					case A:
					case B:
					case C:
					case D:
					case E:
					case F:
					case G:
					case H:
					case I:
					case J:
					case K:
					case L:
					case M:
					case N:
					case O:
					case P:
					case Q:
					case R:
					case S:
					case T:
					case U:
					case V:
					case W:
					case X:
					case Y:
					case Z:
					case LEFT_CURLY_BRACE:
					case PIPE:
					case RIGHT_CURLY_BRACE:
					case TILDE:
						{
						setState(710);
						nonstarchar();
						}
						break;
					case ASTERISK:
						{
						setState(711);
						starwithnonfslash();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(716);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,56,_ctx);
			}
			{
			setState(717);
			match(ASTERISK);
			setState(718);
			match(SLASH);
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
	public static class NonstarcharContext extends ParserRuleContext {
		public SpContext sp() {
			return getRuleContext(SpContext.class,0);
		}
		public HtabContext htab() {
			return getRuleContext(HtabContext.class,0);
		}
		public CrContext cr() {
			return getRuleContext(CrContext.class,0);
		}
		public LfContext lf() {
			return getRuleContext(LfContext.class,0);
		}
		public TerminalNode EXCLAMATION() { return getToken(ECLParser.EXCLAMATION, 0); }
		public TerminalNode QUOTE() { return getToken(ECLParser.QUOTE, 0); }
		public TerminalNode POUND() { return getToken(ECLParser.POUND, 0); }
		public TerminalNode DOLLAR() { return getToken(ECLParser.DOLLAR, 0); }
		public TerminalNode PERCENT() { return getToken(ECLParser.PERCENT, 0); }
		public TerminalNode AMPERSAND() { return getToken(ECLParser.AMPERSAND, 0); }
		public TerminalNode APOSTROPHE() { return getToken(ECLParser.APOSTROPHE, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public TerminalNode PLUS() { return getToken(ECLParser.PLUS, 0); }
		public TerminalNode COMMA() { return getToken(ECLParser.COMMA, 0); }
		public TerminalNode DASH() { return getToken(ECLParser.DASH, 0); }
		public TerminalNode PERIOD() { return getToken(ECLParser.PERIOD, 0); }
		public TerminalNode SLASH() { return getToken(ECLParser.SLASH, 0); }
		public TerminalNode ZERO() { return getToken(ECLParser.ZERO, 0); }
		public TerminalNode ONE() { return getToken(ECLParser.ONE, 0); }
		public TerminalNode TWO() { return getToken(ECLParser.TWO, 0); }
		public TerminalNode THREE() { return getToken(ECLParser.THREE, 0); }
		public TerminalNode FOUR() { return getToken(ECLParser.FOUR, 0); }
		public TerminalNode FIVE() { return getToken(ECLParser.FIVE, 0); }
		public TerminalNode SIX() { return getToken(ECLParser.SIX, 0); }
		public TerminalNode SEVEN() { return getToken(ECLParser.SEVEN, 0); }
		public TerminalNode EIGHT() { return getToken(ECLParser.EIGHT, 0); }
		public TerminalNode NINE() { return getToken(ECLParser.NINE, 0); }
		public TerminalNode COLON() { return getToken(ECLParser.COLON, 0); }
		public TerminalNode SEMICOLON() { return getToken(ECLParser.SEMICOLON, 0); }
		public TerminalNode LESS_THAN() { return getToken(ECLParser.LESS_THAN, 0); }
		public TerminalNode EQUALS() { return getToken(ECLParser.EQUALS, 0); }
		public TerminalNode GREATER_THAN() { return getToken(ECLParser.GREATER_THAN, 0); }
		public TerminalNode QUESTION() { return getToken(ECLParser.QUESTION, 0); }
		public TerminalNode AT() { return getToken(ECLParser.AT, 0); }
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode CAP_B() { return getToken(ECLParser.CAP_B, 0); }
		public TerminalNode CAP_C() { return getToken(ECLParser.CAP_C, 0); }
		public TerminalNode CAP_D() { return getToken(ECLParser.CAP_D, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode CAP_F() { return getToken(ECLParser.CAP_F, 0); }
		public TerminalNode CAP_G() { return getToken(ECLParser.CAP_G, 0); }
		public TerminalNode CAP_H() { return getToken(ECLParser.CAP_H, 0); }
		public TerminalNode CAP_I() { return getToken(ECLParser.CAP_I, 0); }
		public TerminalNode CAP_J() { return getToken(ECLParser.CAP_J, 0); }
		public TerminalNode CAP_K() { return getToken(ECLParser.CAP_K, 0); }
		public TerminalNode CAP_L() { return getToken(ECLParser.CAP_L, 0); }
		public TerminalNode CAP_M() { return getToken(ECLParser.CAP_M, 0); }
		public TerminalNode CAP_N() { return getToken(ECLParser.CAP_N, 0); }
		public TerminalNode CAP_O() { return getToken(ECLParser.CAP_O, 0); }
		public TerminalNode CAP_P() { return getToken(ECLParser.CAP_P, 0); }
		public TerminalNode CAP_Q() { return getToken(ECLParser.CAP_Q, 0); }
		public TerminalNode CAP_R() { return getToken(ECLParser.CAP_R, 0); }
		public TerminalNode CAP_S() { return getToken(ECLParser.CAP_S, 0); }
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode CAP_U() { return getToken(ECLParser.CAP_U, 0); }
		public TerminalNode CAP_V() { return getToken(ECLParser.CAP_V, 0); }
		public TerminalNode CAP_W() { return getToken(ECLParser.CAP_W, 0); }
		public TerminalNode CAP_X() { return getToken(ECLParser.CAP_X, 0); }
		public TerminalNode CAP_Y() { return getToken(ECLParser.CAP_Y, 0); }
		public TerminalNode CAP_Z() { return getToken(ECLParser.CAP_Z, 0); }
		public TerminalNode LEFT_BRACE() { return getToken(ECLParser.LEFT_BRACE, 0); }
		public TerminalNode BACKSLASH() { return getToken(ECLParser.BACKSLASH, 0); }
		public TerminalNode RIGHT_BRACE() { return getToken(ECLParser.RIGHT_BRACE, 0); }
		public TerminalNode CARAT() { return getToken(ECLParser.CARAT, 0); }
		public TerminalNode UNDERSCORE() { return getToken(ECLParser.UNDERSCORE, 0); }
		public TerminalNode ACCENT() { return getToken(ECLParser.ACCENT, 0); }
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode B() { return getToken(ECLParser.B, 0); }
		public TerminalNode C() { return getToken(ECLParser.C, 0); }
		public TerminalNode D() { return getToken(ECLParser.D, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public TerminalNode F() { return getToken(ECLParser.F, 0); }
		public TerminalNode G() { return getToken(ECLParser.G, 0); }
		public TerminalNode H() { return getToken(ECLParser.H, 0); }
		public TerminalNode I() { return getToken(ECLParser.I, 0); }
		public TerminalNode J() { return getToken(ECLParser.J, 0); }
		public TerminalNode K() { return getToken(ECLParser.K, 0); }
		public TerminalNode L() { return getToken(ECLParser.L, 0); }
		public TerminalNode M() { return getToken(ECLParser.M, 0); }
		public TerminalNode N() { return getToken(ECLParser.N, 0); }
		public TerminalNode O() { return getToken(ECLParser.O, 0); }
		public TerminalNode P() { return getToken(ECLParser.P, 0); }
		public TerminalNode Q() { return getToken(ECLParser.Q, 0); }
		public TerminalNode R() { return getToken(ECLParser.R, 0); }
		public TerminalNode S() { return getToken(ECLParser.S, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode U() { return getToken(ECLParser.U, 0); }
		public TerminalNode V() { return getToken(ECLParser.V, 0); }
		public TerminalNode W() { return getToken(ECLParser.W, 0); }
		public TerminalNode X() { return getToken(ECLParser.X, 0); }
		public TerminalNode Y() { return getToken(ECLParser.Y, 0); }
		public TerminalNode Z() { return getToken(ECLParser.Z, 0); }
		public TerminalNode LEFT_CURLY_BRACE() { return getToken(ECLParser.LEFT_CURLY_BRACE, 0); }
		public TerminalNode PIPE() { return getToken(ECLParser.PIPE, 0); }
		public TerminalNode RIGHT_CURLY_BRACE() { return getToken(ECLParser.RIGHT_CURLY_BRACE, 0); }
		public TerminalNode TILDE() { return getToken(ECLParser.TILDE, 0); }
		public TerminalNode UTF8_LETTER() { return getToken(ECLParser.UTF8_LETTER, 0); }
		public NonstarcharContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonstarchar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterNonstarchar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitNonstarchar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitNonstarchar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonstarcharContext nonstarchar() throws RecognitionException {
		NonstarcharContext _localctx = new NonstarcharContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_nonstarchar);
		int _la;
		try {
			setState(727);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SPACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(720);
				sp();
				}
				break;
			case TAB:
				enterOuterAlt(_localctx, 2);
				{
				setState(721);
				htab();
				}
				break;
			case CR:
				enterOuterAlt(_localctx, 3);
				{
				setState(722);
				cr();
				}
				break;
			case LF:
				enterOuterAlt(_localctx, 4);
				{
				setState(723);
				lf();
				}
				break;
			case EXCLAMATION:
			case QUOTE:
			case POUND:
			case DOLLAR:
			case PERCENT:
			case AMPERSAND:
			case APOSTROPHE:
			case LEFT_PAREN:
			case RIGHT_PAREN:
				enterOuterAlt(_localctx, 5);
				{
				setState(724);
				_la = _input.LA(1);
				if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 32704L) != 0) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case PLUS:
			case COMMA:
			case DASH:
			case PERIOD:
			case SLASH:
			case ZERO:
			case ONE:
			case TWO:
			case THREE:
			case FOUR:
			case FIVE:
			case SIX:
			case SEVEN:
			case EIGHT:
			case NINE:
			case COLON:
			case SEMICOLON:
			case LESS_THAN:
			case EQUALS:
			case GREATER_THAN:
			case QUESTION:
			case AT:
			case CAP_A:
			case CAP_B:
			case CAP_C:
			case CAP_D:
			case CAP_E:
			case CAP_F:
			case CAP_G:
			case CAP_H:
			case CAP_I:
			case CAP_J:
			case CAP_K:
			case CAP_L:
			case CAP_M:
			case CAP_N:
			case CAP_O:
			case CAP_P:
			case CAP_Q:
			case CAP_R:
			case CAP_S:
			case CAP_T:
			case CAP_U:
			case CAP_V:
			case CAP_W:
			case CAP_X:
			case CAP_Y:
			case CAP_Z:
			case LEFT_BRACE:
			case BACKSLASH:
			case RIGHT_BRACE:
			case CARAT:
			case UNDERSCORE:
			case ACCENT:
			case A:
			case B:
			case C:
			case D:
			case E:
			case F:
			case G:
			case H:
			case I:
			case J:
			case K:
			case L:
			case M:
			case N:
			case O:
			case P:
			case Q:
			case R:
			case S:
			case T:
			case U:
			case V:
			case W:
			case X:
			case Y:
			case Z:
			case LEFT_CURLY_BRACE:
			case PIPE:
			case RIGHT_CURLY_BRACE:
			case TILDE:
				enterOuterAlt(_localctx, 6);
				{
				setState(725);
				_la = _input.LA(1);
				if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & -65536L) != 0 || (((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 68719476735L) != 0) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case UTF8_LETTER:
				enterOuterAlt(_localctx, 7);
				{
				setState(726);
				match(UTF8_LETTER);
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
	public static class NonspacecharContext extends ParserRuleContext {
		public TerminalNode POUND() { return getToken(ECLParser.POUND, 0); }
		public TerminalNode DOLLAR() { return getToken(ECLParser.DOLLAR, 0); }
		public TerminalNode PERCENT() { return getToken(ECLParser.PERCENT, 0); }
		public TerminalNode AMPERSAND() { return getToken(ECLParser.AMPERSAND, 0); }
		public TerminalNode APOSTROPHE() { return getToken(ECLParser.APOSTROPHE, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public TerminalNode PLUS() { return getToken(ECLParser.PLUS, 0); }
		public TerminalNode COMMA() { return getToken(ECLParser.COMMA, 0); }
		public TerminalNode DASH() { return getToken(ECLParser.DASH, 0); }
		public TerminalNode PERIOD() { return getToken(ECLParser.PERIOD, 0); }
		public TerminalNode SLASH() { return getToken(ECLParser.SLASH, 0); }
		public TerminalNode ZERO() { return getToken(ECLParser.ZERO, 0); }
		public TerminalNode ONE() { return getToken(ECLParser.ONE, 0); }
		public TerminalNode TWO() { return getToken(ECLParser.TWO, 0); }
		public TerminalNode THREE() { return getToken(ECLParser.THREE, 0); }
		public TerminalNode FOUR() { return getToken(ECLParser.FOUR, 0); }
		public TerminalNode FIVE() { return getToken(ECLParser.FIVE, 0); }
		public TerminalNode SIX() { return getToken(ECLParser.SIX, 0); }
		public TerminalNode SEVEN() { return getToken(ECLParser.SEVEN, 0); }
		public TerminalNode EIGHT() { return getToken(ECLParser.EIGHT, 0); }
		public TerminalNode NINE() { return getToken(ECLParser.NINE, 0); }
		public TerminalNode COLON() { return getToken(ECLParser.COLON, 0); }
		public TerminalNode SEMICOLON() { return getToken(ECLParser.SEMICOLON, 0); }
		public TerminalNode LESS_THAN() { return getToken(ECLParser.LESS_THAN, 0); }
		public TerminalNode EQUALS() { return getToken(ECLParser.EQUALS, 0); }
		public TerminalNode GREATER_THAN() { return getToken(ECLParser.GREATER_THAN, 0); }
		public TerminalNode QUESTION() { return getToken(ECLParser.QUESTION, 0); }
		public TerminalNode AT() { return getToken(ECLParser.AT, 0); }
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode CAP_B() { return getToken(ECLParser.CAP_B, 0); }
		public TerminalNode CAP_C() { return getToken(ECLParser.CAP_C, 0); }
		public TerminalNode CAP_D() { return getToken(ECLParser.CAP_D, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode CAP_F() { return getToken(ECLParser.CAP_F, 0); }
		public TerminalNode CAP_G() { return getToken(ECLParser.CAP_G, 0); }
		public TerminalNode CAP_H() { return getToken(ECLParser.CAP_H, 0); }
		public TerminalNode CAP_I() { return getToken(ECLParser.CAP_I, 0); }
		public TerminalNode CAP_J() { return getToken(ECLParser.CAP_J, 0); }
		public TerminalNode CAP_K() { return getToken(ECLParser.CAP_K, 0); }
		public TerminalNode CAP_L() { return getToken(ECLParser.CAP_L, 0); }
		public TerminalNode CAP_M() { return getToken(ECLParser.CAP_M, 0); }
		public TerminalNode CAP_N() { return getToken(ECLParser.CAP_N, 0); }
		public TerminalNode CAP_O() { return getToken(ECLParser.CAP_O, 0); }
		public TerminalNode CAP_P() { return getToken(ECLParser.CAP_P, 0); }
		public TerminalNode CAP_Q() { return getToken(ECLParser.CAP_Q, 0); }
		public TerminalNode CAP_R() { return getToken(ECLParser.CAP_R, 0); }
		public TerminalNode CAP_S() { return getToken(ECLParser.CAP_S, 0); }
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode CAP_U() { return getToken(ECLParser.CAP_U, 0); }
		public TerminalNode CAP_V() { return getToken(ECLParser.CAP_V, 0); }
		public TerminalNode CAP_W() { return getToken(ECLParser.CAP_W, 0); }
		public TerminalNode CAP_X() { return getToken(ECLParser.CAP_X, 0); }
		public TerminalNode CAP_Y() { return getToken(ECLParser.CAP_Y, 0); }
		public TerminalNode CAP_Z() { return getToken(ECLParser.CAP_Z, 0); }
		public TerminalNode LEFT_BRACE() { return getToken(ECLParser.LEFT_BRACE, 0); }
		public TerminalNode BACKSLASH() { return getToken(ECLParser.BACKSLASH, 0); }
		public TerminalNode RIGHT_BRACE() { return getToken(ECLParser.RIGHT_BRACE, 0); }
		public TerminalNode CARAT() { return getToken(ECLParser.CARAT, 0); }
		public TerminalNode UNDERSCORE() { return getToken(ECLParser.UNDERSCORE, 0); }
		public TerminalNode ACCENT() { return getToken(ECLParser.ACCENT, 0); }
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode B() { return getToken(ECLParser.B, 0); }
		public TerminalNode C() { return getToken(ECLParser.C, 0); }
		public TerminalNode D() { return getToken(ECLParser.D, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public TerminalNode F() { return getToken(ECLParser.F, 0); }
		public TerminalNode G() { return getToken(ECLParser.G, 0); }
		public TerminalNode H() { return getToken(ECLParser.H, 0); }
		public TerminalNode I() { return getToken(ECLParser.I, 0); }
		public TerminalNode J() { return getToken(ECLParser.J, 0); }
		public TerminalNode K() { return getToken(ECLParser.K, 0); }
		public TerminalNode L() { return getToken(ECLParser.L, 0); }
		public TerminalNode M() { return getToken(ECLParser.M, 0); }
		public TerminalNode N() { return getToken(ECLParser.N, 0); }
		public TerminalNode O() { return getToken(ECLParser.O, 0); }
		public TerminalNode P() { return getToken(ECLParser.P, 0); }
		public TerminalNode Q() { return getToken(ECLParser.Q, 0); }
		public TerminalNode R() { return getToken(ECLParser.R, 0); }
		public TerminalNode S() { return getToken(ECLParser.S, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode U() { return getToken(ECLParser.U, 0); }
		public TerminalNode V() { return getToken(ECLParser.V, 0); }
		public TerminalNode W() { return getToken(ECLParser.W, 0); }
		public TerminalNode X() { return getToken(ECLParser.X, 0); }
		public TerminalNode Y() { return getToken(ECLParser.Y, 0); }
		public TerminalNode Z() { return getToken(ECLParser.Z, 0); }
		public TerminalNode LEFT_CURLY_BRACE() { return getToken(ECLParser.LEFT_CURLY_BRACE, 0); }
		public TerminalNode PIPE() { return getToken(ECLParser.PIPE, 0); }
		public TerminalNode RIGHT_CURLY_BRACE() { return getToken(ECLParser.RIGHT_CURLY_BRACE, 0); }
		public TerminalNode TILDE() { return getToken(ECLParser.TILDE, 0); }
		public TerminalNode UTF8_LETTER() { return getToken(ECLParser.UTF8_LETTER, 0); }
		public NonspacecharContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonspacechar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterNonspacechar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitNonspacechar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitNonspacechar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonspacecharContext nonspacechar() throws RecognitionException {
		NonspacecharContext _localctx = new NonspacecharContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_nonspacechar);
		int _la;
		try {
			setState(732);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case POUND:
			case DOLLAR:
			case PERCENT:
			case AMPERSAND:
			case APOSTROPHE:
			case LEFT_PAREN:
			case RIGHT_PAREN:
				enterOuterAlt(_localctx, 1);
				{
				setState(729);
				_la = _input.LA(1);
				if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 32512L) != 0) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case PLUS:
			case COMMA:
			case DASH:
			case PERIOD:
			case SLASH:
			case ZERO:
			case ONE:
			case TWO:
			case THREE:
			case FOUR:
			case FIVE:
			case SIX:
			case SEVEN:
			case EIGHT:
			case NINE:
			case COLON:
			case SEMICOLON:
			case LESS_THAN:
			case EQUALS:
			case GREATER_THAN:
			case QUESTION:
			case AT:
			case CAP_A:
			case CAP_B:
			case CAP_C:
			case CAP_D:
			case CAP_E:
			case CAP_F:
			case CAP_G:
			case CAP_H:
			case CAP_I:
			case CAP_J:
			case CAP_K:
			case CAP_L:
			case CAP_M:
			case CAP_N:
			case CAP_O:
			case CAP_P:
			case CAP_Q:
			case CAP_R:
			case CAP_S:
			case CAP_T:
			case CAP_U:
			case CAP_V:
			case CAP_W:
			case CAP_X:
			case CAP_Y:
			case CAP_Z:
			case LEFT_BRACE:
			case BACKSLASH:
			case RIGHT_BRACE:
			case CARAT:
			case UNDERSCORE:
			case ACCENT:
			case A:
			case B:
			case C:
			case D:
			case E:
			case F:
			case G:
			case H:
			case I:
			case J:
			case K:
			case L:
			case M:
			case N:
			case O:
			case P:
			case Q:
			case R:
			case S:
			case T:
			case U:
			case V:
			case W:
			case X:
			case Y:
			case Z:
			case LEFT_CURLY_BRACE:
			case PIPE:
			case RIGHT_CURLY_BRACE:
			case TILDE:
				enterOuterAlt(_localctx, 2);
				{
				setState(730);
				_la = _input.LA(1);
				if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & -65536L) != 0 || (((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 68719476735L) != 0) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case UTF8_LETTER:
				enterOuterAlt(_localctx, 3);
				{
				setState(731);
				match(UTF8_LETTER);
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
	public static class StarwithnonfslashContext extends ParserRuleContext {
		public TerminalNode ASTERISK() { return getToken(ECLParser.ASTERISK, 0); }
		public NonfslashContext nonfslash() {
			return getRuleContext(NonfslashContext.class,0);
		}
		public StarwithnonfslashContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_starwithnonfslash; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterStarwithnonfslash(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitStarwithnonfslash(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitStarwithnonfslash(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StarwithnonfslashContext starwithnonfslash() throws RecognitionException {
		StarwithnonfslashContext _localctx = new StarwithnonfslashContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_starwithnonfslash);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(734);
			match(ASTERISK);
			setState(735);
			nonfslash();
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
	public static class NonfslashContext extends ParserRuleContext {
		public SpContext sp() {
			return getRuleContext(SpContext.class,0);
		}
		public HtabContext htab() {
			return getRuleContext(HtabContext.class,0);
		}
		public CrContext cr() {
			return getRuleContext(CrContext.class,0);
		}
		public LfContext lf() {
			return getRuleContext(LfContext.class,0);
		}
		public TerminalNode EXCLAMATION() { return getToken(ECLParser.EXCLAMATION, 0); }
		public TerminalNode QUOTE() { return getToken(ECLParser.QUOTE, 0); }
		public TerminalNode POUND() { return getToken(ECLParser.POUND, 0); }
		public TerminalNode DOLLAR() { return getToken(ECLParser.DOLLAR, 0); }
		public TerminalNode PERCENT() { return getToken(ECLParser.PERCENT, 0); }
		public TerminalNode AMPERSAND() { return getToken(ECLParser.AMPERSAND, 0); }
		public TerminalNode APOSTROPHE() { return getToken(ECLParser.APOSTROPHE, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public TerminalNode ASTERISK() { return getToken(ECLParser.ASTERISK, 0); }
		public TerminalNode PLUS() { return getToken(ECLParser.PLUS, 0); }
		public TerminalNode COMMA() { return getToken(ECLParser.COMMA, 0); }
		public TerminalNode DASH() { return getToken(ECLParser.DASH, 0); }
		public TerminalNode PERIOD() { return getToken(ECLParser.PERIOD, 0); }
		public TerminalNode ZERO() { return getToken(ECLParser.ZERO, 0); }
		public TerminalNode ONE() { return getToken(ECLParser.ONE, 0); }
		public TerminalNode TWO() { return getToken(ECLParser.TWO, 0); }
		public TerminalNode THREE() { return getToken(ECLParser.THREE, 0); }
		public TerminalNode FOUR() { return getToken(ECLParser.FOUR, 0); }
		public TerminalNode FIVE() { return getToken(ECLParser.FIVE, 0); }
		public TerminalNode SIX() { return getToken(ECLParser.SIX, 0); }
		public TerminalNode SEVEN() { return getToken(ECLParser.SEVEN, 0); }
		public TerminalNode EIGHT() { return getToken(ECLParser.EIGHT, 0); }
		public TerminalNode NINE() { return getToken(ECLParser.NINE, 0); }
		public TerminalNode COLON() { return getToken(ECLParser.COLON, 0); }
		public TerminalNode SEMICOLON() { return getToken(ECLParser.SEMICOLON, 0); }
		public TerminalNode LESS_THAN() { return getToken(ECLParser.LESS_THAN, 0); }
		public TerminalNode EQUALS() { return getToken(ECLParser.EQUALS, 0); }
		public TerminalNode GREATER_THAN() { return getToken(ECLParser.GREATER_THAN, 0); }
		public TerminalNode QUESTION() { return getToken(ECLParser.QUESTION, 0); }
		public TerminalNode AT() { return getToken(ECLParser.AT, 0); }
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode CAP_B() { return getToken(ECLParser.CAP_B, 0); }
		public TerminalNode CAP_C() { return getToken(ECLParser.CAP_C, 0); }
		public TerminalNode CAP_D() { return getToken(ECLParser.CAP_D, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode CAP_F() { return getToken(ECLParser.CAP_F, 0); }
		public TerminalNode CAP_G() { return getToken(ECLParser.CAP_G, 0); }
		public TerminalNode CAP_H() { return getToken(ECLParser.CAP_H, 0); }
		public TerminalNode CAP_I() { return getToken(ECLParser.CAP_I, 0); }
		public TerminalNode CAP_J() { return getToken(ECLParser.CAP_J, 0); }
		public TerminalNode CAP_K() { return getToken(ECLParser.CAP_K, 0); }
		public TerminalNode CAP_L() { return getToken(ECLParser.CAP_L, 0); }
		public TerminalNode CAP_M() { return getToken(ECLParser.CAP_M, 0); }
		public TerminalNode CAP_N() { return getToken(ECLParser.CAP_N, 0); }
		public TerminalNode CAP_O() { return getToken(ECLParser.CAP_O, 0); }
		public TerminalNode CAP_P() { return getToken(ECLParser.CAP_P, 0); }
		public TerminalNode CAP_Q() { return getToken(ECLParser.CAP_Q, 0); }
		public TerminalNode CAP_R() { return getToken(ECLParser.CAP_R, 0); }
		public TerminalNode CAP_S() { return getToken(ECLParser.CAP_S, 0); }
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode CAP_U() { return getToken(ECLParser.CAP_U, 0); }
		public TerminalNode CAP_V() { return getToken(ECLParser.CAP_V, 0); }
		public TerminalNode CAP_W() { return getToken(ECLParser.CAP_W, 0); }
		public TerminalNode CAP_X() { return getToken(ECLParser.CAP_X, 0); }
		public TerminalNode CAP_Y() { return getToken(ECLParser.CAP_Y, 0); }
		public TerminalNode CAP_Z() { return getToken(ECLParser.CAP_Z, 0); }
		public TerminalNode LEFT_BRACE() { return getToken(ECLParser.LEFT_BRACE, 0); }
		public TerminalNode BACKSLASH() { return getToken(ECLParser.BACKSLASH, 0); }
		public TerminalNode RIGHT_BRACE() { return getToken(ECLParser.RIGHT_BRACE, 0); }
		public TerminalNode CARAT() { return getToken(ECLParser.CARAT, 0); }
		public TerminalNode UNDERSCORE() { return getToken(ECLParser.UNDERSCORE, 0); }
		public TerminalNode ACCENT() { return getToken(ECLParser.ACCENT, 0); }
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode B() { return getToken(ECLParser.B, 0); }
		public TerminalNode C() { return getToken(ECLParser.C, 0); }
		public TerminalNode D() { return getToken(ECLParser.D, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public TerminalNode F() { return getToken(ECLParser.F, 0); }
		public TerminalNode G() { return getToken(ECLParser.G, 0); }
		public TerminalNode H() { return getToken(ECLParser.H, 0); }
		public TerminalNode I() { return getToken(ECLParser.I, 0); }
		public TerminalNode J() { return getToken(ECLParser.J, 0); }
		public TerminalNode K() { return getToken(ECLParser.K, 0); }
		public TerminalNode L() { return getToken(ECLParser.L, 0); }
		public TerminalNode M() { return getToken(ECLParser.M, 0); }
		public TerminalNode N() { return getToken(ECLParser.N, 0); }
		public TerminalNode O() { return getToken(ECLParser.O, 0); }
		public TerminalNode P() { return getToken(ECLParser.P, 0); }
		public TerminalNode Q() { return getToken(ECLParser.Q, 0); }
		public TerminalNode R() { return getToken(ECLParser.R, 0); }
		public TerminalNode S() { return getToken(ECLParser.S, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode U() { return getToken(ECLParser.U, 0); }
		public TerminalNode V() { return getToken(ECLParser.V, 0); }
		public TerminalNode W() { return getToken(ECLParser.W, 0); }
		public TerminalNode X() { return getToken(ECLParser.X, 0); }
		public TerminalNode Y() { return getToken(ECLParser.Y, 0); }
		public TerminalNode Z() { return getToken(ECLParser.Z, 0); }
		public TerminalNode LEFT_CURLY_BRACE() { return getToken(ECLParser.LEFT_CURLY_BRACE, 0); }
		public TerminalNode PIPE() { return getToken(ECLParser.PIPE, 0); }
		public TerminalNode RIGHT_CURLY_BRACE() { return getToken(ECLParser.RIGHT_CURLY_BRACE, 0); }
		public TerminalNode TILDE() { return getToken(ECLParser.TILDE, 0); }
		public TerminalNode UTF8_LETTER() { return getToken(ECLParser.UTF8_LETTER, 0); }
		public NonfslashContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonfslash; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterNonfslash(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitNonfslash(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitNonfslash(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonfslashContext nonfslash() throws RecognitionException {
		NonfslashContext _localctx = new NonfslashContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_nonfslash);
		int _la;
		try {
			setState(744);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SPACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(737);
				sp();
				}
				break;
			case TAB:
				enterOuterAlt(_localctx, 2);
				{
				setState(738);
				htab();
				}
				break;
			case CR:
				enterOuterAlt(_localctx, 3);
				{
				setState(739);
				cr();
				}
				break;
			case LF:
				enterOuterAlt(_localctx, 4);
				{
				setState(740);
				lf();
				}
				break;
			case EXCLAMATION:
			case QUOTE:
			case POUND:
			case DOLLAR:
			case PERCENT:
			case AMPERSAND:
			case APOSTROPHE:
			case LEFT_PAREN:
			case RIGHT_PAREN:
			case ASTERISK:
			case PLUS:
			case COMMA:
			case DASH:
			case PERIOD:
				enterOuterAlt(_localctx, 5);
				{
				setState(741);
				_la = _input.LA(1);
				if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 1048512L) != 0) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case ZERO:
			case ONE:
			case TWO:
			case THREE:
			case FOUR:
			case FIVE:
			case SIX:
			case SEVEN:
			case EIGHT:
			case NINE:
			case COLON:
			case SEMICOLON:
			case LESS_THAN:
			case EQUALS:
			case GREATER_THAN:
			case QUESTION:
			case AT:
			case CAP_A:
			case CAP_B:
			case CAP_C:
			case CAP_D:
			case CAP_E:
			case CAP_F:
			case CAP_G:
			case CAP_H:
			case CAP_I:
			case CAP_J:
			case CAP_K:
			case CAP_L:
			case CAP_M:
			case CAP_N:
			case CAP_O:
			case CAP_P:
			case CAP_Q:
			case CAP_R:
			case CAP_S:
			case CAP_T:
			case CAP_U:
			case CAP_V:
			case CAP_W:
			case CAP_X:
			case CAP_Y:
			case CAP_Z:
			case LEFT_BRACE:
			case BACKSLASH:
			case RIGHT_BRACE:
			case CARAT:
			case UNDERSCORE:
			case ACCENT:
			case A:
			case B:
			case C:
			case D:
			case E:
			case F:
			case G:
			case H:
			case I:
			case J:
			case K:
			case L:
			case M:
			case N:
			case O:
			case P:
			case Q:
			case R:
			case S:
			case T:
			case U:
			case V:
			case W:
			case X:
			case Y:
			case Z:
			case LEFT_CURLY_BRACE:
			case PIPE:
			case RIGHT_CURLY_BRACE:
			case TILDE:
				enterOuterAlt(_localctx, 6);
				{
				setState(742);
				_la = _input.LA(1);
				if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & -2097152L) != 0 || (((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 68719476735L) != 0) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case UTF8_LETTER:
				enterOuterAlt(_localctx, 7);
				{
				setState(743);
				match(UTF8_LETTER);
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
	public static class SpContext extends ParserRuleContext {
		public TerminalNode SPACE() { return getToken(ECLParser.SPACE, 0); }
		public SpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterSp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitSp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitSp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpContext sp() throws RecognitionException {
		SpContext _localctx = new SpContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_sp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(746);
			match(SPACE);
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
	public static class HtabContext extends ParserRuleContext {
		public TerminalNode TAB() { return getToken(ECLParser.TAB, 0); }
		public HtabContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htab; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterHtab(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitHtab(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitHtab(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HtabContext htab() throws RecognitionException {
		HtabContext _localctx = new HtabContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_htab);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(748);
			match(TAB);
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
	public static class CrContext extends ParserRuleContext {
		public TerminalNode CR() { return getToken(ECLParser.CR, 0); }
		public CrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterCr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitCr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitCr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CrContext cr() throws RecognitionException {
		CrContext _localctx = new CrContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_cr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(750);
			match(CR);
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
	public static class LfContext extends ParserRuleContext {
		public TerminalNode LF() { return getToken(ECLParser.LF, 0); }
		public LfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterLf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitLf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitLf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LfContext lf() throws RecognitionException {
		LfContext _localctx = new LfContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_lf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(752);
			match(LF);
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
	public static class QmContext extends ParserRuleContext {
		public TerminalNode QUOTE() { return getToken(ECLParser.QUOTE, 0); }
		public QmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterQm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitQm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitQm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QmContext qm() throws RecognitionException {
		QmContext _localctx = new QmContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_qm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(754);
			match(QUOTE);
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
	public static class BsContext extends ParserRuleContext {
		public TerminalNode BACKSLASH() { return getToken(ECLParser.BACKSLASH, 0); }
		public BsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterBs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitBs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitBs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BsContext bs() throws RecognitionException {
		BsContext _localctx = new BsContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_bs);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(756);
			match(BACKSLASH);
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
	public static class DigitContext extends ParserRuleContext {
		public TerminalNode ZERO() { return getToken(ECLParser.ZERO, 0); }
		public TerminalNode ONE() { return getToken(ECLParser.ONE, 0); }
		public TerminalNode TWO() { return getToken(ECLParser.TWO, 0); }
		public TerminalNode THREE() { return getToken(ECLParser.THREE, 0); }
		public TerminalNode FOUR() { return getToken(ECLParser.FOUR, 0); }
		public TerminalNode FIVE() { return getToken(ECLParser.FIVE, 0); }
		public TerminalNode SIX() { return getToken(ECLParser.SIX, 0); }
		public TerminalNode SEVEN() { return getToken(ECLParser.SEVEN, 0); }
		public TerminalNode EIGHT() { return getToken(ECLParser.EIGHT, 0); }
		public TerminalNode NINE() { return getToken(ECLParser.NINE, 0); }
		public DigitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_digit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDigit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDigit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDigit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DigitContext digit() throws RecognitionException {
		DigitContext _localctx = new DigitContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_digit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(758);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 2145386496L) != 0) ) {
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
	public static class ZeroContext extends ParserRuleContext {
		public TerminalNode ZERO() { return getToken(ECLParser.ZERO, 0); }
		public ZeroContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_zero; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterZero(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitZero(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitZero(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ZeroContext zero() throws RecognitionException {
		ZeroContext _localctx = new ZeroContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_zero);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(760);
			match(ZERO);
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
	public static class DigitnonzeroContext extends ParserRuleContext {
		public TerminalNode ONE() { return getToken(ECLParser.ONE, 0); }
		public TerminalNode TWO() { return getToken(ECLParser.TWO, 0); }
		public TerminalNode THREE() { return getToken(ECLParser.THREE, 0); }
		public TerminalNode FOUR() { return getToken(ECLParser.FOUR, 0); }
		public TerminalNode FIVE() { return getToken(ECLParser.FIVE, 0); }
		public TerminalNode SIX() { return getToken(ECLParser.SIX, 0); }
		public TerminalNode SEVEN() { return getToken(ECLParser.SEVEN, 0); }
		public TerminalNode EIGHT() { return getToken(ECLParser.EIGHT, 0); }
		public TerminalNode NINE() { return getToken(ECLParser.NINE, 0); }
		public DigitnonzeroContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_digitnonzero; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDigitnonzero(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDigitnonzero(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDigitnonzero(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DigitnonzeroContext digitnonzero() throws RecognitionException {
		DigitnonzeroContext _localctx = new DigitnonzeroContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_digitnonzero);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(762);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 2143289344L) != 0) ) {
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
	public static class NonwsnonpipeContext extends ParserRuleContext {
		public TerminalNode EXCLAMATION() { return getToken(ECLParser.EXCLAMATION, 0); }
		public TerminalNode QUOTE() { return getToken(ECLParser.QUOTE, 0); }
		public TerminalNode POUND() { return getToken(ECLParser.POUND, 0); }
		public TerminalNode DOLLAR() { return getToken(ECLParser.DOLLAR, 0); }
		public TerminalNode PERCENT() { return getToken(ECLParser.PERCENT, 0); }
		public TerminalNode AMPERSAND() { return getToken(ECLParser.AMPERSAND, 0); }
		public TerminalNode APOSTROPHE() { return getToken(ECLParser.APOSTROPHE, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public TerminalNode ASTERISK() { return getToken(ECLParser.ASTERISK, 0); }
		public TerminalNode PLUS() { return getToken(ECLParser.PLUS, 0); }
		public TerminalNode COMMA() { return getToken(ECLParser.COMMA, 0); }
		public TerminalNode DASH() { return getToken(ECLParser.DASH, 0); }
		public TerminalNode PERIOD() { return getToken(ECLParser.PERIOD, 0); }
		public TerminalNode SLASH() { return getToken(ECLParser.SLASH, 0); }
		public TerminalNode ZERO() { return getToken(ECLParser.ZERO, 0); }
		public TerminalNode ONE() { return getToken(ECLParser.ONE, 0); }
		public TerminalNode TWO() { return getToken(ECLParser.TWO, 0); }
		public TerminalNode THREE() { return getToken(ECLParser.THREE, 0); }
		public TerminalNode FOUR() { return getToken(ECLParser.FOUR, 0); }
		public TerminalNode FIVE() { return getToken(ECLParser.FIVE, 0); }
		public TerminalNode SIX() { return getToken(ECLParser.SIX, 0); }
		public TerminalNode SEVEN() { return getToken(ECLParser.SEVEN, 0); }
		public TerminalNode EIGHT() { return getToken(ECLParser.EIGHT, 0); }
		public TerminalNode NINE() { return getToken(ECLParser.NINE, 0); }
		public TerminalNode COLON() { return getToken(ECLParser.COLON, 0); }
		public TerminalNode SEMICOLON() { return getToken(ECLParser.SEMICOLON, 0); }
		public TerminalNode LESS_THAN() { return getToken(ECLParser.LESS_THAN, 0); }
		public TerminalNode EQUALS() { return getToken(ECLParser.EQUALS, 0); }
		public TerminalNode GREATER_THAN() { return getToken(ECLParser.GREATER_THAN, 0); }
		public TerminalNode QUESTION() { return getToken(ECLParser.QUESTION, 0); }
		public TerminalNode AT() { return getToken(ECLParser.AT, 0); }
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode CAP_B() { return getToken(ECLParser.CAP_B, 0); }
		public TerminalNode CAP_C() { return getToken(ECLParser.CAP_C, 0); }
		public TerminalNode CAP_D() { return getToken(ECLParser.CAP_D, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode CAP_F() { return getToken(ECLParser.CAP_F, 0); }
		public TerminalNode CAP_G() { return getToken(ECLParser.CAP_G, 0); }
		public TerminalNode CAP_H() { return getToken(ECLParser.CAP_H, 0); }
		public TerminalNode CAP_I() { return getToken(ECLParser.CAP_I, 0); }
		public TerminalNode CAP_J() { return getToken(ECLParser.CAP_J, 0); }
		public TerminalNode CAP_K() { return getToken(ECLParser.CAP_K, 0); }
		public TerminalNode CAP_L() { return getToken(ECLParser.CAP_L, 0); }
		public TerminalNode CAP_M() { return getToken(ECLParser.CAP_M, 0); }
		public TerminalNode CAP_N() { return getToken(ECLParser.CAP_N, 0); }
		public TerminalNode CAP_O() { return getToken(ECLParser.CAP_O, 0); }
		public TerminalNode CAP_P() { return getToken(ECLParser.CAP_P, 0); }
		public TerminalNode CAP_Q() { return getToken(ECLParser.CAP_Q, 0); }
		public TerminalNode CAP_R() { return getToken(ECLParser.CAP_R, 0); }
		public TerminalNode CAP_S() { return getToken(ECLParser.CAP_S, 0); }
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode CAP_U() { return getToken(ECLParser.CAP_U, 0); }
		public TerminalNode CAP_V() { return getToken(ECLParser.CAP_V, 0); }
		public TerminalNode CAP_W() { return getToken(ECLParser.CAP_W, 0); }
		public TerminalNode CAP_X() { return getToken(ECLParser.CAP_X, 0); }
		public TerminalNode CAP_Y() { return getToken(ECLParser.CAP_Y, 0); }
		public TerminalNode CAP_Z() { return getToken(ECLParser.CAP_Z, 0); }
		public TerminalNode LEFT_BRACE() { return getToken(ECLParser.LEFT_BRACE, 0); }
		public TerminalNode BACKSLASH() { return getToken(ECLParser.BACKSLASH, 0); }
		public TerminalNode RIGHT_BRACE() { return getToken(ECLParser.RIGHT_BRACE, 0); }
		public TerminalNode CARAT() { return getToken(ECLParser.CARAT, 0); }
		public TerminalNode UNDERSCORE() { return getToken(ECLParser.UNDERSCORE, 0); }
		public TerminalNode ACCENT() { return getToken(ECLParser.ACCENT, 0); }
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode B() { return getToken(ECLParser.B, 0); }
		public TerminalNode C() { return getToken(ECLParser.C, 0); }
		public TerminalNode D() { return getToken(ECLParser.D, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public TerminalNode F() { return getToken(ECLParser.F, 0); }
		public TerminalNode G() { return getToken(ECLParser.G, 0); }
		public TerminalNode H() { return getToken(ECLParser.H, 0); }
		public TerminalNode I() { return getToken(ECLParser.I, 0); }
		public TerminalNode J() { return getToken(ECLParser.J, 0); }
		public TerminalNode K() { return getToken(ECLParser.K, 0); }
		public TerminalNode L() { return getToken(ECLParser.L, 0); }
		public TerminalNode M() { return getToken(ECLParser.M, 0); }
		public TerminalNode N() { return getToken(ECLParser.N, 0); }
		public TerminalNode O() { return getToken(ECLParser.O, 0); }
		public TerminalNode P() { return getToken(ECLParser.P, 0); }
		public TerminalNode Q() { return getToken(ECLParser.Q, 0); }
		public TerminalNode R() { return getToken(ECLParser.R, 0); }
		public TerminalNode S() { return getToken(ECLParser.S, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode U() { return getToken(ECLParser.U, 0); }
		public TerminalNode V() { return getToken(ECLParser.V, 0); }
		public TerminalNode W() { return getToken(ECLParser.W, 0); }
		public TerminalNode X() { return getToken(ECLParser.X, 0); }
		public TerminalNode Y() { return getToken(ECLParser.Y, 0); }
		public TerminalNode Z() { return getToken(ECLParser.Z, 0); }
		public TerminalNode LEFT_CURLY_BRACE() { return getToken(ECLParser.LEFT_CURLY_BRACE, 0); }
		public TerminalNode RIGHT_CURLY_BRACE() { return getToken(ECLParser.RIGHT_CURLY_BRACE, 0); }
		public TerminalNode TILDE() { return getToken(ECLParser.TILDE, 0); }
		public TerminalNode UTF8_LETTER() { return getToken(ECLParser.UTF8_LETTER, 0); }
		public NonwsnonpipeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonwsnonpipe; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterNonwsnonpipe(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitNonwsnonpipe(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitNonwsnonpipe(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonwsnonpipeContext nonwsnonpipe() throws RecognitionException {
		NonwsnonpipeContext _localctx = new NonwsnonpipeContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_nonwsnonpipe);
		int _la;
		try {
			setState(767);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EXCLAMATION:
			case QUOTE:
			case POUND:
			case DOLLAR:
			case PERCENT:
			case AMPERSAND:
			case APOSTROPHE:
			case LEFT_PAREN:
			case RIGHT_PAREN:
			case ASTERISK:
			case PLUS:
			case COMMA:
			case DASH:
			case PERIOD:
			case SLASH:
			case ZERO:
			case ONE:
			case TWO:
			case THREE:
			case FOUR:
			case FIVE:
			case SIX:
			case SEVEN:
			case EIGHT:
			case NINE:
			case COLON:
			case SEMICOLON:
			case LESS_THAN:
			case EQUALS:
			case GREATER_THAN:
			case QUESTION:
			case AT:
			case CAP_A:
			case CAP_B:
			case CAP_C:
			case CAP_D:
			case CAP_E:
			case CAP_F:
			case CAP_G:
			case CAP_H:
			case CAP_I:
			case CAP_J:
			case CAP_K:
			case CAP_L:
			case CAP_M:
			case CAP_N:
			case CAP_O:
			case CAP_P:
			case CAP_Q:
			case CAP_R:
			case CAP_S:
			case CAP_T:
			case CAP_U:
			case CAP_V:
			case CAP_W:
			case CAP_X:
			case CAP_Y:
			case CAP_Z:
			case LEFT_BRACE:
			case BACKSLASH:
			case RIGHT_BRACE:
			case CARAT:
			case UNDERSCORE:
			case ACCENT:
			case A:
			case B:
			case C:
			case D:
			case E:
			case F:
			case G:
			case H:
			case I:
			case J:
			case K:
			case L:
			case M:
			case N:
			case O:
			case P:
			case Q:
			case R:
			case S:
			case T:
			case U:
			case V:
			case W:
			case X:
			case Y:
			case Z:
			case LEFT_CURLY_BRACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(764);
				_la = _input.LA(1);
				if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & -64L) != 0 || (((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 8589934591L) != 0) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case RIGHT_CURLY_BRACE:
			case TILDE:
				enterOuterAlt(_localctx, 2);
				{
				setState(765);
				_la = _input.LA(1);
				if ( !(_la==RIGHT_CURLY_BRACE || _la==TILDE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case UTF8_LETTER:
				enterOuterAlt(_localctx, 3);
				{
				setState(766);
				match(UTF8_LETTER);
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
	public static class AnynonescapedcharContext extends ParserRuleContext {
		public SpContext sp() {
			return getRuleContext(SpContext.class,0);
		}
		public HtabContext htab() {
			return getRuleContext(HtabContext.class,0);
		}
		public CrContext cr() {
			return getRuleContext(CrContext.class,0);
		}
		public LfContext lf() {
			return getRuleContext(LfContext.class,0);
		}
		public TerminalNode SPACE() { return getToken(ECLParser.SPACE, 0); }
		public TerminalNode EXCLAMATION() { return getToken(ECLParser.EXCLAMATION, 0); }
		public TerminalNode POUND() { return getToken(ECLParser.POUND, 0); }
		public TerminalNode DOLLAR() { return getToken(ECLParser.DOLLAR, 0); }
		public TerminalNode PERCENT() { return getToken(ECLParser.PERCENT, 0); }
		public TerminalNode AMPERSAND() { return getToken(ECLParser.AMPERSAND, 0); }
		public TerminalNode APOSTROPHE() { return getToken(ECLParser.APOSTROPHE, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public TerminalNode ASTERISK() { return getToken(ECLParser.ASTERISK, 0); }
		public TerminalNode PLUS() { return getToken(ECLParser.PLUS, 0); }
		public TerminalNode COMMA() { return getToken(ECLParser.COMMA, 0); }
		public TerminalNode DASH() { return getToken(ECLParser.DASH, 0); }
		public TerminalNode PERIOD() { return getToken(ECLParser.PERIOD, 0); }
		public TerminalNode SLASH() { return getToken(ECLParser.SLASH, 0); }
		public TerminalNode ZERO() { return getToken(ECLParser.ZERO, 0); }
		public TerminalNode ONE() { return getToken(ECLParser.ONE, 0); }
		public TerminalNode TWO() { return getToken(ECLParser.TWO, 0); }
		public TerminalNode THREE() { return getToken(ECLParser.THREE, 0); }
		public TerminalNode FOUR() { return getToken(ECLParser.FOUR, 0); }
		public TerminalNode FIVE() { return getToken(ECLParser.FIVE, 0); }
		public TerminalNode SIX() { return getToken(ECLParser.SIX, 0); }
		public TerminalNode SEVEN() { return getToken(ECLParser.SEVEN, 0); }
		public TerminalNode EIGHT() { return getToken(ECLParser.EIGHT, 0); }
		public TerminalNode NINE() { return getToken(ECLParser.NINE, 0); }
		public TerminalNode COLON() { return getToken(ECLParser.COLON, 0); }
		public TerminalNode SEMICOLON() { return getToken(ECLParser.SEMICOLON, 0); }
		public TerminalNode LESS_THAN() { return getToken(ECLParser.LESS_THAN, 0); }
		public TerminalNode EQUALS() { return getToken(ECLParser.EQUALS, 0); }
		public TerminalNode GREATER_THAN() { return getToken(ECLParser.GREATER_THAN, 0); }
		public TerminalNode QUESTION() { return getToken(ECLParser.QUESTION, 0); }
		public TerminalNode AT() { return getToken(ECLParser.AT, 0); }
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode CAP_B() { return getToken(ECLParser.CAP_B, 0); }
		public TerminalNode CAP_C() { return getToken(ECLParser.CAP_C, 0); }
		public TerminalNode CAP_D() { return getToken(ECLParser.CAP_D, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode CAP_F() { return getToken(ECLParser.CAP_F, 0); }
		public TerminalNode CAP_G() { return getToken(ECLParser.CAP_G, 0); }
		public TerminalNode CAP_H() { return getToken(ECLParser.CAP_H, 0); }
		public TerminalNode CAP_I() { return getToken(ECLParser.CAP_I, 0); }
		public TerminalNode CAP_J() { return getToken(ECLParser.CAP_J, 0); }
		public TerminalNode CAP_K() { return getToken(ECLParser.CAP_K, 0); }
		public TerminalNode CAP_L() { return getToken(ECLParser.CAP_L, 0); }
		public TerminalNode CAP_M() { return getToken(ECLParser.CAP_M, 0); }
		public TerminalNode CAP_N() { return getToken(ECLParser.CAP_N, 0); }
		public TerminalNode CAP_O() { return getToken(ECLParser.CAP_O, 0); }
		public TerminalNode CAP_P() { return getToken(ECLParser.CAP_P, 0); }
		public TerminalNode CAP_Q() { return getToken(ECLParser.CAP_Q, 0); }
		public TerminalNode CAP_R() { return getToken(ECLParser.CAP_R, 0); }
		public TerminalNode CAP_S() { return getToken(ECLParser.CAP_S, 0); }
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode CAP_U() { return getToken(ECLParser.CAP_U, 0); }
		public TerminalNode CAP_V() { return getToken(ECLParser.CAP_V, 0); }
		public TerminalNode CAP_W() { return getToken(ECLParser.CAP_W, 0); }
		public TerminalNode CAP_X() { return getToken(ECLParser.CAP_X, 0); }
		public TerminalNode CAP_Y() { return getToken(ECLParser.CAP_Y, 0); }
		public TerminalNode CAP_Z() { return getToken(ECLParser.CAP_Z, 0); }
		public TerminalNode LEFT_BRACE() { return getToken(ECLParser.LEFT_BRACE, 0); }
		public TerminalNode RIGHT_BRACE() { return getToken(ECLParser.RIGHT_BRACE, 0); }
		public TerminalNode CARAT() { return getToken(ECLParser.CARAT, 0); }
		public TerminalNode UNDERSCORE() { return getToken(ECLParser.UNDERSCORE, 0); }
		public TerminalNode ACCENT() { return getToken(ECLParser.ACCENT, 0); }
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode B() { return getToken(ECLParser.B, 0); }
		public TerminalNode C() { return getToken(ECLParser.C, 0); }
		public TerminalNode D() { return getToken(ECLParser.D, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public TerminalNode F() { return getToken(ECLParser.F, 0); }
		public TerminalNode G() { return getToken(ECLParser.G, 0); }
		public TerminalNode H() { return getToken(ECLParser.H, 0); }
		public TerminalNode I() { return getToken(ECLParser.I, 0); }
		public TerminalNode J() { return getToken(ECLParser.J, 0); }
		public TerminalNode K() { return getToken(ECLParser.K, 0); }
		public TerminalNode L() { return getToken(ECLParser.L, 0); }
		public TerminalNode M() { return getToken(ECLParser.M, 0); }
		public TerminalNode N() { return getToken(ECLParser.N, 0); }
		public TerminalNode O() { return getToken(ECLParser.O, 0); }
		public TerminalNode P() { return getToken(ECLParser.P, 0); }
		public TerminalNode Q() { return getToken(ECLParser.Q, 0); }
		public TerminalNode R() { return getToken(ECLParser.R, 0); }
		public TerminalNode S() { return getToken(ECLParser.S, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode U() { return getToken(ECLParser.U, 0); }
		public TerminalNode V() { return getToken(ECLParser.V, 0); }
		public TerminalNode W() { return getToken(ECLParser.W, 0); }
		public TerminalNode X() { return getToken(ECLParser.X, 0); }
		public TerminalNode Y() { return getToken(ECLParser.Y, 0); }
		public TerminalNode Z() { return getToken(ECLParser.Z, 0); }
		public TerminalNode LEFT_CURLY_BRACE() { return getToken(ECLParser.LEFT_CURLY_BRACE, 0); }
		public TerminalNode PIPE() { return getToken(ECLParser.PIPE, 0); }
		public TerminalNode RIGHT_CURLY_BRACE() { return getToken(ECLParser.RIGHT_CURLY_BRACE, 0); }
		public TerminalNode TILDE() { return getToken(ECLParser.TILDE, 0); }
		public TerminalNode UTF8_LETTER() { return getToken(ECLParser.UTF8_LETTER, 0); }
		public AnynonescapedcharContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anynonescapedchar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterAnynonescapedchar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitAnynonescapedchar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitAnynonescapedchar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnynonescapedcharContext anynonescapedchar() throws RecognitionException {
		AnynonescapedcharContext _localctx = new AnynonescapedcharContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_anynonescapedchar);
		int _la;
		try {
			setState(777);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(769);
				sp();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(770);
				htab();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(771);
				cr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(772);
				lf();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(773);
				_la = _input.LA(1);
				if ( !(_la==SPACE || _la==EXCLAMATION) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(774);
				_la = _input.LA(1);
				if ( !((((_la - 8)) & ~0x3f) == 0 && ((1L << (_la - 8)) & 144115188075855871L) != 0) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(775);
				_la = _input.LA(1);
				if ( !((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & 17179869183L) != 0) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(776);
				match(UTF8_LETTER);
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
	public static class EscapedcharContext extends ParserRuleContext {
		public List<BsContext> bs() {
			return getRuleContexts(BsContext.class);
		}
		public BsContext bs(int i) {
			return getRuleContext(BsContext.class,i);
		}
		public QmContext qm() {
			return getRuleContext(QmContext.class,0);
		}
		public EscapedcharContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_escapedchar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterEscapedchar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitEscapedchar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitEscapedchar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EscapedcharContext escapedchar() throws RecognitionException {
		EscapedcharContext _localctx = new EscapedcharContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_escapedchar);
		try {
			setState(785);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(779);
				bs();
				setState(780);
				qm();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(782);
				bs();
				setState(783);
				bs();
				}
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

	public static final String _serializedATN =
		"\u0004\u0001c\u0314\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
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
		"#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007\'\u0002"+
		"(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007,\u0002"+
		"-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u00021\u00071\u0002"+
		"2\u00072\u00023\u00073\u00024\u00074\u00025\u00075\u00026\u00076\u0002"+
		"7\u00077\u00028\u00078\u00029\u00079\u0002:\u0007:\u0002;\u0007;\u0002"+
		"<\u0007<\u0002=\u0007=\u0002>\u0007>\u0002?\u0007?\u0002@\u0007@\u0002"+
		"A\u0007A\u0002B\u0007B\u0002C\u0007C\u0002D\u0007D\u0002E\u0007E\u0002"+
		"F\u0007F\u0002G\u0007G\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0003\u0001\u009b\b\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0003\u0003\u00a8\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0004\u0004\u00b0\b\u0004\u000b\u0004"+
		"\f\u0004\u00b1\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0004\u0005\u00ba\b\u0005\u000b\u0005\f\u0005\u00bb\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0004\u0007\u00c8\b\u0007\u000b"+
		"\u0007\f\u0007\u00c9\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001"+
		"\t\u0003\t\u00d3\b\t\u0001\t\u0001\t\u0001\t\u0003\t\u00d8\b\t\u0001\t"+
		"\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u00e1\b\t\u0001"+
		"\n\u0001\n\u0003\n\u00e5\b\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0003\r\u00f3"+
		"\b\r\u0001\u000e\u0001\u000e\u0001\u000f\u0004\u000f\u00f8\b\u000f\u000b"+
		"\u000f\f\u000f\u00f9\u0001\u000f\u0004\u000f\u00fd\b\u000f\u000b\u000f"+
		"\f\u000f\u00fe\u0001\u000f\u0004\u000f\u0102\b\u000f\u000b\u000f\f\u000f"+
		"\u0103\u0005\u000f\u0106\b\u000f\n\u000f\f\u000f\u0109\t\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0003\u0011\u0113\b\u0011\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u0122\b\u0012"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0003\u0013\u0138\b\u0013\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0003\u0014\u0143\b\u0014\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0003\u0015\u0150\b\u0015\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u0164\b\u0016"+
		"\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017"+
		"\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0003\u0017\u0170\b\u0017"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0003\u0018"+
		"\u0177\b\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0003\u001b\u0188\b\u001b"+
		"\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0004\u001c"+
		"\u018f\b\u001c\u000b\u001c\f\u001c\u0190\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0004\u001d\u0198\b\u001d\u000b\u001d\f"+
		"\u001d\u0199\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0003\u001e\u01a4\b\u001e\u0001\u001f"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0003\u001f\u01aa\b\u001f\u0001 "+
		"\u0001 \u0001 \u0001 \u0001 \u0004 \u01b1\b \u000b \f \u01b2\u0001!\u0001"+
		"!\u0001!\u0001!\u0001!\u0004!\u01ba\b!\u000b!\f!\u01bb\u0001\"\u0001\""+
		"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0003\"\u01c5\b\"\u0001#\u0001"+
		"#\u0001#\u0001#\u0001#\u0003#\u01cc\b#\u0001#\u0001#\u0001#\u0001#\u0001"+
		"#\u0001#\u0001$\u0001$\u0001$\u0001$\u0001$\u0003$\u01d9\b$\u0001$\u0001"+
		"$\u0001$\u0003$\u01de\b$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001"+
		"$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001"+
		"$\u0003$\u01f1\b$\u0001%\u0001%\u0001%\u0001%\u0001&\u0001&\u0001\'\u0001"+
		"\'\u0001\'\u0001(\u0001(\u0003(\u01fe\b(\u0001)\u0001)\u0001*\u0001*\u0001"+
		"+\u0001+\u0001,\u0001,\u0001,\u0003,\u0209\b,\u0001-\u0001-\u0001-\u0001"+
		"-\u0001-\u0001-\u0001-\u0001-\u0001-\u0003-\u0214\b-\u0001.\u0001.\u0001"+
		".\u0003.\u0219\b.\u0001/\u0003/\u021c\b/\u0001/\u0001/\u0003/\u0220\b"+
		"/\u00010\u00010\u00040\u0224\b0\u000b0\f0\u0225\u00011\u00011\u00051\u022a"+
		"\b1\n1\f1\u022d\t1\u00011\u00031\u0230\b1\u00012\u00012\u00012\u00042"+
		"\u0235\b2\u000b2\f2\u0236\u00013\u00013\u00053\u023b\b3\n3\f3\u023e\t"+
		"3\u00013\u00033\u0241\b3\u00014\u00014\u00014\u00014\u00014\u00044\u0248"+
		"\b4\u000b4\f4\u0249\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u0003"+
		"4\u0253\b4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u0003"+
		"4\u02ad\b4\u00034\u02af\b4\u00015\u00015\u00015\u00015\u00015\u00055\u02b6"+
		"\b5\n5\f5\u02b9\t5\u00016\u00016\u00016\u00016\u00016\u00046\u02c0\b6"+
		"\u000b6\f6\u02c1\u00017\u00017\u00017\u00017\u00017\u00057\u02c9\b7\n"+
		"7\f7\u02cc\t7\u00017\u00017\u00017\u00018\u00018\u00018\u00018\u00018"+
		"\u00018\u00018\u00038\u02d8\b8\u00019\u00019\u00019\u00039\u02dd\b9\u0001"+
		":\u0001:\u0001:\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0003"+
		";\u02e9\b;\u0001<\u0001<\u0001=\u0001=\u0001>\u0001>\u0001?\u0001?\u0001"+
		"@\u0001@\u0001A\u0001A\u0001B\u0001B\u0001C\u0001C\u0001D\u0001D\u0001"+
		"E\u0001E\u0001E\u0003E\u0300\bE\u0001F\u0001F\u0001F\u0001F\u0001F\u0001"+
		"F\u0001F\u0001F\u0003F\u030a\bF\u0001G\u0001G\u0001G\u0001G\u0001G\u0001"+
		"G\u0003G\u0312\bG\u0001G\u0000\u0000H\u0000\u0002\u0004\u0006\b\n\f\u000e"+
		"\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDF"+
		"HJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c"+
		"\u008e\u0000\u001d\u0002\u0000))II\u0002\u0000**JJ\u0002\u000088XX\u0002"+
		"\u0000((HH\u0002\u000033SS\u0002\u0000&&FF\u0002\u000099YY\u0002\u0000"+
		"44TT\u0002\u0000++KK\u0002\u000077WW\u0002\u000011QQ\u0002\u0000--MM\u0002"+
		"\u0000..NN\u0002\u000055UU\u0002\u000022RR\u0002\u0000::ZZ\u0002\u0000"+
		"\u0010\u0010\u0012\u0012\u0001\u0000\u0006\u000e\u0001\u0000\u0010c\u0001"+
		"\u0000\b\u000e\u0001\u0000\u0006\u0013\u0001\u0000\u0015c\u0001\u0000"+
		"\u0015\u001e\u0001\u0000\u0016\u001e\u0001\u0000\u0006`\u0001\u0000bc"+
		"\u0001\u0000\u0005\u0006\u0001\u0000\b@\u0001\u0000Bc\u033b\u0000\u0090"+
		"\u0001\u0000\u0000\u0000\u0002\u0095\u0001\u0000\u0000\u0000\u0004\u009e"+
		"\u0001\u0000\u0000\u0000\u0006\u00a7\u0001\u0000\u0000\u0000\b\u00a9\u0001"+
		"\u0000\u0000\u0000\n\u00b3\u0001\u0000\u0000\u0000\f\u00bd\u0001\u0000"+
		"\u0000\u0000\u000e\u00c3\u0001\u0000\u0000\u0000\u0010\u00cb\u0001\u0000"+
		"\u0000\u0000\u0012\u00d2\u0001\u0000\u0000\u0000\u0014\u00e4\u0001\u0000"+
		"\u0000\u0000\u0016\u00e6\u0001\u0000\u0000\u0000\u0018\u00e8\u0001\u0000"+
		"\u0000\u0000\u001a\u00ea\u0001\u0000\u0000\u0000\u001c\u00f4\u0001\u0000"+
		"\u0000\u0000\u001e\u00f7\u0001\u0000\u0000\u0000 \u010a\u0001\u0000\u0000"+
		"\u0000\"\u0112\u0001\u0000\u0000\u0000$\u0121\u0001\u0000\u0000\u0000"+
		"&\u0137\u0001\u0000\u0000\u0000(\u0142\u0001\u0000\u0000\u0000*\u014f"+
		"\u0001\u0000\u0000\u0000,\u0163\u0001\u0000\u0000\u0000.\u016f\u0001\u0000"+
		"\u0000\u00000\u0176\u0001\u0000\u0000\u00002\u0178\u0001\u0000\u0000\u0000"+
		"4\u017c\u0001\u0000\u0000\u00006\u0183\u0001\u0000\u0000\u00008\u018e"+
		"\u0001\u0000\u0000\u0000:\u0197\u0001\u0000\u0000\u0000<\u01a3\u0001\u0000"+
		"\u0000\u0000>\u01a5\u0001\u0000\u0000\u0000@\u01b0\u0001\u0000\u0000\u0000"+
		"B\u01b9\u0001\u0000\u0000\u0000D\u01c4\u0001\u0000\u0000\u0000F\u01cb"+
		"\u0001\u0000\u0000\u0000H\u01d8\u0001\u0000\u0000\u0000J\u01f2\u0001\u0000"+
		"\u0000\u0000L\u01f6\u0001\u0000\u0000\u0000N\u01f8\u0001\u0000\u0000\u0000"+
		"P\u01fd\u0001\u0000\u0000\u0000R\u01ff\u0001\u0000\u0000\u0000T\u0201"+
		"\u0001\u0000\u0000\u0000V\u0203\u0001\u0000\u0000\u0000X\u0208\u0001\u0000"+
		"\u0000\u0000Z\u0213\u0001\u0000\u0000\u0000\\\u0218\u0001\u0000\u0000"+
		"\u0000^\u021b\u0001\u0000\u0000\u0000`\u0223\u0001\u0000\u0000\u0000b"+
		"\u022f\u0001\u0000\u0000\u0000d\u0231\u0001\u0000\u0000\u0000f\u0240\u0001"+
		"\u0000\u0000\u0000h\u02ae\u0001\u0000\u0000\u0000j\u02b7\u0001\u0000\u0000"+
		"\u0000l\u02bf\u0001\u0000\u0000\u0000n\u02c3\u0001\u0000\u0000\u0000p"+
		"\u02d7\u0001\u0000\u0000\u0000r\u02dc\u0001\u0000\u0000\u0000t\u02de\u0001"+
		"\u0000\u0000\u0000v\u02e8\u0001\u0000\u0000\u0000x\u02ea\u0001\u0000\u0000"+
		"\u0000z\u02ec\u0001\u0000\u0000\u0000|\u02ee\u0001\u0000\u0000\u0000~"+
		"\u02f0\u0001\u0000\u0000\u0000\u0080\u02f2\u0001\u0000\u0000\u0000\u0082"+
		"\u02f4\u0001\u0000\u0000\u0000\u0084\u02f6\u0001\u0000\u0000\u0000\u0086"+
		"\u02f8\u0001\u0000\u0000\u0000\u0088\u02fa\u0001\u0000\u0000\u0000\u008a"+
		"\u02ff\u0001\u0000\u0000\u0000\u008c\u0309\u0001\u0000\u0000\u0000\u008e"+
		"\u0311\u0001\u0000\u0000\u0000\u0090\u0091\u0003j5\u0000\u0091\u0092\u0003"+
		"\u0002\u0001\u0000\u0092\u0093\u0003j5\u0000\u0093\u0094\u0005\u0000\u0000"+
		"\u0001\u0094\u0001\u0001\u0000\u0000\u0000\u0095\u009a\u0003j5\u0000\u0096"+
		"\u009b\u0003\u0004\u0002\u0000\u0097\u009b\u0003\u0006\u0003\u0000\u0098"+
		"\u009b\u0003\u000e\u0007\u0000\u0099\u009b\u0003\u0012\t\u0000\u009a\u0096"+
		"\u0001\u0000\u0000\u0000\u009a\u0097\u0001\u0000\u0000\u0000\u009a\u0098"+
		"\u0001\u0000\u0000\u0000\u009a\u0099\u0001\u0000\u0000\u0000\u009b\u009c"+
		"\u0001\u0000\u0000\u0000\u009c\u009d\u0003j5\u0000\u009d\u0003\u0001\u0000"+
		"\u0000\u0000\u009e\u009f\u0003\u0012\t\u0000\u009f\u00a0\u0003j5\u0000"+
		"\u00a0\u00a1\u0005\u001f\u0000\u0000\u00a1\u00a2\u0003j5\u0000\u00a2\u00a3"+
		"\u00036\u001b\u0000\u00a3\u0005\u0001\u0000\u0000\u0000\u00a4\u00a8\u0003"+
		"\b\u0004\u0000\u00a5\u00a8\u0003\n\u0005\u0000\u00a6\u00a8\u0003\f\u0006"+
		"\u0000\u00a7\u00a4\u0001\u0000\u0000\u0000\u00a7\u00a5\u0001\u0000\u0000"+
		"\u0000\u00a7\u00a6\u0001\u0000\u0000\u0000\u00a8\u0007\u0001\u0000\u0000"+
		"\u0000\u00a9\u00af\u0003\u0012\t\u0000\u00aa\u00ab\u0003j5\u0000\u00ab"+
		"\u00ac\u00030\u0018\u0000\u00ac\u00ad\u0003j5\u0000\u00ad\u00ae\u0003"+
		"\u0012\t\u0000\u00ae\u00b0\u0001\u0000\u0000\u0000\u00af\u00aa\u0001\u0000"+
		"\u0000\u0000\u00b0\u00b1\u0001\u0000\u0000\u0000\u00b1\u00af\u0001\u0000"+
		"\u0000\u0000\u00b1\u00b2\u0001\u0000\u0000\u0000\u00b2\t\u0001\u0000\u0000"+
		"\u0000\u00b3\u00b9\u0003\u0012\t\u0000\u00b4\u00b5\u0003j5\u0000\u00b5"+
		"\u00b6\u00032\u0019\u0000\u00b6\u00b7\u0003j5\u0000\u00b7\u00b8\u0003"+
		"\u0012\t\u0000\u00b8\u00ba\u0001\u0000\u0000\u0000\u00b9\u00b4\u0001\u0000"+
		"\u0000\u0000\u00ba\u00bb\u0001\u0000\u0000\u0000\u00bb\u00b9\u0001\u0000"+
		"\u0000\u0000\u00bb\u00bc\u0001\u0000\u0000\u0000\u00bc\u000b\u0001\u0000"+
		"\u0000\u0000\u00bd\u00be\u0003\u0012\t\u0000\u00be\u00bf\u0003j5\u0000"+
		"\u00bf\u00c0\u00034\u001a\u0000\u00c0\u00c1\u0003j5\u0000\u00c1\u00c2"+
		"\u0003\u0012\t\u0000\u00c2\r\u0001\u0000\u0000\u0000\u00c3\u00c7\u0003"+
		"\u0012\t\u0000\u00c4\u00c5\u0003j5\u0000\u00c5\u00c6\u0003\u0010\b\u0000"+
		"\u00c6\u00c8\u0001\u0000\u0000\u0000\u00c7\u00c4\u0001\u0000\u0000\u0000"+
		"\u00c8\u00c9\u0001\u0000\u0000\u0000\u00c9\u00c7\u0001\u0000\u0000\u0000"+
		"\u00c9\u00ca\u0001\u0000\u0000\u0000\u00ca\u000f\u0001\u0000\u0000\u0000"+
		"\u00cb\u00cc\u0003\u0016\u000b\u0000\u00cc\u00cd\u0003j5\u0000\u00cd\u00ce"+
		"\u0003V+\u0000\u00ce\u0011\u0001\u0000\u0000\u0000\u00cf\u00d0\u0003\""+
		"\u0011\u0000\u00d0\u00d1\u0003j5\u0000\u00d1\u00d3\u0001\u0000\u0000\u0000"+
		"\u00d2\u00cf\u0001\u0000\u0000\u0000\u00d2\u00d3\u0001\u0000\u0000\u0000"+
		"\u00d3\u00d7\u0001\u0000\u0000\u0000\u00d4\u00d5\u0003\u0018\f\u0000\u00d5"+
		"\u00d6\u0003j5\u0000\u00d6\u00d8\u0001\u0000\u0000\u0000\u00d7\u00d4\u0001"+
		"\u0000\u0000\u0000\u00d7\u00d8\u0001\u0000\u0000\u0000\u00d8\u00e0\u0001"+
		"\u0000\u0000\u0000\u00d9\u00e1\u0003\u0014\n\u0000\u00da\u00db\u0005\r"+
		"\u0000\u0000\u00db\u00dc\u0003j5\u0000\u00dc\u00dd\u0003\u0002\u0001\u0000"+
		"\u00dd\u00de\u0003j5\u0000\u00de\u00df\u0005\u000e\u0000\u0000\u00df\u00e1"+
		"\u0001\u0000\u0000\u0000\u00e0\u00d9\u0001\u0000\u0000\u0000\u00e0\u00da"+
		"\u0001\u0000\u0000\u0000\u00e1\u0013\u0001\u0000\u0000\u0000\u00e2\u00e5"+
		"\u0003\u001a\r\u0000\u00e3\u00e5\u0003 \u0010\u0000\u00e4\u00e2\u0001"+
		"\u0000\u0000\u0000\u00e4\u00e3\u0001\u0000\u0000\u0000\u00e5\u0015\u0001"+
		"\u0000\u0000\u0000\u00e6\u00e7\u0005\u0013\u0000\u0000\u00e7\u0017\u0001"+
		"\u0000\u0000\u0000\u00e8\u00e9\u0005C\u0000\u0000\u00e9\u0019\u0001\u0000"+
		"\u0000\u0000\u00ea\u00f2\u0003\u001c\u000e\u0000\u00eb\u00ec\u0003j5\u0000"+
		"\u00ec\u00ed\u0005a\u0000\u0000\u00ed\u00ee\u0003j5\u0000\u00ee\u00ef"+
		"\u0003\u001e\u000f\u0000\u00ef\u00f0\u0003j5\u0000\u00f0\u00f1\u0005a"+
		"\u0000\u0000\u00f1\u00f3\u0001\u0000\u0000\u0000\u00f2\u00eb\u0001\u0000"+
		"\u0000\u0000\u00f2\u00f3\u0001\u0000\u0000\u0000\u00f3\u001b\u0001\u0000"+
		"\u0000\u0000\u00f4\u00f5\u0003h4\u0000\u00f5\u001d\u0001\u0000\u0000\u0000"+
		"\u00f6\u00f8\u0003\u008aE\u0000\u00f7\u00f6\u0001\u0000\u0000\u0000\u00f8"+
		"\u00f9\u0001\u0000\u0000\u0000\u00f9\u00f7\u0001\u0000\u0000\u0000\u00f9"+
		"\u00fa\u0001\u0000\u0000\u0000\u00fa\u0107\u0001\u0000\u0000\u0000\u00fb"+
		"\u00fd\u0003x<\u0000\u00fc\u00fb\u0001\u0000\u0000\u0000\u00fd\u00fe\u0001"+
		"\u0000\u0000\u0000\u00fe\u00fc\u0001\u0000\u0000\u0000\u00fe\u00ff\u0001"+
		"\u0000\u0000\u0000\u00ff\u0101\u0001\u0000\u0000\u0000\u0100\u0102\u0003"+
		"\u008aE\u0000\u0101\u0100\u0001\u0000\u0000\u0000\u0102\u0103\u0001\u0000"+
		"\u0000\u0000\u0103\u0101\u0001\u0000\u0000\u0000\u0103\u0104\u0001\u0000"+
		"\u0000\u0000\u0104\u0106\u0001\u0000\u0000\u0000\u0105\u00fc\u0001\u0000"+
		"\u0000\u0000\u0106\u0109\u0001\u0000\u0000\u0000\u0107\u0105\u0001\u0000"+
		"\u0000\u0000\u0107\u0108\u0001\u0000\u0000\u0000\u0108\u001f\u0001\u0000"+
		"\u0000\u0000\u0109\u0107\u0001\u0000\u0000\u0000\u010a\u010b\u0005\u000f"+
		"\u0000\u0000\u010b!\u0001\u0000\u0000\u0000\u010c\u0113\u0003(\u0014\u0000"+
		"\u010d\u0113\u0003&\u0013\u0000\u010e\u0113\u0003$\u0012\u0000\u010f\u0113"+
		"\u0003.\u0017\u0000\u0110\u0113\u0003,\u0016\u0000\u0111\u0113\u0003*"+
		"\u0015\u0000\u0112\u010c\u0001\u0000\u0000\u0000\u0112\u010d\u0001\u0000"+
		"\u0000\u0000\u0112\u010e\u0001\u0000\u0000\u0000\u0112\u010f\u0001\u0000"+
		"\u0000\u0000\u0112\u0110\u0001\u0000\u0000\u0000\u0112\u0111\u0001\u0000"+
		"\u0000\u0000\u0113#\u0001\u0000\u0000\u0000\u0114\u0122\u0005!\u0000\u0000"+
		"\u0115\u0116\u0007\u0000\u0000\u0000\u0116\u0117\u0007\u0001\u0000\u0000"+
		"\u0117\u0118\u0007\u0002\u0000\u0000\u0118\u0119\u0007\u0003\u0000\u0000"+
		"\u0119\u011a\u0007\u0001\u0000\u0000\u011a\u011b\u0007\u0004\u0000\u0000"+
		"\u011b\u011c\u0007\u0000\u0000\u0000\u011c\u011d\u0007\u0005\u0000\u0000"+
		"\u011d\u011e\u0007\u0004\u0000\u0000\u011e\u011f\u0007\u0006\u0000\u0000"+
		"\u011f\u0120\u0007\u0007\u0000\u0000\u0120\u0122\u0007\b\u0000\u0000\u0121"+
		"\u0114\u0001\u0000\u0000\u0000\u0121\u0115\u0001\u0000\u0000\u0000\u0122"+
		"%\u0001\u0000\u0000\u0000\u0123\u0124\u0005!\u0000\u0000\u0124\u0138\u0005"+
		"!\u0000\u0000\u0125\u0126\u0007\u0000\u0000\u0000\u0126\u0127\u0007\u0001"+
		"\u0000\u0000\u0127\u0128\u0007\u0002\u0000\u0000\u0128\u0129\u0007\u0003"+
		"\u0000\u0000\u0129\u012a\u0007\u0001\u0000\u0000\u012a\u012b\u0007\u0004"+
		"\u0000\u0000\u012b\u012c\u0007\u0000\u0000\u0000\u012c\u012d\u0007\u0005"+
		"\u0000\u0000\u012d\u012e\u0007\u0004\u0000\u0000\u012e\u012f\u0007\u0006"+
		"\u0000\u0000\u012f\u0130\u0007\u0007\u0000\u0000\u0130\u0131\u0007\t\u0000"+
		"\u0000\u0131\u0132\u0007\u0002\u0000\u0000\u0132\u0133\u0007\u0001\u0000"+
		"\u0000\u0133\u0134\u0007\n\u0000\u0000\u0134\u0135\u0007\b\u0000\u0000"+
		"\u0135\u0136\u0007\u0007\u0000\u0000\u0136\u0138\u0007\b\u0000\u0000\u0137"+
		"\u0123\u0001\u0000\u0000\u0000\u0137\u0125\u0001\u0000\u0000\u0000\u0138"+
		"\'\u0001\u0000\u0000\u0000\u0139\u013a\u0005!\u0000\u0000\u013a\u0143"+
		"\u0005\u0006\u0000\u0000\u013b\u013c\u0007\u0003\u0000\u0000\u013c\u013d"+
		"\u0007\u000b\u0000\u0000\u013d\u013e\u0007\f\u0000\u0000\u013e\u013f\u0007"+
		"\n\u0000\u0000\u013f\u0140\u0007\u0000\u0000\u0000\u0140\u0141\u0007\u0007"+
		"\u0000\u0000\u0141\u0143\u0007\b\u0000\u0000\u0142\u0139\u0001\u0000\u0000"+
		"\u0000\u0142\u013b\u0001\u0000\u0000\u0000\u0143)\u0001\u0000\u0000\u0000"+
		"\u0144\u0150\u0005#\u0000\u0000\u0145\u0146\u0007\u0005\u0000\u0000\u0146"+
		"\u0147\u0007\u0004\u0000\u0000\u0147\u0148\u0007\u0003\u0000\u0000\u0148"+
		"\u0149\u0007\u0001\u0000\u0000\u0149\u014a\u0007\u0002\u0000\u0000\u014a"+
		"\u014b\u0007\u0006\u0000\u0000\u014b\u014c\u0007\u0007\u0000\u0000\u014c"+
		"\u014d\u0007\t\u0000\u0000\u014d\u014e\u0007\u0007\u0000\u0000\u014e\u0150"+
		"\u0007\b\u0000\u0000\u014f\u0144\u0001\u0000\u0000\u0000\u014f\u0145\u0001"+
		"\u0000\u0000\u0000\u0150+\u0001\u0000\u0000\u0000\u0151\u0152\u0005#\u0000"+
		"\u0000\u0152\u0164\u0005#\u0000\u0000\u0153\u0154\u0007\u0005\u0000\u0000"+
		"\u0154\u0155\u0007\u0004\u0000\u0000\u0155\u0156\u0007\u0003\u0000\u0000"+
		"\u0156\u0157\u0007\u0001\u0000\u0000\u0157\u0158\u0007\u0002\u0000\u0000"+
		"\u0158\u0159\u0007\u0006\u0000\u0000\u0159\u015a\u0007\u0007\u0000\u0000"+
		"\u015a\u015b\u0007\t\u0000\u0000\u015b\u015c\u0007\u0007\u0000\u0000\u015c"+
		"\u015d\u0007\t\u0000\u0000\u015d\u015e\u0007\u0002\u0000\u0000\u015e\u015f"+
		"\u0007\u0001\u0000\u0000\u015f\u0160\u0007\n\u0000\u0000\u0160\u0161\u0007"+
		"\b\u0000\u0000\u0161\u0162\u0007\u0007\u0000\u0000\u0162\u0164\u0007\b"+
		"\u0000\u0000\u0163\u0151\u0001\u0000\u0000\u0000\u0163\u0153\u0001\u0000"+
		"\u0000\u0000\u0164-\u0001\u0000\u0000\u0000\u0165\u0166\u0005#\u0000\u0000"+
		"\u0166\u0170\u0005\u0006\u0000\u0000\u0167\u0168\u0007\r\u0000\u0000\u0168"+
		"\u0169\u0007\u0005\u0000\u0000\u0169\u016a\u0007\t\u0000\u0000\u016a\u016b"+
		"\u0007\u0001\u0000\u0000\u016b\u016c\u0007\u0004\u0000\u0000\u016c\u016d"+
		"\u0007\u0006\u0000\u0000\u016d\u016e\u0007\u0007\u0000\u0000\u016e\u0170"+
		"\u0007\b\u0000\u0000\u016f\u0165\u0001\u0000\u0000\u0000\u016f\u0167\u0001"+
		"\u0000\u0000\u0000\u0170/\u0001\u0000\u0000\u0000\u0171\u0172\u0007\u0005"+
		"\u0000\u0000\u0172\u0173\u0007\u0004\u0000\u0000\u0173\u0174\u0007\u0000"+
		"\u0000\u0000\u0174\u0177\u0003l6\u0000\u0175\u0177\u0005\u0011\u0000\u0000"+
		"\u0176\u0171\u0001\u0000\u0000\u0000\u0176\u0175\u0001\u0000\u0000\u0000"+
		"\u01771\u0001\u0000\u0000\u0000\u0178\u0179\u0007\u0007\u0000\u0000\u0179"+
		"\u017a\u0007\t\u0000\u0000\u017a\u017b\u0003l6\u0000\u017b3\u0001\u0000"+
		"\u0000\u0000\u017c\u017d\u0007\u000e\u0000\u0000\u017d\u017e\u0007\f\u0000"+
		"\u0000\u017e\u017f\u0007\u0004\u0000\u0000\u017f\u0180\u0007\u000f\u0000"+
		"\u0000\u0180\u0181\u0007\u0002\u0000\u0000\u0181\u0182\u0003l6\u0000\u0182"+
		"5\u0001\u0000\u0000\u0000\u0183\u0184\u0003<\u001e\u0000\u0184\u0187\u0003"+
		"j5\u0000\u0185\u0188\u00038\u001c\u0000\u0186\u0188\u0003:\u001d\u0000"+
		"\u0187\u0185\u0001\u0000\u0000\u0000\u0187\u0186\u0001\u0000\u0000\u0000"+
		"\u0187\u0188\u0001\u0000\u0000\u0000\u01887\u0001\u0000\u0000\u0000\u0189"+
		"\u018a\u0003j5\u0000\u018a\u018b\u00030\u0018\u0000\u018b\u018c\u0003"+
		"j5\u0000\u018c\u018d\u0003<\u001e\u0000\u018d\u018f\u0001\u0000\u0000"+
		"\u0000\u018e\u0189\u0001\u0000\u0000\u0000\u018f\u0190\u0001\u0000\u0000"+
		"\u0000\u0190\u018e\u0001\u0000\u0000\u0000\u0190\u0191\u0001\u0000\u0000"+
		"\u0000\u01919\u0001\u0000\u0000\u0000\u0192\u0193\u0003j5\u0000\u0193"+
		"\u0194\u00032\u0019\u0000\u0194\u0195\u0003j5\u0000\u0195\u0196\u0003"+
		"<\u001e\u0000\u0196\u0198\u0001\u0000\u0000\u0000\u0197\u0192\u0001\u0000"+
		"\u0000\u0000\u0198\u0199\u0001\u0000\u0000\u0000\u0199\u0197\u0001\u0000"+
		"\u0000\u0000\u0199\u019a\u0001\u0000\u0000\u0000\u019a;\u0001\u0000\u0000"+
		"\u0000\u019b\u01a4\u0003>\u001f\u0000\u019c\u01a4\u0003F#\u0000\u019d"+
		"\u019e\u0005\r\u0000\u0000\u019e\u019f\u0003j5\u0000\u019f\u01a0\u0003"+
		"6\u001b\u0000\u01a0\u01a1\u0003j5\u0000\u01a1\u01a2\u0005\u000e\u0000"+
		"\u0000\u01a2\u01a4\u0001\u0000\u0000\u0000\u01a3\u019b\u0001\u0000\u0000"+
		"\u0000\u01a3\u019c\u0001\u0000\u0000\u0000\u01a3\u019d\u0001\u0000\u0000"+
		"\u0000\u01a4=\u0001\u0000\u0000\u0000\u01a5\u01a6\u0003D\"\u0000\u01a6"+
		"\u01a9\u0003j5\u0000\u01a7\u01aa\u0003@ \u0000\u01a8\u01aa\u0003B!\u0000"+
		"\u01a9\u01a7\u0001\u0000\u0000\u0000\u01a9\u01a8\u0001\u0000\u0000\u0000"+
		"\u01a9\u01aa\u0001\u0000\u0000\u0000\u01aa?\u0001\u0000\u0000\u0000\u01ab"+
		"\u01ac\u0003j5\u0000\u01ac\u01ad\u00030\u0018\u0000\u01ad\u01ae\u0003"+
		"j5\u0000\u01ae\u01af\u0003D\"\u0000\u01af\u01b1\u0001\u0000\u0000\u0000"+
		"\u01b0\u01ab\u0001\u0000\u0000\u0000\u01b1\u01b2\u0001\u0000\u0000\u0000"+
		"\u01b2\u01b0\u0001\u0000\u0000\u0000\u01b2\u01b3\u0001\u0000\u0000\u0000"+
		"\u01b3A\u0001\u0000\u0000\u0000\u01b4\u01b5\u0003j5\u0000\u01b5\u01b6"+
		"\u00032\u0019\u0000\u01b6\u01b7\u0003j5\u0000\u01b7\u01b8\u0003D\"\u0000"+
		"\u01b8\u01ba\u0001\u0000\u0000\u0000\u01b9\u01b4\u0001\u0000\u0000\u0000"+
		"\u01ba\u01bb\u0001\u0000\u0000\u0000\u01bb\u01b9\u0001\u0000\u0000\u0000"+
		"\u01bb\u01bc\u0001\u0000\u0000\u0000\u01bcC\u0001\u0000\u0000\u0000\u01bd"+
		"\u01c5\u0003H$\u0000\u01be\u01bf\u0005\r\u0000\u0000\u01bf\u01c0\u0003"+
		"j5\u0000\u01c0\u01c1\u0003>\u001f\u0000\u01c1\u01c2\u0003j5\u0000\u01c2"+
		"\u01c3\u0005\u000e\u0000\u0000\u01c3\u01c5\u0001\u0000\u0000\u0000\u01c4"+
		"\u01bd\u0001\u0000\u0000\u0000\u01c4\u01be\u0001\u0000\u0000\u0000\u01c5"+
		"E\u0001\u0000\u0000\u0000\u01c6\u01c7\u0005@\u0000\u0000\u01c7\u01c8\u0003"+
		"J%\u0000\u01c8\u01c9\u0005B\u0000\u0000\u01c9\u01ca\u0003j5\u0000\u01ca"+
		"\u01cc\u0001\u0000\u0000\u0000\u01cb\u01c6\u0001\u0000\u0000\u0000\u01cb"+
		"\u01cc\u0001\u0000\u0000\u0000\u01cc\u01cd\u0001\u0000\u0000\u0000\u01cd"+
		"\u01ce\u0005`\u0000\u0000\u01ce\u01cf\u0003j5\u0000\u01cf\u01d0\u0003"+
		">\u001f\u0000\u01d0\u01d1\u0003j5\u0000\u01d1\u01d2\u0005b\u0000\u0000"+
		"\u01d2G\u0001\u0000\u0000\u0000\u01d3\u01d4\u0005@\u0000\u0000\u01d4\u01d5"+
		"\u0003J%\u0000\u01d5\u01d6\u0005B\u0000\u0000\u01d6\u01d7\u0003j5\u0000"+
		"\u01d7\u01d9\u0001\u0000\u0000\u0000\u01d8\u01d3\u0001\u0000\u0000\u0000"+
		"\u01d8\u01d9\u0001\u0000\u0000\u0000\u01d9\u01dd\u0001\u0000\u0000\u0000"+
		"\u01da\u01db\u0003T*\u0000\u01db\u01dc\u0003j5\u0000\u01dc\u01de\u0001"+
		"\u0000\u0000\u0000\u01dd\u01da\u0001\u0000\u0000\u0000\u01dd\u01de\u0001"+
		"\u0000\u0000\u0000\u01de\u01df\u0001\u0000\u0000\u0000\u01df\u01e0\u0003"+
		"V+\u0000\u01e0\u01f0\u0003j5\u0000\u01e1\u01e2\u0003X,\u0000\u01e2\u01e3"+
		"\u0003j5\u0000\u01e3\u01e4\u0003\u0012\t\u0000\u01e4\u01f1\u0001\u0000"+
		"\u0000\u0000\u01e5\u01e6\u0003Z-\u0000\u01e6\u01e7\u0003j5\u0000\u01e7"+
		"\u01e8\u0005\b\u0000\u0000\u01e8\u01e9\u0003^/\u0000\u01e9\u01f1\u0001"+
		"\u0000\u0000\u0000\u01ea\u01eb\u0003\\.\u0000\u01eb\u01ec\u0003j5\u0000"+
		"\u01ec\u01ed\u0003\u0080@\u0000\u01ed\u01ee\u0003`0\u0000\u01ee\u01ef"+
		"\u0003\u0080@\u0000\u01ef\u01f1\u0001\u0000\u0000\u0000\u01f0\u01e1\u0001"+
		"\u0000\u0000\u0000\u01f0\u01e5\u0001\u0000\u0000\u0000\u01f0\u01ea\u0001"+
		"\u0000\u0000\u0000\u01f1I\u0001\u0000\u0000\u0000\u01f2\u01f3\u0003L&"+
		"\u0000\u01f3\u01f4\u0003N\'\u0000\u01f4\u01f5\u0003P(\u0000\u01f5K\u0001"+
		"\u0000\u0000\u0000\u01f6\u01f7\u0003f3\u0000\u01f7M\u0001\u0000\u0000"+
		"\u0000\u01f8\u01f9\u0005\u0013\u0000\u0000\u01f9\u01fa\u0005\u0013\u0000"+
		"\u0000\u01faO\u0001\u0000\u0000\u0000\u01fb\u01fe\u0003f3\u0000\u01fc"+
		"\u01fe\u0003R)\u0000\u01fd\u01fb\u0001\u0000\u0000\u0000\u01fd\u01fc\u0001"+
		"\u0000\u0000\u0000\u01feQ\u0001\u0000\u0000\u0000\u01ff\u0200\u0005\u000f"+
		"\u0000\u0000\u0200S\u0001\u0000\u0000\u0000\u0201\u0202\u00057\u0000\u0000"+
		"\u0202U\u0001\u0000\u0000\u0000\u0203\u0204\u0003\u0012\t\u0000\u0204"+
		"W\u0001\u0000\u0000\u0000\u0205\u0209\u0005\"\u0000\u0000\u0206\u0207"+
		"\u0005\u0006\u0000\u0000\u0207\u0209\u0005\"\u0000\u0000\u0208\u0205\u0001"+
		"\u0000\u0000\u0000\u0208\u0206\u0001\u0000\u0000\u0000\u0209Y\u0001\u0000"+
		"\u0000\u0000\u020a\u0214\u0005\"\u0000\u0000\u020b\u020c\u0005\u0006\u0000"+
		"\u0000\u020c\u0214\u0005\"\u0000\u0000\u020d\u020e\u0005!\u0000\u0000"+
		"\u020e\u0214\u0005\"\u0000\u0000\u020f\u0214\u0005!\u0000\u0000\u0210"+
		"\u0211\u0005#\u0000\u0000\u0211\u0214\u0005\"\u0000\u0000\u0212\u0214"+
		"\u0005#\u0000\u0000\u0213\u020a\u0001\u0000\u0000\u0000\u0213\u020b\u0001"+
		"\u0000\u0000\u0000\u0213\u020d\u0001\u0000\u0000\u0000\u0213\u020f\u0001"+
		"\u0000\u0000\u0000\u0213\u0210\u0001\u0000\u0000\u0000\u0213\u0212\u0001"+
		"\u0000\u0000\u0000\u0214[\u0001\u0000\u0000\u0000\u0215\u0219\u0005\""+
		"\u0000\u0000\u0216\u0217\u0005\u0006\u0000\u0000\u0217\u0219\u0005\"\u0000"+
		"\u0000\u0218\u0215\u0001\u0000\u0000\u0000\u0218\u0216\u0001\u0000\u0000"+
		"\u0000\u0219]\u0001\u0000\u0000\u0000\u021a\u021c\u0007\u0010\u0000\u0000"+
		"\u021b\u021a\u0001\u0000\u0000\u0000\u021b\u021c\u0001\u0000\u0000\u0000"+
		"\u021c\u021f\u0001\u0000\u0000\u0000\u021d\u0220\u0003d2\u0000\u021e\u0220"+
		"\u0003b1\u0000\u021f\u021d\u0001\u0000\u0000\u0000\u021f\u021e\u0001\u0000"+
		"\u0000\u0000\u0220_\u0001\u0000\u0000\u0000\u0221\u0224\u0003\u008cF\u0000"+
		"\u0222\u0224\u0003\u008eG\u0000\u0223\u0221\u0001\u0000\u0000\u0000\u0223"+
		"\u0222\u0001\u0000\u0000\u0000\u0224\u0225\u0001\u0000\u0000\u0000\u0225"+
		"\u0223\u0001\u0000\u0000\u0000\u0225\u0226\u0001\u0000\u0000\u0000\u0226"+
		"a\u0001\u0000\u0000\u0000\u0227\u022b\u0003\u0088D\u0000\u0228\u022a\u0003"+
		"\u0084B\u0000\u0229\u0228\u0001\u0000\u0000\u0000\u022a\u022d\u0001\u0000"+
		"\u0000\u0000\u022b\u0229\u0001\u0000\u0000\u0000\u022b\u022c\u0001\u0000"+
		"\u0000\u0000\u022c\u0230\u0001\u0000\u0000\u0000\u022d\u022b\u0001\u0000"+
		"\u0000\u0000\u022e\u0230\u0003\u0086C\u0000\u022f\u0227\u0001\u0000\u0000"+
		"\u0000\u022f\u022e\u0001\u0000\u0000\u0000\u0230c\u0001\u0000\u0000\u0000"+
		"\u0231\u0232\u0003b1\u0000\u0232\u0234\u0005\u0013\u0000\u0000\u0233\u0235"+
		"\u0003\u0084B\u0000\u0234\u0233\u0001\u0000\u0000\u0000\u0235\u0236\u0001"+
		"\u0000\u0000\u0000\u0236\u0234\u0001\u0000\u0000\u0000\u0236\u0237\u0001"+
		"\u0000\u0000\u0000\u0237e\u0001\u0000\u0000\u0000\u0238\u023c\u0003\u0088"+
		"D\u0000\u0239\u023b\u0003\u0084B\u0000\u023a\u0239\u0001\u0000\u0000\u0000"+
		"\u023b\u023e\u0001\u0000\u0000\u0000\u023c\u023a\u0001\u0000\u0000\u0000"+
		"\u023c\u023d\u0001\u0000\u0000\u0000\u023d\u0241\u0001\u0000\u0000\u0000"+
		"\u023e\u023c\u0001\u0000\u0000\u0000\u023f\u0241\u0003\u0086C\u0000\u0240"+
		"\u0238\u0001\u0000\u0000\u0000\u0240\u023f\u0001\u0000\u0000\u0000\u0241"+
		"g\u0001\u0000\u0000\u0000\u0242\u0243\u0005M\u0000\u0000\u0243\u0244\u0005"+
		"Y\u0000\u0000\u0244\u0245\u0005Y\u0000\u0000\u0245\u0247\u0005U\u0000"+
		"\u0000\u0246\u0248\u0003r9\u0000\u0247\u0246\u0001\u0000\u0000\u0000\u0248"+
		"\u0249\u0001\u0000\u0000\u0000\u0249\u0247\u0001\u0000\u0000\u0000\u0249"+
		"\u024a\u0001\u0000\u0000\u0000\u024a\u02af\u0001\u0000\u0000\u0000\u024b"+
		"\u024c\u0003\u0088D\u0000\u024c\u024d\u0003\u0084B\u0000\u024d\u024e\u0003"+
		"\u0084B\u0000\u024e\u024f\u0003\u0084B\u0000\u024f\u0250\u0003\u0084B"+
		"\u0000\u0250\u02ac\u0003\u0084B\u0000\u0251\u0253\u0003\u0084B\u0000\u0252"+
		"\u0251\u0001\u0000\u0000\u0000\u0252\u0253\u0001\u0000\u0000\u0000\u0253"+
		"\u02ad\u0001\u0000\u0000\u0000\u0254\u0255\u0003\u0084B\u0000\u0255\u0256"+
		"\u0003\u0084B\u0000\u0256\u02ad\u0001\u0000\u0000\u0000\u0257\u0258\u0003"+
		"\u0084B\u0000\u0258\u0259\u0003\u0084B\u0000\u0259\u025a\u0003\u0084B"+
		"\u0000\u025a\u02ad\u0001\u0000\u0000\u0000\u025b\u025c\u0003\u0084B\u0000"+
		"\u025c\u025d\u0003\u0084B\u0000\u025d\u025e\u0003\u0084B\u0000\u025e\u025f"+
		"\u0003\u0084B\u0000\u025f\u02ad\u0001\u0000\u0000\u0000\u0260\u0261\u0003"+
		"\u0084B\u0000\u0261\u0262\u0003\u0084B\u0000\u0262\u0263\u0003\u0084B"+
		"\u0000\u0263\u0264\u0003\u0084B\u0000\u0264\u0265\u0003\u0084B\u0000\u0265"+
		"\u02ad\u0001\u0000\u0000\u0000\u0266\u0267\u0003\u0084B\u0000\u0267\u0268"+
		"\u0003\u0084B\u0000\u0268\u0269\u0003\u0084B\u0000\u0269\u026a\u0003\u0084"+
		"B\u0000\u026a\u026b\u0003\u0084B\u0000\u026b\u026c\u0003\u0084B\u0000"+
		"\u026c\u02ad\u0001\u0000\u0000\u0000\u026d\u026e\u0003\u0084B\u0000\u026e"+
		"\u026f\u0003\u0084B\u0000\u026f\u0270\u0003\u0084B\u0000\u0270\u0271\u0003"+
		"\u0084B\u0000\u0271\u0272\u0003\u0084B\u0000\u0272\u0273\u0003\u0084B"+
		"\u0000\u0273\u0274\u0003\u0084B\u0000\u0274\u02ad\u0001\u0000\u0000\u0000"+
		"\u0275\u0276\u0003\u0084B\u0000\u0276\u0277\u0003\u0084B\u0000\u0277\u0278"+
		"\u0003\u0084B\u0000\u0278\u0279\u0003\u0084B\u0000\u0279\u027a\u0003\u0084"+
		"B\u0000\u027a\u027b\u0003\u0084B\u0000\u027b\u027c\u0003\u0084B\u0000"+
		"\u027c\u027d\u0003\u0084B\u0000\u027d\u02ad\u0001\u0000\u0000\u0000\u027e"+
		"\u027f\u0003\u0084B\u0000\u027f\u0280\u0003\u0084B\u0000\u0280\u0281\u0003"+
		"\u0084B\u0000\u0281\u0282\u0003\u0084B\u0000\u0282\u0283\u0003\u0084B"+
		"\u0000\u0283\u0284\u0003\u0084B\u0000\u0284\u0285\u0003\u0084B\u0000\u0285"+
		"\u0286\u0003\u0084B\u0000\u0286\u0287\u0003\u0084B\u0000\u0287\u02ad\u0001"+
		"\u0000\u0000\u0000\u0288\u0289\u0003\u0084B\u0000\u0289\u028a\u0003\u0084"+
		"B\u0000\u028a\u028b\u0003\u0084B\u0000\u028b\u028c\u0003\u0084B\u0000"+
		"\u028c\u028d\u0003\u0084B\u0000\u028d\u028e\u0003\u0084B\u0000\u028e\u028f"+
		"\u0003\u0084B\u0000\u028f\u0290\u0003\u0084B\u0000\u0290\u0291\u0003\u0084"+
		"B\u0000\u0291\u0292\u0003\u0084B\u0000\u0292\u02ad\u0001\u0000\u0000\u0000"+
		"\u0293\u0294\u0003\u0084B\u0000\u0294\u0295\u0003\u0084B\u0000\u0295\u0296"+
		"\u0003\u0084B\u0000\u0296\u0297\u0003\u0084B\u0000\u0297\u0298\u0003\u0084"+
		"B\u0000\u0298\u0299\u0003\u0084B\u0000\u0299\u029a\u0003\u0084B\u0000"+
		"\u029a\u029b\u0003\u0084B\u0000\u029b\u029c\u0003\u0084B\u0000\u029c\u029d"+
		"\u0003\u0084B\u0000\u029d\u029e\u0003\u0084B\u0000\u029e\u02ad\u0001\u0000"+
		"\u0000\u0000\u029f\u02a0\u0003\u0084B\u0000\u02a0\u02a1\u0003\u0084B\u0000"+
		"\u02a1\u02a2\u0003\u0084B\u0000\u02a2\u02a3\u0003\u0084B\u0000\u02a3\u02a4"+
		"\u0003\u0084B\u0000\u02a4\u02a5\u0003\u0084B\u0000\u02a5\u02a6\u0003\u0084"+
		"B\u0000\u02a6\u02a7\u0003\u0084B\u0000\u02a7\u02a8\u0003\u0084B\u0000"+
		"\u02a8\u02a9\u0003\u0084B\u0000\u02a9\u02aa\u0003\u0084B\u0000\u02aa\u02ab"+
		"\u0003\u0084B\u0000\u02ab\u02ad\u0001\u0000\u0000\u0000\u02ac\u0252\u0001"+
		"\u0000\u0000\u0000\u02ac\u0254\u0001\u0000\u0000\u0000\u02ac\u0257\u0001"+
		"\u0000\u0000\u0000\u02ac\u025b\u0001\u0000\u0000\u0000\u02ac\u0260\u0001"+
		"\u0000\u0000\u0000\u02ac\u0266\u0001\u0000\u0000\u0000\u02ac\u026d\u0001"+
		"\u0000\u0000\u0000\u02ac\u0275\u0001\u0000\u0000\u0000\u02ac\u027e\u0001"+
		"\u0000\u0000\u0000\u02ac\u0288\u0001\u0000\u0000\u0000\u02ac\u0293\u0001"+
		"\u0000\u0000\u0000\u02ac\u029f\u0001\u0000\u0000\u0000\u02ad\u02af\u0001"+
		"\u0000\u0000\u0000\u02ae\u0242\u0001\u0000\u0000\u0000\u02ae\u024b\u0001"+
		"\u0000\u0000\u0000\u02afi\u0001\u0000\u0000\u0000\u02b0\u02b6\u0003x<"+
		"\u0000\u02b1\u02b6\u0003z=\u0000\u02b2\u02b6\u0003|>\u0000\u02b3\u02b6"+
		"\u0003~?\u0000\u02b4\u02b6\u0003n7\u0000\u02b5\u02b0\u0001\u0000\u0000"+
		"\u0000\u02b5\u02b1\u0001\u0000\u0000\u0000\u02b5\u02b2\u0001\u0000\u0000"+
		"\u0000\u02b5\u02b3\u0001\u0000\u0000\u0000\u02b5\u02b4\u0001\u0000\u0000"+
		"\u0000\u02b6\u02b9\u0001\u0000\u0000\u0000\u02b7\u02b5\u0001\u0000\u0000"+
		"\u0000\u02b7\u02b8\u0001\u0000\u0000\u0000\u02b8k\u0001\u0000\u0000\u0000"+
		"\u02b9\u02b7\u0001\u0000\u0000\u0000\u02ba\u02c0\u0003x<\u0000\u02bb\u02c0"+
		"\u0003z=\u0000\u02bc\u02c0\u0003|>\u0000\u02bd\u02c0\u0003~?\u0000\u02be"+
		"\u02c0\u0003n7\u0000\u02bf\u02ba\u0001\u0000\u0000\u0000\u02bf\u02bb\u0001"+
		"\u0000\u0000\u0000\u02bf\u02bc\u0001\u0000\u0000\u0000\u02bf\u02bd\u0001"+
		"\u0000\u0000\u0000\u02bf\u02be\u0001\u0000\u0000\u0000\u02c0\u02c1\u0001"+
		"\u0000\u0000\u0000\u02c1\u02bf\u0001\u0000\u0000\u0000\u02c1\u02c2\u0001"+
		"\u0000\u0000\u0000\u02c2m\u0001\u0000\u0000\u0000\u02c3\u02c4\u0005\u0014"+
		"\u0000\u0000\u02c4\u02c5\u0005\u000f\u0000\u0000\u02c5\u02ca\u0001\u0000"+
		"\u0000\u0000\u02c6\u02c9\u0003p8\u0000\u02c7\u02c9\u0003t:\u0000\u02c8"+
		"\u02c6\u0001\u0000\u0000\u0000\u02c8\u02c7\u0001\u0000\u0000\u0000\u02c9"+
		"\u02cc\u0001\u0000\u0000\u0000\u02ca\u02c8\u0001\u0000\u0000\u0000\u02ca"+
		"\u02cb\u0001\u0000\u0000\u0000\u02cb\u02cd\u0001\u0000\u0000\u0000\u02cc"+
		"\u02ca\u0001\u0000\u0000\u0000\u02cd\u02ce\u0005\u000f\u0000\u0000\u02ce"+
		"\u02cf\u0005\u0014\u0000\u0000\u02cfo\u0001\u0000\u0000\u0000\u02d0\u02d8"+
		"\u0003x<\u0000\u02d1\u02d8\u0003z=\u0000\u02d2\u02d8\u0003|>\u0000\u02d3"+
		"\u02d8\u0003~?\u0000\u02d4\u02d8\u0007\u0011\u0000\u0000\u02d5\u02d8\u0007"+
		"\u0012\u0000\u0000\u02d6\u02d8\u0005\u0001\u0000\u0000\u02d7\u02d0\u0001"+
		"\u0000\u0000\u0000\u02d7\u02d1\u0001\u0000\u0000\u0000\u02d7\u02d2\u0001"+
		"\u0000\u0000\u0000\u02d7\u02d3\u0001\u0000\u0000\u0000\u02d7\u02d4\u0001"+
		"\u0000\u0000\u0000\u02d7\u02d5\u0001\u0000\u0000\u0000\u02d7\u02d6\u0001"+
		"\u0000\u0000\u0000\u02d8q\u0001\u0000\u0000\u0000\u02d9\u02dd\u0007\u0013"+
		"\u0000\u0000\u02da\u02dd\u0007\u0012\u0000\u0000\u02db\u02dd\u0005\u0001"+
		"\u0000\u0000\u02dc\u02d9\u0001\u0000\u0000\u0000\u02dc\u02da\u0001\u0000"+
		"\u0000\u0000\u02dc\u02db\u0001\u0000\u0000\u0000\u02dds\u0001\u0000\u0000"+
		"\u0000\u02de\u02df\u0005\u000f\u0000\u0000\u02df\u02e0\u0003v;\u0000\u02e0"+
		"u\u0001\u0000\u0000\u0000\u02e1\u02e9\u0003x<\u0000\u02e2\u02e9\u0003"+
		"z=\u0000\u02e3\u02e9\u0003|>\u0000\u02e4\u02e9\u0003~?\u0000\u02e5\u02e9"+
		"\u0007\u0014\u0000\u0000\u02e6\u02e9\u0007\u0015\u0000\u0000\u02e7\u02e9"+
		"\u0005\u0001\u0000\u0000\u02e8\u02e1\u0001\u0000\u0000\u0000\u02e8\u02e2"+
		"\u0001\u0000\u0000\u0000\u02e8\u02e3\u0001\u0000\u0000\u0000\u02e8\u02e4"+
		"\u0001\u0000\u0000\u0000\u02e8\u02e5\u0001\u0000\u0000\u0000\u02e8\u02e6"+
		"\u0001\u0000\u0000\u0000\u02e8\u02e7\u0001\u0000\u0000\u0000\u02e9w\u0001"+
		"\u0000\u0000\u0000\u02ea\u02eb\u0005\u0005\u0000\u0000\u02eby\u0001\u0000"+
		"\u0000\u0000\u02ec\u02ed\u0005\u0002\u0000\u0000\u02ed{\u0001\u0000\u0000"+
		"\u0000\u02ee\u02ef\u0005\u0004\u0000\u0000\u02ef}\u0001\u0000\u0000\u0000"+
		"\u02f0\u02f1\u0005\u0003\u0000\u0000\u02f1\u007f\u0001\u0000\u0000\u0000"+
		"\u02f2\u02f3\u0005\u0007\u0000\u0000\u02f3\u0081\u0001\u0000\u0000\u0000"+
		"\u02f4\u02f5\u0005A\u0000\u0000\u02f5\u0083\u0001\u0000\u0000\u0000\u02f6"+
		"\u02f7\u0007\u0016\u0000\u0000\u02f7\u0085\u0001\u0000\u0000\u0000\u02f8"+
		"\u02f9\u0005\u0015\u0000\u0000\u02f9\u0087\u0001\u0000\u0000\u0000\u02fa"+
		"\u02fb\u0007\u0017\u0000\u0000\u02fb\u0089\u0001\u0000\u0000\u0000\u02fc"+
		"\u0300\u0007\u0018\u0000\u0000\u02fd\u0300\u0007\u0019\u0000\u0000\u02fe"+
		"\u0300\u0005\u0001\u0000\u0000\u02ff\u02fc\u0001\u0000\u0000\u0000\u02ff"+
		"\u02fd\u0001\u0000\u0000\u0000\u02ff\u02fe\u0001\u0000\u0000\u0000\u0300"+
		"\u008b\u0001\u0000\u0000\u0000\u0301\u030a\u0003x<\u0000\u0302\u030a\u0003"+
		"z=\u0000\u0303\u030a\u0003|>\u0000\u0304\u030a\u0003~?\u0000\u0305\u030a"+
		"\u0007\u001a\u0000\u0000\u0306\u030a\u0007\u001b\u0000\u0000\u0307\u030a"+
		"\u0007\u001c\u0000\u0000\u0308\u030a\u0005\u0001\u0000\u0000\u0309\u0301"+
		"\u0001\u0000\u0000\u0000\u0309\u0302\u0001\u0000\u0000\u0000\u0309\u0303"+
		"\u0001\u0000\u0000\u0000\u0309\u0304\u0001\u0000\u0000\u0000\u0309\u0305"+
		"\u0001\u0000\u0000\u0000\u0309\u0306\u0001\u0000\u0000\u0000\u0309\u0307"+
		"\u0001\u0000\u0000\u0000\u0309\u0308\u0001\u0000\u0000\u0000\u030a\u008d"+
		"\u0001\u0000\u0000\u0000\u030b\u030c\u0003\u0082A\u0000\u030c\u030d\u0003"+
		"\u0080@\u0000\u030d\u0312\u0001\u0000\u0000\u0000\u030e\u030f\u0003\u0082"+
		"A\u0000\u030f\u0310\u0003\u0082A\u0000\u0310\u0312\u0001\u0000\u0000\u0000"+
		"\u0311\u030b\u0001\u0000\u0000\u0000\u0311\u030e\u0001\u0000\u0000\u0000"+
		"\u0312\u008f\u0001\u0000\u0000\u0000?\u009a\u00a7\u00b1\u00bb\u00c9\u00d2"+
		"\u00d7\u00e0\u00e4\u00f2\u00f9\u00fe\u0103\u0107\u0112\u0121\u0137\u0142"+
		"\u014f\u0163\u016f\u0176\u0187\u0190\u0199\u01a3\u01a9\u01b2\u01bb\u01c4"+
		"\u01cb\u01d8\u01dd\u01f0\u01fd\u0208\u0213\u0218\u021b\u021f\u0223\u0225"+
		"\u022b\u022f\u0236\u023c\u0240\u0249\u0252\u02ac\u02ae\u02b5\u02b7\u02bf"+
		"\u02c1\u02c8\u02ca\u02d7\u02dc\u02e8\u02ff\u0309\u0311";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}