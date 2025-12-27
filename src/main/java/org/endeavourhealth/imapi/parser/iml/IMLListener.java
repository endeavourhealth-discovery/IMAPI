// Generated from C:/Users/david/GithubRepos/IMAPI/src/main/grammars/IML.g4 by ANTLR 4.13.2
package iml;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link IMLParser}.
 */
public interface IMLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link IMLParser#iml}.
	 * @param ctx the parse tree
	 */
	void enterIml(IMLParser.ImlContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#iml}.
	 * @param ctx the parse tree
	 */
	void exitIml(IMLParser.ImlContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#definition}.
	 * @param ctx the parse tree
	 */
	void enterDefinition(IMLParser.DefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#definition}.
	 * @param ctx the parse tree
	 */
	void exitDefinition(IMLParser.DefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#matchStatement}.
	 * @param ctx the parse tree
	 */
	void enterMatchStatement(IMLParser.MatchStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#matchStatement}.
	 * @param ctx the parse tree
	 */
	void exitMatchStatement(IMLParser.MatchStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#entity}.
	 * @param ctx the parse tree
	 */
	void enterEntity(IMLParser.EntityContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#entity}.
	 * @param ctx the parse tree
	 */
	void exitEntity(IMLParser.EntityContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#fromStatement}.
	 * @param ctx the parse tree
	 */
	void enterFromStatement(IMLParser.FromStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#fromStatement}.
	 * @param ctx the parse tree
	 */
	void exitFromStatement(IMLParser.FromStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterBooleanExpression(IMLParser.BooleanExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitBooleanExpression(IMLParser.BooleanExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#nestedBooleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterNestedBooleanExpression(IMLParser.NestedBooleanExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#nestedBooleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitNestedBooleanExpression(IMLParser.NestedBooleanExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#disjunction}.
	 * @param ctx the parse tree
	 */
	void enterDisjunction(IMLParser.DisjunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#disjunction}.
	 * @param ctx the parse tree
	 */
	void exitDisjunction(IMLParser.DisjunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#conjunction}.
	 * @param ctx the parse tree
	 */
	void enterConjunction(IMLParser.ConjunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#conjunction}.
	 * @param ctx the parse tree
	 */
	void exitConjunction(IMLParser.ConjunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#whereStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhereStatement(IMLParser.WhereStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#whereStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhereStatement(IMLParser.WhereStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#property}.
	 * @param ctx the parse tree
	 */
	void enterProperty(IMLParser.PropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#property}.
	 * @param ctx the parse tree
	 */
	void exitProperty(IMLParser.PropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(IMLParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(IMLParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#exclusion}.
	 * @param ctx the parse tree
	 */
	void enterExclusion(IMLParser.ExclusionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#exclusion}.
	 * @param ctx the parse tree
	 */
	void exitExclusion(IMLParser.ExclusionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#pathSegment}.
	 * @param ctx the parse tree
	 */
	void enterPathSegment(IMLParser.PathSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#pathSegment}.
	 * @param ctx the parse tree
	 */
	void exitPathSegment(IMLParser.PathSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#conceptExpression}.
	 * @param ctx the parse tree
	 */
	void enterConceptExpression(IMLParser.ConceptExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#conceptExpression}.
	 * @param ctx the parse tree
	 */
	void exitConceptExpression(IMLParser.ConceptExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#rangeExpression}.
	 * @param ctx the parse tree
	 */
	void enterRangeExpression(IMLParser.RangeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#rangeExpression}.
	 * @param ctx the parse tree
	 */
	void exitRangeExpression(IMLParser.RangeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#range}.
	 * @param ctx the parse tree
	 */
	void enterRange(IMLParser.RangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#range}.
	 * @param ctx the parse tree
	 */
	void exitRange(IMLParser.RangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#valueCompare}.
	 * @param ctx the parse tree
	 */
	void enterValueCompare(IMLParser.ValueCompareContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#valueCompare}.
	 * @param ctx the parse tree
	 */
	void exitValueCompare(IMLParser.ValueCompareContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#functionExpression}.
	 * @param ctx the parse tree
	 */
	void enterFunctionExpression(IMLParser.FunctionExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#functionExpression}.
	 * @param ctx the parse tree
	 */
	void exitFunctionExpression(IMLParser.FunctionExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#methodCall}.
	 * @param ctx the parse tree
	 */
	void enterMethodCall(IMLParser.MethodCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#methodCall}.
	 * @param ctx the parse tree
	 */
	void exitMethodCall(IMLParser.MethodCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentList(IMLParser.ArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentList(IMLParser.ArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(IMLParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(IMLParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#simpleExpression}.
	 * @param ctx the parse tree
	 */
	void enterSimpleExpression(IMLParser.SimpleExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#simpleExpression}.
	 * @param ctx the parse tree
	 */
	void exitSimpleExpression(IMLParser.SimpleExpressionContext ctx);
}