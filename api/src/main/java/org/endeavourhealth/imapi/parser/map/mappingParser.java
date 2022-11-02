// Generated from C:/Users/david/CloudStation/EhealthTrust/DiscoveryDataService/IMAPI/api/src/main/grammars\mapping.g4 by ANTLR 4.10.1
package org.endeavourhealth.imapi.parser.map;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class mappingParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, BOOL=40, DATE=41, DATETIME=42, TIME=43, IDENTIFIER=44, DELIMITEDIDENTIFIER=45, 
		STRING=46, INTEGER=47, NUMBER=48, WS=49, COMMENT=50, LINE_COMMENT=51;
	public static final int
		RULE_structureMap = 0, RULE_mapId = 1, RULE_url = 2, RULE_identifier = 3, 
		RULE_structure = 4, RULE_structureAlias = 5, RULE_imports = 6, RULE_group = 7, 
		RULE_rules = 8, RULE_typeMode = 9, RULE_extends = 10, RULE_parameters = 11, 
		RULE_parameter = 12, RULE_type = 13, RULE_rule = 14, RULE_ruleName = 15, 
		RULE_ruleSources = 16, RULE_ruleSource = 17, RULE_ruleTargets = 18, RULE_sourceType = 19, 
		RULE_upperBound = 20, RULE_ruleContext = 21, RULE_sourceDefault = 22, 
		RULE_alias = 23, RULE_whereClause = 24, RULE_checkClause = 25, RULE_log = 26, 
		RULE_dependent = 27, RULE_ruleTarget = 28, RULE_transform = 29, RULE_invocation = 30, 
		RULE_paramList = 31, RULE_param = 32, RULE_fhirPath = 33, RULE_literal = 34, 
		RULE_groupTypeMode = 35, RULE_sourceListMode = 36, RULE_targetListMode = 37, 
		RULE_inputMode = 38, RULE_modelMode = 39;
	private static String[] makeRuleNames() {
		return new String[] {
			"structureMap", "mapId", "url", "identifier", "structure", "structureAlias", 
			"imports", "group", "rules", "typeMode", "extends", "parameters", "parameter", 
			"type", "rule", "ruleName", "ruleSources", "ruleSource", "ruleTargets", 
			"sourceType", "upperBound", "ruleContext", "sourceDefault", "alias", 
			"whereClause", "checkClause", "log", "dependent", "ruleTarget", "transform", 
			"invocation", "paramList", "param", "fhirPath", "literal", "groupTypeMode", 
			"sourceListMode", "targetListMode", "inputMode", "modelMode"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'map'", "'='", "'uses'", "'as'", "'alias'", "'imports'", "'group'", 
			"'{'", "'}'", "'<<'", "'>>'", "'extends'", "'('", "','", "')'", "':'", 
			"'->'", "';'", "'..'", "'*'", "'.'", "'default'", "'where'", "'check'", 
			"'log'", "'then'", "'types'", "'type+'", "'first'", "'not_first'", "'last'", 
			"'not_last'", "'only_one'", "'share'", "'collate'", "'source'", "'target'", 
			"'queried'", "'produced'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, "BOOL", "DATE", "DATETIME", "TIME", "IDENTIFIER", 
			"DELIMITEDIDENTIFIER", "STRING", "INTEGER", "NUMBER", "WS", "COMMENT", 
			"LINE_COMMENT"
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
	public String getGrammarFileName() { return "mapping.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public mappingParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class StructureMapContext extends ParserRuleContext {
		public MapIdContext mapId() {
			return getRuleContext(MapIdContext.class,0);
		}
		public TerminalNode EOF() { return getToken(mappingParser.EOF, 0); }
		public List<StructureContext> structure() {
			return getRuleContexts(StructureContext.class);
		}
		public StructureContext structure(int i) {
			return getRuleContext(StructureContext.class,i);
		}
		public List<ImportsContext> imports() {
			return getRuleContexts(ImportsContext.class);
		}
		public ImportsContext imports(int i) {
			return getRuleContext(ImportsContext.class,i);
		}
		public List<GroupContext> group() {
			return getRuleContexts(GroupContext.class);
		}
		public GroupContext group(int i) {
			return getRuleContext(GroupContext.class,i);
		}
		public StructureMapContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structureMap; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterStructureMap(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitStructureMap(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitStructureMap(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StructureMapContext structureMap() throws RecognitionException {
		StructureMapContext _localctx = new StructureMapContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_structureMap);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			mapId();
			setState(84);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(81);
				structure();
				}
				}
				setState(86);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(90);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(87);
				imports();
				}
				}
				setState(92);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(94); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(93);
				group();
				}
				}
				setState(96); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__6 );
			setState(98);
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

	public static class MapIdContext extends ParserRuleContext {
		public UrlContext url() {
			return getRuleContext(UrlContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public MapIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterMapId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitMapId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitMapId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapIdContext mapId() throws RecognitionException {
		MapIdContext _localctx = new MapIdContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_mapId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			match(T__0);
			setState(101);
			url();
			setState(102);
			match(T__1);
			setState(103);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UrlContext extends ParserRuleContext {
		public TerminalNode DELIMITEDIDENTIFIER() { return getToken(mappingParser.DELIMITEDIDENTIFIER, 0); }
		public UrlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_url; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterUrl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitUrl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitUrl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UrlContext url() throws RecognitionException {
		UrlContext _localctx = new UrlContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_url);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			match(DELIMITEDIDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(mappingParser.IDENTIFIER, 0); }
		public TerminalNode DELIMITEDIDENTIFIER() { return getToken(mappingParser.DELIMITEDIDENTIFIER, 0); }
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_identifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			_la = _input.LA(1);
			if ( !(_la==IDENTIFIER || _la==DELIMITEDIDENTIFIER) ) {
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

	public static class StructureContext extends ParserRuleContext {
		public UrlContext url() {
			return getRuleContext(UrlContext.class,0);
		}
		public ModelModeContext modelMode() {
			return getRuleContext(ModelModeContext.class,0);
		}
		public StructureAliasContext structureAlias() {
			return getRuleContext(StructureAliasContext.class,0);
		}
		public StructureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structure; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterStructure(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitStructure(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitStructure(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StructureContext structure() throws RecognitionException {
		StructureContext _localctx = new StructureContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_structure);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(109);
			match(T__2);
			setState(110);
			url();
			setState(112);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(111);
				structureAlias();
				}
			}

			setState(114);
			match(T__3);
			setState(115);
			modelMode();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StructureAliasContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public StructureAliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structureAlias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterStructureAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitStructureAlias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitStructureAlias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StructureAliasContext structureAlias() throws RecognitionException {
		StructureAliasContext _localctx = new StructureAliasContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_structureAlias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			match(T__4);
			setState(118);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImportsContext extends ParserRuleContext {
		public UrlContext url() {
			return getRuleContext(UrlContext.class,0);
		}
		public ImportsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_imports; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterImports(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitImports(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitImports(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportsContext imports() throws RecognitionException {
		ImportsContext _localctx = new ImportsContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_imports);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			match(T__5);
			setState(121);
			url();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GroupContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ParametersContext parameters() {
			return getRuleContext(ParametersContext.class,0);
		}
		public RulesContext rules() {
			return getRuleContext(RulesContext.class,0);
		}
		public ExtendsContext extends_() {
			return getRuleContext(ExtendsContext.class,0);
		}
		public TypeModeContext typeMode() {
			return getRuleContext(TypeModeContext.class,0);
		}
		public GroupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_group; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitGroup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupContext group() throws RecognitionException {
		GroupContext _localctx = new GroupContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_group);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(123);
			match(T__6);
			setState(124);
			identifier();
			setState(125);
			parameters();
			setState(127);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__11) {
				{
				setState(126);
				extends_();
				}
			}

			setState(130);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__9) {
				{
				setState(129);
				typeMode();
				}
			}

			setState(132);
			rules();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RulesContext extends ParserRuleContext {
		public List<RuleContext> rule_() {
			return getRuleContexts(RuleContext.class);
		}
		public RuleContext rule_(int i) {
			return getRuleContext(RuleContext.class,i);
		}
		public RulesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rules; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterRules(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitRules(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitRules(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RulesContext rules() throws RecognitionException {
		RulesContext _localctx = new RulesContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_rules);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(134);
			match(T__7);
			setState(138);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER || _la==DELIMITEDIDENTIFIER) {
				{
				{
				setState(135);
				rule_();
				}
				}
				setState(140);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(141);
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

	public static class TypeModeContext extends ParserRuleContext {
		public GroupTypeModeContext groupTypeMode() {
			return getRuleContext(GroupTypeModeContext.class,0);
		}
		public TypeModeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeMode; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterTypeMode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitTypeMode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitTypeMode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeModeContext typeMode() throws RecognitionException {
		TypeModeContext _localctx = new TypeModeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_typeMode);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
			match(T__9);
			setState(144);
			groupTypeMode();
			setState(145);
			match(T__10);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExtendsContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ExtendsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_extends; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterExtends(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitExtends(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitExtends(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExtendsContext extends_() throws RecognitionException {
		ExtendsContext _localctx = new ExtendsContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_extends);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(147);
			match(T__11);
			setState(148);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParametersContext extends ParserRuleContext {
		public List<ParameterContext> parameter() {
			return getRuleContexts(ParameterContext.class);
		}
		public ParameterContext parameter(int i) {
			return getRuleContext(ParameterContext.class,i);
		}
		public ParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitParameters(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitParameters(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParametersContext parameters() throws RecognitionException {
		ParametersContext _localctx = new ParametersContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_parameters);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150);
			match(T__12);
			setState(151);
			parameter();
			setState(154); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(152);
				match(T__13);
				setState(153);
				parameter();
				}
				}
				setState(156); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__13 );
			setState(158);
			match(T__14);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParameterContext extends ParserRuleContext {
		public InputModeContext inputMode() {
			return getRuleContext(InputModeContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitParameter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitParameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterContext parameter() throws RecognitionException {
		ParameterContext _localctx = new ParameterContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_parameter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160);
			inputMode();
			setState(161);
			identifier();
			setState(163);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__15) {
				{
				setState(162);
				type();
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

	public static class TypeContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(165);
			match(T__15);
			setState(166);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RuleContext extends ParserRuleContext {
		public RuleSourcesContext ruleSources() {
			return getRuleContext(RuleSourcesContext.class,0);
		}
		public RuleTargetsContext ruleTargets() {
			return getRuleContext(RuleTargetsContext.class,0);
		}
		public DependentContext dependent() {
			return getRuleContext(DependentContext.class,0);
		}
		public RuleNameContext ruleName() {
			return getRuleContext(RuleNameContext.class,0);
		}
		public RuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitRule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitRule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleContext rule_() throws RecognitionException {
		RuleContext _localctx = new RuleContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_rule);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168);
			ruleSources();
			setState(171);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__16) {
				{
				setState(169);
				match(T__16);
				setState(170);
				ruleTargets();
				}
			}

			setState(174);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__25) {
				{
				setState(173);
				dependent();
				}
			}

			setState(177);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DELIMITEDIDENTIFIER) {
				{
				setState(176);
				ruleName();
				}
			}

			setState(179);
			match(T__17);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RuleNameContext extends ParserRuleContext {
		public TerminalNode DELIMITEDIDENTIFIER() { return getToken(mappingParser.DELIMITEDIDENTIFIER, 0); }
		public RuleNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterRuleName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitRuleName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitRuleName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleNameContext ruleName() throws RecognitionException {
		RuleNameContext _localctx = new RuleNameContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_ruleName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(181);
			match(DELIMITEDIDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RuleSourcesContext extends ParserRuleContext {
		public List<RuleSourceContext> ruleSource() {
			return getRuleContexts(RuleSourceContext.class);
		}
		public RuleSourceContext ruleSource(int i) {
			return getRuleContext(RuleSourceContext.class,i);
		}
		public RuleSourcesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleSources; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterRuleSources(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitRuleSources(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitRuleSources(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleSourcesContext ruleSources() throws RecognitionException {
		RuleSourcesContext _localctx = new RuleSourcesContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_ruleSources);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			ruleSource();
			setState(188);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__13) {
				{
				{
				setState(184);
				match(T__13);
				setState(185);
				ruleSource();
				}
				}
				setState(190);
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

	public static class RuleSourceContext extends ParserRuleContext {
		public RuleContextContext ruleContext() {
			return getRuleContext(RuleContextContext.class,0);
		}
		public SourceTypeContext sourceType() {
			return getRuleContext(SourceTypeContext.class,0);
		}
		public SourceDefaultContext sourceDefault() {
			return getRuleContext(SourceDefaultContext.class,0);
		}
		public SourceListModeContext sourceListMode() {
			return getRuleContext(SourceListModeContext.class,0);
		}
		public AliasContext alias() {
			return getRuleContext(AliasContext.class,0);
		}
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public CheckClauseContext checkClause() {
			return getRuleContext(CheckClauseContext.class,0);
		}
		public LogContext log() {
			return getRuleContext(LogContext.class,0);
		}
		public RuleSourceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleSource; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterRuleSource(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitRuleSource(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitRuleSource(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleSourceContext ruleSource() throws RecognitionException {
		RuleSourceContext _localctx = new RuleSourceContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_ruleSource);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(191);
			ruleContext();
			setState(193);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__15) {
				{
				setState(192);
				sourceType();
				}
			}

			setState(196);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__21) {
				{
				setState(195);
				sourceDefault();
				}
			}

			setState(199);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__28) | (1L << T__29) | (1L << T__30) | (1L << T__31) | (1L << T__32))) != 0)) {
				{
				setState(198);
				sourceListMode();
				}
			}

			setState(202);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(201);
				alias();
				}
			}

			setState(205);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__22) {
				{
				setState(204);
				whereClause();
				}
			}

			setState(208);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__23) {
				{
				setState(207);
				checkClause();
				}
			}

			setState(211);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__24) {
				{
				setState(210);
				log();
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

	public static class RuleTargetsContext extends ParserRuleContext {
		public List<RuleTargetContext> ruleTarget() {
			return getRuleContexts(RuleTargetContext.class);
		}
		public RuleTargetContext ruleTarget(int i) {
			return getRuleContext(RuleTargetContext.class,i);
		}
		public RuleTargetsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleTargets; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterRuleTargets(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitRuleTargets(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitRuleTargets(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleTargetsContext ruleTargets() throws RecognitionException {
		RuleTargetsContext _localctx = new RuleTargetsContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_ruleTargets);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(213);
			ruleTarget();
			setState(218);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__13) {
				{
				{
				setState(214);
				match(T__13);
				setState(215);
				ruleTarget();
				}
				}
				setState(220);
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

	public static class SourceTypeContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode INTEGER() { return getToken(mappingParser.INTEGER, 0); }
		public UpperBoundContext upperBound() {
			return getRuleContext(UpperBoundContext.class,0);
		}
		public SourceTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sourceType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterSourceType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitSourceType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitSourceType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SourceTypeContext sourceType() throws RecognitionException {
		SourceTypeContext _localctx = new SourceTypeContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_sourceType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			match(T__15);
			setState(222);
			identifier();
			setState(226);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==INTEGER) {
				{
				setState(223);
				match(INTEGER);
				setState(224);
				match(T__18);
				setState(225);
				upperBound();
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

	public static class UpperBoundContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(mappingParser.INTEGER, 0); }
		public UpperBoundContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_upperBound; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterUpperBound(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitUpperBound(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitUpperBound(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UpperBoundContext upperBound() throws RecognitionException {
		UpperBoundContext _localctx = new UpperBoundContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_upperBound);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(228);
			_la = _input.LA(1);
			if ( !(_la==T__19 || _la==INTEGER) ) {
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

	public static class RuleContextContext extends ParserRuleContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public RuleContextContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleContext; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterRuleContext(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitRuleContext(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitRuleContext(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleContextContext ruleContext() throws RecognitionException {
		RuleContextContext _localctx = new RuleContextContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_ruleContext);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(230);
			identifier();
			setState(233);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__20) {
				{
				setState(231);
				match(T__20);
				setState(232);
				identifier();
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

	public static class SourceDefaultContext extends ParserRuleContext {
		public FhirPathContext fhirPath() {
			return getRuleContext(FhirPathContext.class,0);
		}
		public SourceDefaultContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sourceDefault; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterSourceDefault(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitSourceDefault(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitSourceDefault(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SourceDefaultContext sourceDefault() throws RecognitionException {
		SourceDefaultContext _localctx = new SourceDefaultContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_sourceDefault);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(235);
			match(T__21);
			setState(236);
			match(T__12);
			setState(237);
			fhirPath();
			setState(238);
			match(T__14);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AliasContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public AliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitAlias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitAlias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AliasContext alias() throws RecognitionException {
		AliasContext _localctx = new AliasContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(240);
			match(T__3);
			setState(241);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WhereClauseContext extends ParserRuleContext {
		public FhirPathContext fhirPath() {
			return getRuleContext(FhirPathContext.class,0);
		}
		public WhereClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterWhereClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitWhereClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitWhereClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereClauseContext whereClause() throws RecognitionException {
		WhereClauseContext _localctx = new WhereClauseContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(243);
			match(T__22);
			setState(244);
			match(T__12);
			setState(245);
			fhirPath();
			setState(246);
			match(T__14);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CheckClauseContext extends ParserRuleContext {
		public FhirPathContext fhirPath() {
			return getRuleContext(FhirPathContext.class,0);
		}
		public CheckClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_checkClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterCheckClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitCheckClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitCheckClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CheckClauseContext checkClause() throws RecognitionException {
		CheckClauseContext _localctx = new CheckClauseContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_checkClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(248);
			match(T__23);
			setState(249);
			match(T__12);
			setState(250);
			fhirPath();
			setState(251);
			match(T__14);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LogContext extends ParserRuleContext {
		public FhirPathContext fhirPath() {
			return getRuleContext(FhirPathContext.class,0);
		}
		public LogContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_log; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterLog(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitLog(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitLog(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogContext log() throws RecognitionException {
		LogContext _localctx = new LogContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_log);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(253);
			match(T__24);
			setState(254);
			match(T__12);
			setState(255);
			fhirPath();
			setState(256);
			match(T__14);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DependentContext extends ParserRuleContext {
		public List<InvocationContext> invocation() {
			return getRuleContexts(InvocationContext.class);
		}
		public InvocationContext invocation(int i) {
			return getRuleContext(InvocationContext.class,i);
		}
		public RulesContext rules() {
			return getRuleContext(RulesContext.class,0);
		}
		public DependentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dependent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterDependent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitDependent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitDependent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DependentContext dependent() throws RecognitionException {
		DependentContext _localctx = new DependentContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_dependent);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(258);
			match(T__25);
			setState(271);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
			case DELIMITEDIDENTIFIER:
				{
				setState(259);
				invocation();
				setState(264);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__13) {
					{
					{
					setState(260);
					match(T__13);
					setState(261);
					invocation();
					}
					}
					setState(266);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(268);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(267);
					rules();
					}
				}

				}
				break;
			case T__7:
				{
				setState(270);
				rules();
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

	public static class RuleTargetContext extends ParserRuleContext {
		public RuleContextContext ruleContext() {
			return getRuleContext(RuleContextContext.class,0);
		}
		public TransformContext transform() {
			return getRuleContext(TransformContext.class,0);
		}
		public AliasContext alias() {
			return getRuleContext(AliasContext.class,0);
		}
		public TargetListModeContext targetListMode() {
			return getRuleContext(TargetListModeContext.class,0);
		}
		public InvocationContext invocation() {
			return getRuleContext(InvocationContext.class,0);
		}
		public RuleTargetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleTarget; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterRuleTarget(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitRuleTarget(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitRuleTarget(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleTargetContext ruleTarget() throws RecognitionException {
		RuleTargetContext _localctx = new RuleTargetContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_ruleTarget);
		int _la;
		try {
			setState(288);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(273);
				ruleContext();
				setState(276);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(274);
					match(T__1);
					setState(275);
					transform();
					}
				}

				setState(279);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(278);
					alias();
					}
				}

				setState(282);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__28) | (1L << T__30) | (1L << T__33) | (1L << T__34))) != 0)) {
					{
					setState(281);
					targetListMode();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(284);
				invocation();
				setState(286);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(285);
					alias();
					}
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

	public static class TransformContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public RuleContextContext ruleContext() {
			return getRuleContext(RuleContextContext.class,0);
		}
		public InvocationContext invocation() {
			return getRuleContext(InvocationContext.class,0);
		}
		public TransformContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transform; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterTransform(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitTransform(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitTransform(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TransformContext transform() throws RecognitionException {
		TransformContext _localctx = new TransformContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_transform);
		try {
			setState(293);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(290);
				literal();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(291);
				ruleContext();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(292);
				invocation();
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

	public static class InvocationContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ParamListContext paramList() {
			return getRuleContext(ParamListContext.class,0);
		}
		public InvocationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_invocation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterInvocation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitInvocation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitInvocation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InvocationContext invocation() throws RecognitionException {
		InvocationContext _localctx = new InvocationContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_invocation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(295);
			identifier();
			setState(296);
			match(T__12);
			setState(298);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL) | (1L << DATE) | (1L << DATETIME) | (1L << TIME) | (1L << IDENTIFIER) | (1L << DELIMITEDIDENTIFIER) | (1L << STRING) | (1L << INTEGER) | (1L << NUMBER))) != 0)) {
				{
				setState(297);
				paramList();
				}
			}

			setState(300);
			match(T__14);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParamListContext extends ParserRuleContext {
		public List<ParamContext> param() {
			return getRuleContexts(ParamContext.class);
		}
		public ParamContext param(int i) {
			return getRuleContext(ParamContext.class,i);
		}
		public ParamListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterParamList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitParamList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitParamList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamListContext paramList() throws RecognitionException {
		ParamListContext _localctx = new ParamListContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(302);
			param();
			setState(307);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__13) {
				{
				{
				setState(303);
				match(T__13);
				setState(304);
				param();
				}
				}
				setState(309);
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

	public static class ParamContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitParam(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamContext param() throws RecognitionException {
		ParamContext _localctx = new ParamContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_param);
		try {
			setState(312);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BOOL:
			case DATE:
			case DATETIME:
			case TIME:
			case STRING:
			case INTEGER:
			case NUMBER:
				enterOuterAlt(_localctx, 1);
				{
				setState(310);
				literal();
				}
				break;
			case IDENTIFIER:
			case DELIMITEDIDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(311);
				identifier();
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

	public static class FhirPathContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public FhirPathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fhirPath; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterFhirPath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitFhirPath(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitFhirPath(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FhirPathContext fhirPath() throws RecognitionException {
		FhirPathContext _localctx = new FhirPathContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_fhirPath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(314);
			literal();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(mappingParser.INTEGER, 0); }
		public TerminalNode NUMBER() { return getToken(mappingParser.NUMBER, 0); }
		public TerminalNode STRING() { return getToken(mappingParser.STRING, 0); }
		public TerminalNode DATETIME() { return getToken(mappingParser.DATETIME, 0); }
		public TerminalNode DATE() { return getToken(mappingParser.DATE, 0); }
		public TerminalNode TIME() { return getToken(mappingParser.TIME, 0); }
		public TerminalNode BOOL() { return getToken(mappingParser.BOOL, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(316);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL) | (1L << DATE) | (1L << DATETIME) | (1L << TIME) | (1L << STRING) | (1L << INTEGER) | (1L << NUMBER))) != 0)) ) {
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

	public static class GroupTypeModeContext extends ParserRuleContext {
		public GroupTypeModeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupTypeMode; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterGroupTypeMode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitGroupTypeMode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitGroupTypeMode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupTypeModeContext groupTypeMode() throws RecognitionException {
		GroupTypeModeContext _localctx = new GroupTypeModeContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_groupTypeMode);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(318);
			_la = _input.LA(1);
			if ( !(_la==T__26 || _la==T__27) ) {
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

	public static class SourceListModeContext extends ParserRuleContext {
		public SourceListModeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sourceListMode; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterSourceListMode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitSourceListMode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitSourceListMode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SourceListModeContext sourceListMode() throws RecognitionException {
		SourceListModeContext _localctx = new SourceListModeContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_sourceListMode);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(320);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__28) | (1L << T__29) | (1L << T__30) | (1L << T__31) | (1L << T__32))) != 0)) ) {
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

	public static class TargetListModeContext extends ParserRuleContext {
		public TargetListModeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_targetListMode; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterTargetListMode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitTargetListMode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitTargetListMode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TargetListModeContext targetListMode() throws RecognitionException {
		TargetListModeContext _localctx = new TargetListModeContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_targetListMode);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(322);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__28) | (1L << T__30) | (1L << T__33) | (1L << T__34))) != 0)) ) {
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

	public static class InputModeContext extends ParserRuleContext {
		public InputModeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inputMode; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterInputMode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitInputMode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitInputMode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InputModeContext inputMode() throws RecognitionException {
		InputModeContext _localctx = new InputModeContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_inputMode);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(324);
			_la = _input.LA(1);
			if ( !(_la==T__35 || _la==T__36) ) {
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

	public static class ModelModeContext extends ParserRuleContext {
		public ModelModeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modelMode; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).enterModelMode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mappingListener ) ((mappingListener)listener).exitModelMode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof mappingVisitor ) return ((mappingVisitor<? extends T>)visitor).visitModelMode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModelModeContext modelMode() throws RecognitionException {
		ModelModeContext _localctx = new ModelModeContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_modelMode);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(326);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38))) != 0)) ) {
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
		"\u0004\u00013\u0149\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
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
		"#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007\'\u0001"+
		"\u0000\u0001\u0000\u0005\u0000S\b\u0000\n\u0000\f\u0000V\t\u0000\u0001"+
		"\u0000\u0005\u0000Y\b\u0000\n\u0000\f\u0000\\\t\u0000\u0001\u0000\u0004"+
		"\u0000_\b\u0000\u000b\u0000\f\u0000`\u0001\u0000\u0001\u0000\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004"+
		"q\b\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0003\u0007\u0080\b\u0007\u0001\u0007\u0003\u0007"+
		"\u0083\b\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0005\b\u0089\b"+
		"\b\n\b\f\b\u008c\t\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0004"+
		"\u000b\u009b\b\u000b\u000b\u000b\f\u000b\u009c\u0001\u000b\u0001\u000b"+
		"\u0001\f\u0001\f\u0001\f\u0003\f\u00a4\b\f\u0001\r\u0001\r\u0001\r\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u00ac\b\u000e\u0001\u000e\u0003"+
		"\u000e\u00af\b\u000e\u0001\u000e\u0003\u000e\u00b2\b\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0005\u0010\u00bb\b\u0010\n\u0010\f\u0010\u00be\t\u0010\u0001\u0011\u0001"+
		"\u0011\u0003\u0011\u00c2\b\u0011\u0001\u0011\u0003\u0011\u00c5\b\u0011"+
		"\u0001\u0011\u0003\u0011\u00c8\b\u0011\u0001\u0011\u0003\u0011\u00cb\b"+
		"\u0011\u0001\u0011\u0003\u0011\u00ce\b\u0011\u0001\u0011\u0003\u0011\u00d1"+
		"\b\u0011\u0001\u0011\u0003\u0011\u00d4\b\u0011\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0005\u0012\u00d9\b\u0012\n\u0012\f\u0012\u00dc\t\u0012\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u00e3"+
		"\b\u0013\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0003"+
		"\u0015\u00ea\b\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0005\u001b\u0107"+
		"\b\u001b\n\u001b\f\u001b\u010a\t\u001b\u0001\u001b\u0003\u001b\u010d\b"+
		"\u001b\u0001\u001b\u0003\u001b\u0110\b\u001b\u0001\u001c\u0001\u001c\u0001"+
		"\u001c\u0003\u001c\u0115\b\u001c\u0001\u001c\u0003\u001c\u0118\b\u001c"+
		"\u0001\u001c\u0003\u001c\u011b\b\u001c\u0001\u001c\u0001\u001c\u0003\u001c"+
		"\u011f\b\u001c\u0003\u001c\u0121\b\u001c\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0003\u001d\u0126\b\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0003"+
		"\u001e\u012b\b\u001e\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0001"+
		"\u001f\u0005\u001f\u0132\b\u001f\n\u001f\f\u001f\u0135\t\u001f\u0001 "+
		"\u0001 \u0003 \u0139\b \u0001!\u0001!\u0001\"\u0001\"\u0001#\u0001#\u0001"+
		"$\u0001$\u0001%\u0001%\u0001&\u0001&\u0001\'\u0001\'\u0001\'\u0000\u0000"+
		"(\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a"+
		"\u001c\u001e \"$&(*,.02468:<>@BDFHJLN\u0000\b\u0001\u0000,-\u0002\u0000"+
		"\u0014\u0014//\u0002\u0000(+.0\u0001\u0000\u001b\u001c\u0001\u0000\u001d"+
		"!\u0003\u0000\u001d\u001d\u001f\u001f\"#\u0001\u0000$%\u0001\u0000$\'"+
		"\u0144\u0000P\u0001\u0000\u0000\u0000\u0002d\u0001\u0000\u0000\u0000\u0004"+
		"i\u0001\u0000\u0000\u0000\u0006k\u0001\u0000\u0000\u0000\bm\u0001\u0000"+
		"\u0000\u0000\nu\u0001\u0000\u0000\u0000\fx\u0001\u0000\u0000\u0000\u000e"+
		"{\u0001\u0000\u0000\u0000\u0010\u0086\u0001\u0000\u0000\u0000\u0012\u008f"+
		"\u0001\u0000\u0000\u0000\u0014\u0093\u0001\u0000\u0000\u0000\u0016\u0096"+
		"\u0001\u0000\u0000\u0000\u0018\u00a0\u0001\u0000\u0000\u0000\u001a\u00a5"+
		"\u0001\u0000\u0000\u0000\u001c\u00a8\u0001\u0000\u0000\u0000\u001e\u00b5"+
		"\u0001\u0000\u0000\u0000 \u00b7\u0001\u0000\u0000\u0000\"\u00bf\u0001"+
		"\u0000\u0000\u0000$\u00d5\u0001\u0000\u0000\u0000&\u00dd\u0001\u0000\u0000"+
		"\u0000(\u00e4\u0001\u0000\u0000\u0000*\u00e6\u0001\u0000\u0000\u0000,"+
		"\u00eb\u0001\u0000\u0000\u0000.\u00f0\u0001\u0000\u0000\u00000\u00f3\u0001"+
		"\u0000\u0000\u00002\u00f8\u0001\u0000\u0000\u00004\u00fd\u0001\u0000\u0000"+
		"\u00006\u0102\u0001\u0000\u0000\u00008\u0120\u0001\u0000\u0000\u0000:"+
		"\u0125\u0001\u0000\u0000\u0000<\u0127\u0001\u0000\u0000\u0000>\u012e\u0001"+
		"\u0000\u0000\u0000@\u0138\u0001\u0000\u0000\u0000B\u013a\u0001\u0000\u0000"+
		"\u0000D\u013c\u0001\u0000\u0000\u0000F\u013e\u0001\u0000\u0000\u0000H"+
		"\u0140\u0001\u0000\u0000\u0000J\u0142\u0001\u0000\u0000\u0000L\u0144\u0001"+
		"\u0000\u0000\u0000N\u0146\u0001\u0000\u0000\u0000PT\u0003\u0002\u0001"+
		"\u0000QS\u0003\b\u0004\u0000RQ\u0001\u0000\u0000\u0000SV\u0001\u0000\u0000"+
		"\u0000TR\u0001\u0000\u0000\u0000TU\u0001\u0000\u0000\u0000UZ\u0001\u0000"+
		"\u0000\u0000VT\u0001\u0000\u0000\u0000WY\u0003\f\u0006\u0000XW\u0001\u0000"+
		"\u0000\u0000Y\\\u0001\u0000\u0000\u0000ZX\u0001\u0000\u0000\u0000Z[\u0001"+
		"\u0000\u0000\u0000[^\u0001\u0000\u0000\u0000\\Z\u0001\u0000\u0000\u0000"+
		"]_\u0003\u000e\u0007\u0000^]\u0001\u0000\u0000\u0000_`\u0001\u0000\u0000"+
		"\u0000`^\u0001\u0000\u0000\u0000`a\u0001\u0000\u0000\u0000ab\u0001\u0000"+
		"\u0000\u0000bc\u0005\u0000\u0000\u0001c\u0001\u0001\u0000\u0000\u0000"+
		"de\u0005\u0001\u0000\u0000ef\u0003\u0004\u0002\u0000fg\u0005\u0002\u0000"+
		"\u0000gh\u0003\u0006\u0003\u0000h\u0003\u0001\u0000\u0000\u0000ij\u0005"+
		"-\u0000\u0000j\u0005\u0001\u0000\u0000\u0000kl\u0007\u0000\u0000\u0000"+
		"l\u0007\u0001\u0000\u0000\u0000mn\u0005\u0003\u0000\u0000np\u0003\u0004"+
		"\u0002\u0000oq\u0003\n\u0005\u0000po\u0001\u0000\u0000\u0000pq\u0001\u0000"+
		"\u0000\u0000qr\u0001\u0000\u0000\u0000rs\u0005\u0004\u0000\u0000st\u0003"+
		"N\'\u0000t\t\u0001\u0000\u0000\u0000uv\u0005\u0005\u0000\u0000vw\u0003"+
		"\u0006\u0003\u0000w\u000b\u0001\u0000\u0000\u0000xy\u0005\u0006\u0000"+
		"\u0000yz\u0003\u0004\u0002\u0000z\r\u0001\u0000\u0000\u0000{|\u0005\u0007"+
		"\u0000\u0000|}\u0003\u0006\u0003\u0000}\u007f\u0003\u0016\u000b\u0000"+
		"~\u0080\u0003\u0014\n\u0000\u007f~\u0001\u0000\u0000\u0000\u007f\u0080"+
		"\u0001\u0000\u0000\u0000\u0080\u0082\u0001\u0000\u0000\u0000\u0081\u0083"+
		"\u0003\u0012\t\u0000\u0082\u0081\u0001\u0000\u0000\u0000\u0082\u0083\u0001"+
		"\u0000\u0000\u0000\u0083\u0084\u0001\u0000\u0000\u0000\u0084\u0085\u0003"+
		"\u0010\b\u0000\u0085\u000f\u0001\u0000\u0000\u0000\u0086\u008a\u0005\b"+
		"\u0000\u0000\u0087\u0089\u0003\u001c\u000e\u0000\u0088\u0087\u0001\u0000"+
		"\u0000\u0000\u0089\u008c\u0001\u0000\u0000\u0000\u008a\u0088\u0001\u0000"+
		"\u0000\u0000\u008a\u008b\u0001\u0000\u0000\u0000\u008b\u008d\u0001\u0000"+
		"\u0000\u0000\u008c\u008a\u0001\u0000\u0000\u0000\u008d\u008e\u0005\t\u0000"+
		"\u0000\u008e\u0011\u0001\u0000\u0000\u0000\u008f\u0090\u0005\n\u0000\u0000"+
		"\u0090\u0091\u0003F#\u0000\u0091\u0092\u0005\u000b\u0000\u0000\u0092\u0013"+
		"\u0001\u0000\u0000\u0000\u0093\u0094\u0005\f\u0000\u0000\u0094\u0095\u0003"+
		"\u0006\u0003\u0000\u0095\u0015\u0001\u0000\u0000\u0000\u0096\u0097\u0005"+
		"\r\u0000\u0000\u0097\u009a\u0003\u0018\f\u0000\u0098\u0099\u0005\u000e"+
		"\u0000\u0000\u0099\u009b\u0003\u0018\f\u0000\u009a\u0098\u0001\u0000\u0000"+
		"\u0000\u009b\u009c\u0001\u0000\u0000\u0000\u009c\u009a\u0001\u0000\u0000"+
		"\u0000\u009c\u009d\u0001\u0000\u0000\u0000\u009d\u009e\u0001\u0000\u0000"+
		"\u0000\u009e\u009f\u0005\u000f\u0000\u0000\u009f\u0017\u0001\u0000\u0000"+
		"\u0000\u00a0\u00a1\u0003L&\u0000\u00a1\u00a3\u0003\u0006\u0003\u0000\u00a2"+
		"\u00a4\u0003\u001a\r\u0000\u00a3\u00a2\u0001\u0000\u0000\u0000\u00a3\u00a4"+
		"\u0001\u0000\u0000\u0000\u00a4\u0019\u0001\u0000\u0000\u0000\u00a5\u00a6"+
		"\u0005\u0010\u0000\u0000\u00a6\u00a7\u0003\u0006\u0003\u0000\u00a7\u001b"+
		"\u0001\u0000\u0000\u0000\u00a8\u00ab\u0003 \u0010\u0000\u00a9\u00aa\u0005"+
		"\u0011\u0000\u0000\u00aa\u00ac\u0003$\u0012\u0000\u00ab\u00a9\u0001\u0000"+
		"\u0000\u0000\u00ab\u00ac\u0001\u0000\u0000\u0000\u00ac\u00ae\u0001\u0000"+
		"\u0000\u0000\u00ad\u00af\u00036\u001b\u0000\u00ae\u00ad\u0001\u0000\u0000"+
		"\u0000\u00ae\u00af\u0001\u0000\u0000\u0000\u00af\u00b1\u0001\u0000\u0000"+
		"\u0000\u00b0\u00b2\u0003\u001e\u000f\u0000\u00b1\u00b0\u0001\u0000\u0000"+
		"\u0000\u00b1\u00b2\u0001\u0000\u0000\u0000\u00b2\u00b3\u0001\u0000\u0000"+
		"\u0000\u00b3\u00b4\u0005\u0012\u0000\u0000\u00b4\u001d\u0001\u0000\u0000"+
		"\u0000\u00b5\u00b6\u0005-\u0000\u0000\u00b6\u001f\u0001\u0000\u0000\u0000"+
		"\u00b7\u00bc\u0003\"\u0011\u0000\u00b8\u00b9\u0005\u000e\u0000\u0000\u00b9"+
		"\u00bb\u0003\"\u0011\u0000\u00ba\u00b8\u0001\u0000\u0000\u0000\u00bb\u00be"+
		"\u0001\u0000\u0000\u0000\u00bc\u00ba\u0001\u0000\u0000\u0000\u00bc\u00bd"+
		"\u0001\u0000\u0000\u0000\u00bd!\u0001\u0000\u0000\u0000\u00be\u00bc\u0001"+
		"\u0000\u0000\u0000\u00bf\u00c1\u0003*\u0015\u0000\u00c0\u00c2\u0003&\u0013"+
		"\u0000\u00c1\u00c0\u0001\u0000\u0000\u0000\u00c1\u00c2\u0001\u0000\u0000"+
		"\u0000\u00c2\u00c4\u0001\u0000\u0000\u0000\u00c3\u00c5\u0003,\u0016\u0000"+
		"\u00c4\u00c3\u0001\u0000\u0000\u0000\u00c4\u00c5\u0001\u0000\u0000\u0000"+
		"\u00c5\u00c7\u0001\u0000\u0000\u0000\u00c6\u00c8\u0003H$\u0000\u00c7\u00c6"+
		"\u0001\u0000\u0000\u0000\u00c7\u00c8\u0001\u0000\u0000\u0000\u00c8\u00ca"+
		"\u0001\u0000\u0000\u0000\u00c9\u00cb\u0003.\u0017\u0000\u00ca\u00c9\u0001"+
		"\u0000\u0000\u0000\u00ca\u00cb\u0001\u0000\u0000\u0000\u00cb\u00cd\u0001"+
		"\u0000\u0000\u0000\u00cc\u00ce\u00030\u0018\u0000\u00cd\u00cc\u0001\u0000"+
		"\u0000\u0000\u00cd\u00ce\u0001\u0000\u0000\u0000\u00ce\u00d0\u0001\u0000"+
		"\u0000\u0000\u00cf\u00d1\u00032\u0019\u0000\u00d0\u00cf\u0001\u0000\u0000"+
		"\u0000\u00d0\u00d1\u0001\u0000\u0000\u0000\u00d1\u00d3\u0001\u0000\u0000"+
		"\u0000\u00d2\u00d4\u00034\u001a\u0000\u00d3\u00d2\u0001\u0000\u0000\u0000"+
		"\u00d3\u00d4\u0001\u0000\u0000\u0000\u00d4#\u0001\u0000\u0000\u0000\u00d5"+
		"\u00da\u00038\u001c\u0000\u00d6\u00d7\u0005\u000e\u0000\u0000\u00d7\u00d9"+
		"\u00038\u001c\u0000\u00d8\u00d6\u0001\u0000\u0000\u0000\u00d9\u00dc\u0001"+
		"\u0000\u0000\u0000\u00da\u00d8\u0001\u0000\u0000\u0000\u00da\u00db\u0001"+
		"\u0000\u0000\u0000\u00db%\u0001\u0000\u0000\u0000\u00dc\u00da\u0001\u0000"+
		"\u0000\u0000\u00dd\u00de\u0005\u0010\u0000\u0000\u00de\u00e2\u0003\u0006"+
		"\u0003\u0000\u00df\u00e0\u0005/\u0000\u0000\u00e0\u00e1\u0005\u0013\u0000"+
		"\u0000\u00e1\u00e3\u0003(\u0014\u0000\u00e2\u00df\u0001\u0000\u0000\u0000"+
		"\u00e2\u00e3\u0001\u0000\u0000\u0000\u00e3\'\u0001\u0000\u0000\u0000\u00e4"+
		"\u00e5\u0007\u0001\u0000\u0000\u00e5)\u0001\u0000\u0000\u0000\u00e6\u00e9"+
		"\u0003\u0006\u0003\u0000\u00e7\u00e8\u0005\u0015\u0000\u0000\u00e8\u00ea"+
		"\u0003\u0006\u0003\u0000\u00e9\u00e7\u0001\u0000\u0000\u0000\u00e9\u00ea"+
		"\u0001\u0000\u0000\u0000\u00ea+\u0001\u0000\u0000\u0000\u00eb\u00ec\u0005"+
		"\u0016\u0000\u0000\u00ec\u00ed\u0005\r\u0000\u0000\u00ed\u00ee\u0003B"+
		"!\u0000\u00ee\u00ef\u0005\u000f\u0000\u0000\u00ef-\u0001\u0000\u0000\u0000"+
		"\u00f0\u00f1\u0005\u0004\u0000\u0000\u00f1\u00f2\u0003\u0006\u0003\u0000"+
		"\u00f2/\u0001\u0000\u0000\u0000\u00f3\u00f4\u0005\u0017\u0000\u0000\u00f4"+
		"\u00f5\u0005\r\u0000\u0000\u00f5\u00f6\u0003B!\u0000\u00f6\u00f7\u0005"+
		"\u000f\u0000\u0000\u00f71\u0001\u0000\u0000\u0000\u00f8\u00f9\u0005\u0018"+
		"\u0000\u0000\u00f9\u00fa\u0005\r\u0000\u0000\u00fa\u00fb\u0003B!\u0000"+
		"\u00fb\u00fc\u0005\u000f\u0000\u0000\u00fc3\u0001\u0000\u0000\u0000\u00fd"+
		"\u00fe\u0005\u0019\u0000\u0000\u00fe\u00ff\u0005\r\u0000\u0000\u00ff\u0100"+
		"\u0003B!\u0000\u0100\u0101\u0005\u000f\u0000\u0000\u01015\u0001\u0000"+
		"\u0000\u0000\u0102\u010f\u0005\u001a\u0000\u0000\u0103\u0108\u0003<\u001e"+
		"\u0000\u0104\u0105\u0005\u000e\u0000\u0000\u0105\u0107\u0003<\u001e\u0000"+
		"\u0106\u0104\u0001\u0000\u0000\u0000\u0107\u010a\u0001\u0000\u0000\u0000"+
		"\u0108\u0106\u0001\u0000\u0000\u0000\u0108\u0109\u0001\u0000\u0000\u0000"+
		"\u0109\u010c\u0001\u0000\u0000\u0000\u010a\u0108\u0001\u0000\u0000\u0000"+
		"\u010b\u010d\u0003\u0010\b\u0000\u010c\u010b\u0001\u0000\u0000\u0000\u010c"+
		"\u010d\u0001\u0000\u0000\u0000\u010d\u0110\u0001\u0000\u0000\u0000\u010e"+
		"\u0110\u0003\u0010\b\u0000\u010f\u0103\u0001\u0000\u0000\u0000\u010f\u010e"+
		"\u0001\u0000\u0000\u0000\u01107\u0001\u0000\u0000\u0000\u0111\u0114\u0003"+
		"*\u0015\u0000\u0112\u0113\u0005\u0002\u0000\u0000\u0113\u0115\u0003:\u001d"+
		"\u0000\u0114\u0112\u0001\u0000\u0000\u0000\u0114\u0115\u0001\u0000\u0000"+
		"\u0000\u0115\u0117\u0001\u0000\u0000\u0000\u0116\u0118\u0003.\u0017\u0000"+
		"\u0117\u0116\u0001\u0000\u0000\u0000\u0117\u0118\u0001\u0000\u0000\u0000"+
		"\u0118\u011a\u0001\u0000\u0000\u0000\u0119\u011b\u0003J%\u0000\u011a\u0119"+
		"\u0001\u0000\u0000\u0000\u011a\u011b\u0001\u0000\u0000\u0000\u011b\u0121"+
		"\u0001\u0000\u0000\u0000\u011c\u011e\u0003<\u001e\u0000\u011d\u011f\u0003"+
		".\u0017\u0000\u011e\u011d\u0001\u0000\u0000\u0000\u011e\u011f\u0001\u0000"+
		"\u0000\u0000\u011f\u0121\u0001\u0000\u0000\u0000\u0120\u0111\u0001\u0000"+
		"\u0000\u0000\u0120\u011c\u0001\u0000\u0000\u0000\u01219\u0001\u0000\u0000"+
		"\u0000\u0122\u0126\u0003D\"\u0000\u0123\u0126\u0003*\u0015\u0000\u0124"+
		"\u0126\u0003<\u001e\u0000\u0125\u0122\u0001\u0000\u0000\u0000\u0125\u0123"+
		"\u0001\u0000\u0000\u0000\u0125\u0124\u0001\u0000\u0000\u0000\u0126;\u0001"+
		"\u0000\u0000\u0000\u0127\u0128\u0003\u0006\u0003\u0000\u0128\u012a\u0005"+
		"\r\u0000\u0000\u0129\u012b\u0003>\u001f\u0000\u012a\u0129\u0001\u0000"+
		"\u0000\u0000\u012a\u012b\u0001\u0000\u0000\u0000\u012b\u012c\u0001\u0000"+
		"\u0000\u0000\u012c\u012d\u0005\u000f\u0000\u0000\u012d=\u0001\u0000\u0000"+
		"\u0000\u012e\u0133\u0003@ \u0000\u012f\u0130\u0005\u000e\u0000\u0000\u0130"+
		"\u0132\u0003@ \u0000\u0131\u012f\u0001\u0000\u0000\u0000\u0132\u0135\u0001"+
		"\u0000\u0000\u0000\u0133\u0131\u0001\u0000\u0000\u0000\u0133\u0134\u0001"+
		"\u0000\u0000\u0000\u0134?\u0001\u0000\u0000\u0000\u0135\u0133\u0001\u0000"+
		"\u0000\u0000\u0136\u0139\u0003D\"\u0000\u0137\u0139\u0003\u0006\u0003"+
		"\u0000\u0138\u0136\u0001\u0000\u0000\u0000\u0138\u0137\u0001\u0000\u0000"+
		"\u0000\u0139A\u0001\u0000\u0000\u0000\u013a\u013b\u0003D\"\u0000\u013b"+
		"C\u0001\u0000\u0000\u0000\u013c\u013d\u0007\u0002\u0000\u0000\u013dE\u0001"+
		"\u0000\u0000\u0000\u013e\u013f\u0007\u0003\u0000\u0000\u013fG\u0001\u0000"+
		"\u0000\u0000\u0140\u0141\u0007\u0004\u0000\u0000\u0141I\u0001\u0000\u0000"+
		"\u0000\u0142\u0143\u0007\u0005\u0000\u0000\u0143K\u0001\u0000\u0000\u0000"+
		"\u0144\u0145\u0007\u0006\u0000\u0000\u0145M\u0001\u0000\u0000\u0000\u0146"+
		"\u0147\u0007\u0007\u0000\u0000\u0147O\u0001\u0000\u0000\u0000#TZ`p\u007f"+
		"\u0082\u008a\u009c\u00a3\u00ab\u00ae\u00b1\u00bc\u00c1\u00c4\u00c7\u00ca"+
		"\u00cd\u00d0\u00d3\u00da\u00e2\u00e9\u0108\u010c\u010f\u0114\u0117\u011a"+
		"\u011e\u0120\u0125\u012a\u0133\u0138";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}