// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/IMAPI/parser/src/main/grammars\Turtlite.g4 by ANTLR 4.9.1
package org.endeavourhealth.imapi.parser.turtle;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TurtliteParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, NumericLiteral=14, BooleanLiteral=15, 
		String=16, BlankNode=17, WS=18, PN_PREFIX=19, IRIREF=20, PNAME_NS=21, 
		PrefixedName=22, PNAME_LN=23, BLANK_NODE_LABEL=24, LANGTAG=25, INTEGER=26, 
		DECIMAL=27, DOUBLE=28, EXPONENT=29, STRING_LITERAL_LONG_SINGLE_QUOTE=30, 
		STRING_LITERAL_LONG_QUOTE=31, STRING_LITERAL_QUOTE=32, STRING_LITERAL_SINGLE_QUOTE=33, 
		UCHAR=34, ECHAR=35, ANON_WS=36, ANON=37, PN_CHARS_BASE=38, PN_CHARS_U=39, 
		PN_CHARS=40, PN_LOCAL=41, PLX=42, PERCENT=43, HEX=44, PN_LOCAL_ESC=45;
	public static final int
		RULE_turtleDoc = 0, RULE_statement = 1, RULE_directive = 2, RULE_prefixID = 3, 
		RULE_base = 4, RULE_sparqlBase = 5, RULE_sparqlPrefix = 6, RULE_triples = 7, 
		RULE_predicateObjectList = 8, RULE_objectList = 9, RULE_verb = 10, RULE_subject = 11, 
		RULE_predicate = 12, RULE_object = 13, RULE_literal = 14, RULE_blankNodePropertyList = 15, 
		RULE_collection = 16, RULE_rdfLiteral = 17, RULE_iri = 18;
	private static String[] makeRuleNames() {
		return new String[] {
			"turtleDoc", "statement", "directive", "prefixID", "base", "sparqlBase", 
			"sparqlPrefix", "triples", "predicateObjectList", "objectList", "verb", 
			"subject", "predicate", "object", "literal", "blankNodePropertyList", 
			"collection", "rdfLiteral", "iri"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", "'@prefix'", "'@base'", "'BASE'", "'PREFIX'", "';'", "','", 
			"'a'", "'['", "']'", "'('", "')'", "'^^'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, "NumericLiteral", "BooleanLiteral", "String", "BlankNode", 
			"WS", "PN_PREFIX", "IRIREF", "PNAME_NS", "PrefixedName", "PNAME_LN", 
			"BLANK_NODE_LABEL", "LANGTAG", "INTEGER", "DECIMAL", "DOUBLE", "EXPONENT", 
			"STRING_LITERAL_LONG_SINGLE_QUOTE", "STRING_LITERAL_LONG_QUOTE", "STRING_LITERAL_QUOTE", 
			"STRING_LITERAL_SINGLE_QUOTE", "UCHAR", "ECHAR", "ANON_WS", "ANON", "PN_CHARS_BASE", 
			"PN_CHARS_U", "PN_CHARS", "PN_LOCAL", "PLX", "PERCENT", "HEX", "PN_LOCAL_ESC"
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
	public String getGrammarFileName() { return "Turtlite.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TurtliteParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class TurtleDocContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TurtleDocContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_turtleDoc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterTurtleDoc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitTurtleDoc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitTurtleDoc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TurtleDocContext turtleDoc() throws RecognitionException {
		TurtleDocContext _localctx = new TurtleDocContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_turtleDoc);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(41);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__8) | (1L << T__10) | (1L << BlankNode) | (1L << IRIREF) | (1L << PrefixedName))) != 0)) {
				{
				{
				setState(38);
				statement();
				}
				}
				setState(43);
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

	public static class StatementContext extends ParserRuleContext {
		public DirectiveContext directive() {
			return getRuleContext(DirectiveContext.class,0);
		}
		public TriplesContext triples() {
			return getRuleContext(TriplesContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statement);
		try {
			setState(48);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
			case T__2:
			case T__3:
			case T__4:
				enterOuterAlt(_localctx, 1);
				{
				setState(44);
				directive();
				}
				break;
			case T__8:
			case T__10:
			case BlankNode:
			case IRIREF:
			case PrefixedName:
				enterOuterAlt(_localctx, 2);
				{
				setState(45);
				triples();
				setState(46);
				match(T__0);
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

	public static class DirectiveContext extends ParserRuleContext {
		public PrefixIDContext prefixID() {
			return getRuleContext(PrefixIDContext.class,0);
		}
		public BaseContext base() {
			return getRuleContext(BaseContext.class,0);
		}
		public SparqlPrefixContext sparqlPrefix() {
			return getRuleContext(SparqlPrefixContext.class,0);
		}
		public SparqlBaseContext sparqlBase() {
			return getRuleContext(SparqlBaseContext.class,0);
		}
		public DirectiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_directive; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterDirective(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitDirective(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitDirective(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirectiveContext directive() throws RecognitionException {
		DirectiveContext _localctx = new DirectiveContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_directive);
		try {
			setState(54);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				enterOuterAlt(_localctx, 1);
				{
				setState(50);
				prefixID();
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 2);
				{
				setState(51);
				base();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 3);
				{
				setState(52);
				sparqlPrefix();
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 4);
				{
				setState(53);
				sparqlBase();
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

	public static class PrefixIDContext extends ParserRuleContext {
		public TerminalNode PNAME_NS() { return getToken(TurtliteParser.PNAME_NS, 0); }
		public TerminalNode IRIREF() { return getToken(TurtliteParser.IRIREF, 0); }
		public PrefixIDContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefixID; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterPrefixID(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitPrefixID(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitPrefixID(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixIDContext prefixID() throws RecognitionException {
		PrefixIDContext _localctx = new PrefixIDContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_prefixID);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
			match(T__1);
			setState(57);
			match(PNAME_NS);
			setState(58);
			match(IRIREF);
			setState(59);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BaseContext extends ParserRuleContext {
		public TerminalNode IRIREF() { return getToken(TurtliteParser.IRIREF, 0); }
		public BaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_base; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterBase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitBase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitBase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BaseContext base() throws RecognitionException {
		BaseContext _localctx = new BaseContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_base);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61);
			match(T__2);
			setState(62);
			match(IRIREF);
			setState(63);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SparqlBaseContext extends ParserRuleContext {
		public TerminalNode IRIREF() { return getToken(TurtliteParser.IRIREF, 0); }
		public SparqlBaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sparqlBase; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterSparqlBase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitSparqlBase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitSparqlBase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SparqlBaseContext sparqlBase() throws RecognitionException {
		SparqlBaseContext _localctx = new SparqlBaseContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_sparqlBase);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			match(T__3);
			setState(66);
			match(IRIREF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SparqlPrefixContext extends ParserRuleContext {
		public TerminalNode PNAME_NS() { return getToken(TurtliteParser.PNAME_NS, 0); }
		public TerminalNode IRIREF() { return getToken(TurtliteParser.IRIREF, 0); }
		public SparqlPrefixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sparqlPrefix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterSparqlPrefix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitSparqlPrefix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitSparqlPrefix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SparqlPrefixContext sparqlPrefix() throws RecognitionException {
		SparqlPrefixContext _localctx = new SparqlPrefixContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_sparqlPrefix);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			match(T__4);
			setState(69);
			match(PNAME_NS);
			setState(70);
			match(IRIREF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TriplesContext extends ParserRuleContext {
		public SubjectContext subject() {
			return getRuleContext(SubjectContext.class,0);
		}
		public PredicateObjectListContext predicateObjectList() {
			return getRuleContext(PredicateObjectListContext.class,0);
		}
		public BlankNodePropertyListContext blankNodePropertyList() {
			return getRuleContext(BlankNodePropertyListContext.class,0);
		}
		public TriplesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_triples; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterTriples(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitTriples(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitTriples(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TriplesContext triples() throws RecognitionException {
		TriplesContext _localctx = new TriplesContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_triples);
		int _la;
		try {
			setState(79);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__10:
			case BlankNode:
			case IRIREF:
			case PrefixedName:
				enterOuterAlt(_localctx, 1);
				{
				setState(72);
				subject();
				setState(73);
				predicateObjectList();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 2);
				{
				setState(75);
				blankNodePropertyList();
				setState(77);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << IRIREF) | (1L << PrefixedName))) != 0)) {
					{
					setState(76);
					predicateObjectList();
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

	public static class PredicateObjectListContext extends ParserRuleContext {
		public List<VerbContext> verb() {
			return getRuleContexts(VerbContext.class);
		}
		public VerbContext verb(int i) {
			return getRuleContext(VerbContext.class,i);
		}
		public List<ObjectListContext> objectList() {
			return getRuleContexts(ObjectListContext.class);
		}
		public ObjectListContext objectList(int i) {
			return getRuleContext(ObjectListContext.class,i);
		}
		public PredicateObjectListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicateObjectList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterPredicateObjectList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitPredicateObjectList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitPredicateObjectList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateObjectListContext predicateObjectList() throws RecognitionException {
		PredicateObjectListContext _localctx = new PredicateObjectListContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_predicateObjectList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(81);
			verb();
			setState(82);
			objectList();
			}
			setState(92);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(84);
				match(T__5);
				setState(88);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << IRIREF) | (1L << PrefixedName))) != 0)) {
					{
					setState(85);
					verb();
					setState(86);
					objectList();
					}
				}

				}
				}
				setState(94);
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

	public static class ObjectListContext extends ParserRuleContext {
		public List<ObjectContext> object() {
			return getRuleContexts(ObjectContext.class);
		}
		public ObjectContext object(int i) {
			return getRuleContext(ObjectContext.class,i);
		}
		public ObjectListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterObjectList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitObjectList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitObjectList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectListContext objectList() throws RecognitionException {
		ObjectListContext _localctx = new ObjectListContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_objectList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			object();
			setState(100);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__6) {
				{
				{
				setState(96);
				match(T__6);
				setState(97);
				object();
				}
				}
				setState(102);
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

	public static class VerbContext extends ParserRuleContext {
		public PredicateContext predicate() {
			return getRuleContext(PredicateContext.class,0);
		}
		public VerbContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_verb; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterVerb(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitVerb(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitVerb(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VerbContext verb() throws RecognitionException {
		VerbContext _localctx = new VerbContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_verb);
		try {
			setState(105);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRIREF:
			case PrefixedName:
				enterOuterAlt(_localctx, 1);
				{
				setState(103);
				predicate();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 2);
				{
				setState(104);
				match(T__7);
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

	public static class SubjectContext extends ParserRuleContext {
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public TerminalNode BlankNode() { return getToken(TurtliteParser.BlankNode, 0); }
		public CollectionContext collection() {
			return getRuleContext(CollectionContext.class,0);
		}
		public SubjectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subject; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterSubject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitSubject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitSubject(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubjectContext subject() throws RecognitionException {
		SubjectContext _localctx = new SubjectContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_subject);
		try {
			setState(110);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRIREF:
			case PrefixedName:
				enterOuterAlt(_localctx, 1);
				{
				setState(107);
				iri();
				}
				break;
			case BlankNode:
				enterOuterAlt(_localctx, 2);
				{
				setState(108);
				match(BlankNode);
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 3);
				{
				setState(109);
				collection();
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

	public static class PredicateContext extends ParserRuleContext {
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public PredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitPredicate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitPredicate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateContext predicate() throws RecognitionException {
		PredicateContext _localctx = new PredicateContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_predicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			iri();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjectContext extends ParserRuleContext {
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public TerminalNode BlankNode() { return getToken(TurtliteParser.BlankNode, 0); }
		public CollectionContext collection() {
			return getRuleContext(CollectionContext.class,0);
		}
		public BlankNodePropertyListContext blankNodePropertyList() {
			return getRuleContext(BlankNodePropertyListContext.class,0);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public ObjectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_object; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterObject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitObject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitObject(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectContext object() throws RecognitionException {
		ObjectContext _localctx = new ObjectContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_object);
		try {
			setState(119);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRIREF:
			case PrefixedName:
				enterOuterAlt(_localctx, 1);
				{
				setState(114);
				iri();
				}
				break;
			case BlankNode:
				enterOuterAlt(_localctx, 2);
				{
				setState(115);
				match(BlankNode);
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 3);
				{
				setState(116);
				collection();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 4);
				{
				setState(117);
				blankNodePropertyList();
				}
				break;
			case NumericLiteral:
			case BooleanLiteral:
			case String:
				enterOuterAlt(_localctx, 5);
				{
				setState(118);
				literal();
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

	public static class LiteralContext extends ParserRuleContext {
		public RdfLiteralContext rdfLiteral() {
			return getRuleContext(RdfLiteralContext.class,0);
		}
		public TerminalNode NumericLiteral() { return getToken(TurtliteParser.NumericLiteral, 0); }
		public TerminalNode BooleanLiteral() { return getToken(TurtliteParser.BooleanLiteral, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_literal);
		try {
			setState(124);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case String:
				enterOuterAlt(_localctx, 1);
				{
				setState(121);
				rdfLiteral();
				}
				break;
			case NumericLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(122);
				match(NumericLiteral);
				}
				break;
			case BooleanLiteral:
				enterOuterAlt(_localctx, 3);
				{
				setState(123);
				match(BooleanLiteral);
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

	public static class BlankNodePropertyListContext extends ParserRuleContext {
		public PredicateObjectListContext predicateObjectList() {
			return getRuleContext(PredicateObjectListContext.class,0);
		}
		public BlankNodePropertyListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blankNodePropertyList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterBlankNodePropertyList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitBlankNodePropertyList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitBlankNodePropertyList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlankNodePropertyListContext blankNodePropertyList() throws RecognitionException {
		BlankNodePropertyListContext _localctx = new BlankNodePropertyListContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_blankNodePropertyList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			match(T__8);
			setState(127);
			predicateObjectList();
			setState(128);
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

	public static class CollectionContext extends ParserRuleContext {
		public List<ObjectContext> object() {
			return getRuleContexts(ObjectContext.class);
		}
		public ObjectContext object(int i) {
			return getRuleContext(ObjectContext.class,i);
		}
		public CollectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterCollection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitCollection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitCollection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionContext collection() throws RecognitionException {
		CollectionContext _localctx = new CollectionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_collection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(130);
			match(T__10);
			setState(134);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << T__10) | (1L << NumericLiteral) | (1L << BooleanLiteral) | (1L << String) | (1L << BlankNode) | (1L << IRIREF) | (1L << PrefixedName))) != 0)) {
				{
				{
				setState(131);
				object();
				}
				}
				setState(136);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(137);
			match(T__11);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RdfLiteralContext extends ParserRuleContext {
		public TerminalNode String() { return getToken(TurtliteParser.String, 0); }
		public TerminalNode LANGTAG() { return getToken(TurtliteParser.LANGTAG, 0); }
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public RdfLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rdfLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterRdfLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitRdfLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitRdfLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RdfLiteralContext rdfLiteral() throws RecognitionException {
		RdfLiteralContext _localctx = new RdfLiteralContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_rdfLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			match(String);
			setState(143);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LANGTAG:
				{
				setState(140);
				match(LANGTAG);
				}
				break;
			case T__12:
				{
				setState(141);
				match(T__12);
				setState(142);
				iri();
				}
				break;
			case T__0:
			case T__5:
			case T__6:
			case T__8:
			case T__9:
			case T__10:
			case T__11:
			case NumericLiteral:
			case BooleanLiteral:
			case String:
			case BlankNode:
			case IRIREF:
			case PrefixedName:
				break;
			default:
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

	public static class IriContext extends ParserRuleContext {
		public TerminalNode IRIREF() { return getToken(TurtliteParser.IRIREF, 0); }
		public TerminalNode PrefixedName() { return getToken(TurtliteParser.PrefixedName, 0); }
		public IriContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iri; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterIri(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitIri(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitIri(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IriContext iri() throws RecognitionException {
		IriContext _localctx = new IriContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_iri);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(145);
			_la = _input.LA(1);
			if ( !(_la==IRIREF || _la==PrefixedName) ) {
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3/\u0096\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\3\2\7\2*\n\2\f\2\16\2-\13\2\3\3\3\3\3\3\3\3\5\3\63"+
		"\n\3\3\4\3\4\3\4\3\4\5\49\n\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7"+
		"\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\5\tP\n\t\5\tR\n\t\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\5\n[\n\n\7\n]\n\n\f\n\16\n`\13\n\3\13\3\13\3\13\7"+
		"\13e\n\13\f\13\16\13h\13\13\3\f\3\f\5\fl\n\f\3\r\3\r\3\r\5\rq\n\r\3\16"+
		"\3\16\3\17\3\17\3\17\3\17\3\17\5\17z\n\17\3\20\3\20\3\20\5\20\177\n\20"+
		"\3\21\3\21\3\21\3\21\3\22\3\22\7\22\u0087\n\22\f\22\16\22\u008a\13\22"+
		"\3\22\3\22\3\23\3\23\3\23\3\23\5\23\u0092\n\23\3\24\3\24\3\24\2\2\25\2"+
		"\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&\2\3\4\2\26\26\30\30\2\u0098"+
		"\2+\3\2\2\2\4\62\3\2\2\2\68\3\2\2\2\b:\3\2\2\2\n?\3\2\2\2\fC\3\2\2\2\16"+
		"F\3\2\2\2\20Q\3\2\2\2\22S\3\2\2\2\24a\3\2\2\2\26k\3\2\2\2\30p\3\2\2\2"+
		"\32r\3\2\2\2\34y\3\2\2\2\36~\3\2\2\2 \u0080\3\2\2\2\"\u0084\3\2\2\2$\u008d"+
		"\3\2\2\2&\u0093\3\2\2\2(*\5\4\3\2)(\3\2\2\2*-\3\2\2\2+)\3\2\2\2+,\3\2"+
		"\2\2,\3\3\2\2\2-+\3\2\2\2.\63\5\6\4\2/\60\5\20\t\2\60\61\7\3\2\2\61\63"+
		"\3\2\2\2\62.\3\2\2\2\62/\3\2\2\2\63\5\3\2\2\2\649\5\b\5\2\659\5\n\6\2"+
		"\669\5\16\b\2\679\5\f\7\28\64\3\2\2\28\65\3\2\2\28\66\3\2\2\28\67\3\2"+
		"\2\29\7\3\2\2\2:;\7\4\2\2;<\7\27\2\2<=\7\26\2\2=>\7\3\2\2>\t\3\2\2\2?"+
		"@\7\5\2\2@A\7\26\2\2AB\7\3\2\2B\13\3\2\2\2CD\7\6\2\2DE\7\26\2\2E\r\3\2"+
		"\2\2FG\7\7\2\2GH\7\27\2\2HI\7\26\2\2I\17\3\2\2\2JK\5\30\r\2KL\5\22\n\2"+
		"LR\3\2\2\2MO\5 \21\2NP\5\22\n\2ON\3\2\2\2OP\3\2\2\2PR\3\2\2\2QJ\3\2\2"+
		"\2QM\3\2\2\2R\21\3\2\2\2ST\5\26\f\2TU\5\24\13\2U^\3\2\2\2VZ\7\b\2\2WX"+
		"\5\26\f\2XY\5\24\13\2Y[\3\2\2\2ZW\3\2\2\2Z[\3\2\2\2[]\3\2\2\2\\V\3\2\2"+
		"\2]`\3\2\2\2^\\\3\2\2\2^_\3\2\2\2_\23\3\2\2\2`^\3\2\2\2af\5\34\17\2bc"+
		"\7\t\2\2ce\5\34\17\2db\3\2\2\2eh\3\2\2\2fd\3\2\2\2fg\3\2\2\2g\25\3\2\2"+
		"\2hf\3\2\2\2il\5\32\16\2jl\7\n\2\2ki\3\2\2\2kj\3\2\2\2l\27\3\2\2\2mq\5"+
		"&\24\2nq\7\23\2\2oq\5\"\22\2pm\3\2\2\2pn\3\2\2\2po\3\2\2\2q\31\3\2\2\2"+
		"rs\5&\24\2s\33\3\2\2\2tz\5&\24\2uz\7\23\2\2vz\5\"\22\2wz\5 \21\2xz\5\36"+
		"\20\2yt\3\2\2\2yu\3\2\2\2yv\3\2\2\2yw\3\2\2\2yx\3\2\2\2z\35\3\2\2\2{\177"+
		"\5$\23\2|\177\7\20\2\2}\177\7\21\2\2~{\3\2\2\2~|\3\2\2\2~}\3\2\2\2\177"+
		"\37\3\2\2\2\u0080\u0081\7\13\2\2\u0081\u0082\5\22\n\2\u0082\u0083\7\f"+
		"\2\2\u0083!\3\2\2\2\u0084\u0088\7\r\2\2\u0085\u0087\5\34\17\2\u0086\u0085"+
		"\3\2\2\2\u0087\u008a\3\2\2\2\u0088\u0086\3\2\2\2\u0088\u0089\3\2\2\2\u0089"+
		"\u008b\3\2\2\2\u008a\u0088\3\2\2\2\u008b\u008c\7\16\2\2\u008c#\3\2\2\2"+
		"\u008d\u0091\7\22\2\2\u008e\u0092\7\33\2\2\u008f\u0090\7\17\2\2\u0090"+
		"\u0092\5&\24\2\u0091\u008e\3\2\2\2\u0091\u008f\3\2\2\2\u0091\u0092\3\2"+
		"\2\2\u0092%\3\2\2\2\u0093\u0094\t\2\2\2\u0094\'\3\2\2\2\20+\628OQZ^fk"+
		"py~\u0088\u0091";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
