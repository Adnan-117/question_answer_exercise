Feature: Question and Answers

  Scenario Outline: question asked not stored in the database
    Given the user asks a question <question>
    Then verify this question doesn't exist
    And the program prints the default answer
    Examples:
      | question                         |
      | "What is Peters favourite food?" |


  Scenario Outline: question asked with answers
    Given the user adds the question <question> with answers <answers>
    Then verify this question doesn't exist with answers
    And the question includes atleast one answer
    And the program stores the question into the database
    Examples:
      | question                         | answers                      |
      | "What is Peters favourite food?" | "Pasta ,  Rice , Chicken"    |
      | "What is Peters favourite play?" | "Cricket , Soccer"           |


  Scenario Outline: question asked stored in the database
    Given the user asks a question <question> already stored
    Then verify this question exists
    And the program must fetch the answers <answers>
    Examples:
      | question                         | answers                      |
      | "What is Peters favourite food?" | "Pasta ,  Rice , Chicken"    |
      | "What is Peters favourite play?" | "Cricket , Soccer"           |
