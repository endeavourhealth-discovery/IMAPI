package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.sql.MySQLWith
import org.endeavourhealth.imapi.model.sql.quoteIdentifier
import org.endeavourhealth.imapi.model.sql.quoteSqlString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.Locale

class SqlSafetyTest {
  @Test
  fun numbersStayUnquoted() {
    assertEquals("42", SqlLiteralUtils.toSqlLiteral("42"))
    assertEquals("-3.5", SqlLiteralUtils.toSqlLiteral("-3.5"))
    assertEquals("0", SqlLiteralUtils.toSqlLiteral("0"))
  }

  @Test
  fun numericLookingCodesAreQuoted() {
    assertEquals("'0123'", SqlLiteralUtils.toSqlLiteral("0123"))
    assertEquals("'1e5'", SqlLiteralUtils.toSqlLiteral("1e5"))
  }

  @Test
  fun quotesAndBackslashesAreEscaped() {
    assertEquals("'O''Brien'", SqlLiteralUtils.toSqlLiteral("O'Brien"))
    assertEquals("'a\\\\b'", SqlLiteralUtils.toSqlLiteral("a\\b"))
    assertEquals("'x\\\\'' OR 1=1 --'", SqlLiteralUtils.toSqlLiteral("x\\' OR 1=1 --"))
  }

  @Test
  fun datesAreNormalised() {
    assertEquals("'2020-01-31'", SqlLiteralUtils.toSqlLiteral("2020-01-31"))
    assertEquals("'2020-01-31'", SqlLiteralUtils.toSqlLiteral("31/01/2020"))
    assertEquals("'31/13/2020'", SqlLiteralUtils.toSqlLiteral("31/13/2020"))
  }

  @Test
  fun requireNumberRejectsSqlFragments() {
    assertEquals("-5", SqlLiteralUtils.requireNumber("-5"))
    assertThrows(SQLConversionException::class.java) { SqlLiteralUtils.requireNumber("1 DAY) OR (1=1") }
  }

  @Test
  fun queryIrisStayBareForTheQueryRunner() {
    assertEquals("http://endhealth.info/im#Q_RegisteredGMS", SqlLiteralUtils.queryResultIdToken("http://endhealth.info/im#Q_RegisteredGMS"))
    assertEquals(
      "http://endhealth.info/qof#36e70531-462a-427a-8ee2-eeb584018cc9",
      SqlLiteralUtils.queryResultIdToken("http://endhealth.info/qof#36e70531-462a-427a-8ee2-eeb584018cc9")
    )
  }

  @Test
  fun queryIriWithSqlMetaCharactersIsRejected() {
    assertThrows(SQLConversionException::class.java) { SqlLiteralUtils.queryResultIdToken("http://x#a' OR '1'='1") }
    assertThrows(SQLConversionException::class.java) { SqlLiteralUtils.queryResultIdToken("http://x#a; DROP TABLE t") }
    assertThrows(SQLConversionException::class.java) { SqlLiteralUtils.queryResultIdToken("http://x#a?") }
  }

  @Test
  fun identifiersAndStringsAreQuoted() {
    assertEquals("`a``b`", quoteIdentifier("a`b"))
    assertEquals("'it''s'", quoteSqlString("it's"))
  }

  @Test
  fun duplicateAsIsRejectedAndAliasesAreUnique() {
    val aliases = AliasAllocator()
    assertEquals("`a`", aliases.useAs("a"))
    assertThrows(SQLConversionException::class.java) { aliases.useAs("A") }
    assertEquals("`b`", aliases.ensureUniqueAlias("b"))
    assertEquals("`b_2`", aliases.ensureUniqueAlias("b"))
  }

  @Test
  fun overlongAliasesAreReplaced() {
    val alias = AliasAllocator().ensureUniqueAlias("x".repeat(80))
    assertEquals("`cte_1`", alias)
  }

  @Test
  fun aliasesIgnoreDefaultLocale() {
    val original = Locale.getDefault()
    try {
      Locale.setDefault(Locale.forLanguageTag("tr-TR"))
      assertEquals("title", AliasAllocator().sanitiseAlias("TITLE"))
      assertEquals("title", AliasAllocator().cteNormalise("TITLE"))
    } finally {
      Locale.setDefault(original)
    }
  }

  @Test
  fun lastIncludedThrowsWhenNothingToSelectFrom() {
    assertThrows(SQLConversionException::class.java) { emptyList<MySQLWith>().lastIncluded() }
    assertThrows(SQLConversionException::class.java) { listOf(MySQLWith(exclude = true)).lastIncluded() }
  }
}
