// Generated from C:/Users/david/GithubRepos/IMAPI/src/main/grammars/IML.g4 by ANTLR 4.13.2
package iml;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link IMLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface IMLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link IMLParser#iml}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIml(IMLParser.ImlContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinition(IMLParser.DefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#matchStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchStatement(IMLParser.MatchStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#entity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntity(IMLParser.EntityContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#fromStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFromStatement(IMLParser.FromStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#booleanExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanExpression(IMLParser.BooleanExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#nestedBooleanExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNestedBooleanExpression(IMLParser.NestedBooleanExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#disjunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisjunction(IMLParser.DisjunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#conjunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConjunction(IMLParser.ConjunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#whereStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereStatement(IMLParser.WhereStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#standaloneExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStandaloneExpression(IMLParser.StandaloneExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(IMLParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#exclusion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExclusion(IMLParser.ExclusionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#pathExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathExpression(IMLParser.PathExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#pathSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathSegment(IMLParser.PathSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#conceptExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConceptExpression(IMLParser.ConceptExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#rangeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeExpression(IMLParser.RangeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#range}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange(IMLParser.RangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#valueCompare}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueCompare(IMLParser.ValueCompareContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#functionExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionExpression(IMLParser.FunctionExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#methodCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodCall(IMLParser.MethodCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(IMLParser.ArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#comparisonOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonOperator(IMLParser.ComparisonOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#simpleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleExpression(IMLParser.SimpleExpressionContext ctx);
}