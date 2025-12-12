Feature: Set name generator

  Scenario Outline: Use AI to generate names for sets, given their members terms
    When I have a concept set <iri>
    And I get its members
    When I call AI
    Then I should get a sensible name

    Examples:
      | iri |
      | "http://smartlifehealth.info/smh#123bd04c-52b4-4311-ad5d-1253f259baa0" |
