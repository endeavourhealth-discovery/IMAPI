// Generated from java-escape by ANTLR 4.11.1
package org.endeavourhealth.imapi.parser.imq;
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
	 * Visit a parse tree produced by {@link IMQParser#queryRequest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryRequest(IMQParser.QueryRequestContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#searchText}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearchText(IMQParser.SearchTextContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(IMQParser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel(IMQParser.LabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(IMQParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(IMQParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(IMQParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#valueDataList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueDataList(IMQParser.ValueDataListContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#valueIriList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueIriList(IMQParser.ValueIriListContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(IMQParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#iriRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIriRef(IMQParser.IriRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery(IMQParser.QueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#properName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperName(IMQParser.ProperNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#prefixDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixDecl(IMQParser.PrefixDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#selectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectClause(IMQParser.SelectClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#selectList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectList(IMQParser.SelectListContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#select}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect(IMQParser.SelectContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(IMQParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#description}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescription(IMQParser.DescriptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#activeOnly}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActiveOnly(IMQParser.ActiveOnlyContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#fromClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFromClause(IMQParser.FromClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#bracketFrom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracketFrom(IMQParser.BracketFromContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#fromBoolean}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFromBoolean(IMQParser.FromBooleanContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#notFrom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotFrom(IMQParser.NotFromContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#orFrom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrFrom(IMQParser.OrFromContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#andFrom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndFrom(IMQParser.AndFromContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#from}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrom(IMQParser.FromContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#whereClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereClause(IMQParser.WhereClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#subWhere}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubWhere(IMQParser.SubWhereContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#where}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhere(IMQParser.WhereContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#notExist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExist(IMQParser.NotExistContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#valueLabel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueLabel(IMQParser.ValueLabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#whereBoolean}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereBoolean(IMQParser.WhereBooleanContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#notWhere}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotWhere(IMQParser.NotWhereContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#orWhere}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrWhere(IMQParser.OrWhereContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#andWhere}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndWhere(IMQParser.AndWhereContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#bracketWhere}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracketWhere(IMQParser.BracketWhereContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#with}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith(IMQParser.WithContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#whereValueTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereValueTest(IMQParser.WhereValueTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#inClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInClause(IMQParser.InClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#notInClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotInClause(IMQParser.NotInClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#conceptSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConceptSet(IMQParser.ConceptSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#reference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReference(IMQParser.ReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#inverseOf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInverseOf(IMQParser.InverseOfContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#range}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange(IMQParser.RangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#fromRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFromRange(IMQParser.FromRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#toRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToRange(IMQParser.ToRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#whereMeasure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereMeasure(IMQParser.WhereMeasureContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#relativeTo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelativeTo(IMQParser.RelativeToContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(IMQParser.OperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#units}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnits(IMQParser.UnitsContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#sortable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSortable(IMQParser.SortableContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#latest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLatest(IMQParser.LatestContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#earliest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEarliest(IMQParser.EarliestContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#maximum}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaximum(IMQParser.MaximumContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#minimum}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinimum(IMQParser.MinimumContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#count}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCount(IMQParser.CountContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#graph}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGraph(IMQParser.GraphContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#sourceType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceType(IMQParser.SourceTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#subsumption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubsumption(IMQParser.SubsumptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#ancestorAndDescendantOf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAncestorAndDescendantOf(IMQParser.AncestorAndDescendantOfContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#ancestorOf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAncestorOf(IMQParser.AncestorOfContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#descendantof}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescendantof(IMQParser.DescendantofContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#descendantorselfof}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescendantorselfof(IMQParser.DescendantorselfofContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(IMQParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMQParser#alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlias(IMQParser.AliasContext ctx);
}