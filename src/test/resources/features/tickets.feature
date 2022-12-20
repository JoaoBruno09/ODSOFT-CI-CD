Feature: Helpdesk tickets

  Scenario: Access to personal tickets

    Given The url for the customer form
    When I get access to the customer form
    And I submit the form with the name 'Eula' and the adress '1395 Jigror Park'
    And I get access to the tickets page
    Then I should see only the tickets i submitted