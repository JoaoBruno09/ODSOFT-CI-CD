Feature: Supplier

  Scenario: Only administrator can add suppliers

    Given the known credentials 'admin' 'userpass'
    When I login with the following username 'admin' and the following password 'userpass'
    And I get access into the main page
    And I click on suppliers menu 
    Then I should see the suppliers page 

  Scenario: Supplier can supply multiple products categories

    Given the known credentials 'admin' 'userpass'
    When I login with the following username 'admin' and the following password 'userpass'
    And I get access into the main page
    And I click on suppliers menu
    And I click the add supplier button 
    And I on the products combo box
    Then I should see multiple product categories