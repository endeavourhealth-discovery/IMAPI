package org.endeavourhealth.imapi.transformation.engine;

import org.endeavourhealth.imapi.model.qof.Register;
import org.endeavourhealth.imapi.model.qof.Rule;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.transformation.component.QueryBuilder;
import org.endeavourhealth.imapi.transformation.component.MatchBuilder;
import org.endeavourhealth.imapi.transformation.core.TransformationContext;
import org.endeavourhealth.imapi.transformation.util.LogicExpressionParser;
import org.endeavourhealth.imapi.transformation.util.FieldMappingDictionary;

/**
 * Transforms QOF Registers to IMQuery queries.
 * Registers are populations based on Selections with additional filtering rules.
 */
public class RegisterTransformer {
  private final LogicExpressionParser logicParser;
  private final FieldMappingDictionary fieldMapping;

  public RegisterTransformer(LogicExpressionParser logicParser, FieldMappingDictionary fieldMapping) {
    this.logicParser = logicParser;
    this.fieldMapping = fieldMapping;
  }

  /**
   * Transform a QOF Register to an IMQuery Query.
   */
  public Query transform(Register register, TransformationContext context) {
    try {
      String registerIri = generateIri(register.getName());
      
      QueryBuilder queryBuilder = new QueryBuilder(registerIri, register.getName())
        .description(register.getDescription() != null ? register.getDescription() : 
                     String.format("Query for register: %s", register.getName()))
        .typeOf("http://endhealth.info/im#Patient");

      // Link to base Selection as cohort if specified
      if (register.getBase() != null && !register.getBase().isEmpty()) {
        Query baseQuery = context.getQuery(register.getBase());
        if (baseQuery != null) {
          queryBuilder.isCohort(baseQuery.getIri());
        } else {
          String baseIri = "http://endhealth.info/qof#selection-" + toSlugFormat(register.getBase());
          queryBuilder.isCohort(baseIri);
        }
      }

      // Process each rule in the register
      if (register.getRules() != null && !register.getRules().isEmpty()) {
        for (Rule rule : register.getRules()) {
          Match ruleMatch = transformRule(rule, context);
          if (ruleMatch != null) {
            queryBuilder.addRule(ruleMatch);
          }
        }
      }

      Query query = queryBuilder.build();
      context.addQuery(register.getName(), query);
      return query;

    } catch (Exception e) {
      context.addError("Error transforming Register '" + register.getName() + "': " + e.getMessage());
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
          // Merge the entire parsed logic structure into the rule match
          mergeLogicMatch(matchBuilder, logicMatch);
        }
      }

      // Map outcomes
      if (rule.getIfTrue() != null) {
        try {
          // Handle special cases like "Next rule"
          if ("Next rule".equalsIgnoreCase(rule.getIfTrue())) {
            // Next rule is implicit in the rule sequence
          } else {
            RuleAction action = RuleAction.valueOf(rule.getIfTrue().toUpperCase());
            matchBuilder.ifTrue(action);
          }
        } catch (IllegalArgumentException e) {
          context.addWarning("Unknown ifTrue action: " + rule.getIfTrue());
        }
      }

      if (rule.getIfFalse() != null) {
        try {
          if ("Next rule".equalsIgnoreCase(rule.getIfFalse())) {
            // Next rule is implicit
          } else {
            RuleAction action = RuleAction.valueOf(rule.getIfFalse().toUpperCase());
            matchBuilder.ifFalse(action);
          }
        } catch (IllegalArgumentException e) {
          context.addWarning("Unknown ifFalse action: " + rule.getIfFalse());
        }
      }

      if (rule.getDescription() != null) {
        matchBuilder.description(rule.getDescription());
      }

      return matchBuilder.build();

    } catch (Exception e) {
      context.addWarning("Error transforming Register Rule: " + e.getMessage());
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

  private String generateIri(String registerName) {
    return "http://endhealth.info/qof#register-" + toSlugFormat(registerName);
  }

  private String toSlugFormat(String input) {
    return input.toLowerCase()
      .replaceAll("\\s+", "-")
      .replaceAll("[^a-z0-9-]", "");
  }
}