package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.vocabulary.IM

internal const val COHORT_DATA_MODEL_IRI = "http://endhealth.info/im#Cohort"
internal const val ENTITY_ID_FIELD = "entity_id"
internal const val PATIENT_ID_FIELD = "patient_id"
internal const val ROW_NUMBER_ALIAS = "rn"
internal const val COUNT_ALIAS = "Count"

internal const val COHORT_RESULTS_TABLE = "dataset.cohort_results"
internal const val DATASET_RESULTS_TABLE = "dataset.dataset_results"
internal const val QUERY_RESULT_ID_FIELD = "query_result_id"

/** Aliases for the wrapping sub-selects the converter generates. */
internal const val SUBQUERY_ALIAS = "sq"
internal const val ROW_NUMBER_SOURCE_ALIAS = "u"
internal const val UNION_SOURCE_ALIAS = "t"

internal val COUNT_FUNCTION_IRI: String = IM.COUNT.toString()
internal val CONCATENATE_FUNCTION_IRI: String = IM.CONCATENATE.toString()
