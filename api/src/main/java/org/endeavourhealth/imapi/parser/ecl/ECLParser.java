package org.endeavourhealth.imapi.parser.ecl;
// Generated from java-escape by ANTLR 4.11.1
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
		RULE_bracketcompoundexpressionconstraint = 7, RULE_dottedexpressionconstraint = 8, 
		RULE_dottedexpressionattribute = 9, RULE_subexpressionconstraint = 10, 
		RULE_eclfocusconcept = 11, RULE_dot = 12, RULE_memberof = 13, RULE_eclconceptreference = 14, 
		RULE_conceptid = 15, RULE_term = 16, RULE_wildcard = 17, RULE_constraintoperator = 18, 
		RULE_descendantof = 19, RULE_descendantorselfof = 20, RULE_childof = 21, 
		RULE_ancestorof = 22, RULE_ancestororselfof = 23, RULE_parentof = 24, 
		RULE_conjunction = 25, RULE_disjunction = 26, RULE_exclusion = 27, RULE_eclrefinement = 28, 
		RULE_compoundrefinementset = 29, RULE_conjunctionrefinementset = 30, RULE_disjunctionrefinementset = 31, 
		RULE_bracketcompoundrefinementset = 32, RULE_subrefinement = 33, RULE_bracketsubrefinement = 34, 
		RULE_compoundattributeset = 35, RULE_conjunctionattributeset = 36, RULE_disjunctionattributeset = 37, 
		RULE_bracketattributeset = 38, RULE_subattributeset = 39, RULE_eclattributegroup = 40, 
		RULE_eclattribute = 41, RULE_eclattributestringvalue = 42, RULE_eclattributenumbervalue = 43, 
		RULE_eclattributeexpressionvalue = 44, RULE_cardinality = 45, RULE_minvalue = 46, 
		RULE_to = 47, RULE_maxvalue = 48, RULE_many = 49, RULE_reverseflag = 50, 
		RULE_expressioncomparisonoperator = 51, RULE_numericcomparisonoperator = 52, 
		RULE_stringcomparisonoperator = 53, RULE_numericvalue = 54, RULE_stringvalue = 55, 
		RULE_integervalue = 56, RULE_decimalvalue = 57, RULE_nonnegativeintegervalue = 58, 
		RULE_sctid = 59, RULE_ws = 60, RULE_mws = 61, RULE_comment = 62, RULE_nonstarchar = 63, 
		RULE_nonspacechar = 64, RULE_starwithnonfslash = 65, RULE_nonfslash = 66, 
		RULE_sp = 67, RULE_htab = 68, RULE_cr = 69, RULE_lf = 70, RULE_qm = 71, 
		RULE_bs = 72, RULE_digit = 73, RULE_zero = 74, RULE_digitnonzero = 75, 
		RULE_nonwsnonpipe = 76, RULE_anynonescapedchar = 77, RULE_escapedchar = 78;
	private static String[] makeRuleNames() {
		return new String[] {
			"ecl", "expressionconstraint", "refinedexpressionconstraint", "compoundexpressionconstraint", 
			"conjunctionexpressionconstraint", "disjunctionexpressionconstraint", 
			"exclusionexpressionconstraint", "bracketcompoundexpressionconstraint", 
			"dottedexpressionconstraint", "dottedexpressionattribute", "subexpressionconstraint", 
			"eclfocusconcept", "dot", "memberof", "eclconceptreference", "conceptid", 
			"term", "wildcard", "constraintoperator", "descendantof", "descendantorselfof", 
			"childof", "ancestorof", "ancestororselfof", "parentof", "conjunction", 
			"disjunction", "exclusion", "eclrefinement", "compoundrefinementset", 
			"conjunctionrefinementset", "disjunctionrefinementset", "bracketcompoundrefinementset", 
			"subrefinement", "bracketsubrefinement", "compoundattributeset", "conjunctionattributeset", 
			"disjunctionattributeset", "bracketattributeset", "subattributeset", 
			"eclattributegroup", "eclattribute", "eclattributestringvalue", "eclattributenumbervalue", 
			"eclattributeexpressionvalue", "cardinality", "minvalue", "to", "maxvalue", 
			"many", "reverseflag", "expressioncomparisonoperator", "numericcomparisonoperator", 
			"stringcomparisonoperator", "numericvalue", "stringvalue", "integervalue", 
			"decimalvalue", "nonnegativeintegervalue", "sctid", "ws", "mws", "comment", 
			"nonstarchar", "nonspacechar", "starwithnonfslash", "nonfslash", "sp", 
			"htab", "cr", "lf", "qm", "bs", "digit", "zero", "digitnonzero", "nonwsnonpipe", 
			"anynonescapedchar", "escapedchar"
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
			setState(158);
			ws();
			setState(159);
			expressionconstraint();
			setState(160);
			ws();
			setState(161);
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
			setState(163);
			ws();
			setState(168);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(164);
				refinedexpressionconstraint();
				}
				break;
			case 2:
				{
				setState(165);
				compoundexpressionconstraint();
				}
				break;
			case 3:
				{
				setState(166);
				dottedexpressionconstraint();
				}
				break;
			case 4:
				{
				setState(167);
				subexpressionconstraint();
				}
				break;
			}
			setState(170);
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
		public SubexpressionconstraintContext subexpressionconstraint() {
			return getRuleContext(SubexpressionconstraintContext.class,0);
		}
		public BracketcompoundexpressionconstraintContext bracketcompoundexpressionconstraint() {
			return getRuleContext(BracketcompoundexpressionconstraintContext.class,0);
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
			setState(174);
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
			case LESS_THAN:
			case GREATER_THAN:
			case CAP_A:
			case CAP_C:
			case CAP_D:
			case CAP_P:
			case CARAT:
			case A:
			case C:
			case D:
			case H:
			case P:
				{
				setState(172);
				subexpressionconstraint();
				}
				break;
			case LEFT_PAREN:
				{
				setState(173);
				bracketcompoundexpressionconstraint();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(176);
			ws();
			setState(177);
			match(COLON);
			setState(178);
			ws();
			setState(179);
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
			setState(184);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(181);
				conjunctionexpressionconstraint();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(182);
				disjunctionexpressionconstraint();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(183);
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
		public List<BracketcompoundexpressionconstraintContext> bracketcompoundexpressionconstraint() {
			return getRuleContexts(BracketcompoundexpressionconstraintContext.class);
		}
		public BracketcompoundexpressionconstraintContext bracketcompoundexpressionconstraint(int i) {
			return getRuleContext(BracketcompoundexpressionconstraintContext.class,i);
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
			setState(188);
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
			case LESS_THAN:
			case GREATER_THAN:
			case CAP_A:
			case CAP_C:
			case CAP_D:
			case CAP_P:
			case CARAT:
			case A:
			case C:
			case D:
			case H:
			case P:
				{
				setState(186);
				subexpressionconstraint();
				}
				break;
			case LEFT_PAREN:
				{
				setState(187);
				bracketcompoundexpressionconstraint();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(197); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(190);
					ws();
					setState(191);
					conjunction();
					setState(192);
					ws();
					setState(195);
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
					case LESS_THAN:
					case GREATER_THAN:
					case CAP_A:
					case CAP_C:
					case CAP_D:
					case CAP_P:
					case CARAT:
					case A:
					case C:
					case D:
					case H:
					case P:
						{
						setState(193);
						subexpressionconstraint();
						}
						break;
					case LEFT_PAREN:
						{
						setState(194);
						bracketcompoundexpressionconstraint();
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
				setState(199); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
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
		public List<BracketcompoundexpressionconstraintContext> bracketcompoundexpressionconstraint() {
			return getRuleContexts(BracketcompoundexpressionconstraintContext.class);
		}
		public BracketcompoundexpressionconstraintContext bracketcompoundexpressionconstraint(int i) {
			return getRuleContext(BracketcompoundexpressionconstraintContext.class,i);
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
			setState(203);
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
			case LESS_THAN:
			case GREATER_THAN:
			case CAP_A:
			case CAP_C:
			case CAP_D:
			case CAP_P:
			case CARAT:
			case A:
			case C:
			case D:
			case H:
			case P:
				{
				setState(201);
				subexpressionconstraint();
				}
				break;
			case LEFT_PAREN:
				{
				setState(202);
				bracketcompoundexpressionconstraint();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(212); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(205);
					ws();
					setState(206);
					disjunction();
					setState(207);
					ws();
					setState(210);
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
					case LESS_THAN:
					case GREATER_THAN:
					case CAP_A:
					case CAP_C:
					case CAP_D:
					case CAP_P:
					case CARAT:
					case A:
					case C:
					case D:
					case H:
					case P:
						{
						setState(208);
						subexpressionconstraint();
						}
						break;
					case LEFT_PAREN:
						{
						setState(209);
						bracketcompoundexpressionconstraint();
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
				setState(214); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
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
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public ExclusionContext exclusion() {
			return getRuleContext(ExclusionContext.class,0);
		}
		public List<SubexpressionconstraintContext> subexpressionconstraint() {
			return getRuleContexts(SubexpressionconstraintContext.class);
		}
		public SubexpressionconstraintContext subexpressionconstraint(int i) {
			return getRuleContext(SubexpressionconstraintContext.class,i);
		}
		public List<BracketcompoundexpressionconstraintContext> bracketcompoundexpressionconstraint() {
			return getRuleContexts(BracketcompoundexpressionconstraintContext.class);
		}
		public BracketcompoundexpressionconstraintContext bracketcompoundexpressionconstraint(int i) {
			return getRuleContext(BracketcompoundexpressionconstraintContext.class,i);
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
			setState(218);
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
			case LESS_THAN:
			case GREATER_THAN:
			case CAP_A:
			case CAP_C:
			case CAP_D:
			case CAP_P:
			case CARAT:
			case A:
			case C:
			case D:
			case H:
			case P:
				{
				setState(216);
				subexpressionconstraint();
				}
				break;
			case LEFT_PAREN:
				{
				setState(217);
				bracketcompoundexpressionconstraint();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(220);
			ws();
			setState(221);
			exclusion();
			setState(222);
			ws();
			setState(225);
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
			case LESS_THAN:
			case GREATER_THAN:
			case CAP_A:
			case CAP_C:
			case CAP_D:
			case CAP_P:
			case CARAT:
			case A:
			case C:
			case D:
			case H:
			case P:
				{
				setState(223);
				subexpressionconstraint();
				}
				break;
			case LEFT_PAREN:
				{
				setState(224);
				bracketcompoundexpressionconstraint();
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
	public static class BracketcompoundexpressionconstraintContext extends ParserRuleContext {
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public RefinedexpressionconstraintContext refinedexpressionconstraint() {
			return getRuleContext(RefinedexpressionconstraintContext.class,0);
		}
		public CompoundexpressionconstraintContext compoundexpressionconstraint() {
			return getRuleContext(CompoundexpressionconstraintContext.class,0);
		}
		public BracketcompoundexpressionconstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bracketcompoundexpressionconstraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterBracketcompoundexpressionconstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitBracketcompoundexpressionconstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitBracketcompoundexpressionconstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BracketcompoundexpressionconstraintContext bracketcompoundexpressionconstraint() throws RecognitionException {
		BracketcompoundexpressionconstraintContext _localctx = new BracketcompoundexpressionconstraintContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_bracketcompoundexpressionconstraint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(227);
			match(LEFT_PAREN);
			setState(228);
			ws();
			setState(231);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				setState(229);
				refinedexpressionconstraint();
				}
				break;
			case 2:
				{
				setState(230);
				compoundexpressionconstraint();
				}
				break;
			}
			setState(233);
			ws();
			setState(234);
			match(RIGHT_PAREN);
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
		enterRule(_localctx, 16, RULE_dottedexpressionconstraint);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(236);
			subexpressionconstraint();
			setState(240); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(237);
					ws();
					setState(238);
					dottedexpressionattribute();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(242); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
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
		public SubexpressionconstraintContext subexpressionconstraint() {
			return getRuleContext(SubexpressionconstraintContext.class,0);
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
		enterRule(_localctx, 18, RULE_dottedexpressionattribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(244);
			dot();
			setState(245);
			ws();
			setState(246);
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
		enterRule(_localctx, 20, RULE_subexpressionconstraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(251);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la - 33)) & ~0x3f) == 0 && ((1L << (_la - 33)) & 4505386334814629L) != 0) {
				{
				setState(248);
				constraintoperator();
				setState(249);
				ws();
				}
			}

			setState(256);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CARAT) {
				{
				setState(253);
				memberof();
				setState(254);
				ws();
				}
			}

			setState(258);
			eclfocusconcept();
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
		enterRule(_localctx, 22, RULE_eclfocusconcept);
		try {
			setState(262);
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
				setState(260);
				eclconceptreference();
				}
				break;
			case ASTERISK:
				enterOuterAlt(_localctx, 2);
				{
				setState(261);
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
		enterRule(_localctx, 24, RULE_dot);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(264);
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
		enterRule(_localctx, 26, RULE_memberof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(266);
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
		enterRule(_localctx, 28, RULE_eclconceptreference);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(268);
			conceptid();
			setState(276);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(269);
				ws();
				setState(270);
				match(PIPE);
				setState(271);
				ws();
				setState(272);
				term();
				setState(273);
				ws();
				setState(274);
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
		enterRule(_localctx, 30, RULE_conceptid);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(278);
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
		enterRule(_localctx, 32, RULE_term);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(281); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(280);
					nonwsnonpipe();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(283); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(297);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(286); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(285);
						sp();
						}
						}
						setState(288); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==SPACE );
					setState(291); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(290);
							nonwsnonpipe();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(293); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					}
					} 
				}
				setState(299);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
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
		enterRule(_localctx, 34, RULE_wildcard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(300);
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
		enterRule(_localctx, 36, RULE_constraintoperator);
		try {
			setState(308);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(302);
				childof();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(303);
				descendantorselfof();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(304);
				descendantof();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(305);
				parentof();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(306);
				ancestororselfof();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(307);
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
		enterRule(_localctx, 38, RULE_descendantof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(323);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LESS_THAN:
				{
				setState(310);
				match(LESS_THAN);
				}
				break;
			case CAP_D:
			case D:
				{
				{
				setState(311);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(312);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(313);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(314);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(315);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(316);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(317);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(318);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(319);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(320);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(321);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(322);
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
		enterRule(_localctx, 40, RULE_descendantorselfof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(345);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LESS_THAN:
				{
				{
				setState(325);
				match(LESS_THAN);
				setState(326);
				match(LESS_THAN);
				}
				}
				break;
			case CAP_D:
			case D:
				{
				{
				setState(327);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
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
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(331);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(332);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(333);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(334);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(335);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(336);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(337);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(338);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(339);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(340);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(341);
				_la = _input.LA(1);
				if ( !(_la==CAP_L || _la==L) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(342);
				_la = _input.LA(1);
				if ( !(_la==CAP_F || _la==F) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(343);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(344);
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
		enterRule(_localctx, 42, RULE_childof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(356);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LESS_THAN:
				{
				{
				setState(347);
				match(LESS_THAN);
				setState(348);
				match(EXCLAMATION);
				}
				}
				break;
			case CAP_C:
			case C:
				{
				{
				setState(349);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(350);
				_la = _input.LA(1);
				if ( !(_la==CAP_H || _la==H) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(351);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(352);
				_la = _input.LA(1);
				if ( !(_la==CAP_L || _la==L) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(353);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(354);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(355);
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
		enterRule(_localctx, 44, RULE_ancestorof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(369);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GREATER_THAN:
				{
				setState(358);
				match(GREATER_THAN);
				}
				break;
			case CAP_A:
			case A:
				{
				{
				setState(359);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(360);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(361);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
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
				if ( !(_la==CAP_S || _la==S) ) {
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
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(367);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(368);
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
		enterRule(_localctx, 46, RULE_ancestororselfof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(389);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GREATER_THAN:
				{
				{
				setState(371);
				match(GREATER_THAN);
				setState(372);
				match(GREATER_THAN);
				}
				}
				break;
			case CAP_A:
			case A:
				{
				{
				setState(373);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(374);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(375);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(376);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(377);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(378);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(379);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(380);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(381);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(382);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(383);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(384);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(385);
				_la = _input.LA(1);
				if ( !(_la==CAP_L || _la==L) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(386);
				_la = _input.LA(1);
				if ( !(_la==CAP_F || _la==F) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(387);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(388);
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
		enterRule(_localctx, 48, RULE_parentof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(401);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GREATER_THAN:
				{
				{
				setState(391);
				match(GREATER_THAN);
				setState(392);
				match(EXCLAMATION);
				}
				}
				break;
			case CAP_P:
			case P:
				{
				{
				setState(393);
				_la = _input.LA(1);
				if ( !(_la==CAP_P || _la==P) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(394);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(395);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(396);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(397);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(398);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(399);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(400);
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
		enterRule(_localctx, 50, RULE_conjunction);
		int _la;
		try {
			setState(408);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CAP_A:
			case A:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(403);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(404);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(405);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(406);
				mws();
				}
				}
				break;
			case COMMA:
				enterOuterAlt(_localctx, 2);
				{
				setState(407);
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
		enterRule(_localctx, 52, RULE_disjunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(410);
			_la = _input.LA(1);
			if ( !(_la==CAP_O || _la==O) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(411);
			_la = _input.LA(1);
			if ( !(_la==CAP_R || _la==R) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(412);
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
		enterRule(_localctx, 54, RULE_exclusion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(414);
			_la = _input.LA(1);
			if ( !(_la==CAP_M || _la==M) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(415);
			_la = _input.LA(1);
			if ( !(_la==CAP_I || _la==I) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(416);
			_la = _input.LA(1);
			if ( !(_la==CAP_N || _la==N) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(417);
			_la = _input.LA(1);
			if ( !(_la==CAP_U || _la==U) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(418);
			_la = _input.LA(1);
			if ( !(_la==CAP_S || _la==S) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(419);
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
		public CompoundrefinementsetContext compoundrefinementset() {
			return getRuleContext(CompoundrefinementsetContext.class,0);
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
		enterRule(_localctx, 56, RULE_eclrefinement);
		try {
			setState(423);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(421);
				subrefinement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(422);
				compoundrefinementset();
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
	public static class CompoundrefinementsetContext extends ParserRuleContext {
		public ConjunctionrefinementsetContext conjunctionrefinementset() {
			return getRuleContext(ConjunctionrefinementsetContext.class,0);
		}
		public DisjunctionrefinementsetContext disjunctionrefinementset() {
			return getRuleContext(DisjunctionrefinementsetContext.class,0);
		}
		public CompoundrefinementsetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compoundrefinementset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterCompoundrefinementset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitCompoundrefinementset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitCompoundrefinementset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompoundrefinementsetContext compoundrefinementset() throws RecognitionException {
		CompoundrefinementsetContext _localctx = new CompoundrefinementsetContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_compoundrefinementset);
		try {
			setState(427);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(425);
				conjunctionrefinementset();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(426);
				disjunctionrefinementset();
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
	public static class ConjunctionrefinementsetContext extends ParserRuleContext {
		public List<SubrefinementContext> subrefinement() {
			return getRuleContexts(SubrefinementContext.class);
		}
		public SubrefinementContext subrefinement(int i) {
			return getRuleContext(SubrefinementContext.class,i);
		}
		public List<BracketcompoundrefinementsetContext> bracketcompoundrefinementset() {
			return getRuleContexts(BracketcompoundrefinementsetContext.class);
		}
		public BracketcompoundrefinementsetContext bracketcompoundrefinementset(int i) {
			return getRuleContext(BracketcompoundrefinementsetContext.class,i);
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
		enterRule(_localctx, 60, RULE_conjunctionrefinementset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(431);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				{
				setState(429);
				subrefinement();
				}
				break;
			case 2:
				{
				setState(430);
				bracketcompoundrefinementset();
				}
				break;
			}
			setState(440); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(433);
					ws();
					setState(434);
					conjunction();
					setState(435);
					ws();
					setState(438);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
					case 1:
						{
						setState(436);
						subrefinement();
						}
						break;
					case 2:
						{
						setState(437);
						bracketcompoundrefinementset();
						}
						break;
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(442); 
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
	public static class DisjunctionrefinementsetContext extends ParserRuleContext {
		public List<SubrefinementContext> subrefinement() {
			return getRuleContexts(SubrefinementContext.class);
		}
		public SubrefinementContext subrefinement(int i) {
			return getRuleContext(SubrefinementContext.class,i);
		}
		public List<BracketcompoundrefinementsetContext> bracketcompoundrefinementset() {
			return getRuleContexts(BracketcompoundrefinementsetContext.class);
		}
		public BracketcompoundrefinementsetContext bracketcompoundrefinementset(int i) {
			return getRuleContext(BracketcompoundrefinementsetContext.class,i);
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
		enterRule(_localctx, 62, RULE_disjunctionrefinementset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(446);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				{
				setState(444);
				subrefinement();
				}
				break;
			case 2:
				{
				setState(445);
				bracketcompoundrefinementset();
				}
				break;
			}
			setState(455); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(448);
					ws();
					setState(449);
					disjunction();
					setState(450);
					ws();
					setState(453);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
					case 1:
						{
						setState(451);
						subrefinement();
						}
						break;
					case 2:
						{
						setState(452);
						bracketcompoundrefinementset();
						}
						break;
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(457); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
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
	public static class BracketcompoundrefinementsetContext extends ParserRuleContext {
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public CompoundrefinementsetContext compoundrefinementset() {
			return getRuleContext(CompoundrefinementsetContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public BracketcompoundrefinementsetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bracketcompoundrefinementset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterBracketcompoundrefinementset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitBracketcompoundrefinementset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitBracketcompoundrefinementset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BracketcompoundrefinementsetContext bracketcompoundrefinementset() throws RecognitionException {
		BracketcompoundrefinementsetContext _localctx = new BracketcompoundrefinementsetContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_bracketcompoundrefinementset);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(459);
			match(LEFT_PAREN);
			setState(460);
			ws();
			setState(461);
			compoundrefinementset();
			setState(462);
			ws();
			setState(463);
			match(RIGHT_PAREN);
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
		public CompoundattributesetContext compoundattributeset() {
			return getRuleContext(CompoundattributesetContext.class,0);
		}
		public EclattributegroupContext eclattributegroup() {
			return getRuleContext(EclattributegroupContext.class,0);
		}
		public BracketsubrefinementContext bracketsubrefinement() {
			return getRuleContext(BracketsubrefinementContext.class,0);
		}
		public EclattributeContext eclattribute() {
			return getRuleContext(EclattributeContext.class,0);
		}
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
		enterRule(_localctx, 66, RULE_subrefinement);
		try {
			setState(469);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(465);
				compoundattributeset();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(466);
				eclattributegroup();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(467);
				bracketsubrefinement();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(468);
				eclattribute();
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
	public static class BracketsubrefinementContext extends ParserRuleContext {
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
		public BracketsubrefinementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bracketsubrefinement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterBracketsubrefinement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitBracketsubrefinement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitBracketsubrefinement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BracketsubrefinementContext bracketsubrefinement() throws RecognitionException {
		BracketsubrefinementContext _localctx = new BracketsubrefinementContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_bracketsubrefinement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(471);
			match(LEFT_PAREN);
			setState(472);
			ws();
			setState(473);
			eclrefinement();
			setState(474);
			ws();
			setState(475);
			match(RIGHT_PAREN);
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
	public static class CompoundattributesetContext extends ParserRuleContext {
		public ConjunctionattributesetContext conjunctionattributeset() {
			return getRuleContext(ConjunctionattributesetContext.class,0);
		}
		public DisjunctionattributesetContext disjunctionattributeset() {
			return getRuleContext(DisjunctionattributesetContext.class,0);
		}
		public CompoundattributesetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compoundattributeset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterCompoundattributeset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitCompoundattributeset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitCompoundattributeset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompoundattributesetContext compoundattributeset() throws RecognitionException {
		CompoundattributesetContext _localctx = new CompoundattributesetContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_compoundattributeset);
		try {
			setState(479);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(477);
				conjunctionattributeset();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(478);
				disjunctionattributeset();
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
	public static class ConjunctionattributesetContext extends ParserRuleContext {
		public List<SubattributesetContext> subattributeset() {
			return getRuleContexts(SubattributesetContext.class);
		}
		public SubattributesetContext subattributeset(int i) {
			return getRuleContext(SubattributesetContext.class,i);
		}
		public List<BracketattributesetContext> bracketattributeset() {
			return getRuleContexts(BracketattributesetContext.class);
		}
		public BracketattributesetContext bracketattributeset(int i) {
			return getRuleContext(BracketattributesetContext.class,i);
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
		enterRule(_localctx, 72, RULE_conjunctionattributeset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(483);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				{
				setState(481);
				subattributeset();
				}
				break;
			case 2:
				{
				setState(482);
				bracketattributeset();
				}
				break;
			}
			setState(492); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(485);
					ws();
					setState(486);
					conjunction();
					setState(487);
					ws();
					setState(490);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
					case 1:
						{
						setState(488);
						subattributeset();
						}
						break;
					case 2:
						{
						setState(489);
						bracketattributeset();
						}
						break;
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(494); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
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
		public List<SubattributesetContext> subattributeset() {
			return getRuleContexts(SubattributesetContext.class);
		}
		public SubattributesetContext subattributeset(int i) {
			return getRuleContext(SubattributesetContext.class,i);
		}
		public List<BracketattributesetContext> bracketattributeset() {
			return getRuleContexts(BracketattributesetContext.class);
		}
		public BracketattributesetContext bracketattributeset(int i) {
			return getRuleContext(BracketattributesetContext.class,i);
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
		enterRule(_localctx, 74, RULE_disjunctionattributeset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(498);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				{
				setState(496);
				subattributeset();
				}
				break;
			case 2:
				{
				setState(497);
				bracketattributeset();
				}
				break;
			}
			setState(507); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(500);
					ws();
					setState(501);
					disjunction();
					setState(502);
					ws();
					setState(505);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
					case 1:
						{
						setState(503);
						subattributeset();
						}
						break;
					case 2:
						{
						setState(504);
						bracketattributeset();
						}
						break;
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(509); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
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
	public static class BracketattributesetContext extends ParserRuleContext {
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public CompoundattributesetContext compoundattributeset() {
			return getRuleContext(CompoundattributesetContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public BracketattributesetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bracketattributeset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterBracketattributeset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitBracketattributeset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitBracketattributeset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BracketattributesetContext bracketattributeset() throws RecognitionException {
		BracketattributesetContext _localctx = new BracketattributesetContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_bracketattributeset);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(511);
			match(LEFT_PAREN);
			setState(512);
			ws();
			setState(513);
			compoundattributeset();
			setState(514);
			ws();
			setState(515);
			match(RIGHT_PAREN);
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
		public BracketattributesetContext bracketattributeset() {
			return getRuleContext(BracketattributesetContext.class,0);
		}
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
		enterRule(_localctx, 78, RULE_subattributeset);
		try {
			setState(519);
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
			case LESS_THAN:
			case GREATER_THAN:
			case CAP_A:
			case CAP_C:
			case CAP_D:
			case CAP_P:
			case CAP_R:
			case LEFT_BRACE:
			case CARAT:
			case A:
			case C:
			case D:
			case H:
			case P:
			case R:
				enterOuterAlt(_localctx, 1);
				{
				setState(517);
				eclattribute();
				}
				break;
			case LEFT_PAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(518);
				bracketattributeset();
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
	public static class EclattributegroupContext extends ParserRuleContext {
		public TerminalNode LEFT_CURLY_BRACE() { return getToken(ECLParser.LEFT_CURLY_BRACE, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public TerminalNode RIGHT_CURLY_BRACE() { return getToken(ECLParser.RIGHT_CURLY_BRACE, 0); }
		public CompoundattributesetContext compoundattributeset() {
			return getRuleContext(CompoundattributesetContext.class,0);
		}
		public EclattributeContext eclattribute() {
			return getRuleContext(EclattributeContext.class,0);
		}
		public CardinalityContext cardinality() {
			return getRuleContext(CardinalityContext.class,0);
		}
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
		enterRule(_localctx, 80, RULE_eclattributegroup);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(522);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LEFT_BRACE) {
				{
				setState(521);
				cardinality();
				}
			}

			setState(524);
			match(LEFT_CURLY_BRACE);
			setState(525);
			ws();
			setState(528);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				{
				setState(526);
				compoundattributeset();
				}
				break;
			case 2:
				{
				setState(527);
				eclattribute();
				}
				break;
			}
			setState(530);
			ws();
			setState(531);
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
		public SubexpressionconstraintContext subexpressionconstraint() {
			return getRuleContext(SubexpressionconstraintContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public EclattributeexpressionvalueContext eclattributeexpressionvalue() {
			return getRuleContext(EclattributeexpressionvalueContext.class,0);
		}
		public EclattributenumbervalueContext eclattributenumbervalue() {
			return getRuleContext(EclattributenumbervalueContext.class,0);
		}
		public EclattributestringvalueContext eclattributestringvalue() {
			return getRuleContext(EclattributestringvalueContext.class,0);
		}
		public CardinalityContext cardinality() {
			return getRuleContext(CardinalityContext.class,0);
		}
		public ReverseflagContext reverseflag() {
			return getRuleContext(ReverseflagContext.class,0);
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
		enterRule(_localctx, 82, RULE_eclattribute);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(534);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LEFT_BRACE) {
				{
				setState(533);
				cardinality();
				}
			}

			setState(539);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CAP_R || _la==R) {
				{
				setState(536);
				reverseflag();
				setState(537);
				ws();
				}
			}

			setState(541);
			subexpressionconstraint();
			setState(542);
			ws();
			setState(546);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				{
				setState(543);
				eclattributeexpressionvalue();
				}
				break;
			case 2:
				{
				setState(544);
				eclattributenumbervalue();
				}
				break;
			case 3:
				{
				setState(545);
				eclattributestringvalue();
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
	public static class EclattributestringvalueContext extends ParserRuleContext {
		public StringcomparisonoperatorContext stringcomparisonoperator() {
			return getRuleContext(StringcomparisonoperatorContext.class,0);
		}
		public WsContext ws() {
			return getRuleContext(WsContext.class,0);
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
		public EclattributestringvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eclattributestringvalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterEclattributestringvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitEclattributestringvalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitEclattributestringvalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EclattributestringvalueContext eclattributestringvalue() throws RecognitionException {
		EclattributestringvalueContext _localctx = new EclattributestringvalueContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_eclattributestringvalue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(548);
			stringcomparisonoperator();
			setState(549);
			ws();
			setState(550);
			qm();
			setState(551);
			stringvalue();
			setState(552);
			qm();
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
	public static class EclattributenumbervalueContext extends ParserRuleContext {
		public NumericcomparisonoperatorContext numericcomparisonoperator() {
			return getRuleContext(NumericcomparisonoperatorContext.class,0);
		}
		public WsContext ws() {
			return getRuleContext(WsContext.class,0);
		}
		public TerminalNode POUND() { return getToken(ECLParser.POUND, 0); }
		public NumericvalueContext numericvalue() {
			return getRuleContext(NumericvalueContext.class,0);
		}
		public EclattributenumbervalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eclattributenumbervalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterEclattributenumbervalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitEclattributenumbervalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitEclattributenumbervalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EclattributenumbervalueContext eclattributenumbervalue() throws RecognitionException {
		EclattributenumbervalueContext _localctx = new EclattributenumbervalueContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_eclattributenumbervalue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(554);
			numericcomparisonoperator();
			setState(555);
			ws();
			setState(556);
			match(POUND);
			setState(557);
			numericvalue();
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
	public static class EclattributeexpressionvalueContext extends ParserRuleContext {
		public ExpressioncomparisonoperatorContext expressioncomparisonoperator() {
			return getRuleContext(ExpressioncomparisonoperatorContext.class,0);
		}
		public WsContext ws() {
			return getRuleContext(WsContext.class,0);
		}
		public SubexpressionconstraintContext subexpressionconstraint() {
			return getRuleContext(SubexpressionconstraintContext.class,0);
		}
		public BracketcompoundexpressionconstraintContext bracketcompoundexpressionconstraint() {
			return getRuleContext(BracketcompoundexpressionconstraintContext.class,0);
		}
		public EclattributeexpressionvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eclattributeexpressionvalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterEclattributeexpressionvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitEclattributeexpressionvalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitEclattributeexpressionvalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EclattributeexpressionvalueContext eclattributeexpressionvalue() throws RecognitionException {
		EclattributeexpressionvalueContext _localctx = new EclattributeexpressionvalueContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_eclattributeexpressionvalue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(559);
			expressioncomparisonoperator();
			setState(560);
			ws();
			setState(563);
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
			case LESS_THAN:
			case GREATER_THAN:
			case CAP_A:
			case CAP_C:
			case CAP_D:
			case CAP_P:
			case CARAT:
			case A:
			case C:
			case D:
			case H:
			case P:
				{
				setState(561);
				subexpressionconstraint();
				}
				break;
			case LEFT_PAREN:
				{
				setState(562);
				bracketcompoundexpressionconstraint();
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
	public static class CardinalityContext extends ParserRuleContext {
		public TerminalNode LEFT_BRACE() { return getToken(ECLParser.LEFT_BRACE, 0); }
		public MinvalueContext minvalue() {
			return getRuleContext(MinvalueContext.class,0);
		}
		public ToContext to() {
			return getRuleContext(ToContext.class,0);
		}
		public MaxvalueContext maxvalue() {
			return getRuleContext(MaxvalueContext.class,0);
		}
		public TerminalNode RIGHT_BRACE() { return getToken(ECLParser.RIGHT_BRACE, 0); }
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
		enterRule(_localctx, 90, RULE_cardinality);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(565);
			match(LEFT_BRACE);
			setState(566);
			minvalue();
			setState(567);
			to();
			setState(568);
			maxvalue();
			setState(569);
			match(RIGHT_BRACE);
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
		enterRule(_localctx, 92, RULE_minvalue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(571);
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
		public List<MwsContext> mws() {
			return getRuleContexts(MwsContext.class);
		}
		public MwsContext mws(int i) {
			return getRuleContext(MwsContext.class,i);
		}
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode O() { return getToken(ECLParser.O, 0); }
		public TerminalNode CAP_O() { return getToken(ECLParser.CAP_O, 0); }
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
		enterRule(_localctx, 94, RULE_to);
		int _la;
		try {
			setState(580);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PERIOD:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(573);
				match(PERIOD);
				setState(574);
				match(PERIOD);
				}
				}
				break;
			case TAB:
			case LF:
			case CR:
			case SPACE:
			case SLASH:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(575);
				mws();
				setState(576);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(577);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(578);
				mws();
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
		enterRule(_localctx, 96, RULE_maxvalue);
		try {
			setState(584);
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
				setState(582);
				nonnegativeintegervalue();
				}
				break;
			case ASTERISK:
			case CAP_M:
			case M:
				enterOuterAlt(_localctx, 2);
				{
				setState(583);
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
		public TerminalNode M() { return getToken(ECLParser.M, 0); }
		public TerminalNode CAP_M() { return getToken(ECLParser.CAP_M, 0); }
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode N() { return getToken(ECLParser.N, 0); }
		public TerminalNode CAP_N() { return getToken(ECLParser.CAP_N, 0); }
		public TerminalNode Y() { return getToken(ECLParser.Y, 0); }
		public TerminalNode CAP_Y() { return getToken(ECLParser.CAP_Y, 0); }
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
		enterRule(_localctx, 98, RULE_many);
		int _la;
		try {
			setState(591);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ASTERISK:
				enterOuterAlt(_localctx, 1);
				{
				setState(586);
				match(ASTERISK);
				}
				break;
			case CAP_M:
			case M:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(587);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(588);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(589);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(590);
				_la = _input.LA(1);
				if ( !(_la==CAP_Y || _la==Y) ) {
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
		public List<TerminalNode> R() { return getTokens(ECLParser.R); }
		public TerminalNode R(int i) {
			return getToken(ECLParser.R, i);
		}
		public List<TerminalNode> CAP_R() { return getTokens(ECLParser.CAP_R); }
		public TerminalNode CAP_R(int i) {
			return getToken(ECLParser.CAP_R, i);
		}
		public List<TerminalNode> E() { return getTokens(ECLParser.E); }
		public TerminalNode E(int i) {
			return getToken(ECLParser.E, i);
		}
		public List<TerminalNode> CAP_E() { return getTokens(ECLParser.CAP_E); }
		public TerminalNode CAP_E(int i) {
			return getToken(ECLParser.CAP_E, i);
		}
		public TerminalNode V() { return getToken(ECLParser.V, 0); }
		public TerminalNode CAP_V() { return getToken(ECLParser.CAP_V, 0); }
		public TerminalNode S() { return getToken(ECLParser.S, 0); }
		public TerminalNode CAP_S() { return getToken(ECLParser.CAP_S, 0); }
		public TerminalNode O() { return getToken(ECLParser.O, 0); }
		public TerminalNode CAP_O() { return getToken(ECLParser.CAP_O, 0); }
		public TerminalNode F() { return getToken(ECLParser.F, 0); }
		public TerminalNode CAP_F() { return getToken(ECLParser.CAP_F, 0); }
		public MwsContext mws() {
			return getRuleContext(MwsContext.class,0);
		}
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
		enterRule(_localctx, 100, RULE_reverseflag);
		int _la;
		try {
			setState(604);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(593);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(594);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(595);
				_la = _input.LA(1);
				if ( !(_la==CAP_V || _la==V) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(596);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(597);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(598);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(599);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(600);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(601);
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
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(602);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(603);
				mws();
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
		enterRule(_localctx, 102, RULE_expressioncomparisonoperator);
		try {
			setState(609);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EQUALS:
				enterOuterAlt(_localctx, 1);
				{
				setState(606);
				match(EQUALS);
				}
				break;
			case EXCLAMATION:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(607);
				match(EXCLAMATION);
				setState(608);
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
		enterRule(_localctx, 104, RULE_numericcomparisonoperator);
		try {
			setState(620);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(611);
				match(EQUALS);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(612);
				match(EXCLAMATION);
				setState(613);
				match(EQUALS);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(614);
				match(LESS_THAN);
				setState(615);
				match(EQUALS);
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(616);
				match(LESS_THAN);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				{
				setState(617);
				match(GREATER_THAN);
				setState(618);
				match(EQUALS);
				}
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(619);
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
		enterRule(_localctx, 106, RULE_stringcomparisonoperator);
		try {
			setState(625);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EQUALS:
				enterOuterAlt(_localctx, 1);
				{
				setState(622);
				match(EQUALS);
				}
				break;
			case EXCLAMATION:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(623);
				match(EXCLAMATION);
				setState(624);
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
		enterRule(_localctx, 108, RULE_numericvalue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(628);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PLUS || _la==DASH) {
				{
				setState(627);
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

			setState(632);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				{
				setState(630);
				decimalvalue();
				}
				break;
			case 2:
				{
				setState(631);
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
		enterRule(_localctx, 110, RULE_stringvalue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(636); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(636);
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
					setState(634);
					anynonescapedchar();
					}
					break;
				case BACKSLASH:
					{
					setState(635);
					escapedchar();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(638); 
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
		enterRule(_localctx, 112, RULE_integervalue);
		int _la;
		try {
			setState(648);
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
				setState(640);
				digitnonzero();
				setState(644);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((_la) & ~0x3f) == 0 && ((1L << _la) & 2145386496L) != 0) {
					{
					{
					setState(641);
					digit();
					}
					}
					setState(646);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				break;
			case ZERO:
				enterOuterAlt(_localctx, 2);
				{
				setState(647);
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
		enterRule(_localctx, 114, RULE_decimalvalue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(650);
			integervalue();
			setState(651);
			match(PERIOD);
			setState(653); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(652);
				digit();
				}
				}
				setState(655); 
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
		enterRule(_localctx, 116, RULE_nonnegativeintegervalue);
		int _la;
		try {
			setState(665);
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
				setState(657);
				digitnonzero();
				setState(661);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((_la) & ~0x3f) == 0 && ((1L << _la) & 2145386496L) != 0) {
					{
					{
					setState(658);
					digit();
					}
					}
					setState(663);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				break;
			case ZERO:
				enterOuterAlt(_localctx, 2);
				{
				setState(664);
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
		enterRule(_localctx, 118, RULE_sctid);
		int _la;
		try {
			int _alt;
			setState(775);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case H:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(667);
				match(H);
				setState(668);
				match(T);
				setState(669);
				match(T);
				setState(670);
				match(P);
				setState(672); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(671);
						nonspacechar();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(674); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,68,_ctx);
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
				setState(676);
				digitnonzero();
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
				setState(773);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,70,_ctx) ) {
				case 1:
					{
					setState(683);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (((_la) & ~0x3f) == 0 && ((1L << _la) & 2145386496L) != 0) {
						{
						setState(682);
						digit();
						}
					}

					}
					break;
				case 2:
					{
					{
					{
					setState(685);
					digit();
					}
					{
					setState(686);
					digit();
					}
					}
					}
					break;
				case 3:
					{
					{
					{
					setState(688);
					digit();
					}
					{
					setState(689);
					digit();
					}
					{
					setState(690);
					digit();
					}
					}
					}
					break;
				case 4:
					{
					{
					{
					setState(692);
					digit();
					}
					{
					setState(693);
					digit();
					}
					{
					setState(694);
					digit();
					}
					{
					setState(695);
					digit();
					}
					}
					}
					break;
				case 5:
					{
					{
					{
					setState(697);
					digit();
					}
					{
					setState(698);
					digit();
					}
					{
					setState(699);
					digit();
					}
					{
					setState(700);
					digit();
					}
					{
					setState(701);
					digit();
					}
					}
					}
					break;
				case 6:
					{
					{
					{
					setState(703);
					digit();
					}
					{
					setState(704);
					digit();
					}
					{
					setState(705);
					digit();
					}
					{
					setState(706);
					digit();
					}
					{
					setState(707);
					digit();
					}
					{
					setState(708);
					digit();
					}
					}
					}
					break;
				case 7:
					{
					{
					{
					setState(710);
					digit();
					}
					{
					setState(711);
					digit();
					}
					{
					setState(712);
					digit();
					}
					{
					setState(713);
					digit();
					}
					{
					setState(714);
					digit();
					}
					{
					setState(715);
					digit();
					}
					{
					setState(716);
					digit();
					}
					}
					}
					break;
				case 8:
					{
					{
					{
					setState(718);
					digit();
					}
					{
					setState(719);
					digit();
					}
					{
					setState(720);
					digit();
					}
					{
					setState(721);
					digit();
					}
					{
					setState(722);
					digit();
					}
					{
					setState(723);
					digit();
					}
					{
					setState(724);
					digit();
					}
					{
					setState(725);
					digit();
					}
					}
					}
					break;
				case 9:
					{
					{
					{
					setState(727);
					digit();
					}
					{
					setState(728);
					digit();
					}
					{
					setState(729);
					digit();
					}
					{
					setState(730);
					digit();
					}
					{
					setState(731);
					digit();
					}
					{
					setState(732);
					digit();
					}
					{
					setState(733);
					digit();
					}
					{
					setState(734);
					digit();
					}
					{
					setState(735);
					digit();
					}
					}
					}
					break;
				case 10:
					{
					{
					{
					setState(737);
					digit();
					}
					{
					setState(738);
					digit();
					}
					{
					setState(739);
					digit();
					}
					{
					setState(740);
					digit();
					}
					{
					setState(741);
					digit();
					}
					{
					setState(742);
					digit();
					}
					{
					setState(743);
					digit();
					}
					{
					setState(744);
					digit();
					}
					{
					setState(745);
					digit();
					}
					{
					setState(746);
					digit();
					}
					}
					}
					break;
				case 11:
					{
					{
					{
					setState(748);
					digit();
					}
					{
					setState(749);
					digit();
					}
					{
					setState(750);
					digit();
					}
					{
					setState(751);
					digit();
					}
					{
					setState(752);
					digit();
					}
					{
					setState(753);
					digit();
					}
					{
					setState(754);
					digit();
					}
					{
					setState(755);
					digit();
					}
					{
					setState(756);
					digit();
					}
					{
					setState(757);
					digit();
					}
					{
					setState(758);
					digit();
					}
					}
					}
					break;
				case 12:
					{
					{
					{
					setState(760);
					digit();
					}
					{
					setState(761);
					digit();
					}
					{
					setState(762);
					digit();
					}
					{
					setState(763);
					digit();
					}
					{
					setState(764);
					digit();
					}
					{
					setState(765);
					digit();
					}
					{
					setState(766);
					digit();
					}
					{
					setState(767);
					digit();
					}
					{
					setState(768);
					digit();
					}
					{
					setState(769);
					digit();
					}
					{
					setState(770);
					digit();
					}
					{
					setState(771);
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
		enterRule(_localctx, 120, RULE_ws);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(784);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,73,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(782);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case SPACE:
						{
						setState(777);
						sp();
						}
						break;
					case TAB:
						{
						setState(778);
						htab();
						}
						break;
					case CR:
						{
						setState(779);
						cr();
						}
						break;
					case LF:
						{
						setState(780);
						lf();
						}
						break;
					case SLASH:
						{
						setState(781);
						comment();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(786);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,73,_ctx);
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
		enterRule(_localctx, 122, RULE_mws);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(792); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					setState(792);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case SPACE:
						{
						setState(787);
						sp();
						}
						break;
					case TAB:
						{
						setState(788);
						htab();
						}
						break;
					case CR:
						{
						setState(789);
						cr();
						}
						break;
					case LF:
						{
						setState(790);
						lf();
						}
						break;
					case SLASH:
						{
						setState(791);
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
				setState(794); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,75,_ctx);
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
		enterRule(_localctx, 124, RULE_comment);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(796);
			match(SLASH);
			setState(797);
			match(ASTERISK);
			}
			setState(803);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,77,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(801);
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
						setState(799);
						nonstarchar();
						}
						break;
					case ASTERISK:
						{
						setState(800);
						starwithnonfslash();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(805);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,77,_ctx);
			}
			{
			setState(806);
			match(ASTERISK);
			setState(807);
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
		enterRule(_localctx, 126, RULE_nonstarchar);
		int _la;
		try {
			setState(816);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SPACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(809);
				sp();
				}
				break;
			case TAB:
				enterOuterAlt(_localctx, 2);
				{
				setState(810);
				htab();
				}
				break;
			case CR:
				enterOuterAlt(_localctx, 3);
				{
				setState(811);
				cr();
				}
				break;
			case LF:
				enterOuterAlt(_localctx, 4);
				{
				setState(812);
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
				setState(813);
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
				setState(814);
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
				setState(815);
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
		enterRule(_localctx, 128, RULE_nonspacechar);
		int _la;
		try {
			setState(821);
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
				setState(818);
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
				setState(819);
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
				setState(820);
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
		enterRule(_localctx, 130, RULE_starwithnonfslash);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(823);
			match(ASTERISK);
			setState(824);
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
		enterRule(_localctx, 132, RULE_nonfslash);
		int _la;
		try {
			setState(833);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SPACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(826);
				sp();
				}
				break;
			case TAB:
				enterOuterAlt(_localctx, 2);
				{
				setState(827);
				htab();
				}
				break;
			case CR:
				enterOuterAlt(_localctx, 3);
				{
				setState(828);
				cr();
				}
				break;
			case LF:
				enterOuterAlt(_localctx, 4);
				{
				setState(829);
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
				setState(830);
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
				setState(831);
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
				setState(832);
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
		enterRule(_localctx, 134, RULE_sp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(835);
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
		enterRule(_localctx, 136, RULE_htab);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(837);
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
		enterRule(_localctx, 138, RULE_cr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(839);
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
		enterRule(_localctx, 140, RULE_lf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(841);
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
		enterRule(_localctx, 142, RULE_qm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(843);
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
		enterRule(_localctx, 144, RULE_bs);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(845);
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
		enterRule(_localctx, 146, RULE_digit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(847);
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
		enterRule(_localctx, 148, RULE_zero);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(849);
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
		enterRule(_localctx, 150, RULE_digitnonzero);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(851);
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
		enterRule(_localctx, 152, RULE_nonwsnonpipe);
		int _la;
		try {
			setState(856);
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
				setState(853);
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
				setState(854);
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
				setState(855);
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
		enterRule(_localctx, 154, RULE_anynonescapedchar);
		int _la;
		try {
			setState(866);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,82,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(858);
				sp();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(859);
				htab();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(860);
				cr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(861);
				lf();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(862);
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
				setState(863);
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
				setState(864);
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
				setState(865);
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
		enterRule(_localctx, 156, RULE_escapedchar);
		try {
			setState(874);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,83,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(868);
				bs();
				setState(869);
				qm();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(871);
				bs();
				setState(872);
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
		"\u0004\u0001c\u036d\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
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
		"F\u0007F\u0002G\u0007G\u0002H\u0007H\u0002I\u0007I\u0002J\u0007J\u0002"+
		"K\u0007K\u0002L\u0007L\u0002M\u0007M\u0002N\u0007N\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0003\u0001\u00a9\b\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0002\u0001\u0002\u0003\u0002\u00af\b\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0003\u0003\u00b9\b\u0003\u0001\u0004\u0001\u0004\u0003\u0004\u00bd\b"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003"+
		"\u0004\u00c4\b\u0004\u0004\u0004\u00c6\b\u0004\u000b\u0004\f\u0004\u00c7"+
		"\u0001\u0005\u0001\u0005\u0003\u0005\u00cc\b\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u00d3\b\u0005\u0004\u0005"+
		"\u00d5\b\u0005\u000b\u0005\f\u0005\u00d6\u0001\u0006\u0001\u0006\u0003"+
		"\u0006\u00db\b\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0003\u0006\u00e2\b\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0003\u0007\u00e8\b\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0004\b\u00f1\b\b\u000b\b\f\b\u00f2\u0001\t"+
		"\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0003\n\u00fc\b\n\u0001"+
		"\n\u0001\n\u0001\n\u0003\n\u0101\b\n\u0001\n\u0001\n\u0001\u000b\u0001"+
		"\u000b\u0003\u000b\u0107\b\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0003\u000e\u0115\b\u000e\u0001\u000f\u0001\u000f\u0001"+
		"\u0010\u0004\u0010\u011a\b\u0010\u000b\u0010\f\u0010\u011b\u0001\u0010"+
		"\u0004\u0010\u011f\b\u0010\u000b\u0010\f\u0010\u0120\u0001\u0010\u0004"+
		"\u0010\u0124\b\u0010\u000b\u0010\f\u0010\u0125\u0005\u0010\u0128\b\u0010"+
		"\n\u0010\f\u0010\u012b\t\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u0135"+
		"\b\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0003\u0013\u0144\b\u0013\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0003"+
		"\u0014\u015a\b\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u0165"+
		"\b\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003"+
		"\u0016\u0172\b\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0003\u0017\u0186\b\u0017\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0003\u0018\u0192\b\u0018\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u0199\b\u0019\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001c\u0001\u001c\u0003"+
		"\u001c\u01a8\b\u001c\u0001\u001d\u0001\u001d\u0003\u001d\u01ac\b\u001d"+
		"\u0001\u001e\u0001\u001e\u0003\u001e\u01b0\b\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0003\u001e\u01b7\b\u001e\u0004\u001e"+
		"\u01b9\b\u001e\u000b\u001e\f\u001e\u01ba\u0001\u001f\u0001\u001f\u0003"+
		"\u001f\u01bf\b\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001"+
		"\u001f\u0003\u001f\u01c6\b\u001f\u0004\u001f\u01c8\b\u001f\u000b\u001f"+
		"\f\u001f\u01c9\u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001!\u0001"+
		"!\u0001!\u0001!\u0003!\u01d6\b!\u0001\"\u0001\"\u0001\"\u0001\"\u0001"+
		"\"\u0001\"\u0001#\u0001#\u0003#\u01e0\b#\u0001$\u0001$\u0003$\u01e4\b"+
		"$\u0001$\u0001$\u0001$\u0001$\u0001$\u0003$\u01eb\b$\u0004$\u01ed\b$\u000b"+
		"$\f$\u01ee\u0001%\u0001%\u0003%\u01f3\b%\u0001%\u0001%\u0001%\u0001%\u0001"+
		"%\u0003%\u01fa\b%\u0004%\u01fc\b%\u000b%\f%\u01fd\u0001&\u0001&\u0001"+
		"&\u0001&\u0001&\u0001&\u0001\'\u0001\'\u0003\'\u0208\b\'\u0001(\u0003"+
		"(\u020b\b(\u0001(\u0001(\u0001(\u0001(\u0003(\u0211\b(\u0001(\u0001(\u0001"+
		"(\u0001)\u0003)\u0217\b)\u0001)\u0001)\u0001)\u0003)\u021c\b)\u0001)\u0001"+
		")\u0001)\u0001)\u0001)\u0003)\u0223\b)\u0001*\u0001*\u0001*\u0001*\u0001"+
		"*\u0001*\u0001+\u0001+\u0001+\u0001+\u0001+\u0001,\u0001,\u0001,\u0001"+
		",\u0003,\u0234\b,\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0001.\u0001"+
		".\u0001/\u0001/\u0001/\u0001/\u0001/\u0001/\u0001/\u0003/\u0245\b/\u0001"+
		"0\u00010\u00030\u0249\b0\u00011\u00011\u00011\u00011\u00011\u00031\u0250"+
		"\b1\u00012\u00012\u00012\u00012\u00012\u00012\u00012\u00012\u00012\u0001"+
		"2\u00012\u00032\u025d\b2\u00013\u00013\u00013\u00033\u0262\b3\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00034\u026d\b4\u0001"+
		"5\u00015\u00015\u00035\u0272\b5\u00016\u00036\u0275\b6\u00016\u00016\u0003"+
		"6\u0279\b6\u00017\u00017\u00047\u027d\b7\u000b7\f7\u027e\u00018\u0001"+
		"8\u00058\u0283\b8\n8\f8\u0286\t8\u00018\u00038\u0289\b8\u00019\u00019"+
		"\u00019\u00049\u028e\b9\u000b9\f9\u028f\u0001:\u0001:\u0005:\u0294\b:"+
		"\n:\f:\u0297\t:\u0001:\u0003:\u029a\b:\u0001;\u0001;\u0001;\u0001;\u0001"+
		";\u0004;\u02a1\b;\u000b;\f;\u02a2\u0001;\u0001;\u0001;\u0001;\u0001;\u0001"+
		";\u0001;\u0003;\u02ac\b;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001"+
		";\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001"+
		";\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001"+
		";\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001"+
		";\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001"+
		";\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001"+
		";\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001"+
		";\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001"+
		";\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001;\u0001"+
		";\u0001;\u0003;\u0306\b;\u0003;\u0308\b;\u0001<\u0001<\u0001<\u0001<\u0001"+
		"<\u0005<\u030f\b<\n<\f<\u0312\t<\u0001=\u0001=\u0001=\u0001=\u0001=\u0004"+
		"=\u0319\b=\u000b=\f=\u031a\u0001>\u0001>\u0001>\u0001>\u0001>\u0005>\u0322"+
		"\b>\n>\f>\u0325\t>\u0001>\u0001>\u0001>\u0001?\u0001?\u0001?\u0001?\u0001"+
		"?\u0001?\u0001?\u0003?\u0331\b?\u0001@\u0001@\u0001@\u0003@\u0336\b@\u0001"+
		"A\u0001A\u0001A\u0001B\u0001B\u0001B\u0001B\u0001B\u0001B\u0001B\u0003"+
		"B\u0342\bB\u0001C\u0001C\u0001D\u0001D\u0001E\u0001E\u0001F\u0001F\u0001"+
		"G\u0001G\u0001H\u0001H\u0001I\u0001I\u0001J\u0001J\u0001K\u0001K\u0001"+
		"L\u0001L\u0001L\u0003L\u0359\bL\u0001M\u0001M\u0001M\u0001M\u0001M\u0001"+
		"M\u0001M\u0001M\u0003M\u0363\bM\u0001N\u0001N\u0001N\u0001N\u0001N\u0001"+
		"N\u0003N\u036b\bN\u0001N\u0000\u0000O\u0000\u0002\u0004\u0006\b\n\f\u000e"+
		"\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDF"+
		"HJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c"+
		"\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u0000\u001f\u0002\u0000"+
		"))II\u0002\u0000**JJ\u0002\u000088XX\u0002\u0000((HH\u0002\u000033SS\u0002"+
		"\u0000&&FF\u0002\u000099YY\u0002\u000044TT\u0002\u0000++KK\u0002\u0000"+
		"77WW\u0002\u000011QQ\u0002\u0000--MM\u0002\u0000..NN\u0002\u000055UU\u0002"+
		"\u000022RR\u0002\u0000::ZZ\u0002\u0000>>^^\u0002\u0000;;[[\u0002\u0000"+
		"\u0010\u0010\u0012\u0012\u0001\u0000\u0006\u000e\u0001\u0000\u0010c\u0001"+
		"\u0000\b\u000e\u0001\u0000\u0006\u0013\u0001\u0000\u0015c\u0001\u0000"+
		"\u0015\u001e\u0001\u0000\u0016\u001e\u0001\u0000\u0006`\u0001\u0000bc"+
		"\u0001\u0000\u0005\u0006\u0001\u0000\b@\u0001\u0000Bc\u03a1\u0000\u009e"+
		"\u0001\u0000\u0000\u0000\u0002\u00a3\u0001\u0000\u0000\u0000\u0004\u00ae"+
		"\u0001\u0000\u0000\u0000\u0006\u00b8\u0001\u0000\u0000\u0000\b\u00bc\u0001"+
		"\u0000\u0000\u0000\n\u00cb\u0001\u0000\u0000\u0000\f\u00da\u0001\u0000"+
		"\u0000\u0000\u000e\u00e3\u0001\u0000\u0000\u0000\u0010\u00ec\u0001\u0000"+
		"\u0000\u0000\u0012\u00f4\u0001\u0000\u0000\u0000\u0014\u00fb\u0001\u0000"+
		"\u0000\u0000\u0016\u0106\u0001\u0000\u0000\u0000\u0018\u0108\u0001\u0000"+
		"\u0000\u0000\u001a\u010a\u0001\u0000\u0000\u0000\u001c\u010c\u0001\u0000"+
		"\u0000\u0000\u001e\u0116\u0001\u0000\u0000\u0000 \u0119\u0001\u0000\u0000"+
		"\u0000\"\u012c\u0001\u0000\u0000\u0000$\u0134\u0001\u0000\u0000\u0000"+
		"&\u0143\u0001\u0000\u0000\u0000(\u0159\u0001\u0000\u0000\u0000*\u0164"+
		"\u0001\u0000\u0000\u0000,\u0171\u0001\u0000\u0000\u0000.\u0185\u0001\u0000"+
		"\u0000\u00000\u0191\u0001\u0000\u0000\u00002\u0198\u0001\u0000\u0000\u0000"+
		"4\u019a\u0001\u0000\u0000\u00006\u019e\u0001\u0000\u0000\u00008\u01a7"+
		"\u0001\u0000\u0000\u0000:\u01ab\u0001\u0000\u0000\u0000<\u01af\u0001\u0000"+
		"\u0000\u0000>\u01be\u0001\u0000\u0000\u0000@\u01cb\u0001\u0000\u0000\u0000"+
		"B\u01d5\u0001\u0000\u0000\u0000D\u01d7\u0001\u0000\u0000\u0000F\u01df"+
		"\u0001\u0000\u0000\u0000H\u01e3\u0001\u0000\u0000\u0000J\u01f2\u0001\u0000"+
		"\u0000\u0000L\u01ff\u0001\u0000\u0000\u0000N\u0207\u0001\u0000\u0000\u0000"+
		"P\u020a\u0001\u0000\u0000\u0000R\u0216\u0001\u0000\u0000\u0000T\u0224"+
		"\u0001\u0000\u0000\u0000V\u022a\u0001\u0000\u0000\u0000X\u022f\u0001\u0000"+
		"\u0000\u0000Z\u0235\u0001\u0000\u0000\u0000\\\u023b\u0001\u0000\u0000"+
		"\u0000^\u0244\u0001\u0000\u0000\u0000`\u0248\u0001\u0000\u0000\u0000b"+
		"\u024f\u0001\u0000\u0000\u0000d\u025c\u0001\u0000\u0000\u0000f\u0261\u0001"+
		"\u0000\u0000\u0000h\u026c\u0001\u0000\u0000\u0000j\u0271\u0001\u0000\u0000"+
		"\u0000l\u0274\u0001\u0000\u0000\u0000n\u027c\u0001\u0000\u0000\u0000p"+
		"\u0288\u0001\u0000\u0000\u0000r\u028a\u0001\u0000\u0000\u0000t\u0299\u0001"+
		"\u0000\u0000\u0000v\u0307\u0001\u0000\u0000\u0000x\u0310\u0001\u0000\u0000"+
		"\u0000z\u0318\u0001\u0000\u0000\u0000|\u031c\u0001\u0000\u0000\u0000~"+
		"\u0330\u0001\u0000\u0000\u0000\u0080\u0335\u0001\u0000\u0000\u0000\u0082"+
		"\u0337\u0001\u0000\u0000\u0000\u0084\u0341\u0001\u0000\u0000\u0000\u0086"+
		"\u0343\u0001\u0000\u0000\u0000\u0088\u0345\u0001\u0000\u0000\u0000\u008a"+
		"\u0347\u0001\u0000\u0000\u0000\u008c\u0349\u0001\u0000\u0000\u0000\u008e"+
		"\u034b\u0001\u0000\u0000\u0000\u0090\u034d\u0001\u0000\u0000\u0000\u0092"+
		"\u034f\u0001\u0000\u0000\u0000\u0094\u0351\u0001\u0000\u0000\u0000\u0096"+
		"\u0353\u0001\u0000\u0000\u0000\u0098\u0358\u0001\u0000\u0000\u0000\u009a"+
		"\u0362\u0001\u0000\u0000\u0000\u009c\u036a\u0001\u0000\u0000\u0000\u009e"+
		"\u009f\u0003x<\u0000\u009f\u00a0\u0003\u0002\u0001\u0000\u00a0\u00a1\u0003"+
		"x<\u0000\u00a1\u00a2\u0005\u0000\u0000\u0001\u00a2\u0001\u0001\u0000\u0000"+
		"\u0000\u00a3\u00a8\u0003x<\u0000\u00a4\u00a9\u0003\u0004\u0002\u0000\u00a5"+
		"\u00a9\u0003\u0006\u0003\u0000\u00a6\u00a9\u0003\u0010\b\u0000\u00a7\u00a9"+
		"\u0003\u0014\n\u0000\u00a8\u00a4\u0001\u0000\u0000\u0000\u00a8\u00a5\u0001"+
		"\u0000\u0000\u0000\u00a8\u00a6\u0001\u0000\u0000\u0000\u00a8\u00a7\u0001"+
		"\u0000\u0000\u0000\u00a9\u00aa\u0001\u0000\u0000\u0000\u00aa\u00ab\u0003"+
		"x<\u0000\u00ab\u0003\u0001\u0000\u0000\u0000\u00ac\u00af\u0003\u0014\n"+
		"\u0000\u00ad\u00af\u0003\u000e\u0007\u0000\u00ae\u00ac\u0001\u0000\u0000"+
		"\u0000\u00ae\u00ad\u0001\u0000\u0000\u0000\u00af\u00b0\u0001\u0000\u0000"+
		"\u0000\u00b0\u00b1\u0003x<\u0000\u00b1\u00b2\u0005\u001f\u0000\u0000\u00b2"+
		"\u00b3\u0003x<\u0000\u00b3\u00b4\u00038\u001c\u0000\u00b4\u0005\u0001"+
		"\u0000\u0000\u0000\u00b5\u00b9\u0003\b\u0004\u0000\u00b6\u00b9\u0003\n"+
		"\u0005\u0000\u00b7\u00b9\u0003\f\u0006\u0000\u00b8\u00b5\u0001\u0000\u0000"+
		"\u0000\u00b8\u00b6\u0001\u0000\u0000\u0000\u00b8\u00b7\u0001\u0000\u0000"+
		"\u0000\u00b9\u0007\u0001\u0000\u0000\u0000\u00ba\u00bd\u0003\u0014\n\u0000"+
		"\u00bb\u00bd\u0003\u000e\u0007\u0000\u00bc\u00ba\u0001\u0000\u0000\u0000"+
		"\u00bc\u00bb\u0001\u0000\u0000\u0000\u00bd\u00c5\u0001\u0000\u0000\u0000"+
		"\u00be\u00bf\u0003x<\u0000\u00bf\u00c0\u00032\u0019\u0000\u00c0\u00c3"+
		"\u0003x<\u0000\u00c1\u00c4\u0003\u0014\n\u0000\u00c2\u00c4\u0003\u000e"+
		"\u0007\u0000\u00c3\u00c1\u0001\u0000\u0000\u0000\u00c3\u00c2\u0001\u0000"+
		"\u0000\u0000\u00c4\u00c6\u0001\u0000\u0000\u0000\u00c5\u00be\u0001\u0000"+
		"\u0000\u0000\u00c6\u00c7\u0001\u0000\u0000\u0000\u00c7\u00c5\u0001\u0000"+
		"\u0000\u0000\u00c7\u00c8\u0001\u0000\u0000\u0000\u00c8\t\u0001\u0000\u0000"+
		"\u0000\u00c9\u00cc\u0003\u0014\n\u0000\u00ca\u00cc\u0003\u000e\u0007\u0000"+
		"\u00cb\u00c9\u0001\u0000\u0000\u0000\u00cb\u00ca\u0001\u0000\u0000\u0000"+
		"\u00cc\u00d4\u0001\u0000\u0000\u0000\u00cd\u00ce\u0003x<\u0000\u00ce\u00cf"+
		"\u00034\u001a\u0000\u00cf\u00d2\u0003x<\u0000\u00d0\u00d3\u0003\u0014"+
		"\n\u0000\u00d1\u00d3\u0003\u000e\u0007\u0000\u00d2\u00d0\u0001\u0000\u0000"+
		"\u0000\u00d2\u00d1\u0001\u0000\u0000\u0000\u00d3\u00d5\u0001\u0000\u0000"+
		"\u0000\u00d4\u00cd\u0001\u0000\u0000\u0000\u00d5\u00d6\u0001\u0000\u0000"+
		"\u0000\u00d6\u00d4\u0001\u0000\u0000\u0000\u00d6\u00d7\u0001\u0000\u0000"+
		"\u0000\u00d7\u000b\u0001\u0000\u0000\u0000\u00d8\u00db\u0003\u0014\n\u0000"+
		"\u00d9\u00db\u0003\u000e\u0007\u0000\u00da\u00d8\u0001\u0000\u0000\u0000"+
		"\u00da\u00d9\u0001\u0000\u0000\u0000\u00db\u00dc\u0001\u0000\u0000\u0000"+
		"\u00dc\u00dd\u0003x<\u0000\u00dd\u00de\u00036\u001b\u0000\u00de\u00e1"+
		"\u0003x<\u0000\u00df\u00e2\u0003\u0014\n\u0000\u00e0\u00e2\u0003\u000e"+
		"\u0007\u0000\u00e1\u00df\u0001\u0000\u0000\u0000\u00e1\u00e0\u0001\u0000"+
		"\u0000\u0000\u00e2\r\u0001\u0000\u0000\u0000\u00e3\u00e4\u0005\r\u0000"+
		"\u0000\u00e4\u00e7\u0003x<\u0000\u00e5\u00e8\u0003\u0004\u0002\u0000\u00e6"+
		"\u00e8\u0003\u0006\u0003\u0000\u00e7\u00e5\u0001\u0000\u0000\u0000\u00e7"+
		"\u00e6\u0001\u0000\u0000\u0000\u00e8\u00e9\u0001\u0000\u0000\u0000\u00e9"+
		"\u00ea\u0003x<\u0000\u00ea\u00eb\u0005\u000e\u0000\u0000\u00eb\u000f\u0001"+
		"\u0000\u0000\u0000\u00ec\u00f0\u0003\u0014\n\u0000\u00ed\u00ee\u0003x"+
		"<\u0000\u00ee\u00ef\u0003\u0012\t\u0000\u00ef\u00f1\u0001\u0000\u0000"+
		"\u0000\u00f0\u00ed\u0001\u0000\u0000\u0000\u00f1\u00f2\u0001\u0000\u0000"+
		"\u0000\u00f2\u00f0\u0001\u0000\u0000\u0000\u00f2\u00f3\u0001\u0000\u0000"+
		"\u0000\u00f3\u0011\u0001\u0000\u0000\u0000\u00f4\u00f5\u0003\u0018\f\u0000"+
		"\u00f5\u00f6\u0003x<\u0000\u00f6\u00f7\u0003\u0014\n\u0000\u00f7\u0013"+
		"\u0001\u0000\u0000\u0000\u00f8\u00f9\u0003$\u0012\u0000\u00f9\u00fa\u0003"+
		"x<\u0000\u00fa\u00fc\u0001\u0000\u0000\u0000\u00fb\u00f8\u0001\u0000\u0000"+
		"\u0000\u00fb\u00fc\u0001\u0000\u0000\u0000\u00fc\u0100\u0001\u0000\u0000"+
		"\u0000\u00fd\u00fe\u0003\u001a\r\u0000\u00fe\u00ff\u0003x<\u0000\u00ff"+
		"\u0101\u0001\u0000\u0000\u0000\u0100\u00fd\u0001\u0000\u0000\u0000\u0100"+
		"\u0101\u0001\u0000\u0000\u0000\u0101\u0102\u0001\u0000\u0000\u0000\u0102"+
		"\u0103\u0003\u0016\u000b\u0000\u0103\u0015\u0001\u0000\u0000\u0000\u0104"+
		"\u0107\u0003\u001c\u000e\u0000\u0105\u0107\u0003\"\u0011\u0000\u0106\u0104"+
		"\u0001\u0000\u0000\u0000\u0106\u0105\u0001\u0000\u0000\u0000\u0107\u0017"+
		"\u0001\u0000\u0000\u0000\u0108\u0109\u0005\u0013\u0000\u0000\u0109\u0019"+
		"\u0001\u0000\u0000\u0000\u010a\u010b\u0005C\u0000\u0000\u010b\u001b\u0001"+
		"\u0000\u0000\u0000\u010c\u0114\u0003\u001e\u000f\u0000\u010d\u010e\u0003"+
		"x<\u0000\u010e\u010f\u0005a\u0000\u0000\u010f\u0110\u0003x<\u0000\u0110"+
		"\u0111\u0003 \u0010\u0000\u0111\u0112\u0003x<\u0000\u0112\u0113\u0005"+
		"a\u0000\u0000\u0113\u0115\u0001\u0000\u0000\u0000\u0114\u010d\u0001\u0000"+
		"\u0000\u0000\u0114\u0115\u0001\u0000\u0000\u0000\u0115\u001d\u0001\u0000"+
		"\u0000\u0000\u0116\u0117\u0003v;\u0000\u0117\u001f\u0001\u0000\u0000\u0000"+
		"\u0118\u011a\u0003\u0098L\u0000\u0119\u0118\u0001\u0000\u0000\u0000\u011a"+
		"\u011b\u0001\u0000\u0000\u0000\u011b\u0119\u0001\u0000\u0000\u0000\u011b"+
		"\u011c\u0001\u0000\u0000\u0000\u011c\u0129\u0001\u0000\u0000\u0000\u011d"+
		"\u011f\u0003\u0086C\u0000\u011e\u011d\u0001\u0000\u0000\u0000\u011f\u0120"+
		"\u0001\u0000\u0000\u0000\u0120\u011e\u0001\u0000\u0000\u0000\u0120\u0121"+
		"\u0001\u0000\u0000\u0000\u0121\u0123\u0001\u0000\u0000\u0000\u0122\u0124"+
		"\u0003\u0098L\u0000\u0123\u0122\u0001\u0000\u0000\u0000\u0124\u0125\u0001"+
		"\u0000\u0000\u0000\u0125\u0123\u0001\u0000\u0000\u0000\u0125\u0126\u0001"+
		"\u0000\u0000\u0000\u0126\u0128\u0001\u0000\u0000\u0000\u0127\u011e\u0001"+
		"\u0000\u0000\u0000\u0128\u012b\u0001\u0000\u0000\u0000\u0129\u0127\u0001"+
		"\u0000\u0000\u0000\u0129\u012a\u0001\u0000\u0000\u0000\u012a!\u0001\u0000"+
		"\u0000\u0000\u012b\u0129\u0001\u0000\u0000\u0000\u012c\u012d\u0005\u000f"+
		"\u0000\u0000\u012d#\u0001\u0000\u0000\u0000\u012e\u0135\u0003*\u0015\u0000"+
		"\u012f\u0135\u0003(\u0014\u0000\u0130\u0135\u0003&\u0013\u0000\u0131\u0135"+
		"\u00030\u0018\u0000\u0132\u0135\u0003.\u0017\u0000\u0133\u0135\u0003,"+
		"\u0016\u0000\u0134\u012e\u0001\u0000\u0000\u0000\u0134\u012f\u0001\u0000"+
		"\u0000\u0000\u0134\u0130\u0001\u0000\u0000\u0000\u0134\u0131\u0001\u0000"+
		"\u0000\u0000\u0134\u0132\u0001\u0000\u0000\u0000\u0134\u0133\u0001\u0000"+
		"\u0000\u0000\u0135%\u0001\u0000\u0000\u0000\u0136\u0144\u0005!\u0000\u0000"+
		"\u0137\u0138\u0007\u0000\u0000\u0000\u0138\u0139\u0007\u0001\u0000\u0000"+
		"\u0139\u013a\u0007\u0002\u0000\u0000\u013a\u013b\u0007\u0003\u0000\u0000"+
		"\u013b\u013c\u0007\u0001\u0000\u0000\u013c\u013d\u0007\u0004\u0000\u0000"+
		"\u013d\u013e\u0007\u0000\u0000\u0000\u013e\u013f\u0007\u0005\u0000\u0000"+
		"\u013f\u0140\u0007\u0004\u0000\u0000\u0140\u0141\u0007\u0006\u0000\u0000"+
		"\u0141\u0142\u0007\u0007\u0000\u0000\u0142\u0144\u0007\b\u0000\u0000\u0143"+
		"\u0136\u0001\u0000\u0000\u0000\u0143\u0137\u0001\u0000\u0000\u0000\u0144"+
		"\'\u0001\u0000\u0000\u0000\u0145\u0146\u0005!\u0000\u0000\u0146\u015a"+
		"\u0005!\u0000\u0000\u0147\u0148\u0007\u0000\u0000\u0000\u0148\u0149\u0007"+
		"\u0001\u0000\u0000\u0149\u014a\u0007\u0002\u0000\u0000\u014a\u014b\u0007"+
		"\u0003\u0000\u0000\u014b\u014c\u0007\u0001\u0000\u0000\u014c\u014d\u0007"+
		"\u0004\u0000\u0000\u014d\u014e\u0007\u0000\u0000\u0000\u014e\u014f\u0007"+
		"\u0005\u0000\u0000\u014f\u0150\u0007\u0004\u0000\u0000\u0150\u0151\u0007"+
		"\u0006\u0000\u0000\u0151\u0152\u0007\u0007\u0000\u0000\u0152\u0153\u0007"+
		"\t\u0000\u0000\u0153\u0154\u0007\u0002\u0000\u0000\u0154\u0155\u0007\u0001"+
		"\u0000\u0000\u0155\u0156\u0007\n\u0000\u0000\u0156\u0157\u0007\b\u0000"+
		"\u0000\u0157\u0158\u0007\u0007\u0000\u0000\u0158\u015a\u0007\b\u0000\u0000"+
		"\u0159\u0145\u0001\u0000\u0000\u0000\u0159\u0147\u0001\u0000\u0000\u0000"+
		"\u015a)\u0001\u0000\u0000\u0000\u015b\u015c\u0005!\u0000\u0000\u015c\u0165"+
		"\u0005\u0006\u0000\u0000\u015d\u015e\u0007\u0003\u0000\u0000\u015e\u015f"+
		"\u0007\u000b\u0000\u0000\u015f\u0160\u0007\f\u0000\u0000\u0160\u0161\u0007"+
		"\n\u0000\u0000\u0161\u0162\u0007\u0000\u0000\u0000\u0162\u0163\u0007\u0007"+
		"\u0000\u0000\u0163\u0165\u0007\b\u0000\u0000\u0164\u015b\u0001\u0000\u0000"+
		"\u0000\u0164\u015d\u0001\u0000\u0000\u0000\u0165+\u0001\u0000\u0000\u0000"+
		"\u0166\u0172\u0005#\u0000\u0000\u0167\u0168\u0007\u0005\u0000\u0000\u0168"+
		"\u0169\u0007\u0004\u0000\u0000\u0169\u016a\u0007\u0003\u0000\u0000\u016a"+
		"\u016b\u0007\u0001\u0000\u0000\u016b\u016c\u0007\u0002\u0000\u0000\u016c"+
		"\u016d\u0007\u0006\u0000\u0000\u016d\u016e\u0007\u0007\u0000\u0000\u016e"+
		"\u016f\u0007\t\u0000\u0000\u016f\u0170\u0007\u0007\u0000\u0000\u0170\u0172"+
		"\u0007\b\u0000\u0000\u0171\u0166\u0001\u0000\u0000\u0000\u0171\u0167\u0001"+
		"\u0000\u0000\u0000\u0172-\u0001\u0000\u0000\u0000\u0173\u0174\u0005#\u0000"+
		"\u0000\u0174\u0186\u0005#\u0000\u0000\u0175\u0176\u0007\u0005\u0000\u0000"+
		"\u0176\u0177\u0007\u0004\u0000\u0000\u0177\u0178\u0007\u0003\u0000\u0000"+
		"\u0178\u0179\u0007\u0001\u0000\u0000\u0179\u017a\u0007\u0002\u0000\u0000"+
		"\u017a\u017b\u0007\u0006\u0000\u0000\u017b\u017c\u0007\u0007\u0000\u0000"+
		"\u017c\u017d\u0007\t\u0000\u0000\u017d\u017e\u0007\u0007\u0000\u0000\u017e"+
		"\u017f\u0007\t\u0000\u0000\u017f\u0180\u0007\u0002\u0000\u0000\u0180\u0181"+
		"\u0007\u0001\u0000\u0000\u0181\u0182\u0007\n\u0000\u0000\u0182\u0183\u0007"+
		"\b\u0000\u0000\u0183\u0184\u0007\u0007\u0000\u0000\u0184\u0186\u0007\b"+
		"\u0000\u0000\u0185\u0173\u0001\u0000\u0000\u0000\u0185\u0175\u0001\u0000"+
		"\u0000\u0000\u0186/\u0001\u0000\u0000\u0000\u0187\u0188\u0005#\u0000\u0000"+
		"\u0188\u0192\u0005\u0006\u0000\u0000\u0189\u018a\u0007\r\u0000\u0000\u018a"+
		"\u018b\u0007\u0005\u0000\u0000\u018b\u018c\u0007\t\u0000\u0000\u018c\u018d"+
		"\u0007\u0001\u0000\u0000\u018d\u018e\u0007\u0004\u0000\u0000\u018e\u018f"+
		"\u0007\u0006\u0000\u0000\u018f\u0190\u0007\u0007\u0000\u0000\u0190\u0192"+
		"\u0007\b\u0000\u0000\u0191\u0187\u0001\u0000\u0000\u0000\u0191\u0189\u0001"+
		"\u0000\u0000\u0000\u01921\u0001\u0000\u0000\u0000\u0193\u0194\u0007\u0005"+
		"\u0000\u0000\u0194\u0195\u0007\u0004\u0000\u0000\u0195\u0196\u0007\u0000"+
		"\u0000\u0000\u0196\u0199\u0003z=\u0000\u0197\u0199\u0005\u0011\u0000\u0000"+
		"\u0198\u0193\u0001\u0000\u0000\u0000\u0198\u0197\u0001\u0000\u0000\u0000"+
		"\u01993\u0001\u0000\u0000\u0000\u019a\u019b\u0007\u0007\u0000\u0000\u019b"+
		"\u019c\u0007\t\u0000\u0000\u019c\u019d\u0003z=\u0000\u019d5\u0001\u0000"+
		"\u0000\u0000\u019e\u019f\u0007\u000e\u0000\u0000\u019f\u01a0\u0007\f\u0000"+
		"\u0000\u01a0\u01a1\u0007\u0004\u0000\u0000\u01a1\u01a2\u0007\u000f\u0000"+
		"\u0000\u01a2\u01a3\u0007\u0002\u0000\u0000\u01a3\u01a4\u0003z=\u0000\u01a4"+
		"7\u0001\u0000\u0000\u0000\u01a5\u01a8\u0003B!\u0000\u01a6\u01a8\u0003"+
		":\u001d\u0000\u01a7\u01a5\u0001\u0000\u0000\u0000\u01a7\u01a6\u0001\u0000"+
		"\u0000\u0000\u01a89\u0001\u0000\u0000\u0000\u01a9\u01ac\u0003<\u001e\u0000"+
		"\u01aa\u01ac\u0003>\u001f\u0000\u01ab\u01a9\u0001\u0000\u0000\u0000\u01ab"+
		"\u01aa\u0001\u0000\u0000\u0000\u01ac;\u0001\u0000\u0000\u0000\u01ad\u01b0"+
		"\u0003B!\u0000\u01ae\u01b0\u0003@ \u0000\u01af\u01ad\u0001\u0000\u0000"+
		"\u0000\u01af\u01ae\u0001\u0000\u0000\u0000\u01b0\u01b8\u0001\u0000\u0000"+
		"\u0000\u01b1\u01b2\u0003x<\u0000\u01b2\u01b3\u00032\u0019\u0000\u01b3"+
		"\u01b6\u0003x<\u0000\u01b4\u01b7\u0003B!\u0000\u01b5\u01b7\u0003@ \u0000"+
		"\u01b6\u01b4\u0001\u0000\u0000\u0000\u01b6\u01b5\u0001\u0000\u0000\u0000"+
		"\u01b7\u01b9\u0001\u0000\u0000\u0000\u01b8\u01b1\u0001\u0000\u0000\u0000"+
		"\u01b9\u01ba\u0001\u0000\u0000\u0000\u01ba\u01b8\u0001\u0000\u0000\u0000"+
		"\u01ba\u01bb\u0001\u0000\u0000\u0000\u01bb=\u0001\u0000\u0000\u0000\u01bc"+
		"\u01bf\u0003B!\u0000\u01bd\u01bf\u0003@ \u0000\u01be\u01bc\u0001\u0000"+
		"\u0000\u0000\u01be\u01bd\u0001\u0000\u0000\u0000\u01bf\u01c7\u0001\u0000"+
		"\u0000\u0000\u01c0\u01c1\u0003x<\u0000\u01c1\u01c2\u00034\u001a\u0000"+
		"\u01c2\u01c5\u0003x<\u0000\u01c3\u01c6\u0003B!\u0000\u01c4\u01c6\u0003"+
		"@ \u0000\u01c5\u01c3\u0001\u0000\u0000\u0000\u01c5\u01c4\u0001\u0000\u0000"+
		"\u0000\u01c6\u01c8\u0001\u0000\u0000\u0000\u01c7\u01c0\u0001\u0000\u0000"+
		"\u0000\u01c8\u01c9\u0001\u0000\u0000\u0000\u01c9\u01c7\u0001\u0000\u0000"+
		"\u0000\u01c9\u01ca\u0001\u0000\u0000\u0000\u01ca?\u0001\u0000\u0000\u0000"+
		"\u01cb\u01cc\u0005\r\u0000\u0000\u01cc\u01cd\u0003x<\u0000\u01cd\u01ce"+
		"\u0003:\u001d\u0000\u01ce\u01cf\u0003x<\u0000\u01cf\u01d0\u0005\u000e"+
		"\u0000\u0000\u01d0A\u0001\u0000\u0000\u0000\u01d1\u01d6\u0003F#\u0000"+
		"\u01d2\u01d6\u0003P(\u0000\u01d3\u01d6\u0003D\"\u0000\u01d4\u01d6\u0003"+
		"R)\u0000\u01d5\u01d1\u0001\u0000\u0000\u0000\u01d5\u01d2\u0001\u0000\u0000"+
		"\u0000\u01d5\u01d3\u0001\u0000\u0000\u0000\u01d5\u01d4\u0001\u0000\u0000"+
		"\u0000\u01d6C\u0001\u0000\u0000\u0000\u01d7\u01d8\u0005\r\u0000\u0000"+
		"\u01d8\u01d9\u0003x<\u0000\u01d9\u01da\u00038\u001c\u0000\u01da\u01db"+
		"\u0003x<\u0000\u01db\u01dc\u0005\u000e\u0000\u0000\u01dcE\u0001\u0000"+
		"\u0000\u0000\u01dd\u01e0\u0003H$\u0000\u01de\u01e0\u0003J%\u0000\u01df"+
		"\u01dd\u0001\u0000\u0000\u0000\u01df\u01de\u0001\u0000\u0000\u0000\u01e0"+
		"G\u0001\u0000\u0000\u0000\u01e1\u01e4\u0003N\'\u0000\u01e2\u01e4\u0003"+
		"L&\u0000\u01e3\u01e1\u0001\u0000\u0000\u0000\u01e3\u01e2\u0001\u0000\u0000"+
		"\u0000\u01e4\u01ec\u0001\u0000\u0000\u0000\u01e5\u01e6\u0003x<\u0000\u01e6"+
		"\u01e7\u00032\u0019\u0000\u01e7\u01ea\u0003x<\u0000\u01e8\u01eb\u0003"+
		"N\'\u0000\u01e9\u01eb\u0003L&\u0000\u01ea\u01e8\u0001\u0000\u0000\u0000"+
		"\u01ea\u01e9\u0001\u0000\u0000\u0000\u01eb\u01ed\u0001\u0000\u0000\u0000"+
		"\u01ec\u01e5\u0001\u0000\u0000\u0000\u01ed\u01ee\u0001\u0000\u0000\u0000"+
		"\u01ee\u01ec\u0001\u0000\u0000\u0000\u01ee\u01ef\u0001\u0000\u0000\u0000"+
		"\u01efI\u0001\u0000\u0000\u0000\u01f0\u01f3\u0003N\'\u0000\u01f1\u01f3"+
		"\u0003L&\u0000\u01f2\u01f0\u0001\u0000\u0000\u0000\u01f2\u01f1\u0001\u0000"+
		"\u0000\u0000\u01f3\u01fb\u0001\u0000\u0000\u0000\u01f4\u01f5\u0003x<\u0000"+
		"\u01f5\u01f6\u00034\u001a\u0000\u01f6\u01f9\u0003x<\u0000\u01f7\u01fa"+
		"\u0003N\'\u0000\u01f8\u01fa\u0003L&\u0000\u01f9\u01f7\u0001\u0000\u0000"+
		"\u0000\u01f9\u01f8\u0001\u0000\u0000\u0000\u01fa\u01fc\u0001\u0000\u0000"+
		"\u0000\u01fb\u01f4\u0001\u0000\u0000\u0000\u01fc\u01fd\u0001\u0000\u0000"+
		"\u0000\u01fd\u01fb\u0001\u0000\u0000\u0000\u01fd\u01fe\u0001\u0000\u0000"+
		"\u0000\u01feK\u0001\u0000\u0000\u0000\u01ff\u0200\u0005\r\u0000\u0000"+
		"\u0200\u0201\u0003x<\u0000\u0201\u0202\u0003F#\u0000\u0202\u0203\u0003"+
		"x<\u0000\u0203\u0204\u0005\u000e\u0000\u0000\u0204M\u0001\u0000\u0000"+
		"\u0000\u0205\u0208\u0003R)\u0000\u0206\u0208\u0003L&\u0000\u0207\u0205"+
		"\u0001\u0000\u0000\u0000\u0207\u0206\u0001\u0000\u0000\u0000\u0208O\u0001"+
		"\u0000\u0000\u0000\u0209\u020b\u0003Z-\u0000\u020a\u0209\u0001\u0000\u0000"+
		"\u0000\u020a\u020b\u0001\u0000\u0000\u0000\u020b\u020c\u0001\u0000\u0000"+
		"\u0000\u020c\u020d\u0005`\u0000\u0000\u020d\u0210\u0003x<\u0000\u020e"+
		"\u0211\u0003F#\u0000\u020f\u0211\u0003R)\u0000\u0210\u020e\u0001\u0000"+
		"\u0000\u0000\u0210\u020f\u0001\u0000\u0000\u0000\u0211\u0212\u0001\u0000"+
		"\u0000\u0000\u0212\u0213\u0003x<\u0000\u0213\u0214\u0005b\u0000\u0000"+
		"\u0214Q\u0001\u0000\u0000\u0000\u0215\u0217\u0003Z-\u0000\u0216\u0215"+
		"\u0001\u0000\u0000\u0000\u0216\u0217\u0001\u0000\u0000\u0000\u0217\u021b"+
		"\u0001\u0000\u0000\u0000\u0218\u0219\u0003d2\u0000\u0219\u021a\u0003x"+
		"<\u0000\u021a\u021c\u0001\u0000\u0000\u0000\u021b\u0218\u0001\u0000\u0000"+
		"\u0000\u021b\u021c\u0001\u0000\u0000\u0000\u021c\u021d\u0001\u0000\u0000"+
		"\u0000\u021d\u021e\u0003\u0014\n\u0000\u021e\u0222\u0003x<\u0000\u021f"+
		"\u0223\u0003X,\u0000\u0220\u0223\u0003V+\u0000\u0221\u0223\u0003T*\u0000"+
		"\u0222\u021f\u0001\u0000\u0000\u0000\u0222\u0220\u0001\u0000\u0000\u0000"+
		"\u0222\u0221\u0001\u0000\u0000\u0000\u0223S\u0001\u0000\u0000\u0000\u0224"+
		"\u0225\u0003j5\u0000\u0225\u0226\u0003x<\u0000\u0226\u0227\u0003\u008e"+
		"G\u0000\u0227\u0228\u0003n7\u0000\u0228\u0229\u0003\u008eG\u0000\u0229"+
		"U\u0001\u0000\u0000\u0000\u022a\u022b\u0003h4\u0000\u022b\u022c\u0003"+
		"x<\u0000\u022c\u022d\u0005\b\u0000\u0000\u022d\u022e\u0003l6\u0000\u022e"+
		"W\u0001\u0000\u0000\u0000\u022f\u0230\u0003f3\u0000\u0230\u0233\u0003"+
		"x<\u0000\u0231\u0234\u0003\u0014\n\u0000\u0232\u0234\u0003\u000e\u0007"+
		"\u0000\u0233\u0231\u0001\u0000\u0000\u0000\u0233\u0232\u0001\u0000\u0000"+
		"\u0000\u0234Y\u0001\u0000\u0000\u0000\u0235\u0236\u0005@\u0000\u0000\u0236"+
		"\u0237\u0003\\.\u0000\u0237\u0238\u0003^/\u0000\u0238\u0239\u0003`0\u0000"+
		"\u0239\u023a\u0005B\u0000\u0000\u023a[\u0001\u0000\u0000\u0000\u023b\u023c"+
		"\u0003t:\u0000\u023c]\u0001\u0000\u0000\u0000\u023d\u023e\u0005\u0013"+
		"\u0000\u0000\u023e\u0245\u0005\u0013\u0000\u0000\u023f\u0240\u0003z=\u0000"+
		"\u0240\u0241\u0007\u0006\u0000\u0000\u0241\u0242\u0007\u0007\u0000\u0000"+
		"\u0242\u0243\u0003z=\u0000\u0243\u0245\u0001\u0000\u0000\u0000\u0244\u023d"+
		"\u0001\u0000\u0000\u0000\u0244\u023f\u0001\u0000\u0000\u0000\u0245_\u0001"+
		"\u0000\u0000\u0000\u0246\u0249\u0003t:\u0000\u0247\u0249\u0003b1\u0000"+
		"\u0248\u0246\u0001\u0000\u0000\u0000\u0248\u0247\u0001\u0000\u0000\u0000"+
		"\u0249a\u0001\u0000\u0000\u0000\u024a\u0250\u0005\u000f\u0000\u0000\u024b"+
		"\u024c\u0007\u000e\u0000\u0000\u024c\u024d\u0007\u0005\u0000\u0000\u024d"+
		"\u024e\u0007\u0004\u0000\u0000\u024e\u0250\u0007\u0010\u0000\u0000\u024f"+
		"\u024a\u0001\u0000\u0000\u0000\u024f\u024b\u0001\u0000\u0000\u0000\u0250"+
		"c\u0001\u0000\u0000\u0000\u0251\u0252\u0007\t\u0000\u0000\u0252\u0253"+
		"\u0007\u0001\u0000\u0000\u0253\u0254\u0007\u0011\u0000\u0000\u0254\u0255"+
		"\u0007\u0001\u0000\u0000\u0255\u0256\u0007\t\u0000\u0000\u0256\u0257\u0007"+
		"\u0002\u0000\u0000\u0257\u0258\u0007\u0001\u0000\u0000\u0258\u0259\u0007"+
		"\u0007\u0000\u0000\u0259\u025d\u0007\b\u0000\u0000\u025a\u025b\u0007\t"+
		"\u0000\u0000\u025b\u025d\u0003z=\u0000\u025c\u0251\u0001\u0000\u0000\u0000"+
		"\u025c\u025a\u0001\u0000\u0000\u0000\u025de\u0001\u0000\u0000\u0000\u025e"+
		"\u0262\u0005\"\u0000\u0000\u025f\u0260\u0005\u0006\u0000\u0000\u0260\u0262"+
		"\u0005\"\u0000\u0000\u0261\u025e\u0001\u0000\u0000\u0000\u0261\u025f\u0001"+
		"\u0000\u0000\u0000\u0262g\u0001\u0000\u0000\u0000\u0263\u026d\u0005\""+
		"\u0000\u0000\u0264\u0265\u0005\u0006\u0000\u0000\u0265\u026d\u0005\"\u0000"+
		"\u0000\u0266\u0267\u0005!\u0000\u0000\u0267\u026d\u0005\"\u0000\u0000"+
		"\u0268\u026d\u0005!\u0000\u0000\u0269\u026a\u0005#\u0000\u0000\u026a\u026d"+
		"\u0005\"\u0000\u0000\u026b\u026d\u0005#\u0000\u0000\u026c\u0263\u0001"+
		"\u0000\u0000\u0000\u026c\u0264\u0001\u0000\u0000\u0000\u026c\u0266\u0001"+
		"\u0000\u0000\u0000\u026c\u0268\u0001\u0000\u0000\u0000\u026c\u0269\u0001"+
		"\u0000\u0000\u0000\u026c\u026b\u0001\u0000\u0000\u0000\u026di\u0001\u0000"+
		"\u0000\u0000\u026e\u0272\u0005\"\u0000\u0000\u026f\u0270\u0005\u0006\u0000"+
		"\u0000\u0270\u0272\u0005\"\u0000\u0000\u0271\u026e\u0001\u0000\u0000\u0000"+
		"\u0271\u026f\u0001\u0000\u0000\u0000\u0272k\u0001\u0000\u0000\u0000\u0273"+
		"\u0275\u0007\u0012\u0000\u0000\u0274\u0273\u0001\u0000\u0000\u0000\u0274"+
		"\u0275\u0001\u0000\u0000\u0000\u0275\u0278\u0001\u0000\u0000\u0000\u0276"+
		"\u0279\u0003r9\u0000\u0277\u0279\u0003p8\u0000\u0278\u0276\u0001\u0000"+
		"\u0000\u0000\u0278\u0277\u0001\u0000\u0000\u0000\u0279m\u0001\u0000\u0000"+
		"\u0000\u027a\u027d\u0003\u009aM\u0000\u027b\u027d\u0003\u009cN\u0000\u027c"+
		"\u027a\u0001\u0000\u0000\u0000\u027c\u027b\u0001\u0000\u0000\u0000\u027d"+
		"\u027e\u0001\u0000\u0000\u0000\u027e\u027c\u0001\u0000\u0000\u0000\u027e"+
		"\u027f\u0001\u0000\u0000\u0000\u027fo\u0001\u0000\u0000\u0000\u0280\u0284"+
		"\u0003\u0096K\u0000\u0281\u0283\u0003\u0092I\u0000\u0282\u0281\u0001\u0000"+
		"\u0000\u0000\u0283\u0286\u0001\u0000\u0000\u0000\u0284\u0282\u0001\u0000"+
		"\u0000\u0000\u0284\u0285\u0001\u0000\u0000\u0000\u0285\u0289\u0001\u0000"+
		"\u0000\u0000\u0286\u0284\u0001\u0000\u0000\u0000\u0287\u0289\u0003\u0094"+
		"J\u0000\u0288\u0280\u0001\u0000\u0000\u0000\u0288\u0287\u0001\u0000\u0000"+
		"\u0000\u0289q\u0001\u0000\u0000\u0000\u028a\u028b\u0003p8\u0000\u028b"+
		"\u028d\u0005\u0013\u0000\u0000\u028c\u028e\u0003\u0092I\u0000\u028d\u028c"+
		"\u0001\u0000\u0000\u0000\u028e\u028f\u0001\u0000\u0000\u0000\u028f\u028d"+
		"\u0001\u0000\u0000\u0000\u028f\u0290\u0001\u0000\u0000\u0000\u0290s\u0001"+
		"\u0000\u0000\u0000\u0291\u0295\u0003\u0096K\u0000\u0292\u0294\u0003\u0092"+
		"I\u0000\u0293\u0292\u0001\u0000\u0000\u0000\u0294\u0297\u0001\u0000\u0000"+
		"\u0000\u0295\u0293\u0001\u0000\u0000\u0000\u0295\u0296\u0001\u0000\u0000"+
		"\u0000\u0296\u029a\u0001\u0000\u0000\u0000\u0297\u0295\u0001\u0000\u0000"+
		"\u0000\u0298\u029a\u0003\u0094J\u0000\u0299\u0291\u0001\u0000\u0000\u0000"+
		"\u0299\u0298\u0001\u0000\u0000\u0000\u029au\u0001\u0000\u0000\u0000\u029b"+
		"\u029c\u0005M\u0000\u0000\u029c\u029d\u0005Y\u0000\u0000\u029d\u029e\u0005"+
		"Y\u0000\u0000\u029e\u02a0\u0005U\u0000\u0000\u029f\u02a1\u0003\u0080@"+
		"\u0000\u02a0\u029f\u0001\u0000\u0000\u0000\u02a1\u02a2\u0001\u0000\u0000"+
		"\u0000\u02a2\u02a0\u0001\u0000\u0000\u0000\u02a2\u02a3\u0001\u0000\u0000"+
		"\u0000\u02a3\u0308\u0001\u0000\u0000\u0000\u02a4\u02a5\u0003\u0096K\u0000"+
		"\u02a5\u02a6\u0003\u0092I\u0000\u02a6\u02a7\u0003\u0092I\u0000\u02a7\u02a8"+
		"\u0003\u0092I\u0000\u02a8\u02a9\u0003\u0092I\u0000\u02a9\u0305\u0003\u0092"+
		"I\u0000\u02aa\u02ac\u0003\u0092I\u0000\u02ab\u02aa\u0001\u0000\u0000\u0000"+
		"\u02ab\u02ac\u0001\u0000\u0000\u0000\u02ac\u0306\u0001\u0000\u0000\u0000"+
		"\u02ad\u02ae\u0003\u0092I\u0000\u02ae\u02af\u0003\u0092I\u0000\u02af\u0306"+
		"\u0001\u0000\u0000\u0000\u02b0\u02b1\u0003\u0092I\u0000\u02b1\u02b2\u0003"+
		"\u0092I\u0000\u02b2\u02b3\u0003\u0092I\u0000\u02b3\u0306\u0001\u0000\u0000"+
		"\u0000\u02b4\u02b5\u0003\u0092I\u0000\u02b5\u02b6\u0003\u0092I\u0000\u02b6"+
		"\u02b7\u0003\u0092I\u0000\u02b7\u02b8\u0003\u0092I\u0000\u02b8\u0306\u0001"+
		"\u0000\u0000\u0000\u02b9\u02ba\u0003\u0092I\u0000\u02ba\u02bb\u0003\u0092"+
		"I\u0000\u02bb\u02bc\u0003\u0092I\u0000\u02bc\u02bd\u0003\u0092I\u0000"+
		"\u02bd\u02be\u0003\u0092I\u0000\u02be\u0306\u0001\u0000\u0000\u0000\u02bf"+
		"\u02c0\u0003\u0092I\u0000\u02c0\u02c1\u0003\u0092I\u0000\u02c1\u02c2\u0003"+
		"\u0092I\u0000\u02c2\u02c3\u0003\u0092I\u0000\u02c3\u02c4\u0003\u0092I"+
		"\u0000\u02c4\u02c5\u0003\u0092I\u0000\u02c5\u0306\u0001\u0000\u0000\u0000"+
		"\u02c6\u02c7\u0003\u0092I\u0000\u02c7\u02c8\u0003\u0092I\u0000\u02c8\u02c9"+
		"\u0003\u0092I\u0000\u02c9\u02ca\u0003\u0092I\u0000\u02ca\u02cb\u0003\u0092"+
		"I\u0000\u02cb\u02cc\u0003\u0092I\u0000\u02cc\u02cd\u0003\u0092I\u0000"+
		"\u02cd\u0306\u0001\u0000\u0000\u0000\u02ce\u02cf\u0003\u0092I\u0000\u02cf"+
		"\u02d0\u0003\u0092I\u0000\u02d0\u02d1\u0003\u0092I\u0000\u02d1\u02d2\u0003"+
		"\u0092I\u0000\u02d2\u02d3\u0003\u0092I\u0000\u02d3\u02d4\u0003\u0092I"+
		"\u0000\u02d4\u02d5\u0003\u0092I\u0000\u02d5\u02d6\u0003\u0092I\u0000\u02d6"+
		"\u0306\u0001\u0000\u0000\u0000\u02d7\u02d8\u0003\u0092I\u0000\u02d8\u02d9"+
		"\u0003\u0092I\u0000\u02d9\u02da\u0003\u0092I\u0000\u02da\u02db\u0003\u0092"+
		"I\u0000\u02db\u02dc\u0003\u0092I\u0000\u02dc\u02dd\u0003\u0092I\u0000"+
		"\u02dd\u02de\u0003\u0092I\u0000\u02de\u02df\u0003\u0092I\u0000\u02df\u02e0"+
		"\u0003\u0092I\u0000\u02e0\u0306\u0001\u0000\u0000\u0000\u02e1\u02e2\u0003"+
		"\u0092I\u0000\u02e2\u02e3\u0003\u0092I\u0000\u02e3\u02e4\u0003\u0092I"+
		"\u0000\u02e4\u02e5\u0003\u0092I\u0000\u02e5\u02e6\u0003\u0092I\u0000\u02e6"+
		"\u02e7\u0003\u0092I\u0000\u02e7\u02e8\u0003\u0092I\u0000\u02e8\u02e9\u0003"+
		"\u0092I\u0000\u02e9\u02ea\u0003\u0092I\u0000\u02ea\u02eb\u0003\u0092I"+
		"\u0000\u02eb\u0306\u0001\u0000\u0000\u0000\u02ec\u02ed\u0003\u0092I\u0000"+
		"\u02ed\u02ee\u0003\u0092I\u0000\u02ee\u02ef\u0003\u0092I\u0000\u02ef\u02f0"+
		"\u0003\u0092I\u0000\u02f0\u02f1\u0003\u0092I\u0000\u02f1\u02f2\u0003\u0092"+
		"I\u0000\u02f2\u02f3\u0003\u0092I\u0000\u02f3\u02f4\u0003\u0092I\u0000"+
		"\u02f4\u02f5\u0003\u0092I\u0000\u02f5\u02f6\u0003\u0092I\u0000\u02f6\u02f7"+
		"\u0003\u0092I\u0000\u02f7\u0306\u0001\u0000\u0000\u0000\u02f8\u02f9\u0003"+
		"\u0092I\u0000\u02f9\u02fa\u0003\u0092I\u0000\u02fa\u02fb\u0003\u0092I"+
		"\u0000\u02fb\u02fc\u0003\u0092I\u0000\u02fc\u02fd\u0003\u0092I\u0000\u02fd"+
		"\u02fe\u0003\u0092I\u0000\u02fe\u02ff\u0003\u0092I\u0000\u02ff\u0300\u0003"+
		"\u0092I\u0000\u0300\u0301\u0003\u0092I\u0000\u0301\u0302\u0003\u0092I"+
		"\u0000\u0302\u0303\u0003\u0092I\u0000\u0303\u0304\u0003\u0092I\u0000\u0304"+
		"\u0306\u0001\u0000\u0000\u0000\u0305\u02ab\u0001\u0000\u0000\u0000\u0305"+
		"\u02ad\u0001\u0000\u0000\u0000\u0305\u02b0\u0001\u0000\u0000\u0000\u0305"+
		"\u02b4\u0001\u0000\u0000\u0000\u0305\u02b9\u0001\u0000\u0000\u0000\u0305"+
		"\u02bf\u0001\u0000\u0000\u0000\u0305\u02c6\u0001\u0000\u0000\u0000\u0305"+
		"\u02ce\u0001\u0000\u0000\u0000\u0305\u02d7\u0001\u0000\u0000\u0000\u0305"+
		"\u02e1\u0001\u0000\u0000\u0000\u0305\u02ec\u0001\u0000\u0000\u0000\u0305"+
		"\u02f8\u0001\u0000\u0000\u0000\u0306\u0308\u0001\u0000\u0000\u0000\u0307"+
		"\u029b\u0001\u0000\u0000\u0000\u0307\u02a4\u0001\u0000\u0000\u0000\u0308"+
		"w\u0001\u0000\u0000\u0000\u0309\u030f\u0003\u0086C\u0000\u030a\u030f\u0003"+
		"\u0088D\u0000\u030b\u030f\u0003\u008aE\u0000\u030c\u030f\u0003\u008cF"+
		"\u0000\u030d\u030f\u0003|>\u0000\u030e\u0309\u0001\u0000\u0000\u0000\u030e"+
		"\u030a\u0001\u0000\u0000\u0000\u030e\u030b\u0001\u0000\u0000\u0000\u030e"+
		"\u030c\u0001\u0000\u0000\u0000\u030e\u030d\u0001\u0000\u0000\u0000\u030f"+
		"\u0312\u0001\u0000\u0000\u0000\u0310\u030e\u0001\u0000\u0000\u0000\u0310"+
		"\u0311\u0001\u0000\u0000\u0000\u0311y\u0001\u0000\u0000\u0000\u0312\u0310"+
		"\u0001\u0000\u0000\u0000\u0313\u0319\u0003\u0086C\u0000\u0314\u0319\u0003"+
		"\u0088D\u0000\u0315\u0319\u0003\u008aE\u0000\u0316\u0319\u0003\u008cF"+
		"\u0000\u0317\u0319\u0003|>\u0000\u0318\u0313\u0001\u0000\u0000\u0000\u0318"+
		"\u0314\u0001\u0000\u0000\u0000\u0318\u0315\u0001\u0000\u0000\u0000\u0318"+
		"\u0316\u0001\u0000\u0000\u0000\u0318\u0317\u0001\u0000\u0000\u0000\u0319"+
		"\u031a\u0001\u0000\u0000\u0000\u031a\u0318\u0001\u0000\u0000\u0000\u031a"+
		"\u031b\u0001\u0000\u0000\u0000\u031b{\u0001\u0000\u0000\u0000\u031c\u031d"+
		"\u0005\u0014\u0000\u0000\u031d\u031e\u0005\u000f\u0000\u0000\u031e\u0323"+
		"\u0001\u0000\u0000\u0000\u031f\u0322\u0003~?\u0000\u0320\u0322\u0003\u0082"+
		"A\u0000\u0321\u031f\u0001\u0000\u0000\u0000\u0321\u0320\u0001\u0000\u0000"+
		"\u0000\u0322\u0325\u0001\u0000\u0000\u0000\u0323\u0321\u0001\u0000\u0000"+
		"\u0000\u0323\u0324\u0001\u0000\u0000\u0000\u0324\u0326\u0001\u0000\u0000"+
		"\u0000\u0325\u0323\u0001\u0000\u0000\u0000\u0326\u0327\u0005\u000f\u0000"+
		"\u0000\u0327\u0328\u0005\u0014\u0000\u0000\u0328}\u0001\u0000\u0000\u0000"+
		"\u0329\u0331\u0003\u0086C\u0000\u032a\u0331\u0003\u0088D\u0000\u032b\u0331"+
		"\u0003\u008aE\u0000\u032c\u0331\u0003\u008cF\u0000\u032d\u0331\u0007\u0013"+
		"\u0000\u0000\u032e\u0331\u0007\u0014\u0000\u0000\u032f\u0331\u0005\u0001"+
		"\u0000\u0000\u0330\u0329\u0001\u0000\u0000\u0000\u0330\u032a\u0001\u0000"+
		"\u0000\u0000\u0330\u032b\u0001\u0000\u0000\u0000\u0330\u032c\u0001\u0000"+
		"\u0000\u0000\u0330\u032d\u0001\u0000\u0000\u0000\u0330\u032e\u0001\u0000"+
		"\u0000\u0000\u0330\u032f\u0001\u0000\u0000\u0000\u0331\u007f\u0001\u0000"+
		"\u0000\u0000\u0332\u0336\u0007\u0015\u0000\u0000\u0333\u0336\u0007\u0014"+
		"\u0000\u0000\u0334\u0336\u0005\u0001\u0000\u0000\u0335\u0332\u0001\u0000"+
		"\u0000\u0000\u0335\u0333\u0001\u0000\u0000\u0000\u0335\u0334\u0001\u0000"+
		"\u0000\u0000\u0336\u0081\u0001\u0000\u0000\u0000\u0337\u0338\u0005\u000f"+
		"\u0000\u0000\u0338\u0339\u0003\u0084B\u0000\u0339\u0083\u0001\u0000\u0000"+
		"\u0000\u033a\u0342\u0003\u0086C\u0000\u033b\u0342\u0003\u0088D\u0000\u033c"+
		"\u0342\u0003\u008aE\u0000\u033d\u0342\u0003\u008cF\u0000\u033e\u0342\u0007"+
		"\u0016\u0000\u0000\u033f\u0342\u0007\u0017\u0000\u0000\u0340\u0342\u0005"+
		"\u0001\u0000\u0000\u0341\u033a\u0001\u0000\u0000\u0000\u0341\u033b\u0001"+
		"\u0000\u0000\u0000\u0341\u033c\u0001\u0000\u0000\u0000\u0341\u033d\u0001"+
		"\u0000\u0000\u0000\u0341\u033e\u0001\u0000\u0000\u0000\u0341\u033f\u0001"+
		"\u0000\u0000\u0000\u0341\u0340\u0001\u0000\u0000\u0000\u0342\u0085\u0001"+
		"\u0000\u0000\u0000\u0343\u0344\u0005\u0005\u0000\u0000\u0344\u0087\u0001"+
		"\u0000\u0000\u0000\u0345\u0346\u0005\u0002\u0000\u0000\u0346\u0089\u0001"+
		"\u0000\u0000\u0000\u0347\u0348\u0005\u0004\u0000\u0000\u0348\u008b\u0001"+
		"\u0000\u0000\u0000\u0349\u034a\u0005\u0003\u0000\u0000\u034a\u008d\u0001"+
		"\u0000\u0000\u0000\u034b\u034c\u0005\u0007\u0000\u0000\u034c\u008f\u0001"+
		"\u0000\u0000\u0000\u034d\u034e\u0005A\u0000\u0000\u034e\u0091\u0001\u0000"+
		"\u0000\u0000\u034f\u0350\u0007\u0018\u0000\u0000\u0350\u0093\u0001\u0000"+
		"\u0000\u0000\u0351\u0352\u0005\u0015\u0000\u0000\u0352\u0095\u0001\u0000"+
		"\u0000\u0000\u0353\u0354\u0007\u0019\u0000\u0000\u0354\u0097\u0001\u0000"+
		"\u0000\u0000\u0355\u0359\u0007\u001a\u0000\u0000\u0356\u0359\u0007\u001b"+
		"\u0000\u0000\u0357\u0359\u0005\u0001\u0000\u0000\u0358\u0355\u0001\u0000"+
		"\u0000\u0000\u0358\u0356\u0001\u0000\u0000\u0000\u0358\u0357\u0001\u0000"+
		"\u0000\u0000\u0359\u0099\u0001\u0000\u0000\u0000\u035a\u0363\u0003\u0086"+
		"C\u0000\u035b\u0363\u0003\u0088D\u0000\u035c\u0363\u0003\u008aE\u0000"+
		"\u035d\u0363\u0003\u008cF\u0000\u035e\u0363\u0007\u001c\u0000\u0000\u035f"+
		"\u0363\u0007\u001d\u0000\u0000\u0360\u0363\u0007\u001e\u0000\u0000\u0361"+
		"\u0363\u0005\u0001\u0000\u0000\u0362\u035a\u0001\u0000\u0000\u0000\u0362"+
		"\u035b\u0001\u0000\u0000\u0000\u0362\u035c\u0001\u0000\u0000\u0000\u0362"+
		"\u035d\u0001\u0000\u0000\u0000\u0362\u035e\u0001\u0000\u0000\u0000\u0362"+
		"\u035f\u0001\u0000\u0000\u0000\u0362\u0360\u0001\u0000\u0000\u0000\u0362"+
		"\u0361\u0001\u0000\u0000\u0000\u0363\u009b\u0001\u0000\u0000\u0000\u0364"+
		"\u0365\u0003\u0090H\u0000\u0365\u0366\u0003\u008eG\u0000\u0366\u036b\u0001"+
		"\u0000\u0000\u0000\u0367\u0368\u0003\u0090H\u0000\u0368\u0369\u0003\u0090"+
		"H\u0000\u0369\u036b\u0001\u0000\u0000\u0000\u036a\u0364\u0001\u0000\u0000"+
		"\u0000\u036a\u0367\u0001\u0000\u0000\u0000\u036b\u009d\u0001\u0000\u0000"+
		"\u0000T\u00a8\u00ae\u00b8\u00bc\u00c3\u00c7\u00cb\u00d2\u00d6\u00da\u00e1"+
		"\u00e7\u00f2\u00fb\u0100\u0106\u0114\u011b\u0120\u0125\u0129\u0134\u0143"+
		"\u0159\u0164\u0171\u0185\u0191\u0198\u01a7\u01ab\u01af\u01b6\u01ba\u01be"+
		"\u01c5\u01c9\u01d5\u01df\u01e3\u01ea\u01ee\u01f2\u01f9\u01fd\u0207\u020a"+
		"\u0210\u0216\u021b\u0222\u0233\u0244\u0248\u024f\u025c\u0261\u026c\u0271"+
		"\u0274\u0278\u027c\u027e\u0284\u0288\u028f\u0295\u0299\u02a2\u02ab\u0305"+
		"\u0307\u030e\u0310\u0318\u031a\u0321\u0323\u0330\u0335\u0341\u0358\u0362"+
		"\u036a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}