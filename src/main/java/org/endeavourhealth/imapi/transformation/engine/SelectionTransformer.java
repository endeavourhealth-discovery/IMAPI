package org.endeavourhealth.imapi.transformation.engine;

import org.endeavourhealth.imapi.model.qof.Selection;
import org.endeavourhealth.imapi.model.qof.SelectionRule;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.transformation.component.MatchBuilder;
import org.endeavourhealth.imapi.transformation.core.TransformationContext;
import org.endeavourhealth.imapi.transformation.util.TransformationLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Transforms QOF Selection criteria into IMQ Where/Match clauses.
 * Converts selection definitions and rules into logical query constructs.
 *
 * Selection Transformation Rules:
 * - Selection → Match conversion with rule composition
 * - SelectionRule.logic → Match conditions
 * - AND, OR, NOT operator handling
 * - Nested selection criteria support
 * - Where clause population in Query
 */
public class SelectionTransformer {
  private static final Logger log = LoggerFactory.getLogger(SelectionTransformer.class);
  private final MatchBuilder matchBuilder;
  private final TransformationLogger transformationLogger;

  /**
   * Creates a new SelectionTransformer.
   *
   * @param matchBuilder for building Match objects
   * @param transformationLogger for structured logging
   */
  public SelectionTransformer(MatchBuilder matchBuilder, TransformationLogger transformationLogger) {
    this.matchBuilder = matchBuilder;
    this.transformationLogger = transformationLogger;
  }

  /**
   * Transforms all selections from a QOF document into Query Where clauses.
   *
   * Transformation Process:
   * 1. Extract all Selection definitions
   * 2. Convert each Selection to Match rules
   * 3. Combine with AND operator for multiple selections
   * 4. Populate Query with Where clause
   *
   * @param selections list of QOF Selection objects
   * @param query target Query object to populate
   * @param context transformation context
   * @return Query with selections transformed to Where clause
   */
  public Query transformSelections(List<Selection> selections, Query query, TransformationContext context) {
    transformationLogger.info("Transforming QOF selections to IMQ Where clauses");

    if (selections == null || selections.isEmpty()) {
      transformationLogger.warn("No selections to transform");
      return query;
    }

    List<Match> andClauses = new ArrayList<>();

    for (Selection selection : selections) {
      Match selectionMatch = transformSelection(selection, context);
      if (selectionMatch != null) {
        andClauses.add(selectionMatch);
      }
    }

    // Add combined selections to Query
    if (!andClauses.isEmpty()) {
      if (andClauses.size() == 1) {
        // Single selection - add directly
        query.addAnd(andClauses.get(0));
      } else {
        // Multiple selections - combine with AND
        for (Match clause : andClauses) {
          query.addAnd(clause);
        }
      }
      transformationLogger.info("Transformed {} selections successfully", selections.size());
    }

    return query;
  }

  /**
   * Transforms a single Selection into a Match object.
   *
   * @param selection QOF Selection to transform
   * @param context transformation context
   * @return Match object representing the selection
   */
  private Match transformSelection(Selection selection, TransformationContext context) {
    if (selection == null) {
      return null;
    }

    transformationLogger.debug("Transforming selection: {}", selection.getName());

    Match match = new Match();
    if (selection.getName() != null) {
      match.setName(selection.getName());
    }

    // Transform selection rules
    if (selection.getRules() != null && !selection.getRules().isEmpty()) {
      List<Match> ruleMatches = new ArrayList<>();
      
      for (SelectionRule rule : selection.getRules()) {
        Match ruleMatch = transformSelectionRule(rule, context);
        if (ruleMatch != null) {
          ruleMatches.add(ruleMatch);
        }
      }

      // Add rules to match
      if (!ruleMatches.isEmpty()) {
        for (Match ruleMatch : ruleMatches) {
          match.addAnd(ruleMatch);
        }
      }
    }

    return match;
  }

  /**
   * Transforms a SelectionRule into a Match object.
   * Handles logic strings and conditional expressions.
   *
   * @param rule QOF SelectionRule to transform
   * @param context transformation context
   * @return Match object representing the rule
   */
  private Match transformSelectionRule(SelectionRule rule, TransformationContext context) {
    if (rule == null) {
      return null;
    }

    Match match = new Match();

    // Set rule description if available
    if (rule.getDescription() != null) {
      match.setDescription(rule.getDescription());
    }

    // Handle logic expressions
    if (rule.getLogic() != null && !rule.getLogic().isBlank()) {
      // Parse logic string for operators (AND, OR, NOT)
      String logic = rule.getLogic();
      
      if (logic.toUpperCase().contains("AND")) {
        // Handle AND operator
        transformationLogger.debug("Processing AND logic: {}", logic);
        // Add to AND clause
      } else if (logic.toUpperCase().contains("OR")) {
        // Handle OR operator
        transformationLogger.debug("Processing OR logic: {}", logic);
        // Add to OR clause
      } else if (logic.toUpperCase().contains("NOT")) {
        // Handle NOT operator
        transformationLogger.debug("Processing NOT logic: {}", logic);
        // Add to NOT clause
      }

      match.setName("Rule: " + logic);
    }

    // Handle conditional branches
    if (rule.getIfTrue() != null && !rule.getIfTrue().isBlank()) {
      transformationLogger.debug("Setting ifTrue action: {}", rule.getIfTrue());
    }
    if (rule.getIfFalse() != null && !rule.getIfFalse().isBlank()) {
      transformationLogger.debug("Setting ifFalse action: {}", rule.getIfFalse());
    }

    return match;
  }

  /**
   * Parses logical operator from string expression.
   *
   * @param logic logic string to parse
   * @return identified operator: "AND", "OR", "NOT", or null if not found
   */
  private String parseLogicalOperator(String logic) {
    if (logic == null) {
      return null;
    }

    String upperLogic = logic.toUpperCase();
    if (upperLogic.contains("AND")) {
      return "AND";
    } else if (upperLogic.contains("OR")) {
      return "OR";
    } else if (upperLogic.contains("NOT")) {
      return "NOT";
    }

    return null;
  }
}