// Generated from C:/Users/david/GithubRepos/IMAPI/src/main/grammars/IMQ.g4 by ANTLR 4.13.2
package imq;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link IMQParser}.
 */
public interface IMQListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link IMQParser#imq}.
	 * @param ctx the parse tree
	 */
	void enterImq(IMQParser.ImqContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#imq}.
	 * @param ctx the parse tree
	 */
	void exitImq(IMQParser.ImqContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#definition}.
	 * @param ctx the parse tree
	 */
	void enterDefinition(IMQParser.DefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#definition}.
	 * @param ctx the parse tree
	 */
	void exitDefinition(IMQParser.DefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#variableDefinition}.
	 * @param ctx the parse tree
	 */
	void enterVariableDefinition(IMQParser.VariableDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#variableDefinition}.
	 * @param ctx the parse tree
	 */
	void exitVariableDefinition(IMQParser.VariableDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#definedAs}.
	 * @param ctx the parse tree
	 */
	void enterDefinedAs(IMQParser.DefinedAsContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#definedAs}.
	 * @param ctx the parse tree
	 */
	void exitDefinedAs(IMQParser.DefinedAsContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#prefix}.
	 * @param ctx the parse tree
	 */
	void enterPrefix(IMQParser.PrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#prefix}.
	 * @param ctx the parse tree
	 */
	void exitPrefix(IMQParser.PrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#setLabel}.
	 * @param ctx the parse tree
	 */
	void enterSetLabel(IMQParser.SetLabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#setLabel}.
	 * @param ctx the parse tree
	 */
	void exitSetLabel(IMQParser.SetLabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#iri}.
	 * @param ctx the parse tree
	 */
	void enterIri(IMQParser.IriContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#iri}.
	 * @param ctx the parse tree
	 */
	void exitIri(IMQParser.IriContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#cohort}.
	 * @param ctx the parse tree
	 */
	void enterCohort(IMQParser.CohortContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#cohort}.
	 * @param ctx the parse tree
	 */
	void exitCohort(IMQParser.CohortContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(IMQParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(IMQParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#dataset}.
	 * @param ctx the parse tree
	 */
	void enterDataset(IMQParser.DatasetContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#dataset}.
	 * @param ctx the parse tree
	 */
	void exitDataset(IMQParser.DatasetContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#task}.
	 * @param ctx the parse tree
	 */
	void enterTask(IMQParser.TaskContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#task}.
	 * @param ctx the parse tree
	 */
	void exitTask(IMQParser.TaskContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#cohortDefinition}.
	 * @param ctx the parse tree
	 */
	void enterCohortDefinition(IMQParser.CohortDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#cohortDefinition}.
	 * @param ctx the parse tree
	 */
	void exitCohortDefinition(IMQParser.CohortDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(IMQParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(IMQParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#cte}.
	 * @param ctx the parse tree
	 */
	void enterCte(IMQParser.CteContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#cte}.
	 * @param ctx the parse tree
	 */
	void exitCte(IMQParser.CteContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#booleanCte}.
	 * @param ctx the parse tree
	 */
	void enterBooleanCte(IMQParser.BooleanCteContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#booleanCte}.
	 * @param ctx the parse tree
	 */
	void exitBooleanCte(IMQParser.BooleanCteContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#exclude}.
	 * @param ctx the parse tree
	 */
	void enterExclude(IMQParser.ExcludeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#exclude}.
	 * @param ctx the parse tree
	 */
	void exitExclude(IMQParser.ExcludeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#booleanClause}.
	 * @param ctx the parse tree
	 */
	void enterBooleanClause(IMQParser.BooleanClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#booleanClause}.
	 * @param ctx the parse tree
	 */
	void exitBooleanClause(IMQParser.BooleanClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#with}.
	 * @param ctx the parse tree
	 */
	void enterWith(IMQParser.WithContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#with}.
	 * @param ctx the parse tree
	 */
	void exitWith(IMQParser.WithContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#and}.
	 * @param ctx the parse tree
	 */
	void enterAnd(IMQParser.AndContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#and}.
	 * @param ctx the parse tree
	 */
	void exitAnd(IMQParser.AndContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#or}.
	 * @param ctx the parse tree
	 */
	void enterOr(IMQParser.OrContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#or}.
	 * @param ctx the parse tree
	 */
	void exitOr(IMQParser.OrContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#not}.
	 * @param ctx the parse tree
	 */
	void enterNot(IMQParser.NotContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#not}.
	 * @param ctx the parse tree
	 */
	void exitNot(IMQParser.NotContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#from}.
	 * @param ctx the parse tree
	 */
	void enterFrom(IMQParser.FromContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#from}.
	 * @param ctx the parse tree
	 */
	void exitFrom(IMQParser.FromContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#set}.
	 * @param ctx the parse tree
	 */
	void enterSet(IMQParser.SetContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#set}.
	 * @param ctx the parse tree
	 */
	void exitSet(IMQParser.SetContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#where}.
	 * @param ctx the parse tree
	 */
	void enterWhere(IMQParser.WhereContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#where}.
	 * @param ctx the parse tree
	 */
	void exitWhere(IMQParser.WhereContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#inResultSet}.
	 * @param ctx the parse tree
	 */
	void enterInResultSet(IMQParser.InResultSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#inResultSet}.
	 * @param ctx the parse tree
	 */
	void exitInResultSet(IMQParser.InResultSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#resultSet}.
	 * @param ctx the parse tree
	 */
	void enterResultSet(IMQParser.ResultSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#resultSet}.
	 * @param ctx the parse tree
	 */
	void exitResultSet(IMQParser.ResultSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(IMQParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(IMQParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#propertyTest}.
	 * @param ctx the parse tree
	 */
	void enterPropertyTest(IMQParser.PropertyTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#propertyTest}.
	 * @param ctx the parse tree
	 */
	void exitPropertyTest(IMQParser.PropertyTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#valueCompare}.
	 * @param ctx the parse tree
	 */
	void enterValueCompare(IMQParser.ValueCompareContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#valueCompare}.
	 * @param ctx the parse tree
	 */
	void exitValueCompare(IMQParser.ValueCompareContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(IMQParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(IMQParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#is}.
	 * @param ctx the parse tree
	 */
	void enterIs(IMQParser.IsContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#is}.
	 * @param ctx the parse tree
	 */
	void exitIs(IMQParser.IsContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(IMQParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(IMQParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#property}.
	 * @param ctx the parse tree
	 */
	void enterProperty(IMQParser.PropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#property}.
	 * @param ctx the parse tree
	 */
	void exitProperty(IMQParser.PropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(IMQParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(IMQParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(IMQParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(IMQParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#units}.
	 * @param ctx the parse tree
	 */
	void enterUnits(IMQParser.UnitsContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#units}.
	 * @param ctx the parse tree
	 */
	void exitUnits(IMQParser.UnitsContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#ordered}.
	 * @param ctx the parse tree
	 */
	void enterOrdered(IMQParser.OrderedContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#ordered}.
	 * @param ctx the parse tree
	 */
	void exitOrdered(IMQParser.OrderedContext ctx);
}