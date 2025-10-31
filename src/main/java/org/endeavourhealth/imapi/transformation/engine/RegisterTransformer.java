package org.endeavourhealth.imapi.transformation.engine;

import org.endeavourhealth.imapi.model.qof.Register;
import org.endeavourhealth.imapi.model.qof.Rule;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.transformation.core.TransformationContext;
import org.endeavourhealth.imapi.transformation.util.TransformationLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Transforms QOF Register definitions into IMQ dataSet declarations.
 * Maps data registry and source information into query dataset specifications.
 *
 * Register Transformation Rules:
 * - Register.name → Query.dataSet reference
 * - Register.description → metadata preservation
 * - Register.base → data source identification
 * - Multiple registers aggregation with AND logic
 * - Preserve registry metadata and source information
 */
public class RegisterTransformer {
  private static final Logger log = LoggerFactory.getLogger(RegisterTransformer.class);
  private final TransformationLogger transformationLogger;

  /**
   * Creates a new RegisterTransformer.
   *
   * @param transformationLogger for structured logging with correlation tracking
   */
  public RegisterTransformer(TransformationLogger transformationLogger) {
    this.transformationLogger = transformationLogger;
  }

  /**
   * Transforms all registers from a QOF document into Query dataSet specifications.
   *
   * Transformation Process:
   * 1. Extract all Register definitions
   * 2. Convert each Register to dataSet entry
   * 3. Aggregate multiple registers
   * 4. Populate Query with dataSet information
   *
   * @param registers list of QOF Register objects
   * @param query target Query object to populate
   * @param context transformation context
   * @return Query with registers transformed to dataSet declarations
   */
  public Query transformRegisters(List<Register> registers, Query query, TransformationContext context) {
    transformationLogger.info("Transforming QOF registers to IMQ dataSet declarations");

    if (registers == null || registers.isEmpty()) {
      transformationLogger.warn("No registers to transform");
      return query;
    }

    List<Match> registerMatches = new ArrayList<>();

    for (Register register : registers) {
      Match registerMatch = transformRegister(register, context);
      if (registerMatch != null) {
        registerMatches.add(registerMatch);
      }
    }

    // Add combined registers to Query
    if (!registerMatches.isEmpty()) {
      for (Match registerMatch : registerMatches) {
        query.addAnd(registerMatch);
      }
      transformationLogger.info("Transformed {} registers successfully", registers.size());
    }

    return query;
  }

  /**
   * Transforms a single Register into a Match object representing a dataSet.
   *
   * @param register QOF Register to transform
   * @param context transformation context
   * @return Match object representing the register as dataSet
   */
  private Match transformRegister(Register register, TransformationContext context) {
    if (register == null) {
      return null;
    }

    transformationLogger.debug("Transforming register: {}", register.getName());

    Match registerMatch = new Match();

    // Map register name to Match name
    if (register.getName() != null && !register.getName().isBlank()) {
      registerMatch.setName(register.getName());
    }

    // Map register description if available
    if (register.getDescription() != null && !register.getDescription().isBlank()) {
      registerMatch.setDescription(register.getDescription());
    }

    // Map register base (data source)
    if (register.getBase() != null && !register.getBase().isBlank()) {
      transformationLogger.debug("Register base (data source): {}", register.getBase());
      // Store base/source information
      context.mapReference("register_" + register.getName() + "_base", register.getBase());
    }

    // Transform register rules
    if (register.getRules() != null && !register.getRules().isEmpty()) {
      List<Match> ruleMatches = new ArrayList<>();

      for (Rule rule : register.getRules()) {
        Match ruleMatch = transformRegisterRule(rule, context);
        if (ruleMatch != null) {
          ruleMatches.add(ruleMatch);
        }
      }

      // Add rules to register match
      if (!ruleMatches.isEmpty()) {
        for (Match ruleMatch : ruleMatches) {
          registerMatch.addAnd(ruleMatch);
        }
      }
    }

    return registerMatch;
  }

  /**
   * Transforms a Rule within a Register into a Match object.
   *
   * @param rule QOF Rule to transform
   * @param context transformation context
   * @return Match object representing the rule
   */
  private Match transformRegisterRule(Rule rule, TransformationContext context) {
    if (rule == null) {
      return null;
    }

    Match ruleMatch = new Match();

    // Set rule number if available
    if (rule.getRule() > 0) {
      ruleMatch.setRuleNumber(rule.getRule());
      transformationLogger.debug("Processing rule number: {}", rule.getRule());
    }

    // Set rule description
    if (rule.getDescription() != null && !rule.getDescription().isBlank()) {
      ruleMatch.setDescription(rule.getDescription());
    }

    // Handle logic expressions
    if (rule.getLogic() != null && !rule.getLogic().isBlank()) {
      transformationLogger.debug("Processing rule logic: {}", rule.getLogic());
      ruleMatch.setName("Rule " + rule.getRule() + ": " + rule.getLogic());
    }

    // Handle conditional branches
    if (rule.getIfTrue() != null && !rule.getIfTrue().isBlank()) {
      transformationLogger.debug("Rule true action: {}", rule.getIfTrue());
    }
    if (rule.getIfFalse() != null && !rule.getIfFalse().isBlank()) {
      transformationLogger.debug("Rule false action: {}", rule.getIfFalse());
    }

    return ruleMatch;
  }

  /**
   * Validates that all registers have required fields for successful transformation.
   *
   * @param registers list of registers to validate
   * @return true if all registers are valid, false otherwise
   */
  public boolean validateRegisters(List<Register> registers) {
    if (registers == null || registers.isEmpty()) {
      transformationLogger.warn("No registers to validate");
      return true;
    }

    for (Register register : registers) {
      if (register.getName() == null || register.getName().isBlank()) {
        transformationLogger.error("Register has no name");
        return false;
      }
    }

    return true;
  }
}