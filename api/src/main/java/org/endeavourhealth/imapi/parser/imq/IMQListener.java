// Generated from java-escape by ANTLR 4.11.1
package org.endeavourhealth.imapi.parser.imq;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link IMQParser}.
 */
public interface IMQListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link IMQParser#queryRequest}.
	 * @param ctx the parse tree
	 */
	void enterQueryRequest(IMQParser.QueryRequestContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#queryRequest}.
	 * @param ctx the parse tree
	 */
	void exitQueryRequest(IMQParser.QueryRequestContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#searchText}.
	 * @param ctx the parse tree
	 */
	void enterSearchText(IMQParser.SearchTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#searchText}.
	 * @param ctx the parse tree
	 */
	void exitSearchText(IMQParser.SearchTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(IMQParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(IMQParser.ArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(IMQParser.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(IMQParser.LabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#string}.
	 * @param ctx the parse tree
	 */
	void enterString(IMQParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#string}.
	 * @param ctx the parse tree
	 */
	void exitString(IMQParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(IMQParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(IMQParser.ArgumentContext ctx);
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
	 * Enter a parse tree produced by {@link IMQParser#valueDataList}.
	 * @param ctx the parse tree
	 */
	void enterValueDataList(IMQParser.ValueDataListContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#valueDataList}.
	 * @param ctx the parse tree
	 */
	void exitValueDataList(IMQParser.ValueDataListContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#valueIriList}.
	 * @param ctx the parse tree
	 */
	void enterValueIriList(IMQParser.ValueIriListContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#valueIriList}.
	 * @param ctx the parse tree
	 */
	void exitValueIriList(IMQParser.ValueIriListContext ctx);
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
	 * Enter a parse tree produced by {@link IMQParser#iriRef}.
	 * @param ctx the parse tree
	 */
	void enterIriRef(IMQParser.IriRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#iriRef}.
	 * @param ctx the parse tree
	 */
	void exitIriRef(IMQParser.IriRefContext ctx);
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
	 * Enter a parse tree produced by {@link IMQParser#properName}.
	 * @param ctx the parse tree
	 */
	void enterProperName(IMQParser.ProperNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#properName}.
	 * @param ctx the parse tree
	 */
	void exitProperName(IMQParser.ProperNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#prefixDecl}.
	 * @param ctx the parse tree
	 */
	void enterPrefixDecl(IMQParser.PrefixDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#prefixDecl}.
	 * @param ctx the parse tree
	 */
	void exitPrefixDecl(IMQParser.PrefixDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#selectClause}.
	 * @param ctx the parse tree
	 */
	void enterSelectClause(IMQParser.SelectClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#selectClause}.
	 * @param ctx the parse tree
	 */
	void exitSelectClause(IMQParser.SelectClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#selection}.
	 * @param ctx the parse tree
	 */
	void enterSelection(IMQParser.SelectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#selection}.
	 * @param ctx the parse tree
	 */
	void exitSelection(IMQParser.SelectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#selectionList}.
	 * @param ctx the parse tree
	 */
	void enterSelectionList(IMQParser.SelectionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#selectionList}.
	 * @param ctx the parse tree
	 */
	void exitSelectionList(IMQParser.SelectionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#select}.
	 * @param ctx the parse tree
	 */
	void enterSelect(IMQParser.SelectContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#select}.
	 * @param ctx the parse tree
	 */
	void exitSelect(IMQParser.SelectContext ctx);
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
	 * Enter a parse tree produced by {@link IMQParser#description}.
	 * @param ctx the parse tree
	 */
	void enterDescription(IMQParser.DescriptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#description}.
	 * @param ctx the parse tree
	 */
	void exitDescription(IMQParser.DescriptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#activeOnly}.
	 * @param ctx the parse tree
	 */
	void enterActiveOnly(IMQParser.ActiveOnlyContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#activeOnly}.
	 * @param ctx the parse tree
	 */
	void exitActiveOnly(IMQParser.ActiveOnlyContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void enterFromClause(IMQParser.FromClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void exitFromClause(IMQParser.FromClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#fromWhere}.
	 * @param ctx the parse tree
	 */
	void enterFromWhere(IMQParser.FromWhereContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#fromWhere}.
	 * @param ctx the parse tree
	 */
	void exitFromWhere(IMQParser.FromWhereContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#bracketFrom}.
	 * @param ctx the parse tree
	 */
	void enterBracketFrom(IMQParser.BracketFromContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#bracketFrom}.
	 * @param ctx the parse tree
	 */
	void exitBracketFrom(IMQParser.BracketFromContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#fromBoolean}.
	 * @param ctx the parse tree
	 */
	void enterFromBoolean(IMQParser.FromBooleanContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#fromBoolean}.
	 * @param ctx the parse tree
	 */
	void exitFromBoolean(IMQParser.FromBooleanContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#notFrom}.
	 * @param ctx the parse tree
	 */
	void enterNotFrom(IMQParser.NotFromContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#notFrom}.
	 * @param ctx the parse tree
	 */
	void exitNotFrom(IMQParser.NotFromContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#orFrom}.
	 * @param ctx the parse tree
	 */
	void enterOrFrom(IMQParser.OrFromContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#orFrom}.
	 * @param ctx the parse tree
	 */
	void exitOrFrom(IMQParser.OrFromContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#andFrom}.
	 * @param ctx the parse tree
	 */
	void enterAndFrom(IMQParser.AndFromContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#andFrom}.
	 * @param ctx the parse tree
	 */
	void exitAndFrom(IMQParser.AndFromContext ctx);
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
	 * Enter a parse tree produced by {@link IMQParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void enterWhereClause(IMQParser.WhereClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void exitWhereClause(IMQParser.WhereClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#subWhere}.
	 * @param ctx the parse tree
	 */
	void enterSubWhere(IMQParser.SubWhereContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#subWhere}.
	 * @param ctx the parse tree
	 */
	void exitSubWhere(IMQParser.SubWhereContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#whereWith}.
	 * @param ctx the parse tree
	 */
	void enterWhereWith(IMQParser.WhereWithContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#whereWith}.
	 * @param ctx the parse tree
	 */
	void exitWhereWith(IMQParser.WhereWithContext ctx);
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
	 * Enter a parse tree produced by {@link IMQParser#whereWhere}.
	 * @param ctx the parse tree
	 */
	void enterWhereWhere(IMQParser.WhereWhereContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#whereWhere}.
	 * @param ctx the parse tree
	 */
	void exitWhereWhere(IMQParser.WhereWhereContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#notExist}.
	 * @param ctx the parse tree
	 */
	void enterNotExist(IMQParser.NotExistContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#notExist}.
	 * @param ctx the parse tree
	 */
	void exitNotExist(IMQParser.NotExistContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#whereValue}.
	 * @param ctx the parse tree
	 */
	void enterWhereValue(IMQParser.WhereValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#whereValue}.
	 * @param ctx the parse tree
	 */
	void exitWhereValue(IMQParser.WhereValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#valueLabel}.
	 * @param ctx the parse tree
	 */
	void enterValueLabel(IMQParser.ValueLabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#valueLabel}.
	 * @param ctx the parse tree
	 */
	void exitValueLabel(IMQParser.ValueLabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#whereBoolean}.
	 * @param ctx the parse tree
	 */
	void enterWhereBoolean(IMQParser.WhereBooleanContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#whereBoolean}.
	 * @param ctx the parse tree
	 */
	void exitWhereBoolean(IMQParser.WhereBooleanContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#notWhere}.
	 * @param ctx the parse tree
	 */
	void enterNotWhere(IMQParser.NotWhereContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#notWhere}.
	 * @param ctx the parse tree
	 */
	void exitNotWhere(IMQParser.NotWhereContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#orWhere}.
	 * @param ctx the parse tree
	 */
	void enterOrWhere(IMQParser.OrWhereContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#orWhere}.
	 * @param ctx the parse tree
	 */
	void exitOrWhere(IMQParser.OrWhereContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#andWhere}.
	 * @param ctx the parse tree
	 */
	void enterAndWhere(IMQParser.AndWhereContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#andWhere}.
	 * @param ctx the parse tree
	 */
	void exitAndWhere(IMQParser.AndWhereContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#bracketWhere}.
	 * @param ctx the parse tree
	 */
	void enterBracketWhere(IMQParser.BracketWhereContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#bracketWhere}.
	 * @param ctx the parse tree
	 */
	void exitBracketWhere(IMQParser.BracketWhereContext ctx);
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
	 * Enter a parse tree produced by {@link IMQParser#whereValueTest}.
	 * @param ctx the parse tree
	 */
	void enterWhereValueTest(IMQParser.WhereValueTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#whereValueTest}.
	 * @param ctx the parse tree
	 */
	void exitWhereValueTest(IMQParser.WhereValueTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#in}.
	 * @param ctx the parse tree
	 */
	void enterIn(IMQParser.InContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#in}.
	 * @param ctx the parse tree
	 */
	void exitIn(IMQParser.InContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#notin}.
	 * @param ctx the parse tree
	 */
	void enterNotin(IMQParser.NotinContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#notin}.
	 * @param ctx the parse tree
	 */
	void exitNotin(IMQParser.NotinContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#reference}.
	 * @param ctx the parse tree
	 */
	void enterReference(IMQParser.ReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#reference}.
	 * @param ctx the parse tree
	 */
	void exitReference(IMQParser.ReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#range}.
	 * @param ctx the parse tree
	 */
	void enterRange(IMQParser.RangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#range}.
	 * @param ctx the parse tree
	 */
	void exitRange(IMQParser.RangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#fromRange}.
	 * @param ctx the parse tree
	 */
	void enterFromRange(IMQParser.FromRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#fromRange}.
	 * @param ctx the parse tree
	 */
	void exitFromRange(IMQParser.FromRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#toRange}.
	 * @param ctx the parse tree
	 */
	void enterToRange(IMQParser.ToRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#toRange}.
	 * @param ctx the parse tree
	 */
	void exitToRange(IMQParser.ToRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#whereMeasure}.
	 * @param ctx the parse tree
	 */
	void enterWhereMeasure(IMQParser.WhereMeasureContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#whereMeasure}.
	 * @param ctx the parse tree
	 */
	void exitWhereMeasure(IMQParser.WhereMeasureContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#relativeTo}.
	 * @param ctx the parse tree
	 */
	void enterRelativeTo(IMQParser.RelativeToContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#relativeTo}.
	 * @param ctx the parse tree
	 */
	void exitRelativeTo(IMQParser.RelativeToContext ctx);
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
	 * Enter a parse tree produced by {@link IMQParser#sortable}.
	 * @param ctx the parse tree
	 */
	void enterSortable(IMQParser.SortableContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#sortable}.
	 * @param ctx the parse tree
	 */
	void exitSortable(IMQParser.SortableContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#latest}.
	 * @param ctx the parse tree
	 */
	void enterLatest(IMQParser.LatestContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#latest}.
	 * @param ctx the parse tree
	 */
	void exitLatest(IMQParser.LatestContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#earliest}.
	 * @param ctx the parse tree
	 */
	void enterEarliest(IMQParser.EarliestContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#earliest}.
	 * @param ctx the parse tree
	 */
	void exitEarliest(IMQParser.EarliestContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#maximum}.
	 * @param ctx the parse tree
	 */
	void enterMaximum(IMQParser.MaximumContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#maximum}.
	 * @param ctx the parse tree
	 */
	void exitMaximum(IMQParser.MaximumContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#minimum}.
	 * @param ctx the parse tree
	 */
	void enterMinimum(IMQParser.MinimumContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#minimum}.
	 * @param ctx the parse tree
	 */
	void exitMinimum(IMQParser.MinimumContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#count}.
	 * @param ctx the parse tree
	 */
	void enterCount(IMQParser.CountContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#count}.
	 * @param ctx the parse tree
	 */
	void exitCount(IMQParser.CountContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#graph}.
	 * @param ctx the parse tree
	 */
	void enterGraph(IMQParser.GraphContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#graph}.
	 * @param ctx the parse tree
	 */
	void exitGraph(IMQParser.GraphContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#sourceType}.
	 * @param ctx the parse tree
	 */
	void enterSourceType(IMQParser.SourceTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#sourceType}.
	 * @param ctx the parse tree
	 */
	void exitSourceType(IMQParser.SourceTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#subsumption}.
	 * @param ctx the parse tree
	 */
	void enterSubsumption(IMQParser.SubsumptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#subsumption}.
	 * @param ctx the parse tree
	 */
	void exitSubsumption(IMQParser.SubsumptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#ancestorOf}.
	 * @param ctx the parse tree
	 */
	void enterAncestorOf(IMQParser.AncestorOfContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#ancestorOf}.
	 * @param ctx the parse tree
	 */
	void exitAncestorOf(IMQParser.AncestorOfContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#descendantof}.
	 * @param ctx the parse tree
	 */
	void enterDescendantof(IMQParser.DescendantofContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#descendantof}.
	 * @param ctx the parse tree
	 */
	void exitDescendantof(IMQParser.DescendantofContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#descendantorselfof}.
	 * @param ctx the parse tree
	 */
	void enterDescendantorselfof(IMQParser.DescendantorselfofContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#descendantorselfof}.
	 * @param ctx the parse tree
	 */
	void exitDescendantorselfof(IMQParser.DescendantorselfofContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(IMQParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(IMQParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMQParser#alias}.
	 * @param ctx the parse tree
	 */
	void enterAlias(IMQParser.AliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMQParser#alias}.
	 * @param ctx the parse tree
	 */
	void exitAlias(IMQParser.AliasContext ctx);
}