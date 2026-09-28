package org.endeavourhealth.imapi.transforms

import org.endeavourhealth.imapi.errorhandling.SQLConversionException

internal object IndicatorSqlGenerator {
  fun generate(denominator: String?, numerator: String?, dataset: String?): String {
    if (denominator == null || numerator == null || dataset == null) {
      throw SQLConversionException("Missing denominator, numerator or dataset")
    }
    val indSql = """
      SELECT c.entity_id, !ISNULL(n.entity_id) as "Yes/No", d.json
      FROM dataset.cohort_results c
      LEFT JOIN dataset.cohort_results n ON n.entity_id = c.entity_id AND n.query_result_id = $numerator
      LEFT JOIN dataset.dataset_results d ON d.entity_id = c.entity_id AND d.query_result_id = $dataset
      WHERE c.query_result_id = $denominator;
    """.trimIndent()
    return indSql
  }
}
