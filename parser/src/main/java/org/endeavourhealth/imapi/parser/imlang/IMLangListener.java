// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/IMAPI/parser/src/main/grammars\IMLang.g4 by ANTLR 4.9.1
package org.endeavourhealth.imapi.parser.imlang;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link IMLangParser}.
 */
public interface IMLangListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link IMLangParser#concept}.
	 * @param ctx the parse tree
	 */
	void enterConcept(IMLangParser.ConceptContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#concept}.
	 * @param ctx the parse tree
	 */
	void exitConcept(IMLangParser.ConceptContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#iriLabel}.
	 * @param ctx the parse tree
	 */
	void enterIriLabel(IMLangParser.IriLabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#iriLabel}.
	 * @param ctx the parse tree
	 */
	void exitIriLabel(IMLangParser.IriLabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#annotationList}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationList(IMLangParser.AnnotationListContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#annotationList}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationList(IMLangParser.AnnotationListContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#predicateObjectList}.
	 * @param ctx the parse tree
	 */
	void enterPredicateObjectList(IMLangParser.PredicateObjectListContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#predicateObjectList}.
	 * @param ctx the parse tree
	 */
	void exitPredicateObjectList(IMLangParser.PredicateObjectListContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation(IMLangParser.AnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation(IMLangParser.AnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#scheme}.
	 * @param ctx the parse tree
	 */
	void enterScheme(IMLangParser.SchemeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#scheme}.
	 * @param ctx the parse tree
	 */
	void exitScheme(IMLangParser.SchemeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#types}.
	 * @param ctx the parse tree
	 */
	void enterTypes(IMLangParser.TypesContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#types}.
	 * @param ctx the parse tree
	 */
	void exitTypes(IMLangParser.TypesContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#version}.
	 * @param ctx the parse tree
	 */
	void enterVersion(IMLangParser.VersionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#version}.
	 * @param ctx the parse tree
	 */
	void exitVersion(IMLangParser.VersionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#axiom}.
	 * @param ctx the parse tree
	 */
	void enterAxiom(IMLangParser.AxiomContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#axiom}.
	 * @param ctx the parse tree
	 */
	void exitAxiom(IMLangParser.AxiomContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#properties}.
	 * @param ctx the parse tree
	 */
	void enterProperties(IMLangParser.PropertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#properties}.
	 * @param ctx the parse tree
	 */
	void exitProperties(IMLangParser.PropertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#membership}.
	 * @param ctx the parse tree
	 */
	void enterMembership(IMLangParser.MembershipContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#membership}.
	 * @param ctx the parse tree
	 */
	void exitMembership(IMLangParser.MembershipContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#members}.
	 * @param ctx the parse tree
	 */
	void enterMembers(IMLangParser.MembersContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#members}.
	 * @param ctx the parse tree
	 */
	void exitMembers(IMLangParser.MembersContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#notmembers}.
	 * @param ctx the parse tree
	 */
	void enterNotmembers(IMLangParser.NotmembersContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#notmembers}.
	 * @param ctx the parse tree
	 */
	void exitNotmembers(IMLangParser.NotmembersContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#target}.
	 * @param ctx the parse tree
	 */
	void enterTarget(IMLangParser.TargetContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#target}.
	 * @param ctx the parse tree
	 */
	void exitTarget(IMLangParser.TargetContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#minInclusive}.
	 * @param ctx the parse tree
	 */
	void enterMinInclusive(IMLangParser.MinInclusiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#minInclusive}.
	 * @param ctx the parse tree
	 */
	void exitMinInclusive(IMLangParser.MinInclusiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#maxInclusive}.
	 * @param ctx the parse tree
	 */
	void enterMaxInclusive(IMLangParser.MaxInclusiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#maxInclusive}.
	 * @param ctx the parse tree
	 */
	void exitMaxInclusive(IMLangParser.MaxInclusiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#minExclusive}.
	 * @param ctx the parse tree
	 */
	void enterMinExclusive(IMLangParser.MinExclusiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#minExclusive}.
	 * @param ctx the parse tree
	 */
	void exitMinExclusive(IMLangParser.MinExclusiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#maxExclusive}.
	 * @param ctx the parse tree
	 */
	void enterMaxExclusive(IMLangParser.MaxExclusiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#maxExclusive}.
	 * @param ctx the parse tree
	 */
	void exitMaxExclusive(IMLangParser.MaxExclusiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#status}.
	 * @param ctx the parse tree
	 */
	void enterStatus(IMLangParser.StatusContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#status}.
	 * @param ctx the parse tree
	 */
	void exitStatus(IMLangParser.StatusContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#subclassOf}.
	 * @param ctx the parse tree
	 */
	void enterSubclassOf(IMLangParser.SubclassOfContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#subclassOf}.
	 * @param ctx the parse tree
	 */
	void exitSubclassOf(IMLangParser.SubclassOfContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#equivalentTo}.
	 * @param ctx the parse tree
	 */
	void enterEquivalentTo(IMLangParser.EquivalentToContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#equivalentTo}.
	 * @param ctx the parse tree
	 */
	void exitEquivalentTo(IMLangParser.EquivalentToContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#subpropertyOf}.
	 * @param ctx the parse tree
	 */
	void enterSubpropertyOf(IMLangParser.SubpropertyOfContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#subpropertyOf}.
	 * @param ctx the parse tree
	 */
	void exitSubpropertyOf(IMLangParser.SubpropertyOfContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#inverseOf}.
	 * @param ctx the parse tree
	 */
	void enterInverseOf(IMLangParser.InverseOfContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#inverseOf}.
	 * @param ctx the parse tree
	 */
	void exitInverseOf(IMLangParser.InverseOfContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#domain}.
	 * @param ctx the parse tree
	 */
	void enterDomain(IMLangParser.DomainContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#domain}.
	 * @param ctx the parse tree
	 */
	void exitDomain(IMLangParser.DomainContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#range}.
	 * @param ctx the parse tree
	 */
	void enterRange(IMLangParser.RangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#range}.
	 * @param ctx the parse tree
	 */
	void exitRange(IMLangParser.RangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#classExpression}.
	 * @param ctx the parse tree
	 */
	void enterClassExpression(IMLangParser.ClassExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#classExpression}.
	 * @param ctx the parse tree
	 */
	void exitClassExpression(IMLangParser.ClassExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#classIri}.
	 * @param ctx the parse tree
	 */
	void enterClassIri(IMLangParser.ClassIriContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#classIri}.
	 * @param ctx the parse tree
	 */
	void exitClassIri(IMLangParser.ClassIriContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#and}.
	 * @param ctx the parse tree
	 */
	void enterAnd(IMLangParser.AndContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#and}.
	 * @param ctx the parse tree
	 */
	void exitAnd(IMLangParser.AndContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#or}.
	 * @param ctx the parse tree
	 */
	void enterOr(IMLangParser.OrContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#or}.
	 * @param ctx the parse tree
	 */
	void exitOr(IMLangParser.OrContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#not}.
	 * @param ctx the parse tree
	 */
	void enterNot(IMLangParser.NotContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#not}.
	 * @param ctx the parse tree
	 */
	void exitNot(IMLangParser.NotContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#iri}.
	 * @param ctx the parse tree
	 */
	void enterIri(IMLangParser.IriContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#iri}.
	 * @param ctx the parse tree
	 */
	void exitIri(IMLangParser.IriContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(IMLangParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(IMLangParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#existential}.
	 * @param ctx the parse tree
	 */
	void enterExistential(IMLangParser.ExistentialContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#existential}.
	 * @param ctx the parse tree
	 */
	void exitExistential(IMLangParser.ExistentialContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#roleIri}.
	 * @param ctx the parse tree
	 */
	void enterRoleIri(IMLangParser.RoleIriContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#roleIri}.
	 * @param ctx the parse tree
	 */
	void exitRoleIri(IMLangParser.RoleIriContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#propertyRestriction}.
	 * @param ctx the parse tree
	 */
	void enterPropertyRestriction(IMLangParser.PropertyRestrictionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#propertyRestriction}.
	 * @param ctx the parse tree
	 */
	void exitPropertyRestriction(IMLangParser.PropertyRestrictionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#some}.
	 * @param ctx the parse tree
	 */
	void enterSome(IMLangParser.SomeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#some}.
	 * @param ctx the parse tree
	 */
	void exitSome(IMLangParser.SomeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#only}.
	 * @param ctx the parse tree
	 */
	void enterOnly(IMLangParser.OnlyContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#only}.
	 * @param ctx the parse tree
	 */
	void exitOnly(IMLangParser.OnlyContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#propertyIri}.
	 * @param ctx the parse tree
	 */
	void enterPropertyIri(IMLangParser.PropertyIriContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#propertyIri}.
	 * @param ctx the parse tree
	 */
	void exitPropertyIri(IMLangParser.PropertyIriContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#exactCardinality}.
	 * @param ctx the parse tree
	 */
	void enterExactCardinality(IMLangParser.ExactCardinalityContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#exactCardinality}.
	 * @param ctx the parse tree
	 */
	void exitExactCardinality(IMLangParser.ExactCardinalityContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#rangeCardinality}.
	 * @param ctx the parse tree
	 */
	void enterRangeCardinality(IMLangParser.RangeCardinalityContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#rangeCardinality}.
	 * @param ctx the parse tree
	 */
	void exitRangeCardinality(IMLangParser.RangeCardinalityContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#minCardinality}.
	 * @param ctx the parse tree
	 */
	void enterMinCardinality(IMLangParser.MinCardinalityContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#minCardinality}.
	 * @param ctx the parse tree
	 */
	void exitMinCardinality(IMLangParser.MinCardinalityContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#maxCardinality}.
	 * @param ctx the parse tree
	 */
	void enterMaxCardinality(IMLangParser.MaxCardinalityContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#maxCardinality}.
	 * @param ctx the parse tree
	 */
	void exitMaxCardinality(IMLangParser.MaxCardinalityContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#classOrDataType}.
	 * @param ctx the parse tree
	 */
	void enterClassOrDataType(IMLangParser.ClassOrDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#classOrDataType}.
	 * @param ctx the parse tree
	 */
	void exitClassOrDataType(IMLangParser.ClassOrDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(IMLangParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(IMLangParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#description}.
	 * @param ctx the parse tree
	 */
	void enterDescription(IMLangParser.DescriptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#description}.
	 * @param ctx the parse tree
	 */
	void exitDescription(IMLangParser.DescriptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#code}.
	 * @param ctx the parse tree
	 */
	void enterCode(IMLangParser.CodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#code}.
	 * @param ctx the parse tree
	 */
	void exitCode(IMLangParser.CodeContext ctx);
}