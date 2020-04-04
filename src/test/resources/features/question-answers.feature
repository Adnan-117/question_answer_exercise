Feature: Question and Answers

  Scenario Outline: question asked not stored in the database
    Given the user asks a question <question>
    Then verify this question doesn't exist
    And the program prints the default answer
    Examples:
      | question                         |
      | "What is Peters favourite food?" |

  Scenario: user add a question with answers not stored in the database with question overflows the max characters
    Given the user adds question exceeding maximum characters
    Then the question is not stored in the database

  Scenario: user add a question with answers not stored in the database with answer overflows the max characters
    Given the user adds question with answers exceeding maximum characters
    Then the question with answer is not stored in the database


  Scenario Outline: user adds a question with answers not stored in the database
    Given the user adds the question <question> with answers <answers>
    Then verify this question doesn't exist with answers
    And the question includes atleast one answer
    And both question <question> and answers <answers> doesn't exceed the maximum character space of 255
    And the program stores the question into the database
    Examples:
      | question                         | answers                   |
      | "What is Peters favourite food?" | "Pasta ,  Rice , Chicken" |
      | "What is Peters favourite play?" | "Cricket , Soccer"        |


  Scenario Outline: question asked stored in the database
    Given the user asks a question <question> already stored
    Then verify this question exists
    And the program must fetch the answers <answers>
    Examples:
      | question                         | answers                   |
      | "What is Peters favourite food?" | "Pasta ,  Rice , Chicken" |
      | "What is Peters favourite play?" | "Cricket , Soccer"        |

