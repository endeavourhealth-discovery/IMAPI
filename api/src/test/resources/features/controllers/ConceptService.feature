Feature: Concept Service

  Scenario: Code terms retrieved in correct order
    Given a Diabetes entity with out-of-order term codes
    When the entity term codes are retrieved
    Then they should all be received in status, then alphabetic, order

  Scenario: Code terms retrieved in correct order when excluding inactive
    Given a Diabetes entity with out-of-order term codes
    When the entity term codes are retrieved excluding inactive
    Then 2 should be received in alphabetic, order
