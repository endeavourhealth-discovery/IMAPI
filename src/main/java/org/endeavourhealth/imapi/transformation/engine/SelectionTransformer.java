package org.endeavourhealth.imapi.transformation.engine;

import org.endeavourhealth.imapi.model.qof.Selection;
import org.endeavourhealth.imapi.model.qof.SelectionRule;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.transformation.component.QueryBuilder;
import org.endeavourhealth.imapi.transformation.component.MatchBuilder;
import org.endeavourhealth.imapi.transformation.core.TransformationContext;
import org.endeavourhealth.imapi.transformation.util.LogicExpressionParser;
import org.endeavourhealth.imapi.transformation.util.FieldMappingDictionary;

/**
 * Transforms QOF Selections to IMQuery cohort queries.
 * Each Selection becomes a cohort query that can be referenced by Registers and Indicators.
 */
public class SelectionTransformer {
  private final LogicExpressionParser logicParser;
  private final FieldMappingDictionary fieldMapping;

  public SelectionTransformer(LogicExpressionParser logicParser, FieldMappingDictionary fieldMapping) {
    this.logicParser = logicParser;
    this.fieldMapping = fieldMapping;
  }

  /**
   * Transform a QOF Selection to an IMQuery Query.
   */
  public Query transform(Selection selection, TransformationContext context) {
    try {
      String selectionIri = generateIri(selection.getName());
      
      QueryBuilder queryBuilder = new QueryBuilder(selectionIri, selection.getName())
        .description(String.format("Cohort query for selection: %s", selection.getName()))
        .typeOf("http://endhealth.info/im#Patient");

      // Process each rule in the selection
      if (selection.getRules() != null && !selection.getRules().isEmpty()) {
        for (SelectionRule rule : selection.getRules()) {
          Match ruleMatch = transformRule(rule, context);
          if (ruleMatch != null) {
            queryBuilder.addRule(ruleMatch);
          }
        }
      }

      Query query = queryBuilder.build();
      context.addQuery(selection.getName(), query);
      return query;

    } catch (Exception e) {
      context.addError("Error transforming Selection '" + selection.getName() + "': " + e.getMessage());
      return null;
    }
  }

  private Match transformRule(SelectionRule rule, TransformationContext context) {
    try {
      MatchBuilder matchBuilder = new MatchBuilder();

      // Parse the logic expression to WHERE conditions
      if (rule.getLogic() != null && !rule.getLogic().isEmpty()) {
        Match logicMatch = logicParser.parseLogic(rule.getLogic(), fieldMapping);
        if (logicMatch != null) {
          // Merge the entire parsed logic structure into the rule match
          mergeLogicMatch(matchBuilder, logicMatch);
        }
      }

      // Map ifTrue and ifFalse outcomes to RuleActions
      if (rule.getIfTrue() != null) {
        try {
          RuleAction action = RuleAction.valueOf(rule.getIfTrue().toUpperCase());
          matchBuilder.ifTrue(action);
        } catch (IllegalArgumentException e) {
          context.addWarning("Unknown ifTrue action: " + rule.getIfTrue());
          matchBuilder.ifTrue(RuleAction.SELECT);
        }
      }

      if (rule.getIfFalse() != null) {
        try {
          RuleAction action = RuleAction.valueOf(rule.getIfFalse().toUpperCase());
          matchBuilder.ifFalse(action);
        } catch (IllegalArgumentException e) {
          context.addWarning("Unknown ifFalse action: " + rule.getIfFalse());
          matchBuilder.ifFalse(RuleAction.REJECT);
        }
      }

      if (rule.getDescription() != null) {
        matchBuilder.description(rule.getDescription());
      }

      return matchBuilder.build();

    } catch (Exception e) {
      context.addWarning("Error transforming SelectionRule: " + e.getMessage());
      return null;
    }
  }

  /**
   * Merge the parsed logic match structure into the rule match builder.
   * Handles single WHERE clauses as well as nested OR/AND conditions.
   */
  private void mergeLogicMatch(MatchBuilder matchBuilder, Match logicMatch) {
    if (logicMatch.getWhere() != null) {
      matchBuilder.addWhere(logicMatch.getWhere());
    }
    if (logicMatch.getAnd() != null && !logicMatch.getAnd().isEmpty()) {
      for (Match andMatch : logicMatch.getAnd()) {
        matchBuilder.addAnd(andMatch);
      }
    }
    if (logicMatch.getOr() != null && !logicMatch.getOr().isEmpty()) {
      for (Match orMatch : logicMatch.getOr()) {
        matchBuilder.addOr(orMatch);
      }
    }
  }

  private String generateIri(String selectionName) {
    return "http://endhealth.info/qof#selection-" + toSlugFormat(selectionName);
  }

  private String toSlugFormat(String input) {
    return input.toLowerCase()
      .replaceAll("\\s+", "-")
      .replaceAll("[^a-z0-9-]", "");
  }
}