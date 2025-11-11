// Generated from Z:/IdeaProjects/Endeavour/InformationManager/IMAPI/src/main/grammars/QOFDoc.g4 by ANTLR 4.13.2
package org.endeavourhealth.imapi.parser.qofdoc;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link QOFDocParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface QOFDocVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link QOFDocParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(QOFDocParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link QOFDocParser#orExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpression(QOFDocParser.OrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link QOFDocParser#andExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(QOFDocParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link QOFDocParser#notExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpression(QOFDocParser.NotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link QOFDocParser#comparisonExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonExpression(QOFDocParser.ComparisonExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link QOFDocParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(QOFDocParser.TermContext ctx);
}