// Generated from Z:/IdeaProjects/Endeavour/InformationManager/IMAPI/src/main/grammars/QOFDoc.g4 by ANTLR 4.13.2
package org.endeavourhealth.imapi.parser.qofdoc;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link QOFDocParser}.
 */
public interface QOFDocListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link QOFDocParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(QOFDocParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link QOFDocParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(QOFDocParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link QOFDocParser#orExpression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(QOFDocParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link QOFDocParser#orExpression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(QOFDocParser.OrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link QOFDocParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(QOFDocParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link QOFDocParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(QOFDocParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link QOFDocParser#notExpression}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(QOFDocParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link QOFDocParser#notExpression}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(QOFDocParser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link QOFDocParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExpression(QOFDocParser.ComparisonExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link QOFDocParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExpression(QOFDocParser.ComparisonExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link QOFDocParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(QOFDocParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link QOFDocParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(QOFDocParser.TermContext ctx);
}