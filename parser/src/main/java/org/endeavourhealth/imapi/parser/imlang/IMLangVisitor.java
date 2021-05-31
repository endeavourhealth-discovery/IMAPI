// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/IMAPI/parser/src/main/grammars\IMLang.g4 by ANTLR 4.9.1
package org.endeavourhealth.imapi.parser.imlang;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link IMLangParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface IMLangVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link IMLangParser#concept}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcept(IMLangParser.ConceptContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#iriLabel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIriLabel(IMLangParser.IriLabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#annotationList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationList(IMLangParser.AnnotationListContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#predicateObjectList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicateObjectList(IMLangParser.PredicateObjectListContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#annotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotation(IMLangParser.AnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#scheme}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScheme(IMLangParser.SchemeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#types}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypes(IMLangParser.TypesContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#version}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVersion(IMLangParser.VersionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#axiom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAxiom(IMLangParser.AxiomContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperties(IMLangParser.PropertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#membership}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMembership(IMLangParser.MembershipContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#members}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMembers(IMLangParser.MembersContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#notmembers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotmembers(IMLangParser.NotmembersContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#target}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTarget(IMLangParser.TargetContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#minInclusive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinInclusive(IMLangParser.MinInclusiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#maxInclusive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaxInclusive(IMLangParser.MaxInclusiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#minExclusive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinExclusive(IMLangParser.MinExclusiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#maxExclusive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaxExclusive(IMLangParser.MaxExclusiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#status}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatus(IMLangParser.StatusContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#subclassOf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubclassOf(IMLangParser.SubclassOfContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#equivalentTo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquivalentTo(IMLangParser.EquivalentToContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#subpropertyOf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubpropertyOf(IMLangParser.SubpropertyOfContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#inverseOf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInverseOf(IMLangParser.InverseOfContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#domain}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDomain(IMLangParser.DomainContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#range}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange(IMLangParser.RangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#classExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassExpression(IMLangParser.ClassExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#classIri}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassIri(IMLangParser.ClassIriContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#and}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd(IMLangParser.AndContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#or}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr(IMLangParser.OrContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#not}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot(IMLangParser.NotContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#iri}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIri(IMLangParser.IriContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(IMLangParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#existential}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistential(IMLangParser.ExistentialContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#roleIri}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoleIri(IMLangParser.RoleIriContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#propertyRestriction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyRestriction(IMLangParser.PropertyRestrictionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#some}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSome(IMLangParser.SomeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#only}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOnly(IMLangParser.OnlyContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#propertyIri}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyIri(IMLangParser.PropertyIriContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#exactCardinality}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExactCardinality(IMLangParser.ExactCardinalityContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#rangeCardinality}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeCardinality(IMLangParser.RangeCardinalityContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#minCardinality}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinCardinality(IMLangParser.MinCardinalityContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#maxCardinality}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaxCardinality(IMLangParser.MaxCardinalityContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#classOrDataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassOrDataType(IMLangParser.ClassOrDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(IMLangParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#description}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescription(IMLangParser.DescriptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCode(IMLangParser.CodeContext ctx);
}