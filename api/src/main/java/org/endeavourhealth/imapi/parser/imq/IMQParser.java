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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, SIGNED=11, FLOAT=12, INTEGER=13, DIGIT=14, SEARCH_TEXT=15, ARGUMENTS=16, 
		RANGE=17, QUERY=18, FROM=19, GRAPH=20, WHERE=21, NOTEXIST=22, WITH=23, 
		SELECT=24, ASCENDING=25, DESCENDING=26, COUNT=27, SOURCE_TYPE=28, PREFIXES=29, 
		COMMENT=30, DESCRIPTION=31, NAME=32, ALIAS=33, ACTIVE_ONLY=34, IN=35, 
		TYPE=36, SET=37, INSTANCE=38, EQ=39, GT=40, LT=41, GTE=42, LTE=43, STARTS_WITH=44, 
		AND=45, OR=46, NOT=47, TO=48, OC=49, CC=50, OSB=51, CSB=52, OB=53, CB=54, 
		COLON=55, IRI_REF=56, STRING_LITERAL1=57, STRING_LITERAL2=58, PN_CHARS_U=59, 
		COMMA=60, WS=61, DELIM=62, PN_PREFIXED=63, PN_PROPERTY=64, PN_VARIABLE=65, 
		PN_CHARS_BASE=66, PN_CHARS=67, NOTIN=68, VAR=69;
	public static final int
		RULE_queryRequest = 0, RULE_searchText = 1, RULE_arguments = 2, RULE_label = 3, 
		RULE_string = 4, RULE_argument = 5, RULE_parameter = 6, RULE_valueDataList = 7, 
		RULE_valueIriList = 8, RULE_value = 9, RULE_iriRef = 10, RULE_query = 11, 
		RULE_properName = 12, RULE_prefixes = 13, RULE_prefixed = 14, RULE_selectClause = 15, 
		RULE_select = 16, RULE_name = 17, RULE_description = 18, RULE_activeOnly = 19, 
		RULE_fromClause = 20, RULE_booleanFrom = 21, RULE_notFrom = 22, RULE_orFrom = 23, 
		RULE_andFrom = 24, RULE_from = 25, RULE_whereClause = 26, RULE_where = 27, 
		RULE_booleanWhere = 28, RULE_notWhere = 29, RULE_orWhere = 30, RULE_andWhere = 31, 
		RULE_with = 32, RULE_whereValueTest = 33, RULE_valueLabel = 34, RULE_inClause = 35, 
		RULE_notInClause = 36, RULE_reference = 37, RULE_inverseOf = 38, RULE_range = 39, 
		RULE_fromRange = 40, RULE_toRange = 41, RULE_whereMeasure = 42, RULE_number = 43, 
		RULE_relativeTo = 44, RULE_operator = 45, RULE_units = 46, RULE_sortable = 47, 
		RULE_direction = 48, RULE_count = 49, RULE_graph = 50, RULE_sourceType = 51, 
		RULE_type = 52, RULE_set = 53, RULE_var = 54, RULE_ancestorAndDescendantOf = 55, 
		RULE_ancestorOf = 56, RULE_descendantof = 57, RULE_descendantorselfof = 58, 
		RULE_variable = 59, RULE_alias = 60;
	private static String[] makeRuleNames() {
		return new String[] {
			"queryRequest", "searchText", "arguments", "label", "string", "argument", 
			"parameter", "valueDataList", "valueIriList", "value", "iriRef", "query", 
			"properName", "prefixes", "prefixed", "selectClause", "select", "name", 
			"description", "activeOnly", "fromClause", "booleanFrom", "notFrom", 
			"orFrom", "andFrom", "from", "whereClause", "where", "booleanWhere", 
			"notWhere", "orWhere", "andWhere", "with", "whereValueTest", "valueLabel", 
			"inClause", "notInClause", "reference", "inverseOf", "range", "fromRange", 
			"toRange", "whereMeasure", "number", "relativeTo", "operator", "units", 
			"sortable", "direction", "count", "graph", "sourceType", "type", "set", 
			"var", "ancestorAndDescendantOf", "ancestorOf", "descendantof", "descendantorselfof", 
			"variable", "alias"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'name '", "'valueLabel'", "'inverseOf'", "'relativeTo'", "'@'", 
			"'^'", "'$'", "'>><<'", "'>>'", "'<<'", null, null, null, null, "'searchText'", 
			"'arguments'", "'range'", "'query'", "'from'", "'graph'", "'where'", 
			"'notExist'", "'with'", "'select'", "'ascending'", "'descending'", "'count'", 
			"'sourceType'", null, null, "'description'", null, "'alias'", "'activeOnly'", 
			"'in'", "'@type'", "'@set'", "'@id'", "'='", "'>'", "'<'", "'>='", "'<='", 
			"'startsWith'", null, null, null, "'to'", "'{'", "'}'", "'['", "']'", 
			"'('", "')'", "':'", null, null, null, null, "','", null, null, null, 
			null, null, null, null, "'notIn'", "'@var'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, "SIGNED", 
			"FLOAT", "INTEGER", "DIGIT", "SEARCH_TEXT", "ARGUMENTS", "RANGE", "QUERY", 
			"FROM", "GRAPH", "WHERE", "NOTEXIST", "WITH", "SELECT", "ASCENDING", 
			"DESCENDING", "COUNT", "SOURCE_TYPE", "PREFIXES", "COMMENT", "DESCRIPTION", 
			"NAME", "ALIAS", "ACTIVE_ONLY", "IN", "TYPE", "SET", "INSTANCE", "EQ", 
			"GT", "LT", "GTE", "LTE", "STARTS_WITH", "AND", "OR", "NOT", "TO", "OC", 
			"CC", "OSB", "CSB", "OB", "CB", "COLON", "IRI_REF", "STRING_LITERAL1", 
			"STRING_LITERAL2", "PN_CHARS_U", "COMMA", "WS", "DELIM", "PN_PREFIXED", 
			"PN_PROPERTY", "PN_VARIABLE", "PN_CHARS_BASE", "PN_CHARS", "NOTIN", "VAR"
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
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public TerminalNode EOF() { return getToken(IMQParser.EOF, 0); }
		public PrefixesContext prefixes() {
			return getRuleContext(PrefixesContext.class,0);
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
			setState(123);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PREFIXES) {
				{
				setState(122);
				prefixes();
				}
			}

			setState(126);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEARCH_TEXT) {
				{
				setState(125);
				searchText();
				}
			}

			setState(129);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ARGUMENTS) {
				{
				setState(128);
				arguments();
				}
			}

			setState(131);
			query();
			setState(132);
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
			setState(134);
			match(SEARCH_TEXT);
			setState(135);
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
			setState(137);
			match(ARGUMENTS);
			setState(138);
			match(OC);
			setState(139);
			argument();
			setState(144);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(140);
				match(COMMA);
				setState(141);
				argument();
				}
				}
				setState(146);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(147);
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
			setState(149);
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
			setState(151);
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
			setState(153);
			parameter();
			setState(154);
			match(COLON);
			setState(158);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(155);
				value();
				}
				break;
			case 2:
				{
				setState(156);
				valueDataList();
				}
				break;
			case 3:
				{
				setState(157);
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
			setState(160);
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
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
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
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			value();
			setState(167);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(163);
					match(COMMA);
					setState(164);
					value();
					}
					} 
				}
				setState(169);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
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
	public static class ValueIriListContext extends ParserRuleContext {
		public List<IriRefContext> iriRef() {
			return getRuleContexts(IriRefContext.class);
		}
		public IriRefContext iriRef(int i) {
			return getRuleContext(IriRefContext.class,i);
		}
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
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(170);
			iriRef();
			setState(175);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(171);
					match(COMMA);
					setState(172);
					iriRef();
					}
					} 
				}
				setState(177);
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
			setState(178);
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
			setState(180);
			_la = _input.LA(1);
			if ( !(_la==IRI_REF || _la==PN_PREFIXED) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(182);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAME) {
				{
				setState(181);
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
			setState(184);
			match(QUERY);
			setState(185);
			match(OC);
			setState(187);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IRI_REF || _la==PN_PREFIXED) {
				{
				setState(186);
				iriRef();
				}
			}

			setState(190);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(189);
				properName();
				}
			}

			setState(193);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DESCRIPTION) {
				{
				setState(192);
				description();
				}
			}

			setState(196);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ACTIVE_ONLY) {
				{
				setState(195);
				activeOnly();
				}
			}

			setState(198);
			fromClause();
			setState(200);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SELECT) {
				{
				setState(199);
				selectClause();
				}
			}

			setState(205);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==QUERY) {
				{
				{
				setState(202);
				query();
				}
				}
				setState(207);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(208);
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
			setState(210);
			match(T__0);
			setState(211);
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
	public static class PrefixesContext extends ParserRuleContext {
		public TerminalNode PREFIXES() { return getToken(IMQParser.PREFIXES, 0); }
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
		public List<PrefixedContext> prefixed() {
			return getRuleContexts(PrefixedContext.class);
		}
		public PrefixedContext prefixed(int i) {
			return getRuleContext(PrefixedContext.class,i);
		}
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
		public List<TerminalNode> COMMA() { return getTokens(IMQParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(IMQParser.COMMA, i);
		}
		public PrefixesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefixes; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterPrefixes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitPrefixes(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitPrefixes(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixesContext prefixes() throws RecognitionException {
		PrefixesContext _localctx = new PrefixesContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_prefixes);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(213);
			match(PREFIXES);
			setState(214);
			match(OC);
			setState(215);
			prefixed();
			setState(220);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(216);
				match(COMMA);
				setState(217);
				prefixed();
				}
				}
				setState(222);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(223);
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
	public static class PrefixedContext extends ParserRuleContext {
		public TerminalNode PN_PROPERTY() { return getToken(IMQParser.PN_PROPERTY, 0); }
		public TerminalNode COLON() { return getToken(IMQParser.COLON, 0); }
		public TerminalNode IRI_REF() { return getToken(IMQParser.IRI_REF, 0); }
		public PrefixedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefixed; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterPrefixed(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitPrefixed(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitPrefixed(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixedContext prefixed() throws RecognitionException {
		PrefixedContext _localctx = new PrefixedContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_prefixed);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(225);
			match(PN_PROPERTY);
			setState(226);
			match(COLON);
			setState(227);
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
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
		public List<SelectContext> select() {
			return getRuleContexts(SelectContext.class);
		}
		public SelectContext select(int i) {
			return getRuleContext(SelectContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(IMQParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(IMQParser.COMMA, i);
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
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(229);
			match(SELECT);
			setState(230);
			match(OC);
			{
			setState(231);
			select();
			setState(236);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(232);
				match(COMMA);
				setState(233);
				select();
				}
				}
				setState(238);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(239);
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
	public static class SelectContext extends ParserRuleContext {
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public TerminalNode PN_PROPERTY() { return getToken(IMQParser.PN_PROPERTY, 0); }
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
		public List<SelectContext> select() {
			return getRuleContexts(SelectContext.class);
		}
		public SelectContext select(int i) {
			return getRuleContext(SelectContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(IMQParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(IMQParser.COMMA, i);
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
		enterRule(_localctx, 32, RULE_select);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(243);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRI_REF:
			case PN_PREFIXED:
				{
				setState(241);
				iriRef();
				}
				break;
			case PN_PROPERTY:
				{
				setState(242);
				match(PN_PROPERTY);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(246);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(245);
				whereClause();
				}
			}

			setState(259);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OC) {
				{
				setState(248);
				match(OC);
				{
				setState(249);
				select();
				setState(254);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(250);
					match(COMMA);
					setState(251);
					select();
					}
					}
					setState(256);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				setState(257);
				match(CC);
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
		enterRule(_localctx, 34, RULE_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(261);
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
		public TerminalNode DESCRIPTION() { return getToken(IMQParser.DESCRIPTION, 0); }
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
		enterRule(_localctx, 36, RULE_description);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(263);
			match(DESCRIPTION);
			setState(264);
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
		enterRule(_localctx, 38, RULE_activeOnly);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(266);
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
		public FromContext from() {
			return getRuleContext(FromContext.class,0);
		}
		public BooleanFromContext booleanFrom() {
			return getRuleContext(BooleanFromContext.class,0);
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
		enterRule(_localctx, 40, RULE_fromClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(268);
			match(FROM);
			setState(271);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				setState(269);
				from();
				}
				break;
			case 2:
				{
				setState(270);
				booleanFrom();
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
	public static class BooleanFromContext extends ParserRuleContext {
		public AndFromContext andFrom() {
			return getRuleContext(AndFromContext.class,0);
		}
		public OrFromContext orFrom() {
			return getRuleContext(OrFromContext.class,0);
		}
		public NotFromContext notFrom() {
			return getRuleContext(NotFromContext.class,0);
		}
		public BooleanFromContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanFrom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterBooleanFrom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitBooleanFrom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitBooleanFrom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanFromContext booleanFrom() throws RecognitionException {
		BooleanFromContext _localctx = new BooleanFromContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_booleanFrom);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(276);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(273);
				andFrom();
				}
				break;
			case 2:
				{
				setState(274);
				orFrom();
				}
				break;
			case 3:
				{
				setState(275);
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
		public TerminalNode NOT() { return getToken(IMQParser.NOT, 0); }
		public FromContext from() {
			return getRuleContext(FromContext.class,0);
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
		enterRule(_localctx, 44, RULE_notFrom);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(278);
			match(NOT);
			{
			setState(279);
			from();
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
	public static class OrFromContext extends ParserRuleContext {
		public List<FromContext> from() {
			return getRuleContexts(FromContext.class);
		}
		public FromContext from(int i) {
			return getRuleContext(FromContext.class,i);
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
		enterRule(_localctx, 46, RULE_orFrom);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(281);
			from();
			}
			setState(284); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(282);
				match(OR);
				{
				setState(283);
				from();
				}
				}
				}
				setState(286); 
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
	public static class AndFromContext extends ParserRuleContext {
		public List<FromContext> from() {
			return getRuleContexts(FromContext.class);
		}
		public FromContext from(int i) {
			return getRuleContext(FromContext.class,i);
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
		enterRule(_localctx, 48, RULE_andFrom);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(288);
			from();
			}
			setState(291); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(289);
				match(AND);
				{
				setState(290);
				from();
				}
				}
				}
				setState(293); 
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
	public static class FromContext extends ParserRuleContext {
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public GraphContext graph() {
			return getRuleContext(GraphContext.class,0);
		}
		public ReferenceContext reference() {
			return getRuleContext(ReferenceContext.class,0);
		}
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public BooleanFromContext booleanFrom() {
			return getRuleContext(BooleanFromContext.class,0);
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
		enterRule(_localctx, 50, RULE_from);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(295);
			match(OC);
			setState(297);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DESCRIPTION) {
				{
				setState(296);
				description();
				}
			}

			setState(300);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==GRAPH) {
				{
				setState(299);
				graph();
				}
			}

			setState(304);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
			case T__4:
			case T__5:
			case T__6:
			case T__7:
			case T__8:
			case T__9:
			case LT:
			case IRI_REF:
			case PN_PREFIXED:
			case PN_VARIABLE:
				{
				setState(302);
				reference();
				}
				break;
			case NOT:
			case OC:
				{
				{
				setState(303);
				booleanFrom();
				}
				}
				break;
			case WHERE:
			case CC:
				break;
			default:
				break;
			}
			setState(307);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(306);
				whereClause();
				}
			}

			setState(309);
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
	public static class WhereClauseContext extends ParserRuleContext {
		public TerminalNode WHERE() { return getToken(IMQParser.WHERE, 0); }
		public WhereContext where() {
			return getRuleContext(WhereContext.class,0);
		}
		public BooleanWhereContext booleanWhere() {
			return getRuleContext(BooleanWhereContext.class,0);
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
		enterRule(_localctx, 52, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(311);
			match(WHERE);
			setState(314);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				{
				setState(312);
				where();
				}
				break;
			case 2:
				{
				setState(313);
				booleanWhere();
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
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public ReferenceContext reference() {
			return getRuleContext(ReferenceContext.class,0);
		}
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public WithContext with() {
			return getRuleContext(WithContext.class,0);
		}
		public WhereValueTestContext whereValueTest() {
			return getRuleContext(WhereValueTestContext.class,0);
		}
		public BooleanWhereContext booleanWhere() {
			return getRuleContext(BooleanWhereContext.class,0);
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
		enterRule(_localctx, 54, RULE_where);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(316);
			match(OC);
			setState(318);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DESCRIPTION) {
				{
				setState(317);
				description();
				}
			}

			setState(331);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				{
				setState(320);
				reference();
				setState(321);
				whereClause();
				}
				}
				break;
			case 2:
				{
				{
				setState(323);
				reference();
				setState(324);
				with();
				setState(325);
				whereClause();
				}
				}
				break;
			case 3:
				{
				{
				setState(327);
				reference();
				setState(328);
				whereValueTest();
				}
				}
				break;
			case 4:
				{
				{
				setState(330);
				booleanWhere();
				}
				}
				break;
			}
			setState(333);
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
	public static class BooleanWhereContext extends ParserRuleContext {
		public AndWhereContext andWhere() {
			return getRuleContext(AndWhereContext.class,0);
		}
		public OrWhereContext orWhere() {
			return getRuleContext(OrWhereContext.class,0);
		}
		public NotWhereContext notWhere() {
			return getRuleContext(NotWhereContext.class,0);
		}
		public BooleanWhereContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanWhere; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterBooleanWhere(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitBooleanWhere(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitBooleanWhere(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanWhereContext booleanWhere() throws RecognitionException {
		BooleanWhereContext _localctx = new BooleanWhereContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_booleanWhere);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(338);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				{
				setState(335);
				andWhere();
				}
				break;
			case 2:
				{
				setState(336);
				orWhere();
				}
				break;
			case 3:
				{
				setState(337);
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
		public TerminalNode NOT() { return getToken(IMQParser.NOT, 0); }
		public WhereContext where() {
			return getRuleContext(WhereContext.class,0);
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
		enterRule(_localctx, 58, RULE_notWhere);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(340);
			match(NOT);
			setState(341);
			where();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 60, RULE_orWhere);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(343);
			where();
			}
			setState(346); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(344);
				match(OR);
				{
				setState(345);
				where();
				}
				}
				}
				setState(348); 
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
		enterRule(_localctx, 62, RULE_andWhere);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(350);
			where();
			}
			setState(353); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(351);
				match(AND);
				{
				setState(352);
				where();
				}
				}
				}
				setState(355); 
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
	public static class WithContext extends ParserRuleContext {
		public TerminalNode WITH() { return getToken(IMQParser.WITH, 0); }
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
		public SortableContext sortable() {
			return getRuleContext(SortableContext.class,0);
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
		enterRule(_localctx, 64, RULE_with);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(357);
			match(WITH);
			setState(358);
			match(OC);
			setState(359);
			whereClause();
			setState(361);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la - 56)) & ~0x3f) == 0 && ((1L << (_la - 56)) & 385L) != 0) {
				{
				setState(360);
				sortable();
				}
			}

			setState(363);
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
		public RangeContext range() {
			return getRuleContext(RangeContext.class,0);
		}
		public WhereMeasureContext whereMeasure() {
			return getRuleContext(WhereMeasureContext.class,0);
		}
		public ValueLabelContext valueLabel() {
			return getRuleContext(ValueLabelContext.class,0);
		}
		public InClauseContext inClause() {
			return getRuleContext(InClauseContext.class,0);
		}
		public NotInClauseContext notInClause() {
			return getRuleContext(NotInClauseContext.class,0);
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
		enterRule(_localctx, 66, RULE_whereValueTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(371);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IN:
			case NOTIN:
				{
				setState(367);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IN:
					{
					setState(365);
					inClause();
					}
					break;
				case NOTIN:
					{
					setState(366);
					notInClause();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case RANGE:
			case FROM:
			case TO:
				{
				setState(369);
				range();
				}
				break;
			case EQ:
			case GT:
			case LT:
			case GTE:
			case LTE:
			case STARTS_WITH:
				{
				setState(370);
				whereMeasure();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(374);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(373);
				valueLabel();
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
	public static class ValueLabelContext extends ParserRuleContext {
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
		enterRule(_localctx, 68, RULE_valueLabel);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(376);
			match(T__1);
			setState(377);
			string();
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
	public static class InClauseContext extends ParserRuleContext {
		public TerminalNode IN() { return getToken(IMQParser.IN, 0); }
		public List<ReferenceContext> reference() {
			return getRuleContexts(ReferenceContext.class);
		}
		public ReferenceContext reference(int i) {
			return getRuleContext(ReferenceContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(IMQParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(IMQParser.COMMA, i);
		}
		public InClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterInClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitInClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitInClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InClauseContext inClause() throws RecognitionException {
		InClauseContext _localctx = new InClauseContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_inClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(379);
			match(IN);
			{
			setState(380);
			reference();
			setState(385);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(381);
				match(COMMA);
				setState(382);
				reference();
				}
				}
				setState(387);
				_errHandler.sync(this);
				_la = _input.LA(1);
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
	public static class NotInClauseContext extends ParserRuleContext {
		public TerminalNode NOTIN() { return getToken(IMQParser.NOTIN, 0); }
		public List<ReferenceContext> reference() {
			return getRuleContexts(ReferenceContext.class);
		}
		public ReferenceContext reference(int i) {
			return getRuleContext(ReferenceContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(IMQParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(IMQParser.COMMA, i);
		}
		public NotInClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notInClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterNotInClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitNotInClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitNotInClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NotInClauseContext notInClause() throws RecognitionException {
		NotInClauseContext _localctx = new NotInClauseContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_notInClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(388);
			match(NOTIN);
			{
			setState(389);
			reference();
			setState(394);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(390);
				match(COMMA);
				setState(391);
				reference();
				}
				}
				setState(396);
				_errHandler.sync(this);
				_la = _input.LA(1);
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
	public static class ReferenceContext extends ParserRuleContext {
		public InverseOfContext inverseOf() {
			return getRuleContext(InverseOfContext.class,0);
		}
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
		enterRule(_localctx, 74, RULE_reference);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(398);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(397);
				inverseOf();
				}
			}

			setState(401);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((_la) & ~0x3f) == 0 && ((1L << _la) & 2199023257568L) != 0) {
				{
				setState(400);
				sourceType();
				}
			}

			setState(408);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRI_REF:
			case PN_PREFIXED:
				{
				{
				setState(403);
				_la = _input.LA(1);
				if ( !(_la==IRI_REF || _la==PN_PREFIXED) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(405);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAME) {
					{
					setState(404);
					name();
					}
				}

				}
				}
				break;
			case PN_VARIABLE:
				{
				{
				setState(407);
				variable();
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(411);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ALIAS) {
				{
				setState(410);
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
	public static class InverseOfContext extends ParserRuleContext {
		public InverseOfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inverseOf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterInverseOf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitInverseOf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitInverseOf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InverseOfContext inverseOf() throws RecognitionException {
		InverseOfContext _localctx = new InverseOfContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_inverseOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(413);
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
	public static class RangeContext extends ParserRuleContext {
		public TerminalNode RANGE() { return getToken(IMQParser.RANGE, 0); }
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
		public FromRangeContext fromRange() {
			return getRuleContext(FromRangeContext.class,0);
		}
		public ToRangeContext toRange() {
			return getRuleContext(ToRangeContext.class,0);
		}
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
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
		enterRule(_localctx, 78, RULE_range);
		try {
			setState(424);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case RANGE:
				enterOuterAlt(_localctx, 1);
				{
				setState(415);
				match(RANGE);
				setState(416);
				match(OC);
				{
				setState(417);
				fromRange();
				setState(418);
				toRange();
				}
				}
				break;
			case FROM:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(420);
				fromRange();
				}
				}
				break;
			case TO:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(421);
				toRange();
				}
				setState(422);
				match(CC);
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
	public static class FromRangeContext extends ParserRuleContext {
		public TerminalNode FROM() { return getToken(IMQParser.FROM, 0); }
		public WhereMeasureContext whereMeasure() {
			return getRuleContext(WhereMeasureContext.class,0);
		}
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
		enterRule(_localctx, 80, RULE_fromRange);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(426);
			match(FROM);
			setState(427);
			whereMeasure();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public WhereMeasureContext whereMeasure() {
			return getRuleContext(WhereMeasureContext.class,0);
		}
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
		enterRule(_localctx, 82, RULE_toRange);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(429);
			match(TO);
			setState(430);
			whereMeasure();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public UnitsContext units() {
			return getRuleContext(UnitsContext.class,0);
		}
		public RelativeToContext relativeTo() {
			return getRuleContext(RelativeToContext.class,0);
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
		enterRule(_localctx, 84, RULE_whereMeasure);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(432);
			operator();
			setState(435);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING_LITERAL1:
			case STRING_LITERAL2:
				{
				setState(433);
				string();
				}
				break;
			case SIGNED:
			case FLOAT:
			case INTEGER:
				{
				setState(434);
				number();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(438);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PN_PROPERTY) {
				{
				setState(437);
				units();
				}
			}

			setState(441);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(440);
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
	public static class NumberContext extends ParserRuleContext {
		public TerminalNode SIGNED() { return getToken(IMQParser.SIGNED, 0); }
		public TerminalNode INTEGER() { return getToken(IMQParser.INTEGER, 0); }
		public TerminalNode FLOAT() { return getToken(IMQParser.FLOAT, 0); }
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_number);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(443);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 14336L) != 0) ) {
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
		enterRule(_localctx, 88, RULE_relativeTo);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(445);
			match(T__3);
			setState(446);
			_la = _input.LA(1);
			if ( !(_la==PN_PROPERTY || _la==PN_VARIABLE) ) {
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
		enterRule(_localctx, 90, RULE_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(448);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 34634616274944L) != 0) ) {
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
		public TerminalNode PN_PROPERTY() { return getToken(IMQParser.PN_PROPERTY, 0); }
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
		enterRule(_localctx, 92, RULE_units);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(450);
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
	public static class SortableContext extends ParserRuleContext {
		public DirectionContext direction() {
			return getRuleContext(DirectionContext.class,0);
		}
		public CountContext count() {
			return getRuleContext(CountContext.class,0);
		}
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public TerminalNode PN_PROPERTY() { return getToken(IMQParser.PN_PROPERTY, 0); }
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
		enterRule(_localctx, 94, RULE_sortable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(454);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRI_REF:
			case PN_PREFIXED:
				{
				setState(452);
				iriRef();
				}
				break;
			case PN_PROPERTY:
				{
				setState(453);
				match(PN_PROPERTY);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(456);
			direction();
			setState(457);
			count();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DirectionContext extends ParserRuleContext {
		public TerminalNode ASCENDING() { return getToken(IMQParser.ASCENDING, 0); }
		public TerminalNode DESCENDING() { return getToken(IMQParser.DESCENDING, 0); }
		public DirectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_direction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterDirection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitDirection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitDirection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirectionContext direction() throws RecognitionException {
		DirectionContext _localctx = new DirectionContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_direction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(459);
			_la = _input.LA(1);
			if ( !(_la==ASCENDING || _la==DESCENDING) ) {
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
	public static class CountContext extends ParserRuleContext {
		public TerminalNode COUNT() { return getToken(IMQParser.COUNT, 0); }
		public TerminalNode INTEGER() { return getToken(IMQParser.INTEGER, 0); }
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
		enterRule(_localctx, 98, RULE_count);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(461);
			match(COUNT);
			setState(462);
			match(INTEGER);
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
		enterRule(_localctx, 100, RULE_graph);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(464);
			match(GRAPH);
			setState(465);
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
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public SetContext set() {
			return getRuleContext(SetContext.class,0);
		}
		public VarContext var() {
			return getRuleContext(VarContext.class,0);
		}
		public DescendantorselfofContext descendantorselfof() {
			return getRuleContext(DescendantorselfofContext.class,0);
		}
		public DescendantofContext descendantof() {
			return getRuleContext(DescendantofContext.class,0);
		}
		public AncestorOfContext ancestorOf() {
			return getRuleContext(AncestorOfContext.class,0);
		}
		public AncestorAndDescendantOfContext ancestorAndDescendantOf() {
			return getRuleContext(AncestorAndDescendantOfContext.class,0);
		}
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
		enterRule(_localctx, 102, RULE_sourceType);
		try {
			setState(474);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__4:
				enterOuterAlt(_localctx, 1);
				{
				setState(467);
				type();
				}
				break;
			case T__5:
				enterOuterAlt(_localctx, 2);
				{
				setState(468);
				set();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 3);
				{
				setState(469);
				var();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 4);
				{
				setState(470);
				descendantorselfof();
				}
				break;
			case LT:
				enterOuterAlt(_localctx, 5);
				{
				setState(471);
				descendantof();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 6);
				{
				setState(472);
				ancestorOf();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 7);
				{
				setState(473);
				ancestorAndDescendantOf();
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
	public static class TypeContext extends ParserRuleContext {
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(476);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SetContext extends ParserRuleContext {
		public SetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_set; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitSet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitSet(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetContext set() throws RecognitionException {
		SetContext _localctx = new SetContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_set);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(478);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarContext extends ParserRuleContext {
		public VarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_var; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterVar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitVar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarContext var() throws RecognitionException {
		VarContext _localctx = new VarContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_var);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(480);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AncestorAndDescendantOfContext extends ParserRuleContext {
		public AncestorAndDescendantOfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ancestorAndDescendantOf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterAncestorAndDescendantOf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitAncestorAndDescendantOf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitAncestorAndDescendantOf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AncestorAndDescendantOfContext ancestorAndDescendantOf() throws RecognitionException {
		AncestorAndDescendantOfContext _localctx = new AncestorAndDescendantOfContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_ancestorAndDescendantOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(482);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 112, RULE_ancestorOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(484);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 114, RULE_descendantof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(486);
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
		enterRule(_localctx, 116, RULE_descendantorselfof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(488);
			match(T__9);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 118, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(490);
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
		public TerminalNode PN_PROPERTY() { return getToken(IMQParser.PN_PROPERTY, 0); }
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
		enterRule(_localctx, 120, RULE_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(492);
			match(ALIAS);
			setState(493);
			match(COLON);
			setState(494);
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

	public static final String _serializedATN =
		"\u0004\u0001E\u01f1\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
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
		"<\u0007<\u0001\u0000\u0003\u0000|\b\u0000\u0001\u0000\u0003\u0000\u007f"+
		"\b\u0000\u0001\u0000\u0003\u0000\u0082\b\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002\u008f\b\u0002\n\u0002"+
		"\f\u0002\u0092\t\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0003\u0005\u009f\b\u0005\u0001\u0006\u0001\u0006\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0005\u0007\u00a6\b\u0007\n\u0007\f\u0007\u00a9"+
		"\t\u0007\u0001\b\u0001\b\u0001\b\u0005\b\u00ae\b\b\n\b\f\b\u00b1\t\b\u0001"+
		"\t\u0001\t\u0001\n\u0001\n\u0003\n\u00b7\b\n\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0003\u000b\u00bc\b\u000b\u0001\u000b\u0003\u000b\u00bf\b\u000b"+
		"\u0001\u000b\u0003\u000b\u00c2\b\u000b\u0001\u000b\u0003\u000b\u00c5\b"+
		"\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u00c9\b\u000b\u0001\u000b\u0005"+
		"\u000b\u00cc\b\u000b\n\u000b\f\u000b\u00cf\t\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0005"+
		"\r\u00db\b\r\n\r\f\r\u00de\t\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0005\u000f\u00eb\b\u000f\n\u000f\f\u000f\u00ee\t\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0003\u0010\u00f4\b\u0010\u0001"+
		"\u0010\u0003\u0010\u00f7\b\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0005\u0010\u00fd\b\u0010\n\u0010\f\u0010\u0100\t\u0010\u0001\u0010"+
		"\u0001\u0010\u0003\u0010\u0104\b\u0010\u0001\u0011\u0001\u0011\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0003\u0014\u0110\b\u0014\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0003\u0015\u0115\b\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017"+
		"\u0001\u0017\u0001\u0017\u0004\u0017\u011d\b\u0017\u000b\u0017\f\u0017"+
		"\u011e\u0001\u0018\u0001\u0018\u0001\u0018\u0004\u0018\u0124\b\u0018\u000b"+
		"\u0018\f\u0018\u0125\u0001\u0019\u0001\u0019\u0003\u0019\u012a\b\u0019"+
		"\u0001\u0019\u0003\u0019\u012d\b\u0019\u0001\u0019\u0001\u0019\u0003\u0019"+
		"\u0131\b\u0019\u0001\u0019\u0003\u0019\u0134\b\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0003\u001a\u013b\b\u001a\u0001"+
		"\u001b\u0001\u001b\u0003\u001b\u013f\b\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0003\u001b\u014c\b\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0003\u001c\u0153\b\u001c\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0004"+
		"\u001e\u015b\b\u001e\u000b\u001e\f\u001e\u015c\u0001\u001f\u0001\u001f"+
		"\u0001\u001f\u0004\u001f\u0162\b\u001f\u000b\u001f\f\u001f\u0163\u0001"+
		" \u0001 \u0001 \u0001 \u0003 \u016a\b \u0001 \u0001 \u0001!\u0001!\u0003"+
		"!\u0170\b!\u0001!\u0001!\u0003!\u0174\b!\u0001!\u0003!\u0177\b!\u0001"+
		"\"\u0001\"\u0001\"\u0001#\u0001#\u0001#\u0001#\u0005#\u0180\b#\n#\f#\u0183"+
		"\t#\u0001$\u0001$\u0001$\u0001$\u0005$\u0189\b$\n$\f$\u018c\t$\u0001%"+
		"\u0003%\u018f\b%\u0001%\u0003%\u0192\b%\u0001%\u0001%\u0003%\u0196\b%"+
		"\u0001%\u0003%\u0199\b%\u0001%\u0003%\u019c\b%\u0001&\u0001&\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0003\'\u01a9"+
		"\b\'\u0001(\u0001(\u0001(\u0001)\u0001)\u0001)\u0001*\u0001*\u0001*\u0003"+
		"*\u01b4\b*\u0001*\u0003*\u01b7\b*\u0001*\u0003*\u01ba\b*\u0001+\u0001"+
		"+\u0001,\u0001,\u0001,\u0001-\u0001-\u0001.\u0001.\u0001/\u0001/\u0003"+
		"/\u01c7\b/\u0001/\u0001/\u0001/\u00010\u00010\u00011\u00011\u00011\u0001"+
		"2\u00012\u00012\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u0003"+
		"3\u01db\b3\u00014\u00014\u00015\u00015\u00016\u00016\u00017\u00017\u0001"+
		"8\u00018\u00019\u00019\u0001:\u0001:\u0001;\u0001;\u0001<\u0001<\u0001"+
		"<\u0001<\u0001<\u0000\u0000=\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010"+
		"\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPR"+
		"TVXZ\\^`bdfhjlnprtvx\u0000\u0006\u0001\u00009:\u0002\u000088??\u0001\u0000"+
		"\u000b\r\u0001\u0000@A\u0001\u0000\',\u0001\u0000\u0019\u001a\u01f3\u0000"+
		"{\u0001\u0000\u0000\u0000\u0002\u0086\u0001\u0000\u0000\u0000\u0004\u0089"+
		"\u0001\u0000\u0000\u0000\u0006\u0095\u0001\u0000\u0000\u0000\b\u0097\u0001"+
		"\u0000\u0000\u0000\n\u0099\u0001\u0000\u0000\u0000\f\u00a0\u0001\u0000"+
		"\u0000\u0000\u000e\u00a2\u0001\u0000\u0000\u0000\u0010\u00aa\u0001\u0000"+
		"\u0000\u0000\u0012\u00b2\u0001\u0000\u0000\u0000\u0014\u00b4\u0001\u0000"+
		"\u0000\u0000\u0016\u00b8\u0001\u0000\u0000\u0000\u0018\u00d2\u0001\u0000"+
		"\u0000\u0000\u001a\u00d5\u0001\u0000\u0000\u0000\u001c\u00e1\u0001\u0000"+
		"\u0000\u0000\u001e\u00e5\u0001\u0000\u0000\u0000 \u00f3\u0001\u0000\u0000"+
		"\u0000\"\u0105\u0001\u0000\u0000\u0000$\u0107\u0001\u0000\u0000\u0000"+
		"&\u010a\u0001\u0000\u0000\u0000(\u010c\u0001\u0000\u0000\u0000*\u0114"+
		"\u0001\u0000\u0000\u0000,\u0116\u0001\u0000\u0000\u0000.\u0119\u0001\u0000"+
		"\u0000\u00000\u0120\u0001\u0000\u0000\u00002\u0127\u0001\u0000\u0000\u0000"+
		"4\u0137\u0001\u0000\u0000\u00006\u013c\u0001\u0000\u0000\u00008\u0152"+
		"\u0001\u0000\u0000\u0000:\u0154\u0001\u0000\u0000\u0000<\u0157\u0001\u0000"+
		"\u0000\u0000>\u015e\u0001\u0000\u0000\u0000@\u0165\u0001\u0000\u0000\u0000"+
		"B\u0173\u0001\u0000\u0000\u0000D\u0178\u0001\u0000\u0000\u0000F\u017b"+
		"\u0001\u0000\u0000\u0000H\u0184\u0001\u0000\u0000\u0000J\u018e\u0001\u0000"+
		"\u0000\u0000L\u019d\u0001\u0000\u0000\u0000N\u01a8\u0001\u0000\u0000\u0000"+
		"P\u01aa\u0001\u0000\u0000\u0000R\u01ad\u0001\u0000\u0000\u0000T\u01b0"+
		"\u0001\u0000\u0000\u0000V\u01bb\u0001\u0000\u0000\u0000X\u01bd\u0001\u0000"+
		"\u0000\u0000Z\u01c0\u0001\u0000\u0000\u0000\\\u01c2\u0001\u0000\u0000"+
		"\u0000^\u01c6\u0001\u0000\u0000\u0000`\u01cb\u0001\u0000\u0000\u0000b"+
		"\u01cd\u0001\u0000\u0000\u0000d\u01d0\u0001\u0000\u0000\u0000f\u01da\u0001"+
		"\u0000\u0000\u0000h\u01dc\u0001\u0000\u0000\u0000j\u01de\u0001\u0000\u0000"+
		"\u0000l\u01e0\u0001\u0000\u0000\u0000n\u01e2\u0001\u0000\u0000\u0000p"+
		"\u01e4\u0001\u0000\u0000\u0000r\u01e6\u0001\u0000\u0000\u0000t\u01e8\u0001"+
		"\u0000\u0000\u0000v\u01ea\u0001\u0000\u0000\u0000x\u01ec\u0001\u0000\u0000"+
		"\u0000z|\u0003\u001a\r\u0000{z\u0001\u0000\u0000\u0000{|\u0001\u0000\u0000"+
		"\u0000|~\u0001\u0000\u0000\u0000}\u007f\u0003\u0002\u0001\u0000~}\u0001"+
		"\u0000\u0000\u0000~\u007f\u0001\u0000\u0000\u0000\u007f\u0081\u0001\u0000"+
		"\u0000\u0000\u0080\u0082\u0003\u0004\u0002\u0000\u0081\u0080\u0001\u0000"+
		"\u0000\u0000\u0081\u0082\u0001\u0000\u0000\u0000\u0082\u0083\u0001\u0000"+
		"\u0000\u0000\u0083\u0084\u0003\u0016\u000b\u0000\u0084\u0085\u0005\u0000"+
		"\u0000\u0001\u0085\u0001\u0001\u0000\u0000\u0000\u0086\u0087\u0005\u000f"+
		"\u0000\u0000\u0087\u0088\u0007\u0000\u0000\u0000\u0088\u0003\u0001\u0000"+
		"\u0000\u0000\u0089\u008a\u0005\u0010\u0000\u0000\u008a\u008b\u00051\u0000"+
		"\u0000\u008b\u0090\u0003\n\u0005\u0000\u008c\u008d\u0005<\u0000\u0000"+
		"\u008d\u008f\u0003\n\u0005\u0000\u008e\u008c\u0001\u0000\u0000\u0000\u008f"+
		"\u0092\u0001\u0000\u0000\u0000\u0090\u008e\u0001\u0000\u0000\u0000\u0090"+
		"\u0091\u0001\u0000\u0000\u0000\u0091\u0093\u0001\u0000\u0000\u0000\u0092"+
		"\u0090\u0001\u0000\u0000\u0000\u0093\u0094\u00052\u0000\u0000\u0094\u0005"+
		"\u0001\u0000\u0000\u0000\u0095\u0096\u0007\u0000\u0000\u0000\u0096\u0007"+
		"\u0001\u0000\u0000\u0000\u0097\u0098\u0007\u0000\u0000\u0000\u0098\t\u0001"+
		"\u0000\u0000\u0000\u0099\u009a\u0003\f\u0006\u0000\u009a\u009e\u00057"+
		"\u0000\u0000\u009b\u009f\u0003\u0012\t\u0000\u009c\u009f\u0003\u000e\u0007"+
		"\u0000\u009d\u009f\u0003\u0010\b\u0000\u009e\u009b\u0001\u0000\u0000\u0000"+
		"\u009e\u009c\u0001\u0000\u0000\u0000\u009e\u009d\u0001\u0000\u0000\u0000"+
		"\u009f\u000b\u0001\u0000\u0000\u0000\u00a0\u00a1\u0005@\u0000\u0000\u00a1"+
		"\r\u0001\u0000\u0000\u0000\u00a2\u00a7\u0003\u0012\t\u0000\u00a3\u00a4"+
		"\u0005<\u0000\u0000\u00a4\u00a6\u0003\u0012\t\u0000\u00a5\u00a3\u0001"+
		"\u0000\u0000\u0000\u00a6\u00a9\u0001\u0000\u0000\u0000\u00a7\u00a5\u0001"+
		"\u0000\u0000\u0000\u00a7\u00a8\u0001\u0000\u0000\u0000\u00a8\u000f\u0001"+
		"\u0000\u0000\u0000\u00a9\u00a7\u0001\u0000\u0000\u0000\u00aa\u00af\u0003"+
		"\u0014\n\u0000\u00ab\u00ac\u0005<\u0000\u0000\u00ac\u00ae\u0003\u0014"+
		"\n\u0000\u00ad\u00ab\u0001\u0000\u0000\u0000\u00ae\u00b1\u0001\u0000\u0000"+
		"\u0000\u00af\u00ad\u0001\u0000\u0000\u0000\u00af\u00b0\u0001\u0000\u0000"+
		"\u0000\u00b0\u0011\u0001\u0000\u0000\u0000\u00b1\u00af\u0001\u0000\u0000"+
		"\u0000\u00b2\u00b3\u0003\b\u0004\u0000\u00b3\u0013\u0001\u0000\u0000\u0000"+
		"\u00b4\u00b6\u0007\u0001\u0000\u0000\u00b5\u00b7\u0003\"\u0011\u0000\u00b6"+
		"\u00b5\u0001\u0000\u0000\u0000\u00b6\u00b7\u0001\u0000\u0000\u0000\u00b7"+
		"\u0015\u0001\u0000\u0000\u0000\u00b8\u00b9\u0005\u0012\u0000\u0000\u00b9"+
		"\u00bb\u00051\u0000\u0000\u00ba\u00bc\u0003\u0014\n\u0000\u00bb\u00ba"+
		"\u0001\u0000\u0000\u0000\u00bb\u00bc\u0001\u0000\u0000\u0000\u00bc\u00be"+
		"\u0001\u0000\u0000\u0000\u00bd\u00bf\u0003\u0018\f\u0000\u00be\u00bd\u0001"+
		"\u0000\u0000\u0000\u00be\u00bf\u0001\u0000\u0000\u0000\u00bf\u00c1\u0001"+
		"\u0000\u0000\u0000\u00c0\u00c2\u0003$\u0012\u0000\u00c1\u00c0\u0001\u0000"+
		"\u0000\u0000\u00c1\u00c2\u0001\u0000\u0000\u0000\u00c2\u00c4\u0001\u0000"+
		"\u0000\u0000\u00c3\u00c5\u0003&\u0013\u0000\u00c4\u00c3\u0001\u0000\u0000"+
		"\u0000\u00c4\u00c5\u0001\u0000\u0000\u0000\u00c5\u00c6\u0001\u0000\u0000"+
		"\u0000\u00c6\u00c8\u0003(\u0014\u0000\u00c7\u00c9\u0003\u001e\u000f\u0000"+
		"\u00c8\u00c7\u0001\u0000\u0000\u0000\u00c8\u00c9\u0001\u0000\u0000\u0000"+
		"\u00c9\u00cd\u0001\u0000\u0000\u0000\u00ca\u00cc\u0003\u0016\u000b\u0000"+
		"\u00cb\u00ca\u0001\u0000\u0000\u0000\u00cc\u00cf\u0001\u0000\u0000\u0000"+
		"\u00cd\u00cb\u0001\u0000\u0000\u0000\u00cd\u00ce\u0001\u0000\u0000\u0000"+
		"\u00ce\u00d0\u0001\u0000\u0000\u0000\u00cf\u00cd\u0001\u0000\u0000\u0000"+
		"\u00d0\u00d1\u00052\u0000\u0000\u00d1\u0017\u0001\u0000\u0000\u0000\u00d2"+
		"\u00d3\u0005\u0001\u0000\u0000\u00d3\u00d4\u0003\b\u0004\u0000\u00d4\u0019"+
		"\u0001\u0000\u0000\u0000\u00d5\u00d6\u0005\u001d\u0000\u0000\u00d6\u00d7"+
		"\u00051\u0000\u0000\u00d7\u00dc\u0003\u001c\u000e\u0000\u00d8\u00d9\u0005"+
		"<\u0000\u0000\u00d9\u00db\u0003\u001c\u000e\u0000\u00da\u00d8\u0001\u0000"+
		"\u0000\u0000\u00db\u00de\u0001\u0000\u0000\u0000\u00dc\u00da\u0001\u0000"+
		"\u0000\u0000\u00dc\u00dd\u0001\u0000\u0000\u0000\u00dd\u00df\u0001\u0000"+
		"\u0000\u0000\u00de\u00dc\u0001\u0000\u0000\u0000\u00df\u00e0\u00052\u0000"+
		"\u0000\u00e0\u001b\u0001\u0000\u0000\u0000\u00e1\u00e2\u0005@\u0000\u0000"+
		"\u00e2\u00e3\u00057\u0000\u0000\u00e3\u00e4\u00058\u0000\u0000\u00e4\u001d"+
		"\u0001\u0000\u0000\u0000\u00e5\u00e6\u0005\u0018\u0000\u0000\u00e6\u00e7"+
		"\u00051\u0000\u0000\u00e7\u00ec\u0003 \u0010\u0000\u00e8\u00e9\u0005<"+
		"\u0000\u0000\u00e9\u00eb\u0003 \u0010\u0000\u00ea\u00e8\u0001\u0000\u0000"+
		"\u0000\u00eb\u00ee\u0001\u0000\u0000\u0000\u00ec\u00ea\u0001\u0000\u0000"+
		"\u0000\u00ec\u00ed\u0001\u0000\u0000\u0000\u00ed\u00ef\u0001\u0000\u0000"+
		"\u0000\u00ee\u00ec\u0001\u0000\u0000\u0000\u00ef\u00f0\u00052\u0000\u0000"+
		"\u00f0\u001f\u0001\u0000\u0000\u0000\u00f1\u00f4\u0003\u0014\n\u0000\u00f2"+
		"\u00f4\u0005@\u0000\u0000\u00f3\u00f1\u0001\u0000\u0000\u0000\u00f3\u00f2"+
		"\u0001\u0000\u0000\u0000\u00f4\u00f6\u0001\u0000\u0000\u0000\u00f5\u00f7"+
		"\u00034\u001a\u0000\u00f6\u00f5\u0001\u0000\u0000\u0000\u00f6\u00f7\u0001"+
		"\u0000\u0000\u0000\u00f7\u0103\u0001\u0000\u0000\u0000\u00f8\u00f9\u0005"+
		"1\u0000\u0000\u00f9\u00fe\u0003 \u0010\u0000\u00fa\u00fb\u0005<\u0000"+
		"\u0000\u00fb\u00fd\u0003 \u0010\u0000\u00fc\u00fa\u0001\u0000\u0000\u0000"+
		"\u00fd\u0100\u0001\u0000\u0000\u0000\u00fe\u00fc\u0001\u0000\u0000\u0000"+
		"\u00fe\u00ff\u0001\u0000\u0000\u0000\u00ff\u0101\u0001\u0000\u0000\u0000"+
		"\u0100\u00fe\u0001\u0000\u0000\u0000\u0101\u0102\u00052\u0000\u0000\u0102"+
		"\u0104\u0001\u0000\u0000\u0000\u0103\u00f8\u0001\u0000\u0000\u0000\u0103"+
		"\u0104\u0001\u0000\u0000\u0000\u0104!\u0001\u0000\u0000\u0000\u0105\u0106"+
		"\u0005 \u0000\u0000\u0106#\u0001\u0000\u0000\u0000\u0107\u0108\u0005\u001f"+
		"\u0000\u0000\u0108\u0109\u0003\b\u0004\u0000\u0109%\u0001\u0000\u0000"+
		"\u0000\u010a\u010b\u0005\"\u0000\u0000\u010b\'\u0001\u0000\u0000\u0000"+
		"\u010c\u010f\u0005\u0013\u0000\u0000\u010d\u0110\u00032\u0019\u0000\u010e"+
		"\u0110\u0003*\u0015\u0000\u010f\u010d\u0001\u0000\u0000\u0000\u010f\u010e"+
		"\u0001\u0000\u0000\u0000\u0110)\u0001\u0000\u0000\u0000\u0111\u0115\u0003"+
		"0\u0018\u0000\u0112\u0115\u0003.\u0017\u0000\u0113\u0115\u0003,\u0016"+
		"\u0000\u0114\u0111\u0001\u0000\u0000\u0000\u0114\u0112\u0001\u0000\u0000"+
		"\u0000\u0114\u0113\u0001\u0000\u0000\u0000\u0115+\u0001\u0000\u0000\u0000"+
		"\u0116\u0117\u0005/\u0000\u0000\u0117\u0118\u00032\u0019\u0000\u0118-"+
		"\u0001\u0000\u0000\u0000\u0119\u011c\u00032\u0019\u0000\u011a\u011b\u0005"+
		".\u0000\u0000\u011b\u011d\u00032\u0019\u0000\u011c\u011a\u0001\u0000\u0000"+
		"\u0000\u011d\u011e\u0001\u0000\u0000\u0000\u011e\u011c\u0001\u0000\u0000"+
		"\u0000\u011e\u011f\u0001\u0000\u0000\u0000\u011f/\u0001\u0000\u0000\u0000"+
		"\u0120\u0123\u00032\u0019\u0000\u0121\u0122\u0005-\u0000\u0000\u0122\u0124"+
		"\u00032\u0019\u0000\u0123\u0121\u0001\u0000\u0000\u0000\u0124\u0125\u0001"+
		"\u0000\u0000\u0000\u0125\u0123\u0001\u0000\u0000\u0000\u0125\u0126\u0001"+
		"\u0000\u0000\u0000\u01261\u0001\u0000\u0000\u0000\u0127\u0129\u00051\u0000"+
		"\u0000\u0128\u012a\u0003$\u0012\u0000\u0129\u0128\u0001\u0000\u0000\u0000"+
		"\u0129\u012a\u0001\u0000\u0000\u0000\u012a\u012c\u0001\u0000\u0000\u0000"+
		"\u012b\u012d\u0003d2\u0000\u012c\u012b\u0001\u0000\u0000\u0000\u012c\u012d"+
		"\u0001\u0000\u0000\u0000\u012d\u0130\u0001\u0000\u0000\u0000\u012e\u0131"+
		"\u0003J%\u0000\u012f\u0131\u0003*\u0015\u0000\u0130\u012e\u0001\u0000"+
		"\u0000\u0000\u0130\u012f\u0001\u0000\u0000\u0000\u0130\u0131\u0001\u0000"+
		"\u0000\u0000\u0131\u0133\u0001\u0000\u0000\u0000\u0132\u0134\u00034\u001a"+
		"\u0000\u0133\u0132\u0001\u0000\u0000\u0000\u0133\u0134\u0001\u0000\u0000"+
		"\u0000\u0134\u0135\u0001\u0000\u0000\u0000\u0135\u0136\u00052\u0000\u0000"+
		"\u01363\u0001\u0000\u0000\u0000\u0137\u013a\u0005\u0015\u0000\u0000\u0138"+
		"\u013b\u00036\u001b\u0000\u0139\u013b\u00038\u001c\u0000\u013a\u0138\u0001"+
		"\u0000\u0000\u0000\u013a\u0139\u0001\u0000\u0000\u0000\u013b5\u0001\u0000"+
		"\u0000\u0000\u013c\u013e\u00051\u0000\u0000\u013d\u013f\u0003$\u0012\u0000"+
		"\u013e\u013d\u0001\u0000\u0000\u0000\u013e\u013f\u0001\u0000\u0000\u0000"+
		"\u013f\u014b\u0001\u0000\u0000\u0000\u0140\u0141\u0003J%\u0000\u0141\u0142"+
		"\u00034\u001a\u0000\u0142\u014c\u0001\u0000\u0000\u0000\u0143\u0144\u0003"+
		"J%\u0000\u0144\u0145\u0003@ \u0000\u0145\u0146\u00034\u001a\u0000\u0146"+
		"\u014c\u0001\u0000\u0000\u0000\u0147\u0148\u0003J%\u0000\u0148\u0149\u0003"+
		"B!\u0000\u0149\u014c\u0001\u0000\u0000\u0000\u014a\u014c\u00038\u001c"+
		"\u0000\u014b\u0140\u0001\u0000\u0000\u0000\u014b\u0143\u0001\u0000\u0000"+
		"\u0000\u014b\u0147\u0001\u0000\u0000\u0000\u014b\u014a\u0001\u0000\u0000"+
		"\u0000\u014c\u014d\u0001\u0000\u0000\u0000\u014d\u014e\u00052\u0000\u0000"+
		"\u014e7\u0001\u0000\u0000\u0000\u014f\u0153\u0003>\u001f\u0000\u0150\u0153"+
		"\u0003<\u001e\u0000\u0151\u0153\u0003:\u001d\u0000\u0152\u014f\u0001\u0000"+
		"\u0000\u0000\u0152\u0150\u0001\u0000\u0000\u0000\u0152\u0151\u0001\u0000"+
		"\u0000\u0000\u01539\u0001\u0000\u0000\u0000\u0154\u0155\u0005/\u0000\u0000"+
		"\u0155\u0156\u00036\u001b\u0000\u0156;\u0001\u0000\u0000\u0000\u0157\u015a"+
		"\u00036\u001b\u0000\u0158\u0159\u0005.\u0000\u0000\u0159\u015b\u00036"+
		"\u001b\u0000\u015a\u0158\u0001\u0000\u0000\u0000\u015b\u015c\u0001\u0000"+
		"\u0000\u0000\u015c\u015a\u0001\u0000\u0000\u0000\u015c\u015d\u0001\u0000"+
		"\u0000\u0000\u015d=\u0001\u0000\u0000\u0000\u015e\u0161\u00036\u001b\u0000"+
		"\u015f\u0160\u0005-\u0000\u0000\u0160\u0162\u00036\u001b\u0000\u0161\u015f"+
		"\u0001\u0000\u0000\u0000\u0162\u0163\u0001\u0000\u0000\u0000\u0163\u0161"+
		"\u0001\u0000\u0000\u0000\u0163\u0164\u0001\u0000\u0000\u0000\u0164?\u0001"+
		"\u0000\u0000\u0000\u0165\u0166\u0005\u0017\u0000\u0000\u0166\u0167\u0005"+
		"1\u0000\u0000\u0167\u0169\u00034\u001a\u0000\u0168\u016a\u0003^/\u0000"+
		"\u0169\u0168\u0001\u0000\u0000\u0000\u0169\u016a\u0001\u0000\u0000\u0000"+
		"\u016a\u016b\u0001\u0000\u0000\u0000\u016b\u016c\u00052\u0000\u0000\u016c"+
		"A\u0001\u0000\u0000\u0000\u016d\u0170\u0003F#\u0000\u016e\u0170\u0003"+
		"H$\u0000\u016f\u016d\u0001\u0000\u0000\u0000\u016f\u016e\u0001\u0000\u0000"+
		"\u0000\u0170\u0174\u0001\u0000\u0000\u0000\u0171\u0174\u0003N\'\u0000"+
		"\u0172\u0174\u0003T*\u0000\u0173\u016f\u0001\u0000\u0000\u0000\u0173\u0171"+
		"\u0001\u0000\u0000\u0000\u0173\u0172\u0001\u0000\u0000\u0000\u0174\u0176"+
		"\u0001\u0000\u0000\u0000\u0175\u0177\u0003D\"\u0000\u0176\u0175\u0001"+
		"\u0000\u0000\u0000\u0176\u0177\u0001\u0000\u0000\u0000\u0177C\u0001\u0000"+
		"\u0000\u0000\u0178\u0179\u0005\u0002\u0000\u0000\u0179\u017a\u0003\b\u0004"+
		"\u0000\u017aE\u0001\u0000\u0000\u0000\u017b\u017c\u0005#\u0000\u0000\u017c"+
		"\u0181\u0003J%\u0000\u017d\u017e\u0005<\u0000\u0000\u017e\u0180\u0003"+
		"J%\u0000\u017f\u017d\u0001\u0000\u0000\u0000\u0180\u0183\u0001\u0000\u0000"+
		"\u0000\u0181\u017f\u0001\u0000\u0000\u0000\u0181\u0182\u0001\u0000\u0000"+
		"\u0000\u0182G\u0001\u0000\u0000\u0000\u0183\u0181\u0001\u0000\u0000\u0000"+
		"\u0184\u0185\u0005D\u0000\u0000\u0185\u018a\u0003J%\u0000\u0186\u0187"+
		"\u0005<\u0000\u0000\u0187\u0189\u0003J%\u0000\u0188\u0186\u0001\u0000"+
		"\u0000\u0000\u0189\u018c\u0001\u0000\u0000\u0000\u018a\u0188\u0001\u0000"+
		"\u0000\u0000\u018a\u018b\u0001\u0000\u0000\u0000\u018bI\u0001\u0000\u0000"+
		"\u0000\u018c\u018a\u0001\u0000\u0000\u0000\u018d\u018f\u0003L&\u0000\u018e"+
		"\u018d\u0001\u0000\u0000\u0000\u018e\u018f\u0001\u0000\u0000\u0000\u018f"+
		"\u0191\u0001\u0000\u0000\u0000\u0190\u0192\u0003f3\u0000\u0191\u0190\u0001"+
		"\u0000\u0000\u0000\u0191\u0192\u0001\u0000\u0000\u0000\u0192\u0198\u0001"+
		"\u0000\u0000\u0000\u0193\u0195\u0007\u0001\u0000\u0000\u0194\u0196\u0003"+
		"\"\u0011\u0000\u0195\u0194\u0001\u0000\u0000\u0000\u0195\u0196\u0001\u0000"+
		"\u0000\u0000\u0196\u0199\u0001\u0000\u0000\u0000\u0197\u0199\u0003v;\u0000"+
		"\u0198\u0193\u0001\u0000\u0000\u0000\u0198\u0197\u0001\u0000\u0000\u0000"+
		"\u0199\u019b\u0001\u0000\u0000\u0000\u019a\u019c\u0003x<\u0000\u019b\u019a"+
		"\u0001\u0000\u0000\u0000\u019b\u019c\u0001\u0000\u0000\u0000\u019cK\u0001"+
		"\u0000\u0000\u0000\u019d\u019e\u0005\u0003\u0000\u0000\u019eM\u0001\u0000"+
		"\u0000\u0000\u019f\u01a0\u0005\u0011\u0000\u0000\u01a0\u01a1\u00051\u0000"+
		"\u0000\u01a1\u01a2\u0003P(\u0000\u01a2\u01a3\u0003R)\u0000\u01a3\u01a9"+
		"\u0001\u0000\u0000\u0000\u01a4\u01a9\u0003P(\u0000\u01a5\u01a6\u0003R"+
		")\u0000\u01a6\u01a7\u00052\u0000\u0000\u01a7\u01a9\u0001\u0000\u0000\u0000"+
		"\u01a8\u019f\u0001\u0000\u0000\u0000\u01a8\u01a4\u0001\u0000\u0000\u0000"+
		"\u01a8\u01a5\u0001\u0000\u0000\u0000\u01a9O\u0001\u0000\u0000\u0000\u01aa"+
		"\u01ab\u0005\u0013\u0000\u0000\u01ab\u01ac\u0003T*\u0000\u01acQ\u0001"+
		"\u0000\u0000\u0000\u01ad\u01ae\u00050\u0000\u0000\u01ae\u01af\u0003T*"+
		"\u0000\u01afS\u0001\u0000\u0000\u0000\u01b0\u01b3\u0003Z-\u0000\u01b1"+
		"\u01b4\u0003\b\u0004\u0000\u01b2\u01b4\u0003V+\u0000\u01b3\u01b1\u0001"+
		"\u0000\u0000\u0000\u01b3\u01b2\u0001\u0000\u0000\u0000\u01b4\u01b6\u0001"+
		"\u0000\u0000\u0000\u01b5\u01b7\u0003\\.\u0000\u01b6\u01b5\u0001\u0000"+
		"\u0000\u0000\u01b6\u01b7\u0001\u0000\u0000\u0000\u01b7\u01b9\u0001\u0000"+
		"\u0000\u0000\u01b8\u01ba\u0003X,\u0000\u01b9\u01b8\u0001\u0000\u0000\u0000"+
		"\u01b9\u01ba\u0001\u0000\u0000\u0000\u01baU\u0001\u0000\u0000\u0000\u01bb"+
		"\u01bc\u0007\u0002\u0000\u0000\u01bcW\u0001\u0000\u0000\u0000\u01bd\u01be"+
		"\u0005\u0004\u0000\u0000\u01be\u01bf\u0007\u0003\u0000\u0000\u01bfY\u0001"+
		"\u0000\u0000\u0000\u01c0\u01c1\u0007\u0004\u0000\u0000\u01c1[\u0001\u0000"+
		"\u0000\u0000\u01c2\u01c3\u0005@\u0000\u0000\u01c3]\u0001\u0000\u0000\u0000"+
		"\u01c4\u01c7\u0003\u0014\n\u0000\u01c5\u01c7\u0005@\u0000\u0000\u01c6"+
		"\u01c4\u0001\u0000\u0000\u0000\u01c6\u01c5\u0001\u0000\u0000\u0000\u01c7"+
		"\u01c8\u0001\u0000\u0000\u0000\u01c8\u01c9\u0003`0\u0000\u01c9\u01ca\u0003"+
		"b1\u0000\u01ca_\u0001\u0000\u0000\u0000\u01cb\u01cc\u0007\u0005\u0000"+
		"\u0000\u01cca\u0001\u0000\u0000\u0000\u01cd\u01ce\u0005\u001b\u0000\u0000"+
		"\u01ce\u01cf\u0005\r\u0000\u0000\u01cfc\u0001\u0000\u0000\u0000\u01d0"+
		"\u01d1\u0005\u0014\u0000\u0000\u01d1\u01d2\u00058\u0000\u0000\u01d2e\u0001"+
		"\u0000\u0000\u0000\u01d3\u01db\u0003h4\u0000\u01d4\u01db\u0003j5\u0000"+
		"\u01d5\u01db\u0003l6\u0000\u01d6\u01db\u0003t:\u0000\u01d7\u01db\u0003"+
		"r9\u0000\u01d8\u01db\u0003p8\u0000\u01d9\u01db\u0003n7\u0000\u01da\u01d3"+
		"\u0001\u0000\u0000\u0000\u01da\u01d4\u0001\u0000\u0000\u0000\u01da\u01d5"+
		"\u0001\u0000\u0000\u0000\u01da\u01d6\u0001\u0000\u0000\u0000\u01da\u01d7"+
		"\u0001\u0000\u0000\u0000\u01da\u01d8\u0001\u0000\u0000\u0000\u01da\u01d9"+
		"\u0001\u0000\u0000\u0000\u01dbg\u0001\u0000\u0000\u0000\u01dc\u01dd\u0005"+
		"\u0005\u0000\u0000\u01ddi\u0001\u0000\u0000\u0000\u01de\u01df\u0005\u0006"+
		"\u0000\u0000\u01dfk\u0001\u0000\u0000\u0000\u01e0\u01e1\u0005\u0007\u0000"+
		"\u0000\u01e1m\u0001\u0000\u0000\u0000\u01e2\u01e3\u0005\b\u0000\u0000"+
		"\u01e3o\u0001\u0000\u0000\u0000\u01e4\u01e5\u0005\t\u0000\u0000\u01e5"+
		"q\u0001\u0000\u0000\u0000\u01e6\u01e7\u0005)\u0000\u0000\u01e7s\u0001"+
		"\u0000\u0000\u0000\u01e8\u01e9\u0005\n\u0000\u0000\u01e9u\u0001\u0000"+
		"\u0000\u0000\u01ea\u01eb\u0005A\u0000\u0000\u01ebw\u0001\u0000\u0000\u0000"+
		"\u01ec\u01ed\u0005!\u0000\u0000\u01ed\u01ee\u00057\u0000\u0000\u01ee\u01ef"+
		"\u0005@\u0000\u0000\u01efy\u0001\u0000\u0000\u00003{~\u0081\u0090\u009e"+
		"\u00a7\u00af\u00b6\u00bb\u00be\u00c1\u00c4\u00c8\u00cd\u00dc\u00ec\u00f3"+
		"\u00f6\u00fe\u0103\u010f\u0114\u011e\u0125\u0129\u012c\u0130\u0133\u013a"+
		"\u013e\u014b\u0152\u015c\u0163\u0169\u016f\u0173\u0176\u0181\u018a\u018e"+
		"\u0191\u0195\u0198\u019b\u01a8\u01b3\u01b6\u01b9\u01c6\u01da";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}