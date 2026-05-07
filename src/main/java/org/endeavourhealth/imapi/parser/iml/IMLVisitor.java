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
	 * Visit a parse tree produced by {@link IMLParser#prefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefix(IMLParser.PrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#default}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault(IMLParser.DefaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinition(IMLParser.DefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(IMLParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(IMLParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#iriRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIriRef(IMLParser.IriRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#matchType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchType(IMLParser.MatchTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(IMLParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#match}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatch(IMLParser.MatchContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#path}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPath(IMLParser.PathContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#orderBy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderBy(IMLParser.OrderByContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#matchIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchIn(IMLParser.MatchInContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#from}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom(IMLParser.FromContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#keepAs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeepAs(IMLParser.KeepAsContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#booleanMatch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanMatch(IMLParser.BooleanMatchContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#nestedBooleanMatch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNestedBooleanMatch(IMLParser.NestedBooleanMatchContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#disjunctionMatch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisjunctionMatch(IMLParser.DisjunctionMatchContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#exclusionMatch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExclusionMatch(IMLParser.ExclusionMatchContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#conjunctionMatch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConjunctionMatch(IMLParser.ConjunctionMatchContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#disjunctionWhere}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisjunctionWhere(IMLParser.DisjunctionWhereContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#conjunctionWhere}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConjunctionWhere(IMLParser.ConjunctionWhereContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#nestedBooleanWhere}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNestedBooleanWhere(IMLParser.NestedBooleanWhereContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#booleanWhere}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanWhere(IMLParser.BooleanWhereContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#whereClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereClause(IMLParser.WhereClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#where}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhere(IMLParser.WhereContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty(IMLParser.PropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#propertyOf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyOf(IMLParser.PropertyOfContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#whereIs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereIs(IMLParser.WhereIsContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#concept}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcept(IMLParser.ConceptContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#disjunctionConcept}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisjunctionConcept(IMLParser.DisjunctionConceptContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#whereRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereRange(IMLParser.WhereRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#units}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnits(IMLParser.UnitsContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#whereRelativeTo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereRelativeTo(IMLParser.WhereRelativeToContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#whereValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereValue(IMLParser.WhereValueContext ctx);
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