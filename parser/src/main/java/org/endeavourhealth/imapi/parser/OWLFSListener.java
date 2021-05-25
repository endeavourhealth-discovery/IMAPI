// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/IMAPI/parser/src/main/grammar\OWLFS.g4 by ANTLR 4.9.1
package org.endeavourhealth.imapi.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link OWLFSParser}.
 */
public interface OWLFSListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link OWLFSParser#axiom}.
	 * @param ctx the parse tree
	 */
	void enterAxiom(OWLFSParser.AxiomContext ctx);
	/**
	 * Exit a parse tree produced by {@link OWLFSParser#axiom}.
	 * @param ctx the parse tree
	 */
	void exitAxiom(OWLFSParser.AxiomContext ctx);
	/**
	 * Enter a parse tree produced by {@link OWLFSParser#reflexiveObjectProperty}.
	 * @param ctx the parse tree
	 */
	void enterReflexiveObjectProperty(OWLFSParser.ReflexiveObjectPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link OWLFSParser#reflexiveObjectProperty}.
	 * @param ctx the parse tree
	 */
	void exitReflexiveObjectProperty(OWLFSParser.ReflexiveObjectPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link OWLFSParser#transitiveObjectProperty}.
	 * @param ctx the parse tree
	 */
	void enterTransitiveObjectProperty(OWLFSParser.TransitiveObjectPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link OWLFSParser#transitiveObjectProperty}.
	 * @param ctx the parse tree
	 */
	void exitTransitiveObjectProperty(OWLFSParser.TransitiveObjectPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link OWLFSParser#subClassOf}.
	 * @param ctx the parse tree
	 */
	void enterSubClassOf(OWLFSParser.SubClassOfContext ctx);
	/**
	 * Exit a parse tree produced by {@link OWLFSParser#subClassOf}.
	 * @param ctx the parse tree
	 */
	void exitSubClassOf(OWLFSParser.SubClassOfContext ctx);
	/**
	 * Enter a parse tree produced by {@link OWLFSParser#equivalentClasses}.
	 * @param ctx the parse tree
	 */
	void enterEquivalentClasses(OWLFSParser.EquivalentClassesContext ctx);
	/**
	 * Exit a parse tree produced by {@link OWLFSParser#equivalentClasses}.
	 * @param ctx the parse tree
	 */
	void exitEquivalentClasses(OWLFSParser.EquivalentClassesContext ctx);
	/**
	 * Enter a parse tree produced by {@link OWLFSParser#subObjectPropertyOf}.
	 * @param ctx the parse tree
	 */
	void enterSubObjectPropertyOf(OWLFSParser.SubObjectPropertyOfContext ctx);
	/**
	 * Exit a parse tree produced by {@link OWLFSParser#subObjectPropertyOf}.
	 * @param ctx the parse tree
	 */
	void exitSubObjectPropertyOf(OWLFSParser.SubObjectPropertyOfContext ctx);
	/**
	 * Enter a parse tree produced by {@link OWLFSParser#subObjectPropertyExpression}.
	 * @param ctx the parse tree
	 */
	void enterSubObjectPropertyExpression(OWLFSParser.SubObjectPropertyExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OWLFSParser#subObjectPropertyExpression}.
	 * @param ctx the parse tree
	 */
	void exitSubObjectPropertyExpression(OWLFSParser.SubObjectPropertyExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OWLFSParser#propertyExpressionChain}.
	 * @param ctx the parse tree
	 */
	void enterPropertyExpressionChain(OWLFSParser.PropertyExpressionChainContext ctx);
	/**
	 * Exit a parse tree produced by {@link OWLFSParser#propertyExpressionChain}.
	 * @param ctx the parse tree
	 */
	void exitPropertyExpressionChain(OWLFSParser.PropertyExpressionChainContext ctx);
	/**
	 * Enter a parse tree produced by {@link OWLFSParser#superObjectPropertyExpression}.
	 * @param ctx the parse tree
	 */
	void enterSuperObjectPropertyExpression(OWLFSParser.SuperObjectPropertyExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OWLFSParser#superObjectPropertyExpression}.
	 * @param ctx the parse tree
	 */
	void exitSuperObjectPropertyExpression(OWLFSParser.SuperObjectPropertyExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OWLFSParser#subClass}.
	 * @param ctx the parse tree
	 */
	void enterSubClass(OWLFSParser.SubClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link OWLFSParser#subClass}.
	 * @param ctx the parse tree
	 */
	void exitSubClass(OWLFSParser.SubClassContext ctx);
	/**
	 * Enter a parse tree produced by {@link OWLFSParser#superClass}.
	 * @param ctx the parse tree
	 */
	void enterSuperClass(OWLFSParser.SuperClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link OWLFSParser#superClass}.
	 * @param ctx the parse tree
	 */
	void exitSuperClass(OWLFSParser.SuperClassContext ctx);
	/**
	 * Enter a parse tree produced by {@link OWLFSParser#classExpression}.
	 * @param ctx the parse tree
	 */
	void enterClassExpression(OWLFSParser.ClassExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OWLFSParser#classExpression}.
	 * @param ctx the parse tree
	 */
	void exitClassExpression(OWLFSParser.ClassExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OWLFSParser#iri}.
	 * @param ctx the parse tree
	 */
	void enterIri(OWLFSParser.IriContext ctx);
	/**
	 * Exit a parse tree produced by {@link OWLFSParser#iri}.
	 * @param ctx the parse tree
	 */
	void exitIri(OWLFSParser.IriContext ctx);
	/**
	 * Enter a parse tree produced by {@link OWLFSParser#objectIntersectionOf}.
	 * @param ctx the parse tree
	 */
	void enterObjectIntersectionOf(OWLFSParser.ObjectIntersectionOfContext ctx);
	/**
	 * Exit a parse tree produced by {@link OWLFSParser#objectIntersectionOf}.
	 * @param ctx the parse tree
	 */
	void exitObjectIntersectionOf(OWLFSParser.ObjectIntersectionOfContext ctx);
	/**
	 * Enter a parse tree produced by {@link OWLFSParser#objectSomeValuesFrom}.
	 * @param ctx the parse tree
	 */
	void enterObjectSomeValuesFrom(OWLFSParser.ObjectSomeValuesFromContext ctx);
	/**
	 * Exit a parse tree produced by {@link OWLFSParser#objectSomeValuesFrom}.
	 * @param ctx the parse tree
	 */
	void exitObjectSomeValuesFrom(OWLFSParser.ObjectSomeValuesFromContext ctx);
	/**
	 * Enter a parse tree produced by {@link OWLFSParser#objectPropertyExpression}.
	 * @param ctx the parse tree
	 */
	void enterObjectPropertyExpression(OWLFSParser.ObjectPropertyExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OWLFSParser#objectPropertyExpression}.
	 * @param ctx the parse tree
	 */
	void exitObjectPropertyExpression(OWLFSParser.ObjectPropertyExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OWLFSParser#objectProperty}.
	 * @param ctx the parse tree
	 */
	void enterObjectProperty(OWLFSParser.ObjectPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link OWLFSParser#objectProperty}.
	 * @param ctx the parse tree
	 */
	void exitObjectProperty(OWLFSParser.ObjectPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link OWLFSParser#inverseObjectProperty}.
	 * @param ctx the parse tree
	 */
	void enterInverseObjectProperty(OWLFSParser.InverseObjectPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link OWLFSParser#inverseObjectProperty}.
	 * @param ctx the parse tree
	 */
	void exitInverseObjectProperty(OWLFSParser.InverseObjectPropertyContext ctx);
}