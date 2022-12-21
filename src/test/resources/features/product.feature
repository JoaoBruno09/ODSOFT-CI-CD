Feature: product category

  Scenario: Only administrator can add products 

    Given known credentials 'admin' 'userpass'
    When I login with username 'admin' and password 'userpass'
    And I get access to the mainpage
    And I click on product category menu
    Then we should see the product category page 


  Scenario: Normal user try to acess product category page 

    Given known credentials 'user' 'userpass'
    When I login with username 'user' and password 'userpass'
    And I get access to the mainpage
    And I can't see product category menu
    And I try to navigate via URL 
    Then I should see a page that I could not navigate 