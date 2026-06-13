package com.retail.pricing.bdd.steps;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

public class SalesSteps {

    private final ProductSteps productSteps;
    private Response response;

    public SalesSteps(ProductSteps productSteps) {
        this.productSteps = productSteps;
    }

    @When("I record a sale for the product with quantity {int} on date {string}")
    public void iRecordASale(int quantity, String date) {
        response = given()
                .queryParam("productId", productSteps.getProductId())
                .queryParam("quantity", quantity)
                .queryParam("saleDate", date)
                .post("/sales");
    }

    @When("I get daily sales for date {string}")
    public void iGetDailySales(String date) {
        response = given().queryParam("date", date).get("/sales/daily");
    }

    @When("I get the optimum price")
    public void iGetTheOptimumPrice() {
        response = given().get("/sales/optimum-price");
    }

    @Then("the sale quantity is {int}")
    public void theSaleQuantityIs(int quantity) {
        assertThat(response.jsonPath().getInt("quantity")).isEqualTo(quantity);
    }

    @Then("the sale total revenue is {double}")
    public void theSaleTotalRevenueIs(double revenue) {
        assertThat(response.jsonPath().getDouble("totalRevenue")).isEqualTo(revenue, offset(0.01));
    }

    @Then("the daily total quantity is {int}")
    public void theDailyTotalQuantityIs(int quantity) {
        assertThat(response.jsonPath().getInt("totalQuantity")).isEqualTo(quantity);
    }

    @Then("the daily total revenue is {double}")
    public void theDailyTotalRevenueIs(double revenue) {
        assertThat(response.jsonPath().getDouble("totalRevenue")).isEqualTo(revenue, offset(0.01));
    }

    @Then("the optimum product name is {string}")
    public void theOptimumProductNameIs(String name) {
        assertThat(response.jsonPath().getString("productName")).isEqualTo(name);
    }

    @Then("the optimum total revenue is {double}")
    public void theOptimumTotalRevenueIs(double revenue) {
        assertThat(response.jsonPath().getDouble("totalRevenue")).isEqualTo(revenue, offset(0.01));
    }
}
