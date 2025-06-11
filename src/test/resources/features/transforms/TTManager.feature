Feature: TTManager

  Scenario: Entity has a term code with a matching label
    Given an entity
    And it has a list of term codes
    And there is a term code with label "Test"
    Then termUsed("Test") should return true

  Scenario: Entity has a term code without a label
    Given an entity
    And it has a list of term codes
    And there is a term code with no label
    Then termUsed("Test") should return false
