// Generated from C:/Users/david/GithubRepos/IMAPI/src/main/grammars/IMQ.g4 by ANTLR 4.13.2
package imq;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class IMQParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, EXCLUDE=5, IF=6, LATEST=7, EARLIEST=8, 
		WHERE=9, PREFIX=10, CREATE=11, VARIABLES=12, DEFINE=13, NUMERIC=14, INTEGER=15, 
		AND=16, OR=17, NOT=18, WITH=19, FROM=20, OB=21, CB=22, IS=23, IN=24, AS=25, 
		COHORT=26, EQ=27, GT=28, GTE=29, LT=30, LTE=31, VARIABLE=32, PARAMETER=33, 
		VALUE=34, WS=35, IRI_REF=36, PNAME_LN=37, PNAME_NS=38;
	public static final int
		RULE_imq = 0, RULE_definition = 1, RULE_variableDefinition = 2, RULE_definedAs = 3, 
		RULE_prefix = 4, RULE_setLabel = 5, RULE_iri = 6, RULE_cohort = 7, RULE_type = 8, 
		RULE_dataset = 9, RULE_task = 10, RULE_cohortDefinition = 11, RULE_query = 12, 
		RULE_cte = 13, RULE_booleanCte = 14, RULE_exclude = 15, RULE_booleanClause = 16, 
		RULE_with = 17, RULE_and = 18, RULE_or = 19, RULE_not = 20, RULE_from = 21, 
		RULE_set = 22, RULE_where = 23, RULE_inResultSet = 24, RULE_resultSet = 25, 
		RULE_name = 26, RULE_propertyTest = 27, RULE_valueCompare = 28, RULE_parameter = 29, 
		RULE_is = 30, RULE_function = 31, RULE_property = 32, RULE_operator = 33, 
		RULE_value = 34, RULE_units = 35, RULE_ordered = 36;
	private static String[] makeRuleNames() {
		return new String[] {
			"imq", "definition", "variableDefinition", "definedAs", "prefix", "setLabel", 
			"iri", "cohort", "type", "dataset", "task", "cohortDefinition", "query", 
			"cte", "booleanCte", "exclude", "booleanClause", "with", "and", "or", 
			"not", "from", "set", "where", "inResultSet", "resultSet", "name", "propertyTest", 
			"valueCompare", "parameter", "is", "function", "property", "operator", 
			"value", "units", "ordered"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'dataset'", "'task'", "'&'", "'.'", null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, "'('", 
			"')'", null, null, null, null, "'='", "'>'", "'>='", "'<'", "'<='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, "EXCLUDE", "IF", "LATEST", "EARLIEST", 
			"WHERE", "PREFIX", "CREATE", "VARIABLES", "DEFINE", "NUMERIC", "INTEGER", 
			"AND", "OR", "NOT", "WITH", "FROM", "OB", "CB", "IS", "IN", "AS", "COHORT", 
			"EQ", "GT", "GTE", "LT", "LTE", "VARIABLE", "PARAMETER", "VALUE", "WS", 
			"IRI_REF", "PNAME_LN", "PNAME_NS"
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
	public String getGrammarFileName() { return "IMQ.g4"; }

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
	public static class ImqContext extends ParserRuleContext {
		public List<DefinitionContext> definition() {
			return getRuleContexts(DefinitionContext.class);
		}
		public DefinitionContext definition(int i) {
			return getRuleContext(DefinitionContext.class,i);
		}
		public List<PrefixContext> prefix() {
			return getRuleContexts(PrefixContext.class);
		}
		public PrefixContext prefix(int i) {
			return getRuleContext(PrefixContext.class,i);
		}
		public ImqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_imq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterImq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitImq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitImq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImqContext imq() throws RecognitionException {
		ImqContext _localctx = new ImqContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_imq);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(74);
				definition();
				}
				}
				setState(77); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==DEFINE );
			setState(82);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PREFIX) {
				{
				{
				setState(79);
				prefix();
				}
				}
				setState(84);
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
	public static class DefinitionContext extends ParserRuleContext {
		public TerminalNode DEFINE() { return getToken(IMQParser.DEFINE, 0); }
		public VariableDefinitionContext variableDefinition() {
			return getRuleContext(VariableDefinitionContext.class,0);
		}
		public DefinedAsContext definedAs() {
			return getRuleContext(DefinedAsContext.class,0);
		}
		public DefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefinitionContext definition() throws RecognitionException {
		DefinitionContext _localctx = new DefinitionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_definition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			match(DEFINE);
			setState(88);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
				setState(86);
				variableDefinition();
				}
				break;
			case 2:
				{
				setState(87);
				definedAs();
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
	public static class VariableDefinitionContext extends ParserRuleContext {
		public TerminalNode VARIABLE() { return getToken(IMQParser.VARIABLE, 0); }
		public TerminalNode EQ() { return getToken(IMQParser.EQ, 0); }
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public VariableDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterVariableDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitVariableDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitVariableDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDefinitionContext variableDefinition() throws RecognitionException {
		VariableDefinitionContext _localctx = new VariableDefinitionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_variableDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			match(VARIABLE);
			setState(91);
			match(EQ);
			setState(92);
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

	@SuppressWarnings("CheckReturnValue")
	public static class DefinedAsContext extends ParserRuleContext {
		public TerminalNode VARIABLE() { return getToken(IMQParser.VARIABLE, 0); }
		public TerminalNode AS() { return getToken(IMQParser.AS, 0); }
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public DefinedAsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definedAs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterDefinedAs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitDefinedAs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitDefinedAs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefinedAsContext definedAs() throws RecognitionException {
		DefinedAsContext _localctx = new DefinedAsContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_definedAs);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94);
			match(VARIABLE);
			setState(95);
			match(AS);
			setState(96);
			query();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrefixContext extends ParserRuleContext {
		public TerminalNode PREFIX() { return getToken(IMQParser.PREFIX, 0); }
		public TerminalNode VARIABLE() { return getToken(IMQParser.VARIABLE, 0); }
		public TerminalNode EQ() { return getToken(IMQParser.EQ, 0); }
		public TerminalNode IRI_REF() { return getToken(IMQParser.IRI_REF, 0); }
		public PrefixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterPrefix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitPrefix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitPrefix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixContext prefix() throws RecognitionException {
		PrefixContext _localctx = new PrefixContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_prefix);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			match(PREFIX);
			setState(99);
			match(VARIABLE);
			setState(100);
			match(EQ);
			setState(101);
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
	public static class SetLabelContext extends ParserRuleContext {
		public TerminalNode VARIABLE() { return getToken(IMQParser.VARIABLE, 0); }
		public SetLabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setLabel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterSetLabel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitSetLabel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitSetLabel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetLabelContext setLabel() throws RecognitionException {
		SetLabelContext _localctx = new SetLabelContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_setLabel);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			match(VARIABLE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IriContext extends ParserRuleContext {
		public TerminalNode IRI_REF() { return getToken(IMQParser.IRI_REF, 0); }
		public TerminalNode PNAME_LN() { return getToken(IMQParser.PNAME_LN, 0); }
		public IriContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iri; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterIri(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitIri(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitIri(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IriContext iri() throws RecognitionException {
		IriContext _localctx = new IriContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_iri);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			_la = _input.LA(1);
			if ( !(_la==IRI_REF || _la==PNAME_LN) ) {
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
	public static class CohortContext extends ParserRuleContext {
		public TerminalNode COHORT() { return getToken(IMQParser.COHORT, 0); }
		public TerminalNode VARIABLE() { return getToken(IMQParser.VARIABLE, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public CohortDefinitionContext cohortDefinition() {
			return getRuleContext(CohortDefinitionContext.class,0);
		}
		public CohortContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cohort; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterCohort(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitCohort(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitCohort(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CohortContext cohort() throws RecognitionException {
		CohortContext _localctx = new CohortContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_cohort);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			match(COHORT);
			setState(108);
			match(VARIABLE);
			setState(109);
			type();
			setState(110);
			cohortDefinition();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode VARIABLE() { return getToken(IMQParser.VARIABLE, 0); }
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
		enterRule(_localctx, 16, RULE_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			match(VARIABLE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DatasetContext extends ParserRuleContext {
		public TerminalNode VARIABLE() { return getToken(IMQParser.VARIABLE, 0); }
		public DatasetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterDataset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitDataset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitDataset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DatasetContext dataset() throws RecognitionException {
		DatasetContext _localctx = new DatasetContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_dataset);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
			match(T__0);
			setState(115);
			match(VARIABLE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TaskContext extends ParserRuleContext {
		public TerminalNode VARIABLE() { return getToken(IMQParser.VARIABLE, 0); }
		public TaskContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_task; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterTask(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitTask(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitTask(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TaskContext task() throws RecognitionException {
		TaskContext _localctx = new TaskContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_task);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			match(T__1);
			setState(118);
			match(VARIABLE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CohortDefinitionContext extends ParserRuleContext {
		public TerminalNode AS() { return getToken(IMQParser.AS, 0); }
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public CohortDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cohortDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterCohortDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitCohortDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitCohortDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CohortDefinitionContext cohortDefinition() throws RecognitionException {
		CohortDefinitionContext _localctx = new CohortDefinitionContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_cohortDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			match(AS);
			setState(121);
			query();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode OB() { return getToken(IMQParser.OB, 0); }
		public List<CteContext> cte() {
			return getRuleContexts(CteContext.class);
		}
		public CteContext cte(int i) {
			return getRuleContext(CteContext.class,i);
		}
		public TerminalNode CB() { return getToken(IMQParser.CB, 0); }
		public List<WithContext> with() {
			return getRuleContexts(WithContext.class);
		}
		public WithContext with(int i) {
			return getRuleContext(WithContext.class,i);
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
			setState(123);
			match(OB);
			setState(124);
			cte();
			setState(130);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WITH || _la==VARIABLE) {
				{
				{
				setState(125);
				with();
				setState(126);
				cte();
				}
				}
				setState(132);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(133);
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
	public static class CteContext extends ParserRuleContext {
		public WithContext with() {
			return getRuleContext(WithContext.class,0);
		}
		public List<BooleanCteContext> booleanCte() {
			return getRuleContexts(BooleanCteContext.class);
		}
		public BooleanCteContext booleanCte(int i) {
			return getRuleContext(BooleanCteContext.class,i);
		}
		public FromContext from() {
			return getRuleContext(FromContext.class,0);
		}
		public OrderedContext ordered() {
			return getRuleContext(OrderedContext.class,0);
		}
		public WhereContext where() {
			return getRuleContext(WhereContext.class,0);
		}
		public List<BooleanClauseContext> booleanClause() {
			return getRuleContexts(BooleanClauseContext.class);
		}
		public BooleanClauseContext booleanClause(int i) {
			return getRuleContext(BooleanClauseContext.class,i);
		}
		public CteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cte; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterCte(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitCte(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitCte(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CteContext cte() throws RecognitionException {
		CteContext _localctx = new CteContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_cte);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(151);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(135);
				with();
				}
				break;
			case 2:
				{
				setState(149);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1048960L) != 0)) {
					{
					setState(137);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==LATEST || _la==EARLIEST) {
						{
						setState(136);
						ordered();
						}
					}

					setState(139);
					from();
					setState(141);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==WHERE) {
						{
						setState(140);
						where();
						}
					}

					setState(146);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(143);
							booleanClause();
							}
							} 
						}
						setState(148);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
					}
					}
				}

				}
				break;
			}
			setState(156);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(153);
					booleanCte();
					}
					} 
				}
				setState(158);
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

	@SuppressWarnings("CheckReturnValue")
	public static class BooleanCteContext extends ParserRuleContext {
		public CteContext cte() {
			return getRuleContext(CteContext.class,0);
		}
		public AndContext and() {
			return getRuleContext(AndContext.class,0);
		}
		public OrContext or() {
			return getRuleContext(OrContext.class,0);
		}
		public ExcludeContext exclude() {
			return getRuleContext(ExcludeContext.class,0);
		}
		public BooleanCteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanCte; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterBooleanCte(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitBooleanCte(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitBooleanCte(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanCteContext booleanCte() throws RecognitionException {
		BooleanCteContext _localctx = new BooleanCteContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_booleanCte);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
			case AND:
				{
				setState(159);
				and();
				}
				break;
			case OR:
				{
				setState(160);
				or();
				}
				break;
			case EXCLUDE:
				{
				setState(161);
				exclude();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(164);
			cte();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExcludeContext extends ParserRuleContext {
		public TerminalNode EXCLUDE() { return getToken(IMQParser.EXCLUDE, 0); }
		public ExcludeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exclude; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterExclude(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitExclude(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitExclude(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExcludeContext exclude() throws RecognitionException {
		ExcludeContext _localctx = new ExcludeContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_exclude);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			match(EXCLUDE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BooleanClauseContext extends ParserRuleContext {
		public AndContext and() {
			return getRuleContext(AndContext.class,0);
		}
		public OrContext or() {
			return getRuleContext(OrContext.class,0);
		}
		public NotContext not() {
			return getRuleContext(NotContext.class,0);
		}
		public ResultSetContext resultSet() {
			return getRuleContext(ResultSetContext.class,0);
		}
		public WhereContext where() {
			return getRuleContext(WhereContext.class,0);
		}
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public PropertyTestContext propertyTest() {
			return getRuleContext(PropertyTestContext.class,0);
		}
		public BooleanClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterBooleanClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitBooleanClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitBooleanClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanClauseContext booleanClause() throws RecognitionException {
		BooleanClauseContext _localctx = new BooleanClauseContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_booleanClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(171);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
			case AND:
				{
				setState(168);
				and();
				}
				break;
			case OR:
				{
				setState(169);
				or();
				}
				break;
			case NOT:
				{
				setState(170);
				not();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(177);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				{
				setState(173);
				resultSet();
				}
				break;
			case 2:
				{
				setState(174);
				where();
				}
				break;
			case 3:
				{
				setState(175);
				query();
				}
				break;
			case 4:
				{
				setState(176);
				propertyTest();
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
	public static class WithContext extends ParserRuleContext {
		public TerminalNode WITH() { return getToken(IMQParser.WITH, 0); }
		public TerminalNode VARIABLE() { return getToken(IMQParser.VARIABLE, 0); }
		public TerminalNode AS() { return getToken(IMQParser.AS, 0); }
		public TerminalNode OB() { return getToken(IMQParser.OB, 0); }
		public CteContext cte() {
			return getRuleContext(CteContext.class,0);
		}
		public TerminalNode CB() { return getToken(IMQParser.CB, 0); }
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
		enterRule(_localctx, 34, RULE_with);
		try {
			setState(187);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case WITH:
				enterOuterAlt(_localctx, 1);
				{
				setState(179);
				match(WITH);
				{
				setState(180);
				match(VARIABLE);
				setState(181);
				match(AS);
				setState(182);
				match(OB);
				setState(183);
				cte();
				setState(184);
				match(CB);
				}
				}
				break;
			case VARIABLE:
				enterOuterAlt(_localctx, 2);
				{
				setState(186);
				match(VARIABLE);
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
	public static class AndContext extends ParserRuleContext {
		public TerminalNode AND() { return getToken(IMQParser.AND, 0); }
		public AndContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_and; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitAnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitAnd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndContext and() throws RecognitionException {
		AndContext _localctx = new AndContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_and);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(189);
			_la = _input.LA(1);
			if ( !(_la==T__2 || _la==AND) ) {
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
	public static class OrContext extends ParserRuleContext {
		public TerminalNode OR() { return getToken(IMQParser.OR, 0); }
		public OrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_or; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitOr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitOr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrContext or() throws RecognitionException {
		OrContext _localctx = new OrContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_or);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(191);
			match(OR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NotContext extends ParserRuleContext {
		public TerminalNode NOT() { return getToken(IMQParser.NOT, 0); }
		public NotContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_not; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterNot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitNot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitNot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NotContext not() throws RecognitionException {
		NotContext _localctx = new NotContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_not);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(193);
			match(NOT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode FROM() { return getToken(IMQParser.FROM, 0); }
		public SetContext set() {
			return getRuleContext(SetContext.class,0);
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
		enterRule(_localctx, 42, RULE_from);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			match(FROM);
			setState(196);
			set();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode VARIABLE() { return getToken(IMQParser.VARIABLE, 0); }
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
		enterRule(_localctx, 44, RULE_set);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(198);
			match(VARIABLE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode WHERE() { return getToken(IMQParser.WHERE, 0); }
		public PropertyTestContext propertyTest() {
			return getRuleContext(PropertyTestContext.class,0);
		}
		public FunctionContext function() {
			return getRuleContext(FunctionContext.class,0);
		}
		public InResultSetContext inResultSet() {
			return getRuleContext(InResultSetContext.class,0);
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
		enterRule(_localctx, 46, RULE_where);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(200);
			match(WHERE);
			setState(204);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VARIABLE:
				{
				setState(201);
				propertyTest();
				}
				break;
			case PARAMETER:
				{
				setState(202);
				function();
				}
				break;
			case IN:
				{
				setState(203);
				inResultSet();
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
	public static class InResultSetContext extends ParserRuleContext {
		public TerminalNode IN() { return getToken(IMQParser.IN, 0); }
		public ResultSetContext resultSet() {
			return getRuleContext(ResultSetContext.class,0);
		}
		public InResultSetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inResultSet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterInResultSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitInResultSet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitInResultSet(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InResultSetContext inResultSet() throws RecognitionException {
		InResultSetContext _localctx = new InResultSetContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_inResultSet);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(206);
			match(IN);
			setState(207);
			resultSet();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ResultSetContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public ResultSetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resultSet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterResultSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitResultSet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitResultSet(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResultSetContext resultSet() throws RecognitionException {
		ResultSetContext _localctx = new ResultSetContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_resultSet);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(209);
			name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode VARIABLE() { return getToken(IMQParser.VARIABLE, 0); }
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
		enterRule(_localctx, 52, RULE_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(211);
			match(VARIABLE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PropertyTestContext extends ParserRuleContext {
		public PropertyContext property() {
			return getRuleContext(PropertyContext.class,0);
		}
		public ValueCompareContext valueCompare() {
			return getRuleContext(ValueCompareContext.class,0);
		}
		public IsContext is() {
			return getRuleContext(IsContext.class,0);
		}
		public PropertyTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterPropertyTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitPropertyTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitPropertyTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyTestContext propertyTest() throws RecognitionException {
		PropertyTestContext _localctx = new PropertyTestContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_propertyTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(213);
			property();
			setState(216);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EQ:
			case GT:
			case GTE:
			case LT:
			case LTE:
				{
				setState(214);
				valueCompare();
				}
				break;
			case IS:
				{
				setState(215);
				is();
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
	public static class ValueCompareContext extends ParserRuleContext {
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public ParameterContext parameter() {
			return getRuleContext(ParameterContext.class,0);
		}
		public PropertyContext property() {
			return getRuleContext(PropertyContext.class,0);
		}
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public UnitsContext units() {
			return getRuleContext(UnitsContext.class,0);
		}
		public ValueCompareContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueCompare; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterValueCompare(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitValueCompare(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitValueCompare(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueCompareContext valueCompare() throws RecognitionException {
		ValueCompareContext _localctx = new ValueCompareContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_valueCompare);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(218);
			operator();
			setState(225);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NUMERIC:
				{
				{
				setState(219);
				value();
				setState(221);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
				case 1:
					{
					setState(220);
					units();
					}
					break;
				}
				}
				}
				break;
			case PARAMETER:
				{
				setState(223);
				parameter();
				}
				break;
			case VARIABLE:
				{
				setState(224);
				property();
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
	public static class ParameterContext extends ParserRuleContext {
		public TerminalNode PARAMETER() { return getToken(IMQParser.PARAMETER, 0); }
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
		enterRule(_localctx, 58, RULE_parameter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(227);
			match(PARAMETER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IsContext extends ParserRuleContext {
		public TerminalNode IS() { return getToken(IMQParser.IS, 0); }
		public TerminalNode VARIABLE() { return getToken(IMQParser.VARIABLE, 0); }
		public IsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_is; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterIs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitIs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitIs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IsContext is() throws RecognitionException {
		IsContext _localctx = new IsContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_is);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(229);
			match(IS);
			setState(230);
			match(VARIABLE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionContext extends ParserRuleContext {
		public TerminalNode PARAMETER() { return getToken(IMQParser.PARAMETER, 0); }
		public FunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitFunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionContext function() throws RecognitionException {
		FunctionContext _localctx = new FunctionContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_function);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(232);
			match(PARAMETER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PropertyContext extends ParserRuleContext {
		public List<TerminalNode> VARIABLE() { return getTokens(IMQParser.VARIABLE); }
		public TerminalNode VARIABLE(int i) {
			return getToken(IMQParser.VARIABLE, i);
		}
		public PropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_property; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyContext property() throws RecognitionException {
		PropertyContext _localctx = new PropertyContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_property);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(234);
			match(VARIABLE);
			setState(239);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(235);
				match(T__3);
				setState(236);
				match(VARIABLE);
				}
				}
				setState(241);
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
	public static class OperatorContext extends ParserRuleContext {
		public TerminalNode EQ() { return getToken(IMQParser.EQ, 0); }
		public TerminalNode GT() { return getToken(IMQParser.GT, 0); }
		public TerminalNode GTE() { return getToken(IMQParser.GTE, 0); }
		public TerminalNode LT() { return getToken(IMQParser.LT, 0); }
		public TerminalNode LTE() { return getToken(IMQParser.LTE, 0); }
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
		enterRule(_localctx, 66, RULE_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(242);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 4160749568L) != 0)) ) {
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
	public static class ValueContext extends ParserRuleContext {
		public TerminalNode NUMERIC() { return getToken(IMQParser.NUMERIC, 0); }
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
		enterRule(_localctx, 68, RULE_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(244);
			match(NUMERIC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode VARIABLE() { return getToken(IMQParser.VARIABLE, 0); }
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
		enterRule(_localctx, 70, RULE_units);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(246);
			match(VARIABLE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OrderedContext extends ParserRuleContext {
		public TerminalNode LATEST() { return getToken(IMQParser.LATEST, 0); }
		public TerminalNode EARLIEST() { return getToken(IMQParser.EARLIEST, 0); }
		public TerminalNode INTEGER() { return getToken(IMQParser.INTEGER, 0); }
		public OrderedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ordered; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).enterOrdered(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMQListener ) ((IMQListener)listener).exitOrdered(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMQVisitor ) return ((IMQVisitor<? extends T>)visitor).visitOrdered(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderedContext ordered() throws RecognitionException {
		OrderedContext _localctx = new OrderedContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_ordered);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(248);
			_la = _input.LA(1);
			if ( !(_la==LATEST || _la==EARLIEST) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(250);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==INTEGER) {
				{
				setState(249);
				match(INTEGER);
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

	public static final String _serializedATN =
		"\u0004\u0001&\u00fd\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
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
		"#\u0007#\u0002$\u0007$\u0001\u0000\u0004\u0000L\b\u0000\u000b\u0000\f"+
		"\u0000M\u0001\u0000\u0005\u0000Q\b\u0000\n\u0000\f\u0000T\t\u0000\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0003\u0001Y\b\u0001\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t"+
		"\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0005\f\u0081\b\f\n\f\f\f\u0084\t\f\u0001\f"+
		"\u0001\f\u0001\r\u0001\r\u0003\r\u008a\b\r\u0001\r\u0001\r\u0003\r\u008e"+
		"\b\r\u0001\r\u0005\r\u0091\b\r\n\r\f\r\u0094\t\r\u0003\r\u0096\b\r\u0003"+
		"\r\u0098\b\r\u0001\r\u0005\r\u009b\b\r\n\r\f\r\u009e\t\r\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0003\u000e\u00a3\b\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0003\u0010\u00ac"+
		"\b\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0003\u0010\u00b2"+
		"\b\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u00bc\b\u0011\u0001\u0012\u0001"+
		"\u0012\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0003\u0017\u00cd\b\u0017\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0019\u0001\u0019\u0001\u001a\u0001\u001a\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0003\u001b\u00d9\b\u001b\u0001\u001c\u0001\u001c\u0001"+
		"\u001c\u0003\u001c\u00de\b\u001c\u0001\u001c\u0001\u001c\u0003\u001c\u00e2"+
		"\b\u001c\u0001\u001d\u0001\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0001"+
		"\u001f\u0001\u001f\u0001 \u0001 \u0001 \u0005 \u00ee\b \n \f \u00f1\t"+
		" \u0001!\u0001!\u0001\"\u0001\"\u0001#\u0001#\u0001$\u0001$\u0003$\u00fb"+
		"\b$\u0001$\u0000\u0000%\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012"+
		"\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDFH\u0000\u0004"+
		"\u0001\u0000$%\u0002\u0000\u0003\u0003\u0010\u0010\u0001\u0000\u001b\u001f"+
		"\u0001\u0000\u0007\b\u00f1\u0000K\u0001\u0000\u0000\u0000\u0002U\u0001"+
		"\u0000\u0000\u0000\u0004Z\u0001\u0000\u0000\u0000\u0006^\u0001\u0000\u0000"+
		"\u0000\bb\u0001\u0000\u0000\u0000\ng\u0001\u0000\u0000\u0000\fi\u0001"+
		"\u0000\u0000\u0000\u000ek\u0001\u0000\u0000\u0000\u0010p\u0001\u0000\u0000"+
		"\u0000\u0012r\u0001\u0000\u0000\u0000\u0014u\u0001\u0000\u0000\u0000\u0016"+
		"x\u0001\u0000\u0000\u0000\u0018{\u0001\u0000\u0000\u0000\u001a\u0097\u0001"+
		"\u0000\u0000\u0000\u001c\u00a2\u0001\u0000\u0000\u0000\u001e\u00a6\u0001"+
		"\u0000\u0000\u0000 \u00ab\u0001\u0000\u0000\u0000\"\u00bb\u0001\u0000"+
		"\u0000\u0000$\u00bd\u0001\u0000\u0000\u0000&\u00bf\u0001\u0000\u0000\u0000"+
		"(\u00c1\u0001\u0000\u0000\u0000*\u00c3\u0001\u0000\u0000\u0000,\u00c6"+
		"\u0001\u0000\u0000\u0000.\u00c8\u0001\u0000\u0000\u00000\u00ce\u0001\u0000"+
		"\u0000\u00002\u00d1\u0001\u0000\u0000\u00004\u00d3\u0001\u0000\u0000\u0000"+
		"6\u00d5\u0001\u0000\u0000\u00008\u00da\u0001\u0000\u0000\u0000:\u00e3"+
		"\u0001\u0000\u0000\u0000<\u00e5\u0001\u0000\u0000\u0000>\u00e8\u0001\u0000"+
		"\u0000\u0000@\u00ea\u0001\u0000\u0000\u0000B\u00f2\u0001\u0000\u0000\u0000"+
		"D\u00f4\u0001\u0000\u0000\u0000F\u00f6\u0001\u0000\u0000\u0000H\u00f8"+
		"\u0001\u0000\u0000\u0000JL\u0003\u0002\u0001\u0000KJ\u0001\u0000\u0000"+
		"\u0000LM\u0001\u0000\u0000\u0000MK\u0001\u0000\u0000\u0000MN\u0001\u0000"+
		"\u0000\u0000NR\u0001\u0000\u0000\u0000OQ\u0003\b\u0004\u0000PO\u0001\u0000"+
		"\u0000\u0000QT\u0001\u0000\u0000\u0000RP\u0001\u0000\u0000\u0000RS\u0001"+
		"\u0000\u0000\u0000S\u0001\u0001\u0000\u0000\u0000TR\u0001\u0000\u0000"+
		"\u0000UX\u0005\r\u0000\u0000VY\u0003\u0004\u0002\u0000WY\u0003\u0006\u0003"+
		"\u0000XV\u0001\u0000\u0000\u0000XW\u0001\u0000\u0000\u0000Y\u0003\u0001"+
		"\u0000\u0000\u0000Z[\u0005 \u0000\u0000[\\\u0005\u001b\u0000\u0000\\]"+
		"\u0003\f\u0006\u0000]\u0005\u0001\u0000\u0000\u0000^_\u0005 \u0000\u0000"+
		"_`\u0005\u0019\u0000\u0000`a\u0003\u0018\f\u0000a\u0007\u0001\u0000\u0000"+
		"\u0000bc\u0005\n\u0000\u0000cd\u0005 \u0000\u0000de\u0005\u001b\u0000"+
		"\u0000ef\u0005$\u0000\u0000f\t\u0001\u0000\u0000\u0000gh\u0005 \u0000"+
		"\u0000h\u000b\u0001\u0000\u0000\u0000ij\u0007\u0000\u0000\u0000j\r\u0001"+
		"\u0000\u0000\u0000kl\u0005\u001a\u0000\u0000lm\u0005 \u0000\u0000mn\u0003"+
		"\u0010\b\u0000no\u0003\u0016\u000b\u0000o\u000f\u0001\u0000\u0000\u0000"+
		"pq\u0005 \u0000\u0000q\u0011\u0001\u0000\u0000\u0000rs\u0005\u0001\u0000"+
		"\u0000st\u0005 \u0000\u0000t\u0013\u0001\u0000\u0000\u0000uv\u0005\u0002"+
		"\u0000\u0000vw\u0005 \u0000\u0000w\u0015\u0001\u0000\u0000\u0000xy\u0005"+
		"\u0019\u0000\u0000yz\u0003\u0018\f\u0000z\u0017\u0001\u0000\u0000\u0000"+
		"{|\u0005\u0015\u0000\u0000|\u0082\u0003\u001a\r\u0000}~\u0003\"\u0011"+
		"\u0000~\u007f\u0003\u001a\r\u0000\u007f\u0081\u0001\u0000\u0000\u0000"+
		"\u0080}\u0001\u0000\u0000\u0000\u0081\u0084\u0001\u0000\u0000\u0000\u0082"+
		"\u0080\u0001\u0000\u0000\u0000\u0082\u0083\u0001\u0000\u0000\u0000\u0083"+
		"\u0085\u0001\u0000\u0000\u0000\u0084\u0082\u0001\u0000\u0000\u0000\u0085"+
		"\u0086\u0005\u0016\u0000\u0000\u0086\u0019\u0001\u0000\u0000\u0000\u0087"+
		"\u0098\u0003\"\u0011\u0000\u0088\u008a\u0003H$\u0000\u0089\u0088\u0001"+
		"\u0000\u0000\u0000\u0089\u008a\u0001\u0000\u0000\u0000\u008a\u008b\u0001"+
		"\u0000\u0000\u0000\u008b\u008d\u0003*\u0015\u0000\u008c\u008e\u0003.\u0017"+
		"\u0000\u008d\u008c\u0001\u0000\u0000\u0000\u008d\u008e\u0001\u0000\u0000"+
		"\u0000\u008e\u0092\u0001\u0000\u0000\u0000\u008f\u0091\u0003 \u0010\u0000"+
		"\u0090\u008f\u0001\u0000\u0000\u0000\u0091\u0094\u0001\u0000\u0000\u0000"+
		"\u0092\u0090\u0001\u0000\u0000\u0000\u0092\u0093\u0001\u0000\u0000\u0000"+
		"\u0093\u0096\u0001\u0000\u0000\u0000\u0094\u0092\u0001\u0000\u0000\u0000"+
		"\u0095\u0089\u0001\u0000\u0000\u0000\u0095\u0096\u0001\u0000\u0000\u0000"+
		"\u0096\u0098\u0001\u0000\u0000\u0000\u0097\u0087\u0001\u0000\u0000\u0000"+
		"\u0097\u0095\u0001\u0000\u0000\u0000\u0098\u009c\u0001\u0000\u0000\u0000"+
		"\u0099\u009b\u0003\u001c\u000e\u0000\u009a\u0099\u0001\u0000\u0000\u0000"+
		"\u009b\u009e\u0001\u0000\u0000\u0000\u009c\u009a\u0001\u0000\u0000\u0000"+
		"\u009c\u009d\u0001\u0000\u0000\u0000\u009d\u001b\u0001\u0000\u0000\u0000"+
		"\u009e\u009c\u0001\u0000\u0000\u0000\u009f\u00a3\u0003$\u0012\u0000\u00a0"+
		"\u00a3\u0003&\u0013\u0000\u00a1\u00a3\u0003\u001e\u000f\u0000\u00a2\u009f"+
		"\u0001\u0000\u0000\u0000\u00a2\u00a0\u0001\u0000\u0000\u0000\u00a2\u00a1"+
		"\u0001\u0000\u0000\u0000\u00a3\u00a4\u0001\u0000\u0000\u0000\u00a4\u00a5"+
		"\u0003\u001a\r\u0000\u00a5\u001d\u0001\u0000\u0000\u0000\u00a6\u00a7\u0005"+
		"\u0005\u0000\u0000\u00a7\u001f\u0001\u0000\u0000\u0000\u00a8\u00ac\u0003"+
		"$\u0012\u0000\u00a9\u00ac\u0003&\u0013\u0000\u00aa\u00ac\u0003(\u0014"+
		"\u0000\u00ab\u00a8\u0001\u0000\u0000\u0000\u00ab\u00a9\u0001\u0000\u0000"+
		"\u0000\u00ab\u00aa\u0001\u0000\u0000\u0000\u00ac\u00b1\u0001\u0000\u0000"+
		"\u0000\u00ad\u00b2\u00032\u0019\u0000\u00ae\u00b2\u0003.\u0017\u0000\u00af"+
		"\u00b2\u0003\u0018\f\u0000\u00b0\u00b2\u00036\u001b\u0000\u00b1\u00ad"+
		"\u0001\u0000\u0000\u0000\u00b1\u00ae\u0001\u0000\u0000\u0000\u00b1\u00af"+
		"\u0001\u0000\u0000\u0000\u00b1\u00b0\u0001\u0000\u0000\u0000\u00b2!\u0001"+
		"\u0000\u0000\u0000\u00b3\u00b4\u0005\u0013\u0000\u0000\u00b4\u00b5\u0005"+
		" \u0000\u0000\u00b5\u00b6\u0005\u0019\u0000\u0000\u00b6\u00b7\u0005\u0015"+
		"\u0000\u0000\u00b7\u00b8\u0003\u001a\r\u0000\u00b8\u00b9\u0005\u0016\u0000"+
		"\u0000\u00b9\u00bc\u0001\u0000\u0000\u0000\u00ba\u00bc\u0005 \u0000\u0000"+
		"\u00bb\u00b3\u0001\u0000\u0000\u0000\u00bb\u00ba\u0001\u0000\u0000\u0000"+
		"\u00bc#\u0001\u0000\u0000\u0000\u00bd\u00be\u0007\u0001\u0000\u0000\u00be"+
		"%\u0001\u0000\u0000\u0000\u00bf\u00c0\u0005\u0011\u0000\u0000\u00c0\'"+
		"\u0001\u0000\u0000\u0000\u00c1\u00c2\u0005\u0012\u0000\u0000\u00c2)\u0001"+
		"\u0000\u0000\u0000\u00c3\u00c4\u0005\u0014\u0000\u0000\u00c4\u00c5\u0003"+
		",\u0016\u0000\u00c5+\u0001\u0000\u0000\u0000\u00c6\u00c7\u0005 \u0000"+
		"\u0000\u00c7-\u0001\u0000\u0000\u0000\u00c8\u00cc\u0005\t\u0000\u0000"+
		"\u00c9\u00cd\u00036\u001b\u0000\u00ca\u00cd\u0003>\u001f\u0000\u00cb\u00cd"+
		"\u00030\u0018\u0000\u00cc\u00c9\u0001\u0000\u0000\u0000\u00cc\u00ca\u0001"+
		"\u0000\u0000\u0000\u00cc\u00cb\u0001\u0000\u0000\u0000\u00cd/\u0001\u0000"+
		"\u0000\u0000\u00ce\u00cf\u0005\u0018\u0000\u0000\u00cf\u00d0\u00032\u0019"+
		"\u0000\u00d01\u0001\u0000\u0000\u0000\u00d1\u00d2\u00034\u001a\u0000\u00d2"+
		"3\u0001\u0000\u0000\u0000\u00d3\u00d4\u0005 \u0000\u0000\u00d45\u0001"+
		"\u0000\u0000\u0000\u00d5\u00d8\u0003@ \u0000\u00d6\u00d9\u00038\u001c"+
		"\u0000\u00d7\u00d9\u0003<\u001e\u0000\u00d8\u00d6\u0001\u0000\u0000\u0000"+
		"\u00d8\u00d7\u0001\u0000\u0000\u0000\u00d97\u0001\u0000\u0000\u0000\u00da"+
		"\u00e1\u0003B!\u0000\u00db\u00dd\u0003D\"\u0000\u00dc\u00de\u0003F#\u0000"+
		"\u00dd\u00dc\u0001\u0000\u0000\u0000\u00dd\u00de\u0001\u0000\u0000\u0000"+
		"\u00de\u00e2\u0001\u0000\u0000\u0000\u00df\u00e2\u0003:\u001d\u0000\u00e0"+
		"\u00e2\u0003@ \u0000\u00e1\u00db\u0001\u0000\u0000\u0000\u00e1\u00df\u0001"+
		"\u0000\u0000\u0000\u00e1\u00e0\u0001\u0000\u0000\u0000\u00e29\u0001\u0000"+
		"\u0000\u0000\u00e3\u00e4\u0005!\u0000\u0000\u00e4;\u0001\u0000\u0000\u0000"+
		"\u00e5\u00e6\u0005\u0017\u0000\u0000\u00e6\u00e7\u0005 \u0000\u0000\u00e7"+
		"=\u0001\u0000\u0000\u0000\u00e8\u00e9\u0005!\u0000\u0000\u00e9?\u0001"+
		"\u0000\u0000\u0000\u00ea\u00ef\u0005 \u0000\u0000\u00eb\u00ec\u0005\u0004"+
		"\u0000\u0000\u00ec\u00ee\u0005 \u0000\u0000\u00ed\u00eb\u0001\u0000\u0000"+
		"\u0000\u00ee\u00f1\u0001\u0000\u0000\u0000\u00ef\u00ed\u0001\u0000\u0000"+
		"\u0000\u00ef\u00f0\u0001\u0000\u0000\u0000\u00f0A\u0001\u0000\u0000\u0000"+
		"\u00f1\u00ef\u0001\u0000\u0000\u0000\u00f2\u00f3\u0007\u0002\u0000\u0000"+
		"\u00f3C\u0001\u0000\u0000\u0000\u00f4\u00f5\u0005\u000e\u0000\u0000\u00f5"+
		"E\u0001\u0000\u0000\u0000\u00f6\u00f7\u0005 \u0000\u0000\u00f7G\u0001"+
		"\u0000\u0000\u0000\u00f8\u00fa\u0007\u0003\u0000\u0000\u00f9\u00fb\u0005"+
		"\u000f\u0000\u0000\u00fa\u00f9\u0001\u0000\u0000\u0000\u00fa\u00fb\u0001"+
		"\u0000\u0000\u0000\u00fbI\u0001\u0000\u0000\u0000\u0014MRX\u0082\u0089"+
		"\u008d\u0092\u0095\u0097\u009c\u00a2\u00ab\u00b1\u00bb\u00cc\u00d8\u00dd"+
		"\u00e1\u00ef\u00fa";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}