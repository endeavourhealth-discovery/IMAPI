// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/IMAPI/parser/src/main/grammars\SCG.g4 by ANTLR 4.9.1
package org.endeavourhealth.imapi.parser.scg;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SCGParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		TAB=1, LF=2, CR=3, SPACE=4, EXCLAMATION=5, QUOTE=6, HASH=7, DOLLAR=8, 
		PERCENT=9, AMPERSAND=10, APOSTROPHE=11, LEFT_PAREN=12, RIGHT_PAREN=13, 
		ASTERISK=14, PLUS=15, COMMA=16, DASH=17, PERIOD=18, SLASH=19, ZERO=20, 
		ONE=21, TWO=22, THREE=23, FOUR=24, FIVE=25, SIX=26, SEVEN=27, EIGHT=28, 
		NINE=29, COLON=30, SEMICOLON=31, LESS_THAN=32, EQUALS=33, GREATER_THAN=34, 
		QUESTION=35, AT=36, CAP_A=37, CAP_B=38, CAP_C=39, CAP_D=40, CAP_E=41, 
		CAP_F=42, CAP_G=43, CAP_H=44, CAP_I=45, CAP_J=46, CAP_K=47, CAP_L=48, 
		CAP_M=49, CAP_N=50, CAP_O=51, CAP_P=52, CAP_Q=53, CAP_R=54, CAP_S=55, 
		CAP_T=56, CAP_U=57, CAP_V=58, CAP_W=59, CAP_X=60, CAP_Y=61, CAP_Z=62, 
		LEFT_BRACE=63, BACKSLASH=64, RIGHT_BRACE=65, CARAT=66, UNDERSCORE=67, 
		ACCENT=68, A=69, B=70, C=71, D=72, E=73, F=74, G=75, H=76, I=77, J=78, 
		K=79, L=80, M=81, N=82, O=83, P=84, Q=85, R=86, S=87, T=88, U=89, V=90, 
		W=91, X=92, Y=93, Z=94, LEFT_CURLY_BRACE=95, PIPE=96, RIGHT_CURLY_BRACE=97, 
		TILDE=98, U_0080=99, U_0081=100, U_0082=101, U_0083=102, U_0084=103, U_0085=104, 
		U_0086=105, U_0087=106, U_0088=107, U_0089=108, U_008A=109, U_008B=110, 
		U_008C=111, U_008D=112, U_008E=113, U_008F=114, U_0090=115, U_0091=116, 
		U_0092=117, U_0093=118, U_0094=119, U_0095=120, U_0096=121, U_0097=122, 
		U_0098=123, U_0099=124, U_009A=125, U_009B=126, U_009C=127, U_009D=128, 
		U_009E=129, U_009F=130, U_00A0=131, U_00A1=132, U_00A2=133, U_00A3=134, 
		U_00A4=135, U_00A5=136, U_00A6=137, U_00A7=138, U_00A8=139, U_00A9=140, 
		U_00AA=141, U_00AB=142, U_00AC=143, U_00AD=144, U_00AE=145, U_00AF=146, 
		U_00B0=147, U_00B1=148, U_00B2=149, U_00B3=150, U_00B4=151, U_00B5=152, 
		U_00B6=153, U_00B7=154, U_00B8=155, U_00B9=156, U_00BA=157, U_00BB=158, 
		U_00BC=159, U_00BD=160, U_00BE=161, U_00BF=162, U_00C2=163, U_00C3=164, 
		U_00C4=165, U_00C5=166, U_00C6=167, U_00C7=168, U_00C8=169, U_00C9=170, 
		U_00CA=171, U_00CB=172, U_00CC=173, U_00CD=174, U_00CE=175, U_00CF=176, 
		U_00D0=177, U_00D1=178, U_00D2=179, U_00D3=180, U_00D4=181, U_00D5=182, 
		U_00D6=183, U_00D7=184, U_00D8=185, U_00D9=186, U_00DA=187, U_00DB=188, 
		U_00DC=189, U_00DD=190, U_00DE=191, U_00DF=192, U_00E0=193, U_00E1=194, 
		U_00E2=195, U_00E3=196, U_00E4=197, U_00E5=198, U_00E6=199, U_00E7=200, 
		U_00E8=201, U_00E9=202, U_00EA=203, U_00EB=204, U_00EC=205, U_00ED=206, 
		U_00EE=207, U_00EF=208, U_00F0=209, U_00F1=210, U_00F2=211, U_00F3=212, 
		U_00F4=213;
	public static final int
		RULE_expression = 0, RULE_subexpression = 1, RULE_definitionstatus = 2, 
		RULE_equivalentto = 3, RULE_subtypeof = 4, RULE_focusconcept = 5, RULE_conceptreference = 6, 
		RULE_conceptid = 7, RULE_term = 8, RULE_refinement = 9, RULE_attributegroup = 10, 
		RULE_attributeset = 11, RULE_attribute = 12, RULE_attributename = 13, 
		RULE_attributevalue = 14, RULE_expressionvalue = 15, RULE_stringvalue = 16, 
		RULE_numericvalue = 17, RULE_integervalue = 18, RULE_decimalvalue = 19, 
		RULE_booleanvalue = 20, RULE_true_1 = 21, RULE_false_1 = 22, RULE_sctid = 23, 
		RULE_ws = 24, RULE_sp = 25, RULE_htab = 26, RULE_cr = 27, RULE_lf = 28, 
		RULE_qm = 29, RULE_bs = 30, RULE_digit = 31, RULE_zero = 32, RULE_digitnonzero = 33, 
		RULE_nonwsnonpipe = 34, RULE_anynonescapedchar = 35, RULE_escapedchar = 36, 
		RULE_utf8_2 = 37, RULE_utf8_3 = 38, RULE_utf8_4 = 39, RULE_utf8_tail = 40;
	private static String[] makeRuleNames() {
		return new String[] {
			"expression", "subexpression", "definitionstatus", "equivalentto", "subtypeof", 
			"focusconcept", "conceptreference", "conceptid", "term", "refinement", 
			"attributegroup", "attributeset", "attribute", "attributename", "attributevalue", 
			"expressionvalue", "stringvalue", "numericvalue", "integervalue", "decimalvalue", 
			"booleanvalue", "true_1", "false_1", "sctid", "ws", "sp", "htab", "cr", 
			"lf", "qm", "bs", "digit", "zero", "digitnonzero", "nonwsnonpipe", "anynonescapedchar", 
			"escapedchar", "utf8_2", "utf8_3", "utf8_4", "utf8_tail"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'\t'", "'\n'", "'\r'", "' '", "'!'", "'\"'", "'#'", "'$'", "'%'", 
			"'&'", "'''", "'('", "')'", "'*'", "'+'", "','", "'-'", "'.'", "'/'", 
			"'0'", "'1'", "'2'", "'3'", "'4'", "'5'", "'6'", "'7'", "'8'", "'9'", 
			"':'", "';'", "'<'", "'='", "'>'", "'?'", "'@'", "'A'", "'B'", "'C'", 
			"'D'", "'E'", "'F'", "'G'", "'H'", "'I'", "'J'", "'K'", "'L'", "'M'", 
			"'N'", "'O'", "'P'", "'Q'", "'R'", "'S'", "'T'", "'U'", "'V'", "'W'", 
			"'X'", "'Y'", "'Z'", "'['", "'\\'", "']'", "'^'", "'_'", "'`'", "'a'", 
			"'b'", "'c'", "'d'", "'e'", "'f'", "'g'", "'h'", "'i'", "'j'", "'k'", 
			"'l'", "'m'", "'n'", "'o'", "'p'", "'q'", "'r'", "'s'", "'t'", "'u'", 
			"'v'", "'w'", "'x'", "'y'", "'z'", "'{'", "'|'", "'}'", "'~'", "'\u0080'", 
			"'\u0081'", "'\u0082'", "'\u0083'", "'\u0084'", "'\u0085'", "'\u0086'", 
			"'\u0087'", "'\u0088'", "'\u0089'", "'\u008A'", "'\u008B'", "'\u008C'", 
			"'\u008D'", "'\u008E'", "'\u008F'", "'\u0090'", "'\u0091'", "'\u0092'", 
			"'\u0093'", "'\u0094'", "'\u0095'", "'\u0096'", "'\u0097'", "'\u0098'", 
			"'\u0099'", "'\u009A'", "'\u009B'", "'\u009C'", "'\u009D'", "'\u009E'", 
			"'\u009F'", "'\u00A0'", "'\u00A1'", "'\u00A2'", "'\u00A3'", "'\u00A4'", 
			"'\u00A5'", "'\u00A6'", "'\u00A7'", "'\u00A8'", "'\u00A9'", "'\u00AA'", 
			"'\u00AB'", "'\u00AC'", "'\u00AD'", "'\u00AE'", "'\u00AF'", "'\u00B0'", 
			"'\u00B1'", "'\u00B2'", "'\u00B3'", "'\u00B4'", "'\u00B5'", "'\u00B6'", 
			"'\u00B7'", "'\u00B8'", "'\u00B9'", "'\u00BA'", "'\u00BB'", "'\u00BC'", 
			"'\u00BD'", "'\u00BE'", "'\u00BF'", "'\u00C2'", "'\u00C3'", "'\u00C4'", 
			"'\u00C5'", "'\u00C6'", "'\u00C7'", "'\u00C8'", "'\u00C9'", "'\u00CA'", 
			"'\u00CB'", "'\u00CC'", "'\u00CD'", "'\u00CE'", "'\u00CF'", "'\u00D0'", 
			"'\u00D1'", "'\u00D2'", "'\u00D3'", "'\u00D4'", "'\u00D5'", "'\u00D6'", 
			"'\u00D7'", "'\u00D8'", "'\u00D9'", "'\u00DA'", "'\u00DB'", "'\u00DC'", 
			"'\u00DD'", "'\u00DE'", "'\u00DF'", "'\u00E0'", "'\u00E1'", "'\u00E2'", 
			"'\u00E3'", "'\u00E4'", "'\u00E5'", "'\u00E6'", "'\u00E7'", "'\u00E8'", 
			"'\u00E9'", "'\u00EA'", "'\u00EB'", "'\u00EC'", "'\u00ED'", "'\u00EE'", 
			"'\u00EF'", "'\u00F0'", "'\u00F1'", "'\u00F2'", "'\u00F3'", "'\u00F4'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "TAB", "LF", "CR", "SPACE", "EXCLAMATION", "QUOTE", "HASH", "DOLLAR", 
			"PERCENT", "AMPERSAND", "APOSTROPHE", "LEFT_PAREN", "RIGHT_PAREN", "ASTERISK", 
			"PLUS", "COMMA", "DASH", "PERIOD", "SLASH", "ZERO", "ONE", "TWO", "THREE", 
			"FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "COLON", "SEMICOLON", 
			"LESS_THAN", "EQUALS", "GREATER_THAN", "QUESTION", "AT", "CAP_A", "CAP_B", 
			"CAP_C", "CAP_D", "CAP_E", "CAP_F", "CAP_G", "CAP_H", "CAP_I", "CAP_J", 
			"CAP_K", "CAP_L", "CAP_M", "CAP_N", "CAP_O", "CAP_P", "CAP_Q", "CAP_R", 
			"CAP_S", "CAP_T", "CAP_U", "CAP_V", "CAP_W", "CAP_X", "CAP_Y", "CAP_Z", 
			"LEFT_BRACE", "BACKSLASH", "RIGHT_BRACE", "CARAT", "UNDERSCORE", "ACCENT", 
			"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", 
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "LEFT_CURLY_BRACE", 
			"PIPE", "RIGHT_CURLY_BRACE", "TILDE", "U_0080", "U_0081", "U_0082", "U_0083", 
			"U_0084", "U_0085", "U_0086", "U_0087", "U_0088", "U_0089", "U_008A", 
			"U_008B", "U_008C", "U_008D", "U_008E", "U_008F", "U_0090", "U_0091", 
			"U_0092", "U_0093", "U_0094", "U_0095", "U_0096", "U_0097", "U_0098", 
			"U_0099", "U_009A", "U_009B", "U_009C", "U_009D", "U_009E", "U_009F", 
			"U_00A0", "U_00A1", "U_00A2", "U_00A3", "U_00A4", "U_00A5", "U_00A6", 
			"U_00A7", "U_00A8", "U_00A9", "U_00AA", "U_00AB", "U_00AC", "U_00AD", 
			"U_00AE", "U_00AF", "U_00B0", "U_00B1", "U_00B2", "U_00B3", "U_00B4", 
			"U_00B5", "U_00B6", "U_00B7", "U_00B8", "U_00B9", "U_00BA", "U_00BB", 
			"U_00BC", "U_00BD", "U_00BE", "U_00BF", "U_00C2", "U_00C3", "U_00C4", 
			"U_00C5", "U_00C6", "U_00C7", "U_00C8", "U_00C9", "U_00CA", "U_00CB", 
			"U_00CC", "U_00CD", "U_00CE", "U_00CF", "U_00D0", "U_00D1", "U_00D2", 
			"U_00D3", "U_00D4", "U_00D5", "U_00D6", "U_00D7", "U_00D8", "U_00D9", 
			"U_00DA", "U_00DB", "U_00DC", "U_00DD", "U_00DE", "U_00DF", "U_00E0", 
			"U_00E1", "U_00E2", "U_00E3", "U_00E4", "U_00E5", "U_00E6", "U_00E7", 
			"U_00E8", "U_00E9", "U_00EA", "U_00EB", "U_00EC", "U_00ED", "U_00EE", 
			"U_00EF", "U_00F0", "U_00F1", "U_00F2", "U_00F3", "U_00F4"
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
	public String getGrammarFileName() { return "SCG.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SCGParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ExpressionContext extends ParserRuleContext {
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public SubexpressionContext subexpression() {
			return getRuleContext(SubexpressionContext.class,0);
		}
		public DefinitionstatusContext definitionstatus() {
			return getRuleContext(DefinitionstatusContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			ws();
			setState(86);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LESS_THAN || _la==EQUALS) {
				{
				setState(83);
				definitionstatus();
				setState(84);
				ws();
				}
			}

			setState(88);
			subexpression();
			setState(89);
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

	public static class SubexpressionContext extends ParserRuleContext {
		public FocusconceptContext focusconcept() {
			return getRuleContext(FocusconceptContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public TerminalNode COLON() { return getToken(SCGParser.COLON, 0); }
		public RefinementContext refinement() {
			return getRuleContext(RefinementContext.class,0);
		}
		public SubexpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subexpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterSubexpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitSubexpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitSubexpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubexpressionContext subexpression() throws RecognitionException {
		SubexpressionContext _localctx = new SubexpressionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_subexpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(91);
			focusconcept();
			setState(97);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(92);
				ws();
				setState(93);
				match(COLON);
				setState(94);
				ws();
				setState(95);
				refinement();
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

	public static class DefinitionstatusContext extends ParserRuleContext {
		public EquivalenttoContext equivalentto() {
			return getRuleContext(EquivalenttoContext.class,0);
		}
		public SubtypeofContext subtypeof() {
			return getRuleContext(SubtypeofContext.class,0);
		}
		public DefinitionstatusContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definitionstatus; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterDefinitionstatus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitDefinitionstatus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitDefinitionstatus(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefinitionstatusContext definitionstatus() throws RecognitionException {
		DefinitionstatusContext _localctx = new DefinitionstatusContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_definitionstatus);
		try {
			setState(101);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EQUALS:
				enterOuterAlt(_localctx, 1);
				{
				setState(99);
				equivalentto();
				}
				break;
			case LESS_THAN:
				enterOuterAlt(_localctx, 2);
				{
				setState(100);
				subtypeof();
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

	public static class EquivalenttoContext extends ParserRuleContext {
		public List<TerminalNode> EQUALS() { return getTokens(SCGParser.EQUALS); }
		public TerminalNode EQUALS(int i) {
			return getToken(SCGParser.EQUALS, i);
		}
		public EquivalenttoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equivalentto; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterEquivalentto(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitEquivalentto(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitEquivalentto(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EquivalenttoContext equivalentto() throws RecognitionException {
		EquivalenttoContext _localctx = new EquivalenttoContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_equivalentto);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(103);
			match(EQUALS);
			setState(104);
			match(EQUALS);
			setState(105);
			match(EQUALS);
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

	public static class SubtypeofContext extends ParserRuleContext {
		public List<TerminalNode> LESS_THAN() { return getTokens(SCGParser.LESS_THAN); }
		public TerminalNode LESS_THAN(int i) {
			return getToken(SCGParser.LESS_THAN, i);
		}
		public SubtypeofContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subtypeof; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterSubtypeof(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitSubtypeof(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitSubtypeof(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubtypeofContext subtypeof() throws RecognitionException {
		SubtypeofContext _localctx = new SubtypeofContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_subtypeof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(107);
			match(LESS_THAN);
			setState(108);
			match(LESS_THAN);
			setState(109);
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

	public static class FocusconceptContext extends ParserRuleContext {
		public List<ConceptreferenceContext> conceptreference() {
			return getRuleContexts(ConceptreferenceContext.class);
		}
		public ConceptreferenceContext conceptreference(int i) {
			return getRuleContext(ConceptreferenceContext.class,i);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<TerminalNode> PLUS() { return getTokens(SCGParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(SCGParser.PLUS, i);
		}
		public FocusconceptContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_focusconcept; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterFocusconcept(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitFocusconcept(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitFocusconcept(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FocusconceptContext focusconcept() throws RecognitionException {
		FocusconceptContext _localctx = new FocusconceptContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_focusconcept);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(111);
			conceptreference();
			setState(119);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(112);
					ws();
					setState(113);
					match(PLUS);
					setState(114);
					ws();
					setState(115);
					conceptreference();
					}
					} 
				}
				setState(121);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
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

	public static class ConceptreferenceContext extends ParserRuleContext {
		public ConceptidContext conceptid() {
			return getRuleContext(ConceptidContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<TerminalNode> PIPE() { return getTokens(SCGParser.PIPE); }
		public TerminalNode PIPE(int i) {
			return getToken(SCGParser.PIPE, i);
		}
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public ConceptreferenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conceptreference; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterConceptreference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitConceptreference(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitConceptreference(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConceptreferenceContext conceptreference() throws RecognitionException {
		ConceptreferenceContext _localctx = new ConceptreferenceContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_conceptreference);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(122);
			conceptid();
			setState(130);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(123);
				ws();
				setState(124);
				match(PIPE);
				setState(125);
				ws();
				setState(126);
				term();
				setState(127);
				ws();
				setState(128);
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
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterConceptid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitConceptid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitConceptid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConceptidContext conceptid() throws RecognitionException {
		ConceptidContext _localctx = new ConceptidContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_conceptid);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
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
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_term);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(134);
			nonwsnonpipe();
			setState(144);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(138);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==SPACE) {
						{
						{
						setState(135);
						sp();
						}
						}
						setState(140);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(141);
					nonwsnonpipe();
					}
					} 
				}
				setState(146);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
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

	public static class RefinementContext extends ParserRuleContext {
		public AttributesetContext attributeset() {
			return getRuleContext(AttributesetContext.class,0);
		}
		public List<AttributegroupContext> attributegroup() {
			return getRuleContexts(AttributegroupContext.class);
		}
		public AttributegroupContext attributegroup(int i) {
			return getRuleContext(AttributegroupContext.class,i);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SCGParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SCGParser.COMMA, i);
		}
		public RefinementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_refinement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterRefinement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitRefinement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitRefinement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RefinementContext refinement() throws RecognitionException {
		RefinementContext _localctx = new RefinementContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_refinement);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
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
				{
				setState(147);
				attributeset();
				}
				break;
			case LEFT_CURLY_BRACE:
				{
				setState(148);
				attributegroup();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(160);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(151);
					ws();
					setState(154);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(152);
						match(COMMA);
						setState(153);
						ws();
						}
					}

					setState(156);
					attributegroup();
					}
					} 
				}
				setState(162);
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
			exitRule();
		}
		return _localctx;
	}

	public static class AttributegroupContext extends ParserRuleContext {
		public TerminalNode LEFT_CURLY_BRACE() { return getToken(SCGParser.LEFT_CURLY_BRACE, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public AttributesetContext attributeset() {
			return getRuleContext(AttributesetContext.class,0);
		}
		public TerminalNode RIGHT_CURLY_BRACE() { return getToken(SCGParser.RIGHT_CURLY_BRACE, 0); }
		public AttributegroupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributegroup; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterAttributegroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitAttributegroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitAttributegroup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributegroupContext attributegroup() throws RecognitionException {
		AttributegroupContext _localctx = new AttributegroupContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_attributegroup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			match(LEFT_CURLY_BRACE);
			setState(164);
			ws();
			setState(165);
			attributeset();
			setState(166);
			ws();
			setState(167);
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

	public static class AttributesetContext extends ParserRuleContext {
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SCGParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SCGParser.COMMA, i);
		}
		public AttributesetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterAttributeset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitAttributeset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitAttributeset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributesetContext attributeset() throws RecognitionException {
		AttributesetContext _localctx = new AttributesetContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_attributeset);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			attribute();
			setState(177);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(170);
					ws();
					setState(171);
					match(COMMA);
					setState(172);
					ws();
					setState(173);
					attribute();
					}
					} 
				}
				setState(179);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
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

	public static class AttributeContext extends ParserRuleContext {
		public AttributenameContext attributename() {
			return getRuleContext(AttributenameContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public TerminalNode EQUALS() { return getToken(SCGParser.EQUALS, 0); }
		public AttributevalueContext attributevalue() {
			return getRuleContext(AttributevalueContext.class,0);
		}
		public AttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitAttribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitAttribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeContext attribute() throws RecognitionException {
		AttributeContext _localctx = new AttributeContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_attribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(180);
			attributename();
			setState(181);
			ws();
			setState(182);
			match(EQUALS);
			setState(183);
			ws();
			setState(184);
			attributevalue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AttributenameContext extends ParserRuleContext {
		public ConceptreferenceContext conceptreference() {
			return getRuleContext(ConceptreferenceContext.class,0);
		}
		public AttributenameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributename; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterAttributename(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitAttributename(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitAttributename(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributenameContext attributename() throws RecognitionException {
		AttributenameContext _localctx = new AttributenameContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_attributename);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186);
			conceptreference();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AttributevalueContext extends ParserRuleContext {
		public ExpressionvalueContext expressionvalue() {
			return getRuleContext(ExpressionvalueContext.class,0);
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
		public TerminalNode HASH() { return getToken(SCGParser.HASH, 0); }
		public NumericvalueContext numericvalue() {
			return getRuleContext(NumericvalueContext.class,0);
		}
		public BooleanvalueContext booleanvalue() {
			return getRuleContext(BooleanvalueContext.class,0);
		}
		public AttributevalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributevalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterAttributevalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitAttributevalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitAttributevalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributevalueContext attributevalue() throws RecognitionException {
		AttributevalueContext _localctx = new AttributevalueContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_attributevalue);
		try {
			setState(196);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LEFT_PAREN:
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
				setState(188);
				expressionvalue();
				}
				break;
			case QUOTE:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(189);
				qm();
				setState(190);
				stringvalue();
				setState(191);
				qm();
				}
				}
				break;
			case HASH:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(193);
				match(HASH);
				setState(194);
				numericvalue();
				}
				}
				break;
			case CAP_F:
			case CAP_T:
			case F:
			case T:
				enterOuterAlt(_localctx, 4);
				{
				setState(195);
				booleanvalue();
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

	public static class ExpressionvalueContext extends ParserRuleContext {
		public ConceptreferenceContext conceptreference() {
			return getRuleContext(ConceptreferenceContext.class,0);
		}
		public TerminalNode LEFT_PAREN() { return getToken(SCGParser.LEFT_PAREN, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public SubexpressionContext subexpression() {
			return getRuleContext(SubexpressionContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(SCGParser.RIGHT_PAREN, 0); }
		public ExpressionvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionvalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterExpressionvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitExpressionvalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitExpressionvalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionvalueContext expressionvalue() throws RecognitionException {
		ExpressionvalueContext _localctx = new ExpressionvalueContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_expressionvalue);
		try {
			setState(205);
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
				setState(198);
				conceptreference();
				}
				break;
			case LEFT_PAREN:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(199);
				match(LEFT_PAREN);
				setState(200);
				ws();
				setState(201);
				subexpression();
				setState(202);
				ws();
				setState(203);
				match(RIGHT_PAREN);
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
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterStringvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitStringvalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitStringvalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringvalueContext stringvalue() throws RecognitionException {
		StringvalueContext _localctx = new StringvalueContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_stringvalue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(209); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(209);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
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
				case U_00C2:
				case U_00C3:
				case U_00C4:
				case U_00C5:
				case U_00C6:
				case U_00C7:
				case U_00C8:
				case U_00C9:
				case U_00CA:
				case U_00CB:
				case U_00CC:
				case U_00CD:
				case U_00CE:
				case U_00CF:
				case U_00D0:
				case U_00D1:
				case U_00D2:
				case U_00D3:
				case U_00D4:
				case U_00D5:
				case U_00D6:
				case U_00D7:
				case U_00D8:
				case U_00D9:
				case U_00DA:
				case U_00DB:
				case U_00DC:
				case U_00DD:
				case U_00DE:
				case U_00DF:
				case U_00E0:
				case U_00E1:
				case U_00E2:
				case U_00E3:
				case U_00E4:
				case U_00E5:
				case U_00E6:
				case U_00E7:
				case U_00E8:
				case U_00E9:
				case U_00EA:
				case U_00EB:
				case U_00EC:
				case U_00ED:
				case U_00EE:
				case U_00EF:
				case U_00F0:
				case U_00F1:
				case U_00F2:
				case U_00F3:
				case U_00F4:
					{
					setState(207);
					anynonescapedchar();
					}
					break;
				case BACKSLASH:
					{
					setState(208);
					escapedchar();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(211); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TAB) | (1L << LF) | (1L << CR) | (1L << SPACE) | (1L << EXCLAMATION) | (1L << HASH) | (1L << DOLLAR) | (1L << PERCENT) | (1L << AMPERSAND) | (1L << APOSTROPHE) | (1L << LEFT_PAREN) | (1L << RIGHT_PAREN) | (1L << ASTERISK) | (1L << PLUS) | (1L << COMMA) | (1L << DASH) | (1L << PERIOD) | (1L << SLASH) | (1L << ZERO) | (1L << ONE) | (1L << TWO) | (1L << THREE) | (1L << FOUR) | (1L << FIVE) | (1L << SIX) | (1L << SEVEN) | (1L << EIGHT) | (1L << NINE) | (1L << COLON) | (1L << SEMICOLON) | (1L << LESS_THAN) | (1L << EQUALS) | (1L << GREATER_THAN) | (1L << QUESTION) | (1L << AT) | (1L << CAP_A) | (1L << CAP_B) | (1L << CAP_C) | (1L << CAP_D) | (1L << CAP_E) | (1L << CAP_F) | (1L << CAP_G) | (1L << CAP_H) | (1L << CAP_I) | (1L << CAP_J) | (1L << CAP_K) | (1L << CAP_L) | (1L << CAP_M) | (1L << CAP_N) | (1L << CAP_O) | (1L << CAP_P) | (1L << CAP_Q) | (1L << CAP_R) | (1L << CAP_S) | (1L << CAP_T) | (1L << CAP_U) | (1L << CAP_V) | (1L << CAP_W) | (1L << CAP_X) | (1L << CAP_Y) | (1L << CAP_Z) | (1L << LEFT_BRACE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BACKSLASH - 64)) | (1L << (RIGHT_BRACE - 64)) | (1L << (CARAT - 64)) | (1L << (UNDERSCORE - 64)) | (1L << (ACCENT - 64)) | (1L << (A - 64)) | (1L << (B - 64)) | (1L << (C - 64)) | (1L << (D - 64)) | (1L << (E - 64)) | (1L << (F - 64)) | (1L << (G - 64)) | (1L << (H - 64)) | (1L << (I - 64)) | (1L << (J - 64)) | (1L << (K - 64)) | (1L << (L - 64)) | (1L << (M - 64)) | (1L << (N - 64)) | (1L << (O - 64)) | (1L << (P - 64)) | (1L << (Q - 64)) | (1L << (R - 64)) | (1L << (S - 64)) | (1L << (T - 64)) | (1L << (U - 64)) | (1L << (V - 64)) | (1L << (W - 64)) | (1L << (X - 64)) | (1L << (Y - 64)) | (1L << (Z - 64)) | (1L << (LEFT_CURLY_BRACE - 64)) | (1L << (PIPE - 64)) | (1L << (RIGHT_CURLY_BRACE - 64)) | (1L << (TILDE - 64)))) != 0) || ((((_la - 163)) & ~0x3f) == 0 && ((1L << (_la - 163)) & ((1L << (U_00C2 - 163)) | (1L << (U_00C3 - 163)) | (1L << (U_00C4 - 163)) | (1L << (U_00C5 - 163)) | (1L << (U_00C6 - 163)) | (1L << (U_00C7 - 163)) | (1L << (U_00C8 - 163)) | (1L << (U_00C9 - 163)) | (1L << (U_00CA - 163)) | (1L << (U_00CB - 163)) | (1L << (U_00CC - 163)) | (1L << (U_00CD - 163)) | (1L << (U_00CE - 163)) | (1L << (U_00CF - 163)) | (1L << (U_00D0 - 163)) | (1L << (U_00D1 - 163)) | (1L << (U_00D2 - 163)) | (1L << (U_00D3 - 163)) | (1L << (U_00D4 - 163)) | (1L << (U_00D5 - 163)) | (1L << (U_00D6 - 163)) | (1L << (U_00D7 - 163)) | (1L << (U_00D8 - 163)) | (1L << (U_00D9 - 163)) | (1L << (U_00DA - 163)) | (1L << (U_00DB - 163)) | (1L << (U_00DC - 163)) | (1L << (U_00DD - 163)) | (1L << (U_00DE - 163)) | (1L << (U_00DF - 163)) | (1L << (U_00E0 - 163)) | (1L << (U_00E1 - 163)) | (1L << (U_00E2 - 163)) | (1L << (U_00E3 - 163)) | (1L << (U_00E4 - 163)) | (1L << (U_00E5 - 163)) | (1L << (U_00E6 - 163)) | (1L << (U_00E7 - 163)) | (1L << (U_00E8 - 163)) | (1L << (U_00E9 - 163)) | (1L << (U_00EA - 163)) | (1L << (U_00EB - 163)) | (1L << (U_00EC - 163)) | (1L << (U_00ED - 163)) | (1L << (U_00EE - 163)) | (1L << (U_00EF - 163)) | (1L << (U_00F0 - 163)) | (1L << (U_00F1 - 163)) | (1L << (U_00F2 - 163)) | (1L << (U_00F3 - 163)) | (1L << (U_00F4 - 163)))) != 0) );
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode DASH() { return getToken(SCGParser.DASH, 0); }
		public TerminalNode PLUS() { return getToken(SCGParser.PLUS, 0); }
		public NumericvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numericvalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterNumericvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitNumericvalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitNumericvalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumericvalueContext numericvalue() throws RecognitionException {
		NumericvalueContext _localctx = new NumericvalueContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_numericvalue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(214);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PLUS || _la==DASH) {
				{
				setState(213);
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

			setState(218);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(216);
				decimalvalue();
				}
				break;
			case 2:
				{
				setState(217);
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
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterIntegervalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitIntegervalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitIntegervalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntegervalueContext integervalue() throws RecognitionException {
		IntegervalueContext _localctx = new IntegervalueContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_integervalue);
		int _la;
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
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(220);
				digitnonzero();
				setState(224);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ZERO) | (1L << ONE) | (1L << TWO) | (1L << THREE) | (1L << FOUR) | (1L << FIVE) | (1L << SIX) | (1L << SEVEN) | (1L << EIGHT) | (1L << NINE))) != 0)) {
					{
					{
					setState(221);
					digit();
					}
					}
					setState(226);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				break;
			case ZERO:
				enterOuterAlt(_localctx, 2);
				{
				setState(227);
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
		public TerminalNode PERIOD() { return getToken(SCGParser.PERIOD, 0); }
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
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterDecimalvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitDecimalvalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitDecimalvalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DecimalvalueContext decimalvalue() throws RecognitionException {
		DecimalvalueContext _localctx = new DecimalvalueContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_decimalvalue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(230);
			integervalue();
			setState(231);
			match(PERIOD);
			setState(233); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(232);
				digit();
				}
				}
				setState(235); 
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
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterBooleanvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitBooleanvalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitBooleanvalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanvalueContext booleanvalue() throws RecognitionException {
		BooleanvalueContext _localctx = new BooleanvalueContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_booleanvalue);
		try {
			setState(239);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CAP_T:
			case T:
				enterOuterAlt(_localctx, 1);
				{
				setState(237);
				true_1();
				}
				break;
			case CAP_F:
			case F:
				enterOuterAlt(_localctx, 2);
				{
				setState(238);
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

	public static class True_1Context extends ParserRuleContext {
		public TerminalNode CAP_T() { return getToken(SCGParser.CAP_T, 0); }
		public TerminalNode T() { return getToken(SCGParser.T, 0); }
		public TerminalNode CAP_R() { return getToken(SCGParser.CAP_R, 0); }
		public TerminalNode R() { return getToken(SCGParser.R, 0); }
		public TerminalNode CAP_U() { return getToken(SCGParser.CAP_U, 0); }
		public TerminalNode U() { return getToken(SCGParser.U, 0); }
		public TerminalNode CAP_E() { return getToken(SCGParser.CAP_E, 0); }
		public TerminalNode E() { return getToken(SCGParser.E, 0); }
		public True_1Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_true_1; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterTrue_1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitTrue_1(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitTrue_1(this);
			else return visitor.visitChildren(this);
		}
	}

	public final True_1Context true_1() throws RecognitionException {
		True_1Context _localctx = new True_1Context(_ctx, getState());
		enterRule(_localctx, 42, RULE_true_1);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(243);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(241);
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
				setState(242);
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
			setState(247);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				{
				setState(245);
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
				setState(246);
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
			setState(251);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				{
				setState(249);
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
				setState(250);
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
			setState(255);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				{
				setState(253);
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
				setState(254);
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

	public static class False_1Context extends ParserRuleContext {
		public TerminalNode CAP_F() { return getToken(SCGParser.CAP_F, 0); }
		public TerminalNode F() { return getToken(SCGParser.F, 0); }
		public TerminalNode CAP_A() { return getToken(SCGParser.CAP_A, 0); }
		public TerminalNode A() { return getToken(SCGParser.A, 0); }
		public TerminalNode CAP_L() { return getToken(SCGParser.CAP_L, 0); }
		public TerminalNode L() { return getToken(SCGParser.L, 0); }
		public TerminalNode CAP_S() { return getToken(SCGParser.CAP_S, 0); }
		public TerminalNode S() { return getToken(SCGParser.S, 0); }
		public TerminalNode CAP_E() { return getToken(SCGParser.CAP_E, 0); }
		public TerminalNode E() { return getToken(SCGParser.E, 0); }
		public False_1Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_false_1; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterFalse_1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitFalse_1(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitFalse_1(this);
			else return visitor.visitChildren(this);
		}
	}

	public final False_1Context false_1() throws RecognitionException {
		False_1Context _localctx = new False_1Context(_ctx, getState());
		enterRule(_localctx, 44, RULE_false_1);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(259);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				{
				setState(257);
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
				setState(258);
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
			setState(263);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				{
				setState(261);
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
				setState(262);
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
			setState(267);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				{
				setState(265);
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
				setState(266);
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
			setState(271);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				{
				setState(269);
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
				setState(270);
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
			setState(275);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				{
				setState(273);
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
				setState(274);
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
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterSctid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitSctid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitSctid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SctidContext sctid() throws RecognitionException {
		SctidContext _localctx = new SctidContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_sctid);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(277);
			digitnonzero();
			{
			{
			setState(278);
			digit();
			}
			{
			setState(279);
			digit();
			}
			{
			setState(280);
			digit();
			}
			{
			setState(281);
			digit();
			}
			{
			setState(282);
			digit();
			}
			setState(374);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				{
				{
				{
				setState(283);
				digit();
				}
				{
				setState(284);
				digit();
				}
				{
				setState(285);
				digit();
				}
				{
				setState(286);
				digit();
				}
				{
				setState(287);
				digit();
				}
				{
				setState(288);
				digit();
				}
				{
				setState(289);
				digit();
				}
				{
				setState(290);
				digit();
				}
				{
				setState(291);
				digit();
				}
				{
				setState(292);
				digit();
				}
				{
				setState(293);
				digit();
				}
				{
				setState(294);
				digit();
				}
				}
				}
				break;
			case 2:
				{
				{
				{
				setState(296);
				digit();
				}
				{
				setState(297);
				digit();
				}
				{
				setState(298);
				digit();
				}
				{
				setState(299);
				digit();
				}
				{
				setState(300);
				digit();
				}
				{
				setState(301);
				digit();
				}
				{
				setState(302);
				digit();
				}
				{
				setState(303);
				digit();
				}
				{
				setState(304);
				digit();
				}
				{
				setState(305);
				digit();
				}
				{
				setState(306);
				digit();
				}
				}
				}
				break;
			case 3:
				{
				{
				{
				setState(308);
				digit();
				}
				{
				setState(309);
				digit();
				}
				{
				setState(310);
				digit();
				}
				{
				setState(311);
				digit();
				}
				{
				setState(312);
				digit();
				}
				{
				setState(313);
				digit();
				}
				{
				setState(314);
				digit();
				}
				{
				setState(315);
				digit();
				}
				{
				setState(316);
				digit();
				}
				{
				setState(317);
				digit();
				}
				}
				}
				break;
			case 4:
				{
				{
				{
				setState(319);
				digit();
				}
				{
				setState(320);
				digit();
				}
				{
				setState(321);
				digit();
				}
				{
				setState(322);
				digit();
				}
				{
				setState(323);
				digit();
				}
				{
				setState(324);
				digit();
				}
				{
				setState(325);
				digit();
				}
				{
				setState(326);
				digit();
				}
				{
				setState(327);
				digit();
				}
				}
				}
				break;
			case 5:
				{
				{
				{
				setState(329);
				digit();
				}
				{
				setState(330);
				digit();
				}
				{
				setState(331);
				digit();
				}
				{
				setState(332);
				digit();
				}
				{
				setState(333);
				digit();
				}
				{
				setState(334);
				digit();
				}
				{
				setState(335);
				digit();
				}
				{
				setState(336);
				digit();
				}
				}
				}
				break;
			case 6:
				{
				{
				{
				setState(338);
				digit();
				}
				{
				setState(339);
				digit();
				}
				{
				setState(340);
				digit();
				}
				{
				setState(341);
				digit();
				}
				{
				setState(342);
				digit();
				}
				{
				setState(343);
				digit();
				}
				{
				setState(344);
				digit();
				}
				}
				}
				break;
			case 7:
				{
				{
				{
				setState(346);
				digit();
				}
				{
				setState(347);
				digit();
				}
				{
				setState(348);
				digit();
				}
				{
				setState(349);
				digit();
				}
				{
				setState(350);
				digit();
				}
				{
				setState(351);
				digit();
				}
				}
				}
				break;
			case 8:
				{
				{
				{
				setState(353);
				digit();
				}
				{
				setState(354);
				digit();
				}
				{
				setState(355);
				digit();
				}
				{
				setState(356);
				digit();
				}
				{
				setState(357);
				digit();
				}
				}
				}
				break;
			case 9:
				{
				{
				{
				setState(359);
				digit();
				}
				{
				setState(360);
				digit();
				}
				{
				setState(361);
				digit();
				}
				{
				setState(362);
				digit();
				}
				}
				}
				break;
			case 10:
				{
				{
				{
				setState(364);
				digit();
				}
				{
				setState(365);
				digit();
				}
				{
				setState(366);
				digit();
				}
				}
				}
				break;
			case 11:
				{
				{
				{
				setState(368);
				digit();
				}
				{
				setState(369);
				digit();
				}
				}
				}
				break;
			case 12:
				{
				setState(372);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ZERO) | (1L << ONE) | (1L << TWO) | (1L << THREE) | (1L << FOUR) | (1L << FIVE) | (1L << SIX) | (1L << SEVEN) | (1L << EIGHT) | (1L << NINE))) != 0)) {
					{
					setState(371);
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
		public WsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ws; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterWs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitWs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitWs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WsContext ws() throws RecognitionException {
		WsContext _localctx = new WsContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_ws);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(382);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TAB) | (1L << LF) | (1L << CR) | (1L << SPACE))) != 0)) {
				{
				setState(380);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case SPACE:
					{
					setState(376);
					sp();
					}
					break;
				case TAB:
					{
					setState(377);
					htab();
					}
					break;
				case CR:
					{
					setState(378);
					cr();
					}
					break;
				case LF:
					{
					setState(379);
					lf();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(384);
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

	public static class SpContext extends ParserRuleContext {
		public TerminalNode SPACE() { return getToken(SCGParser.SPACE, 0); }
		public SpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterSp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitSp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitSp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpContext sp() throws RecognitionException {
		SpContext _localctx = new SpContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_sp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(385);
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
		public TerminalNode TAB() { return getToken(SCGParser.TAB, 0); }
		public HtabContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htab; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterHtab(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitHtab(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitHtab(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HtabContext htab() throws RecognitionException {
		HtabContext _localctx = new HtabContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_htab);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(387);
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
		public TerminalNode CR() { return getToken(SCGParser.CR, 0); }
		public CrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterCr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitCr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitCr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CrContext cr() throws RecognitionException {
		CrContext _localctx = new CrContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_cr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(389);
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
		public TerminalNode LF() { return getToken(SCGParser.LF, 0); }
		public LfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterLf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitLf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitLf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LfContext lf() throws RecognitionException {
		LfContext _localctx = new LfContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_lf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(391);
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
		public TerminalNode QUOTE() { return getToken(SCGParser.QUOTE, 0); }
		public QmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterQm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitQm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitQm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QmContext qm() throws RecognitionException {
		QmContext _localctx = new QmContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_qm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(393);
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
		public TerminalNode BACKSLASH() { return getToken(SCGParser.BACKSLASH, 0); }
		public BsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterBs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitBs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitBs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BsContext bs() throws RecognitionException {
		BsContext _localctx = new BsContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_bs);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(395);
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
		public TerminalNode ZERO() { return getToken(SCGParser.ZERO, 0); }
		public TerminalNode ONE() { return getToken(SCGParser.ONE, 0); }
		public TerminalNode TWO() { return getToken(SCGParser.TWO, 0); }
		public TerminalNode THREE() { return getToken(SCGParser.THREE, 0); }
		public TerminalNode FOUR() { return getToken(SCGParser.FOUR, 0); }
		public TerminalNode FIVE() { return getToken(SCGParser.FIVE, 0); }
		public TerminalNode SIX() { return getToken(SCGParser.SIX, 0); }
		public TerminalNode SEVEN() { return getToken(SCGParser.SEVEN, 0); }
		public TerminalNode EIGHT() { return getToken(SCGParser.EIGHT, 0); }
		public TerminalNode NINE() { return getToken(SCGParser.NINE, 0); }
		public DigitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_digit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterDigit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitDigit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitDigit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DigitContext digit() throws RecognitionException {
		DigitContext _localctx = new DigitContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_digit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(397);
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
		public TerminalNode ZERO() { return getToken(SCGParser.ZERO, 0); }
		public ZeroContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_zero; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterZero(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitZero(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitZero(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ZeroContext zero() throws RecognitionException {
		ZeroContext _localctx = new ZeroContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_zero);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(399);
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
		public TerminalNode ONE() { return getToken(SCGParser.ONE, 0); }
		public TerminalNode TWO() { return getToken(SCGParser.TWO, 0); }
		public TerminalNode THREE() { return getToken(SCGParser.THREE, 0); }
		public TerminalNode FOUR() { return getToken(SCGParser.FOUR, 0); }
		public TerminalNode FIVE() { return getToken(SCGParser.FIVE, 0); }
		public TerminalNode SIX() { return getToken(SCGParser.SIX, 0); }
		public TerminalNode SEVEN() { return getToken(SCGParser.SEVEN, 0); }
		public TerminalNode EIGHT() { return getToken(SCGParser.EIGHT, 0); }
		public TerminalNode NINE() { return getToken(SCGParser.NINE, 0); }
		public DigitnonzeroContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_digitnonzero; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterDigitnonzero(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitDigitnonzero(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitDigitnonzero(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DigitnonzeroContext digitnonzero() throws RecognitionException {
		DigitnonzeroContext _localctx = new DigitnonzeroContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_digitnonzero);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(401);
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
		public TerminalNode EXCLAMATION() { return getToken(SCGParser.EXCLAMATION, 0); }
		public TerminalNode QUOTE() { return getToken(SCGParser.QUOTE, 0); }
		public TerminalNode HASH() { return getToken(SCGParser.HASH, 0); }
		public TerminalNode DOLLAR() { return getToken(SCGParser.DOLLAR, 0); }
		public TerminalNode PERCENT() { return getToken(SCGParser.PERCENT, 0); }
		public TerminalNode AMPERSAND() { return getToken(SCGParser.AMPERSAND, 0); }
		public TerminalNode APOSTROPHE() { return getToken(SCGParser.APOSTROPHE, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(SCGParser.LEFT_PAREN, 0); }
		public TerminalNode RIGHT_PAREN() { return getToken(SCGParser.RIGHT_PAREN, 0); }
		public TerminalNode ASTERISK() { return getToken(SCGParser.ASTERISK, 0); }
		public TerminalNode PLUS() { return getToken(SCGParser.PLUS, 0); }
		public TerminalNode COMMA() { return getToken(SCGParser.COMMA, 0); }
		public TerminalNode DASH() { return getToken(SCGParser.DASH, 0); }
		public TerminalNode PERIOD() { return getToken(SCGParser.PERIOD, 0); }
		public TerminalNode SLASH() { return getToken(SCGParser.SLASH, 0); }
		public TerminalNode ZERO() { return getToken(SCGParser.ZERO, 0); }
		public TerminalNode ONE() { return getToken(SCGParser.ONE, 0); }
		public TerminalNode TWO() { return getToken(SCGParser.TWO, 0); }
		public TerminalNode THREE() { return getToken(SCGParser.THREE, 0); }
		public TerminalNode FOUR() { return getToken(SCGParser.FOUR, 0); }
		public TerminalNode FIVE() { return getToken(SCGParser.FIVE, 0); }
		public TerminalNode SIX() { return getToken(SCGParser.SIX, 0); }
		public TerminalNode SEVEN() { return getToken(SCGParser.SEVEN, 0); }
		public TerminalNode EIGHT() { return getToken(SCGParser.EIGHT, 0); }
		public TerminalNode NINE() { return getToken(SCGParser.NINE, 0); }
		public TerminalNode COLON() { return getToken(SCGParser.COLON, 0); }
		public TerminalNode SEMICOLON() { return getToken(SCGParser.SEMICOLON, 0); }
		public TerminalNode LESS_THAN() { return getToken(SCGParser.LESS_THAN, 0); }
		public TerminalNode EQUALS() { return getToken(SCGParser.EQUALS, 0); }
		public TerminalNode GREATER_THAN() { return getToken(SCGParser.GREATER_THAN, 0); }
		public TerminalNode QUESTION() { return getToken(SCGParser.QUESTION, 0); }
		public TerminalNode AT() { return getToken(SCGParser.AT, 0); }
		public TerminalNode CAP_A() { return getToken(SCGParser.CAP_A, 0); }
		public TerminalNode CAP_B() { return getToken(SCGParser.CAP_B, 0); }
		public TerminalNode CAP_C() { return getToken(SCGParser.CAP_C, 0); }
		public TerminalNode CAP_D() { return getToken(SCGParser.CAP_D, 0); }
		public TerminalNode CAP_E() { return getToken(SCGParser.CAP_E, 0); }
		public TerminalNode CAP_F() { return getToken(SCGParser.CAP_F, 0); }
		public TerminalNode CAP_G() { return getToken(SCGParser.CAP_G, 0); }
		public TerminalNode CAP_H() { return getToken(SCGParser.CAP_H, 0); }
		public TerminalNode CAP_I() { return getToken(SCGParser.CAP_I, 0); }
		public TerminalNode CAP_J() { return getToken(SCGParser.CAP_J, 0); }
		public TerminalNode CAP_K() { return getToken(SCGParser.CAP_K, 0); }
		public TerminalNode CAP_L() { return getToken(SCGParser.CAP_L, 0); }
		public TerminalNode CAP_M() { return getToken(SCGParser.CAP_M, 0); }
		public TerminalNode CAP_N() { return getToken(SCGParser.CAP_N, 0); }
		public TerminalNode CAP_O() { return getToken(SCGParser.CAP_O, 0); }
		public TerminalNode CAP_P() { return getToken(SCGParser.CAP_P, 0); }
		public TerminalNode CAP_Q() { return getToken(SCGParser.CAP_Q, 0); }
		public TerminalNode CAP_R() { return getToken(SCGParser.CAP_R, 0); }
		public TerminalNode CAP_S() { return getToken(SCGParser.CAP_S, 0); }
		public TerminalNode CAP_T() { return getToken(SCGParser.CAP_T, 0); }
		public TerminalNode CAP_U() { return getToken(SCGParser.CAP_U, 0); }
		public TerminalNode CAP_V() { return getToken(SCGParser.CAP_V, 0); }
		public TerminalNode CAP_W() { return getToken(SCGParser.CAP_W, 0); }
		public TerminalNode CAP_X() { return getToken(SCGParser.CAP_X, 0); }
		public TerminalNode CAP_Y() { return getToken(SCGParser.CAP_Y, 0); }
		public TerminalNode CAP_Z() { return getToken(SCGParser.CAP_Z, 0); }
		public TerminalNode LEFT_BRACE() { return getToken(SCGParser.LEFT_BRACE, 0); }
		public TerminalNode BACKSLASH() { return getToken(SCGParser.BACKSLASH, 0); }
		public TerminalNode RIGHT_BRACE() { return getToken(SCGParser.RIGHT_BRACE, 0); }
		public TerminalNode CARAT() { return getToken(SCGParser.CARAT, 0); }
		public TerminalNode UNDERSCORE() { return getToken(SCGParser.UNDERSCORE, 0); }
		public TerminalNode ACCENT() { return getToken(SCGParser.ACCENT, 0); }
		public TerminalNode A() { return getToken(SCGParser.A, 0); }
		public TerminalNode B() { return getToken(SCGParser.B, 0); }
		public TerminalNode C() { return getToken(SCGParser.C, 0); }
		public TerminalNode D() { return getToken(SCGParser.D, 0); }
		public TerminalNode E() { return getToken(SCGParser.E, 0); }
		public TerminalNode F() { return getToken(SCGParser.F, 0); }
		public TerminalNode G() { return getToken(SCGParser.G, 0); }
		public TerminalNode H() { return getToken(SCGParser.H, 0); }
		public TerminalNode I() { return getToken(SCGParser.I, 0); }
		public TerminalNode J() { return getToken(SCGParser.J, 0); }
		public TerminalNode K() { return getToken(SCGParser.K, 0); }
		public TerminalNode L() { return getToken(SCGParser.L, 0); }
		public TerminalNode M() { return getToken(SCGParser.M, 0); }
		public TerminalNode N() { return getToken(SCGParser.N, 0); }
		public TerminalNode O() { return getToken(SCGParser.O, 0); }
		public TerminalNode P() { return getToken(SCGParser.P, 0); }
		public TerminalNode Q() { return getToken(SCGParser.Q, 0); }
		public TerminalNode R() { return getToken(SCGParser.R, 0); }
		public TerminalNode S() { return getToken(SCGParser.S, 0); }
		public TerminalNode T() { return getToken(SCGParser.T, 0); }
		public TerminalNode U() { return getToken(SCGParser.U, 0); }
		public TerminalNode V() { return getToken(SCGParser.V, 0); }
		public TerminalNode W() { return getToken(SCGParser.W, 0); }
		public TerminalNode X() { return getToken(SCGParser.X, 0); }
		public TerminalNode Y() { return getToken(SCGParser.Y, 0); }
		public TerminalNode Z() { return getToken(SCGParser.Z, 0); }
		public TerminalNode LEFT_CURLY_BRACE() { return getToken(SCGParser.LEFT_CURLY_BRACE, 0); }
		public TerminalNode RIGHT_CURLY_BRACE() { return getToken(SCGParser.RIGHT_CURLY_BRACE, 0); }
		public TerminalNode TILDE() { return getToken(SCGParser.TILDE, 0); }
		public Utf8_2Context utf8_2() {
			return getRuleContext(Utf8_2Context.class,0);
		}
		public Utf8_3Context utf8_3() {
			return getRuleContext(Utf8_3Context.class,0);
		}
		public Utf8_4Context utf8_4() {
			return getRuleContext(Utf8_4Context.class,0);
		}
		public NonwsnonpipeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonwsnonpipe; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterNonwsnonpipe(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitNonwsnonpipe(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitNonwsnonpipe(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonwsnonpipeContext nonwsnonpipe() throws RecognitionException {
		NonwsnonpipeContext _localctx = new NonwsnonpipeContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_nonwsnonpipe);
		int _la;
		try {
			setState(408);
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
				setState(403);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EXCLAMATION) | (1L << QUOTE) | (1L << HASH) | (1L << DOLLAR) | (1L << PERCENT) | (1L << AMPERSAND) | (1L << APOSTROPHE) | (1L << LEFT_PAREN) | (1L << RIGHT_PAREN) | (1L << ASTERISK) | (1L << PLUS) | (1L << COMMA) | (1L << DASH) | (1L << PERIOD) | (1L << SLASH) | (1L << ZERO) | (1L << ONE) | (1L << TWO) | (1L << THREE) | (1L << FOUR) | (1L << FIVE) | (1L << SIX) | (1L << SEVEN) | (1L << EIGHT) | (1L << NINE) | (1L << COLON) | (1L << SEMICOLON) | (1L << LESS_THAN) | (1L << EQUALS) | (1L << GREATER_THAN) | (1L << QUESTION) | (1L << AT) | (1L << CAP_A) | (1L << CAP_B) | (1L << CAP_C) | (1L << CAP_D) | (1L << CAP_E) | (1L << CAP_F) | (1L << CAP_G) | (1L << CAP_H) | (1L << CAP_I) | (1L << CAP_J) | (1L << CAP_K) | (1L << CAP_L) | (1L << CAP_M) | (1L << CAP_N) | (1L << CAP_O) | (1L << CAP_P) | (1L << CAP_Q) | (1L << CAP_R) | (1L << CAP_S) | (1L << CAP_T) | (1L << CAP_U) | (1L << CAP_V) | (1L << CAP_W) | (1L << CAP_X) | (1L << CAP_Y) | (1L << CAP_Z) | (1L << LEFT_BRACE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BACKSLASH - 64)) | (1L << (RIGHT_BRACE - 64)) | (1L << (CARAT - 64)) | (1L << (UNDERSCORE - 64)) | (1L << (ACCENT - 64)) | (1L << (A - 64)) | (1L << (B - 64)) | (1L << (C - 64)) | (1L << (D - 64)) | (1L << (E - 64)) | (1L << (F - 64)) | (1L << (G - 64)) | (1L << (H - 64)) | (1L << (I - 64)) | (1L << (J - 64)) | (1L << (K - 64)) | (1L << (L - 64)) | (1L << (M - 64)) | (1L << (N - 64)) | (1L << (O - 64)) | (1L << (P - 64)) | (1L << (Q - 64)) | (1L << (R - 64)) | (1L << (S - 64)) | (1L << (T - 64)) | (1L << (U - 64)) | (1L << (V - 64)) | (1L << (W - 64)) | (1L << (X - 64)) | (1L << (Y - 64)) | (1L << (Z - 64)) | (1L << (LEFT_CURLY_BRACE - 64)))) != 0)) ) {
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
				setState(404);
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
			case U_00C2:
			case U_00C3:
			case U_00C4:
			case U_00C5:
			case U_00C6:
			case U_00C7:
			case U_00C8:
			case U_00C9:
			case U_00CA:
			case U_00CB:
			case U_00CC:
			case U_00CD:
			case U_00CE:
			case U_00CF:
			case U_00D0:
			case U_00D1:
			case U_00D2:
			case U_00D3:
			case U_00D4:
			case U_00D5:
			case U_00D6:
			case U_00D7:
			case U_00D8:
			case U_00D9:
			case U_00DA:
			case U_00DB:
			case U_00DC:
			case U_00DD:
			case U_00DE:
			case U_00DF:
				enterOuterAlt(_localctx, 3);
				{
				setState(405);
				utf8_2();
				}
				break;
			case U_00E0:
			case U_00E1:
			case U_00E2:
			case U_00E3:
			case U_00E4:
			case U_00E5:
			case U_00E6:
			case U_00E7:
			case U_00E8:
			case U_00E9:
			case U_00EA:
			case U_00EB:
			case U_00EC:
			case U_00ED:
			case U_00EE:
			case U_00EF:
				enterOuterAlt(_localctx, 4);
				{
				setState(406);
				utf8_3();
				}
				break;
			case U_00F0:
			case U_00F1:
			case U_00F2:
			case U_00F3:
			case U_00F4:
				enterOuterAlt(_localctx, 5);
				{
				setState(407);
				utf8_4();
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
		public HtabContext htab() {
			return getRuleContext(HtabContext.class,0);
		}
		public CrContext cr() {
			return getRuleContext(CrContext.class,0);
		}
		public LfContext lf() {
			return getRuleContext(LfContext.class,0);
		}
		public TerminalNode SPACE() { return getToken(SCGParser.SPACE, 0); }
		public TerminalNode EXCLAMATION() { return getToken(SCGParser.EXCLAMATION, 0); }
		public TerminalNode HASH() { return getToken(SCGParser.HASH, 0); }
		public TerminalNode DOLLAR() { return getToken(SCGParser.DOLLAR, 0); }
		public TerminalNode PERCENT() { return getToken(SCGParser.PERCENT, 0); }
		public TerminalNode AMPERSAND() { return getToken(SCGParser.AMPERSAND, 0); }
		public TerminalNode APOSTROPHE() { return getToken(SCGParser.APOSTROPHE, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(SCGParser.LEFT_PAREN, 0); }
		public TerminalNode RIGHT_PAREN() { return getToken(SCGParser.RIGHT_PAREN, 0); }
		public TerminalNode ASTERISK() { return getToken(SCGParser.ASTERISK, 0); }
		public TerminalNode PLUS() { return getToken(SCGParser.PLUS, 0); }
		public TerminalNode COMMA() { return getToken(SCGParser.COMMA, 0); }
		public TerminalNode DASH() { return getToken(SCGParser.DASH, 0); }
		public TerminalNode PERIOD() { return getToken(SCGParser.PERIOD, 0); }
		public TerminalNode SLASH() { return getToken(SCGParser.SLASH, 0); }
		public TerminalNode ZERO() { return getToken(SCGParser.ZERO, 0); }
		public TerminalNode ONE() { return getToken(SCGParser.ONE, 0); }
		public TerminalNode TWO() { return getToken(SCGParser.TWO, 0); }
		public TerminalNode THREE() { return getToken(SCGParser.THREE, 0); }
		public TerminalNode FOUR() { return getToken(SCGParser.FOUR, 0); }
		public TerminalNode FIVE() { return getToken(SCGParser.FIVE, 0); }
		public TerminalNode SIX() { return getToken(SCGParser.SIX, 0); }
		public TerminalNode SEVEN() { return getToken(SCGParser.SEVEN, 0); }
		public TerminalNode EIGHT() { return getToken(SCGParser.EIGHT, 0); }
		public TerminalNode NINE() { return getToken(SCGParser.NINE, 0); }
		public TerminalNode COLON() { return getToken(SCGParser.COLON, 0); }
		public TerminalNode SEMICOLON() { return getToken(SCGParser.SEMICOLON, 0); }
		public TerminalNode LESS_THAN() { return getToken(SCGParser.LESS_THAN, 0); }
		public TerminalNode EQUALS() { return getToken(SCGParser.EQUALS, 0); }
		public TerminalNode GREATER_THAN() { return getToken(SCGParser.GREATER_THAN, 0); }
		public TerminalNode QUESTION() { return getToken(SCGParser.QUESTION, 0); }
		public TerminalNode AT() { return getToken(SCGParser.AT, 0); }
		public TerminalNode CAP_A() { return getToken(SCGParser.CAP_A, 0); }
		public TerminalNode CAP_B() { return getToken(SCGParser.CAP_B, 0); }
		public TerminalNode CAP_C() { return getToken(SCGParser.CAP_C, 0); }
		public TerminalNode CAP_D() { return getToken(SCGParser.CAP_D, 0); }
		public TerminalNode CAP_E() { return getToken(SCGParser.CAP_E, 0); }
		public TerminalNode CAP_F() { return getToken(SCGParser.CAP_F, 0); }
		public TerminalNode CAP_G() { return getToken(SCGParser.CAP_G, 0); }
		public TerminalNode CAP_H() { return getToken(SCGParser.CAP_H, 0); }
		public TerminalNode CAP_I() { return getToken(SCGParser.CAP_I, 0); }
		public TerminalNode CAP_J() { return getToken(SCGParser.CAP_J, 0); }
		public TerminalNode CAP_K() { return getToken(SCGParser.CAP_K, 0); }
		public TerminalNode CAP_L() { return getToken(SCGParser.CAP_L, 0); }
		public TerminalNode CAP_M() { return getToken(SCGParser.CAP_M, 0); }
		public TerminalNode CAP_N() { return getToken(SCGParser.CAP_N, 0); }
		public TerminalNode CAP_O() { return getToken(SCGParser.CAP_O, 0); }
		public TerminalNode CAP_P() { return getToken(SCGParser.CAP_P, 0); }
		public TerminalNode CAP_Q() { return getToken(SCGParser.CAP_Q, 0); }
		public TerminalNode CAP_R() { return getToken(SCGParser.CAP_R, 0); }
		public TerminalNode CAP_S() { return getToken(SCGParser.CAP_S, 0); }
		public TerminalNode CAP_T() { return getToken(SCGParser.CAP_T, 0); }
		public TerminalNode CAP_U() { return getToken(SCGParser.CAP_U, 0); }
		public TerminalNode CAP_V() { return getToken(SCGParser.CAP_V, 0); }
		public TerminalNode CAP_W() { return getToken(SCGParser.CAP_W, 0); }
		public TerminalNode CAP_X() { return getToken(SCGParser.CAP_X, 0); }
		public TerminalNode CAP_Y() { return getToken(SCGParser.CAP_Y, 0); }
		public TerminalNode CAP_Z() { return getToken(SCGParser.CAP_Z, 0); }
		public TerminalNode LEFT_BRACE() { return getToken(SCGParser.LEFT_BRACE, 0); }
		public TerminalNode RIGHT_BRACE() { return getToken(SCGParser.RIGHT_BRACE, 0); }
		public TerminalNode CARAT() { return getToken(SCGParser.CARAT, 0); }
		public TerminalNode UNDERSCORE() { return getToken(SCGParser.UNDERSCORE, 0); }
		public TerminalNode ACCENT() { return getToken(SCGParser.ACCENT, 0); }
		public TerminalNode A() { return getToken(SCGParser.A, 0); }
		public TerminalNode B() { return getToken(SCGParser.B, 0); }
		public TerminalNode C() { return getToken(SCGParser.C, 0); }
		public TerminalNode D() { return getToken(SCGParser.D, 0); }
		public TerminalNode E() { return getToken(SCGParser.E, 0); }
		public TerminalNode F() { return getToken(SCGParser.F, 0); }
		public TerminalNode G() { return getToken(SCGParser.G, 0); }
		public TerminalNode H() { return getToken(SCGParser.H, 0); }
		public TerminalNode I() { return getToken(SCGParser.I, 0); }
		public TerminalNode J() { return getToken(SCGParser.J, 0); }
		public TerminalNode K() { return getToken(SCGParser.K, 0); }
		public TerminalNode L() { return getToken(SCGParser.L, 0); }
		public TerminalNode M() { return getToken(SCGParser.M, 0); }
		public TerminalNode N() { return getToken(SCGParser.N, 0); }
		public TerminalNode O() { return getToken(SCGParser.O, 0); }
		public TerminalNode P() { return getToken(SCGParser.P, 0); }
		public TerminalNode Q() { return getToken(SCGParser.Q, 0); }
		public TerminalNode R() { return getToken(SCGParser.R, 0); }
		public TerminalNode S() { return getToken(SCGParser.S, 0); }
		public TerminalNode T() { return getToken(SCGParser.T, 0); }
		public TerminalNode U() { return getToken(SCGParser.U, 0); }
		public TerminalNode V() { return getToken(SCGParser.V, 0); }
		public TerminalNode W() { return getToken(SCGParser.W, 0); }
		public TerminalNode X() { return getToken(SCGParser.X, 0); }
		public TerminalNode Y() { return getToken(SCGParser.Y, 0); }
		public TerminalNode Z() { return getToken(SCGParser.Z, 0); }
		public TerminalNode LEFT_CURLY_BRACE() { return getToken(SCGParser.LEFT_CURLY_BRACE, 0); }
		public TerminalNode PIPE() { return getToken(SCGParser.PIPE, 0); }
		public TerminalNode RIGHT_CURLY_BRACE() { return getToken(SCGParser.RIGHT_CURLY_BRACE, 0); }
		public TerminalNode TILDE() { return getToken(SCGParser.TILDE, 0); }
		public Utf8_2Context utf8_2() {
			return getRuleContext(Utf8_2Context.class,0);
		}
		public Utf8_3Context utf8_3() {
			return getRuleContext(Utf8_3Context.class,0);
		}
		public Utf8_4Context utf8_4() {
			return getRuleContext(Utf8_4Context.class,0);
		}
		public AnynonescapedcharContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anynonescapedchar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterAnynonescapedchar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitAnynonescapedchar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitAnynonescapedchar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnynonescapedcharContext anynonescapedchar() throws RecognitionException {
		AnynonescapedcharContext _localctx = new AnynonescapedcharContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_anynonescapedchar);
		int _la;
		try {
			setState(419);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TAB:
				enterOuterAlt(_localctx, 1);
				{
				setState(410);
				htab();
				}
				break;
			case CR:
				enterOuterAlt(_localctx, 2);
				{
				setState(411);
				cr();
				}
				break;
			case LF:
				enterOuterAlt(_localctx, 3);
				{
				setState(412);
				lf();
				}
				break;
			case SPACE:
			case EXCLAMATION:
				enterOuterAlt(_localctx, 4);
				{
				setState(413);
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
				enterOuterAlt(_localctx, 5);
				{
				setState(414);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << HASH) | (1L << DOLLAR) | (1L << PERCENT) | (1L << AMPERSAND) | (1L << APOSTROPHE) | (1L << LEFT_PAREN) | (1L << RIGHT_PAREN) | (1L << ASTERISK) | (1L << PLUS) | (1L << COMMA) | (1L << DASH) | (1L << PERIOD) | (1L << SLASH) | (1L << ZERO) | (1L << ONE) | (1L << TWO) | (1L << THREE) | (1L << FOUR) | (1L << FIVE) | (1L << SIX) | (1L << SEVEN) | (1L << EIGHT) | (1L << NINE) | (1L << COLON) | (1L << SEMICOLON) | (1L << LESS_THAN) | (1L << EQUALS) | (1L << GREATER_THAN) | (1L << QUESTION) | (1L << AT) | (1L << CAP_A) | (1L << CAP_B) | (1L << CAP_C) | (1L << CAP_D) | (1L << CAP_E) | (1L << CAP_F) | (1L << CAP_G) | (1L << CAP_H) | (1L << CAP_I) | (1L << CAP_J) | (1L << CAP_K) | (1L << CAP_L) | (1L << CAP_M) | (1L << CAP_N) | (1L << CAP_O) | (1L << CAP_P) | (1L << CAP_Q) | (1L << CAP_R) | (1L << CAP_S) | (1L << CAP_T) | (1L << CAP_U) | (1L << CAP_V) | (1L << CAP_W) | (1L << CAP_X) | (1L << CAP_Y) | (1L << CAP_Z) | (1L << LEFT_BRACE))) != 0)) ) {
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
				enterOuterAlt(_localctx, 6);
				{
				setState(415);
				_la = _input.LA(1);
				if ( !(((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (RIGHT_BRACE - 65)) | (1L << (CARAT - 65)) | (1L << (UNDERSCORE - 65)) | (1L << (ACCENT - 65)) | (1L << (A - 65)) | (1L << (B - 65)) | (1L << (C - 65)) | (1L << (D - 65)) | (1L << (E - 65)) | (1L << (F - 65)) | (1L << (G - 65)) | (1L << (H - 65)) | (1L << (I - 65)) | (1L << (J - 65)) | (1L << (K - 65)) | (1L << (L - 65)) | (1L << (M - 65)) | (1L << (N - 65)) | (1L << (O - 65)) | (1L << (P - 65)) | (1L << (Q - 65)) | (1L << (R - 65)) | (1L << (S - 65)) | (1L << (T - 65)) | (1L << (U - 65)) | (1L << (V - 65)) | (1L << (W - 65)) | (1L << (X - 65)) | (1L << (Y - 65)) | (1L << (Z - 65)) | (1L << (LEFT_CURLY_BRACE - 65)) | (1L << (PIPE - 65)) | (1L << (RIGHT_CURLY_BRACE - 65)) | (1L << (TILDE - 65)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case U_00C2:
			case U_00C3:
			case U_00C4:
			case U_00C5:
			case U_00C6:
			case U_00C7:
			case U_00C8:
			case U_00C9:
			case U_00CA:
			case U_00CB:
			case U_00CC:
			case U_00CD:
			case U_00CE:
			case U_00CF:
			case U_00D0:
			case U_00D1:
			case U_00D2:
			case U_00D3:
			case U_00D4:
			case U_00D5:
			case U_00D6:
			case U_00D7:
			case U_00D8:
			case U_00D9:
			case U_00DA:
			case U_00DB:
			case U_00DC:
			case U_00DD:
			case U_00DE:
			case U_00DF:
				enterOuterAlt(_localctx, 7);
				{
				setState(416);
				utf8_2();
				}
				break;
			case U_00E0:
			case U_00E1:
			case U_00E2:
			case U_00E3:
			case U_00E4:
			case U_00E5:
			case U_00E6:
			case U_00E7:
			case U_00E8:
			case U_00E9:
			case U_00EA:
			case U_00EB:
			case U_00EC:
			case U_00ED:
			case U_00EE:
			case U_00EF:
				enterOuterAlt(_localctx, 8);
				{
				setState(417);
				utf8_3();
				}
				break;
			case U_00F0:
			case U_00F1:
			case U_00F2:
			case U_00F3:
			case U_00F4:
				enterOuterAlt(_localctx, 9);
				{
				setState(418);
				utf8_4();
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
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterEscapedchar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitEscapedchar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitEscapedchar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EscapedcharContext escapedchar() throws RecognitionException {
		EscapedcharContext _localctx = new EscapedcharContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_escapedchar);
		try {
			setState(427);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(421);
				bs();
				setState(422);
				qm();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(424);
				bs();
				setState(425);
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

	public static class Utf8_2Context extends ParserRuleContext {
		public Utf8_tailContext utf8_tail() {
			return getRuleContext(Utf8_tailContext.class,0);
		}
		public TerminalNode U_00C2() { return getToken(SCGParser.U_00C2, 0); }
		public TerminalNode U_00C3() { return getToken(SCGParser.U_00C3, 0); }
		public TerminalNode U_00C4() { return getToken(SCGParser.U_00C4, 0); }
		public TerminalNode U_00C5() { return getToken(SCGParser.U_00C5, 0); }
		public TerminalNode U_00C6() { return getToken(SCGParser.U_00C6, 0); }
		public TerminalNode U_00C7() { return getToken(SCGParser.U_00C7, 0); }
		public TerminalNode U_00C8() { return getToken(SCGParser.U_00C8, 0); }
		public TerminalNode U_00C9() { return getToken(SCGParser.U_00C9, 0); }
		public TerminalNode U_00CA() { return getToken(SCGParser.U_00CA, 0); }
		public TerminalNode U_00CB() { return getToken(SCGParser.U_00CB, 0); }
		public TerminalNode U_00CC() { return getToken(SCGParser.U_00CC, 0); }
		public TerminalNode U_00CD() { return getToken(SCGParser.U_00CD, 0); }
		public TerminalNode U_00CE() { return getToken(SCGParser.U_00CE, 0); }
		public TerminalNode U_00CF() { return getToken(SCGParser.U_00CF, 0); }
		public TerminalNode U_00D0() { return getToken(SCGParser.U_00D0, 0); }
		public TerminalNode U_00D1() { return getToken(SCGParser.U_00D1, 0); }
		public TerminalNode U_00D2() { return getToken(SCGParser.U_00D2, 0); }
		public TerminalNode U_00D3() { return getToken(SCGParser.U_00D3, 0); }
		public TerminalNode U_00D4() { return getToken(SCGParser.U_00D4, 0); }
		public TerminalNode U_00D5() { return getToken(SCGParser.U_00D5, 0); }
		public TerminalNode U_00D6() { return getToken(SCGParser.U_00D6, 0); }
		public TerminalNode U_00D7() { return getToken(SCGParser.U_00D7, 0); }
		public TerminalNode U_00D8() { return getToken(SCGParser.U_00D8, 0); }
		public TerminalNode U_00D9() { return getToken(SCGParser.U_00D9, 0); }
		public TerminalNode U_00DA() { return getToken(SCGParser.U_00DA, 0); }
		public TerminalNode U_00DB() { return getToken(SCGParser.U_00DB, 0); }
		public TerminalNode U_00DC() { return getToken(SCGParser.U_00DC, 0); }
		public TerminalNode U_00DD() { return getToken(SCGParser.U_00DD, 0); }
		public TerminalNode U_00DE() { return getToken(SCGParser.U_00DE, 0); }
		public TerminalNode U_00DF() { return getToken(SCGParser.U_00DF, 0); }
		public Utf8_2Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_utf8_2; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterUtf8_2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitUtf8_2(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitUtf8_2(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Utf8_2Context utf8_2() throws RecognitionException {
		Utf8_2Context _localctx = new Utf8_2Context(_ctx, getState());
		enterRule(_localctx, 74, RULE_utf8_2);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(429);
			_la = _input.LA(1);
			if ( !(((((_la - 163)) & ~0x3f) == 0 && ((1L << (_la - 163)) & ((1L << (U_00C2 - 163)) | (1L << (U_00C3 - 163)) | (1L << (U_00C4 - 163)) | (1L << (U_00C5 - 163)) | (1L << (U_00C6 - 163)) | (1L << (U_00C7 - 163)) | (1L << (U_00C8 - 163)) | (1L << (U_00C9 - 163)) | (1L << (U_00CA - 163)) | (1L << (U_00CB - 163)) | (1L << (U_00CC - 163)) | (1L << (U_00CD - 163)) | (1L << (U_00CE - 163)) | (1L << (U_00CF - 163)) | (1L << (U_00D0 - 163)) | (1L << (U_00D1 - 163)) | (1L << (U_00D2 - 163)) | (1L << (U_00D3 - 163)) | (1L << (U_00D4 - 163)) | (1L << (U_00D5 - 163)) | (1L << (U_00D6 - 163)) | (1L << (U_00D7 - 163)) | (1L << (U_00D8 - 163)) | (1L << (U_00D9 - 163)) | (1L << (U_00DA - 163)) | (1L << (U_00DB - 163)) | (1L << (U_00DC - 163)) | (1L << (U_00DD - 163)) | (1L << (U_00DE - 163)) | (1L << (U_00DF - 163)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(430);
			utf8_tail();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Utf8_3Context extends ParserRuleContext {
		public TerminalNode U_00E0() { return getToken(SCGParser.U_00E0, 0); }
		public List<Utf8_tailContext> utf8_tail() {
			return getRuleContexts(Utf8_tailContext.class);
		}
		public Utf8_tailContext utf8_tail(int i) {
			return getRuleContext(Utf8_tailContext.class,i);
		}
		public TerminalNode U_00A0() { return getToken(SCGParser.U_00A0, 0); }
		public TerminalNode U_00A1() { return getToken(SCGParser.U_00A1, 0); }
		public TerminalNode U_00A2() { return getToken(SCGParser.U_00A2, 0); }
		public TerminalNode U_00A3() { return getToken(SCGParser.U_00A3, 0); }
		public TerminalNode U_00A4() { return getToken(SCGParser.U_00A4, 0); }
		public TerminalNode U_00A5() { return getToken(SCGParser.U_00A5, 0); }
		public TerminalNode U_00A6() { return getToken(SCGParser.U_00A6, 0); }
		public TerminalNode U_00A7() { return getToken(SCGParser.U_00A7, 0); }
		public TerminalNode U_00A8() { return getToken(SCGParser.U_00A8, 0); }
		public TerminalNode U_00A9() { return getToken(SCGParser.U_00A9, 0); }
		public TerminalNode U_00AA() { return getToken(SCGParser.U_00AA, 0); }
		public TerminalNode U_00AB() { return getToken(SCGParser.U_00AB, 0); }
		public TerminalNode U_00AC() { return getToken(SCGParser.U_00AC, 0); }
		public TerminalNode U_00AD() { return getToken(SCGParser.U_00AD, 0); }
		public TerminalNode U_00AE() { return getToken(SCGParser.U_00AE, 0); }
		public TerminalNode U_00AF() { return getToken(SCGParser.U_00AF, 0); }
		public TerminalNode U_00B0() { return getToken(SCGParser.U_00B0, 0); }
		public TerminalNode U_00B1() { return getToken(SCGParser.U_00B1, 0); }
		public TerminalNode U_00B2() { return getToken(SCGParser.U_00B2, 0); }
		public TerminalNode U_00B3() { return getToken(SCGParser.U_00B3, 0); }
		public TerminalNode U_00B4() { return getToken(SCGParser.U_00B4, 0); }
		public TerminalNode U_00B5() { return getToken(SCGParser.U_00B5, 0); }
		public TerminalNode U_00B6() { return getToken(SCGParser.U_00B6, 0); }
		public TerminalNode U_00B7() { return getToken(SCGParser.U_00B7, 0); }
		public TerminalNode U_00B8() { return getToken(SCGParser.U_00B8, 0); }
		public TerminalNode U_00B9() { return getToken(SCGParser.U_00B9, 0); }
		public TerminalNode U_00BA() { return getToken(SCGParser.U_00BA, 0); }
		public TerminalNode U_00BB() { return getToken(SCGParser.U_00BB, 0); }
		public TerminalNode U_00BC() { return getToken(SCGParser.U_00BC, 0); }
		public TerminalNode U_00BD() { return getToken(SCGParser.U_00BD, 0); }
		public TerminalNode U_00BE() { return getToken(SCGParser.U_00BE, 0); }
		public TerminalNode U_00BF() { return getToken(SCGParser.U_00BF, 0); }
		public TerminalNode U_00E1() { return getToken(SCGParser.U_00E1, 0); }
		public TerminalNode U_00E2() { return getToken(SCGParser.U_00E2, 0); }
		public TerminalNode U_00E3() { return getToken(SCGParser.U_00E3, 0); }
		public TerminalNode U_00E4() { return getToken(SCGParser.U_00E4, 0); }
		public TerminalNode U_00E5() { return getToken(SCGParser.U_00E5, 0); }
		public TerminalNode U_00E6() { return getToken(SCGParser.U_00E6, 0); }
		public TerminalNode U_00E7() { return getToken(SCGParser.U_00E7, 0); }
		public TerminalNode U_00E8() { return getToken(SCGParser.U_00E8, 0); }
		public TerminalNode U_00E9() { return getToken(SCGParser.U_00E9, 0); }
		public TerminalNode U_00EA() { return getToken(SCGParser.U_00EA, 0); }
		public TerminalNode U_00EB() { return getToken(SCGParser.U_00EB, 0); }
		public TerminalNode U_00EC() { return getToken(SCGParser.U_00EC, 0); }
		public TerminalNode U_00ED() { return getToken(SCGParser.U_00ED, 0); }
		public TerminalNode U_0080() { return getToken(SCGParser.U_0080, 0); }
		public TerminalNode U_0081() { return getToken(SCGParser.U_0081, 0); }
		public TerminalNode U_0082() { return getToken(SCGParser.U_0082, 0); }
		public TerminalNode U_0083() { return getToken(SCGParser.U_0083, 0); }
		public TerminalNode U_0084() { return getToken(SCGParser.U_0084, 0); }
		public TerminalNode U_0085() { return getToken(SCGParser.U_0085, 0); }
		public TerminalNode U_0086() { return getToken(SCGParser.U_0086, 0); }
		public TerminalNode U_0087() { return getToken(SCGParser.U_0087, 0); }
		public TerminalNode U_0088() { return getToken(SCGParser.U_0088, 0); }
		public TerminalNode U_0089() { return getToken(SCGParser.U_0089, 0); }
		public TerminalNode U_008A() { return getToken(SCGParser.U_008A, 0); }
		public TerminalNode U_008B() { return getToken(SCGParser.U_008B, 0); }
		public TerminalNode U_008C() { return getToken(SCGParser.U_008C, 0); }
		public TerminalNode U_008D() { return getToken(SCGParser.U_008D, 0); }
		public TerminalNode U_008E() { return getToken(SCGParser.U_008E, 0); }
		public TerminalNode U_008F() { return getToken(SCGParser.U_008F, 0); }
		public TerminalNode U_0090() { return getToken(SCGParser.U_0090, 0); }
		public TerminalNode U_0091() { return getToken(SCGParser.U_0091, 0); }
		public TerminalNode U_0092() { return getToken(SCGParser.U_0092, 0); }
		public TerminalNode U_0093() { return getToken(SCGParser.U_0093, 0); }
		public TerminalNode U_0094() { return getToken(SCGParser.U_0094, 0); }
		public TerminalNode U_0095() { return getToken(SCGParser.U_0095, 0); }
		public TerminalNode U_0096() { return getToken(SCGParser.U_0096, 0); }
		public TerminalNode U_0097() { return getToken(SCGParser.U_0097, 0); }
		public TerminalNode U_0098() { return getToken(SCGParser.U_0098, 0); }
		public TerminalNode U_0099() { return getToken(SCGParser.U_0099, 0); }
		public TerminalNode U_009A() { return getToken(SCGParser.U_009A, 0); }
		public TerminalNode U_009B() { return getToken(SCGParser.U_009B, 0); }
		public TerminalNode U_009C() { return getToken(SCGParser.U_009C, 0); }
		public TerminalNode U_009D() { return getToken(SCGParser.U_009D, 0); }
		public TerminalNode U_009E() { return getToken(SCGParser.U_009E, 0); }
		public TerminalNode U_009F() { return getToken(SCGParser.U_009F, 0); }
		public TerminalNode U_00EE() { return getToken(SCGParser.U_00EE, 0); }
		public TerminalNode U_00EF() { return getToken(SCGParser.U_00EF, 0); }
		public Utf8_3Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_utf8_3; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterUtf8_3(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitUtf8_3(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitUtf8_3(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Utf8_3Context utf8_3() throws RecognitionException {
		Utf8_3Context _localctx = new Utf8_3Context(_ctx, getState());
		enterRule(_localctx, 76, RULE_utf8_3);
		int _la;
		try {
			setState(446);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case U_00E0:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(432);
				match(U_00E0);
				setState(433);
				_la = _input.LA(1);
				if ( !(((((_la - 131)) & ~0x3f) == 0 && ((1L << (_la - 131)) & ((1L << (U_00A0 - 131)) | (1L << (U_00A1 - 131)) | (1L << (U_00A2 - 131)) | (1L << (U_00A3 - 131)) | (1L << (U_00A4 - 131)) | (1L << (U_00A5 - 131)) | (1L << (U_00A6 - 131)) | (1L << (U_00A7 - 131)) | (1L << (U_00A8 - 131)) | (1L << (U_00A9 - 131)) | (1L << (U_00AA - 131)) | (1L << (U_00AB - 131)) | (1L << (U_00AC - 131)) | (1L << (U_00AD - 131)) | (1L << (U_00AE - 131)) | (1L << (U_00AF - 131)) | (1L << (U_00B0 - 131)) | (1L << (U_00B1 - 131)) | (1L << (U_00B2 - 131)) | (1L << (U_00B3 - 131)) | (1L << (U_00B4 - 131)) | (1L << (U_00B5 - 131)) | (1L << (U_00B6 - 131)) | (1L << (U_00B7 - 131)) | (1L << (U_00B8 - 131)) | (1L << (U_00B9 - 131)) | (1L << (U_00BA - 131)) | (1L << (U_00BB - 131)) | (1L << (U_00BC - 131)) | (1L << (U_00BD - 131)) | (1L << (U_00BE - 131)) | (1L << (U_00BF - 131)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(434);
				utf8_tail();
				}
				}
				break;
			case U_00E1:
			case U_00E2:
			case U_00E3:
			case U_00E4:
			case U_00E5:
			case U_00E6:
			case U_00E7:
			case U_00E8:
			case U_00E9:
			case U_00EA:
			case U_00EB:
			case U_00EC:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(435);
				_la = _input.LA(1);
				if ( !(((((_la - 194)) & ~0x3f) == 0 && ((1L << (_la - 194)) & ((1L << (U_00E1 - 194)) | (1L << (U_00E2 - 194)) | (1L << (U_00E3 - 194)) | (1L << (U_00E4 - 194)) | (1L << (U_00E5 - 194)) | (1L << (U_00E6 - 194)) | (1L << (U_00E7 - 194)) | (1L << (U_00E8 - 194)) | (1L << (U_00E9 - 194)) | (1L << (U_00EA - 194)) | (1L << (U_00EB - 194)) | (1L << (U_00EC - 194)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				{
				{
				setState(436);
				utf8_tail();
				}
				{
				setState(437);
				utf8_tail();
				}
				}
				}
				}
				break;
			case U_00ED:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(439);
				match(U_00ED);
				setState(440);
				_la = _input.LA(1);
				if ( !(((((_la - 99)) & ~0x3f) == 0 && ((1L << (_la - 99)) & ((1L << (U_0080 - 99)) | (1L << (U_0081 - 99)) | (1L << (U_0082 - 99)) | (1L << (U_0083 - 99)) | (1L << (U_0084 - 99)) | (1L << (U_0085 - 99)) | (1L << (U_0086 - 99)) | (1L << (U_0087 - 99)) | (1L << (U_0088 - 99)) | (1L << (U_0089 - 99)) | (1L << (U_008A - 99)) | (1L << (U_008B - 99)) | (1L << (U_008C - 99)) | (1L << (U_008D - 99)) | (1L << (U_008E - 99)) | (1L << (U_008F - 99)) | (1L << (U_0090 - 99)) | (1L << (U_0091 - 99)) | (1L << (U_0092 - 99)) | (1L << (U_0093 - 99)) | (1L << (U_0094 - 99)) | (1L << (U_0095 - 99)) | (1L << (U_0096 - 99)) | (1L << (U_0097 - 99)) | (1L << (U_0098 - 99)) | (1L << (U_0099 - 99)) | (1L << (U_009A - 99)) | (1L << (U_009B - 99)) | (1L << (U_009C - 99)) | (1L << (U_009D - 99)) | (1L << (U_009E - 99)) | (1L << (U_009F - 99)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(441);
				utf8_tail();
				}
				}
				break;
			case U_00EE:
			case U_00EF:
				enterOuterAlt(_localctx, 4);
				{
				{
				setState(442);
				_la = _input.LA(1);
				if ( !(_la==U_00EE || _la==U_00EF) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				{
				{
				setState(443);
				utf8_tail();
				}
				{
				setState(444);
				utf8_tail();
				}
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

	public static class Utf8_4Context extends ParserRuleContext {
		public TerminalNode U_00F0() { return getToken(SCGParser.U_00F0, 0); }
		public TerminalNode U_0090() { return getToken(SCGParser.U_0090, 0); }
		public TerminalNode U_0091() { return getToken(SCGParser.U_0091, 0); }
		public TerminalNode U_0092() { return getToken(SCGParser.U_0092, 0); }
		public TerminalNode U_0093() { return getToken(SCGParser.U_0093, 0); }
		public TerminalNode U_0094() { return getToken(SCGParser.U_0094, 0); }
		public TerminalNode U_0095() { return getToken(SCGParser.U_0095, 0); }
		public TerminalNode U_0096() { return getToken(SCGParser.U_0096, 0); }
		public TerminalNode U_0097() { return getToken(SCGParser.U_0097, 0); }
		public TerminalNode U_0098() { return getToken(SCGParser.U_0098, 0); }
		public TerminalNode U_0099() { return getToken(SCGParser.U_0099, 0); }
		public TerminalNode U_009A() { return getToken(SCGParser.U_009A, 0); }
		public TerminalNode U_009B() { return getToken(SCGParser.U_009B, 0); }
		public TerminalNode U_009C() { return getToken(SCGParser.U_009C, 0); }
		public TerminalNode U_009D() { return getToken(SCGParser.U_009D, 0); }
		public TerminalNode U_009E() { return getToken(SCGParser.U_009E, 0); }
		public TerminalNode U_009F() { return getToken(SCGParser.U_009F, 0); }
		public TerminalNode U_00A0() { return getToken(SCGParser.U_00A0, 0); }
		public TerminalNode U_00A1() { return getToken(SCGParser.U_00A1, 0); }
		public TerminalNode U_00A2() { return getToken(SCGParser.U_00A2, 0); }
		public TerminalNode U_00A3() { return getToken(SCGParser.U_00A3, 0); }
		public TerminalNode U_00A4() { return getToken(SCGParser.U_00A4, 0); }
		public TerminalNode U_00A5() { return getToken(SCGParser.U_00A5, 0); }
		public TerminalNode U_00A6() { return getToken(SCGParser.U_00A6, 0); }
		public TerminalNode U_00A7() { return getToken(SCGParser.U_00A7, 0); }
		public TerminalNode U_00A8() { return getToken(SCGParser.U_00A8, 0); }
		public TerminalNode U_00A9() { return getToken(SCGParser.U_00A9, 0); }
		public TerminalNode U_00AA() { return getToken(SCGParser.U_00AA, 0); }
		public TerminalNode U_00AB() { return getToken(SCGParser.U_00AB, 0); }
		public TerminalNode U_00AC() { return getToken(SCGParser.U_00AC, 0); }
		public TerminalNode U_00AD() { return getToken(SCGParser.U_00AD, 0); }
		public TerminalNode U_00AE() { return getToken(SCGParser.U_00AE, 0); }
		public TerminalNode U_00AF() { return getToken(SCGParser.U_00AF, 0); }
		public TerminalNode U_00B0() { return getToken(SCGParser.U_00B0, 0); }
		public TerminalNode U_00B1() { return getToken(SCGParser.U_00B1, 0); }
		public TerminalNode U_00B2() { return getToken(SCGParser.U_00B2, 0); }
		public TerminalNode U_00B3() { return getToken(SCGParser.U_00B3, 0); }
		public TerminalNode U_00B4() { return getToken(SCGParser.U_00B4, 0); }
		public TerminalNode U_00B5() { return getToken(SCGParser.U_00B5, 0); }
		public TerminalNode U_00B6() { return getToken(SCGParser.U_00B6, 0); }
		public TerminalNode U_00B7() { return getToken(SCGParser.U_00B7, 0); }
		public TerminalNode U_00B8() { return getToken(SCGParser.U_00B8, 0); }
		public TerminalNode U_00B9() { return getToken(SCGParser.U_00B9, 0); }
		public TerminalNode U_00BA() { return getToken(SCGParser.U_00BA, 0); }
		public TerminalNode U_00BB() { return getToken(SCGParser.U_00BB, 0); }
		public TerminalNode U_00BC() { return getToken(SCGParser.U_00BC, 0); }
		public TerminalNode U_00BD() { return getToken(SCGParser.U_00BD, 0); }
		public TerminalNode U_00BE() { return getToken(SCGParser.U_00BE, 0); }
		public TerminalNode U_00BF() { return getToken(SCGParser.U_00BF, 0); }
		public List<Utf8_tailContext> utf8_tail() {
			return getRuleContexts(Utf8_tailContext.class);
		}
		public Utf8_tailContext utf8_tail(int i) {
			return getRuleContext(Utf8_tailContext.class,i);
		}
		public TerminalNode U_00F1() { return getToken(SCGParser.U_00F1, 0); }
		public TerminalNode U_00F2() { return getToken(SCGParser.U_00F2, 0); }
		public TerminalNode U_00F3() { return getToken(SCGParser.U_00F3, 0); }
		public TerminalNode U_00F4() { return getToken(SCGParser.U_00F4, 0); }
		public TerminalNode U_0080() { return getToken(SCGParser.U_0080, 0); }
		public TerminalNode U_0081() { return getToken(SCGParser.U_0081, 0); }
		public TerminalNode U_0082() { return getToken(SCGParser.U_0082, 0); }
		public TerminalNode U_0083() { return getToken(SCGParser.U_0083, 0); }
		public TerminalNode U_0084() { return getToken(SCGParser.U_0084, 0); }
		public TerminalNode U_0085() { return getToken(SCGParser.U_0085, 0); }
		public TerminalNode U_0086() { return getToken(SCGParser.U_0086, 0); }
		public TerminalNode U_0087() { return getToken(SCGParser.U_0087, 0); }
		public TerminalNode U_0088() { return getToken(SCGParser.U_0088, 0); }
		public TerminalNode U_0089() { return getToken(SCGParser.U_0089, 0); }
		public TerminalNode U_008A() { return getToken(SCGParser.U_008A, 0); }
		public TerminalNode U_008B() { return getToken(SCGParser.U_008B, 0); }
		public TerminalNode U_008C() { return getToken(SCGParser.U_008C, 0); }
		public TerminalNode U_008D() { return getToken(SCGParser.U_008D, 0); }
		public TerminalNode U_008E() { return getToken(SCGParser.U_008E, 0); }
		public TerminalNode U_008F() { return getToken(SCGParser.U_008F, 0); }
		public Utf8_4Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_utf8_4; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterUtf8_4(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitUtf8_4(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitUtf8_4(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Utf8_4Context utf8_4() throws RecognitionException {
		Utf8_4Context _localctx = new Utf8_4Context(_ctx, getState());
		enterRule(_localctx, 78, RULE_utf8_4);
		int _la;
		try {
			setState(463);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case U_00F0:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(448);
				match(U_00F0);
				setState(449);
				_la = _input.LA(1);
				if ( !(((((_la - 115)) & ~0x3f) == 0 && ((1L << (_la - 115)) & ((1L << (U_0090 - 115)) | (1L << (U_0091 - 115)) | (1L << (U_0092 - 115)) | (1L << (U_0093 - 115)) | (1L << (U_0094 - 115)) | (1L << (U_0095 - 115)) | (1L << (U_0096 - 115)) | (1L << (U_0097 - 115)) | (1L << (U_0098 - 115)) | (1L << (U_0099 - 115)) | (1L << (U_009A - 115)) | (1L << (U_009B - 115)) | (1L << (U_009C - 115)) | (1L << (U_009D - 115)) | (1L << (U_009E - 115)) | (1L << (U_009F - 115)) | (1L << (U_00A0 - 115)) | (1L << (U_00A1 - 115)) | (1L << (U_00A2 - 115)) | (1L << (U_00A3 - 115)) | (1L << (U_00A4 - 115)) | (1L << (U_00A5 - 115)) | (1L << (U_00A6 - 115)) | (1L << (U_00A7 - 115)) | (1L << (U_00A8 - 115)) | (1L << (U_00A9 - 115)) | (1L << (U_00AA - 115)) | (1L << (U_00AB - 115)) | (1L << (U_00AC - 115)) | (1L << (U_00AD - 115)) | (1L << (U_00AE - 115)) | (1L << (U_00AF - 115)) | (1L << (U_00B0 - 115)) | (1L << (U_00B1 - 115)) | (1L << (U_00B2 - 115)) | (1L << (U_00B3 - 115)) | (1L << (U_00B4 - 115)) | (1L << (U_00B5 - 115)) | (1L << (U_00B6 - 115)) | (1L << (U_00B7 - 115)) | (1L << (U_00B8 - 115)) | (1L << (U_00B9 - 115)) | (1L << (U_00BA - 115)) | (1L << (U_00BB - 115)) | (1L << (U_00BC - 115)) | (1L << (U_00BD - 115)) | (1L << (U_00BE - 115)) | (1L << (U_00BF - 115)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				{
				{
				setState(450);
				utf8_tail();
				}
				{
				setState(451);
				utf8_tail();
				}
				}
				}
				}
				break;
			case U_00F1:
			case U_00F2:
			case U_00F3:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(453);
				_la = _input.LA(1);
				if ( !(((((_la - 210)) & ~0x3f) == 0 && ((1L << (_la - 210)) & ((1L << (U_00F1 - 210)) | (1L << (U_00F2 - 210)) | (1L << (U_00F3 - 210)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				{
				{
				setState(454);
				utf8_tail();
				}
				{
				setState(455);
				utf8_tail();
				}
				{
				setState(456);
				utf8_tail();
				}
				}
				}
				}
				break;
			case U_00F4:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(458);
				match(U_00F4);
				setState(459);
				_la = _input.LA(1);
				if ( !(((((_la - 99)) & ~0x3f) == 0 && ((1L << (_la - 99)) & ((1L << (U_0080 - 99)) | (1L << (U_0081 - 99)) | (1L << (U_0082 - 99)) | (1L << (U_0083 - 99)) | (1L << (U_0084 - 99)) | (1L << (U_0085 - 99)) | (1L << (U_0086 - 99)) | (1L << (U_0087 - 99)) | (1L << (U_0088 - 99)) | (1L << (U_0089 - 99)) | (1L << (U_008A - 99)) | (1L << (U_008B - 99)) | (1L << (U_008C - 99)) | (1L << (U_008D - 99)) | (1L << (U_008E - 99)) | (1L << (U_008F - 99)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				{
				{
				setState(460);
				utf8_tail();
				}
				{
				setState(461);
				utf8_tail();
				}
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

	public static class Utf8_tailContext extends ParserRuleContext {
		public TerminalNode U_0080() { return getToken(SCGParser.U_0080, 0); }
		public TerminalNode U_0081() { return getToken(SCGParser.U_0081, 0); }
		public TerminalNode U_0082() { return getToken(SCGParser.U_0082, 0); }
		public TerminalNode U_0083() { return getToken(SCGParser.U_0083, 0); }
		public TerminalNode U_0084() { return getToken(SCGParser.U_0084, 0); }
		public TerminalNode U_0085() { return getToken(SCGParser.U_0085, 0); }
		public TerminalNode U_0086() { return getToken(SCGParser.U_0086, 0); }
		public TerminalNode U_0087() { return getToken(SCGParser.U_0087, 0); }
		public TerminalNode U_0088() { return getToken(SCGParser.U_0088, 0); }
		public TerminalNode U_0089() { return getToken(SCGParser.U_0089, 0); }
		public TerminalNode U_008A() { return getToken(SCGParser.U_008A, 0); }
		public TerminalNode U_008B() { return getToken(SCGParser.U_008B, 0); }
		public TerminalNode U_008C() { return getToken(SCGParser.U_008C, 0); }
		public TerminalNode U_008D() { return getToken(SCGParser.U_008D, 0); }
		public TerminalNode U_008E() { return getToken(SCGParser.U_008E, 0); }
		public TerminalNode U_008F() { return getToken(SCGParser.U_008F, 0); }
		public TerminalNode U_0090() { return getToken(SCGParser.U_0090, 0); }
		public TerminalNode U_0091() { return getToken(SCGParser.U_0091, 0); }
		public TerminalNode U_0092() { return getToken(SCGParser.U_0092, 0); }
		public TerminalNode U_0093() { return getToken(SCGParser.U_0093, 0); }
		public TerminalNode U_0094() { return getToken(SCGParser.U_0094, 0); }
		public TerminalNode U_0095() { return getToken(SCGParser.U_0095, 0); }
		public TerminalNode U_0096() { return getToken(SCGParser.U_0096, 0); }
		public TerminalNode U_0097() { return getToken(SCGParser.U_0097, 0); }
		public TerminalNode U_0098() { return getToken(SCGParser.U_0098, 0); }
		public TerminalNode U_0099() { return getToken(SCGParser.U_0099, 0); }
		public TerminalNode U_009A() { return getToken(SCGParser.U_009A, 0); }
		public TerminalNode U_009B() { return getToken(SCGParser.U_009B, 0); }
		public TerminalNode U_009C() { return getToken(SCGParser.U_009C, 0); }
		public TerminalNode U_009D() { return getToken(SCGParser.U_009D, 0); }
		public TerminalNode U_009E() { return getToken(SCGParser.U_009E, 0); }
		public TerminalNode U_009F() { return getToken(SCGParser.U_009F, 0); }
		public TerminalNode U_00A0() { return getToken(SCGParser.U_00A0, 0); }
		public TerminalNode U_00A1() { return getToken(SCGParser.U_00A1, 0); }
		public TerminalNode U_00A2() { return getToken(SCGParser.U_00A2, 0); }
		public TerminalNode U_00A3() { return getToken(SCGParser.U_00A3, 0); }
		public TerminalNode U_00A4() { return getToken(SCGParser.U_00A4, 0); }
		public TerminalNode U_00A5() { return getToken(SCGParser.U_00A5, 0); }
		public TerminalNode U_00A6() { return getToken(SCGParser.U_00A6, 0); }
		public TerminalNode U_00A7() { return getToken(SCGParser.U_00A7, 0); }
		public TerminalNode U_00A8() { return getToken(SCGParser.U_00A8, 0); }
		public TerminalNode U_00A9() { return getToken(SCGParser.U_00A9, 0); }
		public TerminalNode U_00AA() { return getToken(SCGParser.U_00AA, 0); }
		public TerminalNode U_00AB() { return getToken(SCGParser.U_00AB, 0); }
		public TerminalNode U_00AC() { return getToken(SCGParser.U_00AC, 0); }
		public TerminalNode U_00AD() { return getToken(SCGParser.U_00AD, 0); }
		public TerminalNode U_00AE() { return getToken(SCGParser.U_00AE, 0); }
		public TerminalNode U_00AF() { return getToken(SCGParser.U_00AF, 0); }
		public TerminalNode U_00B0() { return getToken(SCGParser.U_00B0, 0); }
		public TerminalNode U_00B1() { return getToken(SCGParser.U_00B1, 0); }
		public TerminalNode U_00B2() { return getToken(SCGParser.U_00B2, 0); }
		public TerminalNode U_00B3() { return getToken(SCGParser.U_00B3, 0); }
		public TerminalNode U_00B4() { return getToken(SCGParser.U_00B4, 0); }
		public TerminalNode U_00B5() { return getToken(SCGParser.U_00B5, 0); }
		public TerminalNode U_00B6() { return getToken(SCGParser.U_00B6, 0); }
		public TerminalNode U_00B7() { return getToken(SCGParser.U_00B7, 0); }
		public TerminalNode U_00B8() { return getToken(SCGParser.U_00B8, 0); }
		public TerminalNode U_00B9() { return getToken(SCGParser.U_00B9, 0); }
		public TerminalNode U_00BA() { return getToken(SCGParser.U_00BA, 0); }
		public TerminalNode U_00BB() { return getToken(SCGParser.U_00BB, 0); }
		public TerminalNode U_00BC() { return getToken(SCGParser.U_00BC, 0); }
		public TerminalNode U_00BD() { return getToken(SCGParser.U_00BD, 0); }
		public TerminalNode U_00BE() { return getToken(SCGParser.U_00BE, 0); }
		public TerminalNode U_00BF() { return getToken(SCGParser.U_00BF, 0); }
		public Utf8_tailContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_utf8_tail; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).enterUtf8_tail(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SCGListener ) ((SCGListener)listener).exitUtf8_tail(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SCGVisitor ) return ((SCGVisitor<? extends T>)visitor).visitUtf8_tail(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Utf8_tailContext utf8_tail() throws RecognitionException {
		Utf8_tailContext _localctx = new Utf8_tailContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_utf8_tail);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(465);
			_la = _input.LA(1);
			if ( !(((((_la - 99)) & ~0x3f) == 0 && ((1L << (_la - 99)) & ((1L << (U_0080 - 99)) | (1L << (U_0081 - 99)) | (1L << (U_0082 - 99)) | (1L << (U_0083 - 99)) | (1L << (U_0084 - 99)) | (1L << (U_0085 - 99)) | (1L << (U_0086 - 99)) | (1L << (U_0087 - 99)) | (1L << (U_0088 - 99)) | (1L << (U_0089 - 99)) | (1L << (U_008A - 99)) | (1L << (U_008B - 99)) | (1L << (U_008C - 99)) | (1L << (U_008D - 99)) | (1L << (U_008E - 99)) | (1L << (U_008F - 99)) | (1L << (U_0090 - 99)) | (1L << (U_0091 - 99)) | (1L << (U_0092 - 99)) | (1L << (U_0093 - 99)) | (1L << (U_0094 - 99)) | (1L << (U_0095 - 99)) | (1L << (U_0096 - 99)) | (1L << (U_0097 - 99)) | (1L << (U_0098 - 99)) | (1L << (U_0099 - 99)) | (1L << (U_009A - 99)) | (1L << (U_009B - 99)) | (1L << (U_009C - 99)) | (1L << (U_009D - 99)) | (1L << (U_009E - 99)) | (1L << (U_009F - 99)) | (1L << (U_00A0 - 99)) | (1L << (U_00A1 - 99)) | (1L << (U_00A2 - 99)) | (1L << (U_00A3 - 99)) | (1L << (U_00A4 - 99)) | (1L << (U_00A5 - 99)) | (1L << (U_00A6 - 99)) | (1L << (U_00A7 - 99)) | (1L << (U_00A8 - 99)) | (1L << (U_00A9 - 99)) | (1L << (U_00AA - 99)) | (1L << (U_00AB - 99)) | (1L << (U_00AC - 99)) | (1L << (U_00AD - 99)) | (1L << (U_00AE - 99)) | (1L << (U_00AF - 99)) | (1L << (U_00B0 - 99)) | (1L << (U_00B1 - 99)) | (1L << (U_00B2 - 99)) | (1L << (U_00B3 - 99)) | (1L << (U_00B4 - 99)) | (1L << (U_00B5 - 99)) | (1L << (U_00B6 - 99)) | (1L << (U_00B7 - 99)) | (1L << (U_00B8 - 99)) | (1L << (U_00B9 - 99)) | (1L << (U_00BA - 99)) | (1L << (U_00BB - 99)) | (1L << (U_00BC - 99)) | (1L << (U_00BD - 99)) | (1L << (U_00BE - 99)) | (1L << (U_00BF - 99)))) != 0)) ) {
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u00d7\u01d6\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\3\2\3\2"+
		"\3\2\3\2\5\2Y\n\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\5\3d\n\3\3\4\3\4"+
		"\5\4h\n\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\7\7"+
		"x\n\7\f\7\16\7{\13\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\b\u0085\n\b\3\t"+
		"\3\t\3\n\3\n\7\n\u008b\n\n\f\n\16\n\u008e\13\n\3\n\7\n\u0091\n\n\f\n\16"+
		"\n\u0094\13\n\3\13\3\13\5\13\u0098\n\13\3\13\3\13\3\13\5\13\u009d\n\13"+
		"\3\13\3\13\7\13\u00a1\n\13\f\13\16\13\u00a4\13\13\3\f\3\f\3\f\3\f\3\f"+
		"\3\f\3\r\3\r\3\r\3\r\3\r\3\r\7\r\u00b2\n\r\f\r\16\r\u00b5\13\r\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3"+
		"\20\5\20\u00c7\n\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\5\21\u00d0\n\21"+
		"\3\22\3\22\6\22\u00d4\n\22\r\22\16\22\u00d5\3\23\5\23\u00d9\n\23\3\23"+
		"\3\23\5\23\u00dd\n\23\3\24\3\24\7\24\u00e1\n\24\f\24\16\24\u00e4\13\24"+
		"\3\24\5\24\u00e7\n\24\3\25\3\25\3\25\6\25\u00ec\n\25\r\25\16\25\u00ed"+
		"\3\26\3\26\5\26\u00f2\n\26\3\27\3\27\5\27\u00f6\n\27\3\27\3\27\5\27\u00fa"+
		"\n\27\3\27\3\27\5\27\u00fe\n\27\3\27\3\27\5\27\u0102\n\27\3\30\3\30\5"+
		"\30\u0106\n\30\3\30\3\30\5\30\u010a\n\30\3\30\3\30\5\30\u010e\n\30\3\30"+
		"\3\30\5\30\u0112\n\30\3\30\3\30\5\30\u0116\n\30\3\31\3\31\3\31\3\31\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\3\31\5\31\u0177\n\31\5\31\u0179\n\31\3\32"+
		"\3\32\3\32\3\32\7\32\u017f\n\32\f\32\16\32\u0182\13\32\3\33\3\33\3\34"+
		"\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3"+
		"$\3$\3$\5$\u019b\n$\3%\3%\3%\3%\3%\3%\3%\3%\3%\5%\u01a6\n%\3&\3&\3&\3"+
		"&\3&\3&\5&\u01ae\n&\3\'\3\'\3\'\3(\3(\3(\3(\3(\3(\3(\3(\3(\3(\3(\3(\3"+
		"(\3(\5(\u01c1\n(\3)\3)\3)\3)\3)\3)\3)\3)\3)\3)\3)\3)\3)\3)\3)\5)\u01d2"+
		"\n)\3*\3*\3*\2\2+\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62"+
		"\64\668:<>@BDFHJLNPR\2\33\4\2\21\21\23\23\4\2::ZZ\4\288XX\4\2;;[[\4\2"+
		"++KK\4\2,,LL\4\2\'\'GG\4\2\62\62RR\4\299YY\3\2\26\37\3\2\27\37\3\2\7a"+
		"\3\2cd\3\2\6\7\3\2\tA\3\2Cd\3\2\u00a5\u00c2\3\2\u0085\u00a4\3\2\u00c4"+
		"\u00cf\3\2e\u0084\3\2\u00d1\u00d2\3\2u\u00a4\3\2\u00d4\u00d6\3\2et\3\2"+
		"e\u00a4\2\u01ee\2T\3\2\2\2\4]\3\2\2\2\6g\3\2\2\2\bi\3\2\2\2\nm\3\2\2\2"+
		"\fq\3\2\2\2\16|\3\2\2\2\20\u0086\3\2\2\2\22\u0088\3\2\2\2\24\u0097\3\2"+
		"\2\2\26\u00a5\3\2\2\2\30\u00ab\3\2\2\2\32\u00b6\3\2\2\2\34\u00bc\3\2\2"+
		"\2\36\u00c6\3\2\2\2 \u00cf\3\2\2\2\"\u00d3\3\2\2\2$\u00d8\3\2\2\2&\u00e6"+
		"\3\2\2\2(\u00e8\3\2\2\2*\u00f1\3\2\2\2,\u00f5\3\2\2\2.\u0105\3\2\2\2\60"+
		"\u0117\3\2\2\2\62\u0180\3\2\2\2\64\u0183\3\2\2\2\66\u0185\3\2\2\28\u0187"+
		"\3\2\2\2:\u0189\3\2\2\2<\u018b\3\2\2\2>\u018d\3\2\2\2@\u018f\3\2\2\2B"+
		"\u0191\3\2\2\2D\u0193\3\2\2\2F\u019a\3\2\2\2H\u01a5\3\2\2\2J\u01ad\3\2"+
		"\2\2L\u01af\3\2\2\2N\u01c0\3\2\2\2P\u01d1\3\2\2\2R\u01d3\3\2\2\2TX\5\62"+
		"\32\2UV\5\6\4\2VW\5\62\32\2WY\3\2\2\2XU\3\2\2\2XY\3\2\2\2YZ\3\2\2\2Z["+
		"\5\4\3\2[\\\5\62\32\2\\\3\3\2\2\2]c\5\f\7\2^_\5\62\32\2_`\7 \2\2`a\5\62"+
		"\32\2ab\5\24\13\2bd\3\2\2\2c^\3\2\2\2cd\3\2\2\2d\5\3\2\2\2eh\5\b\5\2f"+
		"h\5\n\6\2ge\3\2\2\2gf\3\2\2\2h\7\3\2\2\2ij\7#\2\2jk\7#\2\2kl\7#\2\2l\t"+
		"\3\2\2\2mn\7\"\2\2no\7\"\2\2op\7\"\2\2p\13\3\2\2\2qy\5\16\b\2rs\5\62\32"+
		"\2st\7\21\2\2tu\5\62\32\2uv\5\16\b\2vx\3\2\2\2wr\3\2\2\2x{\3\2\2\2yw\3"+
		"\2\2\2yz\3\2\2\2z\r\3\2\2\2{y\3\2\2\2|\u0084\5\20\t\2}~\5\62\32\2~\177"+
		"\7b\2\2\177\u0080\5\62\32\2\u0080\u0081\5\22\n\2\u0081\u0082\5\62\32\2"+
		"\u0082\u0083\7b\2\2\u0083\u0085\3\2\2\2\u0084}\3\2\2\2\u0084\u0085\3\2"+
		"\2\2\u0085\17\3\2\2\2\u0086\u0087\5\60\31\2\u0087\21\3\2\2\2\u0088\u0092"+
		"\5F$\2\u0089\u008b\5\64\33\2\u008a\u0089\3\2\2\2\u008b\u008e\3\2\2\2\u008c"+
		"\u008a\3\2\2\2\u008c\u008d\3\2\2\2\u008d\u008f\3\2\2\2\u008e\u008c\3\2"+
		"\2\2\u008f\u0091\5F$\2\u0090\u008c\3\2\2\2\u0091\u0094\3\2\2\2\u0092\u0090"+
		"\3\2\2\2\u0092\u0093\3\2\2\2\u0093\23\3\2\2\2\u0094\u0092\3\2\2\2\u0095"+
		"\u0098\5\30\r\2\u0096\u0098\5\26\f\2\u0097\u0095\3\2\2\2\u0097\u0096\3"+
		"\2\2\2\u0098\u00a2\3\2\2\2\u0099\u009c\5\62\32\2\u009a\u009b\7\22\2\2"+
		"\u009b\u009d\5\62\32\2\u009c\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d\u009e"+
		"\3\2\2\2\u009e\u009f\5\26\f\2\u009f\u00a1\3\2\2\2\u00a0\u0099\3\2\2\2"+
		"\u00a1\u00a4\3\2\2\2\u00a2\u00a0\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\25"+
		"\3\2\2\2\u00a4\u00a2\3\2\2\2\u00a5\u00a6\7a\2\2\u00a6\u00a7\5\62\32\2"+
		"\u00a7\u00a8\5\30\r\2\u00a8\u00a9\5\62\32\2\u00a9\u00aa\7c\2\2\u00aa\27"+
		"\3\2\2\2\u00ab\u00b3\5\32\16\2\u00ac\u00ad\5\62\32\2\u00ad\u00ae\7\22"+
		"\2\2\u00ae\u00af\5\62\32\2\u00af\u00b0\5\32\16\2\u00b0\u00b2\3\2\2\2\u00b1"+
		"\u00ac\3\2\2\2\u00b2\u00b5\3\2\2\2\u00b3\u00b1\3\2\2\2\u00b3\u00b4\3\2"+
		"\2\2\u00b4\31\3\2\2\2\u00b5\u00b3\3\2\2\2\u00b6\u00b7\5\34\17\2\u00b7"+
		"\u00b8\5\62\32\2\u00b8\u00b9\7#\2\2\u00b9\u00ba\5\62\32\2\u00ba\u00bb"+
		"\5\36\20\2\u00bb\33\3\2\2\2\u00bc\u00bd\5\16\b\2\u00bd\35\3\2\2\2\u00be"+
		"\u00c7\5 \21\2\u00bf\u00c0\5<\37\2\u00c0\u00c1\5\"\22\2\u00c1\u00c2\5"+
		"<\37\2\u00c2\u00c7\3\2\2\2\u00c3\u00c4\7\t\2\2\u00c4\u00c7\5$\23\2\u00c5"+
		"\u00c7\5*\26\2\u00c6\u00be\3\2\2\2\u00c6\u00bf\3\2\2\2\u00c6\u00c3\3\2"+
		"\2\2\u00c6\u00c5\3\2\2\2\u00c7\37\3\2\2\2\u00c8\u00d0\5\16\b\2\u00c9\u00ca"+
		"\7\16\2\2\u00ca\u00cb\5\62\32\2\u00cb\u00cc\5\4\3\2\u00cc\u00cd\5\62\32"+
		"\2\u00cd\u00ce\7\17\2\2\u00ce\u00d0\3\2\2\2\u00cf\u00c8\3\2\2\2\u00cf"+
		"\u00c9\3\2\2\2\u00d0!\3\2\2\2\u00d1\u00d4\5H%\2\u00d2\u00d4\5J&\2\u00d3"+
		"\u00d1\3\2\2\2\u00d3\u00d2\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5\u00d3\3\2"+
		"\2\2\u00d5\u00d6\3\2\2\2\u00d6#\3\2\2\2\u00d7\u00d9\t\2\2\2\u00d8\u00d7"+
		"\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9\u00dc\3\2\2\2\u00da\u00dd\5(\25\2\u00db"+
		"\u00dd\5&\24\2\u00dc\u00da\3\2\2\2\u00dc\u00db\3\2\2\2\u00dd%\3\2\2\2"+
		"\u00de\u00e2\5D#\2\u00df\u00e1\5@!\2\u00e0\u00df\3\2\2\2\u00e1\u00e4\3"+
		"\2\2\2\u00e2\u00e0\3\2\2\2\u00e2\u00e3\3\2\2\2\u00e3\u00e7\3\2\2\2\u00e4"+
		"\u00e2\3\2\2\2\u00e5\u00e7\5B\"\2\u00e6\u00de\3\2\2\2\u00e6\u00e5\3\2"+
		"\2\2\u00e7\'\3\2\2\2\u00e8\u00e9\5&\24\2\u00e9\u00eb\7\24\2\2\u00ea\u00ec"+
		"\5@!\2\u00eb\u00ea\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed\u00eb\3\2\2\2\u00ed"+
		"\u00ee\3\2\2\2\u00ee)\3\2\2\2\u00ef\u00f2\5,\27\2\u00f0\u00f2\5.\30\2"+
		"\u00f1\u00ef\3\2\2\2\u00f1\u00f0\3\2\2\2\u00f2+\3\2\2\2\u00f3\u00f6\t"+
		"\3\2\2\u00f4\u00f6\t\3\2\2\u00f5\u00f3\3\2\2\2\u00f5\u00f4\3\2\2\2\u00f6"+
		"\u00f9\3\2\2\2\u00f7\u00fa\t\4\2\2\u00f8\u00fa\t\4\2\2\u00f9\u00f7\3\2"+
		"\2\2\u00f9\u00f8\3\2\2\2\u00fa\u00fd\3\2\2\2\u00fb\u00fe\t\5\2\2\u00fc"+
		"\u00fe\t\5\2\2\u00fd\u00fb\3\2\2\2\u00fd\u00fc\3\2\2\2\u00fe\u0101\3\2"+
		"\2\2\u00ff\u0102\t\6\2\2\u0100\u0102\t\6\2\2\u0101\u00ff\3\2\2\2\u0101"+
		"\u0100\3\2\2\2\u0102-\3\2\2\2\u0103\u0106\t\7\2\2\u0104\u0106\t\7\2\2"+
		"\u0105\u0103\3\2\2\2\u0105\u0104\3\2\2\2\u0106\u0109\3\2\2\2\u0107\u010a"+
		"\t\b\2\2\u0108\u010a\t\b\2\2\u0109\u0107\3\2\2\2\u0109\u0108\3\2\2\2\u010a"+
		"\u010d\3\2\2\2\u010b\u010e\t\t\2\2\u010c\u010e\t\t\2\2\u010d\u010b\3\2"+
		"\2\2\u010d\u010c\3\2\2\2\u010e\u0111\3\2\2\2\u010f\u0112\t\n\2\2\u0110"+
		"\u0112\t\n\2\2\u0111\u010f\3\2\2\2\u0111\u0110\3\2\2\2\u0112\u0115\3\2"+
		"\2\2\u0113\u0116\t\6\2\2\u0114\u0116\t\6\2\2\u0115\u0113\3\2\2\2\u0115"+
		"\u0114\3\2\2\2\u0116/\3\2\2\2\u0117\u0118\5D#\2\u0118\u0119\5@!\2\u0119"+
		"\u011a\5@!\2\u011a\u011b\5@!\2\u011b\u011c\5@!\2\u011c\u0178\5@!\2\u011d"+
		"\u011e\5@!\2\u011e\u011f\5@!\2\u011f\u0120\5@!\2\u0120\u0121\5@!\2\u0121"+
		"\u0122\5@!\2\u0122\u0123\5@!\2\u0123\u0124\5@!\2\u0124\u0125\5@!\2\u0125"+
		"\u0126\5@!\2\u0126\u0127\5@!\2\u0127\u0128\5@!\2\u0128\u0129\5@!\2\u0129"+
		"\u0179\3\2\2\2\u012a\u012b\5@!\2\u012b\u012c\5@!\2\u012c\u012d\5@!\2\u012d"+
		"\u012e\5@!\2\u012e\u012f\5@!\2\u012f\u0130\5@!\2\u0130\u0131\5@!\2\u0131"+
		"\u0132\5@!\2\u0132\u0133\5@!\2\u0133\u0134\5@!\2\u0134\u0135\5@!\2\u0135"+
		"\u0179\3\2\2\2\u0136\u0137\5@!\2\u0137\u0138\5@!\2\u0138\u0139\5@!\2\u0139"+
		"\u013a\5@!\2\u013a\u013b\5@!\2\u013b\u013c\5@!\2\u013c\u013d\5@!\2\u013d"+
		"\u013e\5@!\2\u013e\u013f\5@!\2\u013f\u0140\5@!\2\u0140\u0179\3\2\2\2\u0141"+
		"\u0142\5@!\2\u0142\u0143\5@!\2\u0143\u0144\5@!\2\u0144\u0145\5@!\2\u0145"+
		"\u0146\5@!\2\u0146\u0147\5@!\2\u0147\u0148\5@!\2\u0148\u0149\5@!\2\u0149"+
		"\u014a\5@!\2\u014a\u0179\3\2\2\2\u014b\u014c\5@!\2\u014c\u014d\5@!\2\u014d"+
		"\u014e\5@!\2\u014e\u014f\5@!\2\u014f\u0150\5@!\2\u0150\u0151\5@!\2\u0151"+
		"\u0152\5@!\2\u0152\u0153\5@!\2\u0153\u0179\3\2\2\2\u0154\u0155\5@!\2\u0155"+
		"\u0156\5@!\2\u0156\u0157\5@!\2\u0157\u0158\5@!\2\u0158\u0159\5@!\2\u0159"+
		"\u015a\5@!\2\u015a\u015b\5@!\2\u015b\u0179\3\2\2\2\u015c\u015d\5@!\2\u015d"+
		"\u015e\5@!\2\u015e\u015f\5@!\2\u015f\u0160\5@!\2\u0160\u0161\5@!\2\u0161"+
		"\u0162\5@!\2\u0162\u0179\3\2\2\2\u0163\u0164\5@!\2\u0164\u0165\5@!\2\u0165"+
		"\u0166\5@!\2\u0166\u0167\5@!\2\u0167\u0168\5@!\2\u0168\u0179\3\2\2\2\u0169"+
		"\u016a\5@!\2\u016a\u016b\5@!\2\u016b\u016c\5@!\2\u016c\u016d\5@!\2\u016d"+
		"\u0179\3\2\2\2\u016e\u016f\5@!\2\u016f\u0170\5@!\2\u0170\u0171\5@!\2\u0171"+
		"\u0179\3\2\2\2\u0172\u0173\5@!\2\u0173\u0174\5@!\2\u0174\u0179\3\2\2\2"+
		"\u0175\u0177\5@!\2\u0176\u0175\3\2\2\2\u0176\u0177\3\2\2\2\u0177\u0179"+
		"\3\2\2\2\u0178\u011d\3\2\2\2\u0178\u012a\3\2\2\2\u0178\u0136\3\2\2\2\u0178"+
		"\u0141\3\2\2\2\u0178\u014b\3\2\2\2\u0178\u0154\3\2\2\2\u0178\u015c\3\2"+
		"\2\2\u0178\u0163\3\2\2\2\u0178\u0169\3\2\2\2\u0178\u016e\3\2\2\2\u0178"+
		"\u0172\3\2\2\2\u0178\u0176\3\2\2\2\u0179\61\3\2\2\2\u017a\u017f\5\64\33"+
		"\2\u017b\u017f\5\66\34\2\u017c\u017f\58\35\2\u017d\u017f\5:\36\2\u017e"+
		"\u017a\3\2\2\2\u017e\u017b\3\2\2\2\u017e\u017c\3\2\2\2\u017e\u017d\3\2"+
		"\2\2\u017f\u0182\3\2\2\2\u0180\u017e\3\2\2\2\u0180\u0181\3\2\2\2\u0181"+
		"\63\3\2\2\2\u0182\u0180\3\2\2\2\u0183\u0184\7\6\2\2\u0184\65\3\2\2\2\u0185"+
		"\u0186\7\3\2\2\u0186\67\3\2\2\2\u0187\u0188\7\5\2\2\u01889\3\2\2\2\u0189"+
		"\u018a\7\4\2\2\u018a;\3\2\2\2\u018b\u018c\7\b\2\2\u018c=\3\2\2\2\u018d"+
		"\u018e\7B\2\2\u018e?\3\2\2\2\u018f\u0190\t\13\2\2\u0190A\3\2\2\2\u0191"+
		"\u0192\7\26\2\2\u0192C\3\2\2\2\u0193\u0194\t\f\2\2\u0194E\3\2\2\2\u0195"+
		"\u019b\t\r\2\2\u0196\u019b\t\16\2\2\u0197\u019b\5L\'\2\u0198\u019b\5N"+
		"(\2\u0199\u019b\5P)\2\u019a\u0195\3\2\2\2\u019a\u0196\3\2\2\2\u019a\u0197"+
		"\3\2\2\2\u019a\u0198\3\2\2\2\u019a\u0199\3\2\2\2\u019bG\3\2\2\2\u019c"+
		"\u01a6\5\66\34\2\u019d\u01a6\58\35\2\u019e\u01a6\5:\36\2\u019f\u01a6\t"+
		"\17\2\2\u01a0\u01a6\t\20\2\2\u01a1\u01a6\t\21\2\2\u01a2\u01a6\5L\'\2\u01a3"+
		"\u01a6\5N(\2\u01a4\u01a6\5P)\2\u01a5\u019c\3\2\2\2\u01a5\u019d\3\2\2\2"+
		"\u01a5\u019e\3\2\2\2\u01a5\u019f\3\2\2\2\u01a5\u01a0\3\2\2\2\u01a5\u01a1"+
		"\3\2\2\2\u01a5\u01a2\3\2\2\2\u01a5\u01a3\3\2\2\2\u01a5\u01a4\3\2\2\2\u01a6"+
		"I\3\2\2\2\u01a7\u01a8\5> \2\u01a8\u01a9\5<\37\2\u01a9\u01ae\3\2\2\2\u01aa"+
		"\u01ab\5> \2\u01ab\u01ac\5> \2\u01ac\u01ae\3\2\2\2\u01ad\u01a7\3\2\2\2"+
		"\u01ad\u01aa\3\2\2\2\u01aeK\3\2\2\2\u01af\u01b0\t\22\2\2\u01b0\u01b1\5"+
		"R*\2\u01b1M\3\2\2\2\u01b2\u01b3\7\u00c3\2\2\u01b3\u01b4\t\23\2\2\u01b4"+
		"\u01c1\5R*\2\u01b5\u01b6\t\24\2\2\u01b6\u01b7\5R*\2\u01b7\u01b8\5R*\2"+
		"\u01b8\u01c1\3\2\2\2\u01b9\u01ba\7\u00d0\2\2\u01ba\u01bb\t\25\2\2\u01bb"+
		"\u01c1\5R*\2\u01bc\u01bd\t\26\2\2\u01bd\u01be\5R*\2\u01be\u01bf\5R*\2"+
		"\u01bf\u01c1\3\2\2\2\u01c0\u01b2\3\2\2\2\u01c0\u01b5\3\2\2\2\u01c0\u01b9"+
		"\3\2\2\2\u01c0\u01bc\3\2\2\2\u01c1O\3\2\2\2\u01c2\u01c3\7\u00d3\2\2\u01c3"+
		"\u01c4\t\27\2\2\u01c4\u01c5\5R*\2\u01c5\u01c6\5R*\2\u01c6\u01d2\3\2\2"+
		"\2\u01c7\u01c8\t\30\2\2\u01c8\u01c9\5R*\2\u01c9\u01ca\5R*\2\u01ca\u01cb"+
		"\5R*\2\u01cb\u01d2\3\2\2\2\u01cc\u01cd\7\u00d7\2\2\u01cd\u01ce\t\31\2"+
		"\2\u01ce\u01cf\5R*\2\u01cf\u01d0\5R*\2\u01d0\u01d2\3\2\2\2\u01d1\u01c2"+
		"\3\2\2\2\u01d1\u01c7\3\2\2\2\u01d1\u01cc\3\2\2\2\u01d2Q\3\2\2\2\u01d3"+
		"\u01d4\t\32\2\2\u01d4S\3\2\2\2)Xcgy\u0084\u008c\u0092\u0097\u009c\u00a2"+
		"\u00b3\u00c6\u00cf\u00d3\u00d5\u00d8\u00dc\u00e2\u00e6\u00ed\u00f1\u00f5"+
		"\u00f9\u00fd\u0101\u0105\u0109\u010d\u0111\u0115\u0176\u0178\u017e\u0180"+
		"\u019a\u01a5\u01ad\u01c0\u01d1";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
