import {Table} from '../model/sql/Table';
import {Join} from '../model/sql/Join';

export const dataModelMap = {
  // IMv1 Tables
  "http://endhealth.info/im#ValueSet" : {
    name: "value_set",
    fields: {
      pk: "dbid",
      "iri": "id"
    }
  },
  "http://endhealth.info/im#ValueSetMember" : {
    name: "value_set_member",
    fields: {
      pk: "dbid",
      "value_set": "value_set",
      "member": "concept"
    }
  },
  "http://endhealth.info/im#concept" : {
    name: "concept",
    fields: {
      pk: "dbid",
      "dbid": "dbid",
      "iri": "id"
    }
  },
  "http://endhealth.info/im#conceptTct" : {
    name: "concept_tct",
    fields: {
      pk: "iri",
      "iri": "iri",
      "child": "child"
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

  // Clinical tables
  "http://endhealth.info/im#Person" : {
    name: "patient",
    fields: {
      pk: "id",
      "http://endhealth.info/im#gpPatientType": "id",   // TODO: Needs to be patient_type function!?
      "http://endhealth.info/im#dateOfBirth": "date_of_birth",
      "http://endhealth.info/im#age": "date_of_birth"
    },
    joins: {
      "http://endhealth.info/im#isSubjectOf": {
        "http://endhealth.info/im#GPRegistration": "{child}.patient_id = {parent}.id",
        "http://endhealth.info/im#Observation": "{child}.patient_id = {parent}.id"
      }
    }
  },
  "http://endhealth.info/im#GPRegistration": {
    name: "registration_status_history",
    pk: "id",
    fields: {
      "http://endhealth.info/im#patientType": "registration_status_concept_id",
      "http://endhealth.info/im#effectiveDate": "start_date",
      "http://endhealth.info/im#endDate": "end_date"
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
};
