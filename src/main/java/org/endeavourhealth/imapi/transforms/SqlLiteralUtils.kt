package org.endeavourhealth.imapi.transforms

import org.endeavourhealth.imapi.errorhandling.SQLConversionException
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

  private fun tryParseDate(value: String): LocalDate? {
    for (formatter in DATE_FORMATS) {
      try {
        return LocalDate.parse(value, formatter)
      } catch (_: DateTimeParseException) {
        continue
      }
    }
    return null
  }

  fun toSqlLiteral(value: String): String {
    if (value.toBigDecimalOrNull() != null) return value

    val date = tryParseDate(value)
    if (date != null) return "'${date.format(SQL_DATE_FORMAT)}'"

    return "'${value.replace("'", "''")}'"
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
