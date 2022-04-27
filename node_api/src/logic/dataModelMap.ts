export const dataModelMap = {
  "http://endhealth.info/im#ValueSet" : {
    name: "value_set",
    pk: "id",
    fields: {
      "iri": "iri"
    }
  },
  "http://endhealth.info/im#ValueSetMember" : {
    name: "value_set_member",
    pk: "id",
  },
  "http://endhealth.info/im#Patient" : {
    name: "patient",
    pk: "id",
  },
  "http://endhealth.info/im#GPRegistration": {
    name: "registration",
    pk: "id",
    fields: {
      "http://endhealth.info/im#patientType": "patient_type",
      "http://endhealth.info/im#effectiveDate": "effective_date",
      "http://endhealth.info/im#isSubjectOf": "patient_id",
      "http://endhealth.info/im#endDate": "registration_end"
    }
  },
};
