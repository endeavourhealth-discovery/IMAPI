// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/IMAPI/parser/src/main/grammars\ECL.g4 by ANTLR 4.9.1
package org.endeavourhealth.imapi.parser.ecl;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ECLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

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
		RULE_expressionconstraint = 0, RULE_refinedexpressionconstraint = 1, RULE_compoundexpressionconstraint = 2, 
		RULE_conjunctionexpressionconstraint = 3, RULE_disjunctionexpressionconstraint = 4, 
		RULE_exclusionexpressionconstraint = 5, RULE_dottedexpressionconstraint = 6, 
		RULE_dottedexpressionattribute = 7, RULE_subexpressionconstraint = 8, 
		RULE_eclfocusconcept = 9, RULE_dot = 10, RULE_memberof = 11, RULE_eclconceptreference = 12, 
		RULE_conceptid = 13, RULE_term = 14, RULE_wildcard = 15, RULE_constraintoperator = 16, 
		RULE_descendantof = 17, RULE_descendantorselfof = 18, RULE_childof = 19, 
		RULE_ancestorof = 20, RULE_ancestororselfof = 21, RULE_parentof = 22, 
		RULE_conjunction = 23, RULE_disjunction = 24, RULE_exclusion = 25, RULE_eclrefinement = 26, 
		RULE_conjunctionrefinementset = 27, RULE_disjunctionrefinementset = 28, 
		RULE_subrefinement = 29, RULE_eclattributeset = 30, RULE_conjunctionattributeset = 31, 
		RULE_disjunctionattributeset = 32, RULE_subattributeset = 33, RULE_eclattributegroup = 34, 
		RULE_eclattribute = 35, RULE_cardinality = 36, RULE_minvalue = 37, RULE_to = 38, 
		RULE_maxvalue = 39, RULE_many = 40, RULE_reverseflag = 41, RULE_eclattributename = 42, 
		RULE_expressioncomparisonoperator = 43, RULE_numericcomparisonoperator = 44, 
		RULE_stringcomparisonoperator = 45, RULE_numericvalue = 46, RULE_stringvalue = 47, 
		RULE_integervalue = 48, RULE_decimalvalue = 49, RULE_nonnegativeintegervalue = 50, 
		RULE_sctid = 51, RULE_ws = 52, RULE_mws = 53, RULE_comment = 54, RULE_nonstarchar = 55, 
		RULE_nonspacechar = 56, RULE_starwithnonfslash = 57, RULE_nonfslash = 58, 
		RULE_sp = 59, RULE_htab = 60, RULE_cr = 61, RULE_lf = 62, RULE_qm = 63, 
		RULE_bs = 64, RULE_digit = 65, RULE_zero = 66, RULE_digitnonzero = 67, 
		RULE_nonwsnonpipe = 68, RULE_anynonescapedchar = 69, RULE_escapedchar = 70;
	private static String[] makeRuleNames() {
		return new String[] {
			"expressionconstraint", "refinedexpressionconstraint", "compoundexpressionconstraint", 
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
			null, null, "'\t'", "'\n'", "'\r'", "' '", "'!'", "'\"'", "'#'", "'$'", 
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
	public String getGrammarFileName() { return "ECL.g4"; }

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
		enterRule(_localctx, 0, RULE_expressionconstraint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(142);
			ws();
			setState(147);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(143);
				refinedexpressionconstraint();
				}
				break;
			case 2:
				{
				setState(144);
				compoundexpressionconstraint();
				}
				break;
			case 3:
				{
				setState(145);
				dottedexpressionconstraint();
				}
				break;
			case 4:
				{
				setState(146);
				subexpressionconstraint();
				}
				break;
			}
			setState(149);
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
		enterRule(_localctx, 2, RULE_refinedexpressionconstraint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(151);
			subexpressionconstraint();
			setState(152);
			ws();
			setState(153);
			match(COLON);
			setState(154);
			ws();
			setState(155);
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
		enterRule(_localctx, 4, RULE_compoundexpressionconstraint);
		try {
			setState(160);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(157);
				conjunctionexpressionconstraint();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(158);
				disjunctionexpressionconstraint();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(159);
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
		enterRule(_localctx, 6, RULE_conjunctionexpressionconstraint);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			subexpressionconstraint();
			setState(168); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(163);
					ws();
					setState(164);
					conjunction();
					setState(165);
					ws();
					setState(166);
					subexpressionconstraint();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(170); 
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
		enterRule(_localctx, 8, RULE_disjunctionexpressionconstraint);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			subexpressionconstraint();
			setState(178); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(173);
					ws();
					setState(174);
					disjunction();
					setState(175);
					ws();
					setState(176);
					subexpressionconstraint();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(180); 
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
		enterRule(_localctx, 10, RULE_exclusionexpressionconstraint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(182);
			subexpressionconstraint();
			setState(183);
			ws();
			setState(184);
			exclusion();
			setState(185);
			ws();
			setState(186);
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
		enterRule(_localctx, 12, RULE_dottedexpressionconstraint);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(188);
			subexpressionconstraint();
			setState(192); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(189);
					ws();
					setState(190);
					dottedexpressionattribute();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(194); 
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
		enterRule(_localctx, 14, RULE_dottedexpressionattribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(196);
			dot();
			setState(197);
			ws();
			setState(198);
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
		enterRule(_localctx, 16, RULE_subexpressionconstraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(203);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LESS_THAN || _la==GREATER_THAN) {
				{
				setState(200);
				constraintoperator();
				setState(201);
				ws();
				}
			}

			setState(208);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CARAT) {
				{
				setState(205);
				memberof();
				setState(206);
				ws();
				}
			}

			setState(217);
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
				setState(210);
				eclfocusconcept();
				}
				break;
			case LEFT_PAREN:
				{
				{
				setState(211);
				match(LEFT_PAREN);
				setState(212);
				ws();
				setState(213);
				expressionconstraint();
				setState(214);
				ws();
				setState(215);
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
		enterRule(_localctx, 18, RULE_eclfocusconcept);
		try {
			setState(221);
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
				setState(219);
				eclconceptreference();
				}
				break;
			case ASTERISK:
				enterOuterAlt(_localctx, 2);
				{
				setState(220);
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
		enterRule(_localctx, 20, RULE_dot);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(223);
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
		enterRule(_localctx, 22, RULE_memberof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(225);
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
		enterRule(_localctx, 24, RULE_eclconceptreference);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(227);
			conceptid();
			setState(235);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				{
				setState(228);
				ws();
				setState(229);
				match(PIPE);
				setState(230);
				ws();
				setState(231);
				term();
				setState(232);
				ws();
				setState(233);
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
		enterRule(_localctx, 26, RULE_conceptid);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(237);
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
		enterRule(_localctx, 28, RULE_term);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(240); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(239);
					nonwsnonpipe();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(242); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(256);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(245); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(244);
						sp();
						}
						}
						setState(247); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==SPACE );
					setState(250); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(249);
							nonwsnonpipe();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(252); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					}
					} 
				}
				setState(258);
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
		enterRule(_localctx, 30, RULE_wildcard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(259);
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
		enterRule(_localctx, 32, RULE_constraintoperator);
		try {
			setState(267);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(261);
				childof();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(262);
				descendantorselfof();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(263);
				descendantof();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(264);
				parentof();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(265);
				ancestororselfof();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(266);
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

	public static class DescendantofContext extends ParserRuleContext {
		public TerminalNode LESS_THAN() { return getToken(ECLParser.LESS_THAN, 0); }
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
		enterRule(_localctx, 34, RULE_descendantof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(269);
			match(LESS_THAN);
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

	public static class DescendantorselfofContext extends ParserRuleContext {
		public List<TerminalNode> LESS_THAN() { return getTokens(ECLParser.LESS_THAN); }
		public TerminalNode LESS_THAN(int i) {
			return getToken(ECLParser.LESS_THAN, i);
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
		enterRule(_localctx, 36, RULE_descendantorselfof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(271);
			match(LESS_THAN);
			setState(272);
			match(LESS_THAN);
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

	public static class ChildofContext extends ParserRuleContext {
		public TerminalNode LESS_THAN() { return getToken(ECLParser.LESS_THAN, 0); }
		public TerminalNode EXCLAMATION() { return getToken(ECLParser.EXCLAMATION, 0); }
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
		enterRule(_localctx, 38, RULE_childof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(274);
			match(LESS_THAN);
			setState(275);
			match(EXCLAMATION);
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

	public static class AncestorofContext extends ParserRuleContext {
		public TerminalNode GREATER_THAN() { return getToken(ECLParser.GREATER_THAN, 0); }
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
		enterRule(_localctx, 40, RULE_ancestorof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(277);
			match(GREATER_THAN);
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

	public static class AncestororselfofContext extends ParserRuleContext {
		public List<TerminalNode> GREATER_THAN() { return getTokens(ECLParser.GREATER_THAN); }
		public TerminalNode GREATER_THAN(int i) {
			return getToken(ECLParser.GREATER_THAN, i);
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
		enterRule(_localctx, 42, RULE_ancestororselfof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(279);
			match(GREATER_THAN);
			setState(280);
			match(GREATER_THAN);
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

	public static class ParentofContext extends ParserRuleContext {
		public TerminalNode GREATER_THAN() { return getToken(ECLParser.GREATER_THAN, 0); }
		public TerminalNode EXCLAMATION() { return getToken(ECLParser.EXCLAMATION, 0); }
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
		enterRule(_localctx, 44, RULE_parentof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(282);
			match(GREATER_THAN);
			setState(283);
			match(EXCLAMATION);
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
		enterRule(_localctx, 46, RULE_conjunction);
		int _la;
		try {
			setState(290);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CAP_A:
			case A:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(285);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(286);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(287);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(288);
				mws();
				}
				}
				break;
			case COMMA:
				enterOuterAlt(_localctx, 2);
				{
				setState(289);
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
		enterRule(_localctx, 48, RULE_disjunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(292);
			_la = _input.LA(1);
			if ( !(_la==CAP_O || _la==O) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(293);
			_la = _input.LA(1);
			if ( !(_la==CAP_R || _la==R) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(294);
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
		enterRule(_localctx, 50, RULE_exclusion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(296);
			_la = _input.LA(1);
			if ( !(_la==CAP_M || _la==M) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(297);
			_la = _input.LA(1);
			if ( !(_la==CAP_I || _la==I) ) {
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
			if ( !(_la==CAP_U || _la==U) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(300);
			_la = _input.LA(1);
			if ( !(_la==CAP_S || _la==S) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(301);
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
		enterRule(_localctx, 52, RULE_eclrefinement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(303);
			subrefinement();
			setState(304);
			ws();
			setState(307);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(305);
				conjunctionrefinementset();
				}
				break;
			case 2:
				{
				setState(306);
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
		enterRule(_localctx, 54, RULE_conjunctionrefinementset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(314); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(309);
					ws();
					setState(310);
					conjunction();
					setState(311);
					ws();
					setState(312);
					subrefinement();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(316); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
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
		enterRule(_localctx, 56, RULE_disjunctionrefinementset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(323); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(318);
					ws();
					setState(319);
					disjunction();
					setState(320);
					ws();
					setState(321);
					subrefinement();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(325); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
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
		enterRule(_localctx, 58, RULE_subrefinement);
		try {
			setState(335);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(327);
				eclattributeset();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(328);
				eclattributegroup();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(329);
				match(LEFT_PAREN);
				setState(330);
				ws();
				setState(331);
				eclrefinement();
				setState(332);
				ws();
				setState(333);
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
		enterRule(_localctx, 60, RULE_eclattributeset);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(337);
			subattributeset();
			setState(338);
			ws();
			setState(341);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				setState(339);
				conjunctionattributeset();
				}
				break;
			case 2:
				{
				setState(340);
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
		enterRule(_localctx, 62, RULE_conjunctionattributeset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(348); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(343);
					ws();
					setState(344);
					conjunction();
					setState(345);
					ws();
					setState(346);
					subattributeset();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(350); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
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
		enterRule(_localctx, 64, RULE_disjunctionattributeset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(357); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(352);
					ws();
					setState(353);
					disjunction();
					setState(354);
					ws();
					setState(355);
					subattributeset();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(359); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
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
		enterRule(_localctx, 66, RULE_subattributeset);
		try {
			setState(368);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(361);
				eclattribute();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(362);
				match(LEFT_PAREN);
				setState(363);
				ws();
				setState(364);
				eclattributeset();
				setState(365);
				ws();
				setState(366);
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
		enterRule(_localctx, 68, RULE_eclattributegroup);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(375);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LEFT_BRACE) {
				{
				setState(370);
				match(LEFT_BRACE);
				setState(371);
				cardinality();
				setState(372);
				match(RIGHT_BRACE);
				setState(373);
				ws();
				}
			}

			setState(377);
			match(LEFT_CURLY_BRACE);
			setState(378);
			ws();
			setState(379);
			eclattributeset();
			setState(380);
			ws();
			setState(381);
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
		enterRule(_localctx, 70, RULE_eclattribute);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(388);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LEFT_BRACE) {
				{
				setState(383);
				match(LEFT_BRACE);
				setState(384);
				cardinality();
				setState(385);
				match(RIGHT_BRACE);
				setState(386);
				ws();
				}
			}

			setState(393);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CAP_R) {
				{
				setState(390);
				reverseflag();
				setState(391);
				ws();
				}
			}

			setState(395);
			eclattributename();
			setState(396);
			ws();
			setState(412);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				{
				{
				setState(397);
				expressioncomparisonoperator();
				setState(398);
				ws();
				setState(399);
				subexpressionconstraint();
				}
				}
				break;
			case 2:
				{
				{
				setState(401);
				numericcomparisonoperator();
				setState(402);
				ws();
				setState(403);
				match(POUND);
				setState(404);
				numericvalue();
				}
				}
				break;
			case 3:
				{
				{
				setState(406);
				stringcomparisonoperator();
				setState(407);
				ws();
				setState(408);
				qm();
				setState(409);
				stringvalue();
				setState(410);
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
		enterRule(_localctx, 72, RULE_cardinality);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(414);
			minvalue();
			setState(415);
			to();
			setState(416);
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
		enterRule(_localctx, 74, RULE_minvalue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(418);
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
		enterRule(_localctx, 76, RULE_to);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(420);
			match(PERIOD);
			setState(421);
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
		enterRule(_localctx, 78, RULE_maxvalue);
		try {
			setState(425);
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
				setState(423);
				nonnegativeintegervalue();
				}
				break;
			case ASTERISK:
				enterOuterAlt(_localctx, 2);
				{
				setState(424);
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
		enterRule(_localctx, 80, RULE_many);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(427);
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
		enterRule(_localctx, 82, RULE_reverseflag);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(429);
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
		enterRule(_localctx, 84, RULE_eclattributename);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(431);
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
		enterRule(_localctx, 86, RULE_expressioncomparisonoperator);
		try {
			setState(436);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EQUALS:
				enterOuterAlt(_localctx, 1);
				{
				setState(433);
				match(EQUALS);
				}
				break;
			case EXCLAMATION:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(434);
				match(EXCLAMATION);
				setState(435);
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
		enterRule(_localctx, 88, RULE_numericcomparisonoperator);
		try {
			setState(447);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(438);
				match(EQUALS);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(439);
				match(EXCLAMATION);
				setState(440);
				match(EQUALS);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(441);
				match(LESS_THAN);
				setState(442);
				match(EQUALS);
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(443);
				match(LESS_THAN);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				{
				setState(444);
				match(GREATER_THAN);
				setState(445);
				match(EQUALS);
				}
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(446);
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
		enterRule(_localctx, 90, RULE_stringcomparisonoperator);
		try {
			setState(452);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EQUALS:
				enterOuterAlt(_localctx, 1);
				{
				setState(449);
				match(EQUALS);
				}
				break;
			case EXCLAMATION:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(450);
				match(EXCLAMATION);
				setState(451);
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
		enterRule(_localctx, 92, RULE_numericvalue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(455);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PLUS || _la==DASH) {
				{
				setState(454);
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

			setState(459);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				setState(457);
				decimalvalue();
				}
				break;
			case 2:
				{
				setState(458);
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
		enterRule(_localctx, 94, RULE_stringvalue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(463); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(463);
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
					setState(461);
					anynonescapedchar();
					}
					break;
				case BACKSLASH:
					{
					setState(462);
					escapedchar();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(465); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << UTF8_LETTER) | (1L << TAB) | (1L << LF) | (1L << CR) | (1L << SPACE) | (1L << EXCLAMATION) | (1L << POUND) | (1L << DOLLAR) | (1L << PERCENT) | (1L << AMPERSAND) | (1L << APOSTROPHE) | (1L << LEFT_PAREN) | (1L << RIGHT_PAREN) | (1L << ASTERISK) | (1L << PLUS) | (1L << COMMA) | (1L << DASH) | (1L << PERIOD) | (1L << SLASH) | (1L << ZERO) | (1L << ONE) | (1L << TWO) | (1L << THREE) | (1L << FOUR) | (1L << FIVE) | (1L << SIX) | (1L << SEVEN) | (1L << EIGHT) | (1L << NINE) | (1L << COLON) | (1L << SEMICOLON) | (1L << LESS_THAN) | (1L << EQUALS) | (1L << GREATER_THAN) | (1L << QUESTION) | (1L << AT) | (1L << CAP_A) | (1L << CAP_B) | (1L << CAP_C) | (1L << CAP_D) | (1L << CAP_E) | (1L << CAP_F) | (1L << CAP_G) | (1L << CAP_H) | (1L << CAP_I) | (1L << CAP_J) | (1L << CAP_K) | (1L << CAP_L) | (1L << CAP_M) | (1L << CAP_N) | (1L << CAP_O) | (1L << CAP_P) | (1L << CAP_Q) | (1L << CAP_R) | (1L << CAP_S) | (1L << CAP_T) | (1L << CAP_U) | (1L << CAP_V) | (1L << CAP_W) | (1L << CAP_X) | (1L << CAP_Y) | (1L << CAP_Z))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (LEFT_BRACE - 64)) | (1L << (BACKSLASH - 64)) | (1L << (RIGHT_BRACE - 64)) | (1L << (CARAT - 64)) | (1L << (UNDERSCORE - 64)) | (1L << (ACCENT - 64)) | (1L << (A - 64)) | (1L << (B - 64)) | (1L << (C - 64)) | (1L << (D - 64)) | (1L << (E - 64)) | (1L << (F - 64)) | (1L << (G - 64)) | (1L << (H - 64)) | (1L << (I - 64)) | (1L << (J - 64)) | (1L << (K - 64)) | (1L << (L - 64)) | (1L << (M - 64)) | (1L << (N - 64)) | (1L << (O - 64)) | (1L << (P - 64)) | (1L << (Q - 64)) | (1L << (R - 64)) | (1L << (S - 64)) | (1L << (T - 64)) | (1L << (U - 64)) | (1L << (V - 64)) | (1L << (W - 64)) | (1L << (X - 64)) | (1L << (Y - 64)) | (1L << (Z - 64)) | (1L << (LEFT_CURLY_BRACE - 64)) | (1L << (PIPE - 64)) | (1L << (RIGHT_CURLY_BRACE - 64)) | (1L << (TILDE - 64)))) != 0) );
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
		enterRule(_localctx, 96, RULE_integervalue);
		int _la;
		try {
			setState(475);
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
				setState(467);
				digitnonzero();
				setState(471);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ZERO) | (1L << ONE) | (1L << TWO) | (1L << THREE) | (1L << FOUR) | (1L << FIVE) | (1L << SIX) | (1L << SEVEN) | (1L << EIGHT) | (1L << NINE))) != 0)) {
					{
					{
					setState(468);
					digit();
					}
					}
					setState(473);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				break;
			case ZERO:
				enterOuterAlt(_localctx, 2);
				{
				setState(474);
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
		enterRule(_localctx, 98, RULE_decimalvalue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(477);
			integervalue();
			setState(478);
			match(PERIOD);
			setState(480); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(479);
				digit();
				}
				}
				setState(482); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ZERO) | (1L << ONE) | (1L << TWO) | (1L << THREE) | (1L << FOUR) | (1L << FIVE) | (1L << SIX) | (1L << SEVEN) | (1L << EIGHT) | (1L << NINE))) != 0) );
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
		enterRule(_localctx, 100, RULE_nonnegativeintegervalue);
		int _la;
		try {
			setState(492);
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
				setState(484);
				digitnonzero();
				setState(488);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ZERO) | (1L << ONE) | (1L << TWO) | (1L << THREE) | (1L << FOUR) | (1L << FIVE) | (1L << SIX) | (1L << SEVEN) | (1L << EIGHT) | (1L << NINE))) != 0)) {
					{
					{
					setState(485);
					digit();
					}
					}
					setState(490);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				break;
			case ZERO:
				enterOuterAlt(_localctx, 2);
				{
				setState(491);
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
		enterRule(_localctx, 102, RULE_sctid);
		int _la;
		try {
			int _alt;
			setState(602);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case H:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(494);
				match(H);
				setState(495);
				match(T);
				setState(496);
				match(T);
				setState(497);
				match(P);
				setState(499); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(498);
						nonspacechar();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(501); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
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
				setState(503);
				digitnonzero();
				{
				setState(504);
				digit();
				}
				{
				setState(505);
				digit();
				}
				{
				setState(506);
				digit();
				}
				{
				setState(507);
				digit();
				}
				{
				setState(508);
				digit();
				}
				setState(600);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
				case 1:
					{
					setState(510);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ZERO) | (1L << ONE) | (1L << TWO) | (1L << THREE) | (1L << FOUR) | (1L << FIVE) | (1L << SIX) | (1L << SEVEN) | (1L << EIGHT) | (1L << NINE))) != 0)) {
						{
						setState(509);
						digit();
						}
					}

					}
					break;
				case 2:
					{
					{
					{
					setState(512);
					digit();
					}
					{
					setState(513);
					digit();
					}
					}
					}
					break;
				case 3:
					{
					{
					{
					setState(515);
					digit();
					}
					{
					setState(516);
					digit();
					}
					{
					setState(517);
					digit();
					}
					}
					}
					break;
				case 4:
					{
					{
					{
					setState(519);
					digit();
					}
					{
					setState(520);
					digit();
					}
					{
					setState(521);
					digit();
					}
					{
					setState(522);
					digit();
					}
					}
					}
					break;
				case 5:
					{
					{
					{
					setState(524);
					digit();
					}
					{
					setState(525);
					digit();
					}
					{
					setState(526);
					digit();
					}
					{
					setState(527);
					digit();
					}
					{
					setState(528);
					digit();
					}
					}
					}
					break;
				case 6:
					{
					{
					{
					setState(530);
					digit();
					}
					{
					setState(531);
					digit();
					}
					{
					setState(532);
					digit();
					}
					{
					setState(533);
					digit();
					}
					{
					setState(534);
					digit();
					}
					{
					setState(535);
					digit();
					}
					}
					}
					break;
				case 7:
					{
					{
					{
					setState(537);
					digit();
					}
					{
					setState(538);
					digit();
					}
					{
					setState(539);
					digit();
					}
					{
					setState(540);
					digit();
					}
					{
					setState(541);
					digit();
					}
					{
					setState(542);
					digit();
					}
					{
					setState(543);
					digit();
					}
					}
					}
					break;
				case 8:
					{
					{
					{
					setState(545);
					digit();
					}
					{
					setState(546);
					digit();
					}
					{
					setState(547);
					digit();
					}
					{
					setState(548);
					digit();
					}
					{
					setState(549);
					digit();
					}
					{
					setState(550);
					digit();
					}
					{
					setState(551);
					digit();
					}
					{
					setState(552);
					digit();
					}
					}
					}
					break;
				case 9:
					{
					{
					{
					setState(554);
					digit();
					}
					{
					setState(555);
					digit();
					}
					{
					setState(556);
					digit();
					}
					{
					setState(557);
					digit();
					}
					{
					setState(558);
					digit();
					}
					{
					setState(559);
					digit();
					}
					{
					setState(560);
					digit();
					}
					{
					setState(561);
					digit();
					}
					{
					setState(562);
					digit();
					}
					}
					}
					break;
				case 10:
					{
					{
					{
					setState(564);
					digit();
					}
					{
					setState(565);
					digit();
					}
					{
					setState(566);
					digit();
					}
					{
					setState(567);
					digit();
					}
					{
					setState(568);
					digit();
					}
					{
					setState(569);
					digit();
					}
					{
					setState(570);
					digit();
					}
					{
					setState(571);
					digit();
					}
					{
					setState(572);
					digit();
					}
					{
					setState(573);
					digit();
					}
					}
					}
					break;
				case 11:
					{
					{
					{
					setState(575);
					digit();
					}
					{
					setState(576);
					digit();
					}
					{
					setState(577);
					digit();
					}
					{
					setState(578);
					digit();
					}
					{
					setState(579);
					digit();
					}
					{
					setState(580);
					digit();
					}
					{
					setState(581);
					digit();
					}
					{
					setState(582);
					digit();
					}
					{
					setState(583);
					digit();
					}
					{
					setState(584);
					digit();
					}
					{
					setState(585);
					digit();
					}
					}
					}
					break;
				case 12:
					{
					{
					{
					setState(587);
					digit();
					}
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
					{
					setState(593);
					digit();
					}
					{
					setState(594);
					digit();
					}
					{
					setState(595);
					digit();
					}
					{
					setState(596);
					digit();
					}
					{
					setState(597);
					digit();
					}
					{
					setState(598);
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
		enterRule(_localctx, 104, RULE_ws);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(611);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,46,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(609);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case SPACE:
						{
						setState(604);
						sp();
						}
						break;
					case TAB:
						{
						setState(605);
						htab();
						}
						break;
					case CR:
						{
						setState(606);
						cr();
						}
						break;
					case LF:
						{
						setState(607);
						lf();
						}
						break;
					case SLASH:
						{
						setState(608);
						comment();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(613);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,46,_ctx);
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
		enterRule(_localctx, 106, RULE_mws);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(619); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					setState(619);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case SPACE:
						{
						setState(614);
						sp();
						}
						break;
					case TAB:
						{
						setState(615);
						htab();
						}
						break;
					case CR:
						{
						setState(616);
						cr();
						}
						break;
					case LF:
						{
						setState(617);
						lf();
						}
						break;
					case SLASH:
						{
						setState(618);
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
				setState(621); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
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
		enterRule(_localctx, 108, RULE_comment);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(623);
			match(SLASH);
			setState(624);
			match(ASTERISK);
			}
			setState(630);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(628);
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
						setState(626);
						nonstarchar();
						}
						break;
					case ASTERISK:
						{
						setState(627);
						starwithnonfslash();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(632);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
			}
			{
			setState(633);
			match(ASTERISK);
			setState(634);
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
		enterRule(_localctx, 110, RULE_nonstarchar);
		int _la;
		try {
			setState(643);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SPACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(636);
				sp();
				}
				break;
			case TAB:
				enterOuterAlt(_localctx, 2);
				{
				setState(637);
				htab();
				}
				break;
			case CR:
				enterOuterAlt(_localctx, 3);
				{
				setState(638);
				cr();
				}
				break;
			case LF:
				enterOuterAlt(_localctx, 4);
				{
				setState(639);
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
				setState(640);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EXCLAMATION) | (1L << QUOTE) | (1L << POUND) | (1L << DOLLAR) | (1L << PERCENT) | (1L << AMPERSAND) | (1L << APOSTROPHE) | (1L << LEFT_PAREN) | (1L << RIGHT_PAREN))) != 0)) ) {
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
				setState(641);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PLUS) | (1L << COMMA) | (1L << DASH) | (1L << PERIOD) | (1L << SLASH) | (1L << ZERO) | (1L << ONE) | (1L << TWO) | (1L << THREE) | (1L << FOUR) | (1L << FIVE) | (1L << SIX) | (1L << SEVEN) | (1L << EIGHT) | (1L << NINE) | (1L << COLON) | (1L << SEMICOLON) | (1L << LESS_THAN) | (1L << EQUALS) | (1L << GREATER_THAN) | (1L << QUESTION) | (1L << AT) | (1L << CAP_A) | (1L << CAP_B) | (1L << CAP_C) | (1L << CAP_D) | (1L << CAP_E) | (1L << CAP_F) | (1L << CAP_G) | (1L << CAP_H) | (1L << CAP_I) | (1L << CAP_J) | (1L << CAP_K) | (1L << CAP_L) | (1L << CAP_M) | (1L << CAP_N) | (1L << CAP_O) | (1L << CAP_P) | (1L << CAP_Q) | (1L << CAP_R) | (1L << CAP_S) | (1L << CAP_T) | (1L << CAP_U) | (1L << CAP_V) | (1L << CAP_W) | (1L << CAP_X) | (1L << CAP_Y) | (1L << CAP_Z))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (LEFT_BRACE - 64)) | (1L << (BACKSLASH - 64)) | (1L << (RIGHT_BRACE - 64)) | (1L << (CARAT - 64)) | (1L << (UNDERSCORE - 64)) | (1L << (ACCENT - 64)) | (1L << (A - 64)) | (1L << (B - 64)) | (1L << (C - 64)) | (1L << (D - 64)) | (1L << (E - 64)) | (1L << (F - 64)) | (1L << (G - 64)) | (1L << (H - 64)) | (1L << (I - 64)) | (1L << (J - 64)) | (1L << (K - 64)) | (1L << (L - 64)) | (1L << (M - 64)) | (1L << (N - 64)) | (1L << (O - 64)) | (1L << (P - 64)) | (1L << (Q - 64)) | (1L << (R - 64)) | (1L << (S - 64)) | (1L << (T - 64)) | (1L << (U - 64)) | (1L << (V - 64)) | (1L << (W - 64)) | (1L << (X - 64)) | (1L << (Y - 64)) | (1L << (Z - 64)) | (1L << (LEFT_CURLY_BRACE - 64)) | (1L << (PIPE - 64)) | (1L << (RIGHT_CURLY_BRACE - 64)) | (1L << (TILDE - 64)))) != 0)) ) {
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
				setState(642);
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
		enterRule(_localctx, 112, RULE_nonspacechar);
		int _la;
		try {
			setState(648);
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
				setState(645);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << POUND) | (1L << DOLLAR) | (1L << PERCENT) | (1L << AMPERSAND) | (1L << APOSTROPHE) | (1L << LEFT_PAREN) | (1L << RIGHT_PAREN))) != 0)) ) {
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
				setState(646);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PLUS) | (1L << COMMA) | (1L << DASH) | (1L << PERIOD) | (1L << SLASH) | (1L << ZERO) | (1L << ONE) | (1L << TWO) | (1L << THREE) | (1L << FOUR) | (1L << FIVE) | (1L << SIX) | (1L << SEVEN) | (1L << EIGHT) | (1L << NINE) | (1L << COLON) | (1L << SEMICOLON) | (1L << LESS_THAN) | (1L << EQUALS) | (1L << GREATER_THAN) | (1L << QUESTION) | (1L << AT) | (1L << CAP_A) | (1L << CAP_B) | (1L << CAP_C) | (1L << CAP_D) | (1L << CAP_E) | (1L << CAP_F) | (1L << CAP_G) | (1L << CAP_H) | (1L << CAP_I) | (1L << CAP_J) | (1L << CAP_K) | (1L << CAP_L) | (1L << CAP_M) | (1L << CAP_N) | (1L << CAP_O) | (1L << CAP_P) | (1L << CAP_Q) | (1L << CAP_R) | (1L << CAP_S) | (1L << CAP_T) | (1L << CAP_U) | (1L << CAP_V) | (1L << CAP_W) | (1L << CAP_X) | (1L << CAP_Y) | (1L << CAP_Z))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (LEFT_BRACE - 64)) | (1L << (BACKSLASH - 64)) | (1L << (RIGHT_BRACE - 64)) | (1L << (CARAT - 64)) | (1L << (UNDERSCORE - 64)) | (1L << (ACCENT - 64)) | (1L << (A - 64)) | (1L << (B - 64)) | (1L << (C - 64)) | (1L << (D - 64)) | (1L << (E - 64)) | (1L << (F - 64)) | (1L << (G - 64)) | (1L << (H - 64)) | (1L << (I - 64)) | (1L << (J - 64)) | (1L << (K - 64)) | (1L << (L - 64)) | (1L << (M - 64)) | (1L << (N - 64)) | (1L << (O - 64)) | (1L << (P - 64)) | (1L << (Q - 64)) | (1L << (R - 64)) | (1L << (S - 64)) | (1L << (T - 64)) | (1L << (U - 64)) | (1L << (V - 64)) | (1L << (W - 64)) | (1L << (X - 64)) | (1L << (Y - 64)) | (1L << (Z - 64)) | (1L << (LEFT_CURLY_BRACE - 64)) | (1L << (PIPE - 64)) | (1L << (RIGHT_CURLY_BRACE - 64)) | (1L << (TILDE - 64)))) != 0)) ) {
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
				setState(647);
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
		enterRule(_localctx, 114, RULE_starwithnonfslash);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(650);
			match(ASTERISK);
			setState(651);
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
		enterRule(_localctx, 116, RULE_nonfslash);
		int _la;
		try {
			setState(660);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SPACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(653);
				sp();
				}
				break;
			case TAB:
				enterOuterAlt(_localctx, 2);
				{
				setState(654);
				htab();
				}
				break;
			case CR:
				enterOuterAlt(_localctx, 3);
				{
				setState(655);
				cr();
				}
				break;
			case LF:
				enterOuterAlt(_localctx, 4);
				{
				setState(656);
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
				setState(657);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EXCLAMATION) | (1L << QUOTE) | (1L << POUND) | (1L << DOLLAR) | (1L << PERCENT) | (1L << AMPERSAND) | (1L << APOSTROPHE) | (1L << LEFT_PAREN) | (1L << RIGHT_PAREN) | (1L << ASTERISK) | (1L << PLUS) | (1L << COMMA) | (1L << DASH) | (1L << PERIOD))) != 0)) ) {
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
				setState(658);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ZERO) | (1L << ONE) | (1L << TWO) | (1L << THREE) | (1L << FOUR) | (1L << FIVE) | (1L << SIX) | (1L << SEVEN) | (1L << EIGHT) | (1L << NINE) | (1L << COLON) | (1L << SEMICOLON) | (1L << LESS_THAN) | (1L << EQUALS) | (1L << GREATER_THAN) | (1L << QUESTION) | (1L << AT) | (1L << CAP_A) | (1L << CAP_B) | (1L << CAP_C) | (1L << CAP_D) | (1L << CAP_E) | (1L << CAP_F) | (1L << CAP_G) | (1L << CAP_H) | (1L << CAP_I) | (1L << CAP_J) | (1L << CAP_K) | (1L << CAP_L) | (1L << CAP_M) | (1L << CAP_N) | (1L << CAP_O) | (1L << CAP_P) | (1L << CAP_Q) | (1L << CAP_R) | (1L << CAP_S) | (1L << CAP_T) | (1L << CAP_U) | (1L << CAP_V) | (1L << CAP_W) | (1L << CAP_X) | (1L << CAP_Y) | (1L << CAP_Z))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (LEFT_BRACE - 64)) | (1L << (BACKSLASH - 64)) | (1L << (RIGHT_BRACE - 64)) | (1L << (CARAT - 64)) | (1L << (UNDERSCORE - 64)) | (1L << (ACCENT - 64)) | (1L << (A - 64)) | (1L << (B - 64)) | (1L << (C - 64)) | (1L << (D - 64)) | (1L << (E - 64)) | (1L << (F - 64)) | (1L << (G - 64)) | (1L << (H - 64)) | (1L << (I - 64)) | (1L << (J - 64)) | (1L << (K - 64)) | (1L << (L - 64)) | (1L << (M - 64)) | (1L << (N - 64)) | (1L << (O - 64)) | (1L << (P - 64)) | (1L << (Q - 64)) | (1L << (R - 64)) | (1L << (S - 64)) | (1L << (T - 64)) | (1L << (U - 64)) | (1L << (V - 64)) | (1L << (W - 64)) | (1L << (X - 64)) | (1L << (Y - 64)) | (1L << (Z - 64)) | (1L << (LEFT_CURLY_BRACE - 64)) | (1L << (PIPE - 64)) | (1L << (RIGHT_CURLY_BRACE - 64)) | (1L << (TILDE - 64)))) != 0)) ) {
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
				setState(659);
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
		enterRule(_localctx, 118, RULE_sp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(662);
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
		enterRule(_localctx, 120, RULE_htab);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(664);
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
		enterRule(_localctx, 122, RULE_cr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(666);
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
		enterRule(_localctx, 124, RULE_lf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(668);
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
		enterRule(_localctx, 126, RULE_qm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(670);
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
		enterRule(_localctx, 128, RULE_bs);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(672);
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
		enterRule(_localctx, 130, RULE_digit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(674);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ZERO) | (1L << ONE) | (1L << TWO) | (1L << THREE) | (1L << FOUR) | (1L << FIVE) | (1L << SIX) | (1L << SEVEN) | (1L << EIGHT) | (1L << NINE))) != 0)) ) {
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
		enterRule(_localctx, 132, RULE_zero);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(676);
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
		enterRule(_localctx, 134, RULE_digitnonzero);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(678);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ONE) | (1L << TWO) | (1L << THREE) | (1L << FOUR) | (1L << FIVE) | (1L << SIX) | (1L << SEVEN) | (1L << EIGHT) | (1L << NINE))) != 0)) ) {
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
		enterRule(_localctx, 136, RULE_nonwsnonpipe);
		int _la;
		try {
			setState(683);
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
				setState(680);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EXCLAMATION) | (1L << QUOTE) | (1L << POUND) | (1L << DOLLAR) | (1L << PERCENT) | (1L << AMPERSAND) | (1L << APOSTROPHE) | (1L << LEFT_PAREN) | (1L << RIGHT_PAREN) | (1L << ASTERISK) | (1L << PLUS) | (1L << COMMA) | (1L << DASH) | (1L << PERIOD) | (1L << SLASH) | (1L << ZERO) | (1L << ONE) | (1L << TWO) | (1L << THREE) | (1L << FOUR) | (1L << FIVE) | (1L << SIX) | (1L << SEVEN) | (1L << EIGHT) | (1L << NINE) | (1L << COLON) | (1L << SEMICOLON) | (1L << LESS_THAN) | (1L << EQUALS) | (1L << GREATER_THAN) | (1L << QUESTION) | (1L << AT) | (1L << CAP_A) | (1L << CAP_B) | (1L << CAP_C) | (1L << CAP_D) | (1L << CAP_E) | (1L << CAP_F) | (1L << CAP_G) | (1L << CAP_H) | (1L << CAP_I) | (1L << CAP_J) | (1L << CAP_K) | (1L << CAP_L) | (1L << CAP_M) | (1L << CAP_N) | (1L << CAP_O) | (1L << CAP_P) | (1L << CAP_Q) | (1L << CAP_R) | (1L << CAP_S) | (1L << CAP_T) | (1L << CAP_U) | (1L << CAP_V) | (1L << CAP_W) | (1L << CAP_X) | (1L << CAP_Y) | (1L << CAP_Z))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (LEFT_BRACE - 64)) | (1L << (BACKSLASH - 64)) | (1L << (RIGHT_BRACE - 64)) | (1L << (CARAT - 64)) | (1L << (UNDERSCORE - 64)) | (1L << (ACCENT - 64)) | (1L << (A - 64)) | (1L << (B - 64)) | (1L << (C - 64)) | (1L << (D - 64)) | (1L << (E - 64)) | (1L << (F - 64)) | (1L << (G - 64)) | (1L << (H - 64)) | (1L << (I - 64)) | (1L << (J - 64)) | (1L << (K - 64)) | (1L << (L - 64)) | (1L << (M - 64)) | (1L << (N - 64)) | (1L << (O - 64)) | (1L << (P - 64)) | (1L << (Q - 64)) | (1L << (R - 64)) | (1L << (S - 64)) | (1L << (T - 64)) | (1L << (U - 64)) | (1L << (V - 64)) | (1L << (W - 64)) | (1L << (X - 64)) | (1L << (Y - 64)) | (1L << (Z - 64)) | (1L << (LEFT_CURLY_BRACE - 64)))) != 0)) ) {
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
				setState(681);
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
				setState(682);
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
		enterRule(_localctx, 138, RULE_anynonescapedchar);
		int _la;
		try {
			setState(693);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(685);
				sp();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(686);
				htab();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(687);
				cr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(688);
				lf();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(689);
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
				setState(690);
				_la = _input.LA(1);
				if ( !(((((_la - 8)) & ~0x3f) == 0 && ((1L << (_la - 8)) & ((1L << (POUND - 8)) | (1L << (DOLLAR - 8)) | (1L << (PERCENT - 8)) | (1L << (AMPERSAND - 8)) | (1L << (APOSTROPHE - 8)) | (1L << (LEFT_PAREN - 8)) | (1L << (RIGHT_PAREN - 8)) | (1L << (ASTERISK - 8)) | (1L << (PLUS - 8)) | (1L << (COMMA - 8)) | (1L << (DASH - 8)) | (1L << (PERIOD - 8)) | (1L << (SLASH - 8)) | (1L << (ZERO - 8)) | (1L << (ONE - 8)) | (1L << (TWO - 8)) | (1L << (THREE - 8)) | (1L << (FOUR - 8)) | (1L << (FIVE - 8)) | (1L << (SIX - 8)) | (1L << (SEVEN - 8)) | (1L << (EIGHT - 8)) | (1L << (NINE - 8)) | (1L << (COLON - 8)) | (1L << (SEMICOLON - 8)) | (1L << (LESS_THAN - 8)) | (1L << (EQUALS - 8)) | (1L << (GREATER_THAN - 8)) | (1L << (QUESTION - 8)) | (1L << (AT - 8)) | (1L << (CAP_A - 8)) | (1L << (CAP_B - 8)) | (1L << (CAP_C - 8)) | (1L << (CAP_D - 8)) | (1L << (CAP_E - 8)) | (1L << (CAP_F - 8)) | (1L << (CAP_G - 8)) | (1L << (CAP_H - 8)) | (1L << (CAP_I - 8)) | (1L << (CAP_J - 8)) | (1L << (CAP_K - 8)) | (1L << (CAP_L - 8)) | (1L << (CAP_M - 8)) | (1L << (CAP_N - 8)) | (1L << (CAP_O - 8)) | (1L << (CAP_P - 8)) | (1L << (CAP_Q - 8)) | (1L << (CAP_R - 8)) | (1L << (CAP_S - 8)) | (1L << (CAP_T - 8)) | (1L << (CAP_U - 8)) | (1L << (CAP_V - 8)) | (1L << (CAP_W - 8)) | (1L << (CAP_X - 8)) | (1L << (CAP_Y - 8)) | (1L << (CAP_Z - 8)) | (1L << (LEFT_BRACE - 8)))) != 0)) ) {
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
				setState(691);
				_la = _input.LA(1);
				if ( !(((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (RIGHT_BRACE - 66)) | (1L << (CARAT - 66)) | (1L << (UNDERSCORE - 66)) | (1L << (ACCENT - 66)) | (1L << (A - 66)) | (1L << (B - 66)) | (1L << (C - 66)) | (1L << (D - 66)) | (1L << (E - 66)) | (1L << (F - 66)) | (1L << (G - 66)) | (1L << (H - 66)) | (1L << (I - 66)) | (1L << (J - 66)) | (1L << (K - 66)) | (1L << (L - 66)) | (1L << (M - 66)) | (1L << (N - 66)) | (1L << (O - 66)) | (1L << (P - 66)) | (1L << (Q - 66)) | (1L << (R - 66)) | (1L << (S - 66)) | (1L << (T - 66)) | (1L << (U - 66)) | (1L << (V - 66)) | (1L << (W - 66)) | (1L << (X - 66)) | (1L << (Y - 66)) | (1L << (Z - 66)) | (1L << (LEFT_CURLY_BRACE - 66)) | (1L << (PIPE - 66)) | (1L << (RIGHT_CURLY_BRACE - 66)) | (1L << (TILDE - 66)))) != 0)) ) {
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
				setState(692);
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
		enterRule(_localctx, 140, RULE_escapedchar);
		try {
			setState(701);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(695);
				bs();
				setState(696);
				qm();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(698);
				bs();
				setState(699);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3e\u02c2\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\3\2"+
		"\3\2\3\2\3\2\3\2\5\2\u0096\n\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4"+
		"\3\4\5\4\u00a3\n\4\3\5\3\5\3\5\3\5\3\5\3\5\6\5\u00ab\n\5\r\5\16\5\u00ac"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\6\6\u00b5\n\6\r\6\16\6\u00b6\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\b\3\b\3\b\3\b\6\b\u00c3\n\b\r\b\16\b\u00c4\3\t\3\t\3\t\3\t"+
		"\3\n\3\n\3\n\5\n\u00ce\n\n\3\n\3\n\3\n\5\n\u00d3\n\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\5\n\u00dc\n\n\3\13\3\13\5\13\u00e0\n\13\3\f\3\f\3\r\3\r\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u00ee\n\16\3\17\3\17\3\20\6\20"+
		"\u00f3\n\20\r\20\16\20\u00f4\3\20\6\20\u00f8\n\20\r\20\16\20\u00f9\3\20"+
		"\6\20\u00fd\n\20\r\20\16\20\u00fe\7\20\u0101\n\20\f\20\16\20\u0104\13"+
		"\20\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u010e\n\22\3\23\3\23"+
		"\3\24\3\24\3\24\3\25\3\25\3\25\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\30"+
		"\3\31\3\31\3\31\3\31\3\31\5\31\u0125\n\31\3\32\3\32\3\32\3\32\3\33\3\33"+
		"\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\5\34\u0136\n\34\3\35\3\35"+
		"\3\35\3\35\3\35\6\35\u013d\n\35\r\35\16\35\u013e\3\36\3\36\3\36\3\36\3"+
		"\36\6\36\u0146\n\36\r\36\16\36\u0147\3\37\3\37\3\37\3\37\3\37\3\37\3\37"+
		"\3\37\5\37\u0152\n\37\3 \3 \3 \3 \5 \u0158\n \3!\3!\3!\3!\3!\6!\u015f"+
		"\n!\r!\16!\u0160\3\"\3\"\3\"\3\"\3\"\6\"\u0168\n\"\r\"\16\"\u0169\3#\3"+
		"#\3#\3#\3#\3#\3#\5#\u0173\n#\3$\3$\3$\3$\3$\5$\u017a\n$\3$\3$\3$\3$\3"+
		"$\3$\3%\3%\3%\3%\3%\5%\u0187\n%\3%\3%\3%\5%\u018c\n%\3%\3%\3%\3%\3%\3"+
		"%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\5%\u019f\n%\3&\3&\3&\3&\3\'\3\'\3("+
		"\3(\3(\3)\3)\5)\u01ac\n)\3*\3*\3+\3+\3,\3,\3-\3-\3-\5-\u01b7\n-\3.\3."+
		"\3.\3.\3.\3.\3.\3.\3.\5.\u01c2\n.\3/\3/\3/\5/\u01c7\n/\3\60\5\60\u01ca"+
		"\n\60\3\60\3\60\5\60\u01ce\n\60\3\61\3\61\6\61\u01d2\n\61\r\61\16\61\u01d3"+
		"\3\62\3\62\7\62\u01d8\n\62\f\62\16\62\u01db\13\62\3\62\5\62\u01de\n\62"+
		"\3\63\3\63\3\63\6\63\u01e3\n\63\r\63\16\63\u01e4\3\64\3\64\7\64\u01e9"+
		"\n\64\f\64\16\64\u01ec\13\64\3\64\5\64\u01ef\n\64\3\65\3\65\3\65\3\65"+
		"\3\65\6\65\u01f6\n\65\r\65\16\65\u01f7\3\65\3\65\3\65\3\65\3\65\3\65\3"+
		"\65\5\65\u0201\n\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65"+
		"\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65"+
		"\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65"+
		"\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65"+
		"\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65"+
		"\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65"+
		"\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\5\65\u025b\n\65\5\65\u025d\n"+
		"\65\3\66\3\66\3\66\3\66\3\66\7\66\u0264\n\66\f\66\16\66\u0267\13\66\3"+
		"\67\3\67\3\67\3\67\3\67\6\67\u026e\n\67\r\67\16\67\u026f\38\38\38\38\3"+
		"8\78\u0277\n8\f8\168\u027a\138\38\38\38\39\39\39\39\39\39\39\59\u0286"+
		"\n9\3:\3:\3:\5:\u028b\n:\3;\3;\3;\3<\3<\3<\3<\3<\3<\3<\5<\u0297\n<\3="+
		"\3=\3>\3>\3?\3?\3@\3@\3A\3A\3B\3B\3C\3C\3D\3D\3E\3E\3F\3F\3F\5F\u02ae"+
		"\nF\3G\3G\3G\3G\3G\3G\3G\3G\5G\u02b8\nG\3H\3H\3H\3H\3H\3H\5H\u02c0\nH"+
		"\3H\2\2I\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668"+
		":<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a"+
		"\u008c\u008e\2\30\4\2((HH\4\2\65\65UU\4\2++KK\4\2\66\66VV\4\299YY\4\2"+
		"\64\64TT\4\2\60\60PP\4\2<<\\\\\4\2::ZZ\4\2\22\22\24\24\3\2\b\20\3\2\22"+
		"e\3\2\n\20\3\2\b\25\3\2\27e\3\2\27 \3\2\30 \3\2\bb\3\2de\3\2\7\b\3\2\n"+
		"B\3\2De\2\u02e4\2\u0090\3\2\2\2\4\u0099\3\2\2\2\6\u00a2\3\2\2\2\b\u00a4"+
		"\3\2\2\2\n\u00ae\3\2\2\2\f\u00b8\3\2\2\2\16\u00be\3\2\2\2\20\u00c6\3\2"+
		"\2\2\22\u00cd\3\2\2\2\24\u00df\3\2\2\2\26\u00e1\3\2\2\2\30\u00e3\3\2\2"+
		"\2\32\u00e5\3\2\2\2\34\u00ef\3\2\2\2\36\u00f2\3\2\2\2 \u0105\3\2\2\2\""+
		"\u010d\3\2\2\2$\u010f\3\2\2\2&\u0111\3\2\2\2(\u0114\3\2\2\2*\u0117\3\2"+
		"\2\2,\u0119\3\2\2\2.\u011c\3\2\2\2\60\u0124\3\2\2\2\62\u0126\3\2\2\2\64"+
		"\u012a\3\2\2\2\66\u0131\3\2\2\28\u013c\3\2\2\2:\u0145\3\2\2\2<\u0151\3"+
		"\2\2\2>\u0153\3\2\2\2@\u015e\3\2\2\2B\u0167\3\2\2\2D\u0172\3\2\2\2F\u0179"+
		"\3\2\2\2H\u0186\3\2\2\2J\u01a0\3\2\2\2L\u01a4\3\2\2\2N\u01a6\3\2\2\2P"+
		"\u01ab\3\2\2\2R\u01ad\3\2\2\2T\u01af\3\2\2\2V\u01b1\3\2\2\2X\u01b6\3\2"+
		"\2\2Z\u01c1\3\2\2\2\\\u01c6\3\2\2\2^\u01c9\3\2\2\2`\u01d1\3\2\2\2b\u01dd"+
		"\3\2\2\2d\u01df\3\2\2\2f\u01ee\3\2\2\2h\u025c\3\2\2\2j\u0265\3\2\2\2l"+
		"\u026d\3\2\2\2n\u0271\3\2\2\2p\u0285\3\2\2\2r\u028a\3\2\2\2t\u028c\3\2"+
		"\2\2v\u0296\3\2\2\2x\u0298\3\2\2\2z\u029a\3\2\2\2|\u029c\3\2\2\2~\u029e"+
		"\3\2\2\2\u0080\u02a0\3\2\2\2\u0082\u02a2\3\2\2\2\u0084\u02a4\3\2\2\2\u0086"+
		"\u02a6\3\2\2\2\u0088\u02a8\3\2\2\2\u008a\u02ad\3\2\2\2\u008c\u02b7\3\2"+
		"\2\2\u008e\u02bf\3\2\2\2\u0090\u0095\5j\66\2\u0091\u0096\5\4\3\2\u0092"+
		"\u0096\5\6\4\2\u0093\u0096\5\16\b\2\u0094\u0096\5\22\n\2\u0095\u0091\3"+
		"\2\2\2\u0095\u0092\3\2\2\2\u0095\u0093\3\2\2\2\u0095\u0094\3\2\2\2\u0096"+
		"\u0097\3\2\2\2\u0097\u0098\5j\66\2\u0098\3\3\2\2\2\u0099\u009a\5\22\n"+
		"\2\u009a\u009b\5j\66\2\u009b\u009c\7!\2\2\u009c\u009d\5j\66\2\u009d\u009e"+
		"\5\66\34\2\u009e\5\3\2\2\2\u009f\u00a3\5\b\5\2\u00a0\u00a3\5\n\6\2\u00a1"+
		"\u00a3\5\f\7\2\u00a2\u009f\3\2\2\2\u00a2\u00a0\3\2\2\2\u00a2\u00a1\3\2"+
		"\2\2\u00a3\7\3\2\2\2\u00a4\u00aa\5\22\n\2\u00a5\u00a6\5j\66\2\u00a6\u00a7"+
		"\5\60\31\2\u00a7\u00a8\5j\66\2\u00a8\u00a9\5\22\n\2\u00a9\u00ab\3\2\2"+
		"\2\u00aa\u00a5\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00aa\3\2\2\2\u00ac\u00ad"+
		"\3\2\2\2\u00ad\t\3\2\2\2\u00ae\u00b4\5\22\n\2\u00af\u00b0\5j\66\2\u00b0"+
		"\u00b1\5\62\32\2\u00b1\u00b2\5j\66\2\u00b2\u00b3\5\22\n\2\u00b3\u00b5"+
		"\3\2\2\2\u00b4\u00af\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b6"+
		"\u00b7\3\2\2\2\u00b7\13\3\2\2\2\u00b8\u00b9\5\22\n\2\u00b9\u00ba\5j\66"+
		"\2\u00ba\u00bb\5\64\33\2\u00bb\u00bc\5j\66\2\u00bc\u00bd\5\22\n\2\u00bd"+
		"\r\3\2\2\2\u00be\u00c2\5\22\n\2\u00bf\u00c0\5j\66\2\u00c0\u00c1\5\20\t"+
		"\2\u00c1\u00c3\3\2\2\2\u00c2\u00bf\3\2\2\2\u00c3\u00c4\3\2\2\2\u00c4\u00c2"+
		"\3\2\2\2\u00c4\u00c5\3\2\2\2\u00c5\17\3\2\2\2\u00c6\u00c7\5\26\f\2\u00c7"+
		"\u00c8\5j\66\2\u00c8\u00c9\5V,\2\u00c9\21\3\2\2\2\u00ca\u00cb\5\"\22\2"+
		"\u00cb\u00cc\5j\66\2\u00cc\u00ce\3\2\2\2\u00cd\u00ca\3\2\2\2\u00cd\u00ce"+
		"\3\2\2\2\u00ce\u00d2\3\2\2\2\u00cf\u00d0\5\30\r\2\u00d0\u00d1\5j\66\2"+
		"\u00d1\u00d3\3\2\2\2\u00d2\u00cf\3\2\2\2\u00d2\u00d3\3\2\2\2\u00d3\u00db"+
		"\3\2\2\2\u00d4\u00dc\5\24\13\2\u00d5\u00d6\7\17\2\2\u00d6\u00d7\5j\66"+
		"\2\u00d7\u00d8\5\2\2\2\u00d8\u00d9\5j\66\2\u00d9\u00da\7\20\2\2\u00da"+
		"\u00dc\3\2\2\2\u00db\u00d4\3\2\2\2\u00db\u00d5\3\2\2\2\u00dc\23\3\2\2"+
		"\2\u00dd\u00e0\5\32\16\2\u00de\u00e0\5 \21\2\u00df\u00dd\3\2\2\2\u00df"+
		"\u00de\3\2\2\2\u00e0\25\3\2\2\2\u00e1\u00e2\7\25\2\2\u00e2\27\3\2\2\2"+
		"\u00e3\u00e4\7E\2\2\u00e4\31\3\2\2\2\u00e5\u00ed\5\34\17\2\u00e6\u00e7"+
		"\5j\66\2\u00e7\u00e8\7c\2\2\u00e8\u00e9\5j\66\2\u00e9\u00ea\5\36\20\2"+
		"\u00ea\u00eb\5j\66\2\u00eb\u00ec\7c\2\2\u00ec\u00ee\3\2\2\2\u00ed\u00e6"+
		"\3\2\2\2\u00ed\u00ee\3\2\2\2\u00ee\33\3\2\2\2\u00ef\u00f0\5h\65\2\u00f0"+
		"\35\3\2\2\2\u00f1\u00f3\5\u008aF\2\u00f2\u00f1\3\2\2\2\u00f3\u00f4\3\2"+
		"\2\2\u00f4\u00f2\3\2\2\2\u00f4\u00f5\3\2\2\2\u00f5\u0102\3\2\2\2\u00f6"+
		"\u00f8\5x=\2\u00f7\u00f6\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9\u00f7\3\2\2"+
		"\2\u00f9\u00fa\3\2\2\2\u00fa\u00fc\3\2\2\2\u00fb\u00fd\5\u008aF\2\u00fc"+
		"\u00fb\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe\u00fc\3\2\2\2\u00fe\u00ff\3\2"+
		"\2\2\u00ff\u0101\3\2\2\2\u0100\u00f7\3\2\2\2\u0101\u0104\3\2\2\2\u0102"+
		"\u0100\3\2\2\2\u0102\u0103\3\2\2\2\u0103\37\3\2\2\2\u0104\u0102\3\2\2"+
		"\2\u0105\u0106\7\21\2\2\u0106!\3\2\2\2\u0107\u010e\5(\25\2\u0108\u010e"+
		"\5&\24\2\u0109\u010e\5$\23\2\u010a\u010e\5.\30\2\u010b\u010e\5,\27\2\u010c"+
		"\u010e\5*\26\2\u010d\u0107\3\2\2\2\u010d\u0108\3\2\2\2\u010d\u0109\3\2"+
		"\2\2\u010d\u010a\3\2\2\2\u010d\u010b\3\2\2\2\u010d\u010c\3\2\2\2\u010e"+
		"#\3\2\2\2\u010f\u0110\7#\2\2\u0110%\3\2\2\2\u0111\u0112\7#\2\2\u0112\u0113"+
		"\7#\2\2\u0113\'\3\2\2\2\u0114\u0115\7#\2\2\u0115\u0116\7\b\2\2\u0116)"+
		"\3\2\2\2\u0117\u0118\7%\2\2\u0118+\3\2\2\2\u0119\u011a\7%\2\2\u011a\u011b"+
		"\7%\2\2\u011b-\3\2\2\2\u011c\u011d\7%\2\2\u011d\u011e\7\b\2\2\u011e/\3"+
		"\2\2\2\u011f\u0120\t\2\2\2\u0120\u0121\t\3\2\2\u0121\u0122\t\4\2\2\u0122"+
		"\u0125\5l\67\2\u0123\u0125\7\23\2\2\u0124\u011f\3\2\2\2\u0124\u0123\3"+
		"\2\2\2\u0125\61\3\2\2\2\u0126\u0127\t\5\2\2\u0127\u0128\t\6\2\2\u0128"+
		"\u0129\5l\67\2\u0129\63\3\2\2\2\u012a\u012b\t\7\2\2\u012b\u012c\t\b\2"+
		"\2\u012c\u012d\t\3\2\2\u012d\u012e\t\t\2\2\u012e\u012f\t\n\2\2\u012f\u0130"+
		"\5l\67\2\u0130\65\3\2\2\2\u0131\u0132\5<\37\2\u0132\u0135\5j\66\2\u0133"+
		"\u0136\58\35\2\u0134\u0136\5:\36\2\u0135\u0133\3\2\2\2\u0135\u0134\3\2"+
		"\2\2\u0135\u0136\3\2\2\2\u0136\67\3\2\2\2\u0137\u0138\5j\66\2\u0138\u0139"+
		"\5\60\31\2\u0139\u013a\5j\66\2\u013a\u013b\5<\37\2\u013b\u013d\3\2\2\2"+
		"\u013c\u0137\3\2\2\2\u013d\u013e\3\2\2\2\u013e\u013c\3\2\2\2\u013e\u013f"+
		"\3\2\2\2\u013f9\3\2\2\2\u0140\u0141\5j\66\2\u0141\u0142\5\62\32\2\u0142"+
		"\u0143\5j\66\2\u0143\u0144\5<\37\2\u0144\u0146\3\2\2\2\u0145\u0140\3\2"+
		"\2\2\u0146\u0147\3\2\2\2\u0147\u0145\3\2\2\2\u0147\u0148\3\2\2\2\u0148"+
		";\3\2\2\2\u0149\u0152\5> \2\u014a\u0152\5F$\2\u014b\u014c\7\17\2\2\u014c"+
		"\u014d\5j\66\2\u014d\u014e\5\66\34\2\u014e\u014f\5j\66\2\u014f\u0150\7"+
		"\20\2\2\u0150\u0152\3\2\2\2\u0151\u0149\3\2\2\2\u0151\u014a\3\2\2\2\u0151"+
		"\u014b\3\2\2\2\u0152=\3\2\2\2\u0153\u0154\5D#\2\u0154\u0157\5j\66\2\u0155"+
		"\u0158\5@!\2\u0156\u0158\5B\"\2\u0157\u0155\3\2\2\2\u0157\u0156\3\2\2"+
		"\2\u0157\u0158\3\2\2\2\u0158?\3\2\2\2\u0159\u015a\5j\66\2\u015a\u015b"+
		"\5\60\31\2\u015b\u015c\5j\66\2\u015c\u015d\5D#\2\u015d\u015f\3\2\2\2\u015e"+
		"\u0159\3\2\2\2\u015f\u0160\3\2\2\2\u0160\u015e\3\2\2\2\u0160\u0161\3\2"+
		"\2\2\u0161A\3\2\2\2\u0162\u0163\5j\66\2\u0163\u0164\5\62\32\2\u0164\u0165"+
		"\5j\66\2\u0165\u0166\5D#\2\u0166\u0168\3\2\2\2\u0167\u0162\3\2\2\2\u0168"+
		"\u0169\3\2\2\2\u0169\u0167\3\2\2\2\u0169\u016a\3\2\2\2\u016aC\3\2\2\2"+
		"\u016b\u0173\5H%\2\u016c\u016d\7\17\2\2\u016d\u016e\5j\66\2\u016e\u016f"+
		"\5> \2\u016f\u0170\5j\66\2\u0170\u0171\7\20\2\2\u0171\u0173\3\2\2\2\u0172"+
		"\u016b\3\2\2\2\u0172\u016c\3\2\2\2\u0173E\3\2\2\2\u0174\u0175\7B\2\2\u0175"+
		"\u0176\5J&\2\u0176\u0177\7D\2\2\u0177\u0178\5j\66\2\u0178\u017a\3\2\2"+
		"\2\u0179\u0174\3\2\2\2\u0179\u017a\3\2\2\2\u017a\u017b\3\2\2\2\u017b\u017c"+
		"\7b\2\2\u017c\u017d\5j\66\2\u017d\u017e\5> \2\u017e\u017f\5j\66\2\u017f"+
		"\u0180\7d\2\2\u0180G\3\2\2\2\u0181\u0182\7B\2\2\u0182\u0183\5J&\2\u0183"+
		"\u0184\7D\2\2\u0184\u0185\5j\66\2\u0185\u0187\3\2\2\2\u0186\u0181\3\2"+
		"\2\2\u0186\u0187\3\2\2\2\u0187\u018b\3\2\2\2\u0188\u0189\5T+\2\u0189\u018a"+
		"\5j\66\2\u018a\u018c\3\2\2\2\u018b\u0188\3\2\2\2\u018b\u018c\3\2\2\2\u018c"+
		"\u018d\3\2\2\2\u018d\u018e\5V,\2\u018e\u019e\5j\66\2\u018f\u0190\5X-\2"+
		"\u0190\u0191\5j\66\2\u0191\u0192\5\22\n\2\u0192\u019f\3\2\2\2\u0193\u0194"+
		"\5Z.\2\u0194\u0195\5j\66\2\u0195\u0196\7\n\2\2\u0196\u0197\5^\60\2\u0197"+
		"\u019f\3\2\2\2\u0198\u0199\5\\/\2\u0199\u019a\5j\66\2\u019a\u019b\5\u0080"+
		"A\2\u019b\u019c\5`\61\2\u019c\u019d\5\u0080A\2\u019d\u019f\3\2\2\2\u019e"+
		"\u018f\3\2\2\2\u019e\u0193\3\2\2\2\u019e\u0198\3\2\2\2\u019fI\3\2\2\2"+
		"\u01a0\u01a1\5L\'\2\u01a1\u01a2\5N(\2\u01a2\u01a3\5P)\2\u01a3K\3\2\2\2"+
		"\u01a4\u01a5\5f\64\2\u01a5M\3\2\2\2\u01a6\u01a7\7\25\2\2\u01a7\u01a8\7"+
		"\25\2\2\u01a8O\3\2\2\2\u01a9\u01ac\5f\64\2\u01aa\u01ac\5R*\2\u01ab\u01a9"+
		"\3\2\2\2\u01ab\u01aa\3\2\2\2\u01acQ\3\2\2\2\u01ad\u01ae\7\21\2\2\u01ae"+
		"S\3\2\2\2\u01af\u01b0\79\2\2\u01b0U\3\2\2\2\u01b1\u01b2\5\22\n\2\u01b2"+
		"W\3\2\2\2\u01b3\u01b7\7$\2\2\u01b4\u01b5\7\b\2\2\u01b5\u01b7\7$\2\2\u01b6"+
		"\u01b3\3\2\2\2\u01b6\u01b4\3\2\2\2\u01b7Y\3\2\2\2\u01b8\u01c2\7$\2\2\u01b9"+
		"\u01ba\7\b\2\2\u01ba\u01c2\7$\2\2\u01bb\u01bc\7#\2\2\u01bc\u01c2\7$\2"+
		"\2\u01bd\u01c2\7#\2\2\u01be\u01bf\7%\2\2\u01bf\u01c2\7$\2\2\u01c0\u01c2"+
		"\7%\2\2\u01c1\u01b8\3\2\2\2\u01c1\u01b9\3\2\2\2\u01c1\u01bb\3\2\2\2\u01c1"+
		"\u01bd\3\2\2\2\u01c1\u01be\3\2\2\2\u01c1\u01c0\3\2\2\2\u01c2[\3\2\2\2"+
		"\u01c3\u01c7\7$\2\2\u01c4\u01c5\7\b\2\2\u01c5\u01c7\7$\2\2\u01c6\u01c3"+
		"\3\2\2\2\u01c6\u01c4\3\2\2\2\u01c7]\3\2\2\2\u01c8\u01ca\t\13\2\2\u01c9"+
		"\u01c8\3\2\2\2\u01c9\u01ca\3\2\2\2\u01ca\u01cd\3\2\2\2\u01cb\u01ce\5d"+
		"\63\2\u01cc\u01ce\5b\62\2\u01cd\u01cb\3\2\2\2\u01cd\u01cc\3\2\2\2\u01ce"+
		"_\3\2\2\2\u01cf\u01d2\5\u008cG\2\u01d0\u01d2\5\u008eH\2\u01d1\u01cf\3"+
		"\2\2\2\u01d1\u01d0\3\2\2\2\u01d2\u01d3\3\2\2\2\u01d3\u01d1\3\2\2\2\u01d3"+
		"\u01d4\3\2\2\2\u01d4a\3\2\2\2\u01d5\u01d9\5\u0088E\2\u01d6\u01d8\5\u0084"+
		"C\2\u01d7\u01d6\3\2\2\2\u01d8\u01db\3\2\2\2\u01d9\u01d7\3\2\2\2\u01d9"+
		"\u01da\3\2\2\2\u01da\u01de\3\2\2\2\u01db\u01d9\3\2\2\2\u01dc\u01de\5\u0086"+
		"D\2\u01dd\u01d5\3\2\2\2\u01dd\u01dc\3\2\2\2\u01dec\3\2\2\2\u01df\u01e0"+
		"\5b\62\2\u01e0\u01e2\7\25\2\2\u01e1\u01e3\5\u0084C\2\u01e2\u01e1\3\2\2"+
		"\2\u01e3\u01e4\3\2\2\2\u01e4\u01e2\3\2\2\2\u01e4\u01e5\3\2\2\2\u01e5e"+
		"\3\2\2\2\u01e6\u01ea\5\u0088E\2\u01e7\u01e9\5\u0084C\2\u01e8\u01e7\3\2"+
		"\2\2\u01e9\u01ec\3\2\2\2\u01ea\u01e8\3\2\2\2\u01ea\u01eb\3\2\2\2\u01eb"+
		"\u01ef\3\2\2\2\u01ec\u01ea\3\2\2\2\u01ed\u01ef\5\u0086D\2\u01ee\u01e6"+
		"\3\2\2\2\u01ee\u01ed\3\2\2\2\u01efg\3\2\2\2\u01f0\u01f1\7O\2\2\u01f1\u01f2"+
		"\7[\2\2\u01f2\u01f3\7[\2\2\u01f3\u01f5\7W\2\2\u01f4\u01f6\5r:\2\u01f5"+
		"\u01f4\3\2\2\2\u01f6\u01f7\3\2\2\2\u01f7\u01f5\3\2\2\2\u01f7\u01f8\3\2"+
		"\2\2\u01f8\u025d\3\2\2\2\u01f9\u01fa\5\u0088E\2\u01fa\u01fb\5\u0084C\2"+
		"\u01fb\u01fc\5\u0084C\2\u01fc\u01fd\5\u0084C\2\u01fd\u01fe\5\u0084C\2"+
		"\u01fe\u025a\5\u0084C\2\u01ff\u0201\5\u0084C\2\u0200\u01ff\3\2\2\2\u0200"+
		"\u0201\3\2\2\2\u0201\u025b\3\2\2\2\u0202\u0203\5\u0084C\2\u0203\u0204"+
		"\5\u0084C\2\u0204\u025b\3\2\2\2\u0205\u0206\5\u0084C\2\u0206\u0207\5\u0084"+
		"C\2\u0207\u0208\5\u0084C\2\u0208\u025b\3\2\2\2\u0209\u020a\5\u0084C\2"+
		"\u020a\u020b\5\u0084C\2\u020b\u020c\5\u0084C\2\u020c\u020d\5\u0084C\2"+
		"\u020d\u025b\3\2\2\2\u020e\u020f\5\u0084C\2\u020f\u0210\5\u0084C\2\u0210"+
		"\u0211\5\u0084C\2\u0211\u0212\5\u0084C\2\u0212\u0213\5\u0084C\2\u0213"+
		"\u025b\3\2\2\2\u0214\u0215\5\u0084C\2\u0215\u0216\5\u0084C\2\u0216\u0217"+
		"\5\u0084C\2\u0217\u0218\5\u0084C\2\u0218\u0219\5\u0084C\2\u0219\u021a"+
		"\5\u0084C\2\u021a\u025b\3\2\2\2\u021b\u021c\5\u0084C\2\u021c\u021d\5\u0084"+
		"C\2\u021d\u021e\5\u0084C\2\u021e\u021f\5\u0084C\2\u021f\u0220\5\u0084"+
		"C\2\u0220\u0221\5\u0084C\2\u0221\u0222\5\u0084C\2\u0222\u025b\3\2\2\2"+
		"\u0223\u0224\5\u0084C\2\u0224\u0225\5\u0084C\2\u0225\u0226\5\u0084C\2"+
		"\u0226\u0227\5\u0084C\2\u0227\u0228\5\u0084C\2\u0228\u0229\5\u0084C\2"+
		"\u0229\u022a\5\u0084C\2\u022a\u022b\5\u0084C\2\u022b\u025b\3\2\2\2\u022c"+
		"\u022d\5\u0084C\2\u022d\u022e\5\u0084C\2\u022e\u022f\5\u0084C\2\u022f"+
		"\u0230\5\u0084C\2\u0230\u0231\5\u0084C\2\u0231\u0232\5\u0084C\2\u0232"+
		"\u0233\5\u0084C\2\u0233\u0234\5\u0084C\2\u0234\u0235\5\u0084C\2\u0235"+
		"\u025b\3\2\2\2\u0236\u0237\5\u0084C\2\u0237\u0238\5\u0084C\2\u0238\u0239"+
		"\5\u0084C\2\u0239\u023a\5\u0084C\2\u023a\u023b\5\u0084C\2\u023b\u023c"+
		"\5\u0084C\2\u023c\u023d\5\u0084C\2\u023d\u023e\5\u0084C\2\u023e\u023f"+
		"\5\u0084C\2\u023f\u0240\5\u0084C\2\u0240\u025b\3\2\2\2\u0241\u0242\5\u0084"+
		"C\2\u0242\u0243\5\u0084C\2\u0243\u0244\5\u0084C\2\u0244\u0245\5\u0084"+
		"C\2\u0245\u0246\5\u0084C\2\u0246\u0247\5\u0084C\2\u0247\u0248\5\u0084"+
		"C\2\u0248\u0249\5\u0084C\2\u0249\u024a\5\u0084C\2\u024a\u024b\5\u0084"+
		"C\2\u024b\u024c\5\u0084C\2\u024c\u025b\3\2\2\2\u024d\u024e\5\u0084C\2"+
		"\u024e\u024f\5\u0084C\2\u024f\u0250\5\u0084C\2\u0250\u0251\5\u0084C\2"+
		"\u0251\u0252\5\u0084C\2\u0252\u0253\5\u0084C\2\u0253\u0254\5\u0084C\2"+
		"\u0254\u0255\5\u0084C\2\u0255\u0256\5\u0084C\2\u0256\u0257\5\u0084C\2"+
		"\u0257\u0258\5\u0084C\2\u0258\u0259\5\u0084C\2\u0259\u025b\3\2\2\2\u025a"+
		"\u0200\3\2\2\2\u025a\u0202\3\2\2\2\u025a\u0205\3\2\2\2\u025a\u0209\3\2"+
		"\2\2\u025a\u020e\3\2\2\2\u025a\u0214\3\2\2\2\u025a\u021b\3\2\2\2\u025a"+
		"\u0223\3\2\2\2\u025a\u022c\3\2\2\2\u025a\u0236\3\2\2\2\u025a\u0241\3\2"+
		"\2\2\u025a\u024d\3\2\2\2\u025b\u025d\3\2\2\2\u025c\u01f0\3\2\2\2\u025c"+
		"\u01f9\3\2\2\2\u025di\3\2\2\2\u025e\u0264\5x=\2\u025f\u0264\5z>\2\u0260"+
		"\u0264\5|?\2\u0261\u0264\5~@\2\u0262\u0264\5n8\2\u0263\u025e\3\2\2\2\u0263"+
		"\u025f\3\2\2\2\u0263\u0260\3\2\2\2\u0263\u0261\3\2\2\2\u0263\u0262\3\2"+
		"\2\2\u0264\u0267\3\2\2\2\u0265\u0263\3\2\2\2\u0265\u0266\3\2\2\2\u0266"+
		"k\3\2\2\2\u0267\u0265\3\2\2\2\u0268\u026e\5x=\2\u0269\u026e\5z>\2\u026a"+
		"\u026e\5|?\2\u026b\u026e\5~@\2\u026c\u026e\5n8\2\u026d\u0268\3\2\2\2\u026d"+
		"\u0269\3\2\2\2\u026d\u026a\3\2\2\2\u026d\u026b\3\2\2\2\u026d\u026c\3\2"+
		"\2\2\u026e\u026f\3\2\2\2\u026f\u026d\3\2\2\2\u026f\u0270\3\2\2\2\u0270"+
		"m\3\2\2\2\u0271\u0272\7\26\2\2\u0272\u0273\7\21\2\2\u0273\u0278\3\2\2"+
		"\2\u0274\u0277\5p9\2\u0275\u0277\5t;\2\u0276\u0274\3\2\2\2\u0276\u0275"+
		"\3\2\2\2\u0277\u027a\3\2\2\2\u0278\u0276\3\2\2\2\u0278\u0279\3\2\2\2\u0279"+
		"\u027b\3\2\2\2\u027a\u0278\3\2\2\2\u027b\u027c\7\21\2\2\u027c\u027d\7"+
		"\26\2\2\u027do\3\2\2\2\u027e\u0286\5x=\2\u027f\u0286\5z>\2\u0280\u0286"+
		"\5|?\2\u0281\u0286\5~@\2\u0282\u0286\t\f\2\2\u0283\u0286\t\r\2\2\u0284"+
		"\u0286\7\3\2\2\u0285\u027e\3\2\2\2\u0285\u027f\3\2\2\2\u0285\u0280\3\2"+
		"\2\2\u0285\u0281\3\2\2\2\u0285\u0282\3\2\2\2\u0285\u0283\3\2\2\2\u0285"+
		"\u0284\3\2\2\2\u0286q\3\2\2\2\u0287\u028b\t\16\2\2\u0288\u028b\t\r\2\2"+
		"\u0289\u028b\7\3\2\2\u028a\u0287\3\2\2\2\u028a\u0288\3\2\2\2\u028a\u0289"+
		"\3\2\2\2\u028bs\3\2\2\2\u028c\u028d\7\21\2\2\u028d\u028e\5v<\2\u028eu"+
		"\3\2\2\2\u028f\u0297\5x=\2\u0290\u0297\5z>\2\u0291\u0297\5|?\2\u0292\u0297"+
		"\5~@\2\u0293\u0297\t\17\2\2\u0294\u0297\t\20\2\2\u0295\u0297\7\3\2\2\u0296"+
		"\u028f\3\2\2\2\u0296\u0290\3\2\2\2\u0296\u0291\3\2\2\2\u0296\u0292\3\2"+
		"\2\2\u0296\u0293\3\2\2\2\u0296\u0294\3\2\2\2\u0296\u0295\3\2\2\2\u0297"+
		"w\3\2\2\2\u0298\u0299\7\7\2\2\u0299y\3\2\2\2\u029a\u029b\7\4\2\2\u029b"+
		"{\3\2\2\2\u029c\u029d\7\6\2\2\u029d}\3\2\2\2\u029e\u029f\7\5\2\2\u029f"+
		"\177\3\2\2\2\u02a0\u02a1\7\t\2\2\u02a1\u0081\3\2\2\2\u02a2\u02a3\7C\2"+
		"\2\u02a3\u0083\3\2\2\2\u02a4\u02a5\t\21\2\2\u02a5\u0085\3\2\2\2\u02a6"+
		"\u02a7\7\27\2\2\u02a7\u0087\3\2\2\2\u02a8\u02a9\t\22\2\2\u02a9\u0089\3"+
		"\2\2\2\u02aa\u02ae\t\23\2\2\u02ab\u02ae\t\24\2\2\u02ac\u02ae\7\3\2\2\u02ad"+
		"\u02aa\3\2\2\2\u02ad\u02ab\3\2\2\2\u02ad\u02ac\3\2\2\2\u02ae\u008b\3\2"+
		"\2\2\u02af\u02b8\5x=\2\u02b0\u02b8\5z>\2\u02b1\u02b8\5|?\2\u02b2\u02b8"+
		"\5~@\2\u02b3\u02b8\t\25\2\2\u02b4\u02b8\t\26\2\2\u02b5\u02b8\t\27\2\2"+
		"\u02b6\u02b8\7\3\2\2\u02b7\u02af\3\2\2\2\u02b7\u02b0\3\2\2\2\u02b7\u02b1"+
		"\3\2\2\2\u02b7\u02b2\3\2\2\2\u02b7\u02b3\3\2\2\2\u02b7\u02b4\3\2\2\2\u02b7"+
		"\u02b5\3\2\2\2\u02b7\u02b6\3\2\2\2\u02b8\u008d\3\2\2\2\u02b9\u02ba\5\u0082"+
		"B\2\u02ba\u02bb\5\u0080A\2\u02bb\u02c0\3\2\2\2\u02bc\u02bd\5\u0082B\2"+
		"\u02bd\u02be\5\u0082B\2\u02be\u02c0\3\2\2\2\u02bf\u02b9\3\2\2\2\u02bf"+
		"\u02bc\3\2\2\2\u02c0\u008f\3\2\2\2;\u0095\u00a2\u00ac\u00b6\u00c4\u00cd"+
		"\u00d2\u00db\u00df\u00ed\u00f4\u00f9\u00fe\u0102\u010d\u0124\u0135\u013e"+
		"\u0147\u0151\u0157\u0160\u0169\u0172\u0179\u0186\u018b\u019e\u01ab\u01b6"+
		"\u01c1\u01c6\u01c9\u01cd\u01d1\u01d3\u01d9\u01dd\u01e4\u01ea\u01ee\u01f7"+
		"\u0200\u025a\u025c\u0263\u0265\u026d\u026f\u0276\u0278\u0285\u028a\u0296"+
		"\u02ad\u02b7\u02bf";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
