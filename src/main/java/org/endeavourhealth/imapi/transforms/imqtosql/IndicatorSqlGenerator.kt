package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.errorhandling.SQLConversionException

internal object IndicatorSqlGenerator {
  fun generate(denominator: String?, numerator: String?, dataset: String?): String {
    if (denominator == null || numerator == null || dataset == null) {
      throw SQLConversionException("Missing denominator, numerator or dataset")
    }
    val numeratorLiteral = SqlLiteralUtils.toSqlLiteral(numerator)
    val datasetLiteral = SqlLiteralUtils.toSqlLiteral(dataset)
    val denominatorLiteral = SqlLiteralUtils.toSqlLiteral(denominator)
    val indSql = """
      SELECT c.entity_id, !ISNULL(n.entity_id) as "Yes/No", d.json
      FROM $COHORT_RESULTS_TABLE c
      LEFT JOIN $COHORT_RESULTS_TABLE n ON n.entity_id = c.entity_id AND n.query_result_id = $numeratorLiteral
      LEFT JOIN $DATASET_RESULTS_TABLE d ON d.entity_id = c.entity_id AND d.query_result_id = $datasetLiteral
      WHERE c.query_result_id = $denominatorLiteral;
    """.trimIndent()
    return indSql
  }
}
