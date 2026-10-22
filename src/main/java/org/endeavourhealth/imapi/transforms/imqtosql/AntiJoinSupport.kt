package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.model.sql.MySQLJoin
import org.endeavourhealth.imapi.model.sql.MySQLPropertyValueWhere
import org.endeavourhealth.imapi.model.sql.MySQLWith

/*
 * Shared pieces of the "keep rows of the previous step that have no match" pattern, used both by not-exists
 * matches and by not-exists order-by matches: the matching rows come from a sub-select aliased [SUBQUERY_ALIAS],
 * RIGHT JOINed back to the previous step, and the rows with no match are those where the sub-select key is NULL.
 */

private fun isCohortStep(previous: MySQLWith) = previous.table.dataModel == COHORT_DATA_MODEL_IRI

/** What an anti-join step selects: the previous step's rows, with cohort rows also exposing their entity id as patient id. */
internal fun carrySelectFrom(previous: MySQLWith): String =
  if (isCohortStep(previous)) "${previous.alias}.*, ${previous.alias}.$ENTITY_ID_FIELD as $PATIENT_ID_FIELD"
  else "${previous.alias}.*"

/** The entity key an anti-join step exposes, matching [carrySelectFrom]. */
internal fun carryEntityKeyFrom(previous: MySQLWith): String? =
  if (isCohortStep(previous)) PATIENT_ID_FIELD else previous.entityKeyField

/** RIGHT JOIN of the [SUBQUERY_ALIAS] sub-select back to [previous] on the given key columns. */
internal fun rightJoinSubqueryTo(previous: MySQLWith, subqueryKey: String, previousKey: String) = MySQLJoin(
  join = "RIGHT JOIN",
  tableFrom = SUBQUERY_ALIAS,
  tableTo = previous.alias,
  tableToAlias = previous.alias,
  fromProperty = subqueryKey,
  toProperty = previousKey,
  reference = true
)

/** Condition that the [SUBQUERY_ALIAS] sub-select found no match for a row of the previous step. */
internal fun subqueryKeyIsNull(subqueryKey: String) =
  MySQLPropertyValueWhere(subqueryKey, "IS", "NULL", table = SUBQUERY_ALIAS)
