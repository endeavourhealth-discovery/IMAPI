import {Table} from '../model/sql/Table';
import {Join} from '../model/sql/Join';

export const dataModelMap = {
  // IMv1 Tables
  "http://endhealth.info/im#ValueSet" : {
    name: "value_set",
    fields: {
      pk: "id",
      "iri": "iri"
    }
  },
  "http://endhealth.info/im#ValueSetMember" : {
    name: "value_set_member",
    fields: {
      pk: "id",
      "value_set": "value_set",
      "member": "member"
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
      "http://endhealth.info/im#gpPatientType": "patient_type",
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
      "http://endhealth.info/im#concept": "core_concept_id",
    }
  },
};

/*
export function getTable(entityTypeId: string, alias: string): Table {
  if (!entityTypeId)
    throw "No entity type provided";

  if (!dataModelMap[entityTypeId])
    throw "Entity [" + entityTypeId + "] does not exist in map";

  const table = JSON.parse(JSON.stringify(dataModelMap[entityTypeId]));
  table.alias = alias;
  table.id = entityTypeId;

  return table;
}

export function getField(table: Table, fieldId: string): string {
  if (!table.fields[fieldId])
    throw "Table [" + table.name + "] does not contain field [" + fieldId + "]";

  return table.alias + "." + table.fields[fieldId];
}

export function getJoin(parent: Table, relationshipId: string, childId: string, alias: string): Join {
  if (!parent.joins[relationshipId])
    throw "Table [" + parent.name + "] does not have relationship [" + relationshipId + "]";

  if (!parent.joins[relationshipId][childId])
    throw "Table [" + parent.name + "] does not have relationship [" + relationshipId + "] to child table [" + childId + "]";

  const join: Join = new Join();
  join.table = getTable(childId, alias);
  join.on = parent.joins[relationshipId][childId];

  join.on = join.on
    .replace("{child}", join.table.alias)
    .replace("{parent}", parent.alias);

  return join;
}
*/
