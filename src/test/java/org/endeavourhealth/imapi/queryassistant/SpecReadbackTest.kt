package org.endeavourhealth.imapi.queryassistant

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.endeavourhealth.imapi.model.imq.Operator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SpecReadbackTest {

  @Test
  fun rendersAsthmaRegister() {
    val spec = jacksonObjectMapper().readValue<CohortSpec>(javaClass.getResource("/queryassistant/ast_reg.spec.json")!!.readText())

    assertEquals(
      """
      AST_REG - Patients on the asthma register
      Population: patient

      Rule 1: in the cohort "Registered and GP for GMS services on the reference date"
        If true: go to the next rule. If false: exclude.

      Rule 2: meets all of:
          - has clinical entry records where concept is one of [ASTRES_COD (set), AST_COD (set)] and effective date <= the search date
            take the latest record per patient (called "Latest_ASTRES_COD_AST_COD_lte")
            and that must have concept is one of [AST_COD (set)] and effective date <= the search date
          - has medication request records where concept is one of [ASTTRT_COD (set)] and effective date > 12 months before the search date and effective date <= the search date
        If true: go to the next rule. If false: exclude.

      Rule 3: has patient records where age >= 5 years
        If true: include in the cohort. If false: exclude.
      """.trimIndent(),
      SpecReadback.render(spec),
    )
  }

  @Test
  fun rendersRelativeReferencesAndExclusions() {
    val f = FilterGroup(
      BoolOp.ANY, listOf(
        ValueFilter("im:effectiveDate", Operator.eq, relativeTo = RelativeTo(alias = "latestSystolic")),
        RangeFilter("im:value", Bound(Operator.gte, "1"), Bound(Operator.lte, "85")),
        ConceptFilter(concepts = listOf(ConceptRef("sct:1", "Diabetes", ConceptMatch.DESCENDANTS_OR_SELF), ConceptRef("sct:2", "Type 1", exclude = true))),
      )
    )
    assertEquals(
      "effective date = the effective date of \"latestSystolic\" or value >= 1 and <= 85" +
        " or concept is one of [Diabetes (and subtypes)] excluding [Type 1]",
      SpecReadback.filter(f),
    )
  }
}
