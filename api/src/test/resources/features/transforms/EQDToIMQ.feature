Feature: EQDToIMQ

  Scenario Outline: EQD can be converted to IM Query and back
    When EDQ document <eqdIn> is passed into transformEqd
    Then the output should be <eqdOut>
    Examples:
      | eqdIn | eqdOut |
      | "a"   | "b"    |
