Feature: Product Management
  As a retail manager
  I want to manage products
  So that I can track product details and prices

  Scenario: Create a new product
    Given the pricing API is running
    When I create a product with name "Laptop" and price 999.99
    Then the response status is 200
    And the product name is "Laptop"
    And the product price is 999.99

  Scenario: Get all products
    Given the pricing API is running
    And a product exists with name "Phone" and price 499.99
    When I get all products
    Then the response status is 200
    And the product list is not empty

  Scenario: Update product price
    Given the pricing API is running
    And a product exists with name "Tablet" and price 299.99
    When I update the product price to 349.99
    Then the response status is 200
    And the product price is 349.99
