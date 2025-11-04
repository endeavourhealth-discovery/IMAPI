package org.endeavourhealth.imapi.transformation.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.qof.*;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.transformation.engine.QOFToIMQTransformer;
import org.endeavourhealth.imapi.transformation.output.QueryOutputWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Demonstrates Phase 4 QOF to IMQuery transformation with a complete example.
 * This example uses a simplified QOF Diabetes specification to show the transformation
 * process end-to-end.
 */
public class Phase4Demonstrator {

  /**
   * Create a sample QOF document for demonstration.
   */
  public static QOFDocument createSampleQOFDocument() {
    QOFDocument doc = new QOFDocument();
    doc.setName("Diabetes_Sample");

    // Create a Selection
    Selection selection = new Selection();
    selection.setName("GMS registration status");
    SelectionRule selRule = new SelectionRule();
    selRule.setLogic("(If REG_DAT ≠ Null AND If DEREG_DAT = Null)OR(If REG_DAT ≠ Null AND If DEREG_DAT > ACHV_DAT)");
    selRule.setIfTrue("Select");
    selRule.setIfFalse("Reject");
    selRule.setDescription("Select patients registered for GMS");
    selection.addRule(selRule);
    doc.getSelections().add(selection);

    // Create a Register
    Register register = new Register();
    register.setName("DM_REG");
    register.setDescription("Diabetes register: Patients aged at least 17 years old with unresolved diabetes");
    register.setBase("GMS registration status");

    Rule rule1 = new Rule();
    rule1.setRule(1);
    rule1.setLogic("If DMLAT_DAT ≠ Null AND If DMRES_DAT = Null");
    rule1.setIfTrue("Next rule");
    rule1.setIfFalse("Reject");
    rule1.setDescription("Check for diabetes diagnosis");
    register.addRule(rule1);

    Rule rule2 = new Rule();
    rule2.setRule(2);
    rule2.setLogic("If PAT_AGE < 17 years");
    rule2.setIfTrue("Reject");
    rule2.setIfFalse("Select");
    rule2.setDescription("Check age requirement");
    register.addRule(rule2);

    doc.getRegisters().add(register);

    // Create extraction fields
    ExtractionField field1 = new ExtractionField();
    field1.setField(1);
    field1.setName("PAT_ID");
    field1.setLogic("Unconditional");
    field1.setDescription("Patient ID");
    doc.getExtractionFields().add(field1);

    ExtractionField field2 = new ExtractionField();
    field2.setField(2);
    field2.setName("PAT_AGE");
    field2.setLogic("Unconditional at ACHV_DAT");
    field2.setDescription("Patient age");
    doc.getExtractionFields().add(field2);

    // Create an Indicator
    Indicator indicator = new Indicator();
    indicator.setName("DIABETES_MEASURE");
    indicator.setDescription("Diabetes quality measure");
    indicator.setBase("DM_REG");

    Rule indRule = new Rule();
    indRule.setRule(1);
    indRule.setLogic("If IFCCHBA_DAT <= ACHV_DAT");
    indRule.setIfTrue("Select");
    indRule.setIfFalse("Reject");
    indRule.setDescription("HbA1c testing");
    indicator.addRule(indRule);

    doc.getIndicators().add(indicator);

    return doc;
  }

  /**
   * Run the complete transformation demonstration.
   */
  public static void main(String[] args) throws Exception {
    System.out.println("=== Phase 4 QOF to IMQuery Transformation Demonstrator ===");
    System.out.println();

    // Step 1: Create sample QOF document
    System.out.println("Step 1: Creating sample QOF document...");
    QOFDocument qofDoc = createSampleQOFDocument();
    System.out.println("✓ Created QOF document: " + qofDoc.getName());
    System.out.println("  - Selections: " + qofDoc.getSelections().size());
    System.out.println("  - Registers: " + qofDoc.getRegisters().size());
    System.out.println("  - Indicators: " + qofDoc.getIndicators().size());
    System.out.println("  - Extraction Fields: " + qofDoc.getExtractionFields().size());
    System.out.println();

    // Step 2: Initialize transformer
    System.out.println("Step 2: Initializing transformer...");
    QOFToIMQTransformer transformer = new QOFToIMQTransformer();
    System.out.println("✓ Transformer initialized");
    System.out.println();

    // Step 3: Execute transformation
    System.out.println("Step 3: Executing transformation...");
    long startTime = System.currentTimeMillis();
    Map<String, Query> queries = transformer.transform(qofDoc);
    long duration = System.currentTimeMillis() - startTime;
    System.out.println("✓ Transformation completed in " + duration + "ms");
    System.out.println("  - Generated " + queries.size() + " queries");
    System.out.println();

    // Step 4: Display generated queries
    System.out.println("Step 4: Generated Queries:");
    for (Map.Entry<String, Query> entry : queries.entrySet()) {
      System.out.println("  - " + entry.getKey());
      Query query = entry.getValue();
      System.out.println("    IRI: " + query.getIri());
      System.out.println("    Name: " + query.getName());
      System.out.println("    Description: " + query.getDescription());
    }
    System.out.println();

    // Step 5: Write output
    System.out.println("Step 5: Writing output...");
    String outputDir = "./transformation-demo-output";
    new File(outputDir).mkdirs();
    QueryOutputWriter.writeQueries(queries, outputDir, QueryOutputWriter.OverwriteStrategy.ALLOW);
    System.out.println("✓ Output written to: " + outputDir);
    System.out.println();

    // Step 6: Write sample JSON
    System.out.println("Step 6: Sample Query Output:");
    if (!queries.isEmpty()) {
      Query firstQuery = queries.values().iterator().next();
      String jsonOutput = QueryOutputWriter.writeAsString(firstQuery);
      System.out.println(jsonOutput);
    }

    System.out.println();
    System.out.println("=== Demonstrator Complete ===");
  }
}