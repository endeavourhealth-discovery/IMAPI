package org.endeavourhealth.imapi.parser.imecl;
// Generated from IMECL.g4 by ANTLR 4.13.1

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link IMECLParser}.
 */
public interface IMECLListener extends ParseTreeListener {
  /**
   * Enter a parse tree produced by {@link IMECLParser#imecl}.
   *
   * @param ctx the parse tree
   */
  void enterImecl(IMECLParser.ImeclContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#imecl}.
   *
   * @param ctx the parse tree
   */
  void exitImecl(IMECLParser.ImeclContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#prefixes}.
   *
   * @param ctx the parse tree
   */
  void enterPrefixes(IMECLParser.PrefixesContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#prefixes}.
   *
   * @param ctx the parse tree
   */
  void exitPrefixes(IMECLParser.PrefixesContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#prefixDecl}.
   *
   * @param ctx the parse tree
   */
  void enterPrefixDecl(IMECLParser.PrefixDeclContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#prefixDecl}.
   *
   * @param ctx the parse tree
   */
  void exitPrefixDecl(IMECLParser.PrefixDeclContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#pname}.
   *
   * @param ctx the parse tree
   */
  void enterPname(IMECLParser.PnameContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#pname}.
   *
   * @param ctx the parse tree
   */
  void exitPname(IMECLParser.PnameContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#iri}.
   *
   * @param ctx the parse tree
   */
  void enterIri(IMECLParser.IriContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#iri}.
   *
   * @param ctx the parse tree
   */
  void exitIri(IMECLParser.IriContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#conceptid}.
   *
   * @param ctx the parse tree
   */
  void enterConceptid(IMECLParser.ConceptidContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#conceptid}.
   *
   * @param ctx the parse tree
   */
  void exitConceptid(IMECLParser.ConceptidContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#eclrefinement}.
   *
   * @param ctx the parse tree
   */
  void enterEclrefinement(IMECLParser.EclrefinementContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#eclrefinement}.
   *
   * @param ctx the parse tree
   */
  void exitEclrefinement(IMECLParser.EclrefinementContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#conjunctionrefinementset}.
   *
   * @param ctx the parse tree
   */
  void enterConjunctionrefinementset(IMECLParser.ConjunctionrefinementsetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#conjunctionrefinementset}.
   *
   * @param ctx the parse tree
   */
  void exitConjunctionrefinementset(IMECLParser.ConjunctionrefinementsetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#disjunctionrefinementset}.
   *
   * @param ctx the parse tree
   */
  void enterDisjunctionrefinementset(IMECLParser.DisjunctionrefinementsetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#disjunctionrefinementset}.
   *
   * @param ctx the parse tree
   */
  void exitDisjunctionrefinementset(IMECLParser.DisjunctionrefinementsetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#eclattributeset}.
   *
   * @param ctx the parse tree
   */
  void enterEclattributeset(IMECLParser.EclattributesetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#eclattributeset}.
   *
   * @param ctx the parse tree
   */
  void exitEclattributeset(IMECLParser.EclattributesetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#conjunctionattributeset}.
   *
   * @param ctx the parse tree
   */
  void enterConjunctionattributeset(IMECLParser.ConjunctionattributesetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#conjunctionattributeset}.
   *
   * @param ctx the parse tree
   */
  void exitConjunctionattributeset(IMECLParser.ConjunctionattributesetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#disjunctionattributeset}.
   *
   * @param ctx the parse tree
   */
  void enterDisjunctionattributeset(IMECLParser.DisjunctionattributesetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#disjunctionattributeset}.
   *
   * @param ctx the parse tree
   */
  void exitDisjunctionattributeset(IMECLParser.DisjunctionattributesetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#expressionconstraint}.
   *
   * @param ctx the parse tree
   */
  void enterExpressionconstraint(IMECLParser.ExpressionconstraintContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#expressionconstraint}.
   *
   * @param ctx the parse tree
   */
  void exitExpressionconstraint(IMECLParser.ExpressionconstraintContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#refinedexpressionconstraint}.
   *
   * @param ctx the parse tree
   */
  void enterRefinedexpressionconstraint(IMECLParser.RefinedexpressionconstraintContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#refinedexpressionconstraint}.
   *
   * @param ctx the parse tree
   */
  void exitRefinedexpressionconstraint(IMECLParser.RefinedexpressionconstraintContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#compoundexpressionconstraint}.
   *
   * @param ctx the parse tree
   */
  void enterCompoundexpressionconstraint(IMECLParser.CompoundexpressionconstraintContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#compoundexpressionconstraint}.
   *
   * @param ctx the parse tree
   */
  void exitCompoundexpressionconstraint(IMECLParser.CompoundexpressionconstraintContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#conjunctionexpressionconstraint}.
   *
   * @param ctx the parse tree
   */
  void enterConjunctionexpressionconstraint(IMECLParser.ConjunctionexpressionconstraintContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#conjunctionexpressionconstraint}.
   *
   * @param ctx the parse tree
   */
  void exitConjunctionexpressionconstraint(IMECLParser.ConjunctionexpressionconstraintContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#disjunctionexpressionconstraint}.
   *
   * @param ctx the parse tree
   */
  void enterDisjunctionexpressionconstraint(IMECLParser.DisjunctionexpressionconstraintContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#disjunctionexpressionconstraint}.
   *
   * @param ctx the parse tree
   */
  void exitDisjunctionexpressionconstraint(IMECLParser.DisjunctionexpressionconstraintContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#exclusionexpressionconstraint}.
   *
   * @param ctx the parse tree
   */
  void enterExclusionexpressionconstraint(IMECLParser.ExclusionexpressionconstraintContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#exclusionexpressionconstraint}.
   *
   * @param ctx the parse tree
   */
  void exitExclusionexpressionconstraint(IMECLParser.ExclusionexpressionconstraintContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#dottedexpressionconstraint}.
   *
   * @param ctx the parse tree
   */
  void enterDottedexpressionconstraint(IMECLParser.DottedexpressionconstraintContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#dottedexpressionconstraint}.
   *
   * @param ctx the parse tree
   */
  void exitDottedexpressionconstraint(IMECLParser.DottedexpressionconstraintContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#dottedexpressionattribute}.
   *
   * @param ctx the parse tree
   */
  void enterDottedexpressionattribute(IMECLParser.DottedexpressionattributeContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#dottedexpressionattribute}.
   *
   * @param ctx the parse tree
   */
  void exitDottedexpressionattribute(IMECLParser.DottedexpressionattributeContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#subexpressionconstraint}.
   *
   * @param ctx the parse tree
   */
  void enterSubexpressionconstraint(IMECLParser.SubexpressionconstraintContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#subexpressionconstraint}.
   *
   * @param ctx the parse tree
   */
  void exitSubexpressionconstraint(IMECLParser.SubexpressionconstraintContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#eclfocusconcept}.
   *
   * @param ctx the parse tree
   */
  void enterEclfocusconcept(IMECLParser.EclfocusconceptContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#eclfocusconcept}.
   *
   * @param ctx the parse tree
   */
  void exitEclfocusconcept(IMECLParser.EclfocusconceptContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#dot}.
   *
   * @param ctx the parse tree
   */
  void enterDot(IMECLParser.DotContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#dot}.
   *
   * @param ctx the parse tree
   */
  void exitDot(IMECLParser.DotContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#memberof}.
   *
   * @param ctx the parse tree
   */
  void enterMemberof(IMECLParser.MemberofContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#memberof}.
   *
   * @param ctx the parse tree
   */
  void exitMemberof(IMECLParser.MemberofContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#refsetfieldnameset}.
   *
   * @param ctx the parse tree
   */
  void enterRefsetfieldnameset(IMECLParser.RefsetfieldnamesetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#refsetfieldnameset}.
   *
   * @param ctx the parse tree
   */
  void exitRefsetfieldnameset(IMECLParser.RefsetfieldnamesetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#refsetfieldname}.
   *
   * @param ctx the parse tree
   */
  void enterRefsetfieldname(IMECLParser.RefsetfieldnameContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#refsetfieldname}.
   *
   * @param ctx the parse tree
   */
  void exitRefsetfieldname(IMECLParser.RefsetfieldnameContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#eclconceptreference}.
   *
   * @param ctx the parse tree
   */
  void enterEclconceptreference(IMECLParser.EclconceptreferenceContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#eclconceptreference}.
   *
   * @param ctx the parse tree
   */
  void exitEclconceptreference(IMECLParser.EclconceptreferenceContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#eclconceptreferenceset}.
   *
   * @param ctx the parse tree
   */
  void enterEclconceptreferenceset(IMECLParser.EclconceptreferencesetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#eclconceptreferenceset}.
   *
   * @param ctx the parse tree
   */
  void exitEclconceptreferenceset(IMECLParser.EclconceptreferencesetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#term}.
   *
   * @param ctx the parse tree
   */
  void enterTerm(IMECLParser.TermContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#term}.
   *
   * @param ctx the parse tree
   */
  void exitTerm(IMECLParser.TermContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#altidentifier}.
   *
   * @param ctx the parse tree
   */
  void enterAltidentifier(IMECLParser.AltidentifierContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#altidentifier}.
   *
   * @param ctx the parse tree
   */
  void exitAltidentifier(IMECLParser.AltidentifierContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#altidentifierschemealias}.
   *
   * @param ctx the parse tree
   */
  void enterAltidentifierschemealias(IMECLParser.AltidentifierschemealiasContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#altidentifierschemealias}.
   *
   * @param ctx the parse tree
   */
  void exitAltidentifierschemealias(IMECLParser.AltidentifierschemealiasContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#altidentifiercodewithinquotes}.
   *
   * @param ctx the parse tree
   */
  void enterAltidentifiercodewithinquotes(IMECLParser.AltidentifiercodewithinquotesContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#altidentifiercodewithinquotes}.
   *
   * @param ctx the parse tree
   */
  void exitAltidentifiercodewithinquotes(IMECLParser.AltidentifiercodewithinquotesContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#altidentifiercodewithoutquotes}.
   *
   * @param ctx the parse tree
   */
  void enterAltidentifiercodewithoutquotes(IMECLParser.AltidentifiercodewithoutquotesContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#altidentifiercodewithoutquotes}.
   *
   * @param ctx the parse tree
   */
  void exitAltidentifiercodewithoutquotes(IMECLParser.AltidentifiercodewithoutquotesContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#wildcard}.
   *
   * @param ctx the parse tree
   */
  void enterWildcard(IMECLParser.WildcardContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#wildcard}.
   *
   * @param ctx the parse tree
   */
  void exitWildcard(IMECLParser.WildcardContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#constraintoperator}.
   *
   * @param ctx the parse tree
   */
  void enterConstraintoperator(IMECLParser.ConstraintoperatorContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#constraintoperator}.
   *
   * @param ctx the parse tree
   */
  void exitConstraintoperator(IMECLParser.ConstraintoperatorContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#descendantof}.
   *
   * @param ctx the parse tree
   */
  void enterDescendantof(IMECLParser.DescendantofContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#descendantof}.
   *
   * @param ctx the parse tree
   */
  void exitDescendantof(IMECLParser.DescendantofContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#descendantorselfof}.
   *
   * @param ctx the parse tree
   */
  void enterDescendantorselfof(IMECLParser.DescendantorselfofContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#descendantorselfof}.
   *
   * @param ctx the parse tree
   */
  void exitDescendantorselfof(IMECLParser.DescendantorselfofContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#childof}.
   *
   * @param ctx the parse tree
   */
  void enterChildof(IMECLParser.ChildofContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#childof}.
   *
   * @param ctx the parse tree
   */
  void exitChildof(IMECLParser.ChildofContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#childorselfof}.
   *
   * @param ctx the parse tree
   */
  void enterChildorselfof(IMECLParser.ChildorselfofContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#childorselfof}.
   *
   * @param ctx the parse tree
   */
  void exitChildorselfof(IMECLParser.ChildorselfofContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#ancestorof}.
   *
   * @param ctx the parse tree
   */
  void enterAncestorof(IMECLParser.AncestorofContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#ancestorof}.
   *
   * @param ctx the parse tree
   */
  void exitAncestorof(IMECLParser.AncestorofContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#ancestororselfof}.
   *
   * @param ctx the parse tree
   */
  void enterAncestororselfof(IMECLParser.AncestororselfofContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#ancestororselfof}.
   *
   * @param ctx the parse tree
   */
  void exitAncestororselfof(IMECLParser.AncestororselfofContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#parentof}.
   *
   * @param ctx the parse tree
   */
  void enterParentof(IMECLParser.ParentofContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#parentof}.
   *
   * @param ctx the parse tree
   */
  void exitParentof(IMECLParser.ParentofContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#parentorselfof}.
   *
   * @param ctx the parse tree
   */
  void enterParentorselfof(IMECLParser.ParentorselfofContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#parentorselfof}.
   *
   * @param ctx the parse tree
   */
  void exitParentorselfof(IMECLParser.ParentorselfofContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#top}.
   *
   * @param ctx the parse tree
   */
  void enterTop(IMECLParser.TopContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#top}.
   *
   * @param ctx the parse tree
   */
  void exitTop(IMECLParser.TopContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#bottom}.
   *
   * @param ctx the parse tree
   */
  void enterBottom(IMECLParser.BottomContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#bottom}.
   *
   * @param ctx the parse tree
   */
  void exitBottom(IMECLParser.BottomContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#conjunction}.
   *
   * @param ctx the parse tree
   */
  void enterConjunction(IMECLParser.ConjunctionContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#conjunction}.
   *
   * @param ctx the parse tree
   */
  void exitConjunction(IMECLParser.ConjunctionContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#disjunction}.
   *
   * @param ctx the parse tree
   */
  void enterDisjunction(IMECLParser.DisjunctionContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#disjunction}.
   *
   * @param ctx the parse tree
   */
  void exitDisjunction(IMECLParser.DisjunctionContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#exclusion}.
   *
   * @param ctx the parse tree
   */
  void enterExclusion(IMECLParser.ExclusionContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#exclusion}.
   *
   * @param ctx the parse tree
   */
  void exitExclusion(IMECLParser.ExclusionContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#subrefinement}.
   *
   * @param ctx the parse tree
   */
  void enterSubrefinement(IMECLParser.SubrefinementContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#subrefinement}.
   *
   * @param ctx the parse tree
   */
  void exitSubrefinement(IMECLParser.SubrefinementContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#subattributeset}.
   *
   * @param ctx the parse tree
   */
  void enterSubattributeset(IMECLParser.SubattributesetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#subattributeset}.
   *
   * @param ctx the parse tree
   */
  void exitSubattributeset(IMECLParser.SubattributesetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#eclattributegroup}.
   *
   * @param ctx the parse tree
   */
  void enterEclattributegroup(IMECLParser.EclattributegroupContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#eclattributegroup}.
   *
   * @param ctx the parse tree
   */
  void exitEclattributegroup(IMECLParser.EclattributegroupContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#eclattribute}.
   *
   * @param ctx the parse tree
   */
  void enterEclattribute(IMECLParser.EclattributeContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#eclattribute}.
   *
   * @param ctx the parse tree
   */
  void exitEclattribute(IMECLParser.EclattributeContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#cardinality}.
   *
   * @param ctx the parse tree
   */
  void enterCardinality(IMECLParser.CardinalityContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#cardinality}.
   *
   * @param ctx the parse tree
   */
  void exitCardinality(IMECLParser.CardinalityContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#minvalue}.
   *
   * @param ctx the parse tree
   */
  void enterMinvalue(IMECLParser.MinvalueContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#minvalue}.
   *
   * @param ctx the parse tree
   */
  void exitMinvalue(IMECLParser.MinvalueContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#to}.
   *
   * @param ctx the parse tree
   */
  void enterTo(IMECLParser.ToContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#to}.
   *
   * @param ctx the parse tree
   */
  void exitTo(IMECLParser.ToContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#maxvalue}.
   *
   * @param ctx the parse tree
   */
  void enterMaxvalue(IMECLParser.MaxvalueContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#maxvalue}.
   *
   * @param ctx the parse tree
   */
  void exitMaxvalue(IMECLParser.MaxvalueContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#many}.
   *
   * @param ctx the parse tree
   */
  void enterMany(IMECLParser.ManyContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#many}.
   *
   * @param ctx the parse tree
   */
  void exitMany(IMECLParser.ManyContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#reverseflag}.
   *
   * @param ctx the parse tree
   */
  void enterReverseflag(IMECLParser.ReverseflagContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#reverseflag}.
   *
   * @param ctx the parse tree
   */
  void exitReverseflag(IMECLParser.ReverseflagContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#eclattributename}.
   *
   * @param ctx the parse tree
   */
  void enterEclattributename(IMECLParser.EclattributenameContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#eclattributename}.
   *
   * @param ctx the parse tree
   */
  void exitEclattributename(IMECLParser.EclattributenameContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#expressioncomparisonoperator}.
   *
   * @param ctx the parse tree
   */
  void enterExpressioncomparisonoperator(IMECLParser.ExpressioncomparisonoperatorContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#expressioncomparisonoperator}.
   *
   * @param ctx the parse tree
   */
  void exitExpressioncomparisonoperator(IMECLParser.ExpressioncomparisonoperatorContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#numericcomparisonoperator}.
   *
   * @param ctx the parse tree
   */
  void enterNumericcomparisonoperator(IMECLParser.NumericcomparisonoperatorContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#numericcomparisonoperator}.
   *
   * @param ctx the parse tree
   */
  void exitNumericcomparisonoperator(IMECLParser.NumericcomparisonoperatorContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#timecomparisonoperator}.
   *
   * @param ctx the parse tree
   */
  void enterTimecomparisonoperator(IMECLParser.TimecomparisonoperatorContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#timecomparisonoperator}.
   *
   * @param ctx the parse tree
   */
  void exitTimecomparisonoperator(IMECLParser.TimecomparisonoperatorContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#stringcomparisonoperator}.
   *
   * @param ctx the parse tree
   */
  void enterStringcomparisonoperator(IMECLParser.StringcomparisonoperatorContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#stringcomparisonoperator}.
   *
   * @param ctx the parse tree
   */
  void exitStringcomparisonoperator(IMECLParser.StringcomparisonoperatorContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#booleancomparisonoperator}.
   *
   * @param ctx the parse tree
   */
  void enterBooleancomparisonoperator(IMECLParser.BooleancomparisonoperatorContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#booleancomparisonoperator}.
   *
   * @param ctx the parse tree
   */
  void exitBooleancomparisonoperator(IMECLParser.BooleancomparisonoperatorContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#idcomparisonoperator}.
   *
   * @param ctx the parse tree
   */
  void enterIdcomparisonoperator(IMECLParser.IdcomparisonoperatorContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#idcomparisonoperator}.
   *
   * @param ctx the parse tree
   */
  void exitIdcomparisonoperator(IMECLParser.IdcomparisonoperatorContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#descriptionfilterconstraint}.
   *
   * @param ctx the parse tree
   */
  void enterDescriptionfilterconstraint(IMECLParser.DescriptionfilterconstraintContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#descriptionfilterconstraint}.
   *
   * @param ctx the parse tree
   */
  void exitDescriptionfilterconstraint(IMECLParser.DescriptionfilterconstraintContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#descriptionfilter}.
   *
   * @param ctx the parse tree
   */
  void enterDescriptionfilter(IMECLParser.DescriptionfilterContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#descriptionfilter}.
   *
   * @param ctx the parse tree
   */
  void exitDescriptionfilter(IMECLParser.DescriptionfilterContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#descriptionidfilter}.
   *
   * @param ctx the parse tree
   */
  void enterDescriptionidfilter(IMECLParser.DescriptionidfilterContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#descriptionidfilter}.
   *
   * @param ctx the parse tree
   */
  void exitDescriptionidfilter(IMECLParser.DescriptionidfilterContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#descriptionidkeyword}.
   *
   * @param ctx the parse tree
   */
  void enterDescriptionidkeyword(IMECLParser.DescriptionidkeywordContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#descriptionidkeyword}.
   *
   * @param ctx the parse tree
   */
  void exitDescriptionidkeyword(IMECLParser.DescriptionidkeywordContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#descriptionid}.
   *
   * @param ctx the parse tree
   */
  void enterDescriptionid(IMECLParser.DescriptionidContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#descriptionid}.
   *
   * @param ctx the parse tree
   */
  void exitDescriptionid(IMECLParser.DescriptionidContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#descriptionidset}.
   *
   * @param ctx the parse tree
   */
  void enterDescriptionidset(IMECLParser.DescriptionidsetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#descriptionidset}.
   *
   * @param ctx the parse tree
   */
  void exitDescriptionidset(IMECLParser.DescriptionidsetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#termfilter}.
   *
   * @param ctx the parse tree
   */
  void enterTermfilter(IMECLParser.TermfilterContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#termfilter}.
   *
   * @param ctx the parse tree
   */
  void exitTermfilter(IMECLParser.TermfilterContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#termkeyword}.
   *
   * @param ctx the parse tree
   */
  void enterTermkeyword(IMECLParser.TermkeywordContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#termkeyword}.
   *
   * @param ctx the parse tree
   */
  void exitTermkeyword(IMECLParser.TermkeywordContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#typedsearchterm}.
   *
   * @param ctx the parse tree
   */
  void enterTypedsearchterm(IMECLParser.TypedsearchtermContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#typedsearchterm}.
   *
   * @param ctx the parse tree
   */
  void exitTypedsearchterm(IMECLParser.TypedsearchtermContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#typedsearchtermset}.
   *
   * @param ctx the parse tree
   */
  void enterTypedsearchtermset(IMECLParser.TypedsearchtermsetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#typedsearchtermset}.
   *
   * @param ctx the parse tree
   */
  void exitTypedsearchtermset(IMECLParser.TypedsearchtermsetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#wild}.
   *
   * @param ctx the parse tree
   */
  void enterWild(IMECLParser.WildContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#wild}.
   *
   * @param ctx the parse tree
   */
  void exitWild(IMECLParser.WildContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#matchkeyword}.
   *
   * @param ctx the parse tree
   */
  void enterMatchkeyword(IMECLParser.MatchkeywordContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#matchkeyword}.
   *
   * @param ctx the parse tree
   */
  void exitMatchkeyword(IMECLParser.MatchkeywordContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#matchsearchterm}.
   *
   * @param ctx the parse tree
   */
  void enterMatchsearchterm(IMECLParser.MatchsearchtermContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#matchsearchterm}.
   *
   * @param ctx the parse tree
   */
  void exitMatchsearchterm(IMECLParser.MatchsearchtermContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#matchsearchtermset}.
   *
   * @param ctx the parse tree
   */
  void enterMatchsearchtermset(IMECLParser.MatchsearchtermsetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#matchsearchtermset}.
   *
   * @param ctx the parse tree
   */
  void exitMatchsearchtermset(IMECLParser.MatchsearchtermsetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#wildsearchterm}.
   *
   * @param ctx the parse tree
   */
  void enterWildsearchterm(IMECLParser.WildsearchtermContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#wildsearchterm}.
   *
   * @param ctx the parse tree
   */
  void exitWildsearchterm(IMECLParser.WildsearchtermContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#wildsearchtermset}.
   *
   * @param ctx the parse tree
   */
  void enterWildsearchtermset(IMECLParser.WildsearchtermsetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#wildsearchtermset}.
   *
   * @param ctx the parse tree
   */
  void exitWildsearchtermset(IMECLParser.WildsearchtermsetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#languagefilter}.
   *
   * @param ctx the parse tree
   */
  void enterLanguagefilter(IMECLParser.LanguagefilterContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#languagefilter}.
   *
   * @param ctx the parse tree
   */
  void exitLanguagefilter(IMECLParser.LanguagefilterContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#language}.
   *
   * @param ctx the parse tree
   */
  void enterLanguage(IMECLParser.LanguageContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#language}.
   *
   * @param ctx the parse tree
   */
  void exitLanguage(IMECLParser.LanguageContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#languagecode}.
   *
   * @param ctx the parse tree
   */
  void enterLanguagecode(IMECLParser.LanguagecodeContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#languagecode}.
   *
   * @param ctx the parse tree
   */
  void exitLanguagecode(IMECLParser.LanguagecodeContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#languagecodeset}.
   *
   * @param ctx the parse tree
   */
  void enterLanguagecodeset(IMECLParser.LanguagecodesetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#languagecodeset}.
   *
   * @param ctx the parse tree
   */
  void exitLanguagecodeset(IMECLParser.LanguagecodesetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#typefilter}.
   *
   * @param ctx the parse tree
   */
  void enterTypefilter(IMECLParser.TypefilterContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#typefilter}.
   *
   * @param ctx the parse tree
   */
  void exitTypefilter(IMECLParser.TypefilterContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#typeidfilter}.
   *
   * @param ctx the parse tree
   */
  void enterTypeidfilter(IMECLParser.TypeidfilterContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#typeidfilter}.
   *
   * @param ctx the parse tree
   */
  void exitTypeidfilter(IMECLParser.TypeidfilterContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#typeid}.
   *
   * @param ctx the parse tree
   */
  void enterTypeid(IMECLParser.TypeidContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#typeid}.
   *
   * @param ctx the parse tree
   */
  void exitTypeid(IMECLParser.TypeidContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#typetokenfilter}.
   *
   * @param ctx the parse tree
   */
  void enterTypetokenfilter(IMECLParser.TypetokenfilterContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#typetokenfilter}.
   *
   * @param ctx the parse tree
   */
  void exitTypetokenfilter(IMECLParser.TypetokenfilterContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#type}.
   *
   * @param ctx the parse tree
   */
  void enterType(IMECLParser.TypeContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#type}.
   *
   * @param ctx the parse tree
   */
  void exitType(IMECLParser.TypeContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#typetoken}.
   *
   * @param ctx the parse tree
   */
  void enterTypetoken(IMECLParser.TypetokenContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#typetoken}.
   *
   * @param ctx the parse tree
   */
  void exitTypetoken(IMECLParser.TypetokenContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#typetokenset}.
   *
   * @param ctx the parse tree
   */
  void enterTypetokenset(IMECLParser.TypetokensetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#typetokenset}.
   *
   * @param ctx the parse tree
   */
  void exitTypetokenset(IMECLParser.TypetokensetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#synonym}.
   *
   * @param ctx the parse tree
   */
  void enterSynonym(IMECLParser.SynonymContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#synonym}.
   *
   * @param ctx the parse tree
   */
  void exitSynonym(IMECLParser.SynonymContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#fullyspecifiedname}.
   *
   * @param ctx the parse tree
   */
  void enterFullyspecifiedname(IMECLParser.FullyspecifiednameContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#fullyspecifiedname}.
   *
   * @param ctx the parse tree
   */
  void exitFullyspecifiedname(IMECLParser.FullyspecifiednameContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#definition}.
   *
   * @param ctx the parse tree
   */
  void enterDefinition(IMECLParser.DefinitionContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#definition}.
   *
   * @param ctx the parse tree
   */
  void exitDefinition(IMECLParser.DefinitionContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#dialectfilter}.
   *
   * @param ctx the parse tree
   */
  void enterDialectfilter(IMECLParser.DialectfilterContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#dialectfilter}.
   *
   * @param ctx the parse tree
   */
  void exitDialectfilter(IMECLParser.DialectfilterContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#dialectidfilter}.
   *
   * @param ctx the parse tree
   */
  void enterDialectidfilter(IMECLParser.DialectidfilterContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#dialectidfilter}.
   *
   * @param ctx the parse tree
   */
  void exitDialectidfilter(IMECLParser.DialectidfilterContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#dialectid}.
   *
   * @param ctx the parse tree
   */
  void enterDialectid(IMECLParser.DialectidContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#dialectid}.
   *
   * @param ctx the parse tree
   */
  void exitDialectid(IMECLParser.DialectidContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#dialectaliasfilter}.
   *
   * @param ctx the parse tree
   */
  void enterDialectaliasfilter(IMECLParser.DialectaliasfilterContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#dialectaliasfilter}.
   *
   * @param ctx the parse tree
   */
  void exitDialectaliasfilter(IMECLParser.DialectaliasfilterContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#dialect}.
   *
   * @param ctx the parse tree
   */
  void enterDialect(IMECLParser.DialectContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#dialect}.
   *
   * @param ctx the parse tree
   */
  void exitDialect(IMECLParser.DialectContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#dialectalias}.
   *
   * @param ctx the parse tree
   */
  void enterDialectalias(IMECLParser.DialectaliasContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#dialectalias}.
   *
   * @param ctx the parse tree
   */
  void exitDialectalias(IMECLParser.DialectaliasContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#dialectaliasset}.
   *
   * @param ctx the parse tree
   */
  void enterDialectaliasset(IMECLParser.DialectaliassetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#dialectaliasset}.
   *
   * @param ctx the parse tree
   */
  void exitDialectaliasset(IMECLParser.DialectaliassetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#dialectidset}.
   *
   * @param ctx the parse tree
   */
  void enterDialectidset(IMECLParser.DialectidsetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#dialectidset}.
   *
   * @param ctx the parse tree
   */
  void exitDialectidset(IMECLParser.DialectidsetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#acceptabilityset}.
   *
   * @param ctx the parse tree
   */
  void enterAcceptabilityset(IMECLParser.AcceptabilitysetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#acceptabilityset}.
   *
   * @param ctx the parse tree
   */
  void exitAcceptabilityset(IMECLParser.AcceptabilitysetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#acceptabilityconceptreferenceset}.
   *
   * @param ctx the parse tree
   */
  void enterAcceptabilityconceptreferenceset(IMECLParser.AcceptabilityconceptreferencesetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#acceptabilityconceptreferenceset}.
   *
   * @param ctx the parse tree
   */
  void exitAcceptabilityconceptreferenceset(IMECLParser.AcceptabilityconceptreferencesetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#acceptabilitytokenset}.
   *
   * @param ctx the parse tree
   */
  void enterAcceptabilitytokenset(IMECLParser.AcceptabilitytokensetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#acceptabilitytokenset}.
   *
   * @param ctx the parse tree
   */
  void exitAcceptabilitytokenset(IMECLParser.AcceptabilitytokensetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#acceptabilitytoken}.
   *
   * @param ctx the parse tree
   */
  void enterAcceptabilitytoken(IMECLParser.AcceptabilitytokenContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#acceptabilitytoken}.
   *
   * @param ctx the parse tree
   */
  void exitAcceptabilitytoken(IMECLParser.AcceptabilitytokenContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#acceptable}.
   *
   * @param ctx the parse tree
   */
  void enterAcceptable(IMECLParser.AcceptableContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#acceptable}.
   *
   * @param ctx the parse tree
   */
  void exitAcceptable(IMECLParser.AcceptableContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#preferred}.
   *
   * @param ctx the parse tree
   */
  void enterPreferred(IMECLParser.PreferredContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#preferred}.
   *
   * @param ctx the parse tree
   */
  void exitPreferred(IMECLParser.PreferredContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#conceptfilterconstraint}.
   *
   * @param ctx the parse tree
   */
  void enterConceptfilterconstraint(IMECLParser.ConceptfilterconstraintContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#conceptfilterconstraint}.
   *
   * @param ctx the parse tree
   */
  void exitConceptfilterconstraint(IMECLParser.ConceptfilterconstraintContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#conceptfilter}.
   *
   * @param ctx the parse tree
   */
  void enterConceptfilter(IMECLParser.ConceptfilterContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#conceptfilter}.
   *
   * @param ctx the parse tree
   */
  void exitConceptfilter(IMECLParser.ConceptfilterContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#definitionstatusfilter}.
   *
   * @param ctx the parse tree
   */
  void enterDefinitionstatusfilter(IMECLParser.DefinitionstatusfilterContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#definitionstatusfilter}.
   *
   * @param ctx the parse tree
   */
  void exitDefinitionstatusfilter(IMECLParser.DefinitionstatusfilterContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#definitionstatusidfilter}.
   *
   * @param ctx the parse tree
   */
  void enterDefinitionstatusidfilter(IMECLParser.DefinitionstatusidfilterContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#definitionstatusidfilter}.
   *
   * @param ctx the parse tree
   */
  void exitDefinitionstatusidfilter(IMECLParser.DefinitionstatusidfilterContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#definitionstatusidkeyword}.
   *
   * @param ctx the parse tree
   */
  void enterDefinitionstatusidkeyword(IMECLParser.DefinitionstatusidkeywordContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#definitionstatusidkeyword}.
   *
   * @param ctx the parse tree
   */
  void exitDefinitionstatusidkeyword(IMECLParser.DefinitionstatusidkeywordContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#definitionstatustokenfilter}.
   *
   * @param ctx the parse tree
   */
  void enterDefinitionstatustokenfilter(IMECLParser.DefinitionstatustokenfilterContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#definitionstatustokenfilter}.
   *
   * @param ctx the parse tree
   */
  void exitDefinitionstatustokenfilter(IMECLParser.DefinitionstatustokenfilterContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#definitionstatuskeyword}.
   *
   * @param ctx the parse tree
   */
  void enterDefinitionstatuskeyword(IMECLParser.DefinitionstatuskeywordContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#definitionstatuskeyword}.
   *
   * @param ctx the parse tree
   */
  void exitDefinitionstatuskeyword(IMECLParser.DefinitionstatuskeywordContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#definitionstatustoken}.
   *
   * @param ctx the parse tree
   */
  void enterDefinitionstatustoken(IMECLParser.DefinitionstatustokenContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#definitionstatustoken}.
   *
   * @param ctx the parse tree
   */
  void exitDefinitionstatustoken(IMECLParser.DefinitionstatustokenContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#definitionstatustokenset}.
   *
   * @param ctx the parse tree
   */
  void enterDefinitionstatustokenset(IMECLParser.DefinitionstatustokensetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#definitionstatustokenset}.
   *
   * @param ctx the parse tree
   */
  void exitDefinitionstatustokenset(IMECLParser.DefinitionstatustokensetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#primitivetoken}.
   *
   * @param ctx the parse tree
   */
  void enterPrimitivetoken(IMECLParser.PrimitivetokenContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#primitivetoken}.
   *
   * @param ctx the parse tree
   */
  void exitPrimitivetoken(IMECLParser.PrimitivetokenContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#definedtoken}.
   *
   * @param ctx the parse tree
   */
  void enterDefinedtoken(IMECLParser.DefinedtokenContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#definedtoken}.
   *
   * @param ctx the parse tree
   */
  void exitDefinedtoken(IMECLParser.DefinedtokenContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#modulefilter}.
   *
   * @param ctx the parse tree
   */
  void enterModulefilter(IMECLParser.ModulefilterContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#modulefilter}.
   *
   * @param ctx the parse tree
   */
  void exitModulefilter(IMECLParser.ModulefilterContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#moduleidkeyword}.
   *
   * @param ctx the parse tree
   */
  void enterModuleidkeyword(IMECLParser.ModuleidkeywordContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#moduleidkeyword}.
   *
   * @param ctx the parse tree
   */
  void exitModuleidkeyword(IMECLParser.ModuleidkeywordContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#effectivetimefilter}.
   *
   * @param ctx the parse tree
   */
  void enterEffectivetimefilter(IMECLParser.EffectivetimefilterContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#effectivetimefilter}.
   *
   * @param ctx the parse tree
   */
  void exitEffectivetimefilter(IMECLParser.EffectivetimefilterContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#effectivetimekeyword}.
   *
   * @param ctx the parse tree
   */
  void enterEffectivetimekeyword(IMECLParser.EffectivetimekeywordContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#effectivetimekeyword}.
   *
   * @param ctx the parse tree
   */
  void exitEffectivetimekeyword(IMECLParser.EffectivetimekeywordContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#timevalue}.
   *
   * @param ctx the parse tree
   */
  void enterTimevalue(IMECLParser.TimevalueContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#timevalue}.
   *
   * @param ctx the parse tree
   */
  void exitTimevalue(IMECLParser.TimevalueContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#timevalueset}.
   *
   * @param ctx the parse tree
   */
  void enterTimevalueset(IMECLParser.TimevaluesetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#timevalueset}.
   *
   * @param ctx the parse tree
   */
  void exitTimevalueset(IMECLParser.TimevaluesetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#year}.
   *
   * @param ctx the parse tree
   */
  void enterYear(IMECLParser.YearContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#year}.
   *
   * @param ctx the parse tree
   */
  void exitYear(IMECLParser.YearContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#month}.
   *
   * @param ctx the parse tree
   */
  void enterMonth(IMECLParser.MonthContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#month}.
   *
   * @param ctx the parse tree
   */
  void exitMonth(IMECLParser.MonthContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#day}.
   *
   * @param ctx the parse tree
   */
  void enterDay(IMECLParser.DayContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#day}.
   *
   * @param ctx the parse tree
   */
  void exitDay(IMECLParser.DayContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#activefilter}.
   *
   * @param ctx the parse tree
   */
  void enterActivefilter(IMECLParser.ActivefilterContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#activefilter}.
   *
   * @param ctx the parse tree
   */
  void exitActivefilter(IMECLParser.ActivefilterContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#activekeyword}.
   *
   * @param ctx the parse tree
   */
  void enterActivekeyword(IMECLParser.ActivekeywordContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#activekeyword}.
   *
   * @param ctx the parse tree
   */
  void exitActivekeyword(IMECLParser.ActivekeywordContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#activevalue}.
   *
   * @param ctx the parse tree
   */
  void enterActivevalue(IMECLParser.ActivevalueContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#activevalue}.
   *
   * @param ctx the parse tree
   */
  void exitActivevalue(IMECLParser.ActivevalueContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#activetruevalue}.
   *
   * @param ctx the parse tree
   */
  void enterActivetruevalue(IMECLParser.ActivetruevalueContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#activetruevalue}.
   *
   * @param ctx the parse tree
   */
  void exitActivetruevalue(IMECLParser.ActivetruevalueContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#activefalsevalue}.
   *
   * @param ctx the parse tree
   */
  void enterActivefalsevalue(IMECLParser.ActivefalsevalueContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#activefalsevalue}.
   *
   * @param ctx the parse tree
   */
  void exitActivefalsevalue(IMECLParser.ActivefalsevalueContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#memberfilterconstraint}.
   *
   * @param ctx the parse tree
   */
  void enterMemberfilterconstraint(IMECLParser.MemberfilterconstraintContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#memberfilterconstraint}.
   *
   * @param ctx the parse tree
   */
  void exitMemberfilterconstraint(IMECLParser.MemberfilterconstraintContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#memberfilter}.
   *
   * @param ctx the parse tree
   */
  void enterMemberfilter(IMECLParser.MemberfilterContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#memberfilter}.
   *
   * @param ctx the parse tree
   */
  void exitMemberfilter(IMECLParser.MemberfilterContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#memberfieldfilter}.
   *
   * @param ctx the parse tree
   */
  void enterMemberfieldfilter(IMECLParser.MemberfieldfilterContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#memberfieldfilter}.
   *
   * @param ctx the parse tree
   */
  void exitMemberfieldfilter(IMECLParser.MemberfieldfilterContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#historysupplement}.
   *
   * @param ctx the parse tree
   */
  void enterHistorysupplement(IMECLParser.HistorysupplementContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#historysupplement}.
   *
   * @param ctx the parse tree
   */
  void exitHistorysupplement(IMECLParser.HistorysupplementContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#historykeyword}.
   *
   * @param ctx the parse tree
   */
  void enterHistorykeyword(IMECLParser.HistorykeywordContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#historykeyword}.
   *
   * @param ctx the parse tree
   */
  void exitHistorykeyword(IMECLParser.HistorykeywordContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#historyprofilesuffix}.
   *
   * @param ctx the parse tree
   */
  void enterHistoryprofilesuffix(IMECLParser.HistoryprofilesuffixContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#historyprofilesuffix}.
   *
   * @param ctx the parse tree
   */
  void exitHistoryprofilesuffix(IMECLParser.HistoryprofilesuffixContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#historyminimumsuffix}.
   *
   * @param ctx the parse tree
   */
  void enterHistoryminimumsuffix(IMECLParser.HistoryminimumsuffixContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#historyminimumsuffix}.
   *
   * @param ctx the parse tree
   */
  void exitHistoryminimumsuffix(IMECLParser.HistoryminimumsuffixContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#historymoderatesuffix}.
   *
   * @param ctx the parse tree
   */
  void enterHistorymoderatesuffix(IMECLParser.HistorymoderatesuffixContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#historymoderatesuffix}.
   *
   * @param ctx the parse tree
   */
  void exitHistorymoderatesuffix(IMECLParser.HistorymoderatesuffixContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#historymaximumsuffix}.
   *
   * @param ctx the parse tree
   */
  void enterHistorymaximumsuffix(IMECLParser.HistorymaximumsuffixContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#historymaximumsuffix}.
   *
   * @param ctx the parse tree
   */
  void exitHistorymaximumsuffix(IMECLParser.HistorymaximumsuffixContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#historysubset}.
   *
   * @param ctx the parse tree
   */
  void enterHistorysubset(IMECLParser.HistorysubsetContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#historysubset}.
   *
   * @param ctx the parse tree
   */
  void exitHistorysubset(IMECLParser.HistorysubsetContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#numericvalue}.
   *
   * @param ctx the parse tree
   */
  void enterNumericvalue(IMECLParser.NumericvalueContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#numericvalue}.
   *
   * @param ctx the parse tree
   */
  void exitNumericvalue(IMECLParser.NumericvalueContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#stringvalue}.
   *
   * @param ctx the parse tree
   */
  void enterStringvalue(IMECLParser.StringvalueContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#stringvalue}.
   *
   * @param ctx the parse tree
   */
  void exitStringvalue(IMECLParser.StringvalueContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#integervalue}.
   *
   * @param ctx the parse tree
   */
  void enterIntegervalue(IMECLParser.IntegervalueContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#integervalue}.
   *
   * @param ctx the parse tree
   */
  void exitIntegervalue(IMECLParser.IntegervalueContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#decimalvalue}.
   *
   * @param ctx the parse tree
   */
  void enterDecimalvalue(IMECLParser.DecimalvalueContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#decimalvalue}.
   *
   * @param ctx the parse tree
   */
  void exitDecimalvalue(IMECLParser.DecimalvalueContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#booleanvalue}.
   *
   * @param ctx the parse tree
   */
  void enterBooleanvalue(IMECLParser.BooleanvalueContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#booleanvalue}.
   *
   * @param ctx the parse tree
   */
  void exitBooleanvalue(IMECLParser.BooleanvalueContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#true_1}.
   *
   * @param ctx the parse tree
   */
  void enterTrue_1(IMECLParser.True_1Context ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#true_1}.
   *
   * @param ctx the parse tree
   */
  void exitTrue_1(IMECLParser.True_1Context ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#false_1}.
   *
   * @param ctx the parse tree
   */
  void enterFalse_1(IMECLParser.False_1Context ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#false_1}.
   *
   * @param ctx the parse tree
   */
  void exitFalse_1(IMECLParser.False_1Context ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#nonnegativeintegervalue}.
   *
   * @param ctx the parse tree
   */
  void enterNonnegativeintegervalue(IMECLParser.NonnegativeintegervalueContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#nonnegativeintegervalue}.
   *
   * @param ctx the parse tree
   */
  void exitNonnegativeintegervalue(IMECLParser.NonnegativeintegervalueContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#sctid}.
   *
   * @param ctx the parse tree
   */
  void enterSctid(IMECLParser.SctidContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#sctid}.
   *
   * @param ctx the parse tree
   */
  void exitSctid(IMECLParser.SctidContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#ws}.
   *
   * @param ctx the parse tree
   */
  void enterWs(IMECLParser.WsContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#ws}.
   *
   * @param ctx the parse tree
   */
  void exitWs(IMECLParser.WsContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#mws}.
   *
   * @param ctx the parse tree
   */
  void enterMws(IMECLParser.MwsContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#mws}.
   *
   * @param ctx the parse tree
   */
  void exitMws(IMECLParser.MwsContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#comment}.
   *
   * @param ctx the parse tree
   */
  void enterComment(IMECLParser.CommentContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#comment}.
   *
   * @param ctx the parse tree
   */
  void exitComment(IMECLParser.CommentContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#nonstarchar}.
   *
   * @param ctx the parse tree
   */
  void enterNonstarchar(IMECLParser.NonstarcharContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#nonstarchar}.
   *
   * @param ctx the parse tree
   */
  void exitNonstarchar(IMECLParser.NonstarcharContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#starwithnonfslash}.
   *
   * @param ctx the parse tree
   */
  void enterStarwithnonfslash(IMECLParser.StarwithnonfslashContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#starwithnonfslash}.
   *
   * @param ctx the parse tree
   */
  void exitStarwithnonfslash(IMECLParser.StarwithnonfslashContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#nonfslash}.
   *
   * @param ctx the parse tree
   */
  void enterNonfslash(IMECLParser.NonfslashContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#nonfslash}.
   *
   * @param ctx the parse tree
   */
  void exitNonfslash(IMECLParser.NonfslashContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#sp}.
   *
   * @param ctx the parse tree
   */
  void enterSp(IMECLParser.SpContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#sp}.
   *
   * @param ctx the parse tree
   */
  void exitSp(IMECLParser.SpContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#htab}.
   *
   * @param ctx the parse tree
   */
  void enterHtab(IMECLParser.HtabContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#htab}.
   *
   * @param ctx the parse tree
   */
  void exitHtab(IMECLParser.HtabContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#cr}.
   *
   * @param ctx the parse tree
   */
  void enterCr(IMECLParser.CrContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#cr}.
   *
   * @param ctx the parse tree
   */
  void exitCr(IMECLParser.CrContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#lf}.
   *
   * @param ctx the parse tree
   */
  void enterLf(IMECLParser.LfContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#lf}.
   *
   * @param ctx the parse tree
   */
  void exitLf(IMECLParser.LfContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#qm}.
   *
   * @param ctx the parse tree
   */
  void enterQm(IMECLParser.QmContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#qm}.
   *
   * @param ctx the parse tree
   */
  void exitQm(IMECLParser.QmContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#bs}.
   *
   * @param ctx the parse tree
   */
  void enterBs(IMECLParser.BsContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#bs}.
   *
   * @param ctx the parse tree
   */
  void exitBs(IMECLParser.BsContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#star}.
   *
   * @param ctx the parse tree
   */
  void enterStar(IMECLParser.StarContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#star}.
   *
   * @param ctx the parse tree
   */
  void exitStar(IMECLParser.StarContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#digit}.
   *
   * @param ctx the parse tree
   */
  void enterDigit(IMECLParser.DigitContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#digit}.
   *
   * @param ctx the parse tree
   */
  void exitDigit(IMECLParser.DigitContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#zero}.
   *
   * @param ctx the parse tree
   */
  void enterZero(IMECLParser.ZeroContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#zero}.
   *
   * @param ctx the parse tree
   */
  void exitZero(IMECLParser.ZeroContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#digitnonzero}.
   *
   * @param ctx the parse tree
   */
  void enterDigitnonzero(IMECLParser.DigitnonzeroContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#digitnonzero}.
   *
   * @param ctx the parse tree
   */
  void exitDigitnonzero(IMECLParser.DigitnonzeroContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#nonwsnonpipe}.
   *
   * @param ctx the parse tree
   */
  void enterNonwsnonpipe(IMECLParser.NonwsnonpipeContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#nonwsnonpipe}.
   *
   * @param ctx the parse tree
   */
  void exitNonwsnonpipe(IMECLParser.NonwsnonpipeContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#anynonescapedchar}.
   *
   * @param ctx the parse tree
   */
  void enterAnynonescapedchar(IMECLParser.AnynonescapedcharContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#anynonescapedchar}.
   *
   * @param ctx the parse tree
   */
  void exitAnynonescapedchar(IMECLParser.AnynonescapedcharContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#escapedchar}.
   *
   * @param ctx the parse tree
   */
  void enterEscapedchar(IMECLParser.EscapedcharContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#escapedchar}.
   *
   * @param ctx the parse tree
   */
  void exitEscapedchar(IMECLParser.EscapedcharContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#escapedwildchar}.
   *
   * @param ctx the parse tree
   */
  void enterEscapedwildchar(IMECLParser.EscapedwildcharContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#escapedwildchar}.
   *
   * @param ctx the parse tree
   */
  void exitEscapedwildchar(IMECLParser.EscapedwildcharContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#nonwsnonescapedchar}.
   *
   * @param ctx the parse tree
   */
  void enterNonwsnonescapedchar(IMECLParser.NonwsnonescapedcharContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#nonwsnonescapedchar}.
   *
   * @param ctx the parse tree
   */
  void exitNonwsnonescapedchar(IMECLParser.NonwsnonescapedcharContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#alpha}.
   *
   * @param ctx the parse tree
   */
  void enterAlpha(IMECLParser.AlphaContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#alpha}.
   *
   * @param ctx the parse tree
   */
  void exitAlpha(IMECLParser.AlphaContext ctx);

  /**
   * Enter a parse tree produced by {@link IMECLParser#dash}.
   *
   * @param ctx the parse tree
   */
  void enterDash(IMECLParser.DashContext ctx);

  /**
   * Exit a parse tree produced by {@link IMECLParser#dash}.
   *
   * @param ctx the parse tree
   */
  void exitDash(IMECLParser.DashContext ctx);
}