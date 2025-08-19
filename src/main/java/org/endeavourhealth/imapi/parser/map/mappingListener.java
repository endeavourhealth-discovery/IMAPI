// Generated from C:/Users/david/CloudStation/EhealthTrust/DiscoveryDataService/IMAPI/api/src/main/grammars\mapping.g4 by ANTLR 4.10.1
package org.endeavourhealth.imapi.parser.map;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link mappingParser}.
 */
public interface mappingListener extends ParseTreeListener {
  /**
   * Enter a parse tree produced by {@link mappingParser#structureMap}.
   *
   * @param ctx the parse tree
   */
  void enterStructureMap(mappingParser.StructureMapContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#structureMap}.
   *
   * @param ctx the parse tree
   */
  void exitStructureMap(mappingParser.StructureMapContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#mapId}.
   *
   * @param ctx the parse tree
   */
  void enterMapId(mappingParser.MapIdContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#mapId}.
   *
   * @param ctx the parse tree
   */
  void exitMapId(mappingParser.MapIdContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#url}.
   *
   * @param ctx the parse tree
   */
  void enterUrl(mappingParser.UrlContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#url}.
   *
   * @param ctx the parse tree
   */
  void exitUrl(mappingParser.UrlContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#identifier}.
   *
   * @param ctx the parse tree
   */
  void enterIdentifier(mappingParser.IdentifierContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#identifier}.
   *
   * @param ctx the parse tree
   */
  void exitIdentifier(mappingParser.IdentifierContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#structure}.
   *
   * @param ctx the parse tree
   */
  void enterStructure(mappingParser.StructureContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#structure}.
   *
   * @param ctx the parse tree
   */
  void exitStructure(mappingParser.StructureContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#structureAlias}.
   *
   * @param ctx the parse tree
   */
  void enterStructureAlias(mappingParser.StructureAliasContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#structureAlias}.
   *
   * @param ctx the parse tree
   */
  void exitStructureAlias(mappingParser.StructureAliasContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#imports}.
   *
   * @param ctx the parse tree
   */
  void enterImports(mappingParser.ImportsContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#imports}.
   *
   * @param ctx the parse tree
   */
  void exitImports(mappingParser.ImportsContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#group}.
   *
   * @param ctx the parse tree
   */
  void enterGroup(mappingParser.GroupContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#group}.
   *
   * @param ctx the parse tree
   */
  void exitGroup(mappingParser.GroupContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#rules}.
   *
   * @param ctx the parse tree
   */
  void enterRules(mappingParser.RulesContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#rules}.
   *
   * @param ctx the parse tree
   */
  void exitRules(mappingParser.RulesContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#typeMode}.
   *
   * @param ctx the parse tree
   */
  void enterTypeMode(mappingParser.TypeModeContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#typeMode}.
   *
   * @param ctx the parse tree
   */
  void exitTypeMode(mappingParser.TypeModeContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#extends_}.
   *
   * @param ctx the parse tree
   */
  void enterExtends(mappingParser.ExtendsContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#extends_}.
   *
   * @param ctx the parse tree
   */
  void exitExtends(mappingParser.ExtendsContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#parameters}.
   *
   * @param ctx the parse tree
   */
  void enterParameters(mappingParser.ParametersContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#parameters}.
   *
   * @param ctx the parse tree
   */
  void exitParameters(mappingParser.ParametersContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#parameter}.
   *
   * @param ctx the parse tree
   */
  void enterParameter(mappingParser.ParameterContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#parameter}.
   *
   * @param ctx the parse tree
   */
  void exitParameter(mappingParser.ParameterContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#type}.
   *
   * @param ctx the parse tree
   */
  void enterType(mappingParser.TypeContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#type}.
   *
   * @param ctx the parse tree
   */
  void exitType(mappingParser.TypeContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#rule_}.
   *
   * @param ctx the parse tree
   */
  void enterRule(mappingParser.RuleContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#rule_}.
   *
   * @param ctx the parse tree
   */
  void exitRule(mappingParser.RuleContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#ruleName}.
   *
   * @param ctx the parse tree
   */
  void enterRuleName(mappingParser.RuleNameContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#ruleName}.
   *
   * @param ctx the parse tree
   */
  void exitRuleName(mappingParser.RuleNameContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#ruleSources}.
   *
   * @param ctx the parse tree
   */
  void enterRuleSources(mappingParser.RuleSourcesContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#ruleSources}.
   *
   * @param ctx the parse tree
   */
  void exitRuleSources(mappingParser.RuleSourcesContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#ruleSource}.
   *
   * @param ctx the parse tree
   */
  void enterRuleSource(mappingParser.RuleSourceContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#ruleSource}.
   *
   * @param ctx the parse tree
   */
  void exitRuleSource(mappingParser.RuleSourceContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#ruleTargets}.
   *
   * @param ctx the parse tree
   */
  void enterRuleTargets(mappingParser.RuleTargetsContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#ruleTargets}.
   *
   * @param ctx the parse tree
   */
  void exitRuleTargets(mappingParser.RuleTargetsContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#sourceType}.
   *
   * @param ctx the parse tree
   */
  void enterSourceType(mappingParser.SourceTypeContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#sourceType}.
   *
   * @param ctx the parse tree
   */
  void exitSourceType(mappingParser.SourceTypeContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#upperBound}.
   *
   * @param ctx the parse tree
   */
  void enterUpperBound(mappingParser.UpperBoundContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#upperBound}.
   *
   * @param ctx the parse tree
   */
  void exitUpperBound(mappingParser.UpperBoundContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#ruleContext}.
   *
   * @param ctx the parse tree
   */
  void enterRuleContext(mappingParser.RuleContextContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#ruleContext}.
   *
   * @param ctx the parse tree
   */
  void exitRuleContext(mappingParser.RuleContextContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#sourceDefault}.
   *
   * @param ctx the parse tree
   */
  void enterSourceDefault(mappingParser.SourceDefaultContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#sourceDefault}.
   *
   * @param ctx the parse tree
   */
  void exitSourceDefault(mappingParser.SourceDefaultContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#alias}.
   *
   * @param ctx the parse tree
   */
  void enterAlias(mappingParser.AliasContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#alias}.
   *
   * @param ctx the parse tree
   */
  void exitAlias(mappingParser.AliasContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#whereClause}.
   *
   * @param ctx the parse tree
   */
  void enterWhereClause(mappingParser.WhereClauseContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#whereClause}.
   *
   * @param ctx the parse tree
   */
  void exitWhereClause(mappingParser.WhereClauseContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#checkClause}.
   *
   * @param ctx the parse tree
   */
  void enterCheckClause(mappingParser.CheckClauseContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#checkClause}.
   *
   * @param ctx the parse tree
   */
  void exitCheckClause(mappingParser.CheckClauseContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#log}.
   *
   * @param ctx the parse tree
   */
  void enterLog(mappingParser.LogContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#log}.
   *
   * @param ctx the parse tree
   */
  void exitLog(mappingParser.LogContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#dependent}.
   *
   * @param ctx the parse tree
   */
  void enterDependent(mappingParser.DependentContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#dependent}.
   *
   * @param ctx the parse tree
   */
  void exitDependent(mappingParser.DependentContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#ruleTarget}.
   *
   * @param ctx the parse tree
   */
  void enterRuleTarget(mappingParser.RuleTargetContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#ruleTarget}.
   *
   * @param ctx the parse tree
   */
  void exitRuleTarget(mappingParser.RuleTargetContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#transform}.
   *
   * @param ctx the parse tree
   */
  void enterTransform(mappingParser.TransformContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#transform}.
   *
   * @param ctx the parse tree
   */
  void exitTransform(mappingParser.TransformContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#invocation}.
   *
   * @param ctx the parse tree
   */
  void enterInvocation(mappingParser.InvocationContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#invocation}.
   *
   * @param ctx the parse tree
   */
  void exitInvocation(mappingParser.InvocationContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#paramList}.
   *
   * @param ctx the parse tree
   */
  void enterParamList(mappingParser.ParamListContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#paramList}.
   *
   * @param ctx the parse tree
   */
  void exitParamList(mappingParser.ParamListContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#param}.
   *
   * @param ctx the parse tree
   */
  void enterParam(mappingParser.ParamContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#param}.
   *
   * @param ctx the parse tree
   */
  void exitParam(mappingParser.ParamContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#fhirPath}.
   *
   * @param ctx the parse tree
   */
  void enterFhirPath(mappingParser.FhirPathContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#fhirPath}.
   *
   * @param ctx the parse tree
   */
  void exitFhirPath(mappingParser.FhirPathContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#literal}.
   *
   * @param ctx the parse tree
   */
  void enterLiteral(mappingParser.LiteralContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#literal}.
   *
   * @param ctx the parse tree
   */
  void exitLiteral(mappingParser.LiteralContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#groupTypeMode}.
   *
   * @param ctx the parse tree
   */
  void enterGroupTypeMode(mappingParser.GroupTypeModeContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#groupTypeMode}.
   *
   * @param ctx the parse tree
   */
  void exitGroupTypeMode(mappingParser.GroupTypeModeContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#sourceListMode}.
   *
   * @param ctx the parse tree
   */
  void enterSourceListMode(mappingParser.SourceListModeContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#sourceListMode}.
   *
   * @param ctx the parse tree
   */
  void exitSourceListMode(mappingParser.SourceListModeContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#targetListMode}.
   *
   * @param ctx the parse tree
   */
  void enterTargetListMode(mappingParser.TargetListModeContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#targetListMode}.
   *
   * @param ctx the parse tree
   */
  void exitTargetListMode(mappingParser.TargetListModeContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#inputMode}.
   *
   * @param ctx the parse tree
   */
  void enterInputMode(mappingParser.InputModeContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#inputMode}.
   *
   * @param ctx the parse tree
   */
  void exitInputMode(mappingParser.InputModeContext ctx);

  /**
   * Enter a parse tree produced by {@link mappingParser#modelMode}.
   *
   * @param ctx the parse tree
   */
  void enterModelMode(mappingParser.ModelModeContext ctx);

  /**
   * Exit a parse tree produced by {@link mappingParser#modelMode}.
   *
   * @param ctx the parse tree
   */
  void exitModelMode(mappingParser.ModelModeContext ctx);
}