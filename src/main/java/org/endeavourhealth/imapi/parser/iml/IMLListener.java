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
	 * Enter a parse tree produced by {@link IMLParser#prefix}.
	 * @param ctx the parse tree
	 */
	void enterPrefix(IMLParser.PrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#prefix}.
	 * @param ctx the parse tree
	 */
	void exitPrefix(IMLParser.PrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#default}.
	 * @param ctx the parse tree
	 */
	void enterDefault(IMLParser.DefaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#default}.
	 * @param ctx the parse tree
	 */
	void exitDefault(IMLParser.DefaultContext ctx);
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
	 * Enter a parse tree produced by {@link IMLParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(IMLParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(IMLParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#iriRef}.
	 * @param ctx the parse tree
	 */
	void enterIriRef(IMLParser.IriRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#iriRef}.
	 * @param ctx the parse tree
	 */
	void exitIriRef(IMLParser.IriRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#matchType}.
	 * @param ctx the parse tree
	 */
	void enterMatchType(IMLParser.MatchTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#matchType}.
	 * @param ctx the parse tree
	 */
	void exitMatchType(IMLParser.MatchTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(IMLParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(IMLParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#match}.
	 * @param ctx the parse tree
	 */
	void enterMatch(IMLParser.MatchContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#match}.
	 * @param ctx the parse tree
	 */
	void exitMatch(IMLParser.MatchContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#path}.
	 * @param ctx the parse tree
	 */
	void enterPath(IMLParser.PathContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#path}.
	 * @param ctx the parse tree
	 */
	void exitPath(IMLParser.PathContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#orderBy}.
	 * @param ctx the parse tree
	 */
	void enterOrderBy(IMLParser.OrderByContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#orderBy}.
	 * @param ctx the parse tree
	 */
	void exitOrderBy(IMLParser.OrderByContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#matchIn}.
	 * @param ctx the parse tree
	 */
	void enterMatchIn(IMLParser.MatchInContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#matchIn}.
	 * @param ctx the parse tree
	 */
	void exitMatchIn(IMLParser.MatchInContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#from}.
	 * @param ctx the parse tree
	 */
	void enterFrom(IMLParser.FromContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#from}.
	 * @param ctx the parse tree
	 */
	void exitFrom(IMLParser.FromContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#keepAs}.
	 * @param ctx the parse tree
	 */
	void enterKeepAs(IMLParser.KeepAsContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#keepAs}.
	 * @param ctx the parse tree
	 */
	void exitKeepAs(IMLParser.KeepAsContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#booleanMatch}.
	 * @param ctx the parse tree
	 */
	void enterBooleanMatch(IMLParser.BooleanMatchContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#booleanMatch}.
	 * @param ctx the parse tree
	 */
	void exitBooleanMatch(IMLParser.BooleanMatchContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#nestedBooleanMatch}.
	 * @param ctx the parse tree
	 */
	void enterNestedBooleanMatch(IMLParser.NestedBooleanMatchContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#nestedBooleanMatch}.
	 * @param ctx the parse tree
	 */
	void exitNestedBooleanMatch(IMLParser.NestedBooleanMatchContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#disjunctionMatch}.
	 * @param ctx the parse tree
	 */
	void enterDisjunctionMatch(IMLParser.DisjunctionMatchContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#disjunctionMatch}.
	 * @param ctx the parse tree
	 */
	void exitDisjunctionMatch(IMLParser.DisjunctionMatchContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#exclusionMatch}.
	 * @param ctx the parse tree
	 */
	void enterExclusionMatch(IMLParser.ExclusionMatchContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#exclusionMatch}.
	 * @param ctx the parse tree
	 */
	void exitExclusionMatch(IMLParser.ExclusionMatchContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#conjunctionMatch}.
	 * @param ctx the parse tree
	 */
	void enterConjunctionMatch(IMLParser.ConjunctionMatchContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#conjunctionMatch}.
	 * @param ctx the parse tree
	 */
	void exitConjunctionMatch(IMLParser.ConjunctionMatchContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#disjunctionWhere}.
	 * @param ctx the parse tree
	 */
	void enterDisjunctionWhere(IMLParser.DisjunctionWhereContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#disjunctionWhere}.
	 * @param ctx the parse tree
	 */
	void exitDisjunctionWhere(IMLParser.DisjunctionWhereContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#conjunctionWhere}.
	 * @param ctx the parse tree
	 */
	void enterConjunctionWhere(IMLParser.ConjunctionWhereContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#conjunctionWhere}.
	 * @param ctx the parse tree
	 */
	void exitConjunctionWhere(IMLParser.ConjunctionWhereContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#nestedBooleanWhere}.
	 * @param ctx the parse tree
	 */
	void enterNestedBooleanWhere(IMLParser.NestedBooleanWhereContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#nestedBooleanWhere}.
	 * @param ctx the parse tree
	 */
	void exitNestedBooleanWhere(IMLParser.NestedBooleanWhereContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#booleanWhere}.
	 * @param ctx the parse tree
	 */
	void enterBooleanWhere(IMLParser.BooleanWhereContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#booleanWhere}.
	 * @param ctx the parse tree
	 */
	void exitBooleanWhere(IMLParser.BooleanWhereContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void enterWhereClause(IMLParser.WhereClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void exitWhereClause(IMLParser.WhereClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#where}.
	 * @param ctx the parse tree
	 */
	void enterWhere(IMLParser.WhereContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#where}.
	 * @param ctx the parse tree
	 */
	void exitWhere(IMLParser.WhereContext ctx);
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
	 * Enter a parse tree produced by {@link IMLParser#propertyOf}.
	 * @param ctx the parse tree
	 */
	void enterPropertyOf(IMLParser.PropertyOfContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#propertyOf}.
	 * @param ctx the parse tree
	 */
	void exitPropertyOf(IMLParser.PropertyOfContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#whereIs}.
	 * @param ctx the parse tree
	 */
	void enterWhereIs(IMLParser.WhereIsContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#whereIs}.
	 * @param ctx the parse tree
	 */
	void exitWhereIs(IMLParser.WhereIsContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#concept}.
	 * @param ctx the parse tree
	 */
	void enterConcept(IMLParser.ConceptContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#concept}.
	 * @param ctx the parse tree
	 */
	void exitConcept(IMLParser.ConceptContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#disjunctionConcept}.
	 * @param ctx the parse tree
	 */
	void enterDisjunctionConcept(IMLParser.DisjunctionConceptContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#disjunctionConcept}.
	 * @param ctx the parse tree
	 */
	void exitDisjunctionConcept(IMLParser.DisjunctionConceptContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#whereRange}.
	 * @param ctx the parse tree
	 */
	void enterWhereRange(IMLParser.WhereRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#whereRange}.
	 * @param ctx the parse tree
	 */
	void exitWhereRange(IMLParser.WhereRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#units}.
	 * @param ctx the parse tree
	 */
	void enterUnits(IMLParser.UnitsContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#units}.
	 * @param ctx the parse tree
	 */
	void exitUnits(IMLParser.UnitsContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#whereRelativeTo}.
	 * @param ctx the parse tree
	 */
	void enterWhereRelativeTo(IMLParser.WhereRelativeToContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#whereRelativeTo}.
	 * @param ctx the parse tree
	 */
	void exitWhereRelativeTo(IMLParser.WhereRelativeToContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#whereValue}.
	 * @param ctx the parse tree
	 */
	void enterWhereValue(IMLParser.WhereValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#whereValue}.
	 * @param ctx the parse tree
	 */
	void exitWhereValue(IMLParser.WhereValueContext ctx);
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