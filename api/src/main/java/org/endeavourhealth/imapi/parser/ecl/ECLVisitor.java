package org.endeavourhealth.imapi.parser.ecl;// Generated from java-escape by ANTLR 4.11.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ECLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ECLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ECLParser#ecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEcl(ECLParser.EclContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#expressionconstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionconstraint(ECLParser.ExpressionconstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#refinedexpressionconstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefinedexpressionconstraint(ECLParser.RefinedexpressionconstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#compoundexpressionconstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompoundexpressionconstraint(ECLParser.CompoundexpressionconstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#conjunctionexpressionconstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConjunctionexpressionconstraint(ECLParser.ConjunctionexpressionconstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#disjunctionexpressionconstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisjunctionexpressionconstraint(ECLParser.DisjunctionexpressionconstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#exclusionexpressionconstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExclusionexpressionconstraint(ECLParser.ExclusionexpressionconstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#dottedexpressionconstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDottedexpressionconstraint(ECLParser.DottedexpressionconstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#dottedexpressionattribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDottedexpressionattribute(ECLParser.DottedexpressionattributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#subexpressionconstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubexpressionconstraint(ECLParser.SubexpressionconstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#eclfocusconcept}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEclfocusconcept(ECLParser.EclfocusconceptContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#dot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDot(ECLParser.DotContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#memberof}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberof(ECLParser.MemberofContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#refsetfieldset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefsetfieldset(ECLParser.RefsetfieldsetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#refsetfield}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefsetfield(ECLParser.RefsetfieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#refsetfieldname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefsetfieldname(ECLParser.RefsetfieldnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#refsetfieldref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefsetfieldref(ECLParser.RefsetfieldrefContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#eclconceptreference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEclconceptreference(ECLParser.EclconceptreferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#eclconceptreferenceset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEclconceptreferenceset(ECLParser.EclconceptreferencesetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#conceptid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConceptid(ECLParser.ConceptidContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(ECLParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#wildcard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWildcard(ECLParser.WildcardContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#constraintoperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintoperator(ECLParser.ConstraintoperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#descendantof}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescendantof(ECLParser.DescendantofContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#descendantorselfof}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescendantorselfof(ECLParser.DescendantorselfofContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#childof}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChildof(ECLParser.ChildofContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#childorselfof}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChildorselfof(ECLParser.ChildorselfofContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#ancestorof}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAncestorof(ECLParser.AncestorofContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#ancestororselfof}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAncestororselfof(ECLParser.AncestororselfofContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#parentof}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParentof(ECLParser.ParentofContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#parentorselfof}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParentorselfof(ECLParser.ParentorselfofContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#conjunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConjunction(ECLParser.ConjunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#disjunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisjunction(ECLParser.DisjunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#exclusion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExclusion(ECLParser.ExclusionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#eclrefinement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEclrefinement(ECLParser.EclrefinementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#conjunctionrefinementset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConjunctionrefinementset(ECLParser.ConjunctionrefinementsetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#disjunctionrefinementset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisjunctionrefinementset(ECLParser.DisjunctionrefinementsetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#subrefinement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubrefinement(ECLParser.SubrefinementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#eclattributeset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEclattributeset(ECLParser.EclattributesetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#conjunctionattributeset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConjunctionattributeset(ECLParser.ConjunctionattributesetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#disjunctionattributeset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisjunctionattributeset(ECLParser.DisjunctionattributesetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#subattributeset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubattributeset(ECLParser.SubattributesetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#eclattributegroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEclattributegroup(ECLParser.EclattributegroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#eclattribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEclattribute(ECLParser.EclattributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#cardinality}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCardinality(ECLParser.CardinalityContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#minvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinvalue(ECLParser.MinvalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#to}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTo(ECLParser.ToContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#maxvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaxvalue(ECLParser.MaxvalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#many}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMany(ECLParser.ManyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#reverseflag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReverseflag(ECLParser.ReverseflagContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#eclattributename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEclattributename(ECLParser.EclattributenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#expressioncomparisonoperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressioncomparisonoperator(ECLParser.ExpressioncomparisonoperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#numericcomparisonoperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericcomparisonoperator(ECLParser.NumericcomparisonoperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#timecomparisonoperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimecomparisonoperator(ECLParser.TimecomparisonoperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#stringcomparisonoperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringcomparisonoperator(ECLParser.StringcomparisonoperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#booleancomparisonoperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleancomparisonoperator(ECLParser.BooleancomparisonoperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#descriptionfilterconstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescriptionfilterconstraint(ECLParser.DescriptionfilterconstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#descriptionfilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescriptionfilter(ECLParser.DescriptionfilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#termfilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermfilter(ECLParser.TermfilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#termkeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermkeyword(ECLParser.TermkeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#typedsearchterm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedsearchterm(ECLParser.TypedsearchtermContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#typedsearchtermset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedsearchtermset(ECLParser.TypedsearchtermsetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#wild}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWild(ECLParser.WildContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#match}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatch(ECLParser.MatchContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#matchsearchterm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchsearchterm(ECLParser.MatchsearchtermContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#matchsearchtermset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchsearchtermset(ECLParser.MatchsearchtermsetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#wildsearchterm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWildsearchterm(ECLParser.WildsearchtermContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#wildsearchtermset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWildsearchtermset(ECLParser.WildsearchtermsetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#languagefilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLanguagefilter(ECLParser.LanguagefilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#language}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLanguage(ECLParser.LanguageContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#languagecode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLanguagecode(ECLParser.LanguagecodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#languagecodeset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLanguagecodeset(ECLParser.LanguagecodesetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#typefilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypefilter(ECLParser.TypefilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#typeidfilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeidfilter(ECLParser.TypeidfilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#typeid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeid(ECLParser.TypeidContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#typetokenfilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypetokenfilter(ECLParser.TypetokenfilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(ECLParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#typetoken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypetoken(ECLParser.TypetokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#typetokenset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypetokenset(ECLParser.TypetokensetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#synonym}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSynonym(ECLParser.SynonymContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#fullyspecifiedname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullyspecifiedname(ECLParser.FullyspecifiednameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinition(ECLParser.DefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#dialectfilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDialectfilter(ECLParser.DialectfilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#dialectidfilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDialectidfilter(ECLParser.DialectidfilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#dialectid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDialectid(ECLParser.DialectidContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#dialectaliasfilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDialectaliasfilter(ECLParser.DialectaliasfilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#dialect}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDialect(ECLParser.DialectContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#dialectalias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDialectalias(ECLParser.DialectaliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#dialectaliasset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDialectaliasset(ECLParser.DialectaliassetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#dialectidset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDialectidset(ECLParser.DialectidsetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#acceptabilityset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAcceptabilityset(ECLParser.AcceptabilitysetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#acceptabilityconceptreferenceset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAcceptabilityconceptreferenceset(ECLParser.AcceptabilityconceptreferencesetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#acceptabilitytokenset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAcceptabilitytokenset(ECLParser.AcceptabilitytokensetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#acceptabilitytoken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAcceptabilitytoken(ECLParser.AcceptabilitytokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#acceptable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAcceptable(ECLParser.AcceptableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#preferred}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreferred(ECLParser.PreferredContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#conceptfilterconstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConceptfilterconstraint(ECLParser.ConceptfilterconstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#conceptfilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConceptfilter(ECLParser.ConceptfilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#definitionstatusfilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinitionstatusfilter(ECLParser.DefinitionstatusfilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#definitionstatusidfilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinitionstatusidfilter(ECLParser.DefinitionstatusidfilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#definitionstatusidkeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinitionstatusidkeyword(ECLParser.DefinitionstatusidkeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#definitionstatustokenfilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinitionstatustokenfilter(ECLParser.DefinitionstatustokenfilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#definitionstatuskeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinitionstatuskeyword(ECLParser.DefinitionstatuskeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#definitionstatustoken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinitionstatustoken(ECLParser.DefinitionstatustokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#definitionstatustokenset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinitionstatustokenset(ECLParser.DefinitionstatustokensetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#primitivetoken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitivetoken(ECLParser.PrimitivetokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#definedtoken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinedtoken(ECLParser.DefinedtokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#modulefilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModulefilter(ECLParser.ModulefilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#moduleidkeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleidkeyword(ECLParser.ModuleidkeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#effectivetimefilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEffectivetimefilter(ECLParser.EffectivetimefilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#effectivetimekeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEffectivetimekeyword(ECLParser.EffectivetimekeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#timevalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimevalue(ECLParser.TimevalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#timevalueset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimevalueset(ECLParser.TimevaluesetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#year}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitYear(ECLParser.YearContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#month}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMonth(ECLParser.MonthContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#day}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDay(ECLParser.DayContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#activefilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActivefilter(ECLParser.ActivefilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#activekeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActivekeyword(ECLParser.ActivekeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#activevalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActivevalue(ECLParser.ActivevalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#activetruevalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActivetruevalue(ECLParser.ActivetruevalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#activefalsevalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActivefalsevalue(ECLParser.ActivefalsevalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#memberfilterconstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberfilterconstraint(ECLParser.MemberfilterconstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#memberfilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberfilter(ECLParser.MemberfilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#memberfieldfilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberfieldfilter(ECLParser.MemberfieldfilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#historysupplement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHistorysupplement(ECLParser.HistorysupplementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#historykeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHistorykeyword(ECLParser.HistorykeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#historyprofilesuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHistoryprofilesuffix(ECLParser.HistoryprofilesuffixContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#historyminimumsuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHistoryminimumsuffix(ECLParser.HistoryminimumsuffixContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#historymoderatesuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHistorymoderatesuffix(ECLParser.HistorymoderatesuffixContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#historymaximumsuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHistorymaximumsuffix(ECLParser.HistorymaximumsuffixContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#historysubset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHistorysubset(ECLParser.HistorysubsetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#numericvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericvalue(ECLParser.NumericvalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#stringvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringvalue(ECLParser.StringvalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#integervalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegervalue(ECLParser.IntegervalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#decimalvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalvalue(ECLParser.DecimalvalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#booleanvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanvalue(ECLParser.BooleanvalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#true_1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrue_1(ECLParser.True_1Context ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#false_1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalse_1(ECLParser.False_1Context ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#nonnegativeintegervalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonnegativeintegervalue(ECLParser.NonnegativeintegervalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#sctid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSctid(ECLParser.SctidContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#ws}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWs(ECLParser.WsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#mws}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMws(ECLParser.MwsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#comment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment(ECLParser.CommentContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#nonstarchar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonstarchar(ECLParser.NonstarcharContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#starwithnonfslash}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStarwithnonfslash(ECLParser.StarwithnonfslashContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#nonfslash}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonfslash(ECLParser.NonfslashContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#sp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSp(ECLParser.SpContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#htab}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHtab(ECLParser.HtabContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#cr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCr(ECLParser.CrContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#lf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLf(ECLParser.LfContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#qm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQm(ECLParser.QmContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#bs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBs(ECLParser.BsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#star}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStar(ECLParser.StarContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#digit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDigit(ECLParser.DigitContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#zero}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZero(ECLParser.ZeroContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#digitnonzero}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDigitnonzero(ECLParser.DigitnonzeroContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#nonwsnonpipe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonwsnonpipe(ECLParser.NonwsnonpipeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#anynonescapedchar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnynonescapedchar(ECLParser.AnynonescapedcharContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#escapedchar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEscapedchar(ECLParser.EscapedcharContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#escapedwildchar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEscapedwildchar(ECLParser.EscapedwildcharContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#nonwsnonescapedchar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonwsnonescapedchar(ECLParser.NonwsnonescapedcharContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#alpha}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlpha(ECLParser.AlphaContext ctx);
	/**
	 * Visit a parse tree produced by {@link ECLParser#dash}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDash(ECLParser.DashContext ctx);
}
