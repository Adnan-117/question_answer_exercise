Feature: Question

  Scenario: question asked not stored in the database
    Given the user asks a question "What is Peters favourite food?"
    Then verify this question doesn't exist
    And the program prints the default answer


  Scenario: question asked with answers
    Given the user adds the question "What is Peters favourite food?" with answers "Pasta , Rice"
    Then verify this question doesn't exist with answers
    And the program stores the question into the database


  Scenario: question asked stored in the database
    Given the user asks a question "What is Peters favourite food?" already stored
    Then verify this question exists
    And the program must fetch the answers "Pasta, Rice"