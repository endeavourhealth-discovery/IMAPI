package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.imq.Match;
import org.junit.jupiter.api.Test;

public class IMQToDSLTest {
  @Test
  public void generateToken() throws JsonProcessingException {
    Match match= getMatch();
    String term= IMQTermGenerator.generateMeaningfulTerm(match);
    System.out.println(term);
  }

  private Match getMatch() throws JsonProcessingException {
    String json= """
      {		 "name": "Medication Issues of Direct oral anticoagulant in last 6 months",
      		 "description": "Medication Issues of Direct oral anticoagulant in last 6 months",
      		 "path": [
      			{
      				 "iri": "http://endhealth.info/im#prescription",
      				 "variable": "MedicationRequest",
      				 "name": "prescriptions",
      				 "typeOf": {
      					 "iri": "http://endhealth.info/im#MedicationRequest"
      				}
      			}
      		],
      		 "where": {
      			 "uuid": "7fb2e3a8-9e99-4d66-a1c4-ac2fd55f7d72",
      			 "and": [
      				{
      					 "iri": "http://endhealth.info/im#concept",
      					 "name": "concept",
      					 "uuid": "f8510fa0-4d26-4b6c-b9e6-a748f0262127",
      					 "nodeRef": "MedicationRequest",
      					 "is": [
      						{
      							 "iri": "http://endhealth.info/qof#361238CD-A7C3-4C27-98CF-D27B08FCB3AF",
      							 "name": "DIRECTORANTICOAGDRUG_COD",
      							 "description": " ",
      							 "memberOf": true
      						}
      					],
      					 "valueLabel": "  DIRECTORANTICOAGDRUG_COD",
      					 "shortLabel": "DIRECTORANTICOAGDRUG_COD"
      				},
      				{
      					 "description": "is within ",
      					 "iri": "http://endhealth.info/im#effectiveDate",
      					 "name": "date",
      					 "operator": ">",
      					 "value": -6,
      					 "relativeTo": {
      						 "description": "search date",
      						 "parameter": "$searchDate"
      					},
      					 "uuid": "f9b208c7-242f-435a-99f7-875250e6e7a1",
      					 "nodeRef": "MedicationRequest",
      					 "valueLabel": "6 Months before the ",
      					 "units": {
      						 "name": "Months",
      						 "iri": "http://endhealth.info/im#Months"
      					},
      					 "function": {
      						 "iri": "http://endhealth.info/im#TimeDifference",
      						 "argument": [
      							{
      								 "parameter": "firstDateTime",
      								 "valuePath": {
      									 "iri": "http://endhealth.info/im#effectiveDate"
      								}
      							},
      							{
      								 "parameter": "relativeTo",
      								 "valueParameter": "$searchDate"
      							},
      							{
      								 "parameter": "units",
      								 "valueIri": {
      									 "name": "Months",
      									 "iri": "http://endhealth.info/im#Months"
      								}
      							}
      						]
      					}
      				},
      				{
      					 "description": "on or before ",
      					 "iri": "http://endhealth.info/im#effectiveDate",
      					 "name": "date",
      					 "operator": "<=",
      					 "relativeTo": {
      						 "description": "achievement date",
      						 "parameter": "$achievementDate"
      					},
      					 "uuid": "422cdafd-a63b-4a68-8033-746b38058396",
      					 "nodeRef": "MedicationRequest",
      					 "valueLabel": ""
      				}
      			]
      		},
      		 "uuid": "65b8fd88-c890-498b-8fa5-08273e6c3e1c",
      		 "keepAs": "Match_598",
      		 "orderBy": {
      			 "property": [
      				{
      					 "iri": "http://endhealth.info/im#effectiveDate",
      					 "nodeRef": "MedicationRequest",
      					 "direction": "descending"
      				}
      			],
      			 "limit": 1,
      			 "description": "latest "
      		}
      	}
      
      """;
    return new ObjectMapper().readValue(json,Match.class);

  }
}
