// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/IMAPI/parser/src/main/grammar\OWLFS.g4 by ANTLR 4.9.1
package org.endeavourhealth.imapi.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link OWLFSParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface OWLFSVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link OWLFSParser#axiom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAxiom(OWLFSParser.AxiomContext ctx);
	/**
	 * Visit a parse tree produced by {@link OWLFSParser#reflexiveObjectProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReflexiveObjectProperty(OWLFSParser.ReflexiveObjectPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link OWLFSParser#transitiveObjectProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransitiveObjectProperty(OWLFSParser.TransitiveObjectPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link OWLFSParser#subClassOf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubClassOf(OWLFSParser.SubClassOfContext ctx);
	/**
	 * Visit a parse tree produced by {@link OWLFSParser#equivalentClasses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquivalentClasses(OWLFSParser.EquivalentClassesContext ctx);
	/**
	 * Visit a parse tree produced by {@link OWLFSParser#subObjectPropertyOf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubObjectPropertyOf(OWLFSParser.SubObjectPropertyOfContext ctx);
	/**
	 * Visit a parse tree produced by {@link OWLFSParser#subObjectPropertyExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubObjectPropertyExpression(OWLFSParser.SubObjectPropertyExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OWLFSParser#propertyExpressionChain}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyExpressionChain(OWLFSParser.PropertyExpressionChainContext ctx);
	/**
	 * Visit a parse tree produced by {@link OWLFSParser#superObjectPropertyExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuperObjectPropertyExpression(OWLFSParser.SuperObjectPropertyExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OWLFSParser#subClass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubClass(OWLFSParser.SubClassContext ctx);
	/**
	 * Visit a parse tree produced by {@link OWLFSParser#superClass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuperClass(OWLFSParser.SuperClassContext ctx);
	/**
	 * Visit a parse tree produced by {@link OWLFSParser#classExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassExpression(OWLFSParser.ClassExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OWLFSParser#iri}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIri(OWLFSParser.IriContext ctx);
	/**
	 * Visit a parse tree produced by {@link OWLFSParser#objectIntersectionOf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectIntersectionOf(OWLFSParser.ObjectIntersectionOfContext ctx);
	/**
	 * Visit a parse tree produced by {@link OWLFSParser#objectSomeValuesFrom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectSomeValuesFrom(OWLFSParser.ObjectSomeValuesFromContext ctx);
	/**
	 * Visit a parse tree produced by {@link OWLFSParser#objectPropertyExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectPropertyExpression(OWLFSParser.ObjectPropertyExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OWLFSParser#objectProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectProperty(OWLFSParser.ObjectPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link OWLFSParser#inverseObjectProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInverseObjectProperty(OWLFSParser.InverseObjectPropertyContext ctx);
}