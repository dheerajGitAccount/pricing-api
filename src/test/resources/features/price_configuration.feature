Feature: Price Configuration Management
  As a retail manager
  I want to configure product prices over date ranges
  So that I can manage time-based pricing

  Scenario: Add a new price configuration
    Given the pricing API is running
    And a product exists with name "Chair" and price 150.00
    When I add a price configuration with price 129.99 currency "GBP" from "2025-03-01" to "2025-03-10"
    Then the response status is 200
    And the config price is 129.99
    And the config currency is "GBP"
    And the config start date is "2025-03-01"
    And the config end date is "2025-03-10"

  Scenario: Update an existing price configuration with same date range
    Given the pricing API is running
    And a product exists with name "Desk" and price 250.00
    And I add a price configuration with price 199.99 currency "GBP" from "2025-04-01" to "2025-04-30"
    When I add a price configuration with price 179.99 currency "GBP" from "2025-04-01" to "2025-04-30"
    Then the response status is 200
    And the config price is 179.99

  Scenario: Overlapping date range trims existing configuration
    Given the pricing API is running
    And a product exists with name "Sofa" and price 500.00
    And I add a price configuration with price 499.99 currency "USD" from "2025-05-01" to "2025-05-31"
    When I add a price configuration with price 449.99 currency "USD" from "2025-05-10" to "2025-05-20"
    Then the response status is 200
    And the config price is 449.99
    And the config start date is "2025-05-10"
    And the config end date is "2025-05-20"
