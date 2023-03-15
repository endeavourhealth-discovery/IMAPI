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
		COMMENT=30, DESCRIPTION=31, ALIAS=32, ACTIVE_ONLY=33, IN=34, TYPE=35, 
		SET=36, INSTANCE=37, EQ=38, GT=39, LT=40, GTE=41, LTE=42, STARTS_WITH=43, 
		UNITS=44, AND=45, OR=46, NOT=47, TO=48, OC=49, CC=50, OSB=51, CSB=52, 
		OB=53, CB=54, COLON=55, IRI_REF=56, STRING_LITERAL1=57, STRING_LITERAL2=58, 
		NAME=59, PN_CHARS_U=60, COMMA=61, WS=62, DELIM=63, PN_PREFIXED=64, PN_PROPERTY=65, 
		PN_VARIABLE=66, PN_CHARS_BASE=67, PN_CHARS=68, NOTIN=69, VAR=70, THEN=71, 
		ORDERBY=72;
	public static final int
		RULE_queryRequest = 0, RULE_searchText = 1, RULE_arguments = 2, RULE_label = 3, 
		RULE_string = 4, RULE_argument = 5, RULE_parameter = 6, RULE_valueDataList = 7, 
		RULE_valueIriList = 8, RULE_value = 9, RULE_iriRef = 10, RULE_query = 11, 
		RULE_properName = 12, RULE_prefixes = 13, RULE_prefixed = 14, RULE_selectClause = 15, 
		RULE_select = 16, RULE_name = 17, RULE_description = 18, RULE_activeOnly = 19, 
		RULE_fromClause = 20, RULE_booleanFrom = 21, RULE_notFrom = 22, RULE_orFrom = 23, 
		RULE_andFrom = 24, RULE_from = 25, RULE_whereClause = 26, RULE_where = 27, 
		RULE_booleanWhere = 28, RULE_notWhere = 29, RULE_orWhere = 30, RULE_andWhere = 31, 
		RULE_with = 32, RULE_then = 33, RULE_whereValueTest = 34, RULE_valueLabel = 35, 
		RULE_inClause = 36, RULE_notInClause = 37, RULE_reference = 38, RULE_inverseOf = 39, 
		RULE_range = 40, RULE_fromRange = 41, RULE_toRange = 42, RULE_whereMeasure = 43, 
		RULE_number = 44, RULE_relativeTo = 45, RULE_operator = 46, RULE_units = 47, 
		RULE_sortable = 48, RULE_direction = 49, RULE_count = 50, RULE_graph = 51, 
		RULE_sourceType = 52, RULE_type = 53, RULE_set = 54, RULE_var = 55, RULE_ancestorAndDescendantOf = 56, 
		RULE_ancestorOf = 57, RULE_descendantof = 58, RULE_descendantorselfof = 59, 
		RULE_variable = 60, RULE_alias = 61;
	private static String[] makeRuleNames() {
		return new String[] {
			"queryRequest", "searchText", "arguments", "label", "string", "argument", 
			"parameter", "valueDataList", "valueIriList", "value", "iriRef", "query", 
			"properName", "prefixes", "prefixed", "selectClause", "select", "name", 
			"description", "activeOnly", "fromClause", "booleanFrom", "notFrom", 
			"orFrom", "andFrom", "from", "whereClause", "where", "booleanWhere", 
			"notWhere", "orWhere", "andWhere", "with", "then", "whereValueTest", 
			"valueLabel", "inClause", "notInClause", "reference", "inverseOf", "range", 
			"fromRange", "toRange", "whereMeasure", "number", "relativeTo", "operator", 
			"units", "sortable", "direction", "count", "graph", "sourceType", "type", 
			"set", "var", "ancestorAndDescendantOf", "ancestorOf", "descendantof", 
			"descendantorselfof", "variable", "alias"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'name '", "'valueLabel'", "'inverseOf'", "'relativeTo'", "'@'", 
			"'^'", "'$'", "'>><<'", "'>>'", "'<<'", null, null, null, null, "'searchText'", 
			"'arguments'", "'range'", "'query'", "'from'", "'graph'", "'where'", 
			"'notExist'", "'with'", "'select'", "'ascending'", "'descending'", "'count'", 
			"'sourceType'", null, null, "'description'", "'alias'", "'activeOnly'", 
			"'in'", "'@type'", "'@set'", "'@id'", "'='", "'>'", "'<'", "'>='", "'<='", 
			"'startsWith'", "'units'", null, null, null, "'to'", "'{'", "'}'", "'['", 
			"']'", "'('", "')'", "':'", null, null, null, null, null, "','", null, 
			null, null, null, null, null, null, "'notIn'", "'@var'", "'then'", "'orderBy'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, "SIGNED", 
			"FLOAT", "INTEGER", "DIGIT", "SEARCH_TEXT", "ARGUMENTS", "RANGE", "QUERY", 
			"FROM", "GRAPH", "WHERE", "NOTEXIST", "WITH", "SELECT", "ASCENDING", 
			"DESCENDING", "COUNT", "SOURCE_TYPE", "PREFIXES", "COMMENT", "DESCRIPTION", 
			"ALIAS", "ACTIVE_ONLY", "IN", "TYPE", "SET", "INSTANCE", "EQ", "GT", 
			"LT", "GTE", "LTE", "STARTS_WITH", "UNITS", "AND", "OR", "NOT", "TO", 
			"OC", "CC", "OSB", "CSB", "OB", "CB", "COLON", "IRI_REF", "STRING_LITERAL1", 
			"STRING_LITERAL2", "NAME", "PN_CHARS_U", "COMMA", "WS", "DELIM", "PN_PREFIXED", 
			"PN_PROPERTY", "PN_VARIABLE", "PN_CHARS_BASE", "PN_CHARS", "NOTIN", "VAR", 
			"THEN", "ORDERBY"
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
			setState(125);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PREFIXES) {
				{
				setState(124);
				prefixes();
				}
			}

			setState(128);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEARCH_TEXT) {
				{
				setState(127);
				searchText();
				}
			}

			setState(131);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ARGUMENTS) {
				{
				setState(130);
				arguments();
				}
			}

			setState(133);
			query();
			setState(134);
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
			setState(136);
			match(SEARCH_TEXT);
			setState(137);
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
			setState(139);
			match(ARGUMENTS);
			setState(140);
			match(OC);
			setState(141);
			argument();
			setState(146);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(142);
				match(COMMA);
				setState(143);
				argument();
				}
				}
				setState(148);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(149);
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
			setState(153);
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
			setState(155);
			parameter();
			setState(156);
			match(COLON);
			setState(160);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(157);
				value();
				}
				break;
			case 2:
				{
				setState(158);
				valueDataList();
				}
				break;
			case 3:
				{
				setState(159);
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
			setState(162);
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
			setState(164);
			value();
			setState(169);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(165);
					match(COMMA);
					setState(166);
					value();
					}
					} 
				}
				setState(171);
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
			setState(172);
			iriRef();
			setState(177);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(173);
					match(COMMA);
					setState(174);
					iriRef();
					}
					} 
				}
				setState(179);
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
			setState(180);
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
			setState(182);
			_la = _input.LA(1);
			if ( !(_la==IRI_REF || _la==PN_PREFIXED) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(184);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAME) {
				{
				setState(183);
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
			setState(186);
			match(QUERY);
			setState(187);
			match(OC);
			setState(189);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IRI_REF || _la==PN_PREFIXED) {
				{
				setState(188);
				iriRef();
				}
			}

			setState(192);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(191);
				properName();
				}
			}

			setState(195);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DESCRIPTION) {
				{
				setState(194);
				description();
				}
			}

			setState(198);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ACTIVE_ONLY) {
				{
				setState(197);
				activeOnly();
				}
			}

			setState(200);
			fromClause();
			setState(202);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SELECT) {
				{
				setState(201);
				selectClause();
				}
			}

			setState(207);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==QUERY) {
				{
				{
				setState(204);
				query();
				}
				}
				setState(209);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(210);
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
			setState(212);
			match(T__0);
			setState(213);
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
			setState(215);
			match(PREFIXES);
			setState(216);
			match(OC);
			setState(217);
			prefixed();
			setState(222);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(218);
				match(COMMA);
				setState(219);
				prefixed();
				}
				}
				setState(224);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(225);
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
			setState(227);
			match(PN_PROPERTY);
			setState(228);
			match(COLON);
			setState(229);
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
			setState(231);
			match(SELECT);
			setState(232);
			match(OC);
			{
			setState(233);
			select();
			setState(238);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(234);
				match(COMMA);
				setState(235);
				select();
				}
				}
				setState(240);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(241);
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
			setState(245);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRI_REF:
			case PN_PREFIXED:
				{
				setState(243);
				iriRef();
				}
				break;
			case PN_PROPERTY:
				{
				setState(244);
				match(PN_PROPERTY);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(248);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(247);
				whereClause();
				}
			}

			setState(261);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OC) {
				{
				setState(250);
				match(OC);
				{
				setState(251);
				select();
				setState(256);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(252);
					match(COMMA);
					setState(253);
					select();
					}
					}
					setState(258);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				setState(259);
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
			setState(263);
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
			setState(265);
			match(DESCRIPTION);
			setState(266);
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
			setState(268);
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
			setState(270);
			match(FROM);
			setState(273);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				setState(271);
				from();
				}
				break;
			case 2:
				{
				setState(272);
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
			setState(278);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(275);
				andFrom();
				}
				break;
			case 2:
				{
				setState(276);
				orFrom();
				}
				break;
			case 3:
				{
				setState(277);
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
			setState(280);
			match(NOT);
			{
			setState(281);
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
			setState(283);
			from();
			}
			setState(286); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(284);
				match(OR);
				{
				setState(285);
				from();
				}
				}
				}
				setState(288); 
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
			setState(290);
			from();
			}
			setState(293); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(291);
				match(AND);
				{
				setState(292);
				from();
				}
				}
				}
				setState(295); 
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
			setState(297);
			match(OC);
			setState(299);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DESCRIPTION) {
				{
				setState(298);
				description();
				}
			}

			setState(302);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==GRAPH) {
				{
				setState(301);
				graph();
				}
			}

			setState(306);
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
				setState(304);
				reference();
				}
				break;
			case NOT:
			case OC:
				{
				{
				setState(305);
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
			setState(309);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(308);
				whereClause();
				}
			}

			setState(311);
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
			setState(313);
			match(WHERE);
			setState(316);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				{
				setState(314);
				where();
				}
				break;
			case 2:
				{
				setState(315);
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
			setState(318);
			match(OC);
			setState(320);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DESCRIPTION) {
				{
				setState(319);
				description();
				}
			}

			setState(336);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				{
				setState(322);
				reference();
				setState(323);
				whereClause();
				}
				}
				break;
			case 2:
				{
				{
				setState(325);
				reference();
				setState(326);
				with();
				setState(327);
				whereClause();
				}
				}
				break;
			case 3:
				{
				{
				setState(329);
				reference();
				setState(330);
				whereValueTest();
				}
				}
				break;
			case 4:
				{
				{
				setState(332);
				reference();
				setState(333);
				with();
				}
				}
				break;
			case 5:
				{
				{
				setState(335);
				booleanWhere();
				}
				}
				break;
			}
			setState(338);
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
			setState(343);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				{
				setState(340);
				andWhere();
				}
				break;
			case 2:
				{
				setState(341);
				orWhere();
				}
				break;
			case 3:
				{
				setState(342);
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
			setState(345);
			match(NOT);
			setState(346);
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
			setState(348);
			where();
			}
			setState(351); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(349);
				match(OR);
				{
				setState(350);
				where();
				}
				}
				}
				setState(353); 
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
			setState(355);
			where();
			}
			setState(358); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(356);
				match(AND);
				{
				setState(357);
				where();
				}
				}
				}
				setState(360); 
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
		public ThenContext then() {
			return getRuleContext(ThenContext.class,0);
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
			setState(362);
			match(WITH);
			setState(363);
			match(OC);
			setState(364);
			whereClause();
			setState(366);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ORDERBY) {
				{
				setState(365);
				sortable();
				}
			}

			setState(369);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==THEN) {
				{
				setState(368);
				then();
				}
			}

			setState(371);
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
	public static class ThenContext extends ParserRuleContext {
		public TerminalNode THEN() { return getToken(IMQParser.THEN, 0); }
		public WhereContext where() {
			return getRuleContext(WhereContext.class,0);
		}
		public BooleanWhereContext booleanWhere() {
			return getRuleContext(BooleanWhereContext.class,0);
		}
		public ThenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_then; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterThen(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitThen(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitThen(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ThenContext then() throws RecognitionException {
		ThenContext _localctx = new ThenContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_then);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(373);
			match(THEN);
			setState(376);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				{
				setState(374);
				where();
				}
				break;
			case 2:
				{
				setState(375);
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
		enterRule(_localctx, 68, RULE_whereValueTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(384);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IN:
			case NOTIN:
				{
				setState(380);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IN:
					{
					setState(378);
					inClause();
					}
					break;
				case NOTIN:
					{
					setState(379);
					notInClause();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case RANGE:
				{
				setState(382);
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
				setState(383);
				whereMeasure();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(387);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(386);
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
		enterRule(_localctx, 70, RULE_valueLabel);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(389);
			match(T__1);
			setState(390);
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
		enterRule(_localctx, 72, RULE_inClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(392);
			match(IN);
			{
			setState(393);
			reference();
			setState(398);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(394);
				match(COMMA);
				setState(395);
				reference();
				}
				}
				setState(400);
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
		enterRule(_localctx, 74, RULE_notInClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(401);
			match(NOTIN);
			{
			setState(402);
			reference();
			setState(407);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(403);
				match(COMMA);
				setState(404);
				reference();
				}
				}
				setState(409);
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
		enterRule(_localctx, 76, RULE_reference);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(410);
				inverseOf();
				}
			}

			setState(414);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((_la) & ~0x3f) == 0 && ((1L << _la) & 1099511629792L) != 0) {
				{
				setState(413);
				sourceType();
				}
			}

			setState(421);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRI_REF:
			case PN_PREFIXED:
				{
				{
				setState(416);
				_la = _input.LA(1);
				if ( !(_la==IRI_REF || _la==PN_PREFIXED) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(418);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAME) {
					{
					setState(417);
					name();
					}
				}

				}
				}
				break;
			case PN_VARIABLE:
				{
				{
				setState(420);
				variable();
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(424);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ALIAS) {
				{
				setState(423);
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
		enterRule(_localctx, 78, RULE_inverseOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(426);
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
		enterRule(_localctx, 80, RULE_range);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(428);
			match(RANGE);
			{
			setState(429);
			fromRange();
			setState(430);
			toRange();
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
	public static class FromRangeContext extends ParserRuleContext {
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
		enterRule(_localctx, 82, RULE_fromRange);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(432);
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
		enterRule(_localctx, 84, RULE_toRange);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(434);
			match(TO);
			setState(435);
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
		enterRule(_localctx, 86, RULE_whereMeasure);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(437);
			operator();
			setState(440);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING_LITERAL1:
			case STRING_LITERAL2:
				{
				setState(438);
				string();
				}
				break;
			case SIGNED:
			case FLOAT:
			case INTEGER:
				{
				setState(439);
				number();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(443);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==UNITS) {
				{
				setState(442);
				units();
				}
			}

			setState(446);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(445);
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
		enterRule(_localctx, 88, RULE_number);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(448);
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
		enterRule(_localctx, 90, RULE_relativeTo);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(450);
			match(T__3);
			setState(451);
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
		enterRule(_localctx, 92, RULE_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(453);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 17317308137472L) != 0) ) {
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
		public TerminalNode UNITS() { return getToken(IMQParser.UNITS, 0); }
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
		enterRule(_localctx, 94, RULE_units);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(455);
			match(UNITS);
			setState(456);
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
		public TerminalNode ORDERBY() { return getToken(IMQParser.ORDERBY, 0); }
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
		enterRule(_localctx, 96, RULE_sortable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(458);
			match(ORDERBY);
			setState(461);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRI_REF:
			case PN_PREFIXED:
				{
				setState(459);
				iriRef();
				}
				break;
			case PN_PROPERTY:
				{
				setState(460);
				match(PN_PROPERTY);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(463);
			direction();
			setState(464);
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
		enterRule(_localctx, 98, RULE_direction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(466);
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
		enterRule(_localctx, 100, RULE_count);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(468);
			match(COUNT);
			setState(469);
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
		enterRule(_localctx, 102, RULE_graph);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(471);
			match(GRAPH);
			setState(472);
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
		enterRule(_localctx, 104, RULE_sourceType);
		try {
			setState(481);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__4:
				enterOuterAlt(_localctx, 1);
				{
				setState(474);
				type();
				}
				break;
			case T__5:
				enterOuterAlt(_localctx, 2);
				{
				setState(475);
				set();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 3);
				{
				setState(476);
				var();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 4);
				{
				setState(477);
				descendantorselfof();
				}
				break;
			case LT:
				enterOuterAlt(_localctx, 5);
				{
				setState(478);
				descendantof();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 6);
				{
				setState(479);
				ancestorOf();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 7);
				{
				setState(480);
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
		enterRule(_localctx, 106, RULE_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(483);
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
		enterRule(_localctx, 108, RULE_set);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(485);
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
		enterRule(_localctx, 110, RULE_var);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(487);
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
		enterRule(_localctx, 112, RULE_ancestorAndDescendantOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(489);
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
		enterRule(_localctx, 114, RULE_ancestorOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(491);
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
		enterRule(_localctx, 116, RULE_descendantof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(493);
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
		enterRule(_localctx, 118, RULE_descendantorselfof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(495);
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
		enterRule(_localctx, 120, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(497);
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
		enterRule(_localctx, 122, RULE_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(499);
			match(ALIAS);
			setState(500);
			match(COLON);
			setState(501);
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
		"\u0004\u0001H\u01f8\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
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
		"<\u0007<\u0002=\u0007=\u0001\u0000\u0003\u0000~\b\u0000\u0001\u0000\u0003"+
		"\u0000\u0081\b\u0000\u0001\u0000\u0003\u0000\u0084\b\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002\u0091\b\u0002"+
		"\n\u0002\f\u0002\u0094\t\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0003\u0005\u00a1\b\u0005\u0001\u0006\u0001\u0006\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0005\u0007\u00a8\b\u0007\n\u0007\f\u0007"+
		"\u00ab\t\u0007\u0001\b\u0001\b\u0001\b\u0005\b\u00b0\b\b\n\b\f\b\u00b3"+
		"\t\b\u0001\t\u0001\t\u0001\n\u0001\n\u0003\n\u00b9\b\n\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0003\u000b\u00be\b\u000b\u0001\u000b\u0003\u000b\u00c1"+
		"\b\u000b\u0001\u000b\u0003\u000b\u00c4\b\u000b\u0001\u000b\u0003\u000b"+
		"\u00c7\b\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u00cb\b\u000b\u0001"+
		"\u000b\u0005\u000b\u00ce\b\u000b\n\u000b\f\u000b\u00d1\t\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0005\r\u00dd\b\r\n\r\f\r\u00e0\t\r\u0001\r\u0001\r\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0005\u000f\u00ed\b\u000f\n\u000f\f\u000f\u00f0\t\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0003\u0010\u00f6\b\u0010"+
		"\u0001\u0010\u0003\u0010\u00f9\b\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0005\u0010\u00ff\b\u0010\n\u0010\f\u0010\u0102\t\u0010\u0001"+
		"\u0010\u0001\u0010\u0003\u0010\u0106\b\u0010\u0001\u0011\u0001\u0011\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0003\u0014\u0112\b\u0014\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0003\u0015\u0117\b\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0004\u0017\u011f\b\u0017\u000b\u0017\f"+
		"\u0017\u0120\u0001\u0018\u0001\u0018\u0001\u0018\u0004\u0018\u0126\b\u0018"+
		"\u000b\u0018\f\u0018\u0127\u0001\u0019\u0001\u0019\u0003\u0019\u012c\b"+
		"\u0019\u0001\u0019\u0003\u0019\u012f\b\u0019\u0001\u0019\u0001\u0019\u0003"+
		"\u0019\u0133\b\u0019\u0001\u0019\u0003\u0019\u0136\b\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0003\u001a\u013d\b\u001a"+
		"\u0001\u001b\u0001\u001b\u0003\u001b\u0141\b\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0003\u001b\u0151\b\u001b\u0001\u001b\u0001\u001b\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0003\u001c\u0158\b\u001c\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0004\u001e\u0160\b\u001e\u000b\u001e"+
		"\f\u001e\u0161\u0001\u001f\u0001\u001f\u0001\u001f\u0004\u001f\u0167\b"+
		"\u001f\u000b\u001f\f\u001f\u0168\u0001 \u0001 \u0001 \u0001 \u0003 \u016f"+
		"\b \u0001 \u0003 \u0172\b \u0001 \u0001 \u0001!\u0001!\u0001!\u0003!\u0179"+
		"\b!\u0001\"\u0001\"\u0003\"\u017d\b\"\u0001\"\u0001\"\u0003\"\u0181\b"+
		"\"\u0001\"\u0003\"\u0184\b\"\u0001#\u0001#\u0001#\u0001$\u0001$\u0001"+
		"$\u0001$\u0005$\u018d\b$\n$\f$\u0190\t$\u0001%\u0001%\u0001%\u0001%\u0005"+
		"%\u0196\b%\n%\f%\u0199\t%\u0001&\u0003&\u019c\b&\u0001&\u0003&\u019f\b"+
		"&\u0001&\u0001&\u0003&\u01a3\b&\u0001&\u0003&\u01a6\b&\u0001&\u0003&\u01a9"+
		"\b&\u0001\'\u0001\'\u0001(\u0001(\u0001(\u0001(\u0001)\u0001)\u0001*\u0001"+
		"*\u0001*\u0001+\u0001+\u0001+\u0003+\u01b9\b+\u0001+\u0003+\u01bc\b+\u0001"+
		"+\u0003+\u01bf\b+\u0001,\u0001,\u0001-\u0001-\u0001-\u0001.\u0001.\u0001"+
		"/\u0001/\u0001/\u00010\u00010\u00010\u00030\u01ce\b0\u00010\u00010\u0001"+
		"0\u00011\u00011\u00012\u00012\u00012\u00013\u00013\u00013\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00034\u01e2\b4\u00015\u00015\u0001"+
		"6\u00016\u00017\u00017\u00018\u00018\u00019\u00019\u0001:\u0001:\u0001"+
		";\u0001;\u0001<\u0001<\u0001=\u0001=\u0001=\u0001=\u0001=\u0000\u0000"+
		">\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a"+
		"\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz\u0000\u0006"+
		"\u0001\u00009:\u0002\u000088@@\u0001\u0000\u000b\r\u0001\u0000AB\u0001"+
		"\u0000&+\u0001\u0000\u0019\u001a\u01fa\u0000}\u0001\u0000\u0000\u0000"+
		"\u0002\u0088\u0001\u0000\u0000\u0000\u0004\u008b\u0001\u0000\u0000\u0000"+
		"\u0006\u0097\u0001\u0000\u0000\u0000\b\u0099\u0001\u0000\u0000\u0000\n"+
		"\u009b\u0001\u0000\u0000\u0000\f\u00a2\u0001\u0000\u0000\u0000\u000e\u00a4"+
		"\u0001\u0000\u0000\u0000\u0010\u00ac\u0001\u0000\u0000\u0000\u0012\u00b4"+
		"\u0001\u0000\u0000\u0000\u0014\u00b6\u0001\u0000\u0000\u0000\u0016\u00ba"+
		"\u0001\u0000\u0000\u0000\u0018\u00d4\u0001\u0000\u0000\u0000\u001a\u00d7"+
		"\u0001\u0000\u0000\u0000\u001c\u00e3\u0001\u0000\u0000\u0000\u001e\u00e7"+
		"\u0001\u0000\u0000\u0000 \u00f5\u0001\u0000\u0000\u0000\"\u0107\u0001"+
		"\u0000\u0000\u0000$\u0109\u0001\u0000\u0000\u0000&\u010c\u0001\u0000\u0000"+
		"\u0000(\u010e\u0001\u0000\u0000\u0000*\u0116\u0001\u0000\u0000\u0000,"+
		"\u0118\u0001\u0000\u0000\u0000.\u011b\u0001\u0000\u0000\u00000\u0122\u0001"+
		"\u0000\u0000\u00002\u0129\u0001\u0000\u0000\u00004\u0139\u0001\u0000\u0000"+
		"\u00006\u013e\u0001\u0000\u0000\u00008\u0157\u0001\u0000\u0000\u0000:"+
		"\u0159\u0001\u0000\u0000\u0000<\u015c\u0001\u0000\u0000\u0000>\u0163\u0001"+
		"\u0000\u0000\u0000@\u016a\u0001\u0000\u0000\u0000B\u0175\u0001\u0000\u0000"+
		"\u0000D\u0180\u0001\u0000\u0000\u0000F\u0185\u0001\u0000\u0000\u0000H"+
		"\u0188\u0001\u0000\u0000\u0000J\u0191\u0001\u0000\u0000\u0000L\u019b\u0001"+
		"\u0000\u0000\u0000N\u01aa\u0001\u0000\u0000\u0000P\u01ac\u0001\u0000\u0000"+
		"\u0000R\u01b0\u0001\u0000\u0000\u0000T\u01b2\u0001\u0000\u0000\u0000V"+
		"\u01b5\u0001\u0000\u0000\u0000X\u01c0\u0001\u0000\u0000\u0000Z\u01c2\u0001"+
		"\u0000\u0000\u0000\\\u01c5\u0001\u0000\u0000\u0000^\u01c7\u0001\u0000"+
		"\u0000\u0000`\u01ca\u0001\u0000\u0000\u0000b\u01d2\u0001\u0000\u0000\u0000"+
		"d\u01d4\u0001\u0000\u0000\u0000f\u01d7\u0001\u0000\u0000\u0000h\u01e1"+
		"\u0001\u0000\u0000\u0000j\u01e3\u0001\u0000\u0000\u0000l\u01e5\u0001\u0000"+
		"\u0000\u0000n\u01e7\u0001\u0000\u0000\u0000p\u01e9\u0001\u0000\u0000\u0000"+
		"r\u01eb\u0001\u0000\u0000\u0000t\u01ed\u0001\u0000\u0000\u0000v\u01ef"+
		"\u0001\u0000\u0000\u0000x\u01f1\u0001\u0000\u0000\u0000z\u01f3\u0001\u0000"+
		"\u0000\u0000|~\u0003\u001a\r\u0000}|\u0001\u0000\u0000\u0000}~\u0001\u0000"+
		"\u0000\u0000~\u0080\u0001\u0000\u0000\u0000\u007f\u0081\u0003\u0002\u0001"+
		"\u0000\u0080\u007f\u0001\u0000\u0000\u0000\u0080\u0081\u0001\u0000\u0000"+
		"\u0000\u0081\u0083\u0001\u0000\u0000\u0000\u0082\u0084\u0003\u0004\u0002"+
		"\u0000\u0083\u0082\u0001\u0000\u0000\u0000\u0083\u0084\u0001\u0000\u0000"+
		"\u0000\u0084\u0085\u0001\u0000\u0000\u0000\u0085\u0086\u0003\u0016\u000b"+
		"\u0000\u0086\u0087\u0005\u0000\u0000\u0001\u0087\u0001\u0001\u0000\u0000"+
		"\u0000\u0088\u0089\u0005\u000f\u0000\u0000\u0089\u008a\u0007\u0000\u0000"+
		"\u0000\u008a\u0003\u0001\u0000\u0000\u0000\u008b\u008c\u0005\u0010\u0000"+
		"\u0000\u008c\u008d\u00051\u0000\u0000\u008d\u0092\u0003\n\u0005\u0000"+
		"\u008e\u008f\u0005=\u0000\u0000\u008f\u0091\u0003\n\u0005\u0000\u0090"+
		"\u008e\u0001\u0000\u0000\u0000\u0091\u0094\u0001\u0000\u0000\u0000\u0092"+
		"\u0090\u0001\u0000\u0000\u0000\u0092\u0093\u0001\u0000\u0000\u0000\u0093"+
		"\u0095\u0001\u0000\u0000\u0000\u0094\u0092\u0001\u0000\u0000\u0000\u0095"+
		"\u0096\u00052\u0000\u0000\u0096\u0005\u0001\u0000\u0000\u0000\u0097\u0098"+
		"\u0007\u0000\u0000\u0000\u0098\u0007\u0001\u0000\u0000\u0000\u0099\u009a"+
		"\u0007\u0000\u0000\u0000\u009a\t\u0001\u0000\u0000\u0000\u009b\u009c\u0003"+
		"\f\u0006\u0000\u009c\u00a0\u00057\u0000\u0000\u009d\u00a1\u0003\u0012"+
		"\t\u0000\u009e\u00a1\u0003\u000e\u0007\u0000\u009f\u00a1\u0003\u0010\b"+
		"\u0000\u00a0\u009d\u0001\u0000\u0000\u0000\u00a0\u009e\u0001\u0000\u0000"+
		"\u0000\u00a0\u009f\u0001\u0000\u0000\u0000\u00a1\u000b\u0001\u0000\u0000"+
		"\u0000\u00a2\u00a3\u0005A\u0000\u0000\u00a3\r\u0001\u0000\u0000\u0000"+
		"\u00a4\u00a9\u0003\u0012\t\u0000\u00a5\u00a6\u0005=\u0000\u0000\u00a6"+
		"\u00a8\u0003\u0012\t\u0000\u00a7\u00a5\u0001\u0000\u0000\u0000\u00a8\u00ab"+
		"\u0001\u0000\u0000\u0000\u00a9\u00a7\u0001\u0000\u0000\u0000\u00a9\u00aa"+
		"\u0001\u0000\u0000\u0000\u00aa\u000f\u0001\u0000\u0000\u0000\u00ab\u00a9"+
		"\u0001\u0000\u0000\u0000\u00ac\u00b1\u0003\u0014\n\u0000\u00ad\u00ae\u0005"+
		"=\u0000\u0000\u00ae\u00b0\u0003\u0014\n\u0000\u00af\u00ad\u0001\u0000"+
		"\u0000\u0000\u00b0\u00b3\u0001\u0000\u0000\u0000\u00b1\u00af\u0001\u0000"+
		"\u0000\u0000\u00b1\u00b2\u0001\u0000\u0000\u0000\u00b2\u0011\u0001\u0000"+
		"\u0000\u0000\u00b3\u00b1\u0001\u0000\u0000\u0000\u00b4\u00b5\u0003\b\u0004"+
		"\u0000\u00b5\u0013\u0001\u0000\u0000\u0000\u00b6\u00b8\u0007\u0001\u0000"+
		"\u0000\u00b7\u00b9\u0003\"\u0011\u0000\u00b8\u00b7\u0001\u0000\u0000\u0000"+
		"\u00b8\u00b9\u0001\u0000\u0000\u0000\u00b9\u0015\u0001\u0000\u0000\u0000"+
		"\u00ba\u00bb\u0005\u0012\u0000\u0000\u00bb\u00bd\u00051\u0000\u0000\u00bc"+
		"\u00be\u0003\u0014\n\u0000\u00bd\u00bc\u0001\u0000\u0000\u0000\u00bd\u00be"+
		"\u0001\u0000\u0000\u0000\u00be\u00c0\u0001\u0000\u0000\u0000\u00bf\u00c1"+
		"\u0003\u0018\f\u0000\u00c0\u00bf\u0001\u0000\u0000\u0000\u00c0\u00c1\u0001"+
		"\u0000\u0000\u0000\u00c1\u00c3\u0001\u0000\u0000\u0000\u00c2\u00c4\u0003"+
		"$\u0012\u0000\u00c3\u00c2\u0001\u0000\u0000\u0000\u00c3\u00c4\u0001\u0000"+
		"\u0000\u0000\u00c4\u00c6\u0001\u0000\u0000\u0000\u00c5\u00c7\u0003&\u0013"+
		"\u0000\u00c6\u00c5\u0001\u0000\u0000\u0000\u00c6\u00c7\u0001\u0000\u0000"+
		"\u0000\u00c7\u00c8\u0001\u0000\u0000\u0000\u00c8\u00ca\u0003(\u0014\u0000"+
		"\u00c9\u00cb\u0003\u001e\u000f\u0000\u00ca\u00c9\u0001\u0000\u0000\u0000"+
		"\u00ca\u00cb\u0001\u0000\u0000\u0000\u00cb\u00cf\u0001\u0000\u0000\u0000"+
		"\u00cc\u00ce\u0003\u0016\u000b\u0000\u00cd\u00cc\u0001\u0000\u0000\u0000"+
		"\u00ce\u00d1\u0001\u0000\u0000\u0000\u00cf\u00cd\u0001\u0000\u0000\u0000"+
		"\u00cf\u00d0\u0001\u0000\u0000\u0000\u00d0\u00d2\u0001\u0000\u0000\u0000"+
		"\u00d1\u00cf\u0001\u0000\u0000\u0000\u00d2\u00d3\u00052\u0000\u0000\u00d3"+
		"\u0017\u0001\u0000\u0000\u0000\u00d4\u00d5\u0005\u0001\u0000\u0000\u00d5"+
		"\u00d6\u0003\b\u0004\u0000\u00d6\u0019\u0001\u0000\u0000\u0000\u00d7\u00d8"+
		"\u0005\u001d\u0000\u0000\u00d8\u00d9\u00051\u0000\u0000\u00d9\u00de\u0003"+
		"\u001c\u000e\u0000\u00da\u00db\u0005=\u0000\u0000\u00db\u00dd\u0003\u001c"+
		"\u000e\u0000\u00dc\u00da\u0001\u0000\u0000\u0000\u00dd\u00e0\u0001\u0000"+
		"\u0000\u0000\u00de\u00dc\u0001\u0000\u0000\u0000\u00de\u00df\u0001\u0000"+
		"\u0000\u0000\u00df\u00e1\u0001\u0000\u0000\u0000\u00e0\u00de\u0001\u0000"+
		"\u0000\u0000\u00e1\u00e2\u00052\u0000\u0000\u00e2\u001b\u0001\u0000\u0000"+
		"\u0000\u00e3\u00e4\u0005A\u0000\u0000\u00e4\u00e5\u00057\u0000\u0000\u00e5"+
		"\u00e6\u00058\u0000\u0000\u00e6\u001d\u0001\u0000\u0000\u0000\u00e7\u00e8"+
		"\u0005\u0018\u0000\u0000\u00e8\u00e9\u00051\u0000\u0000\u00e9\u00ee\u0003"+
		" \u0010\u0000\u00ea\u00eb\u0005=\u0000\u0000\u00eb\u00ed\u0003 \u0010"+
		"\u0000\u00ec\u00ea\u0001\u0000\u0000\u0000\u00ed\u00f0\u0001\u0000\u0000"+
		"\u0000\u00ee\u00ec\u0001\u0000\u0000\u0000\u00ee\u00ef\u0001\u0000\u0000"+
		"\u0000\u00ef\u00f1\u0001\u0000\u0000\u0000\u00f0\u00ee\u0001\u0000\u0000"+
		"\u0000\u00f1\u00f2\u00052\u0000\u0000\u00f2\u001f\u0001\u0000\u0000\u0000"+
		"\u00f3\u00f6\u0003\u0014\n\u0000\u00f4\u00f6\u0005A\u0000\u0000\u00f5"+
		"\u00f3\u0001\u0000\u0000\u0000\u00f5\u00f4\u0001\u0000\u0000\u0000\u00f6"+
		"\u00f8\u0001\u0000\u0000\u0000\u00f7\u00f9\u00034\u001a\u0000\u00f8\u00f7"+
		"\u0001\u0000\u0000\u0000\u00f8\u00f9\u0001\u0000\u0000\u0000\u00f9\u0105"+
		"\u0001\u0000\u0000\u0000\u00fa\u00fb\u00051\u0000\u0000\u00fb\u0100\u0003"+
		" \u0010\u0000\u00fc\u00fd\u0005=\u0000\u0000\u00fd\u00ff\u0003 \u0010"+
		"\u0000\u00fe\u00fc\u0001\u0000\u0000\u0000\u00ff\u0102\u0001\u0000\u0000"+
		"\u0000\u0100\u00fe\u0001\u0000\u0000\u0000\u0100\u0101\u0001\u0000\u0000"+
		"\u0000\u0101\u0103\u0001\u0000\u0000\u0000\u0102\u0100\u0001\u0000\u0000"+
		"\u0000\u0103\u0104\u00052\u0000\u0000\u0104\u0106\u0001\u0000\u0000\u0000"+
		"\u0105\u00fa\u0001\u0000\u0000\u0000\u0105\u0106\u0001\u0000\u0000\u0000"+
		"\u0106!\u0001\u0000\u0000\u0000\u0107\u0108\u0005;\u0000\u0000\u0108#"+
		"\u0001\u0000\u0000\u0000\u0109\u010a\u0005\u001f\u0000\u0000\u010a\u010b"+
		"\u0003\b\u0004\u0000\u010b%\u0001\u0000\u0000\u0000\u010c\u010d\u0005"+
		"!\u0000\u0000\u010d\'\u0001\u0000\u0000\u0000\u010e\u0111\u0005\u0013"+
		"\u0000\u0000\u010f\u0112\u00032\u0019\u0000\u0110\u0112\u0003*\u0015\u0000"+
		"\u0111\u010f\u0001\u0000\u0000\u0000\u0111\u0110\u0001\u0000\u0000\u0000"+
		"\u0112)\u0001\u0000\u0000\u0000\u0113\u0117\u00030\u0018\u0000\u0114\u0117"+
		"\u0003.\u0017\u0000\u0115\u0117\u0003,\u0016\u0000\u0116\u0113\u0001\u0000"+
		"\u0000\u0000\u0116\u0114\u0001\u0000\u0000\u0000\u0116\u0115\u0001\u0000"+
		"\u0000\u0000\u0117+\u0001\u0000\u0000\u0000\u0118\u0119\u0005/\u0000\u0000"+
		"\u0119\u011a\u00032\u0019\u0000\u011a-\u0001\u0000\u0000\u0000\u011b\u011e"+
		"\u00032\u0019\u0000\u011c\u011d\u0005.\u0000\u0000\u011d\u011f\u00032"+
		"\u0019\u0000\u011e\u011c\u0001\u0000\u0000\u0000\u011f\u0120\u0001\u0000"+
		"\u0000\u0000\u0120\u011e\u0001\u0000\u0000\u0000\u0120\u0121\u0001\u0000"+
		"\u0000\u0000\u0121/\u0001\u0000\u0000\u0000\u0122\u0125\u00032\u0019\u0000"+
		"\u0123\u0124\u0005-\u0000\u0000\u0124\u0126\u00032\u0019\u0000\u0125\u0123"+
		"\u0001\u0000\u0000\u0000\u0126\u0127\u0001\u0000\u0000\u0000\u0127\u0125"+
		"\u0001\u0000\u0000\u0000\u0127\u0128\u0001\u0000\u0000\u0000\u01281\u0001"+
		"\u0000\u0000\u0000\u0129\u012b\u00051\u0000\u0000\u012a\u012c\u0003$\u0012"+
		"\u0000\u012b\u012a\u0001\u0000\u0000\u0000\u012b\u012c\u0001\u0000\u0000"+
		"\u0000\u012c\u012e\u0001\u0000\u0000\u0000\u012d\u012f\u0003f3\u0000\u012e"+
		"\u012d\u0001\u0000\u0000\u0000\u012e\u012f\u0001\u0000\u0000\u0000\u012f"+
		"\u0132\u0001\u0000\u0000\u0000\u0130\u0133\u0003L&\u0000\u0131\u0133\u0003"+
		"*\u0015\u0000\u0132\u0130\u0001\u0000\u0000\u0000\u0132\u0131\u0001\u0000"+
		"\u0000\u0000\u0132\u0133\u0001\u0000\u0000\u0000\u0133\u0135\u0001\u0000"+
		"\u0000\u0000\u0134\u0136\u00034\u001a\u0000\u0135\u0134\u0001\u0000\u0000"+
		"\u0000\u0135\u0136\u0001\u0000\u0000\u0000\u0136\u0137\u0001\u0000\u0000"+
		"\u0000\u0137\u0138\u00052\u0000\u0000\u01383\u0001\u0000\u0000\u0000\u0139"+
		"\u013c\u0005\u0015\u0000\u0000\u013a\u013d\u00036\u001b\u0000\u013b\u013d"+
		"\u00038\u001c\u0000\u013c\u013a\u0001\u0000\u0000\u0000\u013c\u013b\u0001"+
		"\u0000\u0000\u0000\u013d5\u0001\u0000\u0000\u0000\u013e\u0140\u00051\u0000"+
		"\u0000\u013f\u0141\u0003$\u0012\u0000\u0140\u013f\u0001\u0000\u0000\u0000"+
		"\u0140\u0141\u0001\u0000\u0000\u0000\u0141\u0150\u0001\u0000\u0000\u0000"+
		"\u0142\u0143\u0003L&\u0000\u0143\u0144\u00034\u001a\u0000\u0144\u0151"+
		"\u0001\u0000\u0000\u0000\u0145\u0146\u0003L&\u0000\u0146\u0147\u0003@"+
		" \u0000\u0147\u0148\u00034\u001a\u0000\u0148\u0151\u0001\u0000\u0000\u0000"+
		"\u0149\u014a\u0003L&\u0000\u014a\u014b\u0003D\"\u0000\u014b\u0151\u0001"+
		"\u0000\u0000\u0000\u014c\u014d\u0003L&\u0000\u014d\u014e\u0003@ \u0000"+
		"\u014e\u0151\u0001\u0000\u0000\u0000\u014f\u0151\u00038\u001c\u0000\u0150"+
		"\u0142\u0001\u0000\u0000\u0000\u0150\u0145\u0001\u0000\u0000\u0000\u0150"+
		"\u0149\u0001\u0000\u0000\u0000\u0150\u014c\u0001\u0000\u0000\u0000\u0150"+
		"\u014f\u0001\u0000\u0000\u0000\u0151\u0152\u0001\u0000\u0000\u0000\u0152"+
		"\u0153\u00052\u0000\u0000\u01537\u0001\u0000\u0000\u0000\u0154\u0158\u0003"+
		">\u001f\u0000\u0155\u0158\u0003<\u001e\u0000\u0156\u0158\u0003:\u001d"+
		"\u0000\u0157\u0154\u0001\u0000\u0000\u0000\u0157\u0155\u0001\u0000\u0000"+
		"\u0000\u0157\u0156\u0001\u0000\u0000\u0000\u01589\u0001\u0000\u0000\u0000"+
		"\u0159\u015a\u0005/\u0000\u0000\u015a\u015b\u00036\u001b\u0000\u015b;"+
		"\u0001\u0000\u0000\u0000\u015c\u015f\u00036\u001b\u0000\u015d\u015e\u0005"+
		".\u0000\u0000\u015e\u0160\u00036\u001b\u0000\u015f\u015d\u0001\u0000\u0000"+
		"\u0000\u0160\u0161\u0001\u0000\u0000\u0000\u0161\u015f\u0001\u0000\u0000"+
		"\u0000\u0161\u0162\u0001\u0000\u0000\u0000\u0162=\u0001\u0000\u0000\u0000"+
		"\u0163\u0166\u00036\u001b\u0000\u0164\u0165\u0005-\u0000\u0000\u0165\u0167"+
		"\u00036\u001b\u0000\u0166\u0164\u0001\u0000\u0000\u0000\u0167\u0168\u0001"+
		"\u0000\u0000\u0000\u0168\u0166\u0001\u0000\u0000\u0000\u0168\u0169\u0001"+
		"\u0000\u0000\u0000\u0169?\u0001\u0000\u0000\u0000\u016a\u016b\u0005\u0017"+
		"\u0000\u0000\u016b\u016c\u00051\u0000\u0000\u016c\u016e\u00034\u001a\u0000"+
		"\u016d\u016f\u0003`0\u0000\u016e\u016d\u0001\u0000\u0000\u0000\u016e\u016f"+
		"\u0001\u0000\u0000\u0000\u016f\u0171\u0001\u0000\u0000\u0000\u0170\u0172"+
		"\u0003B!\u0000\u0171\u0170\u0001\u0000\u0000\u0000\u0171\u0172\u0001\u0000"+
		"\u0000\u0000\u0172\u0173\u0001\u0000\u0000\u0000\u0173\u0174\u00052\u0000"+
		"\u0000\u0174A\u0001\u0000\u0000\u0000\u0175\u0178\u0005G\u0000\u0000\u0176"+
		"\u0179\u00036\u001b\u0000\u0177\u0179\u00038\u001c\u0000\u0178\u0176\u0001"+
		"\u0000\u0000\u0000\u0178\u0177\u0001\u0000\u0000\u0000\u0179C\u0001\u0000"+
		"\u0000\u0000\u017a\u017d\u0003H$\u0000\u017b\u017d\u0003J%\u0000\u017c"+
		"\u017a\u0001\u0000\u0000\u0000\u017c\u017b\u0001\u0000\u0000\u0000\u017d"+
		"\u0181\u0001\u0000\u0000\u0000\u017e\u0181\u0003P(\u0000\u017f\u0181\u0003"+
		"V+\u0000\u0180\u017c\u0001\u0000\u0000\u0000\u0180\u017e\u0001\u0000\u0000"+
		"\u0000\u0180\u017f\u0001\u0000\u0000\u0000\u0181\u0183\u0001\u0000\u0000"+
		"\u0000\u0182\u0184\u0003F#\u0000\u0183\u0182\u0001\u0000\u0000\u0000\u0183"+
		"\u0184\u0001\u0000\u0000\u0000\u0184E\u0001\u0000\u0000\u0000\u0185\u0186"+
		"\u0005\u0002\u0000\u0000\u0186\u0187\u0003\b\u0004\u0000\u0187G\u0001"+
		"\u0000\u0000\u0000\u0188\u0189\u0005\"\u0000\u0000\u0189\u018e\u0003L"+
		"&\u0000\u018a\u018b\u0005=\u0000\u0000\u018b\u018d\u0003L&\u0000\u018c"+
		"\u018a\u0001\u0000\u0000\u0000\u018d\u0190\u0001\u0000\u0000\u0000\u018e"+
		"\u018c\u0001\u0000\u0000\u0000\u018e\u018f\u0001\u0000\u0000\u0000\u018f"+
		"I\u0001\u0000\u0000\u0000\u0190\u018e\u0001\u0000\u0000\u0000\u0191\u0192"+
		"\u0005E\u0000\u0000\u0192\u0197\u0003L&\u0000\u0193\u0194\u0005=\u0000"+
		"\u0000\u0194\u0196\u0003L&\u0000\u0195\u0193\u0001\u0000\u0000\u0000\u0196"+
		"\u0199\u0001\u0000\u0000\u0000\u0197\u0195\u0001\u0000\u0000\u0000\u0197"+
		"\u0198\u0001\u0000\u0000\u0000\u0198K\u0001\u0000\u0000\u0000\u0199\u0197"+
		"\u0001\u0000\u0000\u0000\u019a\u019c\u0003N\'\u0000\u019b\u019a\u0001"+
		"\u0000\u0000\u0000\u019b\u019c\u0001\u0000\u0000\u0000\u019c\u019e\u0001"+
		"\u0000\u0000\u0000\u019d\u019f\u0003h4\u0000\u019e\u019d\u0001\u0000\u0000"+
		"\u0000\u019e\u019f\u0001\u0000\u0000\u0000\u019f\u01a5\u0001\u0000\u0000"+
		"\u0000\u01a0\u01a2\u0007\u0001\u0000\u0000\u01a1\u01a3\u0003\"\u0011\u0000"+
		"\u01a2\u01a1\u0001\u0000\u0000\u0000\u01a2\u01a3\u0001\u0000\u0000\u0000"+
		"\u01a3\u01a6\u0001\u0000\u0000\u0000\u01a4\u01a6\u0003x<\u0000\u01a5\u01a0"+
		"\u0001\u0000\u0000\u0000\u01a5\u01a4\u0001\u0000\u0000\u0000\u01a6\u01a8"+
		"\u0001\u0000\u0000\u0000\u01a7\u01a9\u0003z=\u0000\u01a8\u01a7\u0001\u0000"+
		"\u0000\u0000\u01a8\u01a9\u0001\u0000\u0000\u0000\u01a9M\u0001\u0000\u0000"+
		"\u0000\u01aa\u01ab\u0005\u0003\u0000\u0000\u01abO\u0001\u0000\u0000\u0000"+
		"\u01ac\u01ad\u0005\u0011\u0000\u0000\u01ad\u01ae\u0003R)\u0000\u01ae\u01af"+
		"\u0003T*\u0000\u01afQ\u0001\u0000\u0000\u0000\u01b0\u01b1\u0003V+\u0000"+
		"\u01b1S\u0001\u0000\u0000\u0000\u01b2\u01b3\u00050\u0000\u0000\u01b3\u01b4"+
		"\u0003V+\u0000\u01b4U\u0001\u0000\u0000\u0000\u01b5\u01b8\u0003\\.\u0000"+
		"\u01b6\u01b9\u0003\b\u0004\u0000\u01b7\u01b9\u0003X,\u0000\u01b8\u01b6"+
		"\u0001\u0000\u0000\u0000\u01b8\u01b7\u0001\u0000\u0000\u0000\u01b9\u01bb"+
		"\u0001\u0000\u0000\u0000\u01ba\u01bc\u0003^/\u0000\u01bb\u01ba\u0001\u0000"+
		"\u0000\u0000\u01bb\u01bc\u0001\u0000\u0000\u0000\u01bc\u01be\u0001\u0000"+
		"\u0000\u0000\u01bd\u01bf\u0003Z-\u0000\u01be\u01bd\u0001\u0000\u0000\u0000"+
		"\u01be\u01bf\u0001\u0000\u0000\u0000\u01bfW\u0001\u0000\u0000\u0000\u01c0"+
		"\u01c1\u0007\u0002\u0000\u0000\u01c1Y\u0001\u0000\u0000\u0000\u01c2\u01c3"+
		"\u0005\u0004\u0000\u0000\u01c3\u01c4\u0007\u0003\u0000\u0000\u01c4[\u0001"+
		"\u0000\u0000\u0000\u01c5\u01c6\u0007\u0004\u0000\u0000\u01c6]\u0001\u0000"+
		"\u0000\u0000\u01c7\u01c8\u0005,\u0000\u0000\u01c8\u01c9\u0005A\u0000\u0000"+
		"\u01c9_\u0001\u0000\u0000\u0000\u01ca\u01cd\u0005H\u0000\u0000\u01cb\u01ce"+
		"\u0003\u0014\n\u0000\u01cc\u01ce\u0005A\u0000\u0000\u01cd\u01cb\u0001"+
		"\u0000\u0000\u0000\u01cd\u01cc\u0001\u0000\u0000\u0000\u01ce\u01cf\u0001"+
		"\u0000\u0000\u0000\u01cf\u01d0\u0003b1\u0000\u01d0\u01d1\u0003d2\u0000"+
		"\u01d1a\u0001\u0000\u0000\u0000\u01d2\u01d3\u0007\u0005\u0000\u0000\u01d3"+
		"c\u0001\u0000\u0000\u0000\u01d4\u01d5\u0005\u001b\u0000\u0000\u01d5\u01d6"+
		"\u0005\r\u0000\u0000\u01d6e\u0001\u0000\u0000\u0000\u01d7\u01d8\u0005"+
		"\u0014\u0000\u0000\u01d8\u01d9\u00058\u0000\u0000\u01d9g\u0001\u0000\u0000"+
		"\u0000\u01da\u01e2\u0003j5\u0000\u01db\u01e2\u0003l6\u0000\u01dc\u01e2"+
		"\u0003n7\u0000\u01dd\u01e2\u0003v;\u0000\u01de\u01e2\u0003t:\u0000\u01df"+
		"\u01e2\u0003r9\u0000\u01e0\u01e2\u0003p8\u0000\u01e1\u01da\u0001\u0000"+
		"\u0000\u0000\u01e1\u01db\u0001\u0000\u0000\u0000\u01e1\u01dc\u0001\u0000"+
		"\u0000\u0000\u01e1\u01dd\u0001\u0000\u0000\u0000\u01e1\u01de\u0001\u0000"+
		"\u0000\u0000\u01e1\u01df\u0001\u0000\u0000\u0000\u01e1\u01e0\u0001\u0000"+
		"\u0000\u0000\u01e2i\u0001\u0000\u0000\u0000\u01e3\u01e4\u0005\u0005\u0000"+
		"\u0000\u01e4k\u0001\u0000\u0000\u0000\u01e5\u01e6\u0005\u0006\u0000\u0000"+
		"\u01e6m\u0001\u0000\u0000\u0000\u01e7\u01e8\u0005\u0007\u0000\u0000\u01e8"+
		"o\u0001\u0000\u0000\u0000\u01e9\u01ea\u0005\b\u0000\u0000\u01eaq\u0001"+
		"\u0000\u0000\u0000\u01eb\u01ec\u0005\t\u0000\u0000\u01ecs\u0001\u0000"+
		"\u0000\u0000\u01ed\u01ee\u0005(\u0000\u0000\u01eeu\u0001\u0000\u0000\u0000"+
		"\u01ef\u01f0\u0005\n\u0000\u0000\u01f0w\u0001\u0000\u0000\u0000\u01f1"+
		"\u01f2\u0005B\u0000\u0000\u01f2y\u0001\u0000\u0000\u0000\u01f3\u01f4\u0005"+
		" \u0000\u0000\u01f4\u01f5\u00057\u0000\u0000\u01f5\u01f6\u0005A\u0000"+
		"\u0000\u01f6{\u0001\u0000\u0000\u00004}\u0080\u0083\u0092\u00a0\u00a9"+
		"\u00b1\u00b8\u00bd\u00c0\u00c3\u00c6\u00ca\u00cf\u00de\u00ee\u00f5\u00f8"+
		"\u0100\u0105\u0111\u0116\u0120\u0127\u012b\u012e\u0132\u0135\u013c\u0140"+
		"\u0150\u0157\u0161\u0168\u016e\u0171\u0178\u017c\u0180\u0183\u018e\u0197"+
		"\u019b\u019e\u01a2\u01a5\u01a8\u01b8\u01bb\u01be\u01cd\u01e1";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}