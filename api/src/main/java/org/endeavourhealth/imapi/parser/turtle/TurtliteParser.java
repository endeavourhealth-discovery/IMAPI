// Generated from C:/Users/david/CloudStation/EhealthTrust/DiscoveryDataService/IMAPI/api/src/main/grammars\Turtlite.g4 by ANTLR 4.10.1
package org.endeavourhealth.imapi.parser.turtle;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TurtliteParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, NumericLiteral=14, LINE_COMMENT=15, 
		BooleanLiteral=16, String=17, BlankNode=18, WS=19, PN_PREFIX=20, IRIREF=21, 
		PNAME_NS=22, PrefixedName=23, PNAME_LN=24, BLANK_NODE_LABEL=25, LANGTAG=26, 
		INTEGER=27, DECIMAL=28, DOUBLE=29, EXPONENT=30, STRING_LITERAL_LONG_SINGLE_QUOTE=31, 
		STRING_LITERAL_LONG_QUOTE=32, STRING_LITERAL_QUOTE=33, STRING_LITERAL_SINGLE_QUOTE=34, 
		UCHAR=35, ECHAR=36, ANON_WS=37, ANON=38, PN_CHARS_BASE=39, PN_CHARS_U=40, 
		PN_CHARS=41, PN_LOCAL=42, PLX=43, PERCENT=44, HEX=45, PN_LOCAL_ESC=46;
	public static final int
		RULE_turtleDoc = 0, RULE_statement = 1, RULE_directive = 2, RULE_prefixID = 3, 
		RULE_base = 4, RULE_sparqlBase = 5, RULE_sparqlPrefix = 6, RULE_triples = 7, 
		RULE_predicateObjectList = 8, RULE_objectList = 9, RULE_verb = 10, RULE_subject = 11, 
		RULE_predicate = 12, RULE_object = 13, RULE_literal = 14, RULE_blankNodePropertyList = 15, 
		RULE_collection = 16, RULE_rdfLiteral = 17, RULE_comment = 18, RULE_iri = 19;
	private static String[] makeRuleNames() {
		return new String[] {
			"turtleDoc", "statement", "directive", "prefixID", "base", "sparqlBase", 
			"sparqlPrefix", "triples", "predicateObjectList", "objectList", "verb", 
			"subject", "predicate", "object", "literal", "blankNodePropertyList", 
			"collection", "rdfLiteral", "comment", "iri"
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
			null, null, "NumericLiteral", "LINE_COMMENT", "BooleanLiteral", "String", 
			"BlankNode", "WS", "PN_PREFIX", "IRIREF", "PNAME_NS", "PrefixedName", 
			"PNAME_LN", "BLANK_NODE_LABEL", "LANGTAG", "INTEGER", "DECIMAL", "DOUBLE", 
			"EXPONENT", "STRING_LITERAL_LONG_SINGLE_QUOTE", "STRING_LITERAL_LONG_QUOTE", 
			"STRING_LITERAL_QUOTE", "STRING_LITERAL_SINGLE_QUOTE", "UCHAR", "ECHAR", 
			"ANON_WS", "ANON", "PN_CHARS_BASE", "PN_CHARS_U", "PN_CHARS", "PN_LOCAL", 
			"PLX", "PERCENT", "HEX", "PN_LOCAL_ESC"
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
			setState(43);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__8) | (1L << T__10) | (1L << LINE_COMMENT) | (1L << BlankNode) | (1L << IRIREF) | (1L << PrefixedName))) != 0)) {
				{
				{
				setState(40);
				statement();
				}
				}
				setState(45);
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
		public CommentContext comment() {
			return getRuleContext(CommentContext.class,0);
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
			setState(51);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
			case T__2:
			case T__3:
			case T__4:
				enterOuterAlt(_localctx, 1);
				{
				setState(46);
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
				setState(47);
				triples();
				setState(48);
				match(T__0);
				}
				break;
			case LINE_COMMENT:
				enterOuterAlt(_localctx, 3);
				{
				setState(50);
				comment();
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
			setState(57);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				enterOuterAlt(_localctx, 1);
				{
				setState(53);
				prefixID();
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 2);
				{
				setState(54);
				base();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 3);
				{
				setState(55);
				sparqlPrefix();
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 4);
				{
				setState(56);
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
			setState(59);
			match(T__1);
			setState(60);
			match(PNAME_NS);
			setState(61);
			match(IRIREF);
			setState(62);
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
			setState(64);
			match(T__2);
			setState(65);
			match(IRIREF);
			setState(66);
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
			setState(68);
			match(T__3);
			setState(69);
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
			setState(71);
			match(T__4);
			setState(72);
			match(PNAME_NS);
			setState(73);
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
		public List<PredicateObjectListContext> predicateObjectList() {
			return getRuleContexts(PredicateObjectListContext.class);
		}
		public PredicateObjectListContext predicateObjectList(int i) {
			return getRuleContext(PredicateObjectListContext.class,i);
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
			setState(85);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__10:
			case BlankNode:
			case IRIREF:
			case PrefixedName:
				enterOuterAlt(_localctx, 1);
				{
				setState(75);
				subject();
				setState(76);
				predicateObjectList();
				setState(81);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__5) {
					{
					{
					setState(77);
					match(T__5);
					setState(78);
					predicateObjectList();
					}
					}
					setState(83);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 2);
				{
				setState(84);
				blankNodePropertyList();
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
		public VerbContext verb() {
			return getRuleContext(VerbContext.class,0);
		}
		public ObjectListContext objectList() {
			return getRuleContext(ObjectListContext.class,0);
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
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
			verb();
			setState(88);
			objectList();
			}
		}
		catch (RecognitionException re) {
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
			setState(90);
			object();
			setState(95);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__6) {
				{
				{
				setState(91);
				match(T__6);
				setState(92);
				object();
				}
				}
				setState(97);
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
			setState(100);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRIREF:
			case PrefixedName:
				enterOuterAlt(_localctx, 1);
				{
				setState(98);
				predicate();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 2);
				{
				setState(99);
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
			setState(105);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRIREF:
			case PrefixedName:
				enterOuterAlt(_localctx, 1);
				{
				setState(102);
				iri();
				}
				break;
			case BlankNode:
				enterOuterAlt(_localctx, 2);
				{
				setState(103);
				match(BlankNode);
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 3);
				{
				setState(104);
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
			setState(107);
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
			setState(114);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRIREF:
			case PrefixedName:
				enterOuterAlt(_localctx, 1);
				{
				setState(109);
				iri();
				}
				break;
			case BlankNode:
				enterOuterAlt(_localctx, 2);
				{
				setState(110);
				match(BlankNode);
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 3);
				{
				setState(111);
				collection();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 4);
				{
				setState(112);
				blankNodePropertyList();
				}
				break;
			case NumericLiteral:
			case BooleanLiteral:
			case String:
				enterOuterAlt(_localctx, 5);
				{
				setState(113);
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
			setState(119);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case String:
				enterOuterAlt(_localctx, 1);
				{
				setState(116);
				rdfLiteral();
				}
				break;
			case NumericLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(117);
				match(NumericLiteral);
				}
				break;
			case BooleanLiteral:
				enterOuterAlt(_localctx, 3);
				{
				setState(118);
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
		public List<PredicateObjectListContext> predicateObjectList() {
			return getRuleContexts(PredicateObjectListContext.class);
		}
		public PredicateObjectListContext predicateObjectList(int i) {
			return getRuleContext(PredicateObjectListContext.class,i);
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
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			match(T__8);
			setState(122);
			predicateObjectList();
			setState(127);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(123);
				match(T__5);
				setState(124);
				predicateObjectList();
				}
				}
				setState(129);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(130);
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
			setState(132);
			match(T__10);
			setState(136);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << T__10) | (1L << NumericLiteral) | (1L << BooleanLiteral) | (1L << String) | (1L << BlankNode) | (1L << IRIREF) | (1L << PrefixedName))) != 0)) {
				{
				{
				setState(133);
				object();
				}
				}
				setState(138);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(139);
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
			setState(141);
			match(String);
			setState(145);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LANGTAG:
				{
				setState(142);
				match(LANGTAG);
				}
				break;
			case T__12:
				{
				setState(143);
				match(T__12);
				setState(144);
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

	public static class CommentContext extends ParserRuleContext {
		public TerminalNode LINE_COMMENT() { return getToken(TurtliteParser.LINE_COMMENT, 0); }
		public CommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).enterComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TurtliteListener ) ((TurtliteListener)listener).exitComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TurtliteVisitor ) return ((TurtliteVisitor<? extends T>)visitor).visitComment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommentContext comment() throws RecognitionException {
		CommentContext _localctx = new CommentContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_comment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(147);
			match(LINE_COMMENT);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 38, RULE_iri);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
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
		"\u0004\u0001.\u0098\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0001\u0000\u0005\u0000*\b\u0000\n\u0000\f\u0000"+
		"-\t\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0003\u00014\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0003\u0002:\b\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007P\b\u0007"+
		"\n\u0007\f\u0007S\t\u0007\u0001\u0007\u0003\u0007V\b\u0007\u0001\b\u0001"+
		"\b\u0001\b\u0001\t\u0001\t\u0001\t\u0005\t^\b\t\n\t\f\ta\t\t\u0001\n\u0001"+
		"\n\u0003\ne\b\n\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000bj\b\u000b"+
		"\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0003\rs\b\r"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000ex\b\u000e\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0005\u000f~\b\u000f\n\u000f\f\u000f"+
		"\u0081\t\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0005\u0010"+
		"\u0087\b\u0010\n\u0010\f\u0010\u008a\t\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u0092\b\u0011\u0001"+
		"\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0000\u0000\u0014"+
		"\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a"+
		"\u001c\u001e \"$&\u0000\u0001\u0002\u0000\u0015\u0015\u0017\u0017\u0099"+
		"\u0000+\u0001\u0000\u0000\u0000\u00023\u0001\u0000\u0000\u0000\u00049"+
		"\u0001\u0000\u0000\u0000\u0006;\u0001\u0000\u0000\u0000\b@\u0001\u0000"+
		"\u0000\u0000\nD\u0001\u0000\u0000\u0000\fG\u0001\u0000\u0000\u0000\u000e"+
		"U\u0001\u0000\u0000\u0000\u0010W\u0001\u0000\u0000\u0000\u0012Z\u0001"+
		"\u0000\u0000\u0000\u0014d\u0001\u0000\u0000\u0000\u0016i\u0001\u0000\u0000"+
		"\u0000\u0018k\u0001\u0000\u0000\u0000\u001ar\u0001\u0000\u0000\u0000\u001c"+
		"w\u0001\u0000\u0000\u0000\u001ey\u0001\u0000\u0000\u0000 \u0084\u0001"+
		"\u0000\u0000\u0000\"\u008d\u0001\u0000\u0000\u0000$\u0093\u0001\u0000"+
		"\u0000\u0000&\u0095\u0001\u0000\u0000\u0000(*\u0003\u0002\u0001\u0000"+
		")(\u0001\u0000\u0000\u0000*-\u0001\u0000\u0000\u0000+)\u0001\u0000\u0000"+
		"\u0000+,\u0001\u0000\u0000\u0000,\u0001\u0001\u0000\u0000\u0000-+\u0001"+
		"\u0000\u0000\u0000.4\u0003\u0004\u0002\u0000/0\u0003\u000e\u0007\u0000"+
		"01\u0005\u0001\u0000\u000014\u0001\u0000\u0000\u000024\u0003$\u0012\u0000"+
		"3.\u0001\u0000\u0000\u00003/\u0001\u0000\u0000\u000032\u0001\u0000\u0000"+
		"\u00004\u0003\u0001\u0000\u0000\u00005:\u0003\u0006\u0003\u00006:\u0003"+
		"\b\u0004\u00007:\u0003\f\u0006\u00008:\u0003\n\u0005\u000095\u0001\u0000"+
		"\u0000\u000096\u0001\u0000\u0000\u000097\u0001\u0000\u0000\u000098\u0001"+
		"\u0000\u0000\u0000:\u0005\u0001\u0000\u0000\u0000;<\u0005\u0002\u0000"+
		"\u0000<=\u0005\u0016\u0000\u0000=>\u0005\u0015\u0000\u0000>?\u0005\u0001"+
		"\u0000\u0000?\u0007\u0001\u0000\u0000\u0000@A\u0005\u0003\u0000\u0000"+
		"AB\u0005\u0015\u0000\u0000BC\u0005\u0001\u0000\u0000C\t\u0001\u0000\u0000"+
		"\u0000DE\u0005\u0004\u0000\u0000EF\u0005\u0015\u0000\u0000F\u000b\u0001"+
		"\u0000\u0000\u0000GH\u0005\u0005\u0000\u0000HI\u0005\u0016\u0000\u0000"+
		"IJ\u0005\u0015\u0000\u0000J\r\u0001\u0000\u0000\u0000KL\u0003\u0016\u000b"+
		"\u0000LQ\u0003\u0010\b\u0000MN\u0005\u0006\u0000\u0000NP\u0003\u0010\b"+
		"\u0000OM\u0001\u0000\u0000\u0000PS\u0001\u0000\u0000\u0000QO\u0001\u0000"+
		"\u0000\u0000QR\u0001\u0000\u0000\u0000RV\u0001\u0000\u0000\u0000SQ\u0001"+
		"\u0000\u0000\u0000TV\u0003\u001e\u000f\u0000UK\u0001\u0000\u0000\u0000"+
		"UT\u0001\u0000\u0000\u0000V\u000f\u0001\u0000\u0000\u0000WX\u0003\u0014"+
		"\n\u0000XY\u0003\u0012\t\u0000Y\u0011\u0001\u0000\u0000\u0000Z_\u0003"+
		"\u001a\r\u0000[\\\u0005\u0007\u0000\u0000\\^\u0003\u001a\r\u0000][\u0001"+
		"\u0000\u0000\u0000^a\u0001\u0000\u0000\u0000_]\u0001\u0000\u0000\u0000"+
		"_`\u0001\u0000\u0000\u0000`\u0013\u0001\u0000\u0000\u0000a_\u0001\u0000"+
		"\u0000\u0000be\u0003\u0018\f\u0000ce\u0005\b\u0000\u0000db\u0001\u0000"+
		"\u0000\u0000dc\u0001\u0000\u0000\u0000e\u0015\u0001\u0000\u0000\u0000"+
		"fj\u0003&\u0013\u0000gj\u0005\u0012\u0000\u0000hj\u0003 \u0010\u0000i"+
		"f\u0001\u0000\u0000\u0000ig\u0001\u0000\u0000\u0000ih\u0001\u0000\u0000"+
		"\u0000j\u0017\u0001\u0000\u0000\u0000kl\u0003&\u0013\u0000l\u0019\u0001"+
		"\u0000\u0000\u0000ms\u0003&\u0013\u0000ns\u0005\u0012\u0000\u0000os\u0003"+
		" \u0010\u0000ps\u0003\u001e\u000f\u0000qs\u0003\u001c\u000e\u0000rm\u0001"+
		"\u0000\u0000\u0000rn\u0001\u0000\u0000\u0000ro\u0001\u0000\u0000\u0000"+
		"rp\u0001\u0000\u0000\u0000rq\u0001\u0000\u0000\u0000s\u001b\u0001\u0000"+
		"\u0000\u0000tx\u0003\"\u0011\u0000ux\u0005\u000e\u0000\u0000vx\u0005\u0010"+
		"\u0000\u0000wt\u0001\u0000\u0000\u0000wu\u0001\u0000\u0000\u0000wv\u0001"+
		"\u0000\u0000\u0000x\u001d\u0001\u0000\u0000\u0000yz\u0005\t\u0000\u0000"+
		"z\u007f\u0003\u0010\b\u0000{|\u0005\u0006\u0000\u0000|~\u0003\u0010\b"+
		"\u0000}{\u0001\u0000\u0000\u0000~\u0081\u0001\u0000\u0000\u0000\u007f"+
		"}\u0001\u0000\u0000\u0000\u007f\u0080\u0001\u0000\u0000\u0000\u0080\u0082"+
		"\u0001\u0000\u0000\u0000\u0081\u007f\u0001\u0000\u0000\u0000\u0082\u0083"+
		"\u0005\n\u0000\u0000\u0083\u001f\u0001\u0000\u0000\u0000\u0084\u0088\u0005"+
		"\u000b\u0000\u0000\u0085\u0087\u0003\u001a\r\u0000\u0086\u0085\u0001\u0000"+
		"\u0000\u0000\u0087\u008a\u0001\u0000\u0000\u0000\u0088\u0086\u0001\u0000"+
		"\u0000\u0000\u0088\u0089\u0001\u0000\u0000\u0000\u0089\u008b\u0001\u0000"+
		"\u0000\u0000\u008a\u0088\u0001\u0000\u0000\u0000\u008b\u008c\u0005\f\u0000"+
		"\u0000\u008c!\u0001\u0000\u0000\u0000\u008d\u0091\u0005\u0011\u0000\u0000"+
		"\u008e\u0092\u0005\u001a\u0000\u0000\u008f\u0090\u0005\r\u0000\u0000\u0090"+
		"\u0092\u0003&\u0013\u0000\u0091\u008e\u0001\u0000\u0000\u0000\u0091\u008f"+
		"\u0001\u0000\u0000\u0000\u0091\u0092\u0001\u0000\u0000\u0000\u0092#\u0001"+
		"\u0000\u0000\u0000\u0093\u0094\u0005\u000f\u0000\u0000\u0094%\u0001\u0000"+
		"\u0000\u0000\u0095\u0096\u0007\u0000\u0000\u0000\u0096\'\u0001\u0000\u0000"+
		"\u0000\r+39QU_dirw\u007f\u0088\u0091";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}