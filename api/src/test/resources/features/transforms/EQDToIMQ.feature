Feature: EQDToIMQ

  Scenario Outline: EQD can be converted to IM Query and back
    When EDQ document <EqdIn> is passed into transformEdq
    Then the output should be <EdqOut>
    Examples:
      | EdqOut
      | {
      | "entities": [
      | {
      | "@id": "9f0f309e-6fe4-48cc-ad2f-056c7581a6cf",
      | "http://www.w3.org/1999/02/22-rdf-syntax-ns#type": [
      | {
      | "@id": "http://endhealth.info/im#Folder"
      | }
      | ],
      | "http://www.w3.org/2000/01/rdf-schema#label": "Palliative Care*",
      | "http://endhealth.info/im#isContainedIn": [
      | {
      | "@id": "0155363a-7cf7-470c-ac1c-8d83e7cb616c"
      | }
      | ]
      | }
      | ]
      | }
