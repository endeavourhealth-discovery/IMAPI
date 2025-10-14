// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/IMAPI/parser/src/main/grammars\OWLFS.g4 by ANTLR 4.9.1
package org.endeavourhealth.imapi.parser.owlfs;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class OWLFSParser extends Parser {
  static {
    RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION);
  }

  protected static final DFA[] _decisionToDFA;
  protected static final PredictionContextCache _sharedContextCache =
    new PredictionContextCache();
  public static final int
    T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9,
    T__9 = 10, T__10 = 11, BooleanLiteral = 12, String = 13, BlankNode = 14, WS = 15,
    PN_PREFIX = 16, IRIREF = 17, PNAME_NS = 18, PrefixedName = 19, PNAME_LN = 20, BLANK_NODE_LABEL = 21,
    LANGTAG = 22, INTEGER = 23, DECIMAL = 24, DOUBLE = 25, EXPONENT = 26, STRING_LITERAL_LONG_SINGLE_QUOTE = 27,
    STRING_LITERAL_LONG_QUOTE = 28, STRING_LITERAL_QUOTE = 29, STRING_LITERAL_SINGLE_QUOTE = 30,
    UCHAR = 31, ECHAR = 32, ANON_WS = 33, ANON = 34, PN_CHARS_BASE = 35, PN_CHARS_U = 36,
    PN_CHARS = 37, PN_LOCAL = 38, PLX = 39, PERCENT = 40, HEX = 41, PN_LOCAL_ESC = 42;
  public static final int
    RULE_axiom = 0, RULE_reflexiveObjectProperty = 1, RULE_transitiveObjectProperty = 2,
    RULE_subClassOf = 3, RULE_equivalentClasses = 4, RULE_subObjectPropertyOf = 5,
    RULE_subObjectPropertyExpression = 6, RULE_propertyExpressionChain = 7,
    RULE_superObjectPropertyExpression = 8, RULE_subClass = 9, RULE_superClass = 10,
    RULE_classExpression = 11, RULE_iri = 12, RULE_objectIntersectionOf = 13,
    RULE_objectSomeValuesFrom = 14, RULE_objectPropertyExpression = 15, RULE_objectProperty = 16,
    RULE_inverseObjectProperty = 17;

  private static String[] makeRuleNames() {
    return new String[]{
      "axiom", "reflexiveObjectProperty", "transitiveObjectProperty", "subClassOf",
      "equivalentClasses", "subObjectPropertyOf", "subObjectPropertyExpression",
      "propertyExpressionChain", "superObjectPropertyExpression", "subClass",
      "superClass", "classExpression", "iri", "objectIntersectionOf", "objectSomeValuesFrom",
      "objectPropertyExpression", "objectProperty", "inverseObjectProperty"
    };
  }

  public static final String[] ruleNames = makeRuleNames();

  private static String[] makeLiteralNames() {
    return new String[]{
      null, "'ReflexiveObjectProperty'", "'('", "')'", "'TransitiveObjectProperty'",
      "'SubClassOf'", "'EquivalentClasses'", "'SubObjectPropertyOf'", "'ObjectPropertyChain'",
      "'ObjectIntersectionOf'", "'ObjectSomeValuesFrom'", "'ObjectInverseOf'"
    };
  }

  private static final String[] _LITERAL_NAMES = makeLiteralNames();

  private static String[] makeSymbolicNames() {
    return new String[]{
      null, null, null, null, null, null, null, null, null, null, null, null,
      "BooleanLiteral", "String", "BlankNode", "WS", "PN_PREFIX", "IRIREF",
      "PNAME_NS", "PrefixedName", "PNAME_LN", "BLANK_NODE_LABEL", "LANGTAG",
      "INTEGER", "DECIMAL", "DOUBLE", "EXPONENT", "STRING_LITERAL_LONG_SINGLE_QUOTE",
      "STRING_LITERAL_LONG_QUOTE", "STRING_LITERAL_QUOTE", "STRING_LITERAL_SINGLE_QUOTE",
      "UCHAR", "ECHAR", "ANON_WS", "ANON", "PN_CHARS_BASE", "PN_CHARS_U", "PN_CHARS",
      "PN_LOCAL", "PLX", "PERCENT", "HEX", "PN_LOCAL_ESC"
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
  public String getGrammarFileName() {
    return "OWLFS.g4";
  }

  @Override
  public String[] getRuleNames() {
    return ruleNames;
  }

  @Override
  public String getSerializedATN() {
    return _serializedATN;
  }

  @Override
  public ATN getATN() {
    return _ATN;
  }

  public OWLFSParser(TokenStream input) {
    super(input);
    _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
  }

  public static class AxiomContext extends ParserRuleContext {
    public SubClassOfContext subClassOf() {
      return getRuleContext(SubClassOfContext.class, 0);
    }

    public EquivalentClassesContext equivalentClasses() {
      return getRuleContext(EquivalentClassesContext.class, 0);
    }

    public SubObjectPropertyOfContext subObjectPropertyOf() {
      return getRuleContext(SubObjectPropertyOfContext.class, 0);
    }

    public ReflexiveObjectPropertyContext reflexiveObjectProperty() {
      return getRuleContext(ReflexiveObjectPropertyContext.class, 0);
    }

    public TransitiveObjectPropertyContext transitiveObjectProperty() {
      return getRuleContext(TransitiveObjectPropertyContext.class, 0);
    }

    public AxiomContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_axiom;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).enterAxiom(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).exitAxiom(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof OWLFSVisitor) return ((OWLFSVisitor<? extends T>) visitor).visitAxiom(this);
      else return visitor.visitChildren(this);
    }
  }

  public final AxiomContext axiom() throws RecognitionException {
    AxiomContext _localctx = new AxiomContext(_ctx, getState());
    enterRule(_localctx, 0, RULE_axiom);
    try {
      setState(41);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case T__4:
          enterOuterAlt(_localctx, 1);
        {
          setState(36);
          subClassOf();
        }
        break;
        case T__5:
          enterOuterAlt(_localctx, 2);
        {
          setState(37);
          equivalentClasses();
        }
        break;
        case T__6:
          enterOuterAlt(_localctx, 3);
        {
          setState(38);
          subObjectPropertyOf();
        }
        break;
        case T__0:
          enterOuterAlt(_localctx, 4);
        {
          setState(39);
          reflexiveObjectProperty();
        }
        break;
        case T__3:
          enterOuterAlt(_localctx, 5);
        {
          setState(40);
          transitiveObjectProperty();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ReflexiveObjectPropertyContext extends ParserRuleContext {
    public ObjectPropertyExpressionContext objectPropertyExpression() {
      return getRuleContext(ObjectPropertyExpressionContext.class, 0);
    }

    public ReflexiveObjectPropertyContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_reflexiveObjectProperty;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).enterReflexiveObjectProperty(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).exitReflexiveObjectProperty(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof OWLFSVisitor)
        return ((OWLFSVisitor<? extends T>) visitor).visitReflexiveObjectProperty(this);
      else return visitor.visitChildren(this);
    }
  }

  public final ReflexiveObjectPropertyContext reflexiveObjectProperty() throws RecognitionException {
    ReflexiveObjectPropertyContext _localctx = new ReflexiveObjectPropertyContext(_ctx, getState());
    enterRule(_localctx, 2, RULE_reflexiveObjectProperty);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(43);
        match(T__0);
        setState(44);
        match(T__1);
        setState(45);
        objectPropertyExpression();
        setState(46);
        match(T__2);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class TransitiveObjectPropertyContext extends ParserRuleContext {
    public ObjectPropertyExpressionContext objectPropertyExpression() {
      return getRuleContext(ObjectPropertyExpressionContext.class, 0);
    }

    public TransitiveObjectPropertyContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_transitiveObjectProperty;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).enterTransitiveObjectProperty(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).exitTransitiveObjectProperty(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof OWLFSVisitor)
        return ((OWLFSVisitor<? extends T>) visitor).visitTransitiveObjectProperty(this);
      else return visitor.visitChildren(this);
    }
  }

  public final TransitiveObjectPropertyContext transitiveObjectProperty() throws RecognitionException {
    TransitiveObjectPropertyContext _localctx = new TransitiveObjectPropertyContext(_ctx, getState());
    enterRule(_localctx, 4, RULE_transitiveObjectProperty);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(48);
        match(T__3);
        setState(49);
        match(T__1);
        setState(50);
        objectPropertyExpression();
        setState(51);
        match(T__2);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class SubClassOfContext extends ParserRuleContext {
    public SubClassContext subClass() {
      return getRuleContext(SubClassContext.class, 0);
    }

    public SuperClassContext superClass() {
      return getRuleContext(SuperClassContext.class, 0);
    }

    public SubClassOfContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_subClassOf;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).enterSubClassOf(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).exitSubClassOf(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof OWLFSVisitor) return ((OWLFSVisitor<? extends T>) visitor).visitSubClassOf(this);
      else return visitor.visitChildren(this);
    }
  }

  public final SubClassOfContext subClassOf() throws RecognitionException {
    SubClassOfContext _localctx = new SubClassOfContext(_ctx, getState());
    enterRule(_localctx, 6, RULE_subClassOf);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(53);
        match(T__4);
        setState(54);
        match(T__1);
        setState(55);
        subClass();
        setState(56);
        superClass();
        setState(57);
        match(T__2);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class EquivalentClassesContext extends ParserRuleContext {
    public List<ClassExpressionContext> classExpression() {
      return getRuleContexts(ClassExpressionContext.class);
    }

    public ClassExpressionContext classExpression(int i) {
      return getRuleContext(ClassExpressionContext.class, i);
    }

    public EquivalentClassesContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_equivalentClasses;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).enterEquivalentClasses(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).exitEquivalentClasses(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof OWLFSVisitor) return ((OWLFSVisitor<? extends T>) visitor).visitEquivalentClasses(this);
      else return visitor.visitChildren(this);
    }
  }

  public final EquivalentClassesContext equivalentClasses() throws RecognitionException {
    EquivalentClassesContext _localctx = new EquivalentClassesContext(_ctx, getState());
    enterRule(_localctx, 8, RULE_equivalentClasses);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(59);
        match(T__5);
        setState(60);
        match(T__1);
        setState(61);
        classExpression();
        setState(63);
        _errHandler.sync(this);
        _la = _input.LA(1);
        do {
          {
            {
              setState(62);
              classExpression();
            }
          }
          setState(65);
          _errHandler.sync(this);
          _la = _input.LA(1);
        } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << T__9) | (1L << PrefixedName))) != 0));
        setState(67);
        match(T__2);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class SubObjectPropertyOfContext extends ParserRuleContext {
    public SubObjectPropertyExpressionContext subObjectPropertyExpression() {
      return getRuleContext(SubObjectPropertyExpressionContext.class, 0);
    }

    public SuperObjectPropertyExpressionContext superObjectPropertyExpression() {
      return getRuleContext(SuperObjectPropertyExpressionContext.class, 0);
    }

    public SubObjectPropertyOfContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_subObjectPropertyOf;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).enterSubObjectPropertyOf(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).exitSubObjectPropertyOf(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof OWLFSVisitor) return ((OWLFSVisitor<? extends T>) visitor).visitSubObjectPropertyOf(this);
      else return visitor.visitChildren(this);
    }
  }

  public final SubObjectPropertyOfContext subObjectPropertyOf() throws RecognitionException {
    SubObjectPropertyOfContext _localctx = new SubObjectPropertyOfContext(_ctx, getState());
    enterRule(_localctx, 10, RULE_subObjectPropertyOf);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(69);
        match(T__6);
        setState(70);
        match(T__1);
        setState(71);
        subObjectPropertyExpression();
        setState(72);
        superObjectPropertyExpression();
        setState(73);
        match(T__2);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class SubObjectPropertyExpressionContext extends ParserRuleContext {
    public ObjectPropertyExpressionContext objectPropertyExpression() {
      return getRuleContext(ObjectPropertyExpressionContext.class, 0);
    }

    public PropertyExpressionChainContext propertyExpressionChain() {
      return getRuleContext(PropertyExpressionChainContext.class, 0);
    }

    public SubObjectPropertyExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_subObjectPropertyExpression;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).enterSubObjectPropertyExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).exitSubObjectPropertyExpression(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof OWLFSVisitor)
        return ((OWLFSVisitor<? extends T>) visitor).visitSubObjectPropertyExpression(this);
      else return visitor.visitChildren(this);
    }
  }

  public final SubObjectPropertyExpressionContext subObjectPropertyExpression() throws RecognitionException {
    SubObjectPropertyExpressionContext _localctx = new SubObjectPropertyExpressionContext(_ctx, getState());
    enterRule(_localctx, 12, RULE_subObjectPropertyExpression);
    try {
      setState(77);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case T__10:
        case PrefixedName:
          enterOuterAlt(_localctx, 1);
        {
          setState(75);
          objectPropertyExpression();
        }
        break;
        case T__7:
          enterOuterAlt(_localctx, 2);
        {
          setState(76);
          propertyExpressionChain();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class PropertyExpressionChainContext extends ParserRuleContext {
    public List<ObjectPropertyExpressionContext> objectPropertyExpression() {
      return getRuleContexts(ObjectPropertyExpressionContext.class);
    }

    public ObjectPropertyExpressionContext objectPropertyExpression(int i) {
      return getRuleContext(ObjectPropertyExpressionContext.class, i);
    }

    public PropertyExpressionChainContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_propertyExpressionChain;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).enterPropertyExpressionChain(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).exitPropertyExpressionChain(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof OWLFSVisitor)
        return ((OWLFSVisitor<? extends T>) visitor).visitPropertyExpressionChain(this);
      else return visitor.visitChildren(this);
    }
  }

  public final PropertyExpressionChainContext propertyExpressionChain() throws RecognitionException {
    PropertyExpressionChainContext _localctx = new PropertyExpressionChainContext(_ctx, getState());
    enterRule(_localctx, 14, RULE_propertyExpressionChain);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(79);
        match(T__7);
        setState(80);
        match(T__1);
        setState(81);
        objectPropertyExpression();
        setState(83);
        _errHandler.sync(this);
        _la = _input.LA(1);
        do {
          {
            {
              setState(82);
              objectPropertyExpression();
            }
          }
          setState(85);
          _errHandler.sync(this);
          _la = _input.LA(1);
        } while (_la == T__10 || _la == PrefixedName);
        setState(87);
        match(T__2);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class SuperObjectPropertyExpressionContext extends ParserRuleContext {
    public ObjectPropertyExpressionContext objectPropertyExpression() {
      return getRuleContext(ObjectPropertyExpressionContext.class, 0);
    }

    public SuperObjectPropertyExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_superObjectPropertyExpression;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).enterSuperObjectPropertyExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).exitSuperObjectPropertyExpression(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof OWLFSVisitor)
        return ((OWLFSVisitor<? extends T>) visitor).visitSuperObjectPropertyExpression(this);
      else return visitor.visitChildren(this);
    }
  }

  public final SuperObjectPropertyExpressionContext superObjectPropertyExpression() throws RecognitionException {
    SuperObjectPropertyExpressionContext _localctx = new SuperObjectPropertyExpressionContext(_ctx, getState());
    enterRule(_localctx, 16, RULE_superObjectPropertyExpression);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(89);
        objectPropertyExpression();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class SubClassContext extends ParserRuleContext {
    public ClassExpressionContext classExpression() {
      return getRuleContext(ClassExpressionContext.class, 0);
    }

    public SubClassContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_subClass;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).enterSubClass(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).exitSubClass(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof OWLFSVisitor) return ((OWLFSVisitor<? extends T>) visitor).visitSubClass(this);
      else return visitor.visitChildren(this);
    }
  }

  public final SubClassContext subClass() throws RecognitionException {
    SubClassContext _localctx = new SubClassContext(_ctx, getState());
    enterRule(_localctx, 18, RULE_subClass);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(91);
        classExpression();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class SuperClassContext extends ParserRuleContext {
    public ClassExpressionContext classExpression() {
      return getRuleContext(ClassExpressionContext.class, 0);
    }

    public SuperClassContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_superClass;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).enterSuperClass(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).exitSuperClass(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof OWLFSVisitor) return ((OWLFSVisitor<? extends T>) visitor).visitSuperClass(this);
      else return visitor.visitChildren(this);
    }
  }

  public final SuperClassContext superClass() throws RecognitionException {
    SuperClassContext _localctx = new SuperClassContext(_ctx, getState());
    enterRule(_localctx, 20, RULE_superClass);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(93);
        classExpression();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ClassExpressionContext extends ParserRuleContext {
    public IriContext iri() {
      return getRuleContext(IriContext.class, 0);
    }

    public ObjectIntersectionOfContext objectIntersectionOf() {
      return getRuleContext(ObjectIntersectionOfContext.class, 0);
    }

    public ObjectSomeValuesFromContext objectSomeValuesFrom() {
      return getRuleContext(ObjectSomeValuesFromContext.class, 0);
    }

    public ClassExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classExpression;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).enterClassExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).exitClassExpression(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof OWLFSVisitor) return ((OWLFSVisitor<? extends T>) visitor).visitClassExpression(this);
      else return visitor.visitChildren(this);
    }
  }

  public final ClassExpressionContext classExpression() throws RecognitionException {
    ClassExpressionContext _localctx = new ClassExpressionContext(_ctx, getState());
    enterRule(_localctx, 22, RULE_classExpression);
    try {
      setState(98);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case PrefixedName:
          enterOuterAlt(_localctx, 1);
        {
          setState(95);
          iri();
        }
        break;
        case T__8:
          enterOuterAlt(_localctx, 2);
        {
          setState(96);
          objectIntersectionOf();
        }
        break;
        case T__9:
          enterOuterAlt(_localctx, 3);
        {
          setState(97);
          objectSomeValuesFrom();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class IriContext extends ParserRuleContext {
    public TerminalNode PrefixedName() {
      return getToken(OWLFSParser.PrefixedName, 0);
    }

    public IriContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_iri;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).enterIri(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).exitIri(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof OWLFSVisitor) return ((OWLFSVisitor<? extends T>) visitor).visitIri(this);
      else return visitor.visitChildren(this);
    }
  }

  public final IriContext iri() throws RecognitionException {
    IriContext _localctx = new IriContext(_ctx, getState());
    enterRule(_localctx, 24, RULE_iri);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(100);
        match(PrefixedName);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ObjectIntersectionOfContext extends ParserRuleContext {
    public List<ClassExpressionContext> classExpression() {
      return getRuleContexts(ClassExpressionContext.class);
    }

    public ClassExpressionContext classExpression(int i) {
      return getRuleContext(ClassExpressionContext.class, i);
    }

    public ObjectIntersectionOfContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_objectIntersectionOf;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).enterObjectIntersectionOf(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).exitObjectIntersectionOf(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof OWLFSVisitor) return ((OWLFSVisitor<? extends T>) visitor).visitObjectIntersectionOf(this);
      else return visitor.visitChildren(this);
    }
  }

  public final ObjectIntersectionOfContext objectIntersectionOf() throws RecognitionException {
    ObjectIntersectionOfContext _localctx = new ObjectIntersectionOfContext(_ctx, getState());
    enterRule(_localctx, 26, RULE_objectIntersectionOf);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(102);
        match(T__8);
        setState(103);
        match(T__1);
        setState(104);
        classExpression();
        setState(106);
        _errHandler.sync(this);
        _la = _input.LA(1);
        do {
          {
            {
              setState(105);
              classExpression();
            }
          }
          setState(108);
          _errHandler.sync(this);
          _la = _input.LA(1);
        } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << T__9) | (1L << PrefixedName))) != 0));
        setState(110);
        match(T__2);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ObjectSomeValuesFromContext extends ParserRuleContext {
    public ObjectPropertyExpressionContext objectPropertyExpression() {
      return getRuleContext(ObjectPropertyExpressionContext.class, 0);
    }

    public ClassExpressionContext classExpression() {
      return getRuleContext(ClassExpressionContext.class, 0);
    }

    public ObjectSomeValuesFromContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_objectSomeValuesFrom;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).enterObjectSomeValuesFrom(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).exitObjectSomeValuesFrom(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof OWLFSVisitor) return ((OWLFSVisitor<? extends T>) visitor).visitObjectSomeValuesFrom(this);
      else return visitor.visitChildren(this);
    }
  }

  public final ObjectSomeValuesFromContext objectSomeValuesFrom() throws RecognitionException {
    ObjectSomeValuesFromContext _localctx = new ObjectSomeValuesFromContext(_ctx, getState());
    enterRule(_localctx, 28, RULE_objectSomeValuesFrom);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(112);
        match(T__9);
        setState(113);
        match(T__1);
        setState(114);
        objectPropertyExpression();
        setState(115);
        classExpression();
        setState(116);
        match(T__2);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ObjectPropertyExpressionContext extends ParserRuleContext {
    public ObjectPropertyContext objectProperty() {
      return getRuleContext(ObjectPropertyContext.class, 0);
    }

    public InverseObjectPropertyContext inverseObjectProperty() {
      return getRuleContext(InverseObjectPropertyContext.class, 0);
    }

    public ObjectPropertyExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_objectPropertyExpression;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).enterObjectPropertyExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).exitObjectPropertyExpression(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof OWLFSVisitor)
        return ((OWLFSVisitor<? extends T>) visitor).visitObjectPropertyExpression(this);
      else return visitor.visitChildren(this);
    }
  }

  public final ObjectPropertyExpressionContext objectPropertyExpression() throws RecognitionException {
    ObjectPropertyExpressionContext _localctx = new ObjectPropertyExpressionContext(_ctx, getState());
    enterRule(_localctx, 30, RULE_objectPropertyExpression);
    try {
      setState(120);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case PrefixedName:
          enterOuterAlt(_localctx, 1);
        {
          setState(118);
          objectProperty();
        }
        break;
        case T__10:
          enterOuterAlt(_localctx, 2);
        {
          setState(119);
          inverseObjectProperty();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class ObjectPropertyContext extends ParserRuleContext {
    public IriContext iri() {
      return getRuleContext(IriContext.class, 0);
    }

    public ObjectPropertyContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_objectProperty;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).enterObjectProperty(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).exitObjectProperty(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof OWLFSVisitor) return ((OWLFSVisitor<? extends T>) visitor).visitObjectProperty(this);
      else return visitor.visitChildren(this);
    }
  }

  public final ObjectPropertyContext objectProperty() throws RecognitionException {
    ObjectPropertyContext _localctx = new ObjectPropertyContext(_ctx, getState());
    enterRule(_localctx, 32, RULE_objectProperty);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(122);
        iri();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class InverseObjectPropertyContext extends ParserRuleContext {
    public ObjectPropertyContext objectProperty() {
      return getRuleContext(ObjectPropertyContext.class, 0);
    }

    public InverseObjectPropertyContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_inverseObjectProperty;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).enterInverseObjectProperty(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof OWLFSListener) ((OWLFSListener) listener).exitInverseObjectProperty(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof OWLFSVisitor)
        return ((OWLFSVisitor<? extends T>) visitor).visitInverseObjectProperty(this);
      else return visitor.visitChildren(this);
    }
  }

  public final InverseObjectPropertyContext inverseObjectProperty() throws RecognitionException {
    InverseObjectPropertyContext _localctx = new InverseObjectPropertyContext(_ctx, getState());
    enterRule(_localctx, 34, RULE_inverseObjectProperty);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(124);
        match(T__10);
        setState(125);
        match(T__1);
        setState(126);
        objectProperty();
        setState(127);
        match(T__2);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static final String _serializedATN =
    "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3,\u0084\4\2\t\2\4" +
      "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t" +
      "\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22" +
      "\4\23\t\23\3\2\3\2\3\2\3\2\3\2\5\2,\n\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3" +
      "\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\6\6B\n\6\r\6\16\6C" +
      "\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\5\bP\n\b\3\t\3\t\3\t\3\t\6\t" +
      "V\n\t\r\t\16\tW\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\5\re\n\r" +
      "\3\16\3\16\3\17\3\17\3\17\3\17\6\17m\n\17\r\17\16\17n\3\17\3\17\3\20\3" +
      "\20\3\20\3\20\3\20\3\20\3\21\3\21\5\21{\n\21\3\22\3\22\3\23\3\23\3\23" +
      "\3\23\3\23\3\23\2\2\24\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$\2\2" +
      "\2|\2+\3\2\2\2\4-\3\2\2\2\6\62\3\2\2\2\b\67\3\2\2\2\n=\3\2\2\2\fG\3\2" +
      "\2\2\16O\3\2\2\2\20Q\3\2\2\2\22[\3\2\2\2\24]\3\2\2\2\26_\3\2\2\2\30d\3" +
      "\2\2\2\32f\3\2\2\2\34h\3\2\2\2\36r\3\2\2\2 z\3\2\2\2\"|\3\2\2\2$~\3\2" +
      "\2\2&,\5\b\5\2\',\5\n\6\2(,\5\f\7\2),\5\4\3\2*,\5\6\4\2+&\3\2\2\2+\'\3" +
      "\2\2\2+(\3\2\2\2+)\3\2\2\2+*\3\2\2\2,\3\3\2\2\2-.\7\3\2\2./\7\4\2\2/\60" +
      "\5 \21\2\60\61\7\5\2\2\61\5\3\2\2\2\62\63\7\6\2\2\63\64\7\4\2\2\64\65" +
      "\5 \21\2\65\66\7\5\2\2\66\7\3\2\2\2\678\7\7\2\289\7\4\2\29:\5\24\13\2" +
      ":;\5\26\f\2;<\7\5\2\2<\t\3\2\2\2=>\7\b\2\2>?\7\4\2\2?A\5\30\r\2@B\5\30" +
      "\r\2A@\3\2\2\2BC\3\2\2\2CA\3\2\2\2CD\3\2\2\2DE\3\2\2\2EF\7\5\2\2F\13\3" +
      "\2\2\2GH\7\t\2\2HI\7\4\2\2IJ\5\16\b\2JK\5\22\n\2KL\7\5\2\2L\r\3\2\2\2" +
      "MP\5 \21\2NP\5\20\t\2OM\3\2\2\2ON\3\2\2\2P\17\3\2\2\2QR\7\n\2\2RS\7\4" +
      "\2\2SU\5 \21\2TV\5 \21\2UT\3\2\2\2VW\3\2\2\2WU\3\2\2\2WX\3\2\2\2XY\3\2" +
      "\2\2YZ\7\5\2\2Z\21\3\2\2\2[\\\5 \21\2\\\23\3\2\2\2]^\5\30\r\2^\25\3\2" +
      "\2\2_`\5\30\r\2`\27\3\2\2\2ae\5\32\16\2be\5\34\17\2ce\5\36\20\2da\3\2" +
      "\2\2db\3\2\2\2dc\3\2\2\2e\31\3\2\2\2fg\7\25\2\2g\33\3\2\2\2hi\7\13\2\2" +
      "ij\7\4\2\2jl\5\30\r\2km\5\30\r\2lk\3\2\2\2mn\3\2\2\2nl\3\2\2\2no\3\2\2" +
      "\2op\3\2\2\2pq\7\5\2\2q\35\3\2\2\2rs\7\f\2\2st\7\4\2\2tu\5 \21\2uv\5\30" +
      "\r\2vw\7\5\2\2w\37\3\2\2\2x{\5\"\22\2y{\5$\23\2zx\3\2\2\2zy\3\2\2\2{!" +
      "\3\2\2\2|}\5\32\16\2}#\3\2\2\2~\177\7\r\2\2\177\u0080\7\4\2\2\u0080\u0081" +
      "\5\"\22\2\u0081\u0082\7\5\2\2\u0082%\3\2\2\2\t+COWdnz";
  public static final ATN _ATN =
    new ATNDeserializer().deserialize(_serializedATN.toCharArray());

  static {
    _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
    for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
      _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
    }
  }
}
