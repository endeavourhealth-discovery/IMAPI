// Generated from java-escape by ANTLR 4.11.1
package org.endeavourhealth.imapi.parser.imq;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class IMQParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.11.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, SEARCH_TEXT=5, ARGUMENT=6, ID=7, QUERY=8, 
		FROM=9, GRAPH=10, WHERE=11, NOTEXIST=12, WITH=13, SELECT=14, EARLIEST=15, 
		LATEST=16, MAXIMUM=17, MINIMUM=18, COUNT=19, SOURCE_TYPE=20, PREFIX=21, 
		DESCRIPTION=22, NAME=23, ALIAS=24, ACTIVE_ONLY=25, IN=26, TYPE=27, SET=28, 
		INSTANCE=29, EQ=30, GT=31, LT=32, GTE=33, LTE=34, STARTS_WITH=35, AND=36, 
		OR=37, NOT=38, TO=39, OC=40, CC=41, OSB=42, CSB=43, OB=44, CB=45, COLON=46, 
		UCHAR=47, HEX=48, IRI_REF=49, STRING_LITERAL1=50, STRING_LITERAL2=51, 
		PN_CHARS_U=52, COMMA=53, WS=54, PN_PROPERTY=55, PN_VARIABLE=56, PN_PREFIXED=57, 
		PN_CHARS_BASE=58, PN_CHARS=59, DIGIT=60, NUMBER=61, NOTIN=62, VALUE_LABEL=63;
	public static final int
		RULE_queryRequest = 0, RULE_searchText = 1, RULE_arguments = 2, RULE_argumentList = 3, 
		RULE_label = 4, RULE_string = 5, RULE_argument = 6, RULE_parameter = 7, 
		RULE_valueDataList = 8, RULE_valueIriList = 9, RULE_value = 10, RULE_iriRef = 11, 
		RULE_query = 12, RULE_properName = 13, RULE_prefixDecl = 14, RULE_selectClause = 15, 
		RULE_selection = 16, RULE_selectionList = 17, RULE_select = 18, RULE_name = 19, 
		RULE_description = 20, RULE_activeOnly = 21, RULE_fromClause = 22, RULE_fromWhere = 23, 
		RULE_bracketFrom = 24, RULE_fromBoolean = 25, RULE_notFrom = 26, RULE_orFrom = 27, 
		RULE_andFrom = 28, RULE_from = 29, RULE_whereClause = 30, RULE_whereWith = 31, 
		RULE_where = 32, RULE_whereWhere = 33, RULE_nestedWhere = 34, RULE_notExist = 35, 
		RULE_whereValue = 36, RULE_valueLabel = 37, RULE_whereBoolean = 38, RULE_notWhere = 39, 
		RULE_orWhere = 40, RULE_andWhere = 41, RULE_bracketWhere = 42, RULE_with = 43, 
		RULE_whereValueTest = 44, RULE_in = 45, RULE_fromList = 46, RULE_notin = 47, 
		RULE_referenceList = 48, RULE_reference = 49, RULE_range = 50, RULE_fromRange = 51, 
		RULE_toRange = 52, RULE_whereMeasure = 53, RULE_relativeTo = 54, RULE_operator = 55, 
		RULE_units = 56, RULE_sortable = 57, RULE_latest = 58, RULE_earliest = 59, 
		RULE_maximum = 60, RULE_minimum = 61, RULE_count = 62, RULE_graph = 63, 
		RULE_sourceType = 64, RULE_subsumption = 65, RULE_ancestorOf = 66, RULE_descendantof = 67, 
		RULE_descendantorselfof = 68, RULE_variable = 69, RULE_alias = 70;
	private static String[] makeRuleNames() {
		return new String[] {
			"queryRequest", "searchText", "arguments", "argumentList", "label", "string", 
			"argument", "parameter", "valueDataList", "valueIriList", "value", "iriRef", 
			"query", "properName", "prefixDecl", "selectClause", "selection", "selectionList", 
			"select", "name", "description", "activeOnly", "fromClause", "fromWhere", 
			"bracketFrom", "fromBoolean", "notFrom", "orFrom", "andFrom", "from", 
			"whereClause", "whereWith", "where", "whereWhere", "nestedWhere", "notExist", 
			"whereValue", "valueLabel", "whereBoolean", "notWhere", "orWhere", "andWhere", 
			"bracketWhere", "with", "whereValueTest", "in", "fromList", "notin", 
			"referenceList", "reference", "range", "fromRange", "toRange", "whereMeasure", 
			"relativeTo", "operator", "units", "sortable", "latest", "earliest", 
			"maximum", "minimum", "count", "graph", "sourceType", "subsumption", 
			"ancestorOf", "descendantof", "descendantorselfof", "variable", "alias"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'name '", "'relativeTo'", "'>>'", "'<<'", "'searchText'", "'argument'", 
			"'id'", "'query'", "'from'", "'graph'", "'where'", "'notExist'", "'with'", 
			"'select'", "'earliest'", "'latest'", "'maximum'", "'minimum'", "'count'", 
			"'sourceType'", null, null, null, "'alias'", "'activeOnly'", "'in'", 
			"'@type'", "'@set'", "'@id'", "'='", "'>'", "'<'", "'>='", "'<='", "'startsWith'", 
			null, null, null, "'to'", "'{'", "'}'", "'['", "']'", "'('", "')'", "':'", 
			null, null, null, null, null, null, "','", null, null, null, null, null, 
			null, null, null, "'notIn'", "'valueLabel'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, "SEARCH_TEXT", "ARGUMENT", "ID", "QUERY", 
			"FROM", "GRAPH", "WHERE", "NOTEXIST", "WITH", "SELECT", "EARLIEST", "LATEST", 
			"MAXIMUM", "MINIMUM", "COUNT", "SOURCE_TYPE", "PREFIX", "DESCRIPTION", 
			"NAME", "ALIAS", "ACTIVE_ONLY", "IN", "TYPE", "SET", "INSTANCE", "EQ", 
			"GT", "LT", "GTE", "LTE", "STARTS_WITH", "AND", "OR", "NOT", "TO", "OC", 
			"CC", "OSB", "CSB", "OB", "CB", "COLON", "UCHAR", "HEX", "IRI_REF", "STRING_LITERAL1", 
			"STRING_LITERAL2", "PN_CHARS_U", "COMMA", "WS", "PN_PROPERTY", "PN_VARIABLE", 
			"PN_PREFIXED", "PN_CHARS_BASE", "PN_CHARS", "DIGIT", "NUMBER", "NOTIN", 
			"VALUE_LABEL"
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

	public IMQParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class QueryRequestContext extends ParserRuleContext {
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
		public TerminalNode EOF() { return getToken(IMQParser.EOF, 0); }
		public List<PrefixDeclContext> prefixDecl() {
			return getRuleContexts(PrefixDeclContext.class);
		}
		public PrefixDeclContext prefixDecl(int i) {
			return getRuleContext(PrefixDeclContext.class,i);
		}
		public SearchTextContext searchText() {
			return getRuleContext(SearchTextContext.class,0);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public QueryRequestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryRequest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterQueryRequest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitQueryRequest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitQueryRequest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryRequestContext queryRequest() throws RecognitionException {
		QueryRequestContext _localctx = new QueryRequestContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_queryRequest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(142);
			match(OC);
			setState(146);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PREFIX) {
				{
				{
				setState(143);
				prefixDecl();
				}
				}
				setState(148);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(150);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEARCH_TEXT) {
				{
				setState(149);
				searchText();
				}
			}

			setState(153);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ARGUMENT) {
				{
				setState(152);
				arguments();
				}
			}

			setState(155);
			query();
			setState(156);
			match(CC);
			setState(157);
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
	public static class SearchTextContext extends ParserRuleContext {
		public TerminalNode SEARCH_TEXT() { return getToken(IMQParser.SEARCH_TEXT, 0); }
		public TerminalNode STRING_LITERAL1() { return getToken(IMQParser.STRING_LITERAL1, 0); }
		public TerminalNode STRING_LITERAL2() { return getToken(IMQParser.STRING_LITERAL2, 0); }
		public SearchTextContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_searchText; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterSearchText(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitSearchText(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitSearchText(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SearchTextContext searchText() throws RecognitionException {
		SearchTextContext _localctx = new SearchTextContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_searchText);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(159);
			match(SEARCH_TEXT);
			setState(160);
			_la = _input.LA(1);
			if ( !(_la==STRING_LITERAL1 || _la==STRING_LITERAL2) ) {
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
	public static class ArgumentsContext extends ParserRuleContext {
		public TerminalNode ARGUMENT() { return getToken(IMQParser.ARGUMENT, 0); }
		public ArgumentContext argument() {
			return getRuleContext(ArgumentContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitArguments(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_arguments);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			match(ARGUMENT);
			setState(165);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PN_PROPERTY:
				{
				setState(163);
				argument();
				}
				break;
			case OB:
				{
				setState(164);
				argumentList();
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
	public static class ArgumentListContext extends ParserRuleContext {
		public TerminalNode OB() { return getToken(IMQParser.OB, 0); }
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public TerminalNode CB() { return getToken(IMQParser.CB, 0); }
		public List<TerminalNode> COMMA() { return getTokens(IMQParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(IMQParser.COMMA, i);
		}
		public ArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterArgumentList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitArgumentList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(167);
			match(OB);
			setState(168);
			argument();
			setState(173);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(169);
				match(COMMA);
				setState(170);
				argument();
				}
				}
				setState(175);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(176);
			match(CB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LabelContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL1() { return getToken(IMQParser.STRING_LITERAL1, 0); }
		public TerminalNode STRING_LITERAL2() { return getToken(IMQParser.STRING_LITERAL2, 0); }
		public LabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_label; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterLabel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitLabel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitLabel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LabelContext label() throws RecognitionException {
		LabelContext _localctx = new LabelContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_label);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			_la = _input.LA(1);
			if ( !(_la==STRING_LITERAL1 || _la==STRING_LITERAL2) ) {
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
	public static class StringContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL1() { return getToken(IMQParser.STRING_LITERAL1, 0); }
		public TerminalNode STRING_LITERAL2() { return getToken(IMQParser.STRING_LITERAL2, 0); }
		public StringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringContext string() throws RecognitionException {
		StringContext _localctx = new StringContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_string);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(180);
			_la = _input.LA(1);
			if ( !(_la==STRING_LITERAL1 || _la==STRING_LITERAL2) ) {
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
	public static class ArgumentContext extends ParserRuleContext {
		public ParameterContext parameter() {
			return getRuleContext(ParameterContext.class,0);
		}
		public TerminalNode COLON() { return getToken(IMQParser.COLON, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public ValueDataListContext valueDataList() {
			return getRuleContext(ValueDataListContext.class,0);
		}
		public ValueIriListContext valueIriList() {
			return getRuleContext(ValueIriListContext.class,0);
		}
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_argument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(182);
			parameter();
			setState(183);
			match(COLON);
			setState(188);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(184);
				value();
				}
				break;
			case 2:
				{
				setState(185);
				iriRef();
				}
				break;
			case 3:
				{
				setState(186);
				valueDataList();
				}
				break;
			case 4:
				{
				setState(187);
				valueIriList();
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
	public static class ParameterContext extends ParserRuleContext {
		public TerminalNode PN_PROPERTY() { return getToken(IMQParser.PN_PROPERTY, 0); }
		public ParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitParameter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitParameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterContext parameter() throws RecognitionException {
		ParameterContext _localctx = new ParameterContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_parameter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(190);
			match(PN_PROPERTY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValueDataListContext extends ParserRuleContext {
		public TerminalNode OB() { return getToken(IMQParser.OB, 0); }
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public TerminalNode CB() { return getToken(IMQParser.CB, 0); }
		public List<TerminalNode> COMMA() { return getTokens(IMQParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(IMQParser.COMMA, i);
		}
		public ValueDataListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueDataList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterValueDataList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitValueDataList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitValueDataList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueDataListContext valueDataList() throws RecognitionException {
		ValueDataListContext _localctx = new ValueDataListContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_valueDataList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(192);
			match(OB);
			setState(193);
			value();
			setState(198);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(194);
				match(COMMA);
				setState(195);
				value();
				}
				}
				setState(200);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(201);
			match(CB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValueIriListContext extends ParserRuleContext {
		public TerminalNode OB() { return getToken(IMQParser.OB, 0); }
		public List<IriRefContext> iriRef() {
			return getRuleContexts(IriRefContext.class);
		}
		public IriRefContext iriRef(int i) {
			return getRuleContext(IriRefContext.class,i);
		}
		public TerminalNode CB() { return getToken(IMQParser.CB, 0); }
		public List<TerminalNode> COMMA() { return getTokens(IMQParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(IMQParser.COMMA, i);
		}
		public ValueIriListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueIriList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterValueIriList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitValueIriList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitValueIriList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueIriListContext valueIriList() throws RecognitionException {
		ValueIriListContext _localctx = new ValueIriListContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_valueIriList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(203);
			match(OB);
			setState(204);
			iriRef();
			setState(209);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(205);
				match(COMMA);
				setState(206);
				iriRef();
				}
				}
				setState(211);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(212);
			match(CB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValueContext extends ParserRuleContext {
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(214);
			string();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode IRI_REF() { return getToken(IMQParser.IRI_REF, 0); }
		public TerminalNode PN_PREFIXED() { return getToken(IMQParser.PN_PREFIXED, 0); }
		public TerminalNode PN_PROPERTY() { return getToken(IMQParser.PN_PROPERTY, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public IriRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iriRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterIriRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitIriRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitIriRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IriRefContext iriRef() throws RecognitionException {
		IriRefContext _localctx = new IriRefContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_iriRef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(216);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 180706935048241152L) != 0) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(218);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAME) {
				{
				setState(217);
				name();
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
	public static class QueryContext extends ParserRuleContext {
		public TerminalNode QUERY() { return getToken(IMQParser.QUERY, 0); }
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
		public FromClauseContext fromClause() {
			return getRuleContext(FromClauseContext.class,0);
		}
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
		public ReferenceContext reference() {
			return getRuleContext(ReferenceContext.class,0);
		}
		public ProperNameContext properName() {
			return getRuleContext(ProperNameContext.class,0);
		}
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public ActiveOnlyContext activeOnly() {
			return getRuleContext(ActiveOnlyContext.class,0);
		}
		public SelectClauseContext selectClause() {
			return getRuleContext(SelectClauseContext.class,0);
		}
		public List<QueryContext> query() {
			return getRuleContexts(QueryContext.class);
		}
		public QueryContext query(int i) {
			return getRuleContext(QueryContext.class,i);
		}
		public QueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_query; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryContext query() throws RecognitionException {
		QueryContext _localctx = new QueryContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_query);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(220);
			match(QUERY);
			setState(221);
			match(OC);
			setState(223);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OC) {
				{
				setState(222);
				reference();
				}
			}

			setState(226);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(225);
				properName();
				}
			}

			setState(229);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING_LITERAL1 || _la==STRING_LITERAL2) {
				{
				setState(228);
				description();
				}
			}

			setState(232);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ACTIVE_ONLY) {
				{
				setState(231);
				activeOnly();
				}
			}

			setState(234);
			fromClause();
			setState(236);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SELECT) {
				{
				setState(235);
				selectClause();
				}
			}

			setState(241);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==QUERY) {
				{
				{
				setState(238);
				query();
				}
				}
				setState(243);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(244);
			match(CC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProperNameContext extends ParserRuleContext {
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public ProperNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_properName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterProperName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitProperName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitProperName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProperNameContext properName() throws RecognitionException {
		ProperNameContext _localctx = new ProperNameContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_properName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(246);
			match(T__0);
			setState(247);
			string();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrefixDeclContext extends ParserRuleContext {
		public TerminalNode PREFIX() { return getToken(IMQParser.PREFIX, 0); }
		public TerminalNode PN_PROPERTY() { return getToken(IMQParser.PN_PROPERTY, 0); }
		public TerminalNode COLON() { return getToken(IMQParser.COLON, 0); }
		public TerminalNode IRI_REF() { return getToken(IMQParser.IRI_REF, 0); }
		public PrefixDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefixDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterPrefixDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitPrefixDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitPrefixDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixDeclContext prefixDecl() throws RecognitionException {
		PrefixDeclContext _localctx = new PrefixDeclContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_prefixDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249);
			match(PREFIX);
			setState(250);
			match(PN_PROPERTY);
			setState(251);
			match(COLON);
			setState(252);
			match(IRI_REF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectClauseContext extends ParserRuleContext {
		public TerminalNode SELECT() { return getToken(IMQParser.SELECT, 0); }
		public SelectionContext selection() {
			return getRuleContext(SelectionContext.class,0);
		}
		public SelectClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterSelectClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitSelectClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitSelectClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectClauseContext selectClause() throws RecognitionException {
		SelectClauseContext _localctx = new SelectClauseContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_selectClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(254);
			match(SELECT);
			setState(255);
			selection();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectionContext extends ParserRuleContext {
		public SelectionListContext selectionList() {
			return getRuleContext(SelectionListContext.class,0);
		}
		public SelectContext select() {
			return getRuleContext(SelectContext.class,0);
		}
		public SelectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterSelection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitSelection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitSelection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectionContext selection() throws RecognitionException {
		SelectionContext _localctx = new SelectionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_selection);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(259);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OB:
				{
				setState(257);
				selectionList();
				}
				break;
			case OC:
				{
				setState(258);
				select();
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
	public static class SelectionListContext extends ParserRuleContext {
		public TerminalNode OB() { return getToken(IMQParser.OB, 0); }
		public List<SelectContext> select() {
			return getRuleContexts(SelectContext.class);
		}
		public SelectContext select(int i) {
			return getRuleContext(SelectContext.class,i);
		}
		public TerminalNode CB() { return getToken(IMQParser.CB, 0); }
		public List<TerminalNode> COMMA() { return getTokens(IMQParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(IMQParser.COMMA, i);
		}
		public SelectionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterSelectionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitSelectionList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitSelectionList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectionListContext selectionList() throws RecognitionException {
		SelectionListContext _localctx = new SelectionListContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_selectionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(261);
			match(OB);
			setState(262);
			select();
			setState(267);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(263);
				match(COMMA);
				setState(264);
				select();
				}
				}
				setState(269);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(270);
			match(CB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectContext extends ParserRuleContext {
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public SelectClauseContext selectClause() {
			return getRuleContext(SelectClauseContext.class,0);
		}
		public SelectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_select; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterSelect(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitSelect(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitSelect(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectContext select() throws RecognitionException {
		SelectContext _localctx = new SelectContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_select);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(272);
			match(OC);
			setState(273);
			iriRef();
			setState(275);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(274);
				whereClause();
				}
			}

			setState(278);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SELECT) {
				{
				setState(277);
				selectClause();
				}
			}

			setState(280);
			match(CC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NameContext extends ParserRuleContext {
		public TerminalNode NAME() { return getToken(IMQParser.NAME, 0); }
		public NameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NameContext name() throws RecognitionException {
		NameContext _localctx = new NameContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(282);
			match(NAME);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DescriptionContext extends ParserRuleContext {
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public DescriptionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_description; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterDescription(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitDescription(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitDescription(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DescriptionContext description() throws RecognitionException {
		DescriptionContext _localctx = new DescriptionContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_description);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(284);
			string();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ActiveOnlyContext extends ParserRuleContext {
		public TerminalNode ACTIVE_ONLY() { return getToken(IMQParser.ACTIVE_ONLY, 0); }
		public ActiveOnlyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_activeOnly; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterActiveOnly(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitActiveOnly(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitActiveOnly(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActiveOnlyContext activeOnly() throws RecognitionException {
		ActiveOnlyContext _localctx = new ActiveOnlyContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_activeOnly);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(286);
			match(ACTIVE_ONLY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FromClauseContext extends ParserRuleContext {
		public TerminalNode FROM() { return getToken(IMQParser.FROM, 0); }
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
		public FromContext from() {
			return getRuleContext(FromContext.class,0);
		}
		public FromWhereContext fromWhere() {
			return getRuleContext(FromWhereContext.class,0);
		}
		public FromBooleanContext fromBoolean() {
			return getRuleContext(FromBooleanContext.class,0);
		}
		public BracketFromContext bracketFrom() {
			return getRuleContext(BracketFromContext.class,0);
		}
		public FromClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fromClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterFromClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitFromClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitFromClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FromClauseContext fromClause() throws RecognitionException {
		FromClauseContext _localctx = new FromClauseContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_fromClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(288);
			match(FROM);
			setState(289);
			match(OC);
			setState(294);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				{
				setState(290);
				from();
				}
				break;
			case 2:
				{
				setState(291);
				fromWhere();
				}
				break;
			case 3:
				{
				setState(292);
				fromBoolean();
				}
				break;
			case 4:
				{
				setState(293);
				bracketFrom();
				}
				break;
			}
			setState(296);
			match(CC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FromWhereContext extends ParserRuleContext {
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public FromContext from() {
			return getRuleContext(FromContext.class,0);
		}
		public BracketFromContext bracketFrom() {
			return getRuleContext(BracketFromContext.class,0);
		}
		public FromWhereContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fromWhere; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterFromWhere(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitFromWhere(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitFromWhere(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FromWhereContext fromWhere() throws RecognitionException {
		FromWhereContext _localctx = new FromWhereContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_fromWhere);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(300);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GRAPH:
			case OC:
			case STRING_LITERAL1:
			case STRING_LITERAL2:
				{
				setState(298);
				from();
				}
				break;
			case OB:
				{
				setState(299);
				bracketFrom();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(302);
			whereClause();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BracketFromContext extends ParserRuleContext {
		public TerminalNode OB() { return getToken(IMQParser.OB, 0); }
		public TerminalNode CB() { return getToken(IMQParser.CB, 0); }
		public FromWhereContext fromWhere() {
			return getRuleContext(FromWhereContext.class,0);
		}
		public FromBooleanContext fromBoolean() {
			return getRuleContext(FromBooleanContext.class,0);
		}
		public FromContext from() {
			return getRuleContext(FromContext.class,0);
		}
		public BracketFromContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bracketFrom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterBracketFrom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitBracketFrom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitBracketFrom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BracketFromContext bracketFrom() throws RecognitionException {
		BracketFromContext _localctx = new BracketFromContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_bracketFrom);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(304);
			match(OB);
			setState(308);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(305);
				fromWhere();
				}
				break;
			case 2:
				{
				setState(306);
				fromBoolean();
				}
				break;
			case 3:
				{
				setState(307);
				from();
				}
				break;
			}
			setState(310);
			match(CB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FromBooleanContext extends ParserRuleContext {
		public AndFromContext andFrom() {
			return getRuleContext(AndFromContext.class,0);
		}
		public OrFromContext orFrom() {
			return getRuleContext(OrFromContext.class,0);
		}
		public NotFromContext notFrom() {
			return getRuleContext(NotFromContext.class,0);
		}
		public FromBooleanContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fromBoolean; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterFromBoolean(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitFromBoolean(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitFromBoolean(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FromBooleanContext fromBoolean() throws RecognitionException {
		FromBooleanContext _localctx = new FromBooleanContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_fromBoolean);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(315);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				{
				setState(312);
				andFrom();
				}
				break;
			case 2:
				{
				setState(313);
				orFrom();
				}
				break;
			case 3:
				{
				setState(314);
				notFrom();
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
	public static class NotFromContext extends ParserRuleContext {
		public List<FromContext> from() {
			return getRuleContexts(FromContext.class);
		}
		public FromContext from(int i) {
			return getRuleContext(FromContext.class,i);
		}
		public List<BracketFromContext> bracketFrom() {
			return getRuleContexts(BracketFromContext.class);
		}
		public BracketFromContext bracketFrom(int i) {
			return getRuleContext(BracketFromContext.class,i);
		}
		public List<FromWhereContext> fromWhere() {
			return getRuleContexts(FromWhereContext.class);
		}
		public FromWhereContext fromWhere(int i) {
			return getRuleContext(FromWhereContext.class,i);
		}
		public List<TerminalNode> NOT() { return getTokens(IMQParser.NOT); }
		public TerminalNode NOT(int i) {
			return getToken(IMQParser.NOT, i);
		}
		public NotFromContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notFrom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterNotFrom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitNotFrom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitNotFrom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NotFromContext notFrom() throws RecognitionException {
		NotFromContext _localctx = new NotFromContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_notFrom);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(320);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				{
				setState(317);
				from();
				}
				break;
			case 2:
				{
				setState(318);
				bracketFrom();
				}
				break;
			case 3:
				{
				setState(319);
				fromWhere();
				}
				break;
			}
			setState(328); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(322);
					match(NOT);
					setState(326);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
					case 1:
						{
						setState(323);
						from();
						}
						break;
					case 2:
						{
						setState(324);
						bracketFrom();
						}
						break;
					case 3:
						{
						setState(325);
						fromWhere();
						}
						break;
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(330); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
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
	public static class OrFromContext extends ParserRuleContext {
		public List<FromContext> from() {
			return getRuleContexts(FromContext.class);
		}
		public FromContext from(int i) {
			return getRuleContext(FromContext.class,i);
		}
		public List<BracketFromContext> bracketFrom() {
			return getRuleContexts(BracketFromContext.class);
		}
		public BracketFromContext bracketFrom(int i) {
			return getRuleContext(BracketFromContext.class,i);
		}
		public List<FromWhereContext> fromWhere() {
			return getRuleContexts(FromWhereContext.class);
		}
		public FromWhereContext fromWhere(int i) {
			return getRuleContext(FromWhereContext.class,i);
		}
		public List<TerminalNode> OR() { return getTokens(IMQParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(IMQParser.OR, i);
		}
		public OrFromContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orFrom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterOrFrom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitOrFrom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitOrFrom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrFromContext orFrom() throws RecognitionException {
		OrFromContext _localctx = new OrFromContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_orFrom);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(335);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				{
				setState(332);
				from();
				}
				break;
			case 2:
				{
				setState(333);
				bracketFrom();
				}
				break;
			case 3:
				{
				setState(334);
				fromWhere();
				}
				break;
			}
			setState(343); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(337);
					match(OR);
					setState(341);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
					case 1:
						{
						setState(338);
						from();
						}
						break;
					case 2:
						{
						setState(339);
						bracketFrom();
						}
						break;
					case 3:
						{
						setState(340);
						fromWhere();
						}
						break;
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(345); 
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
	public static class AndFromContext extends ParserRuleContext {
		public List<FromContext> from() {
			return getRuleContexts(FromContext.class);
		}
		public FromContext from(int i) {
			return getRuleContext(FromContext.class,i);
		}
		public List<BracketFromContext> bracketFrom() {
			return getRuleContexts(BracketFromContext.class);
		}
		public BracketFromContext bracketFrom(int i) {
			return getRuleContext(BracketFromContext.class,i);
		}
		public List<FromWhereContext> fromWhere() {
			return getRuleContexts(FromWhereContext.class);
		}
		public FromWhereContext fromWhere(int i) {
			return getRuleContext(FromWhereContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(IMQParser.AND); }
		public TerminalNode AND(int i) {
			return getToken(IMQParser.AND, i);
		}
		public AndFromContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andFrom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterAndFrom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitAndFrom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitAndFrom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndFromContext andFrom() throws RecognitionException {
		AndFromContext _localctx = new AndFromContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_andFrom);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(350);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				{
				setState(347);
				from();
				}
				break;
			case 2:
				{
				setState(348);
				bracketFrom();
				}
				break;
			case 3:
				{
				setState(349);
				fromWhere();
				}
				break;
			}
			setState(358); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(352);
					match(AND);
					setState(356);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
					case 1:
						{
						setState(353);
						from();
						}
						break;
					case 2:
						{
						setState(354);
						bracketFrom();
						}
						break;
					case 3:
						{
						setState(355);
						fromWhere();
						}
						break;
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(360); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
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
	public static class FromContext extends ParserRuleContext {
		public ReferenceContext reference() {
			return getRuleContext(ReferenceContext.class,0);
		}
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public GraphContext graph() {
			return getRuleContext(GraphContext.class,0);
		}
		public FromContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_from; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterFrom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitFrom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitFrom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FromContext from() throws RecognitionException {
		FromContext _localctx = new FromContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_from);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(363);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING_LITERAL1 || _la==STRING_LITERAL2) {
				{
				setState(362);
				description();
				}
			}

			setState(366);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==GRAPH) {
				{
				setState(365);
				graph();
				}
			}

			setState(368);
			reference();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode WHERE() { return getToken(IMQParser.WHERE, 0); }
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
		public WhereContext where() {
			return getRuleContext(WhereContext.class,0);
		}
		public WhereWithContext whereWith() {
			return getRuleContext(WhereWithContext.class,0);
		}
		public WhereValueContext whereValue() {
			return getRuleContext(WhereValueContext.class,0);
		}
		public WhereBooleanContext whereBoolean() {
			return getRuleContext(WhereBooleanContext.class,0);
		}
		public WhereWhereContext whereWhere() {
			return getRuleContext(WhereWhereContext.class,0);
		}
		public WhereClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterWhereClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitWhereClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitWhereClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereClauseContext whereClause() throws RecognitionException {
		WhereClauseContext _localctx = new WhereClauseContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(370);
			match(WHERE);
			setState(371);
			match(OC);
			setState(377);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				{
				setState(372);
				where();
				}
				break;
			case 2:
				{
				setState(373);
				whereWith();
				}
				break;
			case 3:
				{
				setState(374);
				whereValue();
				}
				break;
			case 4:
				{
				setState(375);
				whereBoolean();
				}
				break;
			case 5:
				{
				setState(376);
				whereWhere();
				}
				break;
			}
			setState(379);
			match(CC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhereWithContext extends ParserRuleContext {
		public WithContext with() {
			return getRuleContext(WithContext.class,0);
		}
		public WhereContext where() {
			return getRuleContext(WhereContext.class,0);
		}
		public WhereValueContext whereValue() {
			return getRuleContext(WhereValueContext.class,0);
		}
		public WhereBooleanContext whereBoolean() {
			return getRuleContext(WhereBooleanContext.class,0);
		}
		public WhereWithContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereWith; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterWhereWith(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitWhereWith(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitWhereWith(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereWithContext whereWith() throws RecognitionException {
		WhereWithContext _localctx = new WhereWithContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_whereWith);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(381);
			with();
			setState(385);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(382);
				where();
				}
				break;
			case 2:
				{
				setState(383);
				whereValue();
				}
				break;
			case 3:
				{
				setState(384);
				whereBoolean();
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
		public ReferenceContext reference() {
			return getRuleContext(ReferenceContext.class,0);
		}
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public NotExistContext notExist() {
			return getRuleContext(NotExistContext.class,0);
		}
		public WhereContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_where; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterWhere(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitWhere(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitWhere(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereContext where() throws RecognitionException {
		WhereContext _localctx = new WhereContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_where);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(388);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING_LITERAL1 || _la==STRING_LITERAL2) {
				{
				setState(387);
				description();
				}
			}

			setState(391);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NOTEXIST) {
				{
				setState(390);
				notExist();
				}
			}

			setState(393);
			reference();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhereWhereContext extends ParserRuleContext {
		public WhereContext where() {
			return getRuleContext(WhereContext.class,0);
		}
		public NestedWhereContext nestedWhere() {
			return getRuleContext(NestedWhereContext.class,0);
		}
		public WhereWhereContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereWhere; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterWhereWhere(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitWhereWhere(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitWhereWhere(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereWhereContext whereWhere() throws RecognitionException {
		WhereWhereContext _localctx = new WhereWhereContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_whereWhere);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(395);
			where();
			setState(396);
			nestedWhere();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NestedWhereContext extends ParserRuleContext {
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
		public WhereContext where() {
			return getRuleContext(WhereContext.class,0);
		}
		public WhereWithContext whereWith() {
			return getRuleContext(WhereWithContext.class,0);
		}
		public WhereValueContext whereValue() {
			return getRuleContext(WhereValueContext.class,0);
		}
		public WhereBooleanContext whereBoolean() {
			return getRuleContext(WhereBooleanContext.class,0);
		}
		public WhereWhereContext whereWhere() {
			return getRuleContext(WhereWhereContext.class,0);
		}
		public NestedWhereContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nestedWhere; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterNestedWhere(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitNestedWhere(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitNestedWhere(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NestedWhereContext nestedWhere() throws RecognitionException {
		NestedWhereContext _localctx = new NestedWhereContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_nestedWhere);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(398);
			match(OC);
			setState(404);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				{
				setState(399);
				where();
				}
				break;
			case 2:
				{
				setState(400);
				whereWith();
				}
				break;
			case 3:
				{
				setState(401);
				whereValue();
				}
				break;
			case 4:
				{
				setState(402);
				whereBoolean();
				}
				break;
			case 5:
				{
				setState(403);
				whereWhere();
				}
				break;
			}
			setState(406);
			match(CC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NotExistContext extends ParserRuleContext {
		public TerminalNode NOTEXIST() { return getToken(IMQParser.NOTEXIST, 0); }
		public NotExistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notExist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterNotExist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitNotExist(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitNotExist(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NotExistContext notExist() throws RecognitionException {
		NotExistContext _localctx = new NotExistContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_notExist);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(408);
			match(NOTEXIST);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public WhereContext where() {
			return getRuleContext(WhereContext.class,0);
		}
		public WhereValueTestContext whereValueTest() {
			return getRuleContext(WhereValueTestContext.class,0);
		}
		public ValueLabelContext valueLabel() {
			return getRuleContext(ValueLabelContext.class,0);
		}
		public WhereValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterWhereValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitWhereValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitWhereValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereValueContext whereValue() throws RecognitionException {
		WhereValueContext _localctx = new WhereValueContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_whereValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(410);
			where();
			setState(412);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VALUE_LABEL) {
				{
				setState(411);
				valueLabel();
				}
			}

			setState(414);
			whereValueTest();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValueLabelContext extends ParserRuleContext {
		public TerminalNode VALUE_LABEL() { return getToken(IMQParser.VALUE_LABEL, 0); }
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public ValueLabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueLabel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterValueLabel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitValueLabel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitValueLabel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueLabelContext valueLabel() throws RecognitionException {
		ValueLabelContext _localctx = new ValueLabelContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_valueLabel);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(416);
			match(VALUE_LABEL);
			setState(417);
			string();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhereBooleanContext extends ParserRuleContext {
		public AndWhereContext andWhere() {
			return getRuleContext(AndWhereContext.class,0);
		}
		public OrWhereContext orWhere() {
			return getRuleContext(OrWhereContext.class,0);
		}
		public NotWhereContext notWhere() {
			return getRuleContext(NotWhereContext.class,0);
		}
		public WhereContext where() {
			return getRuleContext(WhereContext.class,0);
		}
		public WhereBooleanContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereBoolean; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterWhereBoolean(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitWhereBoolean(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitWhereBoolean(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereBooleanContext whereBoolean() throws RecognitionException {
		WhereBooleanContext _localctx = new WhereBooleanContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_whereBoolean);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(420);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				{
				setState(419);
				where();
				}
				break;
			}
			setState(425);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
			case 1:
				{
				setState(422);
				andWhere();
				}
				break;
			case 2:
				{
				setState(423);
				orWhere();
				}
				break;
			case 3:
				{
				setState(424);
				notWhere();
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
	public static class NotWhereContext extends ParserRuleContext {
		public List<TerminalNode> NOT() { return getTokens(IMQParser.NOT); }
		public TerminalNode NOT(int i) {
			return getToken(IMQParser.NOT, i);
		}
		public List<WhereContext> where() {
			return getRuleContexts(WhereContext.class);
		}
		public WhereContext where(int i) {
			return getRuleContext(WhereContext.class,i);
		}
		public List<BracketWhereContext> bracketWhere() {
			return getRuleContexts(BracketWhereContext.class);
		}
		public BracketWhereContext bracketWhere(int i) {
			return getRuleContext(BracketWhereContext.class,i);
		}
		public List<WhereValueContext> whereValue() {
			return getRuleContexts(WhereValueContext.class);
		}
		public WhereValueContext whereValue(int i) {
			return getRuleContext(WhereValueContext.class,i);
		}
		public NotWhereContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notWhere; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterNotWhere(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitNotWhere(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitNotWhere(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NotWhereContext notWhere() throws RecognitionException {
		NotWhereContext _localctx = new NotWhereContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_notWhere);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(433); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(427);
				match(NOT);
				setState(431);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
				case 1:
					{
					setState(428);
					where();
					}
					break;
				case 2:
					{
					setState(429);
					bracketWhere();
					}
					break;
				case 3:
					{
					setState(430);
					whereValue();
					}
					break;
				}
				}
				}
				setState(435); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==NOT );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OrWhereContext extends ParserRuleContext {
		public List<WhereContext> where() {
			return getRuleContexts(WhereContext.class);
		}
		public WhereContext where(int i) {
			return getRuleContext(WhereContext.class,i);
		}
		public List<BracketWhereContext> bracketWhere() {
			return getRuleContexts(BracketWhereContext.class);
		}
		public BracketWhereContext bracketWhere(int i) {
			return getRuleContext(BracketWhereContext.class,i);
		}
		public List<WhereValueContext> whereValue() {
			return getRuleContexts(WhereValueContext.class);
		}
		public WhereValueContext whereValue(int i) {
			return getRuleContext(WhereValueContext.class,i);
		}
		public List<TerminalNode> OR() { return getTokens(IMQParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(IMQParser.OR, i);
		}
		public OrWhereContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orWhere; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterOrWhere(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitOrWhere(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitOrWhere(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrWhereContext orWhere() throws RecognitionException {
		OrWhereContext _localctx = new OrWhereContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_orWhere);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(440);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				{
				setState(437);
				where();
				}
				break;
			case 2:
				{
				setState(438);
				bracketWhere();
				}
				break;
			case 3:
				{
				setState(439);
				whereValue();
				}
				break;
			}
			setState(448); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(442);
				match(OR);
				setState(446);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
				case 1:
					{
					setState(443);
					where();
					}
					break;
				case 2:
					{
					setState(444);
					bracketWhere();
					}
					break;
				case 3:
					{
					setState(445);
					whereValue();
					}
					break;
				}
				}
				}
				setState(450); 
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
	public static class AndWhereContext extends ParserRuleContext {
		public List<WhereContext> where() {
			return getRuleContexts(WhereContext.class);
		}
		public WhereContext where(int i) {
			return getRuleContext(WhereContext.class,i);
		}
		public List<BracketWhereContext> bracketWhere() {
			return getRuleContexts(BracketWhereContext.class);
		}
		public BracketWhereContext bracketWhere(int i) {
			return getRuleContext(BracketWhereContext.class,i);
		}
		public List<WhereValueContext> whereValue() {
			return getRuleContexts(WhereValueContext.class);
		}
		public WhereValueContext whereValue(int i) {
			return getRuleContext(WhereValueContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(IMQParser.AND); }
		public TerminalNode AND(int i) {
			return getToken(IMQParser.AND, i);
		}
		public AndWhereContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andWhere; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterAndWhere(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitAndWhere(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitAndWhere(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndWhereContext andWhere() throws RecognitionException {
		AndWhereContext _localctx = new AndWhereContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_andWhere);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(455);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				{
				setState(452);
				where();
				}
				break;
			case 2:
				{
				setState(453);
				bracketWhere();
				}
				break;
			case 3:
				{
				setState(454);
				whereValue();
				}
				break;
			}
			setState(463); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(457);
				match(AND);
				setState(461);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
				case 1:
					{
					setState(458);
					where();
					}
					break;
				case 2:
					{
					setState(459);
					bracketWhere();
					}
					break;
				case 3:
					{
					setState(460);
					whereValue();
					}
					break;
				}
				}
				}
				setState(465); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==AND );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BracketWhereContext extends ParserRuleContext {
		public TerminalNode OB() { return getToken(IMQParser.OB, 0); }
		public TerminalNode CB() { return getToken(IMQParser.CB, 0); }
		public WhereValueContext whereValue() {
			return getRuleContext(WhereValueContext.class,0);
		}
		public WhereBooleanContext whereBoolean() {
			return getRuleContext(WhereBooleanContext.class,0);
		}
		public WhereContext where() {
			return getRuleContext(WhereContext.class,0);
		}
		public BracketWhereContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bracketWhere; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterBracketWhere(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitBracketWhere(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitBracketWhere(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BracketWhereContext bracketWhere() throws RecognitionException {
		BracketWhereContext _localctx = new BracketWhereContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_bracketWhere);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(467);
			match(OB);
			setState(471);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				{
				setState(468);
				whereValue();
				}
				break;
			case 2:
				{
				setState(469);
				whereBoolean();
				}
				break;
			case 3:
				{
				setState(470);
				where();
				}
				break;
			}
			setState(473);
			match(CB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WithContext extends ParserRuleContext {
		public TerminalNode WITH() { return getToken(IMQParser.WITH, 0); }
		public TerminalNode OB() { return getToken(IMQParser.OB, 0); }
		public SortableContext sortable() {
			return getRuleContext(SortableContext.class,0);
		}
		public TerminalNode CB() { return getToken(IMQParser.CB, 0); }
		public WhereContext where() {
			return getRuleContext(WhereContext.class,0);
		}
		public WhereBooleanContext whereBoolean() {
			return getRuleContext(WhereBooleanContext.class,0);
		}
		public WhereValueContext whereValue() {
			return getRuleContext(WhereValueContext.class,0);
		}
		public WhereWhereContext whereWhere() {
			return getRuleContext(WhereWhereContext.class,0);
		}
		public WithContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_with; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterWith(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitWith(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitWith(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WithContext with() throws RecognitionException {
		WithContext _localctx = new WithContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_with);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(475);
			match(WITH);
			setState(476);
			match(OB);
			setState(481);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				{
				setState(477);
				where();
				}
				break;
			case 2:
				{
				setState(478);
				whereBoolean();
				}
				break;
			case 3:
				{
				setState(479);
				whereValue();
				}
				break;
			case 4:
				{
				setState(480);
				whereWhere();
				}
				break;
			}
			setState(483);
			sortable();
			setState(484);
			match(CB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhereValueTestContext extends ParserRuleContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public RangeContext range() {
			return getRuleContext(RangeContext.class,0);
		}
		public WhereMeasureContext whereMeasure() {
			return getRuleContext(WhereMeasureContext.class,0);
		}
		public WhereValueTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereValueTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterWhereValueTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitWhereValueTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitWhereValueTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereValueTestContext whereValueTest() throws RecognitionException {
		WhereValueTestContext _localctx = new WhereValueTestContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_whereValueTest);
		try {
			setState(492);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IN:
			case NOTIN:
				enterOuterAlt(_localctx, 1);
				{
				setState(488);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IN:
					{
					setState(486);
					in();
					}
					break;
				case NOTIN:
					{
					setState(487);
					notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case FROM:
			case TO:
				enterOuterAlt(_localctx, 2);
				{
				setState(490);
				range();
				}
				break;
			case EQ:
			case GT:
			case LT:
			case GTE:
			case LTE:
			case STARTS_WITH:
				enterOuterAlt(_localctx, 3);
				{
				setState(491);
				whereMeasure();
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
	public static class InContext extends ParserRuleContext {
		public TerminalNode IN() { return getToken(IMQParser.IN, 0); }
		public FromListContext fromList() {
			return getRuleContext(FromListContext.class,0);
		}
		public FromContext from() {
			return getRuleContext(FromContext.class,0);
		}
		public FromWhereContext fromWhere() {
			return getRuleContext(FromWhereContext.class,0);
		}
		public FromBooleanContext fromBoolean() {
			return getRuleContext(FromBooleanContext.class,0);
		}
		public BracketFromContext bracketFrom() {
			return getRuleContext(BracketFromContext.class,0);
		}
		public InContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_in; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterIn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitIn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitIn(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InContext in() throws RecognitionException {
		InContext _localctx = new InContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_in);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(494);
			match(IN);
			setState(502);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OSB:
				{
				setState(495);
				fromList();
				}
				break;
			case GRAPH:
			case OC:
			case OB:
			case STRING_LITERAL1:
			case STRING_LITERAL2:
				{
				setState(500);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
				case 1:
					{
					setState(496);
					from();
					}
					break;
				case 2:
					{
					setState(497);
					fromWhere();
					}
					break;
				case 3:
					{
					setState(498);
					fromBoolean();
					}
					break;
				case 4:
					{
					setState(499);
					bracketFrom();
					}
					break;
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
	public static class FromListContext extends ParserRuleContext {
		public TerminalNode OSB() { return getToken(IMQParser.OSB, 0); }
		public TerminalNode CSB() { return getToken(IMQParser.CSB, 0); }
		public List<FromContext> from() {
			return getRuleContexts(FromContext.class);
		}
		public FromContext from(int i) {
			return getRuleContext(FromContext.class,i);
		}
		public List<FromWhereContext> fromWhere() {
			return getRuleContexts(FromWhereContext.class);
		}
		public FromWhereContext fromWhere(int i) {
			return getRuleContext(FromWhereContext.class,i);
		}
		public List<FromBooleanContext> fromBoolean() {
			return getRuleContexts(FromBooleanContext.class);
		}
		public FromBooleanContext fromBoolean(int i) {
			return getRuleContext(FromBooleanContext.class,i);
		}
		public List<BracketFromContext> bracketFrom() {
			return getRuleContexts(BracketFromContext.class);
		}
		public BracketFromContext bracketFrom(int i) {
			return getRuleContext(BracketFromContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(IMQParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(IMQParser.COMMA, i);
		}
		public FromListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fromList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterFromList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitFromList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitFromList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FromListContext fromList() throws RecognitionException {
		FromListContext _localctx = new FromListContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_fromList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(504);
			match(OSB);
			setState(509);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				{
				setState(505);
				from();
				}
				break;
			case 2:
				{
				setState(506);
				fromWhere();
				}
				break;
			case 3:
				{
				setState(507);
				fromBoolean();
				}
				break;
			case 4:
				{
				setState(508);
				bracketFrom();
				}
				break;
			}
			setState(520);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(511);
				match(COMMA);
				setState(516);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
				case 1:
					{
					setState(512);
					from();
					}
					break;
				case 2:
					{
					setState(513);
					fromWhere();
					}
					break;
				case 3:
					{
					setState(514);
					fromBoolean();
					}
					break;
				case 4:
					{
					setState(515);
					bracketFrom();
					}
					break;
				}
				}
				}
				setState(522);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(523);
			match(CSB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NotinContext extends ParserRuleContext {
		public TerminalNode NOTIN() { return getToken(IMQParser.NOTIN, 0); }
		public FromListContext fromList() {
			return getRuleContext(FromListContext.class,0);
		}
		public FromContext from() {
			return getRuleContext(FromContext.class,0);
		}
		public FromWhereContext fromWhere() {
			return getRuleContext(FromWhereContext.class,0);
		}
		public FromBooleanContext fromBoolean() {
			return getRuleContext(FromBooleanContext.class,0);
		}
		public BracketFromContext bracketFrom() {
			return getRuleContext(BracketFromContext.class,0);
		}
		public NotinContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notin; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterNotin(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitNotin(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitNotin(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NotinContext notin() throws RecognitionException {
		NotinContext _localctx = new NotinContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_notin);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(525);
			match(NOTIN);
			setState(533);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OSB:
				{
				setState(526);
				fromList();
				}
				break;
			case GRAPH:
			case OC:
			case OB:
			case STRING_LITERAL1:
			case STRING_LITERAL2:
				{
				setState(531);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
				case 1:
					{
					setState(527);
					from();
					}
					break;
				case 2:
					{
					setState(528);
					fromWhere();
					}
					break;
				case 3:
					{
					setState(529);
					fromBoolean();
					}
					break;
				case 4:
					{
					setState(530);
					bracketFrom();
					}
					break;
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
	public static class ReferenceListContext extends ParserRuleContext {
		public TerminalNode OB() { return getToken(IMQParser.OB, 0); }
		public List<ReferenceContext> reference() {
			return getRuleContexts(ReferenceContext.class);
		}
		public ReferenceContext reference(int i) {
			return getRuleContext(ReferenceContext.class,i);
		}
		public TerminalNode CB() { return getToken(IMQParser.CB, 0); }
		public List<TerminalNode> COMMA() { return getTokens(IMQParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(IMQParser.COMMA, i);
		}
		public ReferenceListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_referenceList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterReferenceList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitReferenceList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitReferenceList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReferenceListContext referenceList() throws RecognitionException {
		ReferenceListContext _localctx = new ReferenceListContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_referenceList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(535);
			match(OB);
			setState(536);
			reference();
			setState(541);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(537);
				match(COMMA);
				setState(538);
				reference();
				}
				}
				setState(543);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(544);
			match(CB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReferenceContext extends ParserRuleContext {
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
		public SourceTypeContext sourceType() {
			return getRuleContext(SourceTypeContext.class,0);
		}
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
		public AliasContext alias() {
			return getRuleContext(AliasContext.class,0);
		}
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public SubsumptionContext subsumption() {
			return getRuleContext(SubsumptionContext.class,0);
		}
		public ReferenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reference; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterReference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitReference(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitReference(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReferenceContext reference() throws RecognitionException {
		ReferenceContext _localctx = new ReferenceContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_reference);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(546);
			match(OC);
			setState(547);
			sourceType();
			setState(556);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
			case 1:
				{
				{
				setState(549);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((_la) & ~0x3f) == 0 && ((1L << _la) & 4294967320L) != 0) {
					{
					setState(548);
					subsumption();
					}
				}

				setState(551);
				iriRef();
				}
				}
				break;
			case 2:
				{
				{
				setState(553);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((_la) & ~0x3f) == 0 && ((1L << _la) & 4294967320L) != 0) {
					{
					setState(552);
					subsumption();
					}
				}

				setState(555);
				variable();
				}
				}
				break;
			}
			setState(559);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ALIAS) {
				{
				setState(558);
				alias();
				}
			}

			setState(561);
			match(CC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public FromRangeContext fromRange() {
			return getRuleContext(FromRangeContext.class,0);
		}
		public ToRangeContext toRange() {
			return getRuleContext(ToRangeContext.class,0);
		}
		public RangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_range; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeContext range() throws RecognitionException {
		RangeContext _localctx = new RangeContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_range);
		try {
			setState(568);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,66,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(563);
				fromRange();
				setState(564);
				toRange();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(566);
				fromRange();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(567);
				toRange();
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
	public static class FromRangeContext extends ParserRuleContext {
		public TerminalNode FROM() { return getToken(IMQParser.FROM, 0); }
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
		public WhereMeasureContext whereMeasure() {
			return getRuleContext(WhereMeasureContext.class,0);
		}
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
		public FromRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fromRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterFromRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitFromRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitFromRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FromRangeContext fromRange() throws RecognitionException {
		FromRangeContext _localctx = new FromRangeContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_fromRange);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(570);
			match(FROM);
			setState(571);
			match(OC);
			setState(572);
			whereMeasure();
			setState(573);
			match(CC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ToRangeContext extends ParserRuleContext {
		public TerminalNode TO() { return getToken(IMQParser.TO, 0); }
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
		public WhereMeasureContext whereMeasure() {
			return getRuleContext(WhereMeasureContext.class,0);
		}
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
		public ToRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_toRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterToRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitToRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitToRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ToRangeContext toRange() throws RecognitionException {
		ToRangeContext _localctx = new ToRangeContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_toRange);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(575);
			match(TO);
			setState(576);
			match(OC);
			setState(577);
			whereMeasure();
			setState(578);
			match(CC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhereMeasureContext extends ParserRuleContext {
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public RelativeToContext relativeTo() {
			return getRuleContext(RelativeToContext.class,0);
		}
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public TerminalNode NUMBER() { return getToken(IMQParser.NUMBER, 0); }
		public UnitsContext units() {
			return getRuleContext(UnitsContext.class,0);
		}
		public WhereMeasureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereMeasure; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterWhereMeasure(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitWhereMeasure(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitWhereMeasure(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereMeasureContext whereMeasure() throws RecognitionException {
		WhereMeasureContext _localctx = new WhereMeasureContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_whereMeasure);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(580);
			operator();
			{
			setState(583);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING_LITERAL1:
			case STRING_LITERAL2:
				{
				setState(581);
				string();
				}
				break;
			case NUMBER:
				{
				setState(582);
				match(NUMBER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(586);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PN_CHARS) {
				{
				setState(585);
				units();
				}
			}

			}
			setState(589);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1 || _la==PN_PROPERTY) {
				{
				setState(588);
				relativeTo();
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
	public static class RelativeToContext extends ParserRuleContext {
		public TerminalNode PN_VARIABLE() { return getToken(IMQParser.PN_VARIABLE, 0); }
		public TerminalNode PN_PROPERTY() { return getToken(IMQParser.PN_PROPERTY, 0); }
		public RelativeToContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relativeTo; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterRelativeTo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitRelativeTo(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitRelativeTo(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelativeToContext relativeTo() throws RecognitionException {
		RelativeToContext _localctx = new RelativeToContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_relativeTo);
		try {
			setState(594);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				enterOuterAlt(_localctx, 1);
				{
				setState(591);
				match(T__1);
				setState(592);
				match(PN_VARIABLE);
				}
				break;
			case PN_PROPERTY:
				enterOuterAlt(_localctx, 2);
				{
				setState(593);
				match(PN_PROPERTY);
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
	public static class OperatorContext extends ParserRuleContext {
		public TerminalNode EQ() { return getToken(IMQParser.EQ, 0); }
		public TerminalNode GT() { return getToken(IMQParser.GT, 0); }
		public TerminalNode LT() { return getToken(IMQParser.LT, 0); }
		public TerminalNode LTE() { return getToken(IMQParser.LTE, 0); }
		public TerminalNode GTE() { return getToken(IMQParser.GTE, 0); }
		public TerminalNode STARTS_WITH() { return getToken(IMQParser.STARTS_WITH, 0); }
		public OperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperatorContext operator() throws RecognitionException {
		OperatorContext _localctx = new OperatorContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(596);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 67645734912L) != 0) ) {
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
	public static class UnitsContext extends ParserRuleContext {
		public TerminalNode PN_CHARS() { return getToken(IMQParser.PN_CHARS, 0); }
		public UnitsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_units; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterUnits(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitUnits(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitUnits(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnitsContext units() throws RecognitionException {
		UnitsContext _localctx = new UnitsContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_units);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(598);
			match(PN_CHARS);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SortableContext extends ParserRuleContext {
		public LatestContext latest() {
			return getRuleContext(LatestContext.class,0);
		}
		public EarliestContext earliest() {
			return getRuleContext(EarliestContext.class,0);
		}
		public MaximumContext maximum() {
			return getRuleContext(MaximumContext.class,0);
		}
		public MinimumContext minimum() {
			return getRuleContext(MinimumContext.class,0);
		}
		public CountContext count() {
			return getRuleContext(CountContext.class,0);
		}
		public SortableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sortable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterSortable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitSortable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitSortable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SortableContext sortable() throws RecognitionException {
		SortableContext _localctx = new SortableContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_sortable);
		try {
			setState(606);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LATEST:
				enterOuterAlt(_localctx, 1);
				{
				setState(600);
				latest();
				}
				break;
			case EARLIEST:
				enterOuterAlt(_localctx, 2);
				{
				setState(601);
				earliest();
				}
				break;
			case MAXIMUM:
				enterOuterAlt(_localctx, 3);
				{
				setState(602);
				maximum();
				}
				break;
			case MINIMUM:
				enterOuterAlt(_localctx, 4);
				{
				setState(603);
				minimum();
				setState(604);
				count();
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
	public static class LatestContext extends ParserRuleContext {
		public TerminalNode LATEST() { return getToken(IMQParser.LATEST, 0); }
		public TerminalNode COLON() { return getToken(IMQParser.COLON, 0); }
		public TerminalNode PN_PROPERTY() { return getToken(IMQParser.PN_PROPERTY, 0); }
		public LatestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_latest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterLatest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitLatest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitLatest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LatestContext latest() throws RecognitionException {
		LatestContext _localctx = new LatestContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_latest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(608);
			match(LATEST);
			setState(609);
			match(COLON);
			setState(610);
			match(PN_PROPERTY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EarliestContext extends ParserRuleContext {
		public TerminalNode EARLIEST() { return getToken(IMQParser.EARLIEST, 0); }
		public TerminalNode COLON() { return getToken(IMQParser.COLON, 0); }
		public TerminalNode PN_PROPERTY() { return getToken(IMQParser.PN_PROPERTY, 0); }
		public EarliestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_earliest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterEarliest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitEarliest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitEarliest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EarliestContext earliest() throws RecognitionException {
		EarliestContext _localctx = new EarliestContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_earliest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(612);
			match(EARLIEST);
			setState(613);
			match(COLON);
			setState(614);
			match(PN_PROPERTY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MaximumContext extends ParserRuleContext {
		public TerminalNode MAXIMUM() { return getToken(IMQParser.MAXIMUM, 0); }
		public TerminalNode COLON() { return getToken(IMQParser.COLON, 0); }
		public TerminalNode PN_PROPERTY() { return getToken(IMQParser.PN_PROPERTY, 0); }
		public MaximumContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_maximum; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterMaximum(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitMaximum(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitMaximum(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MaximumContext maximum() throws RecognitionException {
		MaximumContext _localctx = new MaximumContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_maximum);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(616);
			match(MAXIMUM);
			setState(617);
			match(COLON);
			setState(618);
			match(PN_PROPERTY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MinimumContext extends ParserRuleContext {
		public TerminalNode MINIMUM() { return getToken(IMQParser.MINIMUM, 0); }
		public TerminalNode COLON() { return getToken(IMQParser.COLON, 0); }
		public TerminalNode PN_PROPERTY() { return getToken(IMQParser.PN_PROPERTY, 0); }
		public MinimumContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_minimum; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterMinimum(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitMinimum(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitMinimum(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MinimumContext minimum() throws RecognitionException {
		MinimumContext _localctx = new MinimumContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_minimum);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(620);
			match(MINIMUM);
			setState(621);
			match(COLON);
			setState(622);
			match(PN_PROPERTY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CountContext extends ParserRuleContext {
		public TerminalNode COUNT() { return getToken(IMQParser.COUNT, 0); }
		public TerminalNode DIGIT() { return getToken(IMQParser.DIGIT, 0); }
		public CountContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_count; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterCount(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitCount(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitCount(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CountContext count() throws RecognitionException {
		CountContext _localctx = new CountContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_count);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(624);
			match(COUNT);
			setState(625);
			match(DIGIT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GraphContext extends ParserRuleContext {
		public TerminalNode GRAPH() { return getToken(IMQParser.GRAPH, 0); }
		public TerminalNode IRI_REF() { return getToken(IMQParser.IRI_REF, 0); }
		public GraphContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_graph; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterGraph(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitGraph(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitGraph(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GraphContext graph() throws RecognitionException {
		GraphContext _localctx = new GraphContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_graph);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(627);
			match(GRAPH);
			setState(628);
			match(IRI_REF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SourceTypeContext extends ParserRuleContext {
		public TerminalNode TYPE() { return getToken(IMQParser.TYPE, 0); }
		public TerminalNode SET() { return getToken(IMQParser.SET, 0); }
		public TerminalNode INSTANCE() { return getToken(IMQParser.INSTANCE, 0); }
		public SourceTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sourceType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterSourceType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitSourceType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitSourceType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SourceTypeContext sourceType() throws RecognitionException {
		SourceTypeContext _localctx = new SourceTypeContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_sourceType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(630);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 939524096L) != 0) ) {
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
	public static class SubsumptionContext extends ParserRuleContext {
		public DescendantorselfofContext descendantorselfof() {
			return getRuleContext(DescendantorselfofContext.class,0);
		}
		public DescendantofContext descendantof() {
			return getRuleContext(DescendantofContext.class,0);
		}
		public AncestorOfContext ancestorOf() {
			return getRuleContext(AncestorOfContext.class,0);
		}
		public SubsumptionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subsumption; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterSubsumption(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitSubsumption(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitSubsumption(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubsumptionContext subsumption() throws RecognitionException {
		SubsumptionContext _localctx = new SubsumptionContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_subsumption);
		try {
			setState(635);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__3:
				enterOuterAlt(_localctx, 1);
				{
				setState(632);
				descendantorselfof();
				}
				break;
			case LT:
				enterOuterAlt(_localctx, 2);
				{
				setState(633);
				descendantof();
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 3);
				{
				setState(634);
				ancestorOf();
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
	public static class AncestorOfContext extends ParserRuleContext {
		public AncestorOfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ancestorOf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterAncestorOf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitAncestorOf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitAncestorOf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AncestorOfContext ancestorOf() throws RecognitionException {
		AncestorOfContext _localctx = new AncestorOfContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_ancestorOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(637);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode LT() { return getToken(IMQParser.LT, 0); }
		public DescendantofContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_descendantof; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterDescendantof(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitDescendantof(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitDescendantof(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DescendantofContext descendantof() throws RecognitionException {
		DescendantofContext _localctx = new DescendantofContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_descendantof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(639);
			match(LT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public DescendantorselfofContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_descendantorselfof; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterDescendantorselfof(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitDescendantorselfof(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitDescendantorselfof(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DescendantorselfofContext descendantorselfof() throws RecognitionException {
		DescendantorselfofContext _localctx = new DescendantorselfofContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_descendantorselfof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(641);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableContext extends ParserRuleContext {
		public TerminalNode PN_VARIABLE() { return getToken(IMQParser.PN_VARIABLE, 0); }
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(643);
			match(PN_VARIABLE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AliasContext extends ParserRuleContext {
		public TerminalNode ALIAS() { return getToken(IMQParser.ALIAS, 0); }
		public TerminalNode COLON() { return getToken(IMQParser.COLON, 0); }
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public AliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitAlias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitAlias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AliasContext alias() throws RecognitionException {
		AliasContext _localctx = new AliasContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(645);
			match(ALIAS);
			setState(646);
			match(COLON);
			setState(647);
			string();
			}
		}
		catch (RecognitionException re) {
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
		"\u0004\u0001?\u028a\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
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
		"F\u0007F\u0001\u0000\u0001\u0000\u0005\u0000\u0091\b\u0000\n\u0000\f\u0000"+
		"\u0094\t\u0000\u0001\u0000\u0003\u0000\u0097\b\u0000\u0001\u0000\u0003"+
		"\u0000\u009a\b\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0003"+
		"\u0002\u00a6\b\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0005"+
		"\u0003\u00ac\b\u0003\n\u0003\f\u0003\u00af\t\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006\u00bd\b\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0005\b\u00c5"+
		"\b\b\n\b\f\b\u00c8\t\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t"+
		"\u0005\t\u00d0\b\t\n\t\f\t\u00d3\t\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001"+
		"\u000b\u0001\u000b\u0003\u000b\u00db\b\u000b\u0001\f\u0001\f\u0001\f\u0003"+
		"\f\u00e0\b\f\u0001\f\u0003\f\u00e3\b\f\u0001\f\u0003\f\u00e6\b\f\u0001"+
		"\f\u0003\f\u00e9\b\f\u0001\f\u0001\f\u0003\f\u00ed\b\f\u0001\f\u0005\f"+
		"\u00f0\b\f\n\f\f\f\u00f3\t\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0003\u0010\u0104\b\u0010\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0005\u0011\u010a\b\u0011\n"+
		"\u0011\f\u0011\u010d\t\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0003\u0012\u0114\b\u0012\u0001\u0012\u0003\u0012\u0117"+
		"\b\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0014\u0001"+
		"\u0014\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u0127\b\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0017\u0001\u0017\u0003\u0017\u012d\b\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u0135"+
		"\b\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0003"+
		"\u0019\u013c\b\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0003\u001a\u0141"+
		"\b\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0003\u001a\u0147"+
		"\b\u001a\u0004\u001a\u0149\b\u001a\u000b\u001a\f\u001a\u014a\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0003\u001b\u0150\b\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0003\u001b\u0156\b\u001b\u0004\u001b\u0158\b"+
		"\u001b\u000b\u001b\f\u001b\u0159\u0001\u001c\u0001\u001c\u0001\u001c\u0003"+
		"\u001c\u015f\b\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0003"+
		"\u001c\u0165\b\u001c\u0004\u001c\u0167\b\u001c\u000b\u001c\f\u001c\u0168"+
		"\u0001\u001d\u0003\u001d\u016c\b\u001d\u0001\u001d\u0003\u001d\u016f\b"+
		"\u001d\u0001\u001d\u0001\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0001"+
		"\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0003\u001e\u017a\b\u001e\u0001"+
		"\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0003"+
		"\u001f\u0182\b\u001f\u0001 \u0003 \u0185\b \u0001 \u0003 \u0188\b \u0001"+
		" \u0001 \u0001!\u0001!\u0001!\u0001\"\u0001\"\u0001\"\u0001\"\u0001\""+
		"\u0001\"\u0003\"\u0195\b\"\u0001\"\u0001\"\u0001#\u0001#\u0001$\u0001"+
		"$\u0003$\u019d\b$\u0001$\u0001$\u0001%\u0001%\u0001%\u0001&\u0003&\u01a5"+
		"\b&\u0001&\u0001&\u0001&\u0003&\u01aa\b&\u0001\'\u0001\'\u0001\'\u0001"+
		"\'\u0003\'\u01b0\b\'\u0004\'\u01b2\b\'\u000b\'\f\'\u01b3\u0001(\u0001"+
		"(\u0001(\u0003(\u01b9\b(\u0001(\u0001(\u0001(\u0001(\u0003(\u01bf\b(\u0004"+
		"(\u01c1\b(\u000b(\f(\u01c2\u0001)\u0001)\u0001)\u0003)\u01c8\b)\u0001"+
		")\u0001)\u0001)\u0001)\u0003)\u01ce\b)\u0004)\u01d0\b)\u000b)\f)\u01d1"+
		"\u0001*\u0001*\u0001*\u0001*\u0003*\u01d8\b*\u0001*\u0001*\u0001+\u0001"+
		"+\u0001+\u0001+\u0001+\u0001+\u0003+\u01e2\b+\u0001+\u0001+\u0001+\u0001"+
		",\u0001,\u0003,\u01e9\b,\u0001,\u0001,\u0003,\u01ed\b,\u0001-\u0001-\u0001"+
		"-\u0001-\u0001-\u0001-\u0003-\u01f5\b-\u0003-\u01f7\b-\u0001.\u0001.\u0001"+
		".\u0001.\u0001.\u0003.\u01fe\b.\u0001.\u0001.\u0001.\u0001.\u0001.\u0003"+
		".\u0205\b.\u0005.\u0207\b.\n.\f.\u020a\t.\u0001.\u0001.\u0001/\u0001/"+
		"\u0001/\u0001/\u0001/\u0001/\u0003/\u0214\b/\u0003/\u0216\b/\u00010\u0001"+
		"0\u00010\u00010\u00050\u021c\b0\n0\f0\u021f\t0\u00010\u00010\u00011\u0001"+
		"1\u00011\u00031\u0226\b1\u00011\u00011\u00031\u022a\b1\u00011\u00031\u022d"+
		"\b1\u00011\u00031\u0230\b1\u00011\u00011\u00012\u00012\u00012\u00012\u0001"+
		"2\u00032\u0239\b2\u00013\u00013\u00013\u00013\u00013\u00014\u00014\u0001"+
		"4\u00014\u00014\u00015\u00015\u00015\u00035\u0248\b5\u00015\u00035\u024b"+
		"\b5\u00015\u00035\u024e\b5\u00016\u00016\u00016\u00036\u0253\b6\u0001"+
		"7\u00017\u00018\u00018\u00019\u00019\u00019\u00019\u00019\u00019\u0003"+
		"9\u025f\b9\u0001:\u0001:\u0001:\u0001:\u0001;\u0001;\u0001;\u0001;\u0001"+
		"<\u0001<\u0001<\u0001<\u0001=\u0001=\u0001=\u0001=\u0001>\u0001>\u0001"+
		">\u0001?\u0001?\u0001?\u0001@\u0001@\u0001A\u0001A\u0001A\u0003A\u027c"+
		"\bA\u0001B\u0001B\u0001C\u0001C\u0001D\u0001D\u0001E\u0001E\u0001F\u0001"+
		"F\u0001F\u0001F\u0001F\u0000\u0000G\u0000\u0002\u0004\u0006\b\n\f\u000e"+
		"\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDF"+
		"HJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c"+
		"\u0000\u0004\u0001\u000023\u0003\u0000117799\u0001\u0000\u001e#\u0001"+
		"\u0000\u001b\u001d\u02b4\u0000\u008e\u0001\u0000\u0000\u0000\u0002\u009f"+
		"\u0001\u0000\u0000\u0000\u0004\u00a2\u0001\u0000\u0000\u0000\u0006\u00a7"+
		"\u0001\u0000\u0000\u0000\b\u00b2\u0001\u0000\u0000\u0000\n\u00b4\u0001"+
		"\u0000\u0000\u0000\f\u00b6\u0001\u0000\u0000\u0000\u000e\u00be\u0001\u0000"+
		"\u0000\u0000\u0010\u00c0\u0001\u0000\u0000\u0000\u0012\u00cb\u0001\u0000"+
		"\u0000\u0000\u0014\u00d6\u0001\u0000\u0000\u0000\u0016\u00d8\u0001\u0000"+
		"\u0000\u0000\u0018\u00dc\u0001\u0000\u0000\u0000\u001a\u00f6\u0001\u0000"+
		"\u0000\u0000\u001c\u00f9\u0001\u0000\u0000\u0000\u001e\u00fe\u0001\u0000"+
		"\u0000\u0000 \u0103\u0001\u0000\u0000\u0000\"\u0105\u0001\u0000\u0000"+
		"\u0000$\u0110\u0001\u0000\u0000\u0000&\u011a\u0001\u0000\u0000\u0000("+
		"\u011c\u0001\u0000\u0000\u0000*\u011e\u0001\u0000\u0000\u0000,\u0120\u0001"+
		"\u0000\u0000\u0000.\u012c\u0001\u0000\u0000\u00000\u0130\u0001\u0000\u0000"+
		"\u00002\u013b\u0001\u0000\u0000\u00004\u0140\u0001\u0000\u0000\u00006"+
		"\u014f\u0001\u0000\u0000\u00008\u015e\u0001\u0000\u0000\u0000:\u016b\u0001"+
		"\u0000\u0000\u0000<\u0172\u0001\u0000\u0000\u0000>\u017d\u0001\u0000\u0000"+
		"\u0000@\u0184\u0001\u0000\u0000\u0000B\u018b\u0001\u0000\u0000\u0000D"+
		"\u018e\u0001\u0000\u0000\u0000F\u0198\u0001\u0000\u0000\u0000H\u019a\u0001"+
		"\u0000\u0000\u0000J\u01a0\u0001\u0000\u0000\u0000L\u01a4\u0001\u0000\u0000"+
		"\u0000N\u01b1\u0001\u0000\u0000\u0000P\u01b8\u0001\u0000\u0000\u0000R"+
		"\u01c7\u0001\u0000\u0000\u0000T\u01d3\u0001\u0000\u0000\u0000V\u01db\u0001"+
		"\u0000\u0000\u0000X\u01ec\u0001\u0000\u0000\u0000Z\u01ee\u0001\u0000\u0000"+
		"\u0000\\\u01f8\u0001\u0000\u0000\u0000^\u020d\u0001\u0000\u0000\u0000"+
		"`\u0217\u0001\u0000\u0000\u0000b\u0222\u0001\u0000\u0000\u0000d\u0238"+
		"\u0001\u0000\u0000\u0000f\u023a\u0001\u0000\u0000\u0000h\u023f\u0001\u0000"+
		"\u0000\u0000j\u0244\u0001\u0000\u0000\u0000l\u0252\u0001\u0000\u0000\u0000"+
		"n\u0254\u0001\u0000\u0000\u0000p\u0256\u0001\u0000\u0000\u0000r\u025e"+
		"\u0001\u0000\u0000\u0000t\u0260\u0001\u0000\u0000\u0000v\u0264\u0001\u0000"+
		"\u0000\u0000x\u0268\u0001\u0000\u0000\u0000z\u026c\u0001\u0000\u0000\u0000"+
		"|\u0270\u0001\u0000\u0000\u0000~\u0273\u0001\u0000\u0000\u0000\u0080\u0276"+
		"\u0001\u0000\u0000\u0000\u0082\u027b\u0001\u0000\u0000\u0000\u0084\u027d"+
		"\u0001\u0000\u0000\u0000\u0086\u027f\u0001\u0000\u0000\u0000\u0088\u0281"+
		"\u0001\u0000\u0000\u0000\u008a\u0283\u0001\u0000\u0000\u0000\u008c\u0285"+
		"\u0001\u0000\u0000\u0000\u008e\u0092\u0005(\u0000\u0000\u008f\u0091\u0003"+
		"\u001c\u000e\u0000\u0090\u008f\u0001\u0000\u0000\u0000\u0091\u0094\u0001"+
		"\u0000\u0000\u0000\u0092\u0090\u0001\u0000\u0000\u0000\u0092\u0093\u0001"+
		"\u0000\u0000\u0000\u0093\u0096\u0001\u0000\u0000\u0000\u0094\u0092\u0001"+
		"\u0000\u0000\u0000\u0095\u0097\u0003\u0002\u0001\u0000\u0096\u0095\u0001"+
		"\u0000\u0000\u0000\u0096\u0097\u0001\u0000\u0000\u0000\u0097\u0099\u0001"+
		"\u0000\u0000\u0000\u0098\u009a\u0003\u0004\u0002\u0000\u0099\u0098\u0001"+
		"\u0000\u0000\u0000\u0099\u009a\u0001\u0000\u0000\u0000\u009a\u009b\u0001"+
		"\u0000\u0000\u0000\u009b\u009c\u0003\u0018\f\u0000\u009c\u009d\u0005)"+
		"\u0000\u0000\u009d\u009e\u0005\u0000\u0000\u0001\u009e\u0001\u0001\u0000"+
		"\u0000\u0000\u009f\u00a0\u0005\u0005\u0000\u0000\u00a0\u00a1\u0007\u0000"+
		"\u0000\u0000\u00a1\u0003\u0001\u0000\u0000\u0000\u00a2\u00a5\u0005\u0006"+
		"\u0000\u0000\u00a3\u00a6\u0003\f\u0006\u0000\u00a4\u00a6\u0003\u0006\u0003"+
		"\u0000\u00a5\u00a3\u0001\u0000\u0000\u0000\u00a5\u00a4\u0001\u0000\u0000"+
		"\u0000\u00a6\u0005\u0001\u0000\u0000\u0000\u00a7\u00a8\u0005,\u0000\u0000"+
		"\u00a8\u00ad\u0003\f\u0006\u0000\u00a9\u00aa\u00055\u0000\u0000\u00aa"+
		"\u00ac\u0003\f\u0006\u0000\u00ab\u00a9\u0001\u0000\u0000\u0000\u00ac\u00af"+
		"\u0001\u0000\u0000\u0000\u00ad\u00ab\u0001\u0000\u0000\u0000\u00ad\u00ae"+
		"\u0001\u0000\u0000\u0000\u00ae\u00b0\u0001\u0000\u0000\u0000\u00af\u00ad"+
		"\u0001\u0000\u0000\u0000\u00b0\u00b1\u0005-\u0000\u0000\u00b1\u0007\u0001"+
		"\u0000\u0000\u0000\u00b2\u00b3\u0007\u0000\u0000\u0000\u00b3\t\u0001\u0000"+
		"\u0000\u0000\u00b4\u00b5\u0007\u0000\u0000\u0000\u00b5\u000b\u0001\u0000"+
		"\u0000\u0000\u00b6\u00b7\u0003\u000e\u0007\u0000\u00b7\u00bc\u0005.\u0000"+
		"\u0000\u00b8\u00bd\u0003\u0014\n\u0000\u00b9\u00bd\u0003\u0016\u000b\u0000"+
		"\u00ba\u00bd\u0003\u0010\b\u0000\u00bb\u00bd\u0003\u0012\t\u0000\u00bc"+
		"\u00b8\u0001\u0000\u0000\u0000\u00bc\u00b9\u0001\u0000\u0000\u0000\u00bc"+
		"\u00ba\u0001\u0000\u0000\u0000\u00bc\u00bb\u0001\u0000\u0000\u0000\u00bd"+
		"\r\u0001\u0000\u0000\u0000\u00be\u00bf\u00057\u0000\u0000\u00bf\u000f"+
		"\u0001\u0000\u0000\u0000\u00c0\u00c1\u0005,\u0000\u0000\u00c1\u00c6\u0003"+
		"\u0014\n\u0000\u00c2\u00c3\u00055\u0000\u0000\u00c3\u00c5\u0003\u0014"+
		"\n\u0000\u00c4\u00c2\u0001\u0000\u0000\u0000\u00c5\u00c8\u0001\u0000\u0000"+
		"\u0000\u00c6\u00c4\u0001\u0000\u0000\u0000\u00c6\u00c7\u0001\u0000\u0000"+
		"\u0000\u00c7\u00c9\u0001\u0000\u0000\u0000\u00c8\u00c6\u0001\u0000\u0000"+
		"\u0000\u00c9\u00ca\u0005-\u0000\u0000\u00ca\u0011\u0001\u0000\u0000\u0000"+
		"\u00cb\u00cc\u0005,\u0000\u0000\u00cc\u00d1\u0003\u0016\u000b\u0000\u00cd"+
		"\u00ce\u00055\u0000\u0000\u00ce\u00d0\u0003\u0016\u000b\u0000\u00cf\u00cd"+
		"\u0001\u0000\u0000\u0000\u00d0\u00d3\u0001\u0000\u0000\u0000\u00d1\u00cf"+
		"\u0001\u0000\u0000\u0000\u00d1\u00d2\u0001\u0000\u0000\u0000\u00d2\u00d4"+
		"\u0001\u0000\u0000\u0000\u00d3\u00d1\u0001\u0000\u0000\u0000\u00d4\u00d5"+
		"\u0005-\u0000\u0000\u00d5\u0013\u0001\u0000\u0000\u0000\u00d6\u00d7\u0003"+
		"\n\u0005\u0000\u00d7\u0015\u0001\u0000\u0000\u0000\u00d8\u00da\u0007\u0001"+
		"\u0000\u0000\u00d9\u00db\u0003&\u0013\u0000\u00da\u00d9\u0001\u0000\u0000"+
		"\u0000\u00da\u00db\u0001\u0000\u0000\u0000\u00db\u0017\u0001\u0000\u0000"+
		"\u0000\u00dc\u00dd\u0005\b\u0000\u0000\u00dd\u00df\u0005(\u0000\u0000"+
		"\u00de\u00e0\u0003b1\u0000\u00df\u00de\u0001\u0000\u0000\u0000\u00df\u00e0"+
		"\u0001\u0000\u0000\u0000\u00e0\u00e2\u0001\u0000\u0000\u0000\u00e1\u00e3"+
		"\u0003\u001a\r\u0000\u00e2\u00e1\u0001\u0000\u0000\u0000\u00e2\u00e3\u0001"+
		"\u0000\u0000\u0000\u00e3\u00e5\u0001\u0000\u0000\u0000\u00e4\u00e6\u0003"+
		"(\u0014\u0000\u00e5\u00e4\u0001\u0000\u0000\u0000\u00e5\u00e6\u0001\u0000"+
		"\u0000\u0000\u00e6\u00e8\u0001\u0000\u0000\u0000\u00e7\u00e9\u0003*\u0015"+
		"\u0000\u00e8\u00e7\u0001\u0000\u0000\u0000\u00e8\u00e9\u0001\u0000\u0000"+
		"\u0000\u00e9\u00ea\u0001\u0000\u0000\u0000\u00ea\u00ec\u0003,\u0016\u0000"+
		"\u00eb\u00ed\u0003\u001e\u000f\u0000\u00ec\u00eb\u0001\u0000\u0000\u0000"+
		"\u00ec\u00ed\u0001\u0000\u0000\u0000\u00ed\u00f1\u0001\u0000\u0000\u0000"+
		"\u00ee\u00f0\u0003\u0018\f\u0000\u00ef\u00ee\u0001\u0000\u0000\u0000\u00f0"+
		"\u00f3\u0001\u0000\u0000\u0000\u00f1\u00ef\u0001\u0000\u0000\u0000\u00f1"+
		"\u00f2\u0001\u0000\u0000\u0000\u00f2\u00f4\u0001\u0000\u0000\u0000\u00f3"+
		"\u00f1\u0001\u0000\u0000\u0000\u00f4\u00f5\u0005)\u0000\u0000\u00f5\u0019"+
		"\u0001\u0000\u0000\u0000\u00f6\u00f7\u0005\u0001\u0000\u0000\u00f7\u00f8"+
		"\u0003\n\u0005\u0000\u00f8\u001b\u0001\u0000\u0000\u0000\u00f9\u00fa\u0005"+
		"\u0015\u0000\u0000\u00fa\u00fb\u00057\u0000\u0000\u00fb\u00fc\u0005.\u0000"+
		"\u0000\u00fc\u00fd\u00051\u0000\u0000\u00fd\u001d\u0001\u0000\u0000\u0000"+
		"\u00fe\u00ff\u0005\u000e\u0000\u0000\u00ff\u0100\u0003 \u0010\u0000\u0100"+
		"\u001f\u0001\u0000\u0000\u0000\u0101\u0104\u0003\"\u0011\u0000\u0102\u0104"+
		"\u0003$\u0012\u0000\u0103\u0101\u0001\u0000\u0000\u0000\u0103\u0102\u0001"+
		"\u0000\u0000\u0000\u0104!\u0001\u0000\u0000\u0000\u0105\u0106\u0005,\u0000"+
		"\u0000\u0106\u010b\u0003$\u0012\u0000\u0107\u0108\u00055\u0000\u0000\u0108"+
		"\u010a\u0003$\u0012\u0000\u0109\u0107\u0001\u0000\u0000\u0000\u010a\u010d"+
		"\u0001\u0000\u0000\u0000\u010b\u0109\u0001\u0000\u0000\u0000\u010b\u010c"+
		"\u0001\u0000\u0000\u0000\u010c\u010e\u0001\u0000\u0000\u0000\u010d\u010b"+
		"\u0001\u0000\u0000\u0000\u010e\u010f\u0005-\u0000\u0000\u010f#\u0001\u0000"+
		"\u0000\u0000\u0110\u0111\u0005(\u0000\u0000\u0111\u0113\u0003\u0016\u000b"+
		"\u0000\u0112\u0114\u0003<\u001e\u0000\u0113\u0112\u0001\u0000\u0000\u0000"+
		"\u0113\u0114\u0001\u0000\u0000\u0000\u0114\u0116\u0001\u0000\u0000\u0000"+
		"\u0115\u0117\u0003\u001e\u000f\u0000\u0116\u0115\u0001\u0000\u0000\u0000"+
		"\u0116\u0117\u0001\u0000\u0000\u0000\u0117\u0118\u0001\u0000\u0000\u0000"+
		"\u0118\u0119\u0005)\u0000\u0000\u0119%\u0001\u0000\u0000\u0000\u011a\u011b"+
		"\u0005\u0017\u0000\u0000\u011b\'\u0001\u0000\u0000\u0000\u011c\u011d\u0003"+
		"\n\u0005\u0000\u011d)\u0001\u0000\u0000\u0000\u011e\u011f\u0005\u0019"+
		"\u0000\u0000\u011f+\u0001\u0000\u0000\u0000\u0120\u0121\u0005\t\u0000"+
		"\u0000\u0121\u0126\u0005(\u0000\u0000\u0122\u0127\u0003:\u001d\u0000\u0123"+
		"\u0127\u0003.\u0017\u0000\u0124\u0127\u00032\u0019\u0000\u0125\u0127\u0003"+
		"0\u0018\u0000\u0126\u0122\u0001\u0000\u0000\u0000\u0126\u0123\u0001\u0000"+
		"\u0000\u0000\u0126\u0124\u0001\u0000\u0000\u0000\u0126\u0125\u0001\u0000"+
		"\u0000\u0000\u0127\u0128\u0001\u0000\u0000\u0000\u0128\u0129\u0005)\u0000"+
		"\u0000\u0129-\u0001\u0000\u0000\u0000\u012a\u012d\u0003:\u001d\u0000\u012b"+
		"\u012d\u00030\u0018\u0000\u012c\u012a\u0001\u0000\u0000\u0000\u012c\u012b"+
		"\u0001\u0000\u0000\u0000\u012d\u012e\u0001\u0000\u0000\u0000\u012e\u012f"+
		"\u0003<\u001e\u0000\u012f/\u0001\u0000\u0000\u0000\u0130\u0134\u0005,"+
		"\u0000\u0000\u0131\u0135\u0003.\u0017\u0000\u0132\u0135\u00032\u0019\u0000"+
		"\u0133\u0135\u0003:\u001d\u0000\u0134\u0131\u0001\u0000\u0000\u0000\u0134"+
		"\u0132\u0001\u0000\u0000\u0000\u0134\u0133\u0001\u0000\u0000\u0000\u0135"+
		"\u0136\u0001\u0000\u0000\u0000\u0136\u0137\u0005-\u0000\u0000\u01371\u0001"+
		"\u0000\u0000\u0000\u0138\u013c\u00038\u001c\u0000\u0139\u013c\u00036\u001b"+
		"\u0000\u013a\u013c\u00034\u001a\u0000\u013b\u0138\u0001\u0000\u0000\u0000"+
		"\u013b\u0139\u0001\u0000\u0000\u0000\u013b\u013a\u0001\u0000\u0000\u0000"+
		"\u013c3\u0001\u0000\u0000\u0000\u013d\u0141\u0003:\u001d\u0000\u013e\u0141"+
		"\u00030\u0018\u0000\u013f\u0141\u0003.\u0017\u0000\u0140\u013d\u0001\u0000"+
		"\u0000\u0000\u0140\u013e\u0001\u0000\u0000\u0000\u0140\u013f\u0001\u0000"+
		"\u0000\u0000\u0141\u0148\u0001\u0000\u0000\u0000\u0142\u0146\u0005&\u0000"+
		"\u0000\u0143\u0147\u0003:\u001d\u0000\u0144\u0147\u00030\u0018\u0000\u0145"+
		"\u0147\u0003.\u0017\u0000\u0146\u0143\u0001\u0000\u0000\u0000\u0146\u0144"+
		"\u0001\u0000\u0000\u0000\u0146\u0145\u0001\u0000\u0000\u0000\u0147\u0149"+
		"\u0001\u0000\u0000\u0000\u0148\u0142\u0001\u0000\u0000\u0000\u0149\u014a"+
		"\u0001\u0000\u0000\u0000\u014a\u0148\u0001\u0000\u0000\u0000\u014a\u014b"+
		"\u0001\u0000\u0000\u0000\u014b5\u0001\u0000\u0000\u0000\u014c\u0150\u0003"+
		":\u001d\u0000\u014d\u0150\u00030\u0018\u0000\u014e\u0150\u0003.\u0017"+
		"\u0000\u014f\u014c\u0001\u0000\u0000\u0000\u014f\u014d\u0001\u0000\u0000"+
		"\u0000\u014f\u014e\u0001\u0000\u0000\u0000\u0150\u0157\u0001\u0000\u0000"+
		"\u0000\u0151\u0155\u0005%\u0000\u0000\u0152\u0156\u0003:\u001d\u0000\u0153"+
		"\u0156\u00030\u0018\u0000\u0154\u0156\u0003.\u0017\u0000\u0155\u0152\u0001"+
		"\u0000\u0000\u0000\u0155\u0153\u0001\u0000\u0000\u0000\u0155\u0154\u0001"+
		"\u0000\u0000\u0000\u0156\u0158\u0001\u0000\u0000\u0000\u0157\u0151\u0001"+
		"\u0000\u0000\u0000\u0158\u0159\u0001\u0000\u0000\u0000\u0159\u0157\u0001"+
		"\u0000\u0000\u0000\u0159\u015a\u0001\u0000\u0000\u0000\u015a7\u0001\u0000"+
		"\u0000\u0000\u015b\u015f\u0003:\u001d\u0000\u015c\u015f\u00030\u0018\u0000"+
		"\u015d\u015f\u0003.\u0017\u0000\u015e\u015b\u0001\u0000\u0000\u0000\u015e"+
		"\u015c\u0001\u0000\u0000\u0000\u015e\u015d\u0001\u0000\u0000\u0000\u015f"+
		"\u0166\u0001\u0000\u0000\u0000\u0160\u0164\u0005$\u0000\u0000\u0161\u0165"+
		"\u0003:\u001d\u0000\u0162\u0165\u00030\u0018\u0000\u0163\u0165\u0003."+
		"\u0017\u0000\u0164\u0161\u0001\u0000\u0000\u0000\u0164\u0162\u0001\u0000"+
		"\u0000\u0000\u0164\u0163\u0001\u0000\u0000\u0000\u0165\u0167\u0001\u0000"+
		"\u0000\u0000\u0166\u0160\u0001\u0000\u0000\u0000\u0167\u0168\u0001\u0000"+
		"\u0000\u0000\u0168\u0166\u0001\u0000\u0000\u0000\u0168\u0169\u0001\u0000"+
		"\u0000\u0000\u01699\u0001\u0000\u0000\u0000\u016a\u016c\u0003(\u0014\u0000"+
		"\u016b\u016a\u0001\u0000\u0000\u0000\u016b\u016c\u0001\u0000\u0000\u0000"+
		"\u016c\u016e\u0001\u0000\u0000\u0000\u016d\u016f\u0003~?\u0000\u016e\u016d"+
		"\u0001\u0000\u0000\u0000\u016e\u016f\u0001\u0000\u0000\u0000\u016f\u0170"+
		"\u0001\u0000\u0000\u0000\u0170\u0171\u0003b1\u0000\u0171;\u0001\u0000"+
		"\u0000\u0000\u0172\u0173\u0005\u000b\u0000\u0000\u0173\u0179\u0005(\u0000"+
		"\u0000\u0174\u017a\u0003@ \u0000\u0175\u017a\u0003>\u001f\u0000\u0176"+
		"\u017a\u0003H$\u0000\u0177\u017a\u0003L&\u0000\u0178\u017a\u0003B!\u0000"+
		"\u0179\u0174\u0001\u0000\u0000\u0000\u0179\u0175\u0001\u0000\u0000\u0000"+
		"\u0179\u0176\u0001\u0000\u0000\u0000\u0179\u0177\u0001\u0000\u0000\u0000"+
		"\u0179\u0178\u0001\u0000\u0000\u0000\u017a\u017b\u0001\u0000\u0000\u0000"+
		"\u017b\u017c\u0005)\u0000\u0000\u017c=\u0001\u0000\u0000\u0000\u017d\u0181"+
		"\u0003V+\u0000\u017e\u0182\u0003@ \u0000\u017f\u0182\u0003H$\u0000\u0180"+
		"\u0182\u0003L&\u0000\u0181\u017e\u0001\u0000\u0000\u0000\u0181\u017f\u0001"+
		"\u0000\u0000\u0000\u0181\u0180\u0001\u0000\u0000\u0000\u0182?\u0001\u0000"+
		"\u0000\u0000\u0183\u0185\u0003(\u0014\u0000\u0184\u0183\u0001\u0000\u0000"+
		"\u0000\u0184\u0185\u0001\u0000\u0000\u0000\u0185\u0187\u0001\u0000\u0000"+
		"\u0000\u0186\u0188\u0003F#\u0000\u0187\u0186\u0001\u0000\u0000\u0000\u0187"+
		"\u0188\u0001\u0000\u0000\u0000\u0188\u0189\u0001\u0000\u0000\u0000\u0189"+
		"\u018a\u0003b1\u0000\u018aA\u0001\u0000\u0000\u0000\u018b\u018c\u0003"+
		"@ \u0000\u018c\u018d\u0003D\"\u0000\u018dC\u0001\u0000\u0000\u0000\u018e"+
		"\u0194\u0005(\u0000\u0000\u018f\u0195\u0003@ \u0000\u0190\u0195\u0003"+
		">\u001f\u0000\u0191\u0195\u0003H$\u0000\u0192\u0195\u0003L&\u0000\u0193"+
		"\u0195\u0003B!\u0000\u0194\u018f\u0001\u0000\u0000\u0000\u0194\u0190\u0001"+
		"\u0000\u0000\u0000\u0194\u0191\u0001\u0000\u0000\u0000\u0194\u0192\u0001"+
		"\u0000\u0000\u0000\u0194\u0193\u0001\u0000\u0000\u0000\u0195\u0196\u0001"+
		"\u0000\u0000\u0000\u0196\u0197\u0005)\u0000\u0000\u0197E\u0001\u0000\u0000"+
		"\u0000\u0198\u0199\u0005\f\u0000\u0000\u0199G\u0001\u0000\u0000\u0000"+
		"\u019a\u019c\u0003@ \u0000\u019b\u019d\u0003J%\u0000\u019c\u019b\u0001"+
		"\u0000\u0000\u0000\u019c\u019d\u0001\u0000\u0000\u0000\u019d\u019e\u0001"+
		"\u0000\u0000\u0000\u019e\u019f\u0003X,\u0000\u019fI\u0001\u0000\u0000"+
		"\u0000\u01a0\u01a1\u0005?\u0000\u0000\u01a1\u01a2\u0003\n\u0005\u0000"+
		"\u01a2K\u0001\u0000\u0000\u0000\u01a3\u01a5\u0003@ \u0000\u01a4\u01a3"+
		"\u0001\u0000\u0000\u0000\u01a4\u01a5\u0001\u0000\u0000\u0000\u01a5\u01a9"+
		"\u0001\u0000\u0000\u0000\u01a6\u01aa\u0003R)\u0000\u01a7\u01aa\u0003P"+
		"(\u0000\u01a8\u01aa\u0003N\'\u0000\u01a9\u01a6\u0001\u0000\u0000\u0000"+
		"\u01a9\u01a7\u0001\u0000\u0000\u0000\u01a9\u01a8\u0001\u0000\u0000\u0000"+
		"\u01aaM\u0001\u0000\u0000\u0000\u01ab\u01af\u0005&\u0000\u0000\u01ac\u01b0"+
		"\u0003@ \u0000\u01ad\u01b0\u0003T*\u0000\u01ae\u01b0\u0003H$\u0000\u01af"+
		"\u01ac\u0001\u0000\u0000\u0000\u01af\u01ad\u0001\u0000\u0000\u0000\u01af"+
		"\u01ae\u0001\u0000\u0000\u0000\u01b0\u01b2\u0001\u0000\u0000\u0000\u01b1"+
		"\u01ab\u0001\u0000\u0000\u0000\u01b2\u01b3\u0001\u0000\u0000\u0000\u01b3"+
		"\u01b1\u0001\u0000\u0000\u0000\u01b3\u01b4\u0001\u0000\u0000\u0000\u01b4"+
		"O\u0001\u0000\u0000\u0000\u01b5\u01b9\u0003@ \u0000\u01b6\u01b9\u0003"+
		"T*\u0000\u01b7\u01b9\u0003H$\u0000\u01b8\u01b5\u0001\u0000\u0000\u0000"+
		"\u01b8\u01b6\u0001\u0000\u0000\u0000\u01b8\u01b7\u0001\u0000\u0000\u0000"+
		"\u01b9\u01c0\u0001\u0000\u0000\u0000\u01ba\u01be\u0005%\u0000\u0000\u01bb"+
		"\u01bf\u0003@ \u0000\u01bc\u01bf\u0003T*\u0000\u01bd\u01bf\u0003H$\u0000"+
		"\u01be\u01bb\u0001\u0000\u0000\u0000\u01be\u01bc\u0001\u0000\u0000\u0000"+
		"\u01be\u01bd\u0001\u0000\u0000\u0000\u01bf\u01c1\u0001\u0000\u0000\u0000"+
		"\u01c0\u01ba\u0001\u0000\u0000\u0000\u01c1\u01c2\u0001\u0000\u0000\u0000"+
		"\u01c2\u01c0\u0001\u0000\u0000\u0000\u01c2\u01c3\u0001\u0000\u0000\u0000"+
		"\u01c3Q\u0001\u0000\u0000\u0000\u01c4\u01c8\u0003@ \u0000\u01c5\u01c8"+
		"\u0003T*\u0000\u01c6\u01c8\u0003H$\u0000\u01c7\u01c4\u0001\u0000\u0000"+
		"\u0000\u01c7\u01c5\u0001\u0000\u0000\u0000\u01c7\u01c6\u0001\u0000\u0000"+
		"\u0000\u01c8\u01cf\u0001\u0000\u0000\u0000\u01c9\u01cd\u0005$\u0000\u0000"+
		"\u01ca\u01ce\u0003@ \u0000\u01cb\u01ce\u0003T*\u0000\u01cc\u01ce\u0003"+
		"H$\u0000\u01cd\u01ca\u0001\u0000\u0000\u0000\u01cd\u01cb\u0001\u0000\u0000"+
		"\u0000\u01cd\u01cc\u0001\u0000\u0000\u0000\u01ce\u01d0\u0001\u0000\u0000"+
		"\u0000\u01cf\u01c9\u0001\u0000\u0000\u0000\u01d0\u01d1\u0001\u0000\u0000"+
		"\u0000\u01d1\u01cf\u0001\u0000\u0000\u0000\u01d1\u01d2\u0001\u0000\u0000"+
		"\u0000\u01d2S\u0001\u0000\u0000\u0000\u01d3\u01d7\u0005,\u0000\u0000\u01d4"+
		"\u01d8\u0003H$\u0000\u01d5\u01d8\u0003L&\u0000\u01d6\u01d8\u0003@ \u0000"+
		"\u01d7\u01d4\u0001\u0000\u0000\u0000\u01d7\u01d5\u0001\u0000\u0000\u0000"+
		"\u01d7\u01d6\u0001\u0000\u0000\u0000\u01d8\u01d9\u0001\u0000\u0000\u0000"+
		"\u01d9\u01da\u0005-\u0000\u0000\u01daU\u0001\u0000\u0000\u0000\u01db\u01dc"+
		"\u0005\r\u0000\u0000\u01dc\u01e1\u0005,\u0000\u0000\u01dd\u01e2\u0003"+
		"@ \u0000\u01de\u01e2\u0003L&\u0000\u01df\u01e2\u0003H$\u0000\u01e0\u01e2"+
		"\u0003B!\u0000\u01e1\u01dd\u0001\u0000\u0000\u0000\u01e1\u01de\u0001\u0000"+
		"\u0000\u0000\u01e1\u01df\u0001\u0000\u0000\u0000\u01e1\u01e0\u0001\u0000"+
		"\u0000\u0000\u01e2\u01e3\u0001\u0000\u0000\u0000\u01e3\u01e4\u0003r9\u0000"+
		"\u01e4\u01e5\u0005-\u0000\u0000\u01e5W\u0001\u0000\u0000\u0000\u01e6\u01e9"+
		"\u0003Z-\u0000\u01e7\u01e9\u0003^/\u0000\u01e8\u01e6\u0001\u0000\u0000"+
		"\u0000\u01e8\u01e7\u0001\u0000\u0000\u0000\u01e9\u01ed\u0001\u0000\u0000"+
		"\u0000\u01ea\u01ed\u0003d2\u0000\u01eb\u01ed\u0003j5\u0000\u01ec\u01e8"+
		"\u0001\u0000\u0000\u0000\u01ec\u01ea\u0001\u0000\u0000\u0000\u01ec\u01eb"+
		"\u0001\u0000\u0000\u0000\u01edY\u0001\u0000\u0000\u0000\u01ee\u01f6\u0005"+
		"\u001a\u0000\u0000\u01ef\u01f7\u0003\\.\u0000\u01f0\u01f5\u0003:\u001d"+
		"\u0000\u01f1\u01f5\u0003.\u0017\u0000\u01f2\u01f5\u00032\u0019\u0000\u01f3"+
		"\u01f5\u00030\u0018\u0000\u01f4\u01f0\u0001\u0000\u0000\u0000\u01f4\u01f1"+
		"\u0001\u0000\u0000\u0000\u01f4\u01f2\u0001\u0000\u0000\u0000\u01f4\u01f3"+
		"\u0001\u0000\u0000\u0000\u01f5\u01f7\u0001\u0000\u0000\u0000\u01f6\u01ef"+
		"\u0001\u0000\u0000\u0000\u01f6\u01f4\u0001\u0000\u0000\u0000\u01f7[\u0001"+
		"\u0000\u0000\u0000\u01f8\u01fd\u0005*\u0000\u0000\u01f9\u01fe\u0003:\u001d"+
		"\u0000\u01fa\u01fe\u0003.\u0017\u0000\u01fb\u01fe\u00032\u0019\u0000\u01fc"+
		"\u01fe\u00030\u0018\u0000\u01fd\u01f9\u0001\u0000\u0000\u0000\u01fd\u01fa"+
		"\u0001\u0000\u0000\u0000\u01fd\u01fb\u0001\u0000\u0000\u0000\u01fd\u01fc"+
		"\u0001\u0000\u0000\u0000\u01fe\u0208\u0001\u0000\u0000\u0000\u01ff\u0204"+
		"\u00055\u0000\u0000\u0200\u0205\u0003:\u001d\u0000\u0201\u0205\u0003."+
		"\u0017\u0000\u0202\u0205\u00032\u0019\u0000\u0203\u0205\u00030\u0018\u0000"+
		"\u0204\u0200\u0001\u0000\u0000\u0000\u0204\u0201\u0001\u0000\u0000\u0000"+
		"\u0204\u0202\u0001\u0000\u0000\u0000\u0204\u0203\u0001\u0000\u0000\u0000"+
		"\u0205\u0207\u0001\u0000\u0000\u0000\u0206\u01ff\u0001\u0000\u0000\u0000"+
		"\u0207\u020a\u0001\u0000\u0000\u0000\u0208\u0206\u0001\u0000\u0000\u0000"+
		"\u0208\u0209\u0001\u0000\u0000\u0000\u0209\u020b\u0001\u0000\u0000\u0000"+
		"\u020a\u0208\u0001\u0000\u0000\u0000\u020b\u020c\u0005+\u0000\u0000\u020c"+
		"]\u0001\u0000\u0000\u0000\u020d\u0215\u0005>\u0000\u0000\u020e\u0216\u0003"+
		"\\.\u0000\u020f\u0214\u0003:\u001d\u0000\u0210\u0214\u0003.\u0017\u0000"+
		"\u0211\u0214\u00032\u0019\u0000\u0212\u0214\u00030\u0018\u0000\u0213\u020f"+
		"\u0001\u0000\u0000\u0000\u0213\u0210\u0001\u0000\u0000\u0000\u0213\u0211"+
		"\u0001\u0000\u0000\u0000\u0213\u0212\u0001\u0000\u0000\u0000\u0214\u0216"+
		"\u0001\u0000\u0000\u0000\u0215\u020e\u0001\u0000\u0000\u0000\u0215\u0213"+
		"\u0001\u0000\u0000\u0000\u0216_\u0001\u0000\u0000\u0000\u0217\u0218\u0005"+
		",\u0000\u0000\u0218\u021d\u0003b1\u0000\u0219\u021a\u00055\u0000\u0000"+
		"\u021a\u021c\u0003b1\u0000\u021b\u0219\u0001\u0000\u0000\u0000\u021c\u021f"+
		"\u0001\u0000\u0000\u0000\u021d\u021b\u0001\u0000\u0000\u0000\u021d\u021e"+
		"\u0001\u0000\u0000\u0000\u021e\u0220\u0001\u0000\u0000\u0000\u021f\u021d"+
		"\u0001\u0000\u0000\u0000\u0220\u0221\u0005-\u0000\u0000\u0221a\u0001\u0000"+
		"\u0000\u0000\u0222\u0223\u0005(\u0000\u0000\u0223\u022c\u0003\u0080@\u0000"+
		"\u0224\u0226\u0003\u0082A\u0000\u0225\u0224\u0001\u0000\u0000\u0000\u0225"+
		"\u0226\u0001\u0000\u0000\u0000\u0226\u0227\u0001\u0000\u0000\u0000\u0227"+
		"\u022d\u0003\u0016\u000b\u0000\u0228\u022a\u0003\u0082A\u0000\u0229\u0228"+
		"\u0001\u0000\u0000\u0000\u0229\u022a\u0001\u0000\u0000\u0000\u022a\u022b"+
		"\u0001\u0000\u0000\u0000\u022b\u022d\u0003\u008aE\u0000\u022c\u0225\u0001"+
		"\u0000\u0000\u0000\u022c\u0229\u0001\u0000\u0000\u0000\u022d\u022f\u0001"+
		"\u0000\u0000\u0000\u022e\u0230\u0003\u008cF\u0000\u022f\u022e\u0001\u0000"+
		"\u0000\u0000\u022f\u0230\u0001\u0000\u0000\u0000\u0230\u0231\u0001\u0000"+
		"\u0000\u0000\u0231\u0232\u0005)\u0000\u0000\u0232c\u0001\u0000\u0000\u0000"+
		"\u0233\u0234\u0003f3\u0000\u0234\u0235\u0003h4\u0000\u0235\u0239\u0001"+
		"\u0000\u0000\u0000\u0236\u0239\u0003f3\u0000\u0237\u0239\u0003h4\u0000"+
		"\u0238\u0233\u0001\u0000\u0000\u0000\u0238\u0236\u0001\u0000\u0000\u0000"+
		"\u0238\u0237\u0001\u0000\u0000\u0000\u0239e\u0001\u0000\u0000\u0000\u023a"+
		"\u023b\u0005\t\u0000\u0000\u023b\u023c\u0005(\u0000\u0000\u023c\u023d"+
		"\u0003j5\u0000\u023d\u023e\u0005)\u0000\u0000\u023eg\u0001\u0000\u0000"+
		"\u0000\u023f\u0240\u0005\'\u0000\u0000\u0240\u0241\u0005(\u0000\u0000"+
		"\u0241\u0242\u0003j5\u0000\u0242\u0243\u0005)\u0000\u0000\u0243i\u0001"+
		"\u0000\u0000\u0000\u0244\u0247\u0003n7\u0000\u0245\u0248\u0003\n\u0005"+
		"\u0000\u0246\u0248\u0005=\u0000\u0000\u0247\u0245\u0001\u0000\u0000\u0000"+
		"\u0247\u0246\u0001\u0000\u0000\u0000\u0248\u024a\u0001\u0000\u0000\u0000"+
		"\u0249\u024b\u0003p8\u0000\u024a\u0249\u0001\u0000\u0000\u0000\u024a\u024b"+
		"\u0001\u0000\u0000\u0000\u024b\u024d\u0001\u0000\u0000\u0000\u024c\u024e"+
		"\u0003l6\u0000\u024d\u024c\u0001\u0000\u0000\u0000\u024d\u024e\u0001\u0000"+
		"\u0000\u0000\u024ek\u0001\u0000\u0000\u0000\u024f\u0250\u0005\u0002\u0000"+
		"\u0000\u0250\u0253\u00058\u0000\u0000\u0251\u0253\u00057\u0000\u0000\u0252"+
		"\u024f\u0001\u0000\u0000\u0000\u0252\u0251\u0001\u0000\u0000\u0000\u0253"+
		"m\u0001\u0000\u0000\u0000\u0254\u0255\u0007\u0002\u0000\u0000\u0255o\u0001"+
		"\u0000\u0000\u0000\u0256\u0257\u0005;\u0000\u0000\u0257q\u0001\u0000\u0000"+
		"\u0000\u0258\u025f\u0003t:\u0000\u0259\u025f\u0003v;\u0000\u025a\u025f"+
		"\u0003x<\u0000\u025b\u025c\u0003z=\u0000\u025c\u025d\u0003|>\u0000\u025d"+
		"\u025f\u0001\u0000\u0000\u0000\u025e\u0258\u0001\u0000\u0000\u0000\u025e"+
		"\u0259\u0001\u0000\u0000\u0000\u025e\u025a\u0001\u0000\u0000\u0000\u025e"+
		"\u025b\u0001\u0000\u0000\u0000\u025fs\u0001\u0000\u0000\u0000\u0260\u0261"+
		"\u0005\u0010\u0000\u0000\u0261\u0262\u0005.\u0000\u0000\u0262\u0263\u0005"+
		"7\u0000\u0000\u0263u\u0001\u0000\u0000\u0000\u0264\u0265\u0005\u000f\u0000"+
		"\u0000\u0265\u0266\u0005.\u0000\u0000\u0266\u0267\u00057\u0000\u0000\u0267"+
		"w\u0001\u0000\u0000\u0000\u0268\u0269\u0005\u0011\u0000\u0000\u0269\u026a"+
		"\u0005.\u0000\u0000\u026a\u026b\u00057\u0000\u0000\u026by\u0001\u0000"+
		"\u0000\u0000\u026c\u026d\u0005\u0012\u0000\u0000\u026d\u026e\u0005.\u0000"+
		"\u0000\u026e\u026f\u00057\u0000\u0000\u026f{\u0001\u0000\u0000\u0000\u0270"+
		"\u0271\u0005\u0013\u0000\u0000\u0271\u0272\u0005<\u0000\u0000\u0272}\u0001"+
		"\u0000\u0000\u0000\u0273\u0274\u0005\n\u0000\u0000\u0274\u0275\u00051"+
		"\u0000\u0000\u0275\u007f\u0001\u0000\u0000\u0000\u0276\u0277\u0007\u0003"+
		"\u0000\u0000\u0277\u0081\u0001\u0000\u0000\u0000\u0278\u027c\u0003\u0088"+
		"D\u0000\u0279\u027c\u0003\u0086C\u0000\u027a\u027c\u0003\u0084B\u0000"+
		"\u027b\u0278\u0001\u0000\u0000\u0000\u027b\u0279\u0001\u0000\u0000\u0000"+
		"\u027b\u027a\u0001\u0000\u0000\u0000\u027c\u0083\u0001\u0000\u0000\u0000"+
		"\u027d\u027e\u0005\u0003\u0000\u0000\u027e\u0085\u0001\u0000\u0000\u0000"+
		"\u027f\u0280\u0005 \u0000\u0000\u0280\u0087\u0001\u0000\u0000\u0000\u0281"+
		"\u0282\u0005\u0004\u0000\u0000\u0282\u0089\u0001\u0000\u0000\u0000\u0283"+
		"\u0284\u00058\u0000\u0000\u0284\u008b\u0001\u0000\u0000\u0000\u0285\u0286"+
		"\u0005\u0018\u0000\u0000\u0286\u0287\u0005.\u0000\u0000\u0287\u0288\u0003"+
		"\n\u0005\u0000\u0288\u008d\u0001\u0000\u0000\u0000I\u0092\u0096\u0099"+
		"\u00a5\u00ad\u00bc\u00c6\u00d1\u00da\u00df\u00e2\u00e5\u00e8\u00ec\u00f1"+
		"\u0103\u010b\u0113\u0116\u0126\u012c\u0134\u013b\u0140\u0146\u014a\u014f"+
		"\u0155\u0159\u015e\u0164\u0168\u016b\u016e\u0179\u0181\u0184\u0187\u0194"+
		"\u019c\u01a4\u01a9\u01af\u01b3\u01b8\u01be\u01c2\u01c7\u01cd\u01d1\u01d7"+
		"\u01e1\u01e8\u01ec\u01f4\u01f6\u01fd\u0204\u0208\u0213\u0215\u021d\u0225"+
		"\u0229\u022c\u022f\u0238\u0247\u024a\u024d\u0252\u025e\u027b";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}