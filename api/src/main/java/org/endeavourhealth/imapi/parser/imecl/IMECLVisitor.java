package org.endeavourhealth.imapi.parser.imecl;
// Generated from IMECL.g4 by ANTLR 4.13.1

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link IMECLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *            operations with no return type.
 */
public interface IMECLVisitor<T> extends ParseTreeVisitor<T> {
  /**
   * Visit a parse tree produced by {@link IMECLParser#imecl}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitImecl(IMECLParser.ImeclContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#prefixes}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitPrefixes(IMECLParser.PrefixesContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#prefixDecl}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitPrefixDecl(IMECLParser.PrefixDeclContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#pname}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitPname(IMECLParser.PnameContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#iri}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitIri(IMECLParser.IriContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#conceptid}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitConceptid(IMECLParser.ConceptidContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#eclrefinement}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitEclrefinement(IMECLParser.EclrefinementContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#conjunctionrefinementset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitConjunctionrefinementset(IMECLParser.ConjunctionrefinementsetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#disjunctionrefinementset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDisjunctionrefinementset(IMECLParser.DisjunctionrefinementsetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#eclattributeset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitEclattributeset(IMECLParser.EclattributesetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#conjunctionattributeset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitConjunctionattributeset(IMECLParser.ConjunctionattributesetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#disjunctionattributeset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDisjunctionattributeset(IMECLParser.DisjunctionattributesetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#expressionconstraint}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitExpressionconstraint(IMECLParser.ExpressionconstraintContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#refinedexpressionconstraint}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRefinedexpressionconstraint(IMECLParser.RefinedexpressionconstraintContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#compoundexpressionconstraint}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitCompoundexpressionconstraint(IMECLParser.CompoundexpressionconstraintContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#conjunctionexpressionconstraint}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitConjunctionexpressionconstraint(IMECLParser.ConjunctionexpressionconstraintContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#disjunctionexpressionconstraint}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDisjunctionexpressionconstraint(IMECLParser.DisjunctionexpressionconstraintContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#exclusionexpressionconstraint}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitExclusionexpressionconstraint(IMECLParser.ExclusionexpressionconstraintContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#dottedexpressionconstraint}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDottedexpressionconstraint(IMECLParser.DottedexpressionconstraintContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#dottedexpressionattribute}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDottedexpressionattribute(IMECLParser.DottedexpressionattributeContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#subexpressionconstraint}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSubexpressionconstraint(IMECLParser.SubexpressionconstraintContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#eclfocusconcept}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitEclfocusconcept(IMECLParser.EclfocusconceptContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#dot}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDot(IMECLParser.DotContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#memberof}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitMemberof(IMECLParser.MemberofContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#refsetfieldnameset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRefsetfieldnameset(IMECLParser.RefsetfieldnamesetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#refsetfieldname}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRefsetfieldname(IMECLParser.RefsetfieldnameContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#eclconceptreference}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitEclconceptreference(IMECLParser.EclconceptreferenceContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#eclconceptreferenceset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitEclconceptreferenceset(IMECLParser.EclconceptreferencesetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#term}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTerm(IMECLParser.TermContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#altidentifier}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAltidentifier(IMECLParser.AltidentifierContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#altidentifierschemealias}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAltidentifierschemealias(IMECLParser.AltidentifierschemealiasContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#altidentifiercodewithinquotes}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAltidentifiercodewithinquotes(IMECLParser.AltidentifiercodewithinquotesContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#altidentifiercodewithoutquotes}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAltidentifiercodewithoutquotes(IMECLParser.AltidentifiercodewithoutquotesContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#wildcard}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitWildcard(IMECLParser.WildcardContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#constraintoperator}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitConstraintoperator(IMECLParser.ConstraintoperatorContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#descendantof}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDescendantof(IMECLParser.DescendantofContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#descendantorselfof}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDescendantorselfof(IMECLParser.DescendantorselfofContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#childof}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitChildof(IMECLParser.ChildofContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#childorselfof}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitChildorselfof(IMECLParser.ChildorselfofContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#ancestorof}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAncestorof(IMECLParser.AncestorofContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#ancestororselfof}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAncestororselfof(IMECLParser.AncestororselfofContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#parentof}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitParentof(IMECLParser.ParentofContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#parentorselfof}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitParentorselfof(IMECLParser.ParentorselfofContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#top}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTop(IMECLParser.TopContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#bottom}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitBottom(IMECLParser.BottomContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#conjunction}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitConjunction(IMECLParser.ConjunctionContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#disjunction}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDisjunction(IMECLParser.DisjunctionContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#exclusion}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitExclusion(IMECLParser.ExclusionContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#subrefinement}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSubrefinement(IMECLParser.SubrefinementContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#subattributeset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSubattributeset(IMECLParser.SubattributesetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#eclattributegroup}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitEclattributegroup(IMECLParser.EclattributegroupContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#eclattribute}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitEclattribute(IMECLParser.EclattributeContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#cardinality}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitCardinality(IMECLParser.CardinalityContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#minvalue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitMinvalue(IMECLParser.MinvalueContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#to}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTo(IMECLParser.ToContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#maxvalue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitMaxvalue(IMECLParser.MaxvalueContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#many}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitMany(IMECLParser.ManyContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#reverseflag}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitReverseflag(IMECLParser.ReverseflagContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#eclattributename}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitEclattributename(IMECLParser.EclattributenameContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#expressioncomparisonoperator}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitExpressioncomparisonoperator(IMECLParser.ExpressioncomparisonoperatorContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#numericcomparisonoperator}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitNumericcomparisonoperator(IMECLParser.NumericcomparisonoperatorContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#timecomparisonoperator}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTimecomparisonoperator(IMECLParser.TimecomparisonoperatorContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#stringcomparisonoperator}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitStringcomparisonoperator(IMECLParser.StringcomparisonoperatorContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#booleancomparisonoperator}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitBooleancomparisonoperator(IMECLParser.BooleancomparisonoperatorContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#idcomparisonoperator}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitIdcomparisonoperator(IMECLParser.IdcomparisonoperatorContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#descriptionfilterconstraint}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDescriptionfilterconstraint(IMECLParser.DescriptionfilterconstraintContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#descriptionfilter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDescriptionfilter(IMECLParser.DescriptionfilterContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#descriptionidfilter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDescriptionidfilter(IMECLParser.DescriptionidfilterContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#descriptionidkeyword}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDescriptionidkeyword(IMECLParser.DescriptionidkeywordContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#descriptionid}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDescriptionid(IMECLParser.DescriptionidContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#descriptionidset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDescriptionidset(IMECLParser.DescriptionidsetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#termfilter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTermfilter(IMECLParser.TermfilterContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#termkeyword}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTermkeyword(IMECLParser.TermkeywordContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#typedsearchterm}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTypedsearchterm(IMECLParser.TypedsearchtermContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#typedsearchtermset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTypedsearchtermset(IMECLParser.TypedsearchtermsetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#wild}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitWild(IMECLParser.WildContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#matchkeyword}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitMatchkeyword(IMECLParser.MatchkeywordContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#matchsearchterm}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitMatchsearchterm(IMECLParser.MatchsearchtermContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#matchsearchtermset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitMatchsearchtermset(IMECLParser.MatchsearchtermsetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#wildsearchterm}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitWildsearchterm(IMECLParser.WildsearchtermContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#wildsearchtermset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitWildsearchtermset(IMECLParser.WildsearchtermsetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#languagefilter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitLanguagefilter(IMECLParser.LanguagefilterContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#language}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitLanguage(IMECLParser.LanguageContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#languagecode}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitLanguagecode(IMECLParser.LanguagecodeContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#languagecodeset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitLanguagecodeset(IMECLParser.LanguagecodesetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#typefilter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTypefilter(IMECLParser.TypefilterContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#typeidfilter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTypeidfilter(IMECLParser.TypeidfilterContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#typeid}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTypeid(IMECLParser.TypeidContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#typetokenfilter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTypetokenfilter(IMECLParser.TypetokenfilterContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#type}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitType(IMECLParser.TypeContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#typetoken}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTypetoken(IMECLParser.TypetokenContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#typetokenset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTypetokenset(IMECLParser.TypetokensetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#synonym}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSynonym(IMECLParser.SynonymContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#fullyspecifiedname}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitFullyspecifiedname(IMECLParser.FullyspecifiednameContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#definition}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDefinition(IMECLParser.DefinitionContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#dialectfilter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDialectfilter(IMECLParser.DialectfilterContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#dialectidfilter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDialectidfilter(IMECLParser.DialectidfilterContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#dialectid}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDialectid(IMECLParser.DialectidContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#dialectaliasfilter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDialectaliasfilter(IMECLParser.DialectaliasfilterContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#dialect}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDialect(IMECLParser.DialectContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#dialectalias}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDialectalias(IMECLParser.DialectaliasContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#dialectaliasset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDialectaliasset(IMECLParser.DialectaliassetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#dialectidset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDialectidset(IMECLParser.DialectidsetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#acceptabilityset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAcceptabilityset(IMECLParser.AcceptabilitysetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#acceptabilityconceptreferenceset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAcceptabilityconceptreferenceset(IMECLParser.AcceptabilityconceptreferencesetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#acceptabilitytokenset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAcceptabilitytokenset(IMECLParser.AcceptabilitytokensetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#acceptabilitytoken}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAcceptabilitytoken(IMECLParser.AcceptabilitytokenContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#acceptable}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAcceptable(IMECLParser.AcceptableContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#preferred}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitPreferred(IMECLParser.PreferredContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#conceptfilterconstraint}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitConceptfilterconstraint(IMECLParser.ConceptfilterconstraintContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#conceptfilter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitConceptfilter(IMECLParser.ConceptfilterContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#definitionstatusfilter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDefinitionstatusfilter(IMECLParser.DefinitionstatusfilterContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#definitionstatusidfilter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDefinitionstatusidfilter(IMECLParser.DefinitionstatusidfilterContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#definitionstatusidkeyword}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDefinitionstatusidkeyword(IMECLParser.DefinitionstatusidkeywordContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#definitionstatustokenfilter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDefinitionstatustokenfilter(IMECLParser.DefinitionstatustokenfilterContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#definitionstatuskeyword}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDefinitionstatuskeyword(IMECLParser.DefinitionstatuskeywordContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#definitionstatustoken}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDefinitionstatustoken(IMECLParser.DefinitionstatustokenContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#definitionstatustokenset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDefinitionstatustokenset(IMECLParser.DefinitionstatustokensetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#primitivetoken}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitPrimitivetoken(IMECLParser.PrimitivetokenContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#definedtoken}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDefinedtoken(IMECLParser.DefinedtokenContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#modulefilter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitModulefilter(IMECLParser.ModulefilterContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#moduleidkeyword}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitModuleidkeyword(IMECLParser.ModuleidkeywordContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#effectivetimefilter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitEffectivetimefilter(IMECLParser.EffectivetimefilterContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#effectivetimekeyword}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitEffectivetimekeyword(IMECLParser.EffectivetimekeywordContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#timevalue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTimevalue(IMECLParser.TimevalueContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#timevalueset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTimevalueset(IMECLParser.TimevaluesetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#year}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitYear(IMECLParser.YearContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#month}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitMonth(IMECLParser.MonthContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#day}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDay(IMECLParser.DayContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#activefilter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitActivefilter(IMECLParser.ActivefilterContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#activekeyword}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitActivekeyword(IMECLParser.ActivekeywordContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#activevalue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitActivevalue(IMECLParser.ActivevalueContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#activetruevalue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitActivetruevalue(IMECLParser.ActivetruevalueContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#activefalsevalue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitActivefalsevalue(IMECLParser.ActivefalsevalueContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#memberfilterconstraint}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitMemberfilterconstraint(IMECLParser.MemberfilterconstraintContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#memberfilter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitMemberfilter(IMECLParser.MemberfilterContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#memberfieldfilter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitMemberfieldfilter(IMECLParser.MemberfieldfilterContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#historysupplement}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitHistorysupplement(IMECLParser.HistorysupplementContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#historykeyword}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitHistorykeyword(IMECLParser.HistorykeywordContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#historyprofilesuffix}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitHistoryprofilesuffix(IMECLParser.HistoryprofilesuffixContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#historyminimumsuffix}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitHistoryminimumsuffix(IMECLParser.HistoryminimumsuffixContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#historymoderatesuffix}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitHistorymoderatesuffix(IMECLParser.HistorymoderatesuffixContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#historymaximumsuffix}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitHistorymaximumsuffix(IMECLParser.HistorymaximumsuffixContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#historysubset}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitHistorysubset(IMECLParser.HistorysubsetContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#numericvalue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitNumericvalue(IMECLParser.NumericvalueContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#stringvalue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitStringvalue(IMECLParser.StringvalueContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#integervalue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitIntegervalue(IMECLParser.IntegervalueContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#decimalvalue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDecimalvalue(IMECLParser.DecimalvalueContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#booleanvalue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitBooleanvalue(IMECLParser.BooleanvalueContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#true_1}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTrue_1(IMECLParser.True_1Context ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#false_1}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitFalse_1(IMECLParser.False_1Context ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#nonnegativeintegervalue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitNonnegativeintegervalue(IMECLParser.NonnegativeintegervalueContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#sctid}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSctid(IMECLParser.SctidContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#ws}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitWs(IMECLParser.WsContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#mws}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitMws(IMECLParser.MwsContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#comment}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitComment(IMECLParser.CommentContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#nonstarchar}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitNonstarchar(IMECLParser.NonstarcharContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#starwithnonfslash}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitStarwithnonfslash(IMECLParser.StarwithnonfslashContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#nonfslash}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitNonfslash(IMECLParser.NonfslashContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#sp}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSp(IMECLParser.SpContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#htab}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitHtab(IMECLParser.HtabContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#cr}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitCr(IMECLParser.CrContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#lf}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitLf(IMECLParser.LfContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#qm}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitQm(IMECLParser.QmContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#bs}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitBs(IMECLParser.BsContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#star}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitStar(IMECLParser.StarContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#digit}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDigit(IMECLParser.DigitContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#zero}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitZero(IMECLParser.ZeroContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#digitnonzero}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDigitnonzero(IMECLParser.DigitnonzeroContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#nonwsnonpipe}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitNonwsnonpipe(IMECLParser.NonwsnonpipeContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#anynonescapedchar}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAnynonescapedchar(IMECLParser.AnynonescapedcharContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#escapedchar}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitEscapedchar(IMECLParser.EscapedcharContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#escapedwildchar}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitEscapedwildchar(IMECLParser.EscapedwildcharContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#nonwsnonescapedchar}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitNonwsnonescapedchar(IMECLParser.NonwsnonescapedcharContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#alpha}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAlpha(IMECLParser.AlphaContext ctx);

  /**
   * Visit a parse tree produced by {@link IMECLParser#dash}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDash(IMECLParser.DashContext ctx);
}