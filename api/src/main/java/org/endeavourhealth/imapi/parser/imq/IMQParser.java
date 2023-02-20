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
		T__0=1, T__1=2, T__2=3, T__3=4, SEARCH_TEXT=5, ARGUMENTS=6, ID=7, QUERY=8, 
		FROM=9, GRAPH=10, WHERE=11, NOTEXIST=12, WITH=13, SELECT=14, EARLIEST=15, 
		LATEST=16, MAXIMUM=17, MINIMUM=18, COUNT=19, SOURCE_TYPE=20, PREFIX=21, 
		DESCRIPTION=22, NAME=23, ALIAS=24, ACTIVE_ONLY=25, IN=26, TYPE=27, SET=28, 
		INSTANCE=29, EQ=30, GT=31, LT=32, GTE=33, LTE=34, STARTS_WITH=35, AND=36, 
		OR=37, NOT=38, TO=39, OC=40, CC=41, OSB=42, CSB=43, OB=44, CB=45, COLON=46, 
		UCHAR=47, HEX=48, IRI_REF=49, STRING_LITERAL1=50, STRING_LITERAL2=51, 
		PN_CHARS_U=52, COMMA=53, WS=54, PN_PROPERTY=55, PN_VARIABLE=56, PN_PREFIXED=57, 
		PN_CHARS_BASE=58, PN_CHARS=59, DIGIT=60, NUMBER=61, NOTIN=62, VALUE_LABEL=63;
	public static final int
		RULE_queryRequest = 0, RULE_searchText = 1, RULE_arguments = 2, RULE_label = 3, 
		RULE_string = 4, RULE_argument = 5, RULE_parameter = 6, RULE_valueDataList = 7, 
		RULE_valueIriList = 8, RULE_value = 9, RULE_iriRef = 10, RULE_query = 11, 
		RULE_properName = 12, RULE_prefixDecl = 13, RULE_selectClause = 14, RULE_selection = 15, 
		RULE_selectionList = 16, RULE_select = 17, RULE_name = 18, RULE_description = 19, 
		RULE_activeOnly = 20, RULE_fromClause = 21, RULE_fromWhere = 22, RULE_bracketFrom = 23, 
		RULE_fromBoolean = 24, RULE_notFrom = 25, RULE_orFrom = 26, RULE_andFrom = 27, 
		RULE_from = 28, RULE_whereClause = 29, RULE_subWhere = 30, RULE_whereWith = 31, 
		RULE_where = 32, RULE_whereWhere = 33, RULE_notExist = 34, RULE_whereValue = 35, 
		RULE_valueLabel = 36, RULE_whereBoolean = 37, RULE_notWhere = 38, RULE_orWhere = 39, 
		RULE_andWhere = 40, RULE_bracketWhere = 41, RULE_with = 42, RULE_whereValueTest = 43, 
		RULE_in = 44, RULE_notin = 45, RULE_reference = 46, RULE_range = 47, RULE_fromRange = 48, 
		RULE_toRange = 49, RULE_whereMeasure = 50, RULE_relativeTo = 51, RULE_operator = 52, 
		RULE_units = 53, RULE_sortable = 54, RULE_latest = 55, RULE_earliest = 56, 
		RULE_maximum = 57, RULE_minimum = 58, RULE_count = 59, RULE_graph = 60, 
		RULE_sourceType = 61, RULE_subsumption = 62, RULE_ancestorOf = 63, RULE_descendantof = 64, 
		RULE_descendantorselfof = 65, RULE_variable = 66, RULE_alias = 67;
	private static String[] makeRuleNames() {
		return new String[] {
			"queryRequest", "searchText", "arguments", "label", "string", "argument", 
			"parameter", "valueDataList", "valueIriList", "value", "iriRef", "query", 
			"properName", "prefixDecl", "selectClause", "selection", "selectionList", 
			"select", "name", "description", "activeOnly", "fromClause", "fromWhere", 
			"bracketFrom", "fromBoolean", "notFrom", "orFrom", "andFrom", "from", 
			"whereClause", "subWhere", "whereWith", "where", "whereWhere", "notExist", 
			"whereValue", "valueLabel", "whereBoolean", "notWhere", "orWhere", "andWhere", 
			"bracketWhere", "with", "whereValueTest", "in", "notin", "reference", 
			"range", "fromRange", "toRange", "whereMeasure", "relativeTo", "operator", 
			"units", "sortable", "latest", "earliest", "maximum", "minimum", "count", 
			"graph", "sourceType", "subsumption", "ancestorOf", "descendantof", "descendantorselfof", 
			"variable", "alias"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'name '", "'relativeTo'", "'>>'", "'<<'", "'searchText'", "'arguments'", 
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
			null, null, null, null, null, "SEARCH_TEXT", "ARGUMENTS", "ID", "QUERY", 
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
			setState(136);
			match(OC);
			setState(140);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PREFIX) {
				{
				{
				setState(137);
				prefixDecl();
				}
				}
				setState(142);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(144);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEARCH_TEXT) {
				{
				setState(143);
				searchText();
				}
			}

			setState(147);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ARGUMENTS) {
				{
				setState(146);
				arguments();
				}
			}

			setState(149);
			query();
			setState(150);
			match(CC);
			setState(151);
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
			setState(153);
			match(SEARCH_TEXT);
			setState(154);
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
		public TerminalNode ARGUMENTS() { return getToken(IMQParser.ARGUMENTS, 0); }
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
		public List<TerminalNode> COMMA() { return getTokens(IMQParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(IMQParser.COMMA, i);
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
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(156);
			match(ARGUMENTS);
			setState(157);
			match(OC);
			setState(158);
			argument();
			setState(163);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(159);
				match(COMMA);
				setState(160);
				argument();
				}
				}
				setState(165);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(166);
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
		enterRule(_localctx, 6, RULE_label);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168);
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
		enterRule(_localctx, 8, RULE_string);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(170);
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
		enterRule(_localctx, 10, RULE_argument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			parameter();
			setState(173);
			match(COLON);
			setState(178);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(174);
				value();
				}
				break;
			case 2:
				{
				setState(175);
				iriRef();
				}
				break;
			case 3:
				{
				setState(176);
				valueDataList();
				}
				break;
			case 4:
				{
				setState(177);
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
		enterRule(_localctx, 12, RULE_parameter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(180);
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
		enterRule(_localctx, 14, RULE_valueDataList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(182);
			match(OB);
			setState(183);
			value();
			setState(188);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(184);
				match(COMMA);
				setState(185);
				value();
				}
				}
				setState(190);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(191);
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
		enterRule(_localctx, 16, RULE_valueIriList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(193);
			match(OB);
			setState(194);
			iriRef();
			setState(199);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(195);
				match(COMMA);
				setState(196);
				iriRef();
				}
				}
				setState(201);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(202);
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
		enterRule(_localctx, 18, RULE_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(204);
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
		public TerminalNode INSTANCE() { return getToken(IMQParser.INSTANCE, 0); }
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
		enterRule(_localctx, 20, RULE_iriRef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(206);
			match(INSTANCE);
			setState(207);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 180706935048241152L) != 0) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(209);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAME) {
				{
				setState(208);
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
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
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
		enterRule(_localctx, 22, RULE_query);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(211);
			match(QUERY);
			setState(212);
			match(OC);
			setState(214);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==INSTANCE) {
				{
				setState(213);
				iriRef();
				}
			}

			setState(217);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(216);
				properName();
				}
			}

			setState(220);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING_LITERAL1 || _la==STRING_LITERAL2) {
				{
				setState(219);
				description();
				}
			}

			setState(223);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ACTIVE_ONLY) {
				{
				setState(222);
				activeOnly();
				}
			}

			setState(225);
			fromClause();
			setState(227);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SELECT) {
				{
				setState(226);
				selectClause();
				}
			}

			setState(232);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==QUERY) {
				{
				{
				setState(229);
				query();
				}
				}
				setState(234);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(235);
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
		enterRule(_localctx, 24, RULE_properName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(237);
			match(T__0);
			setState(238);
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
		enterRule(_localctx, 26, RULE_prefixDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(240);
			match(PREFIX);
			setState(241);
			match(PN_PROPERTY);
			setState(242);
			match(COLON);
			setState(243);
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
		enterRule(_localctx, 28, RULE_selectClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(245);
			match(SELECT);
			setState(246);
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
		enterRule(_localctx, 30, RULE_selection);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(250);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OSB:
				{
				setState(248);
				selectionList();
				}
				break;
			case OC:
				{
				setState(249);
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
		public TerminalNode OSB() { return getToken(IMQParser.OSB, 0); }
		public List<SelectContext> select() {
			return getRuleContexts(SelectContext.class);
		}
		public SelectContext select(int i) {
			return getRuleContext(SelectContext.class,i);
		}
		public TerminalNode CSB() { return getToken(IMQParser.CSB, 0); }
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
		enterRule(_localctx, 32, RULE_selectionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(252);
			match(OSB);
			setState(253);
			select();
			setState(258);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(254);
				match(COMMA);
				setState(255);
				select();
				}
				}
				setState(260);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(261);
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
	public static class SelectContext extends ParserRuleContext {
		public List<TerminalNode> OC() { return getTokens(IMQParser.OC); }
		public TerminalNode OC(int i) {
			return getToken(IMQParser.OC, i);
		}
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public TerminalNode PN_PROPERTY() { return getToken(IMQParser.PN_PROPERTY, 0); }
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
		enterRule(_localctx, 34, RULE_select);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(263);
			match(OC);
			setState(266);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INSTANCE:
				{
				setState(264);
				iriRef();
				}
				break;
			case PN_PROPERTY:
				{
				setState(265);
				match(PN_PROPERTY);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(269);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(268);
				whereClause();
				}
			}

			setState(271);
			match(OC);
			setState(273);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SELECT) {
				{
				setState(272);
				selectClause();
				}
			}

			setState(275);
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
		enterRule(_localctx, 36, RULE_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(277);
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
		enterRule(_localctx, 38, RULE_description);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(279);
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
		enterRule(_localctx, 40, RULE_activeOnly);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(281);
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
		enterRule(_localctx, 42, RULE_fromClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(283);
			match(FROM);
			setState(284);
			match(OC);
			setState(289);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				{
				setState(285);
				from();
				}
				break;
			case 2:
				{
				setState(286);
				fromWhere();
				}
				break;
			case 3:
				{
				setState(287);
				fromBoolean();
				}
				break;
			case 4:
				{
				setState(288);
				bracketFrom();
				}
				break;
			}
			setState(291);
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
		enterRule(_localctx, 44, RULE_fromWhere);
		try {
			setState(300);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GRAPH:
			case TYPE:
			case SET:
			case INSTANCE:
			case OB:
			case STRING_LITERAL1:
			case STRING_LITERAL2:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(295);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case GRAPH:
				case TYPE:
				case SET:
				case INSTANCE:
				case STRING_LITERAL1:
				case STRING_LITERAL2:
					{
					setState(293);
					from();
					}
					break;
				case OB:
					{
					setState(294);
					bracketFrom();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(297);
				whereClause();
				}
				}
				break;
			case WHERE:
				enterOuterAlt(_localctx, 2);
				{
				setState(299);
				whereClause();
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
		enterRule(_localctx, 46, RULE_bracketFrom);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(302);
			match(OB);
			setState(306);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				{
				setState(303);
				fromWhere();
				}
				break;
			case 2:
				{
				setState(304);
				fromBoolean();
				}
				break;
			case 3:
				{
				setState(305);
				from();
				}
				break;
			}
			setState(308);
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
		enterRule(_localctx, 48, RULE_fromBoolean);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(313);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				{
				setState(310);
				andFrom();
				}
				break;
			case 2:
				{
				setState(311);
				orFrom();
				}
				break;
			case 3:
				{
				setState(312);
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
		enterRule(_localctx, 50, RULE_notFrom);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(318);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				{
				setState(315);
				from();
				}
				break;
			case 2:
				{
				setState(316);
				bracketFrom();
				}
				break;
			case 3:
				{
				setState(317);
				fromWhere();
				}
				break;
			}
			setState(326); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(320);
					match(NOT);
					setState(324);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
					case 1:
						{
						setState(321);
						from();
						}
						break;
					case 2:
						{
						setState(322);
						bracketFrom();
						}
						break;
					case 3:
						{
						setState(323);
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
				setState(328); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
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
		enterRule(_localctx, 52, RULE_orFrom);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(333);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				{
				setState(330);
				from();
				}
				break;
			case 2:
				{
				setState(331);
				bracketFrom();
				}
				break;
			case 3:
				{
				setState(332);
				fromWhere();
				}
				break;
			}
			setState(341); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(335);
					match(OR);
					setState(339);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
					case 1:
						{
						setState(336);
						from();
						}
						break;
					case 2:
						{
						setState(337);
						bracketFrom();
						}
						break;
					case 3:
						{
						setState(338);
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
				setState(343); 
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
		enterRule(_localctx, 54, RULE_andFrom);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(348);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				setState(345);
				from();
				}
				break;
			case 2:
				{
				setState(346);
				bracketFrom();
				}
				break;
			case 3:
				{
				setState(347);
				fromWhere();
				}
				break;
			}
			setState(356); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(350);
					match(AND);
					setState(354);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
					case 1:
						{
						setState(351);
						from();
						}
						break;
					case 2:
						{
						setState(352);
						bracketFrom();
						}
						break;
					case 3:
						{
						setState(353);
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
				setState(358); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
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
		enterRule(_localctx, 56, RULE_from);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(361);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING_LITERAL1 || _la==STRING_LITERAL2) {
				{
				setState(360);
				description();
				}
			}

			setState(364);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==GRAPH) {
				{
				setState(363);
				graph();
				}
			}

			setState(366);
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
		public SubWhereContext subWhere() {
			return getRuleContext(SubWhereContext.class,0);
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
		enterRule(_localctx, 58, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(368);
			match(WHERE);
			setState(369);
			subWhere();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SubWhereContext extends ParserRuleContext {
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
		public BracketWhereContext bracketWhere() {
			return getRuleContext(BracketWhereContext.class,0);
		}
		public SubWhereContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subWhere; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterSubWhere(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitSubWhere(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitSubWhere(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubWhereContext subWhere() throws RecognitionException {
		SubWhereContext _localctx = new SubWhereContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_subWhere);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(371);
			match(OC);
			setState(378);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
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
			case 6:
				{
				setState(377);
				bracketWhere();
				}
				break;
			}
			setState(380);
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
			setState(382);
			with();
			setState(386);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				{
				setState(383);
				where();
				}
				break;
			case 2:
				{
				setState(384);
				whereValue();
				}
				break;
			case 3:
				{
				setState(385);
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
			setState(389);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING_LITERAL1 || _la==STRING_LITERAL2) {
				{
				setState(388);
				description();
				}
			}

			setState(392);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NOTEXIST) {
				{
				setState(391);
				notExist();
				}
			}

			setState(394);
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
		public SubWhereContext subWhere() {
			return getRuleContext(SubWhereContext.class,0);
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
			setState(396);
			where();
			setState(397);
			subWhere();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 68, RULE_notExist);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(399);
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
		enterRule(_localctx, 70, RULE_whereValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(401);
			where();
			setState(403);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VALUE_LABEL) {
				{
				setState(402);
				valueLabel();
				}
			}

			setState(405);
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
		enterRule(_localctx, 72, RULE_valueLabel);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(407);
			match(VALUE_LABEL);
			setState(408);
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
		enterRule(_localctx, 74, RULE_whereBoolean);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				{
				setState(410);
				where();
				}
				break;
			}
			setState(416);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
			case 1:
				{
				setState(413);
				andWhere();
				}
				break;
			case 2:
				{
				setState(414);
				orWhere();
				}
				break;
			case 3:
				{
				setState(415);
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
		public List<WhereWhereContext> whereWhere() {
			return getRuleContexts(WhereWhereContext.class);
		}
		public WhereWhereContext whereWhere(int i) {
			return getRuleContext(WhereWhereContext.class,i);
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
		enterRule(_localctx, 76, RULE_notWhere);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(425); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(418);
				match(NOT);
				setState(423);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
				case 1:
					{
					setState(419);
					where();
					}
					break;
				case 2:
					{
					setState(420);
					bracketWhere();
					}
					break;
				case 3:
					{
					setState(421);
					whereValue();
					}
					break;
				case 4:
					{
					setState(422);
					whereWhere();
					}
					break;
				}
				}
				}
				setState(427); 
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
		public List<WhereWhereContext> whereWhere() {
			return getRuleContexts(WhereWhereContext.class);
		}
		public WhereWhereContext whereWhere(int i) {
			return getRuleContext(WhereWhereContext.class,i);
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
		enterRule(_localctx, 78, RULE_orWhere);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(433);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				{
				setState(429);
				where();
				}
				break;
			case 2:
				{
				setState(430);
				bracketWhere();
				}
				break;
			case 3:
				{
				setState(431);
				whereValue();
				}
				break;
			case 4:
				{
				setState(432);
				whereWhere();
				}
				break;
			}
			setState(442); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(435);
				match(OR);
				setState(440);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
				case 1:
					{
					setState(436);
					where();
					}
					break;
				case 2:
					{
					setState(437);
					bracketWhere();
					}
					break;
				case 3:
					{
					setState(438);
					whereValue();
					}
					break;
				case 4:
					{
					setState(439);
					whereWhere();
					}
					break;
				}
				}
				}
				setState(444); 
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
		public List<WhereWhereContext> whereWhere() {
			return getRuleContexts(WhereWhereContext.class);
		}
		public WhereWhereContext whereWhere(int i) {
			return getRuleContext(WhereWhereContext.class,i);
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
		enterRule(_localctx, 80, RULE_andWhere);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(450);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				{
				setState(446);
				where();
				}
				break;
			case 2:
				{
				setState(447);
				bracketWhere();
				}
				break;
			case 3:
				{
				setState(448);
				whereValue();
				}
				break;
			case 4:
				{
				setState(449);
				whereWhere();
				}
				break;
			}
			setState(459); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(452);
				match(AND);
				setState(457);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
				case 1:
					{
					setState(453);
					where();
					}
					break;
				case 2:
					{
					setState(454);
					bracketWhere();
					}
					break;
				case 3:
					{
					setState(455);
					whereValue();
					}
					break;
				case 4:
					{
					setState(456);
					whereWhere();
					}
					break;
				}
				}
				}
				setState(461); 
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
		enterRule(_localctx, 82, RULE_bracketWhere);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(463);
			match(OB);
			setState(467);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				{
				setState(464);
				whereValue();
				}
				break;
			case 2:
				{
				setState(465);
				whereBoolean();
				}
				break;
			case 3:
				{
				setState(466);
				where();
				}
				break;
			}
			setState(469);
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
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
		public SortableContext sortable() {
			return getRuleContext(SortableContext.class,0);
		}
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
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
		enterRule(_localctx, 84, RULE_with);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(471);
			match(WITH);
			setState(472);
			match(OC);
			setState(477);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				{
				setState(473);
				where();
				}
				break;
			case 2:
				{
				setState(474);
				whereBoolean();
				}
				break;
			case 3:
				{
				setState(475);
				whereValue();
				}
				break;
			case 4:
				{
				setState(476);
				whereWhere();
				}
				break;
			}
			setState(479);
			sortable();
			setState(480);
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
		enterRule(_localctx, 86, RULE_whereValueTest);
		try {
			setState(488);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GRAPH:
			case WHERE:
			case TYPE:
			case SET:
			case INSTANCE:
			case OB:
			case STRING_LITERAL1:
			case STRING_LITERAL2:
			case NOTIN:
				enterOuterAlt(_localctx, 1);
				{
				setState(484);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case GRAPH:
				case WHERE:
				case TYPE:
				case SET:
				case INSTANCE:
				case OB:
				case STRING_LITERAL1:
				case STRING_LITERAL2:
					{
					setState(482);
					in();
					}
					break;
				case NOTIN:
					{
					setState(483);
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
				setState(486);
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
				setState(487);
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
		enterRule(_localctx, 88, RULE_in);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(494);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
			case 1:
				{
				setState(490);
				from();
				}
				break;
			case 2:
				{
				setState(491);
				fromWhere();
				}
				break;
			case 3:
				{
				setState(492);
				fromBoolean();
				}
				break;
			case 4:
				{
				setState(493);
				bracketFrom();
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
	public static class NotinContext extends ParserRuleContext {
		public TerminalNode NOTIN() { return getToken(IMQParser.NOTIN, 0); }
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
		enterRule(_localctx, 90, RULE_notin);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(496);
			match(NOTIN);
			setState(501);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				{
				setState(497);
				from();
				}
				break;
			case 2:
				{
				setState(498);
				fromWhere();
				}
				break;
			case 3:
				{
				setState(499);
				fromBoolean();
				}
				break;
			case 4:
				{
				setState(500);
				bracketFrom();
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
	public static class ReferenceContext extends ParserRuleContext {
		public SourceTypeContext sourceType() {
			return getRuleContext(SourceTypeContext.class,0);
		}
		public AliasContext alias() {
			return getRuleContext(AliasContext.class,0);
		}
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode IRI_REF() { return getToken(IMQParser.IRI_REF, 0); }
		public TerminalNode PN_PREFIXED() { return getToken(IMQParser.PN_PREFIXED, 0); }
		public TerminalNode PN_PROPERTY() { return getToken(IMQParser.PN_PROPERTY, 0); }
		public SubsumptionContext subsumption() {
			return getRuleContext(SubsumptionContext.class,0);
		}
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
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
		enterRule(_localctx, 92, RULE_reference);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(503);
			sourceType();
			setState(515);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				{
				{
				setState(505);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((_la) & ~0x3f) == 0 && ((1L << _la) & 4294967320L) != 0) {
					{
					setState(504);
					subsumption();
					}
				}

				setState(507);
				_la = _input.LA(1);
				if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 180706935048241152L) != 0) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(509);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAME) {
					{
					setState(508);
					name();
					}
				}

				}
				}
				break;
			case 2:
				{
				{
				setState(512);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((_la) & ~0x3f) == 0 && ((1L << _la) & 4294967320L) != 0) {
					{
					setState(511);
					subsumption();
					}
				}

				setState(514);
				variable();
				}
				}
				break;
			}
			setState(518);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ALIAS) {
				{
				setState(517);
				alias();
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
		enterRule(_localctx, 94, RULE_range);
		try {
			setState(525);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(520);
				fromRange();
				setState(521);
				toRange();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(523);
				fromRange();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(524);
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
		enterRule(_localctx, 96, RULE_fromRange);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(527);
			match(FROM);
			setState(528);
			match(OC);
			setState(529);
			whereMeasure();
			setState(530);
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
		enterRule(_localctx, 98, RULE_toRange);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(532);
			match(TO);
			setState(533);
			match(OC);
			setState(534);
			whereMeasure();
			setState(535);
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
		enterRule(_localctx, 100, RULE_whereMeasure);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(537);
			operator();
			{
			setState(540);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING_LITERAL1:
			case STRING_LITERAL2:
				{
				setState(538);
				string();
				}
				break;
			case NUMBER:
				{
				setState(539);
				match(NUMBER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(543);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PN_CHARS) {
				{
				setState(542);
				units();
				}
			}

			}
			setState(546);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1 || _la==PN_PROPERTY) {
				{
				setState(545);
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
		enterRule(_localctx, 102, RULE_relativeTo);
		try {
			setState(551);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				enterOuterAlt(_localctx, 1);
				{
				setState(548);
				match(T__1);
				setState(549);
				match(PN_VARIABLE);
				}
				break;
			case PN_PROPERTY:
				enterOuterAlt(_localctx, 2);
				{
				setState(550);
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
		enterRule(_localctx, 104, RULE_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(553);
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
		enterRule(_localctx, 106, RULE_units);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(555);
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
		enterRule(_localctx, 108, RULE_sortable);
		try {
			setState(563);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LATEST:
				enterOuterAlt(_localctx, 1);
				{
				setState(557);
				latest();
				}
				break;
			case EARLIEST:
				enterOuterAlt(_localctx, 2);
				{
				setState(558);
				earliest();
				}
				break;
			case MAXIMUM:
				enterOuterAlt(_localctx, 3);
				{
				setState(559);
				maximum();
				}
				break;
			case MINIMUM:
				enterOuterAlt(_localctx, 4);
				{
				setState(560);
				minimum();
				setState(561);
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
		enterRule(_localctx, 110, RULE_latest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(565);
			match(LATEST);
			setState(566);
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
		enterRule(_localctx, 112, RULE_earliest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(568);
			match(EARLIEST);
			setState(569);
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
		enterRule(_localctx, 114, RULE_maximum);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(571);
			match(MAXIMUM);
			setState(572);
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
		enterRule(_localctx, 116, RULE_minimum);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(574);
			match(MINIMUM);
			setState(575);
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
		enterRule(_localctx, 118, RULE_count);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(577);
			match(COUNT);
			setState(578);
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
		enterRule(_localctx, 120, RULE_graph);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(580);
			match(GRAPH);
			setState(581);
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
		enterRule(_localctx, 122, RULE_sourceType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(583);
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
		enterRule(_localctx, 124, RULE_subsumption);
		try {
			setState(588);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__3:
				enterOuterAlt(_localctx, 1);
				{
				setState(585);
				descendantorselfof();
				}
				break;
			case LT:
				enterOuterAlt(_localctx, 2);
				{
				setState(586);
				descendantof();
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 3);
				{
				setState(587);
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
		enterRule(_localctx, 126, RULE_ancestorOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(590);
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
		enterRule(_localctx, 128, RULE_descendantof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(592);
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
		enterRule(_localctx, 130, RULE_descendantorselfof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(594);
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
		enterRule(_localctx, 132, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(596);
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
		enterRule(_localctx, 134, RULE_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(598);
			match(ALIAS);
			setState(599);
			match(COLON);
			setState(600);
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
		"\u0004\u0001?\u025b\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
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
		"A\u0007A\u0002B\u0007B\u0002C\u0007C\u0001\u0000\u0001\u0000\u0005\u0000"+
		"\u008b\b\u0000\n\u0000\f\u0000\u008e\t\u0000\u0001\u0000\u0003\u0000\u0091"+
		"\b\u0000\u0001\u0000\u0003\u0000\u0094\b\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002\u00a2\b\u0002"+
		"\n\u0002\f\u0002\u00a5\t\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u00b3\b\u0005\u0001\u0006\u0001"+
		"\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007\u00bb"+
		"\b\u0007\n\u0007\f\u0007\u00be\t\u0007\u0001\u0007\u0001\u0007\u0001\b"+
		"\u0001\b\u0001\b\u0001\b\u0005\b\u00c6\b\b\n\b\f\b\u00c9\t\b\u0001\b\u0001"+
		"\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0003\n\u00d2\b\n\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0003\u000b\u00d7\b\u000b\u0001\u000b\u0003\u000b"+
		"\u00da\b\u000b\u0001\u000b\u0003\u000b\u00dd\b\u000b\u0001\u000b\u0003"+
		"\u000b\u00e0\b\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u00e4\b\u000b"+
		"\u0001\u000b\u0005\u000b\u00e7\b\u000b\n\u000b\f\u000b\u00ea\t\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f"+
		"\u0003\u000f\u00fb\b\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0005\u0010\u0101\b\u0010\n\u0010\f\u0010\u0104\t\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u010b\b\u0011\u0001"+
		"\u0011\u0003\u0011\u010e\b\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u0112"+
		"\b\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0013\u0001"+
		"\u0013\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u0122\b\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0016\u0001\u0016\u0003\u0016\u0128\b\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0003\u0016\u012d\b\u0016\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0003\u0017\u0133\b\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u013a\b\u0018\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0003\u0019\u013f\b\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0003\u0019\u0145\b\u0019\u0004\u0019\u0147\b\u0019"+
		"\u000b\u0019\f\u0019\u0148\u0001\u001a\u0001\u001a\u0001\u001a\u0003\u001a"+
		"\u014e\b\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0003\u001a"+
		"\u0154\b\u001a\u0004\u001a\u0156\b\u001a\u000b\u001a\f\u001a\u0157\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0003\u001b\u015d\b\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0003\u001b\u0163\b\u001b\u0004\u001b\u0165"+
		"\b\u001b\u000b\u001b\f\u001b\u0166\u0001\u001c\u0003\u001c\u016a\b\u001c"+
		"\u0001\u001c\u0003\u001c\u016d\b\u001c\u0001\u001c\u0001\u001c\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0003\u001e\u017b\b\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0003\u001f"+
		"\u0183\b\u001f\u0001 \u0003 \u0186\b \u0001 \u0003 \u0189\b \u0001 \u0001"+
		" \u0001!\u0001!\u0001!\u0001\"\u0001\"\u0001#\u0001#\u0003#\u0194\b#\u0001"+
		"#\u0001#\u0001$\u0001$\u0001$\u0001%\u0003%\u019c\b%\u0001%\u0001%\u0001"+
		"%\u0003%\u01a1\b%\u0001&\u0001&\u0001&\u0001&\u0001&\u0003&\u01a8\b&\u0004"+
		"&\u01aa\b&\u000b&\f&\u01ab\u0001\'\u0001\'\u0001\'\u0001\'\u0003\'\u01b2"+
		"\b\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0003\'\u01b9\b\'\u0004\'"+
		"\u01bb\b\'\u000b\'\f\'\u01bc\u0001(\u0001(\u0001(\u0001(\u0003(\u01c3"+
		"\b(\u0001(\u0001(\u0001(\u0001(\u0001(\u0003(\u01ca\b(\u0004(\u01cc\b"+
		"(\u000b(\f(\u01cd\u0001)\u0001)\u0001)\u0001)\u0003)\u01d4\b)\u0001)\u0001"+
		")\u0001*\u0001*\u0001*\u0001*\u0001*\u0001*\u0003*\u01de\b*\u0001*\u0001"+
		"*\u0001*\u0001+\u0001+\u0003+\u01e5\b+\u0001+\u0001+\u0003+\u01e9\b+\u0001"+
		",\u0001,\u0001,\u0001,\u0003,\u01ef\b,\u0001-\u0001-\u0001-\u0001-\u0001"+
		"-\u0003-\u01f6\b-\u0001.\u0001.\u0003.\u01fa\b.\u0001.\u0001.\u0003.\u01fe"+
		"\b.\u0001.\u0003.\u0201\b.\u0001.\u0003.\u0204\b.\u0001.\u0003.\u0207"+
		"\b.\u0001/\u0001/\u0001/\u0001/\u0001/\u0003/\u020e\b/\u00010\u00010\u0001"+
		"0\u00010\u00010\u00011\u00011\u00011\u00011\u00011\u00012\u00012\u0001"+
		"2\u00032\u021d\b2\u00012\u00032\u0220\b2\u00012\u00032\u0223\b2\u0001"+
		"3\u00013\u00013\u00033\u0228\b3\u00014\u00014\u00015\u00015\u00016\u0001"+
		"6\u00016\u00016\u00016\u00016\u00036\u0234\b6\u00017\u00017\u00017\u0001"+
		"8\u00018\u00018\u00019\u00019\u00019\u0001:\u0001:\u0001:\u0001;\u0001"+
		";\u0001;\u0001<\u0001<\u0001<\u0001=\u0001=\u0001>\u0001>\u0001>\u0003"+
		">\u024d\b>\u0001?\u0001?\u0001@\u0001@\u0001A\u0001A\u0001B\u0001B\u0001"+
		"C\u0001C\u0001C\u0001C\u0001C\u0000\u0000D\u0000\u0002\u0004\u0006\b\n"+
		"\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.0246"+
		"8:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0000\u0004"+
		"\u0001\u000023\u0003\u0000117799\u0001\u0000\u001e#\u0001\u0000\u001b"+
		"\u001d\u0282\u0000\u0088\u0001\u0000\u0000\u0000\u0002\u0099\u0001\u0000"+
		"\u0000\u0000\u0004\u009c\u0001\u0000\u0000\u0000\u0006\u00a8\u0001\u0000"+
		"\u0000\u0000\b\u00aa\u0001\u0000\u0000\u0000\n\u00ac\u0001\u0000\u0000"+
		"\u0000\f\u00b4\u0001\u0000\u0000\u0000\u000e\u00b6\u0001\u0000\u0000\u0000"+
		"\u0010\u00c1\u0001\u0000\u0000\u0000\u0012\u00cc\u0001\u0000\u0000\u0000"+
		"\u0014\u00ce\u0001\u0000\u0000\u0000\u0016\u00d3\u0001\u0000\u0000\u0000"+
		"\u0018\u00ed\u0001\u0000\u0000\u0000\u001a\u00f0\u0001\u0000\u0000\u0000"+
		"\u001c\u00f5\u0001\u0000\u0000\u0000\u001e\u00fa\u0001\u0000\u0000\u0000"+
		" \u00fc\u0001\u0000\u0000\u0000\"\u0107\u0001\u0000\u0000\u0000$\u0115"+
		"\u0001\u0000\u0000\u0000&\u0117\u0001\u0000\u0000\u0000(\u0119\u0001\u0000"+
		"\u0000\u0000*\u011b\u0001\u0000\u0000\u0000,\u012c\u0001\u0000\u0000\u0000"+
		".\u012e\u0001\u0000\u0000\u00000\u0139\u0001\u0000\u0000\u00002\u013e"+
		"\u0001\u0000\u0000\u00004\u014d\u0001\u0000\u0000\u00006\u015c\u0001\u0000"+
		"\u0000\u00008\u0169\u0001\u0000\u0000\u0000:\u0170\u0001\u0000\u0000\u0000"+
		"<\u0173\u0001\u0000\u0000\u0000>\u017e\u0001\u0000\u0000\u0000@\u0185"+
		"\u0001\u0000\u0000\u0000B\u018c\u0001\u0000\u0000\u0000D\u018f\u0001\u0000"+
		"\u0000\u0000F\u0191\u0001\u0000\u0000\u0000H\u0197\u0001\u0000\u0000\u0000"+
		"J\u019b\u0001\u0000\u0000\u0000L\u01a9\u0001\u0000\u0000\u0000N\u01b1"+
		"\u0001\u0000\u0000\u0000P\u01c2\u0001\u0000\u0000\u0000R\u01cf\u0001\u0000"+
		"\u0000\u0000T\u01d7\u0001\u0000\u0000\u0000V\u01e8\u0001\u0000\u0000\u0000"+
		"X\u01ee\u0001\u0000\u0000\u0000Z\u01f0\u0001\u0000\u0000\u0000\\\u01f7"+
		"\u0001\u0000\u0000\u0000^\u020d\u0001\u0000\u0000\u0000`\u020f\u0001\u0000"+
		"\u0000\u0000b\u0214\u0001\u0000\u0000\u0000d\u0219\u0001\u0000\u0000\u0000"+
		"f\u0227\u0001\u0000\u0000\u0000h\u0229\u0001\u0000\u0000\u0000j\u022b"+
		"\u0001\u0000\u0000\u0000l\u0233\u0001\u0000\u0000\u0000n\u0235\u0001\u0000"+
		"\u0000\u0000p\u0238\u0001\u0000\u0000\u0000r\u023b\u0001\u0000\u0000\u0000"+
		"t\u023e\u0001\u0000\u0000\u0000v\u0241\u0001\u0000\u0000\u0000x\u0244"+
		"\u0001\u0000\u0000\u0000z\u0247\u0001\u0000\u0000\u0000|\u024c\u0001\u0000"+
		"\u0000\u0000~\u024e\u0001\u0000\u0000\u0000\u0080\u0250\u0001\u0000\u0000"+
		"\u0000\u0082\u0252\u0001\u0000\u0000\u0000\u0084\u0254\u0001\u0000\u0000"+
		"\u0000\u0086\u0256\u0001\u0000\u0000\u0000\u0088\u008c\u0005(\u0000\u0000"+
		"\u0089\u008b\u0003\u001a\r\u0000\u008a\u0089\u0001\u0000\u0000\u0000\u008b"+
		"\u008e\u0001\u0000\u0000\u0000\u008c\u008a\u0001\u0000\u0000\u0000\u008c"+
		"\u008d\u0001\u0000\u0000\u0000\u008d\u0090\u0001\u0000\u0000\u0000\u008e"+
		"\u008c\u0001\u0000\u0000\u0000\u008f\u0091\u0003\u0002\u0001\u0000\u0090"+
		"\u008f\u0001\u0000\u0000\u0000\u0090\u0091\u0001\u0000\u0000\u0000\u0091"+
		"\u0093\u0001\u0000\u0000\u0000\u0092\u0094\u0003\u0004\u0002\u0000\u0093"+
		"\u0092\u0001\u0000\u0000\u0000\u0093\u0094\u0001\u0000\u0000\u0000\u0094"+
		"\u0095\u0001\u0000\u0000\u0000\u0095\u0096\u0003\u0016\u000b\u0000\u0096"+
		"\u0097\u0005)\u0000\u0000\u0097\u0098\u0005\u0000\u0000\u0001\u0098\u0001"+
		"\u0001\u0000\u0000\u0000\u0099\u009a\u0005\u0005\u0000\u0000\u009a\u009b"+
		"\u0007\u0000\u0000\u0000\u009b\u0003\u0001\u0000\u0000\u0000\u009c\u009d"+
		"\u0005\u0006\u0000\u0000\u009d\u009e\u0005(\u0000\u0000\u009e\u00a3\u0003"+
		"\n\u0005\u0000\u009f\u00a0\u00055\u0000\u0000\u00a0\u00a2\u0003\n\u0005"+
		"\u0000\u00a1\u009f\u0001\u0000\u0000\u0000\u00a2\u00a5\u0001\u0000\u0000"+
		"\u0000\u00a3\u00a1\u0001\u0000\u0000\u0000\u00a3\u00a4\u0001\u0000\u0000"+
		"\u0000\u00a4\u00a6\u0001\u0000\u0000\u0000\u00a5\u00a3\u0001\u0000\u0000"+
		"\u0000\u00a6\u00a7\u0005)\u0000\u0000\u00a7\u0005\u0001\u0000\u0000\u0000"+
		"\u00a8\u00a9\u0007\u0000\u0000\u0000\u00a9\u0007\u0001\u0000\u0000\u0000"+
		"\u00aa\u00ab\u0007\u0000\u0000\u0000\u00ab\t\u0001\u0000\u0000\u0000\u00ac"+
		"\u00ad\u0003\f\u0006\u0000\u00ad\u00b2\u0005.\u0000\u0000\u00ae\u00b3"+
		"\u0003\u0012\t\u0000\u00af\u00b3\u0003\u0014\n\u0000\u00b0\u00b3\u0003"+
		"\u000e\u0007\u0000\u00b1\u00b3\u0003\u0010\b\u0000\u00b2\u00ae\u0001\u0000"+
		"\u0000\u0000\u00b2\u00af\u0001\u0000\u0000\u0000\u00b2\u00b0\u0001\u0000"+
		"\u0000\u0000\u00b2\u00b1\u0001\u0000\u0000\u0000\u00b3\u000b\u0001\u0000"+
		"\u0000\u0000\u00b4\u00b5\u00057\u0000\u0000\u00b5\r\u0001\u0000\u0000"+
		"\u0000\u00b6\u00b7\u0005,\u0000\u0000\u00b7\u00bc\u0003\u0012\t\u0000"+
		"\u00b8\u00b9\u00055\u0000\u0000\u00b9\u00bb\u0003\u0012\t\u0000\u00ba"+
		"\u00b8\u0001\u0000\u0000\u0000\u00bb\u00be\u0001\u0000\u0000\u0000\u00bc"+
		"\u00ba\u0001\u0000\u0000\u0000\u00bc\u00bd\u0001\u0000\u0000\u0000\u00bd"+
		"\u00bf\u0001\u0000\u0000\u0000\u00be\u00bc\u0001\u0000\u0000\u0000\u00bf"+
		"\u00c0\u0005-\u0000\u0000\u00c0\u000f\u0001\u0000\u0000\u0000\u00c1\u00c2"+
		"\u0005,\u0000\u0000\u00c2\u00c7\u0003\u0014\n\u0000\u00c3\u00c4\u0005"+
		"5\u0000\u0000\u00c4\u00c6\u0003\u0014\n\u0000\u00c5\u00c3\u0001\u0000"+
		"\u0000\u0000\u00c6\u00c9\u0001\u0000\u0000\u0000\u00c7\u00c5\u0001\u0000"+
		"\u0000\u0000\u00c7\u00c8\u0001\u0000\u0000\u0000\u00c8\u00ca\u0001\u0000"+
		"\u0000\u0000\u00c9\u00c7\u0001\u0000\u0000\u0000\u00ca\u00cb\u0005-\u0000"+
		"\u0000\u00cb\u0011\u0001\u0000\u0000\u0000\u00cc\u00cd\u0003\b\u0004\u0000"+
		"\u00cd\u0013\u0001\u0000\u0000\u0000\u00ce\u00cf\u0005\u001d\u0000\u0000"+
		"\u00cf\u00d1\u0007\u0001\u0000\u0000\u00d0\u00d2\u0003$\u0012\u0000\u00d1"+
		"\u00d0\u0001\u0000\u0000\u0000\u00d1\u00d2\u0001\u0000\u0000\u0000\u00d2"+
		"\u0015\u0001\u0000\u0000\u0000\u00d3\u00d4\u0005\b\u0000\u0000\u00d4\u00d6"+
		"\u0005(\u0000\u0000\u00d5\u00d7\u0003\u0014\n\u0000\u00d6\u00d5\u0001"+
		"\u0000\u0000\u0000\u00d6\u00d7\u0001\u0000\u0000\u0000\u00d7\u00d9\u0001"+
		"\u0000\u0000\u0000\u00d8\u00da\u0003\u0018\f\u0000\u00d9\u00d8\u0001\u0000"+
		"\u0000\u0000\u00d9\u00da\u0001\u0000\u0000\u0000\u00da\u00dc\u0001\u0000"+
		"\u0000\u0000\u00db\u00dd\u0003&\u0013\u0000\u00dc\u00db\u0001\u0000\u0000"+
		"\u0000\u00dc\u00dd\u0001\u0000\u0000\u0000\u00dd\u00df\u0001\u0000\u0000"+
		"\u0000\u00de\u00e0\u0003(\u0014\u0000\u00df\u00de\u0001\u0000\u0000\u0000"+
		"\u00df\u00e0\u0001\u0000\u0000\u0000\u00e0\u00e1\u0001\u0000\u0000\u0000"+
		"\u00e1\u00e3\u0003*\u0015\u0000\u00e2\u00e4\u0003\u001c\u000e\u0000\u00e3"+
		"\u00e2\u0001\u0000\u0000\u0000\u00e3\u00e4\u0001\u0000\u0000\u0000\u00e4"+
		"\u00e8\u0001\u0000\u0000\u0000\u00e5\u00e7\u0003\u0016\u000b\u0000\u00e6"+
		"\u00e5\u0001\u0000\u0000\u0000\u00e7\u00ea\u0001\u0000\u0000\u0000\u00e8"+
		"\u00e6\u0001\u0000\u0000\u0000\u00e8\u00e9\u0001\u0000\u0000\u0000\u00e9"+
		"\u00eb\u0001\u0000\u0000\u0000\u00ea\u00e8\u0001\u0000\u0000\u0000\u00eb"+
		"\u00ec\u0005)\u0000\u0000\u00ec\u0017\u0001\u0000\u0000\u0000\u00ed\u00ee"+
		"\u0005\u0001\u0000\u0000\u00ee\u00ef\u0003\b\u0004\u0000\u00ef\u0019\u0001"+
		"\u0000\u0000\u0000\u00f0\u00f1\u0005\u0015\u0000\u0000\u00f1\u00f2\u0005"+
		"7\u0000\u0000\u00f2\u00f3\u0005.\u0000\u0000\u00f3\u00f4\u00051\u0000"+
		"\u0000\u00f4\u001b\u0001\u0000\u0000\u0000\u00f5\u00f6\u0005\u000e\u0000"+
		"\u0000\u00f6\u00f7\u0003\u001e\u000f\u0000\u00f7\u001d\u0001\u0000\u0000"+
		"\u0000\u00f8\u00fb\u0003 \u0010\u0000\u00f9\u00fb\u0003\"\u0011\u0000"+
		"\u00fa\u00f8\u0001\u0000\u0000\u0000\u00fa\u00f9\u0001\u0000\u0000\u0000"+
		"\u00fb\u001f\u0001\u0000\u0000\u0000\u00fc\u00fd\u0005*\u0000\u0000\u00fd"+
		"\u0102\u0003\"\u0011\u0000\u00fe\u00ff\u00055\u0000\u0000\u00ff\u0101"+
		"\u0003\"\u0011\u0000\u0100\u00fe\u0001\u0000\u0000\u0000\u0101\u0104\u0001"+
		"\u0000\u0000\u0000\u0102\u0100\u0001\u0000\u0000\u0000\u0102\u0103\u0001"+
		"\u0000\u0000\u0000\u0103\u0105\u0001\u0000\u0000\u0000\u0104\u0102\u0001"+
		"\u0000\u0000\u0000\u0105\u0106\u0005+\u0000\u0000\u0106!\u0001\u0000\u0000"+
		"\u0000\u0107\u010a\u0005(\u0000\u0000\u0108\u010b\u0003\u0014\n\u0000"+
		"\u0109\u010b\u00057\u0000\u0000\u010a\u0108\u0001\u0000\u0000\u0000\u010a"+
		"\u0109\u0001\u0000\u0000\u0000\u010b\u010d\u0001\u0000\u0000\u0000\u010c"+
		"\u010e\u0003:\u001d\u0000\u010d\u010c\u0001\u0000\u0000\u0000\u010d\u010e"+
		"\u0001\u0000\u0000\u0000\u010e\u010f\u0001\u0000\u0000\u0000\u010f\u0111"+
		"\u0005(\u0000\u0000\u0110\u0112\u0003\u001c\u000e\u0000\u0111\u0110\u0001"+
		"\u0000\u0000\u0000\u0111\u0112\u0001\u0000\u0000\u0000\u0112\u0113\u0001"+
		"\u0000\u0000\u0000\u0113\u0114\u0005)\u0000\u0000\u0114#\u0001\u0000\u0000"+
		"\u0000\u0115\u0116\u0005\u0017\u0000\u0000\u0116%\u0001\u0000\u0000\u0000"+
		"\u0117\u0118\u0003\b\u0004\u0000\u0118\'\u0001\u0000\u0000\u0000\u0119"+
		"\u011a\u0005\u0019\u0000\u0000\u011a)\u0001\u0000\u0000\u0000\u011b\u011c"+
		"\u0005\t\u0000\u0000\u011c\u0121\u0005(\u0000\u0000\u011d\u0122\u0003"+
		"8\u001c\u0000\u011e\u0122\u0003,\u0016\u0000\u011f\u0122\u00030\u0018"+
		"\u0000\u0120\u0122\u0003.\u0017\u0000\u0121\u011d\u0001\u0000\u0000\u0000"+
		"\u0121\u011e\u0001\u0000\u0000\u0000\u0121\u011f\u0001\u0000\u0000\u0000"+
		"\u0121\u0120\u0001\u0000\u0000\u0000\u0122\u0123\u0001\u0000\u0000\u0000"+
		"\u0123\u0124\u0005)\u0000\u0000\u0124+\u0001\u0000\u0000\u0000\u0125\u0128"+
		"\u00038\u001c\u0000\u0126\u0128\u0003.\u0017\u0000\u0127\u0125\u0001\u0000"+
		"\u0000\u0000\u0127\u0126\u0001\u0000\u0000\u0000\u0128\u0129\u0001\u0000"+
		"\u0000\u0000\u0129\u012a\u0003:\u001d\u0000\u012a\u012d\u0001\u0000\u0000"+
		"\u0000\u012b\u012d\u0003:\u001d\u0000\u012c\u0127\u0001\u0000\u0000\u0000"+
		"\u012c\u012b\u0001\u0000\u0000\u0000\u012d-\u0001\u0000\u0000\u0000\u012e"+
		"\u0132\u0005,\u0000\u0000\u012f\u0133\u0003,\u0016\u0000\u0130\u0133\u0003"+
		"0\u0018\u0000\u0131\u0133\u00038\u001c\u0000\u0132\u012f\u0001\u0000\u0000"+
		"\u0000\u0132\u0130\u0001\u0000\u0000\u0000\u0132\u0131\u0001\u0000\u0000"+
		"\u0000\u0133\u0134\u0001\u0000\u0000\u0000\u0134\u0135\u0005-\u0000\u0000"+
		"\u0135/\u0001\u0000\u0000\u0000\u0136\u013a\u00036\u001b\u0000\u0137\u013a"+
		"\u00034\u001a\u0000\u0138\u013a\u00032\u0019\u0000\u0139\u0136\u0001\u0000"+
		"\u0000\u0000\u0139\u0137\u0001\u0000\u0000\u0000\u0139\u0138\u0001\u0000"+
		"\u0000\u0000\u013a1\u0001\u0000\u0000\u0000\u013b\u013f\u00038\u001c\u0000"+
		"\u013c\u013f\u0003.\u0017\u0000\u013d\u013f\u0003,\u0016\u0000\u013e\u013b"+
		"\u0001\u0000\u0000\u0000\u013e\u013c\u0001\u0000\u0000\u0000\u013e\u013d"+
		"\u0001\u0000\u0000\u0000\u013f\u0146\u0001\u0000\u0000\u0000\u0140\u0144"+
		"\u0005&\u0000\u0000\u0141\u0145\u00038\u001c\u0000\u0142\u0145\u0003."+
		"\u0017\u0000\u0143\u0145\u0003,\u0016\u0000\u0144\u0141\u0001\u0000\u0000"+
		"\u0000\u0144\u0142\u0001\u0000\u0000\u0000\u0144\u0143\u0001\u0000\u0000"+
		"\u0000\u0145\u0147\u0001\u0000\u0000\u0000\u0146\u0140\u0001\u0000\u0000"+
		"\u0000\u0147\u0148\u0001\u0000\u0000\u0000\u0148\u0146\u0001\u0000\u0000"+
		"\u0000\u0148\u0149\u0001\u0000\u0000\u0000\u01493\u0001\u0000\u0000\u0000"+
		"\u014a\u014e\u00038\u001c\u0000\u014b\u014e\u0003.\u0017\u0000\u014c\u014e"+
		"\u0003,\u0016\u0000\u014d\u014a\u0001\u0000\u0000\u0000\u014d\u014b\u0001"+
		"\u0000\u0000\u0000\u014d\u014c\u0001\u0000\u0000\u0000\u014e\u0155\u0001"+
		"\u0000\u0000\u0000\u014f\u0153\u0005%\u0000\u0000\u0150\u0154\u00038\u001c"+
		"\u0000\u0151\u0154\u0003.\u0017\u0000\u0152\u0154\u0003,\u0016\u0000\u0153"+
		"\u0150\u0001\u0000\u0000\u0000\u0153\u0151\u0001\u0000\u0000\u0000\u0153"+
		"\u0152\u0001\u0000\u0000\u0000\u0154\u0156\u0001\u0000\u0000\u0000\u0155"+
		"\u014f\u0001\u0000\u0000\u0000\u0156\u0157\u0001\u0000\u0000\u0000\u0157"+
		"\u0155\u0001\u0000\u0000\u0000\u0157\u0158\u0001\u0000\u0000\u0000\u0158"+
		"5\u0001\u0000\u0000\u0000\u0159\u015d\u00038\u001c\u0000\u015a\u015d\u0003"+
		".\u0017\u0000\u015b\u015d\u0003,\u0016\u0000\u015c\u0159\u0001\u0000\u0000"+
		"\u0000\u015c\u015a\u0001\u0000\u0000\u0000\u015c\u015b\u0001\u0000\u0000"+
		"\u0000\u015d\u0164\u0001\u0000\u0000\u0000\u015e\u0162\u0005$\u0000\u0000"+
		"\u015f\u0163\u00038\u001c\u0000\u0160\u0163\u0003.\u0017\u0000\u0161\u0163"+
		"\u0003,\u0016\u0000\u0162\u015f\u0001\u0000\u0000\u0000\u0162\u0160\u0001"+
		"\u0000\u0000\u0000\u0162\u0161\u0001\u0000\u0000\u0000\u0163\u0165\u0001"+
		"\u0000\u0000\u0000\u0164\u015e\u0001\u0000\u0000\u0000\u0165\u0166\u0001"+
		"\u0000\u0000\u0000\u0166\u0164\u0001\u0000\u0000\u0000\u0166\u0167\u0001"+
		"\u0000\u0000\u0000\u01677\u0001\u0000\u0000\u0000\u0168\u016a\u0003&\u0013"+
		"\u0000\u0169\u0168\u0001\u0000\u0000\u0000\u0169\u016a\u0001\u0000\u0000"+
		"\u0000\u016a\u016c\u0001\u0000\u0000\u0000\u016b\u016d\u0003x<\u0000\u016c"+
		"\u016b\u0001\u0000\u0000\u0000\u016c\u016d\u0001\u0000\u0000\u0000\u016d"+
		"\u016e\u0001\u0000\u0000\u0000\u016e\u016f\u0003\\.\u0000\u016f9\u0001"+
		"\u0000\u0000\u0000\u0170\u0171\u0005\u000b\u0000\u0000\u0171\u0172\u0003"+
		"<\u001e\u0000\u0172;\u0001\u0000\u0000\u0000\u0173\u017a\u0005(\u0000"+
		"\u0000\u0174\u017b\u0003@ \u0000\u0175\u017b\u0003>\u001f\u0000\u0176"+
		"\u017b\u0003F#\u0000\u0177\u017b\u0003J%\u0000\u0178\u017b\u0003B!\u0000"+
		"\u0179\u017b\u0003R)\u0000\u017a\u0174\u0001\u0000\u0000\u0000\u017a\u0175"+
		"\u0001\u0000\u0000\u0000\u017a\u0176\u0001\u0000\u0000\u0000\u017a\u0177"+
		"\u0001\u0000\u0000\u0000\u017a\u0178\u0001\u0000\u0000\u0000\u017a\u0179"+
		"\u0001\u0000\u0000\u0000\u017b\u017c\u0001\u0000\u0000\u0000\u017c\u017d"+
		"\u0005)\u0000\u0000\u017d=\u0001\u0000\u0000\u0000\u017e\u0182\u0003T"+
		"*\u0000\u017f\u0183\u0003@ \u0000\u0180\u0183\u0003F#\u0000\u0181\u0183"+
		"\u0003J%\u0000\u0182\u017f\u0001\u0000\u0000\u0000\u0182\u0180\u0001\u0000"+
		"\u0000\u0000\u0182\u0181\u0001\u0000\u0000\u0000\u0183?\u0001\u0000\u0000"+
		"\u0000\u0184\u0186\u0003&\u0013\u0000\u0185\u0184\u0001\u0000\u0000\u0000"+
		"\u0185\u0186\u0001\u0000\u0000\u0000\u0186\u0188\u0001\u0000\u0000\u0000"+
		"\u0187\u0189\u0003D\"\u0000\u0188\u0187\u0001\u0000\u0000\u0000\u0188"+
		"\u0189\u0001\u0000\u0000\u0000\u0189\u018a\u0001\u0000\u0000\u0000\u018a"+
		"\u018b\u0003\\.\u0000\u018bA\u0001\u0000\u0000\u0000\u018c\u018d\u0003"+
		"@ \u0000\u018d\u018e\u0003<\u001e\u0000\u018eC\u0001\u0000\u0000\u0000"+
		"\u018f\u0190\u0005\f\u0000\u0000\u0190E\u0001\u0000\u0000\u0000\u0191"+
		"\u0193\u0003@ \u0000\u0192\u0194\u0003H$\u0000\u0193\u0192\u0001\u0000"+
		"\u0000\u0000\u0193\u0194\u0001\u0000\u0000\u0000\u0194\u0195\u0001\u0000"+
		"\u0000\u0000\u0195\u0196\u0003V+\u0000\u0196G\u0001\u0000\u0000\u0000"+
		"\u0197\u0198\u0005?\u0000\u0000\u0198\u0199\u0003\b\u0004\u0000\u0199"+
		"I\u0001\u0000\u0000\u0000\u019a\u019c\u0003@ \u0000\u019b\u019a\u0001"+
		"\u0000\u0000\u0000\u019b\u019c\u0001\u0000\u0000\u0000\u019c\u01a0\u0001"+
		"\u0000\u0000\u0000\u019d\u01a1\u0003P(\u0000\u019e\u01a1\u0003N\'\u0000"+
		"\u019f\u01a1\u0003L&\u0000\u01a0\u019d\u0001\u0000\u0000\u0000\u01a0\u019e"+
		"\u0001\u0000\u0000\u0000\u01a0\u019f\u0001\u0000\u0000\u0000\u01a1K\u0001"+
		"\u0000\u0000\u0000\u01a2\u01a7\u0005&\u0000\u0000\u01a3\u01a8\u0003@ "+
		"\u0000\u01a4\u01a8\u0003R)\u0000\u01a5\u01a8\u0003F#\u0000\u01a6\u01a8"+
		"\u0003B!\u0000\u01a7\u01a3\u0001\u0000\u0000\u0000\u01a7\u01a4\u0001\u0000"+
		"\u0000\u0000\u01a7\u01a5\u0001\u0000\u0000\u0000\u01a7\u01a6\u0001\u0000"+
		"\u0000\u0000\u01a8\u01aa\u0001\u0000\u0000\u0000\u01a9\u01a2\u0001\u0000"+
		"\u0000\u0000\u01aa\u01ab\u0001\u0000\u0000\u0000\u01ab\u01a9\u0001\u0000"+
		"\u0000\u0000\u01ab\u01ac\u0001\u0000\u0000\u0000\u01acM\u0001\u0000\u0000"+
		"\u0000\u01ad\u01b2\u0003@ \u0000\u01ae\u01b2\u0003R)\u0000\u01af\u01b2"+
		"\u0003F#\u0000\u01b0\u01b2\u0003B!\u0000\u01b1\u01ad\u0001\u0000\u0000"+
		"\u0000\u01b1\u01ae\u0001\u0000\u0000\u0000\u01b1\u01af\u0001\u0000\u0000"+
		"\u0000\u01b1\u01b0\u0001\u0000\u0000\u0000\u01b2\u01ba\u0001\u0000\u0000"+
		"\u0000\u01b3\u01b8\u0005%\u0000\u0000\u01b4\u01b9\u0003@ \u0000\u01b5"+
		"\u01b9\u0003R)\u0000\u01b6\u01b9\u0003F#\u0000\u01b7\u01b9\u0003B!\u0000"+
		"\u01b8\u01b4\u0001\u0000\u0000\u0000\u01b8\u01b5\u0001\u0000\u0000\u0000"+
		"\u01b8\u01b6\u0001\u0000\u0000\u0000\u01b8\u01b7\u0001\u0000\u0000\u0000"+
		"\u01b9\u01bb\u0001\u0000\u0000\u0000\u01ba\u01b3\u0001\u0000\u0000\u0000"+
		"\u01bb\u01bc\u0001\u0000\u0000\u0000\u01bc\u01ba\u0001\u0000\u0000\u0000"+
		"\u01bc\u01bd\u0001\u0000\u0000\u0000\u01bdO\u0001\u0000\u0000\u0000\u01be"+
		"\u01c3\u0003@ \u0000\u01bf\u01c3\u0003R)\u0000\u01c0\u01c3\u0003F#\u0000"+
		"\u01c1\u01c3\u0003B!\u0000\u01c2\u01be\u0001\u0000\u0000\u0000\u01c2\u01bf"+
		"\u0001\u0000\u0000\u0000\u01c2\u01c0\u0001\u0000\u0000\u0000\u01c2\u01c1"+
		"\u0001\u0000\u0000\u0000\u01c3\u01cb\u0001\u0000\u0000\u0000\u01c4\u01c9"+
		"\u0005$\u0000\u0000\u01c5\u01ca\u0003@ \u0000\u01c6\u01ca\u0003R)\u0000"+
		"\u01c7\u01ca\u0003F#\u0000\u01c8\u01ca\u0003B!\u0000\u01c9\u01c5\u0001"+
		"\u0000\u0000\u0000\u01c9\u01c6\u0001\u0000\u0000\u0000\u01c9\u01c7\u0001"+
		"\u0000\u0000\u0000\u01c9\u01c8\u0001\u0000\u0000\u0000\u01ca\u01cc\u0001"+
		"\u0000\u0000\u0000\u01cb\u01c4\u0001\u0000\u0000\u0000\u01cc\u01cd\u0001"+
		"\u0000\u0000\u0000\u01cd\u01cb\u0001\u0000\u0000\u0000\u01cd\u01ce\u0001"+
		"\u0000\u0000\u0000\u01ceQ\u0001\u0000\u0000\u0000\u01cf\u01d3\u0005,\u0000"+
		"\u0000\u01d0\u01d4\u0003F#\u0000\u01d1\u01d4\u0003J%\u0000\u01d2\u01d4"+
		"\u0003@ \u0000\u01d3\u01d0\u0001\u0000\u0000\u0000\u01d3\u01d1\u0001\u0000"+
		"\u0000\u0000\u01d3\u01d2\u0001\u0000\u0000\u0000\u01d4\u01d5\u0001\u0000"+
		"\u0000\u0000\u01d5\u01d6\u0005-\u0000\u0000\u01d6S\u0001\u0000\u0000\u0000"+
		"\u01d7\u01d8\u0005\r\u0000\u0000\u01d8\u01dd\u0005(\u0000\u0000\u01d9"+
		"\u01de\u0003@ \u0000\u01da\u01de\u0003J%\u0000\u01db\u01de\u0003F#\u0000"+
		"\u01dc\u01de\u0003B!\u0000\u01dd\u01d9\u0001\u0000\u0000\u0000\u01dd\u01da"+
		"\u0001\u0000\u0000\u0000\u01dd\u01db\u0001\u0000\u0000\u0000\u01dd\u01dc"+
		"\u0001\u0000\u0000\u0000\u01de\u01df\u0001\u0000\u0000\u0000\u01df\u01e0"+
		"\u0003l6\u0000\u01e0\u01e1\u0005)\u0000\u0000\u01e1U\u0001\u0000\u0000"+
		"\u0000\u01e2\u01e5\u0003X,\u0000\u01e3\u01e5\u0003Z-\u0000\u01e4\u01e2"+
		"\u0001\u0000\u0000\u0000\u01e4\u01e3\u0001\u0000\u0000\u0000\u01e5\u01e9"+
		"\u0001\u0000\u0000\u0000\u01e6\u01e9\u0003^/\u0000\u01e7\u01e9\u0003d"+
		"2\u0000\u01e8\u01e4\u0001\u0000\u0000\u0000\u01e8\u01e6\u0001\u0000\u0000"+
		"\u0000\u01e8\u01e7\u0001\u0000\u0000\u0000\u01e9W\u0001\u0000\u0000\u0000"+
		"\u01ea\u01ef\u00038\u001c\u0000\u01eb\u01ef\u0003,\u0016\u0000\u01ec\u01ef"+
		"\u00030\u0018\u0000\u01ed\u01ef\u0003.\u0017\u0000\u01ee\u01ea\u0001\u0000"+
		"\u0000\u0000\u01ee\u01eb\u0001\u0000\u0000\u0000\u01ee\u01ec\u0001\u0000"+
		"\u0000\u0000\u01ee\u01ed\u0001\u0000\u0000\u0000\u01efY\u0001\u0000\u0000"+
		"\u0000\u01f0\u01f5\u0005>\u0000\u0000\u01f1\u01f6\u00038\u001c\u0000\u01f2"+
		"\u01f6\u0003,\u0016\u0000\u01f3\u01f6\u00030\u0018\u0000\u01f4\u01f6\u0003"+
		".\u0017\u0000\u01f5\u01f1\u0001\u0000\u0000\u0000\u01f5\u01f2\u0001\u0000"+
		"\u0000\u0000\u01f5\u01f3\u0001\u0000\u0000\u0000\u01f5\u01f4\u0001\u0000"+
		"\u0000\u0000\u01f6[\u0001\u0000\u0000\u0000\u01f7\u0203\u0003z=\u0000"+
		"\u01f8\u01fa\u0003|>\u0000\u01f9\u01f8\u0001\u0000\u0000\u0000\u01f9\u01fa"+
		"\u0001\u0000\u0000\u0000\u01fa\u01fb\u0001\u0000\u0000\u0000\u01fb\u01fd"+
		"\u0007\u0001\u0000\u0000\u01fc\u01fe\u0003$\u0012\u0000\u01fd\u01fc\u0001"+
		"\u0000\u0000\u0000\u01fd\u01fe\u0001\u0000\u0000\u0000\u01fe\u0204\u0001"+
		"\u0000\u0000\u0000\u01ff\u0201\u0003|>\u0000\u0200\u01ff\u0001\u0000\u0000"+
		"\u0000\u0200\u0201\u0001\u0000\u0000\u0000\u0201\u0202\u0001\u0000\u0000"+
		"\u0000\u0202\u0204\u0003\u0084B\u0000\u0203\u01f9\u0001\u0000\u0000\u0000"+
		"\u0203\u0200\u0001\u0000\u0000\u0000\u0204\u0206\u0001\u0000\u0000\u0000"+
		"\u0205\u0207\u0003\u0086C\u0000\u0206\u0205\u0001\u0000\u0000\u0000\u0206"+
		"\u0207\u0001\u0000\u0000\u0000\u0207]\u0001\u0000\u0000\u0000\u0208\u0209"+
		"\u0003`0\u0000\u0209\u020a\u0003b1\u0000\u020a\u020e\u0001\u0000\u0000"+
		"\u0000\u020b\u020e\u0003`0\u0000\u020c\u020e\u0003b1\u0000\u020d\u0208"+
		"\u0001\u0000\u0000\u0000\u020d\u020b\u0001\u0000\u0000\u0000\u020d\u020c"+
		"\u0001\u0000\u0000\u0000\u020e_\u0001\u0000\u0000\u0000\u020f\u0210\u0005"+
		"\t\u0000\u0000\u0210\u0211\u0005(\u0000\u0000\u0211\u0212\u0003d2\u0000"+
		"\u0212\u0213\u0005)\u0000\u0000\u0213a\u0001\u0000\u0000\u0000\u0214\u0215"+
		"\u0005\'\u0000\u0000\u0215\u0216\u0005(\u0000\u0000\u0216\u0217\u0003"+
		"d2\u0000\u0217\u0218\u0005)\u0000\u0000\u0218c\u0001\u0000\u0000\u0000"+
		"\u0219\u021c\u0003h4\u0000\u021a\u021d\u0003\b\u0004\u0000\u021b\u021d"+
		"\u0005=\u0000\u0000\u021c\u021a\u0001\u0000\u0000\u0000\u021c\u021b\u0001"+
		"\u0000\u0000\u0000\u021d\u021f\u0001\u0000\u0000\u0000\u021e\u0220\u0003"+
		"j5\u0000\u021f\u021e\u0001\u0000\u0000\u0000\u021f\u0220\u0001\u0000\u0000"+
		"\u0000\u0220\u0222\u0001\u0000\u0000\u0000\u0221\u0223\u0003f3\u0000\u0222"+
		"\u0221\u0001\u0000\u0000\u0000\u0222\u0223\u0001\u0000\u0000\u0000\u0223"+
		"e\u0001\u0000\u0000\u0000\u0224\u0225\u0005\u0002\u0000\u0000\u0225\u0228"+
		"\u00058\u0000\u0000\u0226\u0228\u00057\u0000\u0000\u0227\u0224\u0001\u0000"+
		"\u0000\u0000\u0227\u0226\u0001\u0000\u0000\u0000\u0228g\u0001\u0000\u0000"+
		"\u0000\u0229\u022a\u0007\u0002\u0000\u0000\u022ai\u0001\u0000\u0000\u0000"+
		"\u022b\u022c\u0005;\u0000\u0000\u022ck\u0001\u0000\u0000\u0000\u022d\u0234"+
		"\u0003n7\u0000\u022e\u0234\u0003p8\u0000\u022f\u0234\u0003r9\u0000\u0230"+
		"\u0231\u0003t:\u0000\u0231\u0232\u0003v;\u0000\u0232\u0234\u0001\u0000"+
		"\u0000\u0000\u0233\u022d\u0001\u0000\u0000\u0000\u0233\u022e\u0001\u0000"+
		"\u0000\u0000\u0233\u022f\u0001\u0000\u0000\u0000\u0233\u0230\u0001\u0000"+
		"\u0000\u0000\u0234m\u0001\u0000\u0000\u0000\u0235\u0236\u0005\u0010\u0000"+
		"\u0000\u0236\u0237\u00057\u0000\u0000\u0237o\u0001\u0000\u0000\u0000\u0238"+
		"\u0239\u0005\u000f\u0000\u0000\u0239\u023a\u00057\u0000\u0000\u023aq\u0001"+
		"\u0000\u0000\u0000\u023b\u023c\u0005\u0011\u0000\u0000\u023c\u023d\u0005"+
		"7\u0000\u0000\u023ds\u0001\u0000\u0000\u0000\u023e\u023f\u0005\u0012\u0000"+
		"\u0000\u023f\u0240\u00057\u0000\u0000\u0240u\u0001\u0000\u0000\u0000\u0241"+
		"\u0242\u0005\u0013\u0000\u0000\u0242\u0243\u0005<\u0000\u0000\u0243w\u0001"+
		"\u0000\u0000\u0000\u0244\u0245\u0005\n\u0000\u0000\u0245\u0246\u00051"+
		"\u0000\u0000\u0246y\u0001\u0000\u0000\u0000\u0247\u0248\u0007\u0003\u0000"+
		"\u0000\u0248{\u0001\u0000\u0000\u0000\u0249\u024d\u0003\u0082A\u0000\u024a"+
		"\u024d\u0003\u0080@\u0000\u024b\u024d\u0003~?\u0000\u024c\u0249\u0001"+
		"\u0000\u0000\u0000\u024c\u024a\u0001\u0000\u0000\u0000\u024c\u024b\u0001"+
		"\u0000\u0000\u0000\u024d}\u0001\u0000\u0000\u0000\u024e\u024f\u0005\u0003"+
		"\u0000\u0000\u024f\u007f\u0001\u0000\u0000\u0000\u0250\u0251\u0005 \u0000"+
		"\u0000\u0251\u0081\u0001\u0000\u0000\u0000\u0252\u0253\u0005\u0004\u0000"+
		"\u0000\u0253\u0083\u0001\u0000\u0000\u0000\u0254\u0255\u00058\u0000\u0000"+
		"\u0255\u0085\u0001\u0000\u0000\u0000\u0256\u0257\u0005\u0018\u0000\u0000"+
		"\u0257\u0258\u0005.\u0000\u0000\u0258\u0259\u0003\b\u0004\u0000\u0259"+
		"\u0087\u0001\u0000\u0000\u0000D\u008c\u0090\u0093\u00a3\u00b2\u00bc\u00c7"+
		"\u00d1\u00d6\u00d9\u00dc\u00df\u00e3\u00e8\u00fa\u0102\u010a\u010d\u0111"+
		"\u0121\u0127\u012c\u0132\u0139\u013e\u0144\u0148\u014d\u0153\u0157\u015c"+
		"\u0162\u0166\u0169\u016c\u017a\u0182\u0185\u0188\u0193\u019b\u01a0\u01a7"+
		"\u01ab\u01b1\u01b8\u01bc\u01c2\u01c9\u01cd\u01d3\u01dd\u01e4\u01e8\u01ee"+
		"\u01f5\u01f9\u01fd\u0200\u0203\u0206\u020d\u021c\u021f\u0222\u0227\u0233"+
		"\u024c";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}