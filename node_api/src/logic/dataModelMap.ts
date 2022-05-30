import {Table} from '../model/sql/Table';
import {Join} from '../model/sql/Join';

export const dataModelMap = {
  // IMv1 Tables
  "http://endhealth.info/im#ValueSet" : {
    name: "im_live.value_set",
    fields: {
      pk: "dbid",
      "iri": "id"
    }
  },
  "http://endhealth.info/im#ValueSetMember" : {
    name: "im_live.value_set_member",
    fields: {
      pk: "dbid",
      "value_set": "value_set",
      "member": "concept"
    }
  },
  "http://endhealth.info/im#concept" : {
    name: "im_live.concept",
    fields: {
      pk: "dbid",
      "dbid": "dbid",
      "iri": "id"
    }
  },
  "http://endhealth.info/im#conceptTct" : {
    name: "im_live.concept_tct",
    fields: {
      "target": "target",
      "property": "property",
      "source": "source"
    }
  },
  // Query result tables
  "http://endhealth.info/im#Q_RegisteredGMS" : {
    name: "IMQ_Q_RegisteredGMS",
    fields: {
      pk: "id"
    }
  },
  "urn:uuid:6d517466-813b-46a8-b848-aaf5a4fbdcbf" : {
    name: "IMQ_SMI_Population",
    fields: {
      pk: "id"
    }
  },
  "urn:uuid:7d9a0a98-4df8-4748-8940-061a5148293c" : {
    name: "IMQ_APL_CVD_Bleed",
    fields: {
      pk: "id"
    }
  },

  // Clinical tables
  "http://endhealth.info/im#Person" : {
    name: "person",
    fields: {
      pk: "id",
      "http://endhealth.info/im#gpPatientType": "function",           // TODO: Needs to be patient_type function!?
      "http://endhealth.info/im#gpRegistrationStatus": "function",   // TODO: Needs to be registrations_status function!?
      "http://endhealth.info/im#gpGMSRegistrationDate": "function",   // TODO: Needs to be registrations_status function!?
      "http://endhealth.info/im#dateOfBirth": "date_of_birth",
      "http://endhealth.info/im#age": "function"
    },
    joins: {
      "http://endhealth.info/im#isSubjectOf": {
        "http://endhealth.info/im#GPRegistration": "{child}.patient_id = {parent}.id",
        "http://endhealth.info/im#Observation": "{child}.patient_id = {parent}.id",
        "http://endhealth.info/im#MedicationRequest": "{child}.patient_id = {parent}.id"
      }
    }
  },
  "http://endhealth.info/im#GPRegistration": {
    name: "episode_of_care",
    pk: "id",
    fields: {
      "http://endhealth.info/im#isSubjectOf": "person_id",
      "http://endhealth.info/im#patientType": "registration_type_concept_id",
      "http://endhealth.info/im#effectiveDate": "date_registered",
      "http://endhealth.info/im#endDate": "date_registered_end"
    }
  },
  "http://endhealth.info/im#Observation": {
    name: "observation",
    pk: "id",
    fields: {
      "http://endhealth.info/im#effectiveDate": "effective_date",
      "http://endhealth.info/im#concept": "non_core_concept_id",
    }
  },
  "http://endhealth.info/im#MedicationRequest": {
    name: "medication_order",
    pk: "id",
    fields: {
      "http://endhealth.info/im#effectiveDate": "clinical_effective_date",
      "http://endhealth.info/im#concept": "non_core_concept_id",
      "http://endhealth.info/im#code": "non_core_concept_id",
    }
  }
};
