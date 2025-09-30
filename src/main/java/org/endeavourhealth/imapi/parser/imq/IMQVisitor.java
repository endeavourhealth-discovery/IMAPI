// Generated from C:/Users/david/GithubRepos/IMAPI/src/main/grammars/IMQ.g4 by ANTLR 4.13.2
package imq;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link IMQParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface IMQVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link IMQParser#imq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImq(IMQParser.ImqContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinition(IMQParser.DefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#variableDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDefinition(IMQParser.VariableDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#definedAs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinedAs(IMQParser.DefinedAsContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#prefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefix(IMQParser.PrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#setLabel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetLabel(IMQParser.SetLabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#iri}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIri(IMQParser.IriContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#cohort}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCohort(IMQParser.CohortContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(IMQParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#dataset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataset(IMQParser.DatasetContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#task}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTask(IMQParser.TaskContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#cohortDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCohortDefinition(IMQParser.CohortDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery(IMQParser.QueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#cte}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCte(IMQParser.CteContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#booleanCte}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanCte(IMQParser.BooleanCteContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#exclude}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExclude(IMQParser.ExcludeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#booleanClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanClause(IMQParser.BooleanClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#with}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith(IMQParser.WithContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#and}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd(IMQParser.AndContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#or}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr(IMQParser.OrContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#not}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot(IMQParser.NotContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#from}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom(IMQParser.FromContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#set}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet(IMQParser.SetContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#where}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhere(IMQParser.WhereContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#inResultSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInResultSet(IMQParser.InResultSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#resultSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResultSet(IMQParser.ResultSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(IMQParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#propertyTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyTest(IMQParser.PropertyTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#valueCompare}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueCompare(IMQParser.ValueCompareContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(IMQParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#is}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIs(IMQParser.IsContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(IMQParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty(IMQParser.PropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(IMQParser.OperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(IMQParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#units}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnits(IMQParser.UnitsContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#ordered}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrdered(IMQParser.OrderedContext ctx);
}