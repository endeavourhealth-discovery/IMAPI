package org.endeavourhealth.imapi.queryassistant

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.endeavourhealth.imapi.model.imq.Operator
import org.endeavourhealth.imapi.model.imq.Order
import org.endeavourhealth.imapi.model.imq.RuleAction
import org.endeavourhealth.imapi.vocabulary.NAMESPACE

/*
 * QuerySpec: a compact, LLM-friendly description of a cohort query.
 *
 * It covers the patterns used by the QOF / SMH cohort definitions while hiding IMQ boilerplate
 * (compare left/right, orderBy partitions, cohort flags, then/from wiring, unit IRIs).
 * QuerySpecCompiler turns it into an IMQ Query; QuerySpecDecompiler goes the other way.
 *
 * IRIs may be prefixed (im:, sct:, qof:, smh:, ... see IriPrefixes) or full.
 */

private val IM = NAMESPACE.IM.toString()

internal const val IM_PATIENT = "im:Patient"
internal const val IM_EFFECTIVE_DATE = "im:effectiveDate"
internal const val IM_CONCEPT = "im:concept"

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
data class CohortSpec(
  val name: String? = null,
  val description: String? = null,
  val iri: String? = null,
  /** The population type, nearly always im:Patient. */
  val baseType: String = IM_PATIENT,
  /** Ordered rule chain; each rule decides NEXT / SELECT / REJECT. Mutually exclusive with [allOf]. */
  val rules: List<RuleSpec>? = null,
  /** Plain conjunction of criteria (no rule chain). Mutually exclusive with [rules]. */
  val allOf: List<Criterion>? = null,
)

data class RuleSpec(
  val match: Criterion,
  val ifTrue: RuleAction,
  val ifFalse: RuleAction,
)

// ---------------------------------------------------------------- criteria (what to match)

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "kind")
@JsonSubTypes(
  JsonSubTypes.Type(CohortRef::class, name = "cohort"),
  JsonSubTypes.Type(Records::class, name = "records"),
  JsonSubTypes.Type(Group::class, name = "group"),
)
sealed interface Criterion

/** Membership of an existing cohort/query, e.g. "registered for GMS". Prefer reuse over rebuilding criteria. */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
data class CohortRef(
  val iri: String,
  val name: String? = null,
) : Criterion

/** Records of [entity] (e.g. im:ClinicalEntry, im:MedicationRequest, im:Patient) that satisfy [filter]. */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
data class Records(
  val entity: String,
  val filter: Filter? = null,
  /** Keep only the latest/earliest N records per patient before applying [then]. */
  val pick: Pick? = null,
  /** Name for this result set, so [then] or other filters' [RelativeTo.alias] can refer to it. */
  val alias: String? = null,
  /** Properties exposed for later comparison (IMQ return). */
  val exposes: List<Exposed>? = null,
  /** Further test applied to the picked records, e.g. "the latest BP reading is < 140". */
  val then: ThenTest? = null,
  /** True means the patient must NOT have such records. */
  val exclude: Boolean = false,
) : Criterion

/** Boolean combination of criteria. */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
data class Group(
  val operator: BoolOp,
  val items: List<Criterion>,
  /** Condition relating the items, e.g. "same effective date as <alias>". */
  val filter: Filter? = null,
  val pick: Pick? = null,
  val alias: String? = null,
  val then: ThenTest? = null,
  val exclude: Boolean = false,
) : Criterion

enum class BoolOp { ALL, ANY }

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
data class Pick(
  /** descending = latest / highest first, ascending = earliest / lowest first. */
  val direction: Order,
  val count: Int = 1,
  val orderBy: String = IM_EFFECTIVE_DATE,
  val partitionBy: List<String> = listOf("im:patient"),
)

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
data class ThenTest(
  val filter: Filter,
  val alias: String? = null,
)

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
data class Exposed(
  val property: String,
  val alias: String,
  val name: String? = null,
  /** Node variable the returned property belongs to (IMQ Return.nodeRef); not a result-set alias. */
  val nodeRef: String? = null,
)

// ---------------------------------------------------------------- filters (conditions on a record)

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "kind")
@JsonSubTypes(
  JsonSubTypes.Type(FilterGroup::class, name = "group"),
  JsonSubTypes.Type(ConceptFilter::class, name = "concept"),
  JsonSubTypes.Type(ValueFilter::class, name = "value"),
  JsonSubTypes.Type(RangeFilter::class, name = "range"),
)
sealed interface Filter

data class FilterGroup(
  val operator: BoolOp,
  val items: List<Filter>,
) : Filter

/** [property] (usually im:concept) is one of [concepts]. */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
data class ConceptFilter(
  val property: String = IM_CONCEPT,
  val concepts: List<ConceptRef>,
  val negate: Boolean = false,
  /** Display labels carried through to IMQ (valueLabel / shortLabel). */
  val label: String? = null,
  val shortLabel: String? = null,
) : Filter

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
data class ConceptRef(
  val iri: String,
  val name: String? = null,
  val match: ConceptMatch = ConceptMatch.SELF,
  val exclude: Boolean = false,
)

enum class ConceptMatch(val memberOf: Boolean, val descendantsOrSelfOf: Boolean) {
  SELF(false, false),
  DESCENDANTS_OR_SELF(false, true),
  /** Members of a value set / refset. */
  SET_MEMBERS(true, false),
  SET_MEMBERS_DESCENDANTS_OR_SELF(true, true);

  companion object {
    fun of(memberOf: Boolean, descendantsOrSelfOf: Boolean) =
      entries.first { it.memberOf == memberOf && it.descendantsOrSelfOf == descendantsOrSelfOf }
  }
}

/**
 * [property] [operator] [value] [units], optionally relative to [relativeTo].
 * e.g. effectiveDate > -12 MONTHS relative to $searchDate  ==  "in the 12 months before the search date".
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
data class ValueFilter(
  val property: String,
  val operator: Operator,
  val value: String? = null,
  val units: TimeUnit? = null,
  /** Calendar-period comparisons, e.g. qualifier MONTH + valueTerm "last". */
  val qualifier: PeriodQualifier? = null,
  val valueTerm: String? = null,
  val relativeTo: RelativeTo? = null,
) : Filter

/** [property] between [from] and [to] (each bound has its own operator), optionally relative to [relativeTo]. */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
data class RangeFilter(
  val property: String,
  val from: Bound,
  val to: Bound,
  val relativeTo: RelativeTo? = null,
) : Filter

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
data class Bound(
  val operator: Operator,
  /** Omitted for an open bound at the reference point itself, e.g. "<= $searchDate". */
  val value: String? = null,
  val units: TimeUnit? = null,
)

/**
 * What a filter's value is relative to: either a query parameter (e.g. $searchDate)
 * or a property of another aliased result set (e.g. the date of the latest BP reading).
 * Exactly one of [parameter] / [alias] must be set.
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
data class RelativeTo(
  val parameter: String? = null,
  val alias: String? = null,
  /** Property on the aliased result set; defaults to the filter's own property. */
  val property: String? = null,
)

enum class TimeUnit(val label: String) {
  DAYS("Days"), MONTHS("Months"), YEARS("Years");

  val iri: String get() = IM + label
}

enum class PeriodQualifier(val local: String, val label: String) {
  MONTH("Month", "Month"), QUARTER("Quarter", "Quarter"), FISCAL_YEAR("FiscalYear", "Fiscal Year");

  val iri: String get() = IM + local
}
