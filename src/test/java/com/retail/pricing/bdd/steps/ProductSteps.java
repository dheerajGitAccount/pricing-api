package com.retail.pricing.bdd.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

public class ProductSteps {

    @LocalServerPort
    private int port;

    private Response response;
    private Long productId;

    @Before
    public void setup() {
        RestAssured.port = port;
        RestAssured.basePath = "/api";
    }

    @Given("the pricing API is running")
    public void thePricingApiIsRunning() {
        assertThat(port).isPositive();
    }

    @When("I create a product with name {string} and price {double}")
    public void iCreateAProduct(String name, double price) {
        String body = String.format("{\"name\":\"%s\",\"price\":%.2f}", name, price);
        response = given().contentType(ContentType.JSON).body(body)
                .post("/products");
        productId = response.jsonPath().getLong("id");
    }

    @Given("a product exists with name {string} and price {double}")
    public void aProductExists(String name, double price) {
        String body = String.format("{\"name\":\"%s\",\"price\":%.2f}", name, price);
        response = given().contentType(ContentType.JSON).body(body)
                .post("/products");
        productId = response.jsonPath().getLong("id");
    }

    @When("I get all products")
    public void iGetAllProducts() {
        response = given().get("/products");
    }

    @When("I update the product price to {double}")
    public void iUpdateProductPrice(double price) {
        response = given().queryParam("price", price)
                .put("/products/" + productId + "/price");
    }

    @Then("the response status is {int}")
    public void theResponseStatusIs(int status) {
        assertThat(response.statusCode()).isEqualTo(status);
    }

    @Then("the product name is {string}")
    public void theProductNameIs(String name) {
        assertThat(response.jsonPath().getString("name")).isEqualTo(name);
    }

    @Then("the product price is {double}")
    public void theProductPriceIs(double price) {
        assertThat(response.jsonPath().getDouble("price")).isEqualTo(price, offset(0.01));
    }

    @Then("the product list is not empty")
    public void theProductListIsNotEmpty() {
        assertThat(response.jsonPath().getList("$")).isNotEmpty();
    }

    public Long getProductId() {
        return productId;
    }
}
