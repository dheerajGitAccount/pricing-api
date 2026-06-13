Feature: Sales Management
  As a retail manager
  I want to record and query sales
  So that I can track daily revenue and find the best performing product

  Scenario: Record a sale
    Given the pricing API is running
    And a product exists with name "Monitor" and price 199.99
    When I record a sale for the product with quantity 3 on date "2025-03-01"
    Then the response status is 200
    And the sale quantity is 3
    And the sale total revenue is 599.97

  Scenario: Get daily sales summary
    Given the pricing API is running
    And a product exists with name "Keyboard" and price 49.99
    And I record a sale for the product with quantity 2 on date "2025-03-05"
    When I get daily sales for date "2025-03-05"
    Then the response status is 200
    And the daily total quantity is 2
    And the daily total revenue is 99.98

  Scenario: Get optimum price product
    Given the pricing API is running
    And a product exists with name "Headphones" and price 79.99
    And I record a sale for the product with quantity 5 on date "2025-03-10"
    When I get the optimum price
    Then the response status is 200
    And the optimum product name is "Headphones"
    And the optimum total revenue is 399.95
