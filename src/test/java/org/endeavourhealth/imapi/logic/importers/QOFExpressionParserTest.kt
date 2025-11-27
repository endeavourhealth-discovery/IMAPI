package org.endeavourhealth.imapi.logic.importers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;


class QOFExpressionParserTest {

    @Test
    fun compare() {
      val input = "If (If PATY1_AGE < 80 years AND  If PATY2_AGE = 80 years)  OR  (If PATY1_AGE < 81 years AND  If PATY2_AGE = 81 years AND  If SHVACGP1_DAT <= (PPED – 12 months) AND (If SHVACGP2_DAT > (PPED – 12 months) OR If SHVACGP2_DAT = Null)) THEN Select ELSE Reject"

      println("Input expression:")
      println(input)
      println("\nParsed hierarchy:")

      QOFExpressionParser(input).parse()
    }
}