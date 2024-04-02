// Generated from C:/Users/david/CloudStation/EhealthTrust/DiscoveryDataService/IMAPI/api/src/main/grammars/ECL.g4 by ANTLR 4.13.1
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
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		UTF8_LETTER=1, TAB=2, LF=3, CR=4, SPACE=5, EXCLAMATION=6, QUOTE=7, HASH=8, 
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
		RULE_memberof = 12, RULE_refsetfieldset = 13, RULE_refsetfield = 14, RULE_refsetfieldname = 15, 
		RULE_refsetfieldref = 16, RULE_eclconceptreference = 17, RULE_eclconceptreferenceset = 18, 
		RULE_conceptid = 19, RULE_term = 20, RULE_wildcard = 21, RULE_constraintoperator = 22, 
		RULE_descendantof = 23, RULE_descendantorselfof = 24, RULE_childof = 25, 
		RULE_childorselfof = 26, RULE_ancestorof = 27, RULE_ancestororselfof = 28, 
		RULE_parentof = 29, RULE_parentorselfof = 30, RULE_conjunction = 31, RULE_disjunction = 32, 
		RULE_exclusion = 33, RULE_eclrefinement = 34, RULE_conjunctionrefinementset = 35, 
		RULE_disjunctionrefinementset = 36, RULE_subrefinement = 37, RULE_eclattributeset = 38, 
		RULE_conjunctionattributeset = 39, RULE_disjunctionattributeset = 40, 
		RULE_subattributeset = 41, RULE_eclattributegroup = 42, RULE_eclattribute = 43, 
		RULE_cardinality = 44, RULE_minvalue = 45, RULE_to = 46, RULE_maxvalue = 47, 
		RULE_many = 48, RULE_reverseflag = 49, RULE_eclattributename = 50, RULE_expressioncomparisonoperator = 51, 
		RULE_numericcomparisonoperator = 52, RULE_timecomparisonoperator = 53, 
		RULE_stringcomparisonoperator = 54, RULE_booleancomparisonoperator = 55, 
		RULE_descriptionfilterconstraint = 56, RULE_descriptionfilter = 57, RULE_termfilter = 58, 
		RULE_termkeyword = 59, RULE_typedsearchterm = 60, RULE_typedsearchtermset = 61, 
		RULE_wild = 62, RULE_match = 63, RULE_matchsearchterm = 64, RULE_matchsearchtermset = 65, 
		RULE_wildsearchterm = 66, RULE_wildsearchtermset = 67, RULE_languagefilter = 68, 
		RULE_language = 69, RULE_languagecode = 70, RULE_languagecodeset = 71, 
		RULE_typefilter = 72, RULE_typeidfilter = 73, RULE_typeid = 74, RULE_typetokenfilter = 75, 
		RULE_type = 76, RULE_typetoken = 77, RULE_typetokenset = 78, RULE_synonym = 79, 
		RULE_fullyspecifiedname = 80, RULE_definition = 81, RULE_dialectfilter = 82, 
		RULE_dialectidfilter = 83, RULE_dialectid = 84, RULE_dialectaliasfilter = 85, 
		RULE_dialect = 86, RULE_dialectalias = 87, RULE_dialectaliasset = 88, 
		RULE_dialectidset = 89, RULE_acceptabilityset = 90, RULE_acceptabilityconceptreferenceset = 91, 
		RULE_acceptabilitytokenset = 92, RULE_acceptabilitytoken = 93, RULE_acceptable = 94, 
		RULE_preferred = 95, RULE_conceptfilterconstraint = 96, RULE_conceptfilter = 97, 
		RULE_definitionstatusfilter = 98, RULE_definitionstatusidfilter = 99, 
		RULE_definitionstatusidkeyword = 100, RULE_definitionstatustokenfilter = 101, 
		RULE_definitionstatuskeyword = 102, RULE_definitionstatustoken = 103, 
		RULE_definitionstatustokenset = 104, RULE_primitivetoken = 105, RULE_definedtoken = 106, 
		RULE_modulefilter = 107, RULE_moduleidkeyword = 108, RULE_effectivetimefilter = 109, 
		RULE_effectivetimekeyword = 110, RULE_timevalue = 111, RULE_timevalueset = 112, 
		RULE_year = 113, RULE_month = 114, RULE_day = 115, RULE_activefilter = 116, 
		RULE_activekeyword = 117, RULE_activevalue = 118, RULE_activetruevalue = 119, 
		RULE_activefalsevalue = 120, RULE_memberfilterconstraint = 121, RULE_memberfilter = 122, 
		RULE_memberfieldfilter = 123, RULE_historysupplement = 124, RULE_historykeyword = 125, 
		RULE_historyprofilesuffix = 126, RULE_historyminimumsuffix = 127, RULE_historymoderatesuffix = 128, 
		RULE_historymaximumsuffix = 129, RULE_historysubset = 130, RULE_numericvalue = 131, 
		RULE_stringvalue = 132, RULE_integervalue = 133, RULE_decimalvalue = 134, 
		RULE_booleanvalue = 135, RULE_true_1 = 136, RULE_false_1 = 137, RULE_nonnegativeintegervalue = 138, 
		RULE_sctid = 139, RULE_ws = 140, RULE_mws = 141, RULE_comment = 142, RULE_nonstarchar = 143, 
		RULE_starwithnonfslash = 144, RULE_nonfslash = 145, RULE_sp = 146, RULE_htab = 147, 
		RULE_cr = 148, RULE_lf = 149, RULE_qm = 150, RULE_bs = 151, RULE_star = 152, 
		RULE_digit = 153, RULE_zero = 154, RULE_digitnonzero = 155, RULE_nonwsnonpipe = 156, 
		RULE_anynonescapedchar = 157, RULE_escapedchar = 158, RULE_escapedwildchar = 159, 
		RULE_nonwsnonescapedchar = 160, RULE_alpha = 161, RULE_dash = 162;
	private static String[] makeRuleNames() {
		return new String[] {
			"ecl", "expressionconstraint", "refinedexpressionconstraint", "compoundexpressionconstraint", 
			"conjunctionexpressionconstraint", "disjunctionexpressionconstraint", 
			"exclusionexpressionconstraint", "dottedexpressionconstraint", "dottedexpressionattribute", 
			"subexpressionconstraint", "eclfocusconcept", "dot", "memberof", "refsetfieldset", 
			"refsetfield", "refsetfieldname", "refsetfieldref", "eclconceptreference", 
			"eclconceptreferenceset", "conceptid", "term", "wildcard", "constraintoperator", 
			"descendantof", "descendantorselfof", "childof", "childorselfof", "ancestorof", 
			"ancestororselfof", "parentof", "parentorselfof", "conjunction", "disjunction", 
			"exclusion", "eclrefinement", "conjunctionrefinementset", "disjunctionrefinementset", 
			"subrefinement", "eclattributeset", "conjunctionattributeset", "disjunctionattributeset", 
			"subattributeset", "eclattributegroup", "eclattribute", "cardinality", 
			"minvalue", "to", "maxvalue", "many", "reverseflag", "eclattributename", 
			"expressioncomparisonoperator", "numericcomparisonoperator", "timecomparisonoperator", 
			"stringcomparisonoperator", "booleancomparisonoperator", "descriptionfilterconstraint", 
			"descriptionfilter", "termfilter", "termkeyword", "typedsearchterm", 
			"typedsearchtermset", "wild", "match", "matchsearchterm", "matchsearchtermset", 
			"wildsearchterm", "wildsearchtermset", "languagefilter", "language", 
			"languagecode", "languagecodeset", "typefilter", "typeidfilter", "typeid", 
			"typetokenfilter", "type", "typetoken", "typetokenset", "synonym", "fullyspecifiedname", 
			"definition", "dialectfilter", "dialectidfilter", "dialectid", "dialectaliasfilter", 
			"dialect", "dialectalias", "dialectaliasset", "dialectidset", "acceptabilityset", 
			"acceptabilityconceptreferenceset", "acceptabilitytokenset", "acceptabilitytoken", 
			"acceptable", "preferred", "conceptfilterconstraint", "conceptfilter", 
			"definitionstatusfilter", "definitionstatusidfilter", "definitionstatusidkeyword", 
			"definitionstatustokenfilter", "definitionstatuskeyword", "definitionstatustoken", 
			"definitionstatustokenset", "primitivetoken", "definedtoken", "modulefilter", 
			"moduleidkeyword", "effectivetimefilter", "effectivetimekeyword", "timevalue", 
			"timevalueset", "year", "month", "day", "activefilter", "activekeyword", 
			"activevalue", "activetruevalue", "activefalsevalue", "memberfilterconstraint", 
			"memberfilter", "memberfieldfilter", "historysupplement", "historykeyword", 
			"historyprofilesuffix", "historyminimumsuffix", "historymoderatesuffix", 
			"historymaximumsuffix", "historysubset", "numericvalue", "stringvalue", 
			"integervalue", "decimalvalue", "booleanvalue", "true_1", "false_1", 
			"nonnegativeintegervalue", "sctid", "ws", "mws", "comment", "nonstarchar", 
			"starwithnonfslash", "nonfslash", "sp", "htab", "cr", "lf", "qm", "bs", 
			"star", "digit", "zero", "digitnonzero", "nonwsnonpipe", "anynonescapedchar", 
			"escapedchar", "escapedwildchar", "nonwsnonescapedchar", "alpha", "dash"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'\\u0009'", "'\\u000A'", "'\\u000D'", "' '", "'!'", "'\"'", 
			"'#'", "'$'", "'%'", "'&'", "'''", "'('", "')'", "'*'", "'+'", "','", 
			"'-'", "'.'", "'/'", "'0'", "'1'", "'2'", "'3'", "'4'", "'5'", "'6'", 
			"'7'", "'8'", "'9'", "':'", "';'", "'<'", "'='", "'>'", "'?'", "'@'", 
			"'A'", "'B'", "'C'", "'D'", "'E'", "'F'", "'G'", "'H'", "'I'", "'J'", 
			"'K'", "'L'", "'M'", "'N'", "'O'", "'P'", "'Q'", "'R'", "'S'", "'T'", 
			"'U'", "'V'", "'W'", "'X'", "'Y'", "'Z'", "'['", "'\\'", "']'", "'^'", 
			"'_'", "'`'", "'a'", "'b'", "'c'", "'d'", "'e'", "'f'", "'g'", "'h'", 
			"'i'", "'j'", "'k'", "'l'", "'m'", "'n'", "'o'", "'p'", "'q'", "'r'", 
			"'s'", "'t'", "'u'", "'v'", "'w'", "'x'", "'y'", "'z'", "'{'", "'|'", 
			"'}'", "'~'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "UTF8_LETTER", "TAB", "LF", "CR", "SPACE", "EXCLAMATION", "QUOTE", 
			"HASH", "DOLLAR", "PERCENT", "AMPERSAND", "APOSTROPHE", "LEFT_PAREN", 
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
			setState(326);
			ws();
			setState(327);
			expressionconstraint();
			setState(328);
			ws();
			setState(329);
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
			setState(331);
			ws();
			setState(336);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(332);
				refinedexpressionconstraint();
				}
				break;
			case 2:
				{
				setState(333);
				compoundexpressionconstraint();
				}
				break;
			case 3:
				{
				setState(334);
				dottedexpressionconstraint();
				}
				break;
			case 4:
				{
				setState(335);
				subexpressionconstraint();
				}
				break;
			}
			setState(338);
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
			setState(340);
			subexpressionconstraint();
			setState(341);
			ws();
			setState(342);
			match(COLON);
			setState(343);
			ws();
			setState(344);
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
			setState(349);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(346);
				conjunctionexpressionconstraint();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(347);
				disjunctionexpressionconstraint();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(348);
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
			setState(351);
			subexpressionconstraint();
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
					conjunction();
					setState(354);
					ws();
					setState(355);
					subexpressionconstraint();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(359); 
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
			setState(361);
			subexpressionconstraint();
			setState(367); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(362);
					ws();
					setState(363);
					disjunction();
					setState(364);
					ws();
					setState(365);
					subexpressionconstraint();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(369); 
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
			setState(371);
			subexpressionconstraint();
			setState(372);
			ws();
			setState(373);
			exclusion();
			setState(374);
			ws();
			setState(375);
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
			setState(377);
			subexpressionconstraint();
			setState(381); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(378);
					ws();
					setState(379);
					dottedexpressionattribute();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(383); 
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
			setState(385);
			dot();
			setState(386);
			ws();
			setState(387);
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
		public ConstraintoperatorContext constraintoperator() {
			return getRuleContext(ConstraintoperatorContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public HistorysupplementContext historysupplement() {
			return getRuleContext(HistorysupplementContext.class,0);
		}
		public EclfocusconceptContext eclfocusconcept() {
			return getRuleContext(EclfocusconceptContext.class,0);
		}
		public List<DescriptionfilterconstraintContext> descriptionfilterconstraint() {
			return getRuleContexts(DescriptionfilterconstraintContext.class);
		}
		public DescriptionfilterconstraintContext descriptionfilterconstraint(int i) {
			return getRuleContext(DescriptionfilterconstraintContext.class,i);
		}
		public List<ConceptfilterconstraintContext> conceptfilterconstraint() {
			return getRuleContexts(ConceptfilterconstraintContext.class);
		}
		public ConceptfilterconstraintContext conceptfilterconstraint(int i) {
			return getRuleContext(ConceptfilterconstraintContext.class,i);
		}
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public ExpressionconstraintContext expressionconstraint() {
			return getRuleContext(ExpressionconstraintContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public MemberofContext memberof() {
			return getRuleContext(MemberofContext.class,0);
		}
		public List<MemberfilterconstraintContext> memberfilterconstraint() {
			return getRuleContexts(MemberfilterconstraintContext.class);
		}
		public MemberfilterconstraintContext memberfilterconstraint(int i) {
			return getRuleContext(MemberfilterconstraintContext.class,i);
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
		enterRule(_localctx, 18, RULE_subexpressionconstraint);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(392);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LESS_THAN || _la==GREATER_THAN) {
				{
				setState(389);
				constraintoperator();
				setState(390);
				ws();
				}
			}

			setState(425);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				{
				setState(397);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==CARAT) {
					{
					setState(394);
					memberof();
					setState(395);
					ws();
					}
				}

				setState(406);
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
					{
					setState(399);
					eclfocusconcept();
					}
					break;
				case LEFT_PAREN:
					{
					{
					setState(400);
					match(LEFT_PAREN);
					setState(401);
					ws();
					setState(402);
					expressionconstraint();
					setState(403);
					ws();
					setState(404);
					match(RIGHT_PAREN);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(413);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(408);
						ws();
						setState(409);
						memberfilterconstraint();
						}
						} 
					}
					setState(415);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
				}
				}
				}
				break;
			case 2:
				{
				setState(423);
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
					{
					setState(416);
					eclfocusconcept();
					}
					break;
				case LEFT_PAREN:
					{
					{
					setState(417);
					match(LEFT_PAREN);
					setState(418);
					ws();
					setState(419);
					expressionconstraint();
					setState(420);
					ws();
					setState(421);
					match(RIGHT_PAREN);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			}
			setState(434);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(427);
					ws();
					setState(430);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
					case 1:
						{
						setState(428);
						descriptionfilterconstraint();
						}
						break;
					case 2:
						{
						setState(429);
						conceptfilterconstraint();
						}
						break;
					}
					}
					} 
				}
				setState(436);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			}
			setState(440);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				setState(437);
				ws();
				setState(438);
				historysupplement();
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
			setState(444);
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
				setState(442);
				eclconceptreference();
				}
				break;
			case ASTERISK:
				enterOuterAlt(_localctx, 2);
				{
				setState(443);
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
			setState(446);
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
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public TerminalNode LEFT_BRACE() { return getToken(ECLParser.LEFT_BRACE, 0); }
		public TerminalNode RIGHT_BRACE() { return getToken(ECLParser.RIGHT_BRACE, 0); }
		public RefsetfieldsetContext refsetfieldset() {
			return getRuleContext(RefsetfieldsetContext.class,0);
		}
		public WildcardContext wildcard() {
			return getRuleContext(WildcardContext.class,0);
		}
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
			setState(448);
			match(CARAT);
			setState(459);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(449);
				ws();
				setState(450);
				match(LEFT_BRACE);
				setState(451);
				ws();
				setState(454);
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
					{
					setState(452);
					refsetfieldset();
					}
					break;
				case ASTERISK:
					{
					setState(453);
					wildcard();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(456);
				ws();
				setState(457);
				match(RIGHT_BRACE);
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
	public static class RefsetfieldsetContext extends ParserRuleContext {
		public List<RefsetfieldContext> refsetfield() {
			return getRuleContexts(RefsetfieldContext.class);
		}
		public RefsetfieldContext refsetfield(int i) {
			return getRuleContext(RefsetfieldContext.class,i);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ECLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ECLParser.COMMA, i);
		}
		public RefsetfieldsetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_refsetfieldset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterRefsetfieldset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitRefsetfieldset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitRefsetfieldset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RefsetfieldsetContext refsetfieldset() throws RecognitionException {
		RefsetfieldsetContext _localctx = new RefsetfieldsetContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_refsetfieldset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(461);
			refsetfield();
			setState(469);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(462);
					ws();
					setState(463);
					match(COMMA);
					setState(464);
					ws();
					setState(465);
					refsetfield();
					}
					} 
				}
				setState(471);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
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
	public static class RefsetfieldContext extends ParserRuleContext {
		public RefsetfieldnameContext refsetfieldname() {
			return getRuleContext(RefsetfieldnameContext.class,0);
		}
		public RefsetfieldrefContext refsetfieldref() {
			return getRuleContext(RefsetfieldrefContext.class,0);
		}
		public RefsetfieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_refsetfield; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterRefsetfield(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitRefsetfield(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitRefsetfield(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RefsetfieldContext refsetfield() throws RecognitionException {
		RefsetfieldContext _localctx = new RefsetfieldContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_refsetfield);
		try {
			setState(474);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
				enterOuterAlt(_localctx, 1);
				{
				setState(472);
				refsetfieldname();
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
				setState(473);
				refsetfieldref();
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
	public static class RefsetfieldnameContext extends ParserRuleContext {
		public List<AlphaContext> alpha() {
			return getRuleContexts(AlphaContext.class);
		}
		public AlphaContext alpha(int i) {
			return getRuleContext(AlphaContext.class,i);
		}
		public RefsetfieldnameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_refsetfieldname; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterRefsetfieldname(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitRefsetfieldname(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitRefsetfieldname(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RefsetfieldnameContext refsetfieldname() throws RecognitionException {
		RefsetfieldnameContext _localctx = new RefsetfieldnameContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_refsetfieldname);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(477); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(476);
				alpha();
				}
				}
				setState(479); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 38)) & ~0x3f) == 0 && ((1L << (_la - 38)) & 288230371923853311L) != 0) );
			}
		}
		catch (RecognitionException re) {
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
	public static class RefsetfieldrefContext extends ParserRuleContext {
		public EclconceptreferenceContext eclconceptreference() {
			return getRuleContext(EclconceptreferenceContext.class,0);
		}
		public RefsetfieldrefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_refsetfieldref; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterRefsetfieldref(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitRefsetfieldref(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitRefsetfieldref(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RefsetfieldrefContext refsetfieldref() throws RecognitionException {
		RefsetfieldrefContext _localctx = new RefsetfieldrefContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_refsetfieldref);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(481);
			eclconceptreference();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 34, RULE_eclconceptreference);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(483);
			conceptid();
			setState(491);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				setState(484);
				ws();
				setState(485);
				match(PIPE);
				setState(486);
				ws();
				setState(487);
				term();
				setState(488);
				ws();
				setState(489);
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
	public static class EclconceptreferencesetContext extends ParserRuleContext {
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<EclconceptreferenceContext> eclconceptreference() {
			return getRuleContexts(EclconceptreferenceContext.class);
		}
		public EclconceptreferenceContext eclconceptreference(int i) {
			return getRuleContext(EclconceptreferenceContext.class,i);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public List<MwsContext> mws() {
			return getRuleContexts(MwsContext.class);
		}
		public MwsContext mws(int i) {
			return getRuleContext(MwsContext.class,i);
		}
		public EclconceptreferencesetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eclconceptreferenceset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterEclconceptreferenceset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitEclconceptreferenceset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitEclconceptreferenceset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EclconceptreferencesetContext eclconceptreferenceset() throws RecognitionException {
		EclconceptreferencesetContext _localctx = new EclconceptreferencesetContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_eclconceptreferenceset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(493);
			match(LEFT_PAREN);
			setState(494);
			ws();
			setState(495);
			eclconceptreference();
			setState(499); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(496);
					mws();
					setState(497);
					eclconceptreference();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(501); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(503);
			ws();
			setState(504);
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
		enterRule(_localctx, 38, RULE_conceptid);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(506);
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
		enterRule(_localctx, 40, RULE_term);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(509); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(508);
					nonwsnonpipe();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(511); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(525);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(514); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(513);
						sp();
						}
						}
						setState(516); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==SPACE );
					setState(519); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(518);
							nonwsnonpipe();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(521); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					}
					} 
				}
				setState(527);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
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
		enterRule(_localctx, 42, RULE_wildcard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(528);
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
		public ChildorselfofContext childorselfof() {
			return getRuleContext(ChildorselfofContext.class,0);
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
		public ParentorselfofContext parentorselfof() {
			return getRuleContext(ParentorselfofContext.class,0);
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
		enterRule(_localctx, 44, RULE_constraintoperator);
		try {
			setState(538);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(530);
				childof();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(531);
				childorselfof();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(532);
				descendantorselfof();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(533);
				descendantof();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(534);
				parentof();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(535);
				parentorselfof();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(536);
				ancestororselfof();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(537);
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
		enterRule(_localctx, 46, RULE_descendantof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(540);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 48, RULE_descendantorselfof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(542);
			match(LESS_THAN);
			setState(543);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 50, RULE_childof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(545);
			match(LESS_THAN);
			setState(546);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ChildorselfofContext extends ParserRuleContext {
		public List<TerminalNode> LESS_THAN() { return getTokens(ECLParser.LESS_THAN); }
		public TerminalNode LESS_THAN(int i) {
			return getToken(ECLParser.LESS_THAN, i);
		}
		public TerminalNode EXCLAMATION() { return getToken(ECLParser.EXCLAMATION, 0); }
		public ChildorselfofContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_childorselfof; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterChildorselfof(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitChildorselfof(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitChildorselfof(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ChildorselfofContext childorselfof() throws RecognitionException {
		ChildorselfofContext _localctx = new ChildorselfofContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_childorselfof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(548);
			match(LESS_THAN);
			setState(549);
			match(LESS_THAN);
			setState(550);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 54, RULE_ancestorof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(552);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 56, RULE_ancestororselfof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(554);
			match(GREATER_THAN);
			setState(555);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 58, RULE_parentof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(557);
			match(GREATER_THAN);
			setState(558);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ParentorselfofContext extends ParserRuleContext {
		public List<TerminalNode> GREATER_THAN() { return getTokens(ECLParser.GREATER_THAN); }
		public TerminalNode GREATER_THAN(int i) {
			return getToken(ECLParser.GREATER_THAN, i);
		}
		public TerminalNode EXCLAMATION() { return getToken(ECLParser.EXCLAMATION, 0); }
		public ParentorselfofContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parentorselfof; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterParentorselfof(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitParentorselfof(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitParentorselfof(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParentorselfofContext parentorselfof() throws RecognitionException {
		ParentorselfofContext _localctx = new ParentorselfofContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_parentorselfof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(560);
			match(GREATER_THAN);
			setState(561);
			match(GREATER_THAN);
			setState(562);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ConjunctionContext extends ParserRuleContext {
		public MwsContext mws() {
			return getRuleContext(MwsContext.class,0);
		}
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode CAP_N() { return getToken(ECLParser.CAP_N, 0); }
		public TerminalNode N() { return getToken(ECLParser.N, 0); }
		public TerminalNode CAP_D() { return getToken(ECLParser.CAP_D, 0); }
		public TerminalNode D() { return getToken(ECLParser.D, 0); }
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
		enterRule(_localctx, 62, RULE_conjunction);
		int _la;
		try {
			setState(578);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CAP_A:
			case A:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(566);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
				case 1:
					{
					setState(564);
					_la = _input.LA(1);
					if ( !(_la==CAP_A || _la==A) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					break;
				case 2:
					{
					setState(565);
					_la = _input.LA(1);
					if ( !(_la==CAP_A || _la==A) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					break;
				}
				setState(570);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
				case 1:
					{
					setState(568);
					_la = _input.LA(1);
					if ( !(_la==CAP_N || _la==N) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					break;
				case 2:
					{
					setState(569);
					_la = _input.LA(1);
					if ( !(_la==CAP_N || _la==N) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					break;
				}
				setState(574);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
				case 1:
					{
					setState(572);
					_la = _input.LA(1);
					if ( !(_la==CAP_D || _la==D) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					break;
				case 2:
					{
					setState(573);
					_la = _input.LA(1);
					if ( !(_la==CAP_D || _la==D) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					break;
				}
				setState(576);
				mws();
				}
				}
				break;
			case COMMA:
				enterOuterAlt(_localctx, 2);
				{
				setState(577);
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
		public TerminalNode CAP_O() { return getToken(ECLParser.CAP_O, 0); }
		public TerminalNode O() { return getToken(ECLParser.O, 0); }
		public TerminalNode CAP_R() { return getToken(ECLParser.CAP_R, 0); }
		public TerminalNode R() { return getToken(ECLParser.R, 0); }
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
		enterRule(_localctx, 64, RULE_disjunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(582);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				{
				setState(580);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(581);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(586);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				{
				setState(584);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(585);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(588);
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
		public TerminalNode CAP_M() { return getToken(ECLParser.CAP_M, 0); }
		public TerminalNode M() { return getToken(ECLParser.M, 0); }
		public TerminalNode CAP_I() { return getToken(ECLParser.CAP_I, 0); }
		public TerminalNode I() { return getToken(ECLParser.I, 0); }
		public TerminalNode CAP_N() { return getToken(ECLParser.CAP_N, 0); }
		public TerminalNode N() { return getToken(ECLParser.N, 0); }
		public TerminalNode CAP_U() { return getToken(ECLParser.CAP_U, 0); }
		public TerminalNode U() { return getToken(ECLParser.U, 0); }
		public TerminalNode CAP_S() { return getToken(ECLParser.CAP_S, 0); }
		public TerminalNode S() { return getToken(ECLParser.S, 0); }
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
		enterRule(_localctx, 66, RULE_exclusion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(592);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				setState(590);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(591);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(596);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				{
				setState(594);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(595);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(600);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(598);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(599);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(604);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				{
				setState(602);
				_la = _input.LA(1);
				if ( !(_la==CAP_U || _la==U) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(603);
				_la = _input.LA(1);
				if ( !(_la==CAP_U || _la==U) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(608);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
			case 1:
				{
				setState(606);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(607);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(610);
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
		enterRule(_localctx, 68, RULE_eclrefinement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(615);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				{
				setState(612);
				subrefinement();
				}
				break;
			case 2:
				{
				setState(613);
				conjunctionrefinementset();
				}
				break;
			case 3:
				{
				setState(614);
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
		public List<SubrefinementContext> subrefinement() {
			return getRuleContexts(SubrefinementContext.class);
		}
		public SubrefinementContext subrefinement(int i) {
			return getRuleContext(SubrefinementContext.class,i);
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
		enterRule(_localctx, 70, RULE_conjunctionrefinementset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(617);
			subrefinement();
			setState(623); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(618);
					ws();
					setState(619);
					conjunction();
					setState(620);
					ws();
					setState(621);
					subrefinement();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(625); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,39,_ctx);
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
		enterRule(_localctx, 72, RULE_disjunctionrefinementset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(627);
			subrefinement();
			setState(633); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(628);
					ws();
					setState(629);
					disjunction();
					setState(630);
					ws();
					setState(631);
					subrefinement();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(635); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,40,_ctx);
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
		enterRule(_localctx, 74, RULE_subrefinement);
		try {
			setState(645);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(637);
				eclattributeset();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(638);
				eclattributegroup();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(639);
				match(LEFT_PAREN);
				setState(640);
				ws();
				setState(641);
				eclrefinement();
				setState(642);
				ws();
				setState(643);
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
		enterRule(_localctx, 76, RULE_eclattributeset);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(650);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				{
				setState(647);
				subattributeset();
				}
				break;
			case 2:
				{
				setState(648);
				conjunctionattributeset();
				}
				break;
			case 3:
				{
				setState(649);
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
		public List<SubattributesetContext> subattributeset() {
			return getRuleContexts(SubattributesetContext.class);
		}
		public SubattributesetContext subattributeset(int i) {
			return getRuleContext(SubattributesetContext.class,i);
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
		enterRule(_localctx, 78, RULE_conjunctionattributeset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(652);
			subattributeset();
			setState(658); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(653);
					ws();
					setState(654);
					conjunction();
					setState(655);
					ws();
					setState(656);
					subattributeset();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(660); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,43,_ctx);
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
		enterRule(_localctx, 80, RULE_disjunctionattributeset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(662);
			subattributeset();
			setState(668); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(663);
					ws();
					setState(664);
					disjunction();
					setState(665);
					ws();
					setState(666);
					subattributeset();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(670); 
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
		enterRule(_localctx, 82, RULE_subattributeset);
		try {
			setState(679);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(672);
				eclattribute();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(673);
				match(LEFT_PAREN);
				setState(674);
				ws();
				setState(675);
				eclattributeset();
				setState(676);
				ws();
				setState(677);
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
		enterRule(_localctx, 84, RULE_eclattributegroup);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(686);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LEFT_BRACE) {
				{
				setState(681);
				match(LEFT_BRACE);
				setState(682);
				cardinality();
				setState(683);
				match(RIGHT_BRACE);
				setState(684);
				ws();
				}
			}

			setState(688);
			match(LEFT_CURLY_BRACE);
			setState(689);
			ws();
			setState(690);
			eclattributeset();
			setState(691);
			ws();
			setState(692);
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
		public TerminalNode HASH() { return getToken(ECLParser.HASH, 0); }
		public NumericvalueContext numericvalue() {
			return getRuleContext(NumericvalueContext.class,0);
		}
		public StringcomparisonoperatorContext stringcomparisonoperator() {
			return getRuleContext(StringcomparisonoperatorContext.class,0);
		}
		public BooleancomparisonoperatorContext booleancomparisonoperator() {
			return getRuleContext(BooleancomparisonoperatorContext.class,0);
		}
		public BooleanvalueContext booleanvalue() {
			return getRuleContext(BooleanvalueContext.class,0);
		}
		public TypedsearchtermContext typedsearchterm() {
			return getRuleContext(TypedsearchtermContext.class,0);
		}
		public TypedsearchtermsetContext typedsearchtermset() {
			return getRuleContext(TypedsearchtermsetContext.class,0);
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
		enterRule(_localctx, 86, RULE_eclattribute);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(699);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LEFT_BRACE) {
				{
				setState(694);
				match(LEFT_BRACE);
				setState(695);
				cardinality();
				setState(696);
				match(RIGHT_BRACE);
				setState(697);
				ws();
				}
			}

			setState(704);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CAP_R || _la==R) {
				{
				setState(701);
				reverseflag();
				setState(702);
				ws();
				}
			}

			setState(706);
			eclattributename();
			setState(707);
			ws();
			setState(727);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				{
				{
				setState(708);
				expressioncomparisonoperator();
				setState(709);
				ws();
				setState(710);
				subexpressionconstraint();
				}
				}
				break;
			case 2:
				{
				{
				setState(712);
				numericcomparisonoperator();
				setState(713);
				ws();
				setState(714);
				match(HASH);
				setState(715);
				numericvalue();
				}
				}
				break;
			case 3:
				{
				{
				setState(717);
				stringcomparisonoperator();
				setState(718);
				ws();
				setState(721);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case QUOTE:
				case CAP_M:
				case CAP_W:
				case M:
				case W:
					{
					setState(719);
					typedsearchterm();
					}
					break;
				case LEFT_PAREN:
					{
					setState(720);
					typedsearchtermset();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				break;
			case 4:
				{
				{
				setState(723);
				booleancomparisonoperator();
				setState(724);
				ws();
				setState(725);
				booleanvalue();
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
		enterRule(_localctx, 88, RULE_cardinality);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(729);
			minvalue();
			setState(730);
			to();
			setState(731);
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
		enterRule(_localctx, 90, RULE_minvalue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(733);
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
		enterRule(_localctx, 92, RULE_to);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(735);
			match(PERIOD);
			setState(736);
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
		enterRule(_localctx, 94, RULE_maxvalue);
		try {
			setState(740);
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
				setState(738);
				nonnegativeintegervalue();
				}
				break;
			case ASTERISK:
				enterOuterAlt(_localctx, 2);
				{
				setState(739);
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
		enterRule(_localctx, 96, RULE_many);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(742);
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
		public TerminalNode R() { return getToken(ECLParser.R, 0); }
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
		enterRule(_localctx, 98, RULE_reverseflag);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(744);
			_la = _input.LA(1);
			if ( !(_la==CAP_R || _la==R) ) {
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
		enterRule(_localctx, 100, RULE_eclattributename);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(746);
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
		enterRule(_localctx, 102, RULE_expressioncomparisonoperator);
		try {
			setState(751);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EQUALS:
				enterOuterAlt(_localctx, 1);
				{
				setState(748);
				match(EQUALS);
				}
				break;
			case EXCLAMATION:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(749);
				match(EXCLAMATION);
				setState(750);
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
			setState(762);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(753);
				match(EQUALS);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(754);
				match(EXCLAMATION);
				setState(755);
				match(EQUALS);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(756);
				match(LESS_THAN);
				setState(757);
				match(EQUALS);
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(758);
				match(LESS_THAN);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				{
				setState(759);
				match(GREATER_THAN);
				setState(760);
				match(EQUALS);
				}
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(761);
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
	public static class TimecomparisonoperatorContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(ECLParser.EQUALS, 0); }
		public TerminalNode EXCLAMATION() { return getToken(ECLParser.EXCLAMATION, 0); }
		public TerminalNode LESS_THAN() { return getToken(ECLParser.LESS_THAN, 0); }
		public TerminalNode GREATER_THAN() { return getToken(ECLParser.GREATER_THAN, 0); }
		public TimecomparisonoperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_timecomparisonoperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterTimecomparisonoperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitTimecomparisonoperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitTimecomparisonoperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TimecomparisonoperatorContext timecomparisonoperator() throws RecognitionException {
		TimecomparisonoperatorContext _localctx = new TimecomparisonoperatorContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_timecomparisonoperator);
		try {
			setState(773);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(764);
				match(EQUALS);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(765);
				match(EXCLAMATION);
				setState(766);
				match(EQUALS);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(767);
				match(LESS_THAN);
				setState(768);
				match(EQUALS);
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(769);
				match(LESS_THAN);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				{
				setState(770);
				match(GREATER_THAN);
				setState(771);
				match(EQUALS);
				}
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(772);
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
		enterRule(_localctx, 108, RULE_stringcomparisonoperator);
		try {
			setState(778);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EQUALS:
				enterOuterAlt(_localctx, 1);
				{
				setState(775);
				match(EQUALS);
				}
				break;
			case EXCLAMATION:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(776);
				match(EXCLAMATION);
				setState(777);
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
	public static class BooleancomparisonoperatorContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(ECLParser.EQUALS, 0); }
		public TerminalNode EXCLAMATION() { return getToken(ECLParser.EXCLAMATION, 0); }
		public BooleancomparisonoperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleancomparisonoperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterBooleancomparisonoperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitBooleancomparisonoperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitBooleancomparisonoperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleancomparisonoperatorContext booleancomparisonoperator() throws RecognitionException {
		BooleancomparisonoperatorContext _localctx = new BooleancomparisonoperatorContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_booleancomparisonoperator);
		try {
			setState(783);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EQUALS:
				enterOuterAlt(_localctx, 1);
				{
				setState(780);
				match(EQUALS);
				}
				break;
			case EXCLAMATION:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(781);
				match(EXCLAMATION);
				setState(782);
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
	public static class DescriptionfilterconstraintContext extends ParserRuleContext {
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<DescriptionfilterContext> descriptionfilter() {
			return getRuleContexts(DescriptionfilterContext.class);
		}
		public DescriptionfilterContext descriptionfilter(int i) {
			return getRuleContext(DescriptionfilterContext.class,i);
		}
		public List<TerminalNode> LEFT_CURLY_BRACE() { return getTokens(ECLParser.LEFT_CURLY_BRACE); }
		public TerminalNode LEFT_CURLY_BRACE(int i) {
			return getToken(ECLParser.LEFT_CURLY_BRACE, i);
		}
		public List<TerminalNode> RIGHT_CURLY_BRACE() { return getTokens(ECLParser.RIGHT_CURLY_BRACE); }
		public TerminalNode RIGHT_CURLY_BRACE(int i) {
			return getToken(ECLParser.RIGHT_CURLY_BRACE, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ECLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ECLParser.COMMA, i);
		}
		public TerminalNode CAP_D() { return getToken(ECLParser.CAP_D, 0); }
		public TerminalNode D() { return getToken(ECLParser.D, 0); }
		public DescriptionfilterconstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_descriptionfilterconstraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDescriptionfilterconstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDescriptionfilterconstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDescriptionfilterconstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DescriptionfilterconstraintContext descriptionfilterconstraint() throws RecognitionException {
		DescriptionfilterconstraintContext _localctx = new DescriptionfilterconstraintContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_descriptionfilterconstraint);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(785);
			match(LEFT_CURLY_BRACE);
			setState(786);
			match(LEFT_CURLY_BRACE);
			}
			setState(788);
			ws();
			setState(791);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				{
				setState(789);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(790);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(793);
			ws();
			setState(794);
			descriptionfilter();
			setState(802);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,58,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(795);
					ws();
					setState(796);
					match(COMMA);
					setState(797);
					ws();
					setState(798);
					descriptionfilter();
					}
					} 
				}
				setState(804);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,58,_ctx);
			}
			setState(805);
			ws();
			{
			setState(806);
			match(RIGHT_CURLY_BRACE);
			setState(807);
			match(RIGHT_CURLY_BRACE);
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
	public static class DescriptionfilterContext extends ParserRuleContext {
		public TermfilterContext termfilter() {
			return getRuleContext(TermfilterContext.class,0);
		}
		public LanguagefilterContext languagefilter() {
			return getRuleContext(LanguagefilterContext.class,0);
		}
		public TypefilterContext typefilter() {
			return getRuleContext(TypefilterContext.class,0);
		}
		public DialectfilterContext dialectfilter() {
			return getRuleContext(DialectfilterContext.class,0);
		}
		public ModulefilterContext modulefilter() {
			return getRuleContext(ModulefilterContext.class,0);
		}
		public EffectivetimefilterContext effectivetimefilter() {
			return getRuleContext(EffectivetimefilterContext.class,0);
		}
		public ActivefilterContext activefilter() {
			return getRuleContext(ActivefilterContext.class,0);
		}
		public DescriptionfilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_descriptionfilter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDescriptionfilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDescriptionfilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDescriptionfilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DescriptionfilterContext descriptionfilter() throws RecognitionException {
		DescriptionfilterContext _localctx = new DescriptionfilterContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_descriptionfilter);
		try {
			setState(816);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(809);
				termfilter();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(810);
				languagefilter();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(811);
				typefilter();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(812);
				dialectfilter();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(813);
				modulefilter();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(814);
				effectivetimefilter();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(815);
				activefilter();
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
	public static class TermfilterContext extends ParserRuleContext {
		public TermkeywordContext termkeyword() {
			return getRuleContext(TermkeywordContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public StringcomparisonoperatorContext stringcomparisonoperator() {
			return getRuleContext(StringcomparisonoperatorContext.class,0);
		}
		public TypedsearchtermContext typedsearchterm() {
			return getRuleContext(TypedsearchtermContext.class,0);
		}
		public TypedsearchtermsetContext typedsearchtermset() {
			return getRuleContext(TypedsearchtermsetContext.class,0);
		}
		public TermfilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_termfilter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterTermfilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitTermfilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitTermfilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermfilterContext termfilter() throws RecognitionException {
		TermfilterContext _localctx = new TermfilterContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_termfilter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(818);
			termkeyword();
			setState(819);
			ws();
			setState(820);
			stringcomparisonoperator();
			setState(821);
			ws();
			setState(824);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case QUOTE:
			case CAP_M:
			case CAP_W:
			case M:
			case W:
				{
				setState(822);
				typedsearchterm();
				}
				break;
			case LEFT_PAREN:
				{
				setState(823);
				typedsearchtermset();
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
	public static class TermkeywordContext extends ParserRuleContext {
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public TerminalNode CAP_R() { return getToken(ECLParser.CAP_R, 0); }
		public TerminalNode R() { return getToken(ECLParser.R, 0); }
		public TerminalNode CAP_M() { return getToken(ECLParser.CAP_M, 0); }
		public TerminalNode M() { return getToken(ECLParser.M, 0); }
		public TermkeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_termkeyword; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterTermkeyword(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitTermkeyword(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitTermkeyword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermkeywordContext termkeyword() throws RecognitionException {
		TermkeywordContext _localctx = new TermkeywordContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_termkeyword);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(828);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				{
				setState(826);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(827);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(832);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				{
				setState(830);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(831);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(836);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
			case 1:
				{
				setState(834);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(835);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(840);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
			case 1:
				{
				setState(838);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(839);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class TypedsearchtermContext extends ParserRuleContext {
		public MatchsearchtermsetContext matchsearchtermset() {
			return getRuleContext(MatchsearchtermsetContext.class,0);
		}
		public MatchContext match() {
			return getRuleContext(MatchContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public TerminalNode COLON() { return getToken(ECLParser.COLON, 0); }
		public WildContext wild() {
			return getRuleContext(WildContext.class,0);
		}
		public WildsearchtermsetContext wildsearchtermset() {
			return getRuleContext(WildsearchtermsetContext.class,0);
		}
		public TypedsearchtermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedsearchterm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterTypedsearchterm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitTypedsearchterm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitTypedsearchterm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedsearchtermContext typedsearchterm() throws RecognitionException {
		TypedsearchtermContext _localctx = new TypedsearchtermContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_typedsearchterm);
		int _la;
		try {
			setState(856);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case QUOTE:
			case CAP_M:
			case M:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(847);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==CAP_M || _la==M) {
					{
					setState(842);
					match();
					setState(843);
					ws();
					setState(844);
					match(COLON);
					setState(845);
					ws();
					}
				}

				setState(849);
				matchsearchtermset();
				}
				}
				break;
			case CAP_W:
			case W:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(850);
				wild();
				setState(851);
				ws();
				setState(852);
				match(COLON);
				setState(853);
				ws();
				setState(854);
				wildsearchtermset();
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
	public static class TypedsearchtermsetContext extends ParserRuleContext {
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<TypedsearchtermContext> typedsearchterm() {
			return getRuleContexts(TypedsearchtermContext.class);
		}
		public TypedsearchtermContext typedsearchterm(int i) {
			return getRuleContext(TypedsearchtermContext.class,i);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public List<MwsContext> mws() {
			return getRuleContexts(MwsContext.class);
		}
		public MwsContext mws(int i) {
			return getRuleContext(MwsContext.class,i);
		}
		public TypedsearchtermsetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedsearchtermset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterTypedsearchtermset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitTypedsearchtermset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitTypedsearchtermset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedsearchtermsetContext typedsearchtermset() throws RecognitionException {
		TypedsearchtermsetContext _localctx = new TypedsearchtermsetContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_typedsearchtermset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(858);
			match(LEFT_PAREN);
			setState(859);
			ws();
			setState(860);
			typedsearchterm();
			setState(866);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(861);
					mws();
					setState(862);
					typedsearchterm();
					}
					} 
				}
				setState(868);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
			}
			setState(869);
			ws();
			setState(870);
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
	public static class WildContext extends ParserRuleContext {
		public TerminalNode CAP_W() { return getToken(ECLParser.CAP_W, 0); }
		public TerminalNode W() { return getToken(ECLParser.W, 0); }
		public TerminalNode CAP_I() { return getToken(ECLParser.CAP_I, 0); }
		public TerminalNode I() { return getToken(ECLParser.I, 0); }
		public TerminalNode CAP_L() { return getToken(ECLParser.CAP_L, 0); }
		public TerminalNode L() { return getToken(ECLParser.L, 0); }
		public TerminalNode CAP_D() { return getToken(ECLParser.CAP_D, 0); }
		public TerminalNode D() { return getToken(ECLParser.D, 0); }
		public WildContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wild; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterWild(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitWild(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitWild(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WildContext wild() throws RecognitionException {
		WildContext _localctx = new WildContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_wild);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(874);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
			case 1:
				{
				setState(872);
				_la = _input.LA(1);
				if ( !(_la==CAP_W || _la==W) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(873);
				_la = _input.LA(1);
				if ( !(_la==CAP_W || _la==W) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(878);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
			case 1:
				{
				setState(876);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(877);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(882);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,70,_ctx) ) {
			case 1:
				{
				setState(880);
				_la = _input.LA(1);
				if ( !(_la==CAP_L || _la==L) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(881);
				_la = _input.LA(1);
				if ( !(_la==CAP_L || _la==L) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(886);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,71,_ctx) ) {
			case 1:
				{
				setState(884);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(885);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class MatchContext extends ParserRuleContext {
		public TerminalNode CAP_M() { return getToken(ECLParser.CAP_M, 0); }
		public TerminalNode M() { return getToken(ECLParser.M, 0); }
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode CAP_C() { return getToken(ECLParser.CAP_C, 0); }
		public TerminalNode C() { return getToken(ECLParser.C, 0); }
		public TerminalNode CAP_H() { return getToken(ECLParser.CAP_H, 0); }
		public TerminalNode H() { return getToken(ECLParser.H, 0); }
		public MatchContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_match; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterMatch(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitMatch(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitMatch(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchContext match() throws RecognitionException {
		MatchContext _localctx = new MatchContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_match);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(890);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
			case 1:
				{
				setState(888);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(889);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(894);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,73,_ctx) ) {
			case 1:
				{
				setState(892);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(893);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(898);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
			case 1:
				{
				setState(896);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(897);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(902);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,75,_ctx) ) {
			case 1:
				{
				setState(900);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(901);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(906);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,76,_ctx) ) {
			case 1:
				{
				setState(904);
				_la = _input.LA(1);
				if ( !(_la==CAP_H || _la==H) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(905);
				_la = _input.LA(1);
				if ( !(_la==CAP_H || _la==H) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class MatchsearchtermContext extends ParserRuleContext {
		public List<NonwsnonescapedcharContext> nonwsnonescapedchar() {
			return getRuleContexts(NonwsnonescapedcharContext.class);
		}
		public NonwsnonescapedcharContext nonwsnonescapedchar(int i) {
			return getRuleContext(NonwsnonescapedcharContext.class,i);
		}
		public List<EscapedcharContext> escapedchar() {
			return getRuleContexts(EscapedcharContext.class);
		}
		public EscapedcharContext escapedchar(int i) {
			return getRuleContext(EscapedcharContext.class,i);
		}
		public MatchsearchtermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchsearchterm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterMatchsearchterm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitMatchsearchterm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitMatchsearchterm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchsearchtermContext matchsearchterm() throws RecognitionException {
		MatchsearchtermContext _localctx = new MatchsearchtermContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_matchsearchterm);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(910); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					setState(910);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case UTF8_LETTER:
					case EXCLAMATION:
					case HASH:
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
						setState(908);
						nonwsnonescapedchar();
						}
						break;
					case BACKSLASH:
						{
						setState(909);
						escapedchar();
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
				setState(912); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,78,_ctx);
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
	public static class MatchsearchtermsetContext extends ParserRuleContext {
		public List<QmContext> qm() {
			return getRuleContexts(QmContext.class);
		}
		public QmContext qm(int i) {
			return getRuleContext(QmContext.class,i);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<MatchsearchtermContext> matchsearchterm() {
			return getRuleContexts(MatchsearchtermContext.class);
		}
		public MatchsearchtermContext matchsearchterm(int i) {
			return getRuleContext(MatchsearchtermContext.class,i);
		}
		public List<MwsContext> mws() {
			return getRuleContexts(MwsContext.class);
		}
		public MwsContext mws(int i) {
			return getRuleContext(MwsContext.class,i);
		}
		public MatchsearchtermsetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchsearchtermset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterMatchsearchtermset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitMatchsearchtermset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitMatchsearchtermset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchsearchtermsetContext matchsearchtermset() throws RecognitionException {
		MatchsearchtermsetContext _localctx = new MatchsearchtermsetContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_matchsearchtermset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(914);
			qm();
			setState(915);
			ws();
			setState(916);
			matchsearchterm();
			setState(922);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,79,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(917);
					mws();
					setState(918);
					matchsearchterm();
					}
					} 
				}
				setState(924);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,79,_ctx);
			}
			setState(925);
			ws();
			setState(926);
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
	public static class WildsearchtermContext extends ParserRuleContext {
		public List<AnynonescapedcharContext> anynonescapedchar() {
			return getRuleContexts(AnynonescapedcharContext.class);
		}
		public AnynonescapedcharContext anynonescapedchar(int i) {
			return getRuleContext(AnynonescapedcharContext.class,i);
		}
		public List<EscapedwildcharContext> escapedwildchar() {
			return getRuleContexts(EscapedwildcharContext.class);
		}
		public EscapedwildcharContext escapedwildchar(int i) {
			return getRuleContext(EscapedwildcharContext.class,i);
		}
		public WildsearchtermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildsearchterm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterWildsearchterm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitWildsearchterm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitWildsearchterm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WildsearchtermContext wildsearchterm() throws RecognitionException {
		WildsearchtermContext _localctx = new WildsearchtermContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_wildsearchterm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(930); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(930);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case UTF8_LETTER:
				case TAB:
				case LF:
				case CR:
				case SPACE:
				case EXCLAMATION:
				case HASH:
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
					setState(928);
					anynonescapedchar();
					}
					break;
				case BACKSLASH:
					{
					setState(929);
					escapedwildchar();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(932); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & -130L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 68719476735L) != 0) );
			}
		}
		catch (RecognitionException re) {
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
	public static class WildsearchtermsetContext extends ParserRuleContext {
		public List<QmContext> qm() {
			return getRuleContexts(QmContext.class);
		}
		public QmContext qm(int i) {
			return getRuleContext(QmContext.class,i);
		}
		public WildsearchtermContext wildsearchterm() {
			return getRuleContext(WildsearchtermContext.class,0);
		}
		public WildsearchtermsetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildsearchtermset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterWildsearchtermset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitWildsearchtermset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitWildsearchtermset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WildsearchtermsetContext wildsearchtermset() throws RecognitionException {
		WildsearchtermsetContext _localctx = new WildsearchtermsetContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_wildsearchtermset);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(934);
			qm();
			setState(935);
			wildsearchterm();
			setState(936);
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
	public static class LanguagefilterContext extends ParserRuleContext {
		public LanguageContext language() {
			return getRuleContext(LanguageContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public BooleancomparisonoperatorContext booleancomparisonoperator() {
			return getRuleContext(BooleancomparisonoperatorContext.class,0);
		}
		public LanguagecodeContext languagecode() {
			return getRuleContext(LanguagecodeContext.class,0);
		}
		public LanguagecodesetContext languagecodeset() {
			return getRuleContext(LanguagecodesetContext.class,0);
		}
		public LanguagefilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_languagefilter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterLanguagefilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitLanguagefilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitLanguagefilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LanguagefilterContext languagefilter() throws RecognitionException {
		LanguagefilterContext _localctx = new LanguagefilterContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_languagefilter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(938);
			language();
			setState(939);
			ws();
			setState(940);
			booleancomparisonoperator();
			setState(941);
			ws();
			setState(944);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
				{
				setState(942);
				languagecode();
				}
				break;
			case LEFT_PAREN:
				{
				setState(943);
				languagecodeset();
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
	public static class LanguageContext extends ParserRuleContext {
		public TerminalNode CAP_L() { return getToken(ECLParser.CAP_L, 0); }
		public TerminalNode L() { return getToken(ECLParser.L, 0); }
		public List<TerminalNode> CAP_A() { return getTokens(ECLParser.CAP_A); }
		public TerminalNode CAP_A(int i) {
			return getToken(ECLParser.CAP_A, i);
		}
		public List<TerminalNode> A() { return getTokens(ECLParser.A); }
		public TerminalNode A(int i) {
			return getToken(ECLParser.A, i);
		}
		public TerminalNode CAP_N() { return getToken(ECLParser.CAP_N, 0); }
		public TerminalNode N() { return getToken(ECLParser.N, 0); }
		public List<TerminalNode> CAP_G() { return getTokens(ECLParser.CAP_G); }
		public TerminalNode CAP_G(int i) {
			return getToken(ECLParser.CAP_G, i);
		}
		public List<TerminalNode> G() { return getTokens(ECLParser.G); }
		public TerminalNode G(int i) {
			return getToken(ECLParser.G, i);
		}
		public TerminalNode CAP_U() { return getToken(ECLParser.CAP_U, 0); }
		public TerminalNode U() { return getToken(ECLParser.U, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public LanguageContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_language; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterLanguage(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitLanguage(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitLanguage(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LanguageContext language() throws RecognitionException {
		LanguageContext _localctx = new LanguageContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_language);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(948);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,83,_ctx) ) {
			case 1:
				{
				setState(946);
				_la = _input.LA(1);
				if ( !(_la==CAP_L || _la==L) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(947);
				_la = _input.LA(1);
				if ( !(_la==CAP_L || _la==L) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(952);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
			case 1:
				{
				setState(950);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(951);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(956);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
			case 1:
				{
				setState(954);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(955);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(960);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,86,_ctx) ) {
			case 1:
				{
				setState(958);
				_la = _input.LA(1);
				if ( !(_la==CAP_G || _la==G) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(959);
				_la = _input.LA(1);
				if ( !(_la==CAP_G || _la==G) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(964);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
			case 1:
				{
				setState(962);
				_la = _input.LA(1);
				if ( !(_la==CAP_U || _la==U) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(963);
				_la = _input.LA(1);
				if ( !(_la==CAP_U || _la==U) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(968);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
			case 1:
				{
				setState(966);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(967);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(972);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
			case 1:
				{
				setState(970);
				_la = _input.LA(1);
				if ( !(_la==CAP_G || _la==G) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(971);
				_la = _input.LA(1);
				if ( !(_la==CAP_G || _la==G) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(976);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,90,_ctx) ) {
			case 1:
				{
				setState(974);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(975);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class LanguagecodeContext extends ParserRuleContext {
		public List<AlphaContext> alpha() {
			return getRuleContexts(AlphaContext.class);
		}
		public AlphaContext alpha(int i) {
			return getRuleContext(AlphaContext.class,i);
		}
		public LanguagecodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_languagecode; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterLanguagecode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitLanguagecode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitLanguagecode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LanguagecodeContext languagecode() throws RecognitionException {
		LanguagecodeContext _localctx = new LanguagecodeContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_languagecode);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(978);
			alpha();
			setState(979);
			alpha();
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
	public static class LanguagecodesetContext extends ParserRuleContext {
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<LanguagecodeContext> languagecode() {
			return getRuleContexts(LanguagecodeContext.class);
		}
		public LanguagecodeContext languagecode(int i) {
			return getRuleContext(LanguagecodeContext.class,i);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public List<MwsContext> mws() {
			return getRuleContexts(MwsContext.class);
		}
		public MwsContext mws(int i) {
			return getRuleContext(MwsContext.class,i);
		}
		public LanguagecodesetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_languagecodeset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterLanguagecodeset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitLanguagecodeset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitLanguagecodeset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LanguagecodesetContext languagecodeset() throws RecognitionException {
		LanguagecodesetContext _localctx = new LanguagecodesetContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_languagecodeset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(981);
			match(LEFT_PAREN);
			setState(982);
			ws();
			setState(983);
			languagecode();
			setState(989);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,91,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(984);
					mws();
					setState(985);
					languagecode();
					}
					} 
				}
				setState(991);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,91,_ctx);
			}
			setState(992);
			ws();
			setState(993);
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
	public static class TypefilterContext extends ParserRuleContext {
		public TypeidfilterContext typeidfilter() {
			return getRuleContext(TypeidfilterContext.class,0);
		}
		public TypetokenfilterContext typetokenfilter() {
			return getRuleContext(TypetokenfilterContext.class,0);
		}
		public TypefilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typefilter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterTypefilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitTypefilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitTypefilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypefilterContext typefilter() throws RecognitionException {
		TypefilterContext _localctx = new TypefilterContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_typefilter);
		try {
			setState(997);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,92,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(995);
				typeidfilter();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(996);
				typetokenfilter();
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
	public static class TypeidfilterContext extends ParserRuleContext {
		public TypeidContext typeid() {
			return getRuleContext(TypeidContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public BooleancomparisonoperatorContext booleancomparisonoperator() {
			return getRuleContext(BooleancomparisonoperatorContext.class,0);
		}
		public SubexpressionconstraintContext subexpressionconstraint() {
			return getRuleContext(SubexpressionconstraintContext.class,0);
		}
		public EclconceptreferencesetContext eclconceptreferenceset() {
			return getRuleContext(EclconceptreferencesetContext.class,0);
		}
		public TypeidfilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeidfilter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterTypeidfilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitTypeidfilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitTypeidfilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeidfilterContext typeidfilter() throws RecognitionException {
		TypeidfilterContext _localctx = new TypeidfilterContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_typeidfilter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(999);
			typeid();
			setState(1000);
			ws();
			setState(1001);
			booleancomparisonoperator();
			setState(1002);
			ws();
			setState(1005);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,93,_ctx) ) {
			case 1:
				{
				setState(1003);
				subexpressionconstraint();
				}
				break;
			case 2:
				{
				setState(1004);
				eclconceptreferenceset();
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
	public static class TypeidContext extends ParserRuleContext {
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode CAP_Y() { return getToken(ECLParser.CAP_Y, 0); }
		public TerminalNode Y() { return getToken(ECLParser.Y, 0); }
		public TerminalNode CAP_P() { return getToken(ECLParser.CAP_P, 0); }
		public TerminalNode P() { return getToken(ECLParser.P, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public TerminalNode CAP_I() { return getToken(ECLParser.CAP_I, 0); }
		public TerminalNode I() { return getToken(ECLParser.I, 0); }
		public TerminalNode CAP_D() { return getToken(ECLParser.CAP_D, 0); }
		public TerminalNode D() { return getToken(ECLParser.D, 0); }
		public TypeidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterTypeid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitTypeid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitTypeid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeidContext typeid() throws RecognitionException {
		TypeidContext _localctx = new TypeidContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_typeid);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1009);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,94,_ctx) ) {
			case 1:
				{
				setState(1007);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1008);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1013);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,95,_ctx) ) {
			case 1:
				{
				setState(1011);
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
				break;
			case 2:
				{
				setState(1012);
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
				break;
			}
			setState(1017);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,96,_ctx) ) {
			case 1:
				{
				setState(1015);
				_la = _input.LA(1);
				if ( !(_la==CAP_P || _la==P) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1016);
				_la = _input.LA(1);
				if ( !(_la==CAP_P || _la==P) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1021);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,97,_ctx) ) {
			case 1:
				{
				setState(1019);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1020);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1025);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,98,_ctx) ) {
			case 1:
				{
				setState(1023);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1024);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1029);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,99,_ctx) ) {
			case 1:
				{
				setState(1027);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1028);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class TypetokenfilterContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public BooleancomparisonoperatorContext booleancomparisonoperator() {
			return getRuleContext(BooleancomparisonoperatorContext.class,0);
		}
		public TypetokenContext typetoken() {
			return getRuleContext(TypetokenContext.class,0);
		}
		public TypetokensetContext typetokenset() {
			return getRuleContext(TypetokensetContext.class,0);
		}
		public TypetokenfilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typetokenfilter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterTypetokenfilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitTypetokenfilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitTypetokenfilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypetokenfilterContext typetokenfilter() throws RecognitionException {
		TypetokenfilterContext _localctx = new TypetokenfilterContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_typetokenfilter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1031);
			type();
			setState(1032);
			ws();
			setState(1033);
			booleancomparisonoperator();
			setState(1034);
			ws();
			setState(1037);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CAP_D:
			case CAP_F:
			case CAP_S:
			case D:
			case F:
			case S:
				{
				setState(1035);
				typetoken();
				}
				break;
			case LEFT_PAREN:
				{
				setState(1036);
				typetokenset();
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
	public static class TypeContext extends ParserRuleContext {
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode CAP_Y() { return getToken(ECLParser.CAP_Y, 0); }
		public TerminalNode Y() { return getToken(ECLParser.Y, 0); }
		public TerminalNode CAP_P() { return getToken(ECLParser.CAP_P, 0); }
		public TerminalNode P() { return getToken(ECLParser.P, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1041);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,101,_ctx) ) {
			case 1:
				{
				setState(1039);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1040);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1045);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,102,_ctx) ) {
			case 1:
				{
				setState(1043);
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
				break;
			case 2:
				{
				setState(1044);
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
				break;
			}
			setState(1049);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
			case 1:
				{
				setState(1047);
				_la = _input.LA(1);
				if ( !(_la==CAP_P || _la==P) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1048);
				_la = _input.LA(1);
				if ( !(_la==CAP_P || _la==P) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1053);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,104,_ctx) ) {
			case 1:
				{
				setState(1051);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1052);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class TypetokenContext extends ParserRuleContext {
		public SynonymContext synonym() {
			return getRuleContext(SynonymContext.class,0);
		}
		public FullyspecifiednameContext fullyspecifiedname() {
			return getRuleContext(FullyspecifiednameContext.class,0);
		}
		public DefinitionContext definition() {
			return getRuleContext(DefinitionContext.class,0);
		}
		public TypetokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typetoken; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterTypetoken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitTypetoken(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitTypetoken(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypetokenContext typetoken() throws RecognitionException {
		TypetokenContext _localctx = new TypetokenContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_typetoken);
		try {
			setState(1058);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CAP_S:
			case S:
				enterOuterAlt(_localctx, 1);
				{
				setState(1055);
				synonym();
				}
				break;
			case CAP_F:
			case F:
				enterOuterAlt(_localctx, 2);
				{
				setState(1056);
				fullyspecifiedname();
				}
				break;
			case CAP_D:
			case D:
				enterOuterAlt(_localctx, 3);
				{
				setState(1057);
				definition();
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
	public static class TypetokensetContext extends ParserRuleContext {
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<TypetokenContext> typetoken() {
			return getRuleContexts(TypetokenContext.class);
		}
		public TypetokenContext typetoken(int i) {
			return getRuleContext(TypetokenContext.class,i);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public List<MwsContext> mws() {
			return getRuleContexts(MwsContext.class);
		}
		public MwsContext mws(int i) {
			return getRuleContext(MwsContext.class,i);
		}
		public TypetokensetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typetokenset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterTypetokenset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitTypetokenset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitTypetokenset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypetokensetContext typetokenset() throws RecognitionException {
		TypetokensetContext _localctx = new TypetokensetContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_typetokenset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1060);
			match(LEFT_PAREN);
			setState(1061);
			ws();
			setState(1062);
			typetoken();
			setState(1068);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,106,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1063);
					mws();
					setState(1064);
					typetoken();
					}
					} 
				}
				setState(1070);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,106,_ctx);
			}
			setState(1071);
			ws();
			setState(1072);
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
	public static class SynonymContext extends ParserRuleContext {
		public TerminalNode CAP_S() { return getToken(ECLParser.CAP_S, 0); }
		public TerminalNode S() { return getToken(ECLParser.S, 0); }
		public TerminalNode CAP_Y() { return getToken(ECLParser.CAP_Y, 0); }
		public TerminalNode Y() { return getToken(ECLParser.Y, 0); }
		public TerminalNode CAP_N() { return getToken(ECLParser.CAP_N, 0); }
		public TerminalNode N() { return getToken(ECLParser.N, 0); }
		public SynonymContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_synonym; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterSynonym(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitSynonym(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitSynonym(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SynonymContext synonym() throws RecognitionException {
		SynonymContext _localctx = new SynonymContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_synonym);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1076);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,107,_ctx) ) {
			case 1:
				{
				setState(1074);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1075);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1080);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,108,_ctx) ) {
			case 1:
				{
				setState(1078);
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
				break;
			case 2:
				{
				setState(1079);
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
				break;
			}
			setState(1084);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,109,_ctx) ) {
			case 1:
				{
				setState(1082);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1083);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class FullyspecifiednameContext extends ParserRuleContext {
		public TerminalNode CAP_F() { return getToken(ECLParser.CAP_F, 0); }
		public TerminalNode F() { return getToken(ECLParser.F, 0); }
		public TerminalNode CAP_S() { return getToken(ECLParser.CAP_S, 0); }
		public TerminalNode S() { return getToken(ECLParser.S, 0); }
		public TerminalNode CAP_N() { return getToken(ECLParser.CAP_N, 0); }
		public TerminalNode N() { return getToken(ECLParser.N, 0); }
		public FullyspecifiednameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fullyspecifiedname; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterFullyspecifiedname(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitFullyspecifiedname(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitFullyspecifiedname(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FullyspecifiednameContext fullyspecifiedname() throws RecognitionException {
		FullyspecifiednameContext _localctx = new FullyspecifiednameContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_fullyspecifiedname);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1088);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,110,_ctx) ) {
			case 1:
				{
				setState(1086);
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
				break;
			case 2:
				{
				setState(1087);
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
				break;
			}
			setState(1092);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,111,_ctx) ) {
			case 1:
				{
				setState(1090);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1091);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1096);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,112,_ctx) ) {
			case 1:
				{
				setState(1094);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1095);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class DefinitionContext extends ParserRuleContext {
		public TerminalNode CAP_D() { return getToken(ECLParser.CAP_D, 0); }
		public TerminalNode D() { return getToken(ECLParser.D, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public TerminalNode CAP_F() { return getToken(ECLParser.CAP_F, 0); }
		public TerminalNode F() { return getToken(ECLParser.F, 0); }
		public DefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefinitionContext definition() throws RecognitionException {
		DefinitionContext _localctx = new DefinitionContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_definition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1100);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,113,_ctx) ) {
			case 1:
				{
				setState(1098);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1099);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1104);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,114,_ctx) ) {
			case 1:
				{
				setState(1102);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1103);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1108);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,115,_ctx) ) {
			case 1:
				{
				setState(1106);
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
				break;
			case 2:
				{
				setState(1107);
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
	public static class DialectfilterContext extends ParserRuleContext {
		public DialectidfilterContext dialectidfilter() {
			return getRuleContext(DialectidfilterContext.class,0);
		}
		public DialectaliasfilterContext dialectaliasfilter() {
			return getRuleContext(DialectaliasfilterContext.class,0);
		}
		public WsContext ws() {
			return getRuleContext(WsContext.class,0);
		}
		public AcceptabilitysetContext acceptabilityset() {
			return getRuleContext(AcceptabilitysetContext.class,0);
		}
		public DialectfilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dialectfilter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDialectfilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDialectfilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDialectfilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DialectfilterContext dialectfilter() throws RecognitionException {
		DialectfilterContext _localctx = new DialectfilterContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_dialectfilter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1112);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,116,_ctx) ) {
			case 1:
				{
				setState(1110);
				dialectidfilter();
				}
				break;
			case 2:
				{
				setState(1111);
				dialectaliasfilter();
				}
				break;
			}
			setState(1117);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,117,_ctx) ) {
			case 1:
				{
				setState(1114);
				ws();
				setState(1115);
				acceptabilityset();
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
	public static class DialectidfilterContext extends ParserRuleContext {
		public DialectidContext dialectid() {
			return getRuleContext(DialectidContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public BooleancomparisonoperatorContext booleancomparisonoperator() {
			return getRuleContext(BooleancomparisonoperatorContext.class,0);
		}
		public SubexpressionconstraintContext subexpressionconstraint() {
			return getRuleContext(SubexpressionconstraintContext.class,0);
		}
		public DialectidsetContext dialectidset() {
			return getRuleContext(DialectidsetContext.class,0);
		}
		public DialectidfilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dialectidfilter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDialectidfilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDialectidfilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDialectidfilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DialectidfilterContext dialectidfilter() throws RecognitionException {
		DialectidfilterContext _localctx = new DialectidfilterContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_dialectidfilter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1119);
			dialectid();
			setState(1120);
			ws();
			setState(1121);
			booleancomparisonoperator();
			setState(1122);
			ws();
			setState(1125);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,118,_ctx) ) {
			case 1:
				{
				setState(1123);
				subexpressionconstraint();
				}
				break;
			case 2:
				{
				setState(1124);
				dialectidset();
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
	public static class DialectidContext extends ParserRuleContext {
		public List<TerminalNode> CAP_D() { return getTokens(ECLParser.CAP_D); }
		public TerminalNode CAP_D(int i) {
			return getToken(ECLParser.CAP_D, i);
		}
		public List<TerminalNode> D() { return getTokens(ECLParser.D); }
		public TerminalNode D(int i) {
			return getToken(ECLParser.D, i);
		}
		public List<TerminalNode> CAP_I() { return getTokens(ECLParser.CAP_I); }
		public TerminalNode CAP_I(int i) {
			return getToken(ECLParser.CAP_I, i);
		}
		public List<TerminalNode> I() { return getTokens(ECLParser.I); }
		public TerminalNode I(int i) {
			return getToken(ECLParser.I, i);
		}
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode CAP_L() { return getToken(ECLParser.CAP_L, 0); }
		public TerminalNode L() { return getToken(ECLParser.L, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public TerminalNode CAP_C() { return getToken(ECLParser.CAP_C, 0); }
		public TerminalNode C() { return getToken(ECLParser.C, 0); }
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public DialectidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dialectid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDialectid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDialectid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDialectid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DialectidContext dialectid() throws RecognitionException {
		DialectidContext _localctx = new DialectidContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_dialectid);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1129);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,119,_ctx) ) {
			case 1:
				{
				setState(1127);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1128);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1133);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,120,_ctx) ) {
			case 1:
				{
				setState(1131);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1132);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1137);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,121,_ctx) ) {
			case 1:
				{
				setState(1135);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1136);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1141);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,122,_ctx) ) {
			case 1:
				{
				setState(1139);
				_la = _input.LA(1);
				if ( !(_la==CAP_L || _la==L) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1140);
				_la = _input.LA(1);
				if ( !(_la==CAP_L || _la==L) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1145);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,123,_ctx) ) {
			case 1:
				{
				setState(1143);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1144);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1149);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,124,_ctx) ) {
			case 1:
				{
				setState(1147);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1148);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1153);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,125,_ctx) ) {
			case 1:
				{
				setState(1151);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1152);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1157);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,126,_ctx) ) {
			case 1:
				{
				setState(1155);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1156);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1161);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,127,_ctx) ) {
			case 1:
				{
				setState(1159);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1160);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class DialectaliasfilterContext extends ParserRuleContext {
		public DialectContext dialect() {
			return getRuleContext(DialectContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public BooleancomparisonoperatorContext booleancomparisonoperator() {
			return getRuleContext(BooleancomparisonoperatorContext.class,0);
		}
		public DialectaliasContext dialectalias() {
			return getRuleContext(DialectaliasContext.class,0);
		}
		public DialectaliassetContext dialectaliasset() {
			return getRuleContext(DialectaliassetContext.class,0);
		}
		public DialectaliasfilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dialectaliasfilter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDialectaliasfilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDialectaliasfilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDialectaliasfilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DialectaliasfilterContext dialectaliasfilter() throws RecognitionException {
		DialectaliasfilterContext _localctx = new DialectaliasfilterContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_dialectaliasfilter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1163);
			dialect();
			setState(1164);
			ws();
			setState(1165);
			booleancomparisonoperator();
			setState(1166);
			ws();
			setState(1169);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
				{
				setState(1167);
				dialectalias();
				}
				break;
			case LEFT_PAREN:
				{
				setState(1168);
				dialectaliasset();
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
	public static class DialectContext extends ParserRuleContext {
		public TerminalNode CAP_D() { return getToken(ECLParser.CAP_D, 0); }
		public TerminalNode D() { return getToken(ECLParser.D, 0); }
		public TerminalNode CAP_I() { return getToken(ECLParser.CAP_I, 0); }
		public TerminalNode I() { return getToken(ECLParser.I, 0); }
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode CAP_L() { return getToken(ECLParser.CAP_L, 0); }
		public TerminalNode L() { return getToken(ECLParser.L, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public TerminalNode CAP_C() { return getToken(ECLParser.CAP_C, 0); }
		public TerminalNode C() { return getToken(ECLParser.C, 0); }
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public DialectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dialect; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDialect(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDialect(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDialect(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DialectContext dialect() throws RecognitionException {
		DialectContext _localctx = new DialectContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_dialect);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1173);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,129,_ctx) ) {
			case 1:
				{
				setState(1171);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1172);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1177);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,130,_ctx) ) {
			case 1:
				{
				setState(1175);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1176);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1181);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,131,_ctx) ) {
			case 1:
				{
				setState(1179);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1180);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1185);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,132,_ctx) ) {
			case 1:
				{
				setState(1183);
				_la = _input.LA(1);
				if ( !(_la==CAP_L || _la==L) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1184);
				_la = _input.LA(1);
				if ( !(_la==CAP_L || _la==L) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1189);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,133,_ctx) ) {
			case 1:
				{
				setState(1187);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1188);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1193);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,134,_ctx) ) {
			case 1:
				{
				setState(1191);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1192);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1197);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,135,_ctx) ) {
			case 1:
				{
				setState(1195);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1196);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class DialectaliasContext extends ParserRuleContext {
		public List<AlphaContext> alpha() {
			return getRuleContexts(AlphaContext.class);
		}
		public AlphaContext alpha(int i) {
			return getRuleContext(AlphaContext.class,i);
		}
		public List<DashContext> dash() {
			return getRuleContexts(DashContext.class);
		}
		public DashContext dash(int i) {
			return getRuleContext(DashContext.class,i);
		}
		public List<IntegervalueContext> integervalue() {
			return getRuleContexts(IntegervalueContext.class);
		}
		public IntegervalueContext integervalue(int i) {
			return getRuleContext(IntegervalueContext.class,i);
		}
		public DialectaliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dialectalias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDialectalias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDialectalias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDialectalias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DialectaliasContext dialectalias() throws RecognitionException {
		DialectaliasContext _localctx = new DialectaliasContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_dialectalias);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1199);
			alpha();
			setState(1205);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -272732258304L) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & 67108863L) != 0)) {
				{
				setState(1203);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case DASH:
					{
					setState(1200);
					dash();
					}
					break;
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
					{
					setState(1201);
					alpha();
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
					{
					setState(1202);
					integervalue();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(1207);
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
	public static class DialectaliassetContext extends ParserRuleContext {
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<DialectaliasContext> dialectalias() {
			return getRuleContexts(DialectaliasContext.class);
		}
		public DialectaliasContext dialectalias(int i) {
			return getRuleContext(DialectaliasContext.class,i);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public List<AcceptabilitysetContext> acceptabilityset() {
			return getRuleContexts(AcceptabilitysetContext.class);
		}
		public AcceptabilitysetContext acceptabilityset(int i) {
			return getRuleContext(AcceptabilitysetContext.class,i);
		}
		public List<MwsContext> mws() {
			return getRuleContexts(MwsContext.class);
		}
		public MwsContext mws(int i) {
			return getRuleContext(MwsContext.class,i);
		}
		public DialectaliassetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dialectaliasset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDialectaliasset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDialectaliasset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDialectaliasset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DialectaliassetContext dialectaliasset() throws RecognitionException {
		DialectaliassetContext _localctx = new DialectaliassetContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_dialectaliasset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1208);
			match(LEFT_PAREN);
			setState(1209);
			ws();
			setState(1210);
			dialectalias();
			setState(1214);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,138,_ctx) ) {
			case 1:
				{
				setState(1211);
				ws();
				setState(1212);
				acceptabilityset();
				}
				break;
			}
			setState(1225);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,140,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1216);
					mws();
					setState(1217);
					dialectalias();
					setState(1221);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,139,_ctx) ) {
					case 1:
						{
						setState(1218);
						ws();
						setState(1219);
						acceptabilityset();
						}
						break;
					}
					}
					} 
				}
				setState(1227);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,140,_ctx);
			}
			setState(1228);
			ws();
			setState(1229);
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
	public static class DialectidsetContext extends ParserRuleContext {
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<EclconceptreferenceContext> eclconceptreference() {
			return getRuleContexts(EclconceptreferenceContext.class);
		}
		public EclconceptreferenceContext eclconceptreference(int i) {
			return getRuleContext(EclconceptreferenceContext.class,i);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public List<AcceptabilitysetContext> acceptabilityset() {
			return getRuleContexts(AcceptabilitysetContext.class);
		}
		public AcceptabilitysetContext acceptabilityset(int i) {
			return getRuleContext(AcceptabilitysetContext.class,i);
		}
		public List<MwsContext> mws() {
			return getRuleContexts(MwsContext.class);
		}
		public MwsContext mws(int i) {
			return getRuleContext(MwsContext.class,i);
		}
		public DialectidsetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dialectidset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDialectidset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDialectidset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDialectidset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DialectidsetContext dialectidset() throws RecognitionException {
		DialectidsetContext _localctx = new DialectidsetContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_dialectidset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1231);
			match(LEFT_PAREN);
			setState(1232);
			ws();
			setState(1233);
			eclconceptreference();
			setState(1237);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,141,_ctx) ) {
			case 1:
				{
				setState(1234);
				ws();
				setState(1235);
				acceptabilityset();
				}
				break;
			}
			setState(1248);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,143,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1239);
					mws();
					setState(1240);
					eclconceptreference();
					setState(1244);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,142,_ctx) ) {
					case 1:
						{
						setState(1241);
						ws();
						setState(1242);
						acceptabilityset();
						}
						break;
					}
					}
					} 
				}
				setState(1250);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,143,_ctx);
			}
			setState(1251);
			ws();
			setState(1252);
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
	public static class AcceptabilitysetContext extends ParserRuleContext {
		public AcceptabilityconceptreferencesetContext acceptabilityconceptreferenceset() {
			return getRuleContext(AcceptabilityconceptreferencesetContext.class,0);
		}
		public AcceptabilitytokensetContext acceptabilitytokenset() {
			return getRuleContext(AcceptabilitytokensetContext.class,0);
		}
		public AcceptabilitysetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_acceptabilityset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterAcceptabilityset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitAcceptabilityset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitAcceptabilityset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AcceptabilitysetContext acceptabilityset() throws RecognitionException {
		AcceptabilitysetContext _localctx = new AcceptabilitysetContext(_ctx, getState());
		enterRule(_localctx, 180, RULE_acceptabilityset);
		try {
			setState(1256);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,144,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1254);
				acceptabilityconceptreferenceset();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1255);
				acceptabilitytokenset();
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
	public static class AcceptabilityconceptreferencesetContext extends ParserRuleContext {
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<EclconceptreferenceContext> eclconceptreference() {
			return getRuleContexts(EclconceptreferenceContext.class);
		}
		public EclconceptreferenceContext eclconceptreference(int i) {
			return getRuleContext(EclconceptreferenceContext.class,i);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public List<MwsContext> mws() {
			return getRuleContexts(MwsContext.class);
		}
		public MwsContext mws(int i) {
			return getRuleContext(MwsContext.class,i);
		}
		public AcceptabilityconceptreferencesetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_acceptabilityconceptreferenceset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterAcceptabilityconceptreferenceset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitAcceptabilityconceptreferenceset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitAcceptabilityconceptreferenceset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AcceptabilityconceptreferencesetContext acceptabilityconceptreferenceset() throws RecognitionException {
		AcceptabilityconceptreferencesetContext _localctx = new AcceptabilityconceptreferencesetContext(_ctx, getState());
		enterRule(_localctx, 182, RULE_acceptabilityconceptreferenceset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1258);
			match(LEFT_PAREN);
			setState(1259);
			ws();
			setState(1260);
			eclconceptreference();
			setState(1266);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,145,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1261);
					mws();
					setState(1262);
					eclconceptreference();
					}
					} 
				}
				setState(1268);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,145,_ctx);
			}
			setState(1269);
			ws();
			setState(1270);
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
	public static class AcceptabilitytokensetContext extends ParserRuleContext {
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<AcceptabilitytokenContext> acceptabilitytoken() {
			return getRuleContexts(AcceptabilitytokenContext.class);
		}
		public AcceptabilitytokenContext acceptabilitytoken(int i) {
			return getRuleContext(AcceptabilitytokenContext.class,i);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public List<MwsContext> mws() {
			return getRuleContexts(MwsContext.class);
		}
		public MwsContext mws(int i) {
			return getRuleContext(MwsContext.class,i);
		}
		public AcceptabilitytokensetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_acceptabilitytokenset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterAcceptabilitytokenset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitAcceptabilitytokenset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitAcceptabilitytokenset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AcceptabilitytokensetContext acceptabilitytokenset() throws RecognitionException {
		AcceptabilitytokensetContext _localctx = new AcceptabilitytokensetContext(_ctx, getState());
		enterRule(_localctx, 184, RULE_acceptabilitytokenset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1272);
			match(LEFT_PAREN);
			setState(1273);
			ws();
			setState(1274);
			acceptabilitytoken();
			setState(1280);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,146,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1275);
					mws();
					setState(1276);
					acceptabilitytoken();
					}
					} 
				}
				setState(1282);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,146,_ctx);
			}
			setState(1283);
			ws();
			setState(1284);
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
	public static class AcceptabilitytokenContext extends ParserRuleContext {
		public AcceptableContext acceptable() {
			return getRuleContext(AcceptableContext.class,0);
		}
		public PreferredContext preferred() {
			return getRuleContext(PreferredContext.class,0);
		}
		public AcceptabilitytokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_acceptabilitytoken; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterAcceptabilitytoken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitAcceptabilitytoken(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitAcceptabilitytoken(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AcceptabilitytokenContext acceptabilitytoken() throws RecognitionException {
		AcceptabilitytokenContext _localctx = new AcceptabilitytokenContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_acceptabilitytoken);
		try {
			setState(1288);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CAP_A:
			case A:
				enterOuterAlt(_localctx, 1);
				{
				setState(1286);
				acceptable();
				}
				break;
			case CAP_P:
			case P:
				enterOuterAlt(_localctx, 2);
				{
				setState(1287);
				preferred();
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
	public static class AcceptableContext extends ParserRuleContext {
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public List<TerminalNode> CAP_C() { return getTokens(ECLParser.CAP_C); }
		public TerminalNode CAP_C(int i) {
			return getToken(ECLParser.CAP_C, i);
		}
		public List<TerminalNode> C() { return getTokens(ECLParser.C); }
		public TerminalNode C(int i) {
			return getToken(ECLParser.C, i);
		}
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public TerminalNode CAP_P() { return getToken(ECLParser.CAP_P, 0); }
		public TerminalNode P() { return getToken(ECLParser.P, 0); }
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public AcceptableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_acceptable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterAcceptable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitAcceptable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitAcceptable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AcceptableContext acceptable() throws RecognitionException {
		AcceptableContext _localctx = new AcceptableContext(_ctx, getState());
		enterRule(_localctx, 188, RULE_acceptable);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1292);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,148,_ctx) ) {
			case 1:
				{
				setState(1290);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1291);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1296);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,149,_ctx) ) {
			case 1:
				{
				setState(1294);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1295);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1300);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,150,_ctx) ) {
			case 1:
				{
				setState(1298);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1299);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1304);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,151,_ctx) ) {
			case 1:
				{
				setState(1302);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1303);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1308);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,152,_ctx) ) {
			case 1:
				{
				setState(1306);
				_la = _input.LA(1);
				if ( !(_la==CAP_P || _la==P) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1307);
				_la = _input.LA(1);
				if ( !(_la==CAP_P || _la==P) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1312);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,153,_ctx) ) {
			case 1:
				{
				setState(1310);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1311);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class PreferredContext extends ParserRuleContext {
		public TerminalNode CAP_P() { return getToken(ECLParser.CAP_P, 0); }
		public TerminalNode P() { return getToken(ECLParser.P, 0); }
		public List<TerminalNode> CAP_R() { return getTokens(ECLParser.CAP_R); }
		public TerminalNode CAP_R(int i) {
			return getToken(ECLParser.CAP_R, i);
		}
		public List<TerminalNode> R() { return getTokens(ECLParser.R); }
		public TerminalNode R(int i) {
			return getToken(ECLParser.R, i);
		}
		public List<TerminalNode> CAP_E() { return getTokens(ECLParser.CAP_E); }
		public TerminalNode CAP_E(int i) {
			return getToken(ECLParser.CAP_E, i);
		}
		public List<TerminalNode> E() { return getTokens(ECLParser.E); }
		public TerminalNode E(int i) {
			return getToken(ECLParser.E, i);
		}
		public TerminalNode CAP_F() { return getToken(ECLParser.CAP_F, 0); }
		public TerminalNode F() { return getToken(ECLParser.F, 0); }
		public PreferredContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_preferred; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterPreferred(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitPreferred(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitPreferred(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PreferredContext preferred() throws RecognitionException {
		PreferredContext _localctx = new PreferredContext(_ctx, getState());
		enterRule(_localctx, 190, RULE_preferred);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1316);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,154,_ctx) ) {
			case 1:
				{
				setState(1314);
				_la = _input.LA(1);
				if ( !(_la==CAP_P || _la==P) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1315);
				_la = _input.LA(1);
				if ( !(_la==CAP_P || _la==P) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1320);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,155,_ctx) ) {
			case 1:
				{
				setState(1318);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1319);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1324);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,156,_ctx) ) {
			case 1:
				{
				setState(1322);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1323);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1328);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,157,_ctx) ) {
			case 1:
				{
				setState(1326);
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
				break;
			case 2:
				{
				setState(1327);
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
				break;
			}
			setState(1332);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,158,_ctx) ) {
			case 1:
				{
				setState(1330);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1331);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1336);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,159,_ctx) ) {
			case 1:
				{
				setState(1334);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1335);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class ConceptfilterconstraintContext extends ParserRuleContext {
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<ConceptfilterContext> conceptfilter() {
			return getRuleContexts(ConceptfilterContext.class);
		}
		public ConceptfilterContext conceptfilter(int i) {
			return getRuleContext(ConceptfilterContext.class,i);
		}
		public List<TerminalNode> LEFT_CURLY_BRACE() { return getTokens(ECLParser.LEFT_CURLY_BRACE); }
		public TerminalNode LEFT_CURLY_BRACE(int i) {
			return getToken(ECLParser.LEFT_CURLY_BRACE, i);
		}
		public List<TerminalNode> RIGHT_CURLY_BRACE() { return getTokens(ECLParser.RIGHT_CURLY_BRACE); }
		public TerminalNode RIGHT_CURLY_BRACE(int i) {
			return getToken(ECLParser.RIGHT_CURLY_BRACE, i);
		}
		public TerminalNode CAP_C() { return getToken(ECLParser.CAP_C, 0); }
		public TerminalNode C() { return getToken(ECLParser.C, 0); }
		public List<TerminalNode> COMMA() { return getTokens(ECLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ECLParser.COMMA, i);
		}
		public ConceptfilterconstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conceptfilterconstraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterConceptfilterconstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitConceptfilterconstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitConceptfilterconstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConceptfilterconstraintContext conceptfilterconstraint() throws RecognitionException {
		ConceptfilterconstraintContext _localctx = new ConceptfilterconstraintContext(_ctx, getState());
		enterRule(_localctx, 192, RULE_conceptfilterconstraint);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(1338);
			match(LEFT_CURLY_BRACE);
			setState(1339);
			match(LEFT_CURLY_BRACE);
			}
			setState(1341);
			ws();
			setState(1344);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,160,_ctx) ) {
			case 1:
				{
				setState(1342);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1343);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1346);
			ws();
			setState(1347);
			conceptfilter();
			setState(1355);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,161,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1348);
					ws();
					setState(1349);
					match(COMMA);
					setState(1350);
					ws();
					setState(1351);
					conceptfilter();
					}
					} 
				}
				setState(1357);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,161,_ctx);
			}
			setState(1358);
			ws();
			{
			setState(1359);
			match(RIGHT_CURLY_BRACE);
			setState(1360);
			match(RIGHT_CURLY_BRACE);
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
	public static class ConceptfilterContext extends ParserRuleContext {
		public DefinitionstatusfilterContext definitionstatusfilter() {
			return getRuleContext(DefinitionstatusfilterContext.class,0);
		}
		public ModulefilterContext modulefilter() {
			return getRuleContext(ModulefilterContext.class,0);
		}
		public EffectivetimefilterContext effectivetimefilter() {
			return getRuleContext(EffectivetimefilterContext.class,0);
		}
		public ActivefilterContext activefilter() {
			return getRuleContext(ActivefilterContext.class,0);
		}
		public ConceptfilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conceptfilter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterConceptfilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitConceptfilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitConceptfilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConceptfilterContext conceptfilter() throws RecognitionException {
		ConceptfilterContext _localctx = new ConceptfilterContext(_ctx, getState());
		enterRule(_localctx, 194, RULE_conceptfilter);
		try {
			setState(1366);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CAP_D:
			case D:
				enterOuterAlt(_localctx, 1);
				{
				setState(1362);
				definitionstatusfilter();
				}
				break;
			case CAP_M:
			case M:
				enterOuterAlt(_localctx, 2);
				{
				setState(1363);
				modulefilter();
				}
				break;
			case CAP_E:
			case E:
				enterOuterAlt(_localctx, 3);
				{
				setState(1364);
				effectivetimefilter();
				}
				break;
			case CAP_A:
			case A:
				enterOuterAlt(_localctx, 4);
				{
				setState(1365);
				activefilter();
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
	public static class DefinitionstatusfilterContext extends ParserRuleContext {
		public DefinitionstatusidfilterContext definitionstatusidfilter() {
			return getRuleContext(DefinitionstatusidfilterContext.class,0);
		}
		public DefinitionstatustokenfilterContext definitionstatustokenfilter() {
			return getRuleContext(DefinitionstatustokenfilterContext.class,0);
		}
		public DefinitionstatusfilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definitionstatusfilter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDefinitionstatusfilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDefinitionstatusfilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDefinitionstatusfilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefinitionstatusfilterContext definitionstatusfilter() throws RecognitionException {
		DefinitionstatusfilterContext _localctx = new DefinitionstatusfilterContext(_ctx, getState());
		enterRule(_localctx, 196, RULE_definitionstatusfilter);
		try {
			setState(1370);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,163,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1368);
				definitionstatusidfilter();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1369);
				definitionstatustokenfilter();
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
	public static class DefinitionstatusidfilterContext extends ParserRuleContext {
		public DefinitionstatusidkeywordContext definitionstatusidkeyword() {
			return getRuleContext(DefinitionstatusidkeywordContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public BooleancomparisonoperatorContext booleancomparisonoperator() {
			return getRuleContext(BooleancomparisonoperatorContext.class,0);
		}
		public SubexpressionconstraintContext subexpressionconstraint() {
			return getRuleContext(SubexpressionconstraintContext.class,0);
		}
		public EclconceptreferencesetContext eclconceptreferenceset() {
			return getRuleContext(EclconceptreferencesetContext.class,0);
		}
		public DefinitionstatusidfilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definitionstatusidfilter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDefinitionstatusidfilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDefinitionstatusidfilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDefinitionstatusidfilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefinitionstatusidfilterContext definitionstatusidfilter() throws RecognitionException {
		DefinitionstatusidfilterContext _localctx = new DefinitionstatusidfilterContext(_ctx, getState());
		enterRule(_localctx, 198, RULE_definitionstatusidfilter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1372);
			definitionstatusidkeyword();
			setState(1373);
			ws();
			setState(1374);
			booleancomparisonoperator();
			setState(1375);
			ws();
			setState(1378);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,164,_ctx) ) {
			case 1:
				{
				setState(1376);
				subexpressionconstraint();
				}
				break;
			case 2:
				{
				setState(1377);
				eclconceptreferenceset();
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
	public static class DefinitionstatusidkeywordContext extends ParserRuleContext {
		public List<TerminalNode> CAP_D() { return getTokens(ECLParser.CAP_D); }
		public TerminalNode CAP_D(int i) {
			return getToken(ECLParser.CAP_D, i);
		}
		public List<TerminalNode> D() { return getTokens(ECLParser.D); }
		public TerminalNode D(int i) {
			return getToken(ECLParser.D, i);
		}
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public TerminalNode CAP_F() { return getToken(ECLParser.CAP_F, 0); }
		public TerminalNode F() { return getToken(ECLParser.F, 0); }
		public List<TerminalNode> CAP_I() { return getTokens(ECLParser.CAP_I); }
		public TerminalNode CAP_I(int i) {
			return getToken(ECLParser.CAP_I, i);
		}
		public List<TerminalNode> I() { return getTokens(ECLParser.I); }
		public TerminalNode I(int i) {
			return getToken(ECLParser.I, i);
		}
		public List<TerminalNode> CAP_N() { return getTokens(ECLParser.CAP_N); }
		public TerminalNode CAP_N(int i) {
			return getToken(ECLParser.CAP_N, i);
		}
		public List<TerminalNode> N() { return getTokens(ECLParser.N); }
		public TerminalNode N(int i) {
			return getToken(ECLParser.N, i);
		}
		public List<TerminalNode> CAP_T() { return getTokens(ECLParser.CAP_T); }
		public TerminalNode CAP_T(int i) {
			return getToken(ECLParser.CAP_T, i);
		}
		public List<TerminalNode> T() { return getTokens(ECLParser.T); }
		public TerminalNode T(int i) {
			return getToken(ECLParser.T, i);
		}
		public TerminalNode CAP_O() { return getToken(ECLParser.CAP_O, 0); }
		public TerminalNode O() { return getToken(ECLParser.O, 0); }
		public List<TerminalNode> CAP_S() { return getTokens(ECLParser.CAP_S); }
		public TerminalNode CAP_S(int i) {
			return getToken(ECLParser.CAP_S, i);
		}
		public List<TerminalNode> S() { return getTokens(ECLParser.S); }
		public TerminalNode S(int i) {
			return getToken(ECLParser.S, i);
		}
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode CAP_U() { return getToken(ECLParser.CAP_U, 0); }
		public TerminalNode U() { return getToken(ECLParser.U, 0); }
		public DefinitionstatusidkeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definitionstatusidkeyword; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDefinitionstatusidkeyword(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDefinitionstatusidkeyword(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDefinitionstatusidkeyword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefinitionstatusidkeywordContext definitionstatusidkeyword() throws RecognitionException {
		DefinitionstatusidkeywordContext _localctx = new DefinitionstatusidkeywordContext(_ctx, getState());
		enterRule(_localctx, 200, RULE_definitionstatusidkeyword);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1382);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,165,_ctx) ) {
			case 1:
				{
				setState(1380);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1381);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1386);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,166,_ctx) ) {
			case 1:
				{
				setState(1384);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1385);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1390);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,167,_ctx) ) {
			case 1:
				{
				setState(1388);
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
				break;
			case 2:
				{
				setState(1389);
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
				break;
			}
			setState(1394);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,168,_ctx) ) {
			case 1:
				{
				setState(1392);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1393);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1398);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,169,_ctx) ) {
			case 1:
				{
				setState(1396);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1397);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1402);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,170,_ctx) ) {
			case 1:
				{
				setState(1400);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1401);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1406);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,171,_ctx) ) {
			case 1:
				{
				setState(1404);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1405);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1410);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,172,_ctx) ) {
			case 1:
				{
				setState(1408);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1409);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1414);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,173,_ctx) ) {
			case 1:
				{
				setState(1412);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1413);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1418);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,174,_ctx) ) {
			case 1:
				{
				setState(1416);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1417);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1422);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,175,_ctx) ) {
			case 1:
				{
				setState(1420);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1421);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1426);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,176,_ctx) ) {
			case 1:
				{
				setState(1424);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1425);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1430);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,177,_ctx) ) {
			case 1:
				{
				setState(1428);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1429);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1434);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,178,_ctx) ) {
			case 1:
				{
				setState(1432);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1433);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1438);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,179,_ctx) ) {
			case 1:
				{
				setState(1436);
				_la = _input.LA(1);
				if ( !(_la==CAP_U || _la==U) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1437);
				_la = _input.LA(1);
				if ( !(_la==CAP_U || _la==U) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1442);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,180,_ctx) ) {
			case 1:
				{
				setState(1440);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1441);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1446);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,181,_ctx) ) {
			case 1:
				{
				setState(1444);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1445);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1450);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,182,_ctx) ) {
			case 1:
				{
				setState(1448);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1449);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class DefinitionstatustokenfilterContext extends ParserRuleContext {
		public DefinitionstatuskeywordContext definitionstatuskeyword() {
			return getRuleContext(DefinitionstatuskeywordContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public BooleancomparisonoperatorContext booleancomparisonoperator() {
			return getRuleContext(BooleancomparisonoperatorContext.class,0);
		}
		public DefinitionstatustokenContext definitionstatustoken() {
			return getRuleContext(DefinitionstatustokenContext.class,0);
		}
		public DefinitionstatustokensetContext definitionstatustokenset() {
			return getRuleContext(DefinitionstatustokensetContext.class,0);
		}
		public DefinitionstatustokenfilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definitionstatustokenfilter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDefinitionstatustokenfilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDefinitionstatustokenfilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDefinitionstatustokenfilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefinitionstatustokenfilterContext definitionstatustokenfilter() throws RecognitionException {
		DefinitionstatustokenfilterContext _localctx = new DefinitionstatustokenfilterContext(_ctx, getState());
		enterRule(_localctx, 202, RULE_definitionstatustokenfilter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1452);
			definitionstatuskeyword();
			setState(1453);
			ws();
			setState(1454);
			booleancomparisonoperator();
			setState(1455);
			ws();
			setState(1458);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CAP_D:
			case CAP_P:
			case D:
			case P:
				{
				setState(1456);
				definitionstatustoken();
				}
				break;
			case LEFT_PAREN:
				{
				setState(1457);
				definitionstatustokenset();
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
	public static class DefinitionstatuskeywordContext extends ParserRuleContext {
		public TerminalNode CAP_D() { return getToken(ECLParser.CAP_D, 0); }
		public TerminalNode D() { return getToken(ECLParser.D, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public TerminalNode CAP_F() { return getToken(ECLParser.CAP_F, 0); }
		public TerminalNode F() { return getToken(ECLParser.F, 0); }
		public List<TerminalNode> CAP_I() { return getTokens(ECLParser.CAP_I); }
		public TerminalNode CAP_I(int i) {
			return getToken(ECLParser.CAP_I, i);
		}
		public List<TerminalNode> I() { return getTokens(ECLParser.I); }
		public TerminalNode I(int i) {
			return getToken(ECLParser.I, i);
		}
		public List<TerminalNode> CAP_N() { return getTokens(ECLParser.CAP_N); }
		public TerminalNode CAP_N(int i) {
			return getToken(ECLParser.CAP_N, i);
		}
		public List<TerminalNode> N() { return getTokens(ECLParser.N); }
		public TerminalNode N(int i) {
			return getToken(ECLParser.N, i);
		}
		public List<TerminalNode> CAP_T() { return getTokens(ECLParser.CAP_T); }
		public TerminalNode CAP_T(int i) {
			return getToken(ECLParser.CAP_T, i);
		}
		public List<TerminalNode> T() { return getTokens(ECLParser.T); }
		public TerminalNode T(int i) {
			return getToken(ECLParser.T, i);
		}
		public TerminalNode CAP_O() { return getToken(ECLParser.CAP_O, 0); }
		public TerminalNode O() { return getToken(ECLParser.O, 0); }
		public List<TerminalNode> CAP_S() { return getTokens(ECLParser.CAP_S); }
		public TerminalNode CAP_S(int i) {
			return getToken(ECLParser.CAP_S, i);
		}
		public List<TerminalNode> S() { return getTokens(ECLParser.S); }
		public TerminalNode S(int i) {
			return getToken(ECLParser.S, i);
		}
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode CAP_U() { return getToken(ECLParser.CAP_U, 0); }
		public TerminalNode U() { return getToken(ECLParser.U, 0); }
		public DefinitionstatuskeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definitionstatuskeyword; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDefinitionstatuskeyword(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDefinitionstatuskeyword(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDefinitionstatuskeyword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefinitionstatuskeywordContext definitionstatuskeyword() throws RecognitionException {
		DefinitionstatuskeywordContext _localctx = new DefinitionstatuskeywordContext(_ctx, getState());
		enterRule(_localctx, 204, RULE_definitionstatuskeyword);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1462);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,184,_ctx) ) {
			case 1:
				{
				setState(1460);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1461);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1466);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,185,_ctx) ) {
			case 1:
				{
				setState(1464);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1465);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1470);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,186,_ctx) ) {
			case 1:
				{
				setState(1468);
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
				break;
			case 2:
				{
				setState(1469);
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
				break;
			}
			setState(1474);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,187,_ctx) ) {
			case 1:
				{
				setState(1472);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1473);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1478);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,188,_ctx) ) {
			case 1:
				{
				setState(1476);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1477);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1482);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,189,_ctx) ) {
			case 1:
				{
				setState(1480);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1481);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1486);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,190,_ctx) ) {
			case 1:
				{
				setState(1484);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1485);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1490);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,191,_ctx) ) {
			case 1:
				{
				setState(1488);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1489);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1494);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,192,_ctx) ) {
			case 1:
				{
				setState(1492);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1493);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1498);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,193,_ctx) ) {
			case 1:
				{
				setState(1496);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1497);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1502);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,194,_ctx) ) {
			case 1:
				{
				setState(1500);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1501);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1506);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,195,_ctx) ) {
			case 1:
				{
				setState(1504);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1505);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1510);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,196,_ctx) ) {
			case 1:
				{
				setState(1508);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1509);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1514);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,197,_ctx) ) {
			case 1:
				{
				setState(1512);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1513);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1518);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,198,_ctx) ) {
			case 1:
				{
				setState(1516);
				_la = _input.LA(1);
				if ( !(_la==CAP_U || _la==U) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1517);
				_la = _input.LA(1);
				if ( !(_la==CAP_U || _la==U) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1522);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,199,_ctx) ) {
			case 1:
				{
				setState(1520);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1521);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class DefinitionstatustokenContext extends ParserRuleContext {
		public PrimitivetokenContext primitivetoken() {
			return getRuleContext(PrimitivetokenContext.class,0);
		}
		public DefinedtokenContext definedtoken() {
			return getRuleContext(DefinedtokenContext.class,0);
		}
		public DefinitionstatustokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definitionstatustoken; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDefinitionstatustoken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDefinitionstatustoken(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDefinitionstatustoken(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefinitionstatustokenContext definitionstatustoken() throws RecognitionException {
		DefinitionstatustokenContext _localctx = new DefinitionstatustokenContext(_ctx, getState());
		enterRule(_localctx, 206, RULE_definitionstatustoken);
		try {
			setState(1526);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CAP_P:
			case P:
				enterOuterAlt(_localctx, 1);
				{
				setState(1524);
				primitivetoken();
				}
				break;
			case CAP_D:
			case D:
				enterOuterAlt(_localctx, 2);
				{
				setState(1525);
				definedtoken();
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
	public static class DefinitionstatustokensetContext extends ParserRuleContext {
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<DefinitionstatustokenContext> definitionstatustoken() {
			return getRuleContexts(DefinitionstatustokenContext.class);
		}
		public DefinitionstatustokenContext definitionstatustoken(int i) {
			return getRuleContext(DefinitionstatustokenContext.class,i);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public List<MwsContext> mws() {
			return getRuleContexts(MwsContext.class);
		}
		public MwsContext mws(int i) {
			return getRuleContext(MwsContext.class,i);
		}
		public DefinitionstatustokensetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definitionstatustokenset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDefinitionstatustokenset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDefinitionstatustokenset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDefinitionstatustokenset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefinitionstatustokensetContext definitionstatustokenset() throws RecognitionException {
		DefinitionstatustokensetContext _localctx = new DefinitionstatustokensetContext(_ctx, getState());
		enterRule(_localctx, 208, RULE_definitionstatustokenset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1528);
			match(LEFT_PAREN);
			setState(1529);
			ws();
			setState(1530);
			definitionstatustoken();
			setState(1536);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,201,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1531);
					mws();
					setState(1532);
					definitionstatustoken();
					}
					} 
				}
				setState(1538);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,201,_ctx);
			}
			setState(1539);
			ws();
			setState(1540);
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
	public static class PrimitivetokenContext extends ParserRuleContext {
		public TerminalNode CAP_P() { return getToken(ECLParser.CAP_P, 0); }
		public TerminalNode P() { return getToken(ECLParser.P, 0); }
		public TerminalNode CAP_R() { return getToken(ECLParser.CAP_R, 0); }
		public TerminalNode R() { return getToken(ECLParser.R, 0); }
		public List<TerminalNode> CAP_I() { return getTokens(ECLParser.CAP_I); }
		public TerminalNode CAP_I(int i) {
			return getToken(ECLParser.CAP_I, i);
		}
		public List<TerminalNode> I() { return getTokens(ECLParser.I); }
		public TerminalNode I(int i) {
			return getToken(ECLParser.I, i);
		}
		public TerminalNode CAP_M() { return getToken(ECLParser.CAP_M, 0); }
		public TerminalNode M() { return getToken(ECLParser.M, 0); }
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode CAP_V() { return getToken(ECLParser.CAP_V, 0); }
		public TerminalNode V() { return getToken(ECLParser.V, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public PrimitivetokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitivetoken; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterPrimitivetoken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitPrimitivetoken(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitPrimitivetoken(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimitivetokenContext primitivetoken() throws RecognitionException {
		PrimitivetokenContext _localctx = new PrimitivetokenContext(_ctx, getState());
		enterRule(_localctx, 210, RULE_primitivetoken);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1544);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,202,_ctx) ) {
			case 1:
				{
				setState(1542);
				_la = _input.LA(1);
				if ( !(_la==CAP_P || _la==P) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1543);
				_la = _input.LA(1);
				if ( !(_la==CAP_P || _la==P) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1548);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,203,_ctx) ) {
			case 1:
				{
				setState(1546);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1547);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1552);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,204,_ctx) ) {
			case 1:
				{
				setState(1550);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1551);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1556);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,205,_ctx) ) {
			case 1:
				{
				setState(1554);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1555);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1560);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,206,_ctx) ) {
			case 1:
				{
				setState(1558);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1559);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1564);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,207,_ctx) ) {
			case 1:
				{
				setState(1562);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1563);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1568);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,208,_ctx) ) {
			case 1:
				{
				setState(1566);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1567);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1572);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,209,_ctx) ) {
			case 1:
				{
				setState(1570);
				_la = _input.LA(1);
				if ( !(_la==CAP_V || _la==V) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1571);
				_la = _input.LA(1);
				if ( !(_la==CAP_V || _la==V) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1576);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,210,_ctx) ) {
			case 1:
				{
				setState(1574);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1575);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class DefinedtokenContext extends ParserRuleContext {
		public List<TerminalNode> CAP_D() { return getTokens(ECLParser.CAP_D); }
		public TerminalNode CAP_D(int i) {
			return getToken(ECLParser.CAP_D, i);
		}
		public List<TerminalNode> D() { return getTokens(ECLParser.D); }
		public TerminalNode D(int i) {
			return getToken(ECLParser.D, i);
		}
		public List<TerminalNode> CAP_E() { return getTokens(ECLParser.CAP_E); }
		public TerminalNode CAP_E(int i) {
			return getToken(ECLParser.CAP_E, i);
		}
		public List<TerminalNode> E() { return getTokens(ECLParser.E); }
		public TerminalNode E(int i) {
			return getToken(ECLParser.E, i);
		}
		public TerminalNode CAP_F() { return getToken(ECLParser.CAP_F, 0); }
		public TerminalNode F() { return getToken(ECLParser.F, 0); }
		public TerminalNode CAP_I() { return getToken(ECLParser.CAP_I, 0); }
		public TerminalNode I() { return getToken(ECLParser.I, 0); }
		public TerminalNode CAP_N() { return getToken(ECLParser.CAP_N, 0); }
		public TerminalNode N() { return getToken(ECLParser.N, 0); }
		public DefinedtokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definedtoken; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDefinedtoken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDefinedtoken(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDefinedtoken(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefinedtokenContext definedtoken() throws RecognitionException {
		DefinedtokenContext _localctx = new DefinedtokenContext(_ctx, getState());
		enterRule(_localctx, 212, RULE_definedtoken);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1580);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,211,_ctx) ) {
			case 1:
				{
				setState(1578);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1579);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1584);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,212,_ctx) ) {
			case 1:
				{
				setState(1582);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1583);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1588);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,213,_ctx) ) {
			case 1:
				{
				setState(1586);
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
				break;
			case 2:
				{
				setState(1587);
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
				break;
			}
			setState(1592);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,214,_ctx) ) {
			case 1:
				{
				setState(1590);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1591);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1596);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,215,_ctx) ) {
			case 1:
				{
				setState(1594);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1595);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1600);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,216,_ctx) ) {
			case 1:
				{
				setState(1598);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1599);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1604);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,217,_ctx) ) {
			case 1:
				{
				setState(1602);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1603);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class ModulefilterContext extends ParserRuleContext {
		public ModuleidkeywordContext moduleidkeyword() {
			return getRuleContext(ModuleidkeywordContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public BooleancomparisonoperatorContext booleancomparisonoperator() {
			return getRuleContext(BooleancomparisonoperatorContext.class,0);
		}
		public SubexpressionconstraintContext subexpressionconstraint() {
			return getRuleContext(SubexpressionconstraintContext.class,0);
		}
		public EclconceptreferencesetContext eclconceptreferenceset() {
			return getRuleContext(EclconceptreferencesetContext.class,0);
		}
		public ModulefilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modulefilter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterModulefilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitModulefilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitModulefilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModulefilterContext modulefilter() throws RecognitionException {
		ModulefilterContext _localctx = new ModulefilterContext(_ctx, getState());
		enterRule(_localctx, 214, RULE_modulefilter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1606);
			moduleidkeyword();
			setState(1607);
			ws();
			setState(1608);
			booleancomparisonoperator();
			setState(1609);
			ws();
			setState(1612);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,218,_ctx) ) {
			case 1:
				{
				setState(1610);
				subexpressionconstraint();
				}
				break;
			case 2:
				{
				setState(1611);
				eclconceptreferenceset();
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
	public static class ModuleidkeywordContext extends ParserRuleContext {
		public TerminalNode CAP_M() { return getToken(ECLParser.CAP_M, 0); }
		public TerminalNode M() { return getToken(ECLParser.M, 0); }
		public TerminalNode CAP_O() { return getToken(ECLParser.CAP_O, 0); }
		public TerminalNode O() { return getToken(ECLParser.O, 0); }
		public List<TerminalNode> CAP_D() { return getTokens(ECLParser.CAP_D); }
		public TerminalNode CAP_D(int i) {
			return getToken(ECLParser.CAP_D, i);
		}
		public List<TerminalNode> D() { return getTokens(ECLParser.D); }
		public TerminalNode D(int i) {
			return getToken(ECLParser.D, i);
		}
		public TerminalNode CAP_U() { return getToken(ECLParser.CAP_U, 0); }
		public TerminalNode U() { return getToken(ECLParser.U, 0); }
		public TerminalNode CAP_L() { return getToken(ECLParser.CAP_L, 0); }
		public TerminalNode L() { return getToken(ECLParser.L, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public TerminalNode CAP_I() { return getToken(ECLParser.CAP_I, 0); }
		public TerminalNode I() { return getToken(ECLParser.I, 0); }
		public ModuleidkeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleidkeyword; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterModuleidkeyword(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitModuleidkeyword(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitModuleidkeyword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleidkeywordContext moduleidkeyword() throws RecognitionException {
		ModuleidkeywordContext _localctx = new ModuleidkeywordContext(_ctx, getState());
		enterRule(_localctx, 216, RULE_moduleidkeyword);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1616);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,219,_ctx) ) {
			case 1:
				{
				setState(1614);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1615);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1620);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,220,_ctx) ) {
			case 1:
				{
				setState(1618);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1619);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1624);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,221,_ctx) ) {
			case 1:
				{
				setState(1622);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1623);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1628);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,222,_ctx) ) {
			case 1:
				{
				setState(1626);
				_la = _input.LA(1);
				if ( !(_la==CAP_U || _la==U) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1627);
				_la = _input.LA(1);
				if ( !(_la==CAP_U || _la==U) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1632);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,223,_ctx) ) {
			case 1:
				{
				setState(1630);
				_la = _input.LA(1);
				if ( !(_la==CAP_L || _la==L) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1631);
				_la = _input.LA(1);
				if ( !(_la==CAP_L || _la==L) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1636);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,224,_ctx) ) {
			case 1:
				{
				setState(1634);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1635);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1640);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,225,_ctx) ) {
			case 1:
				{
				setState(1638);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1639);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1644);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,226,_ctx) ) {
			case 1:
				{
				setState(1642);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1643);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class EffectivetimefilterContext extends ParserRuleContext {
		public EffectivetimekeywordContext effectivetimekeyword() {
			return getRuleContext(EffectivetimekeywordContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public TimecomparisonoperatorContext timecomparisonoperator() {
			return getRuleContext(TimecomparisonoperatorContext.class,0);
		}
		public TimevalueContext timevalue() {
			return getRuleContext(TimevalueContext.class,0);
		}
		public TimevaluesetContext timevalueset() {
			return getRuleContext(TimevaluesetContext.class,0);
		}
		public EffectivetimefilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_effectivetimefilter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterEffectivetimefilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitEffectivetimefilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitEffectivetimefilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EffectivetimefilterContext effectivetimefilter() throws RecognitionException {
		EffectivetimefilterContext _localctx = new EffectivetimefilterContext(_ctx, getState());
		enterRule(_localctx, 218, RULE_effectivetimefilter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1646);
			effectivetimekeyword();
			setState(1647);
			ws();
			setState(1648);
			timecomparisonoperator();
			setState(1649);
			ws();
			setState(1652);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case QUOTE:
				{
				setState(1650);
				timevalue();
				}
				break;
			case LEFT_PAREN:
				{
				setState(1651);
				timevalueset();
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
	public static class EffectivetimekeywordContext extends ParserRuleContext {
		public List<TerminalNode> CAP_E() { return getTokens(ECLParser.CAP_E); }
		public TerminalNode CAP_E(int i) {
			return getToken(ECLParser.CAP_E, i);
		}
		public List<TerminalNode> E() { return getTokens(ECLParser.E); }
		public TerminalNode E(int i) {
			return getToken(ECLParser.E, i);
		}
		public List<TerminalNode> CAP_F() { return getTokens(ECLParser.CAP_F); }
		public TerminalNode CAP_F(int i) {
			return getToken(ECLParser.CAP_F, i);
		}
		public List<TerminalNode> F() { return getTokens(ECLParser.F); }
		public TerminalNode F(int i) {
			return getToken(ECLParser.F, i);
		}
		public TerminalNode CAP_C() { return getToken(ECLParser.CAP_C, 0); }
		public TerminalNode C() { return getToken(ECLParser.C, 0); }
		public List<TerminalNode> CAP_T() { return getTokens(ECLParser.CAP_T); }
		public TerminalNode CAP_T(int i) {
			return getToken(ECLParser.CAP_T, i);
		}
		public List<TerminalNode> T() { return getTokens(ECLParser.T); }
		public TerminalNode T(int i) {
			return getToken(ECLParser.T, i);
		}
		public List<TerminalNode> CAP_I() { return getTokens(ECLParser.CAP_I); }
		public TerminalNode CAP_I(int i) {
			return getToken(ECLParser.CAP_I, i);
		}
		public List<TerminalNode> I() { return getTokens(ECLParser.I); }
		public TerminalNode I(int i) {
			return getToken(ECLParser.I, i);
		}
		public TerminalNode CAP_V() { return getToken(ECLParser.CAP_V, 0); }
		public TerminalNode V() { return getToken(ECLParser.V, 0); }
		public TerminalNode CAP_M() { return getToken(ECLParser.CAP_M, 0); }
		public TerminalNode M() { return getToken(ECLParser.M, 0); }
		public EffectivetimekeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_effectivetimekeyword; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterEffectivetimekeyword(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitEffectivetimekeyword(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitEffectivetimekeyword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EffectivetimekeywordContext effectivetimekeyword() throws RecognitionException {
		EffectivetimekeywordContext _localctx = new EffectivetimekeywordContext(_ctx, getState());
		enterRule(_localctx, 220, RULE_effectivetimekeyword);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1656);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,228,_ctx) ) {
			case 1:
				{
				setState(1654);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1655);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1660);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,229,_ctx) ) {
			case 1:
				{
				setState(1658);
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
				break;
			case 2:
				{
				setState(1659);
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
				break;
			}
			setState(1664);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,230,_ctx) ) {
			case 1:
				{
				setState(1662);
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
				break;
			case 2:
				{
				setState(1663);
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
				break;
			}
			setState(1668);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,231,_ctx) ) {
			case 1:
				{
				setState(1666);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1667);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1672);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,232,_ctx) ) {
			case 1:
				{
				setState(1670);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1671);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1676);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,233,_ctx) ) {
			case 1:
				{
				setState(1674);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1675);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1680);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,234,_ctx) ) {
			case 1:
				{
				setState(1678);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1679);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1684);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,235,_ctx) ) {
			case 1:
				{
				setState(1682);
				_la = _input.LA(1);
				if ( !(_la==CAP_V || _la==V) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1683);
				_la = _input.LA(1);
				if ( !(_la==CAP_V || _la==V) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1688);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,236,_ctx) ) {
			case 1:
				{
				setState(1686);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1687);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1692);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,237,_ctx) ) {
			case 1:
				{
				setState(1690);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1691);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1696);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,238,_ctx) ) {
			case 1:
				{
				setState(1694);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1695);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1700);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,239,_ctx) ) {
			case 1:
				{
				setState(1698);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1699);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1704);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,240,_ctx) ) {
			case 1:
				{
				setState(1702);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1703);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class TimevalueContext extends ParserRuleContext {
		public List<QmContext> qm() {
			return getRuleContexts(QmContext.class);
		}
		public QmContext qm(int i) {
			return getRuleContext(QmContext.class,i);
		}
		public YearContext year() {
			return getRuleContext(YearContext.class,0);
		}
		public MonthContext month() {
			return getRuleContext(MonthContext.class,0);
		}
		public DayContext day() {
			return getRuleContext(DayContext.class,0);
		}
		public TimevalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_timevalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterTimevalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitTimevalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitTimevalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TimevalueContext timevalue() throws RecognitionException {
		TimevalueContext _localctx = new TimevalueContext(_ctx, getState());
		enterRule(_localctx, 222, RULE_timevalue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1706);
			qm();
			setState(1711);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2143289344L) != 0)) {
				{
				setState(1707);
				year();
				setState(1708);
				month();
				setState(1709);
				day();
				}
			}

			setState(1713);
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
	public static class TimevaluesetContext extends ParserRuleContext {
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<TimevalueContext> timevalue() {
			return getRuleContexts(TimevalueContext.class);
		}
		public TimevalueContext timevalue(int i) {
			return getRuleContext(TimevalueContext.class,i);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public List<MwsContext> mws() {
			return getRuleContexts(MwsContext.class);
		}
		public MwsContext mws(int i) {
			return getRuleContext(MwsContext.class,i);
		}
		public TimevaluesetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_timevalueset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterTimevalueset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitTimevalueset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitTimevalueset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TimevaluesetContext timevalueset() throws RecognitionException {
		TimevaluesetContext _localctx = new TimevaluesetContext(_ctx, getState());
		enterRule(_localctx, 224, RULE_timevalueset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1715);
			match(LEFT_PAREN);
			setState(1716);
			ws();
			setState(1717);
			timevalue();
			setState(1723);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,242,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1718);
					mws();
					setState(1719);
					timevalue();
					}
					} 
				}
				setState(1725);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,242,_ctx);
			}
			setState(1726);
			ws();
			setState(1727);
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
	public static class YearContext extends ParserRuleContext {
		public DigitnonzeroContext digitnonzero() {
			return getRuleContext(DigitnonzeroContext.class,0);
		}
		public List<DigitContext> digit() {
			return getRuleContexts(DigitContext.class);
		}
		public DigitContext digit(int i) {
			return getRuleContext(DigitContext.class,i);
		}
		public YearContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_year; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterYear(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitYear(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitYear(this);
			else return visitor.visitChildren(this);
		}
	}

	public final YearContext year() throws RecognitionException {
		YearContext _localctx = new YearContext(_ctx, getState());
		enterRule(_localctx, 226, RULE_year);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1729);
			digitnonzero();
			setState(1730);
			digit();
			setState(1731);
			digit();
			setState(1732);
			digit();
			}
		}
		catch (RecognitionException re) {
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
	public static class MonthContext extends ParserRuleContext {
		public TerminalNode ZERO() { return getToken(ECLParser.ZERO, 0); }
		public List<TerminalNode> ONE() { return getTokens(ECLParser.ONE); }
		public TerminalNode ONE(int i) {
			return getToken(ECLParser.ONE, i);
		}
		public TerminalNode TWO() { return getToken(ECLParser.TWO, 0); }
		public TerminalNode THREE() { return getToken(ECLParser.THREE, 0); }
		public TerminalNode FOUR() { return getToken(ECLParser.FOUR, 0); }
		public TerminalNode FIVE() { return getToken(ECLParser.FIVE, 0); }
		public TerminalNode SIX() { return getToken(ECLParser.SIX, 0); }
		public TerminalNode SEVEN() { return getToken(ECLParser.SEVEN, 0); }
		public TerminalNode EIGHT() { return getToken(ECLParser.EIGHT, 0); }
		public TerminalNode NINE() { return getToken(ECLParser.NINE, 0); }
		public MonthContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_month; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterMonth(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitMonth(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitMonth(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MonthContext month() throws RecognitionException {
		MonthContext _localctx = new MonthContext(_ctx, getState());
		enterRule(_localctx, 228, RULE_month);
		try {
			setState(1758);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,243,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(1734);
				match(ZERO);
				setState(1735);
				match(ONE);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(1736);
				match(ZERO);
				setState(1737);
				match(TWO);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(1738);
				match(ZERO);
				setState(1739);
				match(THREE);
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				{
				setState(1740);
				match(ZERO);
				setState(1741);
				match(FOUR);
				}
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				{
				setState(1742);
				match(ZERO);
				setState(1743);
				match(FIVE);
				}
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				{
				setState(1744);
				match(ZERO);
				setState(1745);
				match(SIX);
				}
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				{
				setState(1746);
				match(ZERO);
				setState(1747);
				match(SEVEN);
				}
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				{
				setState(1748);
				match(ZERO);
				setState(1749);
				match(EIGHT);
				}
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				{
				setState(1750);
				match(ZERO);
				setState(1751);
				match(NINE);
				}
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				{
				setState(1752);
				match(ONE);
				setState(1753);
				match(ZERO);
				}
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				{
				setState(1754);
				match(ONE);
				setState(1755);
				match(ONE);
				}
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				{
				setState(1756);
				match(ONE);
				setState(1757);
				match(TWO);
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
	public static class DayContext extends ParserRuleContext {
		public TerminalNode ZERO() { return getToken(ECLParser.ZERO, 0); }
		public List<TerminalNode> ONE() { return getTokens(ECLParser.ONE); }
		public TerminalNode ONE(int i) {
			return getToken(ECLParser.ONE, i);
		}
		public List<TerminalNode> TWO() { return getTokens(ECLParser.TWO); }
		public TerminalNode TWO(int i) {
			return getToken(ECLParser.TWO, i);
		}
		public TerminalNode THREE() { return getToken(ECLParser.THREE, 0); }
		public TerminalNode FOUR() { return getToken(ECLParser.FOUR, 0); }
		public TerminalNode FIVE() { return getToken(ECLParser.FIVE, 0); }
		public TerminalNode SIX() { return getToken(ECLParser.SIX, 0); }
		public TerminalNode SEVEN() { return getToken(ECLParser.SEVEN, 0); }
		public TerminalNode EIGHT() { return getToken(ECLParser.EIGHT, 0); }
		public TerminalNode NINE() { return getToken(ECLParser.NINE, 0); }
		public DayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_day; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDay(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDay(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDay(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DayContext day() throws RecognitionException {
		DayContext _localctx = new DayContext(_ctx, getState());
		enterRule(_localctx, 230, RULE_day);
		try {
			setState(1822);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,244,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(1760);
				match(ZERO);
				setState(1761);
				match(ONE);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(1762);
				match(ZERO);
				setState(1763);
				match(TWO);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(1764);
				match(ZERO);
				setState(1765);
				match(THREE);
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				{
				setState(1766);
				match(ZERO);
				setState(1767);
				match(FOUR);
				}
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				{
				setState(1768);
				match(ZERO);
				setState(1769);
				match(FIVE);
				}
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				{
				setState(1770);
				match(ZERO);
				setState(1771);
				match(SIX);
				}
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				{
				setState(1772);
				match(ZERO);
				setState(1773);
				match(SEVEN);
				}
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				{
				setState(1774);
				match(ZERO);
				setState(1775);
				match(EIGHT);
				}
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				{
				setState(1776);
				match(ZERO);
				setState(1777);
				match(NINE);
				}
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				{
				setState(1778);
				match(ONE);
				setState(1779);
				match(ZERO);
				}
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				{
				setState(1780);
				match(ONE);
				setState(1781);
				match(ONE);
				}
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				{
				setState(1782);
				match(ONE);
				setState(1783);
				match(TWO);
				}
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				{
				setState(1784);
				match(ONE);
				setState(1785);
				match(THREE);
				}
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				{
				setState(1786);
				match(ONE);
				setState(1787);
				match(FOUR);
				}
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				{
				setState(1788);
				match(ONE);
				setState(1789);
				match(FIVE);
				}
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				{
				setState(1790);
				match(ONE);
				setState(1791);
				match(SIX);
				}
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				{
				setState(1792);
				match(ONE);
				setState(1793);
				match(SEVEN);
				}
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				{
				setState(1794);
				match(ONE);
				setState(1795);
				match(EIGHT);
				}
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				{
				setState(1796);
				match(ONE);
				setState(1797);
				match(NINE);
				}
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 20);
				{
				{
				setState(1798);
				match(TWO);
				setState(1799);
				match(ZERO);
				}
				}
				break;
			case 21:
				enterOuterAlt(_localctx, 21);
				{
				{
				setState(1800);
				match(TWO);
				setState(1801);
				match(ONE);
				}
				}
				break;
			case 22:
				enterOuterAlt(_localctx, 22);
				{
				{
				setState(1802);
				match(TWO);
				setState(1803);
				match(TWO);
				}
				}
				break;
			case 23:
				enterOuterAlt(_localctx, 23);
				{
				{
				setState(1804);
				match(TWO);
				setState(1805);
				match(THREE);
				}
				}
				break;
			case 24:
				enterOuterAlt(_localctx, 24);
				{
				{
				setState(1806);
				match(TWO);
				setState(1807);
				match(FOUR);
				}
				}
				break;
			case 25:
				enterOuterAlt(_localctx, 25);
				{
				{
				setState(1808);
				match(TWO);
				setState(1809);
				match(FIVE);
				}
				}
				break;
			case 26:
				enterOuterAlt(_localctx, 26);
				{
				{
				setState(1810);
				match(TWO);
				setState(1811);
				match(SIX);
				}
				}
				break;
			case 27:
				enterOuterAlt(_localctx, 27);
				{
				{
				setState(1812);
				match(TWO);
				setState(1813);
				match(SEVEN);
				}
				}
				break;
			case 28:
				enterOuterAlt(_localctx, 28);
				{
				{
				setState(1814);
				match(TWO);
				setState(1815);
				match(EIGHT);
				}
				}
				break;
			case 29:
				enterOuterAlt(_localctx, 29);
				{
				{
				setState(1816);
				match(TWO);
				setState(1817);
				match(NINE);
				}
				}
				break;
			case 30:
				enterOuterAlt(_localctx, 30);
				{
				{
				setState(1818);
				match(THREE);
				setState(1819);
				match(ZERO);
				}
				}
				break;
			case 31:
				enterOuterAlt(_localctx, 31);
				{
				{
				setState(1820);
				match(THREE);
				setState(1821);
				match(ONE);
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
	public static class ActivefilterContext extends ParserRuleContext {
		public ActivekeywordContext activekeyword() {
			return getRuleContext(ActivekeywordContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public BooleancomparisonoperatorContext booleancomparisonoperator() {
			return getRuleContext(BooleancomparisonoperatorContext.class,0);
		}
		public ActivevalueContext activevalue() {
			return getRuleContext(ActivevalueContext.class,0);
		}
		public ActivefilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_activefilter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterActivefilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitActivefilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitActivefilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActivefilterContext activefilter() throws RecognitionException {
		ActivefilterContext _localctx = new ActivefilterContext(_ctx, getState());
		enterRule(_localctx, 232, RULE_activefilter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1824);
			activekeyword();
			setState(1825);
			ws();
			setState(1826);
			booleancomparisonoperator();
			setState(1827);
			ws();
			setState(1828);
			activevalue();
			}
		}
		catch (RecognitionException re) {
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
	public static class ActivekeywordContext extends ParserRuleContext {
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode CAP_C() { return getToken(ECLParser.CAP_C, 0); }
		public TerminalNode C() { return getToken(ECLParser.C, 0); }
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode CAP_I() { return getToken(ECLParser.CAP_I, 0); }
		public TerminalNode I() { return getToken(ECLParser.I, 0); }
		public TerminalNode CAP_V() { return getToken(ECLParser.CAP_V, 0); }
		public TerminalNode V() { return getToken(ECLParser.V, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public ActivekeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_activekeyword; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterActivekeyword(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitActivekeyword(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitActivekeyword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActivekeywordContext activekeyword() throws RecognitionException {
		ActivekeywordContext _localctx = new ActivekeywordContext(_ctx, getState());
		enterRule(_localctx, 234, RULE_activekeyword);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1832);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,245,_ctx) ) {
			case 1:
				{
				setState(1830);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1831);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1836);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,246,_ctx) ) {
			case 1:
				{
				setState(1834);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1835);
				_la = _input.LA(1);
				if ( !(_la==CAP_C || _la==C) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1840);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,247,_ctx) ) {
			case 1:
				{
				setState(1838);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1839);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1844);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,248,_ctx) ) {
			case 1:
				{
				setState(1842);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1843);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1848);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,249,_ctx) ) {
			case 1:
				{
				setState(1846);
				_la = _input.LA(1);
				if ( !(_la==CAP_V || _la==V) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1847);
				_la = _input.LA(1);
				if ( !(_la==CAP_V || _la==V) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1852);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,250,_ctx) ) {
			case 1:
				{
				setState(1850);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1851);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class ActivevalueContext extends ParserRuleContext {
		public ActivetruevalueContext activetruevalue() {
			return getRuleContext(ActivetruevalueContext.class,0);
		}
		public ActivefalsevalueContext activefalsevalue() {
			return getRuleContext(ActivefalsevalueContext.class,0);
		}
		public ActivevalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_activevalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterActivevalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitActivevalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitActivevalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActivevalueContext activevalue() throws RecognitionException {
		ActivevalueContext _localctx = new ActivevalueContext(_ctx, getState());
		enterRule(_localctx, 236, RULE_activevalue);
		try {
			setState(1856);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ONE:
			case CAP_T:
			case T:
				enterOuterAlt(_localctx, 1);
				{
				setState(1854);
				activetruevalue();
				}
				break;
			case ZERO:
			case CAP_F:
			case F:
				enterOuterAlt(_localctx, 2);
				{
				setState(1855);
				activefalsevalue();
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
	public static class ActivetruevalueContext extends ParserRuleContext {
		public TerminalNode ONE() { return getToken(ECLParser.ONE, 0); }
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode CAP_R() { return getToken(ECLParser.CAP_R, 0); }
		public TerminalNode R() { return getToken(ECLParser.R, 0); }
		public TerminalNode CAP_U() { return getToken(ECLParser.CAP_U, 0); }
		public TerminalNode U() { return getToken(ECLParser.U, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public ActivetruevalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_activetruevalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterActivetruevalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitActivetruevalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitActivetruevalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActivetruevalueContext activetruevalue() throws RecognitionException {
		ActivetruevalueContext _localctx = new ActivetruevalueContext(_ctx, getState());
		enterRule(_localctx, 238, RULE_activetruevalue);
		int _la;
		try {
			setState(1863);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ONE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1858);
				match(ONE);
				}
				break;
			case CAP_T:
			case T:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(1859);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1860);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1861);
				_la = _input.LA(1);
				if ( !(_la==CAP_U || _la==U) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1862);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
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
	public static class ActivefalsevalueContext extends ParserRuleContext {
		public TerminalNode ZERO() { return getToken(ECLParser.ZERO, 0); }
		public TerminalNode CAP_F() { return getToken(ECLParser.CAP_F, 0); }
		public TerminalNode F() { return getToken(ECLParser.F, 0); }
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode CAP_L() { return getToken(ECLParser.CAP_L, 0); }
		public TerminalNode L() { return getToken(ECLParser.L, 0); }
		public TerminalNode CAP_S() { return getToken(ECLParser.CAP_S, 0); }
		public TerminalNode S() { return getToken(ECLParser.S, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public ActivefalsevalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_activefalsevalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterActivefalsevalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitActivefalsevalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitActivefalsevalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActivefalsevalueContext activefalsevalue() throws RecognitionException {
		ActivefalsevalueContext _localctx = new ActivefalsevalueContext(_ctx, getState());
		enterRule(_localctx, 240, RULE_activefalsevalue);
		int _la;
		try {
			setState(1871);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ZERO:
				enterOuterAlt(_localctx, 1);
				{
				setState(1865);
				match(ZERO);
				}
				break;
			case CAP_F:
			case F:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(1866);
				_la = _input.LA(1);
				if ( !(_la==CAP_F || _la==F) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1867);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1868);
				_la = _input.LA(1);
				if ( !(_la==CAP_L || _la==L) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1869);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1870);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
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
	public static class MemberfilterconstraintContext extends ParserRuleContext {
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<MemberfilterContext> memberfilter() {
			return getRuleContexts(MemberfilterContext.class);
		}
		public MemberfilterContext memberfilter(int i) {
			return getRuleContext(MemberfilterContext.class,i);
		}
		public List<TerminalNode> LEFT_CURLY_BRACE() { return getTokens(ECLParser.LEFT_CURLY_BRACE); }
		public TerminalNode LEFT_CURLY_BRACE(int i) {
			return getToken(ECLParser.LEFT_CURLY_BRACE, i);
		}
		public List<TerminalNode> RIGHT_CURLY_BRACE() { return getTokens(ECLParser.RIGHT_CURLY_BRACE); }
		public TerminalNode RIGHT_CURLY_BRACE(int i) {
			return getToken(ECLParser.RIGHT_CURLY_BRACE, i);
		}
		public TerminalNode CAP_M() { return getToken(ECLParser.CAP_M, 0); }
		public TerminalNode M() { return getToken(ECLParser.M, 0); }
		public List<TerminalNode> COMMA() { return getTokens(ECLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ECLParser.COMMA, i);
		}
		public MemberfilterconstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberfilterconstraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterMemberfilterconstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitMemberfilterconstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitMemberfilterconstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemberfilterconstraintContext memberfilterconstraint() throws RecognitionException {
		MemberfilterconstraintContext _localctx = new MemberfilterconstraintContext(_ctx, getState());
		enterRule(_localctx, 242, RULE_memberfilterconstraint);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(1873);
			match(LEFT_CURLY_BRACE);
			setState(1874);
			match(LEFT_CURLY_BRACE);
			}
			setState(1876);
			ws();
			setState(1879);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,254,_ctx) ) {
			case 1:
				{
				setState(1877);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1878);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1881);
			ws();
			setState(1882);
			memberfilter();
			setState(1890);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,255,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1883);
					ws();
					setState(1884);
					match(COMMA);
					setState(1885);
					ws();
					setState(1886);
					memberfilter();
					}
					} 
				}
				setState(1892);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,255,_ctx);
			}
			setState(1893);
			ws();
			{
			setState(1894);
			match(RIGHT_CURLY_BRACE);
			setState(1895);
			match(RIGHT_CURLY_BRACE);
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
	public static class MemberfilterContext extends ParserRuleContext {
		public ModulefilterContext modulefilter() {
			return getRuleContext(ModulefilterContext.class,0);
		}
		public EffectivetimefilterContext effectivetimefilter() {
			return getRuleContext(EffectivetimefilterContext.class,0);
		}
		public ActivefilterContext activefilter() {
			return getRuleContext(ActivefilterContext.class,0);
		}
		public MemberfieldfilterContext memberfieldfilter() {
			return getRuleContext(MemberfieldfilterContext.class,0);
		}
		public MemberfilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberfilter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterMemberfilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitMemberfilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitMemberfilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemberfilterContext memberfilter() throws RecognitionException {
		MemberfilterContext _localctx = new MemberfilterContext(_ctx, getState());
		enterRule(_localctx, 244, RULE_memberfilter);
		try {
			setState(1901);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,256,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1897);
				modulefilter();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1898);
				effectivetimefilter();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1899);
				activefilter();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1900);
				memberfieldfilter();
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
	public static class MemberfieldfilterContext extends ParserRuleContext {
		public RefsetfieldnameContext refsetfieldname() {
			return getRuleContext(RefsetfieldnameContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
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
		public TerminalNode HASH() { return getToken(ECLParser.HASH, 0); }
		public NumericvalueContext numericvalue() {
			return getRuleContext(NumericvalueContext.class,0);
		}
		public StringcomparisonoperatorContext stringcomparisonoperator() {
			return getRuleContext(StringcomparisonoperatorContext.class,0);
		}
		public BooleancomparisonoperatorContext booleancomparisonoperator() {
			return getRuleContext(BooleancomparisonoperatorContext.class,0);
		}
		public BooleanvalueContext booleanvalue() {
			return getRuleContext(BooleanvalueContext.class,0);
		}
		public TimecomparisonoperatorContext timecomparisonoperator() {
			return getRuleContext(TimecomparisonoperatorContext.class,0);
		}
		public TypedsearchtermContext typedsearchterm() {
			return getRuleContext(TypedsearchtermContext.class,0);
		}
		public TypedsearchtermsetContext typedsearchtermset() {
			return getRuleContext(TypedsearchtermsetContext.class,0);
		}
		public TimevalueContext timevalue() {
			return getRuleContext(TimevalueContext.class,0);
		}
		public TimevaluesetContext timevalueset() {
			return getRuleContext(TimevaluesetContext.class,0);
		}
		public MemberfieldfilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberfieldfilter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterMemberfieldfilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitMemberfieldfilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitMemberfieldfilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemberfieldfilterContext memberfieldfilter() throws RecognitionException {
		MemberfieldfilterContext _localctx = new MemberfieldfilterContext(_ctx, getState());
		enterRule(_localctx, 246, RULE_memberfieldfilter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1903);
			refsetfieldname();
			setState(1904);
			ws();
			setState(1931);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,259,_ctx) ) {
			case 1:
				{
				{
				setState(1905);
				expressioncomparisonoperator();
				setState(1906);
				ws();
				setState(1907);
				subexpressionconstraint();
				}
				}
				break;
			case 2:
				{
				{
				setState(1909);
				numericcomparisonoperator();
				setState(1910);
				ws();
				setState(1911);
				match(HASH);
				setState(1912);
				numericvalue();
				}
				}
				break;
			case 3:
				{
				{
				setState(1914);
				stringcomparisonoperator();
				setState(1915);
				ws();
				setState(1918);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case QUOTE:
				case CAP_M:
				case CAP_W:
				case M:
				case W:
					{
					setState(1916);
					typedsearchterm();
					}
					break;
				case LEFT_PAREN:
					{
					setState(1917);
					typedsearchtermset();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				break;
			case 4:
				{
				{
				setState(1920);
				booleancomparisonoperator();
				setState(1921);
				ws();
				setState(1922);
				booleanvalue();
				}
				}
				break;
			case 5:
				{
				{
				setState(1924);
				ws();
				setState(1925);
				timecomparisonoperator();
				setState(1926);
				ws();
				setState(1929);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case QUOTE:
					{
					setState(1927);
					timevalue();
					}
					break;
				case LEFT_PAREN:
					{
					setState(1928);
					timevalueset();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
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
	public static class HistorysupplementContext extends ParserRuleContext {
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(ECLParser.PLUS, 0); }
		public HistorykeywordContext historykeyword() {
			return getRuleContext(HistorykeywordContext.class,0);
		}
		public List<TerminalNode> LEFT_CURLY_BRACE() { return getTokens(ECLParser.LEFT_CURLY_BRACE); }
		public TerminalNode LEFT_CURLY_BRACE(int i) {
			return getToken(ECLParser.LEFT_CURLY_BRACE, i);
		}
		public List<TerminalNode> RIGHT_CURLY_BRACE() { return getTokens(ECLParser.RIGHT_CURLY_BRACE); }
		public TerminalNode RIGHT_CURLY_BRACE(int i) {
			return getToken(ECLParser.RIGHT_CURLY_BRACE, i);
		}
		public HistoryprofilesuffixContext historyprofilesuffix() {
			return getRuleContext(HistoryprofilesuffixContext.class,0);
		}
		public HistorysubsetContext historysubset() {
			return getRuleContext(HistorysubsetContext.class,0);
		}
		public HistorysupplementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_historysupplement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterHistorysupplement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitHistorysupplement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitHistorysupplement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HistorysupplementContext historysupplement() throws RecognitionException {
		HistorysupplementContext _localctx = new HistorysupplementContext(_ctx, getState());
		enterRule(_localctx, 248, RULE_historysupplement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(1933);
			match(LEFT_CURLY_BRACE);
			setState(1934);
			match(LEFT_CURLY_BRACE);
			}
			setState(1936);
			ws();
			setState(1937);
			match(PLUS);
			setState(1938);
			ws();
			setState(1939);
			historykeyword();
			setState(1944);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,260,_ctx) ) {
			case 1:
				{
				setState(1940);
				historyprofilesuffix();
				}
				break;
			case 2:
				{
				{
				setState(1941);
				ws();
				setState(1942);
				historysubset();
				}
				}
				break;
			}
			setState(1946);
			ws();
			{
			setState(1947);
			match(RIGHT_CURLY_BRACE);
			setState(1948);
			match(RIGHT_CURLY_BRACE);
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
	public static class HistorykeywordContext extends ParserRuleContext {
		public TerminalNode CAP_H() { return getToken(ECLParser.CAP_H, 0); }
		public TerminalNode H() { return getToken(ECLParser.H, 0); }
		public TerminalNode CAP_I() { return getToken(ECLParser.CAP_I, 0); }
		public TerminalNode I() { return getToken(ECLParser.I, 0); }
		public TerminalNode CAP_S() { return getToken(ECLParser.CAP_S, 0); }
		public TerminalNode S() { return getToken(ECLParser.S, 0); }
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode CAP_O() { return getToken(ECLParser.CAP_O, 0); }
		public TerminalNode O() { return getToken(ECLParser.O, 0); }
		public TerminalNode CAP_R() { return getToken(ECLParser.CAP_R, 0); }
		public TerminalNode R() { return getToken(ECLParser.R, 0); }
		public TerminalNode CAP_Y() { return getToken(ECLParser.CAP_Y, 0); }
		public TerminalNode Y() { return getToken(ECLParser.Y, 0); }
		public HistorykeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_historykeyword; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterHistorykeyword(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitHistorykeyword(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitHistorykeyword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HistorykeywordContext historykeyword() throws RecognitionException {
		HistorykeywordContext _localctx = new HistorykeywordContext(_ctx, getState());
		enterRule(_localctx, 250, RULE_historykeyword);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1952);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,261,_ctx) ) {
			case 1:
				{
				setState(1950);
				_la = _input.LA(1);
				if ( !(_la==CAP_H || _la==H) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1951);
				_la = _input.LA(1);
				if ( !(_la==CAP_H || _la==H) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1956);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,262,_ctx) ) {
			case 1:
				{
				setState(1954);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1955);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1960);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,263,_ctx) ) {
			case 1:
				{
				setState(1958);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1959);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1964);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,264,_ctx) ) {
			case 1:
				{
				setState(1962);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1963);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1968);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,265,_ctx) ) {
			case 1:
				{
				setState(1966);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1967);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1972);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,266,_ctx) ) {
			case 1:
				{
				setState(1970);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1971);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1976);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,267,_ctx) ) {
			case 1:
				{
				setState(1974);
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
				break;
			case 2:
				{
				setState(1975);
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
	public static class HistoryprofilesuffixContext extends ParserRuleContext {
		public HistoryminimumsuffixContext historyminimumsuffix() {
			return getRuleContext(HistoryminimumsuffixContext.class,0);
		}
		public HistorymoderatesuffixContext historymoderatesuffix() {
			return getRuleContext(HistorymoderatesuffixContext.class,0);
		}
		public HistorymaximumsuffixContext historymaximumsuffix() {
			return getRuleContext(HistorymaximumsuffixContext.class,0);
		}
		public HistoryprofilesuffixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_historyprofilesuffix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterHistoryprofilesuffix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitHistoryprofilesuffix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitHistoryprofilesuffix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HistoryprofilesuffixContext historyprofilesuffix() throws RecognitionException {
		HistoryprofilesuffixContext _localctx = new HistoryprofilesuffixContext(_ctx, getState());
		enterRule(_localctx, 252, RULE_historyprofilesuffix);
		try {
			setState(1981);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,268,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1978);
				historyminimumsuffix();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1979);
				historymoderatesuffix();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1980);
				historymaximumsuffix();
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
	public static class HistoryminimumsuffixContext extends ParserRuleContext {
		public TerminalNode DASH() { return getToken(ECLParser.DASH, 0); }
		public TerminalNode UNDERSCORE() { return getToken(ECLParser.UNDERSCORE, 0); }
		public TerminalNode CAP_M() { return getToken(ECLParser.CAP_M, 0); }
		public TerminalNode M() { return getToken(ECLParser.M, 0); }
		public TerminalNode CAP_I() { return getToken(ECLParser.CAP_I, 0); }
		public TerminalNode I() { return getToken(ECLParser.I, 0); }
		public TerminalNode CAP_N() { return getToken(ECLParser.CAP_N, 0); }
		public TerminalNode N() { return getToken(ECLParser.N, 0); }
		public HistoryminimumsuffixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_historyminimumsuffix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterHistoryminimumsuffix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitHistoryminimumsuffix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitHistoryminimumsuffix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HistoryminimumsuffixContext historyminimumsuffix() throws RecognitionException {
		HistoryminimumsuffixContext _localctx = new HistoryminimumsuffixContext(_ctx, getState());
		enterRule(_localctx, 254, RULE_historyminimumsuffix);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1983);
			_la = _input.LA(1);
			if ( !(_la==DASH || _la==UNDERSCORE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1986);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,269,_ctx) ) {
			case 1:
				{
				setState(1984);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1985);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1990);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,270,_ctx) ) {
			case 1:
				{
				setState(1988);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1989);
				_la = _input.LA(1);
				if ( !(_la==CAP_I || _la==I) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(1994);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,271,_ctx) ) {
			case 1:
				{
				setState(1992);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1993);
				_la = _input.LA(1);
				if ( !(_la==CAP_N || _la==N) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class HistorymoderatesuffixContext extends ParserRuleContext {
		public TerminalNode DASH() { return getToken(ECLParser.DASH, 0); }
		public TerminalNode UNDERSCORE() { return getToken(ECLParser.UNDERSCORE, 0); }
		public TerminalNode CAP_M() { return getToken(ECLParser.CAP_M, 0); }
		public TerminalNode M() { return getToken(ECLParser.M, 0); }
		public TerminalNode CAP_O() { return getToken(ECLParser.CAP_O, 0); }
		public TerminalNode O() { return getToken(ECLParser.O, 0); }
		public TerminalNode CAP_D() { return getToken(ECLParser.CAP_D, 0); }
		public TerminalNode D() { return getToken(ECLParser.D, 0); }
		public HistorymoderatesuffixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_historymoderatesuffix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterHistorymoderatesuffix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitHistorymoderatesuffix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitHistorymoderatesuffix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HistorymoderatesuffixContext historymoderatesuffix() throws RecognitionException {
		HistorymoderatesuffixContext _localctx = new HistorymoderatesuffixContext(_ctx, getState());
		enterRule(_localctx, 256, RULE_historymoderatesuffix);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1996);
			_la = _input.LA(1);
			if ( !(_la==DASH || _la==UNDERSCORE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1999);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,272,_ctx) ) {
			case 1:
				{
				setState(1997);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(1998);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(2003);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,273,_ctx) ) {
			case 1:
				{
				setState(2001);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(2002);
				_la = _input.LA(1);
				if ( !(_la==CAP_O || _la==O) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(2007);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,274,_ctx) ) {
			case 1:
				{
				setState(2005);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(2006);
				_la = _input.LA(1);
				if ( !(_la==CAP_D || _la==D) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class HistorymaximumsuffixContext extends ParserRuleContext {
		public TerminalNode DASH() { return getToken(ECLParser.DASH, 0); }
		public TerminalNode UNDERSCORE() { return getToken(ECLParser.UNDERSCORE, 0); }
		public TerminalNode CAP_M() { return getToken(ECLParser.CAP_M, 0); }
		public TerminalNode M() { return getToken(ECLParser.M, 0); }
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode CAP_X() { return getToken(ECLParser.CAP_X, 0); }
		public TerminalNode X() { return getToken(ECLParser.X, 0); }
		public HistorymaximumsuffixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_historymaximumsuffix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterHistorymaximumsuffix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitHistorymaximumsuffix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitHistorymaximumsuffix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HistorymaximumsuffixContext historymaximumsuffix() throws RecognitionException {
		HistorymaximumsuffixContext _localctx = new HistorymaximumsuffixContext(_ctx, getState());
		enterRule(_localctx, 258, RULE_historymaximumsuffix);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2009);
			_la = _input.LA(1);
			if ( !(_la==DASH || _la==UNDERSCORE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(2012);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,275,_ctx) ) {
			case 1:
				{
				setState(2010);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(2011);
				_la = _input.LA(1);
				if ( !(_la==CAP_M || _la==M) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(2016);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,276,_ctx) ) {
			case 1:
				{
				setState(2014);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(2015);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(2020);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,277,_ctx) ) {
			case 1:
				{
				setState(2018);
				_la = _input.LA(1);
				if ( !(_la==CAP_X || _la==X) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(2019);
				_la = _input.LA(1);
				if ( !(_la==CAP_X || _la==X) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class HistorysubsetContext extends ParserRuleContext {
		public TerminalNode LEFT_PAREN() { return getToken(ECLParser.LEFT_PAREN, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public ExpressionconstraintContext expressionconstraint() {
			return getRuleContext(ExpressionconstraintContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(ECLParser.RIGHT_PAREN, 0); }
		public HistorysubsetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_historysubset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterHistorysubset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitHistorysubset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitHistorysubset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HistorysubsetContext historysubset() throws RecognitionException {
		HistorysubsetContext _localctx = new HistorysubsetContext(_ctx, getState());
		enterRule(_localctx, 260, RULE_historysubset);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2022);
			match(LEFT_PAREN);
			setState(2023);
			ws();
			setState(2024);
			expressionconstraint();
			setState(2025);
			ws();
			setState(2026);
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
		enterRule(_localctx, 262, RULE_numericvalue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2029);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PLUS || _la==DASH) {
				{
				setState(2028);
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

			setState(2033);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,279,_ctx) ) {
			case 1:
				{
				setState(2031);
				decimalvalue();
				}
				break;
			case 2:
				{
				setState(2032);
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
		enterRule(_localctx, 264, RULE_stringvalue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2037); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(2037);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case UTF8_LETTER:
				case TAB:
				case LF:
				case CR:
				case SPACE:
				case EXCLAMATION:
				case HASH:
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
					setState(2035);
					anynonescapedchar();
					}
					break;
				case BACKSLASH:
					{
					setState(2036);
					escapedchar();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(2039); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & -130L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 68719476735L) != 0) );
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 266, RULE_integervalue);
		try {
			int _alt;
			setState(2049);
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
				setState(2041);
				digitnonzero();
				setState(2045);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,282,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(2042);
						digit();
						}
						} 
					}
					setState(2047);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,282,_ctx);
				}
				}
				}
				break;
			case ZERO:
				enterOuterAlt(_localctx, 2);
				{
				setState(2048);
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
		enterRule(_localctx, 268, RULE_decimalvalue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2051);
			integervalue();
			setState(2052);
			match(PERIOD);
			setState(2054); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(2053);
				digit();
				}
				}
				setState(2056); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 2145386496L) != 0) );
			}
		}
		catch (RecognitionException re) {
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
	public static class BooleanvalueContext extends ParserRuleContext {
		public True_1Context true_1() {
			return getRuleContext(True_1Context.class,0);
		}
		public False_1Context false_1() {
			return getRuleContext(False_1Context.class,0);
		}
		public BooleanvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanvalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterBooleanvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitBooleanvalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitBooleanvalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanvalueContext booleanvalue() throws RecognitionException {
		BooleanvalueContext _localctx = new BooleanvalueContext(_ctx, getState());
		enterRule(_localctx, 270, RULE_booleanvalue);
		try {
			setState(2060);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CAP_T:
			case T:
				enterOuterAlt(_localctx, 1);
				{
				setState(2058);
				true_1();
				}
				break;
			case CAP_F:
			case F:
				enterOuterAlt(_localctx, 2);
				{
				setState(2059);
				false_1();
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
	public static class True_1Context extends ParserRuleContext {
		public TerminalNode CAP_T() { return getToken(ECLParser.CAP_T, 0); }
		public TerminalNode T() { return getToken(ECLParser.T, 0); }
		public TerminalNode CAP_R() { return getToken(ECLParser.CAP_R, 0); }
		public TerminalNode R() { return getToken(ECLParser.R, 0); }
		public TerminalNode CAP_U() { return getToken(ECLParser.CAP_U, 0); }
		public TerminalNode U() { return getToken(ECLParser.U, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public True_1Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_true_1; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterTrue_1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitTrue_1(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitTrue_1(this);
			else return visitor.visitChildren(this);
		}
	}

	public final True_1Context true_1() throws RecognitionException {
		True_1Context _localctx = new True_1Context(_ctx, getState());
		enterRule(_localctx, 272, RULE_true_1);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2064);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,286,_ctx) ) {
			case 1:
				{
				setState(2062);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(2063);
				_la = _input.LA(1);
				if ( !(_la==CAP_T || _la==T) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(2068);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,287,_ctx) ) {
			case 1:
				{
				setState(2066);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(2067);
				_la = _input.LA(1);
				if ( !(_la==CAP_R || _la==R) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(2072);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,288,_ctx) ) {
			case 1:
				{
				setState(2070);
				_la = _input.LA(1);
				if ( !(_la==CAP_U || _la==U) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(2071);
				_la = _input.LA(1);
				if ( !(_la==CAP_U || _la==U) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(2076);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,289,_ctx) ) {
			case 1:
				{
				setState(2074);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(2075);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class False_1Context extends ParserRuleContext {
		public TerminalNode CAP_F() { return getToken(ECLParser.CAP_F, 0); }
		public TerminalNode F() { return getToken(ECLParser.F, 0); }
		public TerminalNode CAP_A() { return getToken(ECLParser.CAP_A, 0); }
		public TerminalNode A() { return getToken(ECLParser.A, 0); }
		public TerminalNode CAP_L() { return getToken(ECLParser.CAP_L, 0); }
		public TerminalNode L() { return getToken(ECLParser.L, 0); }
		public TerminalNode CAP_S() { return getToken(ECLParser.CAP_S, 0); }
		public TerminalNode S() { return getToken(ECLParser.S, 0); }
		public TerminalNode CAP_E() { return getToken(ECLParser.CAP_E, 0); }
		public TerminalNode E() { return getToken(ECLParser.E, 0); }
		public False_1Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_false_1; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterFalse_1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitFalse_1(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitFalse_1(this);
			else return visitor.visitChildren(this);
		}
	}

	public final False_1Context false_1() throws RecognitionException {
		False_1Context _localctx = new False_1Context(_ctx, getState());
		enterRule(_localctx, 274, RULE_false_1);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2080);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,290,_ctx) ) {
			case 1:
				{
				setState(2078);
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
				break;
			case 2:
				{
				setState(2079);
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
				break;
			}
			setState(2084);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,291,_ctx) ) {
			case 1:
				{
				setState(2082);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(2083);
				_la = _input.LA(1);
				if ( !(_la==CAP_A || _la==A) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(2088);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,292,_ctx) ) {
			case 1:
				{
				setState(2086);
				_la = _input.LA(1);
				if ( !(_la==CAP_L || _la==L) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(2087);
				_la = _input.LA(1);
				if ( !(_la==CAP_L || _la==L) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(2092);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,293,_ctx) ) {
			case 1:
				{
				setState(2090);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(2091);
				_la = _input.LA(1);
				if ( !(_la==CAP_S || _la==S) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(2096);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,294,_ctx) ) {
			case 1:
				{
				setState(2094);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				{
				setState(2095);
				_la = _input.LA(1);
				if ( !(_la==CAP_E || _la==E) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
		enterRule(_localctx, 276, RULE_nonnegativeintegervalue);
		int _la;
		try {
			setState(2106);
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
				setState(2098);
				digitnonzero();
				setState(2102);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2145386496L) != 0)) {
					{
					{
					setState(2099);
					digit();
					}
					}
					setState(2104);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				break;
			case ZERO:
				enterOuterAlt(_localctx, 2);
				{
				setState(2105);
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
		enterRule(_localctx, 278, RULE_sctid);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2108);
			digitnonzero();
			{
			{
			setState(2109);
			digit();
			}
			{
			setState(2110);
			digit();
			}
			{
			setState(2111);
			digit();
			}
			{
			setState(2112);
			digit();
			}
			{
			setState(2113);
			digit();
			}
			setState(2205);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,298,_ctx) ) {
			case 1:
				{
				{
				{
				setState(2114);
				digit();
				}
				{
				setState(2115);
				digit();
				}
				{
				setState(2116);
				digit();
				}
				{
				setState(2117);
				digit();
				}
				{
				setState(2118);
				digit();
				}
				{
				setState(2119);
				digit();
				}
				{
				setState(2120);
				digit();
				}
				{
				setState(2121);
				digit();
				}
				{
				setState(2122);
				digit();
				}
				{
				setState(2123);
				digit();
				}
				{
				setState(2124);
				digit();
				}
				{
				setState(2125);
				digit();
				}
				}
				}
				break;
			case 2:
				{
				{
				{
				setState(2127);
				digit();
				}
				{
				setState(2128);
				digit();
				}
				{
				setState(2129);
				digit();
				}
				{
				setState(2130);
				digit();
				}
				{
				setState(2131);
				digit();
				}
				{
				setState(2132);
				digit();
				}
				{
				setState(2133);
				digit();
				}
				{
				setState(2134);
				digit();
				}
				{
				setState(2135);
				digit();
				}
				{
				setState(2136);
				digit();
				}
				{
				setState(2137);
				digit();
				}
				}
				}
				break;
			case 3:
				{
				{
				{
				setState(2139);
				digit();
				}
				{
				setState(2140);
				digit();
				}
				{
				setState(2141);
				digit();
				}
				{
				setState(2142);
				digit();
				}
				{
				setState(2143);
				digit();
				}
				{
				setState(2144);
				digit();
				}
				{
				setState(2145);
				digit();
				}
				{
				setState(2146);
				digit();
				}
				{
				setState(2147);
				digit();
				}
				{
				setState(2148);
				digit();
				}
				}
				}
				break;
			case 4:
				{
				{
				{
				setState(2150);
				digit();
				}
				{
				setState(2151);
				digit();
				}
				{
				setState(2152);
				digit();
				}
				{
				setState(2153);
				digit();
				}
				{
				setState(2154);
				digit();
				}
				{
				setState(2155);
				digit();
				}
				{
				setState(2156);
				digit();
				}
				{
				setState(2157);
				digit();
				}
				{
				setState(2158);
				digit();
				}
				}
				}
				break;
			case 5:
				{
				{
				{
				setState(2160);
				digit();
				}
				{
				setState(2161);
				digit();
				}
				{
				setState(2162);
				digit();
				}
				{
				setState(2163);
				digit();
				}
				{
				setState(2164);
				digit();
				}
				{
				setState(2165);
				digit();
				}
				{
				setState(2166);
				digit();
				}
				{
				setState(2167);
				digit();
				}
				}
				}
				break;
			case 6:
				{
				{
				{
				setState(2169);
				digit();
				}
				{
				setState(2170);
				digit();
				}
				{
				setState(2171);
				digit();
				}
				{
				setState(2172);
				digit();
				}
				{
				setState(2173);
				digit();
				}
				{
				setState(2174);
				digit();
				}
				{
				setState(2175);
				digit();
				}
				}
				}
				break;
			case 7:
				{
				{
				{
				setState(2177);
				digit();
				}
				{
				setState(2178);
				digit();
				}
				{
				setState(2179);
				digit();
				}
				{
				setState(2180);
				digit();
				}
				{
				setState(2181);
				digit();
				}
				{
				setState(2182);
				digit();
				}
				}
				}
				break;
			case 8:
				{
				{
				{
				setState(2184);
				digit();
				}
				{
				setState(2185);
				digit();
				}
				{
				setState(2186);
				digit();
				}
				{
				setState(2187);
				digit();
				}
				{
				setState(2188);
				digit();
				}
				}
				}
				break;
			case 9:
				{
				{
				{
				setState(2190);
				digit();
				}
				{
				setState(2191);
				digit();
				}
				{
				setState(2192);
				digit();
				}
				{
				setState(2193);
				digit();
				}
				}
				}
				break;
			case 10:
				{
				{
				{
				setState(2195);
				digit();
				}
				{
				setState(2196);
				digit();
				}
				{
				setState(2197);
				digit();
				}
				}
				}
				break;
			case 11:
				{
				{
				{
				setState(2199);
				digit();
				}
				{
				setState(2200);
				digit();
				}
				}
				}
				break;
			case 12:
				{
				setState(2203);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2145386496L) != 0)) {
					{
					setState(2202);
					digit();
					}
				}

				}
				break;
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
		enterRule(_localctx, 280, RULE_ws);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(2214);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,300,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(2212);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case SPACE:
						{
						setState(2207);
						sp();
						}
						break;
					case TAB:
						{
						setState(2208);
						htab();
						}
						break;
					case CR:
						{
						setState(2209);
						cr();
						}
						break;
					case LF:
						{
						setState(2210);
						lf();
						}
						break;
					case SLASH:
						{
						setState(2211);
						comment();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(2216);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,300,_ctx);
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
		enterRule(_localctx, 282, RULE_mws);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(2222); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					setState(2222);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case SPACE:
						{
						setState(2217);
						sp();
						}
						break;
					case TAB:
						{
						setState(2218);
						htab();
						}
						break;
					case CR:
						{
						setState(2219);
						cr();
						}
						break;
					case LF:
						{
						setState(2220);
						lf();
						}
						break;
					case SLASH:
						{
						setState(2221);
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
				setState(2224); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,302,_ctx);
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
		enterRule(_localctx, 284, RULE_comment);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(2226);
			match(SLASH);
			setState(2227);
			match(ASTERISK);
			}
			setState(2233);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,304,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(2231);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case UTF8_LETTER:
					case TAB:
					case LF:
					case CR:
					case SPACE:
					case EXCLAMATION:
					case QUOTE:
					case HASH:
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
						setState(2229);
						nonstarchar();
						}
						break;
					case ASTERISK:
						{
						setState(2230);
						starwithnonfslash();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(2235);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,304,_ctx);
			}
			{
			setState(2236);
			match(ASTERISK);
			setState(2237);
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
		public TerminalNode HASH() { return getToken(ECLParser.HASH, 0); }
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
		enterRule(_localctx, 286, RULE_nonstarchar);
		int _la;
		try {
			setState(2246);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SPACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(2239);
				sp();
				}
				break;
			case TAB:
				enterOuterAlt(_localctx, 2);
				{
				setState(2240);
				htab();
				}
				break;
			case CR:
				enterOuterAlt(_localctx, 3);
				{
				setState(2241);
				cr();
				}
				break;
			case LF:
				enterOuterAlt(_localctx, 4);
				{
				setState(2242);
				lf();
				}
				break;
			case EXCLAMATION:
			case QUOTE:
			case HASH:
			case DOLLAR:
			case PERCENT:
			case AMPERSAND:
			case APOSTROPHE:
			case LEFT_PAREN:
			case RIGHT_PAREN:
				enterOuterAlt(_localctx, 5);
				{
				setState(2243);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 32704L) != 0)) ) {
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
				setState(2244);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & -65536L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 68719476735L) != 0)) ) {
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
				setState(2245);
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
		enterRule(_localctx, 288, RULE_starwithnonfslash);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2248);
			match(ASTERISK);
			setState(2249);
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
		public TerminalNode HASH() { return getToken(ECLParser.HASH, 0); }
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
		enterRule(_localctx, 290, RULE_nonfslash);
		int _la;
		try {
			setState(2258);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SPACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(2251);
				sp();
				}
				break;
			case TAB:
				enterOuterAlt(_localctx, 2);
				{
				setState(2252);
				htab();
				}
				break;
			case CR:
				enterOuterAlt(_localctx, 3);
				{
				setState(2253);
				cr();
				}
				break;
			case LF:
				enterOuterAlt(_localctx, 4);
				{
				setState(2254);
				lf();
				}
				break;
			case EXCLAMATION:
			case QUOTE:
			case HASH:
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
				setState(2255);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1048512L) != 0)) ) {
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
				setState(2256);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & -2097152L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 68719476735L) != 0)) ) {
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
				setState(2257);
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
		enterRule(_localctx, 292, RULE_sp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2260);
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
		enterRule(_localctx, 294, RULE_htab);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2262);
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
		enterRule(_localctx, 296, RULE_cr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2264);
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
		enterRule(_localctx, 298, RULE_lf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2266);
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
		enterRule(_localctx, 300, RULE_qm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2268);
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
		enterRule(_localctx, 302, RULE_bs);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2270);
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
	public static class StarContext extends ParserRuleContext {
		public TerminalNode ASTERISK() { return getToken(ECLParser.ASTERISK, 0); }
		public StarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_star; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterStar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitStar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitStar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StarContext star() throws RecognitionException {
		StarContext _localctx = new StarContext(_ctx, getState());
		enterRule(_localctx, 304, RULE_star);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2272);
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
		enterRule(_localctx, 306, RULE_digit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2274);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 2145386496L) != 0)) ) {
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
		enterRule(_localctx, 308, RULE_zero);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2276);
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
		enterRule(_localctx, 310, RULE_digitnonzero);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2278);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 2143289344L) != 0)) ) {
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
		public TerminalNode HASH() { return getToken(ECLParser.HASH, 0); }
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
		enterRule(_localctx, 312, RULE_nonwsnonpipe);
		int _la;
		try {
			setState(2283);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EXCLAMATION:
			case QUOTE:
			case HASH:
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
				setState(2280);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & -64L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 8589934591L) != 0)) ) {
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
				setState(2281);
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
				setState(2282);
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
		public TerminalNode HASH() { return getToken(ECLParser.HASH, 0); }
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
		enterRule(_localctx, 314, RULE_anynonescapedchar);
		int _la;
		try {
			setState(2293);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,308,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2285);
				sp();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2286);
				htab();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2287);
				cr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(2288);
				lf();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(2289);
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
				setState(2290);
				_la = _input.LA(1);
				if ( !(((((_la - 8)) & ~0x3f) == 0 && ((1L << (_la - 8)) & 144115188075855871L) != 0)) ) {
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
				setState(2291);
				_la = _input.LA(1);
				if ( !(((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & 17179869183L) != 0)) ) {
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
				setState(2292);
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
		enterRule(_localctx, 316, RULE_escapedchar);
		try {
			setState(2301);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,309,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(2295);
				bs();
				setState(2296);
				qm();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(2298);
				bs();
				setState(2299);
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

	@SuppressWarnings("CheckReturnValue")
	public static class EscapedwildcharContext extends ParserRuleContext {
		public List<BsContext> bs() {
			return getRuleContexts(BsContext.class);
		}
		public BsContext bs(int i) {
			return getRuleContext(BsContext.class,i);
		}
		public QmContext qm() {
			return getRuleContext(QmContext.class,0);
		}
		public StarContext star() {
			return getRuleContext(StarContext.class,0);
		}
		public EscapedwildcharContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_escapedwildchar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterEscapedwildchar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitEscapedwildchar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitEscapedwildchar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EscapedwildcharContext escapedwildchar() throws RecognitionException {
		EscapedwildcharContext _localctx = new EscapedwildcharContext(_ctx, getState());
		enterRule(_localctx, 318, RULE_escapedwildchar);
		try {
			setState(2312);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,310,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(2303);
				bs();
				setState(2304);
				qm();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(2306);
				bs();
				setState(2307);
				bs();
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(2309);
				bs();
				setState(2310);
				star();
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
	public static class NonwsnonescapedcharContext extends ParserRuleContext {
		public TerminalNode EXCLAMATION() { return getToken(ECLParser.EXCLAMATION, 0); }
		public TerminalNode HASH() { return getToken(ECLParser.HASH, 0); }
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
		public NonwsnonescapedcharContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonwsnonescapedchar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterNonwsnonescapedchar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitNonwsnonescapedchar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitNonwsnonescapedchar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonwsnonescapedcharContext nonwsnonescapedchar() throws RecognitionException {
		NonwsnonescapedcharContext _localctx = new NonwsnonescapedcharContext(_ctx, getState());
		enterRule(_localctx, 320, RULE_nonwsnonescapedchar);
		int _la;
		try {
			setState(2318);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EXCLAMATION:
				enterOuterAlt(_localctx, 1);
				{
				setState(2314);
				match(EXCLAMATION);
				}
				break;
			case HASH:
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
				enterOuterAlt(_localctx, 2);
				{
				setState(2315);
				_la = _input.LA(1);
				if ( !(((((_la - 8)) & ~0x3f) == 0 && ((1L << (_la - 8)) & 144115188075855871L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
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
				enterOuterAlt(_localctx, 3);
				{
				setState(2316);
				_la = _input.LA(1);
				if ( !(((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & 17179869183L) != 0)) ) {
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
				enterOuterAlt(_localctx, 4);
				{
				setState(2317);
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
	public static class AlphaContext extends ParserRuleContext {
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
		public AlphaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alpha; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterAlpha(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitAlpha(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitAlpha(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AlphaContext alpha() throws RecognitionException {
		AlphaContext _localctx = new AlphaContext(_ctx, getState());
		enterRule(_localctx, 322, RULE_alpha);
		int _la;
		try {
			setState(2322);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
				enterOuterAlt(_localctx, 1);
				{
				setState(2320);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & -274877906944L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
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
				enterOuterAlt(_localctx, 2);
				{
				setState(2321);
				_la = _input.LA(1);
				if ( !(((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & 67108863L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
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
	public static class DashContext extends ParserRuleContext {
		public TerminalNode DASH() { return getToken(ECLParser.DASH, 0); }
		public DashContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dash; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).enterDash(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ECLListener ) ((ECLListener)listener).exitDash(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ECLVisitor ) return ((ECLVisitor<? extends T>)visitor).visitDash(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DashContext dash() throws RecognitionException {
		DashContext _localctx = new DashContext(_ctx, getState());
		enterRule(_localctx, 324, RULE_dash);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2324);
			match(DASH);
			}
		}
		catch (RecognitionException re) {
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
		"\u0004\u0001c\u0917\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
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
		"K\u0007K\u0002L\u0007L\u0002M\u0007M\u0002N\u0007N\u0002O\u0007O\u0002"+
		"P\u0007P\u0002Q\u0007Q\u0002R\u0007R\u0002S\u0007S\u0002T\u0007T\u0002"+
		"U\u0007U\u0002V\u0007V\u0002W\u0007W\u0002X\u0007X\u0002Y\u0007Y\u0002"+
		"Z\u0007Z\u0002[\u0007[\u0002\\\u0007\\\u0002]\u0007]\u0002^\u0007^\u0002"+
		"_\u0007_\u0002`\u0007`\u0002a\u0007a\u0002b\u0007b\u0002c\u0007c\u0002"+
		"d\u0007d\u0002e\u0007e\u0002f\u0007f\u0002g\u0007g\u0002h\u0007h\u0002"+
		"i\u0007i\u0002j\u0007j\u0002k\u0007k\u0002l\u0007l\u0002m\u0007m\u0002"+
		"n\u0007n\u0002o\u0007o\u0002p\u0007p\u0002q\u0007q\u0002r\u0007r\u0002"+
		"s\u0007s\u0002t\u0007t\u0002u\u0007u\u0002v\u0007v\u0002w\u0007w\u0002"+
		"x\u0007x\u0002y\u0007y\u0002z\u0007z\u0002{\u0007{\u0002|\u0007|\u0002"+
		"}\u0007}\u0002~\u0007~\u0002\u007f\u0007\u007f\u0002\u0080\u0007\u0080"+
		"\u0002\u0081\u0007\u0081\u0002\u0082\u0007\u0082\u0002\u0083\u0007\u0083"+
		"\u0002\u0084\u0007\u0084\u0002\u0085\u0007\u0085\u0002\u0086\u0007\u0086"+
		"\u0002\u0087\u0007\u0087\u0002\u0088\u0007\u0088\u0002\u0089\u0007\u0089"+
		"\u0002\u008a\u0007\u008a\u0002\u008b\u0007\u008b\u0002\u008c\u0007\u008c"+
		"\u0002\u008d\u0007\u008d\u0002\u008e\u0007\u008e\u0002\u008f\u0007\u008f"+
		"\u0002\u0090\u0007\u0090\u0002\u0091\u0007\u0091\u0002\u0092\u0007\u0092"+
		"\u0002\u0093\u0007\u0093\u0002\u0094\u0007\u0094\u0002\u0095\u0007\u0095"+
		"\u0002\u0096\u0007\u0096\u0002\u0097\u0007\u0097\u0002\u0098\u0007\u0098"+
		"\u0002\u0099\u0007\u0099\u0002\u009a\u0007\u009a\u0002\u009b\u0007\u009b"+
		"\u0002\u009c\u0007\u009c\u0002\u009d\u0007\u009d\u0002\u009e\u0007\u009e"+
		"\u0002\u009f\u0007\u009f\u0002\u00a0\u0007\u00a0\u0002\u00a1\u0007\u00a1"+
		"\u0002\u00a2\u0007\u00a2\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0003\u0001\u0151\b\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0003\u0003\u015e\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0004\u0004\u0166\b\u0004\u000b\u0004"+
		"\f\u0004\u0167\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0004\u0005\u0170\b\u0005\u000b\u0005\f\u0005\u0171\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0004\u0007\u017e\b\u0007\u000b"+
		"\u0007\f\u0007\u017f\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001"+
		"\t\u0003\t\u0189\b\t\u0001\t\u0001\t\u0001\t\u0003\t\u018e\b\t\u0001\t"+
		"\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u0197\b\t\u0001"+
		"\t\u0001\t\u0001\t\u0005\t\u019c\b\t\n\t\f\t\u019f\t\t\u0001\t\u0001\t"+
		"\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u01a8\b\t\u0003\t\u01aa"+
		"\b\t\u0001\t\u0001\t\u0001\t\u0003\t\u01af\b\t\u0005\t\u01b1\b\t\n\t\f"+
		"\t\u01b4\t\t\u0001\t\u0001\t\u0001\t\u0003\t\u01b9\b\t\u0001\n\u0001\n"+
		"\u0003\n\u01bd\b\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0003\f\u01c7\b\f\u0001\f\u0001\f\u0001\f\u0003\f\u01cc"+
		"\b\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0005\r\u01d4\b\r"+
		"\n\r\f\r\u01d7\t\r\u0001\u000e\u0001\u000e\u0003\u000e\u01db\b\u000e\u0001"+
		"\u000f\u0004\u000f\u01de\b\u000f\u000b\u000f\f\u000f\u01df\u0001\u0010"+
		"\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u01ec\b\u0011\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0004\u0012"+
		"\u01f4\b\u0012\u000b\u0012\f\u0012\u01f5\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0013\u0001\u0013\u0001\u0014\u0004\u0014\u01fe\b\u0014\u000b"+
		"\u0014\f\u0014\u01ff\u0001\u0014\u0004\u0014\u0203\b\u0014\u000b\u0014"+
		"\f\u0014\u0204\u0001\u0014\u0004\u0014\u0208\b\u0014\u000b\u0014\f\u0014"+
		"\u0209\u0005\u0014\u020c\b\u0014\n\u0014\f\u0014\u020f\t\u0014\u0001\u0015"+
		"\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u021b\b\u0016\u0001\u0017"+
		"\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001b"+
		"\u0001\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001f"+
		"\u0001\u001f\u0003\u001f\u0237\b\u001f\u0001\u001f\u0001\u001f\u0003\u001f"+
		"\u023b\b\u001f\u0001\u001f\u0001\u001f\u0003\u001f\u023f\b\u001f\u0001"+
		"\u001f\u0001\u001f\u0003\u001f\u0243\b\u001f\u0001 \u0001 \u0003 \u0247"+
		"\b \u0001 \u0001 \u0003 \u024b\b \u0001 \u0001 \u0001!\u0001!\u0003!\u0251"+
		"\b!\u0001!\u0001!\u0003!\u0255\b!\u0001!\u0001!\u0003!\u0259\b!\u0001"+
		"!\u0001!\u0003!\u025d\b!\u0001!\u0001!\u0003!\u0261\b!\u0001!\u0001!\u0001"+
		"\"\u0001\"\u0001\"\u0003\"\u0268\b\"\u0001#\u0001#\u0001#\u0001#\u0001"+
		"#\u0001#\u0004#\u0270\b#\u000b#\f#\u0271\u0001$\u0001$\u0001$\u0001$\u0001"+
		"$\u0001$\u0004$\u027a\b$\u000b$\f$\u027b\u0001%\u0001%\u0001%\u0001%\u0001"+
		"%\u0001%\u0001%\u0001%\u0003%\u0286\b%\u0001&\u0001&\u0001&\u0003&\u028b"+
		"\b&\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0004\'\u0293\b\'"+
		"\u000b\'\f\'\u0294\u0001(\u0001(\u0001(\u0001(\u0001(\u0001(\u0004(\u029d"+
		"\b(\u000b(\f(\u029e\u0001)\u0001)\u0001)\u0001)\u0001)\u0001)\u0001)\u0003"+
		")\u02a8\b)\u0001*\u0001*\u0001*\u0001*\u0001*\u0003*\u02af\b*\u0001*\u0001"+
		"*\u0001*\u0001*\u0001*\u0001*\u0001+\u0001+\u0001+\u0001+\u0001+\u0003"+
		"+\u02bc\b+\u0001+\u0001+\u0001+\u0003+\u02c1\b+\u0001+\u0001+\u0001+\u0001"+
		"+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001"+
		"+\u0001+\u0003+\u02d2\b+\u0001+\u0001+\u0001+\u0001+\u0003+\u02d8\b+\u0001"+
		",\u0001,\u0001,\u0001,\u0001-\u0001-\u0001.\u0001.\u0001.\u0001/\u0001"+
		"/\u0003/\u02e5\b/\u00010\u00010\u00011\u00011\u00012\u00012\u00013\u0001"+
		"3\u00013\u00033\u02f0\b3\u00014\u00014\u00014\u00014\u00014\u00014\u0001"+
		"4\u00014\u00014\u00034\u02fb\b4\u00015\u00015\u00015\u00015\u00015\u0001"+
		"5\u00015\u00015\u00015\u00035\u0306\b5\u00016\u00016\u00016\u00036\u030b"+
		"\b6\u00017\u00017\u00017\u00037\u0310\b7\u00018\u00018\u00018\u00018\u0001"+
		"8\u00018\u00038\u0318\b8\u00018\u00018\u00018\u00018\u00018\u00018\u0001"+
		"8\u00058\u0321\b8\n8\f8\u0324\t8\u00018\u00018\u00018\u00018\u00019\u0001"+
		"9\u00019\u00019\u00019\u00019\u00019\u00039\u0331\b9\u0001:\u0001:\u0001"+
		":\u0001:\u0001:\u0001:\u0003:\u0339\b:\u0001;\u0001;\u0003;\u033d\b;\u0001"+
		";\u0001;\u0003;\u0341\b;\u0001;\u0001;\u0003;\u0345\b;\u0001;\u0001;\u0003"+
		";\u0349\b;\u0001<\u0001<\u0001<\u0001<\u0001<\u0003<\u0350\b<\u0001<\u0001"+
		"<\u0001<\u0001<\u0001<\u0001<\u0001<\u0003<\u0359\b<\u0001=\u0001=\u0001"+
		"=\u0001=\u0001=\u0001=\u0005=\u0361\b=\n=\f=\u0364\t=\u0001=\u0001=\u0001"+
		"=\u0001>\u0001>\u0003>\u036b\b>\u0001>\u0001>\u0003>\u036f\b>\u0001>\u0001"+
		">\u0003>\u0373\b>\u0001>\u0001>\u0003>\u0377\b>\u0001?\u0001?\u0003?\u037b"+
		"\b?\u0001?\u0001?\u0003?\u037f\b?\u0001?\u0001?\u0003?\u0383\b?\u0001"+
		"?\u0001?\u0003?\u0387\b?\u0001?\u0001?\u0003?\u038b\b?\u0001@\u0001@\u0004"+
		"@\u038f\b@\u000b@\f@\u0390\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0005"+
		"A\u0399\bA\nA\fA\u039c\tA\u0001A\u0001A\u0001A\u0001B\u0001B\u0004B\u03a3"+
		"\bB\u000bB\fB\u03a4\u0001C\u0001C\u0001C\u0001C\u0001D\u0001D\u0001D\u0001"+
		"D\u0001D\u0001D\u0003D\u03b1\bD\u0001E\u0001E\u0003E\u03b5\bE\u0001E\u0001"+
		"E\u0003E\u03b9\bE\u0001E\u0001E\u0003E\u03bd\bE\u0001E\u0001E\u0003E\u03c1"+
		"\bE\u0001E\u0001E\u0003E\u03c5\bE\u0001E\u0001E\u0003E\u03c9\bE\u0001"+
		"E\u0001E\u0003E\u03cd\bE\u0001E\u0001E\u0003E\u03d1\bE\u0001F\u0001F\u0001"+
		"F\u0001G\u0001G\u0001G\u0001G\u0001G\u0001G\u0005G\u03dc\bG\nG\fG\u03df"+
		"\tG\u0001G\u0001G\u0001G\u0001H\u0001H\u0003H\u03e6\bH\u0001I\u0001I\u0001"+
		"I\u0001I\u0001I\u0001I\u0003I\u03ee\bI\u0001J\u0001J\u0003J\u03f2\bJ\u0001"+
		"J\u0001J\u0003J\u03f6\bJ\u0001J\u0001J\u0003J\u03fa\bJ\u0001J\u0001J\u0003"+
		"J\u03fe\bJ\u0001J\u0001J\u0003J\u0402\bJ\u0001J\u0001J\u0003J\u0406\b"+
		"J\u0001K\u0001K\u0001K\u0001K\u0001K\u0001K\u0003K\u040e\bK\u0001L\u0001"+
		"L\u0003L\u0412\bL\u0001L\u0001L\u0003L\u0416\bL\u0001L\u0001L\u0003L\u041a"+
		"\bL\u0001L\u0001L\u0003L\u041e\bL\u0001M\u0001M\u0001M\u0003M\u0423\b"+
		"M\u0001N\u0001N\u0001N\u0001N\u0001N\u0001N\u0005N\u042b\bN\nN\fN\u042e"+
		"\tN\u0001N\u0001N\u0001N\u0001O\u0001O\u0003O\u0435\bO\u0001O\u0001O\u0003"+
		"O\u0439\bO\u0001O\u0001O\u0003O\u043d\bO\u0001P\u0001P\u0003P\u0441\b"+
		"P\u0001P\u0001P\u0003P\u0445\bP\u0001P\u0001P\u0003P\u0449\bP\u0001Q\u0001"+
		"Q\u0003Q\u044d\bQ\u0001Q\u0001Q\u0003Q\u0451\bQ\u0001Q\u0001Q\u0003Q\u0455"+
		"\bQ\u0001R\u0001R\u0003R\u0459\bR\u0001R\u0001R\u0001R\u0003R\u045e\b"+
		"R\u0001S\u0001S\u0001S\u0001S\u0001S\u0001S\u0003S\u0466\bS\u0001T\u0001"+
		"T\u0003T\u046a\bT\u0001T\u0001T\u0003T\u046e\bT\u0001T\u0001T\u0003T\u0472"+
		"\bT\u0001T\u0001T\u0003T\u0476\bT\u0001T\u0001T\u0003T\u047a\bT\u0001"+
		"T\u0001T\u0003T\u047e\bT\u0001T\u0001T\u0003T\u0482\bT\u0001T\u0001T\u0003"+
		"T\u0486\bT\u0001T\u0001T\u0003T\u048a\bT\u0001U\u0001U\u0001U\u0001U\u0001"+
		"U\u0001U\u0003U\u0492\bU\u0001V\u0001V\u0003V\u0496\bV\u0001V\u0001V\u0003"+
		"V\u049a\bV\u0001V\u0001V\u0003V\u049e\bV\u0001V\u0001V\u0003V\u04a2\b"+
		"V\u0001V\u0001V\u0003V\u04a6\bV\u0001V\u0001V\u0003V\u04aa\bV\u0001V\u0001"+
		"V\u0003V\u04ae\bV\u0001W\u0001W\u0001W\u0001W\u0005W\u04b4\bW\nW\fW\u04b7"+
		"\tW\u0001X\u0001X\u0001X\u0001X\u0001X\u0001X\u0003X\u04bf\bX\u0001X\u0001"+
		"X\u0001X\u0001X\u0001X\u0003X\u04c6\bX\u0005X\u04c8\bX\nX\fX\u04cb\tX"+
		"\u0001X\u0001X\u0001X\u0001Y\u0001Y\u0001Y\u0001Y\u0001Y\u0001Y\u0003"+
		"Y\u04d6\bY\u0001Y\u0001Y\u0001Y\u0001Y\u0001Y\u0003Y\u04dd\bY\u0005Y\u04df"+
		"\bY\nY\fY\u04e2\tY\u0001Y\u0001Y\u0001Y\u0001Z\u0001Z\u0003Z\u04e9\bZ"+
		"\u0001[\u0001[\u0001[\u0001[\u0001[\u0001[\u0005[\u04f1\b[\n[\f[\u04f4"+
		"\t[\u0001[\u0001[\u0001[\u0001\\\u0001\\\u0001\\\u0001\\\u0001\\\u0001"+
		"\\\u0005\\\u04ff\b\\\n\\\f\\\u0502\t\\\u0001\\\u0001\\\u0001\\\u0001]"+
		"\u0001]\u0003]\u0509\b]\u0001^\u0001^\u0003^\u050d\b^\u0001^\u0001^\u0003"+
		"^\u0511\b^\u0001^\u0001^\u0003^\u0515\b^\u0001^\u0001^\u0003^\u0519\b"+
		"^\u0001^\u0001^\u0003^\u051d\b^\u0001^\u0001^\u0003^\u0521\b^\u0001_\u0001"+
		"_\u0003_\u0525\b_\u0001_\u0001_\u0003_\u0529\b_\u0001_\u0001_\u0003_\u052d"+
		"\b_\u0001_\u0001_\u0003_\u0531\b_\u0001_\u0001_\u0003_\u0535\b_\u0001"+
		"_\u0001_\u0003_\u0539\b_\u0001`\u0001`\u0001`\u0001`\u0001`\u0001`\u0003"+
		"`\u0541\b`\u0001`\u0001`\u0001`\u0001`\u0001`\u0001`\u0001`\u0005`\u054a"+
		"\b`\n`\f`\u054d\t`\u0001`\u0001`\u0001`\u0001`\u0001a\u0001a\u0001a\u0001"+
		"a\u0003a\u0557\ba\u0001b\u0001b\u0003b\u055b\bb\u0001c\u0001c\u0001c\u0001"+
		"c\u0001c\u0001c\u0003c\u0563\bc\u0001d\u0001d\u0003d\u0567\bd\u0001d\u0001"+
		"d\u0003d\u056b\bd\u0001d\u0001d\u0003d\u056f\bd\u0001d\u0001d\u0003d\u0573"+
		"\bd\u0001d\u0001d\u0003d\u0577\bd\u0001d\u0001d\u0003d\u057b\bd\u0001"+
		"d\u0001d\u0003d\u057f\bd\u0001d\u0001d\u0003d\u0583\bd\u0001d\u0001d\u0003"+
		"d\u0587\bd\u0001d\u0001d\u0003d\u058b\bd\u0001d\u0001d\u0003d\u058f\b"+
		"d\u0001d\u0001d\u0003d\u0593\bd\u0001d\u0001d\u0003d\u0597\bd\u0001d\u0001"+
		"d\u0003d\u059b\bd\u0001d\u0001d\u0003d\u059f\bd\u0001d\u0001d\u0003d\u05a3"+
		"\bd\u0001d\u0001d\u0003d\u05a7\bd\u0001d\u0001d\u0003d\u05ab\bd\u0001"+
		"e\u0001e\u0001e\u0001e\u0001e\u0001e\u0003e\u05b3\be\u0001f\u0001f\u0003"+
		"f\u05b7\bf\u0001f\u0001f\u0003f\u05bb\bf\u0001f\u0001f\u0003f\u05bf\b"+
		"f\u0001f\u0001f\u0003f\u05c3\bf\u0001f\u0001f\u0003f\u05c7\bf\u0001f\u0001"+
		"f\u0003f\u05cb\bf\u0001f\u0001f\u0003f\u05cf\bf\u0001f\u0001f\u0003f\u05d3"+
		"\bf\u0001f\u0001f\u0003f\u05d7\bf\u0001f\u0001f\u0003f\u05db\bf\u0001"+
		"f\u0001f\u0003f\u05df\bf\u0001f\u0001f\u0003f\u05e3\bf\u0001f\u0001f\u0003"+
		"f\u05e7\bf\u0001f\u0001f\u0003f\u05eb\bf\u0001f\u0001f\u0003f\u05ef\b"+
		"f\u0001f\u0001f\u0003f\u05f3\bf\u0001g\u0001g\u0003g\u05f7\bg\u0001h\u0001"+
		"h\u0001h\u0001h\u0001h\u0001h\u0005h\u05ff\bh\nh\fh\u0602\th\u0001h\u0001"+
		"h\u0001h\u0001i\u0001i\u0003i\u0609\bi\u0001i\u0001i\u0003i\u060d\bi\u0001"+
		"i\u0001i\u0003i\u0611\bi\u0001i\u0001i\u0003i\u0615\bi\u0001i\u0001i\u0003"+
		"i\u0619\bi\u0001i\u0001i\u0003i\u061d\bi\u0001i\u0001i\u0003i\u0621\b"+
		"i\u0001i\u0001i\u0003i\u0625\bi\u0001i\u0001i\u0003i\u0629\bi\u0001j\u0001"+
		"j\u0003j\u062d\bj\u0001j\u0001j\u0003j\u0631\bj\u0001j\u0001j\u0003j\u0635"+
		"\bj\u0001j\u0001j\u0003j\u0639\bj\u0001j\u0001j\u0003j\u063d\bj\u0001"+
		"j\u0001j\u0003j\u0641\bj\u0001j\u0001j\u0003j\u0645\bj\u0001k\u0001k\u0001"+
		"k\u0001k\u0001k\u0001k\u0003k\u064d\bk\u0001l\u0001l\u0003l\u0651\bl\u0001"+
		"l\u0001l\u0003l\u0655\bl\u0001l\u0001l\u0003l\u0659\bl\u0001l\u0001l\u0003"+
		"l\u065d\bl\u0001l\u0001l\u0003l\u0661\bl\u0001l\u0001l\u0003l\u0665\b"+
		"l\u0001l\u0001l\u0003l\u0669\bl\u0001l\u0001l\u0003l\u066d\bl\u0001m\u0001"+
		"m\u0001m\u0001m\u0001m\u0001m\u0003m\u0675\bm\u0001n\u0001n\u0003n\u0679"+
		"\bn\u0001n\u0001n\u0003n\u067d\bn\u0001n\u0001n\u0003n\u0681\bn\u0001"+
		"n\u0001n\u0003n\u0685\bn\u0001n\u0001n\u0003n\u0689\bn\u0001n\u0001n\u0003"+
		"n\u068d\bn\u0001n\u0001n\u0003n\u0691\bn\u0001n\u0001n\u0003n\u0695\b"+
		"n\u0001n\u0001n\u0003n\u0699\bn\u0001n\u0001n\u0003n\u069d\bn\u0001n\u0001"+
		"n\u0003n\u06a1\bn\u0001n\u0001n\u0003n\u06a5\bn\u0001n\u0001n\u0003n\u06a9"+
		"\bn\u0001o\u0001o\u0001o\u0001o\u0001o\u0003o\u06b0\bo\u0001o\u0001o\u0001"+
		"p\u0001p\u0001p\u0001p\u0001p\u0001p\u0005p\u06ba\bp\np\fp\u06bd\tp\u0001"+
		"p\u0001p\u0001p\u0001q\u0001q\u0001q\u0001q\u0001q\u0001r\u0001r\u0001"+
		"r\u0001r\u0001r\u0001r\u0001r\u0001r\u0001r\u0001r\u0001r\u0001r\u0001"+
		"r\u0001r\u0001r\u0001r\u0001r\u0001r\u0001r\u0001r\u0001r\u0001r\u0001"+
		"r\u0001r\u0003r\u06df\br\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001"+
		"s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001"+
		"s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001"+
		"s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001"+
		"s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001"+
		"s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001s\u0001"+
		"s\u0001s\u0001s\u0001s\u0001s\u0001s\u0003s\u071f\bs\u0001t\u0001t\u0001"+
		"t\u0001t\u0001t\u0001t\u0001u\u0001u\u0003u\u0729\bu\u0001u\u0001u\u0003"+
		"u\u072d\bu\u0001u\u0001u\u0003u\u0731\bu\u0001u\u0001u\u0003u\u0735\b"+
		"u\u0001u\u0001u\u0003u\u0739\bu\u0001u\u0001u\u0003u\u073d\bu\u0001v\u0001"+
		"v\u0003v\u0741\bv\u0001w\u0001w\u0001w\u0001w\u0001w\u0003w\u0748\bw\u0001"+
		"x\u0001x\u0001x\u0001x\u0001x\u0001x\u0003x\u0750\bx\u0001y\u0001y\u0001"+
		"y\u0001y\u0001y\u0001y\u0003y\u0758\by\u0001y\u0001y\u0001y\u0001y\u0001"+
		"y\u0001y\u0001y\u0005y\u0761\by\ny\fy\u0764\ty\u0001y\u0001y\u0001y\u0001"+
		"y\u0001z\u0001z\u0001z\u0001z\u0003z\u076e\bz\u0001{\u0001{\u0001{\u0001"+
		"{\u0001{\u0001{\u0001{\u0001{\u0001{\u0001{\u0001{\u0001{\u0001{\u0001"+
		"{\u0001{\u0003{\u077f\b{\u0001{\u0001{\u0001{\u0001{\u0001{\u0001{\u0001"+
		"{\u0001{\u0001{\u0003{\u078a\b{\u0003{\u078c\b{\u0001|\u0001|\u0001|\u0001"+
		"|\u0001|\u0001|\u0001|\u0001|\u0001|\u0001|\u0001|\u0003|\u0799\b|\u0001"+
		"|\u0001|\u0001|\u0001|\u0001}\u0001}\u0003}\u07a1\b}\u0001}\u0001}\u0003"+
		"}\u07a5\b}\u0001}\u0001}\u0003}\u07a9\b}\u0001}\u0001}\u0003}\u07ad\b"+
		"}\u0001}\u0001}\u0003}\u07b1\b}\u0001}\u0001}\u0003}\u07b5\b}\u0001}\u0001"+
		"}\u0003}\u07b9\b}\u0001~\u0001~\u0001~\u0003~\u07be\b~\u0001\u007f\u0001"+
		"\u007f\u0001\u007f\u0003\u007f\u07c3\b\u007f\u0001\u007f\u0001\u007f\u0003"+
		"\u007f\u07c7\b\u007f\u0001\u007f\u0001\u007f\u0003\u007f\u07cb\b\u007f"+
		"\u0001\u0080\u0001\u0080\u0001\u0080\u0003\u0080\u07d0\b\u0080\u0001\u0080"+
		"\u0001\u0080\u0003\u0080\u07d4\b\u0080\u0001\u0080\u0001\u0080\u0003\u0080"+
		"\u07d8\b\u0080\u0001\u0081\u0001\u0081\u0001\u0081\u0003\u0081\u07dd\b"+
		"\u0081\u0001\u0081\u0001\u0081\u0003\u0081\u07e1\b\u0081\u0001\u0081\u0001"+
		"\u0081\u0003\u0081\u07e5\b\u0081\u0001\u0082\u0001\u0082\u0001\u0082\u0001"+
		"\u0082\u0001\u0082\u0001\u0082\u0001\u0083\u0003\u0083\u07ee\b\u0083\u0001"+
		"\u0083\u0001\u0083\u0003\u0083\u07f2\b\u0083\u0001\u0084\u0001\u0084\u0004"+
		"\u0084\u07f6\b\u0084\u000b\u0084\f\u0084\u07f7\u0001\u0085\u0001\u0085"+
		"\u0005\u0085\u07fc\b\u0085\n\u0085\f\u0085\u07ff\t\u0085\u0001\u0085\u0003"+
		"\u0085\u0802\b\u0085\u0001\u0086\u0001\u0086\u0001\u0086\u0004\u0086\u0807"+
		"\b\u0086\u000b\u0086\f\u0086\u0808\u0001\u0087\u0001\u0087\u0003\u0087"+
		"\u080d\b\u0087\u0001\u0088\u0001\u0088\u0003\u0088\u0811\b\u0088\u0001"+
		"\u0088\u0001\u0088\u0003\u0088\u0815\b\u0088\u0001\u0088\u0001\u0088\u0003"+
		"\u0088\u0819\b\u0088\u0001\u0088\u0001\u0088\u0003\u0088\u081d\b\u0088"+
		"\u0001\u0089\u0001\u0089\u0003\u0089\u0821\b\u0089\u0001\u0089\u0001\u0089"+
		"\u0003\u0089\u0825\b\u0089\u0001\u0089\u0001\u0089\u0003\u0089\u0829\b"+
		"\u0089\u0001\u0089\u0001\u0089\u0003\u0089\u082d\b\u0089\u0001\u0089\u0001"+
		"\u0089\u0003\u0089\u0831\b\u0089\u0001\u008a\u0001\u008a\u0005\u008a\u0835"+
		"\b\u008a\n\u008a\f\u008a\u0838\t\u008a\u0001\u008a\u0003\u008a\u083b\b"+
		"\u008a\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001"+
		"\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001"+
		"\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001"+
		"\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001"+
		"\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001"+
		"\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001"+
		"\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001"+
		"\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001"+
		"\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001"+
		"\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001"+
		"\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001"+
		"\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001"+
		"\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001"+
		"\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001"+
		"\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001"+
		"\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0001\u008b\u0003"+
		"\u008b\u089c\b\u008b\u0003\u008b\u089e\b\u008b\u0001\u008c\u0001\u008c"+
		"\u0001\u008c\u0001\u008c\u0001\u008c\u0005\u008c\u08a5\b\u008c\n\u008c"+
		"\f\u008c\u08a8\t\u008c\u0001\u008d\u0001\u008d\u0001\u008d\u0001\u008d"+
		"\u0001\u008d\u0004\u008d\u08af\b\u008d\u000b\u008d\f\u008d\u08b0\u0001"+
		"\u008e\u0001\u008e\u0001\u008e\u0001\u008e\u0001\u008e\u0005\u008e\u08b8"+
		"\b\u008e\n\u008e\f\u008e\u08bb\t\u008e\u0001\u008e\u0001\u008e\u0001\u008e"+
		"\u0001\u008f\u0001\u008f\u0001\u008f\u0001\u008f\u0001\u008f\u0001\u008f"+
		"\u0001\u008f\u0003\u008f\u08c7\b\u008f\u0001\u0090\u0001\u0090\u0001\u0090"+
		"\u0001\u0091\u0001\u0091\u0001\u0091\u0001\u0091\u0001\u0091\u0001\u0091"+
		"\u0001\u0091\u0003\u0091\u08d3\b\u0091\u0001\u0092\u0001\u0092\u0001\u0093"+
		"\u0001\u0093\u0001\u0094\u0001\u0094\u0001\u0095\u0001\u0095\u0001\u0096"+
		"\u0001\u0096\u0001\u0097\u0001\u0097\u0001\u0098\u0001\u0098\u0001\u0099"+
		"\u0001\u0099\u0001\u009a\u0001\u009a\u0001\u009b\u0001\u009b\u0001\u009c"+
		"\u0001\u009c\u0001\u009c\u0003\u009c\u08ec\b\u009c\u0001\u009d\u0001\u009d"+
		"\u0001\u009d\u0001\u009d\u0001\u009d\u0001\u009d\u0001\u009d\u0001\u009d"+
		"\u0003\u009d\u08f6\b\u009d\u0001\u009e\u0001\u009e\u0001\u009e\u0001\u009e"+
		"\u0001\u009e\u0001\u009e\u0003\u009e\u08fe\b\u009e\u0001\u009f\u0001\u009f"+
		"\u0001\u009f\u0001\u009f\u0001\u009f\u0001\u009f\u0001\u009f\u0001\u009f"+
		"\u0001\u009f\u0003\u009f\u0909\b\u009f\u0001\u00a0\u0001\u00a0\u0001\u00a0"+
		"\u0001\u00a0\u0003\u00a0\u090f\b\u00a0\u0001\u00a1\u0001\u00a1\u0003\u00a1"+
		"\u0913\b\u00a1\u0001\u00a2\u0001\u00a2\u0001\u00a2\u0000\u0000\u00a3\u0000"+
		"\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c"+
		"\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084"+
		"\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c"+
		"\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4"+
		"\u00b6\u00b8\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc"+
		"\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4"+
		"\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc"+
		"\u00fe\u0100\u0102\u0104\u0106\u0108\u010a\u010c\u010e\u0110\u0112\u0114"+
		"\u0116\u0118\u011a\u011c\u011e\u0120\u0122\u0124\u0126\u0128\u012a\u012c"+
		"\u012e\u0130\u0132\u0134\u0136\u0138\u013a\u013c\u013e\u0140\u0142\u0144"+
		"\u0000$\u0002\u0000&&FF\u0002\u000033SS\u0002\u0000))II\u0002\u000044"+
		"TT\u0002\u000077WW\u0002\u000022RR\u0002\u0000..NN\u0002\u0000::ZZ\u0002"+
		"\u000088XX\u0002\u000099YY\u0002\u0000**JJ\u0002\u0000<<\\\\\u0002\u0000"+
		"11QQ\u0002\u0000((HH\u0002\u0000--MM\u0002\u0000,,LL\u0002\u0000>>^^\u0002"+
		"\u000055UU\u0002\u0000++KK\u0002\u0000;;[[\u0002\u0000\u0012\u0012DD\u0002"+
		"\u0000==]]\u0002\u0000\u0010\u0010\u0012\u0012\u0001\u0000\u0006\u000e"+
		"\u0001\u0000\u0010c\u0001\u0000\u0006\u0013\u0001\u0000\u0015c\u0001\u0000"+
		"\u0015\u001e\u0001\u0000\u0016\u001e\u0001\u0000\u0006`\u0001\u0000bc"+
		"\u0001\u0000\u0005\u0006\u0001\u0000\b@\u0001\u0000Bc\u0001\u0000&?\u0001"+
		"\u0000F_\u0a1f\u0000\u0146\u0001\u0000\u0000\u0000\u0002\u014b\u0001\u0000"+
		"\u0000\u0000\u0004\u0154\u0001\u0000\u0000\u0000\u0006\u015d\u0001\u0000"+
		"\u0000\u0000\b\u015f\u0001\u0000\u0000\u0000\n\u0169\u0001\u0000\u0000"+
		"\u0000\f\u0173\u0001\u0000\u0000\u0000\u000e\u0179\u0001\u0000\u0000\u0000"+
		"\u0010\u0181\u0001\u0000\u0000\u0000\u0012\u0188\u0001\u0000\u0000\u0000"+
		"\u0014\u01bc\u0001\u0000\u0000\u0000\u0016\u01be\u0001\u0000\u0000\u0000"+
		"\u0018\u01c0\u0001\u0000\u0000\u0000\u001a\u01cd\u0001\u0000\u0000\u0000"+
		"\u001c\u01da\u0001\u0000\u0000\u0000\u001e\u01dd\u0001\u0000\u0000\u0000"+
		" \u01e1\u0001\u0000\u0000\u0000\"\u01e3\u0001\u0000\u0000\u0000$\u01ed"+
		"\u0001\u0000\u0000\u0000&\u01fa\u0001\u0000\u0000\u0000(\u01fd\u0001\u0000"+
		"\u0000\u0000*\u0210\u0001\u0000\u0000\u0000,\u021a\u0001\u0000\u0000\u0000"+
		".\u021c\u0001\u0000\u0000\u00000\u021e\u0001\u0000\u0000\u00002\u0221"+
		"\u0001\u0000\u0000\u00004\u0224\u0001\u0000\u0000\u00006\u0228\u0001\u0000"+
		"\u0000\u00008\u022a\u0001\u0000\u0000\u0000:\u022d\u0001\u0000\u0000\u0000"+
		"<\u0230\u0001\u0000\u0000\u0000>\u0242\u0001\u0000\u0000\u0000@\u0246"+
		"\u0001\u0000\u0000\u0000B\u0250\u0001\u0000\u0000\u0000D\u0267\u0001\u0000"+
		"\u0000\u0000F\u0269\u0001\u0000\u0000\u0000H\u0273\u0001\u0000\u0000\u0000"+
		"J\u0285\u0001\u0000\u0000\u0000L\u028a\u0001\u0000\u0000\u0000N\u028c"+
		"\u0001\u0000\u0000\u0000P\u0296\u0001\u0000\u0000\u0000R\u02a7\u0001\u0000"+
		"\u0000\u0000T\u02ae\u0001\u0000\u0000\u0000V\u02bb\u0001\u0000\u0000\u0000"+
		"X\u02d9\u0001\u0000\u0000\u0000Z\u02dd\u0001\u0000\u0000\u0000\\\u02df"+
		"\u0001\u0000\u0000\u0000^\u02e4\u0001\u0000\u0000\u0000`\u02e6\u0001\u0000"+
		"\u0000\u0000b\u02e8\u0001\u0000\u0000\u0000d\u02ea\u0001\u0000\u0000\u0000"+
		"f\u02ef\u0001\u0000\u0000\u0000h\u02fa\u0001\u0000\u0000\u0000j\u0305"+
		"\u0001\u0000\u0000\u0000l\u030a\u0001\u0000\u0000\u0000n\u030f\u0001\u0000"+
		"\u0000\u0000p\u0311\u0001\u0000\u0000\u0000r\u0330\u0001\u0000\u0000\u0000"+
		"t\u0332\u0001\u0000\u0000\u0000v\u033c\u0001\u0000\u0000\u0000x\u0358"+
		"\u0001\u0000\u0000\u0000z\u035a\u0001\u0000\u0000\u0000|\u036a\u0001\u0000"+
		"\u0000\u0000~\u037a\u0001\u0000\u0000\u0000\u0080\u038e\u0001\u0000\u0000"+
		"\u0000\u0082\u0392\u0001\u0000\u0000\u0000\u0084\u03a2\u0001\u0000\u0000"+
		"\u0000\u0086\u03a6\u0001\u0000\u0000\u0000\u0088\u03aa\u0001\u0000\u0000"+
		"\u0000\u008a\u03b4\u0001\u0000\u0000\u0000\u008c\u03d2\u0001\u0000\u0000"+
		"\u0000\u008e\u03d5\u0001\u0000\u0000\u0000\u0090\u03e5\u0001\u0000\u0000"+
		"\u0000\u0092\u03e7\u0001\u0000\u0000\u0000\u0094\u03f1\u0001\u0000\u0000"+
		"\u0000\u0096\u0407\u0001\u0000\u0000\u0000\u0098\u0411\u0001\u0000\u0000"+
		"\u0000\u009a\u0422\u0001\u0000\u0000\u0000\u009c\u0424\u0001\u0000\u0000"+
		"\u0000\u009e\u0434\u0001\u0000\u0000\u0000\u00a0\u0440\u0001\u0000\u0000"+
		"\u0000\u00a2\u044c\u0001\u0000\u0000\u0000\u00a4\u0458\u0001\u0000\u0000"+
		"\u0000\u00a6\u045f\u0001\u0000\u0000\u0000\u00a8\u0469\u0001\u0000\u0000"+
		"\u0000\u00aa\u048b\u0001\u0000\u0000\u0000\u00ac\u0495\u0001\u0000\u0000"+
		"\u0000\u00ae\u04af\u0001\u0000\u0000\u0000\u00b0\u04b8\u0001\u0000\u0000"+
		"\u0000\u00b2\u04cf\u0001\u0000\u0000\u0000\u00b4\u04e8\u0001\u0000\u0000"+
		"\u0000\u00b6\u04ea\u0001\u0000\u0000\u0000\u00b8\u04f8\u0001\u0000\u0000"+
		"\u0000\u00ba\u0508\u0001\u0000\u0000\u0000\u00bc\u050c\u0001\u0000\u0000"+
		"\u0000\u00be\u0524\u0001\u0000\u0000\u0000\u00c0\u053a\u0001\u0000\u0000"+
		"\u0000\u00c2\u0556\u0001\u0000\u0000\u0000\u00c4\u055a\u0001\u0000\u0000"+
		"\u0000\u00c6\u055c\u0001\u0000\u0000\u0000\u00c8\u0566\u0001\u0000\u0000"+
		"\u0000\u00ca\u05ac\u0001\u0000\u0000\u0000\u00cc\u05b6\u0001\u0000\u0000"+
		"\u0000\u00ce\u05f6\u0001\u0000\u0000\u0000\u00d0\u05f8\u0001\u0000\u0000"+
		"\u0000\u00d2\u0608\u0001\u0000\u0000\u0000\u00d4\u062c\u0001\u0000\u0000"+
		"\u0000\u00d6\u0646\u0001\u0000\u0000\u0000\u00d8\u0650\u0001\u0000\u0000"+
		"\u0000\u00da\u066e\u0001\u0000\u0000\u0000\u00dc\u0678\u0001\u0000\u0000"+
		"\u0000\u00de\u06aa\u0001\u0000\u0000\u0000\u00e0\u06b3\u0001\u0000\u0000"+
		"\u0000\u00e2\u06c1\u0001\u0000\u0000\u0000\u00e4\u06de\u0001\u0000\u0000"+
		"\u0000\u00e6\u071e\u0001\u0000\u0000\u0000\u00e8\u0720\u0001\u0000\u0000"+
		"\u0000\u00ea\u0728\u0001\u0000\u0000\u0000\u00ec\u0740\u0001\u0000\u0000"+
		"\u0000\u00ee\u0747\u0001\u0000\u0000\u0000\u00f0\u074f\u0001\u0000\u0000"+
		"\u0000\u00f2\u0751\u0001\u0000\u0000\u0000\u00f4\u076d\u0001\u0000\u0000"+
		"\u0000\u00f6\u076f\u0001\u0000\u0000\u0000\u00f8\u078d\u0001\u0000\u0000"+
		"\u0000\u00fa\u07a0\u0001\u0000\u0000\u0000\u00fc\u07bd\u0001\u0000\u0000"+
		"\u0000\u00fe\u07bf\u0001\u0000\u0000\u0000\u0100\u07cc\u0001\u0000\u0000"+
		"\u0000\u0102\u07d9\u0001\u0000\u0000\u0000\u0104\u07e6\u0001\u0000\u0000"+
		"\u0000\u0106\u07ed\u0001\u0000\u0000\u0000\u0108\u07f5\u0001\u0000\u0000"+
		"\u0000\u010a\u0801\u0001\u0000\u0000\u0000\u010c\u0803\u0001\u0000\u0000"+
		"\u0000\u010e\u080c\u0001\u0000\u0000\u0000\u0110\u0810\u0001\u0000\u0000"+
		"\u0000\u0112\u0820\u0001\u0000\u0000\u0000\u0114\u083a\u0001\u0000\u0000"+
		"\u0000\u0116\u083c\u0001\u0000\u0000\u0000\u0118\u08a6\u0001\u0000\u0000"+
		"\u0000\u011a\u08ae\u0001\u0000\u0000\u0000\u011c\u08b2\u0001\u0000\u0000"+
		"\u0000\u011e\u08c6\u0001\u0000\u0000\u0000\u0120\u08c8\u0001\u0000\u0000"+
		"\u0000\u0122\u08d2\u0001\u0000\u0000\u0000\u0124\u08d4\u0001\u0000\u0000"+
		"\u0000\u0126\u08d6\u0001\u0000\u0000\u0000\u0128\u08d8\u0001\u0000\u0000"+
		"\u0000\u012a\u08da\u0001\u0000\u0000\u0000\u012c\u08dc\u0001\u0000\u0000"+
		"\u0000\u012e\u08de\u0001\u0000\u0000\u0000\u0130\u08e0\u0001\u0000\u0000"+
		"\u0000\u0132\u08e2\u0001\u0000\u0000\u0000\u0134\u08e4\u0001\u0000\u0000"+
		"\u0000\u0136\u08e6\u0001\u0000\u0000\u0000\u0138\u08eb\u0001\u0000\u0000"+
		"\u0000\u013a\u08f5\u0001\u0000\u0000\u0000\u013c\u08fd\u0001\u0000\u0000"+
		"\u0000\u013e\u0908\u0001\u0000\u0000\u0000\u0140\u090e\u0001\u0000\u0000"+
		"\u0000\u0142\u0912\u0001\u0000\u0000\u0000\u0144\u0914\u0001\u0000\u0000"+
		"\u0000\u0146\u0147\u0003\u0118\u008c\u0000\u0147\u0148\u0003\u0002\u0001"+
		"\u0000\u0148\u0149\u0003\u0118\u008c\u0000\u0149\u014a\u0005\u0000\u0000"+
		"\u0001\u014a\u0001\u0001\u0000\u0000\u0000\u014b\u0150\u0003\u0118\u008c"+
		"\u0000\u014c\u0151\u0003\u0004\u0002\u0000\u014d\u0151\u0003\u0006\u0003"+
		"\u0000\u014e\u0151\u0003\u000e\u0007\u0000\u014f\u0151\u0003\u0012\t\u0000"+
		"\u0150\u014c\u0001\u0000\u0000\u0000\u0150\u014d\u0001\u0000\u0000\u0000"+
		"\u0150\u014e\u0001\u0000\u0000\u0000\u0150\u014f\u0001\u0000\u0000\u0000"+
		"\u0151\u0152\u0001\u0000\u0000\u0000\u0152\u0153\u0003\u0118\u008c\u0000"+
		"\u0153\u0003\u0001\u0000\u0000\u0000\u0154\u0155\u0003\u0012\t\u0000\u0155"+
		"\u0156\u0003\u0118\u008c\u0000\u0156\u0157\u0005\u001f\u0000\u0000\u0157"+
		"\u0158\u0003\u0118\u008c\u0000\u0158\u0159\u0003D\"\u0000\u0159\u0005"+
		"\u0001\u0000\u0000\u0000\u015a\u015e\u0003\b\u0004\u0000\u015b\u015e\u0003"+
		"\n\u0005\u0000\u015c\u015e\u0003\f\u0006\u0000\u015d\u015a\u0001\u0000"+
		"\u0000\u0000\u015d\u015b\u0001\u0000\u0000\u0000\u015d\u015c\u0001\u0000"+
		"\u0000\u0000\u015e\u0007\u0001\u0000\u0000\u0000\u015f\u0165\u0003\u0012"+
		"\t\u0000\u0160\u0161\u0003\u0118\u008c\u0000\u0161\u0162\u0003>\u001f"+
		"\u0000\u0162\u0163\u0003\u0118\u008c\u0000\u0163\u0164\u0003\u0012\t\u0000"+
		"\u0164\u0166\u0001\u0000\u0000\u0000\u0165\u0160\u0001\u0000\u0000\u0000"+
		"\u0166\u0167\u0001\u0000\u0000\u0000\u0167\u0165\u0001\u0000\u0000\u0000"+
		"\u0167\u0168\u0001\u0000\u0000\u0000\u0168\t\u0001\u0000\u0000\u0000\u0169"+
		"\u016f\u0003\u0012\t\u0000\u016a\u016b\u0003\u0118\u008c\u0000\u016b\u016c"+
		"\u0003@ \u0000\u016c\u016d\u0003\u0118\u008c\u0000\u016d\u016e\u0003\u0012"+
		"\t\u0000\u016e\u0170\u0001\u0000\u0000\u0000\u016f\u016a\u0001\u0000\u0000"+
		"\u0000\u0170\u0171\u0001\u0000\u0000\u0000\u0171\u016f\u0001\u0000\u0000"+
		"\u0000\u0171\u0172\u0001\u0000\u0000\u0000\u0172\u000b\u0001\u0000\u0000"+
		"\u0000\u0173\u0174\u0003\u0012\t\u0000\u0174\u0175\u0003\u0118\u008c\u0000"+
		"\u0175\u0176\u0003B!\u0000\u0176\u0177\u0003\u0118\u008c\u0000\u0177\u0178"+
		"\u0003\u0012\t\u0000\u0178\r\u0001\u0000\u0000\u0000\u0179\u017d\u0003"+
		"\u0012\t\u0000\u017a\u017b\u0003\u0118\u008c\u0000\u017b\u017c\u0003\u0010"+
		"\b\u0000\u017c\u017e\u0001\u0000\u0000\u0000\u017d\u017a\u0001\u0000\u0000"+
		"\u0000\u017e\u017f\u0001\u0000\u0000\u0000\u017f\u017d\u0001\u0000\u0000"+
		"\u0000\u017f\u0180\u0001\u0000\u0000\u0000\u0180\u000f\u0001\u0000\u0000"+
		"\u0000\u0181\u0182\u0003\u0016\u000b\u0000\u0182\u0183\u0003\u0118\u008c"+
		"\u0000\u0183\u0184\u0003d2\u0000\u0184\u0011\u0001\u0000\u0000\u0000\u0185"+
		"\u0186\u0003,\u0016\u0000\u0186\u0187\u0003\u0118\u008c\u0000\u0187\u0189"+
		"\u0001\u0000\u0000\u0000\u0188\u0185\u0001\u0000\u0000\u0000\u0188\u0189"+
		"\u0001\u0000\u0000\u0000\u0189\u01a9\u0001\u0000\u0000\u0000\u018a\u018b"+
		"\u0003\u0018\f\u0000\u018b\u018c\u0003\u0118\u008c\u0000\u018c\u018e\u0001"+
		"\u0000\u0000\u0000\u018d\u018a\u0001\u0000\u0000\u0000\u018d\u018e\u0001"+
		"\u0000\u0000\u0000\u018e\u0196\u0001\u0000\u0000\u0000\u018f\u0197\u0003"+
		"\u0014\n\u0000\u0190\u0191\u0005\r\u0000\u0000\u0191\u0192\u0003\u0118"+
		"\u008c\u0000\u0192\u0193\u0003\u0002\u0001\u0000\u0193\u0194\u0003\u0118"+
		"\u008c\u0000\u0194\u0195\u0005\u000e\u0000\u0000\u0195\u0197\u0001\u0000"+
		"\u0000\u0000\u0196\u018f\u0001\u0000\u0000\u0000\u0196\u0190\u0001\u0000"+
		"\u0000\u0000\u0197\u019d\u0001\u0000\u0000\u0000\u0198\u0199\u0003\u0118"+
		"\u008c\u0000\u0199\u019a\u0003\u00f2y\u0000\u019a\u019c\u0001\u0000\u0000"+
		"\u0000\u019b\u0198\u0001\u0000\u0000\u0000\u019c\u019f\u0001\u0000\u0000"+
		"\u0000\u019d\u019b\u0001\u0000\u0000\u0000\u019d\u019e\u0001\u0000\u0000"+
		"\u0000\u019e\u01aa\u0001\u0000\u0000\u0000\u019f\u019d\u0001\u0000\u0000"+
		"\u0000\u01a0\u01a8\u0003\u0014\n\u0000\u01a1\u01a2\u0005\r\u0000\u0000"+
		"\u01a2\u01a3\u0003\u0118\u008c\u0000\u01a3\u01a4\u0003\u0002\u0001\u0000"+
		"\u01a4\u01a5\u0003\u0118\u008c\u0000\u01a5\u01a6\u0005\u000e\u0000\u0000"+
		"\u01a6\u01a8\u0001\u0000\u0000\u0000\u01a7\u01a0\u0001\u0000\u0000\u0000"+
		"\u01a7\u01a1\u0001\u0000\u0000\u0000\u01a8\u01aa\u0001\u0000\u0000\u0000"+
		"\u01a9\u018d\u0001\u0000\u0000\u0000\u01a9\u01a7\u0001\u0000\u0000\u0000"+
		"\u01aa\u01b2\u0001\u0000\u0000\u0000\u01ab\u01ae\u0003\u0118\u008c\u0000"+
		"\u01ac\u01af\u0003p8\u0000\u01ad\u01af\u0003\u00c0`\u0000\u01ae\u01ac"+
		"\u0001\u0000\u0000\u0000\u01ae\u01ad\u0001\u0000\u0000\u0000\u01af\u01b1"+
		"\u0001\u0000\u0000\u0000\u01b0\u01ab\u0001\u0000\u0000\u0000\u01b1\u01b4"+
		"\u0001\u0000\u0000\u0000\u01b2\u01b0\u0001\u0000\u0000\u0000\u01b2\u01b3"+
		"\u0001\u0000\u0000\u0000\u01b3\u01b8\u0001\u0000\u0000\u0000\u01b4\u01b2"+
		"\u0001\u0000\u0000\u0000\u01b5\u01b6\u0003\u0118\u008c\u0000\u01b6\u01b7"+
		"\u0003\u00f8|\u0000\u01b7\u01b9\u0001\u0000\u0000\u0000\u01b8\u01b5\u0001"+
		"\u0000\u0000\u0000\u01b8\u01b9\u0001\u0000\u0000\u0000\u01b9\u0013\u0001"+
		"\u0000\u0000\u0000\u01ba\u01bd\u0003\"\u0011\u0000\u01bb\u01bd\u0003*"+
		"\u0015\u0000\u01bc\u01ba\u0001\u0000\u0000\u0000\u01bc\u01bb\u0001\u0000"+
		"\u0000\u0000\u01bd\u0015\u0001\u0000\u0000\u0000\u01be\u01bf\u0005\u0013"+
		"\u0000\u0000\u01bf\u0017\u0001\u0000\u0000\u0000\u01c0\u01cb\u0005C\u0000"+
		"\u0000\u01c1\u01c2\u0003\u0118\u008c\u0000\u01c2\u01c3\u0005@\u0000\u0000"+
		"\u01c3\u01c6\u0003\u0118\u008c\u0000\u01c4\u01c7\u0003\u001a\r\u0000\u01c5"+
		"\u01c7\u0003*\u0015\u0000\u01c6\u01c4\u0001\u0000\u0000\u0000\u01c6\u01c5"+
		"\u0001\u0000\u0000\u0000\u01c7\u01c8\u0001\u0000\u0000\u0000\u01c8\u01c9"+
		"\u0003\u0118\u008c\u0000\u01c9\u01ca\u0005B\u0000\u0000\u01ca\u01cc\u0001"+
		"\u0000\u0000\u0000\u01cb\u01c1\u0001\u0000\u0000\u0000\u01cb\u01cc\u0001"+
		"\u0000\u0000\u0000\u01cc\u0019\u0001\u0000\u0000\u0000\u01cd\u01d5\u0003"+
		"\u001c\u000e\u0000\u01ce\u01cf\u0003\u0118\u008c\u0000\u01cf\u01d0\u0005"+
		"\u0011\u0000\u0000\u01d0\u01d1\u0003\u0118\u008c\u0000\u01d1\u01d2\u0003"+
		"\u001c\u000e\u0000\u01d2\u01d4\u0001\u0000\u0000\u0000\u01d3\u01ce\u0001"+
		"\u0000\u0000\u0000\u01d4\u01d7\u0001\u0000\u0000\u0000\u01d5\u01d3\u0001"+
		"\u0000\u0000\u0000\u01d5\u01d6\u0001\u0000\u0000\u0000\u01d6\u001b\u0001"+
		"\u0000\u0000\u0000\u01d7\u01d5\u0001\u0000\u0000\u0000\u01d8\u01db\u0003"+
		"\u001e\u000f\u0000\u01d9\u01db\u0003 \u0010\u0000\u01da\u01d8\u0001\u0000"+
		"\u0000\u0000\u01da\u01d9\u0001\u0000\u0000\u0000\u01db\u001d\u0001\u0000"+
		"\u0000\u0000\u01dc\u01de\u0003\u0142\u00a1\u0000\u01dd\u01dc\u0001\u0000"+
		"\u0000\u0000\u01de\u01df\u0001\u0000\u0000\u0000\u01df\u01dd\u0001\u0000"+
		"\u0000\u0000\u01df\u01e0\u0001\u0000\u0000\u0000\u01e0\u001f\u0001\u0000"+
		"\u0000\u0000\u01e1\u01e2\u0003\"\u0011\u0000\u01e2!\u0001\u0000\u0000"+
		"\u0000\u01e3\u01eb\u0003&\u0013\u0000\u01e4\u01e5\u0003\u0118\u008c\u0000"+
		"\u01e5\u01e6\u0005a\u0000\u0000\u01e6\u01e7\u0003\u0118\u008c\u0000\u01e7"+
		"\u01e8\u0003(\u0014\u0000\u01e8\u01e9\u0003\u0118\u008c\u0000\u01e9\u01ea"+
		"\u0005a\u0000\u0000\u01ea\u01ec\u0001\u0000\u0000\u0000\u01eb\u01e4\u0001"+
		"\u0000\u0000\u0000\u01eb\u01ec\u0001\u0000\u0000\u0000\u01ec#\u0001\u0000"+
		"\u0000\u0000\u01ed\u01ee\u0005\r\u0000\u0000\u01ee\u01ef\u0003\u0118\u008c"+
		"\u0000\u01ef\u01f3\u0003\"\u0011\u0000\u01f0\u01f1\u0003\u011a\u008d\u0000"+
		"\u01f1\u01f2\u0003\"\u0011\u0000\u01f2\u01f4\u0001\u0000\u0000\u0000\u01f3"+
		"\u01f0\u0001\u0000\u0000\u0000\u01f4\u01f5\u0001\u0000\u0000\u0000\u01f5"+
		"\u01f3\u0001\u0000\u0000\u0000\u01f5\u01f6\u0001\u0000\u0000\u0000\u01f6"+
		"\u01f7\u0001\u0000\u0000\u0000\u01f7\u01f8\u0003\u0118\u008c\u0000\u01f8"+
		"\u01f9\u0005\u000e\u0000\u0000\u01f9%\u0001\u0000\u0000\u0000\u01fa\u01fb"+
		"\u0003\u0116\u008b\u0000\u01fb\'\u0001\u0000\u0000\u0000\u01fc\u01fe\u0003"+
		"\u0138\u009c\u0000\u01fd\u01fc\u0001\u0000\u0000\u0000\u01fe\u01ff\u0001"+
		"\u0000\u0000\u0000\u01ff\u01fd\u0001\u0000\u0000\u0000\u01ff\u0200\u0001"+
		"\u0000\u0000\u0000\u0200\u020d\u0001\u0000\u0000\u0000\u0201\u0203\u0003"+
		"\u0124\u0092\u0000\u0202\u0201\u0001\u0000\u0000\u0000\u0203\u0204\u0001"+
		"\u0000\u0000\u0000\u0204\u0202\u0001\u0000\u0000\u0000\u0204\u0205\u0001"+
		"\u0000\u0000\u0000\u0205\u0207\u0001\u0000\u0000\u0000\u0206\u0208\u0003"+
		"\u0138\u009c\u0000\u0207\u0206\u0001\u0000\u0000\u0000\u0208\u0209\u0001"+
		"\u0000\u0000\u0000\u0209\u0207\u0001\u0000\u0000\u0000\u0209\u020a\u0001"+
		"\u0000\u0000\u0000\u020a\u020c\u0001\u0000\u0000\u0000\u020b\u0202\u0001"+
		"\u0000\u0000\u0000\u020c\u020f\u0001\u0000\u0000\u0000\u020d\u020b\u0001"+
		"\u0000\u0000\u0000\u020d\u020e\u0001\u0000\u0000\u0000\u020e)\u0001\u0000"+
		"\u0000\u0000\u020f\u020d\u0001\u0000\u0000\u0000\u0210\u0211\u0005\u000f"+
		"\u0000\u0000\u0211+\u0001\u0000\u0000\u0000\u0212\u021b\u00032\u0019\u0000"+
		"\u0213\u021b\u00034\u001a\u0000\u0214\u021b\u00030\u0018\u0000\u0215\u021b"+
		"\u0003.\u0017\u0000\u0216\u021b\u0003:\u001d\u0000\u0217\u021b\u0003<"+
		"\u001e\u0000\u0218\u021b\u00038\u001c\u0000\u0219\u021b\u00036\u001b\u0000"+
		"\u021a\u0212\u0001\u0000\u0000\u0000\u021a\u0213\u0001\u0000\u0000\u0000"+
		"\u021a\u0214\u0001\u0000\u0000\u0000\u021a\u0215\u0001\u0000\u0000\u0000"+
		"\u021a\u0216\u0001\u0000\u0000\u0000\u021a\u0217\u0001\u0000\u0000\u0000"+
		"\u021a\u0218\u0001\u0000\u0000\u0000\u021a\u0219\u0001\u0000\u0000\u0000"+
		"\u021b-\u0001\u0000\u0000\u0000\u021c\u021d\u0005!\u0000\u0000\u021d/"+
		"\u0001\u0000\u0000\u0000\u021e\u021f\u0005!\u0000\u0000\u021f\u0220\u0005"+
		"!\u0000\u0000\u02201\u0001\u0000\u0000\u0000\u0221\u0222\u0005!\u0000"+
		"\u0000\u0222\u0223\u0005\u0006\u0000\u0000\u02233\u0001\u0000\u0000\u0000"+
		"\u0224\u0225\u0005!\u0000\u0000\u0225\u0226\u0005!\u0000\u0000\u0226\u0227"+
		"\u0005\u0006\u0000\u0000\u02275\u0001\u0000\u0000\u0000\u0228\u0229\u0005"+
		"#\u0000\u0000\u02297\u0001\u0000\u0000\u0000\u022a\u022b\u0005#\u0000"+
		"\u0000\u022b\u022c\u0005#\u0000\u0000\u022c9\u0001\u0000\u0000\u0000\u022d"+
		"\u022e\u0005#\u0000\u0000\u022e\u022f\u0005\u0006\u0000\u0000\u022f;\u0001"+
		"\u0000\u0000\u0000\u0230\u0231\u0005#\u0000\u0000\u0231\u0232\u0005#\u0000"+
		"\u0000\u0232\u0233\u0005\u0006\u0000\u0000\u0233=\u0001\u0000\u0000\u0000"+
		"\u0234\u0237\u0007\u0000\u0000\u0000\u0235\u0237\u0007\u0000\u0000\u0000"+
		"\u0236\u0234\u0001\u0000\u0000\u0000\u0236\u0235\u0001\u0000\u0000\u0000"+
		"\u0237\u023a\u0001\u0000\u0000\u0000\u0238\u023b\u0007\u0001\u0000\u0000"+
		"\u0239\u023b\u0007\u0001\u0000\u0000\u023a\u0238\u0001\u0000\u0000\u0000"+
		"\u023a\u0239\u0001\u0000\u0000\u0000\u023b\u023e\u0001\u0000\u0000\u0000"+
		"\u023c\u023f\u0007\u0002\u0000\u0000\u023d\u023f\u0007\u0002\u0000\u0000"+
		"\u023e\u023c\u0001\u0000\u0000\u0000\u023e\u023d\u0001\u0000\u0000\u0000"+
		"\u023f\u0240\u0001\u0000\u0000\u0000\u0240\u0243\u0003\u011a\u008d\u0000"+
		"\u0241\u0243\u0005\u0011\u0000\u0000\u0242\u0236\u0001\u0000\u0000\u0000"+
		"\u0242\u0241\u0001\u0000\u0000\u0000\u0243?\u0001\u0000\u0000\u0000\u0244"+
		"\u0247\u0007\u0003\u0000\u0000\u0245\u0247\u0007\u0003\u0000\u0000\u0246"+
		"\u0244\u0001\u0000\u0000\u0000\u0246\u0245\u0001\u0000\u0000\u0000\u0247"+
		"\u024a\u0001\u0000\u0000\u0000\u0248\u024b\u0007\u0004\u0000\u0000\u0249"+
		"\u024b\u0007\u0004\u0000\u0000\u024a\u0248\u0001\u0000\u0000\u0000\u024a"+
		"\u0249\u0001\u0000\u0000\u0000\u024b\u024c\u0001\u0000\u0000\u0000\u024c"+
		"\u024d\u0003\u011a\u008d\u0000\u024dA\u0001\u0000\u0000\u0000\u024e\u0251"+
		"\u0007\u0005\u0000\u0000\u024f\u0251\u0007\u0005\u0000\u0000\u0250\u024e"+
		"\u0001\u0000\u0000\u0000\u0250\u024f\u0001\u0000\u0000\u0000\u0251\u0254"+
		"\u0001\u0000\u0000\u0000\u0252\u0255\u0007\u0006\u0000\u0000\u0253\u0255"+
		"\u0007\u0006\u0000\u0000\u0254\u0252\u0001\u0000\u0000\u0000\u0254\u0253"+
		"\u0001\u0000\u0000\u0000\u0255\u0258\u0001\u0000\u0000\u0000\u0256\u0259"+
		"\u0007\u0001\u0000\u0000\u0257\u0259\u0007\u0001\u0000\u0000\u0258\u0256"+
		"\u0001\u0000\u0000\u0000\u0258\u0257\u0001\u0000\u0000\u0000\u0259\u025c"+
		"\u0001\u0000\u0000\u0000\u025a\u025d\u0007\u0007\u0000\u0000\u025b\u025d"+
		"\u0007\u0007\u0000\u0000\u025c\u025a\u0001\u0000\u0000\u0000\u025c\u025b"+
		"\u0001\u0000\u0000\u0000\u025d\u0260\u0001\u0000\u0000\u0000\u025e\u0261"+
		"\u0007\b\u0000\u0000\u025f\u0261\u0007\b\u0000\u0000\u0260\u025e\u0001"+
		"\u0000\u0000\u0000\u0260\u025f\u0001\u0000\u0000\u0000\u0261\u0262\u0001"+
		"\u0000\u0000\u0000\u0262\u0263\u0003\u011a\u008d\u0000\u0263C\u0001\u0000"+
		"\u0000\u0000\u0264\u0268\u0003J%\u0000\u0265\u0268\u0003F#\u0000\u0266"+
		"\u0268\u0003H$\u0000\u0267\u0264\u0001\u0000\u0000\u0000\u0267\u0265\u0001"+
		"\u0000\u0000\u0000\u0267\u0266\u0001\u0000\u0000\u0000\u0268E\u0001\u0000"+
		"\u0000\u0000\u0269\u026f\u0003J%\u0000\u026a\u026b\u0003\u0118\u008c\u0000"+
		"\u026b\u026c\u0003>\u001f\u0000\u026c\u026d\u0003\u0118\u008c\u0000\u026d"+
		"\u026e\u0003J%\u0000\u026e\u0270\u0001\u0000\u0000\u0000\u026f\u026a\u0001"+
		"\u0000\u0000\u0000\u0270\u0271\u0001\u0000\u0000\u0000\u0271\u026f\u0001"+
		"\u0000\u0000\u0000\u0271\u0272\u0001\u0000\u0000\u0000\u0272G\u0001\u0000"+
		"\u0000\u0000\u0273\u0279\u0003J%\u0000\u0274\u0275\u0003\u0118\u008c\u0000"+
		"\u0275\u0276\u0003@ \u0000\u0276\u0277\u0003\u0118\u008c\u0000\u0277\u0278"+
		"\u0003J%\u0000\u0278\u027a\u0001\u0000\u0000\u0000\u0279\u0274\u0001\u0000"+
		"\u0000\u0000\u027a\u027b\u0001\u0000\u0000\u0000\u027b\u0279\u0001\u0000"+
		"\u0000\u0000\u027b\u027c\u0001\u0000\u0000\u0000\u027cI\u0001\u0000\u0000"+
		"\u0000\u027d\u0286\u0003L&\u0000\u027e\u0286\u0003T*\u0000\u027f\u0280"+
		"\u0005\r\u0000\u0000\u0280\u0281\u0003\u0118\u008c\u0000\u0281\u0282\u0003"+
		"D\"\u0000\u0282\u0283\u0003\u0118\u008c\u0000\u0283\u0284\u0005\u000e"+
		"\u0000\u0000\u0284\u0286\u0001\u0000\u0000\u0000\u0285\u027d\u0001\u0000"+
		"\u0000\u0000\u0285\u027e\u0001\u0000\u0000\u0000\u0285\u027f\u0001\u0000"+
		"\u0000\u0000\u0286K\u0001\u0000\u0000\u0000\u0287\u028b\u0003R)\u0000"+
		"\u0288\u028b\u0003N\'\u0000\u0289\u028b\u0003P(\u0000\u028a\u0287\u0001"+
		"\u0000\u0000\u0000\u028a\u0288\u0001\u0000\u0000\u0000\u028a\u0289\u0001"+
		"\u0000\u0000\u0000\u028a\u028b\u0001\u0000\u0000\u0000\u028bM\u0001\u0000"+
		"\u0000\u0000\u028c\u0292\u0003R)\u0000\u028d\u028e\u0003\u0118\u008c\u0000"+
		"\u028e\u028f\u0003>\u001f\u0000\u028f\u0290\u0003\u0118\u008c\u0000\u0290"+
		"\u0291\u0003R)\u0000\u0291\u0293\u0001\u0000\u0000\u0000\u0292\u028d\u0001"+
		"\u0000\u0000\u0000\u0293\u0294\u0001\u0000\u0000\u0000\u0294\u0292\u0001"+
		"\u0000\u0000\u0000\u0294\u0295\u0001\u0000\u0000\u0000\u0295O\u0001\u0000"+
		"\u0000\u0000\u0296\u029c\u0003R)\u0000\u0297\u0298\u0003\u0118\u008c\u0000"+
		"\u0298\u0299\u0003@ \u0000\u0299\u029a\u0003\u0118\u008c\u0000\u029a\u029b"+
		"\u0003R)\u0000\u029b\u029d\u0001\u0000\u0000\u0000\u029c\u0297\u0001\u0000"+
		"\u0000\u0000\u029d\u029e\u0001\u0000\u0000\u0000\u029e\u029c\u0001\u0000"+
		"\u0000\u0000\u029e\u029f\u0001\u0000\u0000\u0000\u029fQ\u0001\u0000\u0000"+
		"\u0000\u02a0\u02a8\u0003V+\u0000\u02a1\u02a2\u0005\r\u0000\u0000\u02a2"+
		"\u02a3\u0003\u0118\u008c\u0000\u02a3\u02a4\u0003L&\u0000\u02a4\u02a5\u0003"+
		"\u0118\u008c\u0000\u02a5\u02a6\u0005\u000e\u0000\u0000\u02a6\u02a8\u0001"+
		"\u0000\u0000\u0000\u02a7\u02a0\u0001\u0000\u0000\u0000\u02a7\u02a1\u0001"+
		"\u0000\u0000\u0000\u02a8S\u0001\u0000\u0000\u0000\u02a9\u02aa\u0005@\u0000"+
		"\u0000\u02aa\u02ab\u0003X,\u0000\u02ab\u02ac\u0005B\u0000\u0000\u02ac"+
		"\u02ad\u0003\u0118\u008c\u0000\u02ad\u02af\u0001\u0000\u0000\u0000\u02ae"+
		"\u02a9\u0001\u0000\u0000\u0000\u02ae\u02af\u0001\u0000\u0000\u0000\u02af"+
		"\u02b0\u0001\u0000\u0000\u0000\u02b0\u02b1\u0005`\u0000\u0000\u02b1\u02b2"+
		"\u0003\u0118\u008c\u0000\u02b2\u02b3\u0003L&\u0000\u02b3\u02b4\u0003\u0118"+
		"\u008c\u0000\u02b4\u02b5\u0005b\u0000\u0000\u02b5U\u0001\u0000\u0000\u0000"+
		"\u02b6\u02b7\u0005@\u0000\u0000\u02b7\u02b8\u0003X,\u0000\u02b8\u02b9"+
		"\u0005B\u0000\u0000\u02b9\u02ba\u0003\u0118\u008c\u0000\u02ba\u02bc\u0001"+
		"\u0000\u0000\u0000\u02bb\u02b6\u0001\u0000\u0000\u0000\u02bb\u02bc\u0001"+
		"\u0000\u0000\u0000\u02bc\u02c0\u0001\u0000\u0000\u0000\u02bd\u02be\u0003"+
		"b1\u0000\u02be\u02bf\u0003\u0118\u008c\u0000\u02bf\u02c1\u0001\u0000\u0000"+
		"\u0000\u02c0\u02bd\u0001\u0000\u0000\u0000\u02c0\u02c1\u0001\u0000\u0000"+
		"\u0000\u02c1\u02c2\u0001\u0000\u0000\u0000\u02c2\u02c3\u0003d2\u0000\u02c3"+
		"\u02d7\u0003\u0118\u008c\u0000\u02c4\u02c5\u0003f3\u0000\u02c5\u02c6\u0003"+
		"\u0118\u008c\u0000\u02c6\u02c7\u0003\u0012\t\u0000\u02c7\u02d8\u0001\u0000"+
		"\u0000\u0000\u02c8\u02c9\u0003h4\u0000\u02c9\u02ca\u0003\u0118\u008c\u0000"+
		"\u02ca\u02cb\u0005\b\u0000\u0000\u02cb\u02cc\u0003\u0106\u0083\u0000\u02cc"+
		"\u02d8\u0001\u0000\u0000\u0000\u02cd\u02ce\u0003l6\u0000\u02ce\u02d1\u0003"+
		"\u0118\u008c\u0000\u02cf\u02d2\u0003x<\u0000\u02d0\u02d2\u0003z=\u0000"+
		"\u02d1\u02cf\u0001\u0000\u0000\u0000\u02d1\u02d0\u0001\u0000\u0000\u0000"+
		"\u02d2\u02d8\u0001\u0000\u0000\u0000\u02d3\u02d4\u0003n7\u0000\u02d4\u02d5"+
		"\u0003\u0118\u008c\u0000\u02d5\u02d6\u0003\u010e\u0087\u0000\u02d6\u02d8"+
		"\u0001\u0000\u0000\u0000\u02d7\u02c4\u0001\u0000\u0000\u0000\u02d7\u02c8"+
		"\u0001\u0000\u0000\u0000\u02d7\u02cd\u0001\u0000\u0000\u0000\u02d7\u02d3"+
		"\u0001\u0000\u0000\u0000\u02d8W\u0001\u0000\u0000\u0000\u02d9\u02da\u0003"+
		"Z-\u0000\u02da\u02db\u0003\\.\u0000\u02db\u02dc\u0003^/\u0000\u02dcY\u0001"+
		"\u0000\u0000\u0000\u02dd\u02de\u0003\u0114\u008a\u0000\u02de[\u0001\u0000"+
		"\u0000\u0000\u02df\u02e0\u0005\u0013\u0000\u0000\u02e0\u02e1\u0005\u0013"+
		"\u0000\u0000\u02e1]\u0001\u0000\u0000\u0000\u02e2\u02e5\u0003\u0114\u008a"+
		"\u0000\u02e3\u02e5\u0003`0\u0000\u02e4\u02e2\u0001\u0000\u0000\u0000\u02e4"+
		"\u02e3\u0001\u0000\u0000\u0000\u02e5_\u0001\u0000\u0000\u0000\u02e6\u02e7"+
		"\u0005\u000f\u0000\u0000\u02e7a\u0001\u0000\u0000\u0000\u02e8\u02e9\u0007"+
		"\u0004\u0000\u0000\u02e9c\u0001\u0000\u0000\u0000\u02ea\u02eb\u0003\u0012"+
		"\t\u0000\u02ebe\u0001\u0000\u0000\u0000\u02ec\u02f0\u0005\"\u0000\u0000"+
		"\u02ed\u02ee\u0005\u0006\u0000\u0000\u02ee\u02f0\u0005\"\u0000\u0000\u02ef"+
		"\u02ec\u0001\u0000\u0000\u0000\u02ef\u02ed\u0001\u0000\u0000\u0000\u02f0"+
		"g\u0001\u0000\u0000\u0000\u02f1\u02fb\u0005\"\u0000\u0000\u02f2\u02f3"+
		"\u0005\u0006\u0000\u0000\u02f3\u02fb\u0005\"\u0000\u0000\u02f4\u02f5\u0005"+
		"!\u0000\u0000\u02f5\u02fb\u0005\"\u0000\u0000\u02f6\u02fb\u0005!\u0000"+
		"\u0000\u02f7\u02f8\u0005#\u0000\u0000\u02f8\u02fb\u0005\"\u0000\u0000"+
		"\u02f9\u02fb\u0005#\u0000\u0000\u02fa\u02f1\u0001\u0000\u0000\u0000\u02fa"+
		"\u02f2\u0001\u0000\u0000\u0000\u02fa\u02f4\u0001\u0000\u0000\u0000\u02fa"+
		"\u02f6\u0001\u0000\u0000\u0000\u02fa\u02f7\u0001\u0000\u0000\u0000\u02fa"+
		"\u02f9\u0001\u0000\u0000\u0000\u02fbi\u0001\u0000\u0000\u0000\u02fc\u0306"+
		"\u0005\"\u0000\u0000\u02fd\u02fe\u0005\u0006\u0000\u0000\u02fe\u0306\u0005"+
		"\"\u0000\u0000\u02ff\u0300\u0005!\u0000\u0000\u0300\u0306\u0005\"\u0000"+
		"\u0000\u0301\u0306\u0005!\u0000\u0000\u0302\u0303\u0005#\u0000\u0000\u0303"+
		"\u0306\u0005\"\u0000\u0000\u0304\u0306\u0005#\u0000\u0000\u0305\u02fc"+
		"\u0001\u0000\u0000\u0000\u0305\u02fd\u0001\u0000\u0000\u0000\u0305\u02ff"+
		"\u0001\u0000\u0000\u0000\u0305\u0301\u0001\u0000\u0000\u0000\u0305\u0302"+
		"\u0001\u0000\u0000\u0000\u0305\u0304\u0001\u0000\u0000\u0000\u0306k\u0001"+
		"\u0000\u0000\u0000\u0307\u030b\u0005\"\u0000\u0000\u0308\u0309\u0005\u0006"+
		"\u0000\u0000\u0309\u030b\u0005\"\u0000\u0000\u030a\u0307\u0001\u0000\u0000"+
		"\u0000\u030a\u0308\u0001\u0000\u0000\u0000\u030bm\u0001\u0000\u0000\u0000"+
		"\u030c\u0310\u0005\"\u0000\u0000\u030d\u030e\u0005\u0006\u0000\u0000\u030e"+
		"\u0310\u0005\"\u0000\u0000\u030f\u030c\u0001\u0000\u0000\u0000\u030f\u030d"+
		"\u0001\u0000\u0000\u0000\u0310o\u0001\u0000\u0000\u0000\u0311\u0312\u0005"+
		"`\u0000\u0000\u0312\u0313\u0005`\u0000\u0000\u0313\u0314\u0001\u0000\u0000"+
		"\u0000\u0314\u0317\u0003\u0118\u008c\u0000\u0315\u0318\u0007\u0002\u0000"+
		"\u0000\u0316\u0318\u0007\u0002\u0000\u0000\u0317\u0315\u0001\u0000\u0000"+
		"\u0000\u0317\u0316\u0001\u0000\u0000\u0000\u0317\u0318\u0001\u0000\u0000"+
		"\u0000\u0318\u0319\u0001\u0000\u0000\u0000\u0319\u031a\u0003\u0118\u008c"+
		"\u0000\u031a\u0322\u0003r9\u0000\u031b\u031c\u0003\u0118\u008c\u0000\u031c"+
		"\u031d\u0005\u0011\u0000\u0000\u031d\u031e\u0003\u0118\u008c\u0000\u031e"+
		"\u031f\u0003r9\u0000\u031f\u0321\u0001\u0000\u0000\u0000\u0320\u031b\u0001"+
		"\u0000\u0000\u0000\u0321\u0324\u0001\u0000\u0000\u0000\u0322\u0320\u0001"+
		"\u0000\u0000\u0000\u0322\u0323\u0001\u0000\u0000\u0000\u0323\u0325\u0001"+
		"\u0000\u0000\u0000\u0324\u0322\u0001\u0000\u0000\u0000\u0325\u0326\u0003"+
		"\u0118\u008c\u0000\u0326\u0327\u0005b\u0000\u0000\u0327\u0328\u0005b\u0000"+
		"\u0000\u0328q\u0001\u0000\u0000\u0000\u0329\u0331\u0003t:\u0000\u032a"+
		"\u0331\u0003\u0088D\u0000\u032b\u0331\u0003\u0090H\u0000\u032c\u0331\u0003"+
		"\u00a4R\u0000\u032d\u0331\u0003\u00d6k\u0000\u032e\u0331\u0003\u00dam"+
		"\u0000\u032f\u0331\u0003\u00e8t\u0000\u0330\u0329\u0001\u0000\u0000\u0000"+
		"\u0330\u032a\u0001\u0000\u0000\u0000\u0330\u032b\u0001\u0000\u0000\u0000"+
		"\u0330\u032c\u0001\u0000\u0000\u0000\u0330\u032d\u0001\u0000\u0000\u0000"+
		"\u0330\u032e\u0001\u0000\u0000\u0000\u0330\u032f\u0001\u0000\u0000\u0000"+
		"\u0331s\u0001\u0000\u0000\u0000\u0332\u0333\u0003v;\u0000\u0333\u0334"+
		"\u0003\u0118\u008c\u0000\u0334\u0335\u0003l6\u0000\u0335\u0338\u0003\u0118"+
		"\u008c\u0000\u0336\u0339\u0003x<\u0000\u0337\u0339\u0003z=\u0000\u0338"+
		"\u0336\u0001\u0000\u0000\u0000\u0338\u0337\u0001\u0000\u0000\u0000\u0339"+
		"u\u0001\u0000\u0000\u0000\u033a\u033d\u0007\t\u0000\u0000\u033b\u033d"+
		"\u0007\t\u0000\u0000\u033c\u033a\u0001\u0000\u0000\u0000\u033c\u033b\u0001"+
		"\u0000\u0000\u0000\u033d\u0340\u0001\u0000\u0000\u0000\u033e\u0341\u0007"+
		"\n\u0000\u0000\u033f\u0341\u0007\n\u0000\u0000\u0340\u033e\u0001\u0000"+
		"\u0000\u0000\u0340\u033f\u0001\u0000\u0000\u0000\u0341\u0344\u0001\u0000"+
		"\u0000\u0000\u0342\u0345\u0007\u0004\u0000\u0000\u0343\u0345\u0007\u0004"+
		"\u0000\u0000\u0344\u0342\u0001\u0000\u0000\u0000\u0344\u0343\u0001\u0000"+
		"\u0000\u0000\u0345\u0348\u0001\u0000\u0000\u0000\u0346\u0349\u0007\u0005"+
		"\u0000\u0000\u0347\u0349\u0007\u0005\u0000\u0000\u0348\u0346\u0001\u0000"+
		"\u0000\u0000\u0348\u0347\u0001\u0000\u0000\u0000\u0349w\u0001\u0000\u0000"+
		"\u0000\u034a\u034b\u0003~?\u0000\u034b\u034c\u0003\u0118\u008c\u0000\u034c"+
		"\u034d\u0005\u001f\u0000\u0000\u034d\u034e\u0003\u0118\u008c\u0000\u034e"+
		"\u0350\u0001\u0000\u0000\u0000\u034f\u034a\u0001\u0000\u0000\u0000\u034f"+
		"\u0350\u0001\u0000\u0000\u0000\u0350\u0351\u0001\u0000\u0000\u0000\u0351"+
		"\u0359\u0003\u0082A\u0000\u0352\u0353\u0003|>\u0000\u0353\u0354\u0003"+
		"\u0118\u008c\u0000\u0354\u0355\u0005\u001f\u0000\u0000\u0355\u0356\u0003"+
		"\u0118\u008c\u0000\u0356\u0357\u0003\u0086C\u0000\u0357\u0359\u0001\u0000"+
		"\u0000\u0000\u0358\u034f\u0001\u0000\u0000\u0000\u0358\u0352\u0001\u0000"+
		"\u0000\u0000\u0359y\u0001\u0000\u0000\u0000\u035a\u035b\u0005\r\u0000"+
		"\u0000\u035b\u035c\u0003\u0118\u008c\u0000\u035c\u0362\u0003x<\u0000\u035d"+
		"\u035e\u0003\u011a\u008d\u0000\u035e\u035f\u0003x<\u0000\u035f\u0361\u0001"+
		"\u0000\u0000\u0000\u0360\u035d\u0001\u0000\u0000\u0000\u0361\u0364\u0001"+
		"\u0000\u0000\u0000\u0362\u0360\u0001\u0000\u0000\u0000\u0362\u0363\u0001"+
		"\u0000\u0000\u0000\u0363\u0365\u0001\u0000\u0000\u0000\u0364\u0362\u0001"+
		"\u0000\u0000\u0000\u0365\u0366\u0003\u0118\u008c\u0000\u0366\u0367\u0005"+
		"\u000e\u0000\u0000\u0367{\u0001\u0000\u0000\u0000\u0368\u036b\u0007\u000b"+
		"\u0000\u0000\u0369\u036b\u0007\u000b\u0000\u0000\u036a\u0368\u0001\u0000"+
		"\u0000\u0000\u036a\u0369\u0001\u0000\u0000\u0000\u036b\u036e\u0001\u0000"+
		"\u0000\u0000\u036c\u036f\u0007\u0006\u0000\u0000\u036d\u036f\u0007\u0006"+
		"\u0000\u0000\u036e\u036c\u0001\u0000\u0000\u0000\u036e\u036d\u0001\u0000"+
		"\u0000\u0000\u036f\u0372\u0001\u0000\u0000\u0000\u0370\u0373\u0007\f\u0000"+
		"\u0000\u0371\u0373\u0007\f\u0000\u0000\u0372\u0370\u0001\u0000\u0000\u0000"+
		"\u0372\u0371\u0001\u0000\u0000\u0000\u0373\u0376\u0001\u0000\u0000\u0000"+
		"\u0374\u0377\u0007\u0002\u0000\u0000\u0375\u0377\u0007\u0002\u0000\u0000"+
		"\u0376\u0374\u0001\u0000\u0000\u0000\u0376\u0375\u0001\u0000\u0000\u0000"+
		"\u0377}\u0001\u0000\u0000\u0000\u0378\u037b\u0007\u0005\u0000\u0000\u0379"+
		"\u037b\u0007\u0005\u0000\u0000\u037a\u0378\u0001\u0000\u0000\u0000\u037a"+
		"\u0379\u0001\u0000\u0000\u0000\u037b\u037e\u0001\u0000\u0000\u0000\u037c"+
		"\u037f\u0007\u0000\u0000\u0000\u037d\u037f\u0007\u0000\u0000\u0000\u037e"+
		"\u037c\u0001\u0000\u0000\u0000\u037e\u037d\u0001\u0000\u0000\u0000\u037f"+
		"\u0382\u0001\u0000\u0000\u0000\u0380\u0383\u0007\t\u0000\u0000\u0381\u0383"+
		"\u0007\t\u0000\u0000\u0382\u0380\u0001\u0000\u0000\u0000\u0382\u0381\u0001"+
		"\u0000\u0000\u0000\u0383\u0386\u0001\u0000\u0000\u0000\u0384\u0387\u0007"+
		"\r\u0000\u0000\u0385\u0387\u0007\r\u0000\u0000\u0386\u0384\u0001\u0000"+
		"\u0000\u0000\u0386\u0385\u0001\u0000\u0000\u0000\u0387\u038a\u0001\u0000"+
		"\u0000\u0000\u0388\u038b\u0007\u000e\u0000\u0000\u0389\u038b\u0007\u000e"+
		"\u0000\u0000\u038a\u0388\u0001\u0000\u0000\u0000\u038a\u0389\u0001\u0000"+
		"\u0000\u0000\u038b\u007f\u0001\u0000\u0000\u0000\u038c\u038f\u0003\u0140"+
		"\u00a0\u0000\u038d\u038f\u0003\u013c\u009e\u0000\u038e\u038c\u0001\u0000"+
		"\u0000\u0000\u038e\u038d\u0001\u0000\u0000\u0000\u038f\u0390\u0001\u0000"+
		"\u0000\u0000\u0390\u038e\u0001\u0000\u0000\u0000\u0390\u0391\u0001\u0000"+
		"\u0000\u0000\u0391\u0081\u0001\u0000\u0000\u0000\u0392\u0393\u0003\u012c"+
		"\u0096\u0000\u0393\u0394\u0003\u0118\u008c\u0000\u0394\u039a\u0003\u0080"+
		"@\u0000\u0395\u0396\u0003\u011a\u008d\u0000\u0396\u0397\u0003\u0080@\u0000"+
		"\u0397\u0399\u0001\u0000\u0000\u0000\u0398\u0395\u0001\u0000\u0000\u0000"+
		"\u0399\u039c\u0001\u0000\u0000\u0000\u039a\u0398\u0001\u0000\u0000\u0000"+
		"\u039a\u039b\u0001\u0000\u0000\u0000\u039b\u039d\u0001\u0000\u0000\u0000"+
		"\u039c\u039a\u0001\u0000\u0000\u0000\u039d\u039e\u0003\u0118\u008c\u0000"+
		"\u039e\u039f\u0003\u012c\u0096\u0000\u039f\u0083\u0001\u0000\u0000\u0000"+
		"\u03a0\u03a3\u0003\u013a\u009d\u0000\u03a1\u03a3\u0003\u013e\u009f\u0000"+
		"\u03a2\u03a0\u0001\u0000\u0000\u0000\u03a2\u03a1\u0001\u0000\u0000\u0000"+
		"\u03a3\u03a4\u0001\u0000\u0000\u0000\u03a4\u03a2\u0001\u0000\u0000\u0000"+
		"\u03a4\u03a5\u0001\u0000\u0000\u0000\u03a5\u0085\u0001\u0000\u0000\u0000"+
		"\u03a6\u03a7\u0003\u012c\u0096\u0000\u03a7\u03a8\u0003\u0084B\u0000\u03a8"+
		"\u03a9\u0003\u012c\u0096\u0000\u03a9\u0087\u0001\u0000\u0000\u0000\u03aa"+
		"\u03ab\u0003\u008aE\u0000\u03ab\u03ac\u0003\u0118\u008c\u0000\u03ac\u03ad"+
		"\u0003n7\u0000\u03ad\u03b0\u0003\u0118\u008c\u0000\u03ae\u03b1\u0003\u008c"+
		"F\u0000\u03af\u03b1\u0003\u008eG\u0000\u03b0\u03ae\u0001\u0000\u0000\u0000"+
		"\u03b0\u03af\u0001\u0000\u0000\u0000\u03b1\u0089\u0001\u0000\u0000\u0000"+
		"\u03b2\u03b5\u0007\f\u0000\u0000\u03b3\u03b5\u0007\f\u0000\u0000\u03b4"+
		"\u03b2\u0001\u0000\u0000\u0000\u03b4\u03b3\u0001\u0000\u0000\u0000\u03b5"+
		"\u03b8\u0001\u0000\u0000\u0000\u03b6\u03b9\u0007\u0000\u0000\u0000\u03b7"+
		"\u03b9\u0007\u0000\u0000\u0000\u03b8\u03b6\u0001\u0000\u0000\u0000\u03b8"+
		"\u03b7\u0001\u0000\u0000\u0000\u03b9\u03bc\u0001\u0000\u0000\u0000\u03ba"+
		"\u03bd\u0007\u0001\u0000\u0000\u03bb\u03bd\u0007\u0001\u0000\u0000\u03bc"+
		"\u03ba\u0001\u0000\u0000\u0000\u03bc\u03bb\u0001\u0000\u0000\u0000\u03bd"+
		"\u03c0\u0001\u0000\u0000\u0000\u03be\u03c1\u0007\u000f\u0000\u0000\u03bf"+
		"\u03c1\u0007\u000f\u0000\u0000\u03c0\u03be\u0001\u0000\u0000\u0000\u03c0"+
		"\u03bf\u0001\u0000\u0000\u0000\u03c1\u03c4\u0001\u0000\u0000\u0000\u03c2"+
		"\u03c5\u0007\u0007\u0000\u0000\u03c3\u03c5\u0007\u0007\u0000\u0000\u03c4"+
		"\u03c2\u0001\u0000\u0000\u0000\u03c4\u03c3\u0001\u0000\u0000\u0000\u03c5"+
		"\u03c8\u0001\u0000\u0000\u0000\u03c6\u03c9\u0007\u0000\u0000\u0000\u03c7"+
		"\u03c9\u0007\u0000\u0000\u0000\u03c8\u03c6\u0001\u0000\u0000\u0000\u03c8"+
		"\u03c7\u0001\u0000\u0000\u0000\u03c9\u03cc\u0001\u0000\u0000\u0000\u03ca"+
		"\u03cd\u0007\u000f\u0000\u0000\u03cb\u03cd\u0007\u000f\u0000\u0000\u03cc"+
		"\u03ca\u0001\u0000\u0000\u0000\u03cc\u03cb\u0001\u0000\u0000\u0000\u03cd"+
		"\u03d0\u0001\u0000\u0000\u0000\u03ce\u03d1\u0007\n\u0000\u0000\u03cf\u03d1"+
		"\u0007\n\u0000\u0000\u03d0\u03ce\u0001\u0000\u0000\u0000\u03d0\u03cf\u0001"+
		"\u0000\u0000\u0000\u03d1\u008b\u0001\u0000\u0000\u0000\u03d2\u03d3\u0003"+
		"\u0142\u00a1\u0000\u03d3\u03d4\u0003\u0142\u00a1\u0000\u03d4\u008d\u0001"+
		"\u0000\u0000\u0000\u03d5\u03d6\u0005\r\u0000\u0000\u03d6\u03d7\u0003\u0118"+
		"\u008c\u0000\u03d7\u03dd\u0003\u008cF\u0000\u03d8\u03d9\u0003\u011a\u008d"+
		"\u0000\u03d9\u03da\u0003\u008cF\u0000\u03da\u03dc\u0001\u0000\u0000\u0000"+
		"\u03db\u03d8\u0001\u0000\u0000\u0000\u03dc\u03df\u0001\u0000\u0000\u0000"+
		"\u03dd\u03db\u0001\u0000\u0000\u0000\u03dd\u03de\u0001\u0000\u0000\u0000"+
		"\u03de\u03e0\u0001\u0000\u0000\u0000\u03df\u03dd\u0001\u0000\u0000\u0000"+
		"\u03e0\u03e1\u0003\u0118\u008c\u0000\u03e1\u03e2\u0005\u000e\u0000\u0000"+
		"\u03e2\u008f\u0001\u0000\u0000\u0000\u03e3\u03e6\u0003\u0092I\u0000\u03e4"+
		"\u03e6\u0003\u0096K\u0000\u03e5\u03e3\u0001\u0000\u0000\u0000\u03e5\u03e4"+
		"\u0001\u0000\u0000\u0000\u03e6\u0091\u0001\u0000\u0000\u0000\u03e7\u03e8"+
		"\u0003\u0094J\u0000\u03e8\u03e9\u0003\u0118\u008c\u0000\u03e9\u03ea\u0003"+
		"n7\u0000\u03ea\u03ed\u0003\u0118\u008c\u0000\u03eb\u03ee\u0003\u0012\t"+
		"\u0000\u03ec\u03ee\u0003$\u0012\u0000\u03ed\u03eb\u0001\u0000\u0000\u0000"+
		"\u03ed\u03ec\u0001\u0000\u0000\u0000\u03ee\u0093\u0001\u0000\u0000\u0000"+
		"\u03ef\u03f2\u0007\t\u0000\u0000\u03f0\u03f2\u0007\t\u0000\u0000\u03f1"+
		"\u03ef\u0001\u0000\u0000\u0000\u03f1\u03f0\u0001\u0000\u0000\u0000\u03f2"+
		"\u03f5\u0001\u0000\u0000\u0000\u03f3\u03f6\u0007\u0010\u0000\u0000\u03f4"+
		"\u03f6\u0007\u0010\u0000\u0000\u03f5\u03f3\u0001\u0000\u0000\u0000\u03f5"+
		"\u03f4\u0001\u0000\u0000\u0000\u03f6\u03f9\u0001\u0000\u0000\u0000\u03f7"+
		"\u03fa\u0007\u0011\u0000\u0000\u03f8\u03fa\u0007\u0011\u0000\u0000\u03f9"+
		"\u03f7\u0001\u0000\u0000\u0000\u03f9\u03f8\u0001\u0000\u0000\u0000\u03fa"+
		"\u03fd\u0001\u0000\u0000\u0000\u03fb\u03fe\u0007\n\u0000\u0000\u03fc\u03fe"+
		"\u0007\n\u0000\u0000\u03fd\u03fb\u0001\u0000\u0000\u0000\u03fd\u03fc\u0001"+
		"\u0000\u0000\u0000\u03fe\u0401\u0001\u0000\u0000\u0000\u03ff\u0402\u0007"+
		"\u0006\u0000\u0000\u0400\u0402\u0007\u0006\u0000\u0000\u0401\u03ff\u0001"+
		"\u0000\u0000\u0000\u0401\u0400\u0001\u0000\u0000\u0000\u0402\u0405\u0001"+
		"\u0000\u0000\u0000\u0403\u0406\u0007\u0002\u0000\u0000\u0404\u0406\u0007"+
		"\u0002\u0000\u0000\u0405\u0403\u0001\u0000\u0000\u0000\u0405\u0404\u0001"+
		"\u0000\u0000\u0000\u0406\u0095\u0001\u0000\u0000\u0000\u0407\u0408\u0003"+
		"\u0098L\u0000\u0408\u0409\u0003\u0118\u008c\u0000\u0409\u040a\u0003n7"+
		"\u0000\u040a\u040d\u0003\u0118\u008c\u0000\u040b\u040e\u0003\u009aM\u0000"+
		"\u040c\u040e\u0003\u009cN\u0000\u040d\u040b\u0001\u0000\u0000\u0000\u040d"+
		"\u040c\u0001\u0000\u0000\u0000\u040e\u0097\u0001\u0000\u0000\u0000\u040f"+
		"\u0412\u0007\t\u0000\u0000\u0410\u0412\u0007\t\u0000\u0000\u0411\u040f"+
		"\u0001\u0000\u0000\u0000\u0411\u0410\u0001\u0000\u0000\u0000\u0412\u0415"+
		"\u0001\u0000\u0000\u0000\u0413\u0416\u0007\u0010\u0000\u0000\u0414\u0416"+
		"\u0007\u0010\u0000\u0000\u0415\u0413\u0001\u0000\u0000\u0000\u0415\u0414"+
		"\u0001\u0000\u0000\u0000\u0416\u0419\u0001\u0000\u0000\u0000\u0417\u041a"+
		"\u0007\u0011\u0000\u0000\u0418\u041a\u0007\u0011\u0000\u0000\u0419\u0417"+
		"\u0001\u0000\u0000\u0000\u0419\u0418\u0001\u0000\u0000\u0000\u041a\u041d"+
		"\u0001\u0000\u0000\u0000\u041b\u041e\u0007\n\u0000\u0000\u041c\u041e\u0007"+
		"\n\u0000\u0000\u041d\u041b\u0001\u0000\u0000\u0000\u041d\u041c\u0001\u0000"+
		"\u0000\u0000\u041e\u0099\u0001\u0000\u0000\u0000\u041f\u0423\u0003\u009e"+
		"O\u0000\u0420\u0423\u0003\u00a0P\u0000\u0421\u0423\u0003\u00a2Q\u0000"+
		"\u0422\u041f\u0001\u0000\u0000\u0000\u0422\u0420\u0001\u0000\u0000\u0000"+
		"\u0422\u0421\u0001\u0000\u0000\u0000\u0423\u009b\u0001\u0000\u0000\u0000"+
		"\u0424\u0425\u0005\r\u0000\u0000\u0425\u0426\u0003\u0118\u008c\u0000\u0426"+
		"\u042c\u0003\u009aM\u0000\u0427\u0428\u0003\u011a\u008d\u0000\u0428\u0429"+
		"\u0003\u009aM\u0000\u0429\u042b\u0001\u0000\u0000\u0000\u042a\u0427\u0001"+
		"\u0000\u0000\u0000\u042b\u042e\u0001\u0000\u0000\u0000\u042c\u042a\u0001"+
		"\u0000\u0000\u0000\u042c\u042d\u0001\u0000\u0000\u0000\u042d\u042f\u0001"+
		"\u0000\u0000\u0000\u042e\u042c\u0001\u0000\u0000\u0000\u042f\u0430\u0003"+
		"\u0118\u008c\u0000\u0430\u0431\u0005\u000e\u0000\u0000\u0431\u009d\u0001"+
		"\u0000\u0000\u0000\u0432\u0435\u0007\b\u0000\u0000\u0433\u0435\u0007\b"+
		"\u0000\u0000\u0434\u0432\u0001\u0000\u0000\u0000\u0434\u0433\u0001\u0000"+
		"\u0000\u0000\u0435\u0438\u0001\u0000\u0000\u0000\u0436\u0439\u0007\u0010"+
		"\u0000\u0000\u0437\u0439\u0007\u0010\u0000\u0000\u0438\u0436\u0001\u0000"+
		"\u0000\u0000\u0438\u0437\u0001\u0000\u0000\u0000\u0439\u043c\u0001\u0000"+
		"\u0000\u0000\u043a\u043d\u0007\u0001\u0000\u0000\u043b\u043d\u0007\u0001"+
		"\u0000\u0000\u043c\u043a\u0001\u0000\u0000\u0000\u043c\u043b\u0001\u0000"+
		"\u0000\u0000\u043d\u009f\u0001\u0000\u0000\u0000\u043e\u0441\u0007\u0012"+
		"\u0000\u0000\u043f\u0441\u0007\u0012\u0000\u0000\u0440\u043e\u0001\u0000"+
		"\u0000\u0000\u0440\u043f\u0001\u0000\u0000\u0000\u0441\u0444\u0001\u0000"+
		"\u0000\u0000\u0442\u0445\u0007\b\u0000\u0000\u0443\u0445\u0007\b\u0000"+
		"\u0000\u0444\u0442\u0001\u0000\u0000\u0000\u0444\u0443\u0001\u0000\u0000"+
		"\u0000\u0445\u0448\u0001\u0000\u0000\u0000\u0446\u0449\u0007\u0001\u0000"+
		"\u0000\u0447\u0449\u0007\u0001\u0000\u0000\u0448\u0446\u0001\u0000\u0000"+
		"\u0000\u0448\u0447\u0001\u0000\u0000\u0000\u0449\u00a1\u0001\u0000\u0000"+
		"\u0000\u044a\u044d\u0007\u0002\u0000\u0000\u044b\u044d\u0007\u0002\u0000"+
		"\u0000\u044c\u044a\u0001\u0000\u0000\u0000\u044c\u044b\u0001\u0000\u0000"+
		"\u0000\u044d\u0450\u0001\u0000\u0000\u0000\u044e\u0451\u0007\n\u0000\u0000"+
		"\u044f\u0451\u0007\n\u0000\u0000\u0450\u044e\u0001\u0000\u0000\u0000\u0450"+
		"\u044f\u0001\u0000\u0000\u0000\u0451\u0454\u0001\u0000\u0000\u0000\u0452"+
		"\u0455\u0007\u0012\u0000\u0000\u0453\u0455\u0007\u0012\u0000\u0000\u0454"+
		"\u0452\u0001\u0000\u0000\u0000\u0454\u0453\u0001\u0000\u0000\u0000\u0455"+
		"\u00a3\u0001\u0000\u0000\u0000\u0456\u0459\u0003\u00a6S\u0000\u0457\u0459"+
		"\u0003\u00aaU\u0000\u0458\u0456\u0001\u0000\u0000\u0000\u0458\u0457\u0001"+
		"\u0000\u0000\u0000\u0459\u045d\u0001\u0000\u0000\u0000\u045a\u045b\u0003"+
		"\u0118\u008c\u0000\u045b\u045c\u0003\u00b4Z\u0000\u045c\u045e\u0001\u0000"+
		"\u0000\u0000\u045d\u045a\u0001\u0000\u0000\u0000\u045d\u045e\u0001\u0000"+
		"\u0000\u0000\u045e\u00a5\u0001\u0000\u0000\u0000\u045f\u0460\u0003\u00a8"+
		"T\u0000\u0460\u0461\u0003\u0118\u008c\u0000\u0461\u0462\u0003n7\u0000"+
		"\u0462\u0465\u0003\u0118\u008c\u0000\u0463\u0466\u0003\u0012\t\u0000\u0464"+
		"\u0466\u0003\u00b2Y\u0000\u0465\u0463\u0001\u0000\u0000\u0000\u0465\u0464"+
		"\u0001\u0000\u0000\u0000\u0466\u00a7\u0001\u0000\u0000\u0000\u0467\u046a"+
		"\u0007\u0002\u0000\u0000\u0468\u046a\u0007\u0002\u0000\u0000\u0469\u0467"+
		"\u0001\u0000\u0000\u0000\u0469\u0468\u0001\u0000\u0000\u0000\u046a\u046d"+
		"\u0001\u0000\u0000\u0000\u046b\u046e\u0007\u0006\u0000\u0000\u046c\u046e"+
		"\u0007\u0006\u0000\u0000\u046d\u046b\u0001\u0000\u0000\u0000\u046d\u046c"+
		"\u0001\u0000\u0000\u0000\u046e\u0471\u0001\u0000\u0000\u0000\u046f\u0472"+
		"\u0007\u0000\u0000\u0000\u0470\u0472\u0007\u0000\u0000\u0000\u0471\u046f"+
		"\u0001\u0000\u0000\u0000\u0471\u0470\u0001\u0000\u0000\u0000\u0472\u0475"+
		"\u0001\u0000\u0000\u0000\u0473\u0476\u0007\f\u0000\u0000\u0474\u0476\u0007"+
		"\f\u0000\u0000\u0475\u0473\u0001\u0000\u0000\u0000\u0475\u0474\u0001\u0000"+
		"\u0000\u0000\u0476\u0479\u0001\u0000\u0000\u0000\u0477\u047a\u0007\n\u0000"+
		"\u0000\u0478\u047a\u0007\n\u0000\u0000\u0479\u0477\u0001\u0000\u0000\u0000"+
		"\u0479\u0478\u0001\u0000\u0000\u0000\u047a\u047d\u0001\u0000\u0000\u0000"+
		"\u047b\u047e\u0007\r\u0000\u0000\u047c\u047e\u0007\r\u0000\u0000\u047d"+
		"\u047b\u0001\u0000\u0000\u0000\u047d\u047c\u0001\u0000\u0000\u0000\u047e"+
		"\u0481\u0001\u0000\u0000\u0000\u047f\u0482\u0007\t\u0000\u0000\u0480\u0482"+
		"\u0007\t\u0000\u0000\u0481\u047f\u0001\u0000\u0000\u0000\u0481\u0480\u0001"+
		"\u0000\u0000\u0000\u0482\u0485\u0001\u0000\u0000\u0000\u0483\u0486\u0007"+
		"\u0006\u0000\u0000\u0484\u0486\u0007\u0006\u0000\u0000\u0485\u0483\u0001"+
		"\u0000\u0000\u0000\u0485\u0484\u0001\u0000\u0000\u0000\u0486\u0489\u0001"+
		"\u0000\u0000\u0000\u0487\u048a\u0007\u0002\u0000\u0000\u0488\u048a\u0007"+
		"\u0002\u0000\u0000\u0489\u0487\u0001\u0000\u0000\u0000\u0489\u0488\u0001"+
		"\u0000\u0000\u0000\u048a\u00a9\u0001\u0000\u0000\u0000\u048b\u048c\u0003"+
		"\u00acV\u0000\u048c\u048d\u0003\u0118\u008c\u0000\u048d\u048e\u0003n7"+
		"\u0000\u048e\u0491\u0003\u0118\u008c\u0000\u048f\u0492\u0003\u00aeW\u0000"+
		"\u0490\u0492\u0003\u00b0X\u0000\u0491\u048f\u0001\u0000\u0000\u0000\u0491"+
		"\u0490\u0001\u0000\u0000\u0000\u0492\u00ab\u0001\u0000\u0000\u0000\u0493"+
		"\u0496\u0007\u0002\u0000\u0000\u0494\u0496\u0007\u0002\u0000\u0000\u0495"+
		"\u0493\u0001\u0000\u0000\u0000\u0495\u0494\u0001\u0000\u0000\u0000\u0496"+
		"\u0499\u0001\u0000\u0000\u0000\u0497\u049a\u0007\u0006\u0000\u0000\u0498"+
		"\u049a\u0007\u0006\u0000\u0000\u0499\u0497\u0001\u0000\u0000\u0000\u0499"+
		"\u0498\u0001\u0000\u0000\u0000\u049a\u049d\u0001\u0000\u0000\u0000\u049b"+
		"\u049e\u0007\u0000\u0000\u0000\u049c\u049e\u0007\u0000\u0000\u0000\u049d"+
		"\u049b\u0001\u0000\u0000\u0000\u049d\u049c\u0001\u0000\u0000\u0000\u049e"+
		"\u04a1\u0001\u0000\u0000\u0000\u049f\u04a2\u0007\f\u0000\u0000\u04a0\u04a2"+
		"\u0007\f\u0000\u0000\u04a1\u049f\u0001\u0000\u0000\u0000\u04a1\u04a0\u0001"+
		"\u0000\u0000\u0000\u04a2\u04a5\u0001\u0000\u0000\u0000\u04a3\u04a6\u0007"+
		"\n\u0000\u0000\u04a4\u04a6\u0007\n\u0000\u0000\u04a5\u04a3\u0001\u0000"+
		"\u0000\u0000\u04a5\u04a4\u0001\u0000\u0000\u0000\u04a6\u04a9\u0001\u0000"+
		"\u0000\u0000\u04a7\u04aa\u0007\r\u0000\u0000\u04a8\u04aa\u0007\r\u0000"+
		"\u0000\u04a9\u04a7\u0001\u0000\u0000\u0000\u04a9\u04a8\u0001\u0000\u0000"+
		"\u0000\u04aa\u04ad\u0001\u0000\u0000\u0000\u04ab\u04ae\u0007\t\u0000\u0000"+
		"\u04ac\u04ae\u0007\t\u0000\u0000\u04ad\u04ab\u0001\u0000\u0000\u0000\u04ad"+
		"\u04ac\u0001\u0000\u0000\u0000\u04ae\u00ad\u0001\u0000\u0000\u0000\u04af"+
		"\u04b5\u0003\u0142\u00a1\u0000\u04b0\u04b4\u0003\u0144\u00a2\u0000\u04b1"+
		"\u04b4\u0003\u0142\u00a1\u0000\u04b2\u04b4\u0003\u010a\u0085\u0000\u04b3"+
		"\u04b0\u0001\u0000\u0000\u0000\u04b3\u04b1\u0001\u0000\u0000\u0000\u04b3"+
		"\u04b2\u0001\u0000\u0000\u0000\u04b4\u04b7\u0001\u0000\u0000\u0000\u04b5"+
		"\u04b3\u0001\u0000\u0000\u0000\u04b5\u04b6\u0001\u0000\u0000\u0000\u04b6"+
		"\u00af\u0001\u0000\u0000\u0000\u04b7\u04b5\u0001\u0000\u0000\u0000\u04b8"+
		"\u04b9\u0005\r\u0000\u0000\u04b9\u04ba\u0003\u0118\u008c\u0000\u04ba\u04be"+
		"\u0003\u00aeW\u0000\u04bb\u04bc\u0003\u0118\u008c\u0000\u04bc\u04bd\u0003"+
		"\u00b4Z\u0000\u04bd\u04bf\u0001\u0000\u0000\u0000\u04be\u04bb\u0001\u0000"+
		"\u0000\u0000\u04be\u04bf\u0001\u0000\u0000\u0000\u04bf\u04c9\u0001\u0000"+
		"\u0000\u0000\u04c0\u04c1\u0003\u011a\u008d\u0000\u04c1\u04c5\u0003\u00ae"+
		"W\u0000\u04c2\u04c3\u0003\u0118\u008c\u0000\u04c3\u04c4\u0003\u00b4Z\u0000"+
		"\u04c4\u04c6\u0001\u0000\u0000\u0000\u04c5\u04c2\u0001\u0000\u0000\u0000"+
		"\u04c5\u04c6\u0001\u0000\u0000\u0000\u04c6\u04c8\u0001\u0000\u0000\u0000"+
		"\u04c7\u04c0\u0001\u0000\u0000\u0000\u04c8\u04cb\u0001\u0000\u0000\u0000"+
		"\u04c9\u04c7\u0001\u0000\u0000\u0000\u04c9\u04ca\u0001\u0000\u0000\u0000"+
		"\u04ca\u04cc\u0001\u0000\u0000\u0000\u04cb\u04c9\u0001\u0000\u0000\u0000"+
		"\u04cc\u04cd\u0003\u0118\u008c\u0000\u04cd\u04ce\u0005\u000e\u0000\u0000"+
		"\u04ce\u00b1\u0001\u0000\u0000\u0000\u04cf\u04d0\u0005\r\u0000\u0000\u04d0"+
		"\u04d1\u0003\u0118\u008c\u0000\u04d1\u04d5\u0003\"\u0011\u0000\u04d2\u04d3"+
		"\u0003\u0118\u008c\u0000\u04d3\u04d4\u0003\u00b4Z\u0000\u04d4\u04d6\u0001"+
		"\u0000\u0000\u0000\u04d5\u04d2\u0001\u0000\u0000\u0000\u04d5\u04d6\u0001"+
		"\u0000\u0000\u0000\u04d6\u04e0\u0001\u0000\u0000\u0000\u04d7\u04d8\u0003"+
		"\u011a\u008d\u0000\u04d8\u04dc\u0003\"\u0011\u0000\u04d9\u04da\u0003\u0118"+
		"\u008c\u0000\u04da\u04db\u0003\u00b4Z\u0000\u04db\u04dd\u0001\u0000\u0000"+
		"\u0000\u04dc\u04d9\u0001\u0000\u0000\u0000\u04dc\u04dd\u0001\u0000\u0000"+
		"\u0000\u04dd\u04df\u0001\u0000\u0000\u0000\u04de\u04d7\u0001\u0000\u0000"+
		"\u0000\u04df\u04e2\u0001\u0000\u0000\u0000\u04e0\u04de\u0001\u0000\u0000"+
		"\u0000\u04e0\u04e1\u0001\u0000\u0000\u0000\u04e1\u04e3\u0001\u0000\u0000"+
		"\u0000\u04e2\u04e0\u0001\u0000\u0000\u0000\u04e3\u04e4\u0003\u0118\u008c"+
		"\u0000\u04e4\u04e5\u0005\u000e\u0000\u0000\u04e5\u00b3\u0001\u0000\u0000"+
		"\u0000\u04e6\u04e9\u0003\u00b6[\u0000\u04e7\u04e9\u0003\u00b8\\\u0000"+
		"\u04e8\u04e6\u0001\u0000\u0000\u0000\u04e8\u04e7\u0001\u0000\u0000\u0000"+
		"\u04e9\u00b5\u0001\u0000\u0000\u0000\u04ea\u04eb\u0005\r\u0000\u0000\u04eb"+
		"\u04ec\u0003\u0118\u008c\u0000\u04ec\u04f2\u0003\"\u0011\u0000\u04ed\u04ee"+
		"\u0003\u011a\u008d\u0000\u04ee\u04ef\u0003\"\u0011\u0000\u04ef\u04f1\u0001"+
		"\u0000\u0000\u0000\u04f0\u04ed\u0001\u0000\u0000\u0000\u04f1\u04f4\u0001"+
		"\u0000\u0000\u0000\u04f2\u04f0\u0001\u0000\u0000\u0000\u04f2\u04f3\u0001"+
		"\u0000\u0000\u0000\u04f3\u04f5\u0001\u0000\u0000\u0000\u04f4\u04f2\u0001"+
		"\u0000\u0000\u0000\u04f5\u04f6\u0003\u0118\u008c\u0000\u04f6\u04f7\u0005"+
		"\u000e\u0000\u0000\u04f7\u00b7\u0001\u0000\u0000\u0000\u04f8\u04f9\u0005"+
		"\r\u0000\u0000\u04f9\u04fa\u0003\u0118\u008c\u0000\u04fa\u0500\u0003\u00ba"+
		"]\u0000\u04fb\u04fc\u0003\u011a\u008d\u0000\u04fc\u04fd\u0003\u00ba]\u0000"+
		"\u04fd\u04ff\u0001\u0000\u0000\u0000\u04fe\u04fb\u0001\u0000\u0000\u0000"+
		"\u04ff\u0502\u0001\u0000\u0000\u0000\u0500\u04fe\u0001\u0000\u0000\u0000"+
		"\u0500\u0501\u0001\u0000\u0000\u0000\u0501\u0503\u0001\u0000\u0000\u0000"+
		"\u0502\u0500\u0001\u0000\u0000\u0000\u0503\u0504\u0003\u0118\u008c\u0000"+
		"\u0504\u0505\u0005\u000e\u0000\u0000\u0505\u00b9\u0001\u0000\u0000\u0000"+
		"\u0506\u0509\u0003\u00bc^\u0000\u0507\u0509\u0003\u00be_\u0000\u0508\u0506"+
		"\u0001\u0000\u0000\u0000\u0508\u0507\u0001\u0000\u0000\u0000\u0509\u00bb"+
		"\u0001\u0000\u0000\u0000\u050a\u050d\u0007\u0000\u0000\u0000\u050b\u050d"+
		"\u0007\u0000\u0000\u0000\u050c\u050a\u0001\u0000\u0000\u0000\u050c\u050b"+
		"\u0001\u0000\u0000\u0000\u050d\u0510\u0001\u0000\u0000\u0000\u050e\u0511"+
		"\u0007\r\u0000\u0000\u050f\u0511\u0007\r\u0000\u0000\u0510\u050e\u0001"+
		"\u0000\u0000\u0000\u0510\u050f\u0001\u0000\u0000\u0000\u0511\u0514\u0001"+
		"\u0000\u0000\u0000\u0512\u0515\u0007\r\u0000\u0000\u0513\u0515\u0007\r"+
		"\u0000\u0000\u0514\u0512\u0001\u0000\u0000\u0000\u0514\u0513\u0001\u0000"+
		"\u0000\u0000\u0515\u0518\u0001\u0000\u0000\u0000\u0516\u0519\u0007\n\u0000"+
		"\u0000\u0517\u0519\u0007\n\u0000\u0000\u0518\u0516\u0001\u0000\u0000\u0000"+
		"\u0518\u0517\u0001\u0000\u0000\u0000\u0519\u051c\u0001\u0000\u0000\u0000"+
		"\u051a\u051d\u0007\u0011\u0000\u0000\u051b\u051d\u0007\u0011\u0000\u0000"+
		"\u051c\u051a\u0001\u0000\u0000\u0000\u051c\u051b\u0001\u0000\u0000\u0000"+
		"\u051d\u0520\u0001\u0000\u0000\u0000\u051e\u0521\u0007\t\u0000\u0000\u051f"+
		"\u0521\u0007\t\u0000\u0000\u0520\u051e\u0001\u0000\u0000\u0000\u0520\u051f"+
		"\u0001\u0000\u0000\u0000\u0521\u00bd\u0001\u0000\u0000\u0000\u0522\u0525"+
		"\u0007\u0011\u0000\u0000\u0523\u0525\u0007\u0011\u0000\u0000\u0524\u0522"+
		"\u0001\u0000\u0000\u0000\u0524\u0523\u0001\u0000\u0000\u0000\u0525\u0528"+
		"\u0001\u0000\u0000\u0000\u0526\u0529\u0007\u0004\u0000\u0000\u0527\u0529"+
		"\u0007\u0004\u0000\u0000\u0528\u0526\u0001\u0000\u0000\u0000\u0528\u0527"+
		"\u0001\u0000\u0000\u0000\u0529\u052c\u0001\u0000\u0000\u0000\u052a\u052d"+
		"\u0007\n\u0000\u0000\u052b\u052d\u0007\n\u0000\u0000\u052c\u052a\u0001"+
		"\u0000\u0000\u0000\u052c\u052b\u0001\u0000\u0000\u0000\u052d\u0530\u0001"+
		"\u0000\u0000\u0000\u052e\u0531\u0007\u0012\u0000\u0000\u052f\u0531\u0007"+
		"\u0012\u0000\u0000\u0530\u052e\u0001\u0000\u0000\u0000\u0530\u052f\u0001"+
		"\u0000\u0000\u0000\u0531\u0534\u0001\u0000\u0000\u0000\u0532\u0535\u0007"+
		"\n\u0000\u0000\u0533\u0535\u0007\n\u0000\u0000\u0534\u0532\u0001\u0000"+
		"\u0000\u0000\u0534\u0533\u0001\u0000\u0000\u0000\u0535\u0538\u0001\u0000"+
		"\u0000\u0000\u0536\u0539\u0007\u0004\u0000\u0000\u0537\u0539\u0007\u0004"+
		"\u0000\u0000\u0538\u0536\u0001\u0000\u0000\u0000\u0538\u0537\u0001\u0000"+
		"\u0000\u0000\u0539\u00bf\u0001\u0000\u0000\u0000\u053a\u053b\u0005`\u0000"+
		"\u0000\u053b\u053c\u0005`\u0000\u0000\u053c\u053d\u0001\u0000\u0000\u0000"+
		"\u053d\u0540\u0003\u0118\u008c\u0000\u053e\u0541\u0007\r\u0000\u0000\u053f"+
		"\u0541\u0007\r\u0000\u0000\u0540\u053e\u0001\u0000\u0000\u0000\u0540\u053f"+
		"\u0001\u0000\u0000\u0000\u0541\u0542\u0001\u0000\u0000\u0000\u0542\u0543"+
		"\u0003\u0118\u008c\u0000\u0543\u054b\u0003\u00c2a\u0000\u0544\u0545\u0003"+
		"\u0118\u008c\u0000\u0545\u0546\u0005\u0011\u0000\u0000\u0546\u0547\u0003"+
		"\u0118\u008c\u0000\u0547\u0548\u0003\u00c2a\u0000\u0548\u054a\u0001\u0000"+
		"\u0000\u0000\u0549\u0544\u0001\u0000\u0000\u0000\u054a\u054d\u0001\u0000"+
		"\u0000\u0000\u054b\u0549\u0001\u0000\u0000\u0000\u054b\u054c\u0001\u0000"+
		"\u0000\u0000\u054c\u054e\u0001\u0000\u0000\u0000\u054d\u054b\u0001\u0000"+
		"\u0000\u0000\u054e\u054f\u0003\u0118\u008c\u0000\u054f\u0550\u0005b\u0000"+
		"\u0000\u0550\u0551\u0005b\u0000\u0000\u0551\u00c1\u0001\u0000\u0000\u0000"+
		"\u0552\u0557\u0003\u00c4b\u0000\u0553\u0557\u0003\u00d6k\u0000\u0554\u0557"+
		"\u0003\u00dam\u0000\u0555\u0557\u0003\u00e8t\u0000\u0556\u0552\u0001\u0000"+
		"\u0000\u0000\u0556\u0553\u0001\u0000\u0000\u0000\u0556\u0554\u0001\u0000"+
		"\u0000\u0000\u0556\u0555\u0001\u0000\u0000\u0000\u0557\u00c3\u0001\u0000"+
		"\u0000\u0000\u0558\u055b\u0003\u00c6c\u0000\u0559\u055b\u0003\u00cae\u0000"+
		"\u055a\u0558\u0001\u0000\u0000\u0000\u055a\u0559\u0001\u0000\u0000\u0000"+
		"\u055b\u00c5\u0001\u0000\u0000\u0000\u055c\u055d\u0003\u00c8d\u0000\u055d"+
		"\u055e\u0003\u0118\u008c\u0000\u055e\u055f\u0003n7\u0000\u055f\u0562\u0003"+
		"\u0118\u008c\u0000\u0560\u0563\u0003\u0012\t\u0000\u0561\u0563\u0003$"+
		"\u0012\u0000\u0562\u0560\u0001\u0000\u0000\u0000\u0562\u0561\u0001\u0000"+
		"\u0000\u0000\u0563\u00c7\u0001\u0000\u0000\u0000\u0564\u0567\u0007\u0002"+
		"\u0000\u0000\u0565\u0567\u0007\u0002\u0000\u0000\u0566\u0564\u0001\u0000"+
		"\u0000\u0000\u0566\u0565\u0001\u0000\u0000\u0000\u0567\u056a\u0001\u0000"+
		"\u0000\u0000\u0568\u056b\u0007\n\u0000\u0000\u0569\u056b\u0007\n\u0000"+
		"\u0000\u056a\u0568\u0001\u0000\u0000\u0000\u056a\u0569\u0001\u0000\u0000"+
		"\u0000\u056b\u056e\u0001\u0000\u0000\u0000\u056c\u056f\u0007\u0012\u0000"+
		"\u0000\u056d\u056f\u0007\u0012\u0000\u0000\u056e\u056c\u0001\u0000\u0000"+
		"\u0000\u056e\u056d\u0001\u0000\u0000\u0000\u056f\u0572\u0001\u0000\u0000"+
		"\u0000\u0570\u0573\u0007\u0006\u0000\u0000\u0571\u0573\u0007\u0006\u0000"+
		"\u0000\u0572\u0570\u0001\u0000\u0000\u0000\u0572\u0571\u0001\u0000\u0000"+
		"\u0000\u0573\u0576\u0001\u0000\u0000\u0000\u0574\u0577\u0007\u0001\u0000"+
		"\u0000\u0575\u0577\u0007\u0001\u0000\u0000\u0576\u0574\u0001\u0000\u0000"+
		"\u0000\u0576\u0575\u0001\u0000\u0000\u0000\u0577\u057a\u0001\u0000\u0000"+
		"\u0000\u0578\u057b\u0007\u0006\u0000\u0000\u0579\u057b\u0007\u0006\u0000"+
		"\u0000\u057a\u0578\u0001\u0000\u0000\u0000\u057a\u0579\u0001\u0000\u0000"+
		"\u0000\u057b\u057e\u0001\u0000\u0000\u0000\u057c\u057f\u0007\t\u0000\u0000"+
		"\u057d\u057f\u0007\t\u0000\u0000\u057e\u057c\u0001\u0000\u0000\u0000\u057e"+
		"\u057d\u0001\u0000\u0000\u0000\u057f\u0582\u0001\u0000\u0000\u0000\u0580"+
		"\u0583\u0007\u0006\u0000\u0000\u0581\u0583\u0007\u0006\u0000\u0000\u0582"+
		"\u0580\u0001\u0000\u0000\u0000\u0582\u0581\u0001\u0000\u0000\u0000\u0583"+
		"\u0586\u0001\u0000\u0000\u0000\u0584\u0587\u0007\u0003\u0000\u0000\u0585"+
		"\u0587\u0007\u0003\u0000\u0000\u0586\u0584\u0001\u0000\u0000\u0000\u0586"+
		"\u0585\u0001\u0000\u0000\u0000\u0587\u058a\u0001\u0000\u0000\u0000\u0588"+
		"\u058b\u0007\u0001\u0000\u0000\u0589\u058b\u0007\u0001\u0000\u0000\u058a"+
		"\u0588\u0001\u0000\u0000\u0000\u058a\u0589\u0001\u0000\u0000\u0000\u058b"+
		"\u058e\u0001\u0000\u0000\u0000\u058c\u058f\u0007\b\u0000\u0000\u058d\u058f"+
		"\u0007\b\u0000\u0000\u058e\u058c\u0001\u0000\u0000\u0000\u058e\u058d\u0001"+
		"\u0000\u0000\u0000\u058f\u0592\u0001\u0000\u0000\u0000\u0590\u0593\u0007"+
		"\t\u0000\u0000\u0591\u0593\u0007\t\u0000\u0000\u0592\u0590\u0001\u0000"+
		"\u0000\u0000\u0592\u0591\u0001\u0000\u0000\u0000\u0593\u0596\u0001\u0000"+
		"\u0000\u0000\u0594\u0597\u0007\u0000\u0000\u0000\u0595\u0597\u0007\u0000"+
		"\u0000\u0000\u0596\u0594\u0001\u0000\u0000\u0000\u0596\u0595\u0001\u0000"+
		"\u0000\u0000\u0597\u059a\u0001\u0000\u0000\u0000\u0598\u059b\u0007\t\u0000"+
		"\u0000\u0599\u059b\u0007\t\u0000\u0000\u059a\u0598\u0001\u0000\u0000\u0000"+
		"\u059a\u0599\u0001\u0000\u0000\u0000\u059b\u059e\u0001\u0000\u0000\u0000"+
		"\u059c\u059f\u0007\u0007\u0000\u0000\u059d\u059f\u0007\u0007\u0000\u0000"+
		"\u059e\u059c\u0001\u0000\u0000\u0000\u059e\u059d\u0001\u0000\u0000\u0000"+
		"\u059f\u05a2\u0001\u0000\u0000\u0000\u05a0\u05a3\u0007\b\u0000\u0000\u05a1"+
		"\u05a3\u0007\b\u0000\u0000\u05a2\u05a0\u0001\u0000\u0000\u0000\u05a2\u05a1"+
		"\u0001\u0000\u0000\u0000\u05a3\u05a6\u0001\u0000\u0000\u0000\u05a4\u05a7"+
		"\u0007\u0006\u0000\u0000\u05a5\u05a7\u0007\u0006\u0000\u0000\u05a6\u05a4"+
		"\u0001\u0000\u0000\u0000\u05a6\u05a5\u0001\u0000\u0000\u0000\u05a7\u05aa"+
		"\u0001\u0000\u0000\u0000\u05a8\u05ab\u0007\u0002\u0000\u0000\u05a9\u05ab"+
		"\u0007\u0002\u0000\u0000\u05aa\u05a8\u0001\u0000\u0000\u0000\u05aa\u05a9"+
		"\u0001\u0000\u0000\u0000\u05ab\u00c9\u0001\u0000\u0000\u0000\u05ac\u05ad"+
		"\u0003\u00ccf\u0000\u05ad\u05ae\u0003\u0118\u008c\u0000\u05ae\u05af\u0003"+
		"n7\u0000\u05af\u05b2\u0003\u0118\u008c\u0000\u05b0\u05b3\u0003\u00ceg"+
		"\u0000\u05b1\u05b3\u0003\u00d0h\u0000\u05b2\u05b0\u0001\u0000\u0000\u0000"+
		"\u05b2\u05b1\u0001\u0000\u0000\u0000\u05b3\u00cb\u0001\u0000\u0000\u0000"+
		"\u05b4\u05b7\u0007\u0002\u0000\u0000\u05b5\u05b7\u0007\u0002\u0000\u0000"+
		"\u05b6\u05b4\u0001\u0000\u0000\u0000\u05b6\u05b5\u0001\u0000\u0000\u0000"+
		"\u05b7\u05ba\u0001\u0000\u0000\u0000\u05b8\u05bb\u0007\n\u0000\u0000\u05b9"+
		"\u05bb\u0007\n\u0000\u0000\u05ba\u05b8\u0001\u0000\u0000\u0000\u05ba\u05b9"+
		"\u0001\u0000\u0000\u0000\u05bb\u05be\u0001\u0000\u0000\u0000\u05bc\u05bf"+
		"\u0007\u0012\u0000\u0000\u05bd\u05bf\u0007\u0012\u0000\u0000\u05be\u05bc"+
		"\u0001\u0000\u0000\u0000\u05be\u05bd\u0001\u0000\u0000\u0000\u05bf\u05c2"+
		"\u0001\u0000\u0000\u0000\u05c0\u05c3\u0007\u0006\u0000\u0000\u05c1\u05c3"+
		"\u0007\u0006\u0000\u0000\u05c2\u05c0\u0001\u0000\u0000\u0000\u05c2\u05c1"+
		"\u0001\u0000\u0000\u0000\u05c3\u05c6\u0001\u0000\u0000\u0000\u05c4\u05c7"+
		"\u0007\u0001\u0000\u0000\u05c5\u05c7\u0007\u0001\u0000\u0000\u05c6\u05c4"+
		"\u0001\u0000\u0000\u0000\u05c6\u05c5\u0001\u0000\u0000\u0000\u05c7\u05ca"+
		"\u0001\u0000\u0000\u0000\u05c8\u05cb\u0007\u0006\u0000\u0000\u05c9\u05cb"+
		"\u0007\u0006\u0000\u0000\u05ca\u05c8\u0001\u0000\u0000\u0000\u05ca\u05c9"+
		"\u0001\u0000\u0000\u0000\u05cb\u05ce\u0001\u0000\u0000\u0000\u05cc\u05cf"+
		"\u0007\t\u0000\u0000\u05cd\u05cf\u0007\t\u0000\u0000\u05ce\u05cc\u0001"+
		"\u0000\u0000\u0000\u05ce\u05cd\u0001\u0000\u0000\u0000\u05cf\u05d2\u0001"+
		"\u0000\u0000\u0000\u05d0\u05d3\u0007\u0006\u0000\u0000\u05d1\u05d3\u0007"+
		"\u0006\u0000\u0000\u05d2\u05d0\u0001\u0000\u0000\u0000\u05d2\u05d1\u0001"+
		"\u0000\u0000\u0000\u05d3\u05d6\u0001\u0000\u0000\u0000\u05d4\u05d7\u0007"+
		"\u0003\u0000\u0000\u05d5\u05d7\u0007\u0003\u0000\u0000\u05d6\u05d4\u0001"+
		"\u0000\u0000\u0000\u05d6\u05d5\u0001\u0000\u0000\u0000\u05d7\u05da\u0001"+
		"\u0000\u0000\u0000\u05d8\u05db\u0007\u0001\u0000\u0000\u05d9\u05db\u0007"+
		"\u0001\u0000\u0000\u05da\u05d8\u0001\u0000\u0000\u0000\u05da\u05d9\u0001"+
		"\u0000\u0000\u0000\u05db\u05de\u0001\u0000\u0000\u0000\u05dc\u05df\u0007"+
		"\b\u0000\u0000\u05dd\u05df\u0007\b\u0000\u0000\u05de\u05dc\u0001\u0000"+
		"\u0000\u0000\u05de\u05dd\u0001\u0000\u0000\u0000\u05df\u05e2\u0001\u0000"+
		"\u0000\u0000\u05e0\u05e3\u0007\t\u0000\u0000\u05e1\u05e3\u0007\t\u0000"+
		"\u0000\u05e2\u05e0\u0001\u0000\u0000\u0000\u05e2\u05e1\u0001\u0000\u0000"+
		"\u0000\u05e3\u05e6\u0001\u0000\u0000\u0000\u05e4\u05e7\u0007\u0000\u0000"+
		"\u0000\u05e5\u05e7\u0007\u0000\u0000\u0000\u05e6\u05e4\u0001\u0000\u0000"+
		"\u0000\u05e6\u05e5\u0001\u0000\u0000\u0000\u05e7\u05ea\u0001\u0000\u0000"+
		"\u0000\u05e8\u05eb\u0007\t\u0000\u0000\u05e9\u05eb\u0007\t\u0000\u0000"+
		"\u05ea\u05e8\u0001\u0000\u0000\u0000\u05ea\u05e9\u0001\u0000\u0000\u0000"+
		"\u05eb\u05ee\u0001\u0000\u0000\u0000\u05ec\u05ef\u0007\u0007\u0000\u0000"+
		"\u05ed\u05ef\u0007\u0007\u0000\u0000\u05ee\u05ec\u0001\u0000\u0000\u0000"+
		"\u05ee\u05ed\u0001\u0000\u0000\u0000\u05ef\u05f2\u0001\u0000\u0000\u0000"+
		"\u05f0\u05f3\u0007\b\u0000\u0000\u05f1\u05f3\u0007\b\u0000\u0000\u05f2"+
		"\u05f0\u0001\u0000\u0000\u0000\u05f2\u05f1\u0001\u0000\u0000\u0000\u05f3"+
		"\u00cd\u0001\u0000\u0000\u0000\u05f4\u05f7\u0003\u00d2i\u0000\u05f5\u05f7"+
		"\u0003\u00d4j\u0000\u05f6\u05f4\u0001\u0000\u0000\u0000\u05f6\u05f5\u0001"+
		"\u0000\u0000\u0000\u05f7\u00cf\u0001\u0000\u0000\u0000\u05f8\u05f9\u0005"+
		"\r\u0000\u0000\u05f9\u05fa\u0003\u0118\u008c\u0000\u05fa\u0600\u0003\u00ce"+
		"g\u0000\u05fb\u05fc\u0003\u011a\u008d\u0000\u05fc\u05fd\u0003\u00ceg\u0000"+
		"\u05fd\u05ff\u0001\u0000\u0000\u0000\u05fe\u05fb\u0001\u0000\u0000\u0000"+
		"\u05ff\u0602\u0001\u0000\u0000\u0000\u0600\u05fe\u0001\u0000\u0000\u0000"+
		"\u0600\u0601\u0001\u0000\u0000\u0000\u0601\u0603\u0001\u0000\u0000\u0000"+
		"\u0602\u0600\u0001\u0000\u0000\u0000\u0603\u0604\u0003\u0118\u008c\u0000"+
		"\u0604\u0605\u0005\u000e\u0000\u0000\u0605\u00d1\u0001\u0000\u0000\u0000"+
		"\u0606\u0609\u0007\u0011\u0000\u0000\u0607\u0609\u0007\u0011\u0000\u0000"+
		"\u0608\u0606\u0001\u0000\u0000\u0000\u0608\u0607\u0001\u0000\u0000\u0000"+
		"\u0609\u060c\u0001\u0000\u0000\u0000\u060a\u060d\u0007\u0004\u0000\u0000"+
		"\u060b\u060d\u0007\u0004\u0000\u0000\u060c\u060a\u0001\u0000\u0000\u0000"+
		"\u060c\u060b\u0001\u0000\u0000\u0000\u060d\u0610\u0001\u0000\u0000\u0000"+
		"\u060e\u0611\u0007\u0006\u0000\u0000\u060f\u0611\u0007\u0006\u0000\u0000"+
		"\u0610\u060e\u0001\u0000\u0000\u0000\u0610\u060f\u0001\u0000\u0000\u0000"+
		"\u0611\u0614\u0001\u0000\u0000\u0000\u0612\u0615\u0007\u0005\u0000\u0000"+
		"\u0613\u0615\u0007\u0005\u0000\u0000\u0614\u0612\u0001\u0000\u0000\u0000"+
		"\u0614\u0613\u0001\u0000\u0000\u0000\u0615\u0618\u0001\u0000\u0000\u0000"+
		"\u0616\u0619\u0007\u0006\u0000\u0000\u0617\u0619\u0007\u0006\u0000\u0000"+
		"\u0618\u0616\u0001\u0000\u0000\u0000\u0618\u0617\u0001\u0000\u0000\u0000"+
		"\u0619\u061c\u0001\u0000\u0000\u0000\u061a\u061d\u0007\t\u0000\u0000\u061b"+
		"\u061d\u0007\t\u0000\u0000\u061c\u061a\u0001\u0000\u0000\u0000\u061c\u061b"+
		"\u0001\u0000\u0000\u0000\u061d\u0620\u0001\u0000\u0000\u0000\u061e\u0621"+
		"\u0007\u0006\u0000\u0000\u061f\u0621\u0007\u0006\u0000\u0000\u0620\u061e"+
		"\u0001\u0000\u0000\u0000\u0620\u061f\u0001\u0000\u0000\u0000\u0621\u0624"+
		"\u0001\u0000\u0000\u0000\u0622\u0625\u0007\u0013\u0000\u0000\u0623\u0625"+
		"\u0007\u0013\u0000\u0000\u0624\u0622\u0001\u0000\u0000\u0000\u0624\u0623"+
		"\u0001\u0000\u0000\u0000\u0625\u0628\u0001\u0000\u0000\u0000\u0626\u0629"+
		"\u0007\n\u0000\u0000\u0627\u0629\u0007\n\u0000\u0000\u0628\u0626\u0001"+
		"\u0000\u0000\u0000\u0628\u0627\u0001\u0000\u0000\u0000\u0629\u00d3\u0001"+
		"\u0000\u0000\u0000\u062a\u062d\u0007\u0002\u0000\u0000\u062b\u062d\u0007"+
		"\u0002\u0000\u0000\u062c\u062a\u0001\u0000\u0000\u0000\u062c\u062b\u0001"+
		"\u0000\u0000\u0000\u062d\u0630\u0001\u0000\u0000\u0000\u062e\u0631\u0007"+
		"\n\u0000\u0000\u062f\u0631\u0007\n\u0000\u0000\u0630\u062e\u0001\u0000"+
		"\u0000\u0000\u0630\u062f\u0001\u0000\u0000\u0000\u0631\u0634\u0001\u0000"+
		"\u0000\u0000\u0632\u0635\u0007\u0012\u0000\u0000\u0633\u0635\u0007\u0012"+
		"\u0000\u0000\u0634\u0632\u0001\u0000\u0000\u0000\u0634\u0633\u0001\u0000"+
		"\u0000\u0000\u0635\u0638\u0001\u0000\u0000\u0000\u0636\u0639\u0007\u0006"+
		"\u0000\u0000\u0637\u0639\u0007\u0006\u0000\u0000\u0638\u0636\u0001\u0000"+
		"\u0000\u0000\u0638\u0637\u0001\u0000\u0000\u0000\u0639\u063c\u0001\u0000"+
		"\u0000\u0000\u063a\u063d\u0007\u0001\u0000\u0000\u063b\u063d\u0007\u0001"+
		"\u0000\u0000\u063c\u063a\u0001\u0000\u0000\u0000\u063c\u063b\u0001\u0000"+
		"\u0000\u0000\u063d\u0640\u0001\u0000\u0000\u0000\u063e\u0641\u0007\n\u0000"+
		"\u0000\u063f\u0641\u0007\n\u0000\u0000\u0640\u063e\u0001\u0000\u0000\u0000"+
		"\u0640\u063f\u0001\u0000\u0000\u0000\u0641\u0644\u0001\u0000\u0000\u0000"+
		"\u0642\u0645\u0007\u0002\u0000\u0000\u0643\u0645\u0007\u0002\u0000\u0000"+
		"\u0644\u0642\u0001\u0000\u0000\u0000\u0644\u0643\u0001\u0000\u0000\u0000"+
		"\u0645\u00d5\u0001\u0000\u0000\u0000\u0646\u0647\u0003\u00d8l\u0000\u0647"+
		"\u0648\u0003\u0118\u008c\u0000\u0648\u0649\u0003n7\u0000\u0649\u064c\u0003"+
		"\u0118\u008c\u0000\u064a\u064d\u0003\u0012\t\u0000\u064b\u064d\u0003$"+
		"\u0012\u0000\u064c\u064a\u0001\u0000\u0000\u0000\u064c\u064b\u0001\u0000"+
		"\u0000\u0000\u064d\u00d7\u0001\u0000\u0000\u0000\u064e\u0651\u0007\u0005"+
		"\u0000\u0000\u064f\u0651\u0007\u0005\u0000\u0000\u0650\u064e\u0001\u0000"+
		"\u0000\u0000\u0650\u064f\u0001\u0000\u0000\u0000\u0651\u0654\u0001\u0000"+
		"\u0000\u0000\u0652\u0655\u0007\u0003\u0000\u0000\u0653\u0655\u0007\u0003"+
		"\u0000\u0000\u0654\u0652\u0001\u0000\u0000\u0000\u0654\u0653\u0001\u0000"+
		"\u0000\u0000\u0655\u0658\u0001\u0000\u0000\u0000\u0656\u0659\u0007\u0002"+
		"\u0000\u0000\u0657\u0659\u0007\u0002\u0000\u0000\u0658\u0656\u0001\u0000"+
		"\u0000\u0000\u0658\u0657\u0001\u0000\u0000\u0000\u0659\u065c\u0001\u0000"+
		"\u0000\u0000\u065a\u065d\u0007\u0007\u0000\u0000\u065b\u065d\u0007\u0007"+
		"\u0000\u0000\u065c\u065a\u0001\u0000\u0000\u0000\u065c\u065b\u0001\u0000"+
		"\u0000\u0000\u065d\u0660\u0001\u0000\u0000\u0000\u065e\u0661\u0007\f\u0000"+
		"\u0000\u065f\u0661\u0007\f\u0000\u0000\u0660\u065e\u0001\u0000\u0000\u0000"+
		"\u0660\u065f\u0001\u0000\u0000\u0000\u0661\u0664\u0001\u0000\u0000\u0000"+
		"\u0662\u0665\u0007\n\u0000\u0000\u0663\u0665\u0007\n\u0000\u0000\u0664"+
		"\u0662\u0001\u0000\u0000\u0000\u0664\u0663\u0001\u0000\u0000\u0000\u0665"+
		"\u0668\u0001\u0000\u0000\u0000\u0666\u0669\u0007\u0006\u0000\u0000\u0667"+
		"\u0669\u0007\u0006\u0000\u0000\u0668\u0666\u0001\u0000\u0000\u0000\u0668"+
		"\u0667\u0001\u0000\u0000\u0000\u0669\u066c\u0001\u0000\u0000\u0000\u066a"+
		"\u066d\u0007\u0002\u0000\u0000\u066b\u066d\u0007\u0002\u0000\u0000\u066c"+
		"\u066a\u0001\u0000\u0000\u0000\u066c\u066b\u0001\u0000\u0000\u0000\u066d"+
		"\u00d9\u0001\u0000\u0000\u0000\u066e\u066f\u0003\u00dcn\u0000\u066f\u0670"+
		"\u0003\u0118\u008c\u0000\u0670\u0671\u0003j5\u0000\u0671\u0674\u0003\u0118"+
		"\u008c\u0000\u0672\u0675\u0003\u00deo\u0000\u0673\u0675\u0003\u00e0p\u0000"+
		"\u0674\u0672\u0001\u0000\u0000\u0000\u0674\u0673\u0001\u0000\u0000\u0000"+
		"\u0675\u00db\u0001\u0000\u0000\u0000\u0676\u0679\u0007\n\u0000\u0000\u0677"+
		"\u0679\u0007\n\u0000\u0000\u0678\u0676\u0001\u0000\u0000\u0000\u0678\u0677"+
		"\u0001\u0000\u0000\u0000\u0679\u067c\u0001\u0000\u0000\u0000\u067a\u067d"+
		"\u0007\u0012\u0000\u0000\u067b\u067d\u0007\u0012\u0000\u0000\u067c\u067a"+
		"\u0001\u0000\u0000\u0000\u067c\u067b\u0001\u0000\u0000\u0000\u067d\u0680"+
		"\u0001\u0000\u0000\u0000\u067e\u0681\u0007\u0012\u0000\u0000\u067f\u0681"+
		"\u0007\u0012\u0000\u0000\u0680\u067e\u0001\u0000\u0000\u0000\u0680\u067f"+
		"\u0001\u0000\u0000\u0000\u0681\u0684\u0001\u0000\u0000\u0000\u0682\u0685"+
		"\u0007\n\u0000\u0000\u0683\u0685\u0007\n\u0000\u0000\u0684\u0682\u0001"+
		"\u0000\u0000\u0000\u0684\u0683\u0001\u0000\u0000\u0000\u0685\u0688\u0001"+
		"\u0000\u0000\u0000\u0686\u0689\u0007\r\u0000\u0000\u0687\u0689\u0007\r"+
		"\u0000\u0000\u0688\u0686\u0001\u0000\u0000\u0000\u0688\u0687\u0001\u0000"+
		"\u0000\u0000\u0689\u068c\u0001\u0000\u0000\u0000\u068a\u068d\u0007\t\u0000"+
		"\u0000\u068b\u068d\u0007\t\u0000\u0000\u068c\u068a\u0001\u0000\u0000\u0000"+
		"\u068c\u068b\u0001\u0000\u0000\u0000\u068d\u0690\u0001\u0000\u0000\u0000"+
		"\u068e\u0691\u0007\u0006\u0000\u0000\u068f\u0691\u0007\u0006\u0000\u0000"+
		"\u0690\u068e\u0001\u0000\u0000\u0000\u0690\u068f\u0001\u0000\u0000\u0000"+
		"\u0691\u0694\u0001\u0000\u0000\u0000\u0692\u0695\u0007\u0013\u0000\u0000"+
		"\u0693\u0695\u0007\u0013\u0000\u0000\u0694\u0692\u0001\u0000\u0000\u0000"+
		"\u0694\u0693\u0001\u0000\u0000\u0000\u0695\u0698\u0001\u0000\u0000\u0000"+
		"\u0696\u0699\u0007\n\u0000\u0000\u0697\u0699\u0007\n\u0000\u0000\u0698"+
		"\u0696\u0001\u0000\u0000\u0000\u0698\u0697\u0001\u0000\u0000\u0000\u0699"+
		"\u069c\u0001\u0000\u0000\u0000\u069a\u069d\u0007\t\u0000\u0000\u069b\u069d"+
		"\u0007\t\u0000\u0000\u069c\u069a\u0001\u0000\u0000\u0000\u069c\u069b\u0001"+
		"\u0000\u0000\u0000\u069d\u06a0\u0001\u0000\u0000\u0000\u069e\u06a1\u0007"+
		"\u0006\u0000\u0000\u069f\u06a1\u0007\u0006\u0000\u0000\u06a0\u069e\u0001"+
		"\u0000\u0000\u0000\u06a0\u069f\u0001\u0000\u0000\u0000\u06a1\u06a4\u0001"+
		"\u0000\u0000\u0000\u06a2\u06a5\u0007\u0005\u0000\u0000\u06a3\u06a5\u0007"+
		"\u0005\u0000\u0000\u06a4\u06a2\u0001\u0000\u0000\u0000\u06a4\u06a3\u0001"+
		"\u0000\u0000\u0000\u06a5\u06a8\u0001\u0000\u0000\u0000\u06a6\u06a9\u0007"+
		"\n\u0000\u0000\u06a7\u06a9\u0007\n\u0000\u0000\u06a8\u06a6\u0001\u0000"+
		"\u0000\u0000\u06a8\u06a7\u0001\u0000\u0000\u0000\u06a9\u00dd\u0001\u0000"+
		"\u0000\u0000\u06aa\u06af\u0003\u012c\u0096\u0000\u06ab\u06ac\u0003\u00e2"+
		"q\u0000\u06ac\u06ad\u0003\u00e4r\u0000\u06ad\u06ae\u0003\u00e6s\u0000"+
		"\u06ae\u06b0\u0001\u0000\u0000\u0000\u06af\u06ab\u0001\u0000\u0000\u0000"+
		"\u06af\u06b0\u0001\u0000\u0000\u0000\u06b0\u06b1\u0001\u0000\u0000\u0000"+
		"\u06b1\u06b2\u0003\u012c\u0096\u0000\u06b2\u00df\u0001\u0000\u0000\u0000"+
		"\u06b3\u06b4\u0005\r\u0000\u0000\u06b4\u06b5\u0003\u0118\u008c\u0000\u06b5"+
		"\u06bb\u0003\u00deo\u0000\u06b6\u06b7\u0003\u011a\u008d\u0000\u06b7\u06b8"+
		"\u0003\u00deo\u0000\u06b8\u06ba\u0001\u0000\u0000\u0000\u06b9\u06b6\u0001"+
		"\u0000\u0000\u0000\u06ba\u06bd\u0001\u0000\u0000\u0000\u06bb\u06b9\u0001"+
		"\u0000\u0000\u0000\u06bb\u06bc\u0001\u0000\u0000\u0000\u06bc\u06be\u0001"+
		"\u0000\u0000\u0000\u06bd\u06bb\u0001\u0000\u0000\u0000\u06be\u06bf\u0003"+
		"\u0118\u008c\u0000\u06bf\u06c0\u0005\u000e\u0000\u0000\u06c0\u00e1\u0001"+
		"\u0000\u0000\u0000\u06c1\u06c2\u0003\u0136\u009b\u0000\u06c2\u06c3\u0003"+
		"\u0132\u0099\u0000\u06c3\u06c4\u0003\u0132\u0099\u0000\u06c4\u06c5\u0003"+
		"\u0132\u0099\u0000\u06c5\u00e3\u0001\u0000\u0000\u0000\u06c6\u06c7\u0005"+
		"\u0015\u0000\u0000\u06c7\u06df\u0005\u0016\u0000\u0000\u06c8\u06c9\u0005"+
		"\u0015\u0000\u0000\u06c9\u06df\u0005\u0017\u0000\u0000\u06ca\u06cb\u0005"+
		"\u0015\u0000\u0000\u06cb\u06df\u0005\u0018\u0000\u0000\u06cc\u06cd\u0005"+
		"\u0015\u0000\u0000\u06cd\u06df\u0005\u0019\u0000\u0000\u06ce\u06cf\u0005"+
		"\u0015\u0000\u0000\u06cf\u06df\u0005\u001a\u0000\u0000\u06d0\u06d1\u0005"+
		"\u0015\u0000\u0000\u06d1\u06df\u0005\u001b\u0000\u0000\u06d2\u06d3\u0005"+
		"\u0015\u0000\u0000\u06d3\u06df\u0005\u001c\u0000\u0000\u06d4\u06d5\u0005"+
		"\u0015\u0000\u0000\u06d5\u06df\u0005\u001d\u0000\u0000\u06d6\u06d7\u0005"+
		"\u0015\u0000\u0000\u06d7\u06df\u0005\u001e\u0000\u0000\u06d8\u06d9\u0005"+
		"\u0016\u0000\u0000\u06d9\u06df\u0005\u0015\u0000\u0000\u06da\u06db\u0005"+
		"\u0016\u0000\u0000\u06db\u06df\u0005\u0016\u0000\u0000\u06dc\u06dd\u0005"+
		"\u0016\u0000\u0000\u06dd\u06df\u0005\u0017\u0000\u0000\u06de\u06c6\u0001"+
		"\u0000\u0000\u0000\u06de\u06c8\u0001\u0000\u0000\u0000\u06de\u06ca\u0001"+
		"\u0000\u0000\u0000\u06de\u06cc\u0001\u0000\u0000\u0000\u06de\u06ce\u0001"+
		"\u0000\u0000\u0000\u06de\u06d0\u0001\u0000\u0000\u0000\u06de\u06d2\u0001"+
		"\u0000\u0000\u0000\u06de\u06d4\u0001\u0000\u0000\u0000\u06de\u06d6\u0001"+
		"\u0000\u0000\u0000\u06de\u06d8\u0001\u0000\u0000\u0000\u06de\u06da\u0001"+
		"\u0000\u0000\u0000\u06de\u06dc\u0001\u0000\u0000\u0000\u06df\u00e5\u0001"+
		"\u0000\u0000\u0000\u06e0\u06e1\u0005\u0015\u0000\u0000\u06e1\u071f\u0005"+
		"\u0016\u0000\u0000\u06e2\u06e3\u0005\u0015\u0000\u0000\u06e3\u071f\u0005"+
		"\u0017\u0000\u0000\u06e4\u06e5\u0005\u0015\u0000\u0000\u06e5\u071f\u0005"+
		"\u0018\u0000\u0000\u06e6\u06e7\u0005\u0015\u0000\u0000\u06e7\u071f\u0005"+
		"\u0019\u0000\u0000\u06e8\u06e9\u0005\u0015\u0000\u0000\u06e9\u071f\u0005"+
		"\u001a\u0000\u0000\u06ea\u06eb\u0005\u0015\u0000\u0000\u06eb\u071f\u0005"+
		"\u001b\u0000\u0000\u06ec\u06ed\u0005\u0015\u0000\u0000\u06ed\u071f\u0005"+
		"\u001c\u0000\u0000\u06ee\u06ef\u0005\u0015\u0000\u0000\u06ef\u071f\u0005"+
		"\u001d\u0000\u0000\u06f0\u06f1\u0005\u0015\u0000\u0000\u06f1\u071f\u0005"+
		"\u001e\u0000\u0000\u06f2\u06f3\u0005\u0016\u0000\u0000\u06f3\u071f\u0005"+
		"\u0015\u0000\u0000\u06f4\u06f5\u0005\u0016\u0000\u0000\u06f5\u071f\u0005"+
		"\u0016\u0000\u0000\u06f6\u06f7\u0005\u0016\u0000\u0000\u06f7\u071f\u0005"+
		"\u0017\u0000\u0000\u06f8\u06f9\u0005\u0016\u0000\u0000\u06f9\u071f\u0005"+
		"\u0018\u0000\u0000\u06fa\u06fb\u0005\u0016\u0000\u0000\u06fb\u071f\u0005"+
		"\u0019\u0000\u0000\u06fc\u06fd\u0005\u0016\u0000\u0000\u06fd\u071f\u0005"+
		"\u001a\u0000\u0000\u06fe\u06ff\u0005\u0016\u0000\u0000\u06ff\u071f\u0005"+
		"\u001b\u0000\u0000\u0700\u0701\u0005\u0016\u0000\u0000\u0701\u071f\u0005"+
		"\u001c\u0000\u0000\u0702\u0703\u0005\u0016\u0000\u0000\u0703\u071f\u0005"+
		"\u001d\u0000\u0000\u0704\u0705\u0005\u0016\u0000\u0000\u0705\u071f\u0005"+
		"\u001e\u0000\u0000\u0706\u0707\u0005\u0017\u0000\u0000\u0707\u071f\u0005"+
		"\u0015\u0000\u0000\u0708\u0709\u0005\u0017\u0000\u0000\u0709\u071f\u0005"+
		"\u0016\u0000\u0000\u070a\u070b\u0005\u0017\u0000\u0000\u070b\u071f\u0005"+
		"\u0017\u0000\u0000\u070c\u070d\u0005\u0017\u0000\u0000\u070d\u071f\u0005"+
		"\u0018\u0000\u0000\u070e\u070f\u0005\u0017\u0000\u0000\u070f\u071f\u0005"+
		"\u0019\u0000\u0000\u0710\u0711\u0005\u0017\u0000\u0000\u0711\u071f\u0005"+
		"\u001a\u0000\u0000\u0712\u0713\u0005\u0017\u0000\u0000\u0713\u071f\u0005"+
		"\u001b\u0000\u0000\u0714\u0715\u0005\u0017\u0000\u0000\u0715\u071f\u0005"+
		"\u001c\u0000\u0000\u0716\u0717\u0005\u0017\u0000\u0000\u0717\u071f\u0005"+
		"\u001d\u0000\u0000\u0718\u0719\u0005\u0017\u0000\u0000\u0719\u071f\u0005"+
		"\u001e\u0000\u0000\u071a\u071b\u0005\u0018\u0000\u0000\u071b\u071f\u0005"+
		"\u0015\u0000\u0000\u071c\u071d\u0005\u0018\u0000\u0000\u071d\u071f\u0005"+
		"\u0016\u0000\u0000\u071e\u06e0\u0001\u0000\u0000\u0000\u071e\u06e2\u0001"+
		"\u0000\u0000\u0000\u071e\u06e4\u0001\u0000\u0000\u0000\u071e\u06e6\u0001"+
		"\u0000\u0000\u0000\u071e\u06e8\u0001\u0000\u0000\u0000\u071e\u06ea\u0001"+
		"\u0000\u0000\u0000\u071e\u06ec\u0001\u0000\u0000\u0000\u071e\u06ee\u0001"+
		"\u0000\u0000\u0000\u071e\u06f0\u0001\u0000\u0000\u0000\u071e\u06f2\u0001"+
		"\u0000\u0000\u0000\u071e\u06f4\u0001\u0000\u0000\u0000\u071e\u06f6\u0001"+
		"\u0000\u0000\u0000\u071e\u06f8\u0001\u0000\u0000\u0000\u071e\u06fa\u0001"+
		"\u0000\u0000\u0000\u071e\u06fc\u0001\u0000\u0000\u0000\u071e\u06fe\u0001"+
		"\u0000\u0000\u0000\u071e\u0700\u0001\u0000\u0000\u0000\u071e\u0702\u0001"+
		"\u0000\u0000\u0000\u071e\u0704\u0001\u0000\u0000\u0000\u071e\u0706\u0001"+
		"\u0000\u0000\u0000\u071e\u0708\u0001\u0000\u0000\u0000\u071e\u070a\u0001"+
		"\u0000\u0000\u0000\u071e\u070c\u0001\u0000\u0000\u0000\u071e\u070e\u0001"+
		"\u0000\u0000\u0000\u071e\u0710\u0001\u0000\u0000\u0000\u071e\u0712\u0001"+
		"\u0000\u0000\u0000\u071e\u0714\u0001\u0000\u0000\u0000\u071e\u0716\u0001"+
		"\u0000\u0000\u0000\u071e\u0718\u0001\u0000\u0000\u0000\u071e\u071a\u0001"+
		"\u0000\u0000\u0000\u071e\u071c\u0001\u0000\u0000\u0000\u071f\u00e7\u0001"+
		"\u0000\u0000\u0000\u0720\u0721\u0003\u00eau\u0000\u0721\u0722\u0003\u0118"+
		"\u008c\u0000\u0722\u0723\u0003n7\u0000\u0723\u0724\u0003\u0118\u008c\u0000"+
		"\u0724\u0725\u0003\u00ecv\u0000\u0725\u00e9\u0001\u0000\u0000\u0000\u0726"+
		"\u0729\u0007\u0000\u0000\u0000\u0727\u0729\u0007\u0000\u0000\u0000\u0728"+
		"\u0726\u0001\u0000\u0000\u0000\u0728\u0727\u0001\u0000\u0000\u0000\u0729"+
		"\u072c\u0001\u0000\u0000\u0000\u072a\u072d\u0007\r\u0000\u0000\u072b\u072d"+
		"\u0007\r\u0000\u0000\u072c\u072a\u0001\u0000\u0000\u0000\u072c\u072b\u0001"+
		"\u0000\u0000\u0000\u072d\u0730\u0001\u0000\u0000\u0000\u072e\u0731\u0007"+
		"\t\u0000\u0000\u072f\u0731\u0007\t\u0000\u0000\u0730\u072e\u0001\u0000"+
		"\u0000\u0000\u0730\u072f\u0001\u0000\u0000\u0000\u0731\u0734\u0001\u0000"+
		"\u0000\u0000\u0732\u0735\u0007\u0006\u0000\u0000\u0733\u0735\u0007\u0006"+
		"\u0000\u0000\u0734\u0732\u0001\u0000\u0000\u0000\u0734\u0733\u0001\u0000"+
		"\u0000\u0000\u0735\u0738\u0001\u0000\u0000\u0000\u0736\u0739\u0007\u0013"+
		"\u0000\u0000\u0737\u0739\u0007\u0013\u0000\u0000\u0738\u0736\u0001\u0000"+
		"\u0000\u0000\u0738\u0737\u0001\u0000\u0000\u0000\u0739\u073c\u0001\u0000"+
		"\u0000\u0000\u073a\u073d\u0007\n\u0000\u0000\u073b\u073d\u0007\n\u0000"+
		"\u0000\u073c\u073a\u0001\u0000\u0000\u0000\u073c\u073b\u0001\u0000\u0000"+
		"\u0000\u073d\u00eb\u0001\u0000\u0000\u0000\u073e\u0741\u0003\u00eew\u0000"+
		"\u073f\u0741\u0003\u00f0x\u0000\u0740\u073e\u0001\u0000\u0000\u0000\u0740"+
		"\u073f\u0001\u0000\u0000\u0000\u0741\u00ed\u0001\u0000\u0000\u0000\u0742"+
		"\u0748\u0005\u0016\u0000\u0000\u0743\u0744\u0007\t\u0000\u0000\u0744\u0745"+
		"\u0007\u0004\u0000\u0000\u0745\u0746\u0007\u0007\u0000\u0000\u0746\u0748"+
		"\u0007\n\u0000\u0000\u0747\u0742\u0001\u0000\u0000\u0000\u0747\u0743\u0001"+
		"\u0000\u0000\u0000\u0748\u00ef\u0001\u0000\u0000\u0000\u0749\u0750\u0005"+
		"\u0015\u0000\u0000\u074a\u074b\u0007\u0012\u0000\u0000\u074b\u074c\u0007"+
		"\u0000\u0000\u0000\u074c\u074d\u0007\f\u0000\u0000\u074d\u074e\u0007\b"+
		"\u0000\u0000\u074e\u0750\u0007\n\u0000\u0000\u074f\u0749\u0001\u0000\u0000"+
		"\u0000\u074f\u074a\u0001\u0000\u0000\u0000\u0750\u00f1\u0001\u0000\u0000"+
		"\u0000\u0751\u0752\u0005`\u0000\u0000\u0752\u0753\u0005`\u0000\u0000\u0753"+
		"\u0754\u0001\u0000\u0000\u0000\u0754\u0757\u0003\u0118\u008c\u0000\u0755"+
		"\u0758\u0007\u0005\u0000\u0000\u0756\u0758\u0007\u0005\u0000\u0000\u0757"+
		"\u0755\u0001\u0000\u0000\u0000\u0757\u0756\u0001\u0000\u0000\u0000\u0758"+
		"\u0759\u0001\u0000\u0000\u0000\u0759\u075a\u0003\u0118\u008c\u0000\u075a"+
		"\u0762\u0003\u00f4z\u0000\u075b\u075c\u0003\u0118\u008c\u0000\u075c\u075d"+
		"\u0005\u0011\u0000\u0000\u075d\u075e\u0003\u0118\u008c\u0000\u075e\u075f"+
		"\u0003\u00f4z\u0000\u075f\u0761\u0001\u0000\u0000\u0000\u0760\u075b\u0001"+
		"\u0000\u0000\u0000\u0761\u0764\u0001\u0000\u0000\u0000\u0762\u0760\u0001"+
		"\u0000\u0000\u0000\u0762\u0763\u0001\u0000\u0000\u0000\u0763\u0765\u0001"+
		"\u0000\u0000\u0000\u0764\u0762\u0001\u0000\u0000\u0000\u0765\u0766\u0003"+
		"\u0118\u008c\u0000\u0766\u0767\u0005b\u0000\u0000\u0767\u0768\u0005b\u0000"+
		"\u0000\u0768\u00f3\u0001\u0000\u0000\u0000\u0769\u076e\u0003\u00d6k\u0000"+
		"\u076a\u076e\u0003\u00dam\u0000\u076b\u076e\u0003\u00e8t\u0000\u076c\u076e"+
		"\u0003\u00f6{\u0000\u076d\u0769\u0001\u0000\u0000\u0000\u076d\u076a\u0001"+
		"\u0000\u0000\u0000\u076d\u076b\u0001\u0000\u0000\u0000\u076d\u076c\u0001"+
		"\u0000\u0000\u0000\u076e\u00f5\u0001\u0000\u0000\u0000\u076f\u0770\u0003"+
		"\u001e\u000f\u0000\u0770\u078b\u0003\u0118\u008c\u0000\u0771\u0772\u0003"+
		"f3\u0000\u0772\u0773\u0003\u0118\u008c\u0000\u0773\u0774\u0003\u0012\t"+
		"\u0000\u0774\u078c\u0001\u0000\u0000\u0000\u0775\u0776\u0003h4\u0000\u0776"+
		"\u0777\u0003\u0118\u008c\u0000\u0777\u0778\u0005\b\u0000\u0000\u0778\u0779"+
		"\u0003\u0106\u0083\u0000\u0779\u078c\u0001\u0000\u0000\u0000\u077a\u077b"+
		"\u0003l6\u0000\u077b\u077e\u0003\u0118\u008c\u0000\u077c\u077f\u0003x"+
		"<\u0000\u077d\u077f\u0003z=\u0000\u077e\u077c\u0001\u0000\u0000\u0000"+
		"\u077e\u077d\u0001\u0000\u0000\u0000\u077f\u078c\u0001\u0000\u0000\u0000"+
		"\u0780\u0781\u0003n7\u0000\u0781\u0782\u0003\u0118\u008c\u0000\u0782\u0783"+
		"\u0003\u010e\u0087\u0000\u0783\u078c\u0001\u0000\u0000\u0000\u0784\u0785"+
		"\u0003\u0118\u008c\u0000\u0785\u0786\u0003j5\u0000\u0786\u0789\u0003\u0118"+
		"\u008c\u0000\u0787\u078a\u0003\u00deo\u0000\u0788\u078a\u0003\u00e0p\u0000"+
		"\u0789\u0787\u0001\u0000\u0000\u0000\u0789\u0788\u0001\u0000\u0000\u0000"+
		"\u078a\u078c\u0001\u0000\u0000\u0000\u078b\u0771\u0001\u0000\u0000\u0000"+
		"\u078b\u0775\u0001\u0000\u0000\u0000\u078b\u077a\u0001\u0000\u0000\u0000"+
		"\u078b\u0780\u0001\u0000\u0000\u0000\u078b\u0784\u0001\u0000\u0000\u0000"+
		"\u078c\u00f7\u0001\u0000\u0000\u0000\u078d\u078e\u0005`\u0000\u0000\u078e"+
		"\u078f\u0005`\u0000\u0000\u078f\u0790\u0001\u0000\u0000\u0000\u0790\u0791"+
		"\u0003\u0118\u008c\u0000\u0791\u0792\u0005\u0010\u0000\u0000\u0792\u0793"+
		"\u0003\u0118\u008c\u0000\u0793\u0798\u0003\u00fa}\u0000\u0794\u0799\u0003"+
		"\u00fc~\u0000\u0795\u0796\u0003\u0118\u008c\u0000\u0796\u0797\u0003\u0104"+
		"\u0082\u0000\u0797\u0799\u0001\u0000\u0000\u0000\u0798\u0794\u0001\u0000"+
		"\u0000\u0000\u0798\u0795\u0001\u0000\u0000\u0000\u0798\u0799\u0001\u0000"+
		"\u0000\u0000\u0799\u079a\u0001\u0000\u0000\u0000\u079a\u079b\u0003\u0118"+
		"\u008c\u0000\u079b\u079c\u0005b\u0000\u0000\u079c\u079d\u0005b\u0000\u0000"+
		"\u079d\u00f9\u0001\u0000\u0000\u0000\u079e\u07a1\u0007\u000e\u0000\u0000"+
		"\u079f\u07a1\u0007\u000e\u0000\u0000\u07a0\u079e\u0001\u0000\u0000\u0000"+
		"\u07a0\u079f\u0001\u0000\u0000\u0000\u07a1\u07a4\u0001\u0000\u0000\u0000"+
		"\u07a2\u07a5\u0007\u0006\u0000\u0000\u07a3\u07a5\u0007\u0006\u0000\u0000"+
		"\u07a4\u07a2\u0001\u0000\u0000\u0000\u07a4\u07a3\u0001\u0000\u0000\u0000"+
		"\u07a5\u07a8\u0001\u0000\u0000\u0000\u07a6\u07a9\u0007\b\u0000\u0000\u07a7"+
		"\u07a9\u0007\b\u0000\u0000\u07a8\u07a6\u0001\u0000\u0000\u0000\u07a8\u07a7"+
		"\u0001\u0000\u0000\u0000\u07a9\u07ac\u0001\u0000\u0000\u0000\u07aa\u07ad"+
		"\u0007\t\u0000\u0000\u07ab\u07ad\u0007\t\u0000\u0000\u07ac\u07aa\u0001"+
		"\u0000\u0000\u0000\u07ac\u07ab\u0001\u0000\u0000\u0000\u07ad\u07b0\u0001"+
		"\u0000\u0000\u0000\u07ae\u07b1\u0007\u0003\u0000\u0000\u07af\u07b1\u0007"+
		"\u0003\u0000\u0000\u07b0\u07ae\u0001\u0000\u0000\u0000\u07b0\u07af\u0001"+
		"\u0000\u0000\u0000\u07b1\u07b4\u0001\u0000\u0000\u0000\u07b2\u07b5\u0007"+
		"\u0004\u0000\u0000\u07b3\u07b5\u0007\u0004\u0000\u0000\u07b4\u07b2\u0001"+
		"\u0000\u0000\u0000\u07b4\u07b3\u0001\u0000\u0000\u0000\u07b5\u07b8\u0001"+
		"\u0000\u0000\u0000\u07b6\u07b9\u0007\u0010\u0000\u0000\u07b7\u07b9\u0007"+
		"\u0010\u0000\u0000\u07b8\u07b6\u0001\u0000\u0000\u0000\u07b8\u07b7\u0001"+
		"\u0000\u0000\u0000\u07b9\u00fb\u0001\u0000\u0000\u0000\u07ba\u07be\u0003"+
		"\u00fe\u007f\u0000\u07bb\u07be\u0003\u0100\u0080\u0000\u07bc\u07be\u0003"+
		"\u0102\u0081\u0000\u07bd\u07ba\u0001\u0000\u0000\u0000\u07bd\u07bb\u0001"+
		"\u0000\u0000\u0000\u07bd\u07bc\u0001\u0000\u0000\u0000\u07be\u00fd\u0001"+
		"\u0000\u0000\u0000\u07bf\u07c2\u0007\u0014\u0000\u0000\u07c0\u07c3\u0007"+
		"\u0005\u0000\u0000\u07c1\u07c3\u0007\u0005\u0000\u0000\u07c2\u07c0\u0001"+
		"\u0000\u0000\u0000\u07c2\u07c1\u0001\u0000\u0000\u0000\u07c3\u07c6\u0001"+
		"\u0000\u0000\u0000\u07c4\u07c7\u0007\u0006\u0000\u0000\u07c5\u07c7\u0007"+
		"\u0006\u0000\u0000\u07c6\u07c4\u0001\u0000\u0000\u0000\u07c6\u07c5\u0001"+
		"\u0000\u0000\u0000\u07c7\u07ca\u0001\u0000\u0000\u0000\u07c8\u07cb\u0007"+
		"\u0001\u0000\u0000\u07c9\u07cb\u0007\u0001\u0000\u0000\u07ca\u07c8\u0001"+
		"\u0000\u0000\u0000\u07ca\u07c9\u0001\u0000\u0000\u0000\u07cb\u00ff\u0001"+
		"\u0000\u0000\u0000\u07cc\u07cf\u0007\u0014\u0000\u0000\u07cd\u07d0\u0007"+
		"\u0005\u0000\u0000\u07ce\u07d0\u0007\u0005\u0000\u0000\u07cf\u07cd\u0001"+
		"\u0000\u0000\u0000\u07cf\u07ce\u0001\u0000\u0000\u0000\u07d0\u07d3\u0001"+
		"\u0000\u0000\u0000\u07d1\u07d4\u0007\u0003\u0000\u0000\u07d2\u07d4\u0007"+
		"\u0003\u0000\u0000\u07d3\u07d1\u0001\u0000\u0000\u0000\u07d3\u07d2\u0001"+
		"\u0000\u0000\u0000\u07d4\u07d7\u0001\u0000\u0000\u0000\u07d5\u07d8\u0007"+
		"\u0002\u0000\u0000\u07d6\u07d8\u0007\u0002\u0000\u0000\u07d7\u07d5\u0001"+
		"\u0000\u0000\u0000\u07d7\u07d6\u0001\u0000\u0000\u0000\u07d8\u0101\u0001"+
		"\u0000\u0000\u0000\u07d9\u07dc\u0007\u0014\u0000\u0000\u07da\u07dd\u0007"+
		"\u0005\u0000\u0000\u07db\u07dd\u0007\u0005\u0000\u0000\u07dc\u07da\u0001"+
		"\u0000\u0000\u0000\u07dc\u07db\u0001\u0000\u0000\u0000\u07dd\u07e0\u0001"+
		"\u0000\u0000\u0000\u07de\u07e1\u0007\u0000\u0000\u0000\u07df\u07e1\u0007"+
		"\u0000\u0000\u0000\u07e0\u07de\u0001\u0000\u0000\u0000\u07e0\u07df\u0001"+
		"\u0000\u0000\u0000\u07e1\u07e4\u0001\u0000\u0000\u0000\u07e2\u07e5\u0007"+
		"\u0015\u0000\u0000\u07e3\u07e5\u0007\u0015\u0000\u0000\u07e4\u07e2\u0001"+
		"\u0000\u0000\u0000\u07e4\u07e3\u0001\u0000\u0000\u0000\u07e5\u0103\u0001"+
		"\u0000\u0000\u0000\u07e6\u07e7\u0005\r\u0000\u0000\u07e7\u07e8\u0003\u0118"+
		"\u008c\u0000\u07e8\u07e9\u0003\u0002\u0001\u0000\u07e9\u07ea\u0003\u0118"+
		"\u008c\u0000\u07ea\u07eb\u0005\u000e\u0000\u0000\u07eb\u0105\u0001\u0000"+
		"\u0000\u0000\u07ec\u07ee\u0007\u0016\u0000\u0000\u07ed\u07ec\u0001\u0000"+
		"\u0000\u0000\u07ed\u07ee\u0001\u0000\u0000\u0000\u07ee\u07f1\u0001\u0000"+
		"\u0000\u0000\u07ef\u07f2\u0003\u010c\u0086\u0000\u07f0\u07f2\u0003\u010a"+
		"\u0085\u0000\u07f1\u07ef\u0001\u0000\u0000\u0000\u07f1\u07f0\u0001\u0000"+
		"\u0000\u0000\u07f2\u0107\u0001\u0000\u0000\u0000\u07f3\u07f6\u0003\u013a"+
		"\u009d\u0000\u07f4\u07f6\u0003\u013c\u009e\u0000\u07f5\u07f3\u0001\u0000"+
		"\u0000\u0000\u07f5\u07f4\u0001\u0000\u0000\u0000\u07f6\u07f7\u0001\u0000"+
		"\u0000\u0000\u07f7\u07f5\u0001\u0000\u0000\u0000\u07f7\u07f8\u0001\u0000"+
		"\u0000\u0000\u07f8\u0109\u0001\u0000\u0000\u0000\u07f9\u07fd\u0003\u0136"+
		"\u009b\u0000\u07fa\u07fc\u0003\u0132\u0099\u0000\u07fb\u07fa\u0001\u0000"+
		"\u0000\u0000\u07fc\u07ff\u0001\u0000\u0000\u0000\u07fd\u07fb\u0001\u0000"+
		"\u0000\u0000\u07fd\u07fe\u0001\u0000\u0000\u0000\u07fe\u0802\u0001\u0000"+
		"\u0000\u0000\u07ff\u07fd\u0001\u0000\u0000\u0000\u0800\u0802\u0003\u0134"+
		"\u009a\u0000\u0801\u07f9\u0001\u0000\u0000\u0000\u0801\u0800\u0001\u0000"+
		"\u0000\u0000\u0802\u010b\u0001\u0000\u0000\u0000\u0803\u0804\u0003\u010a"+
		"\u0085\u0000\u0804\u0806\u0005\u0013\u0000\u0000\u0805\u0807\u0003\u0132"+
		"\u0099\u0000\u0806\u0805\u0001\u0000\u0000\u0000\u0807\u0808\u0001\u0000"+
		"\u0000\u0000\u0808\u0806\u0001\u0000\u0000\u0000\u0808\u0809\u0001\u0000"+
		"\u0000\u0000\u0809\u010d\u0001\u0000\u0000\u0000\u080a\u080d\u0003\u0110"+
		"\u0088\u0000\u080b\u080d\u0003\u0112\u0089\u0000\u080c\u080a\u0001\u0000"+
		"\u0000\u0000\u080c\u080b\u0001\u0000\u0000\u0000\u080d\u010f\u0001\u0000"+
		"\u0000\u0000\u080e\u0811\u0007\t\u0000\u0000\u080f\u0811\u0007\t\u0000"+
		"\u0000\u0810\u080e\u0001\u0000\u0000\u0000\u0810\u080f\u0001\u0000\u0000"+
		"\u0000\u0811\u0814\u0001\u0000\u0000\u0000\u0812\u0815\u0007\u0004\u0000"+
		"\u0000\u0813\u0815\u0007\u0004\u0000\u0000\u0814\u0812\u0001\u0000\u0000"+
		"\u0000\u0814\u0813\u0001\u0000\u0000\u0000\u0815\u0818\u0001\u0000\u0000"+
		"\u0000\u0816\u0819\u0007\u0007\u0000\u0000\u0817\u0819\u0007\u0007\u0000"+
		"\u0000\u0818\u0816\u0001\u0000\u0000\u0000\u0818\u0817\u0001\u0000\u0000"+
		"\u0000\u0819\u081c\u0001\u0000\u0000\u0000\u081a\u081d\u0007\n\u0000\u0000"+
		"\u081b\u081d\u0007\n\u0000\u0000\u081c\u081a\u0001\u0000\u0000\u0000\u081c"+
		"\u081b\u0001\u0000\u0000\u0000\u081d\u0111\u0001\u0000\u0000\u0000\u081e"+
		"\u0821\u0007\u0012\u0000\u0000\u081f\u0821\u0007\u0012\u0000\u0000\u0820"+
		"\u081e\u0001\u0000\u0000\u0000\u0820\u081f\u0001\u0000\u0000\u0000\u0821"+
		"\u0824\u0001\u0000\u0000\u0000\u0822\u0825\u0007\u0000\u0000\u0000\u0823"+
		"\u0825\u0007\u0000\u0000\u0000\u0824\u0822\u0001\u0000\u0000\u0000\u0824"+
		"\u0823\u0001\u0000\u0000\u0000\u0825\u0828\u0001\u0000\u0000\u0000\u0826"+
		"\u0829\u0007\f\u0000\u0000\u0827\u0829\u0007\f\u0000\u0000\u0828\u0826"+
		"\u0001\u0000\u0000\u0000\u0828\u0827\u0001\u0000\u0000\u0000\u0829\u082c"+
		"\u0001\u0000\u0000\u0000\u082a\u082d\u0007\b\u0000\u0000\u082b\u082d\u0007"+
		"\b\u0000\u0000\u082c\u082a\u0001\u0000\u0000\u0000\u082c\u082b\u0001\u0000"+
		"\u0000\u0000\u082d\u0830\u0001\u0000\u0000\u0000\u082e\u0831\u0007\n\u0000"+
		"\u0000\u082f\u0831\u0007\n\u0000\u0000\u0830\u082e\u0001\u0000\u0000\u0000"+
		"\u0830\u082f\u0001\u0000\u0000\u0000\u0831\u0113\u0001\u0000\u0000\u0000"+
		"\u0832\u0836\u0003\u0136\u009b\u0000\u0833\u0835\u0003\u0132\u0099\u0000"+
		"\u0834\u0833\u0001\u0000\u0000\u0000\u0835\u0838\u0001\u0000\u0000\u0000"+
		"\u0836\u0834\u0001\u0000\u0000\u0000\u0836\u0837\u0001\u0000\u0000\u0000"+
		"\u0837\u083b\u0001\u0000\u0000\u0000\u0838\u0836\u0001\u0000\u0000\u0000"+
		"\u0839\u083b\u0003\u0134\u009a\u0000\u083a\u0832\u0001\u0000\u0000\u0000"+
		"\u083a\u0839\u0001\u0000\u0000\u0000\u083b\u0115\u0001\u0000\u0000\u0000"+
		"\u083c\u083d\u0003\u0136\u009b\u0000\u083d\u083e\u0003\u0132\u0099\u0000"+
		"\u083e\u083f\u0003\u0132\u0099\u0000\u083f\u0840\u0003\u0132\u0099\u0000"+
		"\u0840\u0841\u0003\u0132\u0099\u0000\u0841\u089d\u0003\u0132\u0099\u0000"+
		"\u0842\u0843\u0003\u0132\u0099\u0000\u0843\u0844\u0003\u0132\u0099\u0000"+
		"\u0844\u0845\u0003\u0132\u0099\u0000\u0845\u0846\u0003\u0132\u0099\u0000"+
		"\u0846\u0847\u0003\u0132\u0099\u0000\u0847\u0848\u0003\u0132\u0099\u0000"+
		"\u0848\u0849\u0003\u0132\u0099\u0000\u0849\u084a\u0003\u0132\u0099\u0000"+
		"\u084a\u084b\u0003\u0132\u0099\u0000\u084b\u084c\u0003\u0132\u0099\u0000"+
		"\u084c\u084d\u0003\u0132\u0099\u0000\u084d\u084e\u0003\u0132\u0099\u0000"+
		"\u084e\u089e\u0001\u0000\u0000\u0000\u084f\u0850\u0003\u0132\u0099\u0000"+
		"\u0850\u0851\u0003\u0132\u0099\u0000\u0851\u0852\u0003\u0132\u0099\u0000"+
		"\u0852\u0853\u0003\u0132\u0099\u0000\u0853\u0854\u0003\u0132\u0099\u0000"+
		"\u0854\u0855\u0003\u0132\u0099\u0000\u0855\u0856\u0003\u0132\u0099\u0000"+
		"\u0856\u0857\u0003\u0132\u0099\u0000\u0857\u0858\u0003\u0132\u0099\u0000"+
		"\u0858\u0859\u0003\u0132\u0099\u0000\u0859\u085a\u0003\u0132\u0099\u0000"+
		"\u085a\u089e\u0001\u0000\u0000\u0000\u085b\u085c\u0003\u0132\u0099\u0000"+
		"\u085c\u085d\u0003\u0132\u0099\u0000\u085d\u085e\u0003\u0132\u0099\u0000"+
		"\u085e\u085f\u0003\u0132\u0099\u0000\u085f\u0860\u0003\u0132\u0099\u0000"+
		"\u0860\u0861\u0003\u0132\u0099\u0000\u0861\u0862\u0003\u0132\u0099\u0000"+
		"\u0862\u0863\u0003\u0132\u0099\u0000\u0863\u0864\u0003\u0132\u0099\u0000"+
		"\u0864\u0865\u0003\u0132\u0099\u0000\u0865\u089e\u0001\u0000\u0000\u0000"+
		"\u0866\u0867\u0003\u0132\u0099\u0000\u0867\u0868\u0003\u0132\u0099\u0000"+
		"\u0868\u0869\u0003\u0132\u0099\u0000\u0869\u086a\u0003\u0132\u0099\u0000"+
		"\u086a\u086b\u0003\u0132\u0099\u0000\u086b\u086c\u0003\u0132\u0099\u0000"+
		"\u086c\u086d\u0003\u0132\u0099\u0000\u086d\u086e\u0003\u0132\u0099\u0000"+
		"\u086e\u086f\u0003\u0132\u0099\u0000\u086f\u089e\u0001\u0000\u0000\u0000"+
		"\u0870\u0871\u0003\u0132\u0099\u0000\u0871\u0872\u0003\u0132\u0099\u0000"+
		"\u0872\u0873\u0003\u0132\u0099\u0000\u0873\u0874\u0003\u0132\u0099\u0000"+
		"\u0874\u0875\u0003\u0132\u0099\u0000\u0875\u0876\u0003\u0132\u0099\u0000"+
		"\u0876\u0877\u0003\u0132\u0099\u0000\u0877\u0878\u0003\u0132\u0099\u0000"+
		"\u0878\u089e\u0001\u0000\u0000\u0000\u0879\u087a\u0003\u0132\u0099\u0000"+
		"\u087a\u087b\u0003\u0132\u0099\u0000\u087b\u087c\u0003\u0132\u0099\u0000"+
		"\u087c\u087d\u0003\u0132\u0099\u0000\u087d\u087e\u0003\u0132\u0099\u0000"+
		"\u087e\u087f\u0003\u0132\u0099\u0000\u087f\u0880\u0003\u0132\u0099\u0000"+
		"\u0880\u089e\u0001\u0000\u0000\u0000\u0881\u0882\u0003\u0132\u0099\u0000"+
		"\u0882\u0883\u0003\u0132\u0099\u0000\u0883\u0884\u0003\u0132\u0099\u0000"+
		"\u0884\u0885\u0003\u0132\u0099\u0000\u0885\u0886\u0003\u0132\u0099\u0000"+
		"\u0886\u0887\u0003\u0132\u0099\u0000\u0887\u089e\u0001\u0000\u0000\u0000"+
		"\u0888\u0889\u0003\u0132\u0099\u0000\u0889\u088a\u0003\u0132\u0099\u0000"+
		"\u088a\u088b\u0003\u0132\u0099\u0000\u088b\u088c\u0003\u0132\u0099\u0000"+
		"\u088c\u088d\u0003\u0132\u0099\u0000\u088d\u089e\u0001\u0000\u0000\u0000"+
		"\u088e\u088f\u0003\u0132\u0099\u0000\u088f\u0890\u0003\u0132\u0099\u0000"+
		"\u0890\u0891\u0003\u0132\u0099\u0000\u0891\u0892\u0003\u0132\u0099\u0000"+
		"\u0892\u089e\u0001\u0000\u0000\u0000\u0893\u0894\u0003\u0132\u0099\u0000"+
		"\u0894\u0895\u0003\u0132\u0099\u0000\u0895\u0896\u0003\u0132\u0099\u0000"+
		"\u0896\u089e\u0001\u0000\u0000\u0000\u0897\u0898\u0003\u0132\u0099\u0000"+
		"\u0898\u0899\u0003\u0132\u0099\u0000\u0899\u089e\u0001\u0000\u0000\u0000"+
		"\u089a\u089c\u0003\u0132\u0099\u0000\u089b\u089a\u0001\u0000\u0000\u0000"+
		"\u089b\u089c\u0001\u0000\u0000\u0000\u089c\u089e\u0001\u0000\u0000\u0000"+
		"\u089d\u0842\u0001\u0000\u0000\u0000\u089d\u084f\u0001\u0000\u0000\u0000"+
		"\u089d\u085b\u0001\u0000\u0000\u0000\u089d\u0866\u0001\u0000\u0000\u0000"+
		"\u089d\u0870\u0001\u0000\u0000\u0000\u089d\u0879\u0001\u0000\u0000\u0000"+
		"\u089d\u0881\u0001\u0000\u0000\u0000\u089d\u0888\u0001\u0000\u0000\u0000"+
		"\u089d\u088e\u0001\u0000\u0000\u0000\u089d\u0893\u0001\u0000\u0000\u0000"+
		"\u089d\u0897\u0001\u0000\u0000\u0000\u089d\u089b\u0001\u0000\u0000\u0000"+
		"\u089e\u0117\u0001\u0000\u0000\u0000\u089f\u08a5\u0003\u0124\u0092\u0000"+
		"\u08a0\u08a5\u0003\u0126\u0093\u0000\u08a1\u08a5\u0003\u0128\u0094\u0000"+
		"\u08a2\u08a5\u0003\u012a\u0095\u0000\u08a3\u08a5\u0003\u011c\u008e\u0000"+
		"\u08a4\u089f\u0001\u0000\u0000\u0000\u08a4\u08a0\u0001\u0000\u0000\u0000"+
		"\u08a4\u08a1\u0001\u0000\u0000\u0000\u08a4\u08a2\u0001\u0000\u0000\u0000"+
		"\u08a4\u08a3\u0001\u0000\u0000\u0000\u08a5\u08a8\u0001\u0000\u0000\u0000"+
		"\u08a6\u08a4\u0001\u0000\u0000\u0000\u08a6\u08a7\u0001\u0000\u0000\u0000"+
		"\u08a7\u0119\u0001\u0000\u0000\u0000\u08a8\u08a6\u0001\u0000\u0000\u0000"+
		"\u08a9\u08af\u0003\u0124\u0092\u0000\u08aa\u08af\u0003\u0126\u0093\u0000"+
		"\u08ab\u08af\u0003\u0128\u0094\u0000\u08ac\u08af\u0003\u012a\u0095\u0000"+
		"\u08ad\u08af\u0003\u011c\u008e\u0000\u08ae\u08a9\u0001\u0000\u0000\u0000"+
		"\u08ae\u08aa\u0001\u0000\u0000\u0000\u08ae\u08ab\u0001\u0000\u0000\u0000"+
		"\u08ae\u08ac\u0001\u0000\u0000\u0000\u08ae\u08ad\u0001\u0000\u0000\u0000"+
		"\u08af\u08b0\u0001\u0000\u0000\u0000\u08b0\u08ae\u0001\u0000\u0000\u0000"+
		"\u08b0\u08b1\u0001\u0000\u0000\u0000\u08b1\u011b\u0001\u0000\u0000\u0000"+
		"\u08b2\u08b3\u0005\u0014\u0000\u0000\u08b3\u08b4\u0005\u000f\u0000\u0000"+
		"\u08b4\u08b9\u0001\u0000\u0000\u0000\u08b5\u08b8\u0003\u011e\u008f\u0000"+
		"\u08b6\u08b8\u0003\u0120\u0090\u0000\u08b7\u08b5\u0001\u0000\u0000\u0000"+
		"\u08b7\u08b6\u0001\u0000\u0000\u0000\u08b8\u08bb\u0001\u0000\u0000\u0000"+
		"\u08b9\u08b7\u0001\u0000\u0000\u0000\u08b9\u08ba\u0001\u0000\u0000\u0000"+
		"\u08ba\u08bc\u0001\u0000\u0000\u0000\u08bb\u08b9\u0001\u0000\u0000\u0000"+
		"\u08bc\u08bd\u0005\u000f\u0000\u0000\u08bd\u08be\u0005\u0014\u0000\u0000"+
		"\u08be\u011d\u0001\u0000\u0000\u0000\u08bf\u08c7\u0003\u0124\u0092\u0000"+
		"\u08c0\u08c7\u0003\u0126\u0093\u0000\u08c1\u08c7\u0003\u0128\u0094\u0000"+
		"\u08c2\u08c7\u0003\u012a\u0095\u0000\u08c3\u08c7\u0007\u0017\u0000\u0000"+
		"\u08c4\u08c7\u0007\u0018\u0000\u0000\u08c5\u08c7\u0005\u0001\u0000\u0000"+
		"\u08c6\u08bf\u0001\u0000\u0000\u0000\u08c6\u08c0\u0001\u0000\u0000\u0000"+
		"\u08c6\u08c1\u0001\u0000\u0000\u0000\u08c6\u08c2\u0001\u0000\u0000\u0000"+
		"\u08c6\u08c3\u0001\u0000\u0000\u0000\u08c6\u08c4\u0001\u0000\u0000\u0000"+
		"\u08c6\u08c5\u0001\u0000\u0000\u0000\u08c7\u011f\u0001\u0000\u0000\u0000"+
		"\u08c8\u08c9\u0005\u000f\u0000\u0000\u08c9\u08ca\u0003\u0122\u0091\u0000"+
		"\u08ca\u0121\u0001\u0000\u0000\u0000\u08cb\u08d3\u0003\u0124\u0092\u0000"+
		"\u08cc\u08d3\u0003\u0126\u0093\u0000\u08cd\u08d3\u0003\u0128\u0094\u0000"+
		"\u08ce\u08d3\u0003\u012a\u0095\u0000\u08cf\u08d3\u0007\u0019\u0000\u0000"+
		"\u08d0\u08d3\u0007\u001a\u0000\u0000\u08d1\u08d3\u0005\u0001\u0000\u0000"+
		"\u08d2\u08cb\u0001\u0000\u0000\u0000\u08d2\u08cc\u0001\u0000\u0000\u0000"+
		"\u08d2\u08cd\u0001\u0000\u0000\u0000\u08d2\u08ce\u0001\u0000\u0000\u0000"+
		"\u08d2\u08cf\u0001\u0000\u0000\u0000\u08d2\u08d0\u0001\u0000\u0000\u0000"+
		"\u08d2\u08d1\u0001\u0000\u0000\u0000\u08d3\u0123\u0001\u0000\u0000\u0000"+
		"\u08d4\u08d5\u0005\u0005\u0000\u0000\u08d5\u0125\u0001\u0000\u0000\u0000"+
		"\u08d6\u08d7\u0005\u0002\u0000\u0000\u08d7\u0127\u0001\u0000\u0000\u0000"+
		"\u08d8\u08d9\u0005\u0004\u0000\u0000\u08d9\u0129\u0001\u0000\u0000\u0000"+
		"\u08da\u08db\u0005\u0003\u0000\u0000\u08db\u012b\u0001\u0000\u0000\u0000"+
		"\u08dc\u08dd\u0005\u0007\u0000\u0000\u08dd\u012d\u0001\u0000\u0000\u0000"+
		"\u08de\u08df\u0005A\u0000\u0000\u08df\u012f\u0001\u0000\u0000\u0000\u08e0"+
		"\u08e1\u0005\u000f\u0000\u0000\u08e1\u0131\u0001\u0000\u0000\u0000\u08e2"+
		"\u08e3\u0007\u001b\u0000\u0000\u08e3\u0133\u0001\u0000\u0000\u0000\u08e4"+
		"\u08e5\u0005\u0015\u0000\u0000\u08e5\u0135\u0001\u0000\u0000\u0000\u08e6"+
		"\u08e7\u0007\u001c\u0000\u0000\u08e7\u0137\u0001\u0000\u0000\u0000\u08e8"+
		"\u08ec\u0007\u001d\u0000\u0000\u08e9\u08ec\u0007\u001e\u0000\u0000\u08ea"+
		"\u08ec\u0005\u0001\u0000\u0000\u08eb\u08e8\u0001\u0000\u0000\u0000\u08eb"+
		"\u08e9\u0001\u0000\u0000\u0000\u08eb\u08ea\u0001\u0000\u0000\u0000\u08ec"+
		"\u0139\u0001\u0000\u0000\u0000\u08ed\u08f6\u0003\u0124\u0092\u0000\u08ee"+
		"\u08f6\u0003\u0126\u0093\u0000\u08ef\u08f6\u0003\u0128\u0094\u0000\u08f0"+
		"\u08f6\u0003\u012a\u0095\u0000\u08f1\u08f6\u0007\u001f\u0000\u0000\u08f2"+
		"\u08f6\u0007 \u0000\u0000\u08f3\u08f6\u0007!\u0000\u0000\u08f4\u08f6\u0005"+
		"\u0001\u0000\u0000\u08f5\u08ed\u0001\u0000\u0000\u0000\u08f5\u08ee\u0001"+
		"\u0000\u0000\u0000\u08f5\u08ef\u0001\u0000\u0000\u0000\u08f5\u08f0\u0001"+
		"\u0000\u0000\u0000\u08f5\u08f1\u0001\u0000\u0000\u0000\u08f5\u08f2\u0001"+
		"\u0000\u0000\u0000\u08f5\u08f3\u0001\u0000\u0000\u0000\u08f5\u08f4\u0001"+
		"\u0000\u0000\u0000\u08f6\u013b\u0001\u0000\u0000\u0000\u08f7\u08f8\u0003"+
		"\u012e\u0097\u0000\u08f8\u08f9\u0003\u012c\u0096\u0000\u08f9\u08fe\u0001"+
		"\u0000\u0000\u0000\u08fa\u08fb\u0003\u012e\u0097\u0000\u08fb\u08fc\u0003"+
		"\u012e\u0097\u0000\u08fc\u08fe\u0001\u0000\u0000\u0000\u08fd\u08f7\u0001"+
		"\u0000\u0000\u0000\u08fd\u08fa\u0001\u0000\u0000\u0000\u08fe\u013d\u0001"+
		"\u0000\u0000\u0000\u08ff\u0900\u0003\u012e\u0097\u0000\u0900\u0901\u0003"+
		"\u012c\u0096\u0000\u0901\u0909\u0001\u0000\u0000\u0000\u0902\u0903\u0003"+
		"\u012e\u0097\u0000\u0903\u0904\u0003\u012e\u0097\u0000\u0904\u0909\u0001"+
		"\u0000\u0000\u0000\u0905\u0906\u0003\u012e\u0097\u0000\u0906\u0907\u0003"+
		"\u0130\u0098\u0000\u0907\u0909\u0001\u0000\u0000\u0000\u0908\u08ff\u0001"+
		"\u0000\u0000\u0000\u0908\u0902\u0001\u0000\u0000\u0000\u0908\u0905\u0001"+
		"\u0000\u0000\u0000\u0909\u013f\u0001\u0000\u0000\u0000\u090a\u090f\u0005"+
		"\u0006\u0000\u0000\u090b\u090f\u0007 \u0000\u0000\u090c\u090f\u0007!\u0000"+
		"\u0000\u090d\u090f\u0005\u0001\u0000\u0000\u090e\u090a\u0001\u0000\u0000"+
		"\u0000\u090e\u090b\u0001\u0000\u0000\u0000\u090e\u090c\u0001\u0000\u0000"+
		"\u0000\u090e\u090d\u0001\u0000\u0000\u0000\u090f\u0141\u0001\u0000\u0000"+
		"\u0000\u0910\u0913\u0007\"\u0000\u0000\u0911\u0913\u0007#\u0000\u0000"+
		"\u0912\u0910\u0001\u0000\u0000\u0000\u0912\u0911\u0001\u0000\u0000\u0000"+
		"\u0913\u0143\u0001\u0000\u0000\u0000\u0914\u0915\u0005\u0012\u0000\u0000"+
		"\u0915\u0145\u0001\u0000\u0000\u0000\u0139\u0150\u015d\u0167\u0171\u017f"+
		"\u0188\u018d\u0196\u019d\u01a7\u01a9\u01ae\u01b2\u01b8\u01bc\u01c6\u01cb"+
		"\u01d5\u01da\u01df\u01eb\u01f5\u01ff\u0204\u0209\u020d\u021a\u0236\u023a"+
		"\u023e\u0242\u0246\u024a\u0250\u0254\u0258\u025c\u0260\u0267\u0271\u027b"+
		"\u0285\u028a\u0294\u029e\u02a7\u02ae\u02bb\u02c0\u02d1\u02d7\u02e4\u02ef"+
		"\u02fa\u0305\u030a\u030f\u0317\u0322\u0330\u0338\u033c\u0340\u0344\u0348"+
		"\u034f\u0358\u0362\u036a\u036e\u0372\u0376\u037a\u037e\u0382\u0386\u038a"+
		"\u038e\u0390\u039a\u03a2\u03a4\u03b0\u03b4\u03b8\u03bc\u03c0\u03c4\u03c8"+
		"\u03cc\u03d0\u03dd\u03e5\u03ed\u03f1\u03f5\u03f9\u03fd\u0401\u0405\u040d"+
		"\u0411\u0415\u0419\u041d\u0422\u042c\u0434\u0438\u043c\u0440\u0444\u0448"+
		"\u044c\u0450\u0454\u0458\u045d\u0465\u0469\u046d\u0471\u0475\u0479\u047d"+
		"\u0481\u0485\u0489\u0491\u0495\u0499\u049d\u04a1\u04a5\u04a9\u04ad\u04b3"+
		"\u04b5\u04be\u04c5\u04c9\u04d5\u04dc\u04e0\u04e8\u04f2\u0500\u0508\u050c"+
		"\u0510\u0514\u0518\u051c\u0520\u0524\u0528\u052c\u0530\u0534\u0538\u0540"+
		"\u054b\u0556\u055a\u0562\u0566\u056a\u056e\u0572\u0576\u057a\u057e\u0582"+
		"\u0586\u058a\u058e\u0592\u0596\u059a\u059e\u05a2\u05a6\u05aa\u05b2\u05b6"+
		"\u05ba\u05be\u05c2\u05c6\u05ca\u05ce\u05d2\u05d6\u05da\u05de\u05e2\u05e6"+
		"\u05ea\u05ee\u05f2\u05f6\u0600\u0608\u060c\u0610\u0614\u0618\u061c\u0620"+
		"\u0624\u0628\u062c\u0630\u0634\u0638\u063c\u0640\u0644\u064c\u0650\u0654"+
		"\u0658\u065c\u0660\u0664\u0668\u066c\u0674\u0678\u067c\u0680\u0684\u0688"+
		"\u068c\u0690\u0694\u0698\u069c\u06a0\u06a4\u06a8\u06af\u06bb\u06de\u071e"+
		"\u0728\u072c\u0730\u0734\u0738\u073c\u0740\u0747\u074f\u0757\u0762\u076d"+
		"\u077e\u0789\u078b\u0798\u07a0\u07a4\u07a8\u07ac\u07b0\u07b4\u07b8\u07bd"+
		"\u07c2\u07c6\u07ca\u07cf\u07d3\u07d7\u07dc\u07e0\u07e4\u07ed\u07f1\u07f5"+
		"\u07f7\u07fd\u0801\u0808\u080c\u0810\u0814\u0818\u081c\u0820\u0824\u0828"+
		"\u082c\u0830\u0836\u083a\u089b\u089d\u08a4\u08a6\u08ae\u08b0\u08b7\u08b9"+
		"\u08c6\u08d2\u08eb\u08f5\u08fd\u0908\u090e\u0912";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}