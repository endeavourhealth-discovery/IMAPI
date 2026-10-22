package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.sql.quoteSqlString
import org.endeavourhealth.imapi.vocabulary.IM
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Stateless SQL literal/unit conversion helpers used by the IMQ-to-SQL conversion pipeline.
 */
object SqlLiteralUtils {
  private val DATE_FORMATS = listOf(
    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
    DateTimeFormatter.ofPattern("dd/MM/yyyy"),
    DateTimeFormatter.ofPattern("MM/dd/yyyy"),
    DateTimeFormatter.ofPattern("dd-MM-yyyy"),
    DateTimeFormatter.ofPattern("MM-dd-yyyy"),
    DateTimeFormatter.ofPattern("d/M/yyyy"),
    DateTimeFormatter.ofPattern("yyyy/MM/dd"),
  )
  private val SQL_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  private val NUMBER_PATTERN = Regex("-?(0|[1-9]\\d*)(\\.\\d+)?")
  private val DATE_SHAPE = Regex("\\d{1,4}[/-]\\d{1,2}[/-]\\d{1,4}")

  /** Formats are tried in order, so ambiguous values such as 01-02-2020 are read day-first. */
  private fun tryParseDate(value: String): LocalDate? {
    if (!DATE_SHAPE.matches(value)) return null
    for (formatter in DATE_FORMATS) {
      try {
        return LocalDate.parse(value, formatter)
      } catch (_: DateTimeParseException) {
        continue
      }
    }
    return null
  }

  /**
   * Renders [value] as a SQL literal: plain numbers unquoted, dates normalised to 'yyyy-MM-dd', anything else
   * a quoted, escaped string. Numeric-looking codes with leading zeros (e.g. "0123") stay strings.
   */
  fun toSqlLiteral(value: String): String {
    if (NUMBER_PATTERN.matches(value)) return value

    val date = tryParseDate(value)
    if (date != null) return "'${date.format(SQL_DATE_FORMAT)}'"

    return quoteSqlString(value)
  }

  private val QUERY_IRI_TOKEN = Regex("[A-Za-z0-9:/#._~%@+,-]+")

  /**
   * A query IRI as it must appear in generated SQL: bare and unquoted. The query runner replaces each query IRI in
   * the SQL with a bound parameter holding that query's numeric query_result_id, so quoting it would turn the
   * parameter into the literal text '?'. Because it is spliced in unquoted, anything outside a safe character set
   * is rejected rather than escaped.
   */
  fun queryResultIdToken(iri: String): String {
    if (!QUERY_IRI_TOKEN.matches(iri)) throw SQLConversionException("Query iri contains characters that cannot be used in SQL: $iri")
    return iri
  }

  /** For values spliced into SQL as numbers (e.g. INTERVAL magnitudes): rejects anything that is not a number. */
  fun requireNumber(value: String): String {
    if (value.trim().toBigDecimalOrNull() == null) throw SQLConversionException("Expected a numeric value but got: $value")
    return value
  }

  fun getUnitNameAndType(iri: String): Pair<String, String> {
    return when (IM.from(iri)) {
      IM.YEARS -> "YEAR" to "Unit"
      IM.YEAR -> "YEAR" to "Qualifier"
      IM.MONTHS -> "MONTH" to "Unit"
      IM.MONTH -> "MONTH" to "Qualifier"
      IM.DAYS -> "DAY" to "Unit"
      IM.DAY -> "DAY" to "Qualifier"
      IM.HOURS -> "HOUR" to "Unit"
      IM.MINUTES -> "MINUTE" to "Unit"
      IM.SECONDS -> "SECOND" to "Unit"
      IM.FISCAL_YEAR -> "FISCAL_YEAR" to "Qualifier"
      IM.QUARTER -> "QUARTER" to "Qualifier"
      else -> throw SQLConversionException("No unit name found for $iri")
    }
  }
}
