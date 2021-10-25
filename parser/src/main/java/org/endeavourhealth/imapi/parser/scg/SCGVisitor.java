// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/IMAPI/parser/src/main/grammars\SCG.g4 by ANTLR 4.9.1
package org.endeavourhealth.imapi.parser.scg;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SCGParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SCGVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SCGParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(SCGParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#subexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubexpression(SCGParser.SubexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#definitionstatus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinitionstatus(SCGParser.DefinitionstatusContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#equivalentto}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquivalentto(SCGParser.EquivalenttoContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#subtypeof}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubtypeof(SCGParser.SubtypeofContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#focusconcept}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFocusconcept(SCGParser.FocusconceptContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#conceptreference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConceptreference(SCGParser.ConceptreferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#conceptid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConceptid(SCGParser.ConceptidContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(SCGParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#refinement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefinement(SCGParser.RefinementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#attributegroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributegroup(SCGParser.AttributegroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#attributeset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeset(SCGParser.AttributesetContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#attribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute(SCGParser.AttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#attributename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributename(SCGParser.AttributenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#attributevalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributevalue(SCGParser.AttributevalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#expressionvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionvalue(SCGParser.ExpressionvalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#stringvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringvalue(SCGParser.StringvalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#numericvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericvalue(SCGParser.NumericvalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#integervalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegervalue(SCGParser.IntegervalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#decimalvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalvalue(SCGParser.DecimalvalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#booleanvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanvalue(SCGParser.BooleanvalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#true_1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrue_1(SCGParser.True_1Context ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#false_1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalse_1(SCGParser.False_1Context ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#sctid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSctid(SCGParser.SctidContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#ws}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWs(SCGParser.WsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#sp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSp(SCGParser.SpContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#htab}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHtab(SCGParser.HtabContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#cr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCr(SCGParser.CrContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#lf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLf(SCGParser.LfContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#qm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQm(SCGParser.QmContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#bs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBs(SCGParser.BsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#digit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDigit(SCGParser.DigitContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#zero}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZero(SCGParser.ZeroContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#digitnonzero}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDigitnonzero(SCGParser.DigitnonzeroContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#nonwsnonpipe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonwsnonpipe(SCGParser.NonwsnonpipeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#anynonescapedchar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnynonescapedchar(SCGParser.AnynonescapedcharContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#escapedchar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEscapedchar(SCGParser.EscapedcharContext ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#utf8_2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUtf8_2(SCGParser.Utf8_2Context ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#utf8_3}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUtf8_3(SCGParser.Utf8_3Context ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#utf8_4}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUtf8_4(SCGParser.Utf8_4Context ctx);
	/**
	 * Visit a parse tree produced by {@link SCGParser#utf8_tail}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUtf8_tail(SCGParser.Utf8_tailContext ctx);
}