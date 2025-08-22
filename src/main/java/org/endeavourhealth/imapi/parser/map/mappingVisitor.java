// Generated from C:/Users/david/CloudStation/EhealthTrust/DiscoveryDataService/IMAPI/api/src/main/grammars\mapping.g4 by ANTLR 4.10.1
package org.endeavourhealth.imapi.parser.map;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link mappingParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *            operations with no return type.
 */
public interface mappingVisitor<T> extends ParseTreeVisitor<T> {
  /**
   * Visit a parse tree produced by {@link mappingParser#structureMap}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitStructureMap(mappingParser.StructureMapContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#mapId}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitMapId(mappingParser.MapIdContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#url}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitUrl(mappingParser.UrlContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#identifier}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitIdentifier(mappingParser.IdentifierContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#structure}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitStructure(mappingParser.StructureContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#structureAlias}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitStructureAlias(mappingParser.StructureAliasContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#imports}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitImports(mappingParser.ImportsContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#group}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroup(mappingParser.GroupContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#rules}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRules(mappingParser.RulesContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#typeMode}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTypeMode(mappingParser.TypeModeContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#extends_}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitExtends(mappingParser.ExtendsContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#parameters}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitParameters(mappingParser.ParametersContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#parameter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitParameter(mappingParser.ParameterContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#type}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitType(mappingParser.TypeContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#rule_}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRule(mappingParser.RuleContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#ruleName}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleName(mappingParser.RuleNameContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#ruleSources}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleSources(mappingParser.RuleSourcesContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#ruleSource}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleSource(mappingParser.RuleSourceContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#ruleTargets}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargets(mappingParser.RuleTargetsContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#sourceType}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSourceType(mappingParser.SourceTypeContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#upperBound}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitUpperBound(mappingParser.UpperBoundContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#ruleContext}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleContext(mappingParser.RuleContextContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#sourceDefault}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSourceDefault(mappingParser.SourceDefaultContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#alias}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAlias(mappingParser.AliasContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#whereClause}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitWhereClause(mappingParser.WhereClauseContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#checkClause}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitCheckClause(mappingParser.CheckClauseContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#log}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitLog(mappingParser.LogContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#dependent}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDependent(mappingParser.DependentContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#ruleTarget}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTarget(mappingParser.RuleTargetContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#transform}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTransform(mappingParser.TransformContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#invocation}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitInvocation(mappingParser.InvocationContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#paramList}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitParamList(mappingParser.ParamListContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#param}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitParam(mappingParser.ParamContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#fhirPath}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitFhirPath(mappingParser.FhirPathContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#literal}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitLiteral(mappingParser.LiteralContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#groupTypeMode}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroupTypeMode(mappingParser.GroupTypeModeContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#sourceListMode}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSourceListMode(mappingParser.SourceListModeContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#targetListMode}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTargetListMode(mappingParser.TargetListModeContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#inputMode}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitInputMode(mappingParser.InputModeContext ctx);

  /**
   * Visit a parse tree produced by {@link mappingParser#modelMode}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitModelMode(mappingParser.ModelModeContext ctx);
}