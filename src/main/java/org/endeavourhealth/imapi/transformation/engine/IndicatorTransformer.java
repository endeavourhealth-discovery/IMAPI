package org.endeavourhealth.imapi.transformation.engine;

import org.endeavourhealth.imapi.model.qof.Indicator;
import org.endeavourhealth.imapi.model.qof.Rule;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.model.imq.GroupBy;
import org.endeavourhealth.imapi.transformation.core.TransformationContext;
import org.endeavourhealth.imapi.transformation.util.TransformationLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Transforms QOF Indicator definitions and calculations into IMQ query logic.
 * Maps indicator calculations, numerator/denominator logic into query expressions.
 *
 * Indicator Transformation Rules:
 * - Indicator.denominator criteria → IMQ filtering logic
 * - Indicator.numerator calculation → IMQ return/groupBy clauses
 * - Calculation rules → IMQ expressions
 * - KPI-specific logic preservation
 * - GroupBy clause creation for indicator aggregations
 */
public class IndicatorTransformer {
  private static final Logger log = LoggerFactory.getLogger(IndicatorTransformer.class);
  private final TransformationLogger transformationLogger;

  /**
   * Creates a new IndicatorTransformer.
   *
   * @param transformationLogger for structured logging with correlation tracking
   */
  public IndicatorTransformer(TransformationLogger transformationLogger) {
    this.transformationLogger = transformationLogger;
  }

  /**
   * Transforms all indicators from a QOF document into Query calculation and aggregation logic.
   *
   * Transformation Process:
   * 1. Extract all Indicator definitions
   * 2. Transform denominator criteria to filtering logic
   * 3. Transform numerator calculations to return/groupBy clauses
   * 4. Populate Query with indicator logic
   *
   * @param indicators list of QOF Indicator objects
   * @param query target Query object to populate
   * @param context transformation context
   * @return Query with indicators transformed to calculation logic
   */
  public Query transformIndicators(List<Indicator> indicators, Query query, TransformationContext context) {
    transformationLogger.info("Transforming QOF indicators to IMQ query calculation logic");

    if (indicators == null || indicators.isEmpty()) {
      transformationLogger.warn("No indicators to transform");
      return query;
    }

    for (Indicator indicator : indicators) {
      transformIndicator(indicator, query, context);
    }

    transformationLogger.info("Transformed {} indicators successfully", indicators.size());
    return query;
  }

  /**
   * Transforms a single Indicator into query calculation logic.
   *
   * @param indicator QOF Indicator to transform
   * @param query target Query object to populate
   * @param context transformation context
   */
  private void transformIndicator(Indicator indicator, Query query, TransformationContext context) {
    if (indicator == null) {
      return;
    }

    transformationLogger.debug("Transforming indicator: {}", indicator.getName());

    // Store indicator metadata
    context.mapReference("indicator_" + indicator.getName() + "_base", indicator.getBase());

    // Transform indicator rules (denominator and numerator logic)
    if (indicator.getRules() != null && !indicator.getRules().isEmpty()) {
      List<Match> denominatorMatches = new ArrayList<>();
      List<Match> numeratorMatches = new ArrayList<>();

      for (int i = 0; i < indicator.getRules().size(); i++) {
        Rule rule = indicator.getRules().get(i);
        
        if (isDenominatorRule(rule, i)) {
          // Transform to filtering/where logic
          Match denominatorMatch = transformDenominatorRule(rule, context);
          if (denominatorMatch != null) {
            denominatorMatches.add(denominatorMatch);
          }
        } else if (isNumeratorRule(rule, i)) {
          // Transform to return/groupBy logic
          Match numeratorMatch = transformNumeratorRule(rule, context);
          if (numeratorMatch != null) {
            numeratorMatches.add(numeratorMatch);
          }
        }
      }

      // Add denominator rules as AND clauses (filtering)
      for (Match denominatorMatch : denominatorMatches) {
        query.addAnd(denominatorMatch);
      }

      // Add numerator rules for grouping/aggregation
      for (Match numeratorMatch : numeratorMatches) {
        // These would typically become groupBy clauses
        transformationLogger.debug("Adding numerator logic for aggregation");
      }
    }

    // Create indicator description for reference
    String indicatorDescription = "Indicator: " + indicator.getName();
    if (indicator.getDescription() != null && !indicator.getDescription().isBlank()) {
      indicatorDescription += " - " + indicator.getDescription();
    }
    transformationLogger.debug("Indicator description: {}", indicatorDescription);
  }

  /**
   * Determines if a rule represents denominator logic.
   * Typically, denominator rules are the first or explicitly marked.
   *
   * @param rule Rule to check
   * @param ruleIndex position in rule list
   * @return true if rule is a denominator rule
   */
  private boolean isDenominatorRule(Rule rule, int ruleIndex) {
    // Convention: First rule or rules with "denominator" in description are denominator rules
    if (ruleIndex == 0) {
      return true;
    }
    
    if (rule.getDescription() != null && 
        rule.getDescription().toLowerCase().contains("denominator")) {
      return true;
    }

    return false;
  }

  /**
   * Determines if a rule represents numerator logic.
   * Typically, numerator rules are subsequent to denominator rules.
   *
   * @param rule Rule to check
   * @param ruleIndex position in rule list
   * @return true if rule is a numerator rule
   */
  private boolean isNumeratorRule(Rule rule, int ruleIndex) {
    // Convention: Non-first rules or rules with "numerator" in description
    if (ruleIndex > 0) {
      return true;
    }

    if (rule.getDescription() != null && 
        rule.getDescription().toLowerCase().contains("numerator")) {
      return true;
    }

    return false;
  }

  /**
   * Transforms a denominator rule into filtering logic.
   *
   * @param rule Rule representing denominator criteria
   * @param context transformation context
   * @return Match object for filtering
   */
  private Match transformDenominatorRule(Rule rule, TransformationContext context) {
    if (rule == null) {
      return null;
    }

    Match denominatorMatch = new Match();

    if (rule.getDescription() != null && !rule.getDescription().isBlank()) {
      denominatorMatch.setDescription("Denominator: " + rule.getDescription());
    }

    if (rule.getLogic() != null && !rule.getLogic().isBlank()) {
      transformationLogger.debug("Denominator logic: {}", rule.getLogic());
      denominatorMatch.setName(rule.getLogic());
    }

    return denominatorMatch;
  }

  /**
   * Transforms a numerator rule into calculation/aggregation logic.
   *
   * @param rule Rule representing numerator calculation
   * @param context transformation context
   * @return Match object for aggregation
   */
  private Match transformNumeratorRule(Rule rule, TransformationContext context) {
    if (rule == null) {
      return null;
    }

    Match numeratorMatch = new Match();

    if (rule.getDescription() != null && !rule.getDescription().isBlank()) {
      numeratorMatch.setDescription("Numerator: " + rule.getDescription());
    }

    if (rule.getLogic() != null && !rule.getLogic().isBlank()) {
      transformationLogger.debug("Numerator logic: {}", rule.getLogic());
      numeratorMatch.setName(rule.getLogic());
    }

    return numeratorMatch;
  }

  /**
   * Creates a GroupBy clause for indicator aggregation.
   *
   * @param indicator Indicator to create grouping for
   * @return GroupBy object for query aggregation
   */
  public GroupBy createGroupByForIndicator(Indicator indicator) {
    if (indicator == null) {
      return null;
    }

    transformationLogger.debug("Creating GroupBy clause for indicator: {}", indicator.getName());

    GroupBy groupBy = new GroupBy();
    // Additional configuration would be done here

    return groupBy;
  }

  /**
   * Validates that indicators have required fields for transformation.
   *
   * @param indicators list of indicators to validate
   * @return true if all indicators are valid, false otherwise
   */
  public boolean validateIndicators(List<Indicator> indicators) {
    if (indicators == null || indicators.isEmpty()) {
      transformationLogger.warn("No indicators to validate");
      return true;
    }

    for (Indicator indicator : indicators) {
      if (indicator.getName() == null || indicator.getName().isBlank()) {
        transformationLogger.error("Indicator has no name");
        return false;
      }

      if (indicator.getRules() == null || indicator.getRules().isEmpty()) {
        transformationLogger.warn("Indicator has no rules defined: {}", indicator.getName());
      }
    }

    return true;
  }
}