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

  /** For values spliced into SQL as numbers (e.g. INTERVAL magnitudes): rejects anything that is not a number. */
  fun requireNumber(value: String): String {
    if (value.trim().toBigDecimalOrNull() == null) throw SQLConversionException("Expected a numeric value but got: $value")
    return value
  }

  /** Whether a unit is a quantity to add/subtract (e.g. "5 days") or a calendar part to compare (e.g. "same day"). */
  enum class UnitKind { UNIT, QUALIFIER }

  data class UnitSpec(val sqlName: String, val kind: UnitKind) {
    /** The SQL interval unit when this is a quantity, else null. */
    val unitName: String? get() = sqlName.takeIf { kind == UnitKind.UNIT }

    /** The SQL calendar part when this is a qualifier, else null. */
    val qualifierName: String? get() = sqlName.takeIf { kind == UnitKind.QUALIFIER }
  }

  fun getUnitNameAndType(iri: String): UnitSpec {
    return when (IM.from(iri)) {
      IM.YEARS -> UnitSpec("YEAR", UnitKind.UNIT)
      IM.YEAR -> UnitSpec("YEAR", UnitKind.QUALIFIER)
      IM.MONTHS -> UnitSpec("MONTH", UnitKind.UNIT)
      IM.MONTH -> UnitSpec("MONTH", UnitKind.QUALIFIER)
      IM.DAYS -> UnitSpec("DAY", UnitKind.UNIT)
      IM.DAY -> UnitSpec("DAY", UnitKind.QUALIFIER)
      IM.HOURS -> UnitSpec("HOUR", UnitKind.UNIT)
      IM.MINUTES -> UnitSpec("MINUTE", UnitKind.UNIT)
      IM.SECONDS -> UnitSpec("SECOND", UnitKind.UNIT)
      IM.FISCAL_YEAR -> UnitSpec("FISCAL_YEAR", UnitKind.QUALIFIER)
      IM.QUARTER -> UnitSpec("QUARTER", UnitKind.QUALIFIER)
      else -> throw SQLConversionException("No unit name found for $iri")
    }
  }
}
