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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, SEARCH_TEXT=7, ARGUMENTS=8, 
		QUERY=9, FROM=10, GRAPH=11, WHERE=12, NOTEXIST=13, WITH=14, SELECT=15, 
		EARLIEST=16, LATEST=17, MAXIMUM=18, MINIMUM=19, COUNT=20, SOURCE_TYPE=21, 
		PREFIX=22, COMMENT=23, DESCRIPTION=24, NAME=25, ALIAS=26, ACTIVE_ONLY=27, 
		IN=28, TYPE=29, SET=30, INSTANCE=31, EQ=32, GT=33, LT=34, GTE=35, LTE=36, 
		STARTS_WITH=37, AND=38, OR=39, NOT=40, TO=41, OC=42, CC=43, OSB=44, CSB=45, 
		OB=46, CB=47, COLON=48, UCHAR=49, HEX=50, IRI_REF=51, STRING_LITERAL1=52, 
		STRING_LITERAL2=53, PN_CHARS_U=54, COMMA=55, WS=56, PN_PROPERTY=57, PN_VARIABLE=58, 
		PN_PREFIXED=59, PN_CHARS_BASE=60, PN_CHARS=61, DIGIT=62, NUMBER=63, NOTIN=64, 
		VALUE_LABEL=65, VAR=66;
	public static final int
		RULE_queryRequest = 0, RULE_searchText = 1, RULE_arguments = 2, RULE_label = 3, 
		RULE_string = 4, RULE_argument = 5, RULE_parameter = 6, RULE_valueDataList = 7, 
		RULE_valueIriList = 8, RULE_value = 9, RULE_iriRef = 10, RULE_query = 11, 
		RULE_properName = 12, RULE_prefixDecl = 13, RULE_selectClause = 14, RULE_selectList = 15, 
		RULE_select = 16, RULE_name = 17, RULE_description = 18, RULE_activeOnly = 19, 
		RULE_fromClause = 20, RULE_bracketFrom = 21, RULE_fromBoolean = 22, RULE_notFrom = 23, 
		RULE_orFrom = 24, RULE_andFrom = 25, RULE_from = 26, RULE_whereClause = 27, 
		RULE_subWhere = 28, RULE_where = 29, RULE_notExist = 30, RULE_valueLabel = 31, 
		RULE_whereBoolean = 32, RULE_notWhere = 33, RULE_orWhere = 34, RULE_andWhere = 35, 
		RULE_bracketWhere = 36, RULE_with = 37, RULE_whereValueTest = 38, RULE_inClause = 39, 
		RULE_notInClause = 40, RULE_conceptSet = 41, RULE_reference = 42, RULE_inverseOf = 43, 
		RULE_range = 44, RULE_fromRange = 45, RULE_toRange = 46, RULE_whereMeasure = 47, 
		RULE_relativeTo = 48, RULE_operator = 49, RULE_units = 50, RULE_sortable = 51, 
		RULE_latest = 52, RULE_earliest = 53, RULE_maximum = 54, RULE_minimum = 55, 
		RULE_count = 56, RULE_graph = 57, RULE_sourceType = 58, RULE_subsumption = 59, 
		RULE_ancestorAndDescendantOf = 60, RULE_ancestorOf = 61, RULE_descendantof = 62, 
		RULE_descendantorselfof = 63, RULE_variable = 64, RULE_alias = 65;
	private static String[] makeRuleNames() {
		return new String[] {
			"queryRequest", "searchText", "arguments", "label", "string", "argument", 
			"parameter", "valueDataList", "valueIriList", "value", "iriRef", "query", 
			"properName", "prefixDecl", "selectClause", "selectList", "select", "name", 
			"description", "activeOnly", "fromClause", "bracketFrom", "fromBoolean", 
			"notFrom", "orFrom", "andFrom", "from", "whereClause", "subWhere", "where", 
			"notExist", "valueLabel", "whereBoolean", "notWhere", "orWhere", "andWhere", 
			"bracketWhere", "with", "whereValueTest", "inClause", "notInClause", 
			"conceptSet", "reference", "inverseOf", "range", "fromRange", "toRange", 
			"whereMeasure", "relativeTo", "operator", "units", "sortable", "latest", 
			"earliest", "maximum", "minimum", "count", "graph", "sourceType", "subsumption", 
			"ancestorAndDescendantOf", "ancestorOf", "descendantof", "descendantorselfof", 
			"variable", "alias"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'name '", "'inverseOf'", "'relativeTo'", "'>><<'", "'>>'", "'<<'", 
			"'searchText'", "'arguments'", "'query'", "'from'", "'graph'", "'where'", 
			"'notExist'", "'with'", "'select'", "'earliest'", "'latest'", "'maximum'", 
			"'minimum'", "'count'", "'sourceType'", null, null, "'description'", 
			null, "'alias'", "'activeOnly'", "'in'", "'@type'", "'@set'", "'@id'", 
			"'='", "'>'", "'<'", "'>='", "'<='", "'startsWith'", null, null, null, 
			"'to'", "'{'", "'}'", "'['", "']'", "'('", "')'", "':'", null, null, 
			null, null, null, null, "','", null, null, null, null, null, null, null, 
			null, "'notIn'", "'valueLabel'", "'@var'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, "SEARCH_TEXT", "ARGUMENTS", 
			"QUERY", "FROM", "GRAPH", "WHERE", "NOTEXIST", "WITH", "SELECT", "EARLIEST", 
			"LATEST", "MAXIMUM", "MINIMUM", "COUNT", "SOURCE_TYPE", "PREFIX", "COMMENT", 
			"DESCRIPTION", "NAME", "ALIAS", "ACTIVE_ONLY", "IN", "TYPE", "SET", "INSTANCE", 
			"EQ", "GT", "LT", "GTE", "LTE", "STARTS_WITH", "AND", "OR", "NOT", "TO", 
			"OC", "CC", "OSB", "CSB", "OB", "CB", "COLON", "UCHAR", "HEX", "IRI_REF", 
			"STRING_LITERAL1", "STRING_LITERAL2", "PN_CHARS_U", "COMMA", "WS", "PN_PROPERTY", 
			"PN_VARIABLE", "PN_PREFIXED", "PN_CHARS_BASE", "PN_CHARS", "DIGIT", "NUMBER", 
			"NOTIN", "VALUE_LABEL", "VAR"
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
			setState(132);
			match(OC);
			setState(136);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PREFIX) {
				{
				{
				setState(133);
				prefixDecl();
				}
				}
				setState(138);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(140);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEARCH_TEXT) {
				{
				setState(139);
				searchText();
				}
			}

			setState(143);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ARGUMENTS) {
				{
				setState(142);
				arguments();
				}
			}

			setState(145);
			query();
			setState(146);
			match(CC);
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
			setState(149);
			match(SEARCH_TEXT);
			setState(150);
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
			setState(152);
			match(ARGUMENTS);
			setState(153);
			match(OC);
			setState(154);
			argument();
			setState(159);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(155);
				match(COMMA);
				setState(156);
				argument();
				}
				}
				setState(161);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(162);
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
			setState(164);
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
			setState(166);
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
			setState(168);
			parameter();
			setState(169);
			match(COLON);
			setState(173);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(170);
				value();
				}
				break;
			case 2:
				{
				setState(171);
				valueDataList();
				}
				break;
			case 3:
				{
				setState(172);
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
			setState(175);
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
			setState(177);
			value();
			setState(182);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(178);
					match(COMMA);
					setState(179);
					value();
					}
					} 
				}
				setState(184);
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
			setState(185);
			iriRef();
			setState(190);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(186);
					match(COMMA);
					setState(187);
					iriRef();
					}
					} 
				}
				setState(192);
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
			setState(193);
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
			setState(195);
			match(INSTANCE);
			setState(196);
			_la = _input.LA(1);
			if ( !(_la==IRI_REF || _la==PN_PREFIXED) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(198);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAME) {
				{
				setState(197);
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
			setState(200);
			match(QUERY);
			setState(201);
			match(OC);
			setState(203);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==INSTANCE) {
				{
				setState(202);
				iriRef();
				}
			}

			setState(206);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(205);
				properName();
				}
			}

			setState(209);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DESCRIPTION) {
				{
				setState(208);
				description();
				}
			}

			setState(212);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ACTIVE_ONLY) {
				{
				setState(211);
				activeOnly();
				}
			}

			setState(214);
			fromClause();
			setState(216);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SELECT) {
				{
				setState(215);
				selectClause();
				}
			}

			setState(221);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==QUERY) {
				{
				{
				setState(218);
				query();
				}
				}
				setState(223);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(224);
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
			setState(226);
			match(T__0);
			setState(227);
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
			setState(229);
			match(PREFIX);
			setState(230);
			match(PN_PROPERTY);
			setState(231);
			match(COLON);
			setState(232);
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
		public SelectListContext selectList() {
			return getRuleContext(SelectListContext.class,0);
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
			setState(234);
			match(SELECT);
			setState(235);
			selectList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectListContext extends ParserRuleContext {
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
		public SelectListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterSelectList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitSelectList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitSelectList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectListContext selectList() throws RecognitionException {
		SelectListContext _localctx = new SelectListContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_selectList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(237);
			select();
			setState(242);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(238);
				match(COMMA);
				setState(239);
				select();
				}
				}
				setState(244);
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
	public static class SelectContext extends ParserRuleContext {
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
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
		enterRule(_localctx, 32, RULE_select);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(245);
			match(OC);
			setState(248);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INSTANCE:
				{
				setState(246);
				iriRef();
				}
				break;
			case PN_PROPERTY:
				{
				setState(247);
				match(PN_PROPERTY);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(251);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(250);
				whereClause();
				}
			}

			setState(254);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SELECT) {
				{
				setState(253);
				selectClause();
				}
			}

			setState(256);
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
		enterRule(_localctx, 34, RULE_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(258);
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
			setState(260);
			match(DESCRIPTION);
			setState(261);
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
			setState(263);
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
		enterRule(_localctx, 40, RULE_fromClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(265);
			match(FROM);
			setState(269);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				{
				setState(266);
				from();
				}
				break;
			case 2:
				{
				setState(267);
				fromBoolean();
				}
				break;
			case 3:
				{
				setState(268);
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
	public static class BracketFromContext extends ParserRuleContext {
		public List<TerminalNode> OC() { return getTokens(IMQParser.OC); }
		public TerminalNode OC(int i) {
			return getToken(IMQParser.OC, i);
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
		enterRule(_localctx, 42, RULE_bracketFrom);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(271);
			match(OC);
			setState(274);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				{
				setState(272);
				fromBoolean();
				}
				break;
			case 2:
				{
				setState(273);
				from();
				}
				break;
			}
			setState(276);
			match(OC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 44, RULE_fromBoolean);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(281);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				setState(278);
				andFrom();
				}
				break;
			case 2:
				{
				setState(279);
				orFrom();
				}
				break;
			case 3:
				{
				setState(280);
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
		enterRule(_localctx, 46, RULE_notFrom);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(285);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(283);
				from();
				}
				break;
			case 2:
				{
				setState(284);
				bracketFrom();
				}
				break;
			}
			setState(292); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(287);
				match(NOT);
				setState(290);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
				case 1:
					{
					setState(288);
					from();
					}
					break;
				case 2:
					{
					setState(289);
					bracketFrom();
					}
					break;
				}
				}
				}
				setState(294); 
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
		enterRule(_localctx, 48, RULE_orFrom);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(298);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				{
				setState(296);
				from();
				}
				break;
			case 2:
				{
				setState(297);
				bracketFrom();
				}
				break;
			}
			setState(305); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(300);
				match(OR);
				setState(303);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
				case 1:
					{
					setState(301);
					from();
					}
					break;
				case 2:
					{
					setState(302);
					bracketFrom();
					}
					break;
				}
				}
				}
				setState(307); 
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
		public List<BracketFromContext> bracketFrom() {
			return getRuleContexts(BracketFromContext.class);
		}
		public BracketFromContext bracketFrom(int i) {
			return getRuleContext(BracketFromContext.class,i);
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
		enterRule(_localctx, 50, RULE_andFrom);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(311);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				{
				setState(309);
				from();
				}
				break;
			case 2:
				{
				setState(310);
				bracketFrom();
				}
				break;
			}
			setState(318); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(313);
				match(AND);
				setState(316);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
				case 1:
					{
					setState(314);
					from();
					}
					break;
				case 2:
					{
					setState(315);
					bracketFrom();
					}
					break;
				}
				}
				}
				setState(320); 
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
		enterRule(_localctx, 52, RULE_from);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(322);
			match(OC);
			setState(324);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DESCRIPTION) {
				{
				setState(323);
				description();
				}
			}

			setState(327);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==GRAPH) {
				{
				setState(326);
				graph();
				}
			}

			setState(330);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((_la) & ~0x3f) == 0 && ((1L << _la) & 3758096388L) != 0 || _la==VAR) {
				{
				setState(329);
				reference();
				}
			}

			setState(333);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(332);
				whereClause();
				}
			}

			setState(335);
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
		enterRule(_localctx, 54, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(337);
			match(WHERE);
			setState(338);
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
		public WhereContext where() {
			return getRuleContext(WhereContext.class,0);
		}
		public WhereBooleanContext whereBoolean() {
			return getRuleContext(WhereBooleanContext.class,0);
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
		enterRule(_localctx, 56, RULE_subWhere);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(343);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				{
				setState(340);
				where();
				}
				break;
			case 2:
				{
				setState(341);
				whereBoolean();
				}
				break;
			case 3:
				{
				setState(342);
				bracketWhere();
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
		public ReferenceContext reference() {
			return getRuleContext(ReferenceContext.class,0);
		}
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
		public WithContext with() {
			return getRuleContext(WithContext.class,0);
		}
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public NotExistContext notExist() {
			return getRuleContext(NotExistContext.class,0);
		}
		public WhereValueTestContext whereValueTest() {
			return getRuleContext(WhereValueTestContext.class,0);
		}
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
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
		enterRule(_localctx, 58, RULE_where);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(345);
			match(OC);
			setState(347);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WITH) {
				{
				setState(346);
				with();
				}
			}

			setState(350);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DESCRIPTION) {
				{
				setState(349);
				description();
				}
			}

			setState(353);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NOTEXIST) {
				{
				setState(352);
				notExist();
				}
			}

			setState(355);
			reference();
			setState(357);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & 18014400921468929L) != 0) {
				{
				setState(356);
				whereValueTest();
				}
			}

			setState(360);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(359);
				whereClause();
				}
			}

			setState(362);
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
		enterRule(_localctx, 60, RULE_notExist);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(364);
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
		enterRule(_localctx, 62, RULE_valueLabel);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(366);
			match(VALUE_LABEL);
			setState(367);
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
		enterRule(_localctx, 64, RULE_whereBoolean);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(372);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				{
				setState(369);
				andWhere();
				}
				break;
			case 2:
				{
				setState(370);
				orWhere();
				}
				break;
			case 3:
				{
				setState(371);
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
		enterRule(_localctx, 66, RULE_notWhere);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(379); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(374);
				match(NOT);
				setState(377);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
				case 1:
					{
					setState(375);
					where();
					}
					break;
				case 2:
					{
					setState(376);
					bracketWhere();
					}
					break;
				}
				}
				}
				setState(381); 
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
		enterRule(_localctx, 68, RULE_orWhere);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(385);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				{
				setState(383);
				where();
				}
				break;
			case 2:
				{
				setState(384);
				bracketWhere();
				}
				break;
			}
			setState(392); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(387);
				match(OR);
				setState(390);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
				case 1:
					{
					setState(388);
					where();
					}
					break;
				case 2:
					{
					setState(389);
					bracketWhere();
					}
					break;
				}
				}
				}
				setState(394); 
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
		enterRule(_localctx, 70, RULE_andWhere);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(398);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				{
				setState(396);
				where();
				}
				break;
			case 2:
				{
				setState(397);
				bracketWhere();
				}
				break;
			}
			setState(405); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(400);
				match(AND);
				setState(403);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
				case 1:
					{
					setState(401);
					where();
					}
					break;
				case 2:
					{
					setState(402);
					bracketWhere();
					}
					break;
				}
				}
				}
				setState(407); 
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
		public TerminalNode OC() { return getToken(IMQParser.OC, 0); }
		public TerminalNode CC() { return getToken(IMQParser.CC, 0); }
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
		enterRule(_localctx, 72, RULE_bracketWhere);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(409);
			match(OC);
			setState(412);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
			case 1:
				{
				setState(410);
				whereBoolean();
				}
				break;
			case 2:
				{
				setState(411);
				where();
				}
				break;
			}
			setState(414);
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
		enterRule(_localctx, 74, RULE_with);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(416);
			match(WITH);
			setState(417);
			match(OC);
			setState(420);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				{
				setState(418);
				where();
				}
				break;
			case 2:
				{
				setState(419);
				whereBoolean();
				}
				break;
			}
			setState(422);
			sortable();
			setState(423);
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
		public InClauseContext inClause() {
			return getRuleContext(InClauseContext.class,0);
		}
		public NotInClauseContext notInClause() {
			return getRuleContext(NotInClauseContext.class,0);
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
		enterRule(_localctx, 76, RULE_whereValueTest);
		try {
			setState(431);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IN:
			case NOTIN:
				enterOuterAlt(_localctx, 1);
				{
				setState(427);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IN:
					{
					setState(425);
					inClause();
					}
					break;
				case NOTIN:
					{
					setState(426);
					notInClause();
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
				setState(429);
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
				setState(430);
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
	public static class InClauseContext extends ParserRuleContext {
		public TerminalNode IN() { return getToken(IMQParser.IN, 0); }
		public ConceptSetContext conceptSet() {
			return getRuleContext(ConceptSetContext.class,0);
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
		enterRule(_localctx, 78, RULE_inClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(433);
			match(IN);
			setState(434);
			conceptSet();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public ConceptSetContext conceptSet() {
			return getRuleContext(ConceptSetContext.class,0);
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
		enterRule(_localctx, 80, RULE_notInClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(436);
			match(NOTIN);
			setState(437);
			conceptSet();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConceptSetContext extends ParserRuleContext {
		public FromContext from() {
			return getRuleContext(FromContext.class,0);
		}
		public BracketFromContext bracketFrom() {
			return getRuleContext(BracketFromContext.class,0);
		}
		public FromBooleanContext fromBoolean() {
			return getRuleContext(FromBooleanContext.class,0);
		}
		public ConceptSetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conceptSet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterConceptSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitConceptSet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitConceptSet(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConceptSetContext conceptSet() throws RecognitionException {
		ConceptSetContext _localctx = new ConceptSetContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_conceptSet);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(442);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
			case 1:
				{
				setState(439);
				from();
				}
				break;
			case 2:
				{
				setState(440);
				bracketFrom();
				}
				break;
			case 3:
				{
				setState(441);
				fromBoolean();
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
		public InverseOfContext inverseOf() {
			return getRuleContext(InverseOfContext.class,0);
		}
		public AliasContext alias() {
			return getRuleContext(AliasContext.class,0);
		}
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode IRI_REF() { return getToken(IMQParser.IRI_REF, 0); }
		public TerminalNode PN_PREFIXED() { return getToken(IMQParser.PN_PREFIXED, 0); }
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
		enterRule(_localctx, 84, RULE_reference);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(445);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(444);
				inverseOf();
				}
			}

			setState(447);
			sourceType();
			setState(459);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				{
				{
				setState(449);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((_la) & ~0x3f) == 0 && ((1L << _la) & 17179869296L) != 0) {
					{
					setState(448);
					subsumption();
					}
				}

				setState(451);
				_la = _input.LA(1);
				if ( !(_la==IRI_REF || _la==PN_PREFIXED) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(453);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAME) {
					{
					setState(452);
					name();
					}
				}

				}
				}
				break;
			case 2:
				{
				{
				setState(456);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((_la) & ~0x3f) == 0 && ((1L << _la) & 17179869296L) != 0) {
					{
					setState(455);
					subsumption();
					}
				}

				setState(458);
				variable();
				}
				}
				break;
			}
			setState(462);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ALIAS) {
				{
				setState(461);
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
		enterRule(_localctx, 86, RULE_inverseOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(464);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 88, RULE_range);
		try {
			setState(471);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(466);
				fromRange();
				setState(467);
				toRange();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(469);
				fromRange();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(470);
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
		enterRule(_localctx, 90, RULE_fromRange);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(473);
			match(FROM);
			setState(474);
			match(OC);
			setState(475);
			whereMeasure();
			setState(476);
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
		enterRule(_localctx, 92, RULE_toRange);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(478);
			match(TO);
			setState(479);
			match(OC);
			setState(480);
			whereMeasure();
			setState(481);
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
		enterRule(_localctx, 94, RULE_whereMeasure);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(483);
			operator();
			{
			setState(486);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING_LITERAL1:
			case STRING_LITERAL2:
				{
				setState(484);
				string();
				}
				break;
			case NUMBER:
				{
				setState(485);
				match(NUMBER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(489);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PN_CHARS) {
				{
				setState(488);
				units();
				}
			}

			}
			setState(492);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2 || _la==PN_PROPERTY) {
				{
				setState(491);
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
		enterRule(_localctx, 96, RULE_relativeTo);
		try {
			setState(497);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
				enterOuterAlt(_localctx, 1);
				{
				setState(494);
				match(T__2);
				setState(495);
				match(PN_VARIABLE);
				}
				break;
			case PN_PROPERTY:
				enterOuterAlt(_localctx, 2);
				{
				setState(496);
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
		enterRule(_localctx, 98, RULE_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(499);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 270582939648L) != 0) ) {
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
		enterRule(_localctx, 100, RULE_units);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(501);
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
		enterRule(_localctx, 102, RULE_sortable);
		try {
			setState(509);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LATEST:
				enterOuterAlt(_localctx, 1);
				{
				setState(503);
				latest();
				}
				break;
			case EARLIEST:
				enterOuterAlt(_localctx, 2);
				{
				setState(504);
				earliest();
				}
				break;
			case MAXIMUM:
				enterOuterAlt(_localctx, 3);
				{
				setState(505);
				maximum();
				}
				break;
			case MINIMUM:
				enterOuterAlt(_localctx, 4);
				{
				setState(506);
				minimum();
				setState(507);
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
		enterRule(_localctx, 104, RULE_latest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(511);
			match(LATEST);
			setState(512);
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
		enterRule(_localctx, 106, RULE_earliest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(514);
			match(EARLIEST);
			setState(515);
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
		enterRule(_localctx, 108, RULE_maximum);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(517);
			match(MAXIMUM);
			setState(518);
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
		enterRule(_localctx, 110, RULE_minimum);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(520);
			match(MINIMUM);
			setState(521);
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
		enterRule(_localctx, 112, RULE_count);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(523);
			match(COUNT);
			setState(524);
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
		enterRule(_localctx, 114, RULE_graph);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(526);
			match(GRAPH);
			setState(527);
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
		public TerminalNode VAR() { return getToken(IMQParser.VAR, 0); }
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
		enterRule(_localctx, 116, RULE_sourceType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(529);
			_la = _input.LA(1);
			if ( !((((_la - 29)) & ~0x3f) == 0 && ((1L << (_la - 29)) & 137438953479L) != 0) ) {
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
		public AncestorAndDescendantOfContext ancestorAndDescendantOf() {
			return getRuleContext(AncestorAndDescendantOfContext.class,0);
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
		enterRule(_localctx, 118, RULE_subsumption);
		try {
			setState(535);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__5:
				enterOuterAlt(_localctx, 1);
				{
				setState(531);
				descendantorselfof();
				}
				break;
			case LT:
				enterOuterAlt(_localctx, 2);
				{
				setState(532);
				descendantof();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 3);
				{
				setState(533);
				ancestorOf();
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 4);
				{
				setState(534);
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
		enterRule(_localctx, 120, RULE_ancestorAndDescendantOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(537);
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
		enterRule(_localctx, 122, RULE_ancestorOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(539);
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
		enterRule(_localctx, 124, RULE_descendantof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(541);
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
		enterRule(_localctx, 126, RULE_descendantorselfof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(543);
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
		enterRule(_localctx, 128, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(545);
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
		enterRule(_localctx, 130, RULE_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(547);
			match(ALIAS);
			setState(548);
			match(COLON);
			setState(549);
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
		"\u0004\u0001B\u0228\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
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
		"A\u0007A\u0001\u0000\u0001\u0000\u0005\u0000\u0087\b\u0000\n\u0000\f\u0000"+
		"\u008a\t\u0000\u0001\u0000\u0003\u0000\u008d\b\u0000\u0001\u0000\u0003"+
		"\u0000\u0090\b\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0005\u0002\u009e\b\u0002\n\u0002\f\u0002\u00a1\t\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005"+
		"\u00ae\b\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0005\u0007\u00b5\b\u0007\n\u0007\f\u0007\u00b8\t\u0007\u0001\b\u0001"+
		"\b\u0001\b\u0005\b\u00bd\b\b\n\b\f\b\u00c0\t\b\u0001\t\u0001\t\u0001\n"+
		"\u0001\n\u0001\n\u0003\n\u00c7\b\n\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0003\u000b\u00cc\b\u000b\u0001\u000b\u0003\u000b\u00cf\b\u000b\u0001"+
		"\u000b\u0003\u000b\u00d2\b\u000b\u0001\u000b\u0003\u000b\u00d5\b\u000b"+
		"\u0001\u000b\u0001\u000b\u0003\u000b\u00d9\b\u000b\u0001\u000b\u0005\u000b"+
		"\u00dc\b\u000b\n\u000b\f\u000b\u00df\t\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0005\u000f"+
		"\u00f1\b\u000f\n\u000f\f\u000f\u00f4\t\u000f\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0003\u0010\u00f9\b\u0010\u0001\u0010\u0003\u0010\u00fc\b\u0010"+
		"\u0001\u0010\u0003\u0010\u00ff\b\u0010\u0001\u0010\u0001\u0010\u0001\u0011"+
		"\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u010e\b\u0014"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u0113\b\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u011a\b\u0016"+
		"\u0001\u0017\u0001\u0017\u0003\u0017\u011e\b\u0017\u0001\u0017\u0001\u0017"+
		"\u0001\u0017\u0003\u0017\u0123\b\u0017\u0004\u0017\u0125\b\u0017\u000b"+
		"\u0017\f\u0017\u0126\u0001\u0018\u0001\u0018\u0003\u0018\u012b\b\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u0130\b\u0018\u0004\u0018"+
		"\u0132\b\u0018\u000b\u0018\f\u0018\u0133\u0001\u0019\u0001\u0019\u0003"+
		"\u0019\u0138\b\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u013d"+
		"\b\u0019\u0004\u0019\u013f\b\u0019\u000b\u0019\f\u0019\u0140\u0001\u001a"+
		"\u0001\u001a\u0003\u001a\u0145\b\u001a\u0001\u001a\u0003\u001a\u0148\b"+
		"\u001a\u0001\u001a\u0003\u001a\u014b\b\u001a\u0001\u001a\u0003\u001a\u014e"+
		"\b\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0003\u001c\u0158\b\u001c\u0001\u001d\u0001"+
		"\u001d\u0003\u001d\u015c\b\u001d\u0001\u001d\u0003\u001d\u015f\b\u001d"+
		"\u0001\u001d\u0003\u001d\u0162\b\u001d\u0001\u001d\u0001\u001d\u0003\u001d"+
		"\u0166\b\u001d\u0001\u001d\u0003\u001d\u0169\b\u001d\u0001\u001d\u0001"+
		"\u001d\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0001\u001f\u0001"+
		" \u0001 \u0001 \u0003 \u0175\b \u0001!\u0001!\u0001!\u0003!\u017a\b!\u0004"+
		"!\u017c\b!\u000b!\f!\u017d\u0001\"\u0001\"\u0003\"\u0182\b\"\u0001\"\u0001"+
		"\"\u0001\"\u0003\"\u0187\b\"\u0004\"\u0189\b\"\u000b\"\f\"\u018a\u0001"+
		"#\u0001#\u0003#\u018f\b#\u0001#\u0001#\u0001#\u0003#\u0194\b#\u0004#\u0196"+
		"\b#\u000b#\f#\u0197\u0001$\u0001$\u0001$\u0003$\u019d\b$\u0001$\u0001"+
		"$\u0001%\u0001%\u0001%\u0001%\u0003%\u01a5\b%\u0001%\u0001%\u0001%\u0001"+
		"&\u0001&\u0003&\u01ac\b&\u0001&\u0001&\u0003&\u01b0\b&\u0001\'\u0001\'"+
		"\u0001\'\u0001(\u0001(\u0001(\u0001)\u0001)\u0001)\u0003)\u01bb\b)\u0001"+
		"*\u0003*\u01be\b*\u0001*\u0001*\u0003*\u01c2\b*\u0001*\u0001*\u0003*\u01c6"+
		"\b*\u0001*\u0003*\u01c9\b*\u0001*\u0003*\u01cc\b*\u0001*\u0003*\u01cf"+
		"\b*\u0001+\u0001+\u0001,\u0001,\u0001,\u0001,\u0001,\u0003,\u01d8\b,\u0001"+
		"-\u0001-\u0001-\u0001-\u0001-\u0001.\u0001.\u0001.\u0001.\u0001.\u0001"+
		"/\u0001/\u0001/\u0003/\u01e7\b/\u0001/\u0003/\u01ea\b/\u0001/\u0003/\u01ed"+
		"\b/\u00010\u00010\u00010\u00030\u01f2\b0\u00011\u00011\u00012\u00012\u0001"+
		"3\u00013\u00013\u00013\u00013\u00013\u00033\u01fe\b3\u00014\u00014\u0001"+
		"4\u00015\u00015\u00015\u00016\u00016\u00016\u00017\u00017\u00017\u0001"+
		"8\u00018\u00018\u00019\u00019\u00019\u0001:\u0001:\u0001;\u0001;\u0001"+
		";\u0001;\u0003;\u0218\b;\u0001<\u0001<\u0001=\u0001=\u0001>\u0001>\u0001"+
		"?\u0001?\u0001@\u0001@\u0001A\u0001A\u0001A\u0001A\u0001A\u0000\u0000"+
		"B\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a"+
		"\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082"+
		"\u0000\u0004\u0001\u000045\u0002\u000033;;\u0001\u0000 %\u0002\u0000\u001d"+
		"\u001fBB\u0234\u0000\u0084\u0001\u0000\u0000\u0000\u0002\u0095\u0001\u0000"+
		"\u0000\u0000\u0004\u0098\u0001\u0000\u0000\u0000\u0006\u00a4\u0001\u0000"+
		"\u0000\u0000\b\u00a6\u0001\u0000\u0000\u0000\n\u00a8\u0001\u0000\u0000"+
		"\u0000\f\u00af\u0001\u0000\u0000\u0000\u000e\u00b1\u0001\u0000\u0000\u0000"+
		"\u0010\u00b9\u0001\u0000\u0000\u0000\u0012\u00c1\u0001\u0000\u0000\u0000"+
		"\u0014\u00c3\u0001\u0000\u0000\u0000\u0016\u00c8\u0001\u0000\u0000\u0000"+
		"\u0018\u00e2\u0001\u0000\u0000\u0000\u001a\u00e5\u0001\u0000\u0000\u0000"+
		"\u001c\u00ea\u0001\u0000\u0000\u0000\u001e\u00ed\u0001\u0000\u0000\u0000"+
		" \u00f5\u0001\u0000\u0000\u0000\"\u0102\u0001\u0000\u0000\u0000$\u0104"+
		"\u0001\u0000\u0000\u0000&\u0107\u0001\u0000\u0000\u0000(\u0109\u0001\u0000"+
		"\u0000\u0000*\u010f\u0001\u0000\u0000\u0000,\u0119\u0001\u0000\u0000\u0000"+
		".\u011d\u0001\u0000\u0000\u00000\u012a\u0001\u0000\u0000\u00002\u0137"+
		"\u0001\u0000\u0000\u00004\u0142\u0001\u0000\u0000\u00006\u0151\u0001\u0000"+
		"\u0000\u00008\u0157\u0001\u0000\u0000\u0000:\u0159\u0001\u0000\u0000\u0000"+
		"<\u016c\u0001\u0000\u0000\u0000>\u016e\u0001\u0000\u0000\u0000@\u0174"+
		"\u0001\u0000\u0000\u0000B\u017b\u0001\u0000\u0000\u0000D\u0181\u0001\u0000"+
		"\u0000\u0000F\u018e\u0001\u0000\u0000\u0000H\u0199\u0001\u0000\u0000\u0000"+
		"J\u01a0\u0001\u0000\u0000\u0000L\u01af\u0001\u0000\u0000\u0000N\u01b1"+
		"\u0001\u0000\u0000\u0000P\u01b4\u0001\u0000\u0000\u0000R\u01ba\u0001\u0000"+
		"\u0000\u0000T\u01bd\u0001\u0000\u0000\u0000V\u01d0\u0001\u0000\u0000\u0000"+
		"X\u01d7\u0001\u0000\u0000\u0000Z\u01d9\u0001\u0000\u0000\u0000\\\u01de"+
		"\u0001\u0000\u0000\u0000^\u01e3\u0001\u0000\u0000\u0000`\u01f1\u0001\u0000"+
		"\u0000\u0000b\u01f3\u0001\u0000\u0000\u0000d\u01f5\u0001\u0000\u0000\u0000"+
		"f\u01fd\u0001\u0000\u0000\u0000h\u01ff\u0001\u0000\u0000\u0000j\u0202"+
		"\u0001\u0000\u0000\u0000l\u0205\u0001\u0000\u0000\u0000n\u0208\u0001\u0000"+
		"\u0000\u0000p\u020b\u0001\u0000\u0000\u0000r\u020e\u0001\u0000\u0000\u0000"+
		"t\u0211\u0001\u0000\u0000\u0000v\u0217\u0001\u0000\u0000\u0000x\u0219"+
		"\u0001\u0000\u0000\u0000z\u021b\u0001\u0000\u0000\u0000|\u021d\u0001\u0000"+
		"\u0000\u0000~\u021f\u0001\u0000\u0000\u0000\u0080\u0221\u0001\u0000\u0000"+
		"\u0000\u0082\u0223\u0001\u0000\u0000\u0000\u0084\u0088\u0005*\u0000\u0000"+
		"\u0085\u0087\u0003\u001a\r\u0000\u0086\u0085\u0001\u0000\u0000\u0000\u0087"+
		"\u008a\u0001\u0000\u0000\u0000\u0088\u0086\u0001\u0000\u0000\u0000\u0088"+
		"\u0089\u0001\u0000\u0000\u0000\u0089\u008c\u0001\u0000\u0000\u0000\u008a"+
		"\u0088\u0001\u0000\u0000\u0000\u008b\u008d\u0003\u0002\u0001\u0000\u008c"+
		"\u008b\u0001\u0000\u0000\u0000\u008c\u008d\u0001\u0000\u0000\u0000\u008d"+
		"\u008f\u0001\u0000\u0000\u0000\u008e\u0090\u0003\u0004\u0002\u0000\u008f"+
		"\u008e\u0001\u0000\u0000\u0000\u008f\u0090\u0001\u0000\u0000\u0000\u0090"+
		"\u0091\u0001\u0000\u0000\u0000\u0091\u0092\u0003\u0016\u000b\u0000\u0092"+
		"\u0093\u0005+\u0000\u0000\u0093\u0094\u0005\u0000\u0000\u0001\u0094\u0001"+
		"\u0001\u0000\u0000\u0000\u0095\u0096\u0005\u0007\u0000\u0000\u0096\u0097"+
		"\u0007\u0000\u0000\u0000\u0097\u0003\u0001\u0000\u0000\u0000\u0098\u0099"+
		"\u0005\b\u0000\u0000\u0099\u009a\u0005*\u0000\u0000\u009a\u009f\u0003"+
		"\n\u0005\u0000\u009b\u009c\u00057\u0000\u0000\u009c\u009e\u0003\n\u0005"+
		"\u0000\u009d\u009b\u0001\u0000\u0000\u0000\u009e\u00a1\u0001\u0000\u0000"+
		"\u0000\u009f\u009d\u0001\u0000\u0000\u0000\u009f\u00a0\u0001\u0000\u0000"+
		"\u0000\u00a0\u00a2\u0001\u0000\u0000\u0000\u00a1\u009f\u0001\u0000\u0000"+
		"\u0000\u00a2\u00a3\u0005+\u0000\u0000\u00a3\u0005\u0001\u0000\u0000\u0000"+
		"\u00a4\u00a5\u0007\u0000\u0000\u0000\u00a5\u0007\u0001\u0000\u0000\u0000"+
		"\u00a6\u00a7\u0007\u0000\u0000\u0000\u00a7\t\u0001\u0000\u0000\u0000\u00a8"+
		"\u00a9\u0003\f\u0006\u0000\u00a9\u00ad\u00050\u0000\u0000\u00aa\u00ae"+
		"\u0003\u0012\t\u0000\u00ab\u00ae\u0003\u000e\u0007\u0000\u00ac\u00ae\u0003"+
		"\u0010\b\u0000\u00ad\u00aa\u0001\u0000\u0000\u0000\u00ad\u00ab\u0001\u0000"+
		"\u0000\u0000\u00ad\u00ac\u0001\u0000\u0000\u0000\u00ae\u000b\u0001\u0000"+
		"\u0000\u0000\u00af\u00b0\u00059\u0000\u0000\u00b0\r\u0001\u0000\u0000"+
		"\u0000\u00b1\u00b6\u0003\u0012\t\u0000\u00b2\u00b3\u00057\u0000\u0000"+
		"\u00b3\u00b5\u0003\u0012\t\u0000\u00b4\u00b2\u0001\u0000\u0000\u0000\u00b5"+
		"\u00b8\u0001\u0000\u0000\u0000\u00b6\u00b4\u0001\u0000\u0000\u0000\u00b6"+
		"\u00b7\u0001\u0000\u0000\u0000\u00b7\u000f\u0001\u0000\u0000\u0000\u00b8"+
		"\u00b6\u0001\u0000\u0000\u0000\u00b9\u00be\u0003\u0014\n\u0000\u00ba\u00bb"+
		"\u00057\u0000\u0000\u00bb\u00bd\u0003\u0014\n\u0000\u00bc\u00ba\u0001"+
		"\u0000\u0000\u0000\u00bd\u00c0\u0001\u0000\u0000\u0000\u00be\u00bc\u0001"+
		"\u0000\u0000\u0000\u00be\u00bf\u0001\u0000\u0000\u0000\u00bf\u0011\u0001"+
		"\u0000\u0000\u0000\u00c0\u00be\u0001\u0000\u0000\u0000\u00c1\u00c2\u0003"+
		"\b\u0004\u0000\u00c2\u0013\u0001\u0000\u0000\u0000\u00c3\u00c4\u0005\u001f"+
		"\u0000\u0000\u00c4\u00c6\u0007\u0001\u0000\u0000\u00c5\u00c7\u0003\"\u0011"+
		"\u0000\u00c6\u00c5\u0001\u0000\u0000\u0000\u00c6\u00c7\u0001\u0000\u0000"+
		"\u0000\u00c7\u0015\u0001\u0000\u0000\u0000\u00c8\u00c9\u0005\t\u0000\u0000"+
		"\u00c9\u00cb\u0005*\u0000\u0000\u00ca\u00cc\u0003\u0014\n\u0000\u00cb"+
		"\u00ca\u0001\u0000\u0000\u0000\u00cb\u00cc\u0001\u0000\u0000\u0000\u00cc"+
		"\u00ce\u0001\u0000\u0000\u0000\u00cd\u00cf\u0003\u0018\f\u0000\u00ce\u00cd"+
		"\u0001\u0000\u0000\u0000\u00ce\u00cf\u0001\u0000\u0000\u0000\u00cf\u00d1"+
		"\u0001\u0000\u0000\u0000\u00d0\u00d2\u0003$\u0012\u0000\u00d1\u00d0\u0001"+
		"\u0000\u0000\u0000\u00d1\u00d2\u0001\u0000\u0000\u0000\u00d2\u00d4\u0001"+
		"\u0000\u0000\u0000\u00d3\u00d5\u0003&\u0013\u0000\u00d4\u00d3\u0001\u0000"+
		"\u0000\u0000\u00d4\u00d5\u0001\u0000\u0000\u0000\u00d5\u00d6\u0001\u0000"+
		"\u0000\u0000\u00d6\u00d8\u0003(\u0014\u0000\u00d7\u00d9\u0003\u001c\u000e"+
		"\u0000\u00d8\u00d7\u0001\u0000\u0000\u0000\u00d8\u00d9\u0001\u0000\u0000"+
		"\u0000\u00d9\u00dd\u0001\u0000\u0000\u0000\u00da\u00dc\u0003\u0016\u000b"+
		"\u0000\u00db\u00da\u0001\u0000\u0000\u0000\u00dc\u00df\u0001\u0000\u0000"+
		"\u0000\u00dd\u00db\u0001\u0000\u0000\u0000\u00dd\u00de\u0001\u0000\u0000"+
		"\u0000\u00de\u00e0\u0001\u0000\u0000\u0000\u00df\u00dd\u0001\u0000\u0000"+
		"\u0000\u00e0\u00e1\u0005+\u0000\u0000\u00e1\u0017\u0001\u0000\u0000\u0000"+
		"\u00e2\u00e3\u0005\u0001\u0000\u0000\u00e3\u00e4\u0003\b\u0004\u0000\u00e4"+
		"\u0019\u0001\u0000\u0000\u0000\u00e5\u00e6\u0005\u0016\u0000\u0000\u00e6"+
		"\u00e7\u00059\u0000\u0000\u00e7\u00e8\u00050\u0000\u0000\u00e8\u00e9\u0005"+
		"3\u0000\u0000\u00e9\u001b\u0001\u0000\u0000\u0000\u00ea\u00eb\u0005\u000f"+
		"\u0000\u0000\u00eb\u00ec\u0003\u001e\u000f\u0000\u00ec\u001d\u0001\u0000"+
		"\u0000\u0000\u00ed\u00f2\u0003 \u0010\u0000\u00ee\u00ef\u00057\u0000\u0000"+
		"\u00ef\u00f1\u0003 \u0010\u0000\u00f0\u00ee\u0001\u0000\u0000\u0000\u00f1"+
		"\u00f4\u0001\u0000\u0000\u0000\u00f2\u00f0\u0001\u0000\u0000\u0000\u00f2"+
		"\u00f3\u0001\u0000\u0000\u0000\u00f3\u001f\u0001\u0000\u0000\u0000\u00f4"+
		"\u00f2\u0001\u0000\u0000\u0000\u00f5\u00f8\u0005*\u0000\u0000\u00f6\u00f9"+
		"\u0003\u0014\n\u0000\u00f7\u00f9\u00059\u0000\u0000\u00f8\u00f6\u0001"+
		"\u0000\u0000\u0000\u00f8\u00f7\u0001\u0000\u0000\u0000\u00f9\u00fb\u0001"+
		"\u0000\u0000\u0000\u00fa\u00fc\u00036\u001b\u0000\u00fb\u00fa\u0001\u0000"+
		"\u0000\u0000\u00fb\u00fc\u0001\u0000\u0000\u0000\u00fc\u00fe\u0001\u0000"+
		"\u0000\u0000\u00fd\u00ff\u0003\u001c\u000e\u0000\u00fe\u00fd\u0001\u0000"+
		"\u0000\u0000\u00fe\u00ff\u0001\u0000\u0000\u0000\u00ff\u0100\u0001\u0000"+
		"\u0000\u0000\u0100\u0101\u0005+\u0000\u0000\u0101!\u0001\u0000\u0000\u0000"+
		"\u0102\u0103\u0005\u0019\u0000\u0000\u0103#\u0001\u0000\u0000\u0000\u0104"+
		"\u0105\u0005\u0018\u0000\u0000\u0105\u0106\u0003\b\u0004\u0000\u0106%"+
		"\u0001\u0000\u0000\u0000\u0107\u0108\u0005\u001b\u0000\u0000\u0108\'\u0001"+
		"\u0000\u0000\u0000\u0109\u010d\u0005\n\u0000\u0000\u010a\u010e\u00034"+
		"\u001a\u0000\u010b\u010e\u0003,\u0016\u0000\u010c\u010e\u0003*\u0015\u0000"+
		"\u010d\u010a\u0001\u0000\u0000\u0000\u010d\u010b\u0001\u0000\u0000\u0000"+
		"\u010d\u010c\u0001\u0000\u0000\u0000\u010e)\u0001\u0000\u0000\u0000\u010f"+
		"\u0112\u0005*\u0000\u0000\u0110\u0113\u0003,\u0016\u0000\u0111\u0113\u0003"+
		"4\u001a\u0000\u0112\u0110\u0001\u0000\u0000\u0000\u0112\u0111\u0001\u0000"+
		"\u0000\u0000\u0113\u0114\u0001\u0000\u0000\u0000\u0114\u0115\u0005*\u0000"+
		"\u0000\u0115+\u0001\u0000\u0000\u0000\u0116\u011a\u00032\u0019\u0000\u0117"+
		"\u011a\u00030\u0018\u0000\u0118\u011a\u0003.\u0017\u0000\u0119\u0116\u0001"+
		"\u0000\u0000\u0000\u0119\u0117\u0001\u0000\u0000\u0000\u0119\u0118\u0001"+
		"\u0000\u0000\u0000\u011a-\u0001\u0000\u0000\u0000\u011b\u011e\u00034\u001a"+
		"\u0000\u011c\u011e\u0003*\u0015\u0000\u011d\u011b\u0001\u0000\u0000\u0000"+
		"\u011d\u011c\u0001\u0000\u0000\u0000\u011e\u0124\u0001\u0000\u0000\u0000"+
		"\u011f\u0122\u0005(\u0000\u0000\u0120\u0123\u00034\u001a\u0000\u0121\u0123"+
		"\u0003*\u0015\u0000\u0122\u0120\u0001\u0000\u0000\u0000\u0122\u0121\u0001"+
		"\u0000\u0000\u0000\u0123\u0125\u0001\u0000\u0000\u0000\u0124\u011f\u0001"+
		"\u0000\u0000\u0000\u0125\u0126\u0001\u0000\u0000\u0000\u0126\u0124\u0001"+
		"\u0000\u0000\u0000\u0126\u0127\u0001\u0000\u0000\u0000\u0127/\u0001\u0000"+
		"\u0000\u0000\u0128\u012b\u00034\u001a\u0000\u0129\u012b\u0003*\u0015\u0000"+
		"\u012a\u0128\u0001\u0000\u0000\u0000\u012a\u0129\u0001\u0000\u0000\u0000"+
		"\u012b\u0131\u0001\u0000\u0000\u0000\u012c\u012f\u0005\'\u0000\u0000\u012d"+
		"\u0130\u00034\u001a\u0000\u012e\u0130\u0003*\u0015\u0000\u012f\u012d\u0001"+
		"\u0000\u0000\u0000\u012f\u012e\u0001\u0000\u0000\u0000\u0130\u0132\u0001"+
		"\u0000\u0000\u0000\u0131\u012c\u0001\u0000\u0000\u0000\u0132\u0133\u0001"+
		"\u0000\u0000\u0000\u0133\u0131\u0001\u0000\u0000\u0000\u0133\u0134\u0001"+
		"\u0000\u0000\u0000\u01341\u0001\u0000\u0000\u0000\u0135\u0138\u00034\u001a"+
		"\u0000\u0136\u0138\u0003*\u0015\u0000\u0137\u0135\u0001\u0000\u0000\u0000"+
		"\u0137\u0136\u0001\u0000\u0000\u0000\u0138\u013e\u0001\u0000\u0000\u0000"+
		"\u0139\u013c\u0005&\u0000\u0000\u013a\u013d\u00034\u001a\u0000\u013b\u013d"+
		"\u0003*\u0015\u0000\u013c\u013a\u0001\u0000\u0000\u0000\u013c\u013b\u0001"+
		"\u0000\u0000\u0000\u013d\u013f\u0001\u0000\u0000\u0000\u013e\u0139\u0001"+
		"\u0000\u0000\u0000\u013f\u0140\u0001\u0000\u0000\u0000\u0140\u013e\u0001"+
		"\u0000\u0000\u0000\u0140\u0141\u0001\u0000\u0000\u0000\u01413\u0001\u0000"+
		"\u0000\u0000\u0142\u0144\u0005*\u0000\u0000\u0143\u0145\u0003$\u0012\u0000"+
		"\u0144\u0143\u0001\u0000\u0000\u0000\u0144\u0145\u0001\u0000\u0000\u0000"+
		"\u0145\u0147\u0001\u0000\u0000\u0000\u0146\u0148\u0003r9\u0000\u0147\u0146"+
		"\u0001\u0000\u0000\u0000\u0147\u0148\u0001\u0000\u0000\u0000\u0148\u014a"+
		"\u0001\u0000\u0000\u0000\u0149\u014b\u0003T*\u0000\u014a\u0149\u0001\u0000"+
		"\u0000\u0000\u014a\u014b\u0001\u0000\u0000\u0000\u014b\u014d\u0001\u0000"+
		"\u0000\u0000\u014c\u014e\u00036\u001b\u0000\u014d\u014c\u0001\u0000\u0000"+
		"\u0000\u014d\u014e\u0001\u0000\u0000\u0000\u014e\u014f\u0001\u0000\u0000"+
		"\u0000\u014f\u0150\u0005+\u0000\u0000\u01505\u0001\u0000\u0000\u0000\u0151"+
		"\u0152\u0005\f\u0000\u0000\u0152\u0153\u00038\u001c\u0000\u01537\u0001"+
		"\u0000\u0000\u0000\u0154\u0158\u0003:\u001d\u0000\u0155\u0158\u0003@ "+
		"\u0000\u0156\u0158\u0003H$\u0000\u0157\u0154\u0001\u0000\u0000\u0000\u0157"+
		"\u0155\u0001\u0000\u0000\u0000\u0157\u0156\u0001\u0000\u0000\u0000\u0158"+
		"9\u0001\u0000\u0000\u0000\u0159\u015b\u0005*\u0000\u0000\u015a\u015c\u0003"+
		"J%\u0000\u015b\u015a\u0001\u0000\u0000\u0000\u015b\u015c\u0001\u0000\u0000"+
		"\u0000\u015c\u015e\u0001\u0000\u0000\u0000\u015d\u015f\u0003$\u0012\u0000"+
		"\u015e\u015d\u0001\u0000\u0000\u0000\u015e\u015f\u0001\u0000\u0000\u0000"+
		"\u015f\u0161\u0001\u0000\u0000\u0000\u0160\u0162\u0003<\u001e\u0000\u0161"+
		"\u0160\u0001\u0000\u0000\u0000\u0161\u0162\u0001\u0000\u0000\u0000\u0162"+
		"\u0163\u0001\u0000\u0000\u0000\u0163\u0165\u0003T*\u0000\u0164\u0166\u0003"+
		"L&\u0000\u0165\u0164\u0001\u0000\u0000\u0000\u0165\u0166\u0001\u0000\u0000"+
		"\u0000\u0166\u0168\u0001\u0000\u0000\u0000\u0167\u0169\u00036\u001b\u0000"+
		"\u0168\u0167\u0001\u0000\u0000\u0000\u0168\u0169\u0001\u0000\u0000\u0000"+
		"\u0169\u016a\u0001\u0000\u0000\u0000\u016a\u016b\u0005+\u0000\u0000\u016b"+
		";\u0001\u0000\u0000\u0000\u016c\u016d\u0005\r\u0000\u0000\u016d=\u0001"+
		"\u0000\u0000\u0000\u016e\u016f\u0005A\u0000\u0000\u016f\u0170\u0003\b"+
		"\u0004\u0000\u0170?\u0001\u0000\u0000\u0000\u0171\u0175\u0003F#\u0000"+
		"\u0172\u0175\u0003D\"\u0000\u0173\u0175\u0003B!\u0000\u0174\u0171\u0001"+
		"\u0000\u0000\u0000\u0174\u0172\u0001\u0000\u0000\u0000\u0174\u0173\u0001"+
		"\u0000\u0000\u0000\u0175A\u0001\u0000\u0000\u0000\u0176\u0179\u0005(\u0000"+
		"\u0000\u0177\u017a\u0003:\u001d\u0000\u0178\u017a\u0003H$\u0000\u0179"+
		"\u0177\u0001\u0000\u0000\u0000\u0179\u0178\u0001\u0000\u0000\u0000\u017a"+
		"\u017c\u0001\u0000\u0000\u0000\u017b\u0176\u0001\u0000\u0000\u0000\u017c"+
		"\u017d\u0001\u0000\u0000\u0000\u017d\u017b\u0001\u0000\u0000\u0000\u017d"+
		"\u017e\u0001\u0000\u0000\u0000\u017eC\u0001\u0000\u0000\u0000\u017f\u0182"+
		"\u0003:\u001d\u0000\u0180\u0182\u0003H$\u0000\u0181\u017f\u0001\u0000"+
		"\u0000\u0000\u0181\u0180\u0001\u0000\u0000\u0000\u0182\u0188\u0001\u0000"+
		"\u0000\u0000\u0183\u0186\u0005\'\u0000\u0000\u0184\u0187\u0003:\u001d"+
		"\u0000\u0185\u0187\u0003H$\u0000\u0186\u0184\u0001\u0000\u0000\u0000\u0186"+
		"\u0185\u0001\u0000\u0000\u0000\u0187\u0189\u0001\u0000\u0000\u0000\u0188"+
		"\u0183\u0001\u0000\u0000\u0000\u0189\u018a\u0001\u0000\u0000\u0000\u018a"+
		"\u0188\u0001\u0000\u0000\u0000\u018a\u018b\u0001\u0000\u0000\u0000\u018b"+
		"E\u0001\u0000\u0000\u0000\u018c\u018f\u0003:\u001d\u0000\u018d\u018f\u0003"+
		"H$\u0000\u018e\u018c\u0001\u0000\u0000\u0000\u018e\u018d\u0001\u0000\u0000"+
		"\u0000\u018f\u0195\u0001\u0000\u0000\u0000\u0190\u0193\u0005&\u0000\u0000"+
		"\u0191\u0194\u0003:\u001d\u0000\u0192\u0194\u0003H$\u0000\u0193\u0191"+
		"\u0001\u0000\u0000\u0000\u0193\u0192\u0001\u0000\u0000\u0000\u0194\u0196"+
		"\u0001\u0000\u0000\u0000\u0195\u0190\u0001\u0000\u0000\u0000\u0196\u0197"+
		"\u0001\u0000\u0000\u0000\u0197\u0195\u0001\u0000\u0000\u0000\u0197\u0198"+
		"\u0001\u0000\u0000\u0000\u0198G\u0001\u0000\u0000\u0000\u0199\u019c\u0005"+
		"*\u0000\u0000\u019a\u019d\u0003@ \u0000\u019b\u019d\u0003:\u001d\u0000"+
		"\u019c\u019a\u0001\u0000\u0000\u0000\u019c\u019b\u0001\u0000\u0000\u0000"+
		"\u019d\u019e\u0001\u0000\u0000\u0000\u019e\u019f\u0005+\u0000\u0000\u019f"+
		"I\u0001\u0000\u0000\u0000\u01a0\u01a1\u0005\u000e\u0000\u0000\u01a1\u01a4"+
		"\u0005*\u0000\u0000\u01a2\u01a5\u0003:\u001d\u0000\u01a3\u01a5\u0003@"+
		" \u0000\u01a4\u01a2\u0001\u0000\u0000\u0000\u01a4\u01a3\u0001\u0000\u0000"+
		"\u0000\u01a5\u01a6\u0001\u0000\u0000\u0000\u01a6\u01a7\u0003f3\u0000\u01a7"+
		"\u01a8\u0005+\u0000\u0000\u01a8K\u0001\u0000\u0000\u0000\u01a9\u01ac\u0003"+
		"N\'\u0000\u01aa\u01ac\u0003P(\u0000\u01ab\u01a9\u0001\u0000\u0000\u0000"+
		"\u01ab\u01aa\u0001\u0000\u0000\u0000\u01ac\u01b0\u0001\u0000\u0000\u0000"+
		"\u01ad\u01b0\u0003X,\u0000\u01ae\u01b0\u0003^/\u0000\u01af\u01ab\u0001"+
		"\u0000\u0000\u0000\u01af\u01ad\u0001\u0000\u0000\u0000\u01af\u01ae\u0001"+
		"\u0000\u0000\u0000\u01b0M\u0001\u0000\u0000\u0000\u01b1\u01b2\u0005\u001c"+
		"\u0000\u0000\u01b2\u01b3\u0003R)\u0000\u01b3O\u0001\u0000\u0000\u0000"+
		"\u01b4\u01b5\u0005@\u0000\u0000\u01b5\u01b6\u0003R)\u0000\u01b6Q\u0001"+
		"\u0000\u0000\u0000\u01b7\u01bb\u00034\u001a\u0000\u01b8\u01bb\u0003*\u0015"+
		"\u0000\u01b9\u01bb\u0003,\u0016\u0000\u01ba\u01b7\u0001\u0000\u0000\u0000"+
		"\u01ba\u01b8\u0001\u0000\u0000\u0000\u01ba\u01b9\u0001\u0000\u0000\u0000"+
		"\u01bbS\u0001\u0000\u0000\u0000\u01bc\u01be\u0003V+\u0000\u01bd\u01bc"+
		"\u0001\u0000\u0000\u0000\u01bd\u01be\u0001\u0000\u0000\u0000\u01be\u01bf"+
		"\u0001\u0000\u0000\u0000\u01bf\u01cb\u0003t:\u0000\u01c0\u01c2\u0003v"+
		";\u0000\u01c1\u01c0\u0001\u0000\u0000\u0000\u01c1\u01c2\u0001\u0000\u0000"+
		"\u0000\u01c2\u01c3\u0001\u0000\u0000\u0000\u01c3\u01c5\u0007\u0001\u0000"+
		"\u0000\u01c4\u01c6\u0003\"\u0011\u0000\u01c5\u01c4\u0001\u0000\u0000\u0000"+
		"\u01c5\u01c6\u0001\u0000\u0000\u0000\u01c6\u01cc\u0001\u0000\u0000\u0000"+
		"\u01c7\u01c9\u0003v;\u0000\u01c8\u01c7\u0001\u0000\u0000\u0000\u01c8\u01c9"+
		"\u0001\u0000\u0000\u0000\u01c9\u01ca\u0001\u0000\u0000\u0000\u01ca\u01cc"+
		"\u0003\u0080@\u0000\u01cb\u01c1\u0001\u0000\u0000\u0000\u01cb\u01c8\u0001"+
		"\u0000\u0000\u0000\u01cc\u01ce\u0001\u0000\u0000\u0000\u01cd\u01cf\u0003"+
		"\u0082A\u0000\u01ce\u01cd\u0001\u0000\u0000\u0000\u01ce\u01cf\u0001\u0000"+
		"\u0000\u0000\u01cfU\u0001\u0000\u0000\u0000\u01d0\u01d1\u0005\u0002\u0000"+
		"\u0000\u01d1W\u0001\u0000\u0000\u0000\u01d2\u01d3\u0003Z-\u0000\u01d3"+
		"\u01d4\u0003\\.\u0000\u01d4\u01d8\u0001\u0000\u0000\u0000\u01d5\u01d8"+
		"\u0003Z-\u0000\u01d6\u01d8\u0003\\.\u0000\u01d7\u01d2\u0001\u0000\u0000"+
		"\u0000\u01d7\u01d5\u0001\u0000\u0000\u0000\u01d7\u01d6\u0001\u0000\u0000"+
		"\u0000\u01d8Y\u0001\u0000\u0000\u0000\u01d9\u01da\u0005\n\u0000\u0000"+
		"\u01da\u01db\u0005*\u0000\u0000\u01db\u01dc\u0003^/\u0000\u01dc\u01dd"+
		"\u0005+\u0000\u0000\u01dd[\u0001\u0000\u0000\u0000\u01de\u01df\u0005)"+
		"\u0000\u0000\u01df\u01e0\u0005*\u0000\u0000\u01e0\u01e1\u0003^/\u0000"+
		"\u01e1\u01e2\u0005+\u0000\u0000\u01e2]\u0001\u0000\u0000\u0000\u01e3\u01e6"+
		"\u0003b1\u0000\u01e4\u01e7\u0003\b\u0004\u0000\u01e5\u01e7\u0005?\u0000"+
		"\u0000\u01e6\u01e4\u0001\u0000\u0000\u0000\u01e6\u01e5\u0001\u0000\u0000"+
		"\u0000\u01e7\u01e9\u0001\u0000\u0000\u0000\u01e8\u01ea\u0003d2\u0000\u01e9"+
		"\u01e8\u0001\u0000\u0000\u0000\u01e9\u01ea\u0001\u0000\u0000\u0000\u01ea"+
		"\u01ec\u0001\u0000\u0000\u0000\u01eb\u01ed\u0003`0\u0000\u01ec\u01eb\u0001"+
		"\u0000\u0000\u0000\u01ec\u01ed\u0001\u0000\u0000\u0000\u01ed_\u0001\u0000"+
		"\u0000\u0000\u01ee\u01ef\u0005\u0003\u0000\u0000\u01ef\u01f2\u0005:\u0000"+
		"\u0000\u01f0\u01f2\u00059\u0000\u0000\u01f1\u01ee\u0001\u0000\u0000\u0000"+
		"\u01f1\u01f0\u0001\u0000\u0000\u0000\u01f2a\u0001\u0000\u0000\u0000\u01f3"+
		"\u01f4\u0007\u0002\u0000\u0000\u01f4c\u0001\u0000\u0000\u0000\u01f5\u01f6"+
		"\u0005=\u0000\u0000\u01f6e\u0001\u0000\u0000\u0000\u01f7\u01fe\u0003h"+
		"4\u0000\u01f8\u01fe\u0003j5\u0000\u01f9\u01fe\u0003l6\u0000\u01fa\u01fb"+
		"\u0003n7\u0000\u01fb\u01fc\u0003p8\u0000\u01fc\u01fe\u0001\u0000\u0000"+
		"\u0000\u01fd\u01f7\u0001\u0000\u0000\u0000\u01fd\u01f8\u0001\u0000\u0000"+
		"\u0000\u01fd\u01f9\u0001\u0000\u0000\u0000\u01fd\u01fa\u0001\u0000\u0000"+
		"\u0000\u01feg\u0001\u0000\u0000\u0000\u01ff\u0200\u0005\u0011\u0000\u0000"+
		"\u0200\u0201\u00059\u0000\u0000\u0201i\u0001\u0000\u0000\u0000\u0202\u0203"+
		"\u0005\u0010\u0000\u0000\u0203\u0204\u00059\u0000\u0000\u0204k\u0001\u0000"+
		"\u0000\u0000\u0205\u0206\u0005\u0012\u0000\u0000\u0206\u0207\u00059\u0000"+
		"\u0000\u0207m\u0001\u0000\u0000\u0000\u0208\u0209\u0005\u0013\u0000\u0000"+
		"\u0209\u020a\u00059\u0000\u0000\u020ao\u0001\u0000\u0000\u0000\u020b\u020c"+
		"\u0005\u0014\u0000\u0000\u020c\u020d\u0005>\u0000\u0000\u020dq\u0001\u0000"+
		"\u0000\u0000\u020e\u020f\u0005\u000b\u0000\u0000\u020f\u0210\u00053\u0000"+
		"\u0000\u0210s\u0001\u0000\u0000\u0000\u0211\u0212\u0007\u0003\u0000\u0000"+
		"\u0212u\u0001\u0000\u0000\u0000\u0213\u0218\u0003~?\u0000\u0214\u0218"+
		"\u0003|>\u0000\u0215\u0218\u0003z=\u0000\u0216\u0218\u0003x<\u0000\u0217"+
		"\u0213\u0001\u0000\u0000\u0000\u0217\u0214\u0001\u0000\u0000\u0000\u0217"+
		"\u0215\u0001\u0000\u0000\u0000\u0217\u0216\u0001\u0000\u0000\u0000\u0218"+
		"w\u0001\u0000\u0000\u0000\u0219\u021a\u0005\u0004\u0000\u0000\u021ay\u0001"+
		"\u0000\u0000\u0000\u021b\u021c\u0005\u0005\u0000\u0000\u021c{\u0001\u0000"+
		"\u0000\u0000\u021d\u021e\u0005\"\u0000\u0000\u021e}\u0001\u0000\u0000"+
		"\u0000\u021f\u0220\u0005\u0006\u0000\u0000\u0220\u007f\u0001\u0000\u0000"+
		"\u0000\u0221\u0222\u0005:\u0000\u0000\u0222\u0081\u0001\u0000\u0000\u0000"+
		"\u0223\u0224\u0005\u001a\u0000\u0000\u0224\u0225\u00050\u0000\u0000\u0225"+
		"\u0226\u00059\u0000\u0000\u0226\u0083\u0001\u0000\u0000\u0000C\u0088\u008c"+
		"\u008f\u009f\u00ad\u00b6\u00be\u00c6\u00cb\u00ce\u00d1\u00d4\u00d8\u00dd"+
		"\u00f2\u00f8\u00fb\u00fe\u010d\u0112\u0119\u011d\u0122\u0126\u012a\u012f"+
		"\u0133\u0137\u013c\u0140\u0144\u0147\u014a\u014d\u0157\u015b\u015e\u0161"+
		"\u0165\u0168\u0174\u0179\u017d\u0181\u0186\u018a\u018e\u0193\u0197\u019c"+
		"\u01a4\u01ab\u01af\u01ba\u01bd\u01c1\u01c5\u01c8\u01cb\u01ce\u01d7\u01e6"+
		"\u01e9\u01ec\u01f1\u01fd\u0217";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}