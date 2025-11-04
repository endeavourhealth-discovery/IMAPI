package org.endeavourhealth.imapi.transformation.engine;

import org.endeavourhealth.imapi.model.qof.Indicator;
import org.endeavourhealth.imapi.model.qof.Rule;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.transformation.component.QueryBuilder;
import org.endeavourhealth.imapi.transformation.component.MatchBuilder;
import org.endeavourhealth.imapi.transformation.core.TransformationContext;
import org.endeavourhealth.imapi.transformation.util.LogicExpressionParser;
import org.endeavourhealth.imapi.transformation.util.FieldMappingDictionary;

/**
 * Transforms QOF Indicators to IMQuery queries.
 * Indicators represent calculated measures based on Registers.
 */
public class IndicatorTransformer {
  private final LogicExpressionParser logicParser;
  private final FieldMappingDictionary fieldMapping;

  public IndicatorTransformer(LogicExpressionParser logicParser, FieldMappingDictionary fieldMapping) {
    this.logicParser = logicParser;
    this.fieldMapping = fieldMapping;
  }

  /**
   * Transform a QOF Indicator to an IMQuery Query.
   */
  public Query transform(Indicator indicator, TransformationContext context) {
    try {
      String indicatorIri = generateIri(indicator.getName());
      
      QueryBuilder queryBuilder = new QueryBuilder(indicatorIri, indicator.getName())
        .description(indicator.getDescription() != null ? indicator.getDescription() :
                     String.format("Indicator: %s", indicator.getName()))
        .typeOf("http://endhealth.info/im#Patient");

      // Link to base Register as cohort
      if (indicator.getBase() != null && !indicator.getBase().isEmpty()) {
        Query baseQuery = context.getQuery(indicator.getBase());
        if (baseQuery != null) {
          queryBuilder.isCohort(baseQuery.getIri());
        } else {
          String baseIri = "http://endhealth.info/qof#register-" + toSlugFormat(indicator.getBase());
          queryBuilder.isCohort(baseIri);
        }
      }

      // Process each rule
      if (indicator.getRules() != null && !indicator.getRules().isEmpty()) {
        for (Rule rule : indicator.getRules()) {
          Match ruleMatch = transformRule(rule, context);
          if (ruleMatch != null) {
            queryBuilder.addRule(ruleMatch);
          }
        }
      }

      Query query = queryBuilder.build();
      context.addQuery(indicator.getName(), query);
      return query;

    } catch (Exception e) {
      context.addError("Error transforming Indicator '" + indicator.getName() + "': " + e.getMessage());
      return null;
    }
  }

  private Match transformRule(Rule rule, TransformationContext context) {
    try {
      MatchBuilder matchBuilder = new MatchBuilder();

      // Set rule number
      if (rule.getRule() > 0) {
        matchBuilder.ruleNumber(rule.getRule());
      }

      // Parse logic expression
      if (rule.getLogic() != null && !rule.getLogic().isEmpty()) {
        Match logicMatch = logicParser.parseLogic(rule.getLogic(), fieldMapping);
        if (logicMatch != null) {
          mergeLogicMatch(logicMatch, matchBuilder);
        }
      }

      // Map outcomes
      if (rule.getIfTrue() != null) {
        try {
          if (!"Next rule".equalsIgnoreCase(rule.getIfTrue())) {
            RuleAction action = RuleAction.valueOf(rule.getIfTrue().toUpperCase());
            matchBuilder.ifTrue(action);
          }
        } catch (IllegalArgumentException e) {
          context.addWarning("Unknown ifTrue action in Indicator rule: " + rule.getIfTrue());
        }
      }

      if (rule.getIfFalse() != null) {
        try {
          if (!"Next rule".equalsIgnoreCase(rule.getIfFalse())) {
            RuleAction action = RuleAction.valueOf(rule.getIfFalse().toUpperCase());
            matchBuilder.ifFalse(action);
          }
        } catch (IllegalArgumentException e) {
          context.addWarning("Unknown ifFalse action in Indicator rule: " + rule.getIfFalse());
        }
      }

      if (rule.getDescription() != null) {
        matchBuilder.description(rule.getDescription());
      }

      return matchBuilder.build();

    } catch (Exception e) {
      context.addWarning("Error transforming Indicator Rule: " + e.getMessage());
      return null;
    }
  }

  private void mergeLogicMatch(Match logicMatch, MatchBuilder matchBuilder) {
    if (logicMatch.getWhere() != null) {
      matchBuilder.addWhere(logicMatch.getWhere());
    }
    
    if (logicMatch.getAnd() != null && !logicMatch.getAnd().isEmpty()) {
      for (Match andCondition : logicMatch.getAnd()) {
        matchBuilder.addAnd(andCondition);
      }
    }
    
    if (logicMatch.getOr() != null && !logicMatch.getOr().isEmpty()) {
      for (Match orCondition : logicMatch.getOr()) {
        matchBuilder.addOr(orCondition);
      }
    }
  }

  private String generateIri(String indicatorName) {
    return "http://endhealth.info/qof#indicator-" + toSlugFormat(indicatorName);
  }

  private String toSlugFormat(String input) {
    return input.toLowerCase()
      .replaceAll("\\s+", "-")
      .replaceAll("[^a-z0-9-]", "");
  }
}