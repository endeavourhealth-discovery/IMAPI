Feature: IMQ to SQL conversion
  @IMQTest
  Scenario Outline: IMQ converts to SQL without errors
    When IMQ to SQL conversion is executed for <iri>
    Then SQL should be generated successfully

  Examples:
    | iri |
    | "http://endhealth.info/im#Q_RegisteredGMS" |
    | "http://endhealth.info/im#Q_TestQuery" |
    | "http://smartlifehealth.info/smh#931e901b-8a0f-41b9-8064-beb3c4e2c2e4" |
    | "http://smartlifehealth.info/smh#74198a12-677c-4614-b806-fda7bd754928" |
    | "http://smartlifehealth.info/smh#bb5b362f-fdd4-46f5-9f8d-ead00b14ac18" |
    | "http://smartlifehealth.info/smh#44103a56-4adf-493d-b73f-bf9df0ad36ba" |
    | "http://smartlifehealth.info/smh#e736c24b-77bf-4f01-8a4f-b7b996cac424" |
    | "http://smartlifehealth.info/smh#7c420252-4e06-4b12-81d6-a7ec6d125c10" |
    | "http://smartlifehealth.info/smh#eba32279-45ec-4abb-90b6-10af84b5875e" |
    | "http://smartlifehealth.info/smh#d74bcda7-d2f0-4060-97ef-2086eb1cc5b9" |
    | "http://smartlifehealth.info/smh#2dc54f28-3055-4db3-b31a-e79e5981fbd0" |
    | "http://smartlifehealth.info/smh#59812b33-b915-4293-8bb0-66167eb90190" |
    | "http://smartlifehealth.info/smh#c56bc95d-3237-42f8-903c-1d3517681e05" |
    | "http://smartlifehealth.info/smh#441fa4d1-3160-4b9d-88e6-02f914c1fe83" |
    | "http://smartlifehealth.info/smh#c8c7a8af-4c2e-401c-a62d-20f28e0ee308" |
    | "http://smartlifehealth.info/smh#f0db5915-dc2b-45cc-a9c1-8659b651dcf1" |
    | "http://smartlifehealth.info/smh#e74f604f-14e1-4e1c-a09f-6405ff03efd7" |
    | "http://smartlifehealth.info/smh#3f524680-7813-4443-ab10-c21e282a0847" |
    | "http://smartlifehealth.info/smh#4d4a12d2-4691-4b9f-af59-7ca5227f53a0" |
    | "http://smartlifehealth.info/smh#7c6875fb-5f08-4ab2-a3cf-03b2574640d0" |
    | "http://smartlifehealth.info/smh#5ab3f776-9072-497d-9e5f-8e7b3e2ad3b4" |